package cn.com.cnfic.contractmanage.contract.service.impl;

import cn.com.cnfic.contractmanage.common.service.ISerialNoService;
import cn.com.cnfic.contractmanage.contract.constant.ContractConstant;
import cn.com.cnfic.contractmanage.contract.entity.Agreement;
import cn.com.cnfic.contractmanage.contract.entity.UserAgreementFile;
import cn.com.cnfic.contractmanage.contract.enums.ErrorCodeEnum;
import cn.com.cnfic.contractmanage.contract.service.IAgreementService;
import cn.com.cnfic.contractmanage.contract.service.IUserAgreementFileService;
import cn.com.cnfic.contractmanage.contract.wrapper.UserAgreementWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.com.cnfic.contractmanage.contract.dto.UserAgreementDTO;
import cn.com.cnfic.contractmanage.contract.entity.UserAgreement;
import cn.com.cnfic.contractmanage.contract.mapper.UserAgreementMapper;
import cn.com.cnfic.contractmanage.contract.service.IUserAgreementService;
import cn.com.cnfic.contractmanage.contract.vo.UserAgreementVO;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 用户协议表 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-05
 */
@Service
public class UserAgreementServiceImpl extends BaseServiceImpl<UserAgreementMapper, UserAgreement> implements IUserAgreementService {

	@Autowired
	private IUserAgreementFileService userAgreementFileService;

	@Autowired
	private IAgreementService agreementService;

	@Autowired
	private ISerialNoService serialNoService;

	@Autowired
	private UserAgreementMapper userAgreementMapper;

	@Override
	public IPage<UserAgreementVO> selectUserAgreementPage(Query query, UserAgreementDTO userAgreement) {
		IPage<UserAgreementVO> page = Condition.getPage(query);
		List<UserAgreementVO> userAgreementVOS = userAgreementMapper.selectUserAgreementPage(page, userAgreement);
		userAgreementVOS.stream().forEach( e -> {
			if (Func.isNotEmpty(e.getAgreementEndTime())) {
				LocalDate agreementStartTime = e.getAgreementStartTime();
				LocalDate agreementEndTime = e.getAgreementEndTime();
				//与当前时间比较获取当前时间
				//有效开始时间比较
				//若有效开始未到当前时间则置为无效(不再判断此结束时间)
				boolean after = agreementStartTime.isAfter(LocalDate.now());
				if (after) {
					e.setAgreementState(ContractConstant.AGREEMENT_STATE_INVALID);
					Period between = Period.between(agreementEndTime, agreementStartTime);
					e.setTermOfMonth(between.getMonths());
					return;
				}
				//有效结束时间比较
				boolean before = agreementEndTime.isBefore(LocalDate.now());
				if (before) {
					e.setAgreementState(ContractConstant.AGREEMENT_STATE_INVALID);
				} else {
					e.setAgreementState(ContractConstant.AGREEMENT_STATE_EFFECTIVE);
				}
				//计算期限(协议结束时间-协议开始时间取整月)
				long until = agreementStartTime.until(agreementEndTime, ChronoUnit.MONTHS);
				e.setTermOfMonth(new Long(until).intValue());
			} else {
				e.setAgreementState(ContractConstant.AGREEMENT_STATE_EFFECTIVE);
			}
		});
		return page.setRecords(userAgreementVOS);

	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public R saveOrUpdateAndProtocolFile(UserAgreementDTO userAgreement) {
		QueryWrapper<UserAgreement> queryWrapper = Condition.getQueryWrapper(new UserAgreement());
		UserAgreement userAgreeFromDB = super.getById(userAgreement.getId());
		if (Func.isNotEmpty(userAgreeFromDB)) {
			//修改重名判断
			queryWrapper.eq("agreement_name", userAgreement.getAgreementName());
			queryWrapper.ne("id", userAgreement.getId());
			queryWrapper.eq("is_deleted", 0);
			List<UserAgreement> uptList = this.list(queryWrapper);
			if (Func.isEmpty(uptList)) {
				super.updateById(userAgreement);
			} else {
				return R.fail(ErrorCodeEnum.ERROR_FAILSAVEORUPDATE_NAMEREPEAT.getCode(),
						ErrorCodeEnum.ERROR_FAILSAVEORUPDATE_NAMEREPEAT.getMsg());
			}
		} else {
			//新增时填充协议编码
			userAgreement.setAgreementNo(serialNoService.getSerialNo("userAgreementNo", "XY", 4));
			//新增时需判断是否重名
			queryWrapper.eq("agreement_name", userAgreement.getAgreementName());
			queryWrapper.eq("is_deleted", 0);
			List<UserAgreement> list = this.list(queryWrapper);
			if (Func.isEmpty(list)) {
				super.save(userAgreement);
			} else {
				return R.fail(ErrorCodeEnum.ERROR_FAILSAVEORUPDATE_NAMEREPEAT.getCode(),
						ErrorCodeEnum.ERROR_FAILSAVEORUPDATE_NAMEREPEAT.getMsg());
			}
		}
		//1、删除子表文件
		userAgreementFileService.remove(new QueryWrapper<UserAgreementFile>().eq("agreement_id", userAgreement.getId()));
		//2、重新保存文件信息
		List<UserAgreementFile> protocolFile = userAgreement.getProtocolFile();
		protocolFile.stream().forEach(e -> {
			e.setAgreementId(userAgreement.getId());
			e.setCreateUser(userAgreement.getCreateUser());
			e.setCreateTime(DateUtil.now());
			e.setIsDeleted(0);
		});

		return Func.isNotEmpty(protocolFile) ?
				R.status(userAgreementFileService.saveBatch(protocolFile)) : R.status(true);
	}

	@Override
	public UserAgreementVO getUserAgreementDetail(UserAgreement userAgreement) {
		//1、查询用户协议详情
		UserAgreement one = getOne(Condition.getQueryWrapper(userAgreement));
		UserAgreementVO userAgreementVO = BeanUtil.copy(one, UserAgreementVO.class);
		//2、包装用户协议文件列表
		List<UserAgreementFile> fileList = userAgreementFileService.getUserAgreementFileLisstByAgreementId(userAgreement.getId());
		userAgreementVO.setProtocolFile(fileList);
		return userAgreementVO;
	}

	@Override
	public R remove(String ids) {
		//用户协议已关联合约，不能删除(con_agreement)
		QueryWrapper<Agreement> queryWrapper = Condition.getQueryWrapper(new Agreement());
		queryWrapper.select("id");
		queryWrapper.in("user_agreement_id", ids.split(","));
		List<Agreement> list = agreementService.list(queryWrapper);
		if (Func.isEmpty(list)) {
			return R.status(deleteLogic(Func.toLongList(ids)));
		} else {
			return R.fail(ErrorCodeEnum.ERROR_FAILDELETE_USED.getCode(),
					ErrorCodeEnum.ERROR_FAILDELETE_USED.getMsg());
		}
	}
}

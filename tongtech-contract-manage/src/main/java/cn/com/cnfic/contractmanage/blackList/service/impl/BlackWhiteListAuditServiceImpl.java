package cn.com.cnfic.contractmanage.blackList.service.impl;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.mapper.BlackWhiteListMapper;
import cn.com.cnfic.contractmanage.blackList.service.IBlackWhiteListAuditService;
import cn.com.cnfic.contractmanage.blackList.service.IBlackWhiteListService;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import cn.com.cnfic.contractmanage.blackList.wrapper.BlackWhiteListWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 黑白名单审核 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
@AllArgsConstructor
@Service
public class BlackWhiteListAuditServiceImpl extends ServiceImpl<BlackWhiteListMapper, BlackWhiteList> implements IBlackWhiteListAuditService {

    /**
     * 选择客户校验错误返回信息
     * {@value}
     */
    private static final String CUSTOMER_REPEAT_ERROR_MSG = "黑白名单已存在，无法修";
    /**
     * 黑白名单审核参数验证错误返回消息
     * {@value}
     */
    private static final String AUDIT_STATUS_ERROR_MSG = "黑白名单审核参数异常";
    /**
     * 已审核
     * {@value}
     */
    private static final String HAS_AUDIT = "已审核";
    /**
     * 待审核
     * {@value}
     */
    private static final String WAIT_AUDIT = "待审核";

    private final IBlackWhiteListService blackWhiteListService;

    @Override
    public R chooseCustomer(BlackWhiteList blackWhiteList) {
        //校验更改的数据是否已存在（校验规则：交易所-数据编码-名单类型-客户编码）
        LambdaQueryWrapper<BlackWhiteList> customerCodeWrapper = Wrappers.lambdaQuery(BlackWhiteList.class)
                .eq(BlackWhiteList::getExchange, blackWhiteList.getExchange())
                .eq(BlackWhiteList::getDataCode, blackWhiteList.getDataCode())
                .eq(BlackWhiteList::getCustCode, blackWhiteList.getCustCode());
        if (Objects.nonNull(baseMapper.selectOne(customerCodeWrapper))) {
            return R.fail(CUSTOMER_REPEAT_ERROR_MSG);
        }
        //更新客户编码
        LambdaUpdateWrapper<BlackWhiteList> auditWrapper = Wrappers.lambdaUpdate();
        auditWrapper.eq(BlackWhiteList::getId, blackWhiteList.getId());
        auditWrapper.set(BlackWhiteList::getCustCode, blackWhiteList.getCustCode())
                .set(BlackWhiteList::getUpdateTime, DateUtil.now())
                .set(BlackWhiteList::getUpdateUser, SecureUtil.getUserId());
        return R.status(update(auditWrapper));
    }

    @Override
    public R<IPage<BlackWhiteListVO>> page(BlackWhiteListVO blackWhiteListVO, Query query) {
        //客户名称、客户编码支持模糊查询
        String custName = blackWhiteListVO.getCustName(),
                custCode = blackWhiteListVO.getCustCode();
        blackWhiteListVO.setCustName(null);
        blackWhiteListVO.setCustCode(null);

        LambdaQueryWrapper<BlackWhiteList> queryWrapper = Wrappers.lambdaQuery(blackWhiteListVO);
        if (StringUtil.isNotBlank(custName)) {
            queryWrapper.like(BlackWhiteList::getCustName, custName);
        }
        if (StringUtil.isNotBlank(custCode)) {
            queryWrapper.like(BlackWhiteList::getCustCode, custCode);
        }
        if (StringUtil.equals(blackWhiteListVO.getAuditCheckTab(), HAS_AUDIT)) {
            queryWrapper.ne(BlackWhiteList::getAuditState, BlackWhiteListConstant.DEFAULT_AUDIT_VALUE);
        }
        if (StringUtil.equals(blackWhiteListVO.getAuditCheckTab(), WAIT_AUDIT)) {
            queryWrapper.eq(BlackWhiteList::getAuditState, BlackWhiteListConstant.DEFAULT_AUDIT_VALUE);
        }
        if (ObjectUtils.allNotNull(blackWhiteListVO.getStartTime(), blackWhiteListVO.getEndTime())) {
            queryWrapper.apply("date_format(create_time,'%Y-%m-%d') between {0} and {1}",
                    blackWhiteListVO.getStartTime(),
                    blackWhiteListVO.getEndTime());
        }
        //申请人姓名查询
        // TODO: 2021/3/12 根据申请人姓名，调用系统用户获取用户ID

        IPage<BlackWhiteListVO> page = BlackWhiteListWrapper.build().pageVO(page(Condition.getPage(query), queryWrapper));
        return R.data(page);
    }

    @Override
    public R audit(BlackWhiteListVO blackWhiteListVO) {
        if (StringUtils.isAnyBlank(blackWhiteListVO.getIds(), blackWhiteListVO.getAuditState())) {
            return R.fail(AUDIT_STATUS_ERROR_MSG);
        }
        if (!ArrayUtils.contains(BlackWhiteListConstant.AUDIT_STATUS, blackWhiteListVO.getAuditState())) {
            return R.fail(AUDIT_STATUS_ERROR_MSG);
        }
        LambdaUpdateWrapper<BlackWhiteList> auditWrapper = Wrappers.lambdaUpdate();
        auditWrapper.in(BlackWhiteList::getId, Func.toLongList(blackWhiteListVO.getIds()));
        BladeUser user = SecureUtil.getUser();
        auditWrapper.set(BlackWhiteList::getAuditState, blackWhiteListVO.getAuditState())
                .set(BlackWhiteList::getAuditResult, blackWhiteListVO.getAuditResult())
                .set(BlackWhiteList::getAuditTime, DateUtil.now())
                .set(BlackWhiteList::getAuditUser, user.getUserName())
                .set(BlackWhiteList::getUpdateTime, DateUtil.now())
                .set(BlackWhiteList::getUpdateUser, user.getUserId());
        return R.status(update(auditWrapper));
    }

    @Override
    public R<BlackWhiteListVO> getBlackWhiteList(BlackWhiteList blackWhiteList) {
        return blackWhiteListService.getBlackWhiteList(blackWhiteList);
    }
}

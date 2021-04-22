package cn.com.cnfic.contractmanage.contract.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.com.cnfic.contractmanage.contract.dto.UserAgreementDTO;
import cn.com.cnfic.contractmanage.contract.entity.UserAgreement;
import cn.com.cnfic.contractmanage.contract.vo.UserAgreementVO;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;

/**
 * 用户协议表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-05
 */
public interface IUserAgreementService extends BaseService<UserAgreement> {

	/**
	 * 自定义分页
	 *
	 * @param query
	 * @param userAgreement
	 * @return
	 */
	IPage<UserAgreementVO> selectUserAgreementPage(Query query, UserAgreementDTO userAgreement);

	/**
	 * 用户协议修改Or保存
	 *
	 * @param userAgreement
	 * @return
	 */
	R saveOrUpdateAndProtocolFile(UserAgreementDTO userAgreement);

	/**
	 * 获取用户协议详情
	 *
	 * @param userAgreement
	 * @return
	 */
	UserAgreementVO getUserAgreementDetail(UserAgreement userAgreement);

	/**
	 * 删除用户协议
	 *
	 * @param ids
	 * @return
	 */
	R remove(String ids);
}

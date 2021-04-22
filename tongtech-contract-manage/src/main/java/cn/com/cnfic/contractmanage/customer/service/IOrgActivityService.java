package cn.com.cnfic.contractmanage.customer.service;

import cn.com.cnfic.contractmanage.customer.entity.OrgActivity;
import cn.com.cnfic.contractmanage.customer.vo.OrgActivityVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 企业客户活动表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
public interface IOrgActivityService extends BaseService<OrgActivity> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgActivity
	 * @return
	 */
	IPage<OrgActivityVO> selectOrgActivityPage(IPage<OrgActivityVO> page, OrgActivityVO orgActivity);

	/**
	 * 通过客户Id 批量逻辑删除
	 * @param id
	 */
	void deleteLogicByCustomerId(Long id);

	/**
	 * 通过客户Id集合查询客户信息
	 * @param customerIds
	 * @return
	 */
    List<OrgActivityVO> findByCustomerIds(List<Long> customerIds);
}

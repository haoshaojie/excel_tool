package cn.com.cnfic.contractmanage.customer.mapper;

import cn.com.cnfic.contractmanage.customer.entity.OrgActivity;
import cn.com.cnfic.contractmanage.customer.vo.OrgActivityVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 企业客户活动表 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
public interface OrgActivityMapper extends BaseMapper<OrgActivity> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgActivity
	 * @return
	 */
	List<OrgActivityVO> selectOrgActivityPage(IPage page, OrgActivityVO orgActivity);

	/**
	 * 通过客户Id 批量逻辑删除
	 * @param id
	 */
    void deleteLogicByCustomerId(Long id);

	/**
	 * 通过客户Id集合查询客户信息
	 * @param customerIds 客户id集合
	 * @return
	 */
	List<OrgActivityVO> findByCustomerIds(List<Long> customerIds);


}

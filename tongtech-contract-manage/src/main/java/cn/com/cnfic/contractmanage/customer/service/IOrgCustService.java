package cn.com.cnfic.contractmanage.customer.service;

import cn.com.cnfic.contractmanage.customer.entity.OrgCust;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 客户管理行业三级分类 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-12
 */
public interface IOrgCustService extends BaseService<OrgCust> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgCust
	 * @return
	 */
	IPage<OrgCustVO> selectOrgCustPage(IPage<OrgCustVO> page, OrgCustVO orgCust);

	/**
	 * 查询行业树结构
	 * @param  custLevel  查询几级
	 * @return
	 */
	List<OrgCustVO> queryTree(Integer custLevel);
}

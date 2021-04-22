package cn.com.cnfic.contractmanage.customer.mapper;

import cn.com.cnfic.contractmanage.customer.entity.OrgCust;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 客户管理行业三级分类 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-12
 */
public interface OrgCustMapper extends BaseMapper<OrgCust> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgCust
	 * @return
	 */
	List<OrgCustVO> selectOrgCustPage(IPage page, OrgCustVO orgCust);

	/**
	 * 查询行业树结构
	 * @param  custLevel  查询几级
	 * @return
	 */
	List<OrgCustVO>  queryTree(Integer custLevel);
}

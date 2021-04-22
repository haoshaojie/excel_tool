package cn.com.cnfic.contractmanage.customer.mapper;

import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustomerVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 企业客户表 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-11
 */
public interface OrgCustomerMapper extends BaseMapper<OrgCustomer> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgCustomer
	 * @return
	 */
	List<OrgCustomerVO> selectOrgCustomerPage(IPage page, OrgCustomerVO orgCustomer);

	/**
	 * 通过Id 查询详情信息
	 * @param id 客户id
	 * @return
	 */
    OrgCustomerVO getDetailById(Long id);
}

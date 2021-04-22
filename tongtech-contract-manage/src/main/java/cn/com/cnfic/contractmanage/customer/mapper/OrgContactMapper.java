package cn.com.cnfic.contractmanage.customer.mapper;

import cn.com.cnfic.contractmanage.customer.entity.OrgContact;
import cn.com.cnfic.contractmanage.customer.vo.OrgContactVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 企业客户联系人表 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
public interface OrgContactMapper extends BaseMapper<OrgContact> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgContact
	 * @return
	 */
	List<OrgContactVO> selectOrgContactPage(IPage page, OrgContactVO orgContact);

	/**
	 * 通过客户Id 批量逻辑删除
	 * @param id
	 */
    void deleteLogicByCustomerId(Long id);

	/**
	 * 通过客户Id 集合查询联系人信息
	 * @param customerIds 客户Id 集合
	 * @return List<OrgContactVO>
	 */

	List<OrgContactVO> findByCustomerIds(List<Long> customerIds);
}

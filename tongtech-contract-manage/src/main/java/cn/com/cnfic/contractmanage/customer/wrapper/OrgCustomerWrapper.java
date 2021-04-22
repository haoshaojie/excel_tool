package cn.com.cnfic.contractmanage.customer.wrapper;

import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustomerVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 企业客户表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-11
 */
public class OrgCustomerWrapper extends BaseEntityWrapper<OrgCustomer, OrgCustomerVO>  {

    public static OrgCustomerWrapper build() {
        return new OrgCustomerWrapper();
    }

	@Override
	public OrgCustomerVO entityVO(OrgCustomer orgCustomer) {
		OrgCustomerVO orgCustomerVO = BeanUtil.copy(orgCustomer, OrgCustomerVO.class);

		return orgCustomerVO;
	}

}

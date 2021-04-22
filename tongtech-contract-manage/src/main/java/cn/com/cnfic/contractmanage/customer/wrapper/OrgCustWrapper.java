package cn.com.cnfic.contractmanage.customer.wrapper;

import cn.com.cnfic.contractmanage.customer.entity.OrgCust;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 客户管理行业三级分类包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-12
 */
public class OrgCustWrapper extends BaseEntityWrapper<OrgCust, OrgCustVO>  {

    public static OrgCustWrapper build() {
        return new OrgCustWrapper();
    }

	@Override
	public OrgCustVO entityVO(OrgCust orgCust) {
		OrgCustVO orgCustVO = BeanUtil.copy(orgCust, OrgCustVO.class);

		return orgCustVO;
	}

}

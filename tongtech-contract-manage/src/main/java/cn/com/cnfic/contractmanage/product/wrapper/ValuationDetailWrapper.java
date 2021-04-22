package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.entity.ValuationDetail;
import cn.com.cnfic.contractmanage.product.vo.ValuationDetailVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 产品计价配置明细表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
public class ValuationDetailWrapper extends BaseEntityWrapper<ValuationDetail, ValuationDetailVO>  {

    public static ValuationDetailWrapper build() {
        return new ValuationDetailWrapper();
    }

	@Override
	public ValuationDetailVO entityVO(ValuationDetail valuationDetail) {
		ValuationDetailVO valuationDetailVO = BeanUtil.copy(valuationDetail, ValuationDetailVO.class);

		return valuationDetailVO;
	}

}

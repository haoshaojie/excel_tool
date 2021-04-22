package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.Valuation;
import cn.com.cnfic.contractmanage.product.vo.ValuationVO;
import cn.com.cnfic.system.cache.DictCache;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

/**
 * 产品计价配置表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
public class ValuationWrapper extends BaseEntityWrapper<Valuation, ValuationVO>  {

    public static ValuationWrapper build() {
        return new ValuationWrapper();
    }

	@Override
	public ValuationVO entityVO(Valuation valuation) {
		ValuationVO valuationVO = BeanUtil.copy(valuation, ValuationVO.class);
		// 币种
		if(Func.isNotEmpty(valuation.getCurrency())){
			valuationVO.setCurrencyName(DictCache.getValue(ProductConstants.CURRENCY_TYPE_CODE, valuation.getCurrency()));
		}
		return valuationVO;
	}
}

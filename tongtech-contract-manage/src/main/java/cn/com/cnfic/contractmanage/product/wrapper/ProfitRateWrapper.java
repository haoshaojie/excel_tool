package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.entity.ProfitRate;
import cn.com.cnfic.contractmanage.product.vo.ProfitRateVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 产品分润比例表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
public class ProfitRateWrapper extends BaseEntityWrapper<ProfitRate, ProfitRateVO>  {

    public static ProfitRateWrapper build() {
        return new ProfitRateWrapper();
    }

	@Override
	public ProfitRateVO entityVO(ProfitRate profitRate) {
		ProfitRateVO profitRateVO = BeanUtil.copy(profitRate, ProfitRateVO.class);

		return profitRateVO;
	}

}

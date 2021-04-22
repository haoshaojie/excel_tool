package cn.com.cnfic.contractmanage.contract.wrapper;

import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import cn.com.cnfic.contractmanage.contract.entity.ConProduct;
import cn.com.cnfic.contractmanage.contract.vo.ConProductVO;

/**
 * @desc 合约产品表包装类,返回视图层所需的字段
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
public class ConProductWrapper extends BaseEntityWrapper<ConProduct, ConProductVO>  {

    public static ConProductWrapper build() {
        return new ConProductWrapper();
    }

	@Override
	public ConProductVO entityVO(ConProduct product) {
		ConProductVO productVO = BeanUtil.copy(product, ConProductVO.class);

		return productVO;
	}

}

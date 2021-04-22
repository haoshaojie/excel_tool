package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.entity.ProdItemRelation;
import cn.com.cnfic.contractmanage.product.vo.ProdItemRelationVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * @desc 产品与产品项关联表包装类,返回视图层所需的字段
 * @author Cnfic-UserManage
 * @date 2021-04-02
 */
public class ProdItemRelationWrapper extends BaseEntityWrapper<ProdItemRelation, ProdItemRelationVO>  {

    public static ProdItemRelationWrapper build() {
        return new ProdItemRelationWrapper();
    }

	@Override
	public ProdItemRelationVO entityVO(ProdItemRelation prodItemRelation) {
		ProdItemRelationVO prodItemRelationVO = BeanUtil.copy(prodItemRelation, ProdItemRelationVO.class);

		return prodItemRelationVO;
	}

}

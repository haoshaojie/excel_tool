package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.GroupDetail;
import cn.com.cnfic.contractmanage.product.vo.GroupDetailVO;
import cn.com.cnfic.system.cache.DictCache;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

/**
 * @desc 组合产品与产品/产品项关联表包装类,返回视图层所需的字段
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
public class GroupDetailWrapper extends BaseEntityWrapper<GroupDetail, GroupDetailVO>  {

    public static GroupDetailWrapper build() {
        return new GroupDetailWrapper();
    }

	@Override
	public GroupDetailVO entityVO(GroupDetail groupDetail) {
		GroupDetailVO groupDetailVO = BeanUtil.copy(groupDetail, GroupDetailVO.class);
		// 币种
		if(Func.isNotEmpty(groupDetail.getCurrency())){
			groupDetailVO.setCurrencyName(DictCache.getValue(ProductConstants.CURRENCY_TYPE_CODE, groupDetail.getCurrency()));
		}
		return groupDetailVO;
	}

}

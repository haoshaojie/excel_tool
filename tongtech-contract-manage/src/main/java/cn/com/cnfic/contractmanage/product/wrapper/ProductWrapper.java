package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.Product;
import cn.com.cnfic.contractmanage.product.vo.ProductVO;
import cn.com.cnfic.system.cache.DictCache;
import cn.com.cnfic.system.cache.SysCache;
import cn.com.cnfic.system.user.cache.UserCache;
import cn.com.cnfic.system.user.entity.UserInfo;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

/**
 * @desc 产品表包装类,返回视图层所需的字段
 * @author Cnfic-UserManage
 * @date 2021-03-24
 */
public class ProductWrapper extends BaseEntityWrapper<Product, ProductVO>  {

    public static ProductWrapper build() {
        return new ProductWrapper();
    }

	@Override
	public ProductVO entityVO(Product product) {
		ProductVO productVO = BeanUtil.copy(product, ProductVO.class);
		// 创建人
		if(Func.isNotEmpty(product.getCreateUser())){
			UserInfo createUser = UserCache.getUser(product.getCreateUser());
			productVO.setCreateUserName(createUser.getUser().getRealName());
		}
		// 修改人
		if(Func.isNotEmpty(product.getUpdateUser())){
			UserInfo updateUser = UserCache.getUser(product.getUpdateUser());
			productVO.setUpdateUserName(updateUser.getUser().getRealName());
		}
		//  产品类型
		if(Func.isNotEmpty(product.getProdType())){
			productVO.setProdTypeName(DictCache.getValue(ProductConstants.PRODUCT_TYPE_CODE,product.getProdType()));
		}
		//  发布范围
		if(Func.isNotEmpty(product.getReleaseScope())){
			productVO.setReleaseScopeName(DictCache.getValue(ProductConstants.PRODUCT_RANGE_CODE,product.getReleaseScope()));
		}
		//  产品来源
		if(Func.isNotEmpty(product.getItemSourse())){
			productVO.setItemSourseName(DictCache.getValue(ProductConstants.PRODUCT_SOURSE_CODE,product.getItemSourse()));
		}
		// 部门
		if(Func.isNotEmpty(product.getPropDept())){
			productVO.setProdDeptName(SysCache.getDeptName(product.getPropDept()));
		}
		// 状态
		if(Func.isNotEmpty(product.getProdState())){
			productVO.setProdStateName(DictCache.getValue(ProductConstants.PRODUCT_STATE_CODE,product.getProdState()));
		}
		return productVO;
	}

}

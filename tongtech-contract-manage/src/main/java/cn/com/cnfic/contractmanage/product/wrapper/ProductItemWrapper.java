package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.Category;
import cn.com.cnfic.contractmanage.product.entity.Product;
import cn.com.cnfic.contractmanage.product.entity.ProductItem;
import cn.com.cnfic.contractmanage.product.service.ICategoryService;
import cn.com.cnfic.contractmanage.product.service.IProductService;
import cn.com.cnfic.contractmanage.product.vo.ProductItemVO;
import cn.com.cnfic.system.cache.DictCache;
import cn.com.cnfic.system.cache.SysCache;
import cn.com.cnfic.system.user.cache.UserCache;
import cn.com.cnfic.system.user.entity.UserInfo;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 产品项表包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
public class ProductItemWrapper extends BaseEntityWrapper<ProductItem, ProductItemVO>  {

	private static IProductService productService;
	private static ICategoryService categoryService;

	static {
		productService = SpringUtil.getBean(IProductService.class);
		categoryService = SpringUtil.getBean(ICategoryService.class);
	}
    public static ProductItemWrapper build() {
        return new ProductItemWrapper();
    }

	@Override
	public ProductItemVO entityVO(ProductItem productItem) {
		ProductItemVO productItemVO = BeanUtil.copy(productItem, ProductItemVO.class);
		// 创建人
		if(Func.isNotEmpty(productItem.getCreateUser())){
			UserInfo createUser = UserCache.getUser(productItem.getCreateUser());
			productItemVO.setCreateUserName(createUser.getUser().getRealName());
		}
		// 修改人
		if(Func.isNotEmpty(productItem.getUpdateUser())){
			UserInfo updateUser = UserCache.getUser(productItem.getUpdateUser());
			productItemVO.setUpdateUserName(updateUser.getUser().getRealName());
		}
		//  发布范围
		if(Func.isNotEmpty(productItem.getReleaseScope())){
			productItemVO.setReleaseScopeName(DictCache.getValue(ProductConstants.PRODUCT_RANGE_CODE,productItem.getReleaseScope()));
		}
		//  产品来源
		if(Func.isNotEmpty(productItem.getItemSourse())){
			productItemVO.setItemSourseName(DictCache.getValue(ProductConstants.PRODUCT_SOURSE_CODE,productItem.getItemSourse()));
		}
		// 部门
		if(Func.isNotEmpty(productItem.getPropDept())){
			productItemVO.setPropDeptName(SysCache.getDeptName(productItem.getPropDept()));
		}
		// 产品名称
		if(Func.isNotEmpty(productItem.getProdId())){
			Product product = productService.getById(productItem.getProdId());
			if(Func.isNotEmpty(product)){
				productItemVO.setProdIdName(product.getProdName());
			}
		}
		// 产品类型
		if(Func.isNotEmpty(productItem.getCateId())){
			Category category = categoryService.getById(productItem.getCateId());
			if(Func.isNotEmpty(category)){
				productItemVO.setCateIdName(category.getCateName());
			}
		}
		// 状态
		if(Func.isNotEmpty(productItem.getItemState())){
			productItemVO.setItemStateName(DictCache.getValue(ProductConstants.PRODUCT_STATE_CODE,productItem.getItemState()));
		}
		return productItemVO;

	}

	public ProductItemVO entityDetailVO(ProductItem productItem) {
		ProductItemVO productItemVO = BeanUtil.copy(productItem, ProductItemVO.class);
		return productItemVO;
	}
}

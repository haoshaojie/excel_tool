package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.common.wrapper.CommonWrapper;
import cn.com.cnfic.contractmanage.product.entity.Category;
import cn.com.cnfic.contractmanage.product.vo.CategoryVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 产品类型表包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public class CategoryWrapper extends BaseEntityWrapper<Category, CategoryVO> {

    public static CategoryWrapper build() {
        return new CategoryWrapper();
    }

    @Override
    public CategoryVO entityVO(Category category) {
        CategoryVO categoryVO = BeanUtil.copy(category, CategoryVO.class);
        CommonWrapper.userIdConvertUserName(categoryVO);
        return categoryVO;
    }

}

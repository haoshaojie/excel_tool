package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.common.wrapper.CommonWrapper;
import cn.com.cnfic.contractmanage.product.entity.CategoryProperty;
import cn.com.cnfic.contractmanage.product.vo.CategoryPropertyVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 产品类型关联属性字典包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public class CategoryPropertyWrapper extends BaseEntityWrapper<CategoryProperty, CategoryPropertyVO> {

    public static CategoryPropertyWrapper build() {
        return new CategoryPropertyWrapper();
    }

    @Override
    public CategoryPropertyVO entityVO(CategoryProperty categoryProperty) {
        CategoryPropertyVO categoryPropertyVO = BeanUtil.copy(categoryProperty, CategoryPropertyVO.class);
        CommonWrapper.userIdConvertUserName(categoryProperty);
        return categoryPropertyVO;
    }

}

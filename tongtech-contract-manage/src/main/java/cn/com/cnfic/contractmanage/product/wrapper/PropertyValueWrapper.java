package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.common.wrapper.CommonWrapper;
import cn.com.cnfic.contractmanage.product.entity.PropertyValue;
import cn.com.cnfic.contractmanage.product.vo.PropertyValueVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 产品属性值表包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public class PropertyValueWrapper extends BaseEntityWrapper<PropertyValue, PropertyValueVO> {

    public static PropertyValueWrapper build() {
        return new PropertyValueWrapper();
    }

    @Override
    public PropertyValueVO entityVO(PropertyValue propertyValue) {
        PropertyValueVO propertyValueVO = BeanUtil.copy(propertyValue, PropertyValueVO.class);
        CommonWrapper.userIdConvertUserName(propertyValueVO);
        return propertyValueVO;
    }

}

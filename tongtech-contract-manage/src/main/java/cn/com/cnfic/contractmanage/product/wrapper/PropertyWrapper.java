package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.common.wrapper.CommonWrapper;
import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.Property;
import cn.com.cnfic.contractmanage.product.service.IPropertyValueService;
import cn.com.cnfic.contractmanage.product.vo.PropertyVO;
import cn.com.cnfic.system.feign.IDictClient;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.api.ResultCode;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 产品属性字典表包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public class PropertyWrapper extends BaseEntityWrapper<Property, PropertyVO> {

    public static PropertyWrapper build() {
        return new PropertyWrapper();
    }

    private static IDictClient dictClient;
    private static IPropertyValueService iPropertyValueService;
    static {
        dictClient = SpringUtil.getBean(IDictClient.class);
        iPropertyValueService = SpringUtil.getBean(IPropertyValueService.class);
    }

    @Override
    public PropertyVO entityVO(Property property) {
        PropertyVO propertyVO = BeanUtil.copy(property, PropertyVO.class);
        R<String> dict = dictClient.getValue(ProductConstants.SHOW_TYPE, Func.toInt(property.getShowType()));
        if (dict.getCode() == ResultCode.SUCCESS.getCode()) {
            propertyVO.setShowTypeName(dict.getData());
        }
        if (Func.toInt(property.getShowType()) == Func.toInt(ProductConstants.SHOW_SELECT_TYPE)) {
            propertyVO.setOptions(iPropertyValueService.queryListByProp(property.getId()));
        }
        CommonWrapper.userIdConvertUserName(propertyVO);
        return propertyVO;
    }

}

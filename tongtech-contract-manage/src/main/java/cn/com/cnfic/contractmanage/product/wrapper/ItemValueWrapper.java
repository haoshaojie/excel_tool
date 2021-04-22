package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.entity.ItemValue;
import cn.com.cnfic.contractmanage.product.service.IPropertyService;
import cn.com.cnfic.contractmanage.product.service.IPropertyValueService;
import cn.com.cnfic.contractmanage.product.vo.ItemValueVO;
import cn.com.cnfic.contractmanage.product.vo.PropertyVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 产品项扩展信息表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
public class ItemValueWrapper extends BaseEntityWrapper<ItemValue, ItemValueVO>  {

    public static ItemValueWrapper build() {
        return new ItemValueWrapper();
    }
	private static IPropertyValueService iPropertyValueService;
	private static IPropertyService iPropertyService;
	static {
		iPropertyService = SpringUtil.getBean(IPropertyService.class);
		iPropertyValueService = SpringUtil.getBean(IPropertyValueService.class);
	}
	@Override
	public ItemValueVO entityVO(ItemValue itemValue) {
		ItemValueVO itemValueVO = BeanUtil.copy(itemValue, ItemValueVO.class);
		if(Func.isNotEmpty(itemValue.getPropId())){
			PropertyVO prop = iPropertyService.getDetailById(itemValue.getPropId());
			if(Func.isNotEmpty(prop)){
				itemValueVO.setShowType(prop.getShowType());
			}
			itemValueVO.setOptions(iPropertyValueService.queryListByProp(itemValue.getPropId()));
		}
		return itemValueVO;
	}

}

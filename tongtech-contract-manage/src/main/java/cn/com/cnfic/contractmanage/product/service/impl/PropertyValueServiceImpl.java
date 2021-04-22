package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.entity.PropertyValue;
import cn.com.cnfic.contractmanage.product.mapper.PropertyValueMapper;
import cn.com.cnfic.contractmanage.product.service.IPropertyValueService;
import cn.com.cnfic.contractmanage.product.vo.PropertyValueVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品属性值表 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
@Service
public class PropertyValueServiceImpl extends ServiceImpl<PropertyValueMapper, PropertyValue> implements IPropertyValueService {

    @Override
    public IPage<PropertyValueVO> selectPropertyValuePage(IPage<PropertyValueVO> page, PropertyValueVO propertyValue) {
        return page.setRecords(baseMapper.selectPropertyValuePage(page, propertyValue));
    }

    @Override
    public List<PropertyValue> queryListByProp(Long propId) {
        QueryWrapper<PropertyValue> query = Condition.getQueryWrapper(new PropertyValue());
        query.eq("prop_id", propId);
        return this.list(query);
    }

}

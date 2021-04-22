package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.common.wrapper.CommonWrapper;
import cn.com.cnfic.contractmanage.product.dto.PropertyDTO;
import cn.com.cnfic.contractmanage.product.dto.PropertyValueDTO;
import cn.com.cnfic.contractmanage.product.entity.CategoryProperty;
import cn.com.cnfic.contractmanage.product.entity.Property;
import cn.com.cnfic.contractmanage.product.entity.PropertyValue;
import cn.com.cnfic.contractmanage.product.mapper.PropertyMapper;
import cn.com.cnfic.contractmanage.product.mapper.PropertyValueMapper;
import cn.com.cnfic.contractmanage.product.service.ICategoryPropertyService;
import cn.com.cnfic.contractmanage.product.service.IPropertyService;
import cn.com.cnfic.contractmanage.product.service.IPropertyValueService;
import cn.com.cnfic.contractmanage.product.vo.PropertyVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品属性字典表 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
@Service
@AllArgsConstructor
public class PropertyServiceImpl extends BaseServiceImpl<PropertyMapper, Property> implements IPropertyService {
    /**
     * '输入框
     */
    static int SHOW_TYPE_1 = 1;
    /**
     * 下拉框
     */
    static int SHOW_TYPE_2 = 2;
    @Autowired
    IPropertyValueService iPropertyValueService;
    @Autowired
    ICategoryPropertyService iCategoryPropertyService;
    @Autowired
    PropertyMapper propertyMapper;
    @Autowired
    PropertyValueMapper propertyValueMapper;

    @Override
    public IPage<PropertyVO> selectPropertyPage(Query query, PropertyDTO property) {
        IPage<PropertyVO> page = Condition.getPage(query);
        List<PropertyVO> propertyVOList = propertyMapper.selectPropertyPage(page, property);
        propertyVOList.stream().map(o -> {
            CommonWrapper.userIdConvertUserName(o);
            return o;
        }).collect(Collectors.toList());
        return page.setRecords(propertyVOList);
    }

    @Override
    public PropertyVO getDetailById(Long id) {
        PropertyVO propertyVO = propertyMapper.getDetailById(id);
        CommonWrapper.userIdConvertUserName(propertyVO);
        return propertyVO;
    }

    @Override
    public boolean save(PropertyDTO property) {
        if (!super.save(property)) {
            return false;
        }
        //如果是下拉框，还需保存下拉框的属性键值对
        if (property.getShowType() != null && property.getShowType() == SHOW_TYPE_2) {
            List<PropertyValueDTO> propertyValueDTOList = property.getPropertyValue();
            //DTO转化为entity--可写个公共方法，但是没有规划公共方法的位置
            if (propertyValueDTOList != null && propertyValueDTOList.size() > 0) {
                List<PropertyValue> propertyValueList = new ArrayList<PropertyValue>(propertyValueDTOList.size());
                propertyValueDTOList.forEach(propertyValueDTO -> {
                    PropertyValue propertyValue = BeanUtil.copy(propertyValueDTO, PropertyValue.class);
                    propertyValue.setPropId(property.getId());
                    BladeUser user = SecureUtil.getUser();
                    if (user != null) {
                        propertyValue.setCreateUser(user.getUserId());
                        propertyValue.setUpdateUser(user.getUserId());
                    }

                    Date now = DateUtil.now();
                    propertyValue.setCreateTime(now);
                    propertyValue.setUpdateTime(now);
                    propertyValue.setIsDeleted(0);
                    propertyValueList.add(propertyValue);
                });
                if (!iPropertyValueService.saveOrUpdateBatch(propertyValueList)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean updateById(PropertyDTO property) {
        if (!super.updateById(property)) {
            return false;
        }
        //如果是下拉框，还需保存下拉框的属性键值对
        if (property.getShowType() != null && property.getShowType() == SHOW_TYPE_2) {
            //删除现有的属性值
            List<PropertyValue> propertyValueDTOListExisting = propertyValueMapper.selectPropertyValueByPropId(property.getId());
            if (propertyValueDTOListExisting != null && propertyValueDTOListExisting.size() > 0) {
//                propertyValueDTOListExisting.forEach(propertyValue -> {
//                    propertyValue.setIsDeleted(1);
//                });
                if (!propertyValueMapper.updateIsDeleted(1, propertyValueDTOListExisting)) {
                    return false;
                }
            }
            //获取页面数据
            List<PropertyValueDTO> propertyValueDTOList = property.getPropertyValue();
            //DTO转化为entity--可写个公共方法，但是没有规划公共方法的位置
            if (propertyValueDTOList != null && propertyValueDTOList.size() > 0) {
                List<PropertyValue> propertyValueList = new ArrayList<PropertyValue>(propertyValueDTOList.size());
                propertyValueDTOList.forEach(propertyValueDTO -> {
                    PropertyValue propertyValue = BeanUtil.copy(propertyValueDTO, PropertyValue.class);
                    propertyValue.setPropId(property.getId());
                    BladeUser user = SecureUtil.getUser();
                    if (user != null) {
                        propertyValue.setCreateUser(user.getUserId());
                        propertyValue.setUpdateUser(user.getUserId());
                    }

                    Date now = DateUtil.now();
                    propertyValue.setCreateTime(now);
                    propertyValue.setUpdateTime(now);
                    propertyValue.setIsDeleted(0);
                    propertyValueList.add(propertyValue);
                });
                //恢复未删除的属性值
                propertyValueMapper.updateIsDeleted(0, propertyValueList);
                if (!iPropertyValueService.saveOrUpdateBatch(propertyValueList)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int selectPropertyByCodeOrName(PropertyDTO property) {
        return propertyMapper.selectPropertyByCodeOrName(property);
    }

    @Override
    public IPage<PropertyVO> selectPropertyPageInCategory(Query query, PropertyDTO property) {
        IPage<PropertyVO> page = Condition.getPage(query);
        List<PropertyVO> propertyVOList = propertyMapper.selectPropertyPageInCategory(page, property);
        propertyVOList.stream().map(o -> {
            CommonWrapper.userIdConvertUserName(o);
            return o;
        }).collect(Collectors.toList());
        return page.setRecords(propertyVOList);
    }

    @Override
    public List<PropertyVO> selectPropertyNotInCategory(PropertyDTO property) {
        List<PropertyVO> propertyVOList = propertyMapper.selectPropertyNotInCategory(property);
        propertyVOList.stream().map(o -> {
            CommonWrapper.userIdConvertUserName(o);
            return o;
        }).collect(Collectors.toList());
        return propertyVOList;
    }

    @Override
    public List<Property> getListByCate(PropertyDTO property) {
        QueryWrapper<CategoryProperty> cpQuery= Condition.getQueryWrapper(new CategoryProperty());
        if(Func.isNotEmpty(property.getCateId())){
            cpQuery.select("prop_id").eq("cate_id", property.getCateId());
        }
        if(Func.isNoneBlank(property.getExclude())){
            cpQuery.notIn("prop_id", Func.toLongArray(property.getExclude()));
        }
        if (Func.isNoneBlank(property.getInclude())) {
            cpQuery.in("prop_id", Func.toLongArray(property.getInclude()));
        }
        List<CategoryProperty> cplist = iCategoryPropertyService.list(cpQuery);
        List<Property> plist = new ArrayList<>();
        if (Func.isNotEmpty(cplist)) {
            QueryWrapper<Property> pquery = Condition.getQueryWrapper(new Property());
            pquery.in("id", cplist.stream().map(item -> item.getPropId()).collect(Collectors.toList()));
            pquery.eq("prop_type", property.getPropType());
            plist = this.list(pquery);
        }
        return plist;
    }

    public boolean getOne(Property property, List<PropertyValue> propertyValueList) {
        if (property.getShowType() != null && property.getShowType() == SHOW_TYPE_2) {
            if (!iPropertyValueService.saveOrUpdateBatch(propertyValueList)) {
                return false;
            }
        }
        if (!super.save(property)) {
            return false;
        }
        return true;
    }
}

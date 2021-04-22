package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.entity.CategoryProperty;
import cn.com.cnfic.contractmanage.product.mapper.CategoryPropertyMapper;
import cn.com.cnfic.contractmanage.product.service.ICategoryPropertyService;
import cn.com.cnfic.contractmanage.product.vo.CategoryPropertyVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品类型关联属性字典 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
@Service
public class CategoryPropertyServiceImpl extends BaseServiceImpl<CategoryPropertyMapper, CategoryProperty> implements ICategoryPropertyService {
    @Autowired
    CategoryPropertyMapper categoryPropertyMapper;

    @Override
    public IPage<CategoryPropertyVO> selectCategoryPropertyPage(IPage<CategoryPropertyVO> page, CategoryPropertyVO categoryProperty) {
        return page.setRecords(baseMapper.selectCategoryPropertyPage(page, categoryProperty));
    }

    @Override
    public Boolean saveOrUpdate(List<Long> propIds, Long cateId) {
        if (propIds == null || propIds.size() < 1) {
            return true;
        }
        List<CategoryProperty> categoryPropertyList = new ArrayList<CategoryProperty>(propIds.size());
        propIds.forEach(propId -> {
            CategoryProperty categoryProperty = new CategoryProperty();
            categoryProperty.setCateId(cateId);
            categoryProperty.setPropId(propId);
            List<CategoryPropertyVO> categoryPropertyListExist = categoryPropertyMapper.selectCategoryProperty(categoryProperty);
            if (categoryPropertyListExist == null || categoryPropertyListExist.size() < 1) {
                BladeUser user = SecureUtil.getUser();
                if (user != null) {
                    categoryProperty.setCreateUser(user.getUserId());
                    categoryProperty.setUpdateUser(user.getUserId());
                }

                Date now = DateUtil.now();
                categoryProperty.setCreateTime(now);
                categoryProperty.setUpdateTime(now);
                categoryProperty.setIsDeleted(0);
                categoryPropertyList.add(categoryProperty);
            }
            ;
        });
        this.saveOrUpdateBatch(categoryPropertyList);
        return true;
    }

}

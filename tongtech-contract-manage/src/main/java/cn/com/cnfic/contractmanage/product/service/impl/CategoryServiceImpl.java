package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.dto.CategoryDTO;
import cn.com.cnfic.contractmanage.product.entity.Category;
import cn.com.cnfic.contractmanage.product.mapper.CategoryMapper;
import cn.com.cnfic.contractmanage.product.service.ICategoryService;
import cn.com.cnfic.system.user.feign.IUserClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品类型表 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    /**
     * 用户信息feign 客户端
     */
    private static IUserClient iUserClient;

    @Override
    public IPage<Category> selectCategoryPage(Query query, CategoryDTO category) {
        IPage<Category> page = Condition.getPage(query);
        List<Category> propertyList = categoryMapper.selectCategoryPage(page, category);
        return page.setRecords(propertyList);
    }

    @Override
    public Integer selectCategoryByCodeOrName(CategoryDTO category) {
        return categoryMapper.selectCategoryByCodeOrName(category);
    }

}

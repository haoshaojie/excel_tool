package cn.com.cnfic.contractmanage.product.service;

import cn.com.cnfic.contractmanage.product.dto.CategoryDTO;
import cn.com.cnfic.contractmanage.product.entity.Category;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;

/**
 * 产品类型表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public interface ICategoryService extends BaseService<Category> {

    /**
     * 自定义分页
     *
     * @param query
     * @param category
     * @return
     */
    IPage<Category> selectCategoryPage(Query query, CategoryDTO category);

    /**
     * 查询名称和编码是否重复，大于0就是重复
     *
     * @param category
     * @return
     */
    Integer selectCategoryByCodeOrName(CategoryDTO category);
}

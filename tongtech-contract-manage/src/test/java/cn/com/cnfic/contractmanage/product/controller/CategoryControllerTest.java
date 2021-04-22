package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.common.constant.TongTechConstant;
import cn.com.cnfic.contractmanage.ContractManageApplication;
import cn.com.cnfic.contractmanage.product.dto.CategoryDTO;
import cn.com.cnfic.contractmanage.product.vo.CategoryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringExtension;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @Auther: wxy
 * @Date: 2021/04/01/14:29
 * @Description: 产品类型测试类
 */
@ExtendWith(BladeSpringExtension.class)
@SpringBootTest(classes = ContractManageApplication.class)
@BladeBootTest(appName = TongTechConstant.APPLICATION_CONTRACT_MANAGE_NAME, profile = "dev", enableLoader = true)
class CategoryControllerTest {
    @Autowired
    private CategoryController categoryController;
    /**
     * 对象参数
     */
    CategoryDTO categoryDTO = new CategoryDTO();
    /**
     * 分页返回结果
     */
    R<IPage<CategoryVO>> pageDate;
    /**
     * 未删除标志
     */
    Integer deleteFlag = 0;

    @Test
    void save() {
        categoryDTO.setCateName("类型名称");
        categoryDTO.setCateCode("类型编码");
        categoryDTO.setRemark("备注");
        categoryDTO.setIsDeleted(deleteFlag);
        R r = categoryController.save(categoryDTO);
        assertEquals(r.getCode(), 200);
    }

    @Test
    void detail() {
        Long id = categoryDTO.getId();
        if (id != null) {
            assertNotNull(categoryController.detail(categoryDTO));
        }
    }

    @Test
    void page() {
        Query query = new Query();
        query.setCurrent(1);
        query.setSize(5);
        pageDate = categoryController.page(categoryDTO, query);
    }

    @Test
    void update() {
        categoryDTO.setCateName("类型名称加测");
        categoryController.update(categoryDTO);
    }

    @Test
    void remove() {
        Long id = categoryDTO.getId();
        if (id != null) {
            categoryController.remove(id.toString());
        }
    }

    @Test
    void selectItems() {
        categoryController.selectItems();
    }
}
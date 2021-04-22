package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.common.constant.TongTechConstant;
import cn.com.cnfic.contractmanage.ContractManageApplication;
import cn.com.cnfic.contractmanage.product.dto.PropertyDTO;
import cn.com.cnfic.contractmanage.product.entity.CategoryProperty;
import cn.com.cnfic.contractmanage.product.vo.PropertyVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringExtension;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Auther: wxy
 * @Date: 2021/04/01/14:30
 * @Description: 产品类型关联属性测试类
 */
@ExtendWith(BladeSpringExtension.class)
@SpringBootTest(classes = ContractManageApplication.class)
@BladeBootTest(appName = TongTechConstant.APPLICATION_CONTRACT_MANAGE_NAME, profile = "dev", enableLoader = true)
class CategoryPropertyControllerTest {
    @Autowired
    private CategoryPropertyController categoryPropertyController;
    /**
     * 对象参数
     */
    PropertyDTO propertyDTO = new PropertyDTO();
    /**
     * 对象参数
     */
    CategoryProperty categoryProperty = new CategoryProperty();
    /**
     * 分页返回结果
     */
    R<IPage<PropertyVO>> pageDate;
    /**
     * 返回结果单体对象
     */
    PropertyVO propertyVO = null;
    /**
     * 未删除标志
     */
    Integer deleteFlag = 0;

    /**
     * 类型关联属性id
     */
    Long catePropId;
    /**
     * 类型id
     */
    Long cateId;
    /**
     * 属性id
     */
    Long propId;

    @Test
    void propertyPageInCategory() {
        Query query = new Query();
        query.setCurrent(1);
        query.setSize(5);
        pageDate = categoryPropertyController.propertyPageInCategory(propertyDTO, query);
        if (pageDate != null && pageDate.getData() != null && pageDate.getData().getRecords() != null && pageDate.getData().getRecords().size() > 0) {
            propertyVO = pageDate.getData().getRecords().get(0);
            catePropId = propertyVO.getCatePropId();
        }
    }

    @Test
    void detail() {
        if (catePropId != null) {
            categoryProperty.setId(catePropId);
            cateId = categoryPropertyController.detail(categoryProperty).getData().getCateId();
        }
    }


    @Test
    void propertyNotInCategory() {
        if (cateId != null) {
            propertyDTO.setCateId(cateId);
            R<List<PropertyVO>> list = categoryPropertyController.propertyNotInCategory(propertyDTO);
            if (list.getData() != null && list.getData().size() > 0) {
                propId = list.getData().get(0).getId();
            }
        }
    }

    @Test
    void submit() {
        if (cateId != null && propId != null) {
            categoryPropertyController.submit(propId.toString(), cateId.toString());
        }

    }

    @Test
    void remove() {
        if (catePropId != null) {
            categoryPropertyController.remove(catePropId.toString());
        }
    }
}
package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.common.constant.TongTechConstant;
import cn.com.cnfic.contractmanage.ContractManageApplication;
import cn.com.cnfic.contractmanage.product.dto.PropertyDTO;
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

/**
 * @Auther: wxy
 * @Date: 2021/04/01/14:30
 * @Description: 产品属性测试类
 */
@ExtendWith(BladeSpringExtension.class)
@SpringBootTest(classes = ContractManageApplication.class)
@BladeBootTest(appName = TongTechConstant.APPLICATION_CONTRACT_MANAGE_NAME, profile = "dev", enableLoader = true)
class PropertyControllerTest {
    @Autowired
    private PropertyController propertyController;
    /**
     * 对象参数
     */
    PropertyDTO propertyDTO = new PropertyDTO();
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

    @Test
    void save() {
        propertyDTO.setPropCode("测试01");
        propertyDTO.setPropName("测试02");
        propertyDTO.setPropType(1);
        propertyDTO.setIsRequired(1);
        propertyDTO.setIsDeleted(deleteFlag);
        propertyDTO.setPropType(1);
        propertyController.save(propertyDTO);
    }

    @Test
    void detail() {
        Long id = propertyDTO.getId();
        if (id != null) {
            propertyController.detail(id);
        }
    }

    @Test
    void page() {
        Query query = new Query();
        query.setCurrent(1);
        query.setSize(5);
        pageDate = propertyController.page(propertyDTO, query);
        if (pageDate != null && pageDate.getData() != null && pageDate.getData().getRecords() != null) {
            propertyVO = pageDate.getData().getRecords().get(0);
        }
    }


    @Test
    void update() {
        propertyDTO.setPropName("测试02侧");
        propertyController.update(propertyDTO);
    }

    @Test
    void remove() {
        Long id = propertyDTO.getId();
        if (id != null) {
            propertyController.remove(id.toString());
        }
    }

    @Test
    void getListByCate() {
        propertyController.getListByCate(propertyDTO);
    }
}
package cn.com.cnfic.contractmanage.order.service.impl;

import cn.com.cnfic.common.constant.TongTechConstant;
import cn.com.cnfic.contractmanage.ContractManageApplication;
import cn.com.cnfic.contractmanage.order.dto.UserOrderGoodsDTO;
import cn.com.cnfic.contractmanage.order.entity.UserOrder;
import cn.com.cnfic.contractmanage.order.entity.UserOrderGoods;
import cn.com.cnfic.contractmanage.order.service.IUserOrderGoodsService;
import cn.com.cnfic.contractmanage.order.service.IUserOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther: wxy
 * @Date: 2021/04/14/20:25
 * @Description:
 */
@ExtendWith(BladeSpringExtension.class)
@SpringBootTest(classes = ContractManageApplication.class)
@BladeBootTest(appName = TongTechConstant.APPLICATION_CONTRACT_MANAGE_NAME, profile = "dev", enableLoader = true)
class UserOrderGoodsServiceImplTest {
    @Autowired
    private IUserOrderGoodsService userOrderGoodsService;
    @Autowired
    private IUserOrderService userOrderService;


    @BeforeEach
    void setUp() {
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderNo("999");
        userOrder.setPhoneNumber("13675670867");
        userOrder.setMailbox("123@22.com");
        userOrderService.save(userOrder);
        for (int i = 0; i < 10; i++) {
            UserOrderGoods userOrderGoods = new UserOrderGoods();
            userOrderGoods.setOrderNo("999");
            userOrderGoods.setGoodsNo("NO" + i);
            userOrderGoods.setGoodsPrice(new BigDecimal(i));
            userOrderGoodsService.save(userOrderGoods);
        }
    }

    @Test
    void selectUserOrderGoodsPage() {
        UserOrderGoodsDTO userOrderGoodsDTO = new UserOrderGoodsDTO();
        userOrderGoodsDTO.setPhoneNumber("13675670867");
        IPage<UserOrderGoods> page = new Page<>();
        userOrderGoodsService.selectUserOrderGoodsPage(page, userOrderGoodsDTO);
    }

    @Test
    void selectUserOrderGoods() {
        List<UserOrderGoodsDTO> userOrderGoodsDTOList = new ArrayList<UserOrderGoodsDTO>();
        for (int i = 0; i < 10; i++) {
            UserOrderGoodsDTO userOrderGoodsDTO = new UserOrderGoodsDTO();
            userOrderGoodsDTO.setPhoneNumber("1367567086" + i);
            userOrderGoodsDTOList.add(userOrderGoodsDTO);
        }

        userOrderGoodsService.selectUserOrderGoods(userOrderGoodsDTOList);
    }

    @Test
    void selectUserOrderExcel() {
    }
}
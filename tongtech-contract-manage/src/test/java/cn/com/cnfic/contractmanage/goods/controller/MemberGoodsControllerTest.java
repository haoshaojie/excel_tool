package cn.com.cnfic.contractmanage.goods.controller;

import cn.com.cnfic.common.constant.TongTechConstant;
import cn.com.cnfic.contractmanage.ContractManageApplication;
import cn.com.cnfic.contractmanage.goods.dto.MemberGoodsDTO;
import cn.com.cnfic.contractmanage.goods.entity.MemberGoods;
import cn.com.cnfic.contractmanage.goods.service.IMemberGoodsService;
import cn.com.cnfic.contractmanage.goods.vo.MemberGoodsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringExtension;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Auther: wxy
 * @Date: 2021/04/07/9:22
 * @Description:
 */
@ExtendWith(BladeSpringExtension.class)
@SpringBootTest(classes = ContractManageApplication.class)
@BladeBootTest(appName = TongTechConstant.APPLICATION_CONTRACT_MANAGE_NAME, profile = "dev", enableLoader = true)
class MemberGoodsControllerTest {
    @Autowired
    private MemberGoodsController memberGoodsController;
    @Autowired
    private IMemberGoodsService iMemberGoodsService;
    /**
     * 初始化实体
     */
    MemberGoodsDTO memberGoodsDTO = new MemberGoodsDTO();
    /**
     * 分页返回结果
     */
    R<IPage<MemberGoodsVO>> pageDate;
    /**
     * 单个返回结果
     */
    MemberGoodsVO memberGoodsVO = null;

    @BeforeEach
    void setUp() {

        MemberGoods memberGoods = new MemberGoods();
        memberGoods.setGoodsTitle("测试标题");
        iMemberGoodsService.save(memberGoods);
        memberGoodsDTO = BeanUtil.copy(memberGoods, MemberGoodsDTO.class);
    }

    @Test
    void page() {
        Query query = new Query();
        query.setCurrent(1);
        query.setSize(5);
        pageDate = memberGoodsController.page(memberGoodsDTO, query);
    }

    @Test
    void exportData() {
        memberGoodsController.exportData(memberGoodsDTO);
    }

    @Test
    void remove() {
        memberGoodsController.remove(memberGoodsDTO.getId().toString());
    }
}
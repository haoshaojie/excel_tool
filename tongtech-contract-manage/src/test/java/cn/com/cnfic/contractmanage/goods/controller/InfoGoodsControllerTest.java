package cn.com.cnfic.contractmanage.goods.controller;

import cn.com.cnfic.common.constant.TongTechConstant;
import cn.com.cnfic.contractmanage.ContractManageApplication;
import cn.com.cnfic.contractmanage.contract.constant.ContractConstant;
import cn.com.cnfic.contractmanage.goods.dto.InfoGoodsDTO;
import cn.com.cnfic.contractmanage.goods.vo.InfoGoodsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringExtension;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(BladeSpringExtension.class)
@SpringBootTest(classes = ContractManageApplication.class)
@BladeBootTest(appName = TongTechConstant.APPLICATION_CONTRACT_MANAGE_NAME, profile = "dev", enableLoader = true)
class InfoGoodsControllerTest {
    @Autowired
    private InfoGoodsController infoGoodsController;
    /**
     * 初始化实体
     */
    InfoGoodsDTO infoGoods = new InfoGoodsDTO();
    /**
     * 分页返回结果
     */
    R<IPage<InfoGoodsVO>> pageDate;
    /**
     * 单个返回结果
     */
    InfoGoodsVO infoGoodsVO = null;

    @Test
    void page() {
        Query query = new Query();
        query.setCurrent(1);
        query.setSize(5);
        pageDate = infoGoodsController.page(infoGoods, query);
        if (pageDate.getData() != null && pageDate.getData().getRecords() != null) {
            infoGoodsVO = pageDate.getData().getRecords().get(0);
        }

    }

    @Test
    void detail() {
        if (infoGoodsVO == null) {
            infoGoods.setId(infoGoodsVO.getId());
            infoGoods.setType(infoGoodsVO.getType());
            System.out.print(infoGoodsController.detail(infoGoods));
        }
    }

    @Test
    void update() {
        String disposeSuggest = "测试意见";
        if (ContractConstant.TYPE_INFO.equals(infoGoodsVO.getType())) {
            infoGoodsController.update(infoGoodsVO.getId().toString(), "", disposeSuggest);
        } else {
            infoGoodsController.update("", infoGoodsVO.getId().toString(), disposeSuggest);
        }

    }

    @Test
    void remove() {
        if (ContractConstant.TYPE_INFO.equals(infoGoodsVO.getType())) {
            infoGoodsController.remove(infoGoodsVO.getId().toString(), "");
        } else {
            infoGoodsController.remove("", infoGoodsVO.getId().toString());
        }
    }
}
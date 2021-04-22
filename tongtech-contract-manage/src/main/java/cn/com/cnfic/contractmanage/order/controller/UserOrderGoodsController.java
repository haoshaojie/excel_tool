package cn.com.cnfic.contractmanage.order.controller;

import cn.com.cnfic.contractmanage.order.dto.UserOrderGoodsDTO;
import cn.com.cnfic.contractmanage.order.entity.UserOrderGoods;
import cn.com.cnfic.contractmanage.order.service.IUserOrderGoodsService;
import cn.com.cnfic.contractmanage.order.vo.UserOrderGoodsVO;
import cn.com.cnfic.contractmanage.order.wrapper.UserOrderGoodsWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单商品表 控制器
 * @date 2021-04-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/userordergoods")
@Api(value = "用户订单商品表", tags = "用户订单商品表接口")
public class UserOrderGoodsController extends BladeController {

    private IUserOrderGoodsService userOrderGoodsService;

    /**
     * 用户订单商品表
     */
    @PostMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "导出数据", notes = "传入userOrderGoods")
    public R<List<UserOrderGoodsVO>> list(@RequestBody UserOrderGoodsDTO[] userOrderGoodsList) {
        List<UserOrderGoodsDTO> paramList = new ArrayList<>(userOrderGoodsList.length);
        Collections.addAll(paramList, userOrderGoodsList);
        List<UserOrderGoodsVO> list = userOrderGoodsService.selectUserOrderGoods(paramList);
        return R.data(list);
    }

    /**
     * 自定义分页 用户订单商品表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入userOrderGoods")
    public R<IPage<UserOrderGoodsVO>> page(UserOrderGoodsDTO userOrderGoods, Query query) {
        IPage<UserOrderGoods> pages = userOrderGoodsService.selectUserOrderGoodsPage(Condition.getPage(query), userOrderGoods);
        return R.data(UserOrderGoodsWrapper.build().pageVO(pages));
    }

}

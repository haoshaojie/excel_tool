package cn.com.cnfic.contractmanage.order.controller;

import cn.com.cnfic.contractmanage.order.dto.UserOrderDTO;
import cn.com.cnfic.contractmanage.order.service.IUserOrderGoodsService;
import cn.com.cnfic.contractmanage.order.service.IUserOrderService;
import cn.com.cnfic.contractmanage.order.vo.UserOrderGoodsVO;
import cn.com.cnfic.contractmanage.order.vo.UserOrderVO;
import cn.com.cnfic.contractmanage.order.wrapper.UserOrderWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单表 控制器
 * @date 2021-04-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/order")
@Api(value = "用户订单表", tags = "用户订单表接口")
public class UserOrderController extends BladeController {
    /**
     * 用户订单service
     */
    private IUserOrderService userOrderService;
    /**
     * 订单商品service
     */
    private IUserOrderGoodsService userOrderGoodsService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入userOrder")
    public R<UserOrderVO> detail(Long id) {
        UserOrderVO detail = userOrderService.getDetail(id);
        return R.data(UserOrderWrapper.build().entityVO(detail));
    }

    /**
     * 自定义分页 用户订单表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入userOrder")
    public R<IPage<UserOrderVO>> page(UserOrderDTO userOrder, Query query) {
        IPage<UserOrderVO> pages = userOrderService.selectUserOrderPage(Condition.getPage(query), userOrder);
        return R.data(pages);
    }


    /**
     * 导出用户订单表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "导出数据", notes = "传入userOrder")
    public R<List<UserOrderGoodsVO>> list(UserOrderDTO userOrder) {
        List<UserOrderGoodsVO> list = userOrderGoodsService.selectUserOrderExcel(userOrder);
        return R.data(list);
    }


}

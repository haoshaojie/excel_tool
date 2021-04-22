package cn.com.cnfic.contractmanage.partner.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.com.cnfic.contractmanage.partner.entity.OrderProfit;
import cn.com.cnfic.contractmanage.partner.vo.OrderProfitVO;
import cn.com.cnfic.contractmanage.partner.wrapper.OrderProfitWrapper;
import cn.com.cnfic.contractmanage.partner.service.IOrderProfitService;
import org.springblade.core.boot.ctrl.BladeController;
import java.util.List;

/**
 * 订单分润信息表 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/orderprofit")
@Api(value = "订单分润信息表", tags = "订单分润信息表接口")
public class OrderProfitController extends BladeController {

	private IOrderProfitService orderProfitService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入orderProfit")
	public R<OrderProfitVO> detail(OrderProfit orderProfit) {
		OrderProfit detail = orderProfitService.getOne(Condition.getQueryWrapper(orderProfit));
		return R.data(OrderProfitWrapper.build().entityVO(detail));
	}

	/**
	* 分页 订单分润信息表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入orderProfit")
	public R<IPage<OrderProfitVO>> list(OrderProfit orderProfit, Query query) {
		IPage<OrderProfit> pages = orderProfitService.page(Condition.getPage(query), Condition.getQueryWrapper(orderProfit));
		return R.data(OrderProfitWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 订单分润信息表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orderProfit")
	public R<IPage<OrderProfitVO>> page(OrderProfitVO orderProfit, Query query) {
		IPage<OrderProfitVO> pages = orderProfitService.selectOrderProfitPage(Condition.getPage(query), orderProfit);
		return R.data(pages);
	}

	/**
	* 新增 订单分润信息表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入orderProfit")
	public R save(@Valid @RequestBody OrderProfit orderProfit) {
		return R.status(orderProfitService.save(orderProfit));
	}

	/**
	* 修改 订单分润信息表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入orderProfit")
	public R update(@Valid @RequestBody OrderProfit orderProfit) {
		return R.status(orderProfitService.updateById(orderProfit));
	}

	/**
	* 新增或修改 订单分润信息表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入orderProfit")
	public R submit(@Valid @RequestBody OrderProfit orderProfit) {
		return R.status(orderProfitService.saveOrUpdate(orderProfit));
	}

	
	/**
	* 删除 订单分润信息表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(orderProfitService.deleteLogic(Func.toLongList(ids)));
	}

	
}

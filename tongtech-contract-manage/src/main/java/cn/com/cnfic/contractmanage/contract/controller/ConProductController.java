package cn.com.cnfic.contractmanage.contract.controller;

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
import cn.com.cnfic.contractmanage.contract.entity.ConProduct;
import cn.com.cnfic.contractmanage.contract.vo.ConProductVO;
import cn.com.cnfic.contractmanage.contract.wrapper.ConProductWrapper;
import cn.com.cnfic.contractmanage.contract.service.IConProductService;
import org.springblade.core.boot.ctrl.BladeController;
import java.util.List;

/**
 * @desc 合约产品表 控制器
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/conproduct")
@Api(value = "合约产品表", tags = "合约产品表接口")
public class ConProductController extends BladeController {

	private IConProductService productService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入product")
	public R<ConProductVO> detail(ConProduct product) {
		ConProduct detail = productService.getOne(Condition.getQueryWrapper(product));
		return R.data(ConProductWrapper.build().entityVO(detail));
	}

	/**
	* 分页 合约产品表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入product")
	public R<IPage<ConProductVO>> list(ConProduct product, Query query) {
		IPage<ConProduct> pages = productService.page(Condition.getPage(query), Condition.getQueryWrapper(product));
		return R.data(ConProductWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 合约产品表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入product")
	public R<IPage<ConProductVO>> page(ConProductVO product, Query query) {
		IPage<ConProductVO> pages = productService.selectProductPage(Condition.getPage(query), product);
		return R.data(pages);
	}

	/**
	* 新增 合约产品表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入product")
	public R save(@Valid @RequestBody ConProduct product) {
		return R.status(productService.save(product));
	}

	/**
	* 修改 合约产品表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入product")
	public R update(@Valid @RequestBody ConProduct product) {
		return R.status(productService.updateById(product));
	}

	/**
	* 新增或修改 合约产品表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入product")
	public R submit(@Valid @RequestBody ConProduct product) {
		return R.status(productService.saveOrUpdate(product));
	}

	
	/**
	* 删除 合约产品表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(productService.deleteLogic(Func.toLongList(ids)));
	}

	
}

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
import cn.com.cnfic.contractmanage.contract.entity.ConUserProd;
import cn.com.cnfic.contractmanage.contract.vo.ConUserProdVO;
import cn.com.cnfic.contractmanage.contract.wrapper.ConUserProdWrapper;
import cn.com.cnfic.contractmanage.contract.service.IConUserProdService;
import org.springblade.core.boot.ctrl.BladeController;
import java.util.List;

/**
 * @desc 合约用户产品权限表 控制器
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/userprod")
@Api(value = "合约用户产品权限表", tags = "合约用户产品权限表接口")
public class ConUserProdController extends BladeController {

	private IConUserProdService userProdService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入userProd")
	public R<ConUserProdVO> detail(ConUserProd userProd) {
		ConUserProd detail = userProdService.getOne(Condition.getQueryWrapper(userProd));
		return R.data(ConUserProdWrapper.build().entityVO(detail));
	}

	/**
	* 分页 合约用户产品权限表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入userProd")
	public R<IPage<ConUserProdVO>> list(ConUserProd userProd, Query query) {
		IPage<ConUserProd> pages = userProdService.page(Condition.getPage(query), Condition.getQueryWrapper(userProd));
		return R.data(ConUserProdWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 合约用户产品权限表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入userProd")
	public R<IPage<ConUserProdVO>> page(ConUserProdVO userProd, Query query) {
		IPage<ConUserProdVO> pages = userProdService.selectUserProdPage(Condition.getPage(query), userProd);
		return R.data(pages);
	}

	/**
	* 新增 合约用户产品权限表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入userProd")
	public R save(@Valid @RequestBody ConUserProd userProd) {
		return R.status(userProdService.save(userProd));
	}

	/**
	* 修改 合约用户产品权限表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入userProd")
	public R update(@Valid @RequestBody ConUserProd userProd) {
		return R.status(userProdService.updateById(userProd));
	}

	/**
	* 新增或修改 合约用户产品权限表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入userProd")
	public R submit(@Valid @RequestBody ConUserProd userProd) {
		return R.status(userProdService.saveOrUpdate(userProd));
	}

	
	/**
	* 删除 合约用户产品权限表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(userProdService.deleteLogic(Func.toLongList(ids)));
	}

	
}

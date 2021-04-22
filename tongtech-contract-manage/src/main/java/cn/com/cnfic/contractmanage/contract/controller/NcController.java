package cn.com.cnfic.contractmanage.contract.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import cn.com.cnfic.contractmanage.contract.dto.NcDTO;
import cn.com.cnfic.contractmanage.contract.entity.Nc;
import cn.com.cnfic.contractmanage.contract.service.INcService;
import cn.com.cnfic.contractmanage.contract.vo.NcVO;
import cn.com.cnfic.contractmanage.contract.wrapper.NcWrapper;
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
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 用友合同表? 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/nc")
@Api(value = "用友合同表?", tags = "用友合同表?接口")
public class NcController extends BladeController {

	private INcService ncService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入nc")
	public R<NcVO> detail(Nc nc) {
		return R.data(ncService.getDetail(nc.getId()));
	}

	/**
	* 分页 用友合同表?
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入nc")
	public R<IPage<NcVO>> list(Nc nc, Query query) {
		IPage<Nc> pages = ncService.page(Condition.getPage(query), Condition.getQueryWrapper(nc));
		return R.data(NcWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 用友合同表?
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入nc")
	public R<IPage<NcVO>> page(NcDTO nc, Query query) {
		IPage<NcVO> pages = ncService.selectNcPage(query, nc);
		return R.data(pages);
	}

	/**
	* 新增 用友合同表?
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入nc")
	public R save(@Valid @RequestBody Nc nc) {
		return R.status(ncService.save(nc));
	}

	/**
	* 修改 用友合同表?
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入nc")
	public R update(@Valid @RequestBody Nc nc) {
		return R.status(ncService.updateById(nc));
	}

	/**
	* 新增或修改 用友合同表?
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入nc")
	public R submit(@Valid @RequestBody Nc nc) {
		return R.status(ncService.saveOrUpdate(nc));
	}

	
	/**
	* 删除 用友合同表?
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(ncService.deleteLogic(Func.toLongList(ids)));
	}

	
}

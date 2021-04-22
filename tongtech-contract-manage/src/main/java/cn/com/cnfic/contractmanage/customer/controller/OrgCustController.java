package cn.com.cnfic.contractmanage.customer.controller;

import cn.com.cnfic.contractmanage.customer.entity.OrgCust;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustService;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustVO;
import cn.com.cnfic.contractmanage.customer.wrapper.OrgCustWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @desc 客户管理行业三级分类Api
 * @auther yangchuan
 * @date 2021/3/26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/orgcust")
@Api(value = "客户管理行业三级分类Api", tags = "客户管理行业三级分类接口")
public class OrgCustController extends BladeController {

	private IOrgCustService orgCustService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入orgCust")
	public R<OrgCustVO> detail(OrgCust orgCust) {
		OrgCust detail = orgCustService.getOne(Condition.getQueryWrapper(orgCust));
		return R.data(OrgCustWrapper.build().entityVO(detail));
	}

	/**
	* 分页 客户管理行业三级分类
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入orgCust")
	public R<IPage<OrgCustVO>> list(OrgCust orgCust, Query query) {
		IPage<OrgCust> pages = orgCustService.page(Condition.getPage(query), Condition.getQueryWrapper(orgCust));
		return R.data(OrgCustWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 客户管理行业三级分类
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orgCust")
	public R<IPage<OrgCustVO>> page(OrgCustVO orgCust, Query query) {
		IPage<OrgCustVO> pages = orgCustService.selectOrgCustPage(Condition.getPage(query), orgCust);
		return R.data(pages);
	}

	/**
	* 新增 客户管理行业三级分类
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入orgCust")
	public R save(@Valid @RequestBody OrgCust orgCust) {
		return R.status(orgCustService.save(orgCust));
	}

	/**
	* 修改 客户管理行业三级分类
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入orgCust")
	public R update(@Valid @RequestBody OrgCust orgCust) {
		return R.status(orgCustService.updateById(orgCust));
	}

	/**
	* 新增或修改 客户管理行业三级分类
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入orgCust")
	public R submit(@Valid @RequestBody OrgCust orgCust) {
		return R.status(orgCustService.saveOrUpdate(orgCust));
	}

	
	/**
	* 删除 客户管理行业三级分类
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(orgCustService.deleteLogic(Func.toLongList(ids)));
	}

	/**
	 *  懒加载客户管理行业三级分类
	 */
	@PostMapping("/queryALl")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "查询下级全部行业", notes = "传入ids")
	public R queryALl(@ApiParam(value = "主键集合", required = false) @RequestParam(required = false) String code) {
		QueryWrapper<OrgCust> queryWrapper=new QueryWrapper();
		if (StringUtils.isBlank(code)){
			queryWrapper.eq("parent_code",0);
		}else {
			queryWrapper.eq("parent_code",code);
		}
		queryWrapper.eq("is_deleted",0);
		return R.data(orgCustService.list(queryWrapper));
	}

	/**
	 *查询客户管理行业三级树
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "查询行业树")
	public R<List<OrgCustVO>> queryTree(@RequestParam(required = false) Integer custLevel) {
		return R.data(orgCustService.queryTree(custLevel));
	}

}

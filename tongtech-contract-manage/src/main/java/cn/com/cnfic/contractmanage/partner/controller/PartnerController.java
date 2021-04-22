package cn.com.cnfic.contractmanage.partner.controller;

import cn.com.cnfic.contractmanage.partner.entity.Partner;
import cn.com.cnfic.contractmanage.partner.service.IPartnerService;
import cn.com.cnfic.contractmanage.partner.vo.OrderProfitTotalVO;
import cn.com.cnfic.contractmanage.partner.vo.PartnerProductVO;
import cn.com.cnfic.contractmanage.partner.vo.PartnerVO;
import cn.com.cnfic.contractmanage.partner.wrapper.PartnerWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 合作商表 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/partner")
@Api(value = "合作商表", tags = "合作商表接口")
public class PartnerController extends BladeController {

	private IPartnerService partnerService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入partner")
	public R<PartnerVO> detail(Partner partner) {
		Partner detail = partnerService.getOne(Condition.getQueryWrapper(partner));
		return R.data(PartnerWrapper.build().entityVO(detail));
	}

	/**
	* 分页 合作商表（未使用）
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入partner")
	public R<IPage<PartnerVO>> list(Partner partner, Query query) {

		QueryWrapper<Partner> queryWrapper = Condition.getQueryWrapper(partner);
		queryWrapper.clear();
		if(StringUtils.isNotBlank(partner.getParCode())) {
			queryWrapper.like("par_code", partner.getParCode());
		}
		if(StringUtils.isNotBlank(partner.getParName())) {
			queryWrapper.like("par_name", partner.getParName());
		}

		IPage<Partner> pages = partnerService.page(Condition.getPage(query), queryWrapper);
		return R.data(PartnerWrapper.build().pageVO(pages));
	}

	/**
	 * 分页 合作商产品/产品项列表
	 */
	@GetMapping("/partnerProductList")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入partnerProduct")
	public R<IPage<PartnerProductVO>> partnerProductList(PartnerProductVO partnerProduct, Query query) {
		IPage<PartnerProductVO> pages = partnerService.selectPartnerProductPage(Condition.getPage(query), partnerProduct);
		return R.data(pages);
	}

	/**
	 * 分页 合作商分润明细统计
	 */
	@GetMapping("/partnerOrderProfitTotal")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orderProfitTotal")
	public R<IPage<OrderProfitTotalVO>> partnerOrderProfitTotal(OrderProfitTotalVO orderProfitTotal, Query query) {
		IPage<OrderProfitTotalVO> pages = partnerService.selectOrderProfitTotalPage(Condition.getPage(query), orderProfitTotal);
		return R.data(pages);
	}

	/**
	* 自定义分页 合作商表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入partner")
	public R<IPage<PartnerVO>> page(PartnerVO partner, Query query) {
		IPage<PartnerVO> pages = partnerService.selectPartnerPage(Condition.getPage(query), partner);
		return R.data(pages);
	}

	/**
	* 新增 合作商表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入partner")
	public R save(@Valid @RequestBody Partner partner) {
		return R.status(partnerService.save(partner));
	}

	/**
	* 修改 合作商表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入partner")
	public R update(@Valid @RequestBody Partner partner) {
		return R.status(partnerService.updateById(partner));
	}

	/**
	* 新增或修改 合作商表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入partner")
	public R submit(@Valid @RequestBody Partner partner) {
		return R.status(partnerService.saveOrUpdate(partner));
	}

	
	/**
	* 删除 合作商表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		// 判断供应商是否关联的有产品数据
		PartnerProductVO partnerProduct = new PartnerProductVO();
		partnerProduct.setPartnerIds(Func.toLongList(ids));
		IPage<PartnerProductVO> partnerProductPages = partnerService.selectPartnerProductPage(Condition.getPage(new Query()),partnerProduct);
		if(partnerProductPages.getTotal() > 0) {
			return R.fail(-1, "删除失败，合作商已关联产品或产品项！");
		}

		return R.status(partnerService.deleteLogic(Func.toLongList(ids)));
	}

	@PostMapping("/selectItems")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "下拉框列表", notes = "下拉框列表")
	public R<List<Partner>> selectItems(){
		List<Partner> categorys= partnerService.list();
		return R.data(categorys);
	}
}

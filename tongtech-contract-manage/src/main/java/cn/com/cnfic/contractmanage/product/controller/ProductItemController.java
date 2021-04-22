package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.dto.ProductItemDTO;
import cn.com.cnfic.contractmanage.product.dto.ValuationDTO;
import cn.com.cnfic.contractmanage.product.entity.*;
import cn.com.cnfic.contractmanage.product.excel.ExcelUtil;
import cn.com.cnfic.contractmanage.product.service.*;
import cn.com.cnfic.contractmanage.product.vo.*;
import cn.com.cnfic.contractmanage.product.wrapper.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 产品项表 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/productitem")
@Api(value = "产品项表", tags = "产品项表接口")
public class ProductItemController extends BladeController {

	private IProductItemService productItemService;
	private IItemValueService iItemValueService;
	private IImagesService iImagesService;
	private IProfitRateService iProfitRateService;
	private IValuationService iValuationService;
	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入productItem")
	public R<ProductItemVO> detail(ProductItem productItem) {
		ProductItem detail = productItemService.getOne(Condition.getQueryWrapper(productItem));
		return R.data(ProductItemWrapper.build().entityDetailVO(detail));
	}

	/**
	* 分页 产品项表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入productItem")
	public R<IPage<ProductItemVO>> list(ProductItemDTO productItem, Query query) {
		IPage<ProductItem> pages =  productItemService.selectProductItemPage(query, productItem);
		return R.data(ProductItemWrapper.build().pageVO(pages));
	}

	/**
	* 修改 产品项表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入productItem")
	public R update(@Valid @RequestBody ProductItemDTO productItem) {
		return productItemService.updateItem(productItem);
	}

	/**
	 *
	 * @param ids
	 * @param itemState
	 * @return
	 */
	@PostMapping("/updateItemState")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "产品项上下架", notes = "传入ids,itemState")
	public R updateItemState(@ApiParam(value = "主键集合", required = true) @RequestParam String ids,
							 @ApiParam(value = "产品状态", required = true) @RequestParam Integer itemState){
		return productItemService.updateItemState(ids, itemState);
	}
	/**
	* 新增或修改 产品项表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增", notes = "传入productItem")
	public R submit(@Valid @RequestBody ProductItemDTO productItem) {
		return productItemService.saveItem(productItem);
	}

	
	/**
	* 删除 产品项表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return productItemService.removeByIds(ids);
	}
	/**
	 * 校验产品项编码
	 * @return
	 */
	@PostMapping("/checkPropCode")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "校验产品项编码", notes = "校验产品项编码")
	public R<Boolean> checkPropCode(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "propCode", required = true) String propCode){
		return productItemService.checkPropCode(id, propCode);
	}

	/**
	 * 导出数据
	 */
	@GetMapping("exportData")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出数据", notes = "传入ProductItemDTO")
	public void exportData(ProductItemDTO productItem, HttpServletResponse response) {
		Map<String,List<List<String>>> excelMap = productItemService.exportData(productItem);
		try {
			ExcelUtil.export(response, ProductConstants.PRODUCT_ITEM_FILE_NAME + DateUtil.format(new Date(), "yyyyMMddHHmmss"), ProductConstants.PRODUCT_ITEM_SHEET_NAME, excelMap.get("head"), excelMap.get("data"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取扩展属性
	 * @param prodId
	 * @return
	 */
	@GetMapping("itemValueByProdId")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取扩展属性", notes = "传入prodId")
	public R<List<ItemValueVO>> getItemValueByProdId(Long prodId){
		List<ItemValue> item = iItemValueService.getListByProdId(prodId);
		return R.data(ItemValueWrapper.build().listVO(item));
	}
	/**
	 * 获取图片列表
	 * @param prodId
	 * @return
	 */
	@GetMapping("imagesByProdId")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取产品项图片", notes = "传入prodId")
	public R<List<ImagesVO>> getImagesByProdId(@RequestParam("prodId") Long prodId, @RequestParam("type") Integer type){
		List<Images> item = iImagesService.getListByProdId(prodId, type);
		return R.data(ImagesWrapper.build().listVO(item));
	}

	/**
	 * 获取分润比例
	 * @param prodId
	 * @return
	 */
	@GetMapping("profitRateByProdId")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取分润比例", notes = "传入prodId")
	public R<List<ProfitRateVO>> getProfitRateByProdId(@RequestParam("prodId") Long prodId, @RequestParam("type") Integer type){
		List<ProfitRate> item = iProfitRateService.getListByProdId(prodId, type);
		return R.data(ProfitRateWrapper.build().listVO(item));
	}

	/**
	 * 获取计价属性
	 * @param value
	 * @return
	 */
	@GetMapping("getValuations")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取计价属性", notes = "传入prodId")
	public R<List<ValuationVO>> getValuations(ValuationDTO value){
		List<Valuation> item = iValuationService.getValuations(value);
		return R.data(ValuationWrapper.build().listVO(item));
	}

	/**
	 * 获取已上架产品列表
	 * @return
	 */
	@PostMapping("/getAddedList")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取已上架产品列表")
	public R<List<ProductItemVO>> getAddedList(ProductItemDTO productItem){
		return R.data(ProductItemWrapper.build().listVO(productItemService.getAddedList(productItem)));
	}

	/**
	 *
	 */
}

package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.dto.ProductDTO;
import cn.com.cnfic.contractmanage.product.entity.Product;
import cn.com.cnfic.contractmanage.product.excel.ExcelUtil;
import cn.com.cnfic.contractmanage.product.service.IProductService;
import cn.com.cnfic.contractmanage.product.vo.ProductVO;
import cn.com.cnfic.contractmanage.product.wrapper.ProductWrapper;
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
 * @desc 产品表 控制器
 * @author Cnfic-UserManage
 * @date 2021-03-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/product")
@Api(value = "产品表", tags = "产品表接口")
public class ProductController extends BladeController {

	private IProductService productService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入product")
	public R<ProductVO> detail(Product product) {
		Product detail = productService.getOne(Condition.getQueryWrapper(product));
		return R.data(ProductWrapper.build().entityVO(detail));
	}

	/**
	* 分页 产品表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入product")
	public R<IPage<ProductVO>> list(ProductDTO product, Query query) {
		IPage<Product> pages = productService.selectProductPage(query, product);
		return R.data(ProductWrapper.build().pageVO(pages));
	}

	/**
	 * 修改状态
	 * @param ids
	 * @param state
	 * @return
	 */
	@PostMapping("/updateState")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "产品上下架", notes = "传入ids,state")
	public R updateState(@ApiParam(value = "主键集合", required = true) @RequestParam String ids,
							 @ApiParam(value = "产品状态", required = true) @RequestParam Integer state){
		return productService.updateProductState(ids, state);
	}
	/**
	* 修改 产品表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入product")
	public R update(@Valid @RequestBody ProductDTO product) {
		return productService.updateProd(product);
	}

	/**
	* 新增或修改 产品表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入product")
	public R submit(@Valid @RequestBody ProductDTO product) {

		return productService.saveProd(product);
	}
	/**
	 * 导出数据
	 */
	@GetMapping("exportData")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出数据", notes = "传入ProductItemDTO")
	public void exportData(ProductDTO product, HttpServletResponse response) {
		Map<String,List<List<String>>> excelMap = productService.exportData(product);
		try {
			ExcelUtil.export(response, ProductConstants.PRODUCT_FILE_NAME + DateUtil.format(new Date(), "yyyyMMddHHmmss"), ProductConstants.PRODUCT_SHEET_NAME, excelMap.get("head"), excelMap.get("data"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* 删除 产品表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return productService.removeProductByIds(ids);
	}

	/**
	 * 校验产品编码
	 * @return
	 */
	@PostMapping("/validProdCode")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "校验产品编码", notes = "校验产品编码")
	public R validProdCode(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "code", required = true) String code){
		return productService.validProdCode(id,code);
	}

	/**
	 * 获取已上架产品列表
	 * @return
	 */
	@PostMapping("/getAddedList")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取已上架产品列表")
	public R<List<ProductVO>> getAddedList(ProductDTO product){
		return R.data(ProductWrapper.build().listVO(productService.getAddedList(product)));
	}

}

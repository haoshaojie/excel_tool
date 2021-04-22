package cn.com.cnfic.contractmanage.customer.controller;

import cn.com.cnfic.contractmanage.customer.dto.OrgCustomerDTO;
import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustomerService;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustomerVO;
import cn.com.cnfic.contractmanage.customer.wrapper.OrgCustomerWrapper;
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
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @desc 企业客户表Api
 * @auther yangchuan
 * @date 2021/3/26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/orgcustomer")
@Api(value = "企业客户表", tags = "企业客户表接口")
public class OrgCustomerController extends BladeController {

	private IOrgCustomerService orgCustomerService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入orgCustomer")
	public R<OrgCustomerVO> detail(OrgCustomer orgCustomer) {
		OrgCustomerVO detail = orgCustomerService.getOneDtail(orgCustomer);

		return R.data(detail);
	}

	/**
	* 分页 企业客户表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入orgCustomer")
	public R<IPage<OrgCustomerVO>> list(OrgCustomer orgCustomer, Query query) {
		IPage<OrgCustomer> pages = orgCustomerService.page(Condition.getPage(query), Condition.getQueryWrapper(orgCustomer));
		return R.data(OrgCustomerWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 企业客户表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orgCustomer")
	public R<IPage<OrgCustomerVO>> page(OrgCustomerVO orgCustomer, Query query) {
		IPage<OrgCustomerVO> pages = orgCustomerService.selectOrgCustomerPage(Condition.getPage(query), orgCustomer);
		return R.data(pages);
	}

	/**
	* 新增 企业客户表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入orgCustomer")
	public R save(@Valid @RequestBody OrgCustomerDTO orgCustomer) {
		return R.status(orgCustomerService.saveCustomer(orgCustomer));
	}

	/**
	* 修改 企业客户表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入orgCustomer")
	public R update(@Valid @RequestBody OrgCustomer orgCustomer) {
		return R.status(orgCustomerService.updateById(orgCustomer));
	}

	/**
	* 新增或修改 企业客户表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入orgCustomer")
	public R submit(@Valid @RequestBody OrgCustomerDTO orgCustomer) {
		return R.status(orgCustomerService.saveCustomer(orgCustomer));
	}

	
	/**
	* 删除 企业客户表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return orgCustomerService.deleteByCustomerId(ids);
	}

	/**
	 *	导出客户信息数据
//	 * @param orgCustomer
	 * @param response
	 */
	@GetMapping("/export")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "客户管理导出")
	public void exportBlackWhiteList(HttpServletResponse response, OrgCustomerVO orgCustomerVO) {
		orgCustomerService.exportOrgCustomerList(response, orgCustomerVO);
	}


	/**
	 *
	 * @param response
	 * @param key
	 */
	@GetMapping("/downloadCustomerTemplate")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "下载客户信息模板")
	public void downloadCustomerTemplate(HttpServletResponse response,@RequestParam(required = false) String key) {
		try {
			orgCustomerService.downloadCustomerTemplate(response,key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@PostMapping("/importCustomerFile")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "导入客户管理文件")
	public R importBlackWhiteList(MultipartFile file) {
		return orgCustomerService.imporCustomerFile(file);
	}

	@GetMapping("/getImportData")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "获取导入客户数据")
	public R getImportData(@RequestParam(value = "redisKey") String redisKey, @RequestParam(value = "isSuccess")Boolean isSuccess, Query query) {
		return orgCustomerService.getImportData(redisKey,isSuccess,query);
	}
	@GetMapping("/submitpassdata")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "提交客户信息数据")
	public R submitpassdata(@RequestParam(value = "redisKey") String redisKey) {
		return orgCustomerService.submitpassdata(redisKey);
	}
	@GetMapping("/verifCustName")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "验证用户名称重复")
	public R verifCustName(@RequestParam(value = "custName") String custName ) {
		return orgCustomerService.verifCustName(custName, "");
	}

	/**
	 * 分页 企业客户表
	 */
	@GetMapping("/customList")
	@ApiOperationSupport(order = 15)
	@ApiOperation(value = "自定义查询列表", notes = "传入orgCustomer")
	public R<List<OrgCustomer>> list(OrgCustomer orgCustomer) {
		List<OrgCustomer> result = orgCustomerService.customList(orgCustomer);
		return R.data(result);
	}

	@GetMapping("/companySearch")
	@ApiOperationSupport(order = 16)
	@ApiOperation(value = "新华信用验证查询", notes = "传入orgCustomer")
	public R companySearch(String name,String pageNo,String pageSize) {
		Object result = orgCustomerService.companySearch(name,pageNo,pageSize);
		return R.data(result);
	}
}

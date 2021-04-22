package cn.com.cnfic.contractmanage.user.controller;

import cn.com.cnfic.contractmanage.customer.vo.OrgCustomerVO;
import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.service.IOrgUserService;
import cn.com.cnfic.contractmanage.user.vo.OrgUserVO;
import cn.com.cnfic.contractmanage.user.wrapper.OrgUserWrapper;
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

/**
 * @desc 企业用户表 控制器
 * @author Cnfic-UserManage
 * @date 2021-03-29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/orguser")
@Api(value = "企业用户表", tags = "企业用户表接口")
public class OrgUserController extends BladeController {

	private IOrgUserService orgUserService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入orgUser")
	public R<OrgUserVO> detail(OrgUser orgUser) {
		OrgUserVO detail = orgUserService.getDetail(Condition.getQueryWrapper(orgUser));
		return R.data(detail);
	}

	/**
	* 分页 企业用户表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入orgUser")
	public R<IPage<OrgUserVO>> list(OrgUser orgUser, Query query) {
		IPage<OrgUser> pages = orgUserService.page(Condition.getPage(query), Condition.getQueryWrapper(orgUser));
		return R.data(OrgUserWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 企业用户表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orgUser")
	public R<IPage<OrgUserVO>> page(OrgUserVO orgUser, Query query) {
		IPage<OrgUserVO> pages = orgUserService.selectOrgUserPage(Condition.getPage(query), orgUser);
		return R.data(pages);
	}

	/**
	* 新增 企业用户表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入orgUser")
	public R save(@Valid @RequestBody OrgUser orgUser) {
		return R.status(orgUserService.save(orgUser));
	}

	/**
	* 修改 企业用户表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入orgUser")
	public R update(@Valid @RequestBody OrgUser orgUser) {
		return R.status(orgUserService.updateById(orgUser));
	}

	/**
	* 新增或修改 企业用户表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入orgUser")
	public R submit(@Valid @RequestBody OrgUser orgUser) {
		return R.status(orgUserService.saveOrUpdateUser(orgUser));
	}

	
	/**
	* 删除 企业用户表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(orgUserService.deleteLogic(Func.toLongList(ids)));
	}
	@GetMapping("/downloadUserTemplate")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "下载导入用户模板，如果传入key就是下载对比失败文件")
	public void downloadTemplate(HttpServletResponse response, @RequestParam(required = false) String key) {
		orgUserService.downloadTemplate(response,key);
	}
	@PostMapping("/importUserFile")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "导入客户管理文件")
	public R importUserFile(MultipartFile file) {
		return orgUserService.imporCustomerFile(file);
	}

	@GetMapping("/getImportData")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "获取导入客户数据")
	public R getImportData(@RequestParam(value = "redisKey") String redisKey, @RequestParam(value = "isSuccess")Boolean isSuccess, Query query) {
		return orgUserService.getImportData(redisKey,isSuccess,query);
	}
	@GetMapping("/submitpassdata")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "提交用户信息数据")
	public R submitpassdata(@RequestParam(value = "redisKey") String redisKey) {
		return orgUserService.submitpassdata(redisKey);
	}

	@GetMapping("/export")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "机构用户管理导出")
	public void exportOrgUserList(HttpServletResponse response, OrgUserVO orgUser) {
		orgUserService.exportOrgUserList(response, orgUser);
	}
}

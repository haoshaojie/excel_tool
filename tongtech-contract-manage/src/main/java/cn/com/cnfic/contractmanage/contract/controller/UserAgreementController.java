package cn.com.cnfic.contractmanage.contract.controller;

import cn.com.cnfic.contractmanage.contract.dto.UserAgreementDTO;
import cn.com.cnfic.contractmanage.contract.entity.UserAgreement;
import cn.com.cnfic.contractmanage.contract.service.IUserAgreementFileService;
import cn.com.cnfic.contractmanage.contract.service.IUserAgreementService;
import cn.com.cnfic.contractmanage.contract.vo.UserAgreementVO;
import cn.com.cnfic.contractmanage.contract.wrapper.UserAgreementWrapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户协议表 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-05
 */
@RestController
@AllArgsConstructor
@RequestMapping("/useragreement")
@Api(value = "用户协议表", tags = "用户协议表接口")
public class UserAgreementController extends BladeController {

	private IUserAgreementService userAgreementService;

	private IUserAgreementFileService userAgreementFileService;
	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入userAgreement")
	public R<UserAgreementVO> detail(UserAgreement userAgreement) {
		UserAgreementVO detail = userAgreementService.getUserAgreementDetail(userAgreement);
		return R.data(detail);
	}

	/**
	* 分页 用户协议表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入userAgreement")
	public R<IPage<UserAgreementVO>> list(UserAgreement userAgreement, Query query) {
		IPage<UserAgreement> pages = userAgreementService.page(Condition.getPage(query), Condition.getQueryWrapper(userAgreement));
		return R.data(UserAgreementWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 用户协议表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入userAgreement")
	public R<IPage<UserAgreementVO>> page(UserAgreementDTO userAgreement, Query query) {
		IPage<UserAgreementVO> pages = userAgreementService.selectUserAgreementPage(query, userAgreement);
		return R.data(pages);
	}

	/**
	* 新增 用户协议表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入userAgreement")
	public R save(@Valid @RequestBody UserAgreement userAgreement) {
		return R.status(userAgreementService.save(userAgreement));
	}

	/**
	* 修改 用户协议表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入userAgreement")
	public R update(@Valid @RequestBody UserAgreement userAgreement) {
		return R.status(userAgreementService.updateById(userAgreement));
	}

	/**
	* 新增或修改 用户协议表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入userAgreement")
	public R submit(@Valid @RequestBody UserAgreementDTO userAgreement) {
		return userAgreementService.saveOrUpdateAndProtocolFile(userAgreement);
	}


	/**
	* 删除 用户协议表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return userAgreementService.remove(ids);
	}


}

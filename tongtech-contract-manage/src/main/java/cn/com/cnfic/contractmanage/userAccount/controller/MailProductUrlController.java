package cn.com.cnfic.contractmanage.userAccount.controller;

import cn.com.cnfic.contractmanage.userAccount.entity.MailProductUrl;
import cn.com.cnfic.contractmanage.userAccount.service.IMailProductUrlService;
import cn.com.cnfic.contractmanage.userAccount.vo.MailProductUrlVO;
import cn.com.cnfic.contractmanage.userAccount.wrapper.MailProductUrlWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 邮件模板产品下载链接表 控制器
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mailProductUrl")
@Api(value = "邮件模板产品下载链接表", tags = "邮件模板产品下载链接表接口")
public class MailProductUrlController extends BladeController {

	private IMailProductUrlService mailProductUrlService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入mailProductUrl")
	public R<MailProductUrlVO> detail(MailProductUrl mailProductUrl) {
		MailProductUrl detail = mailProductUrlService.getOne(Condition.getQueryWrapper(mailProductUrl));
		return R.data(MailProductUrlWrapper.build().entityVO(detail));
	}

	/**
	* 分页 邮件模板产品下载链接表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入mailProductUrl")
	public R<IPage<MailProductUrlVO>> list(MailProductUrl mailProductUrl, Query query) {
		IPage<MailProductUrl> pages = mailProductUrlService.page(Condition.getPage(query), Condition.getQueryWrapper(mailProductUrl));
		return R.data(MailProductUrlWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 邮件模板产品下载链接表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入mailProductUrl")
	public R<IPage<MailProductUrlVO>> page(MailProductUrlVO mailProductUrl, Query query) {
		IPage<MailProductUrlVO> pages = mailProductUrlService.selectMailProductUrlPage(Condition.getPage(query), mailProductUrl);
		return R.data(pages);
	}

	/**
	* 新增 邮件模板产品下载链接表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入mailProductUrl")
	public R save(@Valid @RequestBody MailProductUrl mailProductUrl) {
		return R.status(mailProductUrlService.save(mailProductUrl));
	}

	/**
	* 修改 邮件模板产品下载链接表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入mailProductUrl")
	public R update(@Valid @RequestBody MailProductUrl mailProductUrl) {
		return R.status(mailProductUrlService.updateById(mailProductUrl));
	}

	/**
	* 新增或修改 邮件模板产品下载链接表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入mailProductUrl")
	public R submit(@Valid @RequestBody MailProductUrl mailProductUrl) {
		return R.status(mailProductUrlService.saveOrUpdate(mailProductUrl));
	}


	/**
	* 删除 邮件模板产品下载链接表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(mailProductUrlService.deleteLogic(Func.toLongList(ids)));
	}


}

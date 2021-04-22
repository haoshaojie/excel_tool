package cn.com.cnfic.contractmanage.userAccount.controller;

import cn.com.cnfic.contractmanage.userAccount.entity.MailTemplate;
import cn.com.cnfic.contractmanage.userAccount.service.IMailTemplateService;
import cn.com.cnfic.contractmanage.userAccount.vo.MailTemplateVO;
import cn.com.cnfic.contractmanage.userAccount.wrapper.MailTemplateWrapper;
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
 * 邮件模板表 控制器
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mailTemplate")
@Api(value = "邮件模板表", tags = "邮件模板表接口")
public class MailTemplateController extends BladeController {

	private IMailTemplateService mailTemplateService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入mailTemplate")
	public R<MailTemplateVO> detail(MailTemplate mailTemplate) {
		MailTemplate detail = mailTemplateService.getOne(Condition.getQueryWrapper(mailTemplate));
		return R.data(MailTemplateWrapper.build().entityVO(detail));
	}

	/**
	* 分页 邮件模板表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入mailTemplate")
	public R<IPage<MailTemplateVO>> list(MailTemplate mailTemplate, Query query) {
		IPage<MailTemplate> pages = mailTemplateService.page(Condition.getPage(query), Condition.getQueryWrapper(mailTemplate));
		return R.data(MailTemplateWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 邮件模板表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入mailTemplate")
	public R<IPage<MailTemplateVO>> page(MailTemplateVO mailTemplate, Query query) {
		IPage<MailTemplateVO> pages = mailTemplateService.selectMailTemplatePage(Condition.getPage(query), mailTemplate);
		return R.data(pages);
	}

	/**
	* 新增 邮件模板表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入mailTemplate")
	public R save(@Valid @RequestBody MailTemplate mailTemplate) {
		return R.status(mailTemplateService.save(mailTemplate));
	}

	/**
	* 修改 邮件模板表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入mailTemplate")
	public R update(@Valid @RequestBody MailTemplate mailTemplate) {
		return R.status(mailTemplateService.updateById(mailTemplate));
	}

	/**
	* 新增或修改 邮件模板表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入mailTemplate")
	public R submit(@Valid @RequestBody MailTemplate mailTemplate) {
		return R.status(mailTemplateService.saveOrUpdate(mailTemplate));
	}


	/**
	* 删除 邮件模板表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(mailTemplateService.deleteLogic(Func.toLongList(ids)));
	}


}

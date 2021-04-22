package cn.com.cnfic.contractmanage.userAccount.controller;

import cn.com.cnfic.contractmanage.userAccount.dto.MailRecordDTO;
import cn.com.cnfic.contractmanage.userAccount.entity.MailRecord;
import cn.com.cnfic.contractmanage.userAccount.model.TemplateEmailParam;
import cn.com.cnfic.contractmanage.userAccount.service.IMailRecordService;
import cn.com.cnfic.contractmanage.userAccount.service.impl.MailRecordServiceImpl;
import cn.com.cnfic.contractmanage.userAccount.vo.MailRecordVO;
import cn.com.cnfic.contractmanage.userAccount.wrapper.MailRecordWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

/**
 * 邮件发送记录表 控制器
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mailRecord")
@Api(value = "邮件发送记录表", tags = "邮件发送记录表接口")
public class MailRecordController extends BladeController {

    private IMailRecordService mailRecordService;

    /**
     * 分页 邮件发送记录表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入mailRecord")
    public R<IPage<MailRecordVO>> list(MailRecordDTO mailRecord, Query query) {
        IPage<MailRecord> pages = mailRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(mailRecord));
        return R.data(MailRecordWrapper.build().pageVO(pages));
    }

    /**
     * 邮件发送
     *
     * @see MailRecordServiceImpl#sendTemplateMail(TemplateEmailParam)
     */
    @PostMapping("/sendtemplatemail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入TemplateEmailParam")
    public R sendTemplateMail(@RequestBody TemplateEmailParam emailParam) {
        return mailRecordService.sendTemplateMail(emailParam);
    }


}

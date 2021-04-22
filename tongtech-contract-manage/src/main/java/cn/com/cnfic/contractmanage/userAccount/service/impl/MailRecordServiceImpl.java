package cn.com.cnfic.contractmanage.userAccount.service.impl;


import cn.com.cnfic.contractmanage.userAccount.email.EmailService;
import cn.com.cnfic.contractmanage.userAccount.email.TemplateDataDTO;
import cn.com.cnfic.contractmanage.userAccount.email.model.Sender;
import cn.com.cnfic.contractmanage.userAccount.email.model.TemplateEmail;
import cn.com.cnfic.contractmanage.userAccount.entity.MailRecord;
import cn.com.cnfic.contractmanage.userAccount.entity.MailTemplate;
import cn.com.cnfic.contractmanage.userAccount.mapper.MailRecordMapper;
import cn.com.cnfic.contractmanage.userAccount.model.EmailReceiverDTO;
import cn.com.cnfic.contractmanage.userAccount.model.TemplateEmailParam;
import cn.com.cnfic.contractmanage.userAccount.service.IMailRecordService;
import cn.com.cnfic.contractmanage.userAccount.service.IMailTemplateService;
import cn.com.cnfic.contractmanage.userAccount.vo.MailRecordVO;
import cn.com.cnfic.system.entity.Param;
import cn.com.cnfic.system.feign.ISysParamClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static cn.com.cnfic.contractmanage.userAccount.email.EmailService.newTemplate;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 邮件发送记录表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
@AllArgsConstructor
@Service
public class MailRecordServiceImpl extends BaseServiceImpl<MailRecordMapper, MailRecord> implements IMailRecordService {

    /**
     * 系统邮件发送者参数
     * {@value}
     */
    private static final String SYS_EMAIL_SENDER = "system.email.sender";
    /**
     * 邮件模版不存在提示消息
     * {@value}
     */
    private static final String TEMPLATE_FILE_NOT_EXIT = "邮件模版文件不存在";
    /**
     * 邮件接收者信息不能为空
     * {@value}
     */
    private static final String RECEIVER_IS_REQUIRED = "邮件接收者数据不能为空";
    /**
     * 邮件模版数据不能为空
     * {@value}
     */
    private static final String template_IS_REQUIRED = "邮件模版数据不能为空";

    private final EmailService emailService;
    private final IMailTemplateService mailTemplateService;
    private final ISysParamClient sysParamClient;
    private final Validator validator;


    /**
     * 获取系统参数：邮件发送者信息
     *
     * @return
     */
    private Sender getSystemEmailSender() {
        Param param = sysParamClient.getByKey(SYS_EMAIL_SENDER);
        return JsonUtil.parse(param.getParamValue(), Sender.class);
    }

    /**
     * 邮件发送输入参数校验
     *
     * @param emailParam
     * @return
     */
    private R inputParaCheck(TemplateEmailParam emailParam) {
        if (CollectionUtils.isEmpty(emailParam.getReceivers())) {
            return R.fail(RECEIVER_IS_REQUIRED);
        }
        if (StringUtils.isBlank(emailParam.getTempFile())) {
            return R.fail(template_IS_REQUIRED);
        }
        for (EmailReceiverDTO receiver : emailParam.getReceivers()) {
            DataBinder binder = new DataBinder(receiver);
            binder.setValidator(validator);
            binder.validate();
            BindingResult bindingResult = binder.getBindingResult();
            if (bindingResult.hasErrors()) {
                return R.fail(String.join(";",
                        bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(toList())));
            }
        }
        return R.status(Boolean.TRUE);
    }


    @Override
    public R sendTemplateMail(TemplateEmailParam emailParam) {
        //输入校验
        R checkResult = inputParaCheck(emailParam);
        if (R.isNotSuccess(checkResult)) {
            return checkResult;
        }
        //获取系统邮件模版
        MailTemplate mailTemplate =
                mailTemplateService.getOne(Wrappers.lambdaQuery(MailTemplate.class).eq(MailTemplate::getTempFile
                        , emailParam.getTempFile()));
        if (Objects.isNull(mailTemplate)) {
            return R.fail(TEMPLATE_FILE_NOT_EXIT);
        }
        Template systemTemplateFile = emailService.getFreemarkerTemplate(mailTemplate.getTempFile());
        if (Objects.isNull(systemTemplateFile)) {
            return R.fail(TEMPLATE_FILE_NOT_EXIT);

        }
        //获取系统邮件发送者信息
        Sender systemEmailSenderInfo = getSystemEmailSender();
        JavaMailSender mailSender = emailService.createSender(systemEmailSenderInfo);
        //创建模版邮件数据
        List<MailRecord> mailRecords = Lists.newArrayList();
        Date createTime1 = new Date();
        LocalDateTime createTime2 = LocalDateTime.now();
        Long createUser = SecureUtil.getUserId();
        List<TemplateEmail> emails = emailParam.getReceivers().stream().map(r -> {
            //模版邮件
            TemplateEmail email = new TemplateEmail();
            email.setSubject(mailTemplate.getMailSubject());
            email.setReceivers(Sets.newHashSet(r.getEmail()));
            email.setFromAddr(systemEmailSenderInfo.getUsername());
            email.setEmailTemplate(newTemplate(BeanUtil.copy(r, TemplateDataDTO.class), systemTemplateFile));
            //邮件记录
            MailRecord mailRecord = new MailRecord();
            mailRecord.setAccountId(Long.valueOf(r.getAccId()));
            mailRecord.setUserName(r.getUserName());
            mailRecord.setEmail(r.getEmail());
            mailRecord.setMailSubject(mailTemplate.getMailSubject());
            mailRecord.setMailSendEmail(systemEmailSenderInfo.getUsername());
            mailRecord.setMailSendState(NumberUtils.INTEGER_ONE);
            mailRecord.setMailSendTime(createTime2);
            mailRecord.setCreateUser(createUser);
            mailRecord.setCreateTime(createTime1);
            mailRecords.add(mailRecord);
            return email;
        }).collect(toList());
        //发送模版邮件
        emailService.sendTemplateEmailAsync(emails, mailSender).thenAccept(sendFailResult -> {
            //更新邮件记录发送状态
            Map<String, MailRecord> initRecords = mailRecords.stream().collect(
                    toMap(MailRecord::getEmail, Function.identity(), (v1, v2) -> v2));
            sendFailResult.forEach(f -> {
                MailRecord failRecord = initRecords.get(f.getReceivers().toArray()[0]);
                if (Objects.nonNull(failRecord)) {
                    failRecord.setMailSendState(NumberUtils.INTEGER_TWO);
                }
            });
            //保存邮件发送记录
            this.saveBatch(mailRecords);
        });
        return R.status(Boolean.TRUE);
    }

    @Override
    public IPage<MailRecordVO> selectMailRecordPage(IPage<MailRecordVO> page, MailRecordVO mailRecord) {
        return page.setRecords(baseMapper.selectMailRecordPage(page, mailRecord));
    }

}

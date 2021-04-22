package cn.com.cnfic.contractmanage.userAccount.email;

import cn.com.cnfic.contractmanage.userAccount.email.model.Email;
import cn.com.cnfic.contractmanage.userAccount.email.model.EmailTemplate;
import cn.com.cnfic.contractmanage.userAccount.email.model.Sender;
import cn.com.cnfic.contractmanage.userAccount.email.model.TemplateEmail;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 邮件服务
 * @Author misterbig
 * @Date 2021/4/12
 */
public interface EmailService {
    /**
     * 异步发送模版邮件
     *
     * @param emails
     * @param javaMailSender
     * @return
     */
    CompletableFuture<Set<TemplateEmail>> sendTemplateEmailAsync(List<TemplateEmail> emails, JavaMailSender javaMailSender);

    /**
     * 发送模版邮件
     *
     * @param emails
     * @param javaMailSender
     * @return 发送失败的模版邮件
     */
    Set<TemplateEmail> sendTemplateEmail(List<TemplateEmail> emails, JavaMailSender javaMailSender);

    /**
     * 创建邮件消息发送者
     *
     * @param param
     * @return
     */
    default JavaMailSender createSender(Sender param) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(param.getHost());
        sender.setUsername(param.getUsername());
        sender.setPassword(param.getUsername());
        sender.setDefaultEncoding("Utf-8");
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", param.getHost());
        props.setProperty("mail.smtp.auth", "true");
        sender.setSession(Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(param.getUsername(), param.getPassword());
            }
        }));
        return sender;
    }

    /**
     * 获取系统邮件模版文件
     *
     * @param fileName
     * @return
     */
    Template getFreemarkerTemplate(String fileName);

    /**
     * 创建邮件信息
     *
     * @param builder
     * @param param
     * @return
     */
    @SneakyThrows
    default MimeMessage createMessage(JavaMailSender builder, Email param) {
        MimeMessage emailMessage = builder.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(emailMessage, true);
        // 标题
        messageHelper.setSubject(param.getSubject());
        // 发件人地址
        messageHelper.setFrom(param.getFromAddr());
        //收件人地址
        messageHelper.setTo(param.getReceivers().stream().toArray(String[]::new));
        //设置邮件内容
        messageHelper.setText(param.getContent(), param.isHtml());
        return emailMessage;
    }

    /**
     * 创建模版邮件内容
     *
     * @param freemarkerTemplate
     * @param model
     * @return
     */
    @SneakyThrows
    default String processTemplate(Template freemarkerTemplate, Object model) {
        //渲染模版
        return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
    }

    /**
     * 创建邮件模版
     *
     * @param data
     * @param template 模版文件
     * @return
     */
    static EmailTemplate newTemplate(TemplateDataDTO data, Template template) {
        return new EmailTemplate() {{
            setModel(BeanUtil.toMap(data));
            setTemplate(template);
        }};
    }

}

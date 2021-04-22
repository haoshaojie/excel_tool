package cn.com.cnfic.contractmanage.userAccount.email.impl;

import cn.com.cnfic.contractmanage.userAccount.email.EmailService;
import cn.com.cnfic.contractmanage.userAccount.email.model.TemplateEmail;
import com.google.common.collect.Sets;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 邮件服务实现
 * @Author misterbig
 * @Date 2021/4/12
 */
@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public CompletableFuture<Set<TemplateEmail>> sendTemplateEmailAsync(List<TemplateEmail> emails, JavaMailSender javaMailSender) {
        return CompletableFuture.supplyAsync(() -> {
            return sendTemplateEmail(emails, javaMailSender);
        });
    }

    @Override
    public Set<TemplateEmail> sendTemplateEmail(List<TemplateEmail> emails, JavaMailSender javaMailSender) {
        Set sendFailResult = Sets.newHashSet();
        emails.forEach(m -> {
            try {
                m.setContent(processTemplate(m.getEmailTemplate().getTemplate(), m.getEmailTemplate().getModel()));
                javaMailSender.send(createMessage(javaMailSender, m));
            } catch (Exception e) {
                sendFailResult.add(m);
            }
        });
        return sendFailResult;
    }

    @SneakyThrows
    @Override
    public Template getFreemarkerTemplate(String fileName) {
        return freeMarkerConfigurer.getConfiguration().getTemplate(fileName);
    }

}

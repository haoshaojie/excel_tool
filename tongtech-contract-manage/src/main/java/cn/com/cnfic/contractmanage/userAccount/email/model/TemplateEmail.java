package cn.com.cnfic.contractmanage.userAccount.email.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 模版邮件
 * @Author misterbig
 * @Date 2021/4/12
 */
@ApiModel(value = "模版邮件", description = "模版邮件")
@Data
public class TemplateEmail extends Email {
    @ApiModelProperty("邮件模版")
    private EmailTemplate emailTemplate;
}

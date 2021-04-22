package cn.com.cnfic.contractmanage.userAccount.email.model;

import freemarker.template.Template;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @Description 邮件模版
 * @Author misterbig
 * @Date 2021/4/16
 */
@ApiModel(value = "邮件模版", description = "邮件模版")
@Data
public class EmailTemplate extends Email {
    @ApiModelProperty("模版数据")
    private Map<String, Object> model;
    @ApiModelProperty("系统模版文件")
    private Template template;
}

package cn.com.cnfic.contractmanage.userAccount.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Description 邮件发送参数对象
 * @Author misterbig
 * @Date 2021/4/13
 */
@ApiModel(value = "邮件发送参数对象", description = "邮件发送参数对象")
@Data
public class TemplateEmailParam {
    @ApiModelProperty("收件人")
    private List<EmailReceiverDTO> receivers;
    @ApiModelProperty("邮件类型：模版文件名称")
    @NotBlank(message = "邮件模版文件名称不能为空")
    private String tempFile;
}

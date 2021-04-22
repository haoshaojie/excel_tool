package cn.com.cnfic.contractmanage.userAccount.model;

import cn.com.cnfic.contractmanage.userAccount.email.TemplateDataDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 邮件接收者信息
 * @Author misterbig
 * @Date 2021/4/13
 */
@ApiModel(value = "邮件接收者信息", description = "邮件接收者信息")
@Data
public class EmailReceiverDTO extends TemplateDataDTO {
    @ApiModelProperty("用户账号ID")
    @NotBlank(message = "账号id不能为空")
    private String accId;
    @ApiModelProperty("用户邮箱")
    @NotBlank(message = "用户邮箱不能为空")
    private String email;
}

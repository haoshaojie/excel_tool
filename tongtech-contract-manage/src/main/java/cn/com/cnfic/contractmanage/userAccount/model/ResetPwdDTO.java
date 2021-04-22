package cn.com.cnfic.contractmanage.userAccount.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 账号管理-重置密码
 * @Author misterbig
 * @Date 2021/4/6
 */
@Data
@ApiModel(value = "账号管理-重置密码数据模型", description = "账号管理-重置密码数据模型")
public class ResetPwdDTO {
    @ApiModelProperty("发送邮件通知：true-发送，false-不发送")
    private Boolean email;
    @ApiModelProperty("发送短信通知：true-发送，false-不发送")
    private Boolean phone;
    @ApiModelProperty("账号ID")
    @NotBlank(message = "账号ID不能为空")
    private String accId;
}

package cn.com.cnfic.contractmanage.userAccount.email;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 模版邮件数据模型
 * @Author misterbig
 * @Date 2021/4/16
 */
@ApiModel(value = "模版邮件数据模型", description = "模版邮件数据模型")
@Data
public class TemplateDataDTO {
    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    @NotBlank(message = "用户账号不能为空")
    private String account;
    /**
     * 账号有效期
     */
    @ApiModelProperty("账号有效期")
    private String accountExpire;
    /**
     * 初始密码
     */
    @ApiModelProperty("初始密码")
    @NotBlank(message = "初始密码不能为空")
    private String initPassword;
    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String userName;
    /**
     * 产品链接
     */
    @ApiModelProperty("产品链接")
    private String productLink;
    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private String orderNum;
}

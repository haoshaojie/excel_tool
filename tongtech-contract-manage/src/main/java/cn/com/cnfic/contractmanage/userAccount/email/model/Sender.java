package cn.com.cnfic.contractmanage.userAccount.email.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 邮件发送者信息
 * @Author misterbig
 * @Date 2021/4/12
 */
@ApiModel(value = "邮件发送者信息", description = "邮件发送者信息")
@Data
public class Sender {
    @ApiModelProperty("主键id 雪花算法产生")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty("邮件服务主机")
    private String host;
    @ApiModelProperty("发送邮件的账户")
    private String username;
    @ApiModelProperty("授权码或者发件人邮箱密码")
    private String password;
}

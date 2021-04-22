package cn.com.cnfic.contractmanage.userAccount.email.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @Description 邮件数据模型
 * @Author misterbig
 * @Date 2021/4/12
 */
@ApiModel(value = "邮件数据模型", description = "邮件数据模型")
@Data
public class Email {
    @ApiModelProperty("主键id 雪花算法产生")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty("邮件主题")
    private String subject;
    @ApiModelProperty("邮件内容")
    private String content;
    @ApiModelProperty("邮件内容是否包含html代码")
    private boolean html = true;
    @ApiModelProperty("邮件接收者")
    private Set<String> receivers;
    @ApiModelProperty("邮件发送者账号")
    private String fromAddr;
}

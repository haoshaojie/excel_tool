package cn.com.cnfic.contractmanage.userAccount.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 账号管理-锁定/解锁数据模型
 * @Author misterbig
 * @Date 2021/4/6
 */
@Data
@ApiModel(value = "账号管理-锁定/解锁数据模型", description = "账号管理-锁定/解锁数据模型")
public class ResetLockDTO {
    @ApiModelProperty("重置状态：true-锁定，false-解锁")
    @NotNull(message = "账号锁定标识不能为空")
    private Boolean flag;
    @ApiModelProperty("账号ID")
    @NotBlank(message = "账号ID不能为空")
    private String accId;
}

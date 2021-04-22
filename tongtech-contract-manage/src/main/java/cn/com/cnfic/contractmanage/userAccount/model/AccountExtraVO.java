package cn.com.cnfic.contractmanage.userAccount.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.ToString;

import java.util.List;

/**
 * @Description 企业用户账号管理-用户账户号-详情附属信息
 * @Author misterbig
 * @Date 2021/4/6
 */
@Builder
@ToString
@ApiModel(value = "用户账号详情扩展信息数据模型", description = "用户账号详情附属信息")
public class AccountExtraVO<T> {
    @ApiModelProperty("数量：合约数量/访问产品数量")
    private Integer number;
    @ApiModelProperty("数据：合约数据/访问产品数据")
    private List<T> data;
}

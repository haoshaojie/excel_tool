package cn.com.cnfic.contractmanage.userAccount.excel;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 账号订单扩展数据模型
 * @Author misterbig
 * @Date 2021/4/15
 */
@Data
@ApiModel(value = "账号订单扩展数据模型", description = "账号订单扩展数据模型")
public class AccountOrderDTO extends AccountExportDTO {
    @ApiModelProperty("账号订单查询参数")
    private List<AccInfoForTreaty> orderParams;
}

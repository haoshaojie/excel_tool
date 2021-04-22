package cn.com.cnfic.contractmanage.userAccount.excel;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 用户账号管理导出数据模型
 * @Author misterbig
 * @Date 2021/4/8
 */
@Data
@ApiModel(value = "用户账号管理导出数据模型", description = "用户账号管理导出数据模型")
public class AccountExportDTO extends AccInfoForTreaty {
    @ApiModelProperty("导出选择列")
    private List<String> exportField;
    @ApiModelProperty("导出选择列名称")
    private List<String> fieldName;
    @ApiModelProperty("是否订单信息")
    private Boolean orderChecked;
    @ApiModelProperty("是否导出合约信息")
    private Boolean contractChecked;
    @ApiModelProperty("是否导出产品信息")
    private Boolean productChecked;
}

package cn.com.cnfic.contractmanage.userAccount.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description 用户账号导出工作表数据模型
 * @Author misterbig
 * @Date 2021/4/8
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "用户账号导出工作表数据模型", description = "用户账号导出工作表数据模型")
public class ExcelSheetDTO<T> {
    @ApiModelProperty("工作表index")
    private Integer sheetNo;
    @ApiModelProperty("工作表名称")
    private String name;
    @ApiModelProperty("动态表头")
    private List<List<String>> dynamicHead;
    @ApiModelProperty("行数据")
    private List<T> data;
    @ApiModelProperty("固定表头")
    private Class<T> fixedHead;


}

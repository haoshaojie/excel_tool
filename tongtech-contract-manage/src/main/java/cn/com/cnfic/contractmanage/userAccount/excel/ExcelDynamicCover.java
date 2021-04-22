package cn.com.cnfic.contractmanage.userAccount.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @Description excel动态表头对应的行数据转换器模型
 * @Author misterbig
 * @Date 2021/4/14
 */
@Data
@ApiModel(value = "excel动态表头对应的行数据转换器模型", description = "excel动态表头对应的行数据转换器模型")
public class ExcelDynamicCover {
    @ApiModelProperty("需要转换的字段名称")
    private String field;
    @ApiModelProperty("键值映射表：key-字段值，value-转义名称")
    private Map<Integer, Object> values;
}

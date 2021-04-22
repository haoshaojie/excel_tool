package cn.com.cnfic.contractmanage.blackList.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description 黑白名单导入数据模型
 * @Author misterbig
 * @Date 2021/3/15
 * @see ExcelConstant
 */
@Data
@ContentRowHeight(ExcelConstant.CONTENT_ROW_HEIGHT)
@HeadRowHeight(ExcelConstant.HEAD_ROW_HEIGHT)
@ColumnWidth(ExcelConstant.COLUMN_WIDTH)
public class BlackWhiteListImport implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易所
     */
    @ExcelProperty(value = {"黑白名单导入模版", "注：带*号为必填项，请不要增删表头，名单类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*交易所"}, order = 0)
    @NotBlank
    @Size(min = 1, max = 50, message = "交易所必填，限制50字以内")
    private String exchange;
    /**
     * 数据编码
     */
    @ExcelProperty(value = {"黑白名单导入模版", "注：带*号为必填项，请不要增删表头，名单类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*数据编码"}, order = 1)
    @NotBlank
    @Size(min = 1, max = 50, message = "数据编码必填，限制50字以内")
    private String dataCode;
    /**
     * 类型1：黑名单；2：白名单
     */
    @ExcelProperty(value = {"黑白名单导入模版", "注：带*号为必填项，请不要增删表头，名单类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*名单类型"}, order = 2, converter = TypeOfBlackWhiteListConverter.class)
    @NotNull(message = "类型必选：1-黑名单，2-白名单")
    @Range(min = 1, max = 2, message = "类型必选：1-黑名单，2-白名单")
    private Integer type;
    /**
     * 客户名称
     */
    @ExcelProperty(value = {"黑白名单导入模版", "注：带*号为必填项，请不要增删表头，名单类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户名称"}, order = 3)
    @NotBlank
    @Size(min = 1, max = 50, message = "客户名称必填，限制50字以内")
    private String custName;
    /**
     * 客户编码
     */
    @ExcelProperty(value = {"黑白名单导入模版", "注：带*号为必填项，请不要增删表头，名单类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "客户编码"}, order = 4)
    private String custCode;
    /**
     * 备注
     */
    @ExcelProperty(value = {"黑白名单导入模版", "注：带*号为必填项，请不要增删表头，名单类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "备注"}, order = 5)
    @Size(max = 100, message = "备注非必要，限制100个字内")
    private String remark;

}

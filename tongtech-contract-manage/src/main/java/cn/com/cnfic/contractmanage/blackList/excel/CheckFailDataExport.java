package cn.com.cnfic.contractmanage.blackList.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author misterbig
 * @Date 2021/3/18
 */
@Data
@ContentRowHeight(ExcelConstant.CONTENT_ROW_HEIGHT)
@HeadRowHeight(ExcelConstant.HEAD_ROW_HEIGHT)
@ColumnWidth(ExcelConstant.COLUMN_WIDTH)
public class CheckFailDataExport implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelIgnore
    private Long id;

    /**
     * 交易所
     */
    @ExcelProperty(value = {"黑白名单校验失败数据列表", "交易所"}, order = 0)
    private String exchange;
    /**
     * 数据编码
     */
    @ExcelProperty(value = {"黑白名单校验失败数据列表", "数据编码"}, order = 1)
    private String dataCode;
    /**
     * 类型1：黑名单；2：白名单
     */
    @ExcelProperty(value = {"黑白名单校验失败数据列表", "名单类型"}, order = 2, converter = TypeOfBlackWhiteListConverter.class)
    private Integer type;
    /**
     * 客户名称
     */
    @ExcelProperty(value = {"黑白名单校验失败数据列表", "客户名称"}, order = 3)
    private String custName;
    /**
     * 备注
     */
    @ExcelProperty(value = {"黑白名单校验失败数据列表", "备注"}, order = 4)
    private String remark;
    /**
     * 错误提示
     */
    @ExcelProperty(value = {"黑白名单校验失败数据列表", "错误提示"}, order = 5)
    private String errorMsg;
}

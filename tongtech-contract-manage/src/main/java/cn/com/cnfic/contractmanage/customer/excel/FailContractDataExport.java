package cn.com.cnfic.contractmanage.customer.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * @desc 校验失败的联系人实体
 * @auther yangchuan
 * @date 2021/3/26
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
public class FailContractDataExport extends  CheckContractDataExport{
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "错误信息"}, order = 11)
    private String msg;
}

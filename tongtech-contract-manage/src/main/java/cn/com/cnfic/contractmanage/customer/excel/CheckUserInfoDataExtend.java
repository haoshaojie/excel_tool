package cn.com.cnfic.contractmanage.customer.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * @desc 对比失败不能入库的客户基本信息实体
 * @auther yangchuan
 * @date 2021/3/26
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
public class CheckUserInfoDataExtend  extends CheckUserInfoDataExport {
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "错误类型"}, order = 17)
    private String msg;
}

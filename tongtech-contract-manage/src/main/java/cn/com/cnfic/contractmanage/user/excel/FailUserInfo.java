package cn.com.cnfic.contractmanage.user.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @desc
 * @auther yangchuan
 * @date 2021/4/1
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
public class FailUserInfo extends  UserImportTemplate {
    /**
     * 导入失败信息
     */
    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "导入失败信息"}, order = 13)
    private String failMsg;
}

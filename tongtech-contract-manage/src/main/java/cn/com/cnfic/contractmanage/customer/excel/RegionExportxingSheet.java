package cn.com.cnfic.contractmanage.customer.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc 导出行政区划实体
 * @auther yangchuan
 * @date 2021/4/14
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
@NoArgsConstructor
public class RegionExportxingSheet {
    @ExcelProperty(value = "省份",order = 1)
    private String province;
    @ExcelProperty(value = "市",order = 2)
    private String city;

    public RegionExportxingSheet(Object province, Object city) {
        this.province = province==null?null:String.valueOf(province);
        this.city = city==null?null:String.valueOf(city);;
    }
}

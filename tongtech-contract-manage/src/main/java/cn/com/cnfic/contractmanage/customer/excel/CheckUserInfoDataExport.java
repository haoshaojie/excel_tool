package cn.com.cnfic.contractmanage.customer.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @desc 客户基本信息导入模版实体
 * @auther yangchuan
 * @date 2021/3/26
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
public class CheckUserInfoDataExport  implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户名称"}, order = 1)
    @NotBlank(message = "客户名称必填")
    @Size(min = 1, max = 50, message = "客户名称必填，限制50字以内")
    private String custName;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户类型"}, order = 2)
    @NotBlank(message = "客户类型必填")
    @Size(message = "客户类型必填，限制50字以内")
    private String custTypeName;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "上级单位"}, order = 3)
    private String superUnitName;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "所属行业(一级行业"}, order = 4)
    private String firstName;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "所属行业（二级级行业）"}, order = 5)
    private String secondName;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "所属行业（三级行业）"}, order = 6)
    private String thirdName;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "所属地域（省"}, order = 7)
    private String custProvince;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "所属地域（市）"}, order = 8)
    private String custCity;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "详细地址"}, order = 9)
    private String custAddress;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "邮政编码"}, order = 10)
    private String postcode;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "公司电话"}, order = 11)
    private String comPhone;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "公司传真"}, order = 12)
    private String comFax;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "公司网址"}, order = 13)
    private String comWebsite;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "客户状态"}, order = 14)
    private String custStateName;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "业务机会状态"}, order = 15)
    private String businessChanceName;
    @ExcelProperty(value = {"客户信息导入模版", "注：带*号为必填项，请不要增删表头，客户类型、客户状态、业务机会状态根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "业务描述"}, order = 16)
    private String businessDesc;

    //区划代码
    @ExcelIgnore
    private String custProvinceCode;
    //区划代码
    @ExcelIgnore
    private String custCityCode;
}

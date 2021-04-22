package cn.com.cnfic.contractmanage.customer.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @desc 联系人导入模版实体
 * @auther yangchuan
 * @date 2021/3/26
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
public class CheckContractDataExport  implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户编码"}, order = 1)
    @NotEmpty(message = "客户编码必填")
    private String custCode;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户名称"}, order = 2)
    @NotEmpty(message = "客户名称必填")
    private String custName;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "联系人姓名"}, order = 3)
    @Size(max = 5,message = "联系人名称最长限制5个字")
    private String contactPerson;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "联系人类型"}, order = 4)
    @Size(max = 10,message = "联系人类型最长限制10个字")
    private String contactType;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "联系方式"}, order = 5)
    private String contactPhone;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "邮箱"}, order = 6)
    private String contactEmail;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "部门"}, order = 7)
    @Size(max = 10,message = "部门最长限制10个字")
    private String contactDept;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "职务"}, order = 8)
    @Size(max = 10,message = "联系人职务最长限制10个字")
    private String contactJob;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户经理"}, order = 9)
    @NotEmpty(message = "客户经理必填")
    @Size(max = 5,message = "客户经理最长限制5个字")
    private String custManager;
    @ExcelProperty(value = {"联系人导入模版", "注：带*号为必填项，请不要增删表头，所属机构类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*所属机构"}, order = 10)
    @NotEmpty(message = "所属机构必填")
    private String custOrgName;
    @ExcelIgnore
    @ApiModelProperty(value = "部门id")
    private Long custOrg;
    @ExcelIgnore
    @ApiModelProperty(value = "客户id")
    private Long custId;
}

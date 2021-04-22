package cn.com.cnfic.contractmanage.user.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @desc 用户导入模板实体
 * @auther yangchuan
 * @date 2021/4/1
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
@ApiModel(value = "用户导入模板实体")
public class UserImportTemplate  implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户编码"}, order = 1)
    @ApiModelProperty(value = "客户编码")
    @NotEmpty(message = "客户编码必填")
    private String custCode;
    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户名称"}, order = 2)
    @ApiModelProperty(value = "客户名称")
    @NotEmpty(message = "客户名称必填")
    private String custName;
    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*姓名"}, order = 3)
    @ApiModelProperty(value = "姓名")
    @NotEmpty(message = "姓名必填")
    private String userName;
    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*性别"}, order = 4)
    @ApiModelProperty(value = "性别")
    @NotEmpty(message = "性别必填")
    private String userSexName;
    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "部门"}, order = 5)
    @ApiModelProperty(value = "部门")
    private String custOrg;

    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "邮箱"}, order = 6)
    @ApiModelProperty(value = "邮箱")
    private String userEmail;

    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "手机号码"}, order = 7)
    @ApiModelProperty(value = "手机号码")
    private String userPhone;

    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "固定电话"}, order = 8)
    @ApiModelProperty(value = "固定电话")
    private String userTele;

    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "传真"}, order = 9)
    @ApiModelProperty(value = "传真")
    private String userFax;

    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "用户类型"}, order = 10)
    @ApiModelProperty(value = "用户类型")
    private String userTypeName;

    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "用户描述"}, order = 11)
    @ApiModelProperty(value = "用户描述")
    private String userDesc;

    @ExcelProperty(value = {"用户信息导入模版", "注：带*号为必填项，请不要增删表头，客户经理、性别、用户类型根据系统配置自动生成下拉列表请不要更改对应枚举值，否则导入后无法识别。", "*客户经理"}, order = 12)
    @ApiModelProperty(value = "客户经理")
    @NotEmpty(message = "客户经理必填")
    private String custManagerName;



}

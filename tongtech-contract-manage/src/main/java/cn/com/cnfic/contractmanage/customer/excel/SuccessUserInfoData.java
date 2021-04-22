package cn.com.cnfic.contractmanage.customer.excel;

import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @desc 客户基本信息导入成功实体
 * @auther yangchuan
 * @date 2021/3/26
 */
@Data
@ApiModel(value = "成功导入实体")
public class SuccessUserInfoData extends OrgCustomer {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "客户类型名称")
    private String custTypeName;
    @ApiModelProperty(value = "上级单位名称")
    private String superUnitName;
    @ApiModelProperty(value = "客户状态名称")
    private String custStateName;
    @ApiModelProperty(value = "业务机会状态")
    private String businessChanceName;
    @ApiModelProperty(value = "重复提示信息")
    private String repeatTip;

    //区划代码
    @ApiModelProperty(value = "区划代码")
    private String custProvinceCode;
    //区划代码
    @ApiModelProperty(value = "区划代码")
    private String custCityCode;
}

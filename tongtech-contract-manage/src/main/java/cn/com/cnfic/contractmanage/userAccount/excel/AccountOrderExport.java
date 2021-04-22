package cn.com.cnfic.contractmanage.userAccount.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 账号订单数据导出模型
 * @Author misterbig
 * @Date 2021/4/15
 */
@Data
@ApiModel(value = "账号订单数据导出模型", description = "账号订单数据导出模型")
public class AccountOrderExport {
    @ApiModelProperty("账号邮箱")
    @ExcelProperty(value = "邮箱", order = 0)
    private String userEmail;
    @ApiModelProperty("账号手机号")
    @ExcelProperty(value = "手机号码", order = 1)
    private String userPhone;
    @ApiModelProperty("用户姓名")
    @ExcelProperty(value = "姓名", order = 2)
    private String userName;
    @ApiModelProperty("订单号")
    @ExcelProperty(value = "订单号", order = 3)
    private String orderNo;
    @ApiModelProperty("商品名称")
    @ExcelProperty(value = "商品名称", order = 4)
    private String goodsName;
    @ApiModelProperty("订购时间")
    @ExcelProperty(value = "订购时间", order = 5)
    private LocalDateTime expiredDate;
    @ApiModelProperty("到期时间")
    @ExcelProperty(value = "到期时间", order = 6)
    private LocalDateTime orderTime;
}

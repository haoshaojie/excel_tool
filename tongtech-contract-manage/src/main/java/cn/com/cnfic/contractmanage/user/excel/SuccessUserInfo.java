package cn.com.cnfic.contractmanage.user.excel;

import lombok.Data;

/**
 * @desc 检查通过可以入库的用户信息
 * @auther yangchuan
 * @date 2021/4/1
 */
@Data
public class SuccessUserInfo extends  UserImportTemplate{
    /**
     * 客户类型
     */
    private Integer userType;
    /**
     * 性别
     */
    private Integer userSex;
    /**
     * 客户经理的Id
     */
    private String custManager;
    /**
     * 客户Id
     */
    private Long custId;
}

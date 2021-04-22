package cn.com.cnfic.contractmanage.user.excel;

/**
 * @desc 用户导入导出相关常量
 * @auther yangchuan
 * @date 2021/4/1
 */
public class UserExcelExportConstans {
    /**
     * 用户导入模板文件
     */
    public static final String EXPORT_USER_TEMPLATE_FILE="用户导入模板";
    /**
     * 导出验证失败数据文件名称
     */
    public static final String EXPORT_USER_FAIL_FILE="导出验证失败数据";
    /**
     * 用户类型字典code
     */
    public static final String DIC_USER_TYPE_CODE="user_type";
    /**
     * 性别字典code
     */
    public static final String DIC_SEX_CODE="sex";
    /**
     * 用户导入生成的sheet名称
     */
    public static final String EXPORT_CUSTOMER_SHEET1_NAME="sheet1";
    /**
     * excel 检查结果提示key
     */
    public static final String CHECK_RESULT="msg";

    /**
     * excel 检查结果提示信息
     */
    public static final String ERROR_NOT_MANAGER="未查询到客户经理";
    /**
     * excel 检查结果提示信息
     */
    public static final String ERROR_NOT_CUST="未查询到客户信息";

    /**
     * 客户经理Id
     */
    public static final String MANAGER_ID="managerId";
    /**
     * 客户ID
     */
    public static final String CUST_ID="custId";

    /**
     * 用户类型数据字典值
     */
    public static final String USER_TYPE_VALUE="userType";
    /**
     * 性别类型数据字典值
     */
    public static final String USER_SEX_VALUE="userSex";
    /**
     * excel 检查结果提示信息
     */
    public static final String ERROR_NOT_USER_TYPE="未查询到用户类型";
    /**
     * 错误行号提示信息
     */
    public static final String LINE_NUMBER_ERROR="错误行号:";

    /**
     * 用户导入成功key
     */
    public static final String EXCEL_PASS_REDIS_KEY_PREFIX = "userImport:sus:";
    /**
     * 用户导入失败key
     */
    public static final String EXCEL_FAIL_REDIS_KEY_PREFIX = "userImport:fail:";

    /**
     * 过期时间 单位秒
     */
    public static final int EXCEL_REDIS_EXPIRE_TIME = 2000;

    /**
     * 用户导出文件名称
     */
    public static final String EXPORT_USER_FILE="用户导出文件";

    /**
     * 导出个人注册用户数据列
     */

    public static final String EXPORT_PERSONAL_USER_COLMUN="userNo,userPhone,userNickname,userEmail,userName,userBirthday,registChannel,originCreateTime,originUpdateTime,updateUserName";
    /**
     * 导出个人注册用户文件名称
     */
    public static final String EXPORT_PERSONAL_USER_FILE_NAME="个人注册用户导出";
}

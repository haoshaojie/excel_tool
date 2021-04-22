package cn.com.cnfic.contractmanage.customer.excel.importAnalysis;

/**
 * @desc 导出excel相关常量
 * @auther yangchuan
 * @date 2021/3/26
 */
public class CheckConstants {
    /**
     * 比对失败错误提示信息
     */
    public static final String CUST_TYPE_ERROR="客户类型比对失败,请选择模板下拉选项";
    public static final String CUST_STATE_ERROR="客户状态比对失败,请选择模板下拉选项";
    public static final String CUST_BUSINESS_CHANCE_ERROR="业务机会状态比对失败,请选择模板下拉选项";
    public static final String CUST_SUPER_UNIT_NAME_ERROR="上级单位比对失败,请选择模板下拉选项";
    /**
     * 检查结果msg
     */
    public static final String CHECK_RESULT="msg";
    /**
     * 行政区划级别
     */
    public static final Integer REGION_LEVEL=2;
    /**
     * 行业信息验证
     */
    public static final String CUST_ERROR="行业信息比对失败,请参照行业字典录入";
    public static final String REGION_ERROR="城市比对失败";
    /**
     * 行号错误提示
     */
    public static final String LINE_NUMBER_ERROR="错误行号:";
    /**
     * 客户导入成功key
     */
    public static final String EXCEL_PASS_REDIS_KEY_PREFIX = "CheckUserInfoListener:sus:";
    /**
     * 客户导入失败key
     */
    public static final String EXCEL_FAIL_REDIS_KEY_PREFIX = "CheckUserInfoListener:fail:";
    /**
     * 过期时间 单位秒
     */
    public static final int EXCEL_REDIS_EXPIRE_TIME = 2000;
    /**
     * 提交数据验证提示信息
     */
    public static final String REDIS_DATA_ERROR_MSG = "上传验证通过的数据已不存在，请重新上传";

    /**
     * 名称检查是否重复
     */
    public static final String CHECK_NAME_REPEAT="repeat";

    /**
     * 名称检查重复提示
     */
    public static final String CHECK_NAME_REPEAT_NAME="客户名称已存在";

    /**
     * 客户联系人相关导入常量
     */
    public static final String LINKMAN_ORG_FIELD="custOrg";
    public static final String LINKMAN_ORG_ERROR_MSG="所属机构比对失败,请选择所属机构下拉列表";
    public static final String LINKMAN_CUSTNAME_CODE_ERROR_MSG="根据客户名称及客户编码未匹配到客户";
    /**
     * 联系人导入成功key
     */
    public static final String EXCEL_CONTRACT_PASS_REDIS_KEY_PREFIX = "contractInfoListener:sus:";
    /**
     * 联系人导入失败key
     */
    public static final String EXCEL_CONTRACT_FAIL_REDIS_KEY_PREFIX = "contractListener:fail:";
    /**
     * 客户管理字段,客户类型
     */
    public static final String ORG_FEILD_CUSTTYPE = "custType";
    public static final String ORG_FEILD_CUSTSTATE = "custState";
    public static final String ORG_FEILD_BUSINESSCHANCE = "businessChance";
    public static final String ORG_FEILD_SUPERUNIT = "superUnit";
    public static final String ORG_FEILD_CUSTINDUSTRY= "custIndustry";




}

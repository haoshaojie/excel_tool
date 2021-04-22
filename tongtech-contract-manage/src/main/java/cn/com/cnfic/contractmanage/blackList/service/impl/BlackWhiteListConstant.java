package cn.com.cnfic.contractmanage.blackList.service.impl;

/**
 * @Description 黑白名单常量参数
 * @Author misterbig
 * @Date 2021/3/17
 */
final class BlackWhiteListConstant {

    /**
     * 黑白名单新增和更新后的审核状态初始值
     * {@value}
     */
    static final String DEFAULT_AUDIT_VALUE = "审批中";

    /**
     * 黑白名单审核状态候选项
     * {@value}
     */
    static final String[] AUDIT_STATUS = {"审批通过", "审批不通过"};

    private BlackWhiteListConstant() {
    }
}

package cn.com.cnfic.contractmanage.contract.constant;

/**
 * @ClassName ContractConstant
 * @Description 合约管理常量管理
 * @Author Jack
 * @Date 2021/3/9 15:17
 * @Version 1.0
 */
public class ContractConstant {

    /**
     * 用户协议状态(有效)
     */
    public static final Integer AGREEMENT_STATE_EFFECTIVE = 1;

    /**
     * 用户协议状态(无效)
     */
    public static final Integer AGREEMENT_STATE_INVALID = 2;

    /**
     * 资讯商品
     */

    public static final Integer TYPE_INFO = 1;
    /**
     * 研报商品
     */
    public static final Integer TYPE_REPORT = 2;
    /**
     * 商品正常状态
     */
    public static final Integer GOODS_STATE_NORMAL = 1;
    /**
     * 商品异常状态
     */
    public static final Integer GOODS_STATE_ABNORMAL = 2;
    /**
     * 合约来源-合同同步
     */
    public static final Integer CONTRACT_SOURCE_NC = 1;
    /**
     * 合约来源-手动创建
     */
    public static final Integer CONTRACT_SOURCE_CREATE = 2;
    /**
     * 合约编号编码
     */
    public static final String CONTRACT_NO_CODE="contractNo";
    /**
     * 合约编号前缀
     */
    public static final String CONTRACT_NO_FIX="HY";
    /**
     * 合约编号长度
     */
    public static final Integer CONTRACT_NO_LENGTH = 5;
    /**
     * 合约已完成创建
     */
    public static final Integer CONTRACT_CREATED_YES = 0;
    /**
     * 合约未完成创建
     */
    public static final Integer CONTRACT_CREATED_NO = 1;
}

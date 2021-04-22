// 数据字典常量维护文件-- 进行系统内用到的常量数据维护
// 在需要获取字典值的地方直接调用 this.getDicValue(this.CONSTANT.PRODUCT_STATE)
const PRODUCT_STATE = "product_state"
const PRODUCT_RANGE = "product_range"
const BLACKLIST_TYPE = "blackList_type"
const AGREEMENT_TYPE = "agreement_type"
const AGREEMENT_STATE = "agreement_state"
const PROP_TYPE = "prop_type"
const IS_REQUIRED = "is_required"
const SHOW_TYPE = "show_type"
const CURRENCY_TYPE = "currency_type"
const ACTIVITY_TYPE = "activity_type"
const CUST_STATE = "cust_state"
const CUST_TYPE = "cust_type"
const BUSINESS_CHANCE = "business_chance"
const SINGLE_FILE_SIZE = 10;
const GOODS_TYPE = "goods_type";
const USER_TYPE = "user_type";
const SEX = "sex";
const CONTRACT_SCOPE = "contract_scope";
const CONTRACT_TYPE = "contract_type";
const CONTRACT_STATE = "contract_state";
const SIGN_STATE = "sign_state";
const SIGN_TYPE = "sign_type";
const RENEWAL_STATE = "renewal_state";
const REGIST_CHANNEL = "regist_channel";
const GOODS_TYPE_VALUE = {
  TYPE_INFO: 1,//资讯商品
  TYPE_REPORT: 2 //研报商品
};
const GOODS_STATE_VALUE = {
  STATE_NORMAL: 1,//正常状态
  STATE_ABNORMAL: 2//异常状态
};
const PRODUCT_TYPE = 'product_type';
const REGAPP_CHANNEL_TYPE = "regAppChannelType";
const ACC_STATUS = "accStatus";
const APP_CODE = "app_code";
const MAIL_SEND_STATE = "MailRecord_mailSendState";
const MAIL_TEMP_STATE = "MailTemplate_tempState";

export default{
    PRODUCT_STATE,
    PRODUCT_RANGE,
    AGREEMENT_TYPE,
    AGREEMENT_STATE,
    BLACKLIST_TYPE,
    PROP_TYPE,
    IS_REQUIRED,
    SHOW_TYPE,
    SINGLE_FILE_SIZE,
    PRODUCT_TYPE,
    CURRENCY_TYPE,
    ACTIVITY_TYPE,
    CUST_TYPE,
    CUST_STATE,
    BUSINESS_CHANCE,
    GOODS_TYPE,
    USER_TYPE,
    SEX,
    CONTRACT_SCOPE,
    CONTRACT_TYPE,
    CONTRACT_STATE,
    SIGN_STATE,
    SIGN_TYPE,
    RENEWAL_STATE,
    GOODS_TYPE_VALUE,
    GOODS_STATE_VALUE,
    REGAPP_CHANNEL_TYPE,
    ACC_STATUS,
    APP_CODE,
  REGIST_CHANNEL,
  MAIL_SEND_STATE,
  MAIL_TEMP_STATE,

}


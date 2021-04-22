package cn.com.cnfic.contractmanage.blackList.excel;

/**
 * @Description 黑白名单excel常量配置
 * @Author misterbig
 * @Date 2021/3/16
 */
final class ExcelConstant {

    /**
     * 内容行高度
     * {@value}
     */
    static final int CONTENT_ROW_HEIGHT = 20;
    /**
     * 标题行高度
     * {@value}
     */
    static final int HEAD_ROW_HEIGHT = 30;
    /**
     * 列宽度
     * {@value}
     */
    static final int COLUMN_WIDTH = 25;
    /**
     * 导入模版黑白名单类型下拉选择初始化开始行
     * {@value}
     */
    static final int INIT_TYPE_START_ROW = 3;
    /**
     * 导入模版黑白名单类型下拉选择初始化结束行
     * {@value}
     */
    static final int INIT_TYPE_END_ROW = 1000;
    /**
     * 导入模版黑白名单类型下拉选择初始化列
     * {@value}
     */
    static final int INIT_TYPE_CELL_INDEX = 2;
    /**
     * 黑白名单导入，默认每隔3000条存储数据库
     * {@value}
     */
    static final int BATCH_INSERT_COUNT = 3000;
    /**
     * 黑白名单导入redis缓存默认过期时间，单位秒
     * {@value}
     */
    static final int EXCEL_REDIS_EXPIRE_TIME = 1200;
    /**
     * 黑白名单excel导入数据验证通过的缓存key前缀
     * {@value}
     */
    static final String EXCEL_PASS_REDIS_KEY_PREFIX = "ImportListenerOfBlackWhiteList:pass:";
    /**
     * 黑白名单excel导入数据验证失败的缓存key前缀
     * {@value}
     */
    static final String EXCEL_FAIL_REDIS_KEY_PREFIX = "ImportListenerOfBlackWhiteList:fail:";

    private ExcelConstant() {
    }

}

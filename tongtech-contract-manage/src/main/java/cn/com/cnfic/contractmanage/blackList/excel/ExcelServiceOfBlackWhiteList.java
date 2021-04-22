package cn.com.cnfic.contractmanage.blackList.excel;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description 黑白名单excel服务
 * @Author misterbig
 * @Date 2021/3/17
 */
public interface ExcelServiceOfBlackWhiteList {

    /**
     * 黑白名单导入验证失败的数据下载
     *
     * @param response
     */
    void exportCheckFailData(HttpServletResponse response, String failRedisKey);

    /**
     * 黑白名单导入模板下载
     *
     * @param response
     */
    void exportTemplate(HttpServletResponse response);

    /**
     * 黑白名单导出
     *
     * @param response
     * @param blackWhiteList
     */
    void exportBlackWhiteList(HttpServletResponse response, BlackWhiteList blackWhiteList);

    /**
     * 提交黑白名单导入验证通过的数据
     *
     * @param passCacheKey
     * @return
     */
    R submitPassData(String passCacheKey);

    /**
     * 黑白名单导入验证通过的数据
     *
     * @param passRedisKey
     * @param query
     * @return
     */
    R pageOfCheckPassData(String passRedisKey, Query query);

    /**
     * 黑白名单导入验证失败的数据
     *
     * @param failRedisKey
     * @param query
     * @return
     */
    R pageOfCheckFailData(String failRedisKey, Query query);

    /**
     * 黑白名单导入
     *
     * @param file
     * @return
     */
    R importBlackWhiteList(MultipartFile file);
}

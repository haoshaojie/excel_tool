package cn.com.cnfic.contractmanage.blackList.service;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.excel.BlackWhiteListExport;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;

import java.util.List;

/**
 * 黑白名单表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
public interface IBlackWhiteListService extends IService<BlackWhiteList> {

    /**
     * 根据条件查询黑白名单导出数据
     *
     * @return
     */
    List<BlackWhiteListExport> selectExport(BlackWhiteList blackWhiteList);

    /**
     * 黑白名单审核列表分页查询
     *
     * @param blackWhiteList
     * @param query
     * @return
     */
    R<IPage<BlackWhiteList>> page(BlackWhiteList blackWhiteList, Query query);

    /**
     * 黑白名单详情
     *
     * @param blackWhiteList
     * @return
     */
    R<BlackWhiteListVO> getBlackWhiteList(BlackWhiteList blackWhiteList);

    /**
     * 新增或者更新黑白名单
     *
     * @param blackWhiteList
     * @return
     */
    R submit(BlackWhiteList blackWhiteList);

    /**
     * 初始化黑白名单数据基础信息
     *
     * @param blackWhiteList
     * @return
     */
    String initBlackWhiteListData(BlackWhiteList blackWhiteList);

    /**
     * 检查黑白名单参数
     *
     * @param blackWhiteList
     * @return
     */
    String checkBlackWhiteListParams(BlackWhiteList blackWhiteList);

}

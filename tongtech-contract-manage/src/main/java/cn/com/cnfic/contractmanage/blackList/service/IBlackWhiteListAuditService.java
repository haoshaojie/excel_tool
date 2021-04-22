package cn.com.cnfic.contractmanage.blackList.service;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;


/**
 * 黑白名单表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
public interface IBlackWhiteListAuditService extends IService<BlackWhiteList> {

    /**
     * 选择客户
     *
     * @param blackWhiteList
     * @return
     */
    R chooseCustomer(BlackWhiteList blackWhiteList);

    /**
     * 黑白名单审核列表分页查询
     *
     * @param blackWhiteListVO
     * @param query
     * @return
     */
    R<IPage<BlackWhiteListVO>> page(BlackWhiteListVO blackWhiteListVO, Query query);

    /**
     * 黑白名单审核
     *
     * @param blackWhiteListVO
     * @return
     */
    R audit(BlackWhiteListVO blackWhiteListVO);

    /**
     * 黑白名单详情
     *
     * @param blackWhiteList
     * @return
     */
    R<BlackWhiteListVO> getBlackWhiteList(BlackWhiteList blackWhiteList);
}

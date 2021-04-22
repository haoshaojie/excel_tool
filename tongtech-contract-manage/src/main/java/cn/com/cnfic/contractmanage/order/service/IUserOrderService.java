package cn.com.cnfic.contractmanage.order.service;

import cn.com.cnfic.contractmanage.order.dto.UserOrderDTO;
import cn.com.cnfic.contractmanage.order.entity.UserOrder;
import cn.com.cnfic.contractmanage.order.vo.UserOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单表 服务类
 * @date 2021-04-07
 */
public interface IUserOrderService extends BaseService<UserOrder> {

    /**
     * 自定义分页
     *
     * @param page
     * @param userOrder
     * @return
     */
    IPage<UserOrderVO> selectUserOrderPage(IPage<UserOrderVO> page, UserOrderDTO userOrder);

    /**
     * 根据id查询用户订单详情
     *
     * @param id
     * @return
     */
    UserOrderVO getDetail(Long id);

}

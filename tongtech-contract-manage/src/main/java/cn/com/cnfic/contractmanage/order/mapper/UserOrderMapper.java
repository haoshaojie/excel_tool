package cn.com.cnfic.contractmanage.order.mapper;

import cn.com.cnfic.contractmanage.order.dto.UserOrderDTO;
import cn.com.cnfic.contractmanage.order.entity.UserOrder;
import cn.com.cnfic.contractmanage.order.vo.UserOrderVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单表 Mapper 接口
 * @date 2021-04-07
 */
@Repository
public interface UserOrderMapper extends BaseMapper<UserOrder> {

    /**
     * 自定义分页
     *
     * @param page
     * @param userOrder
     * @return
     */
    List<UserOrder> selectUserOrderPage(IPage page, UserOrderDTO userOrder);

    /**
     * 查询用户订单详情
     *
     * @param id
     * @return
     */
    UserOrderVO selectUserOrderDetail(@Param(value = "id") Long id);

    /**
     * 导出数据
     *
     * @param userOrder
     * @return
     */
    List<UserOrderVO> selectUserOrder(UserOrderDTO userOrder);
}

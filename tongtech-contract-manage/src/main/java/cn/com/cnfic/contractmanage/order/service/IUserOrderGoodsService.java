package cn.com.cnfic.contractmanage.order.service;

import cn.com.cnfic.contractmanage.order.dto.UserOrderDTO;
import cn.com.cnfic.contractmanage.order.dto.UserOrderGoodsDTO;
import cn.com.cnfic.contractmanage.order.entity.UserOrderGoods;
import cn.com.cnfic.contractmanage.order.vo.UserOrderGoodsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单商品表 服务类
 * @date 2021-04-07
 */
public interface IUserOrderGoodsService extends BaseService<UserOrderGoods> {

    /**
     * 自定义分页
     *
     * @param page
     * @param userOrderGoods
     * @return
     */
    IPage<UserOrderGoods> selectUserOrderGoodsPage(IPage<UserOrderGoods> page, UserOrderGoodsDTO userOrderGoods);

    /**
     * 为用户账号提供查询导出数据
     *
     * @param userOrderGoodsList
     * @return
     */
    List<UserOrderGoodsVO> selectUserOrderGoods(List<UserOrderGoodsDTO> userOrderGoodsList);

    /**
     * 查询用户订单导出数据
     *
     * @param userOrder
     * @return
     */
    List<UserOrderGoodsVO> selectUserOrderExcel(UserOrderDTO userOrder);
}

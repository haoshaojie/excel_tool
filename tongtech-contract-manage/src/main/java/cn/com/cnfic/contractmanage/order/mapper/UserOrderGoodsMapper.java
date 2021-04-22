package cn.com.cnfic.contractmanage.order.mapper;

import cn.com.cnfic.contractmanage.order.dto.UserOrderDTO;
import cn.com.cnfic.contractmanage.order.dto.UserOrderGoodsDTO;
import cn.com.cnfic.contractmanage.order.entity.UserOrderGoods;
import cn.com.cnfic.contractmanage.order.vo.UserOrderGoodsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单商品表 Mapper 接口
 * @date 2021-04-07
 */
@Repository
public interface UserOrderGoodsMapper extends BaseMapper<UserOrderGoods> {

    /**
     * 自定义分页
     *
     * @param page
     * @param userOrderGoods
     * @return
     */
    List<UserOrderGoods> selectUserOrderGoodsPage(IPage page, UserOrderGoodsDTO userOrderGoods);

    /**
     * 为用户账号提供订单导出数据
     *
     * @param userOrderGoodsList
     * @return
     */
    List<UserOrderGoodsVO> selectUserOrderGoods(List<UserOrderGoodsDTO> userOrderGoodsList);

    /**
     * 订单导出数据查询
     *
     * @param userOrder
     * @return
     */
    List<UserOrderGoodsVO> selectUserOrderGoodsExcel(UserOrderDTO userOrder);
}

package cn.com.cnfic.contractmanage.order.wrapper;

import cn.com.cnfic.contractmanage.order.entity.UserOrderGoods;
import cn.com.cnfic.contractmanage.order.vo.UserOrderGoodsVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单商品表包装类, 返回视图层所需的字段
 * @date 2021-04-07
 */
public class UserOrderGoodsWrapper extends BaseEntityWrapper<UserOrderGoods, UserOrderGoodsVO> {

    public static UserOrderGoodsWrapper build() {
        return new UserOrderGoodsWrapper();
    }

    @Override
    public UserOrderGoodsVO entityVO(UserOrderGoods userOrderGoods) {
        UserOrderGoodsVO userOrderGoodsVO = BeanUtil.copy(userOrderGoods, UserOrderGoodsVO.class);

        return userOrderGoodsVO;
    }

}

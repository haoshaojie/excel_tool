package cn.com.cnfic.contractmanage.partner.wrapper;

import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import cn.com.cnfic.contractmanage.partner.entity.OrderProfit;
import cn.com.cnfic.contractmanage.partner.vo.OrderProfitVO;

/**
 * 订单分润信息表包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
public class OrderProfitWrapper extends BaseEntityWrapper<OrderProfit, OrderProfitVO>  {

    public static OrderProfitWrapper build() {
        return new OrderProfitWrapper();
    }

	@Override
	public OrderProfitVO entityVO(OrderProfit orderProfit) {
		OrderProfitVO orderProfitVO = BeanUtil.copy(orderProfit, OrderProfitVO.class);

		return orderProfitVO;
	}

}

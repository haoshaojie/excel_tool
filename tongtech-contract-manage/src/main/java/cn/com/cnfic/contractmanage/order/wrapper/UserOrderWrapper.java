package cn.com.cnfic.contractmanage.order.wrapper;

import cn.com.cnfic.contractmanage.order.entity.UserOrder;
import cn.com.cnfic.contractmanage.order.vo.UserOrderVO;
import cn.com.cnfic.system.user.feign.IUserClient;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单表包装类, 返回视图层所需的字段
 * @date 2021-04-07
 */
public class UserOrderWrapper extends BaseEntityWrapper<UserOrder, UserOrderVO> {

    public static UserOrderWrapper build() {
        return new UserOrderWrapper();
    }

    /**
     * 用户信息feign 客户端
     */
    private static IUserClient iUserClient;

    static {
        iUserClient = SpringUtil.getBean(IUserClient.class);
    }

    @Override
    public UserOrderVO entityVO(UserOrder userOrder) {
        UserOrderVO userOrderVO = BeanUtil.copy(userOrder, UserOrderVO.class);
        return userOrderVO;
    }

}

package cn.com.cnfic.contractmanage.order.service.impl;

import cn.com.cnfic.contractmanage.order.dto.UserOrderDTO;
import cn.com.cnfic.contractmanage.order.entity.UserOrder;
import cn.com.cnfic.contractmanage.order.mapper.UserOrderGoodsMapper;
import cn.com.cnfic.contractmanage.order.mapper.UserOrderMapper;
import cn.com.cnfic.contractmanage.order.service.IUserOrderService;
import cn.com.cnfic.contractmanage.order.vo.UserOrderVO;
import cn.com.cnfic.contractmanage.order.wrapper.UserOrderWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单表 服务实现类
 * @date 2021-04-07
 */
@Service
public class UserOrderServiceImpl extends BaseServiceImpl<UserOrderMapper, UserOrder> implements IUserOrderService {
    @Autowired
    private UserOrderMapper userOrderMapper;
    @Autowired
    private UserOrderGoodsMapper userOrderGoodsMapper;

    @Override
    public IPage<UserOrderVO> selectUserOrderPage(IPage<UserOrderVO> page, UserOrderDTO userOrder) {
        List<UserOrder> list = userOrderMapper.selectUserOrderPage(page, userOrder);
        list.stream().map(o -> {
            if (StringUtil.isNotBlank(o.getPhoneNumber())) {
                o.setPhoneNumber(o.getPhoneNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
            return o;
        }).collect(Collectors.toList());
        return page.setRecords(UserOrderWrapper.build().listVO(list));
    }

    @Override
    public UserOrderVO getDetail(Long id) {
        return userOrderMapper.selectUserOrderDetail(id);
    }

}

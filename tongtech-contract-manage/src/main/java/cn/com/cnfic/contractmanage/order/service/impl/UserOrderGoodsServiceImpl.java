package cn.com.cnfic.contractmanage.order.service.impl;

import cn.com.cnfic.contractmanage.order.dto.UserOrderDTO;
import cn.com.cnfic.contractmanage.order.dto.UserOrderGoodsDTO;
import cn.com.cnfic.contractmanage.order.entity.UserOrderGoods;
import cn.com.cnfic.contractmanage.order.mapper.UserOrderGoodsMapper;
import cn.com.cnfic.contractmanage.order.service.IUserOrderGoodsService;
import cn.com.cnfic.contractmanage.order.vo.UserOrderGoodsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cnfic-UserManage
 * @desc 用户订单商品表 服务实现类
 * @date 2021-04-07
 */
@Service
public class UserOrderGoodsServiceImpl extends BaseServiceImpl<UserOrderGoodsMapper, UserOrderGoods> implements IUserOrderGoodsService {
    /**
     * 订单商品mapper
     */
    @Autowired
    private UserOrderGoodsMapper userOrderGoodsMapper;

    @Override
    public IPage<UserOrderGoods> selectUserOrderGoodsPage(IPage<UserOrderGoods> page, UserOrderGoodsDTO userOrderGoods) {
        return page.setRecords(userOrderGoodsMapper.selectUserOrderGoodsPage(page, userOrderGoods));
    }

    @Override
    public List<UserOrderGoodsVO> selectUserOrderGoods(List<UserOrderGoodsDTO> userOrderGoodsList) {
        return userOrderGoodsMapper.selectUserOrderGoods(userOrderGoodsList);
    }

    @Override
    public List<UserOrderGoodsVO> selectUserOrderExcel(UserOrderDTO userOrder) {
        return userOrderGoodsMapper.selectUserOrderGoodsExcel(userOrder).stream().map(o -> {
            if (StringUtil.isNotBlank(o.getPhoneNumber())) {
                o.setPhoneNumber(o.getPhoneNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
            return o;
        }).collect(Collectors.toList());
    }

}

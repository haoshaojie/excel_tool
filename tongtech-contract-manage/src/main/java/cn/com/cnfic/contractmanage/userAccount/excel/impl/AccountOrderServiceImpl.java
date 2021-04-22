package cn.com.cnfic.contractmanage.userAccount.excel.impl;

import cn.com.cnfic.contractmanage.order.dto.UserOrderGoodsDTO;
import cn.com.cnfic.contractmanage.order.service.IUserOrderGoodsService;
import cn.com.cnfic.contractmanage.order.vo.UserOrderGoodsVO;
import cn.com.cnfic.contractmanage.userAccount.excel.AccountOrderDTO;
import cn.com.cnfic.contractmanage.userAccount.excel.AccountOrderExport;
import cn.com.cnfic.contractmanage.userAccount.excel.ExcelDataServiceOfAccount;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @Description 订单数据查询服务
 * @Author misterbig
 * @Date 2021/4/9
 */
@AllArgsConstructor
@Service
public class AccountOrderServiceImpl implements ExcelDataServiceOfAccount<AccountOrderExport, AccountOrderDTO> {

    private final IUserOrderGoodsService userOrderGoodsService;

    @Override
    public List<AccountOrderExport> selectData(AccountOrderDTO param) {
        List<UserOrderGoodsDTO> userOrderGoodsParam = param.getOrderParams().stream().map(o -> {
            UserOrderGoodsDTO filter = new UserOrderGoodsDTO();
            filter.setPhoneNumber(o.getUserPhone());
            filter.setMailbox(o.getUserEmail());
            return filter;
        }).collect(toList());
        List<UserOrderGoodsVO> userOrderGoods = userOrderGoodsService.selectUserOrderGoods(userOrderGoodsParam);
        if (!CollectionUtils.isEmpty(userOrderGoods)) {
            Map<String, List<UserOrderGoodsVO>> orderMap = userOrderGoods.stream().collect(groupingBy(o -> o.getPhoneNumber() + o.getMailbox()));
            List<AccountOrderExport> result = Lists.newArrayList();
            param.getOrderParams().forEach(p -> {
                List<UserOrderGoodsVO> orders = orderMap.get(p.getUserPhone() + p.getUserEmail());
                if (!CollectionUtils.isEmpty(orders)) {
                    result.addAll(orders.stream().map(o -> {
                        AccountOrderExport orderExport = new AccountOrderExport();
                        orderExport.setUserName(p.getUserName());
                        orderExport.setUserPhone(p.getUserPhone());
                        orderExport.setUserEmail(p.getUserEmail());
                        orderExport.setOrderNo(o.getOrderNo());
                        orderExport.setOrderTime(o.getOrderTime());
                        orderExport.setGoodsName(o.getGoodsName());
                        orderExport.setExpiredDate(o.getExpiredDate());
                        return orderExport;
                    }).collect(toList()));
                }
            });
            return result;
        }
        return Collections.emptyList();
    }
}

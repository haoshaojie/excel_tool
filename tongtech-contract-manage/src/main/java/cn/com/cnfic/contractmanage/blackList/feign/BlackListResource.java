package cn.com.cnfic.contractmanage.blackList.feign;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.service.IBlackWhiteListService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;

/**
 * @Description 合约管理-黑白名单feign实现
 * @Author misterbig
 * @Date 2021/4/9
 */
@ApiIgnore
@AllArgsConstructor
@RestController
public class BlackListResource implements BlackListClient {

    private final IBlackWhiteListService blackWhiteListService;

    @Override
    public List<BlackWhiteList> listByCustId(String custId) {
        if (StringUtils.isBlank(custId)) {
            return Collections.emptyList();
        }
        return blackWhiteListService.list(Wrappers.lambdaUpdate(BlackWhiteList.class)
                .eq(BlackWhiteList::getCustId, custId)
                .eq(BlackWhiteList::getIsDeleted, NumberUtils.INTEGER_ZERO));
    }

    @Override
    public List<BlackWhiteList> listByCustCode(String custCode) {
        if (StringUtils.isBlank(custCode)) {
            return Collections.emptyList();
        }
        return blackWhiteListService.list(Wrappers.lambdaUpdate(BlackWhiteList.class)
                .eq(BlackWhiteList::getCustCode, custCode)
                .eq(BlackWhiteList::getIsDeleted, NumberUtils.INTEGER_ZERO));
    }
}

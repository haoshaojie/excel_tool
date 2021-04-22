package cn.com.cnfic.contractmanage.userAccount.service.impl;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.accountmanage.feign.AccountFeign;
import cn.com.cnfic.contractmanage.userAccount.service.LogoffAccountService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Service;

/**
 * @Description 账号注销申请服务中转
 * @Author misterbig
 * @Date 2021/3/31
 */
@AllArgsConstructor
@Service
public class LogoffAccountServiceImpl implements LogoffAccountService {

    private final AccountFeign accountFeign;


    @Override
    public R logoff(String accId) {
        return accountFeign.logoutAccForTreaty(new AccInfoForTreaty() {{
            setAccId(Long.valueOf(accId));
        }});
    }

    @Override
    public R<Page<AccInfoForTreaty>> page(AccInfoForTreaty accInfoForTreaty) {
        return accountFeign.logoutListForTreaty(accInfoForTreaty);
    }
}

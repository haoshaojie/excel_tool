package cn.com.cnfic.contractmanage.userAccount.service.impl;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.accountmanage.feign.AccountFeign;
import cn.com.cnfic.contractmanage.userAccount.service.PersonalAccountService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Service;

/**
 * @Description 个人账号服务中转中心
 * @Author misterbig
 * @Date 2021/3/31
 */
@AllArgsConstructor
@Service
public class PersonalAccountServiceImpl implements PersonalAccountService {

    private final AccountFeign accountFeign;

    @Override
    public R<AccInfoForTreaty> detail(String accId) {
        return accountFeign.accountDetails(new AccInfoForTreaty() {{
            setAccId(Long.valueOf(accId));
        }});
    }

    @Override
    public R<Page<AccInfoForTreaty>> page(AccInfoForTreaty accInfoForTreaty) {
        return accountFeign.queryAccForTreaty(accInfoForTreaty);
    }
}

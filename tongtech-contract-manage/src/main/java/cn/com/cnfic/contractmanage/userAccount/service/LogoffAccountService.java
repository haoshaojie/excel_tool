package cn.com.cnfic.contractmanage.userAccount.service;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springblade.core.tool.api.R;

/**
 * @Description 注销账号申请服务中转中心
 * @Author misterbig
 * @Date 2021/3/31
 */
public interface LogoffAccountService {
    /**
     * 账号注销
     *
     * @param accId
     * @return
     */
    R logoff(String accId);

    /**
     * 分页查询
     *
     * @param accInfoForTreaty
     * @return
     */
    R<Page<AccInfoForTreaty>> page(AccInfoForTreaty accInfoForTreaty);
}

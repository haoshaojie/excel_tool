package cn.com.cnfic.contractmanage.userAccount.service;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springblade.core.tool.api.R;

/**
 * @Description 个人账号服务中转中心
 * @Author misterbig
 * @Date 2021/3/31
 */
public interface PersonalAccountService {

    /**
     * 个人账号详情
     *
     * @param accId
     * @return
     */
    R<AccInfoForTreaty> detail(String accId);

    /**
     * 个人账号分页查询
     *
     * @param accInfoForTreaty
     * @return
     */
    R<Page<AccInfoForTreaty>> page(AccInfoForTreaty accInfoForTreaty);

}

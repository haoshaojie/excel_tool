package cn.com.cnfic.contractmanage.userAccount.service;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.contractmanage.userAccount.model.ResetLockDTO;
import cn.com.cnfic.contractmanage.userAccount.model.ResetPwdDTO;
import org.springblade.core.tool.api.R;

/**
 * @Description 账号服务中转中心
 * @Author misterbig
 * @Date 2021/3/31
 */
public interface AccountService {

    /**
     * 通过手机号重置账号密码
     *
     * @param resetPwd
     * @return
     */
    R resetPassword(ResetPwdDTO resetPwd);

    /**
     * 账号锁定
     *
     * @param resetLock
     * @return
     */
    R resetlock(ResetLockDTO resetLock);

    /**
     * 账号修改
     *
     * @param accInfoForTreaty
     * @return
     */
    R submit(AccInfoForTreaty accInfoForTreaty);
}

package cn.com.cnfic.contractmanage.userAccount.service.impl;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.accountmanage.feign.AccountFeign;
import cn.com.cnfic.contractmanage.userAccount.model.ResetLockDTO;
import cn.com.cnfic.contractmanage.userAccount.model.ResetPwdDTO;
import cn.com.cnfic.contractmanage.userAccount.service.AccountService;
import cn.com.cnfic.system.entity.Param;
import cn.com.cnfic.system.feign.ISysParamClient;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 账号服务中转中心
 * @Author misterbig
 * @Date 2021/3/31
 */
@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    /**
     * 账号初始密码键
     * {@value}
     */
    private static final String ACCOUNT_INIT_PASSWORD_KEY = "account.initPassword";
    /**
     * 账号初始密码系统参数获取失败消息
     * {@value}
     */
    private static final String ACCOUNT_KEY_ERROR_MSG = "从系统参数中，未获取到账号初始密码的配置";

    private final AccountFeign accountFeign;
    private final ISysParamClient sysParamClient;

    @Override
    public R resetPassword(ResetPwdDTO resetPwd) {
        Param param = sysParamClient.getByKey(ACCOUNT_INIT_PASSWORD_KEY);
        if (Objects.isNull(param)) {
            return R.fail(ACCOUNT_KEY_ERROR_MSG);
        }
        // TODO: 2021/4/7 实现密码重置邮件和短消息通知
        if (resetPwd.getEmail()) {
            //发送邮件通知
            CompletableFuture.runAsync(() -> {

            });
        }
        if (resetPwd.getPhone()) {
            //发送短信通知
            CompletableFuture.runAsync(() -> {

            });
        }
        return accountFeign.resetPwdForTreaty(new AccInfoForTreaty() {{
            setAccId(Long.valueOf(resetPwd.getAccId()));
            setResetPassword(param.getParamValue());
        }});
    }

    @Override
    public R resetlock(ResetLockDTO resetLock) {
        if (resetLock.getFlag()) {
            //锁定
            return accountFeign.lockUserForTreaty(new AccInfoForTreaty() {{
                setAccId(Long.valueOf(resetLock.getAccId()));
            }});
        } else {
            //解锁
            return accountFeign.unlockUserForTreaty(new AccInfoForTreaty() {{
                setAccId(Long.valueOf(resetLock.getAccId()));
            }});
        }
    }

    @Override
    public R submit(AccInfoForTreaty accInfoForTreaty) {
        return accountFeign.modifyAccForTreaty(accInfoForTreaty);
    }
}

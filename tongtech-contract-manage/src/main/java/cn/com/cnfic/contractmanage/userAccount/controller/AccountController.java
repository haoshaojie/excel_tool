package cn.com.cnfic.contractmanage.userAccount.controller;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.contractmanage.userAccount.model.ResetLockDTO;
import cn.com.cnfic.contractmanage.userAccount.model.ResetPwdDTO;
import cn.com.cnfic.contractmanage.userAccount.service.AccountService;
import cn.com.cnfic.contractmanage.userAccount.service.impl.AccountServiceImpl;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description 账号管理控制器
 * @Author misterbig
 * @Date 2021/3/25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/account")
@Api(value = "账号管理", tags = "账号管理公共接口")
public class AccountController {

    private final AccountService accountService;

    /**
     * 账号编辑
     * <p>
     * 涉及手机号码、邮箱、用户状态允许修改
     * </p>
     *
     * @see AccountServiceImpl#submit(AccInfoForTreaty)
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "账号编辑", notes = "传入AccInfoForTreaty")
    public R update(@RequestBody AccInfoForTreaty accInfoForTreaty) {
        return accountService.submit(accInfoForTreaty);
    }

    /**
     * 账号锁定/解锁
     *
     * @see AccountServiceImpl#resetlock(ResetLockDTO)
     */
    @PostMapping("/resetlock")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "账号锁定/解锁", notes = "传入ResetLockDTO")
    public R resetlock(@Valid @RequestBody ResetLockDTO resetLock) {
        return accountService.resetlock(resetLock);
    }

    /**
     * 账号密码重置
     *
     * @see AccountServiceImpl#resetPassword(ResetPwdDTO)
     */
    @PostMapping("/resetpassword")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "账号密码重置", notes = "传入ResetPwdDTO")
    public R resetPassword(@Valid @RequestBody ResetPwdDTO resetPwd) {
        return accountService.resetPassword(resetPwd);
    }

}

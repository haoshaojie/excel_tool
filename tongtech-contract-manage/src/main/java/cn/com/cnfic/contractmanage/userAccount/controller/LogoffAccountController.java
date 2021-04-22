package cn.com.cnfic.contractmanage.userAccount.controller;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.accountmanage.entity.AccountParam;
import cn.com.cnfic.contractmanage.userAccount.service.LogoffAccountService;
import cn.com.cnfic.contractmanage.userAccount.service.impl.LogoffAccountServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 注销账号申请
 * @Author misterbig
 * @Date 2021/3/31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/logoffaccount")
@Api(value = "注销账号申请", tags = "注销账号申请接口")
public class LogoffAccountController {

    private final LogoffAccountService logoffAccountService;

    /**
     * 分页 注销账号申请分页查询
     *
     * @see LogoffAccountServiceImpl#page(AccInfoForTreaty)
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "分页", notes = "传入AccInfoForTreaty")
    public R<Page<AccInfoForTreaty>> list(AccInfoForTreaty accInfoForTreaty) {
        return logoffAccountService.page(accInfoForTreaty);
    }

    /**
     * 用户未到期产品列表
     */
    @GetMapping("/productofunexpired")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "用户未到期产品列表")
    public R productOfUnexpired(AccountParam accountParam) {
        return null;
    }

    /**
     * 注销账号
     *
     * @see LogoffAccountServiceImpl#logoff(String)
     */
    @GetMapping("/logoff")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "注销账号")
    public R logoff(@RequestParam(value = "accId") String accId) {
        return logoffAccountService.logoff(accId);
    }
}

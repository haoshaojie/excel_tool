package cn.com.cnfic.contractmanage.userAccount.controller;

import cn.com.cnfic.accountmanage.entity.AccountParam;
import cn.com.cnfic.contractmanage.userAccount.model.AccountExtraVO;
import cn.com.cnfic.contractmanage.userAccount.service.EnterpriseAccountService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author misterbig
 * @Date 2021/3/25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/enterpriseaccount")
@Api(value = "企业账号管理", tags = "企业账号管理接口")
public class EnterpriseAccountController {

    private final EnterpriseAccountService enterpriseAccountService;

    /**
     * 企业账号-用户账号分页查询
     */
    @GetMapping("/listuser")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "用户账号分页查询", notes = "传入AccountInfo")
    public R<Object> listUser(AccountParam accountParam, Query query) {
        return enterpriseAccountService.listUser(accountParam, query);
    }

    /**
     * 企业账号-用户账号详情
     */
    @GetMapping("/userdetail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "用户账号详情", notes = "传入AccountInfo")
    public R<Object> userDetail(AccountParam accountParam, Query query) {
        return enterpriseAccountService.userDetail(accountParam);
    }

    /**
     * 企业账号-用户账号详情-合约信息
     */
    @GetMapping("/userdetail/contract")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "用户账号详情", notes = "传入AccountInfo")
    public R<AccountExtraVO<Object>> userDetailContract(AccountParam accountParam, Query query) {
        return enterpriseAccountService.userDetailContract(accountParam);
    }

    /**
     * 企业账号-用户账号详情-产品权限
     */
    @GetMapping("/userdetail/product")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "用户账号详情-产品权限", notes = "传入AccountInfo")
    public R<AccountExtraVO<Object>> userDetailProduct(AccountParam accountParam, Query query) {
        return enterpriseAccountService.userDetailProduct(accountParam);
    }

    /**
     * 企业账号-用户账号详情-产品权限-合约信息
     */
    @GetMapping("/userdetail/product/contract")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "用户账号详情-产品权限-合约信息", notes = "传入AccountInfo")
    public R<AccountExtraVO<Object>> userProductContract(AccountParam accountParam, Query query) {
        return enterpriseAccountService.userProductContract(accountParam);
    }

    /**
     * 企业账号-用户账号启用/禁用
     */
    @GetMapping("/resetable")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "用户账号启用/禁用", notes = "传入AccountInfo")
    public R<Object> resetAble(AccountParam accountParam, Query query) {
        return null;
    }

    /**
     * 企业账号-用户账号导出
     */
    @GetMapping("/export-enterpriseaccount")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "用户账号导出")
    public void exportEnterpriseAccount(Object object, HttpServletResponse response) {

    }

    /**
     * 企业账号-用户账号欢迎邮件
     */
    @PostMapping("/welcome")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "用户账号导出")
    public R welcomeEmail(@RequestParam String accIds) {
        return enterpriseAccountService.welcomeEmail(accIds);
    }

    /**
     * 企业账号-公共账号分页查询
     */
    @GetMapping("/listpublic")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "公共账号分页查询", notes = "传入AccountInfo")
    public R<Object> listPublic(AccountParam accountParam, Query query) {
        return null;
    }

    /**
     * 企业账号-公共账号详情
     */
    @GetMapping("/publicdetail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "公共账号详情", notes = "传入AccountInfo")
    public R<Object> publicDetail(AccountParam accountParam, Query query) {
        return null;
    }

    /**
     * 企业账号-公共账号新增/编辑
     */
    @GetMapping("/publicsubmit")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "公共账号新增/编辑", notes = "传入AccountInfo")
    public R<Object> publicSubmit(AccountParam accountParam, Query query) {
        return null;
    }

    /**
     * 企业账号-公共账号批量删除
     */
    @GetMapping("/publicremove")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "公共账号批量删除", notes = "传入AccountInfo")
    public R<Object> publicRemove(AccountParam accountParam, Query query) {
        return null;
    }
}

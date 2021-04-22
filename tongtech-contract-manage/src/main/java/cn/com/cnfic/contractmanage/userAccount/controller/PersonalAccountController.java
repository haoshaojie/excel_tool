package cn.com.cnfic.contractmanage.userAccount.controller;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.contractmanage.userAccount.excel.AccountExportDTO;
import cn.com.cnfic.contractmanage.userAccount.excel.ExcelServiceOfUserAccount;
import cn.com.cnfic.contractmanage.userAccount.excel.impl.ExcelServiceImplOfUserAccount;
import cn.com.cnfic.contractmanage.userAccount.service.PersonalAccountService;
import cn.com.cnfic.contractmanage.userAccount.service.impl.PersonalAccountServiceImpl;
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

import javax.servlet.http.HttpServletResponse;

/**
 * @Description 个人账号管理
 * @Author misterbig
 * @Date 2021/3/25
 */
@AllArgsConstructor
@RestController
@RequestMapping("/personalaccount")
@Api(value = "个人账号管理", tags = "个人账号管理接口")
public class PersonalAccountController {

    private final PersonalAccountService personalAccountService;
    private final ExcelServiceOfUserAccount excelServiceOfUserAccount;

    /**
     * 分页 个人账号分页查询
     *
     * @see PersonalAccountServiceImpl#page(AccInfoForTreaty)
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "分页", notes = "传入AccInfoForTreaty")
    public R<Page<AccInfoForTreaty>> list(AccInfoForTreaty accInfoForTreaty) {
        return personalAccountService.page(accInfoForTreaty);
    }

    /**
     * 导出个人账号
     *
     * @see ExcelServiceImplOfUserAccount#exportPersonalAccount(AccountExportDTO, HttpServletResponse)
     */
    @GetMapping("/export-personalaccount")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "导出个人账号", notes = "传入AccountExportDTO")
    public void exportPersonalAccount(AccountExportDTO accountExportDTO, HttpServletResponse response) {
        excelServiceOfUserAccount.exportPersonalAccount(accountExportDTO, response);
    }

    /**
     * 个人账号详情
     *
     * @see PersonalAccountServiceImpl#detail(String)
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "个人账号详情", notes = "传入accId")
    public R<AccInfoForTreaty> detail(@RequestParam(value = "accId") String accId) {
        return personalAccountService.detail(accId);
    }
}

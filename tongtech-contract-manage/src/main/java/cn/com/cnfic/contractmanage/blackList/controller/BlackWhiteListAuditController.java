package cn.com.cnfic.contractmanage.blackList.controller;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.service.IBlackWhiteListAuditService;
import cn.com.cnfic.contractmanage.blackList.service.impl.BlackWhiteListAuditServiceImpl;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

/**
 * 黑白名单审核 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/blackwhitelistaudit")
@Api(value = "黑白名单审核", tags = "黑白名单审核接口")
public class BlackWhiteListAuditController extends BladeController {

    private IBlackWhiteListAuditService blackWhiteListAuditService;

    /**
     * 分页 黑白名单表
     *
     * @see BlackWhiteListAuditServiceImpl#page(BlackWhiteListVO, Query)
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "分页", notes = "传入blackWhiteList")
    public R<IPage<BlackWhiteListVO>> list(BlackWhiteListVO blackWhiteListVO, Query query) {
        return blackWhiteListAuditService.page(blackWhiteListVO, query);
    }

    /**
     * 详情
     *
     * @see BlackWhiteListAuditServiceImpl#getBlackWhiteList(BlackWhiteList)
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "详情", notes = "传入blackWhiteList")
    public R<BlackWhiteListVO> detail(BlackWhiteList blackWhiteList) {
        return blackWhiteListAuditService.getBlackWhiteList(blackWhiteList);
    }

    /**
     * 审核黑白名单
     *
     * @see BlackWhiteListAuditServiceImpl#audit(BlackWhiteListVO)
     */
    @PostMapping("/audit")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "审核", notes = "传入blackWhiteListVO")
    public R audit(@RequestBody BlackWhiteListVO blackWhiteListVO) {
        return blackWhiteListAuditService.audit(blackWhiteListVO);
    }

    /**
     * 选择客户
     *
     * @see BlackWhiteListAuditServiceImpl#chooseCustomer(BlackWhiteList)
     */
    @PostMapping("/choosecustomer")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "选择客户", notes = "传入blackWhiteList")
    public R chooseCustomer(@RequestBody BlackWhiteList blackWhiteList) {
        return blackWhiteListAuditService.chooseCustomer(blackWhiteList);
    }

}

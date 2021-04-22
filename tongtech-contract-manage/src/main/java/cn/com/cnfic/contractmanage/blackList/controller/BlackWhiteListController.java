package cn.com.cnfic.contractmanage.blackList.controller;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.excel.ExcelServiceOfBlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.excel.impl.ExcelServiceImplOfBlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.service.IBlackWhiteListService;
import cn.com.cnfic.contractmanage.blackList.service.impl.BlackWhiteListServiceImpl;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import cn.com.cnfic.contractmanage.blackList.wrapper.BlackWhiteListWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

/**
 * 黑白名单管理 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/blackwhitelist")
@Api(value = "黑白名单表", tags = "黑白名单表接口")
public class BlackWhiteListController extends BladeController {

    private final IBlackWhiteListService blackWhiteListService;

    private final ExcelServiceOfBlackWhiteList excelServiceOfBlackWhiteList;

    /**
     * 分页 黑白名单表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "分页", notes = "传入blackWhiteList")
    public R<IPage<BlackWhiteList>> list(BlackWhiteList blackWhiteList, Query query) {
        return blackWhiteListService.page(blackWhiteList, query);
    }

    /**
     * 详情
     *
     * @see BlackWhiteListServiceImpl#getBlackWhiteList(BlackWhiteList)
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "详情", notes = "传入blackWhiteList")
    public R<BlackWhiteListVO> detail(BlackWhiteList blackWhiteList) {
        return blackWhiteListService.getBlackWhiteList(blackWhiteList);
    }

    /**
     * 新增 黑白名单表
     *
     * @see BlackWhiteListServiceImpl#submit(BlackWhiteList)
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "新增", notes = "传入blackWhiteList")
    public R save(@Valid @RequestBody BlackWhiteList blackWhiteList) {
        return blackWhiteListService.submit(blackWhiteList);
    }

    /**
     * 修改 黑白名单表
     *
     * @see BlackWhiteListServiceImpl#submit(BlackWhiteList)
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "修改", notes = "传入blackWhiteList")
    public R update(@Valid @RequestBody BlackWhiteListVO blackWhiteListVO) {
        return blackWhiteListService.submit(blackWhiteListVO);
    }

    /**
     * 删除 黑白名单表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(blackWhiteListService.removeByIds(Func.toLongList(ids)));
    }

    /**
     * 导出黑白名单
     *
     * @param blackWhiteList
     * @param response
     * @see ExcelServiceImplOfBlackWhiteList#exportBlackWhiteList(HttpServletResponse, BlackWhiteList)
     */
    @GetMapping("/export-blackwhitelist")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "导出黑白名单")
    public void exportBlackWhiteList(BlackWhiteList blackWhiteList, HttpServletResponse response) {
        excelServiceOfBlackWhiteList.exportBlackWhiteList(response, blackWhiteList);
    }

    /**
     * 导出模板
     *
     * @see ExcelServiceImplOfBlackWhiteList#exportTemplate(HttpServletResponse)
     */
    @GetMapping("export-template")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "导出模板")
    public void exportTemplate(HttpServletResponse response) {
        excelServiceOfBlackWhiteList.exportTemplate(response);
    }

    /**
     * 数据导入
     *
     * @see ExcelServiceImplOfBlackWhiteList#importBlackWhiteList(MultipartFile)
     */
    @PostMapping("import-blackwhitelist")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "数据导入")
    public R importBlackWhiteList(MultipartFile file) {
        return excelServiceOfBlackWhiteList.importBlackWhiteList(file);
    }

    /**
     * 提交验证通过的数据
     *
     * @see ExcelServiceImplOfBlackWhiteList#submitPassData(String)
     */
    @GetMapping("submitpassdata")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "提交验证通过的数据")
    public R submitPassData(@RequestParam(value = "passRedisKey") String passRedisKey) {
        return excelServiceOfBlackWhiteList.submitPassData(passRedisKey);
    }

    /**
     * 导入验证成功的缓存数据
     *
     * @see ExcelServiceImplOfBlackWhiteList#pageOfCheckPassData(String, Query)
     */
    @GetMapping("checkpassdata")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "导入验证成功的缓存数据，分页")
    public R checkPassData(@RequestParam(value = "passRedisKey") String passRedisKey, Query query) {
        return excelServiceOfBlackWhiteList.pageOfCheckPassData(passRedisKey, query);
    }

    /**
     * 导入验证失败的缓存数据
     *
     * @see ExcelServiceImplOfBlackWhiteList#pageOfCheckFailData(String, Query)
     */
    @GetMapping("checkfaildata")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "导入验证失败的缓存数据，分页")
    public R checkFailData(@RequestParam(value = "failRedisKey") String failRedisKey, Query query) {
        return excelServiceOfBlackWhiteList.pageOfCheckFailData(failRedisKey, query);
    }

    /**
     * 导出黑白名单导入验证失败的数据
     *
     * @see ExcelServiceImplOfBlackWhiteList#exportTemplate(HttpServletResponse)
     */
    @GetMapping("export-checkfaildata")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "导出模板")
    public void exportCheckFailData(HttpServletResponse response, @RequestParam(value = "failRedisKey") String failRedisKey) {
        excelServiceOfBlackWhiteList.exportCheckFailData(response, failRedisKey);
    }
}

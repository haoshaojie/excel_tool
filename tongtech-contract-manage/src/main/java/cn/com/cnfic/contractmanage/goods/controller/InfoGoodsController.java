package cn.com.cnfic.contractmanage.goods.controller;

import cn.com.cnfic.contractmanage.contract.constant.ContractConstant;
import cn.com.cnfic.contractmanage.goods.dto.InfoGoodsDTO;
import cn.com.cnfic.contractmanage.goods.dto.ReportGoodsDTO;
import cn.com.cnfic.contractmanage.goods.entity.InfoGoods;
import cn.com.cnfic.contractmanage.goods.entity.ReportGoods;
import cn.com.cnfic.contractmanage.goods.service.IInfoGoodsService;
import cn.com.cnfic.contractmanage.goods.service.IReportGoodsService;
import cn.com.cnfic.contractmanage.goods.vo.InfoGoodsVO;
import cn.com.cnfic.contractmanage.goods.wrapper.InfoGoodsWrapper;
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

import javax.validation.Valid;
import java.util.List;

/**
 * 资讯商品表 控制器
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/infogoods")
@Api(value = "资讯商品表", tags = "资讯商品表接口")
public class InfoGoodsController extends BladeController {

    private IInfoGoodsService infoGoodsService;
    private IReportGoodsService iReportGoodsService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入infoGoods")
    public R<InfoGoodsVO> detail(@Valid InfoGoodsDTO infoGoods) {
        InfoGoods detail = new InfoGoods();
        Integer type = infoGoods.getType();
        if (ContractConstant.TYPE_INFO.equals(type)) {
            detail = infoGoodsService.getOne(Condition.getQueryWrapper(infoGoods));
        } else if (ContractConstant.TYPE_REPORT.equals(type)) {
            ReportGoodsDTO reportGoodsDTO = Func.copy(infoGoods, ReportGoodsDTO.class);
            ReportGoods reportGoods = iReportGoodsService.getOne(Condition.getQueryWrapper(reportGoodsDTO));
            detail = Func.copy(reportGoods, InfoGoods.class);
        }
        InfoGoodsVO infoGoodsVO = InfoGoodsWrapper.build().entityVO(detail);
        infoGoodsVO.setType(infoGoods.getType());
        return R.data(infoGoodsVO);
    }

    /**
     * 自定义分页 资讯商品表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入infoGoods")
    public R<IPage<InfoGoodsVO>> page(InfoGoodsDTO infoGoods, Query query) {
        IPage<InfoGoods> pages = infoGoodsService.selectInfoGoodsPage(query, infoGoods);
        return R.data(InfoGoodsWrapper.build().pageVO(pages));
    }

    /**
     * 新增 资讯商品表
     */
//    @PostMapping("/save")
//    @ApiOperationSupport(order = 4)
//    @ApiOperation(value = "新增", notes = "传入infoGoods")
//    public R save(@Valid @RequestBody InfoGoods infoGoods) {
//        return R.status(infoGoodsService.save(infoGoods));
//    }

    /**
     * 修改 资讯商品表
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "修改", notes = "传入infoGoods")
    public R update(@ApiParam(value = "主键集合", required = true) @RequestParam String infoIds,
                    @ApiParam(value = "主键集合", required = true) @RequestParam String reportIds,
                    @ApiParam(value = "处理意见", required = true) @RequestParam String disposeSuggest) {
        return R.status(infoGoodsService.updateById(Func.toLongList(infoIds), Func.toLongList(reportIds), disposeSuggest));
    }

    /**
     * 删除 资讯商品表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "逻辑删除", notes = "分类型传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String infoIds,
                    @ApiParam(value = "主键集合", required = true) @RequestParam String reportIds) {
        return R.status(infoGoodsService.deleteLogic(Func.toLongList(infoIds), Func.toLongList(reportIds)));
    }

    /**
     * 导出单篇商品
     */
    @GetMapping("/exportData")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "导出单篇商品数据查询", notes = "传入infoGoods")
    public R<List<InfoGoodsVO>> exportData(InfoGoodsDTO infoGoods) {
        List<InfoGoods> infoGoodsList = infoGoodsService.selectInfoGoodsExport(infoGoods);
        return R.data(InfoGoodsWrapper.build().listVO(infoGoodsList));
    }
}

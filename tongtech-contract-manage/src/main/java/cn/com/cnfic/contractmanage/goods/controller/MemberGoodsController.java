package cn.com.cnfic.contractmanage.goods.controller;

import cn.com.cnfic.contractmanage.goods.dto.MemberGoodsDTO;
import cn.com.cnfic.contractmanage.goods.entity.MemberGoods;
import cn.com.cnfic.contractmanage.goods.service.IMemberGoodsService;
import cn.com.cnfic.contractmanage.goods.vo.MemberGoodsVO;
import cn.com.cnfic.contractmanage.goods.wrapper.MemberGoodsWrapper;
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

import java.util.List;

/**
 * 会员商品表 控制器
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/membergoods")
@Api(value = "会员商品表", tags = "会员商品表接口")
public class MemberGoodsController extends BladeController {

    private IMemberGoodsService memberGoodsService;


    /**
     * 自定义分页 会员商品表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "分页", notes = "传入memberGoods")
    public R<IPage<MemberGoodsVO>> page(MemberGoodsDTO memberGoods, Query query) {
        IPage<MemberGoods> pages = memberGoodsService.selectMemberGoodsPage(Condition.getPage(query), memberGoods);
        return R.data(MemberGoodsWrapper.build().pageVO(pages));
    }

    /**
     * 会员商品导出列表数据
     */
    @GetMapping("/exportData")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "导出数据", notes = "传入memberGoods")
    public R<List<MemberGoodsVO>> exportData(MemberGoodsDTO memberGoods) {
        return R.data(MemberGoodsWrapper.build().listVO(memberGoodsService.exportData(memberGoods)));
    }

    /**
     * 删除 会员商品表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "逻辑删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(memberGoodsService.deleteLogic(Func.toLongList(ids)));
    }


}

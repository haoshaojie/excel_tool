package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.contractmanage.product.dto.CategoryDTO;
import cn.com.cnfic.contractmanage.product.entity.Category;
import cn.com.cnfic.contractmanage.product.service.ICategoryService;
import cn.com.cnfic.contractmanage.product.vo.CategoryVO;
import cn.com.cnfic.contractmanage.product.wrapper.CategoryWrapper;
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
 * 产品类型表 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/category")
@Api(value = "产品类型表", tags = "产品类型表接口")
public class CategoryController extends BladeController {

    private ICategoryService categoryService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入category")
    public R<CategoryVO> detail(Category category) {
        Category detail = categoryService.getOne(Condition.getQueryWrapper(category));
        return R.data(CategoryWrapper.build().entityVO(detail));
    }

    /**
     * 自定义分页 产品类型表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入category")
    public R<IPage<CategoryVO>> page(CategoryDTO category, Query query) {
        IPage<Category> pages = categoryService.selectCategoryPage(query, category);
        return R.data(CategoryWrapper.build().pageVO(pages));
    }

    /**
     * 新增 产品类型表
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "新增", notes = "传入category")
    public R save(@Valid @RequestBody CategoryDTO category) {
        int count = categoryService.selectCategoryByCodeOrName(category);
        if (count > 0) {
            return R.fail("产品类型名称或产品类型编码已经存在");
        }
        return R.status(categoryService.save(category));
    }

    /**
     * 修改 产品类型表
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "修改", notes = "传入category")
    public R update(@Valid @RequestBody CategoryDTO category) {
        int count = categoryService.selectCategoryByCodeOrName(category);
        if (count > 0) {
            return R.fail("产品类型名称或产品类型编码已经存在");
        }
        return R.status(categoryService.updateById(category));
    }


    /**
     * 删除 产品类型表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "逻辑删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(categoryService.deleteLogic(Func.toLongList(ids)));
    }

    @PostMapping("/selectItems")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "下拉框列表", notes = "下拉框列表")
    public R<List<Category>> selectItems() {
        List<Category> categorys = categoryService.list();
        return R.data(categorys);
    }


}

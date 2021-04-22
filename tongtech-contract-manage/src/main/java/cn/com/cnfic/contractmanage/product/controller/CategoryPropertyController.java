package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.contractmanage.product.dto.PropertyDTO;
import cn.com.cnfic.contractmanage.product.entity.CategoryProperty;
import cn.com.cnfic.contractmanage.product.service.ICategoryPropertyService;
import cn.com.cnfic.contractmanage.product.service.IPropertyService;
import cn.com.cnfic.contractmanage.product.vo.CategoryPropertyVO;
import cn.com.cnfic.contractmanage.product.vo.PropertyVO;
import cn.com.cnfic.contractmanage.product.wrapper.CategoryPropertyWrapper;
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
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品类型关联属性字典 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/categoryproperty")
@Api(value = "产品类型关联属性字典", tags = "产品类型关联属性字典接口")
public class CategoryPropertyController extends BladeController {

    private ICategoryPropertyService categoryPropertyService;
    private IPropertyService propertyService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入categoryProperty")
    public R<CategoryPropertyVO> detail(CategoryProperty categoryProperty) {
        CategoryProperty detail = categoryPropertyService.getOne(Condition.getQueryWrapper(categoryProperty));
        return R.data(CategoryPropertyWrapper.build().entityVO(detail));
    }

    /**
     * 分页查询产品类型包含的产品属性
     */
    @GetMapping("/propertyPageInCategory")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页产品类型包含的产品属性", notes = "传入property")
    public R<IPage<PropertyVO>> propertyPageInCategory(PropertyDTO property, Query query) {
        IPage<PropertyVO> pages = propertyService.selectPropertyPageInCategory(query, property);
        return R.data(pages);
    }

    /**
     * 产品类型未包含的产品属性
     */
    @GetMapping("/propertyNotInCategory")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "产品类型未包含的产品属性", notes = "传入property")
    public R<List<PropertyVO>> propertyNotInCategory(PropertyDTO property) {
        List<PropertyVO> data = propertyService.selectPropertyNotInCategory(property);
        return R.data(data);
    }

    /**
     * 新增或修改 产品类型关联属性字典
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增或修改产品类型关联属性", notes = "传入产品类型主键和属性主键集合")
    public R submit(@ApiParam(value = "产品属性主键集合", required = true) @RequestParam String propIds,
                    @ApiParam(value = "产品类型主键", required = true) @RequestParam String cateId) {
        if (StringUtil.isBlank(propIds) || StringUtil.isBlank(cateId)) {
            return R.fail("产品类型或产品属性不能为空");
        }
        return R.status(categoryPropertyService.saveOrUpdate(Func.toLongList(propIds), Func.toLong(cateId)));
    }


    /**
     * 删除 产品类型关联属性字典
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "逻辑删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(categoryPropertyService.deleteLogic(Func.toLongList(ids)));
    }


}

package cn.com.cnfic.contractmanage.product.controller;

import cn.com.cnfic.contractmanage.product.dto.PropertyDTO;
import cn.com.cnfic.contractmanage.product.entity.Property;
import cn.com.cnfic.contractmanage.product.service.IPropertyService;
import cn.com.cnfic.contractmanage.product.vo.PropertyVO;
import cn.com.cnfic.contractmanage.product.wrapper.PropertyWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 产品属性字典表 控制器
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/property")
@Api(value = "产品属性字典表", tags = "产品属性字典表接口")
public class PropertyController extends BladeController {

    private IPropertyService propertyService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入property")
    public R<PropertyVO> detail(long id) {
        PropertyVO detail = propertyService.getDetailById(id);
        return R.data(detail);
    }

    /**
     * 自定义分页 产品属性字典表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入property")
    public R<IPage<PropertyVO>> page(PropertyDTO property, Query query) {
        IPage<PropertyVO> pages = propertyService.selectPropertyPage(query, property);
        return R.data(pages);
    }

    /**
     * 新增 产品属性字典表
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "新增", notes = "传入property")
    public R save(@Valid @RequestBody PropertyDTO property) {
        int numberProperty = propertyService.selectPropertyByCodeOrName(property);
        if (numberProperty > 0) {
            return R.fail("属性编码或属性名称已经存在");
        }
        return R.status(propertyService.save(property));
    }

    /**
     * 修改 产品属性字典表
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "修改", notes = "传入property")
    public R update(@Valid @RequestBody PropertyDTO property) {
        int numberProperty = propertyService.selectPropertyByCodeOrName(property);
        if (numberProperty > 0) {
            return R.fail("属性编码或属性名称已经存在");
        }
        return R.status(propertyService.updateById(property));
    }


    /**
     * 删除 产品属性字典表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "逻辑删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(propertyService.deleteLogic(Func.toLongList(ids)));
    }


    /**
     * 产品类型未包含的产品属性
     */
    @GetMapping("/getListByCate")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "产品类型关联产品属性列表", notes = "传入property")
    public R<List<PropertyVO>> getListByCate(PropertyDTO property) {
        List<Property> list = propertyService.getListByCate(property);
        return R.data(PropertyWrapper.build().listVO(list));
    }
}

package cn.com.cnfic.contractmanage.user.controller;

import cn.com.cnfic.contractmanage.user.entity.PersonalUser;
import cn.com.cnfic.contractmanage.user.service.IPersonalUserService;
import cn.com.cnfic.contractmanage.user.vo.PersonalUserVO;
import cn.com.cnfic.contractmanage.user.wrapper.PersonalUserWrapper;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author Cnfic-UserManage
 * @desc 个人用户表 控制器
 * @date 2021-04-01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/personaluser")
@Api(value = "个人用户表", tags = "个人用户表接口")
public class PersonalUserController extends BladeController {

    private IPersonalUserService personalUserService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入personalUser")
    public R<PersonalUserVO> detail(PersonalUser personalUser) {
        PersonalUser detail = personalUserService.getOne(Condition.getQueryWrapper(personalUser));
        if (Objects.nonNull(detail)) {
            return R.data(PersonalUserWrapper.build().entityVO(detail));
        }
        return R.fail("数据不存在");
    }

    /**
     * 分页 个人用户表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入personalUser")
    public R<IPage<PersonalUserVO>> list(PersonalUser personalUser, Query query) {
        IPage<PersonalUser> pages = personalUserService.page(Condition.getPage(query), Condition.getQueryWrapper(personalUser));
        return R.data(PersonalUserWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 个人用户表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入personalUser")
    public R<IPage<PersonalUserVO>> page(PersonalUserVO personalUser, Query query) {
        IPage<PersonalUserVO> pages = personalUserService.selectPersonalUserPage(Condition.getPage(query), personalUser);
        return R.data(pages);
    }

    /**
     * 新增 个人用户表
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入personalUser")
    public R save(@Valid @RequestBody PersonalUser personalUser) {
        return R.status(personalUserService.save(personalUser));
    }

    /**
     * 修改 个人用户表
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入personalUser")
    public R update(@Valid @RequestBody PersonalUser personalUser) {
        return R.status(personalUserService.updateById(personalUser));
    }

    /**
     * 新增或修改 个人用户表
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入personalUser")
    public R submit(@Valid @RequestBody PersonalUser personalUser) {
        return R.status(personalUserService.saveOrUpdate(personalUser));
    }


    /**
     * 删除 个人用户表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "逻辑删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(personalUserService.deleteLogic(Func.toLongList(ids)));
    }

    @GetMapping("/export")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "个人注册用户导出")
    public void exportPersonalUser(HttpServletResponse response, PersonalUserVO personalUser) {
        personalUserService.exportPersonalUser(response, personalUser);
    }

}

package cn.com.cnfic.contractmanage.login.controller;

import cn.com.cnfic.contractmanage.login.service.IUpdatePasswordService;
import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.vo.OrgUserVO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 判断密码是否需要修改
 * @auther yangchuan
 * @date 2021/4/15
 */
@RestController
@RequestMapping("/password")
@Api(value = "密码修改验证", tags = "密码修改")
@AllArgsConstructor
public class UpdatePasswordController {
    private IUpdatePasswordService updatePasswordService;

    @GetMapping("/queryPassword")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入orgUser")
    public R queryPasswordById(Long userId) {
        return updatePasswordService.queryPasswordById(userId);
    }
}

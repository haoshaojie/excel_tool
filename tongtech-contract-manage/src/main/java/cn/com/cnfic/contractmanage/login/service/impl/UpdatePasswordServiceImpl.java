package cn.com.cnfic.contractmanage.login.service.impl;

import cn.com.cnfic.contractmanage.login.service.IUpdatePasswordService;
import cn.com.cnfic.system.user.entity.User;
import cn.com.cnfic.system.user.entity.UserInfo;
import cn.com.cnfic.system.user.feign.IUserClient;
import cn.hutool.http.HttpStatus;
import org.apache.commons.lang3.ObjectUtils;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @desc
 * @auther yangchuan
 * @date 2021/4/15
 */
@Service
public class UpdatePasswordServiceImpl implements IUpdatePasswordService {
    @Autowired
    private IUserClient userClient;
    @Override
    public R queryPasswordById(Long userId) {
        List<User> data = userClient.listUserByIds(String.valueOf(userId));
        //判断是否修改过密码
        if (ObjectUtils.isNotEmpty(data)){
            Date validDate = data.get(0).getValidDate();
            //未修改过密码
            if (ObjectUtils.isEmpty(validDate)){
                return R.data(HttpStatus.HTTP_PROXY_AUTH,data,"首次登录请修改密码");
            }
        }
        return null;
    }
}

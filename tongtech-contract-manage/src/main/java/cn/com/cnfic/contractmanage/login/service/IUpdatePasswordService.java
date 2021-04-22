package cn.com.cnfic.contractmanage.login.service;

import org.springblade.core.tool.api.R;

/**
 * @desc
 * @auther yangchuan
 * @date 2021/4/15
 */
public interface IUpdatePasswordService {
    /**
     *  判断密码是否需要修改
     * @param userId
     * @return
     */
    R queryPasswordById(Long userId);
}

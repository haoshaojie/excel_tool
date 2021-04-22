package cn.com.cnfic.contractmanage.common.wrapper;

import cn.com.cnfic.system.user.entity.User;
import cn.com.cnfic.system.user.feign.IUserClient;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * @Auther: wxy
 * @Date: 2021-03-31
 * @Description: 公共类用户id转换姓名
 */
public class CommonWrapper {
    /**
     * 用户信息feign 客户端
     */
    private static IUserClient iUserClient;
    /**
     * 获取创建用户id方法名
     */
    private static String GET_CTREATE_USER = "getCreateUser";
    /**
     * 获取更新用户id方法名
     */
    private static String GET_UPDATE_USER = "getUpdateUser";
    /**
     * 设置创建用户名方法名
     */
    private static String SET_CTREATE_USER_NAME = "setCreateUserName";
    /**
     * 设置更新用户名方法名
     */
    private static String SET_UPDATE_USER_NAME = "setUpdateUserName";

    static {
        iUserClient = SpringUtil.getBean(IUserClient.class);
    }

    /**
     * 用户id补充用户姓名
     *
     * @param t VO对象
     */
    public static void userIdConvertUserName(Object t) {
        try {
            Long createUserId = (Long) t.getClass().getMethod(GET_CTREATE_USER).invoke(t);
            if (Func.isNotEmpty(createUserId)) {
                User createUser = iUserClient.userInfo(createUserId).getData().getUser();
                t.getClass().getMethod(SET_CTREATE_USER_NAME, String.class).invoke(t, createUser.getRealName());
            }
            Long updateUserId = (Long) t.getClass().getMethod(GET_CTREATE_USER).invoke(t);
            if (Func.isNotEmpty(updateUserId)) {
                User updateUser = iUserClient.userInfo(updateUserId).getData().getUser();
                t.getClass().getMethod(SET_UPDATE_USER_NAME, String.class).invoke(t, updateUser.getRealName());
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}

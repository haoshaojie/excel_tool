package cn.com.cnfic.contractmanage.blackList.wrapper;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import cn.com.cnfic.system.user.entity.User;
import cn.com.cnfic.system.user.feign.IUserClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 黑白名单表包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-10
 */
public class BlackWhiteListWrapper extends BaseEntityWrapper<BlackWhiteList, BlackWhiteListVO> {

    private static IUserClient userClient;

    static {
        userClient = SpringUtil.getBean(IUserClient.class);
    }


    public static BlackWhiteListWrapper build() {
        return new BlackWhiteListWrapper();
    }

    @Override
    public BlackWhiteListVO entityVO(BlackWhiteList blackWhiteList) {
        BlackWhiteListVO blackWhiteListVO = BeanUtil.copy(blackWhiteList, BlackWhiteListVO.class);
        Set<String> ids = Sets.newHashSet();
        this.getUserIds(ids, blackWhiteList);
        this.setCreateAndUpdateUserName(this.userNameMap(ids), blackWhiteListVO);
        return blackWhiteListVO;
    }

    /**
     * 设置创建人和修改人姓名
     *
     * @param userMap
     * @param blackWhiteListVO
     */
    private void setCreateAndUpdateUserName(Map<Long, String> userMap, BlackWhiteListVO blackWhiteListVO) {
        blackWhiteListVO.setCreateUserName(userMap.getOrDefault(blackWhiteListVO.getCreateUser(), StringUtils.EMPTY));
        blackWhiteListVO.setUpdateUserName(userMap.getOrDefault(blackWhiteListVO.getUpdateUser(), StringUtils.EMPTY));
    }

    /**
     * 调用feign接口查询用户信息
     *
     * @param ids
     * @param <T>
     * @return
     */
    private <T extends Collection<String>> Map<Long, String> userNameMap(T ids) {
        List<User> users = userClient.listUserByIds(String.join(",", ids));
        return users.stream().collect(toMap(User::getId, User::getRealName, (v1, v2) -> v2));
    }

    /**
     * 获取创建人和修改人ID
     *
     * @param ids
     * @param blackWhiteList
     */
    private void getUserIds(Set<String> ids, BlackWhiteList blackWhiteList) {
        if (Objects.nonNull(blackWhiteList.getCreateUser())) {
            ids.add(blackWhiteList.getCreateUser().toString());
        }
        if (Objects.nonNull(blackWhiteList.getUpdateUser())) {
            ids.add(blackWhiteList.getUpdateUser().toString());
        }
    }

    @Override
    public IPage<BlackWhiteListVO> pageVO(IPage<BlackWhiteList> pages) {
        Set<String> ids = Sets.newHashSet();
        List<BlackWhiteListVO> records = pages.getRecords().stream().map(b -> {
            this.getUserIds(ids, b);
            return BeanUtil.copy(b, BlackWhiteListVO.class);
        }).collect(toList());
        Map<Long, String> userNameMap = this.userNameMap(ids);
        records.forEach(r -> {
            this.setCreateAndUpdateUserName(userNameMap, r);
        });
        IPage<BlackWhiteListVO> pageVo = new Page(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(records);
        return pageVo;
    }
}

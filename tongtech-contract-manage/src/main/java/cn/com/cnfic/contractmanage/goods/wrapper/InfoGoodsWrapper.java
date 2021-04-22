package cn.com.cnfic.contractmanage.goods.wrapper;

import cn.com.cnfic.contractmanage.goods.entity.InfoGoods;
import cn.com.cnfic.contractmanage.goods.vo.InfoGoodsVO;
import cn.com.cnfic.system.user.entity.User;
import cn.com.cnfic.system.user.entity.UserInfo;
import cn.com.cnfic.system.user.feign.IUserClient;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 资讯商品表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
public class InfoGoodsWrapper extends BaseEntityWrapper<InfoGoods, InfoGoodsVO> {
    /**
     * 用户信息服务接口客户端
     */
    private static IUserClient iUserClient;

    /**
     *静态加载
     */
    static {
        iUserClient = SpringUtil.getBean(IUserClient.class);
    }

    public static InfoGoodsWrapper build() {
        return new InfoGoodsWrapper();
    }

    @Override
    public InfoGoodsVO entityVO(InfoGoods infoGoods) {
        InfoGoodsVO infoGoodsVO = BeanUtil.copy(infoGoods, InfoGoodsVO.class);
        if (Func.isNotEmpty(infoGoodsVO.getDisposeUser())) {
            R<UserInfo> userInfo = iUserClient.userInfo(infoGoodsVO.getDisposeUser());
            if (userInfo.getData() != null && userInfo.getData().getUser() != null) {
                User createUser = userInfo.getData().getUser();
                infoGoodsVO.setDisposeUserName(createUser.getRealName());
            }
        }
        return infoGoodsVO;
    }

}

package cn.com.cnfic.contractmanage.goods.wrapper;

import cn.com.cnfic.contractmanage.goods.entity.ReportGoods;
import cn.com.cnfic.contractmanage.goods.vo.ReportGoodsVO;
import cn.com.cnfic.system.user.entity.User;
import cn.com.cnfic.system.user.feign.IUserClient;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 研报商品表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
public class ReportGoodsWrapper extends BaseEntityWrapper<ReportGoods, ReportGoodsVO> {
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

    public static ReportGoodsWrapper build() {
        return new ReportGoodsWrapper();
    }

    @Override
    public ReportGoodsVO entityVO(ReportGoods reportGoods) {
        ReportGoodsVO reportGoodsVO = BeanUtil.copy(reportGoods, ReportGoodsVO.class);
        if (Func.isNotEmpty(reportGoodsVO.getDisposeUser())) {
            User createUser = iUserClient.userInfo(reportGoodsVO.getDisposeUser()).getData().getUser();
            reportGoodsVO.setDisposeUserName(createUser.getRealName());
        }
        return reportGoodsVO;
    }

}

package cn.com.cnfic.contractmanage.goods.service;

import cn.com.cnfic.contractmanage.goods.dto.MemberGoodsDTO;
import cn.com.cnfic.contractmanage.goods.entity.MemberGoods;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 会员商品表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
public interface IMemberGoodsService extends BaseService<MemberGoods> {

    /**
     * 自定义分页
     *
     * @param page
     * @param memberGoods
     * @return
     */
    IPage<MemberGoods> selectMemberGoodsPage(IPage<MemberGoods> page, MemberGoodsDTO memberGoods);

    /**
     * 导出数据
     *
     * @param memberGoods
     * @return
     */
    List<MemberGoods> exportData(MemberGoodsDTO memberGoods);
}

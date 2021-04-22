package cn.com.cnfic.contractmanage.goods.service;

import cn.com.cnfic.contractmanage.goods.dto.InfoGoodsDTO;
import cn.com.cnfic.contractmanage.goods.entity.InfoGoods;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 资讯商品表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
public interface IInfoGoodsService extends BaseService<InfoGoods> {

    /**
     * 自定义分页
     *
     * @param query
     * @param infoGoods
     * @return
     */
    IPage<InfoGoods> selectInfoGoodsPage(Query query, InfoGoodsDTO infoGoods);

    /**
     * 逻辑删除
     *
     * @param infoIds
     * @param reportIds
     * @return
     */
    Boolean deleteLogic(List<Long> infoIds, List<Long> reportIds);

    /**
     * 更新处理意见
     *
     * @param infoIds
     * @param reportIds
     * @param disposeSuggest
     * @return
     */
    Boolean updateById(List<Long> infoIds, List<Long> reportIds, String disposeSuggest);

    /**
     * 导出数据
     *
     * @param infoGoods
     * @return
     */
    List<InfoGoods> selectInfoGoodsExport(InfoGoodsDTO infoGoods);
}

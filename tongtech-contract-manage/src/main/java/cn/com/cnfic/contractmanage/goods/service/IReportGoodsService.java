package cn.com.cnfic.contractmanage.goods.service;

import cn.com.cnfic.contractmanage.goods.entity.ReportGoods;
import cn.com.cnfic.contractmanage.goods.vo.ReportGoodsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

/**
 * 研报商品表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
public interface IReportGoodsService extends BaseService<ReportGoods> {

    /**
     * 自定义分页
     *
     * @param page
     * @param reportGoods
     * @return
     */
    IPage<ReportGoodsVO> selectReportGoodsPage(IPage<ReportGoodsVO> page, ReportGoodsVO reportGoods);

}

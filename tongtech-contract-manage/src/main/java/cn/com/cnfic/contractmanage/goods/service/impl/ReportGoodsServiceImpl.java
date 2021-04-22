package cn.com.cnfic.contractmanage.goods.service.impl;

import cn.com.cnfic.contractmanage.goods.entity.ReportGoods;
import cn.com.cnfic.contractmanage.goods.mapper.ReportGoodsMapper;
import cn.com.cnfic.contractmanage.goods.service.IReportGoodsService;
import cn.com.cnfic.contractmanage.goods.vo.ReportGoodsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 研报商品表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
@Service
public class ReportGoodsServiceImpl extends BaseServiceImpl<ReportGoodsMapper, ReportGoods> implements IReportGoodsService {

    @Override
    public IPage<ReportGoodsVO> selectReportGoodsPage(IPage<ReportGoodsVO> page, ReportGoodsVO reportGoods) {
        return page.setRecords(baseMapper.selectReportGoodsPage(page, reportGoods));
    }

}

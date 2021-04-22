package cn.com.cnfic.contractmanage.goods.service.impl;

import cn.com.cnfic.contractmanage.contract.constant.ContractConstant;
import cn.com.cnfic.contractmanage.goods.dto.InfoGoodsDTO;
import cn.com.cnfic.contractmanage.goods.entity.InfoGoods;
import cn.com.cnfic.contractmanage.goods.entity.ReportGoods;
import cn.com.cnfic.contractmanage.goods.mapper.InfoGoodsMapper;
import cn.com.cnfic.contractmanage.goods.service.IInfoGoodsService;
import cn.com.cnfic.contractmanage.goods.service.IReportGoodsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资讯商品表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
@Service
public class InfoGoodsServiceImpl extends BaseServiceImpl<InfoGoodsMapper, InfoGoods> implements IInfoGoodsService {

    @Autowired
    private InfoGoodsMapper infoGoodsMapper;
    @Autowired
    private IReportGoodsService iReportGoodsService;

    @Override
    public IPage<InfoGoods> selectInfoGoodsPage(Query query, InfoGoodsDTO infoGoods) {
        IPage<InfoGoods> page = Condition.getPage(query);
//        分开查询效率高，联合查询好维护。后期可以根据情况调整
//        Integer type = infoGoods.getType();
//        if (type != null && type == TYPE_INFO) {
//            return page.setRecords(infoGoodsMapper.selectInfoGoodsPage(page, infoGoods));
//        } else if (type != null && type == TYPE_REPOT) {
//            return page.setRecords(infoGoodsMapper.selectReportGoodsPage(page, infoGoods));
//        }
        return page.setRecords(infoGoodsMapper.selectUnionGoodsPage(page, infoGoods));
    }

    @Override
    public Boolean deleteLogic(List<Long> infoIds, List<Long> reportIds) {
        Boolean infoResult = true;
        if (infoIds != null && infoIds.size() > 0) {
            infoResult = this.deleteLogic(infoIds);
        }
        Boolean reportResult = true;
        if (reportIds != null && reportIds.size() > 0) {
            reportResult = iReportGoodsService.deleteLogic(reportIds);
        }
        return (infoResult && reportResult);
    }

    @Override
    public Boolean updateById(List<Long> infoIds, List<Long> reportIds, String disposeSuggest) {
        Boolean infoResult = true;
        //资讯商品
        if (infoIds != null && infoIds.size() > 0) {
            List<InfoGoods> infoGoodsList = infoIds.stream().map(o -> {
                InfoGoods infoGoods = new InfoGoods();
                infoGoods.setId(o);
                infoGoods.setDisposeSuggest(disposeSuggest);
                BladeUser user = SecureUtil.getUser();
                if (user != null) {
                    infoGoods.setDisposeUser(user.getUserId());
                    infoGoods.setUpdateUser(user.getUserId());
                }

                LocalDateTime now = LocalDateTime.now();
                infoGoods.setDisposeTime(now);
                infoGoods.setUpdateTime(DateUtil.toDate(now));
                infoGoods.setIsDeleted(0);
                infoGoods.setState(ContractConstant.GOODS_STATE_NORMAL);
                return infoGoods;
            }).collect(Collectors.toList());
            infoResult = this.updateBatchById(infoGoodsList);
        }
        //研报商品
        Boolean reportResult = true;
        if (reportIds != null && reportIds.size() > 0) {
            List<ReportGoods> reportGoodsList = reportIds.stream().map(o -> {
                ReportGoods reportGoods = new ReportGoods();
                reportGoods.setId(o);
                reportGoods.setDisposeSuggest(disposeSuggest);
                BladeUser user = SecureUtil.getUser();
                if (user != null) {
                    reportGoods.setDisposeUser(user.getUserId());
                    reportGoods.setUpdateUser(user.getUserId());
                }

                LocalDateTime now = LocalDateTime.now();
                reportGoods.setDisposeTime(now);
                reportGoods.setUpdateTime(DateUtil.toDate(now));
                reportGoods.setIsDeleted(0);
                reportGoods.setState(ContractConstant.GOODS_STATE_NORMAL);
                return reportGoods;
            }).collect(Collectors.toList());
            reportResult = iReportGoodsService.updateBatchById(reportGoodsList);
        }
        return (infoResult && reportResult);
    }

    @Override
    public List<InfoGoods> selectInfoGoodsExport(InfoGoodsDTO infoGoods) {
        return infoGoodsMapper.selectUnionGoodsPage(null, infoGoods);
    }
}

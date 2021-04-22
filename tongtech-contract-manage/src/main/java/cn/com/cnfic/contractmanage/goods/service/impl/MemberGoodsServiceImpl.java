package cn.com.cnfic.contractmanage.goods.service.impl;

import cn.com.cnfic.contractmanage.goods.dto.MemberGoodsDTO;
import cn.com.cnfic.contractmanage.goods.entity.MemberGoods;
import cn.com.cnfic.contractmanage.goods.mapper.MemberGoodsMapper;
import cn.com.cnfic.contractmanage.goods.service.IMemberGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员商品表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
@Service
public class MemberGoodsServiceImpl extends BaseServiceImpl<MemberGoodsMapper, MemberGoods> implements IMemberGoodsService {

    @Override
    public IPage<MemberGoods> selectMemberGoodsPage(IPage<MemberGoods> page, MemberGoodsDTO memberGoods) {
        LambdaQueryWrapper<MemberGoods> queryWrapper = Wrappers.<MemberGoods>query().lambda();
        if (StringUtil.isNotBlank(memberGoods.getGoodsTitle())) {
            queryWrapper.like(MemberGoods::getGoodsTitle, memberGoods.getGoodsTitle());
        }
        if (StringUtil.isNotBlank(memberGoods.getSource())) {
            queryWrapper.like(MemberGoods::getSource, memberGoods.getSource());
        }
        if (StringUtil.isNotBlank(memberGoods.getSynTimeBegin())) {
            queryWrapper.ge(MemberGoods::getSynTime, memberGoods.getSynTimeBegin());
        }
        if (StringUtil.isNotBlank(memberGoods.getSynTimeEnd())) {
            queryWrapper.le(MemberGoods::getSynTime, memberGoods.getSynTimeEnd());
        }
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<MemberGoods> exportData(MemberGoodsDTO memberGoods) {
        LambdaQueryWrapper<MemberGoods> queryWrapper = Wrappers.<MemberGoods>query().lambda();
        if (StringUtil.isNotBlank(memberGoods.getGoodsTitle())) {
            queryWrapper.like(MemberGoods::getGoodsTitle, memberGoods.getGoodsTitle());
        }
        if (StringUtil.isNotBlank(memberGoods.getSource())) {
            queryWrapper.like(MemberGoods::getSource, memberGoods.getSource());
        }
        if (StringUtil.isNotBlank(memberGoods.getSynTimeBegin())) {
            queryWrapper.ge(MemberGoods::getSynTime, memberGoods.getSynTimeBegin());
        }
        if (StringUtil.isNotBlank(memberGoods.getSynTimeEnd())) {
            queryWrapper.le(MemberGoods::getSynTime, memberGoods.getSynTimeEnd());
        }
        return baseMapper.selectList(queryWrapper);
    }

}

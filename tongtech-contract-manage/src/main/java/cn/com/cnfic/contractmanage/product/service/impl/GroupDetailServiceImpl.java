package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.GroupDetail;
import cn.com.cnfic.contractmanage.product.mapper.GroupDetailMapper;
import cn.com.cnfic.contractmanage.product.mapper.GroupMapper;
import cn.com.cnfic.contractmanage.product.service.IGroupDetailService;
import cn.com.cnfic.contractmanage.product.vo.GroupDetailVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @desc 组合产品与产品/产品项关联表 服务实现类
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
@Service
@AllArgsConstructor
public class GroupDetailServiceImpl extends BaseServiceImpl<GroupDetailMapper, GroupDetail> implements IGroupDetailService {

	private GroupMapper groupMapper;
	@Override
	public IPage<GroupDetailVO> selectGroupDetailPage(IPage<GroupDetailVO> page, GroupDetailVO groupDetail) {
		return page.setRecords(baseMapper.selectGroupDetailPage(page, groupDetail));
	}

	@Override
	public int queryGroupByItem(Integer type, Long ...id) {
		QueryWrapper<GroupDetail> queryWrapper = Condition.getQueryWrapper(new GroupDetail());
		queryWrapper.eq("type", type);
		queryWrapper.in("item_id", id);
		List<GroupDetail> details= baseMapper.selectList(queryWrapper);
		List<Long> ids = details.stream().map(GroupDetail::getProdId).collect(Collectors.toList());
		if(Func.isNotEmpty(ids)){
			return groupMapper.selectCountByGroupId(ProductConstants.PRODUCT_ITEM_PUBLISH_STATE, ids);
		}
		return 0;
	}

	@Override
	public int removeByItemId(Integer type, Long... id) {
		if(Func.isNotEmpty(id)){
			return baseMapper.removeByItemId(type, id);
		}
		return 0;
	}

	@Override
	public int removeByGroupIds(Long... ids) {
		return baseMapper.removeByGroupId(ids);
	}

	@Override
	public List<GroupDetail> getDetailsById(Long gid) {
		QueryWrapper<GroupDetail> queryWrapper = Condition.getQueryWrapper(new GroupDetail());
		queryWrapper.eq("prod_id",gid);
		return baseMapper.selectList(queryWrapper);
	}

}

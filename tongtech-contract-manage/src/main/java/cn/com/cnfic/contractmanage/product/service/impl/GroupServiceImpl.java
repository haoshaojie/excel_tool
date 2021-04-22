package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.dto.GroupDTO;
import cn.com.cnfic.contractmanage.product.entity.Group;
import cn.com.cnfic.contractmanage.product.entity.GroupDetail;
import cn.com.cnfic.contractmanage.product.enums.MsgEnum;
import cn.com.cnfic.contractmanage.product.mapper.GroupMapper;
import cn.com.cnfic.contractmanage.product.service.IGroupDetailService;
import cn.com.cnfic.contractmanage.product.service.IGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.springblade.core.tool.utils.DateUtil.now;

/**
 * @desc 组合产品表 服务实现类
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
@Service
@AllArgsConstructor
public class GroupServiceImpl extends BaseServiceImpl<GroupMapper, Group> implements IGroupService {

	private IGroupDetailService iGroupDetailService;

	@Override
	public IPage<Group> selectGroupPage(Query query, GroupDTO group) {
		QueryWrapper<Group> queryWrapper = Condition.getQueryWrapper(new Group());
		getQueryWrapper(queryWrapper, group);
		queryWrapper.orderByDesc("create_time");
		return this.page(Condition.getPage(query), queryWrapper);
	}

	@Override
	public R<Boolean> removeGroupByIds(String ids) {
		// 删除组合产品详情
		iGroupDetailService.removeByGroupIds(Func.toLong(ids));
		// 删除组合产品
		return R.data(baseMapper.deleteBatchIds(Func.toLongList(ids))>0);
	}

	@Override
	public R<Boolean> updateGroupState(String ids, Integer state) {
		Group group = new Group();
		group.setProdState(state);
		if(Func.toInt(state) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
			group.setAddedDate(LocalDateTime.now());
		}else if(Func.toInt(state) == ProductConstants.PRODUCT_ITEM_EXPIRED_STATE){
			group.setExpiredDate(LocalDateTime.now());
		}
		// 更新上下架状态
		int t=baseMapper.updateGroupState(group,Func.toLongList(ids));
		return R.data(t>0,t>0?"操作成功！":"操作失败！");
	}

	@Override
	public R<Boolean> saveGroup(GroupDTO group) {
		checkValid(group);
		// 保存组合产品明细
		Long userId = SecureUtil.getUserId();
		group.setId(IdWorker.getId());
		group.setCreateTime(now());
		group.setCreateUser(userId);
		if(Func.toInt(group.getProdState()) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
			group.setAddedDate(LocalDateTime.now());
		}
		// 新增组合产品详情
		saveDetailsBatch(group.getGroupDetails(), group.getId(), userId);
		// 保存组合产品
		return R.data(this.save(group));
	}

	@Override
	public R<Boolean> updateGroup(GroupDTO group) {
		checkValid(group);

		// 保存组合产品明细
		Long userId = SecureUtil.getUserId();
		group.setUpdateTime(now());
		group.setUpdateUser(userId);
		if(Func.toInt(group.getProdState()) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
			group.setAddedDate(LocalDateTime.now());
		}
		// 删除组合产品明细
		iGroupDetailService.removeByGroupIds(Func.toLong(group.getId()));
		// 新增组合产品详情
		saveDetailsBatch(group.getGroupDetails(), group.getId(), userId);

		// 修改组合产品
		return R.data(baseMapper.updateByGroupId(group)>0);
	}

	@Override
	public R<Boolean> validProdName(Long id, String prodName) {
		// 校验组合产品名称
		QueryWrapper<Group> query = Condition.getQueryWrapper(new Group());
		// 不为空表示为编辑 即校验非当前数据是否重复
		if(Func.isNotEmpty(id)){
			query.ne("id",id);
		}
		query.eq("prod_name", prodName);
		return R.data(this.count(query)==0,
				this.count(query)==0?MsgEnum.SUCCESS_MSG.getMsg() : MsgEnum.CHECK_GROUP_PROP_NAME_REPEAT_MSG.getMsg());
	}

	/**
	 * 批量保存组合产品详情
	 * @param groupDetails
	 * @param gid
	 * @param userId
	 */
	private void saveDetailsBatch(List<GroupDetail> groupDetails,Long gid, Long userId){
		if(Func.isNotEmpty(groupDetails)){
			for (GroupDetail detail:groupDetails) {
				detail.setItemId(detail.getId());
				detail.setId(IdWorker.getId());
				detail.setProdId(gid);
				detail.setCreateTime(now());
				detail.setCreateUser(userId);
			}
			iGroupDetailService.saveBatch(groupDetails);
		}
	}
	/**
	 * 数据校验
	 * @param group
	 */
	private void checkValid(GroupDTO group){
		//校验数据
		R<Boolean> r = this.validProdName(group.getId(), group.getProdName());
		Assert.isTrue(r.getData(),r.getMsg());
		Assert.isTrue(Func.isNotEmpty(group.getProdName()),MsgEnum.CHECK_ITEM_PROP_NAME_NULL_MSG.getMsg());
		if(group.getProdState() < ProductConstants.PRODUCT_ITEM_DRAFT_STATE){
			Assert.isTrue(Func.isNotEmpty(group.getPropDept()),MsgEnum.CHECK_ITEM_PROP_DEPT_NULL_MSG.getMsg());
		}
	}

	/**
	 * 获取查询条件
	 * @param queryWrapper
	 * @param dto
	 * @return
	 */
	private QueryWrapper<Group> getQueryWrapper(QueryWrapper<Group> queryWrapper, GroupDTO dto){
		// 产品名称
		if(Func.isNotEmpty(dto.getProdName())){
			queryWrapper.likeRight("prod_name",dto.getProdName());
		}
		// 所属部门
		if(Func.isNotEmpty(dto.getPropDept())){
			queryWrapper.eq("prop_dept",dto.getPropDept());
		}
		return queryWrapper;
	}

}

package cn.com.cnfic.contractmanage.product.service;

import cn.com.cnfic.contractmanage.product.dto.GroupDTO;
import cn.com.cnfic.contractmanage.product.entity.Group;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;

/**
 * @desc 组合产品表 服务类
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
public interface IGroupService extends BaseService<Group> {

	/**
	 * 自定义分页
	 *
	 * @param query
	 * @param group
	 * @return
	 */
	IPage<Group> selectGroupPage(Query query, GroupDTO group);

	/**
	 * 删除组合产品
	 * @param ids
	 * @return
	 */
	R<Boolean> removeGroupByIds(String ids);

	/**
	 * 组合产品上下架
	 * @param ids
	 * @param state
	 * @return
	 */
	R<Boolean> updateGroupState(String ids, Integer  state);

	/**
	 * 保存组合产品
	 * @param group
	 * @return
	 */
	R<Boolean> saveGroup(GroupDTO group);

	/**
	 * 修改组合产品
	 * @param group
	 * @return
	 */
	R<Boolean> updateGroup(GroupDTO group);

	/**
	 * 校验ProdName
	 * @param id
	 * @param prodCode
	 * @return
	 */
	R<Boolean> validProdName(Long id, String prodCode);

}

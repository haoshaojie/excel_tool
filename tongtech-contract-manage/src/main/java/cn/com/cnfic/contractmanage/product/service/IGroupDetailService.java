package cn.com.cnfic.contractmanage.product.service;

import cn.com.cnfic.contractmanage.product.entity.GroupDetail;
import cn.com.cnfic.contractmanage.product.vo.GroupDetailVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * @desc 组合产品与产品/产品项关联表 服务类
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
public interface IGroupDetailService extends BaseService<GroupDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param groupDetail
	 * @return
	 */
	IPage<GroupDetailVO> selectGroupDetailPage(IPage<GroupDetailVO> page, GroupDetailVO groupDetail);

	/**
	 * 根据产品/产品项查询组合产品数量
	 * @param type
	 * @param id
	 * @return
	 */
	int queryGroupByItem(Integer type, Long ...id);

	/**
	 * 根据产品/产品项删除组合产品详情
	 * @param type
	 * @param id
	 * @return
	 */
	int removeByItemId(Integer type, Long ...id);

	/**
	 * 根据组合产品id 删除明细
	 * @param ids
	 * @return
	 */
	int removeByGroupIds(Long ...ids);

	/**
	 * 根据组合产品id 获取 明细列表
	 * @param gid
	 * @return
	 */
	List<GroupDetail> getDetailsById(Long gid);
}

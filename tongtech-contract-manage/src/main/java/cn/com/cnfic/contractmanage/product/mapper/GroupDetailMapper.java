package cn.com.cnfic.contractmanage.product.mapper;

import cn.com.cnfic.contractmanage.product.entity.GroupDetail;
import cn.com.cnfic.contractmanage.product.vo.GroupDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 组合产品与产品/产品项关联表 Mapper 接口
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
public interface GroupDetailMapper extends BaseMapper<GroupDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param groupDetail
	 * @return
	 */
	List<GroupDetailVO> selectGroupDetailPage(IPage page, GroupDetailVO groupDetail);

	/**
	 * 根据id 删除未上架的组合产品详情
	 * @param ids
	 * @param type
	 * @return
	 */
	int removeByItemId(@Param(value = "type") Integer type, @Param(value = "ids") Long ...ids);

	/**
	 * 根据组合产品id 删除组合产品明细
	 * @param ids
	 * @return
	 */
	int removeByGroupId(@Param(value = "ids") Long ...ids);
}

package cn.com.cnfic.contractmanage.product.mapper;

import cn.com.cnfic.contractmanage.product.dto.GroupDTO;
import cn.com.cnfic.contractmanage.product.entity.Group;
import cn.com.cnfic.contractmanage.product.vo.GroupVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 组合产品表 Mapper 接口
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
public interface GroupMapper extends BaseMapper<Group> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param group
	 * @return
	 */
	List<GroupVO> selectGroupPage(IPage page, GroupVO group);

	/**
	 * 获取组合产品数量
	 * @param state
	 * @param ids
	 * @return
	 */
	int selectCountByGroupId(@Param(value = "prodState")Integer state, @Param(value = "ids")List<Long> ids);

	/**
	 * 根据组合产品id 修改数据
	 * @param group
	 * @return
	 */
	int updateByGroupId(GroupDTO group);

	/**
	 * 组合产品上架下架
	 * @param group
	 * @param ids
	 * @return
	 */
	int updateGroupState(@Param(value = "group") Group group, @Param(value = "ids")List<Long> ids);
}

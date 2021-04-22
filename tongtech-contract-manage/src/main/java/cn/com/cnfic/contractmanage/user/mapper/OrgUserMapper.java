package cn.com.cnfic.contractmanage.user.mapper;

import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.vo.OrgUserExcelVO;
import cn.com.cnfic.contractmanage.user.vo.OrgUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 企业用户表 Mapper 接口
 * @author Cnfic-UserManage
 * @date 2021-03-29
 */
public interface OrgUserMapper extends BaseMapper<OrgUser> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orgUser
	 * @return
	 */
	List<OrgUserVO> selectOrgUserPage(IPage page, OrgUserVO orgUser);

	/**
	 * 根据条件查询所有机构用户
	 * @param orgUser 条件
	 * @return
	 */
	List<OrgUserVO> selectOrgUserPage(@Param("orgUser")OrgUserVO orgUser);
	/**
	 * 通过客户id集合查询用户信息
	 * @param customerIds 客户id集合
	 * @return List<OrgUserExcelVO>
	 */
    List<OrgUserExcelVO> findByCustomerIds(List<Long> customerIds);
}

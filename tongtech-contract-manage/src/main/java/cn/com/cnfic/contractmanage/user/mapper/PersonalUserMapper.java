package cn.com.cnfic.contractmanage.user.mapper;

import cn.com.cnfic.contractmanage.user.dto.PersonalUserDTO;
import cn.com.cnfic.contractmanage.user.entity.PersonalUser;
import cn.com.cnfic.contractmanage.user.vo.PersonalUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 个人用户表 Mapper 接口
 * @author Cnfic-UserManage
 * @date 2021-04-01
 */
public interface PersonalUserMapper extends BaseMapper<PersonalUser> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param personalUser
	 * @return
	 */
	List<PersonalUserVO> selectPersonalUserPage(IPage page, PersonalUserVO personalUser);
	/**
	 * 自定义查询集合
	 *
	 * @param personalUser
	 * @return
	 */
	List<PersonalUserVO> selectPersonalUserPage(@Param("personalUser") PersonalUserVO personalUser);

	/**
	 * 修改根据账号用户
	 * @param personalUser
	 * @return
	 */
	int updatePersonalUserByAccount(@Param("personalUser") PersonalUserDTO personalUser);
}

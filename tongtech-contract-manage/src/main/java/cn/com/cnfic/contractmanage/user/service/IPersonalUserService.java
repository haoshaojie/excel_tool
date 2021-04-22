package cn.com.cnfic.contractmanage.user.service;

import cn.com.cnfic.contractmanage.user.dto.PersonalUserDTO;
import cn.com.cnfic.contractmanage.user.entity.PersonalUser;
import cn.com.cnfic.contractmanage.user.vo.PersonalUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.api.R;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.tool.api.R;

import javax.servlet.http.HttpServletResponse;

/**
 * @desc 个人用户表 服务类
 * @author Cnfic-UserManage
 * @date 2021-04-01
 */
public interface IPersonalUserService extends BaseService<PersonalUser> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param personalUser
	 * @return
	 */
	IPage<PersonalUserVO> selectPersonalUserPage(IPage<PersonalUserVO> page, PersonalUserVO personalUser);

	/**
	 * 根据查询条件导出个人注册用户
	 * @param response
	 * @param personalUser 查询参数
	 */
    void exportPersonalUser(HttpServletResponse response, PersonalUserVO personalUser);

	/**
	 * 新增用户
	 * @param personalUser
	 * @return
	 */
	R<Boolean> savePersonalUser(PersonalUserDTO personalUser);

	/**
	 * 修改用户
	 * @param personalUser
	 * @return
	 */
	R<Boolean> updatePersonalUser(PersonalUserDTO personalUser);

	/**
	 * 查询邀请码
	 * @param code
	 * @param accountId
	 * @return
	 */
	R<Boolean> existInviteCode(String code, Long accountId);

}

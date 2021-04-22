package cn.com.cnfic.contractmanage.user.wrapper;

import cn.com.cnfic.contractmanage.user.entity.PersonalUser;
import cn.com.cnfic.contractmanage.user.vo.PersonalUserVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * @desc 个人用户表包装类,返回视图层所需的字段
 * @author Cnfic-UserManage
 * @date 2021-04-01
 */
public class PersonalUserWrapper extends BaseEntityWrapper<PersonalUser, PersonalUserVO>  {

    public static PersonalUserWrapper build() {
        return new PersonalUserWrapper();
    }

	@Override
	public PersonalUserVO entityVO(PersonalUser personalUser) {
		PersonalUserVO personalUserVO = BeanUtil.copy(personalUser, PersonalUserVO.class);

		return personalUserVO;
	}

}

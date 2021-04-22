package cn.com.cnfic.contractmanage.user.wrapper;

import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.vo.OrgUserVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * @desc 企业用户表包装类,返回视图层所需的字段
 * @author Cnfic-UserManage
 * @date 2021-03-29
 */
public class OrgUserWrapper extends BaseEntityWrapper<OrgUser, OrgUserVO>  {

    public static OrgUserWrapper build() {
        return new OrgUserWrapper();
    }

	@Override
	public OrgUserVO entityVO(OrgUser orgUser) {
		OrgUserVO orgUserVO = BeanUtil.copy(orgUser, OrgUserVO.class);

		return orgUserVO;
	}

}

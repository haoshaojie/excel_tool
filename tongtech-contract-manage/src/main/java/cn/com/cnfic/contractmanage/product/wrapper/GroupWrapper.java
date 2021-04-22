package cn.com.cnfic.contractmanage.product.wrapper;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.Group;
import cn.com.cnfic.contractmanage.product.vo.GroupVO;
import cn.com.cnfic.system.cache.DictCache;
import cn.com.cnfic.system.user.cache.UserCache;
import cn.com.cnfic.system.user.entity.UserInfo;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

/**
 * @desc 组合产品表包装类,返回视图层所需的字段
 * @author Cnfic-UserManage
 * @date 2021-03-26
 */
public class GroupWrapper extends BaseEntityWrapper<Group, GroupVO>  {

    public static GroupWrapper build() {
        return new GroupWrapper();
    }

	@Override
	public GroupVO entityVO(Group group) {
		GroupVO groupVO = BeanUtil.copy(group, GroupVO.class);
		// 创建人
		if(Func.isNotEmpty(group.getCreateUser())){
			UserInfo createUser = UserCache.getUser(group.getCreateUser());
			groupVO.setCreateUserName(createUser.getUser().getRealName());
		}
		// 修改人
		if(Func.isNotEmpty(group.getUpdateUser())){
			UserInfo updateUser = UserCache.getUser(group.getUpdateUser());
			groupVO.setUpdateUserName(updateUser.getUser().getRealName());
		}
		// 状态
		if(Func.isNotEmpty(group.getProdState())){
			groupVO.setProdStateName(DictCache.getValue(ProductConstants.PRODUCT_STATE_CODE,group.getProdState()));
		}
		return groupVO;
	}

}

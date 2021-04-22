package cn.com.cnfic.contractmanage.contract.wrapper;

import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import cn.com.cnfic.contractmanage.contract.entity.ConUserProd;
import cn.com.cnfic.contractmanage.contract.vo.ConUserProdVO;

/**
 * @desc 合约用户产品权限表包装类,返回视图层所需的字段
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
public class ConUserProdWrapper extends BaseEntityWrapper<ConUserProd, ConUserProdVO>  {

    public static ConUserProdWrapper build() {
        return new ConUserProdWrapper();
    }

	@Override
	public ConUserProdVO entityVO(ConUserProd userProd) {
		ConUserProdVO userProdVO = BeanUtil.copy(userProd, ConUserProdVO.class);

		return userProdVO;
	}

}

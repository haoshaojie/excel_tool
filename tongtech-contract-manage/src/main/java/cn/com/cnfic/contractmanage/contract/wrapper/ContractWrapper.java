package cn.com.cnfic.contractmanage.contract.wrapper;

import cn.com.cnfic.contractmanage.contract.entity.Contract;
import cn.com.cnfic.contractmanage.contract.vo.ContractVO;
import org.springblade.core.mp.support.BaseEntityWrapper;

/**
 * 合约表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public class ContractWrapper extends BaseEntityWrapper<ContractVO, ContractVO>  {

    public static ContractWrapper build() {
        return new ContractWrapper();
    }

	@Override
	public ContractVO entityVO(ContractVO contract) {
//		ContractVO contractVO = BeanUtil.copy(contract, ContractVO.class);

		return contract;
	}

}

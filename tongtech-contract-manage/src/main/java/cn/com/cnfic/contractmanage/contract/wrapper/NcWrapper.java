package cn.com.cnfic.contractmanage.contract.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

import cn.com.cnfic.contractmanage.contract.entity.Nc;
import cn.com.cnfic.contractmanage.contract.vo.NcVO;

/**
 * 用友合同表?包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public class NcWrapper extends BaseEntityWrapper<Nc, NcVO>  {

    public static NcWrapper build() {
        return new NcWrapper();
    }

	@Override
	public NcVO entityVO(Nc nc) {
		NcVO ncVO = BeanUtil.copy(nc, NcVO.class);

		return ncVO;
	}

}

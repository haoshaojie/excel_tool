package cn.com.cnfic.contractmanage.partner.wrapper;

import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import cn.com.cnfic.contractmanage.partner.entity.Partner;
import cn.com.cnfic.contractmanage.partner.vo.PartnerVO;

/**
 * 合作商表包装类,返回视图层所需的字段
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public class PartnerWrapper extends BaseEntityWrapper<Partner, PartnerVO>  {

    public static PartnerWrapper build() {
        return new PartnerWrapper();
    }

	@Override
	public PartnerVO entityVO(Partner partner) {
		PartnerVO partnerVO = BeanUtil.copy(partner, PartnerVO.class);

		return partnerVO;
	}

}

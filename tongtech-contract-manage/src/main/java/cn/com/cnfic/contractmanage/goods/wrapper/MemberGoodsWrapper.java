package cn.com.cnfic.contractmanage.goods.wrapper;

import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import cn.com.cnfic.contractmanage.goods.entity.MemberGoods;
import cn.com.cnfic.contractmanage.goods.vo.MemberGoodsVO;

/**
 * 会员商品表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-17
 */
public class MemberGoodsWrapper extends BaseEntityWrapper<MemberGoods, MemberGoodsVO>  {

    public static MemberGoodsWrapper build() {
        return new MemberGoodsWrapper();
    }

	@Override
	public MemberGoodsVO entityVO(MemberGoods memberGoods) {
		MemberGoodsVO memberGoodsVO = BeanUtil.copy(memberGoods, MemberGoodsVO.class);

		return memberGoodsVO;
	}

}

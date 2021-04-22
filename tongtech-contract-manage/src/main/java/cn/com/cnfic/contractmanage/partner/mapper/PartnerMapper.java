/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.cnfic.contractmanage.partner.mapper;

import cn.com.cnfic.contractmanage.partner.entity.Partner;
import cn.com.cnfic.contractmanage.partner.vo.OrderProfitTotalVO;
import cn.com.cnfic.contractmanage.partner.vo.PartnerProductVO;
import cn.com.cnfic.contractmanage.partner.vo.PartnerVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 合作商表 Mapper 接口
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public interface PartnerMapper extends BaseMapper<Partner> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param partner
	 * @return
	 */
	List<PartnerVO> selectPartnerPage(IPage page, PartnerVO partner);


	List<PartnerProductVO> selectPartnerProductPage(IPage page, PartnerProductVO partnerProduct);


	List<OrderProfitTotalVO> selectOrderProfitTotalPage(IPage page, OrderProfitTotalVO orderProfitTotal);

}

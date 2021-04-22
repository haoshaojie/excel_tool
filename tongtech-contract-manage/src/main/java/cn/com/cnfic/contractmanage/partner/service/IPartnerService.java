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
package cn.com.cnfic.contractmanage.partner.service;

import cn.com.cnfic.contractmanage.partner.entity.Partner;
import cn.com.cnfic.contractmanage.partner.vo.OrderProfitTotalVO;
import cn.com.cnfic.contractmanage.partner.vo.PartnerProductVO;
import cn.com.cnfic.contractmanage.partner.vo.PartnerVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.support.Condition;

/**
 * 合作商表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public interface IPartnerService extends BaseService<Partner> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param partner
	 * @return
	 */
	IPage<PartnerVO> selectPartnerPage(IPage<PartnerVO> page, PartnerVO partner);

	/**
	 * 查询合作商的产品项/产品列表
	 * @param page
	 * @param partnerProduct
	 * @return
	 */
	IPage<PartnerProductVO> selectPartnerProductPage(IPage<PartnerProductVO> page, PartnerProductVO partnerProduct);

	/**
	 * 查询合作商的分润汇总
	 * @param page
	 * @param orderProfitTotal
	 * @return
	 */
	IPage<OrderProfitTotalVO> selectOrderProfitTotalPage(IPage<OrderProfitTotalVO> page, OrderProfitTotalVO orderProfitTotal);

}

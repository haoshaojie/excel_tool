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
package cn.com.cnfic.contractmanage.partner.service.impl;

import cn.com.cnfic.contractmanage.partner.entity.Partner;
import cn.com.cnfic.contractmanage.partner.vo.OrderProfitTotalVO;
import cn.com.cnfic.contractmanage.partner.vo.PartnerProductVO;
import cn.com.cnfic.contractmanage.partner.vo.PartnerVO;
import cn.com.cnfic.contractmanage.partner.mapper.PartnerMapper;
import cn.com.cnfic.contractmanage.partner.service.IPartnerService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 合作商表 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
@Service
public class PartnerServiceImpl extends BaseServiceImpl<PartnerMapper, Partner> implements IPartnerService {

	@Override
	public IPage<PartnerVO> selectPartnerPage(IPage<PartnerVO> page, PartnerVO partner) {
		return page.setRecords(baseMapper.selectPartnerPage(page, partner));
	}

	@Override
	public IPage<PartnerProductVO> selectPartnerProductPage(IPage<PartnerProductVO> page, PartnerProductVO partnerProduct) {
		return page.setRecords(baseMapper.selectPartnerProductPage(page, partnerProduct));
	}

	@Override
	public IPage<OrderProfitTotalVO> selectOrderProfitTotalPage(IPage<OrderProfitTotalVO> page, OrderProfitTotalVO orderProfitTotal) {
		return page.setRecords(baseMapper.selectOrderProfitTotalPage(page, orderProfitTotal));
	}
}

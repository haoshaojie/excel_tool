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
package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.ProfitRate;
import cn.com.cnfic.contractmanage.product.mapper.ProfitRateMapper;
import cn.com.cnfic.contractmanage.product.service.IProfitRateService;
import cn.com.cnfic.contractmanage.product.vo.ProfitRateVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springblade.core.tool.utils.DateUtil.now;

/**
 * 产品分润比例表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
@Service
public class ProfitRateServiceImpl extends BaseServiceImpl<ProfitRateMapper, ProfitRate> implements IProfitRateService {

	@Override
	public IPage<ProfitRateVO> selectProfitRatePage(IPage<ProfitRateVO> page, ProfitRateVO profitRate) {
		return page.setRecords(baseMapper.selectProfitRatePage(page, profitRate));
	}

	@Override
	public int removeByProdId(Integer type, Long ...prodId) {
		return baseMapper.removeByProdId( type, prodId);
	}

	@Override
	public List<ProfitRate> getListByProdId(Long prodId, Integer type) {
		QueryWrapper<ProfitRate> query= Condition.getQueryWrapper(new ProfitRate());
		query.eq("prod_id",prodId);
		query.eq("type",type);
		return baseMapper.selectList(query);
	}
	/**
	 * 保存分润比例数据
	 * @param profitRates
	 * @param userId
	 * @param prodId
	 */
	@Override
	public void handleProfitRates(List<ProfitRate> profitRates, Long userId, Long prodId, Integer type){
		if(Func.isNotEmpty(profitRates)){
			for (ProfitRate profitRate: profitRates) {
				profitRate.setType(type);
				profitRate.setIsDeleted(ProductConstants.IS_DELETED);
				profitRate.setProdId(prodId);
				profitRate.setCreateUser(userId);
				profitRate.setCreateTime(now());
				profitRate.setId(IdWorker.getId());
			}
			// 删除数据后在保存
			this.removeByProdId(type, prodId);
			// 保存数据
			this.saveBatch(profitRates);
		}
	}
}

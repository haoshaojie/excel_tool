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
import cn.com.cnfic.contractmanage.product.dto.ValuationDTO;
import cn.com.cnfic.contractmanage.product.entity.Valuation;
import cn.com.cnfic.contractmanage.product.entity.ValuationDetail;
import cn.com.cnfic.contractmanage.product.mapper.ValuationMapper;
import cn.com.cnfic.contractmanage.product.service.IValuationDetailService;
import cn.com.cnfic.contractmanage.product.service.IValuationService;
import cn.com.cnfic.contractmanage.product.vo.ValuationVO;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springblade.core.tool.utils.DateUtil.now;

/**
 * 产品计价配置表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
@Service
@AllArgsConstructor
public class ValuationServiceImpl extends BaseServiceImpl<ValuationMapper, Valuation> implements IValuationService {

	private IValuationDetailService iValuationDetailService;

	@Override
	public IPage<ValuationVO> selectValuationPage(IPage<ValuationVO> page, ValuationVO valuation) {
		return page.setRecords(baseMapper.selectValuationPage(page, valuation));
	}

	@Override
	public int removeByProdId(Integer type,Long ...prodId) {
		return baseMapper.removeByProdId(type, prodId);
	}

	@Override
	public List<Valuation> getValuations(ValuationDTO value) {
		QueryWrapper<Valuation> query= Condition.getQueryWrapper(new Valuation());
		List<Valuation> valuations = new ArrayList<>();
		if(Func.isNotEmpty(value)){
			if(Func.isNotEmpty(value.getProdId())){
				query.eq("prod_id",value.getProdId());
			}
			if(Func.isNotEmpty(value.getType())){
				query.eq("type",value.getType());
			}
			if(Func.isNotEmpty(value.getStartPrice()) && Func.isNotEmpty(value.getEndPrice())){
				query.between("standard_price",value.getStartPrice(), value.getEndPrice());
			}
			if(Func.isNotEmpty(value.getExclude())){
				query.notIn("id",Func.toLongArray(value.getExclude()));
			}
			List<Valuation> list = baseMapper.selectList(query);
			if(Func.isNotEmpty(value.getKeyword())){
				for (Valuation val :list) {
					if(Func.isNotEmpty(val.getValuationValues())){
						JSONArray jsonArray = JSONUtil.parseArray(val.getValuationValues());
						for (Object obj:jsonArray) {
							JSONObject json = JSONUtil.parseObj(obj);
							if(json.get("propValue").equals(value.getKeyword())){
								valuations.add(val);
								break;
							}
						}
					}
				}
			}else{
				valuations.addAll(list);
			}
		}
		return valuations;
	}

	/**
	 * 保存分润比例数据
	 * @param valuations
	 * @param userId
	 * @param prodId
	 */
	@Override
	public void handleValuations(List<Valuation> valuations, Long userId, Long prodId, Integer type){
		if(Func.isNotEmpty(valuations)){
			List<ValuationDetail> details = new ArrayList<>();
			ValuationDetail detail = null;
			for (Valuation valuation: valuations) {
				valuation.setType(type);
				valuation.setIsDeleted(ProductConstants.IS_DELETED);
				valuation.setProdId(prodId);
				valuation.setCreateUser(userId);
				valuation.setCreateTime(now());
				valuation.setId(IdWorker.getId());
				JSONArray jsonArray = new JSONArray(valuation.getValuationValues());
				for (Object obj: jsonArray) {
					JSONObject json = JSONUtil.parseObj(obj);
					detail = new ValuationDetail();
					detail.setValuationId(valuation.getId());
					detail.setType(type);
					detail.setIsDeleted(ProductConstants.IS_DELETED);
					detail.setProdId(prodId);
					detail.setCreateUser(userId);
					detail.setCreateTime(now());
					detail.setPropCode(Func.toStr(json.get("propCode")));
					detail.setPropValue(Func.toStr(json.get("propValue")));
					detail.setPropId(Func.toLong(json.get("propId")));
					detail.setPropName(Func.toStr(json.get("propName")));
					details.add(detail);
				}
			}
			// 删除数据后在保存
			this.removeByProdId(type, prodId);
			// 删除明细
			iValuationDetailService.removeByProdId(type, prodId);
			// 保存数据
			this.saveBatch(valuations);
			// 保存明细
			iValuationDetailService.saveBatch(details);
		}
	}

}

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
import cn.com.cnfic.contractmanage.product.entity.ItemValue;
import cn.com.cnfic.contractmanage.product.mapper.ItemValueMapper;
import cn.com.cnfic.contractmanage.product.service.IItemValueService;
import cn.com.cnfic.contractmanage.product.vo.ItemValueVO;
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
 * 产品项扩展信息表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
@Service
public class ItemValueServiceImpl extends BaseServiceImpl<ItemValueMapper, ItemValue> implements IItemValueService {

	@Override
	public IPage<ItemValueVO> selectItemValuePage(IPage<ItemValueVO> page, ItemValueVO itemValue) {
		return page.setRecords(baseMapper.selectItemValuePage(page, itemValue));
	}

	@Override
	public int removeByProdId(Long ...prodId) {
		return baseMapper.removeByProdId(prodId);
	}

	@Override
	public List<ItemValue> getListByProdId(Long prodId) {
		QueryWrapper<ItemValue> query = Condition.getQueryWrapper(new ItemValue());
		query.eq("prod_id",prodId);
		return baseMapper.selectList(query);
	}

	/**
	 * 保存扩展属性
	 * @param itemValues
	 * @param userId
	 * @param prodId
	 */
	@Override
	public void handleItemValues(List<ItemValue> itemValues, Long userId, Long prodId){
		if(Func.isNotEmpty(itemValues)){
			for (ItemValue itemValue: itemValues) {
				itemValue.setIsDeleted(ProductConstants.IS_DELETED);
				itemValue.setProdId(prodId);
				itemValue.setPropId(itemValue.getPropId());
				itemValue.setCreateUser(userId);
				itemValue.setCreateTime(now());
				itemValue.setId(IdWorker.getId());
			}
			// 删除数据后在保存
			this.removeByProdId(prodId);
			// 保存数据
			this.saveBatch(itemValues);
		}
	}

}

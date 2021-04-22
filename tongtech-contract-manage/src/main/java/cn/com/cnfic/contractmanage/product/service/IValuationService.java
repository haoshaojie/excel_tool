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
package cn.com.cnfic.contractmanage.product.service;

import cn.com.cnfic.contractmanage.product.dto.ValuationDTO;
import cn.com.cnfic.contractmanage.product.entity.Valuation;
import cn.com.cnfic.contractmanage.product.vo.ValuationVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 产品计价配置表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
public interface IValuationService extends BaseService<Valuation> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param valuation
	 * @return
	 */
	IPage<ValuationVO> selectValuationPage(IPage<ValuationVO> page, ValuationVO valuation);

	/**
	 * 根据产品项id 删除计价属性
	 * @param prodId
	 * @param type
	 * @return
	 */
	int removeByProdId(Integer type,Long ...prodId);

	/**
	 * 根据prodid 获取计价属性
	 * @param value
	 * @return
	 */
	List<Valuation> getValuations(ValuationDTO value);
	/**
	 * 保存计价属性
	 * @param valuations
	 * @param userId
	 * @param prodId
	 * @param type
	 * @return
	 */
	void handleValuations(List<Valuation> valuations, Long userId, Long prodId, Integer type);
}

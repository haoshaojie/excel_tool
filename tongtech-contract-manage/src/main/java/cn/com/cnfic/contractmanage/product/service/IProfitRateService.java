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

import cn.com.cnfic.contractmanage.product.entity.ProfitRate;
import cn.com.cnfic.contractmanage.product.vo.ProfitRateVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 产品分润比例表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
public interface IProfitRateService extends BaseService<ProfitRate> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param profitRate
	 * @return
	 */
	IPage<ProfitRateVO> selectProfitRatePage(IPage<ProfitRateVO> page, ProfitRateVO profitRate);

	/**
	 * 根据产品项id 删除分润比例列表
	 * @param prodId
	 * @param type
	 * @return
	 */
	int removeByProdId(Integer type, Long ...prodId);

	/**
	 * 根据prodid 获取扩展属性
	 * @param prodId
	 * @param type
	 * @return
	 */
	List<ProfitRate> getListByProdId(Long prodId, Integer type);

	/**
	 * 处理分润比例
	 * @param profitRates
	 * @param userId
	 * @param prodId
	 * @param type
	 */
	void handleProfitRates(List<ProfitRate> profitRates, Long userId, Long prodId, Integer type);
}

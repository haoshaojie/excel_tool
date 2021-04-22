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
package cn.com.cnfic.contractmanage.product.mapper;

import cn.com.cnfic.contractmanage.product.entity.ProfitRate;
import cn.com.cnfic.contractmanage.product.vo.ProfitRateVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品分润比例表 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-15
 */
public interface ProfitRateMapper extends BaseMapper<ProfitRate> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param profitRate
	 * @return
	 */
	List<ProfitRateVO> selectProfitRatePage(IPage page, ProfitRateVO profitRate);

	/**
	 * 根据产品项id 删除分润比例列表
	 * @param ids
	 * @param type
	 * @return
	 */
	int removeByProdId(@Param(value = "type") Integer type, @Param(value = "ids") Long... ids);
}

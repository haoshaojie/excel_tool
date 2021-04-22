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

import cn.com.cnfic.contractmanage.product.dto.ProductItemDTO;
import cn.com.cnfic.contractmanage.product.entity.ProductItem;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;

import java.util.List;
import java.util.Map;

/**
 * 产品项表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
public interface IProductItemService extends IService<ProductItem> {

	/**
	 * 保存产品项数据
	 * @param productItem
	 * @return
	 */
	R<Boolean> saveItem(ProductItemDTO productItem);
	/**
	 * 修改产品项数据
	 * @param productItem
	 * @return
	 */
	R<Boolean> updateItem(ProductItemDTO productItem);

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	R<Boolean> removeByIds(String ids);
	/**
	 * 自定义分页
	 *
	 * @param query
	 * @param productItem
	 * @return
	 */
	IPage<ProductItem> selectProductItemPage(Query query, ProductItemDTO productItem);

	/**
	 * 校验propcode
	 * @param id
	 * @param propCode
	 * @return
	 */
	R<Boolean> checkPropCode(Long id, String propCode);

	/**
	 * 产品项上下架
	 * @param ids
	 * @param itemState
	 * @return
	 */
	R<Boolean> updateItemState(String ids, Integer  itemState);

	/**
	 * 定时任务根据时间批量更新上下架状态
	 * @param itemState
	 * @return
	 */
	R<Boolean> updateStateByTask(Integer  itemState);

	/**
	 * 导出列表
	 * @param productItem
	 * @return
	 */
	Map<String,List<List<String>>> exportData(ProductItemDTO productItem);

	/**
	 * 获取上架列表
	 * @param productItem
	 * @return
	 */
	List<ProductItem> getAddedList(ProductItemDTO productItem);
}

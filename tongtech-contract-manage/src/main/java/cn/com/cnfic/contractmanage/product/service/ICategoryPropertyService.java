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

import cn.com.cnfic.contractmanage.product.entity.CategoryProperty;
import cn.com.cnfic.contractmanage.product.vo.CategoryPropertyVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 产品类型关联属性字典 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-09
 */
public interface ICategoryPropertyService extends BaseService<CategoryProperty> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param categoryProperty
	 * @return
	 */
	IPage<CategoryPropertyVO> selectCategoryPropertyPage(IPage<CategoryPropertyVO> page, CategoryPropertyVO categoryProperty);

	/**
	 * 绑定产品类型和属性关系
	 * @param propIds 产品属性主键集合
	 * @param cateId 产品类型主键
	 * @return
	 */
	Boolean saveOrUpdate(List<Long> propIds,Long cateId);
}

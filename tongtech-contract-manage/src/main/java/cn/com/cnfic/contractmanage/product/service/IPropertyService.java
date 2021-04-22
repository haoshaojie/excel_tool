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

import cn.com.cnfic.contractmanage.product.dto.PropertyDTO;
import cn.com.cnfic.contractmanage.product.entity.Property;
import cn.com.cnfic.contractmanage.product.vo.PropertyVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 产品属性字典表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
public interface IPropertyService extends BaseService<Property> {

	/**
	 * 自定义分页
	 *
	 * @param query
	 * @param property
	 * @return
	 */
	IPage<PropertyVO> selectPropertyPage(Query query, PropertyDTO property);

	/**
	 * 保存产品属性
	 * @param property
	 * @return
	 */
	boolean save(PropertyDTO property);

	/**
	 * 修改产品属性
	 * @param property
	 * @return
	 */
	boolean updateById(PropertyDTO property);
	/**
	 * 根据名称和编码精确查询数量
	 * 用于判断编码和名称是否唯一
	 * @param property
	 * @return
	 */
	int selectPropertyByCodeOrName(PropertyDTO property);

	/**
	 * 根据主键查询详情
	 * @param id
	 * @return
	 */
	PropertyVO getDetailById(@Param(value = "id") Long id);

	/**
	 * 查询某个类型拥有的属性
	 * @param query
	 * @param property
	 * @return
	 */
	IPage<PropertyVO> selectPropertyPageInCategory(Query query, PropertyDTO property);

	/**
	 * 查询某个类型未拥有的属性
	 * @param property
	 * @return
	 */
	List<PropertyVO> selectPropertyNotInCategory(PropertyDTO property);

	/**
	 * 根据类型获取属性列表
	 * @param property
	 * @return
	 */
	List<Property> getListByCate(PropertyDTO property);
}

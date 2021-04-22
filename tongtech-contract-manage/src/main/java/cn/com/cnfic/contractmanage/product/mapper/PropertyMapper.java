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

import cn.com.cnfic.contractmanage.product.dto.PropertyDTO;
import cn.com.cnfic.contractmanage.product.entity.Property;
import cn.com.cnfic.contractmanage.product.vo.PropertyVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品属性字典表 Mapper 接口
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
public interface PropertyMapper extends BaseMapper<Property> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param property
	 * @return
	 */
	List<PropertyVO> selectPropertyPage(IPage page, PropertyDTO property);

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
	 * @param page
	 * @param property
	 * @return
	 */
	List<PropertyVO> selectPropertyPageInCategory(IPage page, PropertyDTO property);

	/**
	 * 查询某个类型未拥有的属性
	 * @param property
	 * @return
	 */
	List<PropertyVO> selectPropertyNotInCategory(PropertyDTO property);
}

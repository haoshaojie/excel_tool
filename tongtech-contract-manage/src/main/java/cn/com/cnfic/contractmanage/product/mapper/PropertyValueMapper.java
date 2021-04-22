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

import cn.com.cnfic.contractmanage.product.entity.PropertyValue;
import cn.com.cnfic.contractmanage.product.vo.PropertyValueVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品属性值表 Mapper 接口
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
public interface PropertyValueMapper extends BaseMapper<PropertyValue> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param propertyValue
	 * @return
	 */
	List<PropertyValueVO> selectPropertyValuePage(IPage page, PropertyValueVO propertyValue);

	/**
	 * 根据属性id 查询属性值列表
	 * @param id
	 * @return
	 */
	List<PropertyValue> selectPropertyValueByPropId(@Param(value = "id") Long id);

	/**
	 * 逻辑删除
	 * @param deletedValue
	 * @param propertyValueList
	 * @return
	 */
	boolean updateIsDeleted(@Param(value = "deletedValue") Integer deletedValue,List<?> propertyValueList);
}

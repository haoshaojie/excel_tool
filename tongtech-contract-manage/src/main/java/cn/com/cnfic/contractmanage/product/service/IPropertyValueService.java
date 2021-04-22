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

import cn.com.cnfic.contractmanage.product.entity.PropertyValue;
import cn.com.cnfic.contractmanage.product.vo.PropertyValueVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 产品属性值表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
public interface IPropertyValueService extends IService<PropertyValue> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param propertyValue
	 * @return
	 */
	IPage<PropertyValueVO> selectPropertyValuePage(IPage<PropertyValueVO> page, PropertyValueVO propertyValue);

	/**
	 * 根据属性id 获取属性值列表
	 * @param propId
	 * @return
	 */
	List<PropertyValue> queryListByProp(Long propId);
}

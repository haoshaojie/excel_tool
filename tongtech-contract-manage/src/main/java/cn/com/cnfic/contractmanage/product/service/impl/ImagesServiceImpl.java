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
import cn.com.cnfic.contractmanage.product.entity.Images;
import cn.com.cnfic.contractmanage.product.mapper.ImagesMapper;
import cn.com.cnfic.contractmanage.product.service.IImagesService;
import cn.com.cnfic.contractmanage.product.vo.ImagesVO;
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
 * 产品项图片表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
@Service
public class ImagesServiceImpl extends BaseServiceImpl<ImagesMapper, Images> implements IImagesService {

	@Override
	public IPage<ImagesVO> selectImagesPage(IPage<ImagesVO> page, ImagesVO images) {
		return page.setRecords(baseMapper.selectImagesPage(page, images));
	}

	@Override
	public int removeByProdId( Integer type,Long ...prodId) {
		return baseMapper.removeByProdId(type, prodId);
	}

	@Override
	public List<Images> getListByProdId(Long prodId, Integer type) {
		QueryWrapper<Images> query= Condition.getQueryWrapper(new Images());
		query.eq("prod_id",prodId);
		query.eq("type",type);
		return baseMapper.selectList(query);
	}

	/**
	 * 保存图片列表
	 * @param fileList
	 * @param userId
	 * @param prodId
	 * @param type
	 */
	@Override
	public void handlePictures(List<Images> fileList, Long userId, Long prodId, Integer type){
		if(Func.isNotEmpty(fileList)){
			for (Images image: fileList) {
				image.setType(type);
				image.setIsDeleted(ProductConstants.IS_DELETED);
				image.setProdId(prodId);
				image.setCreateUser(userId);
				image.setCreateTime(now());
				image.setId(IdWorker.getId());
			}
			// 删除数据后在保存
			this.removeByProdId(type, prodId);
			// 保存数据
			this.saveBatch(fileList);
		}

	}

}

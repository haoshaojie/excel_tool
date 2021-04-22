package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.entity.ProdItemRelation;
import cn.com.cnfic.contractmanage.product.mapper.ProdItemRelationMapper;
import cn.com.cnfic.contractmanage.product.service.IProdItemRelationService;
import cn.com.cnfic.contractmanage.product.vo.ProdItemRelationVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springblade.core.tool.utils.DateUtil.now;

/**
 * @desc 产品与产品项关联表 服务实现类
 * @author Cnfic-UserManage
 * @date 2021-04-02
 */
@Service
public class ProdItemRelationServiceImpl extends BaseServiceImpl<ProdItemRelationMapper, ProdItemRelation> implements IProdItemRelationService {

	@Override
	public IPage<ProdItemRelationVO> selectProdItemRelationPage(IPage<ProdItemRelationVO> page, ProdItemRelationVO prodItemRelation) {
		return page.setRecords(baseMapper.selectProdItemRelationPage(page, prodItemRelation));
	}

	@Override
	public void handleProdItemRelation(String prodIds, Long itemId, Long userId) {
		if(Func.isNotEmpty(prodIds)){
			ProdItemRelation relation = null;
			List<ProdItemRelation> relationList = new ArrayList<>();
			Long [] ids=Func.toLongArray("\\|",prodIds);
			for (Long id:ids) {
				relation = new ProdItemRelation();
				relation.setProdId(id);
				relation.setItemId(itemId);
				relation.setIsDeleted(ProductConstants.IS_DELETED);
				relation.setCreateUser(userId);
				relation.setCreateTime(now());
				relationList.add(relation);
			}
			// 删除数据后在保存
			this.removeRelationById(itemId);
			// 保存数据
			this.saveBatch(relationList);
		}
	}

	@Override
	public int removeRelationById(Long itemId) {
		return baseMapper.removeByRelationId(itemId);
	}

	@Override
	public int getItemListByProdId(Long... ids) {
		QueryWrapper<ProdItemRelation> query= Condition.getQueryWrapper(new ProdItemRelation());
		if(Func.isNotEmpty(ids)) {
			query.in("prod_id", ids);
		}
		return baseMapper.selectCount(query);
	}
}

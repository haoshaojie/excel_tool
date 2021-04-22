package cn.com.cnfic.contractmanage.product.service;

import cn.com.cnfic.contractmanage.product.entity.ProdItemRelation;
import cn.com.cnfic.contractmanage.product.vo.ProdItemRelationVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

/**
 * @desc 产品与产品项关联表 服务类
 * @author Cnfic-UserManage
 * @date 2021-04-02
 */
public interface IProdItemRelationService extends BaseService<ProdItemRelation> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param prodItemRelation
	 * @return
	 */
	IPage<ProdItemRelationVO> selectProdItemRelationPage(IPage<ProdItemRelationVO> page, ProdItemRelationVO prodItemRelation);

	/**
	 * 处理产品和产品项关联表
	 * @param prodIds
	 * @param itemId
	 * @param userId
	 */
	void handleProdItemRelation(String prodIds,Long itemId, Long userId);

	/**
	 * 根据id 删除关系表
	 * @param itemId
	 * @return
	 */
	int removeRelationById(Long itemId);

	/**
	 * 根据产品ids 获取产品项id数量
	 * @param ids
	 * @return
	 */
	int getItemListByProdId(Long ...ids);
}

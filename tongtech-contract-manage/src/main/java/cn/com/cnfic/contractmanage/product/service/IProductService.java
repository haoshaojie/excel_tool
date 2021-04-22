package cn.com.cnfic.contractmanage.product.service;

import cn.com.cnfic.contractmanage.product.dto.ProductDTO;
import cn.com.cnfic.contractmanage.product.entity.Product;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;

import java.util.List;
import java.util.Map;

/**
 * @desc 产品表 服务类
 * @author Cnfic-UserManage
 * @date 2021-03-24
 */
public interface IProductService extends BaseService<Product> {

	/**
	 * 自定义分页
	 *
	 * @param query
	 * @param product
	 * @return
	 */
	IPage<Product> selectProductPage(Query query, ProductDTO product);

	/**
	 * 产品项上下架
	 * @param ids
	 * @param state
	 * @return
	 */
	R<Boolean> updateProductState(String ids, Integer  state);

	/**
	 * 定时任务根据时间批量更新上下架状态
	 * @param state
	 * @return
	 */
	R<Boolean> updateStateByTask(Integer state);
	/**
	 * 删除产品
	 * @param ids
	 * @return
	 */
	R<Boolean> removeProductByIds(String ids);

	/**
	 * 校验propcode
	 * @param id
	 * @param prodCode
	 * @return
	 */
	R<Boolean> validProdCode(Long id, String prodCode);

	/**
	 * 保存产品
	 * @param product
	 * @return
	 */
	R<Boolean> saveProd(ProductDTO product);

	/**
	 * 修改产品
	 * @param product
	 * @return
	 */
	R<Boolean> updateProd(ProductDTO product);

	/**
	 * 导出列表
	 * @param product
	 * @return
	 */
	Map<String,List<List<String>>> exportData(ProductDTO product);

	/**
	 * 获取上架列表
	 * @param product
	 * @return
	 */
	List<Product> getAddedList(ProductDTO product);
}

package cn.com.cnfic.contractmanage.product.mapper;

import cn.com.cnfic.contractmanage.product.entity.Product;
import cn.com.cnfic.contractmanage.product.vo.ProductVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @desc 产品表 Mapper 接口
 * @author Cnfic-UserManage
 * @date 2021-03-24
 */
public interface ProductMapper extends BaseMapper<Product> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param product
	 * @return
	 */
	List<ProductVO> selectProductPage(IPage page, ProductVO product);

	/**
	 * 修改产品状态
	 * @param product
	 * @param ids
	 * @return
	 */
	int updateState(@Param(value = "prod") Product product, @Param(value = "ids")List<Long> ids);

	/**
	 * 修改产品状态
	 * @param product
	 * @return
	 */
	int updateStateByDate(@Param(value = "prod") Product product);

	/**
	 * 修改产品
	 * @param product
	 * @return
	 */
	int updateProdById(Product product);

	/**
	 * 获取产品计价属性
	 * @param product
	 * @return
	 */
	List<Map<String,Object>> selectProdAndValuation(@Param(value = "item") Product product);
}

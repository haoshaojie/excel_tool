package cn.com.cnfic.contractmanage.product.mapper;

import cn.com.cnfic.contractmanage.product.entity.ProductItem;
import cn.com.cnfic.contractmanage.product.vo.ProductItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产品项表 Mapper 接口
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
public interface ProductItemMapper extends BaseMapper<ProductItem> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param productItem
	 * @return
	 */
	List<ProductItemVO> selectProductItemPage(IPage page, ProductItemVO productItem);

	/**
	 * 修改
	 * @param productItem
	 * @return
	 */
	int updateByProdId(ProductItem productItem);
	/**
	 * 产品项上下架
	 * @param productItem
	 * @param ids
	 * @return
	 */
	int updateItemState(@Param(value = "item") ProductItem productItem, @Param(value = "ids")List<Long> ids);

	/**
	 * 根据时间修改上下架
	 * @param productItem
	 * @return
	 */
	int updateItemStateByDate(@Param(value = "item") ProductItem productItem);

	/**
	 * 获取计价属性
	 * @param productItem
	 * @return
	 */
	List<Map<String,Object>> selectItemAndValuation(@Param(value = "item") ProductItem productItem);
}

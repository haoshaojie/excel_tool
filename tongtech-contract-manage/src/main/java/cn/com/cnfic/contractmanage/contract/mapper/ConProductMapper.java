package cn.com.cnfic.contractmanage.contract.mapper;

import cn.com.cnfic.contractmanage.contract.entity.ConProduct;
import cn.com.cnfic.contractmanage.contract.vo.ConProductVO;
import cn.com.cnfic.contractmanage.contract.vo.ExcelProductVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @desc 合约产品表 Mapper 接口
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
public interface ConProductMapper extends BaseMapper<ConProduct> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param product
	 * @return
	 */
	List<ConProductVO> selectProductPage(IPage page, ConProductVO product);

	/**
	 * 查询产品
	 *
	 * @param productIds 产品标识
	 * @return
	 */
	List<ExcelProductVO> getProductAndValues(List<Long> productIds);

	/**
	 * 查询产品项
	 *
	 * @param productItemIds 产品项标识
	 * @return
	 */
	List<ExcelProductVO> getProductItemAndValues(List<Long> productItemIds);

}

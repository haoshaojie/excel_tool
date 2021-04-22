package cn.com.cnfic.contractmanage.contract.service;

import cn.com.cnfic.contractmanage.contract.entity.ConProduct;
import cn.com.cnfic.contractmanage.contract.vo.ConProductVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @desc 合约产品表 服务类
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
public interface IConProductService extends BaseService<ConProduct> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param product
	 * @return
	 */
	IPage<ConProductVO> selectProductPage(IPage<ConProductVO> page, ConProductVO product);

}

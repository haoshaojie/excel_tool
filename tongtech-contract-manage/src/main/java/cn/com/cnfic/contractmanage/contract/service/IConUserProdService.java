package cn.com.cnfic.contractmanage.contract.service;

import cn.com.cnfic.contractmanage.contract.entity.ConUserProd;
import cn.com.cnfic.contractmanage.contract.vo.ConUserProdVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @desc 合约用户产品权限表 服务类
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
public interface IConUserProdService extends BaseService<ConUserProd> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userProd
	 * @return
	 */
	IPage<ConUserProdVO> selectUserProdPage(IPage<ConUserProdVO> page, ConUserProdVO userProd);

}

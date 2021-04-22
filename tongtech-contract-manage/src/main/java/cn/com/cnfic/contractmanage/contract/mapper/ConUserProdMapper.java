package cn.com.cnfic.contractmanage.contract.mapper;

import cn.com.cnfic.contractmanage.contract.entity.ConUserProd;
import cn.com.cnfic.contractmanage.contract.vo.ConUserProdVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * @desc 合约用户产品权限表 Mapper 接口
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
public interface ConUserProdMapper extends BaseMapper<ConUserProd> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userProd
	 * @return
	 */
	List<ConUserProdVO> selectUserProdPage(IPage page, ConUserProdVO userProd);

}

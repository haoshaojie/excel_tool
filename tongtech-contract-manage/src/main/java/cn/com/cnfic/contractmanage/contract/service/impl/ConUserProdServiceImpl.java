package cn.com.cnfic.contractmanage.contract.service.impl;

import cn.com.cnfic.contractmanage.contract.entity.ConUserProd;
import cn.com.cnfic.contractmanage.contract.vo.ConUserProdVO;
import cn.com.cnfic.contractmanage.contract.mapper.ConUserProdMapper;
import cn.com.cnfic.contractmanage.contract.service.IConUserProdService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @desc 合约用户产品权限表 服务实现类
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
@Service
public class ConUserProdServiceImpl extends BaseServiceImpl<ConUserProdMapper, ConUserProd> implements IConUserProdService {

	@Override
	public IPage<ConUserProdVO> selectUserProdPage(IPage<ConUserProdVO> page, ConUserProdVO userProd) {
		List<ConUserProdVO> records = baseMapper.selectUserProdPage(page, userProd);
		return page.setRecords(records);
	}

}

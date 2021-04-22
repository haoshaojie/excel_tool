package cn.com.cnfic.contractmanage.contract.service.impl;

import cn.com.cnfic.contractmanage.contract.entity.ConProduct;
import cn.com.cnfic.contractmanage.contract.vo.ConProductVO;
import cn.com.cnfic.contractmanage.contract.mapper.ConProductMapper;
import cn.com.cnfic.contractmanage.contract.service.IConProductService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @desc 合约产品表 服务实现类
 * @author Cnfic-UserManage
 * @date 2021-04-19
 */
@Service
public class ConProductServiceImpl extends BaseServiceImpl<ConProductMapper, ConProduct> implements IConProductService {

	@Override
	public IPage<ConProductVO> selectProductPage(IPage<ConProductVO> page, ConProductVO product) {
		List<ConProductVO> records = baseMapper.selectProductPage(page, product);
		return page.setRecords(records);
	}

}

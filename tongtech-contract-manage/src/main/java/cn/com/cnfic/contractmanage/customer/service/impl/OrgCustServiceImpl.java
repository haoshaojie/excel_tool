package cn.com.cnfic.contractmanage.customer.service.impl;

import cn.com.cnfic.contractmanage.customer.entity.OrgCust;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustVO;
import cn.com.cnfic.contractmanage.customer.mapper.OrgCustMapper;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 客户管理行业三级分类 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-12
 */
@Service
public class OrgCustServiceImpl extends BaseServiceImpl<OrgCustMapper, OrgCust> implements IOrgCustService {

	@Override
	public IPage<OrgCustVO> selectOrgCustPage(IPage<OrgCustVO> page, OrgCustVO orgCust) {
		return page.setRecords(baseMapper.selectOrgCustPage(page, orgCust));
	}

	@Override
	public List<OrgCustVO> queryTree(Integer custLevel) {
		List<OrgCustVO> items = baseMapper.queryTree(custLevel);
		return ForestNodeMerger.merge(items);
	}

}

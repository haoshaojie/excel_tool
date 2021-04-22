package cn.com.cnfic.contractmanage.customer.service.impl;

import cn.com.cnfic.contractmanage.customer.entity.OrgActivity;
import cn.com.cnfic.contractmanage.customer.vo.OrgActivityVO;
import cn.com.cnfic.contractmanage.customer.mapper.OrgActivityMapper;
import cn.com.cnfic.contractmanage.customer.service.IOrgActivityService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 企业客户活动表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
@Service
public class OrgActivityServiceImpl extends BaseServiceImpl<OrgActivityMapper, OrgActivity> implements IOrgActivityService {

	@Override
	public IPage<OrgActivityVO> selectOrgActivityPage(IPage<OrgActivityVO> page, OrgActivityVO orgActivity) {
		return page.setRecords(baseMapper.selectOrgActivityPage(page, orgActivity));
	}

	@Override
	public void deleteLogicByCustomerId(Long id) {
		baseMapper.deleteLogicByCustomerId(id);
	}

	@Override
	public List<OrgActivityVO> findByCustomerIds(List<Long> customerIds) {
		return baseMapper.findByCustomerIds(customerIds);
	}

}

package cn.com.cnfic.contractmanage.userAccount.service.impl;


import cn.com.cnfic.contractmanage.userAccount.entity.MailTemplate;
import cn.com.cnfic.contractmanage.userAccount.mapper.MailTemplateMapper;
import cn.com.cnfic.contractmanage.userAccount.service.IMailTemplateService;
import cn.com.cnfic.contractmanage.userAccount.vo.MailTemplateVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 邮件模板表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
@Service
public class MailTemplateServiceImpl extends BaseServiceImpl<MailTemplateMapper, MailTemplate> implements IMailTemplateService {

	@Override
	public IPage<MailTemplateVO> selectMailTemplatePage(IPage<MailTemplateVO> page, MailTemplateVO mailTemplate) {
		return page.setRecords(baseMapper.selectMailTemplatePage(page, mailTemplate));
	}

}

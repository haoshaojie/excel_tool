package cn.com.cnfic.contractmanage.userAccount.service.impl;


import cn.com.cnfic.contractmanage.userAccount.entity.MailProductUrl;
import cn.com.cnfic.contractmanage.userAccount.mapper.MailProductUrlMapper;
import cn.com.cnfic.contractmanage.userAccount.service.IMailProductUrlService;
import cn.com.cnfic.contractmanage.userAccount.vo.MailProductUrlVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 邮件模板产品下载链接表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
@Service
public class MailProductUrlServiceImpl extends BaseServiceImpl<MailProductUrlMapper, MailProductUrl> implements IMailProductUrlService {

	@Override
	public IPage<MailProductUrlVO> selectMailProductUrlPage(IPage<MailProductUrlVO> page, MailProductUrlVO mailProductUrl) {
		return page.setRecords(baseMapper.selectMailProductUrlPage(page, mailProductUrl));
	}

}

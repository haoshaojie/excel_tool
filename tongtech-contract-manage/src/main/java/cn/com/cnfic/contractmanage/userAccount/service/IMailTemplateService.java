package cn.com.cnfic.contractmanage.userAccount.service;


import cn.com.cnfic.contractmanage.userAccount.entity.MailTemplate;
import cn.com.cnfic.contractmanage.userAccount.vo.MailTemplateVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

/**
 * 邮件模板表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public interface IMailTemplateService extends BaseService<MailTemplate> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param mailTemplate
	 * @return
	 */
	IPage<MailTemplateVO> selectMailTemplatePage(IPage<MailTemplateVO> page, MailTemplateVO mailTemplate);

}

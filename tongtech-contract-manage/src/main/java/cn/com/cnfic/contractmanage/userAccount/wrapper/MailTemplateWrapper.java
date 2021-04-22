package cn.com.cnfic.contractmanage.userAccount.wrapper;


import cn.com.cnfic.contractmanage.userAccount.entity.MailTemplate;
import cn.com.cnfic.contractmanage.userAccount.vo.MailTemplateVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 邮件模板表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public class MailTemplateWrapper extends BaseEntityWrapper<MailTemplate, MailTemplateVO> {

    public static MailTemplateWrapper build() {
        return new MailTemplateWrapper();
    }

	@Override
	public MailTemplateVO entityVO(MailTemplate mailTemplate) {
		MailTemplateVO mailTemplateVO = BeanUtil.copy(mailTemplate, MailTemplateVO.class);

		return mailTemplateVO;
	}

}

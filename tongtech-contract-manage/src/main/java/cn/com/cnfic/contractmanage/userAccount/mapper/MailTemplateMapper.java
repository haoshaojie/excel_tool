package cn.com.cnfic.contractmanage.userAccount.mapper;


import cn.com.cnfic.contractmanage.userAccount.entity.MailTemplate;
import cn.com.cnfic.contractmanage.userAccount.vo.MailTemplateVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 邮件模板表 Mapper 接口
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public interface MailTemplateMapper extends BaseMapper<MailTemplate> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param mailTemplate
	 * @return
	 */
	List<MailTemplateVO> selectMailTemplatePage(IPage page, MailTemplateVO mailTemplate);

}

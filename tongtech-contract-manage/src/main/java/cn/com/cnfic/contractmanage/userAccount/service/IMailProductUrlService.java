package cn.com.cnfic.contractmanage.userAccount.service;


import cn.com.cnfic.contractmanage.userAccount.entity.MailProductUrl;
import cn.com.cnfic.contractmanage.userAccount.vo.MailProductUrlVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;

/**
 * 邮件模板产品下载链接表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public interface IMailProductUrlService extends BaseService<MailProductUrl> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param mailProductUrl
	 * @return
	 */
	IPage<MailProductUrlVO> selectMailProductUrlPage(IPage<MailProductUrlVO> page, MailProductUrlVO mailProductUrl);

}

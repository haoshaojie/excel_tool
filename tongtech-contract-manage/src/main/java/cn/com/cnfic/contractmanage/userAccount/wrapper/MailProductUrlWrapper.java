package cn.com.cnfic.contractmanage.userAccount.wrapper;


import cn.com.cnfic.contractmanage.userAccount.entity.MailProductUrl;
import cn.com.cnfic.contractmanage.userAccount.vo.MailProductUrlVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 邮件模板产品下载链接表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public class MailProductUrlWrapper extends BaseEntityWrapper<MailProductUrl, MailProductUrlVO> {

    public static MailProductUrlWrapper build() {
        return new MailProductUrlWrapper();
    }

	@Override
	public MailProductUrlVO entityVO(MailProductUrl mailProductUrl) {
		MailProductUrlVO mailProductUrlVO = BeanUtil.copy(mailProductUrl, MailProductUrlVO.class);

		return mailProductUrlVO;
	}

}

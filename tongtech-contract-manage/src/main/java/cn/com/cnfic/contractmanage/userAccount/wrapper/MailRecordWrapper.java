package cn.com.cnfic.contractmanage.userAccount.wrapper;


import cn.com.cnfic.contractmanage.userAccount.entity.MailRecord;
import cn.com.cnfic.contractmanage.userAccount.vo.MailRecordVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 邮件发送记录表包装类,返回视图层所需的字段
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public class MailRecordWrapper extends BaseEntityWrapper<MailRecord, MailRecordVO> {

    public static MailRecordWrapper build() {
        return new MailRecordWrapper();
    }

	@Override
	public MailRecordVO entityVO(MailRecord mailRecord) {
		MailRecordVO mailRecordVO = BeanUtil.copy(mailRecord, MailRecordVO.class);

		return mailRecordVO;
	}

}

package cn.com.cnfic.contractmanage.userAccount.service;


import cn.com.cnfic.contractmanage.userAccount.entity.MailRecord;
import cn.com.cnfic.contractmanage.userAccount.model.TemplateEmailParam;
import cn.com.cnfic.contractmanage.userAccount.vo.MailRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.tool.api.R;

/**
 * 邮件发送记录表 服务类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-23
 */
public interface IMailRecordService extends BaseService<MailRecord> {

    /**
     * 发送模版邮件
     *
     * @param emailParam
     * @return
     */
    R sendTemplateMail(TemplateEmailParam emailParam);

    /**
     * 自定义分页
     *
     * @param page
     * @param mailRecord
     * @return
     */
    IPage<MailRecordVO> selectMailRecordPage(IPage<MailRecordVO> page, MailRecordVO mailRecord);

}

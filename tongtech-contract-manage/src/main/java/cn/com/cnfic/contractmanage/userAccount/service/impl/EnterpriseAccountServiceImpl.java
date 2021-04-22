package cn.com.cnfic.contractmanage.userAccount.service.impl;

import cn.com.cnfic.contractmanage.userAccount.model.AccountExtraVO;
import cn.com.cnfic.contractmanage.userAccount.service.EnterpriseAccountService;
import cn.com.cnfic.contractmanage.userAccount.service.IMailRecordService;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author misterbig
 * @Date 2021/4/6
 */
@AllArgsConstructor
@Service
public class EnterpriseAccountServiceImpl implements EnterpriseAccountService {

    /**
     * 欢迎邮件模版名称
     * {@value}
     */
    private static final String welcome_email_template = "Welcome.ftl";

    private final IMailRecordService mailRecordService;

    @Override
    public R welcomeEmail(String accIds) {
        return mailRecordService.sendTemplateMail(null);
    }

    @Override
    public R<AccountExtraVO<Object>> userProductContract(Object object) {

        return null;
    }

    @Override
    public R<AccountExtraVO<Object>> userDetailProduct(Object object) {
        return null;
    }

    @Override
    public R<AccountExtraVO<Object>> userDetailContract(Object object) {
        return null;
    }

    @Override
    public R userDetail(Object object) {
        return null;
    }

    @Override
    public R<Object> listUser(Object object, Query query) {
        return null;
    }
}

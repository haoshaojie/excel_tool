package cn.com.cnfic.contractmanage.userAccount.service;

import cn.com.cnfic.contractmanage.userAccount.model.AccountExtraVO;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;

/**
 * @Description 企业账号服务中转中心
 * @Author misterbig
 * @Date 2021/3/31
 */
public interface EnterpriseAccountService {

    /**
     * 发送欢迎邮件
     *
     * @param accIds
     * @return
     */
    R welcomeEmail(String accIds);

    /**
     * 企业账号-用户账号详情-产品权限-合约信息
     *
     * @return
     */
    R<AccountExtraVO<Object>> userProductContract(Object object);

    /**
     * 企业账号-用户账号详情-产品权限
     *
     * @param object
     * @return
     */
    R<AccountExtraVO<Object>> userDetailProduct(Object object);

    /**
     * 企业账号-用户账号详情-合约信息
     *
     * @param object
     * @return
     */
    R<AccountExtraVO<Object>> userDetailContract(Object object);

    /**
     * 企业账号-用户账号详情
     *
     * @param object
     * @return
     */
    R userDetail(Object object);

    /**
     * 企业账号-用户账号列表分页查询
     *
     * @param object
     * @param query
     * @return
     */
    R<Object> listUser(Object object, Query query);

}

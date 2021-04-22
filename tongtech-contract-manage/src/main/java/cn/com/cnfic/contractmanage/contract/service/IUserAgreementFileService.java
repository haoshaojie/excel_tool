package cn.com.cnfic.contractmanage.contract.service;


import cn.com.cnfic.contractmanage.contract.entity.UserAgreementFile;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 用户协议附件表 服务类
 *
 * @author TongTech-UserManage
 * @since 2021-03-05
 */
public interface IUserAgreementFileService extends BaseService<UserAgreementFile> {

    /**
     * 根据用户ID查询用户协议附件表
     *
     * @param agreementId
     * @return List<UserAgreementFile>
     */
    List<UserAgreementFile> getUserAgreementFileLisstByAgreementId(Long agreementId);
}

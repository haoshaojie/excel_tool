package cn.com.cnfic.contractmanage.contract.service;


import cn.com.cnfic.contractmanage.contract.entity.ContractFile;
import cn.com.cnfic.contractmanage.contract.entity.UserAgreementFile;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 合约附件表 服务类
 *
 * @author zhaijw
 * @since 2021-04-15
 */
public interface IContractFileService extends BaseService<ContractFile> {

    /**
     * 根据合约ID查询合约附件列表表
     *
     * @param contId
     * @return List<ContractFile>
     */
    List<ContractFile> getContractFileListByContId(Long contId);
}

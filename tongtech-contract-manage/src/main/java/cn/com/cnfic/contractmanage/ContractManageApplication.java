package cn.com.cnfic.contractmanage;

import cn.com.cnfic.common.annotation.TongTechCloudApplication;
import cn.com.cnfic.common.constant.TongTechConstant;
import cn.com.cnfic.common.core.TongTechApplication;

/**
 * 合约管理启动器
 *
 * @author xuchy
 */
@TongTechCloudApplication
public class ContractManageApplication {

    public static void main(String[] args) {
        TongTechApplication.run(TongTechConstant.APPLICATION_CONTRACT_MANAGE_NAME, ContractManageApplication.class, args);
    }

}


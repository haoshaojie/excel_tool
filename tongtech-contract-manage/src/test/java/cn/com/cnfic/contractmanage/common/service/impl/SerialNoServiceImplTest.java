package cn.com.cnfic.contractmanage.common.service.impl;

import cn.com.cnfic.common.constant.TongTechConstant;
import cn.com.cnfic.contractmanage.ContractManageApplication;
import cn.com.cnfic.contractmanage.common.service.ISerialNoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(BladeSpringExtension.class)
@SpringBootTest(classes = ContractManageApplication.class)
@BladeBootTest(appName = TongTechConstant.APPLICATION_CONTRACT_MANAGE_NAME, profile = "dev", enableLoader = true)
public class SerialNoServiceImplTest {

    @Autowired
    private ISerialNoService serialNoService;

    @Test
    public void getSerialNo() {
        String serialNo = serialNoService.getSerialNo("contractNo", "XY", 4);
        System.out.println(serialNo);
    }
}

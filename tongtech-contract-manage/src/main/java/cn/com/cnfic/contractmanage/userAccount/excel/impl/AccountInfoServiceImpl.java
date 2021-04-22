package cn.com.cnfic.contractmanage.userAccount.excel.impl;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.accountmanage.feign.AccountFeign;
import cn.com.cnfic.contractmanage.userAccount.excel.AccountExportDTO;
import cn.com.cnfic.contractmanage.userAccount.excel.ExcelDataServiceOfAccount;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Description 账号数据查询服务
 * @Author misterbig
 * @Date 2021/4/9
 */
@AllArgsConstructor
@Service
public class AccountInfoServiceImpl implements ExcelDataServiceOfAccount<AccInfoForTreaty, AccountExportDTO> {

    private final AccountFeign accountFeign;

    @Override
    public List<AccInfoForTreaty> selectData(AccountExportDTO param) {
        R<List<AccInfoForTreaty>> result = accountFeign.accExportForTreaty(param);
        return R.isSuccess(result) ? result.getData() : Collections.emptyList();
    }
}

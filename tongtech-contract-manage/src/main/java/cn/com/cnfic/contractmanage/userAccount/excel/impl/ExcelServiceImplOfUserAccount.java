package cn.com.cnfic.contractmanage.userAccount.excel.impl;

import cn.com.cnfic.accountmanage.entity.AccInfoForTreaty;
import cn.com.cnfic.contractmanage.userAccount.excel.*;
import cn.com.cnfic.system.entity.Dict;
import cn.com.cnfic.system.feign.IDictClient;
import com.alibaba.excel.ExcelWriter;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toMap;

/**
 * @Description 用户账号管理excel服务实现
 * @Author misterbig
 * @Date 2021/4/8
 */
@Slf4j
@AllArgsConstructor
@Service
public class ExcelServiceImplOfUserAccount implements ExcelServiceOfUserAccount {

    /**
     * 企业账号导出excel文件名称
     * {@value}
     */
    private static final String ENTERPRISE_EXCEL_FILE_NAME = "企业账号导出";
    /**
     * 个人账号导出excel文件名称
     * {@value}
     */
    private static final String PERSONAL_EXCEL_FILE_NAME = "个人账号导出";
    /**
     * 个人账号导出excel文件名称
     * {@value}
     */
    private static final String SHEET_ACCOUNT_NAME = "账号信息";
    /**
     * 个人账号导出excel文件名称
     * {@value}
     */
    private static final String SHEET_ORDER_NAME = "订单信息";
    /**
     * 个人账号导出excel文件名称
     * {@value}
     */
    private static final String SHEET_CONTRACT_NAME = "合约信息";
    /**
     * 个人账号导出excel文件名称
     * {@value}
     */
    private static final String SHEET_PRODUCT_NAME = "产品信息";
    /**
     * 账号状态数据字典获取key
     * {@value}
     */
    private static final String ACCINFOFORTREATY_ACCSTATUS_DICT_CODE = "accStatus";
    /**
     * 注册渠道数据字典获取key
     * {@value}
     */
    private static final String ACCINFOFORTREATY_REGAPPCHANNELTYPE_DICT_CODE = "regAppChannelType";

    private final List<ExcelDataServiceOfAccount> excelDataServiceOfAccounts;
    private final IDictClient dictClient;

    @SneakyThrows
    @Override
    public void exportEnterpriseAccount(AccountExportDTO accountExportDTO, HttpServletResponse response) {
        //账号数据
        CompletableFuture<List<AccInfoForTreaty>> accountFuture = getDataAsync(
                getDataClient(excelDataServiceOfAccounts, AccountInfoServiceImpl.class),
                accountExportDTO);
        //合约信息
        // TODO: 2021/4/9实现账号对应的合约信息获取：翟继巍
        CompletableFuture<List<AccInfoForTreaty>> contractFuture = null;
        //产品信息
        // TODO: 2021/4/9实现账号对应的产品信息获取：沙正秋
        CompletableFuture<List<AccInfoForTreaty>> productFuture = null;
        //获取数据字典对应的数据项
        CompletableFuture<Map<String, ExcelDynamicCover>> coverFuture = CompletableFuture.supplyAsync(() -> {
            Map<String, ExcelDynamicCover> covers = Maps.newHashMap();
            return covers;
        });
        CompletableFuture.allOf(accountFuture, coverFuture).join();
        responseConfig(response, ENTERPRISE_EXCEL_FILE_NAME);
        ExcelWriter excelWriter = getExcelWriter(response);
        try {
            int sheetIndex = 0;
            //写入账号数据
            writeExcel(excelWriter, new ExcelSheetDTO(sheetIndex,
                    SHEET_ACCOUNT_NAME,
                    dynamicHeadbuild(accountExportDTO.getFieldName()),
                    dynamicDatabuild(accountExportDTO.getExportField(), accountFuture.get(), coverFuture.get()),
                    null));
            //写入合约数据
            if (accountExportDTO.getOrderChecked()) {
                writeExcel(excelWriter, new ExcelSheetDTO(++sheetIndex,
                        SHEET_CONTRACT_NAME,
                        dynamicHeadbuild(accountExportDTO.getFieldName()),
                        Collections.emptyList(),
                        null));
            }
            //写入产品数据
            if (accountExportDTO.getOrderChecked()) {
                writeExcel(excelWriter, new ExcelSheetDTO(++sheetIndex,
                        SHEET_PRODUCT_NAME,
                        dynamicHeadbuild(accountExportDTO.getFieldName()),
                        Collections.emptyList(),
                        null));
            }
        } finally {
            if (Objects.nonNull(excelWriter)) {
                excelWriter.finish();
            }
        }
    }

    @SneakyThrows
    @Override
    public void exportPersonalAccount(AccountExportDTO accountExportDTO, HttpServletResponse response) {
        //账号数据
        List<AccInfoForTreaty> accInfoForTreaties = getData(
                getDataClient(excelDataServiceOfAccounts, AccountInfoServiceImpl.class),
                accountExportDTO);
        //查询订单数据
        AccountOrderDTO accountOrderDTO = BeanUtil.copy(accountExportDTO, AccountOrderDTO.class);
        accountOrderDTO.setOrderParams(accInfoForTreaties);
        CompletableFuture<List<AccountOrderExport>> orderFuture = getDataAsync(
                getDataClient(excelDataServiceOfAccounts, AccountOrderServiceImpl.class),
                accountOrderDTO);
        //获取数据字典对应的数据项
        CompletableFuture<Map<String, ExcelDynamicCover>> coverFuture = CompletableFuture.supplyAsync(() -> {
            Map<String, ExcelDynamicCover> covers = Maps.newHashMap();
            putCover(covers, ACCINFOFORTREATY_ACCSTATUS_DICT_CODE);
            putCover(covers, ACCINFOFORTREATY_REGAPPCHANNELTYPE_DICT_CODE);
            return covers;
        });
        CompletableFuture.allOf(orderFuture, coverFuture).join();
        responseConfig(response, PERSONAL_EXCEL_FILE_NAME);
        ExcelWriter excelWriter = getExcelWriter(response);
        try {
            int sheetIndex = 0;
            //写入用户数据
            writeExcel(excelWriter, new ExcelSheetDTO(sheetIndex,
                    SHEET_ACCOUNT_NAME,
                    dynamicHeadbuild(accountExportDTO.getFieldName()),
                    dynamicDatabuild(accountExportDTO.getExportField(), accInfoForTreaties, coverFuture.get()),
                    null));
            //写入订单数据
            if (accountExportDTO.getOrderChecked()) {
                writeExcel(excelWriter, new ExcelSheetDTO(++sheetIndex,
                        SHEET_ORDER_NAME,
                        null,
                        orderFuture.get(),
                        AccountOrderExport.class));
            }
        } finally {
            if (Objects.nonNull(excelWriter)) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 获取转换数据，并添加到转换集合
     *
     * @param covers
     * @param fieldDictCode
     */
    private void putCover(Map<String, ExcelDynamicCover> covers, String fieldDictCode) {
        R<List<Dict>> registerChannel = dictClient.getList(fieldDictCode);
        if (R.isSuccess(registerChannel)) {
            List<Dict> dicts = registerChannel.getData();
            if (!CollectionUtils.isEmpty(dicts)) {
                covers.put(fieldDictCode, new ExcelDynamicCover() {{
                    setField(fieldDictCode);
                    setValues(dicts.stream().collect(toMap(Dict::getDictKey, Dict::getDictValue, (v1, v2) -> v2)));
                }});
            }
        }
    }

}

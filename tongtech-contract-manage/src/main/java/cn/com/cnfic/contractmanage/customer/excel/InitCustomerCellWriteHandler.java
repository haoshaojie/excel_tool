package cn.com.cnfic.contractmanage.customer.excel;

import cn.com.cnfic.contractmanage.customer.constant.CustomerConstants;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;

/**
 * @desc 客户基本信息导出模板实体拦截器
 * @auther yangchuan
 * @date 2021/3/26
 */
@NoArgsConstructor
public class InitCustomerCellWriteHandler implements CellWriteHandler {
    private List<String> custTypes;
    private List<String> custStates;
    private List<String> businessChances;

    public InitCustomerCellWriteHandler(List<String> custTypes, List<String> custStates, List<String> businessChances) {
        this.custTypes = custTypes;
        this.custStates = custStates;
        this.businessChances = businessChances;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        //是否添加 下拉
        boolean flag = false;
        // sheet 1 客户基本信息
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(3, 2000, 1, 1);
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(this.custTypes.toArray(new String[]{}));
        DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
        Sheet sheet = writeSheetHolder.getSheet();
        if (sheet.getSheetName().equals(CustomerConstants.EXPORT_CUSTOMER_SHEET1_NAME)) {
            flag = true;
        }
        if (flag) {
            sheet.addValidationData(dataValidation);
        }

        // sheet2客户状态信息
        if (flag) {
            CellRangeAddressList cellRangeAddressList2 = new CellRangeAddressList(3, 2000, 13, 13);
            DataValidationHelper helper2 = writeSheetHolder.getSheet().getDataValidationHelper();
            DataValidationConstraint constraint2 = helper.createExplicitListConstraint(this.custStates.toArray(new String[]{}));
            DataValidation dataValidation2 = helper2.createValidation(constraint2, cellRangeAddressList2);
            writeSheetHolder.getSheet().addValidationData(dataValidation2);
        }

        // sheet2客户业务机会状态信息
        if (flag) {
            CellRangeAddressList cellRangeAddressList3 = new CellRangeAddressList(3, 2000, 14, 14);
            DataValidationHelper helper3 = writeSheetHolder.getSheet().getDataValidationHelper();
            DataValidationConstraint constraint3 = helper3.createExplicitListConstraint(this.businessChances.toArray(new String[]{}));
            DataValidation dataValidation3 = helper3.createValidation(constraint3, cellRangeAddressList3);
            writeSheetHolder.getSheet().addValidationData(dataValidation3);
        }
    }
}

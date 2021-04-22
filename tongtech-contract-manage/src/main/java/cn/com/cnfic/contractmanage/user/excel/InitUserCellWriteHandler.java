package cn.com.cnfic.contractmanage.user.excel;

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
 * @desc 初始化用户导出模板Handler
 * @auther yangchuan
 * @date 2021/4/1
 */
@NoArgsConstructor
public class InitUserCellWriteHandler  implements CellWriteHandler {
     private List<String> userTypes;
     private List<String> userSexs;
     private List<String> managers;
    public InitUserCellWriteHandler(List<String> userTypes,List<String> userSexs,List<String> managers) {
        this.userTypes=userTypes;
        this.userSexs=userSexs;
        this.managers=managers;
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
        //补充用户类型下拉
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(3, 2000, 9, 9);
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(this.userTypes.toArray(new String[]{}));
        DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
        writeSheetHolder.getSheet().addValidationData(dataValidation);

        // 性别下拉
        CellRangeAddressList cellRangeAddressList2 = new CellRangeAddressList(3, 2000, 3, 3);
        DataValidationHelper helper2 = writeSheetHolder.getSheet().getDataValidationHelper();
        DataValidationConstraint constraint2 = helper.createExplicitListConstraint(this.userSexs.toArray(new String[]{}));
        DataValidation dataValidation2 = helper2.createValidation(constraint2, cellRangeAddressList2);
        writeSheetHolder.getSheet().addValidationData(dataValidation2);
        // 客户经理下拉
        CellRangeAddressList cellRangeAddressList3 = new CellRangeAddressList(3, 2000, 11, 11);
        DataValidationHelper helper3 = writeSheetHolder.getSheet().getDataValidationHelper();
        DataValidationConstraint constraint3 = helper3.createExplicitListConstraint(this.managers.toArray(new String[]{}));
        DataValidation dataValidation3 = helper3.createValidation(constraint3, cellRangeAddressList3);
        writeSheetHolder.getSheet().addValidationData(dataValidation3);
    }
}

package cn.com.cnfic.contractmanage.customer.excel;

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
 * @desc 客户联系人机构信息导出模板实体拦截器
 * @auther yangchuan
 * @date 2021/3/26
 */
@NoArgsConstructor
public class InitCustOrgCellWriteHandler  implements CellWriteHandler {
    private List<String> deptNames;
    public InitCustOrgCellWriteHandler(List<String> deptNames) {
        this.deptNames=deptNames;
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
            // 区间设置 第一列第一行和第二行的数据。由于第一行是头，所以第一、二行的数据实际上是第二三行
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(3, 2000, 9, 9);
            DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
            DataValidationConstraint constraint = helper.createExplicitListConstraint(this.deptNames.toArray(new String[]{}));
            DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
            writeSheetHolder.getSheet().addValidationData(dataValidation);
    }
}

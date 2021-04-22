package cn.com.cnfic.contractmanage.blackList.excel;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;

/**
 * @Description 黑白名单导入模版设置黑白名单类型下拉
 * @Author misterbig
 * @Date 2021/3/15
 * @see ExcelConstant
 */
@Slf4j
public class TypeOfBlackWhiteListCellWriteHandler implements CellWriteHandler {

    /**
     * 黑白名单导入模版黑白名单类型默认初始化行数
     * {@value}
     */
    private static final CellRangeAddressList CELL_RANGE_ADDRESS_LIST = new CellRangeAddressList(ExcelConstant.INIT_TYPE_START_ROW, ExcelConstant.INIT_TYPE_END_ROW, ExcelConstant.INIT_TYPE_CELL_INDEX, ExcelConstant.INIT_TYPE_CELL_INDEX);
    /**
     * 黑白名单导入模版黑白名单类型下拉数据
     * {@value}
     */
    private static final String[] EXPLICITLIST_VALUE = {"黑名单", "白名单"};


    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (cell.getColumnIndex() == ExcelConstant.INIT_TYPE_CELL_INDEX) {
            DataValidationHelper validationHelper = writeSheetHolder.getSheet().getDataValidationHelper();
            DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(EXPLICITLIST_VALUE);
            DataValidation dataValidation = validationHelper.createValidation(constraint, CELL_RANGE_ADDRESS_LIST);
            writeSheetHolder.getSheet().addValidationData(dataValidation);
        }
    }
}

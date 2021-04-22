package cn.com.cnfic.contractmanage.blackList.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.utils.StringUtil;

import java.util.Objects;

/**
 * @Description 黑白名单类型转换器
 * @Author misterbig
 * @Date 2021/3/16
 */
public class TypeOfBlackWhiteListConverter implements Converter<Integer> {
    private static final String[] TYPE_VALUE = {"", "黑名单", "白名单"};

    @Override
    public Class supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return Objects.nonNull(cellData) ?
                (StringUtil.equals(TYPE_VALUE[1], cellData.getStringValue()) ? 1 :
                        (StringUtil.equals(TYPE_VALUE[2], cellData.getStringValue()) ? 2 : null)) : null;
    }

    @Override
    public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData(Objects.nonNull(value) ? (value.equals(1) ? TYPE_VALUE[1] :
                (value.equals(2) ? TYPE_VALUE[2] : StringUtils.EMPTY)) : StringUtils.EMPTY);
    }
}

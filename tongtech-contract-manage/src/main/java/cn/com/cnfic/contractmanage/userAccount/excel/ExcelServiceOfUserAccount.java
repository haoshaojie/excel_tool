package cn.com.cnfic.contractmanage.userAccount.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springblade.core.tool.utils.Charsets;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

/**
 * @Description 用户账号excel服务接口
 * @Author misterbig
 * @Date 2021/4/8
 */
public interface ExcelServiceOfUserAccount {
    /**
     * 企业账号管理导出
     *
     * @param response
     */
    void exportEnterpriseAccount(AccountExportDTO accountExportDTO, HttpServletResponse response);

    /**
     * 个人账号管理导出
     *
     * @param response
     */
    void exportPersonalAccount(AccountExportDTO accountExportDTO, HttpServletResponse response);

    /**
     * 获取数据
     *
     * @param client
     * @param param
     * @param <T>
     * @param <E>
     * @param <R>
     * @return
     */
    default <T extends ExcelDataServiceOfAccount, E extends AccountExportDTO, R extends Object> List<R> getData(T client, E param) {
        if (Objects.nonNull(client)) {
            return client.selectData(param);
        }
        return Collections.emptyList();
    }

    /**
     * 异步获取数据
     *
     * @param client
     * @param param
     * @param <T>
     * @param <E>
     * @param <R>
     * @return
     */
    default <T extends ExcelDataServiceOfAccount, E extends AccountExportDTO, R extends Object> CompletableFuture<List<R>> getDataAsync(T client, E param) {
        if (Objects.nonNull(client)) {
            return client.selectDataAsync(param);
        }
        return CompletableFuture.supplyAsync(() -> {
            return Lists.newArrayList();
        });
    }

    /**
     * 获取用户账号数据服务
     *
     * @param dataServiceOfAccounts
     * @param tClass
     * @param <T>
     * @return
     */
    default <T extends ExcelDataServiceOfAccount> T getDataClient(List<ExcelDataServiceOfAccount> dataServiceOfAccounts, Class<T> tClass) {
        return (T) dataServiceOfAccounts.stream().filter(i -> i.getClass().equals(tClass)).findFirst().orElse(null);
    }

    /**
     * excel动态表头构建
     *
     * @param fieldNames
     * @return
     */
    default List<List<String>> dynamicHeadbuild(List<String> fieldNames) {
        return fieldNames.stream().map(n -> {
            return Lists.newArrayList(StringUtils.trimToEmpty(n));
        }).collect(toList());
    }

    /**
     * excel动态表头对应的行数据构建
     *
     * @param exportFields
     * @param data
     * @param covers
     * @param <T>
     * @param <E>
     * @return
     */
    default <T extends Object, E extends Object> List<E> dynamicDatabuild(List<String> exportFields, List<T> data,
                                                                          Map<String, ExcelDynamicCover> covers) {
        List rows = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(d -> {
                List row = exportFields.stream().map(f -> {
                    Object fieldValue = null;
                    try {
                        fieldValue = FieldUtils.readDeclaredField(d, f, true);
                        ExcelDynamicCover dynamicCover = covers.get(f);
                        if (Objects.nonNull(dynamicCover)) {
                            fieldValue = dynamicCover.getValues().getOrDefault(Integer.valueOf(fieldValue.toString()), StringUtils.EMPTY);
                        }
                    } catch (IllegalAccessException | NumberFormatException e) {
                        System.err.println("default <T extends Object, E extends Object> List<E> dynamicDatabuild: " +
                                e.getMessage());
                        fieldValue = StringUtils.EMPTY;
                    }
                    return fieldValue.toString();
                }).collect(toList());
                rows.add(row);
            });
        }
        return rows;
    }

    /**
     * 写入数据表
     *
     * @param excelWriter
     * @param excelSheetDTO
     * @param <T>
     */
    default <T extends ExcelSheetDTO> void writeExcel(ExcelWriter excelWriter, T excelSheetDTO) {
        ExcelWriterSheetBuilder sheetBuilder = EasyExcel.writerSheet(excelSheetDTO.getSheetNo(), excelSheetDTO.getName());
        WriteSheet writeSheet;
        if (Objects.nonNull(excelSheetDTO.getFixedHead())) {
            writeSheet = sheetBuilder.head(excelSheetDTO.getFixedHead()).build();
        } else {
            writeSheet = sheetBuilder.head(excelSheetDTO.getDynamicHead()).build();
        }
        excelWriter.write(excelSheetDTO.getData(), writeSheet);
    }

    /**
     * 获取excelWriter
     *
     * @param response
     * @return
     */
    @SneakyThrows
    default ExcelWriter getExcelWriter(HttpServletResponse response) {
        return EasyExcel.write(response.getOutputStream()).build();
    }

    /**
     * 响应对象配置
     *
     * @param response
     * @param fileName
     * @return
     */
    @SneakyThrows
    default HttpServletResponse responseConfig(HttpServletResponse response, String fileName) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(Charsets.UTF_8.name());
        String fileNameEncode = URLEncoder.encode(fileName, Charsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ".xlsx");
        return response;
    }
}

package cn.com.cnfic.contractmanage.product.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.write.handler.SheetWriteHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class ExcelUtil {
    public ExcelUtil() {
    }

    public static <T> void export(HttpServletResponse response, List<T> dataList, Class<T> clazz) throws IOException {
        try {
            export(response, DateUtils.format(new Date(), "yyyyMMddHHmmss"), "导出数据", dataList, clazz);
        } catch (Throwable e) {
            throw e;
        }
    }
    public static <T> void export(HttpServletResponse response, String fileName, String sheetName, List<List<String>> head, List<List<String>> dataList) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream()).head(head).sheet(sheetName).doWrite(dataList);
        } catch (Throwable e) {
            throw e;
        }
    }
    public static <T> void export(HttpServletResponse response, String fileName, String sheetName, List<T> dataList, Class<T> clazz) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(dataList);
        } catch (Throwable e) {
            throw e;
        }
    }
    public static <T> void export(HttpServletResponse response, String fileName, String sheetName, List<T> dataList, Class<T> clazz, SheetWriteHandler handlerClazz) throws IOException {

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clazz).registerWriteHandler(handlerClazz).sheet(sheetName).doWrite(dataList);
        } catch (Exception e) {
            throw e;
        }
    }
}

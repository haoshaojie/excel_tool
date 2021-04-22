package cn.com.cnfic.contractmanage.blackList.excel.impl;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.excel.*;
import cn.com.cnfic.contractmanage.blackList.service.IBlackWhiteListService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @Description
 * @Author misterbig
 * @Date 2021/3/17
 */
@AllArgsConstructor
@Service
public class ExcelServiceImplOfBlackWhiteList implements ExcelServiceOfBlackWhiteList {

    private final IBlackWhiteListService blackWhiteListService;

    private final Validator validator;

    private final RedisUtil redisUtil;

    /**
     * 黑白名单excel导入上传的文件后缀名称，office03
     * {@value}
     */
    private static final String EXCEL_FAIL_SUFFIX_03 = ".xls";
    /**
     * 黑白名单excel导入上传的文件后缀名称，office07
     * {@value}
     */
    private static final String EXCEL_FAIL_SUFFIX_07 = ".xlsx";

    /**
     * 黑白名单导出文件名称
     * {@value}
     */
    private static final String EXPORT_BLACKLIST_FILE = "黑白名单导出模版";
    /**
     * 黑白名单导出模版文件名称
     * {@value}
     */
    private static final String EXPORT_BLACKLIST_TEMPLATE_FILE = "黑白名单导入模版";
    /**
     * redis数据没有获取到，返回提示消息
     * {@value}
     */
    private static final String REDIS_DATA_ERROR_MSG = "上传验证通过的数据已不存在，请重新上传";
    /**
     * redis数据没有获取到，返回提示消息
     * {@value}
     */
    private static final String IMPORT_FILE_EMPTY_MSG = "请上传文件!";
    /**
     * redis数据没有获取到，返回提示消息
     * {@value}
     */
    private static final String IMPORT_FILE_FORMAT_MSG = "请上传正确的excel文件!";

    @Override
    public void exportCheckFailData(HttpServletResponse response, String failRedisKey) {
        List<Object> failRedisData = redisUtil.lGet(failRedisKey, 0, redisUtil.lGetListSize(failRedisKey));
        if (CollectionUtil.isNotEmpty(failRedisData)) {
            List<CheckFailDataExport> checkFailDataExports = failRedisData.stream().map(o -> {
                return BeanUtil.copy(o, CheckFailDataExport.class);
            }).collect(toList());
            this.excelExport(response, EXPORT_BLACKLIST_FILE, CheckFailDataExport.class, checkFailDataExports);
        } else {
            this.excelExport(response, EXPORT_BLACKLIST_FILE, CheckFailDataExport.class, Lists.newArrayList());
        }
    }

    @Override
    public void exportTemplate(HttpServletResponse response) {
        this.excelExport(response, EXPORT_BLACKLIST_TEMPLATE_FILE, BlackWhiteListImport.class, Lists.newArrayList());
    }

    /**
     * excel导出设置
     *
     * @param response
     * @param fileName
     * @param dataClass
     * @param data
     * @param <T>
     */
    @SneakyThrows
    private <T> void excelExport(HttpServletResponse response, String fileName, Class<T> dataClass, List<T> data) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(Charsets.UTF_8.name());
        String fileNameEncode = URLEncoder.encode(fileName, Charsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ".xlsx");
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), dataClass);
        if (StringUtil.equals(EXPORT_BLACKLIST_TEMPLATE_FILE, fileName)) {
            excelWriterBuilder.registerWriteHandler(new TypeOfBlackWhiteListCellWriteHandler());
        }
        excelWriterBuilder.sheet(fileNameEncode).doWrite(data);
    }

    @Override
    public void exportBlackWhiteList(HttpServletResponse response, BlackWhiteList blackWhiteList) {
        List<BlackWhiteListExport> data = blackWhiteListService.selectExport(blackWhiteList);
        this.excelExport(response, EXPORT_BLACKLIST_FILE, BlackWhiteListExport.class, data);
    }

    @Override
    public R submitPassData(String passCacheKey) {
        List<Object> passRedisData = redisUtil.lGet(passCacheKey, 0, redisUtil.lGetListSize(passCacheKey));
        if (CollectionUtil.isEmpty(passRedisData)) {
            return R.fail(REDIS_DATA_ERROR_MSG);
        }
        int success = 0;
        for (Object data : passRedisData) {
            BlackWhiteList blackWhiteList = (BlackWhiteList) data;
            R r = blackWhiteListService.submit(blackWhiteList);
            if (R.isSuccess(r)) {
                success += 1;
            }
        }
        return R.data(success);
    }

    @Override
    public R pageOfCheckPassData(String passRedisKey, Query query) {
        return pageOfRedisData(passRedisKey, query);
    }

    /**
     * redis缓存分页查询
     *
     * @param passCacheKey
     * @param query
     * @return
     */
    private R pageOfRedisData(String passCacheKey, Query query) {
        long cacheSize = redisUtil.lGetListSize(passCacheKey);
        Page<Object> passRedisPage = new Page<>(query.getCurrent(), query.getSize(), cacheSize,
                Boolean.FALSE);
        long current = passRedisPage.getCurrent();
        if (cacheSize < 1L || current < 1L) {
            return R.data(passRedisPage);
        }
        long offset = (current - 1) * passRedisPage.getSize(), end = offset + passRedisPage.getSize();
        passRedisPage.setRecords(redisUtil.lGet(passCacheKey, offset, end));
        return R.data(passRedisPage);
    }

    @Override
    public R pageOfCheckFailData(String failRedisKey, Query query) {
        return pageOfRedisData(failRedisKey, query);
    }

    @Override
    public R importBlackWhiteList(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            throw new IllegalArgumentException(IMPORT_FILE_EMPTY_MSG);
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, EXCEL_FAIL_SUFFIX_03) && !StringUtils.endsWithIgnoreCase(filename, EXCEL_FAIL_SUFFIX_07))) {
            throw new IllegalArgumentException(IMPORT_FILE_FORMAT_MSG);
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImportListenerOfBlackWhiteList importListener = new ImportListenerOfBlackWhiteList(blackWhiteListService, validator, redisUtil);
        ExcelReaderBuilder builder = EasyExcel.read(inputStream, BlackWhiteListImport.class, importListener);
        if (Objects.nonNull(builder)) {
            builder.doReadAll();
        }
        return R.data(importListener.getRedisKeys());
    }
}

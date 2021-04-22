package cn.com.cnfic.contractmanage.userAccount.excel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 用户账号管理excel导出扩展信息服务
 * @Author misterbig
 * @Date 2021/4/9
 */
public interface ExcelDataServiceOfAccount<T, E extends AccountExportDTO> {

    /**
     * 查询excel导出数据
     *
     * @param param
     * @return
     */
    List<T> selectData(E param);

    /**
     * 异步查询excel导出数据
     *
     * @param param
     * @return
     */
    default CompletableFuture<List<T>> selectDataAsync(E param) {
        return CompletableFuture.supplyAsync(() -> {
            return this.selectData(param);
        });
    }
}

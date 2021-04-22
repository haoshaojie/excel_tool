package cn.com.cnfic.contractmanage.blackList.excel;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.service.IBlackWhiteListService;
import cn.com.cnfic.contractmanage.blackList.vo.BlackWhiteListVO;
import cn.com.cnfic.contractmanage.blackList.wrapper.BlackWhiteListWrapper;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.com.cnfic.contractmanage.blackList.excel.ExcelConstant.*;
import static java.util.stream.Collectors.toList;

/**
 * @Description 黑白名单导入监听
 * @Author misterbig
 * @Date 2021/3/16
 */
@EqualsAndHashCode(callSuper = true)
public class ImportListenerOfBlackWhiteList extends AnalysisEventListener<BlackWhiteListImport> {
    /**
     * 数据校验失败对应的excel行号
     * {@value}
     */
    private static final String ERROR_ROW_INDEX_PREFIX = "错误行号：";
    /**
     * 返回rediskes的key
     */
    private static final String PASS_KEY = "passRedisKey";
    private static final String FAIL_KEY = "failRedisKey";
    /**
     * 验证通过的数据
     */
    private List<Object> passData = Lists.newArrayList();
    /**
     * 验证失败的数据
     */
    private List<Object> failData = Lists.newArrayList();

    private final IBlackWhiteListService blackWhiteListService;

    private final Validator validator;

    private final RedisUtil redisUtil;

    @Getter
    private final String passRedisKey;
    @Getter
    private final String failRedisKey;

    {
        String suffix = Integer.toHexString(this.hashCode());
        this.passRedisKey = EXCEL_PASS_REDIS_KEY_PREFIX + suffix;
        this.failRedisKey = EXCEL_FAIL_REDIS_KEY_PREFIX + suffix;
    }

    public ImportListenerOfBlackWhiteList(IBlackWhiteListService blackWhiteListService, Validator validator, RedisUtil redisUtil) {
        this.blackWhiteListService = blackWhiteListService;
        this.validator = validator;
        this.redisUtil = redisUtil;
    }

    @Override
    public void invoke(BlackWhiteListImport data, AnalysisContext context) {
        //数据校验和分别存储
        checkDivideData(data, context.readRowHolder().getRowIndex());
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (passData.size() >= BATCH_INSERT_COUNT) {
            passDataPushToRedis();
        }
        if (failData.size() >= BATCH_INSERT_COUNT) {
            failDataPushToRedis();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        passDataPushToRedis();
        failDataPushToRedis();

    }

    /**
     * 验证通过的数据推送到redis缓存，并设置默认的过期时间{@linkplain ExcelConstant#EXCEL_REDIS_EXPIRE_TIME}
     */
    private void passDataPushToRedis() {
        if (!passData.isEmpty()){
            redisUtil.lSet(passRedisKey, passData, EXCEL_REDIS_EXPIRE_TIME);
            passData.clear();
        }
    }

    /**
     * 验证失败的数据推送到redis缓存，并设置默认的过期时间{@linkplain ExcelConstant#EXCEL_REDIS_EXPIRE_TIME}
     */
    private void failDataPushToRedis() {
        if (!failData.isEmpty()){
            redisUtil.lSet(failRedisKey, failData, EXCEL_REDIS_EXPIRE_TIME);
            failData.clear();
        }
    }

    /**
     * 验证数据并分别存储
     *
     * @param blackWhiteListImport
     * @param rowIndex
     */
    private void checkDivideData(BlackWhiteListImport blackWhiteListImport, int rowIndex) {
        //输入校验
        DataBinder binder = new DataBinder(blackWhiteListImport);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        //业务校验
        BlackWhiteList blackWhiteList = Objects.requireNonNull(BeanUtil.copy(blackWhiteListImport, BlackWhiteList.class));
        String result = blackWhiteListService.checkBlackWhiteListParams(blackWhiteList);
        //校验结果集分隔保存
        if (!bindingResult.hasErrors() && StringUtil.isBlank(result)) {
            passData.add(blackWhiteList);
        } else {
            BlackWhiteListVO blackWhiteListVO = BlackWhiteListWrapper.build().entityVO(blackWhiteList);
            List<String> errorMsg = Lists.newArrayList(ERROR_ROW_INDEX_PREFIX + rowIndex);
            if (bindingResult.hasErrors()) {
                errorMsg.addAll(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(toList()));
            }
            if (StringUtil.isNotBlank(result)) {
                errorMsg.add(result);
            }
            blackWhiteListVO.setErrorMsg(String.join(";", errorMsg));
            failData.add(blackWhiteListVO);
        }
    }

    /**
     * 获取导入数据的rediskeys
     *
     * @return
     */
    public Map<String, String> getRedisKeys() {
        return new HashMap<String, String>(16) {{
            put(PASS_KEY, passRedisKey);
            put(FAIL_KEY, failRedisKey);
        }};
    }
}

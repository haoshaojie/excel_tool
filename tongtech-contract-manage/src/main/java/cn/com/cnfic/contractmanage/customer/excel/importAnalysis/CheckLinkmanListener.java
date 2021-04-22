package cn.com.cnfic.contractmanage.customer.excel.importAnalysis;

import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.excel.CheckContractDataExport;
import cn.com.cnfic.contractmanage.customer.excel.FailContractDataExport;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustomerService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.RedisUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @desc 联系人导入解析监听器
 * @auther yangchuan
 * @date 2021/3/26
 */
@NoArgsConstructor
public class CheckLinkmanListener extends AnalysisEventListener<CheckContractDataExport> {
    private IOrgCustomerService orgCustomerService;
    private Validator validator;
    private RedisUtil redisUtil;
    private Map<String, Long> deptMap;
    private List<Object> failMsg = Lists.newArrayList();
    private List<Object> successData = Lists.newArrayList();
    private Long suff;
    public CheckLinkmanListener(IOrgCustomerService orgCustomerService, Validator validator, RedisUtil redisUtil, Long suff,Map<String, Long> deptMap) {
        this.orgCustomerService = orgCustomerService;
        this.validator = validator;
        this.redisUtil = redisUtil;
        this.deptMap = deptMap;
        this.suff=suff;
    }

    @Override
    public void invoke(CheckContractDataExport checkUserInfoDataExport, AnalysisContext analysisContext) {
        if (ObjectUtils.allNull(checkUserInfoDataExport)) {
            return;
        }
        checkUserInfoField(checkUserInfoDataExport, analysisContext.readRowHolder().getRowIndex());
    }

    /**
     * 检查UserInfo 数据
     * @param contractData excel每行数据
     * @param rowIndex 行号
     */
    private void checkUserInfoField(CheckContractDataExport contractData, Integer rowIndex) {
        DataBinder binder = new DataBinder(contractData);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        Map<String, String> checkOtherData = checkOtherData(contractData);
        String msg = checkOtherData.get(CheckConstants.CHECK_RESULT);
        //必填校验 校验通过
        if (!bindingResult.hasErrors() && StringUtils.isEmpty(msg)) {
            successData.add(contractData);
        } else {
            List<String> errMsg = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(toList());
            FailContractDataExport failUserInfoData = BeanUtil.copy(contractData, FailContractDataExport.class);
            if (StringUtils.isNotEmpty(msg)) {
                errMsg.add(msg);
            }
            StringBuffer msgSb = new StringBuffer().append(CheckConstants.LINE_NUMBER_ERROR).append(rowIndex).append(":").append(StringUtils.join(errMsg, ","));
            failUserInfoData.setMsg(msgSb.toString());
            failMsg.add(failUserInfoData);
        }
    }


    /**
     * 检查下拉框及比对上级单位,省市区划，行业
     *
     * @param contractDataExport excel行数据
     * @return 校验结果及数据库映射类型
     */
    private Map<String, String> checkOtherData(CheckContractDataExport contractDataExport) {
        Map<String, String> result = new HashMap<>();
        //部门验证
        String custOrgName = contractDataExport.getCustOrgName();
        if(StringUtils.isNotEmpty(custOrgName)){
            Long deptId = deptMap.get(custOrgName);
            if (ObjectUtils.allNotNull(deptId)){
                result.put(CheckConstants.LINKMAN_ORG_FIELD,String.valueOf(deptId));
                contractDataExport.setCustOrg(deptId);
            }else {
                result.put(CheckConstants.CHECK_RESULT,CheckConstants.LINKMAN_ORG_ERROR_MSG);
                return result;
            }
        }
        //客户名称编码验证
        String custCode = contractDataExport.getCustCode();
        String custName = contractDataExport.getCustName();
        OrgCustomer data = orgCustomerService.verifCustName(custName, custCode).getData();
        //不存在
        if (ObjectUtils.allNull(data)){
            result.put(CheckConstants.CHECK_RESULT,CheckConstants.LINKMAN_CUSTNAME_CODE_ERROR_MSG);
        }else {
            contractDataExport.setCustId(data.getId());
        }
        return result;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        redisUtil.del(CheckConstants.EXCEL_CONTRACT_PASS_REDIS_KEY_PREFIX+suff,CheckConstants.EXCEL_CONTRACT_FAIL_REDIS_KEY_PREFIX+suff);
        if (failMsg.size()>0) {
            redisUtil.lSet(CheckConstants.EXCEL_CONTRACT_FAIL_REDIS_KEY_PREFIX+suff, failMsg, CheckConstants.EXCEL_REDIS_EXPIRE_TIME);
        }
        if (successData.size()>0) {
            redisUtil.lSet(CheckConstants.EXCEL_CONTRACT_PASS_REDIS_KEY_PREFIX+suff, successData, CheckConstants.EXCEL_REDIS_EXPIRE_TIME);
        }
    }
}

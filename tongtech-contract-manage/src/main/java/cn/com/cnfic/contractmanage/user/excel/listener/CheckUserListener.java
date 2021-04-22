package cn.com.cnfic.contractmanage.user.excel.listener;

import cn.com.cnfic.contractmanage.customer.entity.OrgContact;
import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.service.IOrgContactService;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustomerService;
import cn.com.cnfic.contractmanage.user.excel.FailUserInfo;
import cn.com.cnfic.contractmanage.user.excel.SuccessUserInfo;
import cn.com.cnfic.contractmanage.user.excel.UserExcelExportConstans;
import cn.com.cnfic.contractmanage.user.excel.UserImportTemplate;
import cn.com.cnfic.contractmanage.user.service.IOrgUserService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;
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
 * @desc
 * @auther yangchuan
 * @date 2021/4/1
 */
public class CheckUserListener extends AnalysisEventListener<UserImportTemplate> {
    private IOrgUserService orgUserService;
    private Validator validator;
    private RedisUtil redisUtil;
    private Long redisKey;
    private IOrgContactService contactService;
    private Map<String, Integer> custTypes;
    private Map<String, Integer> sexs;
    private List<Object> failMsg = Lists.newArrayList();
    private List<Object> successData = Lists.newArrayList();
    private IOrgCustomerService customerService;

    public CheckUserListener(IOrgUserService orgUserService, Validator validator, RedisUtil redisUtil, IOrgContactService contactService, Map<String, Integer> custTypes, Long redisKey,IOrgCustomerService customerService,Map<String, Integer> sexs) {
        this.orgUserService = orgUserService;
        this.validator = validator;
        this.redisUtil = redisUtil;
        this.contactService = contactService;
        this.custTypes = custTypes;
        this.redisKey = redisKey;
        this.customerService = customerService;
        this.sexs = sexs;
    }

    @Override
    public void invoke(UserImportTemplate userImportTemplate, AnalysisContext analysisContext) {
        if (ObjectUtils.allNull(userImportTemplate)) {
            return;
        }
        checkUserField(userImportTemplate, analysisContext.readRowHolder().getRowIndex());

    }

    /**
     * 检查导入每行数据字段
     *
     * @param userImportTemplate excel 每行数据
     * @param rowIndex           行号
     */
    private void checkUserField(UserImportTemplate userImportTemplate, Integer rowIndex) {
        DataBinder binder = new DataBinder(userImportTemplate);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        Map<String, String> checkOtherData = checkOtherData(userImportTemplate);
        String msg = checkOtherData.get(UserExcelExportConstans.CHECK_RESULT);
        //必填校验 校验通过
        if (!bindingResult.hasErrors() && StringUtils.isEmpty(msg)) {
            SuccessUserInfo user = buildSuccessUserInfo(userImportTemplate,checkOtherData);
            successData.add(user);
        }else {
            List<String> errMsg = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(toList());
            errMsg.add(msg);
            FailUserInfo failUserInfo = BeanUtil.copy(userImportTemplate, FailUserInfo.class);
            StringBuffer msgSb = new StringBuffer().append(UserExcelExportConstans.LINE_NUMBER_ERROR).append(rowIndex).append(":").append(StringUtils.join(errMsg, ","));

            failUserInfo.setFailMsg(msgSb.toString());
            failMsg.add(failUserInfo);
        }
    }

    /**
     * 构建入库数据
     * @param userImportTemplate excel 数据
     * @param checkOtherData 数据库对应值
     * @return
     */
    private SuccessUserInfo buildSuccessUserInfo(UserImportTemplate userImportTemplate, Map<String, String> checkOtherData) {
        SuccessUserInfo userInfo = BeanUtil.copy(userImportTemplate, SuccessUserInfo.class);
        String managerId = checkOtherData.get(UserExcelExportConstans.MANAGER_ID);
        userInfo.setCustManager(managerId);
        String userType = checkOtherData.get(UserExcelExportConstans.USER_TYPE_VALUE);
        if (StringUtils.isNotEmpty(userType)){
            userInfo.setUserType(Integer.valueOf(userType));
        }
        String sex = checkOtherData.get(UserExcelExportConstans.USER_SEX_VALUE);
        if (StringUtils.isNotEmpty(sex)){
            userInfo.setUserType(Integer.valueOf(userType));
        }
        String custId = checkOtherData.get(UserExcelExportConstans.CUST_ID);

        if (StringUtils.isNotEmpty(sex)){
            userInfo.setCustId(Long.valueOf(custId));
        }
        return userInfo;
    }

    /**
     * 检查用户数据是否符合入库标准
     * @param userImportTemplate 导入实体
     * @return Map<String, String> 返回检查结果提示，获取客户经理的Id
     */
    private Map<String, String> checkOtherData(UserImportTemplate userImportTemplate) {
        Map<String, String> result=new HashMap<>();
        //检查客户是否存在
        OrgCustomer data = customerService.verifCustName(userImportTemplate.getCustName(), userImportTemplate.getCustCode()).getData();
        if (ObjectUtils.allNotNull(data)){
            result.put(UserExcelExportConstans.CUST_ID,data.getId().toString());
        }else {
            result.put(UserExcelExportConstans.CHECK_RESULT,UserExcelExportConstans.ERROR_NOT_CUST);
        }

        //  检查性别类型是否正确
        String sexName = userImportTemplate.getUserSexName();
        if (StringUtils.isNotEmpty(sexName)){
            Integer sexValue = sexs.get(sexName);
            if (ObjectUtils.anyNotNull(sexValue)){
                result.put(UserExcelExportConstans.USER_SEX_VALUE,sexValue.toString());
            }else {
                result.put(UserExcelExportConstans.CHECK_RESULT,UserExcelExportConstans.ERROR_NOT_USER_TYPE);
            }
        }
        //检查客户经理是否存在
        String custManager = userImportTemplate.getCustManagerName();
        if (StringUtils.isNotEmpty(custManager)){
            OrgContact contact=contactService.selectOneByCustManager(custManager);
            if (ObjectUtils.allNotNull(contact)){
                result.put(UserExcelExportConstans.MANAGER_ID,contact.getId().toString());
            }else {
                result.put(UserExcelExportConstans.CHECK_RESULT,UserExcelExportConstans.ERROR_NOT_MANAGER);
            }
        }
        //  检查用户类型是否正确
        String userTypeName = userImportTemplate.getUserTypeName();
        if (StringUtils.isNotEmpty(userTypeName)){
            Integer userTypeValue = custTypes.get(userTypeName);
            if (ObjectUtils.anyNotNull(userTypeValue)){
                result.put(UserExcelExportConstans.USER_TYPE_VALUE,userTypeValue.toString());
            }else {
                result.put(UserExcelExportConstans.CHECK_RESULT,UserExcelExportConstans.ERROR_NOT_USER_TYPE);
            }
        }


        return result;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        redisUtil.del(UserExcelExportConstans.EXCEL_FAIL_REDIS_KEY_PREFIX+redisKey,UserExcelExportConstans.EXCEL_PASS_REDIS_KEY_PREFIX+redisKey);
        if (failMsg.size()>0) {
            redisUtil.lSet(UserExcelExportConstans.EXCEL_FAIL_REDIS_KEY_PREFIX+redisKey, failMsg, UserExcelExportConstans.EXCEL_REDIS_EXPIRE_TIME);
        }
        if (successData.size()>0) {
            redisUtil.lSet(UserExcelExportConstans.EXCEL_PASS_REDIS_KEY_PREFIX+redisKey, successData, UserExcelExportConstans.EXCEL_REDIS_EXPIRE_TIME);
        }
    }
}

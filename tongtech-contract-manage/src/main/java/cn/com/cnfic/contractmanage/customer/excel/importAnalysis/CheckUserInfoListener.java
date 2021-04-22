package cn.com.cnfic.contractmanage.customer.excel.importAnalysis;

import cn.com.cnfic.contractmanage.common.service.ISerialNoService;
import cn.com.cnfic.contractmanage.customer.constant.CustomerConstants;
import cn.com.cnfic.contractmanage.customer.entity.OrgCust;
import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.excel.CheckUserInfoDataExport;
import cn.com.cnfic.contractmanage.customer.excel.FailUserInfoData;
import cn.com.cnfic.contractmanage.customer.excel.SuccessUserInfoData;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustService;
import cn.com.cnfic.contractmanage.customer.service.impl.OrgCustomerServiceImpl;
import cn.com.cnfic.system.entity.Region;
import cn.com.cnfic.system.feign.region.IRegionClient;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.DateUtil;
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
 * @desc 客户基本信息导出监听器
 * @auther yangchuan
 * @date 2021/3/26
 */
@NoArgsConstructor
public class CheckUserInfoListener extends AnalysisEventListener<CheckUserInfoDataExport> {
    private OrgCustomerServiceImpl orgCustomerService;
    private Validator validator;
    private RedisUtil redisUtil;
    private Map<String, Integer> businessChances;
    private Map<String, Integer> custStates;
    private Map<String, Integer> custTypes;
    private IOrgCustService orgCustService;
    private List<Object> failMsg = Lists.newArrayList();
    private List<Object> successData = Lists.newArrayList();
    private ISerialNoService serialNoService;
    private Long suff;
    private IRegionClient regionClient;
    public CheckUserInfoListener(OrgCustomerServiceImpl orgCustomerService, Validator validator, RedisUtil redisUtil, Map<String, Integer> businessChances, Map<String, Integer> custStates, Map<String, Integer> custTypes, IOrgCustService orgCustService,ISerialNoService serialNoService,Long suff,IRegionClient regionClient) {
        this.orgCustomerService = orgCustomerService;
        this.validator = validator;
        this.redisUtil = redisUtil;
        this.businessChances = businessChances;
        this.custStates = custStates;
        this.custTypes = custTypes;
        this.orgCustService = orgCustService;
        this.serialNoService = serialNoService;
        this.suff=suff;
        this.regionClient=regionClient;
    }

    @Override
    public void invoke(CheckUserInfoDataExport checkUserInfoDataExport, AnalysisContext analysisContext) {
        if (ObjectUtils.allNull(checkUserInfoDataExport)) {
            return;
        }
        checkUserInfoField(checkUserInfoDataExport, analysisContext.readRowHolder().getRowIndex());
    }

    /**
     * 检查UserInfo 数据
     * @param checkUserInfoDataExport excel每行数据
     * @param rowIndex 行号
     */
    private void checkUserInfoField(CheckUserInfoDataExport checkUserInfoDataExport, Integer rowIndex) {
        DataBinder binder = new DataBinder(checkUserInfoDataExport);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        Map<String, String> checkOtherData = checkOtherData(checkUserInfoDataExport);
        String msg = checkOtherData.get(CheckConstants.CHECK_RESULT);
        //必填校验 校验通过
        if (!bindingResult.hasErrors() && StringUtils.isEmpty(msg)) {
            SuccessUserInfoData orgCustomer = BeanUtil.copy(checkUserInfoDataExport, SuccessUserInfoData.class);
            buildOrgCustomer(checkOtherData, orgCustomer);
            String msgTip = checkOtherData.get(CheckConstants.CHECK_NAME_REPEAT);
            if (StringUtils.isNotEmpty(msgTip)){
                StringBuffer msgSb = new StringBuffer().append(CheckConstants.LINE_NUMBER_ERROR).append(rowIndex).append(":").append(msgTip);
                orgCustomer.setRepeatTip(String.valueOf(msgSb));
            }
            //写入数据
            orgCustomer.setId(null);
            successData.add(orgCustomer);
        } else {
            List<String> errMsg = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(toList());
            if (StringUtils.isNotEmpty(msg)) {
                FailUserInfoData failUserInfoData = BeanUtil.copy(checkUserInfoDataExport, FailUserInfoData.class);
                errMsg.add(msg);
                StringBuffer msgSb = new StringBuffer().append(CheckConstants.LINE_NUMBER_ERROR).append(rowIndex).append(":").append(StringUtils.join(errMsg, ","));
                failUserInfoData.setMsg(msgSb.toString());
                failMsg.add(failUserInfoData);
            }
        }
    }

    /**
     * 构造 OrgCustomer 初始化数据
     * @param checkOtherData 检查结果
     * @param orgCustomer 构建 OrgCustomer
     */
    private void buildOrgCustomer(Map<String, String> checkOtherData, SuccessUserInfoData orgCustomer) {
        orgCustomer.setCreateUser(SecureUtil.getUserId());
        orgCustomer.setIsDeleted(0);
        orgCustomer.setCreateTime(DateUtil.now());
        String serialNo = serialNoService.getSerialNo(CustomerConstants.CUSTOMER_CODE, CustomerConstants.CUSTOMER_PREFIX, CustomerConstants.CUSTOMER_LENGTH);
        orgCustomer.setCustCode(serialNo);
        orgCustomer.setCustType(checkOtherData.get(CheckConstants.ORG_FEILD_CUSTTYPE));
        orgCustomer.setCustState(checkOtherData.get(CheckConstants.ORG_FEILD_CUSTSTATE));
        orgCustomer.setBusinessChance(checkOtherData.get(CheckConstants.ORG_FEILD_BUSINESSCHANCE));
        orgCustomer.setSuperUnit(checkOtherData.get(CheckConstants.ORG_FEILD_SUPERUNIT)==null?null:Long.valueOf(checkOtherData.get(CheckConstants.ORG_FEILD_SUPERUNIT)));
        orgCustomer.setCustIndustry(checkOtherData.get(CheckConstants.ORG_FEILD_CUSTINDUSTRY));

    }

    /**
     * 检查下拉框及比对上级单位,省市区划，行业
     *
     * @param checkUserInfoDataExport
     * @return
     */
    private Map<String, String> checkOtherData(CheckUserInfoDataExport checkUserInfoDataExport) {
        Map<String, String> result = new HashMap<>();
        //客户名称验证
        String custName = checkUserInfoDataExport.getCustName();
        if (StringUtils.isNotEmpty(custName)){
            OrgCustomer data = orgCustomerService.verifCustName(custName, "").getData();
            if (ObjectUtils.allNotNull(data)){
                result.put(CheckConstants.CHECK_NAME_REPEAT, CheckConstants.CHECK_NAME_REPEAT_NAME);
            }
        }
        String custTypeName = checkUserInfoDataExport.getCustTypeName();
        //客户类型
        if (StringUtils.isNoneBlank(custTypeName)) {
            Integer custType = custTypes.get(custTypeName);
            if (ObjectUtils.allNull(custType)) {
                result.put(CheckConstants.CHECK_RESULT, CheckConstants.CUST_TYPE_ERROR);
                return result;
            } else {
                result.put(CheckConstants.ORG_FEILD_CUSTTYPE, String.valueOf(custType));
            }
        }
        //客户类型
        String custStateName = checkUserInfoDataExport.getCustStateName();
        if (StringUtils.isNoneBlank(custStateName)) {
            Integer custState = custStates.get(custStateName);
            if (ObjectUtils.allNull(custState)) {
                result.put(CheckConstants.CHECK_RESULT, CheckConstants.CUST_STATE_ERROR);
                return result;
            } else {
                result.put(CheckConstants.ORG_FEILD_CUSTSTATE, String.valueOf(custState));
            }
        }
        //业务机会状态
        String businessChanceName = checkUserInfoDataExport.getBusinessChanceName();
        if (StringUtils.isNoneBlank(businessChanceName)) {
            Integer businessChance = businessChances.get(businessChanceName);
            if (ObjectUtils.allNull(businessChance)) {
                result.put(CheckConstants.CHECK_RESULT, CheckConstants.CUST_BUSINESS_CHANCE_ERROR);
                return result;
            } else {
                result.put(CheckConstants.ORG_FEILD_BUSINESSCHANCE, String.valueOf(businessChance));
            }
        }
        //上级单位
        String superUnitName = checkUserInfoDataExport.getSuperUnitName();
        if (StringUtils.isNoneBlank(superUnitName)) {
            QueryWrapper<OrgCustomer> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cust_name", superUnitName);
            OrgCustomer customer = orgCustomerService.getOne(queryWrapper);
            if (ObjectUtils.allNull(customer)) {
                result.put(CheckConstants.CHECK_RESULT, CheckConstants.CUST_SUPER_UNIT_NAME_ERROR);
                return result;
            } else {
                result.put(CheckConstants.ORG_FEILD_SUPERUNIT, String.valueOf(customer.getId()));
            }
        }
        //行业验证
        String firstName = checkUserInfoDataExport.getFirstName();
        String secondName = checkUserInfoDataExport.getSecondName();
        String thirdName = checkUserInfoDataExport.getThirdName();
        if (StringUtils.isNotEmpty(thirdName) || StringUtils.isNotEmpty(secondName) || StringUtils.isNotEmpty(firstName)) {
            QueryWrapper<OrgCust> custQueryWrapper = new QueryWrapper<>();
            custQueryWrapper.eq("first_name", firstName).or().eq("second_name", secondName).or().eq("third_name", thirdName);
            custQueryWrapper.eq("is_deleted", 0);
            custQueryWrapper.orderByDesc("cust_level").last(" limit 1");
            OrgCust cust = orgCustService.getOne(custQueryWrapper);
            if (ObjectUtils.allNotNull(cust)) {
                result.put(CheckConstants.ORG_FEILD_CUSTINDUSTRY, cust.getAncestors());
            } else {
                result.put(CheckConstants.CHECK_RESULT, CheckConstants.CUST_ERROR);
                return result;
            }
        }
        //行政区划验证
        String custCity = checkUserInfoDataExport.getCustCity();
        if (StringUtils.isNotEmpty(custCity)){
            Region data = regionClient.getOneByNameAndLevel(custCity, CheckConstants.REGION_LEVEL).getData();
            if (ObjectUtils.allNotNull(data)){
                checkUserInfoDataExport.setCustProvinceCode(data.getCode());
                checkUserInfoDataExport.setCustCityCode(data.getCode());
            }else {
                //去掉最后一个字在查询
                Region repeat = regionClient.getOneByNameAndLevel(custCity.substring(0,custCity.length()-1), CheckConstants.REGION_LEVEL).getData();
                if (ObjectUtils.allNotNull(repeat)){
                    checkUserInfoDataExport.setCustProvinceCode(data.getCode());
                    checkUserInfoDataExport.setCustCityCode(data.getCode());
                }else {
                    result.put(CheckConstants.CHECK_RESULT, CheckConstants.REGION_ERROR);
                }
            }

        }
        return result;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        redisUtil.del(CheckConstants.EXCEL_FAIL_REDIS_KEY_PREFIX+suff,CheckConstants.EXCEL_PASS_REDIS_KEY_PREFIX+suff);
        if (failMsg.size()>0) {
            redisUtil.lSet(CheckConstants.EXCEL_FAIL_REDIS_KEY_PREFIX+suff, failMsg, CheckConstants.EXCEL_REDIS_EXPIRE_TIME);
        }
        if (successData.size()>0) {
            redisUtil.lSet(CheckConstants.EXCEL_PASS_REDIS_KEY_PREFIX+suff, successData, CheckConstants.EXCEL_REDIS_EXPIRE_TIME);
        }
    }

}

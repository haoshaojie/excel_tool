package cn.com.cnfic.contractmanage.customer.service.impl;

import cn.com.cnfic.contractmanage.blackList.entity.BlackWhiteList;
import cn.com.cnfic.contractmanage.blackList.feign.BlackListResource;
import cn.com.cnfic.contractmanage.common.service.ISerialNoService;
import cn.com.cnfic.contractmanage.contract.service.IContractService;
import cn.com.cnfic.contractmanage.contract.vo.ContractVO;
import cn.com.cnfic.contractmanage.customer.constant.CustomerConstants;
import cn.com.cnfic.contractmanage.customer.constant.XHConstans;
import cn.com.cnfic.contractmanage.customer.dto.OrgCustomerDTO;
import cn.com.cnfic.contractmanage.customer.entity.OrgActivity;
import cn.com.cnfic.contractmanage.customer.entity.OrgContact;
import cn.com.cnfic.contractmanage.customer.entity.OrgCust;
import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.excel.CheckUserInfoDataExport;
import cn.com.cnfic.contractmanage.customer.excel.CheckUserInfoDataExtend;
import cn.com.cnfic.contractmanage.customer.excel.InitCustomerCellWriteHandler;
import cn.com.cnfic.contractmanage.customer.excel.RegionExportxingSheet;
import cn.com.cnfic.contractmanage.customer.excel.importAnalysis.CheckConstants;
import cn.com.cnfic.contractmanage.customer.excel.importAnalysis.CheckUserInfoListener;
import cn.com.cnfic.contractmanage.customer.mapper.OrgCustomerMapper;
import cn.com.cnfic.contractmanage.customer.service.IOrgActivityService;
import cn.com.cnfic.contractmanage.customer.service.IOrgContactService;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustService;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustomerService;
import cn.com.cnfic.contractmanage.customer.vo.OrgActivityVO;
import cn.com.cnfic.contractmanage.customer.vo.OrgContactVO;
import cn.com.cnfic.contractmanage.customer.vo.OrgCustomerVO;
import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.service.IOrgUserService;
import cn.com.cnfic.contractmanage.user.vo.OrgUserExcelVO;
import cn.com.cnfic.system.entity.Dict;
import cn.com.cnfic.system.entity.Param;
import cn.com.cnfic.system.entity.Region;
import cn.com.cnfic.system.feign.IDictClient;
import cn.com.cnfic.system.feign.ISysParamClient;
import cn.com.cnfic.system.feign.region.IRegionClient;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.commons.io.Charsets;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 企业客户表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-11
 */
@Service
@Slf4j
public class OrgCustomerServiceImpl extends BaseServiceImpl<OrgCustomerMapper, OrgCustomer> implements IOrgCustomerService {
    @Autowired
    private IOrgActivityService activityService;
    @Autowired
    @Lazy
    private IOrgContactService contactService;
    @Autowired
    private ISerialNoService serialNoService;
    @Autowired
    private IDictClient dictClient;
    @Autowired
    private IOrgCustService orgCustService;
    @Autowired
    private Validator validator;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IRegionClient regionClient;
    @Autowired
    @Lazy
    private IOrgUserService userService;
    @Autowired
    private BlackListResource blackListResource;
    @Autowired
    private IContractService contractService;
    @Autowired
    private ISysParamClient sysParamClient;
    @Override
    public IPage<OrgCustomerVO> selectOrgCustomerPage(IPage<OrgCustomerVO> page, OrgCustomerVO orgCustomer) {
        List<String> custIndustrys = orgCustomer.getCustIndustrys();
        if (custIndustrys != null && !custIndustrys.isEmpty()) {
            buildCustIndustrySql(orgCustomer, custIndustrys);
        }
        List<OrgCustomerVO> records = baseMapper.selectOrgCustomerPage(page, orgCustomer);
        //添加行政区划名称
//        redisUtil
        records.stream().forEach(e -> {
            String custProvince = e.getCustProvince();
            if (StringUtils.isNotEmpty(custProvince)) {
                String[] codeArr = custProvince.split(",");
                String code=codeArr[codeArr.length-1];
                Object regionName = redisUtil.get(CustomerConstants.REGION_REDIS_KEY_PREFIX + code);
                if (ObjectUtils.isNotEmpty(regionName)){
                    e.setRegionName(String.valueOf(regionName));
                }else {
                    try {
                        Region data = regionClient.getOneByAncestors(code).getData();
                        e.setRegionName(getRegionName(data));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        log.error("获取行政区划出错，行政区划：{}",code);
                    }
                }

            }
        });
        return page.setRecords(records);
    }

    /**
     * 获取行政区划名称
     * @param data Region
     * @return "河北省/唐山市" 返回两级
     */
    private String getRegionName(Region data) {
        if (ObjectUtils.isNotEmpty(data)){
            StringBuilder sb=new StringBuilder();
            String provinceName = data.getProvinceName();
            if (StringUtils.isNotEmpty(provinceName)){
                sb.append(provinceName);
            }
            String cityName = data.getCityName();
            if (StringUtils.isNotEmpty(cityName)){
                sb.append("/");
                sb.append(cityName);
            }
            String regionName = sb.toString();
            redisUtil.set(CustomerConstants.REGION_REDIS_KEY_PREFIX+data.getCode(),regionName);
            return regionName;

        }
        return null;
    }

    /**
     * 构建行业分类查询
     * @param orgCustomer 查询实体
     * @param custIndustrys 行业集合
     */
    private void buildCustIndustrySql(OrgCustomerVO orgCustomer, List<String> custIndustrys) {
        StringBuilder sb = new StringBuilder();
        sb.append(" and (");
        boolean andFlag = false;
//            拼接多个行业查询
        for (String cust : custIndustrys) {
            if (andFlag) {
                sb.append(" or c.cust_industry like '" + cust + "%' ");
            } else {
                andFlag = true;
                sb.append("   c.cust_industry like '" + cust + "%' ");
            }
        }
        sb.append(")");
        orgCustomer.setSbSql(sb.toString());
    }

    @Override
    public Boolean saveCustomer(OrgCustomerDTO orgCustomer) {
        //行业分类
        List<String> custIndustrys = orgCustomer.getCustIndustrys();
        //地域
//        String region = orgCustomer.getRegion();
        //活动信息
        List<OrgActivity> orgActivities = orgCustomer.getOrgActivities();
        //联系用户
        List<OrgContact> orgContacts = orgCustomer.getOrgContacts();
        orgCustomer.setCustIndustry(StringUtils.join(custIndustrys, ","));
        if (ObjectUtils.allNull(orgCustomer.getId())) {
            String serialNo = serialNoService.getSerialNo(CustomerConstants.CUSTOMER_CODE, CustomerConstants.CUSTOMER_PREFIX, CustomerConstants.CUSTOMER_LENGTH);
            orgCustomer.setCustCode(serialNo);
            this.save(orgCustomer);
        } else {
            this.updateById(orgCustomer);
            //删除活动
            activityService.deleteLogicByCustomerId(orgCustomer.getId());

        }
        Long orgCustomerId = orgCustomer.getId();
        //保存活动信息
        if (!orgActivities.isEmpty()) {
            orgActivities.stream().forEach(e -> {
                e.setCustId(orgCustomerId);
                e.setId(null);
                e.setIsDeleted(0);
                String serialNo = serialNoService.getSerialNo(CustomerConstants.CUSTOMER_AVTIVITY_CODE, CustomerConstants.CUSTOMER_ACTIVITY_PREFIX, CustomerConstants.CUSTOMER_LENGTH);
                e.setActivityNo(serialNo);
            });
            activityService.saveBatch(orgActivities);
        }
        //保存用户信息
        if (!orgContacts.isEmpty()) {
            orgContacts.stream().forEach(e -> {
                if (ObjectUtils.isNotEmpty(e.getId())){
                    e.setCustId(orgCustomerId);
                    e.setIsDeleted(0);
                    contactService.updateById(e);
                }else {
                    e.setCustId(orgCustomerId);
                    e.setIsDeleted(0);
                    contactService.save(e);
                }

            });

        }
        return true;
    }

    @Override
    public OrgCustomerVO getOneDtail(OrgCustomer orgCustomer) {
        Long id = orgCustomer.getId();
        OrgCustomerVO orgCustomerVO = baseMapper.getDetailById(id);
        QueryWrapper<OrgActivity> activityWrapper = new QueryWrapper<>();
        activityWrapper.eq("cust_id", id);
        List<OrgActivity> orgActivities = activityService.list(activityWrapper);
        List<Dict> dicts = dictClient.getList(CustomerConstants.DIC_ACTIVITY_TYPE_CODE).getData();
        Map<Integer, String> dictMap = dicts.stream().collect(Collectors.toMap(Dict::getDictKey, Dict::getDictValue, (key1, key2) -> key2));
        orgActivities.stream().forEach(e -> {
            e.setActivityTypeName(dictMap.get(Integer.valueOf(e.getActivityType())));
        });
        orgCustomerVO.setOrgActivities(orgActivities);
        orgCustomerVO.setOrgContacts(contactService.list(Wrappers.<OrgContact>query().lambda().eq(OrgContact::getCustId,id).eq(OrgContact::getIsDeleted,0)));
        String custIndustry = orgCustomerVO.getCustIndustry();
        if (StringUtils.isNotEmpty(custIndustry)) {
            orgCustomerVO.setCustIndustrys(Arrays.asList(custIndustry.split(",")));
        }
        return orgCustomerVO;
    }

    @Override
    public void exportOrgCustomerList(HttpServletResponse response, OrgCustomerVO orgCustomer) {
        List<String> selectColumn = orgCustomer.getSelectColumn();
        Query query = new Query();
        query.setCurrent(1);
        query.setSize(Integer.MAX_VALUE);
        IPage<OrgCustomerVO> orgCustomerVOIPage = this.selectOrgCustomerPage(Condition.getPage(query), orgCustomer);
        List<OrgCustomerVO> records = orgCustomerVOIPage.getRecords();
        if (!selectColumn.isEmpty()) {
            ExcelWriter excelWriter = null;
            try {
                response.setContentType("application/vnd.ms-excel");
                response.setCharacterEncoding("utf-8");
                String fileName = URLEncoder.encode(CustomerConstants.EXPORT_CUSTOMER_FILE_NAME, "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
                //导出客户基本信息
                //行业映射 如果有行业名称就导出1-3级

                if (selectColumn.contains(CustomerConstants.COLUMN_CUST_INDUSTRY_NAME)) {
                    selectColumn.remove(CustomerConstants.COLUMN_CUST_INDUSTRY_NAME);
                    selectColumn.addAll(Arrays.asList(CustomerConstants.COLUMN_CUST_INDUSTRY_NAME_RESULT.split(",")));
                }
                excelWriter = EasyExcel.write(response.getOutputStream()).build();
                WriteSheet customerSheet = EasyExcel.writerSheet(CustomerConstants.USER_INFO_SHEET_NAME).head(OrgCustomerVO.class).includeColumnFiledNames(new HashSet<>(selectColumn)).build();
                excelWriter.write(records, customerSheet);
                //联系人信息
                List<Long> customerIds = records.stream().map(OrgCustomer::getId).collect(toList());
                if (orgCustomer.getLinkmanInfoChecked()) {
                    List<OrgContactVO> contactVOS = contactService.findByCustomerIds(customerIds);
                    WriteSheet contaceSheet = EasyExcel.writerSheet(CustomerConstants.LINKMAN_SHEET_NAME).head(OrgContactVO.class).includeColumnFiledNames(Arrays.asList(CustomerConstants.EXPORT_CONTACT_COLUMN.split(","))).build();
                    excelWriter.write(contactVOS, contaceSheet);
                }
                //客户管理活动
                if (orgCustomer.getCustomerManagementChecked()) {
                    List<OrgActivityVO> activityVOS = activityService.findByCustomerIds(customerIds);
                    WriteSheet contaceSheet = EasyExcel.writerSheet(CustomerConstants.ACTIVITY_SHEET_NAME).head(OrgActivityVO.class).includeColumnFiledNames(Arrays.asList(CustomerConstants.EXPORT_ACTIVITY_COLUMN.split(","))).build();
                    excelWriter.write(activityVOS, contaceSheet);
                }
                //是否导出用户信息
                if (orgCustomer.getUserInfoChecked()) {
                    Map<Integer, String> sexs = dictClient.getList(CustomerConstants.DIC_SEX_CODE).getData().stream().collect(toMap(Dict::getDictKey, Dict::getDictValue, (key1, key2) -> key2));
                    ;
                    Map<Integer, String> creatAccs = dictClient.getList(CustomerConstants.DIC_IS_CREATE_ACCOUNT_CODE).getData().stream().collect(toMap(Dict::getDictKey, Dict::getDictValue, (key1, key2) -> key2));
                    ;
                    Map<Integer, String> userTypes = dictClient.getList(CustomerConstants.DIC_USER_TYPE_CODE).getData().stream().collect(toMap(Dict::getDictKey, Dict::getDictValue, (key1, key2) -> key2));
                    List<OrgUserExcelVO> userExcelVOS = userService.findByCustomerIds(customerIds);
                    userExcelVOS.stream().forEach(e -> {
                        e.setIsCreateAccountName(getDicValue(creatAccs, e.getIsCreateAccount()));
                        e.setUserSexName(getDicValue(sexs, e.getUserSex()));
                        e.setUserTypeName(getDicValue(userTypes, e.getUserType()));
                    });
                    WriteSheet contaceSheet = EasyExcel.writerSheet(CustomerConstants.USER_SHEET_NAME).head(OrgUserExcelVO.class).build();
                    excelWriter.write(userExcelVOS, contaceSheet);
                }
                //是否导出合约信息
                if (orgCustomer.getContactInfoChecked()) {

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        }
    }

    /**
     * 通过key获取字典表的value
     *
     * @param map 字典集合
     * @param key 字典key
     * @return 字典value
     */
    private String getDicValue(Map<Integer, String> map, Object key) {
        return map.get(key);
    }


    @Override
    public void downloadCustomerTemplate(HttpServletResponse response, String key) {
        if (!StringUtils.isBlank(key)) {
            List<Object> failRedisData = redisUtil.lGet(CheckConstants.EXCEL_FAIL_REDIS_KEY_PREFIX + key, 0, redisUtil.lGetListSize(CheckConstants.EXCEL_FAIL_REDIS_KEY_PREFIX + key));
            List<CheckUserInfoDataExtend> data = failRedisData.stream().map(e -> {
                return BeanUtil.copy(e, CheckUserInfoDataExtend.class);
            }).collect(toList());
            this.excelExportCustomerFailFile(response, CustomerConstants.EXPORT_CUSTOMER_FAIL_FILE, CheckUserInfoDataExtend.class, data);
        } else {
            this.excelExportCustomer(response, CustomerConstants.EXPORT_CUSTOMER_TEMPLATE_FILE, CheckUserInfoDataExport.class, new ArrayList<>());
        }
    }

    /**
     * @param response  下载
     * @param fileName  文件名
     * @param dataClass 导出实体
     * @param data      实体数据
     */
    @SneakyThrows
    private void excelExportCustomerFailFile(HttpServletResponse response, String fileName, Class<CheckUserInfoDataExtend> dataClass, List<CheckUserInfoDataExtend> data) {
        buildResponse(response, fileName);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet customerSheet = EasyExcel.writerSheet(CustomerConstants.EXPORT_CUSTOMER_SHEET1_NAME).head(dataClass).build();
            excelWriter.write(data, customerSheet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    @Override
    public R imporCustomerFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            throw new IllegalArgumentException(CustomerConstants.IMPORT_FILE_EMPTY_MSG);
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, CustomerConstants.EXCEL_FAIL_SUFFIX_03) && !StringUtils.endsWithIgnoreCase(filename, CustomerConstants.EXCEL_FAIL_SUFFIX_07))) {
            throw new IllegalArgumentException(CustomerConstants.IMPORT_FILE_FORMAT_MSG);
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Integer> businessChances = dictClient.getList(CustomerConstants.DIC_BUSINESS_CHANCE_CODE).getData().stream().collect(Collectors.toMap(Dict::getDictValue, Dict::getDictKey, (key1, key2) -> key2));
        //  数据字典客户状态
        Map<String, Integer> custStates = dictClient.getList(CustomerConstants.DIC_CUST_STATE_CODE).getData().stream().collect(Collectors.toMap(Dict::getDictValue, Dict::getDictKey, (key1, key2) -> key2));
        // 客户类型
        Map<String, Integer> custTypes = dictClient.getList(CustomerConstants.DIC_CUST_TYPE_CODE).getData().stream().collect(Collectors.toMap(Dict::getDictValue, Dict::getDictKey, (key1, key2) -> key2));
        long redisKey = DateUtil.now().getTime();
        CheckUserInfoListener importListener = new CheckUserInfoListener(this, validator, redisUtil, businessChances, custStates, custTypes, orgCustService, serialNoService, redisKey, regionClient);
        ExcelReaderBuilder builder = EasyExcel.read(inputStream, CheckUserInfoDataExport.class, importListener);
        if (Objects.nonNull(builder)) {
            builder.doReadAll();
        }
        return R.data(redisKey);
    }

    @Override
    public R getImportData(String redisKey, Boolean isSuccess, Query query) {
        if (isSuccess) {
            return pageOfRedisData(CheckConstants.EXCEL_PASS_REDIS_KEY_PREFIX + redisKey, query);
        }
        return pageOfRedisData(CheckConstants.EXCEL_FAIL_REDIS_KEY_PREFIX + redisKey, query);
    }

    @Override
    public R submitpassdata(String redisKey) {
        List<Object> passRedisData = redisUtil.lGet(CheckConstants.EXCEL_PASS_REDIS_KEY_PREFIX + redisKey, 0, redisUtil.lGetListSize(CheckConstants.EXCEL_PASS_REDIS_KEY_PREFIX + redisKey));
        if (CollectionUtil.isEmpty(passRedisData)) {
            return R.fail(CustomerConstants.REDIS_DATA_ERROR_MSG);
        }
        int success = 0;
        for (Object data : passRedisData) {
            OrgCustomer orgCustomer = BeanUtil.copy(data, OrgCustomer.class);
            orgCustomer.setCustCity(orgCustomer.getCustCityCode());
            orgCustomer.setCustProvince(orgCustomer.getCustProvinceCode());
            try {
                this.save(orgCustomer);
                success += 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return R.data(success);
    }

    @Override
    public R<OrgCustomer> verifCustName(String custName, String custCode) {
        QueryWrapper<OrgCustomer> orgCustomerQueryWrapper = new QueryWrapper<>();
        orgCustomerQueryWrapper.eq("cust_name", custName);
        orgCustomerQueryWrapper.eq("is_deleted", 0);
        if (StringUtils.isNotEmpty(custCode)) {
            orgCustomerQueryWrapper.eq("cust_code", custCode);
        }
        orgCustomerQueryWrapper.last(" limit 1 ");
        OrgCustomer result = this.getOne(orgCustomerQueryWrapper);
        return R.data(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteByCustomerId(String ids) {
        String[] idsArr = ids.split(",");
        for (int i = 0; i < idsArr.length; i++) {
            String id = idsArr[i];
            //查询黑白名单是否有关联
            List<BlackWhiteList> blackWhiteLists = blackListResource.listByCustId(id);
            if (!blackWhiteLists.isEmpty()) {
                List<String> exchanges = blackWhiteLists.stream().map(BlackWhiteList::getExchange).collect(toList());
                throw new ServiceException(CustomerConstants.DELETE_NOT_BLACK_WHITE_MSG + StringUtils.join(exchanges, ","));
            }
            //查询用户是否有关联
            List<OrgUser> contacts = userService.list(Wrappers.lambdaUpdate(OrgUser.class).eq(OrgUser::getCustId,id).eq(OrgUser::getIsDeleted,0));
            if (!contacts.isEmpty()) {
                List<String> contactNames = contacts.stream().map(OrgUser::getUserName).collect(toList());
                throw new ServiceException(CustomerConstants.DELETE_NOT_CONTACT_MSG + StringUtils.join(contactNames, ","));
            }
            //查询关联上级单位
            List<OrgCustomer> superUnits = this.list(Wrappers.lambdaUpdate(OrgCustomer.class).eq(OrgCustomer::getSuperUnit, id).eq(BaseEntity::getIsDeleted, 0));
            if (!superUnits.isEmpty()) {
                List<String> custNames = superUnits.stream().map(OrgCustomer::getCustName).collect(toList());
                throw new ServiceException(CustomerConstants.DELETE_NOT_SUPERUNIT_MSG + StringUtils.join(custNames, ","));
            }
//          查询合约是否有关联
            List<ContractVO> contractVOS = contractService.findByCustId(Long.valueOf(id));
            if (!contractVOS.isEmpty()) {
                List<String> conNames = contractVOS.stream().map(ContractVO::getConName).collect(toList());
                throw new ServiceException(CustomerConstants.DELETE_NOT_CON_NAME_MSG + StringUtils.join(conNames, ","));
            }
        }
        //删除客户
        this.deleteLogic(Func.toLongList(ids));
        //删除联系人 如果联系人有关联也不能删除
        for (int i = 0; i < idsArr.length; i++) {
            List<OrgContact> orgContacts = contactService.findByCustomerId(idsArr[i]);
            if (!orgContacts.isEmpty()) {
                orgContacts.stream().forEach(e -> {
                    contactService.deleteByContactId(String.valueOf(e.getId()));
                });
            }
        }
        return R.success(StringUtils.EMPTY);
    }

    @Override
    public List<OrgCustomer> customList(OrgCustomer orgCustomer) {
        QueryWrapper<OrgCustomer> queryWrapper=new QueryWrapper();
        queryWrapper.eq("is_deleted",0);
        String custName = orgCustomer.getCustName();
        if (StringUtils.isNotEmpty(custName)){
            queryWrapper.like("cust_name",custName);
        }
        return this.list(queryWrapper);
    }

    @Override
    public Object companySearch(String name,String pageNo,String pageSize) {
        Map<String,String> query=new HashMap<>();
        query.put(XHConstans.COMPANY_NAME,name);
        query.put(XHConstans.PAGENO,pageNo);
        query.put(XHConstans.PAGESIZE,pageSize);
        Map<String,String> header=new HashMap<>();
        Param param = sysParamClient.getByKey(XHConstans.API_TOKEN);
        if (ObjectUtils.isNotEmpty(param)){
            header.put(XHConstans.API_TOKEN,param.getParamValue());
        }else {
            throw new ServiceException("获取系统参数异常:"+XHConstans.API_TOKEN);
        }
        String result = OkHttpUtil.get(XHConstans.HOST, header, query);
        return JSONUtil.parseObj(result);
    }

    /**
     * @param redisKey redis key
     * @param query    分页条件
     * @return Page<Object>
     */
    private R pageOfRedisData(String redisKey, Query query) {
        long cacheTatol = redisUtil.lGetListSize(redisKey);
        Page<Object> passRedisPage = new Page<>(query.getCurrent(), query.getSize(), cacheTatol,
                Boolean.FALSE);
        long current = passRedisPage.getCurrent();
        if (cacheTatol < 1L || current < 1L) {
            return R.data(passRedisPage);
        }
        long offset = (current - 1) * passRedisPage.getSize(), end = offset + passRedisPage.getSize();
        passRedisPage.setRecords(redisUtil.lGet(redisKey, offset, end));
        return R.data(passRedisPage);
    }

    /**
     * 导出客户基本信息模板
     *
     * @param response
     * @param fileName  文件名
     * @param dataClass 导出实体
     * @param data      导入的实体数据
     * @param <T>
     */
    @SneakyThrows
    private <T> void excelExportCustomer(HttpServletResponse response, String fileName, Class<T> dataClass, ArrayList<T> data) {
        buildResponse(response, fileName);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
            if (StringUtil.equals(CustomerConstants.EXPORT_CUSTOMER_TEMPLATE_FILE, fileName)) {
                //            *客户类型
                List<String> custTypes = dictClient.getList(CustomerConstants.DIC_CUST_TYPE_CODE).getData().stream().map(Dict::getDictValue).collect(toList());
                List<String> custStates = dictClient.getList(CustomerConstants.DIC_CUST_STATE_CODE).getData().stream().map(Dict::getDictValue).collect(toList());
                List<String> businessChances = dictClient.getList(CustomerConstants.DIC_BUSINESS_CHANCE_CODE).getData().stream().map(Dict::getDictValue).collect(toList());
                excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new InitCustomerCellWriteHandler(custTypes, custStates, businessChances)).build();
                //添加
            } else {
                excelWriter = EasyExcel.write(response.getOutputStream()).build();
            }
            WriteSheet customerSheet = EasyExcel.writerSheet(CustomerConstants.EXPORT_CUSTOMER_SHEET1_NAME).head(dataClass).build();
            excelWriter.write(data, customerSheet);
            //补充行业
            if (StringUtil.equals(CustomerConstants.EXPORT_CUSTOMER_TEMPLATE_FILE, fileName)) {
                QueryWrapper<OrgCust> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("is_deleted", 0);
                queryWrapper.orderByDesc("first_name");
                List<OrgCust> list = orgCustService.list(queryWrapper);
                WriteSheet customerSheet2 = EasyExcel.writerSheet(CustomerConstants.EXPORT_CUSTOMER_SHEET2_NAME).head(OrgCust.class).includeColumnFiledNames(Arrays.asList(CustomerConstants.EXPORT_CUST_COLUMN.split(","))).build();
                excelWriter.write(list, customerSheet2);
                //补充行政区划
                List<Map<String,Object>> regionVOList = (List<Map<String,Object>>) regionClient.getRegionTree(2).getData();
                List<RegionExportxingSheet> regionExportxingSheets=buildRegionExcel(regionVOList);
                WriteSheet customerSheet3 = EasyExcel.writerSheet(CustomerConstants.EXPORT_PROVINCE_CITY_SHEET2_NAME).head(RegionExportxingSheet.class).build();
                excelWriter.write(regionExportxingSheets, customerSheet3);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 构建导出行政区划实体
     * @param treeNodes regionVOList
     * @return RegionExportxingSheet
     */
    private List<RegionExportxingSheet> buildRegionExcel(List<Map<String,Object>> treeNodes) {
        List<RegionExportxingSheet> result=new ArrayList<>();
        for (Map<String,Object> treeNode:treeNodes) {
            Object hasChildren = treeNode.get("hasChildren");
            if (ObjectUtils.isNotEmpty(hasChildren)&&Boolean.valueOf(hasChildren.toString())){
                List<Map<String,Object>> children = (List<Map<String, Object>>) treeNode.get("children");
               result.addAll( children.stream().map(e->{
                   return new RegionExportxingSheet(treeNode.get("title"),e.get("title"));
               } ).collect(Collectors.toList()));
            }

        }
        return result;
    }


    /**
     * 构建下载信息
     *
     * @param response
     * @param fileName 下载的文件名称
     * @return 文件Unicode 编码名称
     * @throws UnsupportedEncodingException
     */
    private String buildResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(Charsets.UTF_8.name());
        String fileNameEncode = URLEncoder.encode(fileName, Charsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ".xlsx");
        return fileNameEncode;
    }


}

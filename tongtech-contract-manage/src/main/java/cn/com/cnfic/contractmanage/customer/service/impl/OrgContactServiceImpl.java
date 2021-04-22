package cn.com.cnfic.contractmanage.customer.service.impl;

import cn.com.cnfic.contractmanage.contract.entity.Contract;
import cn.com.cnfic.contractmanage.contract.service.IContractService;
import cn.com.cnfic.contractmanage.contract.vo.ContractVO;
import cn.com.cnfic.contractmanage.customer.constant.CustomerConstants;
import cn.com.cnfic.contractmanage.customer.entity.OrgContact;
import cn.com.cnfic.contractmanage.customer.excel.CheckContractDataExport;
import cn.com.cnfic.contractmanage.customer.excel.FailContractDataExport;
import cn.com.cnfic.contractmanage.customer.excel.InitCustOrgCellWriteHandler;
import cn.com.cnfic.contractmanage.customer.excel.importAnalysis.CheckConstants;
import cn.com.cnfic.contractmanage.customer.excel.importAnalysis.CheckLinkmanListener;
import cn.com.cnfic.contractmanage.customer.mapper.OrgContactMapper;
import cn.com.cnfic.contractmanage.customer.service.IOrgContactService;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustomerService;
import cn.com.cnfic.contractmanage.customer.vo.OrgContactVO;
import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.service.IOrgUserService;
import cn.com.cnfic.system.entity.Dept;
import cn.com.cnfic.system.feign.ISysClient;
import com.alibaba.cloud.commons.io.Charsets;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 企业客户联系人表 服务实现类
 *
 * @author Cnfic-UserManage
 * @since 2021-03-16
 */
@Service
public class OrgContactServiceImpl extends BaseServiceImpl<OrgContactMapper, OrgContact> implements IOrgContactService {
	@Autowired
	private ISysClient sysClient;
	@Autowired
	@Lazy
	private IOrgCustomerService orgCustomerService;
	@Autowired
	private Validator validator;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	@Lazy
	private IOrgUserService orgUserService;
	@Autowired
	private IContractService contractService;

	@Override
	public IPage<OrgContactVO> selectOrgContactPage(IPage<OrgContactVO> page, OrgContactVO orgContact) {
		return page.setRecords(baseMapper.selectOrgContactPage(page, orgContact));
	}

	@Override
	public void deleteLogicByCustomerId(Long id) {
		baseMapper.deleteLogicByCustomerId(id);
	}

	@Override
	public List<OrgContactVO> findByCustomerIds(List<Long> customerIds) {
		return baseMapper.findByCustomerIds(customerIds);
	}

	@Override
	public void downloadTemplate(HttpServletResponse response, String key) {
		if (!StringUtils.isBlank(key)){
			List<Object> failRedisData = redisUtil.lGet(CheckConstants.EXCEL_CONTRACT_FAIL_REDIS_KEY_PREFIX+key, 0, redisUtil.lGetListSize(CheckConstants.EXCEL_CONTRACT_FAIL_REDIS_KEY_PREFIX+key));
			List<FailContractDataExport> data=failRedisData.stream().map(e->{
				return BeanUtil.copy(e,FailContractDataExport.class);
			}).collect(toList());
			this.excelFailExport(response, CustomerConstants.EXPORT_CONTRACT_FAIL_FILE, FailContractDataExport.class,data);

		}else {

			this.excelExport(response, CustomerConstants.EXPORT_CONTRACT_TEMPLATE_FILE, CheckContractDataExport.class,new ArrayList<>());
		}
	}
	@SneakyThrows
	private void excelFailExport(HttpServletResponse response, String fileName, Class<FailContractDataExport> dataClass, List<FailContractDataExport> data) {
		buildResponse(response, fileName);
		ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), dataClass);
		excelWriterBuilder.sheet(CustomerConstants.EXPORT_CONTRACT_FAIL_FILE).doWrite(data);
	}

	@Override
	public R importLinkmanFile(MultipartFile file) {
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
		//验证部门信息
		List<Dept> detList = sysClient.getDetList();
		Map<String, Long> deptMap = detList.stream().collect(Collectors.toMap(Dept::getDeptName, Dept::getId, (key1, key2) -> key2));
		long redisKey = DateUtil.now().getTime();
		CheckLinkmanListener importListener = new CheckLinkmanListener( orgCustomerService,  validator,  redisUtil,  redisKey,deptMap);
		ExcelReaderBuilder builder = EasyExcel.read(inputStream, CheckContractDataExport.class, importListener);
		if (Objects.nonNull(builder)) {
			builder.doReadAll();
		}
		return R.data(redisKey);
	}

	@Override
	public R getImportData(String redisKey, Boolean isSuccess, Query query) {
		if (isSuccess){
			return pageOfRedisData(CheckConstants.EXCEL_CONTRACT_PASS_REDIS_KEY_PREFIX+redisKey,query);
		}
		return pageOfRedisData(CheckConstants.EXCEL_CONTRACT_FAIL_REDIS_KEY_PREFIX+redisKey,query);
	}

	@Override
	public R submitpassdata(String redisKey) {
		List<Object> passRedisData = redisUtil.lGet(CheckConstants.EXCEL_CONTRACT_PASS_REDIS_KEY_PREFIX+redisKey, 0, redisUtil.lGetListSize(CheckConstants.EXCEL_CONTRACT_PASS_REDIS_KEY_PREFIX+redisKey));
		if (CollectionUtil.isEmpty(passRedisData)) {
			return R.fail(CheckConstants.REDIS_DATA_ERROR_MSG);
		}
		int success = 0;
		for (Object data : passRedisData) {
			OrgContact orgContact =BeanUtil.copy(data,OrgContact.class);
			try {
				this.save(orgContact);
				success += 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return R.data(success);
	}

	@Override
	public OrgContact selectOneByCustManager(String custManager) {
		QueryWrapper<OrgContact> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("cust_manager",custManager).eq("is_deleted",0).last(" limit 1");
		return this.getOne(queryWrapper);
	}

	@Override
	public R deleteByContactId(String id) {
		//查询用户管理是否引用
		OrgUser user=orgUserService.findOneOrgUserByManager(id);
		if (ObjectUtils.isNotEmpty(user)){
			throw new ServiceException(CustomerConstants.QUERY_FIND_ORG_USER+user.getUserName());
		}
		//校验是否有合约数据
		List<ContractVO> contractVOS = contractService.findByCustContactId(Long.valueOf(id));
		if (contractVOS!=null&&contractVOS.size()>0){
			List<String> conNames = contractVOS.stream().map(Contract::getConName).collect(toList());
			throw new ServiceException(CustomerConstants.QUERY_FIND_CONTRACT+StringUtils.join(conNames,","));
		}
		//TODO 用户账号管理->企业账号管理->公共账号引用未验证
		deleteLogic(Func.toLongList(id));
		return R.success(StringUtils.EMPTY);
	}

	@Override
	public List<OrgContact> findByCustomerId(String id) {
		QueryWrapper<OrgContact> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("is_deleted",0);
		queryWrapper.eq("cust_id",id);
		return getBaseMapper().selectList(queryWrapper);
	}

	/**
	 *
	 * @param redisKey redis key
	 * @param query 分页条件
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
	@SneakyThrows
	private <T> void excelExport(HttpServletResponse response, String fileName, Class<T> dataClass, List<T> data) {
		buildResponse(response, fileName);
		ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), dataClass);
		if (StringUtil.equals(CustomerConstants.EXPORT_CONTRACT_TEMPLATE_FILE, fileName)) {
			List<Dept> detList = sysClient.getDetList();
			List<String> deptNames=detList.stream().map(Dept::getDeptName).collect(toList());
			excelWriterBuilder.registerWriteHandler(new InitCustOrgCellWriteHandler(deptNames));
		}
		excelWriterBuilder.sheet(CustomerConstants.EXPORT_CONTRACT_TEMPLATE_FILE).doWrite(data);
	}
	private String buildResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileNameEncode = URLEncoder.encode(fileName, Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ".xlsx");
		return fileNameEncode;
	}
}

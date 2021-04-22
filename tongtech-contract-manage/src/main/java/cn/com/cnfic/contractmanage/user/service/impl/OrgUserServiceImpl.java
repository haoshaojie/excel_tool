package cn.com.cnfic.contractmanage.user.service.impl;

import cn.com.cnfic.contractmanage.common.service.ISerialNoService;
import cn.com.cnfic.contractmanage.customer.constant.CustomerConstants;
import cn.com.cnfic.contractmanage.customer.entity.OrgContact;
import cn.com.cnfic.contractmanage.customer.entity.OrgCustomer;
import cn.com.cnfic.contractmanage.customer.service.IOrgContactService;
import cn.com.cnfic.contractmanage.customer.service.IOrgCustomerService;
import cn.com.cnfic.contractmanage.user.constant.UserConstants;
import cn.com.cnfic.contractmanage.user.entity.OrgUser;
import cn.com.cnfic.contractmanage.user.excel.FailUserInfo;
import cn.com.cnfic.contractmanage.user.excel.InitUserCellWriteHandler;
import cn.com.cnfic.contractmanage.user.excel.UserExcelExportConstans;
import cn.com.cnfic.contractmanage.user.excel.UserImportTemplate;
import cn.com.cnfic.contractmanage.user.excel.listener.CheckUserListener;
import cn.com.cnfic.contractmanage.user.mapper.OrgUserMapper;
import cn.com.cnfic.contractmanage.user.service.IOrgUserService;
import cn.com.cnfic.contractmanage.user.vo.OrgUserExcelVO;
import cn.com.cnfic.contractmanage.user.vo.OrgUserVO;
import cn.com.cnfic.contractmanage.user.wrapper.OrgUserWrapper;
import cn.com.cnfic.system.entity.Dept;
import cn.com.cnfic.system.entity.Dict;
import cn.com.cnfic.system.feign.IDictClient;
import cn.com.cnfic.system.feign.ISysClient;
import cn.com.cnfic.system.user.entity.User;
import cn.com.cnfic.system.user.entity.UserInfo;
import cn.com.cnfic.system.user.feign.IUserClient;
import com.alibaba.cloud.commons.io.Charsets;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.CollectionUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.RedisUtil;
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
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @desc 企业用户表 服务实现类
 * @author Cnfic-UserManage
 * @date 2021-03-29
 */
@Service
public class OrgUserServiceImpl extends BaseServiceImpl<OrgUserMapper, OrgUser> implements IOrgUserService {
	@Autowired
	@Lazy
	private IOrgCustomerService customerService;
	@Autowired
	private ISerialNoService serialNoService;
	@Autowired
	private  IUserClient userClient;
	@Autowired
	private IDictClient dictClient;
	@Autowired
	private Validator validator;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	@Lazy
	private IOrgContactService contactService;
	@Autowired
	private ISysClient sysClient;
	private static final String REDIS_DATA_ERROR_MSG = "上传验证通过的数据已不存在，请重新上传";

	@Override
	public IPage<OrgUserVO> selectOrgUserPage(IPage<OrgUserVO> page, OrgUserVO orgUser) {
		List<OrgUserVO> records = baseMapper.selectOrgUserPage(page, orgUser);
		return page.setRecords(records);
	}

	@Override
	public OrgUserVO getDetail(QueryWrapper<OrgUser> queryWrapper) {
		OrgUser user = getOne(queryWrapper);
		if (ObjectUtils.allNotNull(user)){
			OrgUserVO orgUserVO = OrgUserWrapper.build().entityVO(user);
			Long custId = user.getCustId();
			//补充客户信息
			if (ObjectUtils.allNotNull(custId)){
				OrgCustomer customer = customerService.getOne(new QueryWrapper<OrgCustomer>().eq("id", custId));
					if (ObjectUtils.allNotNull(custId)) {
						orgUserVO.setCustName(customer.getCustName());
						orgUserVO.setCustCode(customer.getCustCode());
					}
				}
			//补充创建修改者信息
			Long createUserId = user.getCreateUser();
			if (ObjectUtils.allNotNull(createUserId)){
				UserInfo data = userClient.userInfo(createUserId).getData();
				if (ObjectUtils.allNotNull(createUserId)){
					orgUserVO.setCreateUserName(data.getUser().getName());
				}
			}
			Long updateUserId = user.getUpdateUser();
			if (ObjectUtils.allNotNull(updateUserId)){
				UserInfo data = userClient.userInfo(updateUserId).getData();
				orgUserVO.setUpdateUserName(data.getUser().getName());
			}
			return orgUserVO;
		}
		return null;
	}

	@Override
	public Boolean saveOrUpdateUser(OrgUser orgUser) {
		String serialNo = serialNoService.getSerialNo(UserConstants.USER_CODE, UserConstants.USER_PREFIX, UserConstants.CUSTOMER_LENGTH);
		orgUser.setUserNo(serialNo);
		return saveOrUpdate(orgUser);
	}

	@Override
	public void downloadTemplate(HttpServletResponse response, String key) {
		if (StringUtils.isNotEmpty(key)){
			List<Object> failRedisData = redisUtil.lGet(UserExcelExportConstans.EXCEL_FAIL_REDIS_KEY_PREFIX+key, 0, redisUtil.lGetListSize(UserExcelExportConstans.EXCEL_FAIL_REDIS_KEY_PREFIX+key));
			List<FailUserInfo> data=failRedisData.stream().map(e->{
				return BeanUtil.copy(e, FailUserInfo.class);
			}).collect(toList());
			this.exportTemplate(response, UserExcelExportConstans.EXPORT_USER_FAIL_FILE, FailUserInfo.class,data, null);
		}else {
			this.exportTemplate(response, UserExcelExportConstans.EXPORT_USER_TEMPLATE_FILE, UserImportTemplate.class,new ArrayList<>(), null);
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
//        客户类型
		Map<String,Integer> custTypes = dictClient.getList(UserExcelExportConstans.DIC_USER_TYPE_CODE).getData().stream().collect(Collectors.toMap(Dict::getDictValue, Dict::getDictKey, (key1, key2) -> key2));
		Map<String,Integer> sexs = dictClient.getList(UserExcelExportConstans.DIC_SEX_CODE).getData().stream().collect(Collectors.toMap(Dict::getDictValue, Dict::getDictKey, (key1, key2) -> key2));
		long redisKey = DateUtil.now().getTime();
		CheckUserListener importListener = new CheckUserListener(this, validator, redisUtil,contactService,custTypes,redisKey,customerService,sexs);
		ExcelReaderBuilder builder = EasyExcel.read(inputStream, UserImportTemplate.class, importListener);
		if (Objects.nonNull(builder)) {
			builder.doReadAll();
		}
		return R.data(redisKey);
	}

	@Override
	public R getImportData(String redisKey, Boolean isSuccess, Query query) {
		if (isSuccess){
			return pageOfRedisData(UserExcelExportConstans.EXCEL_PASS_REDIS_KEY_PREFIX+redisKey,query);
		}
		return pageOfRedisData(UserExcelExportConstans.EXCEL_FAIL_REDIS_KEY_PREFIX+redisKey,query);
	}

	@Override
	public R submitpassdata(String redisKey) {
		List<Object> passRedisData = redisUtil.lGet(UserExcelExportConstans.EXCEL_PASS_REDIS_KEY_PREFIX+redisKey, 0, redisUtil.lGetListSize(UserExcelExportConstans.EXCEL_PASS_REDIS_KEY_PREFIX+redisKey));
		if (CollectionUtil.isEmpty(passRedisData)) {
			return R.fail(REDIS_DATA_ERROR_MSG);
		}
		int success = 0;
		for (Object data : passRedisData) {
			OrgUser user = BeanUtil.copy(data,OrgUser.class);
			try {
				String serialNo = serialNoService.getSerialNo(UserConstants.USER_CODE, UserConstants.USER_PREFIX, UserConstants.CUSTOMER_LENGTH);
				user.setUserNo(serialNo);
				this.save(user);
				success += 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return R.data(success);
	}

	@Override
	public void exportOrgUserList(HttpServletResponse response, OrgUserVO orgUser) {
		List<OrgUserVO> userVOS = this.getBaseMapper().selectOrgUserPage(orgUser);
		Set<String> userIds=new HashSet<>();
		userVOS.stream().forEach(u->{
			Long createUser = u.getCreateUser();
			Long updateUser = u.getUpdateUser();
			if (createUser!=null){
				userIds.add(createUser.toString());
			}
			if (updateUser!=null){
				userIds.add(updateUser.toString());
			}
		});
		//不全创建人姓名
		Map<String, String> userNames = userClient.listUserByIds(StringUtils.join(userIds, ",")).stream().collect(Collectors.toMap(o->o.getId().toString(), User::getRealName, (key1, key2) -> key2));
		//性别
		Map<String,String> sexs = dictClient.getList(UserExcelExportConstans.DIC_SEX_CODE).getData().stream().collect(Collectors.toMap(o->o.getDictKey().toString(), Dict::getDictValue, (key1, key2) -> key2));
		//用户类型
		Map<String,String> userTypes = dictClient.getList(UserExcelExportConstans.DIC_USER_TYPE_CODE).getData().stream().collect(Collectors.toMap(o->o.getDictKey().toString(), Dict::getDictValue, (key1, key2) -> key2));
		//所属机构
		Map<String,String> detList = sysClient.getDetList().stream().collect(toMap(o->o.getId().toString(), Dept::getDeptName, (key1, key2) -> key2));
		//补充用户企业用户 其他信息
		userVOS.stream().forEach(e->{
			e.setCreateUserName(getStrField(userNames,e.getCreateUser()));
			e.setUpdateUserName(getStrField(userNames,e.getUpdateUser()));
			e.setUserSexName(getStrField(sexs,e.getUserSex()));
			e.setUserTypeName(getStrField(userTypes,e.getUserType()));
			e.setCustOrgName(getStrField(detList,e.getCustOrgName()));
		});
		this.exportTemplate(response,UserExcelExportConstans.EXPORT_USER_FILE,OrgUserVO.class,userVOS,orgUser.getColumn());
	}

	@Override
	public OrgUser findOneOrgUserByManager(String id) {
		QueryWrapper<OrgUser> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("cust_manager",id);
		queryWrapper.eq("is_deleted",0);
		queryWrapper.last("  limit 1");
		OrgUser user = this.getOne(queryWrapper);
		return user;
	}

	@Override
	public List<OrgUserExcelVO> findByCustomerIds(List<Long> customerIds) {
		return baseMapper.findByCustomerIds(customerIds);
	}

	private String getStrField(Map<String, String> map, Object key) {
		if (ObjectUtils.isNotEmpty(key)) {
			return map.get(key.toString());
		}
		return null;
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
	/**
	 *  @param response  输出流
	 * @param fileName 下载文件名称
	 * @param dataClass 导入的class模板
	 * @param data 模板数据
	 * @param column 需要导出的列  为null 或者size=0 就全部导出
	 */
	@SneakyThrows
	private <T> void exportTemplate(HttpServletResponse response, String fileName, Class<T> dataClass, List<T> data, List<String> column) {
		buildResponse(response, fileName);
		ExcelWriter excelWriter = null;
		try {
			if (StringUtil.equals(UserExcelExportConstans.EXPORT_USER_TEMPLATE_FILE, fileName)) {
				//添加用户类型下拉用户类型
				List<String> userTypes = dictClient.getList(UserExcelExportConstans.DIC_USER_TYPE_CODE).getData().stream().map(Dict::getDictValue).collect(toList());
				//性别
				List<String> userSexs = dictClient.getList(UserExcelExportConstans.DIC_SEX_CODE).getData().stream().map(Dict::getDictValue).collect(toList());
				//客户经理
				List<String> managers = contactService.list(new QueryWrapper<OrgContact>().eq("is_deleted", 0)).stream().map(OrgContact::getCustManager).collect(toList());
				excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new InitUserCellWriteHandler(userTypes,userSexs,managers)).build();
			}else {
				excelWriter=EasyExcel.write(response.getOutputStream()).build();
			}
			WriteSheet customerSheet = EasyExcel.writerSheet(UserExcelExportConstans.EXPORT_CUSTOMER_SHEET1_NAME).head(dataClass).build();
			if (column!=null&&column.size()>0){
				customerSheet.setIncludeColumnFiledNames(column);
			}
			excelWriter.write(data, customerSheet);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}
	/**
	 * 构建下载信息
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

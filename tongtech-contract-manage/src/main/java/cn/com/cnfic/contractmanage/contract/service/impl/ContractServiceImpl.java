package cn.com.cnfic.contractmanage.contract.service.impl;

import cn.com.cnfic.contractmanage.common.service.ISerialNoService;
import cn.com.cnfic.contractmanage.common.util.StrRepUtil;
import cn.com.cnfic.contractmanage.contract.constant.ContractConstant;
import cn.com.cnfic.contractmanage.contract.dto.ContractDTO;
import cn.com.cnfic.contractmanage.contract.entity.*;
import cn.com.cnfic.contractmanage.contract.excel.ExportContractConstant;
import cn.com.cnfic.contractmanage.contract.mapper.ConProductMapper;
import cn.com.cnfic.contractmanage.contract.mapper.ContractMapper;
import cn.com.cnfic.contractmanage.contract.service.IConAgreementService;
import cn.com.cnfic.contractmanage.contract.service.IContractFileService;
import cn.com.cnfic.contractmanage.contract.service.IContractService;
import cn.com.cnfic.contractmanage.contract.vo.ContractVO;
import cn.com.cnfic.contractmanage.contract.vo.ExcelConContactVO;
import cn.com.cnfic.contractmanage.contract.vo.ExcelConUserVO;
import cn.com.cnfic.contractmanage.contract.vo.ExcelProductVO;
import cn.com.cnfic.contractmanage.customer.entity.OrgContact;
import cn.com.cnfic.contractmanage.customer.service.IOrgContactService;
import cn.com.cnfic.contractmanage.product.entity.ItemValue;
import cn.com.cnfic.contractmanage.product.mapper.ItemValueMapper;
import cn.com.cnfic.contractmanage.user.excel.InitUserCellWriteHandler;
import cn.com.cnfic.contractmanage.user.excel.UserExcelExportConstans;
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
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 合约表服务实现类
 *
 * @author zhaijw
 * @since 2021-03-23
 */
@Service
@AllArgsConstructor
public class ContractServiceImpl extends BaseServiceImpl<ContractMapper, Contract> implements IContractService {

	private IUserClient userClient;

	private ContractMapper contractMapper;

	private ISerialNoService serialNoService;

	private IContractFileService contractFileService;

	private IConAgreementService conAgreementService;

	private IDictClient dictClient;

	private ISysClient sysClient;

	private ConProductMapper conProductMapper;

	private ItemValueMapper itemValueMapper;


	/**
	 * 用户数据权限list的分隔符
	 */
	private static final String DATA_AUTH_DIVISION = ",";
	/**
	 * 合约编号常量字符串
	 */
	private static final String CONTRACT_NO = "合约编号【";
	/**
	 * 产品关联合约提示信息
	 */
	private static final String HAS_ASSOCIATED_PRODUCTION = "】,有关联产品不可删除<br>";
	/**
	 * 用户关联合约提示信息
	 */
	private static final String HAS_ASSOCIATED_USER = "】,有关联用户不可删除<br>";

	@Override
	public IPage<ContractVO> selectContractPage(Query query, ContractDTO contractDto) {
		//获取前端选择的部门信息，如果没有选择，则根据当前登录人的部门数据权限过滤
		//部门过滤条件统一放在一个list集合中
		List<String> userDataAuthList = new ArrayList<String>();
		Long deptId = contractDto.getDeptId();
		if(deptId!=null && deptId!=0L){
			userDataAuthList.add(deptId.toString());
		}else{
			//获取当前登录人的id
			Long userId = SecureUtil.getUser().getUserId();
			//获取当前登录人的部门权限信息
			UserInfo user = userClient.userInfo(userId).getData();
			String dataAuths = user.getUser().getDataAuthority();
			userDataAuthList.addAll(StrRepUtil.strConvertList(dataAuths, DATA_AUTH_DIVISION));
		}
		IPage<ContractDTO> page = Condition.getPage(query);
		return contractMapper.selectContractPage(page,contractDto,userDataAuthList);
	}

	@Override
	public List<ContractVO> findByCustContactId(Long custContactId) {
		return contractMapper.selectByCustContactId(custContactId);
	}

	@Override
	public List<ContractVO> findByCustId(Long custId) {
		return contractMapper.selectByCustId(custId);
	}

	@Override
	@Transactional
	public R<Contract> saveBasicInfo(ContractDTO contractDto) {
		//判断id是否为空，如果id为空是新创建的
		if(Func.isEmpty(contractDto.getId())) {
			//合约来源定义为手动创建
			contractDto.setConSource(ContractConstant.CONTRACT_SOURCE_CREATE);
			//生成合约编号
			String contractNo = serialNoService.getSerialNo(ContractConstant.CONTRACT_NO_CODE,
					ContractConstant.CONTRACT_NO_FIX, ContractConstant.CONTRACT_NO_LENGTH);
			contractDto.setConNo(contractNo);
			//设置合约未完成创建状态
			contractDto.setIsCreated(ContractConstant.CONTRACT_CREATED_NO);
		}
		super.saveOrUpdate(contractDto);
		if(Func.isNotEmpty(contractDto.getFileList())) {
			//更新合约附件文件,先删除
			contractFileService.remove(new QueryWrapper<ContractFile>().eq("cont_id", contractDto.getId()));
			//重新保存文件信息
			List<ContractFile> fileList = contractDto.getFileList();
			fileList.stream().forEach(e -> {
				e.setContId(contractDto.getId());
				e.setCreateUser(contractDto.getCreateUser());
				e.setCreateTime(DateUtil.now());
				e.setIsDeleted(0);
			});
			contractFileService.saveBatch(fileList);
		}
		return R.data(contractDto);
	}

	@Override
	@Transactional
	public R saveUserAgreementInfo(ContractDTO contractDto) {
		// 合约标识
		Long id = contractDto.getId();
		// 合约未创建则更新
		if (Func.isEmpty(id)) {
			saveBasicInfo(contractDto);
			id = contractDto.getId();
		}
		List<Long> agreementIdList = contractDto.getAgreementIdList();
		// 保存新协议前删除旧协议
		conAgreementService.remove(new QueryWrapper<ConAgreement>().eq("con_id", id));
		// 批量保存
		List<ConAgreement> conAgreementList = new ArrayList<>();
		boolean result = true;
		// 没有数据则不做处理
		if (!Func.isEmpty(agreementIdList)) {
			for (Long agreementId : agreementIdList) {
				ConAgreement e = new ConAgreement();
				e.setConId(id);
				e.setUserAgreementId(agreementId);
				e.setCreateUser(contractDto.getCreateUser());
				e.setCreateTime(DateUtil.now());
				e.setIsDeleted(0);
				conAgreementList.add(e);
			}
			result = conAgreementService.saveBatch(conAgreementList);
		}
		return result ? R.data(String.valueOf(id)) : R.status(false);
	}

	@Override
	@Transactional
	public R removeContract(ContractDTO contractDto) {
		List<Long> contractRemoveIds = contractDto.getContractRemoveIds();
		if (Func.isEmpty(contractRemoveIds)) {
			return R.data(new ArrayList<>());
		}
		// 1、校验绑定产品和用户 产品权限
		List<ContractVO> contractProUserInfo = contractMapper.getContractProUserInfo(contractRemoveIds);
		if (Func.isEmpty(contractProUserInfo)) {
			return R.data(new ArrayList<>());
		}
		// 可删除合约集合
		List<Long> canDel = new ArrayList<>();
		// 不可删除合约集合
		List<ContractVO> canNotDel = new ArrayList<>();
		// 错误信息
		StringBuffer errorMsg = new StringBuffer();
		// 2、过滤绑定了的合约id,同时删除其他合约
		contractProUserInfo.stream().forEach((e) -> {
			if (e.getProductCnt() > 0L || e.getUserCnt() > 0L) {
				canNotDel.add(e);
				errorMsg.append(CONTRACT_NO)
						.append(e.getConNo())
						.append(e.getProductCnt() > 0L ? HAS_ASSOCIATED_PRODUCTION : HAS_ASSOCIATED_USER);
			} else {
				canDel.add(e.getId());
			}
		});

		if (!Func.isEmpty(canDel)) {
			// 3、可删除的合约删除
			contractMapper.deleteBatchIds(canDel);
			// 4、更新 合约表同步用友信息合同id  用友合同表更新是否已创建合约
			contractMapper.updateContractAndNc(canDel);
		}
		if(!Func.isEmpty(canNotDel)) {
			return R.data(canNotDel, errorMsg.toString());
		}
		return R.data(new ArrayList<>());
	}

	@Override
	public void exportContractList(HttpServletResponse response, ContractDTO contractDto) {
		if(Func.isEmpty(contractDto.getSelectedRow())) {
			contractDto.setOnlySelected(false);
		} else {
			contractDto.setOnlySelected(true);
		}
		//查询基础数据
		List<ContractVO> result = contractMapper.selectContractInfo(contractDto);
		if(Func.isEmpty(result)) return ;
		// 编辑基础信息
		Set<String> userIds=new HashSet<>();
		List<Long> contractIds = new ArrayList<>();
		Map<Long, String> contractIdCode = new HashMap<>();
		for(ContractVO e : result) {
			Long createUser = e.getCreateUser();
			Long updateUser = e.getUpdateUser();
			if (Func.isEmpty(createUser)){
				userIds.add(createUser.toString());
			}
			if (Func.isEmpty(updateUser)){
				userIds.add(updateUser.toString());
			}
			contractIds.add(e.getId());
			contractIdCode.put(e.getId(), e.getConNo());
		}
		editBaseContractInfo(result, userIds);
		List<OrgContact> orgContacts = null;
		// 查询联系人信息
		List<ExcelConContactVO> excelConContactVOList = new ArrayList<>();
		if (contractDto.getLinkmanInfoChecked()) {
			excelConContactVOList = contractMapper.selectExcelConContract(contractIds);
		}

		// 查询用户信息
		List<ExcelConUserVO> excelConUserVOList = new ArrayList<>();
		if (contractDto.getUserInfoChecked()) {
			excelConUserVOList = contractMapper.selectExcelContractUsers(contractIds);
		}

		// 查询产品信息
		List<ExcelProductVO> proDucts = new ArrayList<>();
		if (contractDto.getContactInfoChecked()) {
			// 编辑产品信息
			editProduction(contractIds, contractIdCode, proDucts);
		}

		ExcelWriter excelWriter = null;
		try {
			buildResponse(response, ExportContractConstant.EXPORT_FILE_NAME);
			excelWriter = EasyExcel.write(response.getOutputStream()).build();
			// 导出基本信息
			exportTemplate(excelWriter, ContractVO.class, result,
					null, ExportContractConstant.EXPORT_SHEET_NAME_BASE);
			// 导出联系人信息
			if(contractDto.getLinkmanInfoChecked()) {
				exportTemplate(excelWriter, ExcelConContactVO.class, excelConContactVOList,
						null, ExportContractConstant.EXPORT_SHEET_NAME_LINK);
			}
			// 导出用户信息
			if(contractDto.getUserInfoChecked()) {
				exportTemplate(excelWriter, ExcelConUserVO.class, excelConUserVOList,
						null, ExportContractConstant.EXPORT_SHEET_NAME_USER);
			}
			// 导出产品信息
			if(contractDto.getContactInfoChecked()) {
				exportTemplate(excelWriter, ExcelProductVO.class, proDucts,
						null, ExportContractConstant.EXPORT_SHEET_NAME_PRODUCT);
			}

		} catch (IOException e) {

		} finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	/**
	 * 设置产品项扩展属性
	 * @param proId 产品id
	 * @param props 产品属性
	 * @param proItemCollection 产品项集合
	 */
	private void setExpandProp(Long proId, List<ItemValue> props, Map<String, List<ExcelProductVO>> proItemCollection) {
		List<Map<String, String>> itemPros = new ArrayList<>();
		if(Func.isEmpty(props)) return ;
		// 搜集属性
		props.forEach((e)->{
			Map<String, String> item = new HashMap<>();
			item.put(e.getPropName(), e.getPropValue());
			itemPros.add(item);
		});
		// 设置扩展属性
		List<ExcelProductVO> excelProductVOS = proItemCollection.get(proId);
		if(!Func.isEmpty(excelProductVOS)) {
			excelProductVOS.forEach((e)->{
				e.setExpandProp(JSON.toJSONString(itemPros));
			});
		}
	}

	/**
	 *  产品信息获取
	 * @param contractIds 合约标识集合
	 * @param contractIdCode 合约标识同合约编码对应对象
	 * @param proDucts 保存产品信息
	 */
	private void editProduction(List<Long> contractIds, Map<Long, String> contractIdCode, List<ExcelProductVO> proDucts ) {
		// 1、查询合约产品信息
		QueryWrapper qw = new QueryWrapper<ConProduct>();
		qw.in("con_id",contractIds);
		List<ConProduct> conProList = conProductMapper.selectList(qw);

		// 2、按合约类型分组 Map<Boolean, List<ConProduct>> collect =
		// 产品id集合
		List<Long> pro = new ArrayList<>();
		// 产品项id集合
		List<Long> proItem = new ArrayList<>();
		if(!Func.isEmpty(conProList)){
			conProList.stream().collect(Collectors.groupingBy((e) -> {
				return e.getType();
			}, toList())).forEach((k, v)->{
				if(k) {
					pro.addAll(v.stream().map((e) -> {
						return e.getProdId();
					}).collect(toList()));
				} else {
					proItem.addAll(v.stream().map((e) -> {
						return e.getProdId();
					}).collect(toList()));
				}
			});

		}
		// 3、查询产品信息
		List<ExcelProductVO> productAndValues = new ArrayList<>();
		if(!Func.isEmpty(pro)) {
			productAndValues = conProductMapper.getProductAndValues(pro);
			setConNo(productAndValues, contractIdCode);
		}
		List<ExcelProductVO> productItemAndValues = new ArrayList<>();
		// 4 、查询产品项信息
		if(!Func.isEmpty(proItem)) {
			productItemAndValues = conProductMapper.getProductAndValues(proItem);
			setConNo(productItemAndValues, contractIdCode);
			// 获取产品扩展属性
			Map<String, List<ExcelProductVO>> tempList = productItemAndValues.stream().collect(Collectors.groupingBy(ExcelProductVO::getProdId, toList()));
			QueryWrapper<ItemValue> queryProp = new QueryWrapper<ItemValue>();
			queryProp.in("prod_id",proItem);
			itemValueMapper.selectList(queryProp)
					.stream()
					.collect(Collectors.groupingBy(ItemValue::getProdId, toList())).forEach((k, v)->{
				setExpandProp(k, v, tempList);
			});
		}
		proDucts.addAll(productItemAndValues);
		proDucts.addAll(productItemAndValues);
	}
	/**
	 * 设定合约编号
	 * @param excelProductList
	 * @param contractIdCode
	 */
	private void setConNo(List<ExcelProductVO> excelProductList, Map<Long, String> contractIdCode) {
		if(!Func.isEmpty(excelProductList)) {
			excelProductList.forEach((e)->{
				e.setConNo(contractIdCode.get(e.getConId()));
			});
		}
	}

	/**
	 *  编辑合约基础信息
	 * @param contractList
	 */
	private void editBaseContractInfo(List<ContractVO> contractList, Set<String> userIds) {

		if(Func.isEmpty(contractList)) {
			return ;
		}

		// 合约范围字典
		Map<Integer, String> conLimitMap = dictClient.getList(ExportContractConstant.DIC_CON_LIMIT_CODE).getData()
				.stream().collect(Collectors.toMap( Dict::getDictKey, Dict::getDictValue,(key1, key2) -> key2));
		// 合约类型字典
		Map<Integer, String> contractTypeMap = dictClient.getList(ExportContractConstant.DIC_CONTRACT_TYPE_CODE).getData()
				.stream().collect(Collectors.toMap( Dict::getDictKey, Dict::getDictValue,(key1, key2) -> key2));
		// 合约状态字典
		Map<Integer, String> contractStateMap = dictClient.getList(ExportContractConstant.DIC_CONTRACT_STATE_CODE).getData()
				.stream().collect(Collectors.toMap( Dict::getDictKey, Dict::getDictValue,(key1, key2) -> key2));
		// 签约状态字典
		Map<Integer, String> signStateMap = dictClient.getList(ExportContractConstant.DIC_SIGN_STATE_CODE).getData()
				.stream().collect(Collectors.toMap( Dict::getDictKey, Dict::getDictValue,(key1, key2) -> key2));
		Map<Long, String> deptMap =sysClient.getDetList().stream().collect(Collectors.toMap(Dept::getId, Dept::getDeptName));
		//不全创建人姓名
		Map<Long, String> userNames = userClient.listUserByIds(StringUtils.join(userIds, ",")).stream()
				.collect(Collectors.toMap(o->o.getId(), User::getRealName, (key1, key2) -> key2));

		for(ContractVO ele : contractList) {
			ele.setConLimitName(getStrField(conLimitMap, ele.getConLimit()));
			ele.setConStateName(getStrField(contractStateMap, ele.getConState()));
			ele.setConTypeName(getStrField(contractTypeMap, ele.getConType()));
			ele.setSignStateName(getStrField(signStateMap, ele.getSignState()));
			ele.setDeptName(getStrField(deptMap, ele.getDeptId()));
			ele.setUpdatedUserName(getStrField(userNames, ele.getUpdateUser()));
			ele.setUpdatedTime(DateUtils.format(ele.getUpdateTime(),DateUtils.DATE_FORMAT_19));
			ele.setCreateUserName(getStrField(userNames, ele.getUpdateUser()));
			ele.setCreatedTime(DateUtils.format(ele.getCreateTime(),DateUtils.DATE_FORMAT_19));
		}
	}

	/**
	 *  转换字典值
	 * @param map 字典
	 * @param key 键
	 * @return
	 */
	private String getStrField(Map map, Object key) {
		if (ObjectUtils.isNotEmpty(key)) {
			Object val = map.get(key);
			return Func.isEmpty(val) ? null : val.toString();
		}
		return null;
	}

	/**
	 *
	 * @param excelWriter excel写工具
	 * @param dataClass 导入的class模板
	 * @param data 模板数据
	 * @param column  需要导出的列  为null 或者size=0 就全部导出
	 * @param sheetName excelsheet名
	 */
	@SneakyThrows
	private <T> void exportTemplate(ExcelWriter excelWriter, Class<T> dataClass, List<T> data, List<String> column, String sheetName) {
			WriteSheet customerSheet = EasyExcel.writerSheet(sheetName).head(dataClass).build();
			if (column != null && column.size() > 0) {
				customerSheet.setIncludeColumnFiledNames(column);
			}
			excelWriter.write(data, customerSheet);
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
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		return fileNameEncode;
	}
}

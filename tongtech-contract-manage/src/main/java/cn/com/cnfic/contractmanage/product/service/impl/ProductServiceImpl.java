package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.dto.ProductDTO;
import cn.com.cnfic.contractmanage.product.entity.Product;
import cn.com.cnfic.contractmanage.product.entity.ValuationDetail;
import cn.com.cnfic.contractmanage.product.enums.MsgEnum;
import cn.com.cnfic.contractmanage.product.mapper.ProductMapper;
import cn.com.cnfic.contractmanage.product.service.*;
import cn.com.cnfic.system.entity.Dept;
import cn.com.cnfic.system.entity.Dict;
import cn.com.cnfic.system.feign.IDictClient;
import cn.com.cnfic.system.feign.ISysClient;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.api.ResultCode;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springblade.core.tool.utils.DateUtil.now;

/**
 * @desc 产品表 服务实现类
 * @author Cnfic-UserManage
 * @date 2021-03-24
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements IProductService {

	private IProfitRateService iProfitRateService;

	private IImagesService iImagesService;

	private IValuationService iValuationService;

	private IValuationDetailService iValuationDetailService;

	private IGroupDetailService iGroupDetailService;

	private IDictClient dictClient;

	private ISysClient sysClient;
	private IProdItemRelationService iProdItemRelationService;

	@Override
	public IPage<Product> selectProductPage(Query query, ProductDTO product) {
		QueryWrapper<Product>  queryWrapper= Condition.getQueryWrapper(new Product());
		queryWrapper.select("*");
		getQueryWrapper(queryWrapper, product);
		queryWrapper.orderByDesc("create_time");
		return this.page(Condition.getPage(query), queryWrapper);
	}

	@Override
	public R<Boolean> updateProductState(String ids, Integer state) {
		Product prod = new Product();
		prod.setProdState(state);
		if(Func.toInt(state) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
			prod.setAddedDate(LocalDateTime.now());
		}else if(Func.toInt(state) == ProductConstants.PRODUCT_ITEM_EXPIRED_STATE){
			prod.setExpiredDate(LocalDateTime.now());
			//判断是否在产品项中 即存在则不能下架
			int p = iProdItemRelationService.getItemListByProdId(Func.toLongArray(ids));
			Assert.isTrue(p<=0, MsgEnum.CHECK_ITEM_EXIST_ITEM_MSG.getMsg());
			//判断是否在已上架的组合产品中 即存在则不能下架/删除
			int i = iGroupDetailService.queryGroupByItem(ProductConstants.PRODUCT_TYPE,Func.toLongArray(ids));
			Assert.isTrue(i<=0, MsgEnum.CHECK_ITEM_EXIST_GROUP_MSG.getMsg());
		}
		int t=baseMapper.updateState(prod,Func.toLongList(ids));
		return R.data(t>0,t>0?"操作成功！":"操作失败！");
	}

	@Override
	public R<Boolean> updateStateByTask(Integer state) {
		Product prod = new Product();
		prod.setProdState(state);
		if(Func.toInt(state) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
			prod.setAddedDate(LocalDateTime.now());
		}else if(Func.toInt(state) == ProductConstants.PRODUCT_ITEM_EXPIRED_STATE){
			prod.setExpiredDate(LocalDateTime.now());
		}
		int t=baseMapper.updateStateByDate(prod);
		return R.data(t>0,t>0?"操作成功！":"操作失败！");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> removeProductByIds(String ids) {
		//判断是否在产品项中 即存在则不能删除
		if(Func.isNotEmpty(ids)){
			int p = iProdItemRelationService.getItemListByProdId(Func.toLongArray(ids));
			Assert.isTrue(p<=0, MsgEnum.CHECK_ITEM_EXIST_ITEM_MSG.getMsg());
			//判断是否在已上架的组合产品中 即存在则不能删除
			int i = iGroupDetailService.queryGroupByItem(ProductConstants.PRODUCT_TYPE,Func.toLongArray(ids));
			Assert.isTrue(i<=0, MsgEnum.CHECK_ITEM_EXIST_GROUP_MSG.getMsg());
			iProfitRateService.removeByProdId(ProductConstants.PRODUCT_TYPE, Func.toLongArray(ids));
			iImagesService.removeByProdId(ProductConstants.PRODUCT_TYPE, Func.toLongArray(ids));
			iValuationService.removeByProdId(ProductConstants.PRODUCT_TYPE, Func.toLongArray(ids));
			iValuationDetailService.removeByProdId(ProductConstants.PRODUCT_TYPE, Func.toLongArray(ids));
			iGroupDetailService.removeByItemId(ProductConstants.PRODUCT_TYPE, Func.toLongArray(ids));
			return R.data(baseMapper.deleteBatchIds(Func.toLongList(ids))>0);
		}
		return R.data(false);
	}

	@Override
	public R<Boolean> validProdCode(Long id, String code) {
		QueryWrapper<Product> query = Condition.getQueryWrapper(new Product());
		// 不为空表示为编辑 即校验非当前数据是否重复
		if(Func.isNotEmpty(id)){
			query.ne("id",id);
		}
		query.eq("prod_code", code);
		return R.data(this.count(query)==0,
				this.count(query)==0? MsgEnum.SUCCESS_MSG.getMsg() : MsgEnum.CHECK_PROD_CODE_REPEAT_MSG.getMsg());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> saveProd(ProductDTO product) {
		//校验数据
		checkValid(product);
		Long userId = SecureUtil.getUserId();
		// 保存 产品项信息
		product.setIsValuation(product.getIsValuation());
		product.setCreateUser(userId);
		product.setCreateTime(now());
		product.setId(IdWorker.getId());
		product.setIsDeleted(ProductConstants.IS_DELETED);
		if(Func.toInt(product.getProdState()) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
			product.setAddedDate(LocalDateTime.now());
		}
		// 保存计价属性信息以及计价详情信息
		iValuationService.handleValuations(product.getValuations(), userId, product.getId(),ProductConstants.PRODUCT_TYPE);
		// 保存分润比例数据-阶梯比例
		iProfitRateService.handleProfitRates(product.getProfitRates(), userId, product.getId(),ProductConstants.PRODUCT_TYPE);
		// 保存产品项图片
		iImagesService.handlePictures(product.getFileList(), userId, product.getId(),ProductConstants.PRODUCT_TYPE);
		return R.data(this.save(product));
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> updateProd(ProductDTO product) {
		//校验数据
		checkValid(product);
		Long userId = SecureUtil.getUserId();
		// 保存 产品项信息
		product.setIsValuation(product.getIsValuation());
		product.setUpdateUser(userId);
		product.setUpdateTime(now());
		product.setIsDeleted(ProductConstants.IS_DELETED);
		if(Func.toInt(product.getProdState()) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
			product.setAddedDate(LocalDateTime.now());
		}
		// 保存计价属性信息以及计价详情信息
		iValuationService.handleValuations(product.getValuations(), userId, product.getId(),ProductConstants.PRODUCT_TYPE);
		// 保存分润比例数据-阶梯比例
		iProfitRateService.handleProfitRates(product.getProfitRates(), userId, product.getId(),ProductConstants.PRODUCT_TYPE);
		// 保存产品项图片
		iImagesService.handlePictures(product.getFileList(), userId, product.getId(),ProductConstants.PRODUCT_TYPE);
		return R.data(baseMapper.updateProdById(product)>0);
	}

	@Override
	public Map<String, List<List<String>>> exportData(ProductDTO product) {
		// head-Name set
		String[] headName=new String[]{"产品名称","产品编码","所属部门","应用分类",
				"发布范围","上架时间","下架时间","产品描述"};
		String[] headLastName=new String[]{"标准价","注销价","币种"};
		String[] headCode=new String[]{"prodName","prodCode","propDept","prodType",
				"releaseScope","addedDate","expiredDate","prodDesc"};
		String[] headLastCode=new String[]{"standardPrice","promotionPrice","currency"};
		// head-Name Set
		LinkedHashSet<String> headNameSet = new LinkedHashSet(Arrays.asList(headName));
		// head-code set
		LinkedHashSet<String> headCodeSet = new LinkedHashSet(Arrays.asList(headCode));
		// 查询所有计价属性
		List<ValuationDetail> valuatioList = getAllValuation(ProductConstants.PRODUCT_TYPE);
		if(Func.isNotEmpty(valuatioList)){
			for (ValuationDetail detail:valuatioList) {
				headNameSet.add(detail.getPropName());
				headCodeSet.add(detail.getPropCode());
			}
		}
		headNameSet.addAll(Arrays.asList(headLastName));
		headCodeSet.addAll(Arrays.asList(headLastCode));
		Map<String,List<List<String>>> excelMap = new HashMap<>(16);
		excelMap.put("head",getExcelHeadList(headNameSet));
		excelMap.put("data",getExcelDateList(product, headCodeSet));
		return excelMap;
	}

	@Override
	public List<Product> getAddedList(ProductDTO product) {
		QueryWrapper<Product> query = Condition.getQueryWrapper(new Product());
		query.select("*");
		query.eq("prod_state", ProductConstants.PRODUCT_ITEM_PUBLISH_STATE);
		if(Func.isNotEmpty(product)){
			if(Func.isNotEmpty(product.getProdName())){
				query.likeRight("prod_name", product.getProdName());
			}
			if(Func.isNotEmpty(product.getProdType())){
				query.eq("prod_type", product.getProdType());
			}
			if(Func.isNotEmpty(product.getExclude())){
				query.notIn("id", Func.toLongList(product.getExclude()));
			}
			if(Func.isNotEmpty(product.getInclude())){
				query.in("id", Func.toLongList(product.getInclude()));
			}
		}
		return baseMapper.selectList(query);
	}

	/**
	 * 获取导出数据
	 * @param product
	 * @param headCodeSet
	 * @return
	 */
	private List<List<String>> getExcelDateList(ProductDTO product,LinkedHashSet<String> headCodeSet){
		//币种
		Map<Integer,String> currencyType = getDicValueByCode(ProductConstants.CURRENCY_TYPE_CODE);
		//发布范围
		Map<Integer,String> productRange = getDicValueByCode(ProductConstants.PRODUCT_RANGE_CODE);
		//应用分类
		Map<Integer,String> productType = getDicValueByCode(ProductConstants.PRODUCT_TYPE_CODE);
		// 获取部门map
		Map<Long,String> deptMap = getDeptMap();
		List<List<String>> excelDataList = new ArrayList<>();
		List<Map<String,Object>> excelVos = baseMapper.selectProdAndValuation(product);
		JSONArray jsonArray = null;
		Map<String,Object> jsonMap = null;
		List<String> data = null;
		// 开始组装数据
		for (Map<String,Object> excel:excelVos) {
			data = new ArrayList<>();
			if(excel!=null && excel.containsKey("valuationValues")){
				jsonArray = JSONUtil.parseArray(Func.toStr(excel.get("valuationValues")));
				jsonMap= new HashMap<>(16);
				for (Object obj: jsonArray) {
					JSONObject json = JSONUtil.parseObj(obj);
					jsonMap.put(Func.toStr(json.get("propCode")),json.get("propValue"));
				}
			}
			for (String code :headCodeSet) {
				if(excel.containsKey(code)){
					if(Func.isNotEmpty(deptMap) && "propDept".equals(code)){
						data.add(deptMap.get(excel.get(code)));
					}else if(Func.isNotEmpty(productRange) && "releaseScope".equals(code)){
						data.add(productRange.get(excel.get(code)));
					}else if(Func.isNotEmpty(productType) && "prodType".equals(code)){
						data.add(productType.get(excel.get(code)));
					}else if(Func.isNotEmpty(currencyType) && "currency".equals(code)){
						data.add(currencyType.get(Func.toInt(excel.get(code))));
					}else{
						data.add(Func.toStr(excel.get(code)));
					}
				}else if(Func.isNotEmpty(jsonMap) && jsonMap.containsKey(code)){
					data.add(Func.toStr(jsonMap.get(code)));
				}else{
					data.add("");
				}
			}
			excelDataList.add(data);
		}
		return excelDataList;
	}
	/**
	 * 获取excel 头部信息
	 * @param headNameSet
	 * @return
	 */
	private List<List<String>> getExcelHeadList(LinkedHashSet<String> headNameSet){
		List<List<String>> excelHeadList = new ArrayList<>();
		List<String> excelHead = null;
		for (String str:headNameSet) {
			excelHead=new ArrayList<>();
			excelHead.add(str);
			excelHeadList.add(excelHead);
		}
		return excelHeadList;
	}
	/**
	 * 获取部门map
	 * @return
	 */
	private Map<Long,String> getDeptMap(){
		Map<Long, String> res = null;
		List<Dept> depts =sysClient.getDetList();
		if(Func.isNotEmpty(depts)) {
			res = depts.stream().collect(Collectors.toMap(Dept::getId, Dept::getDeptName));
		}
		return res;
	}
	/**
	 * 获取所有计价属性
	 * @return
	 */
	private List<ValuationDetail> getAllValuation(Integer type){
		QueryWrapper<ValuationDetail> query= Condition.getQueryWrapper(new ValuationDetail());
		query.select("prop_code,prop_name");
		query.eq("type",type);
		query.groupBy("prop_code,prop_name");
		return iValuationDetailService.list(query);
	}
	/**
	 * 根据字典编码获取字典map 对象
	 * @param code
	 * @return
	 */
	private Map<Integer,String> getDicValueByCode(String code){
		Map<Integer,String> res = null;
		R<List<Dict>> listR = dictClient.getList(code);
		if(ResultCode.SUCCESS.getCode()==listR.getCode()){
			List<Dict> dictList= listR.getData();
			res = dictList.stream().collect(Collectors.toMap(Dict::getDictKey, Dict::getDictValue));
		}
		return res;
	}
	/**
	 * 数据校验
	 * @param product
	 */
	private void checkValid(ProductDTO product){
		//校验数据
		R<Boolean> r = this.validProdCode(product.getId(), product.getProdCode());
		Assert.isTrue(r.getData(),r.getMsg());
		Assert.isTrue(Func.isNotEmpty(product.getProdCode()),MsgEnum.CHECK_PROD_CODE_REPEAT_MSG.getMsg());
		Assert.isTrue(Func.isNotEmpty(product.getProdName()),MsgEnum.CHECK_PROD_NAME_NULL_MSG.getMsg());
		if(product.getProdState() < ProductConstants.PRODUCT_ITEM_DRAFT_STATE){
			Assert.isTrue(Func.isNotEmpty(product.getPropDept()),MsgEnum.CHECK_ITEM_PROP_DEPT_NULL_MSG.getMsg());
			Assert.isTrue(Func.isNotEmpty(product.getReleaseScope()),MsgEnum.CHECK_ITEM_RELEASE_SCOPE_NULL_MSG.getMsg());
			Assert.isTrue(Func.isNotEmpty(product.getProdType()),MsgEnum.CHECK_PROD_TYPE_NULL_MSG.getMsg());
		}
	}
	/**
	 * 获取查询条件
	 * @param queryWrapper
	 * @param dto
	 * @return
	 */
	private QueryWrapper<Product> getQueryWrapper(QueryWrapper<Product> queryWrapper, ProductDTO dto){
		// 产品名称
		if(Func.isNotEmpty(dto.getProdName())){
			queryWrapper.likeRight("prod_name",dto.getProdName());
		}
		// 产品编码
		if(Func.isNotEmpty(dto.getProdCode())){
			queryWrapper.likeRight("prod_code",dto.getProdCode());
		}
		// 发布范围
		if(Func.isNotEmpty(dto.getReleaseScope())){
			queryWrapper.eq("release_scope",dto.getReleaseScope());
		}
		// 产品状态
		if(Func.isNotEmpty(dto.getProdState())){
			queryWrapper.eq("prod_state",dto.getProdState());
		}
		// 所属部门
		if(Func.isNotEmpty(dto.getPropDept())){
			queryWrapper.eq("prop_dept",dto.getPropDept());
		}
		// 应用分类
		if(Func.isNotEmpty(dto.getProdType())){
			queryWrapper.eq("prod_type",dto.getProdType());
		}
		// 创建时间
		if(Func.isNotEmpty(dto.getCreateStartTime()) && Func.isNotEmpty(dto.getCreateEndTime())){
			queryWrapper.between("create_time", dto.getCreateStartTime(), dto.getCreateEndTime());
		}
		// 修改时间
		if(Func.isNotEmpty(dto.getUpdateStartTime()) && Func.isNotEmpty(dto.getUpdateEndTime())){
			queryWrapper.between("update_time", dto.getUpdateStartTime(), dto.getUpdateEndTime());
		}
		return queryWrapper;
	}
}

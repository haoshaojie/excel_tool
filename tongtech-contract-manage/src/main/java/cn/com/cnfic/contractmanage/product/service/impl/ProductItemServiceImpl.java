/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.cnfic.contractmanage.product.service.impl;

import cn.com.cnfic.contractmanage.product.constant.ProductConstants;
import cn.com.cnfic.contractmanage.product.dto.ProductItemDTO;
import cn.com.cnfic.contractmanage.product.entity.*;
import cn.com.cnfic.contractmanage.product.enums.MsgEnum;
import cn.com.cnfic.contractmanage.product.mapper.ProductItemMapper;
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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
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
 * 产品项表 服务实现类
 *
 * @author TongTech-UserManage
 * @since 2021-03-04
 */
@Service
@AllArgsConstructor
public class ProductItemServiceImpl extends ServiceImpl<ProductItemMapper, ProductItem> implements IProductItemService {

	private IItemValueService iItemValueService;

	private IProfitRateService iProfitRateService;

	private IImagesService iImagesService;

	private IValuationService iValuationService;

    private IValuationDetailService iValuationDetailService;

	private IDictClient dictClient;

	private ISysClient sysClient;

	private ICategoryService categoryService;

	private IGroupDetailService iGroupDetailService;

	private IProductService iProductService;

	private IProdItemRelationService iProdItemRelationService;

	@Override
    public R<Boolean> checkPropCode(Long id, String propCode) {
        QueryWrapper<ProductItem> query = Condition.getQueryWrapper(new ProductItem());
        // 不为空表示为编辑 即校验非当前数据是否重复
        if(Func.isNotEmpty(id)){
            query.ne("id",id);
        }
        query.eq("prop_code", propCode);
        return R.data(this.count(query)==0,
                this.count(query)==0?MsgEnum.SUCCESS_MSG.getMsg() : MsgEnum.CHECK_ITEM_PROP_CODE_REPEAT_MSG.getMsg());
    }

    @Override
    public R<Boolean> updateItemState(String ids, Integer itemState) {
        ProductItem item = new ProductItem();
        item.setItemState(itemState);
	    if(Func.toInt(itemState) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
            item.setAddedDate(LocalDateTime.now());
        }else if(Func.toInt(itemState) == ProductConstants.PRODUCT_ITEM_EXPIRED_STATE){
            item.setExpiredDate(LocalDateTime.now());
            //判断是否在已上架的组合产品中 即存在则不能下架/删除
            int i = iGroupDetailService.queryGroupByItem(ProductConstants.PRODUCT_ITEM_TYPE,Func.toLongArray(ids));
            Assert.isTrue(i<=0, MsgEnum.CHECK_ITEM_EXIST_GROUP_MSG.getMsg());
        }

        int t=baseMapper.updateItemState(item,Func.toLongList(ids));
        return R.data(t>0,t>0?"操作成功！":"操作失败！");
    }

    @Override
    public R<Boolean> updateStateByTask(Integer itemState) {
        ProductItem item = new ProductItem();
        item.setItemState(itemState);
        if(itemState.equals(ProductConstants.PRODUCT_ITEM_PUBLISH_STATE)){
            item.setAddedDate(LocalDateTime.now());
        }else{
            item.setExpiredDate(LocalDateTime.now());
        }
        int t=baseMapper.updateItemStateByDate(item);
        return R.data(t>0);
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> saveItem(ProductItemDTO productItem) {
		//校验数据
		checkValid(productItem);
		Long userId = SecureUtil.getUserId();
		// 保存 产品项信息
		productItem.setIsValuation(productItem.getIsValuation());
		productItem.setCreateUser(userId);
		productItem.setCreateTime(now());
        productItem.setId(IdWorker.getId());
		productItem.setIsDeleted(ProductConstants.IS_DELETED);
		if(Func.toInt(productItem.getItemState()) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
            productItem.setAddedDate(LocalDateTime.now());
        }
		// 更新产品项和产品扩展表
        iProdItemRelationService.handleProdItemRelation(productItem.getProdId(),productItem.getId(), userId);
		// 保存扩展信息数据
        iItemValueService.handleItemValues(productItem.getItemValues(), userId, productItem.getId());
		// 保存计价属性信息以及计价详情信息
        iValuationService.handleValuations(productItem.getValuations(), userId, productItem.getId(),ProductConstants.PRODUCT_ITEM_TYPE);
		// 保存分润比例数据-阶梯比例
        iProfitRateService.handleProfitRates(productItem.getProfitRates(), userId, productItem.getId(),ProductConstants.PRODUCT_ITEM_TYPE);
        // 保存产品项图片
        iImagesService.handlePictures(productItem.getFileList(), userId, productItem.getId(),ProductConstants.PRODUCT_ITEM_TYPE);
		return R.data(this.save(productItem));
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateItem(ProductItemDTO productItem) {
        //校验数据
        checkValid(productItem);
        Long userId = SecureUtil.getUserId();
        // 保存 产品项信息
        productItem.setIsValuation(productItem.getIsValuation());
        productItem.setUpdateUser(userId);
        productItem.setUpdateTime(now());
        productItem.setIsDeleted(ProductConstants.IS_DELETED);
        if(Func.toInt(productItem.getItemState()) == ProductConstants.PRODUCT_ITEM_PUBLISH_STATE){
            productItem.setAddedDate(LocalDateTime.now());
        }
        // 更新产品项和产品扩展表
        iProdItemRelationService.handleProdItemRelation(productItem.getProdId(),productItem.getId(), userId);
        // 保存扩展信息数据
        iItemValueService.handleItemValues(productItem.getItemValues(), userId, productItem.getId());
        // 保存计价属性信息以及计价详情信息
        iValuationService.handleValuations(productItem.getValuations(), userId, productItem.getId(),ProductConstants.PRODUCT_ITEM_TYPE);
        // 保存分润比例数据-阶梯比例
        iProfitRateService.handleProfitRates(productItem.getProfitRates(), userId, productItem.getId(),ProductConstants.PRODUCT_ITEM_TYPE);
        // 保存产品项图片
        iImagesService.handlePictures(productItem.getFileList(), userId, productItem.getId(),ProductConstants.PRODUCT_ITEM_TYPE);
        return R.data(baseMapper.updateByProdId(productItem)>0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> removeByIds(String ids) {
        // 判断是否在已上架的组合产品中 即存在则不能删除
        if(Func.isNotEmpty(ids)){
            int i = iGroupDetailService.queryGroupByItem(ProductConstants.PRODUCT_ITEM_TYPE,Func.toLongArray(ids));
            Assert.isTrue(i<=0, MsgEnum.CHECK_ITEM_EXIST_GROUP_MSG.getMsg());
            //判断是否在已上架的组合产品中 即存在则不能下架/删除
            int t = iGroupDetailService.queryGroupByItem(ProductConstants.PRODUCT_ITEM_TYPE,Func.toLongArray(ids));
            Assert.isTrue(t<=0, MsgEnum.CHECK_ITEM_EXIST_GROUP_MSG.getMsg());
            iItemValueService.removeByProdId(Func.toLongArray(ids));
            iProfitRateService.removeByProdId(ProductConstants.PRODUCT_ITEM_TYPE, Func.toLongArray(ids));
            iImagesService.removeByProdId(ProductConstants.PRODUCT_ITEM_TYPE, Func.toLongArray(ids));
            iValuationService.removeByProdId(ProductConstants.PRODUCT_ITEM_TYPE, Func.toLongArray(ids));
            iValuationDetailService.removeByProdId(ProductConstants.PRODUCT_ITEM_TYPE, Func.toLongArray(ids));
            iGroupDetailService.removeByItemId(ProductConstants.PRODUCT_ITEM_TYPE, Func.toLongArray(ids));
            return R.data(baseMapper.deleteBatchIds(Func.toLongList(ids))>0);
        }
        return R.data(false);
    }

    @Override
	public IPage<ProductItem> selectProductItemPage(Query query, ProductItemDTO productItem) {
		QueryWrapper<ProductItem> queryWrapper = Condition.getQueryWrapper(new ProductItem());
		queryWrapper.select("*");
        getQueryWrapper(queryWrapper, productItem);
		queryWrapper.orderByDesc("create_time");
		return this.page(Condition.getPage(query), queryWrapper);
	}

    @Override
    public  Map<String,List<List<String>>> exportData(ProductItemDTO productItem) {
        // head-Name set
        String[] headName=new String[]{"产品项名称","产品项编码","所属部门","产品类型","发布应用","发布范围"};
        String[] headLastName=new String[]{"标准价","注销价","币种"};
        String[] headCode=new String[]{"propName","propCode","propDept","cateId","prodId","releaseScope"};
        String[] headLastCode=new String[]{"standardPrice","promotionPrice","currency"};
        LinkedHashSet<String> headNameSet = new LinkedHashSet(Arrays.asList(headName));
        // head-code set
        LinkedHashSet<String> headCodeSet = new LinkedHashSet(Arrays.asList(headCode));
        // 获取扩展属性
        Map<Long,Map<String,Object>> itemValueMap = getItemValueMap(headCodeSet, headNameSet);
        // 查询所有计价属性
        List<ValuationDetail> valuatioList = getAllValuation(ProductConstants.PRODUCT_ITEM_TYPE);
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
        excelMap.put("data",getExcelDateList(productItem, headCodeSet, itemValueMap));
        return excelMap;
    }

    @Override
    public List<ProductItem> getAddedList(ProductItemDTO productItem) {
        QueryWrapper<ProductItem> query = Condition.getQueryWrapper(new ProductItem());
        query.select("*");
        query.eq("item_state", ProductConstants.PRODUCT_ITEM_PUBLISH_STATE);
        if(Func.isNotEmpty(productItem)){
            if(Func.isNotEmpty(productItem.getPropName())){
                query.likeRight("prod_name", productItem.getPropName());
            }
            if(Func.isNotEmpty(productItem.getCateId())){
                query.eq("cate_id", productItem.getCateId());
            }
            if(Func.isNotEmpty(productItem.getExclude())){
                query.notIn("id", Func.toLongList(productItem.getExclude()));
            }
            if(Func.isNotEmpty(productItem.getInclude())){
                query.in("id", Func.toLongList(productItem.getInclude()));
            }
        }
        return baseMapper.selectList(query);
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
     * 获取产品类型map
     * @return
     */
    private Map<Long,String> getCateMap(){
        Map<Long, String> res = null;
        QueryWrapper<Category> query= Condition.getQueryWrapper(new Category());
        List<Category> cateList = categoryService.list(query);
        if(Func.isNotEmpty(cateList)){
            res = cateList.stream().collect(Collectors.toMap(Category::getId, Category::getCateName));
        }
        return res;
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
     * 获取产品map
     * @return
     */
    private Map<Long,String> getProductMap(){
        Map<Long, String> res = null;
        QueryWrapper query = Condition.getQueryWrapper(new Product());
        query.select("id,prod_name");
        List<Product> products = iProductService.list(query);
        if(Func.isNotEmpty(products)) {
            res = products.stream().collect(Collectors.toMap(Product::getId, Product::getProdName));
        }
        return res;
    }

    /**
     * 获取扩展属性
     * @param headCodeSet
     * @param headNameSet
     * @return
     */
    private Map<Long,Map<String,Object>> getItemValueMap (LinkedHashSet<String> headCodeSet, LinkedHashSet<String> headNameSet){
        Map<Long,Map<String,Object>> itemValueMap = new HashMap<>(16);
        Map<String,Object> itemValue = null;
        // 查询所有产品项的扩展属性
        List<ItemValue> itemValueList = iItemValueService.list(Condition.getQueryWrapper(new ItemValue()));
        for (ItemValue value:itemValueList) {
            headNameSet.add(value.getPropName());
            headCodeSet.add(value.getPropCode());
            if(itemValueMap.containsKey(value.getProdId())){
                itemValue= itemValueMap.get(value.getProdId());
            }else{
                itemValue = new HashMap<>(16);
            }
            itemValue.put(value.getPropCode(),value.getPropValue());
            itemValueMap.put(value.getProdId(), itemValue);
        }
        return itemValueMap;
    }

    /**
     * 获取导出数据
     * @param productItem
     * @param headCodeSet
     * @return
     */
    private List<List<String>> getExcelDateList(ProductItemDTO productItem, LinkedHashSet<String> headCodeSet, Map<Long,Map<String,Object>> itemValueMap){
        //币种
        Map<Integer,String> currencyType = getDicValueByCode(ProductConstants.CURRENCY_TYPE_CODE);
        //发布范围
        Map<Integer,String> productRange = getDicValueByCode(ProductConstants.PRODUCT_RANGE_CODE);
        Map<Long,String> cateMap = getCateMap();
        Map<Long,String> deptMap = getDeptMap();
        Map<Long,String> prodMap = getProductMap();

        List<List<String>> excelDataList = new ArrayList<>();
        List<Map<String,Object>> excelVos = baseMapper.selectItemAndValuation(productItem);
        JSONArray jsonArray = null;
        Map<String,Object> value = null;
        Map<String,Object> jsonMap = null;
        List<String> data = null;
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
            if(excel!=null && excel.containsKey("id")){
                value = itemValueMap.get(excel.get("id"));
            }
            for (String code :headCodeSet) {
                if(excel.containsKey(code)){
                    if(Func.isNotEmpty(cateMap) && "cateId".equals(code)){
                        data.add(cateMap.get(excel.get(code)));
                    }else if(Func.isNotEmpty(deptMap) && "propDept".equals(code)){
                        data.add(deptMap.get(excel.get(code)));
                    }else if(Func.isNotEmpty(prodMap) && "prodId".equals(code)){
                        data.add(prodMap.get(Func.toLong(excel.get(code))));
                    }else if(Func.isNotEmpty(productRange) && "releaseScope".equals(code)){
                        data.add(productRange.get(excel.get(code)));
                    }else if(Func.isNotEmpty(currencyType) && "currency".equals(code)){
                        data.add(currencyType.get(Func.toInt(excel.get(code))));
                    }else{
                        data.add(Func.toStr(excel.get(code)));
                    }
                }else if(Func.isNotEmpty(jsonMap) && jsonMap.containsKey(code)){
                    data.add(Func.toStr(jsonMap.get(code)));
                }else if(Func.isNotEmpty(value) && value.containsKey(code)){
                    data.add(Func.toStr(value.get(code)));
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
     * 数据校验
     * @param productItem
     */
    private void checkValid(ProductItemDTO productItem){
        //校验数据
        R<Boolean> r = this.checkPropCode(productItem.getId(), productItem.getPropCode());
        Assert.isTrue(r.getData(),r.getMsg());
        Assert.isTrue(Func.isNotEmpty(productItem.getPropCode()),MsgEnum.CHECK_ITEM_PROP_CODE_NULL_MSG.getMsg());
        Assert.isTrue(Func.isNotEmpty(productItem.getPropName()),MsgEnum.CHECK_ITEM_PROP_NAME_NULL_MSG.getMsg());
        if(productItem.getItemState() < ProductConstants.PRODUCT_ITEM_DRAFT_STATE){
            Assert.isTrue(Func.isNotEmpty(productItem.getPropDept()),MsgEnum.CHECK_ITEM_PROP_DEPT_NULL_MSG.getMsg());
            Assert.isTrue(Func.isNotEmpty(productItem.getCateId()),MsgEnum.CHECK_ITEM_CATE_ID_NULL_MSG.getMsg());
            Assert.isTrue(Func.isNotEmpty(productItem.getReleaseScope()),MsgEnum.CHECK_ITEM_RELEASE_SCOPE_NULL_MSG.getMsg());
            Assert.isTrue(Func.isNotEmpty(productItem.getProdId()),MsgEnum.CHECK_ITEM_PROP_ID_NULL_MSG.getMsg());
        }
    }

    /**
     * 获取查询条件
     * @param queryWrapper
     * @param productItem
     * @return
     */
    private QueryWrapper<ProductItem> getQueryWrapper(QueryWrapper<ProductItem> queryWrapper, ProductItemDTO productItem){
        // 产品项名称
        if(Func.isNotEmpty(productItem.getPropName())){
            queryWrapper.likeRight("prop_name",productItem.getPropName());
        }
        // 产品项编码
        if(Func.isNotEmpty(productItem.getPropCode())){
            queryWrapper.likeRight("prop_code",productItem.getPropCode());
        }
        // 发布范围
        if(Func.isNotEmpty(productItem.getReleaseScope())){
            queryWrapper.eq("release_scope",productItem.getReleaseScope());
        }
        // 产品类型
        if(Func.isNotEmpty(productItem.getCateId())){
            queryWrapper.eq("cate_id",productItem.getCateId());
        }
        // 发布应用
        if(Func.isNotEmpty(productItem.getProdId())){
            queryWrapper.eq("prod_id",productItem.getProdId());
        }
        // 产品状态
        if(Func.isNotEmpty(productItem.getItemState())){
            queryWrapper.eq("item_state",productItem.getItemState());
        }
        // 所属部门
        if(Func.isNotEmpty(productItem.getPropDept())){
            queryWrapper.eq("prop_dept",productItem.getPropDept());
        }
        // 创建时间
        if(Func.isNotEmpty(productItem.getCreateStartTime()) && Func.isNotEmpty(productItem.getCreateEndTime())){
            queryWrapper.between("create_time", productItem.getCreateStartTime(), productItem.getCreateEndTime());
        }
        // 修改时间
        if(Func.isNotEmpty(productItem.getUpdateStartTime()) && Func.isNotEmpty(productItem.getUpdateEndTime())){
            queryWrapper.between("update_time", productItem.getUpdateStartTime(), productItem.getUpdateEndTime());
        }
        return queryWrapper;
    }
}

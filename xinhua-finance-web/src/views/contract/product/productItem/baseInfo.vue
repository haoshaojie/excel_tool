
<template>
  <div>
    <el-form ref="draftForm" :rules="draftRules" :model="baseInfo" label-width="100px" class="demo-form-inline"  size="small">
      <el-row>
        <el-col span="12">
          <el-form-item label="产品项名称" prop="propName">
            <el-input placeholder="请输入产品项名称" :disabled="isView" v-model="baseInfo.propName" maxlength='50' show-word-limit size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col span="12">
          <el-form-item label="产品项编码" prop="propCode">
            <el-input placeholder="请输入产品项编码" :disabled="isView" v-model="baseInfo.propCode" maxlength='30' show-word-limit size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-form ref="baseForm" :rules="baseRules" :model="baseInfo" label-width="100px" class="demo-form-inline"  size="small">
      <el-row>
        <el-col span="12">
          <el-form-item label="所属部门"  prop="propDept">
          <el-select style="width:100%" :disabled="isView" v-model="baseInfo.propDept" filterable placeholder="请选择所属部门" size="small">
            <el-option v-for="(item,i) in propDepts" :key="i" :label="item.title" :value="item.key"></el-option>
          </el-select>
          </el-form-item>
        </el-col>
        <el-col span="12">
          <el-form-item label="产品类型" prop="cateId">
          <el-select ref="cateId" style="width:100%" :disabled="isView" size="small" v-model="baseInfo.cateId" @visible-change="showChange" @change="handleChange"  filterable  placeholder="请选择产品类型"> 
            <el-option v-for="(item,i) in cateIds" :key="i" :label="item.cateName" :value="item.id"></el-option>
          </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col span="12">
          <el-form-item label="发布范围" prop="releaseScope">
            <el-select style="width:100%" :disabled="isView" v-model="baseInfo.releaseScope" placeholder="请选择发布范围" size="small">
              <el-option v-for="(item,i) in productRanges" :key="i" :label="item.dictValue" :value="item.dictKey"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col span="12">
          <el-form-item label="发布应用" prop="prodIds">
            <el-select style="width:100%" :disabled="isView" v-model="baseInfo.prodIds" multiple placeholder="请选择发布应用">
              <el-option  v-for="item in prodData" :key="item.key" :label="item.label" :value="item.key"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>  
      <el-row>
        <el-col span="12">
          <el-form-item label="上架时间">
            <el-col :span="11" style="width: 100%;">
              <el-date-picker  
                size="small"
                :disabled="isView"
                v-model="baseInfo.addedDate"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间" style="width:100%">
              </el-date-picker>
            </el-col>
          </el-form-item>
        </el-col>
        <el-col span="12">
          <el-form-item label="下架时间">
            <el-col :span="11" style="width: 100%;">
              <el-date-picker
                size="small"
                :disabled="isView"
                v-model="baseInfo.expiredDate"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间" style="width:100%">
              </el-date-picker>
            </el-col>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-dialog 
    :visible.sync="confirmDialog"
    title="提示" 
    width="30%" 
    center>
      <span>是否确定更改产品类型！（更改产品类型后将根据所更改类型的扩展、计价属性重新生成扩展、计价属性信息，当前扩展、计价信息所填写内容将不会保存！）</span>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="sure" size="small">确 定</el-button>
        <el-button @click="cancel" size="small">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {getProdAddedList} from "@/api/contract/product/product";
import {checkPropCode} from "@/api/contract/product/productitem";
import {selectItems} from "@/api/contract/product/category";
import {getDeptTree} from "@/api/system/dept";
import {getDictByCode} from "@/api/system/dict";
import bus from '@/assets/js/event-bus';
export default {
  name:"baseInfo",
  props:{
    isView:false,
    baseInfo:{},
    draftForm:{}
  },
  data(){
      return {
        confirmDialog:false,
        preValue:"",//产品类型前一次选中的值
        propDepts:[],
        productRanges:[],
        cateIds:[],
        prodData:[],
        draftRules:{
          propName: [
            { required: true, message: '请输入产品项名称', trigger: 'blur' },
          ],
          propCode:[
            { required: true, validator:this.checkPropCode, trigger: 'blur'}
          ],
        },
        baseRules:{
           propName: [
            { required: true, message: '请输入产品项名称', trigger: 'blur' },
          ],
          propCode:[
            { required: true, validator:this.checkPropCode, trigger: 'blur'}
          ],
          propDept:[
            { required: true, message: '请选择所属部门', trigger: 'change'}
          ],
          cateId:[
            { required: true, message: '请选择产品类型', trigger: 'change'}
          ],
          releaseScope:[
            { required: true, message: '请选择发布范围', trigger: 'change'}
          ],
          prodIds:[
            { required: true, message: '请选择发布应用', trigger: 'change'}
          ],
        }
      }
  },
  mounted(){
   this.onLoad();
  },
  methods: {
    showData(data){
      console.log("base",data)
      this.baseInfo.id = data.id
      this.baseInfo.itemState = data.itemState;
      this.baseInfo.propName = data.propName;
      this.baseInfo.propCode = data.propCode;
      this.baseInfo.propDept = data.propDept?data.propDept:'';
      this.baseInfo.cateId = data.cateId?data.cateId:'';
      this.baseInfo.releaseScope = data.releaseScope?data.releaseScope:'';
      this.baseInfo.prodIds = data.prodId?data.prodId.split('|'):[];
      this.baseInfo.addedDate = data.addedDate;
      this.baseInfo.expiredDate = data.expiredDate;
      
      // bus.$emit('cateIdBus',this.baseInfo.cateId);
    },
    showChange(type){
      if(type){
        this.preValue=this.$refs['cateId'].value;
      }
    },
    handleChange(){
      if(this.preValue){
        this.confirmDialog=true;
      }else{
        bus.$emit('cateIdBus',this.baseInfo.cateId);
      }
      
    },
    sure(){
      this.confirmDialog=false;
      bus.$emit('cateIdBus',this.baseInfo.cateId);
    },
    cancel(){
      this.baseInfo.cateId=this.preValue;
      this.confirmDialog=false;
    },
    checkPropCode(rule, value, callback){
      if(value == ''){
        callback(new Error("请输入产品项编码"))
      }else{
        checkPropCode({propCode: value, id: this.baseInfo.id}).then(res => {
          const re = res.data
          if(!re.data){
             callback(new Error(re.msg))
          }else{
             callback()
          }
        })
      }
    },
    onLoad() {
      getDeptTree().then(res => {
        this.propDepts=res.data.data;
      });
      getDictByCode(this.CONSTANT.PRODUCT_RANGE).then(res => {
        this.productRanges=res.data.data
      });
      selectItems().then(res => {
        this.cateIds=res.data.data
      });
      getProdAddedList().then(res =>{
          let data = res.data.data;
          for(let prod of data){
            this.prodData.push({
              label: prod.prodName,
              key: prod.id
            });
          }
          
        })
    }
  }
}
</script>
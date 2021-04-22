
<template>
  <div>
    <el-form ref="draftForm" :rules="baseRules" :model="baseInfo" label-width="100px" class="demo-form-inline"  size="small">
      <el-row>
        <el-col span="12">
          <el-form-item label="产品名称" prop="prodName">
            <el-input placeholder="请输入产品名称" :disabled="isView" v-model="baseInfo.prodName" maxlength='50' show-word-limit size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col span="12">
          <el-form-item label="产品编码" prop="prodCode">
            <el-input placeholder="请输入产品编码" :disabled="isView" v-model="baseInfo.prodCode" maxlength='30' show-word-limit size="small"></el-input>
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
          <el-form-item label="应用分类" prop="prodType">
          <el-select ref="prodType" style="width:100%" :disabled="isView" size="small" v-model="baseInfo.prodType"  filterable  placeholder="请选择应用分类"> 
            <el-option v-for="(item,i) in prodTypes" :key="i" :label="item.dictValue" :value="item.dictKey"></el-option>
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
      </el-row>  
      <el-row>
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
  </div>
</template>
<script>
import {validProdCode} from "@/api/contract/product/product";
import {getDeptTree} from "@/api/system/dept";
import {getDictByCode} from "@/api/system/dict";
export default {
  name:"baseInfo",
  props:{
    isView:false,
    baseInfo:{},
    draftForm:{}
  },
  data(){
      return {
        propDepts:[],
        prodTypes:[],
        productRanges:[],
        baseRules:{
          prodName: [
            { required: true, message: '请输入产品名称', trigger: 'blur' },
          ],
          prodCode:[
            { required: true, validator:this.checkProdCode, trigger: 'blur'}
          ],
          propDept:[
            { required: true, message: '请选择所属部门', trigger: 'change'}
          ],
          releaseScope:[
            { required: true, message: '请选择发布范围', trigger: 'change'}
          ],
          prodType:[
            { required: true, message: '请选择应用分类', trigger: 'change'}
          ]
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
      this.baseInfo.prodState = data.prodState;
      this.baseInfo.prodName = data.prodName;
      this.baseInfo.prodCode = data.prodCode;
      this.baseInfo.propDept = data.propDept?data.propDept:'';
      this.baseInfo.prodType = data.prodType?parseInt(data.prodType):'';
      this.baseInfo.releaseScope = data.releaseScope?data.releaseScope:'';
      
      this.baseInfo.addedDate = data.addedDate;
      this.baseInfo.expiredDate = data.expiredDate;
      this.$forceUpdate()
    },
    checkProdCode(rule, value, callback){
      if(!value){
        callback(new Error("请输入产品编码"))
      }else{
        validProdCode({code: value, id: this.baseInfo.id}).then(res => {
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
      getDictByCode(this.CONSTANT.PRODUCT_TYPE).then(res => {
        this.prodTypes=res.data.data

      });
    }
  }
}
</script>
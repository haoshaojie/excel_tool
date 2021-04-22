<template>
  <div>
    <el-form ref="draftForm" :rules="baseRules" :model="baseInfo" label-width="100px" class="demo-form-inline"  size="small">
      <el-row>
        <el-col span="12">
          <el-form-item label="产品名称" prop="prodName">
            <el-input placeholder="请输入产品名称" :disabled="isView" v-model="baseInfo.prodName" maxlength='50' show-word-limit size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-form ref="baseForm" :rules="baseRules" :model="baseInfo" label-width="100px" class="demo-form-inline"  size="small">
      <el-row>
        <el-col span="12">
          <el-form-item label="所属部门"  prop="propDept">
          <el-select style="width:100%" :disabled="isView" v-model="baseInfo.propDept" filterable placeholder="请选择所属部门" size="small">
            <el-option v-for="item in propDepts" :key="item.key" :label="item.title" :value="item.key"></el-option>
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
import {validProdName} from "@/api/contract/product/group";
import {getDeptTree} from "@/api/system/dept";
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
            { required: true, validator:this.checkProdName, trigger: 'blur' },
          ],
          propDept:[
            { required: true, message: '请选择所属部门', trigger: 'change'}
          ]
        }
      }
  },
  mounted(){
   this.onLoad();
  },
  methods: {
    checkProdName(rule, value, callback){
      if(!value){
        callback(new Error("请输入产品名称"))
      }else{
        validProdName({name: value, id: this.baseInfo.id}).then(res => {
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
    }
  }
}
</script>
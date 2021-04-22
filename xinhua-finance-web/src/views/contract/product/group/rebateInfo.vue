<template>
  <div>
    <el-form ref="rebateForm" :rules="rebateRules" :model="rebateInfo" label-width="100px" class="demo-form-inline"  size="small">
      <el-row>
        <el-col span="12">
          <el-form-item label="折扣比例" prop="discount">
            <el-input placeholder="请输入折扣比例" :disabled="isView" v-model="rebateInfo.discount" maxlength='3' show-word-limit size="small"></el-input>
            <el-link type="info" :underline="false">注：</el-link>
            <el-link type="info" :underline="false">1.请输入1~100的数字；</el-link>
            <el-link type="info" :underline="false">2.折扣比例的合约绑定组合产品时进行计算组合产品的最终价格。</el-link>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>
<script>
export default {
  name:'rebate-info',
  props:{
    isView:false,
    rebateInfo:{},
  },
  data() {
    return {
      rebateRules:{
        discount: [
          { required: true, validator:this.checkDiscount, trigger: 'blur' },
        ]
      }
    }
  },
  methods:{
    checkDiscount(rule, value, callback){
      if(value == ''){
        callback(new Error("请输入折扣比例"))
      }else{
        if(value>=1 && value <=100){
          callback()
        }else{
          callback(new Error("折扣比例应为1%至100%之间"))
        }
      }
    }
  }
}
</script>
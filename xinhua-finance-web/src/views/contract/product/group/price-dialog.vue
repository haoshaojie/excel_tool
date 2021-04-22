<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogOpen"
    :show-close=false
    width="80%"
    >
    <template>
      <el-form :inline="true" ref="dialogForms" :model="dialogForm" size='mini' class="demo-form-inline">
        <el-form-item label="产品名称">
          <el-input v-model="dialogForm.prodName" placeholder="请输入产品名称"></el-input>
        </el-form-item>
        <el-form-item label="应用分类">
          <el-select ref="prodType" style="width:100%" :disabled="isView" size="small" v-model="dialogForm.prodType"  filterable  placeholder="请选择应用分类"> 
            <el-option v-for="(item,i) in prodTypes" :key="i" :label="item.dictValue" :value="item.dictKey"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryList">查询</el-button>
        </el-form-item>
      </el-form>
      <transfer-simple :leftList="initList" :checkbox="true"
        :columns="columns" @selectionChange="selectionChange" ></transfer-simple>
    </template>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancelPriceDialog">取 消</el-button>
      <el-button type="primary" @click="savePrice">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import transferSimple from "@/components/transfer/index-simple";
import {getValuations} from "@/api/contract/product/productitem";

export default {
  components:{transferSimple},
  name:"priceDialog",
  props:{
    dialogTitle:"",
    dialogOpen:false,
    dialogForm:{
      prodName:'',
      prodType:'',
      prodId:'',
      type:''
    }
  },
  data(){
      return {
        selections:[],
        columns:[{
          prop: "standardPrice",
          label:"标准价"
        },{
          prop: "promotionPrice",
          label:"促销价"
        },{
          prop: "currencyName",
          label:"币种"
        }],
        initList:[],
        prodList:[]
      }
  },
  watch:{
    'dialogOpen'(){
      if(this.dialogOpen === true){
        this.prodList=[];
        this.columns=[{
          prop: "standardPrice",
          label:"标准价"
        },{
          prop: "promotionPrice",
          label:"促销价"
        },{
          prop: "currencyName",
          label:"币种"
        }];
        this.queryList();
      }
    }
  },
  methods: {
    queryList(){
      getValuations(this.dialogForm).then(res => {
        const data = res.data.data;
        if(data){
          let flag = true
          for(let obj of data){
            for(let val of JSON.parse(obj.valuationValues)){
              if(flag){
                flag = false
                this.columns.unshift({prop:val.propCode, label:val.propName})
              }
              obj[val.propCode]=val.propValue
            }
          }
        }
        this.initList = data;
      });
    },
    cancelPriceDialog(){
      this.$emit('cancel');
    },
    selectionChange(selection){
      this.selections = selection
    },
    savePrice(){
      if(this.selections && this.selections.length == 1){
         this.$emit('result', this.selections[0]);
      }else{
        // 请选择数据 且只能选择一条数据
        this.$message({
          type: "warning",
          message: "请选择一条数据!"
        });
        return 
      }
    }
  }
}
</script>
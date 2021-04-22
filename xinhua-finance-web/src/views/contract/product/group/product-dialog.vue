<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="prodDialog"
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
      <transfer :leftList="initList" 
        :rightList="prodList"
        :columns="columns" @transferChange="transferChange" ></transfer>
    </template>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancelPriceDialog">取 消</el-button>
      <el-button type="primary" @click="savePrice">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import transfer from "@/components/transfer/index";
import {getDictByCode} from "@/api/system/dict";
import {getProdAddedList} from "@/api/contract/product/product";
export default {
  components:{transfer},
  name:"productDialog",
  props:{
    dialogTitle:"",
    prodDialog:false
  },
  data(){
      return {
        dialogForm:{
          prodName:'',
          prodType:''
        },
        columns:[{
          prop: "prodName",
          label:"产品名称",
          width:120
        },{
          prop: "prodCode",
          label:"产品编码",
          width: 90
        },{
          prop: "prodDeptName",
          label:"所属部门"
        },{
          prop: "prodTypeName",
          label:"应用分类"
        },{
          prop: "releaseScopeName",
          label:"发布范围"
        },{
          prop: "itemSourseName",
          label: "产品来源",
          width: 90
        }],
        excludeIds:[],
        prodTypes:[],
        initList:[],
        prodList:[]
      }
  },
  watch:{
    'prodDialog'(){
      if(this.prodDialog === true){
        this.prodList=[];
        this.queryList();
      }
    }
  },
  mounted(){
   this.onLoad();
  },
  methods: {
    onLoad() {
      getDictByCode(this.CONSTANT.PRODUCT_TYPE).then(res => {
        this.prodTypes=res.data.data
      });
    },
    queryList(){
      this.excludeIds=[];
      for(let prod of this.prodList){
        this.excludeIds.push(prod.id)
      }
      getProdAddedList(Object.assign(this.dialogForm, {exclude:this.excludeIds.join(',')})).then(res => {
        const data = res.data.data;
        this.initList = data;
      });
    },
    cancelPriceDialog(){
      this.$emit('cancel');
    },
    transferChange(rigth,left){
      this.initList=left;
      this.prodList=rigth;
    },
    savePrice(){
      this.$emit('result',this.prodList);
    }
  }
}
</script>
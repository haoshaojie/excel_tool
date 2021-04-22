<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="prodDialog"
    :show-close=false
    width="80%"
    >
    <template>
      <el-form :inline="true" ref="dialogForms" :model="dialogForm" size='mini' class="demo-form-inline">
        <el-form-item label="产品项名称">
          <el-input v-model="dialogForm.propName" placeholder="请输入产品名称"></el-input>
        </el-form-item>
        <el-form-item label="产品类型">
          <el-select ref="cateId" style="width:100%" :disabled="isView" size="small" v-model="dialogForm.cateId"  filterable  placeholder="请选择应用分类"> 
            <el-option v-for="(item,i) in cateIds" :key="i" :label="item.cateName" :value="item.id"></el-option>
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
import {selectItems} from "@/api/contract/product/category";
import {getItemAddedList} from "@/api/contract/product/productitem";
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
          propName:'',
          cateId:''
        },
        columns:[{
          prop: "propName",
          label:"产品项名称",
          width:120
        },{
          prop: "propCode",
          label:"产品项编码",
          width: 90
        },{
          prop: "propDeptName",
          label:"所属部门"
        },{
          prop: "cateIdName",
          label:"产品类型"
        },{
          prop: "prodIdName",
          label:"发布应用"
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
      selectItems().then(res => {
        this.cateIds=res.data.data
      });
    },
    queryList(){
      this.excludeIds=[];
      for(let prod of this.prodList){
        this.excludeIds.push(prod.id)
      }
      getItemAddedList(Object.assign(this.dialogForm, {exclude:this.excludeIds.join(',')})).then(res => {
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
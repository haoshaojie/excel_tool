<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="proDialog"
    :show-close=false
    width="80%"
    >
    <template>
      <el-form :inline="true" ref="dialogForms" :model="dialogForm" size='mini' class="demo-form-inline">
        <el-form-item label="属性名称">
          <el-input v-model="dialogForm.propName" placeholder="请输入属性名称"></el-input>
        </el-form-item>
        <el-form-item label="属性编码">
          <el-input v-model="dialogForm.propCode" placeholder="请输入属性编码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryList">查询</el-button>
        </el-form-item>
      </el-form>
      <transfer :leftList="initList" 
        :rightList="proList"
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
import { getListByCate} from "@/api/contract/product/property";
export default {
  components:{transfer},
  name:"priceProDialog",
  props:{
    dialogTitle:"",
    proDialog:false,
    dialogForm:{}
  },
  data(){
      return {
        columns:[{
          prop: "propName",
          label:"属性名称"
        },{
          prop: "propCode",
          label:"属性编码"
        },{
          prop: "showTypeName",
          label:"表现形式"
        }],
        initList:[],
        proList:[]
      }
  },
  watch:{
    'proDialog'(){
      if(this.proDialog === true){
        this.proList=[];
        this.queryList();
      }
    }
  },
  methods: {
    queryList(){
      getListByCate(this.dialogForm).then(res => {
        const data = res.data.data;
        this.initList = data;
      });
    },
    cancelPriceDialog(){
      this.$emit('cancelPrice');
    },
    transferChange(rigth,left){
      this.initList=left;
      this.proList=rigth;
    },
    savePrice(){
      this.$emit('resultPrice',this.proList);
    }
  }
}
</script>
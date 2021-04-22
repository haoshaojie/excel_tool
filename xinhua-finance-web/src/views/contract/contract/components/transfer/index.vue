<template>
<basic-container>
    <el-row>
      <el-col>
          <transferTable :dataList="leftList" :columns="letfColumns" :rowBtns="rowBtns.left" @rowClick="rowClick"></transferTable>
      </el-col>
    </el-row>
    <el-row>
      <el-col align="middle">
          <el-button icon="el-icon-arrow-down" size='mini' @click="toRightAll"></el-button>
          <el-button icon="el-icon-arrow-up" size='mini'  @click="toLeftAll"></el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <!-- 已选产品： -->
        <div class="el-select-product"><slot name="selectLabel"></slot>{{rightList.length}}</div> 
        <transferTable :dataList="rightList" :columns="rightColumns" :rowBtns="rowBtns.right" @rowClick="rowClick"></transferTable>
      </el-col>
    </el-row>
  </basic-container>
</template>
<script>
import transferTable from "./transfer-table";
export default {
  components:{transferTable},
  props:{
    leftList:[],
    rightList:[],
    letfColumns:[],
    rightColumns:[]
  },
  data(){
    return {
      rowBtns:{
        left:[{
          label:"选择",
          icon:"el-icon-circle-check",
          type:"left",
          
        }],
        right:[{
          label:"取消选择",
          icon:"el-icon-circle-close",
          type:"right"
        }]
      }
    }
  },
  methods:{
    toLeftAll(){
      if(this.rightList.length>0){
        this.leftList.unshift(...this.rightList);
        this.rightList.splice(0,this.rightList.length);
        this.$emit("transferChange",this.rightList,this.leftList);
      }
    },
    toRightAll(){
      if(this.leftList.length>0){
        this.rightList.unshift(...this.leftList);
        this.leftList.splice(0,this.leftList.length);
        this.$emit("transferChange",this.rightList,this.leftList);
      }
    },
    rowClick(row,type,index){
      if(type === 'right'){
        this.leftList.unshift(row)
        this.rightList.splice(index, 1)
      }else if(type === 'left'){
        this.rightList.unshift(row);
        this.leftList.splice(index, 1)
      }
      this.$emit("transferChange",this.rightList,this.leftList);
    }
  }
}
</script>
<style lang="scss">
.el-select-product{
  font-size: 14px;
  font-weight: 500;
  padding: 10px 0;
  color: #078ecf;
}
</style>
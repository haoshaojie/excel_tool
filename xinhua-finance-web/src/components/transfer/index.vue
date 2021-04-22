<template>
  <el-row>
    <el-col :span="10">
      <slot name="leftSearch"></slot>
      <transferTable :dataList="leftList" :columns="columns" :rowBtns="rowBtns.left" @rowClick="rowClick"></transferTable>
    </el-col>
    <el-col :span="4" align="middle" class="el-arrow-button">
        <div><el-button icon="el-icon-d-arrow-left" size='mini'  @click="toLeftAll" ></el-button></div>
        <div><el-button icon="el-icon-d-arrow-right" size='mini' @click="toRightAll" ></el-button></div>
    </el-col>
    <el-col :span="10">
      <slot name="rightSearch"></slot>
      <transferTable :dataList="rightList" :columns="rightColumns.length>0?rightColumns:columns" :rowBtns="rowBtns.right" @rowClick="rowClick"></transferTable>
    </el-col>
  </el-row>
</template>
<script>
import transferTable from "./transfer-table";
export default {
  components:{transferTable},
  props:{
    leftList:[],
    rightList:[],
    columns:[],
    rightColumns:{
      type:Array,
      default:()=>([])
    }
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
.el-arrow-button{
  margin-top:20px;
  div{
    margin-bottom: 10px;
  }
}
</style>
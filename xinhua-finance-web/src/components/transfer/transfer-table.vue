<template>
  <el-table
    :data="dataList"
    border
    size='small'
    @selection-change="selectionChange"
    style="width: 100%">
    <el-table-column v-if="checkbox"  type="selection"  width="55"></el-table-column>
    <el-table-column v-for="(item, i) in columns" :key='i' :label="item.label" :prop="item.prop"
                     size='small '
                     :formatter="item.formatter" :width="item.width?item.width:''">
    </el-table-column>
    <el-table-column
        v-if="rowBtns"
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button v-for="(item, i) in rowBtns" :key='i'
            type="text"
            :icon="item.icon"
            @click="transferClick(scope.row, item.type,scope.$index)"
            size="mini">{{item.label}}</el-button>
        </template>
    </el-table-column>
  </el-table>
</template>
<script>
export default {
  props:{
    checkbox:false,
    dataList:[],
    columns:[],
    rowBtns:[]
  },
  methods:{
    transferClick(row,type,index){
      this.$emit("rowClick", row, type,index);
    },
    selectionChange(selection){
      this.$emit("selectionChange", selection);
    }
  }
}
</script>

<template>
<div>
  <el-table
    :data="dataList"
    border
    size='small '
    style="width: 100%">
    <div v-for="(item, i) in columns" :key='i'>
       <el-table-column 
        v-if="!item.slot"
        :label="item.label" 
        :prop="item.prop"
        size="small"
        :formatter="item.formatter">
      </el-table-column>
      <el-table-column 
        v-if="item.slot"
        :label="item.label" 
        :prop="item.prop"
        size="small"
        :formatter="item.formatter">
        <template  slot-scope="scope">
          <el-button type="text" size="mini" @click="getDetail(scope.row,item)">{{item.prop=="productName"?scope.row.productName:scope.row.productPrice}}</el-button>
        </template>
    </el-table-column>
    </div>
    <el-table-column
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
  <!-- 计价/产品信息 -->
  <el-dialog
  :title="title"
  :visible.sync="showDialog"
  append-to-body
  :modal="false"
  width="30%"
  >
  <avue-crud
  :option="option"
  :data="data"
  ></avue-crud>
  </el-dialog>
</div>
</template>
<script>
const PRODUCT_INFO=[
  {
    label:"交易所",
    prop:"exchange"
  },
  {
    label:"行情类型",
    prop:"marketType"
  },
  {
    label:"行情实效",
    prop:"marketEfficiency"
  },
  {
    label:"数据类型",
    prop:"dataType"
  },
  {
    label:"可售区域",
    prop:"availableArea"
  }
];
const VALUATION=[{
    label:"时间维度",
    prop:"timeDimension"
  },
  {
    label:"发布区域",
    prop:"releaseArea"
  },
  {
    label:"标准价",
    prop:"standardPrice"
  },
  {
    label:"促销价",
    prop:"promotionPrice"
  },
  {
    label:"币种",
    prop:"currency"
  }];
export default {
  props:{
    dataList:[],
    columns:[],
    rowBtns:[]
  },
  data(){
    return{
      title:"计价",
      showDialog:false,
      option:{
        calcHeight: 210,
        searchShow: false,
        searchMenuSpan: 6,
        tip: false,
        border: true,
        index: false,
        viewBtn:false,
        addBtn:false,
        editBtn:false,
        delBtn:true,
        header:false,
        selection: false,
        menu:false,
        column:PRODUCT_INFO
      },
      data:[]
    }
  },
  mounted(){
  },
  methods:{
    transferClick(row,type,index){
      this.$emit("rowClick", row, type,index);
    },
    getDetail(row,item){
      this.showDialog=true;
      if(item.prop=="productName"){
        this.title="产品信息";
        this.option.column=PRODUCT_INFO;
      }else{
        this.title="计价";
        this.option.column=VALUATION;
      }
    }
  }
}
</script>

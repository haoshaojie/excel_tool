<template>
  <el-form ref="itemForm" :model="itemInfo" label-width="100px" class="demo-form-inline"  size="mini">
    <el-form-item label="产品信息">
      <el-button type="primary" v-if="!isView" size="small" @click="openDialog('prod')" class="add-btn">添加产品信息</el-button>
        <el-table border size="small"
          show-summary
          :summary-method="getSummaries"
          :span-method="arraySpanMethod"
          :data="itemInfo.items">
          <el-table-column label="产品项名称" prop="propName" width="120"></el-table-column>
          <el-table-column label="产品项编码" prop="propCode" width="90"></el-table-column>
          <el-table-column label="所属部门" prop="propDeptName"></el-table-column>
          <el-table-column label="产品类型" prop="cateIdName"></el-table-column>
          <el-table-column label="发布应用" prop="prodIdName"></el-table-column>
          <el-table-column label="发布范围" prop="releaseScopeName" width="90"></el-table-column>
          <el-table-column label="产品来源" prop="itemSourseName" width="90"></el-table-column>
          <el-table-column label="标准价"  prop="price">
            <template slot="header" slot-scope="scope">
              <span><i class="table-tip">*</i>标准价</span> 
            </template>
            <template slot-scope="scope">
              <el-form-item :prop="'items.' + scope.$index +'.price'"
                :rules="[{ required: true, message: '请选择', trigger: 'blur' }]">
              <el-input type="number"
                disabled  size="mini"
              v-model="scope.row.price" 
              @click.native="openDialog('price', scope.row, scope.$index)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="促销价" prop="promotionPrice">
            <template slot="header" slot-scope="scope">
              <span><i class="table-tip">*</i>促销价</span> 
            </template>
            <template slot-scope="scope">
              <el-form-item :prop="'items.' + scope.$index +'.promotionPrice'"
                :rules="[ { required: true, message: '请输入', trigger: 'blur' }]">
                <el-input type="number" v-model="scope.row.promotionPrice"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="币种" prop="currencyName"></el-table-column>
          <el-table-column v-if="!isView" label="操作" fixed="right" width="80">
            <template slot-scope="scope">
              <el-button icon="el-icon-circle-plus-outline" type="text" size="small" @click="rmRow(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
    </el-form-item>
    <itemDialog dialogTitle="添加产品" :dialogForm="dialogForm" :prodDialog="dialogOpen.prod" @result="addRow" @cancel="cancelDialog('prod')"></itemDialog>
    <priceDialog dialogTitle="选择价格" :dialogForm="dialogPriceForm" :dialogOpen="dialogOpen.price" @result="addPrice" @cancel="cancelDialog('price')"></priceDialog>
  </el-form>
</template>
<script>
import itemDialog from "./item-dialog";
import priceDialog from "./price-dialog"
export default {
  name:'product-info',
  components:{itemDialog, priceDialog},
  props:{
    isView:false,
    itemInfo:{
    }
  },
  data() {
    return {
      dialogForm:{},
      dialogPriceForm:{
        prodName:'',
        prodType:'',
        prodId:'',
        type:''
      },
      dialogOpen:{
        prod:false,
        price:false
      }
    }
  },
  mounted(){
    this.onLoad()
  },
  methods:{
    onLoad(){
    },
    arraySpanMethod(){
      var that = this;
      setTimeout(function () {
        if (that.$el.querySelector(".el-table__footer-wrapper")) {
          var current = that.$el.querySelector(".el-table__footer-wrapper")
            .querySelector(".el-table__footer");
          var cells = current.rows[0].cells;
          cells[0].colSpan = "7";
          cells[1].colSpan = "4";
        }
      }, 50);
    },
    getSummaries(param){
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '总计';
          return;
        }
        if(column.property == 'price'){
          const values = data.map(item => Number(item['price']));
          if (!values.every(value => isNaN(value))) {
            sums[1] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
          } else {
            sums[index] = '';
          }
        }else{
          sums[index] = '';
          return;
        }
        
      });
      return sums;
    },
    openDialog(type, row, index){
      if(type == 'price'){
        this.dialogPriceForm.prodId = row.id;
        this.dialogPriceForm.type = 2;
        this.dialogPriceForm.index = index;
      }
      this.dialogOpen[type]=true
    },
    cancelDialog(type){
      this.dialogOpen[type]=false
    },
    rmRow(index){
      this.itemInfo.items.splice(index, 1);
    },
    addRow(data){
      this.itemInfo.items.push(Object.assign(...data,{type:2}));
      this.cancelDialog('prod');
    },
    addPrice(data){
      let result=[];
      this.itemInfo.items.forEach((item,index)=>{
        if(index == this.dialogPriceForm.index){
          item.price= data.standardPrice;
          item.currency= data.currency;
          item.currencyName= data.currencyName;
        }
        result.push(item);
      })
      this.itemInfo.items = result;
      this.cancelDialog('price');
    }
  }
}
</script>
<style lang="scss" scoped>
.add-btn{
  margin-bottom: 1rem;
}
.table-tip{
  color: #F56C6C;
  font-weight: normal;
  vertical-align: middle;
  margin-right: 2px;
  font-size: 14px;
}
.normal-input{
  margin-bottom: 18px;
}
</style>
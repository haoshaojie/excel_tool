<template>
  <el-form ref="priceForm" :model="priceInfo" label-width="100px" class="demo-form-inline"  size="mini">
    <el-form-item label="是否计价">
      <el-radio-group :disabled="isView" v-model="priceInfo.isValuation" @change="valuationChange">
        <el-radio :label="1">是</el-radio>
        <el-radio :label="2">否</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="计价方式" v-show="priceInfo.isValuation==1">
      <el-button type="primary" v-if="!isView" size="small" @click="openDialog" class="add-btn">添加计价属性</el-button>
      <template>
         <el-form 
          :model="priceInfo" 
          ref="inServForm"
          size="small">
          <el-table border size="small"
            :data="priceInfo.valuation">
            <el-table-column v-for="(item, i) in itemColumns" :key='item.id' prop="propValue" :label="item.propName">
              <template slot="header" slot-scope="scope">
                <span><i v-if="item.isRequired==1" class="table-tip">*</i>{{item.propName}}</span><i v-if="!isView" style="float: right; color: #F56C6C;" class="el-icon-remove-outline" @click="rmPriceColumn(item)"></i>   
              </template>
              <template slot-scope="scope">
                  <el-form-item 
                  :rules="[{ required:item.isRequired==1?true:false,message:item.showType == 1?`请输入${item.propName}`:`请选择${item.propName}`,trigger:item.showType == 1?'blur':'change'}]"
                  :prop="'valuation.' + scope.$index +'.valuationValue.'+i+'.propValue'">
                    <el-input :disabled="isView" v-model="scope.row.valuationValue[i].propValue" placeholder="请输入" size="mini" v-if="item.showType == 1" ></el-input>
                    <el-select :disabled="isView" style="width:100%" size="mini" v-model="scope.row.valuationValue[i].propValue" placeholder="请选择"  v-if="item.showType == 2">
                      <el-option v-for="(im,t) in item.options" :key="t" :label="im.propValue" :value="im.propValue"></el-option>
                    </el-select>
                  </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="标准价">
              <template slot="header" slot-scope="scope">
                <span><i class="table-tip">*</i>标准价</span>   
              </template>
              <template slot-scope="scope">
                <el-form-item 
                :prop="'valuation.' + scope.$index + '.standardPrice'"
                :rules="[{required:true,message:'请输入标准价',trigger:'blur'}]">
                  <el-input :disabled="isView" type="number" v-model="scope.row.standardPrice" ></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="促销价">
              <template slot-scope="scope">
                <el-input :disabled="isView" type="number" v-model="scope.row.promotionPrice"  class="normal-input"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="币种" >
              <template slot="header" slot-scope="scope">
                <span><i class="table-tip">*</i>币种</span>   
              </template>
              <template slot-scope="scope">
                <el-form-item 
                  :prop="'valuation.' + scope.$index + '.currency'"
                  :rules="[{required:true,message:'请选择标币种',trigger:'blur'}]">
                  <el-select :disabled="isView" v-model="scope.row.currency" filterable placeholder="请选择">
                    <el-option v-for="(item,i) in currencyTypes" :key="i" :label="item.dictValue" :value="item.dictKey"></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column v-if="!isView" label="操作" fixed="right" width="80">
              <template slot-scope="scope">
                  <el-button v-if="scope.$index == 0" icon="el-icon-circle-plus-outline" type="text" size="small" @click="priceInfoAddRow(scope.$index, scope.row)">新增</el-button>
                  <el-button v-if="scope.$index > 0" icon="el-icon-delete" type="text" size="small" style='color: #E02020;' @click="priceInfoRmRow(scope.$index, scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          </el-form>
      </template>
    </el-form-item>
    <price-pro-dialog dialogTitle="添加计价属性" :dialogForm="dialogForm" :proDialog="openPriceDialog" @resultPrice="priceAddColumn" @cancelPrice="cancelPriceDialog"></price-pro-dialog>
  </el-form>
</template>
<script>
import priceProDialog from "./priceProDialog";
import {getDictByCode} from "@/api/system/dict";
import {getValuations} from "@/api/contract/product/productitem";
import { getListByCate} from "@/api/contract/product/property";
import bus from '@/assets/js/event-bus';
export default {
  components:{priceProDialog},
  name:"priceInfo",
  props:{
    isView:false,
    priceInfo:{
    }
  },
  data(){
      return {
        dialogForm:{
          productType:1,// 1产品类型；2产品项；3组成产品；
          propType:1,
          exclude:'',
          include:'',
        },
        cateId:'',
        itemColumns:[],
        excludeIds:[],
        includeIds:[],
        currencyTypes:[],
        openPriceDialog:false,
      }
  },
  mounted(){
    this.onLoad()
    bus.$on('cateIdBus',data =>{
      this.cateId = data
      this.priceInfo.isValuation=2
      this.valuationChange(this.priceInfo.isValuation)
    })
  },
  methods:{
    showData(data){
      let _this = this;
      this.priceInfo.isValuation = data.isValuation;
      this.$forceUpdate()
      getValuations({prodId:data.id, type:this.productType}).then(res =>{
        data = res.data.data
        console.log("price",data)
        data.forEach((item ,i)=>{
          if(i==0){
            JSON.parse(item.valuationValues).forEach(column=>{
              _this.includeIds.push(column.propId)
              _this.excludeIds.push(column.propId)
            })
          }
          this.priceInfo.valuation.push({
            valuationValue:JSON.parse(item.valuationValues),
            standardPrice:item.standardPrice,
            promotionPrice:item.promotionPrice,
            currency:item.currency
          });
        })
        this.dialogForm.include = _this.includeIds.join(',');
        getListByCate(this.dialogForm).then(res =>{
          let column={}
          res.data.data.forEach(item=>{
            column[item.id]=item
          })
          _this.includeIds.forEach(id =>{
            if(column[id]){
              this.itemColumns.push(column[id])
            }
          })
          this.dialogForm.include='';
          console.log(1255,this.itemColumns)
        })
        this.dialogForm.exclude = _this.excludeIds.join(',');
      })
    },
    valuationChange(value){
      if(value==1){ // 是否计价 是
        this.priceInfo.valuation=[{
          valuationValue:this.handleColumns(),
          standardPrice:'',
          promotionPrice:'',
          currency:''
        }];
        if(this.cateId !=''){// // 产品项 需要重新将选择属性的计价属性带出来
          getListByCate({propType:this.dialogForm.propType,cateId:this.cateId}).then(res => {
            const data = res.data.data;
            this.priceAddColumn(data)
          });
        }
      }else if(value==2){ // 否
        this.priceInfo.valuation=[]
        this.itemColumns=[]
        this.excludeIds=[]
        this.dialogForm.exclude=''
      }
    },
    onLoad(){
      getDictByCode(this.CONSTANT.CURRENCY_TYPE).then(res => {
        this.currencyTypes=res.data.data
      });
    },
    openDialog(){
      if(this.productType == 2 && this.dialogForm.cateId ==''){
        this.$message.warning('请先选择产品类型');
      }else{
        this.openPriceDialog = true
      }
    },
    cancelPriceDialog(){
      this.openPriceDialog = false
    },
    rmPriceColumn(item){
      this.itemColumns.splice(this.itemColumns.findIndex(_ => {
          return _.id === item.id
       }), 1);
      this.excludeIds.splice(this.excludeIds.findIndex(_ => {
          return _.id === item.id
       }), 1);
      this.dialogForm.exclude = this.excludeIds.join(',');
      // 删除 已经添加的行数
      this.priceInfo.valuation.forEach(val =>{
        val.valuationValue.splice(this.itemColumns.findIndex(_ => {
          return _.id === item.id
        }), 1);
      })
    },
    // 添加列
    priceAddColumn(data) {
      this.itemColumns.push(...data)
      data.forEach(item =>{
        this.excludeIds.push(item.id);
        this.priceInfo.valuation.forEach(val =>{
          val.valuationValue.push({
            propId:item.id,
            propCode:item.propCode,
            propName:item.propName,
            propValue:''
          })
        })
      })
      this.dialogForm.exclude = this.excludeIds.join(',');
      this.openPriceDialog = false;
      console.log('add-column',this.priceInfo.valuation)
    },
    // 处理列数据
    handleColumns(){
      let columns=[]
      this.itemColumns.forEach(item=>{
        columns.push({
          propId:item.id,
          propCode:item.propCode,
          propName:item.propName,
          propValue:''
        })
      })
      return columns
    },
    priceInfoAddRow(){
      
      this.priceInfo.valuation.push({
        valuationValue:this.handleColumns(),
        standardPrice:'',
        promotionPrice:'',
        currency:''
      });
      console.log('add-row',this.priceInfo.valuation)
    },
    priceInfoRmRow(index){
      this.priceInfo.valuation.splice(index, 1);
      this.excludeIds.splice(index, 1);
      this.dialogForm.exclude = this.excludeIds.join(',');
    },
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
<template>
  <el-form  ref="profitForm" :rules="profitRules" :model="profitInfo" label-width="100px" class="demo-form-inline"  size="mini">
    <el-form-item label="产品来源">
      <el-col :span="12">
        <el-radio-group :disabled="isView" v-model="profitInfo.itemSourse" @change="itemSourseChange">
          <el-radio :label="1" >自有产品</el-radio>
          <el-radio :label="2" >合作商产品</el-radio>
        </el-radio-group>
      </el-col>
    </el-form-item>
    <el-form-item label="合作商" v-show="profitInfo.itemSourse==2" prop='partnerId'>
      <el-col :span="12">
        <el-select :disabled="isView" style="width:100%" v-model="profitInfo.partnerId" placeholder="请选择合作商">
          <el-option v-for="(item,i) in pars" :key="i" :label="item.parName" :value="item.id"></el-option>
        </el-select>
      </el-col>
    </el-form-item>
    <el-form-item label="分润比例" v-show="profitInfo.itemSourse==2"  prop="shareResult">
      <el-col :span="12">
        <el-radio-group :disabled="isView" v-model="profitInfo.shareType" @change="shareTypeChange">
          <el-radio :label="1" >固定比例</el-radio>
          <el-radio :label="2" >阶梯比例</el-radio>
        </el-radio-group>
        <template v-if="profitInfo.itemSourse==2 && profitInfo.shareType == 1">
          <el-input :disabled="isView" placeholder="请输入分润比例" type="number" v-model="profitInfo.shareResult">
            <template slot="append">%</template>
          </el-input>
          <el-link type="info" :underline="false">注：分润比例为合作商的分润比例</el-link>
        </template>
        <template v-if="profitInfo.itemSourse==2 && profitInfo.shareType == 2">
          <el-form :model="profitInfo" :disabled="isView" ref="ratesForm" size="small">
          <el-table border size="small"
            :data="profitInfo.profitRates"
            style="width: 100%">
            <el-table-column label="人数范围">
              <template slot-scope="scope">
                <el-form-item 
                :prop="'profitRates.' + scope.$index + '.beginUserRcnt'" 
                class="table-item">
                  <el-input  
                  disabled
                  v-model.number="scope.row.beginUserRcnt" 
                  size="small"
                  ></el-input>
                </el-form-item>
                至
                <el-form-item 
                :prop="'profitRates.' + scope.$index + '.endUserCnt'" 
                :rules="[{required:true,validator:( rule, value, callback)=>{checkUserRcnt(scope.row,rule, value, callback)} ,trigger:'blur'}]" 
                class="table-item">
                  <el-input @input="userRcntInput(scope.row.endUserCnt,scope.$index)" v-model.number="scope.row.endUserCnt" size="small" placeholder="请输入"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="比例（%）" width="120">
              <template slot-scope="scope">
                <el-form-item 
                :prop="'profitRates.' + scope.$index + '.profirRate'"
                :rules="[{required:true,message:'请输入比例',trigger:'blur'},
                {pattern:/^([1-9]\d{0,1}|100)$/, message: '请输入1-100的正整数', trigger: 'blur'}]">
                  <el-input type="number" :min="1" :max="100" v-model.number="scope.row.profirRate" size="small" placeholder="请输入"></el-input>
                </el-form-item>
              </template>
            </el-table-column>>
            <el-table-column v-if="!isView" label="操作" fixed="right" width="80"  class="table-item">
              <template slot-scope="scope">
                  <el-button v-if="scope.$index == 0" icon="el-icon-circle-plus-outline" type="text" size="small" @click="profitInfoAddItem(scope.$index, scope.row)">新增</el-button>
                  <el-button v-if="scope.$index > 0" icon="el-icon-delete" type="text" size="small" style='color: #E02020;' @click="profitInfoRmItem(scope.$index, scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          </el-form>
        </template>
      </el-col>
    </el-form-item>
  </el-form>
</template>
<script>
import {profitRateByProdId} from "@/api/contract/product/productitem";
import {selectPartnerItems} from '@/api/partner/partner';
export default {
  name:"profitInfo",
  props:{
    isView:false,
    profitInfo:{
      partnerId:'',
      profitRates:[],
      itemSourse: 1,
      shareType: '',
      shareResult:''
    }
  },
  data(){
      return {
        productType:1,// 1产品类型；2产品项；3组成产品；
        profitRules:{
          partnerId:[
            {required: true, validator:this.checkParId, trigger: 'change'}
          ],
          shareResult:[
            {required: true, validator:this.checkShareResult, trigger: 'blur'}
          ],
          releaseScope:[
            { required: true, message: '请输入发布范围', trigger: 'blur'}
          ],
          prodId:[
            { required: true, message: '请输入发布应用', trigger: 'blur'}
          ],
        }
      }
  },
  mounted(){
   this.onLoad();
  },
  methods:{
    showData(data){
      this.profitInfo.itemSourse = data.itemSourse
      if(data.itemSourse == 2){
        this.profitInfo.partnerId = data.partnerId
        this.profitInfo.shareType = data.shareType
        this.profitInfo.shareResult = data.shareResult
        if(data.shareType == 2){
          profitRateByProdId(data.id, this.productType).then(res =>{
            let data = res.data.data;
            console.log("profit",data)
            this.profitInfo.profitRates= data
          })
        }
        
      }
    },
    itemSourseChange(value){
        // 分润比例 1 自有产品 2 合作商产品
      if (value == 1) {
        this.profitInfo.partnerId='';
        this.profitInfo.shareType = '';
      }else if(value === 2){
        this.profitInfo.shareType = 1;
      }
    },
    shareTypeChange(value){
      // 分润比例 1 固定 2 阶梯
      if (value == 1) { 
        this.profitInfo.profitRates=[];
      }else if(value == 2){
        this.profitInfo.shareResult="";
        this.profitInfo.profitRates=[{beginUserRcnt:1,endUserCnt:'',profirRate:''}];
      }
    },
    checkParId(rule, value, callback){
      console.log(this.profitInfo.itemSourse, value)
      if(this.profitInfo.itemSourse==2){
        if(!this.profitInfo.partnerId){
          callback(new Error("请选择合作商"))
        }else{
          callback()
        }
      }else{
        callback()
      }
    },
   
    checkShareResult(rule, value, callback){
      if(this.profitInfo.itemSourse==2 && this.profitInfo.shareType == 1){
        if(value == ''){
          callback(new Error("请输入分润比例"))
        }else{
          if(value>=1 && value <=100){
            callback()
          }else{
            callback(new Error("分润比例应为1%至100%之间"))
          }
        }
      }else if(this.profitInfo.itemSourse==2 && this.profitInfo.shareType == 2){
        console.log("校验表格内容")
        callback()
      }else{
        callback()
      }
    },
    checkUserRcnt(row, rule, value, callback){
      console.log(row)
      if(!value){
        callback(new Error("请输入"))
      }else if(value<=row.beginUserRcnt){
          callback(new Error("请大于"+row.beginUserRcnt))
      }else{
        callback()
      }
      
    },
    profitInfoAddItem(){
      let beginUserRcnt='';
      if(this.profitInfo.profitRates[this.profitInfo.profitRates.length-1]){
        let row = this.profitInfo.profitRates[this.profitInfo.profitRates.length-1]
        if(row.endUserCnt){
          beginUserRcnt = parseInt(row.endUserCnt)+1
        }
      }
      this.profitInfo.profitRates.push({beginUserRcnt:beginUserRcnt, endUserCnt:'',profirRate:''})
    },
    userRcntInput(value, index){
      if(value){
        value=(value+"").replace(/[^\d]/g,'')
        this.profitInfo.profitRates[index].endUserCnt=value
        if(value && this.profitInfo.profitRates[index+1]){
          this.profitInfo.profitRates[index+1].beginUserRcnt = parseInt(value) +1
        }
      }
    },
    profitInfoRmItem(index){
      this.profitInfo.profitRates.splice(index, 1)
    },
    onLoad() {
      selectPartnerItems().then(res => {
        this.pars=res.data.data
      });
    }
  }
}
</script>
<style lang="scss" scoped>
.table-item{
  width: 40%;
  display: inline-block;
  margin-bottom: 13px;
}
</style>
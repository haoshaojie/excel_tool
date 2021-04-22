<template>
  <basic-container>
    <template>
      <el-row>
        <el-col :span="16">
          <el-page-header @back="goBack" :content="title" class="header">
          </el-page-header>
        </el-col>
        <el-col :span="8">
          <el-row v-if="!isView" style='float:right'>
            <el-button size="small" v-if="!btnState" type="primary" @click.native="save(4)">保存至草稿</el-button>
            <el-button size="small" v-if="!btnState" @click.native="save(3)">保存暂不上架</el-button>
            <el-button size="small" v-if="!btnState" type="primary" @click.native="save(1)">保存并上架</el-button>
            <el-button size="small" v-if="btnState"  type="primary" @click.native="save(5)">保存</el-button>
          </el-row>
        </el-col>
      </el-row>
    </template>
    <template>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="基础信息" name="baseInfo">
          <baseInfo ref="baseInfo" :baseInfo="form.baseInfo" :is-view="isView"></baseInfo>
        </el-tab-pane>
        <el-tab-pane label="图文信息" name="picInfo" >
          <pic-info ref="picInfo" :picInfo="form.picInfo" :is-view="isView"></pic-info>
        </el-tab-pane>
        <el-tab-pane label="计价设置" name="priceInfo">
          <priceInfo ref="priceInfo" :priceInfo="form.priceInfo" :is-view="isView"></priceInfo>
        </el-tab-pane>
        <el-tab-pane label="分润信息" name="profitInfo">
          <profitInfo ref="profitInfo" :profitInfo="form.profitInfo" :is-view="isView"></profitInfo>
        </el-tab-pane>
      </el-tabs>
    </template>
  </basic-container>
</template>
<script>
import {add,getDetail,update} from "@/api/contract/product/product";
import baseInfo from "./baseInfo"
import profitInfo from "../components/profitInfo";
import picInfo from "../components/picInfo";
import priceInfo from "../components/priceInfo";

export default {
  name:"product-add",
  components:{baseInfo,priceInfo,picInfo,profitInfo},
  props:{
    id:'',
    title:"新增",
    isView:false
  },
  data(){
    return {
      activeName: 'baseInfo',
      headers:{},
      loading:false,
      btnState:false,
      picFiles:[],
      form:{
        baseInfo:{
          prodCode: '',
          prodName: '',
          propDept: '',
          prodType: '',
          releaseScope: '',
          addedDate: '',
          expiredDate: '',
          prodState:''
        },
        priceInfo:{
          isValuation:2,
          valuation:[]
        },
        picInfo:{
          prodDesc:'',
          itemDesc:'',
          proImage:'',
          fileLists:[],
          dialogImageUrl: '',
          dialogVisible: false
        },
        profitInfo:{
          partnerId:'',
          profitRates:[],
          itemSourse: 1,
          shareType: '',
          shareResult:'' 
        }
      }
    }
  },
  mounted() {
    this.$refs['priceInfo'].productType=1;
    this.$refs['picInfo'].productType=1;
    this.$refs['profitInfo'].productType=1;
    if(this.id){
      if(this.isView){
        this.title="详情";
      }else{
        this.title="编辑";
      }
      this.detail();
    }else{
      this.title="新增";
    }
  },
  methods: {
    detail(){
      let _this = this
      this.loading = true;
      getDetail(_this.id).then(res =>{
        console.log(res.data.data)
        this.loading = false;
        if(res.data.data){
          if(res.data.data.prodState == 1){
            this.btnState =true;
          }
          res.data.data.itemDesc = res.data.data.prodDesc
          _this.$refs["baseInfo"].showData(res.data.data);
          _this.$refs["priceInfo"].showData(res.data.data);
          _this.$refs["picInfo"].showData(res.data.data);
          _this.$refs["profitInfo"].showData(res.data.data);
        }
      })
    },
    save(prodState) {
      let validForms = [];
      let _this = this;
      validForms.push({page:"baseInfo", formName:'draftForm'})
      if(prodState < 4 || prodState ==5){
        validForms.push(
            {page:"baseInfo", formName:'baseForm'},
            {page:"priceInfo", formName:'priceForm'},
            {page:"priceInfo", formName:'inServForm'},
            {page:"profitInfo", formName:'profitForm'})
        if(this.$refs['profitInfo'].$refs['ratesForm']){
          //保存暂不上架和保存并上架校验全部表单
          //阶梯比例表单没显示时不校验该表单
          validForms.push({page:"profitInfo",formName:"ratesForm"})
        }
      }
      Promise.all(
        validForms.map(({page,formName}) => {
            return this.$refs[page].$refs[formName].validate();
        })
      ).then(() => {
        if(_this.form.picInfo.fileLists){
          _this.form.picInfo.fileLists.forEach(file =>{
            if(file.id){
              this.picFiles.push({imageUrl:file.url, fileName:file.name, fileSize:file.fileSize});
            }else{
              let data = file.response.data;
              this.picFiles.push({imageUrl:data.link,fileName:data.originalName,fileSize:file.size});
            }
          })
          _this.form.picInfo.fileList = this.picFiles;
        }
        if(_this.form.priceInfo && _this.form.priceInfo.isValuation ==1){ // 存在计价属性
          let priceInfoStr="|";
          _this.form.priceInfo.valuations = [];
          for(let info of _this.form.priceInfo.valuation){
            // 判断是否都为空 为空则不存入数据库
            let b = true; // 为false 说明全部为空 从列表中删除 ;true 不为空 
            if(info.currency =='' && info.standardPrice =='' && info.promotionPrice ==''){// 三个常列为空
              b=false
              if(info.valuationValue.length != 0){ // // 有扩展列
                for(var i=0; i<info.valuationValue.length; i++){
                  if(info.valuationValue[i].propValue != ''){ // 扩展列有值
                    b=true
                    break;
                  }
                }
              }
            }
            if(b){
              // 判断当前
              let infoStr = JSON.stringify(info);
              console.log(priceInfoStr,priceInfoStr.includes('|'+infoStr+'|'))
              if(priceInfoStr != '|' && priceInfoStr.includes('|'+infoStr+'|')){
                this.$message({
                  type: "warning",
                  message: "计价属性存在完全一致的数据，请核对!"
                });
                return false;
              }else{
                priceInfoStr += infoStr+'|'
                _this.form.priceInfo.valuations.push({
                  valuationValues:JSON.stringify(info.valuationValue),
                  currency: info.currency,
                  promotionPrice: info.promotionPrice,
                  standardPrice: info.promotionPrice
                })
              }
            }
          }
          if(_this.form.priceInfo.valuations.length<=0){
            this.$message({
              type: "warning",
              message: "请最少输入一条计价信息!"
            });
            return false;
          }
        }
        _this.form.picInfo.prodDesc = _this.form.picInfo.itemDesc
        let params = {..._this.form.baseInfo, ..._this.form.priceInfo,
         ..._this.form.picInfo, ..._this.form.profitInfo}
        if(prodState!=5){
          params.prodState = prodState;
        }
        this.loading = true;
        console.log(123,params);
        if(this.id){
          update(params).then(res => {
            this.loading = false;
            this.$message.success(res.data.msg);
            this.goBack();
          });
        }else{
          add(params).then(res => {
            this.loading = false;
            this.$message.success(res.data.msg);
            this.goBack();
          });
        }
      }).catch(() => {
        //校验失败
        console.log("error");
      })
    },
    goBack() {
      this.$emit("back");
      // this.$router.go(-1);
    }
  }
}
</script>
<style >
  .header .el-page-header__content {
    font-size: 14px;
    font-weight: 500;
    color: #303133;
  }
</style>
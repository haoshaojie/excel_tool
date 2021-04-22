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
            <el-button size="small" v-if="!btnState"  @click.native="save(3)">保存暂不上架</el-button>
            <el-button size="small" v-if="!btnState" type="primary" @click.native="save(1)">保存并上架</el-button>
            <el-button size="small" v-if="btnState" type="primary" @click.native="save(5)">保存</el-button>
          </el-row>
        </el-col>
      </el-row>
    </template>
    <template>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="基础信息" name="baseInfo">
          <baseInfo ref="baseInfo" :baseInfo="form.baseInfo" :is-view="isView"></baseInfo>
        </el-tab-pane>
        <el-tab-pane label="产品" name="prodInfo" >
          <prodInfo ref="prodInfo" :prodInfo="form.prodInfo" :is-view="isView"></prodInfo>
        </el-tab-pane>
        <el-tab-pane label="产品项" name="priceInfo">
          <itemInfo ref="itemInfo" :itemInfo="form.itemInfo" :is-view="isView"></itemInfo>
        </el-tab-pane>
        <el-tab-pane label="折扣信息" name="profitInfo">
          <rebateInfo ref="rebateInfo" :rebateInfo="form.baseInfo" :is-view="isView"></rebateInfo>
        </el-tab-pane>
      </el-tabs>
    </template>
  </basic-container>
</template>
<script>
import {add,getDetail,update} from "@/api/contract/product/group";
import {getProdAddedList} from "@/api/contract/product/product";
import {getItemAddedList} from "@/api/contract/product/productitem";
import baseInfo from "./baseInfo"
import rebateInfo from "./rebateInfo"
import prodInfo from "./prodInfo"
import itemInfo from "./itemInfo"

export default {
  name:"product-add",
  components:{baseInfo, prodInfo, itemInfo, rebateInfo},
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
      prodIds:[],
      itemIds:[],
      form:{
        baseInfo:{
          id:'',
          prodName: '',
          propDept: '',
          addedDate: '',
          expiredDate: '',
          discount:'',
          prodState:''
        },
        prodInfo:{
          products:[]
        },
        itemInfo:{
          items:[]
        }
      }
    }
  },
  mounted() {
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
        let data = res.data.data;
        this.form.baseInfo = data;
        this.form.baseInfo.propDept = data.propDept?data.propDept:'';
        if(data.prodState == 1){
          this.btnState =true;
        }
        let detailList = data.detailList;
        let products=[];
        let items=[];
        for(let detail of detailList){
          if(detail.type == 1){// 1产品2产品项
            products.push(detail)
            this.prodIds.push(detail.itemId)
          }else{
            items.push(detail)
            this.itemIds.push(detail.itemId)
          }
        }
        // 获取产品列表
        getProdAddedList({include:this.prodIds.join(',')}).then(res=>{
          let data = res.data.data
          let prods= {};
          for(let prod of data){
            prods[prod.id]=prod
          }
          for(let p of products){
            p['prodName'] = prods[p.itemId].prodName
            p['prodCode'] = prods[p.itemId].prodCode
            p['prodDeptName'] = prods[p.itemId].prodDeptName
            p['prodTypeName'] = prods[p.itemId].prodTypeName
            p['releaseScopeName'] = prods[p.itemId].releaseScopeName
            p['itemSourseName'] = prods[p.itemId].itemSourseName
          }
          this.form.prodInfo.products = products
        })
        // 获取产品项列表
        getItemAddedList({include:this.itemIds.join(',')}).then(res=>{
          let data = res.data.data
          let its= {};
          for(let it of data){
            its[it.id]=it
          }
          console.log(items)
          for(let item of items){
            item['propName'] = its[item.itemId].propName
            item['propCode'] = its[item.itemId].propCode
            item['propDeptName'] = its[item.itemId].propDeptName
            item['cateIdName'] = its[item.itemId].cateIdName
            item['prodIdName'] = its[item.itemId].prodIdName
            item['releaseScopeName'] = its[item.itemId].releaseScopeName
            item['itemSourseName'] = its[item.itemId].itemSourseName
          }
          console.log(items)
          this.form.itemInfo.items = items
        })
      })
    },
    save(prodState) {
      this.loading = true;
      console.log(prodState)
      let validForms = [];
      // let _this = this;
      validForms.push({page:"baseInfo", formName:'draftForm'})
      if(prodState < 4 || prodState ==5){
        validForms.push(
          {page:"baseInfo", formName:'baseForm'},
          {page:"prodInfo", formName:'prodForm'},
          {page:"itemInfo", formName:'itemForm'},
          {page:"rebateInfo", formName:'rebateForm'}
        )
      }
      Promise.all(
        validForms.map(({page,formName}) => {
            return this.$refs[page].$refs[formName].validate();
        })
      ).then(() => {
        // 校验通过
        // 校验产品信息
        if(prodState!=4){
          if(!this.form.prodInfo || this.form.prodInfo.products.length ==0){
            this.$message({ type: "warning", message: "请添加产品信息!" });
            return false;
          }
          if(!this.checekProds(this.form.prodInfo.products)){
            this.$message({
              type: "warning",
              message: "产品信息存在完全一致的数据，请核对!"
            });
            return false;
          }
          
          // 校验产品项信息
          if(!this.form.itemInfo || this.form.itemInfo.items.length ==0){
            this.$message({ type: "warning", message: "请添加产品项信息!" });
            return false;
          }
          if(!this.checekProds(this.form.itemInfo.items)){
            this.$message({
              type: "warning",
              message: "产品项存在完全一致的数据，请核对!"
            });
            return false;
          }
        }
        
        // 组合数据进行保存/修改
        let params = Object.assign(this.form.baseInfo,{groupDetails:this.form.itemInfo.items.concat(this.form.prodInfo.products)})
        if(prodState!=5){
          params.prodState = prodState;
        }
       
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
        this.loading = false;
      })
    },
    checekProds(list){
      let prodStr="|";
      for(let prod of list){
        let infoStr = JSON.stringify(prod);
        if(prodStr != '|' && prodStr.includes('|'+infoStr+'|')){
          
          return false;
        }else{
          prodStr += infoStr+'|'
        }
      }
      return true;
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
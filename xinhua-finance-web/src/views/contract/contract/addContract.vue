<!--创建合约-->
<template>
   <basic-container>
        <el-row style="margin-bottom: 20px;">
        <el-col :span="24">
          <el-page-header @back="goBackList" :content="title">
          </el-page-header>
        </el-col>
        </el-row>
       <el-steps :active="activeStep">
            <el-step title="基本信息"></el-step>
            <el-step title="产品信息"></el-step>
            <el-step title="用户协议"></el-step>
            <el-step title="用户授权"></el-step>
        </el-steps>
        <basic-info ref="basicInfo" v-if="activeStep==1" @update="updateStep" :dicDataList="dicDataList" :basicInfo="contractInfo.basicInfo" @saveBasic="saveBasic"></basic-info>
        <product-info ref="productInfo" v-if="activeStep==2" @update="updateStep"></product-info>
        <user-agreement-info  ref="userAgreementInfo" v-if="activeStep==3" @update="updateStep"  @saveUserAgreementInfo="saveUserAgreementInfo" :userAgreementInfo="contractInfo.userAgreementInfo"></user-agreement-info>
        <authorization-info  ref="authorizationInfo" v-if="activeStep==4" @update="updateStep"></authorization-info>
   </basic-container>
</template>
<script>
import basicInfo from './components/basicInfo';
import productInfo from './components/productInfo';
import userAgreementInfo from './components/userAgreementInfo';
import authorizationInfo from './components/authorizationInfo';
import {saveBasicInfo, saveUserAgreementInfo} from "@/api/contract/contract/contract";
import {Message} from 'element-ui'
export default {
    name:"add-contract",
    components:{
        basicInfo,
        productInfo,
        userAgreementInfo,
        authorizationInfo
    },
    props:{
      id:'',
      title:"新增",
      dicDataList:[]
    },
    data(){
        return{
            activeStep:1,
            contractInfo:{
                id:"",
                basicInfo:{
                    conSource:"",
                    conNcId:"",
                    conNcNo:"",
                    conNo:"",
                    conName:"",
                    conLimit:"",
                    custId:"",
                    custName:"",
                    custContractId:"",
                    deptId:"",
                    conPartb:"",
                    conType:"",
                    conState:"",
                    signState:"",
                    signType:"",
                    renewalState:"",
                    signDate:"",
                    contractTime:"",
                    conStartTime:"",
                    conEndTime:"",
                    conAmount:"",
                    conCurrAmount:"",
                    conDemand:"",
                    isCreated:"",

                    fileList:[],//用户协议附件list
                    custContract:[],//合约联系人信息
                    contactsNumber:0//可选合约联系人数量
                },
                productInfo:{},
                userAgreementInfo:{},
                authorizationInfo:{}
            }
        }
    },
    methods:{
        updateStep(step){
            if(step==5){
                this.activeStep=1;
                this.$emit("addBack");
            }else{
                this.activeStep=step;
            }
        },
        goBackList() {
          this.$emit("addBack");
        },
        //保存基本信息
        saveBasic(){
            this.contractInfo.basicInfo.id = this.contractInfo.id;//有可能其他模块先创建数据，已经生成了合约id，但是basicinfo里面id还没有，所以这里要赋值一下
            var basicInfo = this.contractInfo.basicInfo;
            if(basicInfo.contractTime){
                basicInfo.conStartTime = basicInfo.contractTime[0] + " 00:00:00";
                basicInfo.conEndTime = basicInfo.contractTime[1] + " 23:59:59";
            }
            var fileList = basicInfo.fileList;
            for(let i =0; i<fileList.length; i++){
                fileList[i].fileUrl = fileList[i].response.data.link;
                fileList[i].fileName = fileList[i].name;
                fileList[i].status = null;
            }
            saveBasicInfo(basicInfo).then(res =>{
                this.contractInfo.id = res.data.data.id;
                this.contractInfo.basicInfo.id = res.data.data.id;
                this.contractInfo.basicInfo.conNo = res.data.data.conNo;
                this.contractInfo.basicInfo.isCreated = res.data.data.isCreated;
                this.$message.success('保存成功');
            } ,error =>{
                this.$message.error(error);
            });
        },
        saveUserAgreementInfo(userAgreementInfo) {
           let vm = this;
            // 如果未选择数据 则不做处理
            if (userAgreementInfo == null || userAgreementInfo == undefined ) return;
            this.contractInfo.userAgreementInfo = userAgreementInfo;
            console.log(this.contractInfo.userAgreementInfo);
            console.log(userAgreementInfo);
            // 抽取协议id
            let agreementId = [];
            userAgreementInfo.forEach(function(value,index){
              agreementId.push("".concat(value.id));
            })
            // 定义传值对象
            let params = {
              id: this.contractInfo.id,
              agreementIdList: agreementId
            }

            // 保存信息
            saveUserAgreementInfo(params).then(function (rtnData) {
              vm.contractInfo.id = rtnData.data.data;
              Message({
                message: rtnData.data.msg,
                type: 'success'
              })
            });
        }
    }
}
</script>
<style lang="scss">
.el-basic-title{
    font-size: 16px;
    font-weight: 500;
    color: #333;
    padding:1rem 0;
}
.el-product-btn{
   margin: 0 0 1rem 0.5rem;
}
.el-basic-content{
    .el-form--inline .el-form-item,
    .el-select,
    .el-date-editor.el-input,
    .el-range-editor--small.el-input__inner{
        width: 100%;
    }
    .el-form--inline .el-form-item__content{
        width: calc( 100% - 200px );
    }
    .add-contacts{
        margin-bottom: 1rem;
    }
}
 .el-step-footer{
    text-align: center;
}
.dialog-footer{
    text-align: center;
}
</style>

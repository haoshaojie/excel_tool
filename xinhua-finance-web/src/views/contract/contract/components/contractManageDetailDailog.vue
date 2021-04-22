<!--合约管理用户详情-->
<template>
    <el-dialog
    title="合约信息详情"
    width="60%"
    :visible.sync="showDetail">
    <el-tabs v-model="activeTab">
        <el-tab-pane  label="基本信息" name="basicInfo">
            <basic-info-detail ref="basicInfoDetail"></basic-info-detail>
        </el-tab-pane>
        <el-tab-pane  label="产品信息" name="productInfo">
            <product-info-detail ref="productinfoDetail"></product-info-detail>
        </el-tab-pane>
        <el-tab-pane  label="用户协议" name="userInfo">
            <user-agreement-detail ref="userAgreementDetail"></user-agreement-detail>
        </el-tab-pane>
        <el-tab-pane  label="合同信息" name="contractinfo">
            <contract-info-detail ref="contractInfoDetail"></contract-info-detail>
        </el-tab-pane>
    </el-tabs>
    <el-button type="primary" size="small" class="el-change-contract">转为正式合约</el-button>
    </el-dialog>
</template>
<script>
import basicInfoDetail from "./basicInfoDetail";
import productInfoDetail from "./productInfoDetail";
import userAgreementDetail from "./userAgreementDetail";
import contractInfoDetail from "./contractInfoDetail";
export default {
    name:"contract-manageDetail-dailog",
    components:{
        basicInfoDetail,
        productInfoDetail,
        userAgreementDetail,
        contractInfoDetail
    },
    data(){
        return{
            showDetail:false,
            activeTab:"basicInfo"
        }
    },
    methods:{
        openDalog(data){
            this.showDetail=true;
        }
    }
}
</script>
<style lang="scss">
.el-change-contract{
    position: absolute;
    top: 5rem;
    right: 1.2rem;
}
.el-detail-table {
    width: 100%;
    background-color: #FFFFFF;
    color: #222222;
    tr td {
      width: 25%;
      padding: 0.5rem 0 0.8rem 0;
      box-sizing: border-box;
      border: 1px solid #D7D9E2;
      font-size: 13px !important;
      text-align: center;
    }

    tr td:nth-child(odd) {
      font-weight: bold;
      background-color: #E3F2FD;
    }
  }
.dialog-footer{
    text-align: center;
}
 .el-table-title{
   margin:1rem 0;
   font-size: 14px;
   color: #333;
 }
</style>
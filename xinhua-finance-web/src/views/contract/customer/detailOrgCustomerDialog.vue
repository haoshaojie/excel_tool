<template>
  <el-dialog
    title="客户信息详情"
    :visible.sync="show"
    width="80%"
    @close="cancelDialog"
  >

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基本信息" name="first">
        <base-info ref="baseInfo" :caseInfoList="caseInfoList"  />
      </el-tab-pane>
      <el-tab-pane label="合约信息" name="second">
        <contract-info></contract-info>
      </el-tab-pane>
      <el-tab-pane label="用户信息" name="third">
        <user-info></user-info>
      </el-tab-pane>
      <el-tab-pane label="客户管理活动" name="fourth">
        <customer-management :caseInfoList="caseInfoList"></customer-management>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script>
  import baseInfo from "./eltabs/baseInfo";
  import contractInfo  from "./eltabs/contractInfo";
  import userInfo  from "./eltabs/userInfo";
  import customerManagement  from "./eltabs/customerManagement";
  import { getDetail} from "@/api/contract/customer/orgcustomer";

  export default {
      components:{
        baseInfo,
        contractInfo,
        userInfo,
        customerManagement
      },
      name:"detail-org-customer-dialog",
      props:{
        show:{
          type: Boolean,
          default:false
        },
        id:{
          type:String,
          default:""
        }
      },
      data(){
        return {
          activeName: 'first',
          caseInfoList:{
            caseNo:1
          }
        }
      },
      methods: {
        initData(detailId){
          getDetail(detailId).then(res => {
            this.caseInfoList=res.data.data;
          });
          this.$nextTick(()=>{
            // this.$refs.baseInfo.initOrgCust();
          })
        },
        cancelDialog() {
          this.$emit('cancel');
        },
        handleClick(tab, event) {
          console.log(tab, event);
        }
      },

    };
</script>
<style lang="scss">
.el-header{
    display:flex;
    justify-content: space-between;
    align-items: center;
}
</style>

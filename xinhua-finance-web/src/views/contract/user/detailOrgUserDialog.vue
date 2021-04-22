<template>
  <el-dialog
    title="用户详情"
    :visible.sync="showDetailFlag"
    width="80%"
    @close="cancelDialog"
  >
    <basic-container class="delivery-content delivery-details">
      <!--跨列：colspan 跨行：rowspan-->
      <table>
        <tr>
          <td style="width: 20rem">用户编码</td>
          <td style="width: 20rem">{{userInfoData.userNo}}</td>
          <td style="width: 20rem">姓名</td>
          <td style="width: 20rem">{{userInfoData.userName}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">性别</td>
          <td style="width: 20rem">{{ userInfoData.userSexName}}</td>
          <td style="width: 20rem">部门</td>
          <td style="width: 20rem">{{userInfoData.custOrg}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">客户编码</td>
          <td style="width: 20rem">{{userInfoData.custCode}}</td>
          <td style="width: 20rem">客户名称</td>
          <td style="width: 20rem">{{userInfoData.custName}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">邮箱</td>
          <td style="width: 20rem">{{userInfoData.userEmail}}</td>
          <td style="width: 20rem">手机号码</td>
          <td style="width: 20rem">{{userInfoData.userPhone}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">固定电话</td>
          <td style="width: 20rem">{{userInfoData.userTele}}</td>
          <td style="width: 20rem">传真</td>
          <td style="width: 20rem">{{userInfoData.userFax}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">用户类型</td>
          <td style="width: 20rem" colspan="3">{{userInfoData.userTypeName}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">用户描述</td>
          <td style="width: 20rem" colspan="3">{{userInfoData.userDesc}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">客户经理</td>
          <td style="width: 60rem">{{contactPerson}}</td>
          <td style="width: 20rem">所属机构</td>
          <td style="width: 60rem">{{custOrgName}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">创建人</td>
          <td style="width: 20rem">{{userInfoData.createUserName}}</td>
          <td style="width: 20rem">创建时间</td>
          <td style="width: 20rem">{{userInfoData.createTime}}</td>
        </tr>
        <tr>
          <td style="width: 20rem">最后修改人</td>
          <td style="width: 20rem">{{userInfoData.updateUserName}}</td>
          <td style="width: 20rem">最后修改时间</td>
          <td style="width: 20rem">{{userInfoData.updateTime}}</td>
        </tr>
      </table>
      <div class="el-table-title">账号信息</div>
    </basic-container>
  </el-dialog>
</template>

<script>
  import {getDetail, getDic,getContractById} from "@/api/contract/user/orguser";
  import {getDept,getDeptList} from "@/api/system/dept";

  export default {
    components: {},
    name: "detail-org-user-dialog",
    props: {
      showDetailFlag: {
        type: Boolean,
        default: false
      },
      id: {
        type: String,
        default: ""
      }
    },
    data() {
      return {
        contactPerson:"",
        custOrgName:"",
        userInfoData: {
          caseNo: 1
        },
        sexs: [],
        userTypes: [],
      }
    },
    filters: {},
    methods: {
      initData(detailId) {
        getDetail(detailId).then(res => {
          this.userInfoData = res.data.data;
          var userType=this.userInfoData.userType;
          if (userType) {
            this.userTypes.forEach(item=>{
              if (userType==item.dictKey){
                this.userInfoData.userTypeName=item.dictValue
              }
            })
          }
          var sex=this.userInfoData.userSex;
          if (sex){
            this.sexs.forEach(item=>{
              if (sex==item.dictKey){
                this.userInfoData.userSexName=item.dictValue
              }
            })
          }
          var custManager = this.userInfoData.custManager;
          if (custManager) {
            getContractById(custManager).then(res => {
              var data=res.data.data
              this.contactPerson=data.custManager;
              var custOrg = data.custOrg;
              //补充机构名称
              if (custOrg){
                getDeptList('000000').then(res=>{
                  res.data.data.forEach(item=>{
                    if (item.id==custOrg) {
                      this.custOrgName=item.deptName;
                    }

                  });
                })
              }
            })
          }
        });
      },
      cancelDialog() {
        this.$emit('cancel');
      },
      init() {
        //获取性别字典
        getDic(this.getDicValue(this.CONSTANT.SEX)).then(res => {
          this.sexs = res.data.data;
        });
        //获取用户类型字典
        getDic(this.getDicValue(this.CONSTANT.USER_TYPE)).then(res => {
          this.userTypes = res.data.data;
        });
      }
    },
    mounted(){
      this.init();
    }
  };
</script>
<style lang="scss" scoped>
  .delivery-details {
    padding-top: 0 !important;
  }

  table {
    width: 100%;
    background-color: #FFFFFF;
    color: #222222;
    tr td {
      height: 40px;
      padding: 0 0 0 1.25rem;
      box-sizing: border-box;
      border: 1px solid #D7D9E2;
      line-height: 36px;
      font-size: 13px !important;
    }

    tr td:nth-child(odd) {
      font-weight: bold;
      background-color: #E3F2FD;
    }
  }

  .el-table-title {
    margin: 1rem 0;
    font-size: 14px;
    color: #333;
  }

  .el-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
</style>

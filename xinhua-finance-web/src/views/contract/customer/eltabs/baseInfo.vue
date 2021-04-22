<template>
  <basic-container class="delivery-content delivery-details">
    <!--跨列：colspan 跨行：rowspan-->
    <table >
      <tr>
        <td style="width: 20rem">客户编码</td>
        <td style="width: 20rem">{{caseInfoList.custCode}}</td>
        <td style="width: 20rem">客户名称</td>
        <td style="width: 20rem">{{caseInfoList.custName}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">新华信用</td>
        <td style="width: 20rem">已验证</td>
        <td style="width: 20rem">上级单位 </td>
        <td style="width: 20rem">{{caseInfoList.superUnitName}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">客户类型</td>
        <td style="width: 20rem">{{caseInfoList.custTypeName}}</td>
        <td style="width: 20rem">所属行业</td>
        <td style="width: 20rem">{{caseInfoList.custIndustryName}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">所属地域</td>
        <td style="width: 20rem">{{caseInfoList.regionName}}</td>
        <td style="width: 20rem">详细地址</td>
        <td style="width: 20rem">{{caseInfoList.custAddress}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">邮政编码</td>
        <td style="width: 20rem">{{caseInfoList.postcode}}</td>
        <td style="width: 20rem">公司电话</td>
        <td style="width: 20rem">{{caseInfoList.comPhone}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">公司传真</td>
        <td style="width: 20rem">{{caseInfoList.comFax}}</td>
        <td style="width: 20rem">公司网址</td>
        <td style="width: 20rem">{{caseInfoList.comWebsite}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">客户状态</td>
        <td style="width: 20rem">{{caseInfoList.custStateName}}</td>
        <td style="width: 20rem">业务机会状态</td>
        <td style="width: 20rem">{{caseInfoList.businessChanceName}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">业务描述</td>
        <td style="width: 60rem" colspan="3">{{caseInfoList.businessDesc}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">创建人</td>
        <td style="width: 20rem">{{caseInfoList.createUserName}}</td>
        <td style="width: 20rem">创建时间</td>
        <td style="width: 20rem">{{caseInfoList.createTime}}</td>
      </tr>
      <tr>
        <td style="width: 20rem">最后修改人</td>
        <td style="width: 20rem">{{caseInfoList.updateUserName}}</td>
        <td style="width: 20rem">最后修改时间</td>
        <td style="width: 20rem">{{caseInfoList.updateTime}}</td>
      </tr>
    </table>
    <div class="el-table-title">联系人信息</div>
    <avue-crud
    :data="caseInfoList.orgContacts"
    :option="option"
    >
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getDeptTree} from "@/api/system/dept";
  export default {
    name: "baseInfo",
    props: {
      // 案件信息
      caseInfoList: {
        type: Object,
        required: true
      },
      // 查看详情
      isDetails: {
        type: Boolean,
        default: false
      }
    },
    methods:{
      initOrgCust(){
        let _this = this
        getDeptTree('000000').then(res => {
          var custOrg = _this.findObject(_this.option.column,'custOrg')
          custOrg.dicData=res.data.data;
        });
      }

    },
    data(){
      return{
         page: {
            pageSize: 10,
            currentPage: 1,
            total: 1
        },
        data: [
          {
            contact:'联系人',
            contactType:'联系人类型',
            phone:'18410678906',
            email:'1318921038@qq.com',
            department:'业务发展部',
            position:"技术",
            manager:"网易",
            institutions:"中经社"
          }, {
            contact:'联系人',
            contactType:'联系人类型',
            phone:'18410678906',
            email:'1318921038@qq.com',
            department:'业务发展部',
            position:"技术",
            manager:"网易",
            institutions:"中经社"
          }
        ],
          option:{
            width:"100%",
            addBtn:false,
            editBtn:false,
            delBtn:false,
            refreshBtn:false,
            columnBtn:false,
            menu:false,
            header:false,
            border:true,
            align:'left',
            column:[
              {
                label:'联系人',
                prop:'contactPerson',
              }, {
                label:'联系人类型',
                prop:'contactType',
              },
              {
                label:'联系人方式',
                prop:'contactPhone',
              },
              {
                label:'邮箱',
                prop:'contactEmail',
              },
              {
                label:'部门',
                prop:'contactDept',
              },
              {
                label:'职务',
                prop:'contactJob',
              },
              {
                label:'客户经理',
                prop:'custManager',
              },
              {
                label:'所属机构',
                prop:'custOrg',
                dicData:[],
                type: 'tree',
                props: {
                  label: 'title',
                  value: 'value'
                },
              }
            ]
        }
      }
    },
    watch:{
      caseInfoList(val){
        //caseInfoList发生变化，初始化函数
          this.initOrgCust()
      }
    }
  }
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

 .el-table-title{
   margin:1rem 0;
   font-size: 14px;
   color: #333;
 }
</style>


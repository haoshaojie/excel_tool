<template>
  <el-dialog
    title="导出客户"
    :visible.sync="exportDialogFlag"
    width="80%" center
    @close="cancelDialog">
    <el-form ref="form" :model="form" label-width="180px" >
      <el-form-item label="导出信息选择">
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <div style="margin: 15px 0;"></div>
        <el-checkbox-group v-model="form.selectColumn" >
          <el-checkbox v-for="item in columns" :label="item.value" :key="item.value">{{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-divider></el-divider>
      <el-form-item label="">
        <el-checkbox v-model="form.customerManagementChecked" >客户管理活动（活动编码、活动类型、具体内容）</el-checkbox>
      </el-form-item>
      <el-form-item label="">
        <el-checkbox v-model="form.linkmanInfoChecked">联系人信息</el-checkbox>
      </el-form-item>
      <el-form-item label="">
        <el-checkbox v-model="form.contactInfoChecked">合约信息（客户下的合约信息）</el-checkbox>
      </el-form-item>
      <el-form-item label="">
        <el-checkbox v-model="form.userInfoChecked">用户信息（客户下的用户信息）</el-checkbox>
      </el-form-item>
    </el-form>


    <span slot="footer" class="dialog-footer">
  <el-button type="primary" @click="exportData">确 定</el-button>
  <el-button @click="cancelDialog">取 消</el-button>
  </span>
  </el-dialog>
</template>

<script>
  import {exportData} from "@/api/contract/customer/orgcustomer";
  import website from "@/config/website";
  import {getToken} from '@/util/auth';
  import qs from 'qs'
  export default {
    name: "customer-export-dialog",
    props: {
      exportDialogFlag: {
        type: Boolean,
        default: false
      }
    },
    mounted() {
      for (let i = 0; i < this.columns.length; i++) {
        this.column.push(this.columns[i].value);
      }
    },
    data() {
      return {
        form:{
          selectColumn: [],
          customerManagementChecked:false,
          linkmanInfoChecked:false,
          contactInfoChecked:false,
          userInfoChecked:false,
        },
        checkAll: false,
        isIndeterminate: true,
        column:[],
        columns : [{
          name:'客户编码',
          value:'custCode'
        },{
          name:'客户名称',
          value:'custName'
        },{
          name:'上级单位',
          value:'superUnitName'
        },{
          name:'所属行业',
          value:'custIndustryName'
        },{
          name:'所属地域',
          value:'regionName'
        },{
          name:'客户类型',
          value:'custTypeName'
        },{
          name:'详细地址',
          value:'custAddress'
        },{
          name:'邮政编码',
          value:'postcode'
        },{
          name:'公司电话',
          value:'comPhone'
        },{
          name:'公司网址',
          value:'comWebsite'
        },{
          name:'客户状态',
          value:'custStateName'
        },{
          name:'业务机会状态',
          value:'businessChanceName'
        },{
          name:'业务描述',
          value:'businessDesc'
        },{
          name:'创建人',
          value:'createUserName'
        },{
          name:'创建时间',
          value:'originCreateTime'
        },{
          name:'最后修改人',
          value:'updateUserName'
        },{
          name:'最后修改时间',
          value:'originUpdateTime'
        },

        ],
      };
    },
    methods: {
      cancelDialog() {
        this.$emit('cancel');
      },
      exportData(){
          var form=this.form;
        if (form.selectColumn.length==0&&!form.customerManagementChecked&&!form.linkmanInfoChecked&&!form.contactInfoChecked&&!form.userInfoChecked){
          this.$message({
            message: "请选择要导出的列",
            type: "error"
          });
          return;
        }
        window.open(`/api/cnfic-contract-manage/orgcustomer/export?${qs.stringify(this.form)}&${website.tokenHeader}=${getToken()}`);
        this.cancelDialog();
      },
      handleCheckAllChange(val) {
        this.form.selectColumn = val ? this.columns.map(item => item.value): [];

        if (val){
          this.form.customerManagementChecked=true;
          this.form.linkmanInfoChecked=true;
          this.form.contactInfoChecked=true;
          this.form.userInfoChecked=true;
        } else {
          this.form.customerManagementChecked=false;
          this.form.linkmanInfoChecked=false;
          this.form.contactInfoChecked=false;
          this.form.userInfoChecked=false;
        }
        this.isIndeterminate = false;
      },

    }
  };
</script>

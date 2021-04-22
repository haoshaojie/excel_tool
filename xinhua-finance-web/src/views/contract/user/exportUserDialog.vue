<template>
  <el-dialog
    title="导出机构用户"
    :visible.sync="exportDialogFlag"
    width="60%" center
    @close="cancelDialog">
    <el-form ref="form" :model="form" label-width="180px" >
      <el-form-item label="导出信息选择">
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <div style="margin: 15px 0;"></div>
      </el-form-item>
      <el-form-item>
        <el-checkbox-group v-model="form.selectColumn" >
          <el-checkbox v-for="item in columns" :label="item.value" :key="item.value">{{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-divider></el-divider>
      <el-form-item label="">
        <el-checkbox-group v-model="form.selectColumnAccount" >
          <el-checkbox v-for="item in columnsAccount" :label="item.value" :key="item.value">{{item.name}}</el-checkbox>
        </el-checkbox-group>
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
    name: "export-user-dialog",
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
          query:{},
          selectColumn: [],
          selectColumnAccount: [],
          customerManagementChecked:false,
          linkmanInfoChecked:false,
          contactInfoChecked:false,
          userInfoChecked:false,
        },
        checkAll: false,
        isIndeterminate: false,
        column:[],
        columnsAccount:[
          {
            name:'是否生成账号',
            value:'isCreateAccount'
          },{
            name:'用户账号',
            value:'userAccount'
          },{
            name:'用户状态',
            value:'userStatus'
          },{
            name:'账号状态',
            value:'accountStatus'
          }
        ],
        columns : [{
          name:'用户编码',
          value:'userNo'
        },{
          name:'姓名',
          value:'userName'
        },{
          name:'性别',
          value:'userSexName'
        },{
          name:'客户编码',
          value:'custCode'
        },{
          name:'部门',
          value:'custOrg'
        },{
          name:'用户类型',
          value:'userTypeName'
        },{
          name:'用户描述',
          value:'userDesc'
        },{
          name:'固定电话',
          value:'userTele'
        },{
          name:'传真',
          value:'userFax'
        },{
          name:'邮箱',
          value:'userEmail'
        },{
          name:'手机号码',
          value:'userPhone'
        },{
          name:'创建时间',
          value:'originCreateTime'
        },{
          name:'创建人',
          value:'createUserName'
        },{
          name:'最后修改时间',
          value:'originUpdateTime'
        },{
          name:'最后修改人',
          value:'updateUserName'
        },{
          name:'客户经理',
          value:'custManagerName'
        },{
          name:'所属机构',
          value:'custOrgName'
        },

        ],
      };
    },
    methods: {
      cancelDialog() {
        this.$emit('cancel');
      },
      exportData(){
        var concat = this.form.selectColumn.concat(this.form.selectColumnAccount);
        if (concat.length==0){
          this.$message({
            message: "请选择要导出的列",
            type: "error"
          });
          return;
        }
        this.form.column=concat;
        Object.assign(this.form,this.query);
        window.open(`/api/cnfic-contract-manage/orguser/export?${qs.stringify(this.form)}&${website.tokenHeader}=${getToken()}`);
        this.cancelDialog();

      },
      handleCheckAllChange(val) {
        this.form.selectColumnAccount = val ? this.columnsAccount.map(item => item.value) : []
        this.form.selectColumn = val ? this.columns.map(item => item.value): [];
        this.isIndeterminate = false;
      },
      accept(val){
        this.query=val;
      }
    }
  };
</script>

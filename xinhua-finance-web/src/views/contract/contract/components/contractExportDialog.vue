<template>
  <el-dialog
    title="导出"
    :visible.sync="exportDialogFlag"
    width="80%"
    @close="cancelDialog"
    @open="init">
    <el-form ref="form" :model="form" label-width="180px" >
      <el-form-item label="导出信息选择">
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <div style="margin: 15px 0;"></div>
        <el-checkbox-group v-model="form.selectColumn" >
          <el-checkbox v-for="item in columns" :label="item.value" :key="item.value" :checked="item.checked">{{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-divider></el-divider>
      <el-form-item label="">
        <el-checkbox v-model="form.linkmanInfoChecked">联系人信息</el-checkbox>
      </el-form-item>
      <el-form-item label="">
        <el-checkbox v-model="form.contactInfoChecked">产品信息（合约下的合约信息）</el-checkbox>
      </el-form-item>
      <el-form-item label="">
        <el-checkbox v-model="form.userInfoChecked">用户信息（合约下的用户信息）</el-checkbox>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
  <el-button type="primary" @click="exportData">确 定</el-button>
  <el-button @click="cancelDialog">取 消</el-button>
  </span>
  </el-dialog>
</template>

<script>
  import website from "@/config/website";
  import {getToken} from '@/util/auth';
  import qs from 'qs'
  export default {
    name: "customer-export-dialog",
    props: {
      exportDialogFlag: {
        type: Boolean,
        default: false
      },
      searchCondition:{
        type:Object
      },
      selectedRows:{
        type:Object
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
          linkmanInfoChecked:false,
          contactInfoChecked:false,
          userInfoChecked:false,
          selectedRow:[]
        },
        checkAll: false,
        isIndeterminate: true,
        column:[],
        columns : [{
          name:'合约编码',
          value:'conNo',
        },{
          name:'合约名称',
          value:'conName',
        },{
          name:'合约范围',
          value:'conLimit'
        },{
          name:'合约类型',
          value:'conType',
        },{
          name:'合约状态',
          value:'conState',
        },{
          name:'所属部门',
          value:'deptId'
        },{
          name:'客户编号',
          value:'custCode',
        },{
          name:'客户名称',
          value:'custName',
        },{
          name:'签约乙方',
          value:'conPartb',
        },{
          name:'签约状态',
          value:'signState',
          checked:true
        },{
          name:'签约日期',
          value:'signDate'
        },{
          name:'合约起始日期',
          value:'conStartTime'
        },{
          name:'合约终止日期',
          value:'conEndTime'
        },{
          name:'合约金额',
          value:'conAmount'
        },{
          name:'合约当年期金额',
          value:'conCurrAmount'
        },{
          name:'具体服务要求',
          value:'conDemand'
        },{
          name:'创建人',
          value:'createUser'
        },
        {
          name:'创建时间',
          value:'createTime'
        },
        {
          name:'最后修改人',
          value:'updateUser'
        },
        {
          name:'最后修改时间',
          value:'updateTime'
        },
        ],
      };
    },
    methods: {
      cancelDialog() {
        this.$emit('cancel');
      },
      init(){
        // 组件初始化页面展示
        this.form.selectColumn = ['conNo','conName','conType','conState','custCode','conPartb','custName'];
        this.form.linkmanInfoChecked=false;
        this.form.contactInfoChecked=false;
        this.form.userInfoChecked=false;
        this.checkAll=false;
        // 继承查询条件属性
        for (let pro in this.searchCondition) {
          if(this.searchCondition[pro] != null && this.searchCondition[pro] != undefined) {
            if(!(pro.indexOf("$")>= 0)){
                this.form[pro] = this.searchCondition[pro];
            }
          }
        }
        for(let id in this.selectedRows) {
          this.form.selectedRow.push(id);
        }
      },
      exportData(){
        var form=this.form;
        if (form.selectColumn.length==0&&!form.linkmanInfoChecked&&!form.contactInfoChecked&&!form.userInfoChecked){
          this.$message({
            message: "请选择要导出的列",
            type: "error"
          });
          return;
        }
        window.open(`/api/cnfic-contract-manage/contract/export?${qs.stringify(this.form)}&${website.tokenHeader}=${getToken()}`);
        this.cancelDialog();
      },
      handleCheckAllChange(val) {
        this.form.selectColumn = val ? this.columns.map(item => item.value): [];
        if (val){
          this.form.linkmanInfoChecked=true;
          this.form.contactInfoChecked=true;
          this.form.userInfoChecked=true;
        } else {
          this.form.linkmanInfoChecked=false;
          this.form.contactInfoChecked=false;
          this.form.userInfoChecked=false;
        }
        this.isIndeterminate = false;
      },

    }
  };
</script>

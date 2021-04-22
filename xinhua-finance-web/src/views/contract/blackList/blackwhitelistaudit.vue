<template>
  <basic-container>
    <avue-tabs :option="tabsOption" @change="handleChange"></avue-tabs>
      <avue-crud :option="noAuditOption"
                        :search.sync="search"
                        :show-column.sync="showColumn"
                        :table-loading="loading"
                        :data="data"
                        :page.sync="page"
                        :permission="permissionList"
                        :before-close="beforeClose"
                        :before-open="beforeOpen"
                        v-model="form"
                        ref="crud"
                        @row-update="rowUpdate"
                        @search-change="searchChange"
                        @search-reset="searchReset"
                        @selection-change="selectionChange"
                        @current-change="currentChange"
                        @size-change="sizeChange"
                        @on-load="onLoad">
        <template slot-scope="" slot="custCodeSearch">
          <el-input placeholder="请输入客户编码" v-model="search.custCode"></el-input>
        </template>
      <template slot="menuLeft">
        <el-button type="primary"
        size="small"
        class="el-menu-button"
        v-show="!showState"
        v-if="permission.blackwhitelistaudit_audit"
        @click="showAuditDialog(null)">审批</el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" v-show="!showState" size="small" v-if="permission.blackwhitelistaudit_audit" @click.native="showAuditDialog(scope.row)">审批</el-button>
      </template>
    <template slot-scope="" slot="search">
      <el-form ref="form" :model="search" label-width="90px" v-show="showState" class="el-form-search">
            <el-form-item label="审核状态：">
              <el-select placeholder="请选择" size="small" v-model="search.auditState">
                <el-option
                v-for="item in statusList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
        </el-form>
      </template>
    </avue-crud>
      <el-dialog title="审批"
               :visible.sync="auditDialog"
               width="50%">
        <avue-form ref="auditForm" :option="auditOption" v-model="auditObj" @submit="handleAudit"></avue-form>
      </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, audit, choosecustomer,listcustomer} from "@/api/contract/blackList/blackwhitelistaudit";
  import {mapGetters} from "vuex";
  const DIC=[{
    label:'审批通过',
    value:"审批通过"
  },{
    label:'审批不通过',
    value:"审批不通过"
  }];
  export default {
    data(){
      var validateAuditResult = (rule, value, callback)=>{
        if (value === '') {
          callback();
        } else {
          let textReg = new RegExp("[\u4E00-\u9FA5]","g");
          if (textReg.test(value)){
            callback();
          }else {
            callback(new Error('不能输入特殊字符'));
          }
        }
      };
      return {
        statusList:DIC,
        type:{},
        showColumn:['exchange','dataCode','type','custName','custCode','auditState','createUserName','createTime','remark'],
        tabsOption: {
          column: [{
            label: '待审核',
            prop: 'tab1',
          }, {
            label: '已审核',
            prop: 'tab2',
          }],
        },
        showState:false,
        form: {},
        search:{},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        noAuditOption: {
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan:4,
          tip: false,
          border: true,
          index: false,
          viewBtn: false,
          viewBtnText:"详情",
          addBtn: false,
          delBtn: false,
          editBtn:true,
          editBtnText:"选择客户",
          selection: true,
          dialogWidth:"35%",
          menuPosition:"center",
          column: [
            {
              label: "交易所",
              prop: "exchange",
              order:1,
              search:true,
              searchOrder:7,
              labelslot:true,
              editDisabled:true,
            },
            {
              label: "数据编码",
              prop: "dataCode",
              order:2,
              search:true,
              searchOrder:5,
              editDisabled:true
            },
            {
              label: "类型",
              prop: "type",
              order:3,
              type: 'select',
              dicUrl: this.getDicValue(this.CONSTANT.BLACKLIST_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              search:true,
              searchOrder:3,
              editDisabled:true
            },
            {
              label: "客户名称",
              prop: "custName",
              order:4,
              search:true,
              searchOrder:6,
              editDisabled:true
            },
            {
              label: "客户编码",
              prop: "custCode",
              order:5,
              search:true,
              searchOrder:4,
              type:"select",
              searchslot:true,
              dicData: [],
              props: {
                label: "custCode",
                value: "custCode"
              },
              change:({value})=>{
              this.custCodeList.forEach(item=>{
                  if(item.custCode==value){
                    this.form.custId=item.id;
                    console.log("客户表id",this.form.custId);
                  }
                })
              },
            },
            {
              label: "客户表id",
              prop: "custId",
              addDisplay:false,
              editDisplay:false,
              viewDisplay:false,
              hide:true,
            },
            {
              label: "申请人",
              prop: "createUserName",
              editDisplay:false,
              order:6,
            },
            {
              label: "申请时间",
              prop: "createTime",
              editDisplay:false,
              order:7,
            },
            {
              label: "审核人",
              prop: "auditUser",
              editDisplay:false,
              hide:true,
              order:40,
            },
            {
              label: "审核时间",
              prop: "auditTime",
              editDisplay:false,
              order:8
            },
            {
              label: "审核状态",
              prop: "auditState",
              editDisplay:false,
              order:9
            },
            {
              label: "审核意见",
              prop: "auditResult",
              editDisplay:false,
              hide: true,
              order:9
            },
            {
              label: "申请时间",
              prop: "createTimeRange",
              editDisplay:false,
              viewDisplay:false,
              hide:true,
              search:true,
              searchOrder:8,
              type:"daterange",
              searchRange:true,
              valueFormat: "yyyy-MM-dd",
              props: {
                startPlaceholder: "开始日期",
                endPlaceholder: "结束日期",
                rangeSeparator:'至'
              },
            }
          ]
        },
        auditDialog:false,
        auditObj:{},
        auditOption:{
          column: [
            {
              label: '审核方式',
              prop: 'auditState',
              type: 'radio',
              span:24,
              border:false,
              dicData:DIC,
              rules: [{
                required: true,
                message: "请勾选审核方式",
                trigger: "change"
              }],
            },
            {
              label: "审核意见",
              prop: "auditResult",
              type:"textarea",
              maxRows:4,
              minRows:3,
              maxlength:50,
              span:24,
              showWordLimit:true,
              rules: [{ validator: validateAuditResult, trigger: 'blur' }]
            },
          ]
        },
        custCodeList:[]
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.blackwhitelist_add, false),
          viewBtn: this.vaildData(this.permission.blackwhitelist_view, false),
          editBtn: this.vaildData(this.permission.blackwhitelist_edit, false)
        };
      },
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    mounted(){
      this.type = this.tabsOption.column[0];
      if (this.type.prop =='tab1'){
        this.query.auditState = "";
      }
    },
    watch:{
      'form.custName'() {
        const column = this.findObject(this.noAuditOption.column, "custCode");
        if (this.form.custName!=''&&this.form.custName!=undefined) {
          listcustomer(1, 20, {custName:this.form.custName}).then(res => {
            this.custCodeList=res.data.data.records.length>0?res.data.data.records:[{custCode:"",custCode:"无数据"}];
            column.dicData=this.custCodeList;
          });
        }
      }
    },
    methods: {
      handleChange(column) {
        this.showState=!this.showState;
        this.type = column;
        if(column.prop=='tab2'){
          this.showColumn.push('auditTime');
          this.noAuditOption.viewBtn=this.showState;
          this.noAuditOption.editBtn=!this.showState;
          this.noAuditOption.selection=!this.showState;
          this.$refs.crud.searchReset();
        }else{
          for (const i in this.showColumn) {
            if(this.showColumn[i]=='auditTime'){
              this.showColumn.splice(i,1);
            }
          }
          this.noAuditOption.viewBtn=this.showState;
          this.noAuditOption.editBtn=!this.showState;
          this.noAuditOption.selection=!this.showState;
          this.$refs.crud.searchReset();
        }
      },
      showAuditDialog(row){
        if (row){
          this.auditObj.ids = row.id;
          this.auditDialog = true;
        } else {
          if (this.selectionList.length === 0) {
            this.$message.warning("请选择至少一条数据");
            return;
          }
          this.auditObj.ids = this.ids;
          this.auditDialog = true;
        }
      },
      handleAudit(auditForm,done,loading){
        audit(auditForm).then(() => {
          done();
        this.auditDialog = false;
        this.$refs.auditForm.resetForm();
        this.onLoad(this.page);
        this.$message({
          type: "success",
          message: "操作成功!"
        });
      }, error => {
          done();
          window.console.log(error);
          this.$refs.auditForm.resetForm();
          // loading();
          });
      },
      rowUpdate(row, index, done, loading) {
        choosecustomer(row).then(() => {
          done();
        this.$refs.crud.searchReset();
        this.$message({
          type: "success",
          message: "操作成功!"
        });
      }, error => {
          window.console.log(error);
          loading();
        });
      },
      beforeClose(done, type){
        this.custCodeList=[{custCode:"",custCode:"无数据"}];
        const column = this.findObject(this.noAuditOption.column, "custCode");
        column.dicData=this.custCodeList;
        this.$forceUpdate();
        done();
      },
      beforeOpen(done, type) {
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            done();
          this.form = res.data.data;
        }, error => {
            window.console.log(error);
            this.onLoad(this.page);
          });
        }
      },
      searchReset() {
        this.form={};
        this.query = {};
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        this.query = params;
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done();
      },
      selectionChange(list) {
        this.selectionList = list;
      },
      selectionClear() {
        this.selectionList = [];
        this.$refs.crud.toggleSelection();
      },
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        if (!this.type.prop || this.type.prop == 'tab1') {
          this.query.auditCheckTab = '待审核';
        }else {
          this.query.auditCheckTab = '已审核';
        }
        if (this.query.createTimeRange){
          this.query.startTime = this.query.createTimeRange[0];
          this.query.endTime = this.query.createTimeRange[1];
          this.query.createTimeRange = null;
        }
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
        this.loading = false;
        this.selectionClear();
      });
      }
    }
  };
</script>

<style lang="scss">
.el-menu-button{
  padding: 9px 25px;
}
.el-form-search{
  width: 24.4%;
  .el-form-item{
    margin-bottom: 14px;
  }
  .el-form-item__label{
    padding:0;
    line-height: 32px;
  }
  .el-form-item__content{
    line-height: 32px;
  }
}
</style>

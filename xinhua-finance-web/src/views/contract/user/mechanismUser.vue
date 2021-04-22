<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot="menuLeft">

        <el-button type="primary" size="small"
                   icon="el-icon-plus" @click="handleAdd"
                    >新增</el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.orguser_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button type="success"
                   size="small"
                   plain
                   icon="el-icon-upload2"
                   @click="handleUpload">导入
        </el-button>
        <el-button type="warning"
                   size="small"
                   plain
                   icon="el-icon-download"
                   @click="handleExport">导出
        </el-button>
      </template>
      <template slot-scope="scope" slot="menuForm">
        <el-button type="primary" icon="el-icon-s-check" size="small"   @click="addAccount ">生成账号</el-button>
        <el-button type="primary" icon="el-icon-check" size="small"  v-if="type=='add'" @click="$refs.crud.rowSave()">保存</el-button>
        <el-button type="primary" icon="el-icon-check" size="small"  v-if="type=='edit'" @click="$refs.crud.rowUpdate()">修改</el-button>
        <el-button type="primary" icon="el-icon-close" size="small"  @click="$refs.crud.closeDialog()">取消</el-button>
      </template>
      <template slot-scope="{row}" slot="userNo">
        <el-button
          type="text"
          size="mini"
          @click="detailDialog(row)">
          {{row.userNo}}
        </el-button>
      </template>
      <template slot-scope="{row}" slot="isCreateAccountQuerySearch">
        <el-radio-group v-model="form.isCreateAccount">
          <el-radio v-for="item in dicCreateAccountData " :key="item.value"
                    :value="item.value"
                    :label="item.value"
          >{{item.label}}
          </el-radio>
        </el-radio-group>
      </template>
      <template slot-scope="" slot="createTimeSearch">
        <el-date-picker
          v-model="createTime"
          type="daterange"
          value-format="yyyy-MM-dd"
          format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </template>
      <template slot-scope="" slot="updateTimeSearch">
        <el-date-picker
          v-model="updateTime"
          type="daterange"
          value-format="yyyy-MM-dd"
          format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </template>
      <template slot-scope="" slot="userPhoneForm">
        <div>
          <el-input  v-model="form.userPhone" disabled placeholder="请添加手机号" style="width: 100%;">
            <el-button slot="append" icon="el-icon-plus" size="mini" @click="addPhone('userPhone','手机号')" style="width: 10%;">请添加手机号</el-button>
          </el-input>
        </div>
      </template>

      <template slot-scope="" slot="addCustNameForm">
        <div>
          <el-input  v-model="form.addCustName"   disabled placeholder="请选择客户"   style="width: 100%;">
            <el-button slot="append" icon="el-icon-plus" size="mini" @click="addSuperUnit" style="width: 10%;">请选择客户</el-button>
          </el-input>
        </div>
      </template>
      <template slot-scope="" slot="userEmailForm">
        <div>
          <el-input  v-model="form.userEmail" disabled placeholder="请添加邮件" style="width: 100%;">
            <el-button  slot="append" icon="el-icon-plus" size="mini" @click="addPhone('userEmail','邮件')" style="width: 10%;">请添加邮件</el-button>
          </el-input>

        </div>
      </template>
    </avue-crud>
    <add-email-or-phone ref="addEmailOrPhone" :emailOrPhoneData="emailOrPhoneData" :addEmailOrPhoneDialogFlag="addEmailOrPhoneDialogFlag"  @cancelEmailOrPhoneDialog="cancelEmailOrPhoneDialog" @accept="acceptEmailAndPhone" ></add-email-or-phone>
    <detail-org-user-dialog ref="detailOrgUserDialog"  :showDetailFlag="showDetailFlag" @cancel="cancelDetail" ></detail-org-user-dialog>
    <customer-dialog ref="customerDialog" :customerDialogFlag="customerDialogFlag" @acceptSuperUnit="acceptSuperUnit" @cancelCustomerDialog="cancelCustomerDialog" ></customer-dialog>
    <upload-user-dialog  ref="uploadUserDialog" :uploadUserDialogFlag="uploadUserDialogFlag" @cancelUploadUserDialog="cancelUploadUserDialog"></upload-user-dialog>
    <export-user-dialog ref="exportUserDialog" :exportDialogFlag="exportDialogFlag" @cancel="cancelExportDialog"></export-user-dialog>
    <add-account-user ref="addAccountUser" :addAccountUserFlag="addAccountUserFlag" @cancelDialog="cancelAddAccountUserDialog"></add-account-user>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, getContractList} from "@/api/contract/user/orguser";
  import {getCustomerList} from "@/api/contract/customer/orgcustomer";
  import {getDeptList} from "@/api/system/dept";
  import {mapGetters} from "vuex";
  import detailOrgUserDialog from "./detailOrgUserDialog";
  import addEmailOrPhone from "./addEmailOrPhone";
  import customerDialog from "../customer/customerDialog";
  import uploadUserDialog from "./uploadUserDialog";
  import exportUserDialog from "./exportUserDialog";
  import addAccountUser from "./addAccountUser";
  export default {
    components: {
      addAccountUser,
      exportUserDialog,
      uploadUserDialog,
      customerDialog,
      addEmailOrPhone,detailOrgUserDialog
    },
    mounted(){
      //初始化机构树
      getDeptList('000000').then(res => {
      this.depts=res.data.data;
  });
    },
    data() {
      return {
        type:'',
        depts:[],
        custId: null,
        contracts: [],
        createTime: [],
        addEmailOrPhoneDialogFlag:false,
        exportDialogFlag:false,
        uploadUserDialogFlag:false,
        addAccountUserFlag:false,
        customerDialogFlag:false,
        showDetailFlag:false,
        updateTime: [],
        dicCreateAccountData: [{
          label: '全部',
          value: null
        }, {
          label: '否',
          value: 0
        }, {
          label: '是',
          value: 1
        }],
        form: {},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          index: false,
          searchLabelWidth: 100,
          calcHeight: 210,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          viewBtn: false,
          saveBtn:false,
          addBtn:false,
          updateBtn:false,
          cancelBtn:false,
          menuAlign:'center',
          align:'center',
          selection: true,
          searchShow: true,
          column: [
            {
              label: "主键id 雪花算法产生",
              prop: "id",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            },
            {
              label: "用户编码",
              prop: "userNo",
              search: true,
              searchOrder: 7,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              slot:true
            },
            {
              label: "姓名",
              prop: "userName",
              search: true,
              searchOrder: 10,
              viewDisplay: false,
              rules: [{required: true,message: "请输入姓名",trigger: "blur"},
                { min: 0, max: 5, message: '最长限5个字', trigger: 'blur' }],
            },
            {
              label: "性别",
              prop: "userSex",
              hide: true,
              viewDisplay: false,
              rules: [{
                required: true,
                message: "请选择性别",
                trigger: "blur"
              }],
              type: "radio",
              dicUrl: this.getDicValue(this.CONSTANT.SEX),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: '客户名称',
              prop: 'addCustName',
              formslot:true,
              hide: true,
              rules:[{
                required: true, message:`客户名称不能为空`, trigger: 'blur'
              }]

            },
            {
              label: '部门',
              prop: "custOrg",
              hide: true,
              rules: [{ min: 0, max: 10, message: '最长限10个字', trigger: 'blur' }],
            },
            {
              label: "邮箱",
              prop: "userEmail",
              search: true,
              searchOrder: 9,
              formslot:true,
              rules:[{ min: 0, max: 100, message: '最长限100个字', trigger: 'blur' }],


            },
            {
              label: "手机号",
              prop: "userPhone",
              search: true,
              searchOrder: 8,
              formslot:true,

            },
            {
              label: "客户编码",
              prop: "custCode",
              search: true,
              searchOrder: 6,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            },
            {
              label: "客户名称",
              prop: "custName",
              search: true,
              searchOrder: 5,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              rules:[
                {required: true,message: "请选择客户名称",trigger: "blur"}
              ]
            },
            {
              label: "用户类型",
              prop: "userType",
              search: true,
              searchOrder: 4,
              viewDisplay: false,
              type: "select",
              searchMultiple: true,
              dicUrl: this.getDicValue(this.CONSTANT.USER_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "用户描述",
              prop: "userDesc",
              type: "textarea",
              hide: true,
              span: 24,
              rules:[{ min: 0, max: 100, message: '最长限100个字', trigger: 'blur' }],
            },
            {
              label: "固定电话",
              prop: "userTele",
              hide: true,
              rules:[{ min: 0, max: 20, message: '最长限20个字', trigger: 'blur' }],

            },
            {
              label: "传真",
              prop: "userFax",
              hide: true,
              rules:[{ min: 0, max: 20, message: '最长限20个字', trigger: 'blur' }],

            },
            {
              label: "客户经理",
              prop: "custManager",
              hide: true,
              type: "select",
              dicData: [],
              props: {
                label: "custManager",
                value: "id"
              },
              change: ({value,column}) => {
                if (!value){
                  this.form.custOrgName=null;
                }
                if (column.dicData){
                  var contract=column.dicData.find(item=>{
                    return item.id==value;
                  });
                  if (contract){
                    this.depts.find(dic=>{
                      if (dic.id==contract.custOrg){
                        this.form.custOrgName=dic.deptName;
                      }
                    })
                  } ;
                }

              },

            },
            {
              label: "所属机构",
              prop: "custOrgName",
              hide: true,
              detail:true
            },
            {
              label: "是否生成账号",
              prop: "isCreateAccount",
              labelWidth: 100,
              type: 'select',
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              dicData: [{
                label: '否',
                value: 0
              }, {
                label: '是',
                value: 1
              }],
            },
            {
              label: '是否生成账号',
              prop: 'isCreateAccountQuery',
              searchOrder: 3,
              searchslot: true,
              search: true,
              hide: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            },
            {
              label: "创建时间",
              prop: "createTime",
              searchslot: true,
              type: 'date',
              format: 'yyyy-MM-dd',
              valueFormat: 'yyyy-MM-dd',
              search: true,
              span: 12,
              searchOrder: 2,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
            }, {
              label: "最后修改时间",
              prop: "updateTime",
              searchOrder: 1,
              searchslot: true,
              type: 'date',
              format: 'yyyy-MM-dd',
              valueFormat: 'yyyy-MM-dd',
              search: true,
              span: 12,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
            }
          ],
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.orguser_add, false),
          viewBtn: this.vaildData(this.permission.orguser_view, false),
          delBtn: this.vaildData(this.permission.orguser_delete, false),
          editBtn: this.vaildData(this.permission.orguser_edit, false)
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
    methods: {
      rowSave(row, done, loading) {
        add(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowUpdate(row, index, done, loading) {
        update(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowDel(row) {
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return remove(row.id);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
          });
      },
      handleDelete() {
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return remove(this.ids);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
            this.$refs.crud.toggleSelection();
          });
      },
      beforeOpen(done, type) {
        this.type=type;
        var _that=this;
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
            //添加编辑时展示的客户名称
            this.form.addCustName=this.form.custName;
            // this.detailManagerName(this.form.custManager);
            //加载客户经理下拉树
            getContractList(_that.form.custId).then(res => {
              this.option.column.find(item=>{
                if (item.prop=='custManager'){
                  var manager = res.data.data;
                  item.dicData=manager;
                  //加载机构名称
                  if (manager){
                    var contract=manager.find(item=>{
                      return item.id==_that.form.custManager;
                    });
                    if (contract){
                      this.depts.find(dic=>{
                        if (dic.id==contract.custOrg){
                          _that.form.custOrgName=dic.deptName;
                        }
                      })
                    } ;
                  }
                }
              })
            });
          });

          console.log(_that.form)

        }else {
          this.form.userPhone=null;
          this.form.userEmail=null;
        }
        done();
      },
      searchReset() {
        this.createTime=[];
        this.updateTime=[];
        this.query = {};
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        this.query = params;
        if (params.userType) {
          params.userTypes = params.userType;
          params.userType = null;
        }
        if (this.form.isCreateAccount != null) {
          params.isCreateAccount = this.form.isCreateAccount;
        }
        if (this.updateTime) {
          this.query.updateTimeStartTime = this.updateTime[0];
          this.query.updateTimeEndTime = this.updateTime[1];
        }
        if (this.createTime) {
          this.query.createStartTime = this.createTime[0];
          this.query.createEndTime = this.createTime[1];
        }
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
      currentChange(currentPage) {
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize) {
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });

      },
      detailDialog(row){
        this.showDetailFlag=true;
        this.$refs.detailOrgUserDialog.initData(row.id);
      },
      addPhone(field,label){

        this.$refs.addEmailOrPhone.openType({
          'field':field,
          'label':label,
          'value':this.form[field]
        })
        this.addEmailOrPhoneDialogFlag=true;
      },
      cancelEmailOrPhoneDialog(){
        this.addEmailOrPhoneDialogFlag=false;
      },
      acceptEmailAndPhone(obj){
        this.form[obj.field]=obj.arrValue;
      },
      cancelDetail(){
        this.showDetailFlag=false;
      },
      //客户弹窗
      addSuperUnit() {
        this.customerDialogFlag = true
      },
      cancelCustomerDialog(){
        this.customerDialogFlag = false
      },
      //接受上级单位
      acceptSuperUnit(row) {
        this.form.addCustName = row.custName;
        this.form.custId = row.id;
        this.form.custManager="";
        this.form.custOrgName="";
        this.detailManagerName(row.id)
      },
      restMange(){
        this.form.custManager="";
      },
      detailManagerName(custId){
        getContractList(custId).then(res => {
          //加载联系人下拉
          this.option.column.find(item=>{
            if (item.dicData){
              item.dicData=[];
              item.dicData=res.data.data;
              if (res.data.data.length==1){
                this.form.custManager=res.data.data[0].id

              }
            }
          })
        })
      },
      handleUpload(){
        this.uploadUserDialogFlag=true;
      },
      cancelUploadUserDialog(){
        this.uploadUserDialogFlag=false;
      },
      handleExport(){
       this.exportDialogFlag=true;
        this.$refs.exportUserDialog.accept(this.query);
      },
      cancelExportDialog(){
        this.exportDialogFlag=false;
      },
      addAccount(){
        this.addAccountUserFlag=true;
      },
      cancelAddAccountUserDialog(){
        this.addAccountUserFlag=false;
      },
      handleAdd(){
        this.$refs.crud.rowAdd();
      },
      handleEdit (row, index) {
        this.$refs.crud.rowEdit(row, index);
      },
    }
  };
</script>

<style>
</style>

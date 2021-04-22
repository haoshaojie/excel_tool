<template>
  <basic-container>
    <avue-crud :option="option"
               :search.sync="search"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-close="beforeClose"
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
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.blackwhitelist_delete"
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
      <template slot-scope="" slot="custCodeSearch">
        <el-input placeholder="请输入客户编码" v-model="search.custCode"></el-input>
      </template>
    </avue-crud>
    <import-dialog ref="importDialog" :showDialog="showDialog" @close="closeImportDialog"></import-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, listcustomer} from "@/api/contract/blackList/blackwhitelist";
  import website from '@/config/website';
  import {getToken} from '@/util/auth';
  import {mapGetters} from "vuex";
  import importDialog from './importdialog'
  const DIC=[{
    label:'全部',
    value:""
  },{
    label:'审批通过',
    value:"审批通过"
  },{
    label:'审批不通过',
    value:"审批不通过"
  },{
    label:'审批中',
    value:"审批中"
  }];
  export default {
    components:{importDialog},
    data() {
      return {
        showDialog:false,
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
        customer:[],
        option: {
          height: 'auto',
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,
          dialogWidth:"35%",
          menuPosition:"center",
          column: [
            {
              label: "交易所",
              prop: "exchange",
              order:140,
              rules: [{
                required: true,
                message: "请输入交易所",
                trigger: "blur"
              }, {
                min: 1,
                max: 50,
                message: '交易所必填，限制50字以内',
                trigger: 'blur'
              }],
              span:24,
              search:true,
              searchOrder:6
            },
            {
              label: "数据编码",
              prop: "dataCode",
              order:130,
              span:24,
              rules: [{
                required: true,
                message: "请输入数据编码",
                trigger: "blur"
              }, {
                min: 1,
                max: 50,
                message: '数据编码必填，限制50字以内',
                trigger: 'blur'
              }],
              search:true,
              searchOrder:5
            },
            {
              label: "类型",
              prop: "type",
              order:120,
              span:24,
              rules: [{
                required: true,
                message: "请选择类型",
                trigger: "blur"
              }],
              type: 'select',
              dicUrl: this.getDicValue(this.CONSTANT.BLACKLIST_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              search:true,
              searchOrder:1,
              editDisabled:true
            },
            {
              label: "客户名称",
              prop: "custName",
              order:110,
              span:24,
              rules: [{
                required: true,
                message: "请输入客户名称",
                trigger: "blur"
              },{
                min: 1,
                max: 50,
                message: '客户名称必填，限制50字以内',
                trigger: 'blur'
               }],
              search:true,
              searchOrder:3,
            },
            {
              label: "客户编码",
              prop: "custCode",
              order:100,
              span:24,
              search:true,
              searchOrder:3,
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
              label: "备注",
              prop: "remark",
              order:90,
              span:24,
              rules: [{
                required: false,
                message: "请输入",
                trigger: "blur"
              },{
                min: 0,
                max: 100,
                message: '备注非必要，限制100个字内',
                trigger: 'blur'
              }],
            },
            {
              label: "审批状态",
              prop: "auditState",
              addDisplay: false,
              editDisplay:false,
              order:20,
              search:true,
              searchOrder:1,
              type:"select",
              dicData:DIC

            },
            {
              label: "创建人",
              prop: "createUserName",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              order:80,
            },
            {
              label: "创建时间",
              prop: "createTime",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              order:70,
            },
            {
              label: "最后更改人",
              prop: "updateUserName",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              order:60,
            },
            {
              label: "最后更改时间",
              prop: "updateTime",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              order:50,
            },
            {
              label: "审核人",
              prop: "auditUser",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              order:40,
            },
            {
              label: "审核时间",
              prop: "auditTime",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              order:30,
            },
            {
              label: "审核意见",
              prop: "auditResult",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              order:10,
            },
            {
              label: "客户表id",
              prop: "custId",
              addDisplay:false,
              editDisplay:false,
              viewDisplay:false,
              hide:true,
              order:10,
            },
          ]
        },
        data: [],
        custCodeList:[]
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.blackwhitelist_add, false),
          viewBtn: this.vaildData(this.permission.blackwhitelist_view, false),
          delBtn: this.vaildData(this.permission.blackwhitelist_delete, false),
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
    watch:{
       'form.custName'() {
          const column = this.findObject(this.option.column, "custCode");
        if (this.form.custName!=''&&this.form.custName!=undefined) {
          listcustomer(1, 20, {custName:this.form.custName}).then(res => {
              this.custCodeList=res.data.data.records.length>0?res.data.data.records:[{custCode:"",custCode:"无数据"}];
              column.dicData=this.custCodeList;
            });
        }
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
      handleUpload(){
        this.showDialog=true;
        this.$refs.importDialog.activeStep=1;
      },
      closeImportDialog(){
        this.showDialog=false;
      },
      handleExport(){
        this.$confirm("是否导出黑白名单数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          let exchange = "", dataCode = "", type="",custName="",custCode="",auditState="";
          if (this.search.exchange){
            exchange = this.search.exchange;
          }
          if (this.search.dataCode){
            dataCode = this.search.dataCode;
          }
          if (this.search.type){
            type = this.search.type;
          }
          if (this.search.custName){
            custName = this.search.custName;
          }
          if (this.search.custCode){
            custCode = this.search.custCode;
          }
          if (this.search.auditState){
            auditState = this.search.auditState;
          }
          window.open(`/api/cnfic-contract-manage/blackwhitelist/export-blackwhitelist?${website.tokenHeader}=${getToken()}&exchange=${exchange}&dataCode=${dataCode}&type=${type}&custName=${custName}&custCode=${custCode}&auditState=${auditState}`);
      });
      },
      beforeClose(done, type){
        this.custCodeList=[{custCode:"",custCode:"无数据"}];
        const column = this.findObject(this.option.column, "custCode");
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
        }else{
          done();
        }
      },
      searchReset() {
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

<style>
</style>


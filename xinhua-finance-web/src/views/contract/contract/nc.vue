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
    <template slot-scope="{}" slot="signDateSearch">
      <el-date-picker
        v-model="signDate"
        type="daterange"
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期">
      </el-date-picker>
    </template>
    <template slot-scope="{}" slot="synTimeSearch">
      <el-date-picker
        v-model="synTime"
        type="datetimerange"
        value-format="yyyy-MM-dd HH:mm:ss"
        format="yyyy-MM-dd HH:mm:ss"
        range-separator="-"
        start-placeholder="开始时间"
        end-placeholder="结束时间">
      </el-date-picker>
    </template>
    <template slot-scope="scope" slot="menu">
      <el-button type="text" size="mini"  @click.stop="handleEdit(scope.row,scope.index)">生成合约</el-button>
    </template>
    <template slot="conNcNo" slot-scope="scope">
      <el-button type="text"
                  size="mini"
                  v-if="permission.nc_view"
                  class="conNcNoClass"
                  @click.stop="handleView(scope.row)">{{scope.row.conNcNo}}
      </el-button>
    </template>
    </avue-crud>
    <nc-contract-detail-dialog ref="ncContractDetailDialog" :showDetail="showDetail"></nc-contract-detail-dialog>
  </basic-container>
</template>

<script>
  import {getPage,getDetail, add, update, remove} from "@/api/contract/contract/nc";
  import {mapGetters} from "vuex";
  import ncContractDetailDialog from "./components/ncContractDetailDialog";
  export default {
    components:{
      ncContractDetailDialog
    },
    data() {
      return {
        showDetail:false,
        form: {},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        signDate:[],
        synTime:[],
        selectionList: [],
        option: {
          height: 'auto',
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: false,
          delBtn:false,
          editBtn:false,
          selection: true,
          excelBtnText:"同步合同",
          excelBtn:true,
          addBtn:false,
          column: [
            {
              label: "主键id 雪花算法产生",
              prop: "id",
              hide: true,
              display: false, //在查看，新增，编辑页面是否显示
              rules: [{
                required: true,
                message: "请输入主键id 雪花算法产生",
                trigger: "blur"
              }]
            },
            {
              label: "财务组织",
              prop: "financialOrg",
              hide: true,
              rules: [{
                required: true,
                message: "请输入财务组织",
                trigger: "blur"
              }]
            },
            {
              label: "项目名称",
              prop: "projectName",
              hide: true,
              rules: [{
                required: true,
                message: "请输入项目名称",
                trigger: "blur"
              }]
            },
            {
              label: "合同编号",
              prop: "conNcNo",
              search:true,
              order:5,
              rules: [{
                required: true,
                message: "请输入合同编号",
                trigger: "blur"
              }],
              slot:true
            },
            {
              label: "合同名称",
              prop: "conName",
        search:true,
        order:1,
              rules: [{
                required: true,
                message: "请输入合同名称",
                trigger: "blur"
              }]
            },
            {
              label: "客户名称",
              prop: "custName",
        search:true,
        order:2,
              rules: [{
                required: true,
                message: "请输入客户名称",
                trigger: "blur"
              }]
            },
      {
        label: "签约部门",
        prop: "signDept",
        search:true,
        order:3,
        rules: [{
          required: true,
          message: "请输入签约部门",
          trigger: "blur"
        }]
      },
      {
        label: "销售人员",
        prop: "salesman",
        search:true,
        order:4,
        rules: [{
          required: true,
          message: "请输入销售人员",
          trigger: "blur"
        }]
      },
      {
        label: "合同金额(元)",
        prop: "conAmount",
        rules: [{
          required: true,
          message: "请输入合同金额,单位元",
          trigger: "blur"
        }]
      },
      {
        label: "合同签约类型",
        prop: "signType",
        rules: [{
          required: true,
          message: "请输入合同签约类型",
          trigger: "blur"
        }]
      },
      {
        label: "签约日期",
        prop: "signDate",
        searchslot:true,
        search:true,
        order:6,
        rules: [{
          required: true,
          message: "请输入签约日期",
          trigger: "blur"
        }]
      },
      {
        label: "同步时间",
        prop: "synTime",
        searchslot:true,
        search:true,
        order:7,
        rules: [{
          required: true,
          message: "请输入同步时间",
          trigger: "blur"
        }]
      },
            {
              label: "合同起始日期年月日时分秒",
              prop: "conStartTime",
              hide: true,
              rules: [{
                required: true,
                message: "请输入合同起始日期年月日时分秒",
                trigger: "blur"
              }]
            },
            {
              label: "合同终止日期年月日时分秒",
              prop: "conEndTime",
              hide: true,
              rules: [{
                required: true,
                message: "请输入合同终止日期年月日时分秒",
                trigger: "blur"
              }]
            },
            {
              label: "客户性质一",
              prop: "custCharacter1",
              hide: true,
              rules: [{
                required: true,
                message: "请输入客户性质一",
                trigger: "blur"
              }]
            },
            {
              label: "客户性质二",
              prop: "custCharacter2",
              hide: true,
              rules: [{
                required: true,
                message: "请输入客户性质二",
                trigger: "blur"
              }]
            },
            {
              label: "客户性质三",
              prop: "custCharacter3",
              hide: true,
              rules: [{
                required: true,
                message: "请输入客户性质三",
                trigger: "blur"
              }]
            },
            {
              label: "签约主体",
              prop: "contractParty",
              hide: true,
              rules: [{
                required: true,
                message: "请输入签约主体",
                trigger: "blur"
              }]
            },
            {
              label: "实际服务客户",
              prop: "actualServiceCust",
              hide: true,
              rules: [{
                required: true,
                message: "请输入实际服务客户",
                trigger: "blur"
              }]
            },
            {
              label: "销售人员电话",
              prop: "salesmanPhone",
              hide: true,
              rules: [{
                required: true,
                message: "请输入销售人员电话",
                trigger: "blur"
              }]
            },
            {
              label: "合同完结状态",
              prop: "contFinishState",
              hide: true,
              rules: [{
                required: true,
                message: "请输入合同完结状态",
                trigger: "blur"
              }]
            },
            {
              label: "具体服务要求",
              prop: "serviceRequest",
              hide: true,
              rules: [{
                required: true,
                message: "请输入具体服务要求",
                trigger: "blur"
              }]
            },
            {
              label: "合同完结说明",
              prop: "finishDesc",
              hide: true,
              rules: [{
                required: true,
                message: "请输入合同完结说明",
                trigger: "blur"
              }]
            },
            {
              label: "确认收入方式",
              prop: "revenueMethod",
              hide: true,
              rules: [{
                required: true,
                message: "请输入确认收入方式",
                trigger: "blur"
              }]
            },
            {
              label: "派工开始时间",
              prop: "workStartTime",
              hide: true,
              rules: [{
                required: true,
                message: "请输入派工开始时间",
                trigger: "blur"
              }]
            },
            {
              label: "派工结束时间",
              prop: "workEndTime",
              hide: true,
              rules: [{
                required: true,
                message: "请输入派工结束时间",
                trigger: "blur"
              }]
            },
            {
              label: "生效日期",
              prop: "effectiveDate",
              hide: true,
              rules: [{
                required: true,
                message: "请输入生效日期",
                trigger: "blur"
              }]
            },
            {
              label: "第三方机构性质",
              prop: "thirdPartyNature",
              hide: true,
              rules: [{
                required: true,
                message: "请输入第三方机构性质",
                trigger: "blur"
              }]
            },
            {
              label: "第三方客户",
              prop: "thirdPartyCust",
              hide: true,
              rules: [{
                required: true,
                message: "请输入第三方客户",
                trigger: "blur"
              }]
            },
            {
              label: "同步状态：1：已同步2：未同步",
              prop: "syncState",
              hide: true,
              rules: [{
                required: true,
                message: "请输入同步状态：1：已同步2：未同步",
                trigger: "blur"
              }]
            },
            {
              label: "是否已创建合约：0：否,1： 是",
              prop: "isCreate",
              hide: true,
              rules: [{
                required: true,
                message: "请输入是否已创建合约：0：否,1： 是",
                trigger: "blur"
              }]
            },
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.nc_add, false),
          viewBtn: this.vaildData(this.permission.nc_view, false),
          delBtn: this.vaildData(this.permission.nc_delete, false),
          editBtn: this.vaildData(this.permission.nc_edit, false)
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
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
          });
        }
        done();
      },
      searchReset() {
        this.query = {};
		this.signDate='';
		this.synTime='';
        this.onLoad(this.page);
      },
    searchChange(params, done) {
        this.query = params;
        if(this.signDate){
          this.query.signDateStartTime = this.signDate[0];
          this.query.signDateEndTime = this.signDate[1];
        }
        if(this.synTime){ 
          this.query.synTimeStartTime = this.synTime[0];
          this.query.synTimeEndTime = this.synTime[1];
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
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      handleView(row) {
        this.showDetail=true;
        this.$refs.ncContractDetailDialog.openDialog(row);
        // this.$refs.crud.rowView(row, index);
      },
      onLoad(page, params = {}) {
        this.loading = true;
        getPage(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
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

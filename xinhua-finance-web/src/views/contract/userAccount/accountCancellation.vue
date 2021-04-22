<!--账号注销-->
<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page.sync="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small" @click.native="showCancellationDialog(scope.row)">注销
        </el-button>
      </template>
      <template slot-scope="{disabled,size}" slot="deleteTimeSearch">
        <el-date-picker
          v-model="applyTimeArr"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          format="yyyy-MM-dd HH:mm:ss"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </template>
    </avue-crud>
    <el-dialog title="账号注销" center :visible.sync="cancellationDialogVisible">
      <p>确认注销该账号吗？</p>
      <p>账号注销后，账号绑定的手机号、邮箱、产品等信息将被释放，账号无法登录！</p>
      <p>用户未到期产品：</p>
      <avue-crud :data="productOfUnexpiredData" :option="productOfUnexpiredOption"></avue-crud>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleCancellation">确 定</el-button>
        <el-button @click="cancellationDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </basic-container>
</template>

<script>
import {getList, getProductOfUnexpired, logoff} from "@/api/contract/userAccount/accountCancellation";
import {mapGetters} from "vuex";

export default {
  data() {
    return {
      cancellationAccId: "",
      cancellationDialogVisible: false,
      productOfUnexpiredOption: {
        height: 'auto',
        calcHeight: 210,
        tip: false,
        border: true,
        index: false,
        menu: false,
        header: false,
        align: 'center',
        column: [
          {
            label: "订购产品",
            prop: "product",
          },
          {
            label: "到期时间",
            prop: "expires",
          }
        ]
      },
      productOfUnexpiredData: [],
      form: {},
      query: {},
      loading: true,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      applyTimeArr: [],
      option: {
        height: 'auto',
        calcHeight: 210,
        searchShow: true,
        searchMenuSpan: 6,
        tip: false,
        border: true,
        index: false,
        viewBtn: false,
        addBtn: false,//添加按钮
        editBtn: false,//编辑按钮
        delBtn: false,//删除按钮
        column: [
          {
            label: "账号ID",
            prop: "accId",
            hide: true,
          },
          {
            label: "用户账号",
            prop: "userAccount",
          },
          {
            label: "手机号码",
            prop: "userPhone",
            search: true,
            searchOrder: 3,
          },
          {
            label: "邮箱",
            prop: "userEmail",
            search: true,
            searchOrder: 2,
          },
          {
            label: "姓名",
            prop: "userName",
          },
          {
            label: "注册渠道",
            prop: "regAppChannelType",
            type: "select",
            dicUrl: this.getDicValue(this.CONSTANT.REGAPP_CHANNEL_TYPE),
            props: {
              label: "dictValue",
              value: "dictKey"
            },
          },
          {
            label: "申请时间",
            prop: "deleteTime",
            searchslot: true,
            search: true,
            searchOrder: 1,
          }
        ]
      },
      data: []
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(this.permission.personaluser_add, false),
        viewBtn: this.vaildData(this.permission.personaluser_view, false),
        delBtn: this.vaildData(this.permission.personaluser_delete, false),
        editBtn: this.vaildData(this.permission.personaluser_edit, false)
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
    queryUnexpiredProduct() {
      //查询未到期产品列表
    },
    showCancellationDialog(row) {
      this.cancellationAccId = row.accId;
      this.queryUnexpiredProduct();
      this.cancellationDialogVisible = true;
    },
    handleCancellation() {
      logoff(this.cancellationAccId).then(() => {
        this.cancellationDialogVisible = false;
        this.onLoad(this.page);
        this.$message({
          type: "success",
          message: "操作成功!"
        });
      }, error => {
        window.console.log(error);
        this.cancellationDialogVisible = false;
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
      this.applyTimeArr = [];
      this.onLoad(this.page);
    },
    searchChange(params, done) {
      this.query = params;
      if (this.applyTimeArr) {
        this.query.startDeleteTime = this.applyTimeArr[0];
        this.query.endDeleteTime = this.applyTimeArr[1];
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
    }
  }
};
</script>

<style>
</style>

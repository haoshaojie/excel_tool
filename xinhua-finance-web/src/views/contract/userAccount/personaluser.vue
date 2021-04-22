<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :search.sync="search"
               :data="data"
               :page.sync="page"
               :permission="permissionList"
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
      <template slot="menuLeft">
        <el-button type="primary"
                   size="small"
                   icon="el-icon-download"
                   plain
                   v-if=""
                   @click="handleExport">导 出
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small" @click.native="handleLockOrUnlock(scope.row)">
          {{scope.row.accStatus==1 ? "锁定":""}}
          {{scope.row.accStatus==2 ? "解锁":""}}
        </el-button>
        <el-button type="text" size="small" @click.native="resetPassword(scope.row)">重置密码</el-button>
        <el-button type="text" size="small" @click.native="showAccountDetail(scope.row)">详情</el-button>
      </template>
      <template slot-scope="{disabled,size}" slot="createTimeSearch">
        <el-date-picker
          v-model="createTimeArr"
          type="daterange"
          value-format="yyyy-MM-dd"
          format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </template>
      <template slot-scope="{disabled,size}" slot="updateTimeSearch">
        <el-date-picker
          v-model="updateTimeArr"
          type="daterange"
          value-format="yyyy-MM-dd"
          format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </template>
    </avue-crud>
    <user-detail-dialog ref="userDetaildialog" type="personal"
                        :showDetail="showDetail"
                        :personal-account-detail="accPersonalDetail"
                        @close="closeDetailDialog"/>
    <reset-password-dialog ref="resetPasswordDialog"
                           :showResetPasswordDialog="showResetPasswordDialog"
                           :has-email="hasEmail"
                           :has-phone="hasPhone"
                           :account-id="accountId"
                           @close="closeResetPasswordDialog"/>
    <account-export-dialog ref="resetPasswordDialog"
                           type="personal"
                           :exportDialogFlag="showExportDialog"
                           :exportColumns="exportColumns"
                           @close="closeExportDialog"/>
  </basic-container>
</template>

<script>
import {getList, getPersonalDetail} from "@/api/contract/userAccount/personaluser";
import {resetLock, update} from "@/api/contract/userAccount/account";
import {mapGetters} from "vuex";
import userDetailDialog from "./userDetailDialog";
import resetPasswordDialog from './resetPasswordDialog';
import accountExportDialog from './accountExportDialog';

export default {
  components: {
    userDetailDialog,
    resetPasswordDialog,
    accountExportDialog
  },
  data() {
    return {
      showExportDialog: false,
      exportColumns: [],
      showDetail: false,
      accPersonalDetail: {},
      showResetPasswordDialog: false,
      accountId: "",
      hasPhone: false,
      hasEmail: false,
      form: {},
      query: {},
      loading: true,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      createTimeArr: [],
      updateTimeArr: [],
      option: {
        height: 'auto',
        calcHeight: 210,
        searchShow: true,
        searchMenuSpan: 6,
        tip: false,
        border: true,
        index: false,
        viewBtn: false,
        selection: true,
        addBtn: false,
        editBtn: true,
        delBtn: false,
        column: [
          {
            label: "账号ID",
            prop: "accId",
            hide: true,
            addDisplay: false,
            editDisplay: false,
          },
          {
            label: "手机号码",
            prop: "userPhone",
            search: true,
            rules: [{
              required: true,
              message: "请输入手机号",
              trigger: "blur"
            }]
          },
          {
            label: "邮箱",
            prop: "userEmail",
            search: true,
            rules: [{
              required: true,
              message: "请输入邮箱",
              trigger: "blur"
            }]
          },
          {
            label: "姓名",
            prop: "userName",
            editDisabled: true,
          },
          {
            label: "注册渠道",
            prop: "regAppChannelType",
            search: true,
            type: "select",
            dicUrl: this.getDicValue(this.CONSTANT.REGAPP_CHANNEL_TYPE),
            props: {
              label: "dictValue",
              value: "dictKey"
            },
            order: 6,
            editDisabled: true,
          },
          {
            label: "注册时间",
            prop: "createTime",
            searchslot: true,
            search: true,
            editDisabled: true,
          },
          {
            label: "账号状态",
            prop: "accStatus",
            search: true,
            type: "select",
            dicUrl: this.getDicValue(this.CONSTANT.ACC_STATUS),
            props: {
              label: "dictValue",
              value: "dictKey"
            },
            order: 6,
            editDisabled: true,
            dataType: 'number'
          },
          {
            label: "修改时间",
            prop: "updateTime",
            searchslot: true,
            search: true,
            display: false,
            hide: true
          },
        ]
      },
      data: [{
        "userNickname": "XHCJ0001",
        "email": "18410379896",
        "phone": "18410379896",
        nickName: "",
        userName: "",
        customerName: "",
        userState: "",
        accountState: "",
        createTime: "",
        updateTime: ""
      }]
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
  created() {
    this.option.column.forEach(item => {
      if (!item.hide) {
        this.exportColumns.push({
          label: item.label,
          prop: item.prop,
        })
      }
    })
  },
  methods: {
    queryPersonalDetail(accId) {
      if (accId && accId.length > 0) {
        getPersonalDetail(accId).then(res => {
          this.accPersonalDetail = res.data.data;
          this.showDetail = true;
        }, error => {
          this.showDetail = false;
          window.console.log(error);
        });
      }
    },
    showAccountDetail(row) {
      this.queryPersonalDetail(row.accId)
    },
    closeDetailDialog() {
      this.showDetail = false;
    },
    closeExportDialog() {
      this.showExportDialog = false;
      this.exportColumns = [];
    },
    handleLockOrUnlock(row) {
      let message = "", flag = false;
      if (row.accStatus == 1) {
        message = "确认锁定该账号吗？";
        flag = true;
      } else if (row.accStatus == 2) {
        message = "确认解锁该账号吗";
        flag = false;
      } else {
        return
      }
      this.$confirm(message, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          return resetLock({
            flag: flag,
            accId: row.accId,
          });
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        });
    },
    resetPassword(row) {
      this.showResetPasswordDialog = true;
      if (typeof row.userPhone == 'string' && row.userPhone.length > 0) {
        this.hasPhone = true;
      }
      if (typeof row.userEmail == 'string' && row.userEmail.length > 0) {
        this.hasEmail = true;
      }
      this.accountId = row.accId;
    },
    closeResetPasswordDialog() {
      this.showResetPasswordDialog = false;
      this.accountId = null;
      this.hasPhone = false;
      this.hasEmail = false;
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
    handleExport() {
      this.showExportDialog = true;
    },
    beforeOpen(done, type) {
      if (["edit", "view"].includes(type)) {
        console.log("detailForm", this.form)
        getPersonalDetail(this.form.accId).then(res => {
          done();
          this.form = res.data.data;
        }, error => {
          window.console.log(error);
          this.onLoad(this.page);
        });
      } else {
        done();
      }
    },
    searchReset() {
      this.query = {};
      this.createTimeArr = [];
      this.updateTimeArr = [];
      this.onLoad(this.page);
    },
    searchChange(params, done) {
      this.query = params;
      if (this.createTimeArr) {
        this.query.startRegistTime = this.createTimeArr[0];
        this.query.endRegistTime = this.createTimeArr[1];
      }
      if (this.updateTimeArr) {
        this.query.startUpdateTime = this.updateTimeArr[0];
        this.query.endUpdateTime = this.updateTimeArr[1];
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

<template>
  <basic-container>
    <avue-tabs :option="optionTab" @change="handleChange"></avue-tabs>
    <div>
      <avue-crud :option="option"
                 :table-loading="loading"
                 :data="data"
                 :page.sync="page"
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
        <template slot-scope="{row}" slot="userAccount">
          <el-button
            type="text"
            size="mini"
            @click="detailDialog(row)">
            {{row.userAccount}}
          </el-button>
        </template>
        <template slot="menuLeft">
          <div v-if="this.type.prop=='userAccount'">
            <el-button type="primary"
                       size="small"
                       plain
                       v-if=""
                       @click="welcomeEmail(null)">欢迎邮件
            </el-button>
            <el-button type="primary"
                       size="small"
                       plain
                       v-if=""
                       @click="mailRecord">邮件记录
            </el-button>
            <el-button type="primary"
                       size="small"
                       plain
                       v-if=""
                       @click="mailTemlate">邮件模板
            </el-button>
            <el-button type="primary"
                       size="small"
                       plain
                       v-if=""
                       @click="enableBtn">启用
            </el-button>
            <el-button type="warning"
                       size="small"
                       plain
                       v-if=""
                       @click="disableBtn">禁用
            </el-button>
            <el-button type="primary"
                       size="small"
                       plain
                       v-if=""
                       @click="handleExport">导 出
            </el-button>
          </div>
          <div v-if="this.type.prop!='userAccount'">
            <el-button type="primary"
                       size="small"
                       plain
                       v-if=""
                       @click="commonAccountAdd">新 增
            </el-button>
            <el-button type="danger"
                       size="small"
                       class="el-icon-delete"
                       plain
                       v-if=""
                       @click="commonAccountDel">删 除
            </el-button>
          </div>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button type="text" size="small" v-if="type.prop!=='userAccount'"
                     @click.native="handleUpdate(scope.row)">编辑
          </el-button>
          <div v-if="type.prop=='userAccount'">
            <el-button type="text" size="small"
                       @click.native="handleLockOrUnlock(scope.row)">
              {{scope.row.accountState==1 ? "锁定" : "解锁"}}
            </el-button>
            <el-button type="text" size="small"
                       @click.native="resetPassword(scope.row)">重置密码
            </el-button>
            <el-button type="text" size="small"
                       @click.native="welcomeEmail(scope.row)">欢迎邮件
            </el-button>
          </div>
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
    </div>
    <user-detail-dialog ref="userDetaildialog"
                        :showDetail="showUserDetail"
                        type="enterprise"
                        @close="closeDetailDialog"/>
    <public-detail-dialog ref="publicDetailDialog"
                          :showPublicDetailDialog="showPublicDetail"
                          @close="closeDetailDialog"/>
    <public-form-dialog ref="publicFormDialog"
                        :publicFormTitle="publicFormTitle"
                        :showPublicFormDialog="showPublicForm"
                        :publicFormData="publicFormData"
                        @close="closeFormDialog"/>
    <resetPassword-dialog ref="resetPasswordDialog"
                          :showResetPasswordDialog="showResetPasswordDialog"
                          :has-email="hasEmail"
                          :has-phone="hasPhone"
                          :account-id="accountId"
                          @close="closeResetPasswordDialog"/>
    <reset-password-dialog ref="resetPasswordDialog"
                           :showResetPasswordDialog="showResetPasswordDialog"
                           :has-email="hasEmail"
                           :has-phone="hasPhone"
                           :account-id="accountId"
                           @close="closeResetPasswordDialog"/>
    <account-export-dialog ref="resetPasswordDialog"
                           type="enterprise"
                           :exportDialogFlag="showExportDialog"
                           :exportColumns="exportColumns"
                           @close="closeExportDialog"/>
  </basic-container>
</template>

<script>
import {listPublic, listUser} from "@/api/contract/userAccount/enterpriseAccount";
import {resetLock, update} from "@/api/contract/userAccount/account";
import {sendTemplateMail} from "@/api/contract/userAccount/mailRecord";
import {mapGetters} from "vuex";
import userDetailDialog from "./userDetailDialog";
import publicDetailDialog from "./publicDetailDialog";
import publicFormDialog from "./publicFormDialog";
import resetPasswordDialog from './resetPasswordDialog';
import accountExportDialog from './accountExportDialog';

const TEMPLATE_FILE = {
  welcome: "Welcome.ftl",
  resetPassword: "ResetPassword.ftl",
};
export default {
  components: {userDetailDialog, publicDetailDialog, publicFormDialog, resetPasswordDialog, accountExportDialog},
  data() {
    return {
      showExportDialog: false,
      exportColumns: [],
      showResetPasswordDialog: false,
      accountId: "",
      hasPhone: false,
      hasEmail: false,
      showUserDetail: false,
      showPublicDetail: false,
      showPublicForm: false,
      publicFormTitle: '',
      publicFormData: {},
      form: {},
      query: {},
      loading: false,
      createTimeArr: [],
      updateTimeArr: [],
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      type: {},
      optionTab: {
        column: [{
          label: '用户账号',
          prop: 'userAccount',
        }, {
          label: '公共账号',
          prop: 'commonAccount',
        }],
      },
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
        addBtn: false,//添加按钮
        viewBtn: false,//添加按钮
        delBtn: false,//添加按钮
        editBtn: true,//添加按钮
        column: [{
          label: '用户账号',
          prop: 'userAccount',
          search: true,
          slot: true,
          editDisabled: true,
        }, {
          label: '邮箱',
          prop: 'email',
          search: true
        }, {
          label: '手机号码',
          prop: 'phone',
          search: true
        }, {
          label: '姓名',
          prop: 'userName',
          search: true,
          editDisabled: true,
        }, {
          label: '客户名称',
          prop: 'customerName',
          search: true,
          editDisabled: true,
        }, {
          label: '用户状态',
          prop: 'userState',
          search: true
        }, {
          label: '账号状态',
          prop: 'accountState',
          search: true,
          editDisabled: true,
        }, {
          label: '创建时间',
          prop: 'createTime',
          searchslot: true,
          search: true,
          editDisabled: true,
        }, {
          label: '最后修改时间',
          prop: 'updateTime',
          searchslot: true,
          search: true,
          editDisabled: true,
        }],
      },
      userAccountColumn: [
        {
          label: "账号ID",
          prop: "accId",
          hide: true,
          addDisplay: false,
          editDisplay: false,
        }, {
          label: '用户账号',
          prop: 'userAccount',
          search: true,
          slot: true,
          editDisabled: true,
        }, {
          label: '邮箱',
          prop: 'userEmail',
          search: true
        }, {
          label: '手机号码',
          prop: 'userPhone',
          search: true
        }, {
          label: '姓名',
          prop: 'userName',
          search: true,
          editDisabled: true,
        }, {
          label: '客户名称',
          prop: 'customerName',
          search: true,
          editDisabled: true,
        }, {
          label: '用户状态',
          prop: 'userState',
          search: true
        }, {
          label: '账号状态',
          prop: 'accountState',
          search: true,
          editDisabled: true,
          editDisplay: false,
          dicUrl: this.getDicValue(this.CONSTANT.ACC_STATUS),
          props: {
            label: "dictValue",
            value: "dictKey"
          },
          dataType: 'number'
        }, {
          label: '创建时间',
          prop: 'createTime',
          searchslot: true,
          search: true,
          editDisabled: true,
          editDisplay: false,
        }, {
          label: '最后修改时间',
          prop: 'updateTime',
          searchslot: true,
          search: true,
          editDisabled: true,
          editDisplay: false,
        }],
      publicAccountColumn: [{
        label: 'IP',
        prop: 'ip',
        hide: true,
        search: true,
      }, {
        label: '客户名称',
        prop: 'ip',
        hide: true,
        search: true,
        editDisabled: true,
      }, {
        label: '用户账号',
        prop: 'userAccount',
        slot: true,
        editDisabled: true,
      }, {
        label: '并发',
        prop: 'concurrent',
      }, {
        label: '所属客户',
        prop: 'customers',
      }, {
        label: '联系人',
        prop: 'contacts',
        search: true,
      }, {
        label: '联系人手机号码',
        prop: 'contactsTel',
      }, {
        label: '联系人邮箱',
        prop: 'contactsEmail',
      }, {
        label: '创建时间',
        prop: 'createTime',
        searchslot: true,
        search: true,
      }, {
        label: '最后修改时间',
        prop: 'updateTime',
        searchslot: true,
        search: true,
      }],
      data: [
        {
          userAccount: "XHCJ0001",
          userEmail: "18410379896",
          userPhone: "18410379896",
          nickName: "",
          userName: "",
          customerName: "",
          userState: "",
          accountState: "",
          createTime: "",
          updateTime: ""
        },
        {
          userAccount: "XHCJ0001",
          userEmail: "18410379896",
          userPhone: "",
          nickName: "",
          userName: "",
          customerName: "",
          userState: "",
          accountState: "",
          createTime: "",
          updateTime: ""
        }
      ],
      templateMailParam: {
        tempFile: "",
        receivers: [],
      },
    }
  },
  created() {
    this.type = this.optionTab.column[0];
    this.option.column.forEach(item => {
      if (!item.hide) {
        this.exportColumns.push({
          label: item.label,
          prop: item.prop,
        })
      }
    });
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
    closeExportDialog() {
      this.showExportDialog = false;
      this.exportColumns = [];
    },
    closeFormDialog() {
      this.publicFormData = null;
      this.showPublicForm = false;
    },
    handleUpdate(row) {
      this.publicFormTitle = "编辑公共账号";
      this.publicFormData = row;
      this.showPublicForm = true;
      console.log("formEdit", "=========================")
      console.log("publicFormData", this.publicFormData)
    },
    handleLockOrUnlock(row) {
      let message = row.accountState == 1 ? "确认锁定该账号吗？" : "确认解锁该账号吗"
      this.$confirm(message, {
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
    detailDialog(row) {
      if (this.type.prop == 'userAccount') {
        this.showUserDetail = true;
      } else {
        this.showPublicDetail = true;
      }
    },
    closeDetailDialog() {
      if (this.type.prop == 'userAccount') {
        this.showUserDetail = false;
      } else {
        this.showPublicDetail = false;
      }
    },
    handleChange(column) {
      this.type = column;
      if (this.type.prop == 'userAccount') {
        this.option.editBtn = true;
      } else {
        this.option.editBtn = false;
      }
      this.onLoad(this.page);
    },
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
      this.$refs.crud.rowUpdate();
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
    //欢迎邮件
    welcomeEmail(row) {
      let msg = "是否发送欢迎邮件？";
      this.templateMailParam.tempFile = TEMPLATE_FILE.welcome;
      this.templateMailParam.receivers = [];
      if (row) {
        this.templateMailParam.receivers.push({
          accId: row.accId,
          email: row.userEmail,
          account: row.accId,
          accountExpire: row.accountExpire,
          initPassword: row.initPassword,
          userName: row.userName,
          productLink: row.productLink,
          orderNum: row.orderNum,
        })
      } else {
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        msg = "是否批量发送欢迎邮件？";
        this.templateMailParam.receivers = this.selectionList.map(item => {
          return {
            accId: item.accId,
            email: item.userEmail,
            account: item.accId,
            accountExpire: item.accountExpire,
            initPassword: item.initPassword,
            userName: item.userName,
            productLink: item.productLink,
            orderNum: item.orderNum,
          };
        })
      }
      this.$confirm(msg, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //执行发送欢迎邮件逻辑
          return sendTemplateMail(this.templateMailParam);
        })
        .then(() => {
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.$refs.crud.toggleSelection();
        });
    },
    //邮件记录
    mailRecord() {
      this.$router.push({path: '/contract/userAccount/mailRecord'});
    },
    closeDialog(val) {
      console.log(val);
    },
    //邮件模板
    mailTemlate() {
      this.$router.push({path: '/contract/userAccount/mailTemplate'});
    },
    //启用按钮
    enableBtn() {
      this.openDialog = true;
    },
    //停用
    disableBtn() {
      this.openDialog = true;
    },
    //导出
    handleExport() {
      this.showExportDialog = true;
    },
    //公共账号新增
    commonAccountAdd() {
      this.publicFormTitle = "新增公共账号";
      this.publicFormData = null;
      this.showPublicForm = true;
      console.log("formAdd", "=========================")
    },
    //公共账号删除
    commonAccountDel() {
      this.$refs.crud.rowDel();
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
        if (this.type.prop == 'userAccount') {

        } else {

        }
      }
      done();
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
      // this.$refs.crud.toggleSelection();
    },
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
    },
    onLoad(page, params = {}) {
      //this.loading = true;
      if (this.type.prop == 'userAccount') {
        this.option.column = this.userAccountColumn;
        // listUser(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
        //   this.option.column = this.userAccountColumn;
        //   const data = res.data.data;
        //   this.page.total = data.total;
        //   this.data = data.records;
        //   this.loading = false;
        //   this.selectionClear();
        // });
      } else {
        this.option.column = this.publicAccountColumn;
        // listPublic(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
        //   this.option.column = this.publicAccountColumn;
        //   const data = res.data.data;
        //   this.page.total = data.total;
        //   this.data = data.records;
        //   this.loading = false;
        //   this.selectionClear();
        // });
      }
    }
  }
};
</script>

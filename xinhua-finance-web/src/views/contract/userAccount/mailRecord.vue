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
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small" v-if="scope.row.mailSendState == 2"
                   @click.native="sendMailAgain(scope.row)">重新发送
        </el-button>
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
    </avue-crud>
  </basic-container>
</template>

<script>
import {add, getDetail, getList, remove, sendTemplateMail, update} from "@/api/contract/userAccount/mailRecord";
import {mapGetters} from "vuex";

export default {
  data() {
    return {
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
      option: {
        height: 'auto',
        calcHeight: 210,
        searchShow: true,
        searchMenuSpan: 6,
        tip: false,
        border: true,
        index: false,
        viewBtn: false,
        addBtn: false,
        delBtn: false,
        editBtn: false,
        column: [
          {
            label: "主键id 雪花算法产生",
            prop: "id",
            hide: true,
            display: false, //在查看，新增，编辑页面是否显示
          },
          {
            label: "用户账号",
            prop: "accountId",
            search: true
          },
          {
            label: "姓名",
            prop: "userName"
          },
          {
            label: "邮箱",
            prop: "email",
            search: true
          },
          {
            label: "邮件主题",
            prop: "mailSubject"
          },
          {
            label: "发件人",
            prop: "mailSendEmail"
          },
          {
            label: "发送结果",
            prop: "mailSendState",
            search: true,
            type: "select",
            dicUrl: this.getDicValue(this.CONSTANT.MAIL_SEND_STATE),
            props: {
              label: "dictValue",
              value: "dictKey"
            },
          },
          {
            label: "发送时间",
            prop: "createTime",
            searchslot: true,
            search: true,
          }
        ]
      },
      data: [],
      templateMailParam: {
        tempFile: "",
        receivers: [],
      },
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
    sendMailAgain(row) {
      if (typeof row.mailTempFile == 'string' && row.mailTempFile.length > 0) {
        this.templateMailParam.tempFile = row.mailTempFile;
      } else {
        return;
      }
      this.$confirm("是否重新发送邮件？", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        //构建参数
        this.templateMailParam.receivers.push({
          accId: row.accountId,
          email: row.email,
          account: row.accountId,
          accountExpire: row.accountExpire,
          initPassword: row.initPassword,
          userName: row.userName,
          productLink: row.productLink,
          orderNum: row.orderNum,
        })
        //执行发送欢迎邮件逻辑
        return sendTemplateMail(this.templateMailParam);
      })
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

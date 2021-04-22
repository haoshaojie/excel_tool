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
      <template slot="menuLeft">
        <el-button type="primary"
                   size="small"
                   icon="el-icon-plus"
                   plain
                   v-if="">新 增
        </el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="">删 除
        </el-button>
        <el-button type="primary"
                   size="small"
                   icon="el-icon-link"
                   plain
                   v-if=""
                   @click="productLinkBtn">产品链接
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small"
                   @click.native="handleEnOrDisable(scope.row)">
          {{scope.row.accountState==1 ? "启用" : "停用"}}
        </el-button>
      </template>
      <template slot-scope="{disabled,size}" slot="registTimeSearch">
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
  </basic-container>
</template>

<script>
import {add, getDetail, getList, remove, update} from "@/api/contract/userAccount/mailTemplate";
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
        delBtn: false,
        column: [
          {
            label: "主键id 雪花算法产生",
            prop: "id",
            hide: true,
            display: false, //在查看，新增，编辑页面是否显示
          },
          {
            label: "发件人地址",
            labelWidth: 100,
            prop: "sendUserAddr",
            showWordLimit: true,
            hide: true,
            display: true, //在查看，新增，编辑页面是否显示
            rules: [
              {required: true, message: "请输入发件人地址", trigger: "blur"},
              {min: 1, max: 128, message: '长度在 1 到 30 个字符', trigger: 'blur'}
            ],
            required: true,
            span: 12
          },
          {
            label: "邮箱密码",
            type: 'password',
            prop: "emailPassword",
            showWordLimit: true,
            hide: true,
            display: true, //在查看，新增，编辑页面是否显示
            span: 12,
            required: true,
            rules: [
              {
                required: true,
                message: '邮箱密码必须填写'
              }
            ]
          },
          {
            label: "模板编号",
            prop: "tempCode",
            display: false, //在查看，新增，编辑页面是否显示
          },
          {
            label: "邮件主题",
            labelWidth: 100,
            prop: "mailSubject",
            search: true,
            showWordLimit: true,
            hide: false,
            display: true, //在查看，新增，编辑页面是否显示
            span: 24,
            required: true,
            rules: [
              {
                required: true,
                message: '邮箱主题必须填写'
              }
            ]
          },
          {
            type: 'textarea',
            label: '邮箱模板',
            labelWidth: 100,
            span: 24,
            hide: true,
            display: true,
            prop: 'tempContent',
            maxlength: 5000,
            showWordLimit: true,
            required: true,
            rules: [
              {
                required: true,
                message: '邮箱模板必须填写'
              }
            ]
          },
          {
            label: "最后修改人",
            prop: "updateUser",
            display: false,
          },
          {
            label: "最后修改时间",
            prop: "updateUser",
            display: false,
          },
          {
            label: "模板状态",
            prop: "tempState",
            type: "select",
            display: false,
            dicUrl: this.getDicValue(this.CONSTANT.MAIL_TEMP_STATE),
            props: {
              label: "dictValue",
              value: "dictKey"
            },
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
    handleEnOrDisable(row) {
      row.tempState = row.tempState == 1 ? 2 : 1;
      update(row).then(() => {
        this.onLoad(this.page);
        this.$message({
          type: "success",
          message: "操作成功!"
        });
      }, error => {
        window.console.log(error);
      });
    },
    productLinkBtn() {
      this.$router.push({path: '/contract/userAccount/productLink'});
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
      // .then(() => {
      //   return remove(this.ids);
      // })
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

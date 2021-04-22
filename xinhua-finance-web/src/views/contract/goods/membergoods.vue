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
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.membergoods_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button type="primary" name="excelBtn"
                   size="small"
                   icon="el-icon-download"
                   plain
                   v-if="permission.membergoods_excel"
                   @click="exportExcel">导出
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {exportData, getList, remove} from "@/api/contract/goods/membergoods";
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
        option: {
          height: 'auto',
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: false,
          editBtn: false,
          addBtn: false,
          selection: true,
          column: [
            {
              label: "标题",
              prop: "goodsTitle",
              search: true,
              rules: [{
                required: true,
                message: "请输入标题",
                trigger: "blur"
              }]
            },
            {
              label: "价格",
              prop: "price",
              rules: [{
                required: true,
                message: "请输入价格",
                trigger: "blur"
              }]
            },
            {
              label: "渠道",
              prop: "source",
              search: true,
              rules: [{
                required: true,
                message: "请输入渠道",
                trigger: "blur"
              }]
            },

            {
              label: "描述",
              prop: "abstractContent",
              rules: [{
                required: true,
                message: "请输入描述",
                trigger: "blur"
              }]
            },
            {
              label: "同步时间",
              prop: "synTime",
              search: true,
              format: 'yyyy-MM-dd HH:mm:ss',                  // 这是组件展示的日期格式
              valueFormat: 'yyyy-MM-dd HH:mm:ss',        // 这是组件value值的格式
              type: "datetime",
              searchRange: true,
              startPlaceholder: '开始日期',
              endPlaceholder: '结束日期',
              rules: [{
                required: true,
                message: "请输入同步时间",
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
          addBtn: this.vaildData(this.permission.membergoods_add, false),
          viewBtn: this.vaildData(this.permission.membergoods_view, false),
          delBtn: this.vaildData(this.permission.membergoods_delete, false),
          editBtn: this.vaildData(this.permission.membergoods_edit, false),
          excelBtn: this.vaildData(this.permission.membergoods_excel, false),
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
      searchReset() {
        this.query = {};
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        if (params.synTime) {
          params.synTimeBegin = params.synTime[0];// + " 00:00:00";
          params.synTimeEnd = params.synTime[1];// + " 23:59:59";
          params.synTime = null;
        }
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
      },
      exportExcel() {
        exportData(this.query).then(res => {
          let downData = res.data.data;
          for (let i = 0; i < downData.length; i++) {
            downData[i].disposeUser = "我";
          }
          this.$export.excel({
            title: "会员商品商品",
            columns: this.option.column,
            data: downData,
          });
        })
      },
    }
  };
</script>

<style>
</style>

<template>
  <basic-container>
    <avue-crud v-show="!showConfige"
               :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @cell-click="productItemList"
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
                   v-if="permission.category_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small" icon="el-icon-check"
                   v-if="permission.category_configure"
                   @click.native="categoryConfige(scope.row)">属性配置
        </el-button>
      </template>
    </avue-crud>
    <category-dialog ref="refProductItemBox" :productItemBox="productItemBox" :id="id"
                     @close="closeDialog"></category-dialog>
    <category-property v-if="showConfige" @back="back" :cateId="id" :title="title"></category-property>
  </basic-container>
</template>

<script>
  import {add, getDetail, getList, remove, update} from "@/api/contract/product/category";
  import {mapGetters} from "vuex";
  import categoryDialog from "./category-dialog";
  import categoryProperty from "./category-property";

  export default {
    components: {categoryDialog, categoryProperty},
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        productItemBox: false,
        showConfige: false,
        id: {},
        title: "",
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          height: 'auto',
          dialogWidth: "40%",
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan: 6,
          align: 'center',
          menuAlign: 'center',
          tip: false,
          border: true,
          index: false,
          indexLabel: '序号',
          viewBtn: false,
          emptyBtn: false,
          selection: true,
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          column: [
            {
              label: "产品类型编码",
              labelWidth: "20%",
              searchLabelWidth: 100,
              prop: "cateCode",
              maxlength: 20,
              minlength: 1,
              search: true,
              searchOrder: 2,
              span: 24,
              rules: [{
                required: true,
                message: "请输入产品类型编码",
                trigger: "blur"
              }]
            },
            {
              label: "产品类型名称",
              labelWidth: "20%",
              searchLabelWidth: 100,
              prop: "cateName",
              maxlength: 20,
              minlength: 1,
              search: true,
              order: 1,
              searchOrder: 1,
              span: 24,
              rules: [{
                required: true,
                message: "请输入产品类型名称",
                trigger: "blur"
              }]
            },
            {
              label: "产品项",
              prop: "productItemCount",
              display: false,
              formatter: (row, value) => {
                if (value > 0) {
                  return "<div style='color:blue;cursor: pointer;'>" + value + "</div>";
                } else {
                  return 0;
                }

              },

            },
            {
              label: "创建人",
              prop: "createUserName",
              span: 24,
              addDisplay: false, //新增时是否显示
              editDisplay: false, //编辑时是否显示
              viewDisplay: true, //详情时是否显示
              rules: [{
                required: false,
                message: "",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "createTime",
              span: 24,
              addDisplay: false, //新增时是否显示
              editDisplay: false, //编辑时是否显示
              viewDisplay: true, //详情时是否显示
              rules: [{
                required: false,
                message: "",
                trigger: "blur"
              }]
            },
            {
              label: "更新人",
              prop: "updateUserName",
              span: 24,
              hide: false,
              addDisplay: false, //新增时是否显示
              editDisplay: false, //编辑时是否显示
              viewDisplay: true, //详情时是否显示
              rules: [{
                required: false,
                message: "",
                trigger: "blur"
              }]
            },
            {
              label: "更新时间",
              prop: "updateTime",
              span: 24,
              hide: false,
              addDisplay: false, //新增时是否显示
              editDisplay: false, //编辑时是否显示
              viewDisplay: true, //详情时是否显示
              rules: [{
                required: false,
                message: "",
                trigger: "blur"
              }]
            },
            {
              label: "备注",
              labelWidth: "20%",
              prop: "remark",
              maxlength: 100,
              minlength: 1,
              span: 24,
              rules: [{
                required: false,
                message: "请输入备注",
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
          addBtn: this.vaildData(this.permission.category_add, false),
          viewBtn: this.vaildData(this.permission.category_view, false),
          delBtn: this.vaildData(this.permission.category_delete, false),
          editBtn: this.vaildData(this.permission.category_edit, false)
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
        if (row.productItemCount > 0) {
          this.$message.warning("已经关联产品，不能删除");
          return;
        }
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
        for (let i = 0; i < this.selectionList.length; i++) {
          if (this.selectionList[i].productItemCount > 0) {
            this.$message.warning("已经关联产品，不能删除");
            return;
          }
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
      },
      categoryConfige(row) {
        // console.log(row.id);
        this.id = row.id;
        this.title = row.cateName;
        this.showConfige = true;
        // this.$router.push({path: '/contract/product/category-property', query: {cateId: row.id}});
        // this.$route.query.cateId;
        // this.$message({
        //   type: "success",
        //   message: "操作成功!"
        // });
      },
      back() {
        this.showConfige = false;
      },
      closeDialog(val) {
        this.productItemBox = val;
        this.$emit("close", false);
      },
      productItemList(row, column) {
        if (column.property == "productItemCount" && row.productItemCount > 0) {
          this.id = row.id;
          this.$refs.refProductItemBox.searchById(this.id);
          this.productItemBox = true;
        }
      }
    }
  };
</script>

<style>
</style>

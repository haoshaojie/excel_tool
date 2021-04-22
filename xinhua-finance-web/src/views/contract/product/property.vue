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
               @cell-click="categoryList"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot-scope="{}" slot="buttonForm">
        <el-button @click="addAll" size="small" type="primary">添加10条子表单数据</el-button>
      </template>
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.property_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <!--      <template slot="categoryCount" slot-scope="{row}">-->
      <!--        <el-tag @click="categoryList">{{row.categoryCount}}</el-tag>-->
      <!--      </template>-->
    </avue-crud>
    <property-dialog ref="refCategoryBox" :categoryBox="categoryBox" :id="id" @close="closeDialog"
                     @reLoad="searchChange"></property-dialog>
  </basic-container>
</template>

<script>
  import {add, getDetail, getList, remove, update} from "@/api/contract/product/property";
  import {mapGetters} from "vuex";
  import propertyDialog from "./property-dialog";

  export default {
    components: {propertyDialog},
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        categoryBox: false,
        id: 0,
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
          align: 'center',
          tip: false,
          border: true,
          index: false,
          indexLabel: '序号',
          viewBtn: true,
          selection: true,
          dialogWidth: "40%",
          defaultSort: {
            prop: 'createTime',
            order: 'descending'
          },
          labelWidth: "20%",
          viewWidth: "15%",
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          column: [
            {
              label: "主键id 雪花算法产生",
              prop: "id",
              hide: true,
              display: false,
              rules: [{
                required: false,
                message: "请输入主键id 雪花算法产生",
                trigger: "blur"
              }]
            },
            {
              label: "属性名称",
              prop: "propName",
              maxlength: 20,
              minlength: 1,
              searchOrder: 1,
              order: 1,
              search: true,
              span: 23,
              rules: [{
                required: true,
                message: "请输入属性名称",
                trigger: "blur"
              }]
            },
            {
              label: "属性编码",
              prop: "propCode",
              maxlength: 20,
              minlength: 1,
              search: true,
              span: 23,
              rules: [{
                required: true,
                message: "请输入属性编码",
                trigger: "blur"
              }]
            },
            {
              label: "属性类型",
              prop: "propType",
              search: true,
              span: 23,
              type: "radio",
              value: 2,
              dicUrl: this.getDicValue(this.CONSTANT.PROP_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              rules: [{
                required: true,
                message: "请选择属性类型",
                trigger: "blur"
              }]
            },
            {
              label: "是否必填",
              prop: "isRequired",
              hide: true,
              span: 23,
              type: "radio",
              value: 1,
              dicUrl: this.getDicValue(this.CONSTANT.IS_REQUIRED),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              rules: [{
                required: true,
                message: "请选择是否必填",
                trigger: "blur"
              }]
            },
            {
              label: "备注",
              prop: "remark",
              maxlength: 100,
              minlength: 1,
              hide: true,
              span: 23,
              rules: [{
                required: false,
                max: 100,
                message: "请输入属性备注",
                trigger: "blur"
              }]
            },
            {
              label: "表现方式",
              prop: "showType",
              span: 23,
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.SHOW_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              change: ({value}) => {
                let propertyValueForm = this.findObject(this.option.column, 'propertyValue');
                propertyValueForm.display = false;
                if (value == 2) {
                  propertyValueForm.display = true;
                  this.addSelect();
                }
              },
              rules: [{
                required: true,
                message: "请选择表现方式",
                trigger: "blur"
              }]
            },
            {
              label: "属性值",
              prop: 'propertyValue',
              display: false,
              type: 'dynamic',
              span: 24,
              required: true,
              rules: [{
                required: true,
                // message: "请选择表现方式",
                trigger: "blur"
              }],
              children: {
                align: 'center',
                type: 'form',
                index: false,
                calcHeight: 180,
                // gutter:10,
                headerAlign: 'center',
                // rowAdd: (done) => {
                //   this.$message.success('新增回调');
                //   done({
                //     // input: '默认值'
                //   });
                // },
                // rowDel: (row, done) => {
                //   this.$message.success('删除回调' + JSON.stringify(row));
                //   done();
                // },
                column: [{
                  type: 'input',
                  label: '',
                  labelWidth: "0%",
                  span: 12,
                  display: true,
                  prop: 'propCode',
                  maxlength: 20,
                  // tip: '属性值编码',
                  tipPlacement: 'left',
                  placeholder: "属性值编码",
                  required: true,
                  rules: [
                    {
                      required: true,
                      message: '属性值编码',
                      trigger: "blur"
                    }
                  ]
                },
                  {
                    type: 'input',
                    label: '',
                    labelWidth: "0%",
                    span: 11,
                    display: true,
                    prop: 'propValue',
                    maxlength: 20,
                    // tip: '属性值',
                    placeholder: "属性值",
                    rules: [{
                      required: true,
                      message: '属性值',
                      trigger: "blur"
                    }]
                  }]
              }
            },
            {
              label: "产品类型",
              prop: "categoryCount",
              display: false,
              formatter: (row, value) => {
                if (value > 0) {
                  return "<div style='color:blue;cursor: pointer;'>" + value + "</div>";
                } else {
                  return 0;
                }
              }
              // slot:true
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
              label: "最后修改人",
              prop: "updateUserName",
              span: 24,
              hide: true,
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
              label: "最后修改时间",
              prop: "updateTime",
              span: 24,
              hide: true,
              addDisplay: false, //新增时是否显示
              editDisplay: false, //编辑时是否显示
              viewDisplay: true, //详情时是否显示
              rules: [{
                required: false,
                message: "",
                trigger: "blur"
              }]
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
          addBtn: this.vaildData(this.permission.property_add, false),
          viewBtn: this.vaildData(this.permission.property_view, false),
          delBtn: this.vaildData(this.permission.property_delete, false),
          editBtn: this.vaildData(this.permission.property_edit, false)
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

    },
    methods: {
      addSelect() {
        this.form.propertyValue.push({
          input: 1
        })
      },
      closeDialog(val) {
        this.categoryBox = val;
        this.$emit("close", false);
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
      categoryList(row, column) {
        if (column.property == "categoryCount" && row.categoryCount > 0) {
          console.log("传值", row.id);
          this.id = row.id;
          this.$refs.refCategoryBox.searchOpen(this.id);
          this.categoryBox = true;
        }
      },
    }
  };

</script>

<style lang="scss">
  .avue-dynamic__header {
    position: absolute;
    right: -5px;
    top: 0;
    z-index: 10000;
  }

  .avue-dynamic__row {
    padding-top: 0;
    border: none;
    height: 50px;

    .avue-group .el-collapse-item__wrap {
      border-bottom: 0;
    }

    .avue-form__group--flex {
      .avue-form__row {
        padding-left: 0 !important;
        padding-right: 5px !important;
      }
    }

    .avue-dynamic__menu {
      margin-right: -5px;
    }
  }

  .avue-dynamic__row:first-child {
    .avue-dynamic__menu {
      display: none;
    }
  }

  .el-dialog {
    width: 50%;
  }

</style>

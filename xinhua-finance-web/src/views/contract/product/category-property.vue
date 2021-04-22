<template>

  <basic-container>
    <template>
      <el-row
        style="margin-bottom:20px;">
        <el-col
          :span="16">
          <el-page-header
            @back="goBack" :content="title"></el-page-header>
        </el-col>
      </el-row>
    </template>

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
        <el-button type="primary" icon="el-icon-plus" size="small" plain @click.stop="handleAdd">添加</el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.property_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
    <category-property-dialog ref="refPropertyBox" :propertyBox="propertyBox" :cateId="cateId" :page="page"
                              @close="closeDialog" @onLoad="onLoad"></category-property-dialog>
  </basic-container>
</template>

<script>
  import {getDetail, getList, remove} from "@/api/contract/product/category-property";
  import {mapGetters} from "vuex";
  import categoryPropertyDialog from "./category-property-dialog";

  export default {
    name: "categoryProperty",
    components: {categoryPropertyDialog},
    props: {
      cateId: {},
      title: ""
    },
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        propertyBox: false,
        cateId: 0,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          height: 'auto',
          calcHeight: 210,
          dialogWidth: "40%",
          searchShow: true,
          searchMenuSpan: 6,
          // formWidth:"50%",
          align: 'center',
          tip: false,
          border: true,
          index: false,
          indexLabel: '序号',
          editBtn: false,
          addBtn: false,
          viewBtn: true,
          viewBtnOrder: 1,
          selection: true,
          defaultSort: {
            prop: 'createTime',
            order: 'descending'
          },
          labelWidth: "15%",
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
              label: "属性编码",
              prop: "propCode",
              maxlength: 20,
              minlength: 1,
              search: true,
              // searchOrder: 2,
              span: 24,
              rules: [{
                required: true,
                message: "请输入属性编码",
                trigger: "blur"
              }]
            },
            {
              label: "属性名称",
              prop: "propName",
              order: 1,
              maxlength: 20,
              minlength: 1,
              searchOrder: 1,
              search: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入属性名称",
                trigger: "blur"
              }]
            },
            {
              label: "属性类型",
              prop: "propType",
              search: true,
              span: 24,
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
              span: 24,
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
              span: 24,
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
              span: 24,
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
              // labelWidth: "3%",
              prop: 'propertyValue',
              display: false,
              type: 'dynamic',
              span: 24,
              children: {
                align: 'center',
                type: 'form',
                index: false,
                calcHeight: 180,
                gutter: 10,
                headerAlign: 'center',
                column: [{
                  type: 'input',
                  // label: '属性值编码',
                  label: '',
                  labelWidth: "0%",
                  span: 10,
                  display: true,
                  prop: 'propCode',
                  maxlength: 20,
                  tip: '属性值编码',
                  tipPlacement: 'left',
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
                    // label: '属性值',
                    label: '',
                    labelWidth: "0%",
                    span: 10,
                    display: true,
                    prop: 'propValue',
                    tip: '属性值',
                    rules: [{
                      required: true,
                      message: '属性值',
                      trigger: "blur"
                    }]
                  }]
              }
            },
            {
              label: "操作人",
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
              label: "操作时间",
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
          ids.push(ele.catePropId);
        });
        return ids.join(",");
      },
    },
    methods: {
      rowDel(row) {
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return remove(row.catePropId);
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
        console.log("类型包含的属性加载列表数据" + this.cateId);
        this.loading = true;
        // this.cateId = this.$route.query.cateId;
        params.cateId = this.cateId;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
      goBack() {
        this.$emit("back");
      },
      closeDialog(val) {
        this.propertyBox = val;
      },
      handleAdd() {
        this.$refs.refPropertyBox.onLoad(this.cateId);
        this.propertyBox = true;
      }
    }
  };

</script>

<style>
  .cell-blue {
    color: blue !important;
    cursor: pointer;
  }
</style>

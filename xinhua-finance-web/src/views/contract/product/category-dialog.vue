<template>
  <el-dialog title="产品详情"
             append-to-body
             :visible.sync="productItemBox"
             @close="closeDialog"
             width="70%">
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @row-save="rowSave"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
    </avue-crud>
  </el-dialog>

</template>


<script>
  import {getList} from "@/api/contract/product/productitem";
  import {getDeptTree} from "@/api/system/dept";

  export default {
    props: {
      productItemBox: {
        type: Boolean,
        default: false
      },
      id: {
        type: Number,
        default: -1,
      },
    },
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0,
        },
        selectionList: [],
        option: {
          height: 'auto',
          menu: false,
          addBtn: false,
          selectClearBtn: false,
          calcHeight: 210,
          // searchShow: true,
          searchMenuSpan: 6,
          align: 'center',
          menuAlign: 'center',
          tip: false,
          border: true,
          // selectionFixed: false,
          index: false,
          indexLabel: '序号',
          viewBtn: false,
          selection: false,
          searchLabelWidth: 100,
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          column: [
            {
              label: "id",
              prop: "id",
              hide: true
            },
            {
              label: "产品项名称",
              prop: "propName",
              fixed: true,
              search: true
            },
            {
              label: "产品项编码",
              prop: "propCode",
              fixed: true,
              search: true,
              width: 100
            },

            {
              label: "所属部门",
              prop: "propDept",
              type: "select",
              search: true,
              searchFilterable: true,
              dicData: [],
              props: {
                label: "title"
              }
            },
            {
              label: "创建人",
              prop: "createUserName"
            },
            {
              label: "创建时间",
              prop: "createTime",
              search: false,
              type: 'datetimerange',
              span: 24,
              row: true,
              startPlaceholder: '时间日期开始范围自定义',
              endPlaceholder: '时间日期结束范围自定义'
            },
          ]
        },
        data: []
      };
    },
    watch: {},
    computed: {},
    methods: {
      closeDialog() {
        this.$refs.crud.searchReset();
        this.$emit("close", false);
      },
      searchReset(param) {
        this.query = {};
        this.onLoad(this.page);
      },
      searchById(paramId) {
        this.query = {};
        this.id = paramId;
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
        params.cateId = this.id;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
        getDeptTree(this.form.tenantId).then(res => {
          const column = this.findObject(this.option.column, "propDept");
          column.dicData = res.data.data;
        });
      }
    }
  };
</script>

<style>
</style>

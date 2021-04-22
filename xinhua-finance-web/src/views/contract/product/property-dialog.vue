<template>
  <el-dialog title="产品类型"
             append-to-body
             :visible.sync="categoryBox"
             @close="closeDialog"
             width="70%">
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               v-model="form"
               ref="crud"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange">
    </avue-crud>
  </el-dialog>
</template>

<script>
  import {getList} from "@/api/contract/product/category";

  export default {
    name: "product-dialog",
    props: {
      categoryBox: {
        type: Boolean,
        default: false
      },
      id: {
        type: Number,
        default: -1,
      },

    },
    methods: {
      closeDialog() {
        this.$refs.crud.searchReset();
        this.$emit("close", false);
      },
      searchOpen(param) {
        console.log(param);
        this.id = param;
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
        console.log("重新加载数据", this.id);
        this.loading = true;
        params.propId = this.id;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
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
          total: 0
        },
        selectionList: [],
        option: {
          height: 'auto',
          menu: false,
          addBtn: false,
          selectClearBtn: false,
          calcHeight: 210,
          searchShow: true,
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
          emptyBtn: false,
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          column: [
            {
              label: "产品类型名称",
              labelWidth: "15%",
              searchLabelWidth: 100,
              prop: "cateName",
              maxlength: 20,
              minlength: 1,
              search: true,
              searchOrder: 2,
              span: 24,
              rules: [{
                required: true,
                message: "请输入产品类型名称",
                trigger: "blur"
              }]
            },
            {
              label: "产品类型编码",
              labelWidth: "15%",
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
            }
          ]
        },
        data: []
      };
    },
  }
</script>

<style scoped>

</style>

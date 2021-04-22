<template>
  <el-dialog
    title="请选择客户"
    :visible.sync="customerDialogFlag"
    @close="closeDialog" width="80%" center
  >
    <avue-crud ref="crud"
               :option="option"
               :page.sync="page"
               :search.sync="query"
               :table-loading="loading"
               @on-load="onLoad"
               @refresh-change="refreshChange"
               @search-reset="searchReset"
               @search-change="searchChange"
               v-model="form"
               :data="data" @row-click="handleRowClick">
    </avue-crud>
    <span slot="footer" class="dialog-footer">
    <el-button @click="closeDialog">取 消</el-button>
    <el-button type="primary" @click="addSuperUnit">确 定</el-button>
  </span>
  </el-dialog>
</template>

<script>
  import {getList} from "@/api/contract/customer/orgcustomer";

  export default {
    name: "customerDialog",
    props: {
      customerDialogFlag: {
        type: Boolean,
        default: false
      },
      id: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        page: {},
        query: {},
        form: {},
        params: {},
        loading: false,
        row:{},
        data: [],
        option:{
          highlightCurrentRow:true,
          searchMenuSpan: 6,
          menu:false,
          addBtn:false,
          refreshBtn:false,
          border: true,
          column: [{
            label: '客户编号',
            prop: 'custCode'
          }, {
            label: '客户名称',
            prop: 'custName',
            search: true,
          }, {
            label: '所属地域',
            prop: 'regionName'
          }, {
            label: '所属行业',
            prop: 'custIndustryName'
          }, {
            label: '客户状态',
            prop: 'custStateName',

          },
            {
              label: '客户状态',
              prop: "custState",
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.CUST_STATE),
              hide: true,
              search: true,
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            }
          ]
        }
      }
    },
    methods: {
      closeDialog() {
        this.query.custName=null;
        this.query.custState=null;
        this.searchReset();
        this.$emit("cancelCustomerDialog");
      },
      refreshChange () {
        this.query = {};
        this.onLoad(this.page);
      },
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
        });
      },
      searchChange(params, done) {
        this.page.currentPage = 1;
        this.onLoad(this.page, this.query);
        done();
      },
      handleRowClick (row, event, column) {
       this.row=row;
      },
      addSuperUnit(){
        if (this.id&&this.id==this.row.id){
          this.$message.error("上级单位不能是自己本身")
          return;
        }
        this.$emit("acceptSuperUnit",this.row);
        this.$emit("cancelCustomerDialog");
      },
      searchReset() {
        this.query = {};
        this.onLoad(this.page);
      },
    }
  }
</script>

<style scoped>

</style>

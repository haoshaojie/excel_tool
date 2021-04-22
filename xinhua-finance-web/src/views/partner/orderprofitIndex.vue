<!-- 分润明细统计 -->
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
      <template slot-scope="scope" slot="menu">
        <el-button type="text"
                   icon="el-icon-view"
                   size="small" @click="viewOrderprofit(scope.row)">查看分润明细
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getPartnerOrderProfitTotal} from "@/api/partner/partner";

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
          searchLabelWidth: 100,
          searchShow: true,
          addBtn: false,
          addRowBtn: false,
          editBtn: false,
          viewBtn: false,
          delBtn: false,
          searchBtn: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          selection: false,
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          column: [
            {
              label: "合作商ID",
              prop: "id",
              hide: true
            },
            {
              label: "起止时间",
              prop: "profitTime",
              type: 'datetimerange',
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              hide: true,
              searchRange: true,
              searchSpan: 7,
              search: true,
              clearable: false,
              searchClearable: false,
              searchOrder: 2,
              searchValue: [this.getCurrentMonthFirst(), this.getCurrentMonthLast()]
            },
            {
              label: "合作商编码",
              prop: "parCode",
              searchSpan: 5,
              search: true,
              showWordLimit: true,
              maxlength: 30
            },
            {
              label: "合作商名称",
              search: true,
              prop: "parName",
              searchSpan: 5,
              showWordLimit: true,
              searchOrder: 1,
              maxlength: 30
            },
            {
              label: "企业客户分润总额（元）",
              prop: "customerTotal"
            },
            {
              label: "个人用户分润总额（元）",
              prop: "userTotal"
            },
            {
              label: "分润总额（元）",
              prop: "total"
            },
          ]
        },
        data: []
      };
    },
    computed: {
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    methods: {
      searchReset() {
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
        if (this.query.profitTime) {
          this.query.startProfitTime = this.query.profitTime[0];
          this.query.endProfitTime = this.query.profitTime[1];
        } else {
          this.query.startProfitTime = this.getCurrentMonthFirst();
          this.query.endProfitTime = this.getCurrentMonthLast();
        }
        getPartnerOrderProfitTotal(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
      viewOrderprofit(row) {
        this.$router.push({
          path: '/partner/orderprofit',
          query: {
            id: row.id,
            startProfitTime: this.query.profitTime ? this.query.profitTime[0] : this.getCurrentMonthFirst(),
            endProfitTime: this.query.profitTime ? this.query.profitTime[1] : this.getCurrentMonthLast()
          }
        })
      },
      getCurrentMonthFirst() {
        var date = new Date();
        date.setDate(1);
        var month = parseInt(date.getMonth() + 1);
        var day = date.getDate();
        if (month < 10) {
          month = '0' + month
        }
        if (day < 10) {
          day = '0' + day
        }
        return date.getFullYear() + '-' + month + '-' + day + " 00:00:00";
      },
      getCurrentMonthLast() {
        var date = new Date();
        var currentMonth = date.getMonth();
        var nextMonth = ++currentMonth;
        var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
        var oneDay = 1000 * 60 * 60 * 24;
        var lastTime = new Date(nextMonthFirstDay - oneDay);
        var month = parseInt(lastTime.getMonth() + 1);
        var day = lastTime.getDate();
        if (month < 10) {
          month = '0' + month
        }
        if (day < 10) {
          day = '0' + day
        }
        return date.getFullYear() + '-' + month + '-' + day + " 23:59:59";
      }
    }
  };
</script>

<style>
</style>

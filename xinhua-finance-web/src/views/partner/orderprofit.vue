<template>
 <basic-container>
  <avue-form ref="form" v-model="searchParam" :option="searchOption" @reset-change="emptytChange" @submit="doSearch">
    <template slot-scope="scope" slot="menuForm">
        <el-button @click="doSearch" icon="el-icon-search">搜索</el-button>
    </template>
  </avue-form>


   <avue-tabs :option="tabOption" @change="handleChange"></avue-tabs>
   <span v-if="tabType.prop==='tab1'">
        <avue-crud :option="optionTab1"
                   :table-loading="loading"
                   :data="dataTab1"
                   :page="pageTab1"
                   v-model="form"
                   ref="crud"
                   @search-change="searchChange1"
                   @search-reset="searchReset1"
                   @selection-change="selectionChange1"
                   @current-change="currentChange1"
                   @size-change="sizeChange1"
                   @on-load="onLoad">
         <template slot="profitRate" slot-scope="scope">
           <a @click="viewProfitRate(scope.row)" style="color:#078ecf;">查看</a>
         </template>

        </avue-crud>
   </span>
   <span v-else-if="tabType.prop==='tab2'">
     <avue-crud :option="optionTab2"
                :table-loading="loading"
                :data="dataTab2"
                :page="pageTab2"
                v-model="form"
                ref="crud"
                @search-change="searchChange2"
                @search-reset="searchReset2"
                @selection-change="selectionChange2"
                @current-change="currentChange2"
                @size-change="sizeChange2"
                @on-load="onLoad">
     </avue-crud>
   </span>
   <el-dialog
     title="分润信息"
     :visible.sync="dialogVisible"
     width="30%"
     :before-close="handleClose">

     <el-table
       :data="tableData"

       style="width: 100%">
       <el-table-column
         prop="userRcnt"
         align="center"
         label="用户人数"
         width="180">
       </el-table-column>
       <el-table-column
         prop="profitRate"
         align="center"
         label="分润比例"
         width="180">
       </el-table-column>
       <el-table-column
         prop="profitAmount"
         align="center"
         label="分润金额(元)">
       </el-table-column>
     </el-table>

   </el-dialog>
 </basic-container>

</template>

<script>
  import {getList} from "@/api/partner/orderprofit";

  export default {
    data() {
      return {
        tableData: [],
        dialogVisible: false,
        searchParam:{
          queryProfitTime:[ this.$route.query.startProfitTime,this.$route.query.endProfitTime]
        },
        form: {},
        query: {},
        loading: true,
        pageTab1: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        pageTab2: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        partnerId:0,
        selectionList: [],
        searchOption:{
          menuSpan:6,
          submitBtn:false,
          column:[
             {
              label:'分润时间',
              prop:'queryProfitTime',
              type: 'datetimerange',
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              span: 8
            }, {
              label:'产品名称',
              prop:'prodName',
              span: 5,
              showWordLimit:true,
              maxlength:30
            }, {
              label:'产品编码',
              prop:'prodCode',
              span: 5,
              showWordLimit:true,
              maxlength:30
            }
          ]
        },
        tabType:{},
        tabOption:{
          column: [{
            icon:'el-icon-tickets',
            label: '企业客户',
            prop: 'tab1',
          }, {
            icon:'el-icon-tickets',
            label: '个人用户',
            prop: 'tab2',
          }]
        },
        optionTab1: {
          height: 'auto',
          calcHeight: 210,
          addBtn:false,
          addRowBtn:false,
          editBtn:false,
          viewBtn:false,
          delBtn:false,
          refreshBtn:false,
          searchBtn:false,
          searchShowBtn:false,
          filterBtn:false,
          searchShow: false,
          menu:false,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: true,
          selection: false,
          column: [
            {
              label: "主键id 雪花算法产生",
              prop: "id",
              hide:true
            },
            {
              label: "合约编码",
              prop: "conCode"
            },
            {
              label: "合约名称",
              prop: "conName"
            },
            {
              label: "合约表id",
              prop: "conId",
              hide:true
            },
            {
              label: "客户名称",
              prop: "customerName"
            },
            {
              label: "产品名称",
              prop: "prodName",
            },
            {
              label: "产品编码",
              prop: "prodCode",
            },
            {
              label: "合约金额(元)",
              prop: "orderAmount"
            },
            {
              label: "分润金额(元)",
              prop: "profitAmount"
            },
            {
              label: "分润比例",
              prop: "profitRate",
              slot:true
            },
            {
              label: "分润时间",
              prop: "profitTime"
            },
          ]
        },
        optionTab2: {
          height: 'auto',
          calcHeight: 210,
          addBtn:false,
          addRowBtn:false,
          editBtn:false,
          viewBtn:false,
          delBtn:false,
          refreshBtn:false,
          searchBtn:false,
          searchShowBtn:false,
          filterBtn:false,
          searchShow: false,
          menu:false,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: true,
          selection: false,
          column: [
            {
              label: "订单编号",
              prop: "orderCode"
            },
            {
              label: "订单来源",
              prop: "prodType"
            },
            {
              label: "产品名称",
              prop: "prodName",
            },
            {
              label: "产品编码",
              prop: "prodCode",
            },
            {
              label: "订单金额(元)",
              prop: "profitAmount"
            },
            {
              label: "分润比例",
              prop: "profitRate"
            },
           {
             label: "分润金额(元)",
             prop: "profitAmount"
           },
            {
              label: "分润时间",
              prop: "profitTime",
            }
          ]
        },
        dataTab1: [],  // 客户分润明细
        dataTab2:[]   // 个人分润明细
      };
    },
    created() {
       this.tabType = this.tabOption.column[0];
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
      // tabs切换
       handleChange(column) {
         this.tabType = column;
         // this.$message.success(JSON.stringify(column));
       },
      currentChange1(currentPage){
        this.pageTab1.currentPage = currentPage;
      },
      currentChange2(currentPage){
        this.pageTab2.currentPage = currentPage;
      },
      sizeChange1(pageSize){
        this.pageTab1.pageSize = pageSize;
      },
      sizeChange2(pageSize){
        this.pageTab2.pageSize = pageSize;
      },
      onLoad() {
        this.loading = true;
        this.partnerId =  this.$route.query.id;
        let startProfitTime = this.$route.query.startProfitTime;
        let endProfitTime = this.$route.query.endProfitTime;
        this.searchParam.queryPprofitTime=[startProfitTime,endProfitTime];

        this.query.parId = this.partnerId;
         //加载客户分润明细
         getList(this.pageTab1.currentPage, this.pageTab1.pageSize, Object.assign({profitType:1}, this.query)).then(res => {
           const data = res.data.data;
           this.pageTab1.total = data.total;
           this.dataTab1 = data.records;
           this.loading = false;
         });

         // 加载个人分润明细
         this.pageTab1.currentPage = 1;
         getList(this.pageTab2.currentPage, this.pageTab2.pageSize, Object.assign({profitType:2}, this.query)).then(res => {
           const data = res.data.data;
           this.pageTab2.total = data.total;
           this.dataTab2 = data.records;
           this.loading = false;
         });
      },
      // 搜索
      doSearch() {
       // this.$message.success('当前数据'+JSON.stringify(this.searchParam));

        // 查询操作
        this.query = this.searchParam;
        if(this.searchParam.queryProfitTime) {
          this.query.startProfitTime = this.searchParam.queryProfitTime[0];
          this.query.endProfitTime = this.searchParam.queryProfitTime[1];
        }
        this.pageTab1.currentPage = 1;
        this.pageTab2.currentPage = 1;
        this.onLoad();

      },
      emptytChange(){
        // this.$message.success('清空方法回调');
      },
      viewProfitRate(row){ //查看合约分润明细
        if(row.profitRate) {
          let rateData = JSON.parse(row.profitRate);
          this.tableData = rateData;
        } else{
          this.tableData = [];
        }
        this.dialogVisible = true;
      }
    }
  };
</script>

<style>
</style>

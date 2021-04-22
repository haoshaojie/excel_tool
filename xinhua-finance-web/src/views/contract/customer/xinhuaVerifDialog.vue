<template>
  <el-dialog
  title="新华信用验证"
  :visible.sync="xinhuaVerifFlag" :modal-append-to-body="false"
  width="60%"
  :before-close="cancelDialog">

    <div>
      <el-row>
        <el-col  :span="15">
          <el-input v-model="query.name"></el-input>
        </el-col>
        <el-col  :span="1"></el-col>
        <el-col  :span="8">
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="reset">清空</el-button>
        </el-col>
      </el-row>
      <el-row style="color: red;margin-bottom: 10px">
        双击选择企业，选择的信息会录入到客户信息中
      </el-row>
      <div v-for="item in data">
        <el-row  class="title" >
          <div @dblclick=clickCol(item)>
            企业名称：{{item._source.company_name}}
          </div>
        </el-row>
        <el-row>
          公司类型：{{item._source.econ_kind}}
        </el-row>
        <el-row>
          <el-col :span="6">
            法定代表人：{{item._source.oper_name}}
          </el-col>
          <el-col :span="6">
            注册资本：{{item._source.raw_regist_capi}}
          </el-col>
          <el-col :span="6">
            成立时间：{{item._source.start_date}}
          </el-col>
          <el-col :span="6">
            经营状态：{{item._source.status}}
          </el-col>
        </el-row>
        <el-divider></el-divider>
      </div>
    </div>
    <el-pagination
      @size-change="handleSizeChange" background
      @current-change="handleCurrentChange"
      :page-sizes="[10, 30, 50]"
      :page-size="query.pageSize"
      layout="sizes, prev, pager, next"
      :total="query.totalCount">
    </el-pagination>
  </el-dialog>
</template>

<script>
  import {companySearch} from "@/api/contract/customer/orgcustomer";

  export default {
    name: "xinhua-verif-dialog",
    props: {
      xinhuaVerifFlag: {
        type: Boolean,
        default: false
      },
      custName: {
        type: String,
        default: ''
      },
      enterpriseInfo: {
        type: Object,
        default: null
      }
    },
    watch:{
      custName(val){
         this.query.name=val;

      },
      xinhuaVerifFlag(val){
        if (val){
          this.search();
        }
      }
    },
    data(){
      return{

        query:{
          name:"",
          pageNo:1,
          pageSize:10,
          totalCount:0,
        },
        data:[
        ]
      }
    },
    methods:{
      cancelDialog(row){
        this.xinhuaVerifFlag=false;
        this.search();
        this.$emit('cancel',row);
      },
      clickCol(row){
        this.cancelDialog(row._source);
      },
      search(){
        companySearch(this.query).then(res=>{
          var data = res.data.data;
          this.query.totalCount=data.page.totalCount;
          this.data=data.data.hits.hits
        })
      },
      reset(){
        this.query={
            name:"",
            pageNo:1,
            pageSize:10,
            totalCount:0,
        };
        this.search();
      },
      handleSizeChange(val) {
        this.query.pageSize=val;
        this.search();
      },
      handleCurrentChange(val) {
        this.query.pageNo=val;
        this.search();
      }
    },
  }
</script>

<style scoped>
  div {
    line-height: 200%;
    margin-top: 5px;
  }
  div .title{
    font-weight:bold;
    margin-top: 5px;
  }
</style>

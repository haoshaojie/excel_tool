<!--用友合同详情--->
<template>
    <el-dialog
    title="合同详情"
    width="60%"
    :visible.sync="showDetail">
    <div class="el-table-title">合同信息</div>
    <table class="el-detail-table ">
        <tr>
            <td>财务组织</td>
            <td>{{detailData.financialOrg}}</td>
            <td>项目名称</td>
            <td>{{detailData.projectName}}</td>
        </tr>
         <tr>
            <td>合同编码</td>
            <td>{{detailData.conNcNo}}</td>
            <td>合同名称</td>
            <td>{{detailData.conName}}</td>
        </tr>
        <tr>
            <td>客户名称</td>
            <td>{{detailData.custName}}</td>
            <td>合同起始日期</td>
            <td>{{detailData.conStartTime}}</td>
        </tr>
        <tr>
            <td>合同终止日期</td>
            <td>{{detailData.conEndTime}}</td>
            <td>合同金额</td>
            <td>{{detailData.conAmount}}</td>
        </tr>
        <tr>
            <td>客户性质一</td>
            <td>{{detailData.custCharacter1}}</td>
            <td>客户性质二</td>
            <td>{{detailData.custCharacter2}}</td>
        </tr>
        <tr>
            <td>客户性质三</td>
            <td>{{detailData.custCharacter3}}</td>
            <td>签约主体</td>
            <td>{{detailData.contractParty}}</td>
        </tr>
        <tr>
            <td>合同签约类型</td>
            <td>{{detailData.signType}}</td>
            <td>实际服务客户</td>
            <td>{{detailData.actualServiceCust}}</td>
        </tr>
        <tr>
            <td>销售人员电话</td>
            <td>{{detailData.salesmanPhone}}</td>
            <td>合同完结状态</td>
            <td>{{detailData.contFinishState}}</td>
        </tr>
        <tr>
            <td>具体服务要求</td>
            <td>{{detailData.serviceRequest}}</td>
            <td>签约日期</td>
            <td>{{detailData.signDate}}</td>
        </tr>
        <tr>
            <td>合同完结说明</td>
            <td>{{detailData.finishDesc}}</td>
            <td>确认收入方式</td>
            <td>{{detailData.revenueMethod}}</td>
        </tr>
        <tr>
            <td>派工开始时间</td>
            <td>{{detailData.workStartTime}}</td>
            <td>派工结束时间</td>
            <td>{{detailData.workEndTime}}</td>
        </tr>
        <tr>
            <td>生效日期</td>
            <td>{{detailData.effectiveDate}}</td>
            <td>第三方机构性质</td>
            <td>{{detailData.thirdPartyNature}}</td>
        </tr>
        <tr>
            <td>第三方客户</td>
            <td>{{detailData.thirdPartyCust}}</td>
            <td>签约部门</td>
            <td>{{detailData.signDept}}</td>
        </tr>
        <tr>
            <td>销售人员</td>
            <td colspan="3">{{detailData.salesman}}</td>
        </tr>
    </table>
    <div class="el-table-title">产品信息 </div>
    <avue-crud 
    ref="productInfo"
    :data="productData"
    :option="option"
    ></avue-crud>
    <div class="el-table-title">同步信息</div>
    <table class="el-detail-table">
        <tr>
            <td>同步时间</td>
            <td>{{detailData.synTime}}</td>
            <td>同步状态</td>
            <td>{{detailData.syncStateStr}}</td>
        </tr>
    </table>
     <div slot="footer" class="dialog-footer">
        <el-button  @click="showDetail=false" size="small">关 闭</el-button>
    </div>
    </el-dialog>

</template>
<script>
import {getDetail} from "@/api/contract/contract/nc";
import {getDictByCode} from "@/api/system/dict";
export default {
    name:"nc-contract-detail-dialog",
    data(){
        return{
            showDetail:false,
            detailData:{},
            option:{
                calcHeight: 100,
                searchShow: false,
                searchMenuSpan: 6,
                border: true,
                index: false,
                viewBtn: false,
                addBtn:false,
                refreshBtn:false,
                columnBtn:false,
                header:false,
                menu:false,
                column:[
                    {
                        label:"产品名称",
                        prop:"prodName"
                    },
                    {
                        label:"产品部门",
                        prop:"prodDept"
                    },
                    {
                        label:"产品平台",
                        prop:"prodPlatform"
                    },
                    {
                        label:"单价",
                        prop:"prodPrice"
                    },
                    {
                        label:"数量",
                        prop:"prodNum"
                    },
                    {
                        label:"产品金额",
                        prop:"prodAmount"
                    },
                    {
                        label:"产品组织",
                        prop:"prodOrg"
                    },
                ]
            },
            productData:[]
        }
    },
    methods:{
      openDialog(data){
        this.showDetail=true;
        getDetail(data.id).then(res => {
          var data = res.data.data;
          //转化同步状态字段
          var syncState = data.syncState;
          getDictByCode("nc_syncstate").then(res =>{
            const dicArr = res.data.data;
            dicArr.forEach(element => {
              if(element.dictKey==syncState){
                data.syncStateStr = element.dictValue;
              }
            });
            this.detailData=data;
            this.productData = data.proList;
          });
        });
      }
    }
}
</script>
<style lang="scss" scoped>
.el-detail-table {
    width: 100%;
    background-color: #FFFFFF;
    color: #222222;
    tr td {
      width: 25%;
      padding: 0.5rem 0 0.8rem 0;
      box-sizing: border-box;
      border: 1px solid #D7D9E2;
      font-size: 13px !important;
      text-align: center;
    }

    tr td:nth-child(odd) {
      font-weight: bold;
      background-color: #E3F2FD;
    }
  }
.dialog-footer{
    text-align: center;
}
 .el-table-title{
   margin:1rem 0;
   font-size: 14px;
   color: #333;
 }
</style>
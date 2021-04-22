<!--合约信息-->
<template>
    <basic-container>
        <div class="el-header">
            <div>合约数量:<span>5</span></div>
            <el-button type="text" >创建合约</el-button>
        </div>
        <avue-crud
            :option="option"
            :data="data"
            :page="page"
        ></avue-crud>
    </basic-container>
</template>
<script>
export default {
    name:"contract-info",
    data(){
        return{
            page: {
                pageSize: 10,
                currentPage: 1,
                total:1
            },
            option:{
            addBtn:false,
            editBtn:false,
            delBtn:false,
            refreshBtn:false,
            columnBtn:false,
            header:false,
            border:true,
            viewBtn:true,
            viewBtnText:"查看详情",
            column:[
                {
                label:'合约编号',
                prop:'contractCode'
              },
               {
                label:'合约名称',
                prop:'contractName'
              },
              {
                label:'合约金额（元）',
                prop:'amount'
              },
              {
                label:'合约状态',
                prop:'contractStatus'
              },
              {
                label:'合约起始日期',
                prop:'startTime',
                type: 'date',
                format: 'yyyy-MM-dd',
                valueFormat: 'yyyy-MM-dd'
              },
              {
                label:'合约终止日期',
                prop:'endTime',
                type: 'date',
                format: 'yyyy-MM-dd',
                valueFormat: 'yyyy-MM-dd'
              }
            ]
            },
            data:[
                {
                contractCode:"合约编码",
                contractName:"合约名称",
                amount:"合约金额",
                contractStatus:"合约状态",
                startTime:"2021-01-09",
                endTime:"2021-07-09"
                }
            ]
        }
    },
    methods:{

    }
}
</script>

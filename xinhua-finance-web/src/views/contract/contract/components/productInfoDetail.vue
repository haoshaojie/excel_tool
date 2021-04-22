<!--产品信息详情-->
<template>
    <div>
        <avue-crud 
        ref="productInfo"
        :summary-method="summaryMethod" 
        :option="option"
        :data="data"
        ></avue-crud>
    </div>
</template>
<script>
export default {
    name:"product-info-detail",
    data(){
        return{
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
                showSummary:true,
                column:[
                    {
                        label:"产品编码",
                        prop:"productCode"
                    },
                    {
                        label:"产品名称",
                        prop:"productName"
                    },
                    {
                        label:"产品部门",
                        prop:"department"
                    },
                    {
                        label:"单价",
                        prop:"price"
                    },
                    {
                        label:"货币",
                        prop:"currency"
                    },
                    {
                        label:"数量",
                        prop:"count"
                    },
                    {
                        label:"已绑用户数量",
                        prop:"bindNumber"
                    },
                    {
                        label:"产品金额",
                        prop:"amount"
                    },
                    {
                        label:"起始日期",
                        prop:"startTime"
                    },
                    {
                        label:"终止日期",
                        prop:"endTime"
                    },
                    {
                        label:"扩展属性",
                        prop:"extends"
                    }
                ]
            },
            data:[
                {
                    productCode:"LME00001",
                    productName:"港交所基金L1实时",
                    department:"新华财经",
                    price:"5000.00",
                    currency:"元",
                    count:"11",
                    bindNumber:"200",
                    amount:"30000.00",
                    startTime:"2020-12-23 00:00:00",
                    endTime:"2021-12-23 23:59:59",
                    extends:"终端：移动终端,datafeed区域：全部接入点：5用户流量：500M",
                }
            ]
        }
    },
    methods:{
        summaryMethod({ columns, data }){
            const sums = [];
            if (columns.length > 0) {
                columns.forEach((column, index) => {
                if (index === 0) {
                    sums[index] = '合计'
                } else {
                    if(column.label=="已绑用户数量"){
                        sums[index]=0;
                        data.forEach(item => {
                            sums[index]+=Number(item.bindNumber);
                        });
                    }else if(column.label=="产品金额"){
                        sums[index]=0;
                         data.forEach(item => {
                            sums[index]=(Number(sums[index])+Number(item.amount)).toFixed(2);
                        });
                    }
                }
                });
            }
            return sums;
        },
    }
}
</script>
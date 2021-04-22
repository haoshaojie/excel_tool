<!--用户协议详情-->
<template>
    <avue-crud 
        ref="productInfo"
        :option="option"
        :data="data"
        >
        <template slot="menu" slot-scope="">
            <el-button type="text" size="small">下载</el-button>
        </template>
    </avue-crud>
</template>
<script>
export default {
     name:"user-agreement-detail",
    data(){
        return{
            option:{
                calcHeight: 100,
                searchShow: false,
                searchMenuSpan: 6,
                border: true,
                index: false,
                viewBtn: true,
                viewBtnText:"查看协议",
                addBtn:false,
                editBtn:false,
                delBtn:false,
                refreshBtn:false,
                columnBtn:false,
                header:false,
                column:[
                    {
                        label:"协议编码",
                        prop:"productCode"
                    },
                    {
                        label:"协议名称",
                        prop:"productName"
                    },
                    {
                        label:"协议类型",
                        prop:"department"
                    },
                    {
                        label:"用户协议附件",
                        prop:"price"
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
    }
}
</script>
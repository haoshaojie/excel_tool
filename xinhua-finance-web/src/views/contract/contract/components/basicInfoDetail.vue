<!--基本信息详情-->
<template>
    <div>
        <div class="el-table-title">基本信息</div>
        <table class="el-detail-table ">
            <tr>
                <td>合约编码</td>
                <td>2020122314210001</td>
                <td>合约名称</td>
                <td>中国工商银行2020专业终端服务合同</td>
            </tr>
            <tr>
                <td>合约范围</td>
                <td>企业合约</td>
                <td>合约类型</td>
                <td>试用合约</td>
            </tr>
            <tr>
                <td>合约状态</td>
                <td>启用</td>
                <td>所属部门</td>
                <td>新华财经</td>
            </tr>
            <tr>
                <td>客户编码</td>
                <td>KH202101299991</td>
                <td>客户名称</td>
                <td>中国工商银行</td>
            </tr>
            <tr>
                <td>签约乙方</td>
                <td>中经社</td>
                <td>签约状态</td>
                <td>已续约</td>
            </tr>
            <tr>
                <td>签约日期</td>
                <td>2021-01-01</td>
                <td>合同期限（月）</td>
                <td>12</td>
            </tr>
            <tr>
                <td>合约起始日期</td>
                <td>2020-12-12</td>
                <td>合约终止日期</td>
                <td>2021-12-31</td>
            </tr>
            <tr>
                <td>合约金额</td>
                <td>10000000.00元</td>
                <td>合约当前期金额</td>
                <td>1000000.00元</td>
            </tr>
            <tr>
                <td>合约附件</td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td>具体服务要求</td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td>是否用友同步</td>
                <td>是</td>
                <td>同步时间</td>
                <td>2020-12-14 12:00:00</td>
            </tr>
            <tr>
                <td>创建人</td>
                <td>张萌</td>
                <td>创建时间</td>
                <td>2020-12-14 12:00:00</td>
            </tr>
            <tr>
                <td>最后修改人</td>
                <td>温一飞</td>
                <td>最后修改时间</td>
                <td>2021-01-02 12:00:00</td>
            </tr>
        </table>
        <div class="el-table-title">联系人信息</div>
        <avue-crud 
        ref="contactInfo" 
        :option="option"
        :data="data"
        ></avue-crud>
    </div>
</template>
<script>
export default {
    name:"basic-info-detail",
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
                column:[
                    {
                        label:"联系人",
                        prop:"contact"
                    },
                    {
                        label:"联系人类型",
                        prop:"contactType"
                    },
                    {
                        label:"联系方式",
                        prop:"phoneNumber"
                    },
                    {
                        label:"邮箱",
                        prop:"email"
                    },
                    {
                        label:"部门",
                        prop:"department"
                    },
                    {
                        label:"职务",
                        prop:"position"
                    },
                    {
                        label:"客户经理",
                        prop:"manager"
                    },
                    {
                        label:"所属机构",
                        prop:"institutions"
                    },
                ]
            },
            data:[{   
                    contact:"张明",
                    contactType:"培训",
                    phoneNumber:"18709980000",
                    email:"504642566@qq.com",
                    department:"业务发展部",
                    position:"销售",
                    manager:"张静",
                    institutions:"新华财经"
                }
            ]
        }
    }
}
</script>
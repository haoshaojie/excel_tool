<!--创建合约的用户授权-->
<template>
    <basic-container>
        <div class="el-basic-title">产品列表</div>
        <div class="el-product-btn">
            <el-button 
                type="primary" 
                size="small"
                @click="showSelect=true">
                选择用户
            </el-button>
        </div>
        <div>
            <avue-crud
            ref="crud"
            v-model="form"
            :option="option"
            :data="data"
            @selection-change="selectionChange"
            >
            <template slot="authorized" slot-scope="{row}">
                <el-button type="text" @click="getAuthorizedList(row.id)" size="small">{{row.authorized}}</el-button>
            </template>
             <template slot="menu">
                <el-button type="text" @click="authorization" size="small">批量授权</el-button>
                <el-button type="text" @click="editPermissions" size="small">编辑权限</el-button>
            </template>
            </avue-crud>
        </div>
        <div class="el-step-footer">
            <el-button type="primary" size="small" plain @click="back">上一步</el-button>
            <el-button type="primary" size="small" @click="save">提&nbsp;&nbsp;&nbsp;交</el-button>
            <el-button type="primary" size="small" plain @click="close">关&nbsp;&nbsp;&nbsp;闭</el-button>
        </div>
        <select-user-dialog ref="selectUserDialog" :showSelect="showSelect" @close="closeSelect"></select-user-dialog>
        <authorized ref="authorized"  :showAuthorized="showAuthorized" @close="colseAuthorized"></authorized>
        <batch-authorization ref="batchAuthorization" :showBatch="showBatch" @close="closeBatch"></batch-authorization>
    </basic-container>
</template>
<script>
import authorized from "./authorized";
import batchAuthorization from "./batchAuthorization";
import selectUserDialog from "./selectUserDialog";
export default {
    name:"authorizationInfo",
    components:{
        authorized,
        batchAuthorization,
        selectUserDialog
    },
    data(){
        return{
            showAuthorized:false,
            showBatch:false,
            showSelect:false,
            form:{},
            selectionList:[],
            option:{
                calcHeight: 210,
                searchMenuSpan:6,
                tip: false,
                border: true,
                index: false,
                viewBtn:false,
                addBtn:false,
                editBtn:false,
                delBtn:false,
                menu:true,
                header:false,
                selection: true,
                column:[
                     {
                        label:"产品编码",
                        prop:"productNo"
                    },
                     {
                        label:"产品名称",
                        prop:"productName"
                    },
                     {
                        label:"起始时间",
                        prop:"startTime",
                        type:"datetime",
                    },
                    {
                        label:"终止时间",
                        prop:"endTime",
                        type:"datetime",
                    },
                    {
                        label:"已授权用户",
                        prop:"authorized",
                        slot:true
                    },
                    {
                        label:"剩余用户",
                        prop:"remaining"
                    }
                ]
            },
            data:[
                {
                    productNo:"产品编码",
                    productName:"productNo",
                    startTime:"2021-01-02",
                    endTime:"2021-04-06",
                    authorized:100,
                    remaining:100
                },
            ]
        }
    },
    methods:{
        authorization(){
            this.showBatch=true;
        },
        closeBatch(){
            this.showBatch=false;
        },
        closeSelect(){
            this.showSelect=false;
        },
        editPermissions(){},
        
        getAuthorizedList(id){
            this.showAuthorized=true;
        },
        colseAuthorized(){
            this.showAuthorized=false;
        },
        back(){
            this.$emit("update",3);
        },
        save(){

        },
        close(){
            this.$emit("update",5);
        },
         selectionChange(list) {
            this.selectionList = list;
        },

    }
}
</script>
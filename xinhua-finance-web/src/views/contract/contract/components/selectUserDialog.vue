<!--选择用户的弹窗--->
<template>
    <el-dialog
    title="选择用户"
    :visible.sync="showSelect"
    width="80%"
    >
        <transfer 
        :leftList="leftList" 
        :rightList="rightList" 
        :columns="leftColumns" 
        :rightColumns="rightColumns"
        @transferChange="transferChange">
        <div slot="leftSearch" class="el-select-slot">
            <div class="el-select-slot-header">客户用户列表（{{leftData.length}}）</div>
            <!--  @keyup.enter.native="seachLeftList" -->
            <el-input placeholder="请输入部门/姓名/邮箱/手机号搜索" v-model="leftSearch" size="small" @change="seachLeftList" ></el-input>
        </div>
        <div slot="rightSearch" class="el-select-slot">
            <div class="el-select-slot-header">已选人员（{{rightData.length}})</div>
            <el-input placeholder="请输入姓名/邮箱" size="small" v-model="rightSearch"  @change="seachRightList"></el-input>
        </div>
        </transfer>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="closeDialog" size="small">确 认</el-button>
            <el-button  @click="closeDialog" size="small">取 消</el-button>
        </div>
    </el-dialog>
</template>
<script>
import transfer from "@/components/transfer/index";
export default {
    name:"select-user-dialog",
    components:{
        transfer
    },
    props:{
        showSelect:{
            type:Boolean,
            default:false
        }
    },
    data(){
        return{
            leftList:[
                {
                    account:"XHCJ0001",
                    email:"504642566@qq.com",
                    phoneNumber:"18703817652 ",
                    name:"孟一",
                    department:"市场部"
                },
                {
                    account:"XHCJ0002",
                    email:"504642566@qq.com",
                    phoneNumber:"18703817652 ",
                    name:"孟而",
                    department:"市场部"
                }
            ],
            rightList:[],
            rightColumns:[
                {
                    label:"姓名",
                    prop:"name"
                }],
            leftColumns:[
                {
                    label:"账号",
                    prop:"account"
                },
                {
                    label:"邮箱",
                    prop:"email"
                },
                {
                    label:"手机号",
                    prop:"phoneNumber"
                },
                {
                    label:"姓名",
                    prop:"name"
                },
                {
                    label:"部门",
                    prop:"department"
                }],
                leftData:[],
                rightData:[],
                leftSearch:"",
                rightSearch:""
        }
    },
    methods:{
        closeDialog(){
            this.$emit("close");
        },
        transferChange(left,right){
            this.leftData=left;
            this.rightData=right;
        },
        seachLeftList(){},
        seachRightList(){}
    }
}
</script>
<style lang="scss">
.el-select-slot{
  font-size: 14px;
  font-weight: 500;
  padding: 10px 0;
  color: #078ecf;
  display: flex;
  justify-content: space-between;
  align-items: center;
    &-header{
        width: 130px;
    }
    .el-input--small{
        width:calc( 100% - 130px);
    }
}
</style>
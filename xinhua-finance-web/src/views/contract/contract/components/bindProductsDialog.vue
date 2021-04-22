<!--产品信息的绑定用户产品弹窗-->
<template>
    <el-dialog
    title="选择产品信息"
    :visible.sync="showBind"
    width="60%"
    >
    <el-radio-group v-model="productType" @change="changeRadio" class="el-product-radio">
        <el-radio :label="1">产品</el-radio>
        <el-radio :label="2">产品项</el-radio>
        <el-radio :label="3">组合产品</el-radio>
    </el-radio-group>
     <el-form :inline="true" :model="formPro" size="mini" label-width="5.7rem" class="el-bind-form">
        <el-form-item label="所属部门">
            <el-select v-model="formPro.department" placeholder="所属部门">
                <el-option label="新华信用" value="1"></el-option>
                <el-option label="新华丝路" value="2"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="产品分类" v-if="productType==1">
            <el-select v-model="formPro.productType" placeholder="产品分类">
                <el-option label="客户端" value="1"></el-option>
                <el-option label="公共端" value="2"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="产品类型" v-if="productType==2">
            <el-select v-model="formPro.productType" placeholder="产品分类">
                <el-option label="行情" value="1"></el-option>
                <el-option label="资讯" value="2"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="产品编码" v-if="productType!=3">
            <el-input v-model="formPro.productNo" placeholder="产品编码"></el-input>
        </el-form-item>
         <el-form-item label="产品名称">
            <el-input v-model="formPro.productName" placeholder="产品名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    <transfer style="padding-top:0;" :leftList="topList" :rightList="bottomList" :letfColumns="topColumns" :rightColumns="bottomColumns" @transferChange="transferChange">
        <span slot="selectLabel">已选产品：</span>
    </transfer>
    <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="sureAndClose" size="small">确 认</el-button>
        <el-button  @click="sureAndClose" size="small">取 消</el-button>
    </div>
    </el-dialog>
</template>
<script>
import transfer from "./transfer/index";
const PRODUCT=[{
                    label:"产品编码",
                    prop:"productNo"
                },
                {
                    label:"产品名称",
                    prop:"productName"
                },
                {
                    label:"所属部门",
                    prop:"department"
                },
                {
                    label:"产品分类",
                    prop:"productType"
                },
                {
                    label:"发布区域",
                    prop:"releaseArea"
                },
                {
                    label:"产品价格",
                    prop:"productPrice",
                    slot:true
                },
                {
                    label:"货币",
                    prop:"currency"
                }];
const PRODUCT_ITEM=[{
                        label:"产品项编码",
                        prop:"productNo"
                    },
                    {
                        label:"产品项名称",
                        prop:"productName",
                        slot:true
                    },
                    {
                        label:"所属部门",
                        prop:"department"
                    },
                    {
                        label:"产品类型",
                        prop:"productType"
                    },
                    {
                        label:"发布范围",
                        prop:"releaseArea"
                    },
                    {
                        label:"发布应用",
                        prop:"releaseApplication"
                    },
                    {
                        label:"产品价格",
                        prop:"productPrice",
                        slot:true
                    },
                    {
                        label:"货币",
                        prop:"currency"
                    }];
const COMBINATION_PRODUCT=[
                    {
                        label:"产品项名称",
                        prop:"productName"
                    },
                    {
                        label:"所属部门",
                        prop:"department"
                    },
                    {
                        label:"产品价格",
                        prop:"productPrice"
                    },
                    {
                        label:"货币",
                        prop:"currency"
                    }]; 


export default {
    name:"bind-products-dialog",
    components:{
        transfer
    },
    props:{
        showBind:{
            type:Boolean,
            default:false
        }
    },
    data(){
        return{
            productType:1,
            formPro: {},
            topList:[
                {
                    "productNo":"AD20200112001",
                    "productName":"红枣价格监测系统",
                    "department":"新华财经",
                    "productType":"客户端",
                    "releaseArea":"中国大陆",
                    "productPrice":"1000.00",
                    "releaseApplication":"专业终端",
                    "currency":"元",
                },
                {
                    "productNo":"AD20200112002",
                    "productName":"红枣价格监测系统",
                    "department":"新华财经",
                    "productType":"客户端",
                    "releaseArea":"中国大陆",
                    "productPrice":"1000.00",
                    "releaseApplication":"专业终端",
                    "currency":"元",
                }
            ],
            bottomList:[],
            topColumns:PRODUCT,
            bottomColumns:[
                {
                    label:"产品编码",
                    prop:"productNo"
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
                    label:"产品价格",
                    prop:"productPrice"
                },
                {
                    label:"货币",
                    prop:"currency"
                },
                 {
                    label:"发布区域",
                    prop:"releaseArea"
                },
            ]
        }
    },
    methods:{
        //查询更新表格数据
        onSubmit(){},
        sureAndClose(){ 
           this.$emit("close",this.bottomList);
        },
        transferChange(rigth, left) {
            this.topList = left;
            this.bottomList = rigth;
        },
        changeRadio(val){
            if(val==1){
                this.topColumns=PRODUCT;
            }else if(val==2){
                this.topColumns=PRODUCT_ITEM;
            }else{
                this.topColumns=COMBINATION_PRODUCT;
            }
        }
    }
}
</script>
<style lang="scss">
.el-product-radio{
    margin-bottom: 1rem;
    padding:0 36px;
}
.el-bind-form{
    display: flex;
    justify-content: left;
    align-items: center;
    width: 100%;
    .el-form-item{
        display: flex!important;
        justify-content: space-between;
        margin-bottom: 0;
    }
}
</style>
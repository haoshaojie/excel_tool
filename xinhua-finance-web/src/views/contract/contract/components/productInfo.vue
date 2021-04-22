<!---产品信息-->
<template>
    <basic-container>
        <div class="el-product-btn">
            <el-button 
                type="primary" 
                size="small"
                icon="el-icon-plus"
                @click="showBind=true"
                >
                绑定产品
            </el-button>
            <el-button 
                type="danger" 
                size="small"
                icon="el-icon-delete"
                plain>
                删除产品
            </el-button>
        </div>
        <avue-crud
        v-model="form"
         :option="option"
         :data="data"
        >
        <template slot="menu">
            <el-button type="text" @click="extension">扩展属性</el-button>
        </template>
        <template slot="productPrice" slot-scope="{row}">
            <div class="price-slot">
                <el-input placeholder="单价" size="small" v-model="row.productPrice"></el-input>
                <el-select placeholder="币种" size="small" v-model="row.currency">
                    <el-option label="元" value="1"></el-option>
                    <el-option label="美元" value="2"></el-option>
                    <el-option label="欧元" value="3"></el-option>
                </el-select>
            </div>
        </template>
        <template slot="number" slot-scope="{row}">
             <el-input placeholder="数量" size="small" v-model="row.number"></el-input>
        </template>
         <template slot="amount" slot-scope="{row}">
             <el-input placeholder="金额" size="small" v-model="row.amount"></el-input>
        </template>
        </avue-crud>
        <div class="el-step-footer">
            <el-button type="primary" size="small" plain @click="back">上一步</el-button>
            <el-button type="primary" size="small" @click="save">保&nbsp;&nbsp;&nbsp;存</el-button>
            <el-button type="primary" size="small" plain @click="next">下一步</el-button>
        </div>
        <!-- 扩展属性 -->
        <el-dialog
         title="扩展属性"
         :visible.sync="showExtend"
         width="30%"
        >
        <div class="el-extend-form">
            <el-form size="small" :inline="true" label-width="100px">
                <el-form-item label="使用端">
                    <el-select multiple placeholder="使用端">
                        <el-option label="专业终端" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="发布区域">
                    <el-select multiple placeholder="发布区域">
                        <el-option label="中国大陆" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="用户流量">
                    <el-input placeholder="用户流量" ></el-input>
                </el-form-item>
                <el-form-item label="接入点">
                   <el-input placeholder="接入点" ></el-input>
                </el-form-item>
            </el-form>
        </div>
         <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="showExtend=false" size="small">确 认</el-button>
            <el-button  @click="showExtend=false" size="small">取 消</el-button>
        </div>
        </el-dialog>
        <bind-products-dialog ref="bindProductsDialog"  :showBind="showBind" @close="closeDailog"></bind-products-dialog>
    </basic-container>
</template>
<script>
import bindProductsDialog from "./bindProductsDialog";
export default {
    name:"product-info",
    components:{
        bindProductsDialog
    },
    data(){
        return{
            showExtend:false,
            showBind:false,
            form:{},
            option:{
                calcHeight: 210,
                searchShow: false,
                searchMenuSpan: 6,
                tip: false,
                border: true,
                index: false,
                viewBtn:false,
                addBtn:false,
                editBtn:false,
                delBtn:true,
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
                        label:"所属部门",
                        prop:"department"
                    },
                     {
                        label:"发布区域",
                        prop:"releaseArea"
                    },
                    {
                        label:"单价",
                        prop:"productPrice",
                        width:200,
                        slot:true,
                        cell: true
                    },
                     {
                        label:"币种",
                        prop:"currency",
                        hide:true,
                        cell: true
                    },
                    {
                        label:"数量",
                        prop:"number",
                        slot:true,
                        cell: true
                    },
                    {
                        label:"金额",
                        prop:"amount",
                        slot:true,
                        cell: true
                    },
                    {
                        label:"起始时间",
                        prop:"startTime"
                    },
                    {
                        label:"终止时间",
                        prop:"endTime"
                    }
                ]
            },
            data:[
                {
                   productNo:"HY990087",
                   productName:"专业终端",
                   department:"新华财经",
                   releaseArea:"中国大陆",
                   productPrice:"",
                   number:"",
                   amount:"",
                   startTime:"2020-12-12 23:00:00",
                   endTime:"2021-12-12 23:00:00"
                }
            ]
        }
    },
    methods:{
        back(){
            this.$emit("update",1);
        },
        save(){

        },
        next(){
            this.$emit("update",3);
        },
        // 扩展属性
        extension(){
            this.showExtend=true;
        },
        closeDailog(list){
            this.showBind=false;
             list.forEach(item => {
                this.data.push(item);
            });
        }
    }
}
</script>
<style lang="scss">
.price-slot{
    display: flex;
}
.el-extend-form{
    .el-form--inline .el-form-item,
    .el-select{
        width: 100%;
    }
    .el-form--inline .el-form-item__content{
        width: calc( 100% - 120px);
    }
}
</style>
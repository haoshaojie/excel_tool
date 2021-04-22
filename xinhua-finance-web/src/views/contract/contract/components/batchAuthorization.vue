<!---批量授权-->
<template>
    <el-dialog
    title="批量授权"
    :visible.sync="showBatch"
    @close="closeDialog"
    >
        <el-steps :active="activeStep">
            <el-step title="上传文件"></el-step>
            <el-step title="校验数据"></el-step>
            <el-step title="授权成功"></el-step>
        </el-steps>
        <div class="el-step-content" v-if="activeStep==1">
            <div class="el-download-action">
                 <el-link type="primary">下载用户授权导入模版</el-link>
            </div>
            <el-upload
                class="el-download-action"
                action="https://jsonplaceholder.typicode.com/posts/"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :before-remove="beforeRemove"
                multiple
                :limit="3"
                :on-exceed="handleExceed"
                :file-list="fileList">
                    <el-button size="small" type="primary">开始上传</el-button>
                    <div slot="tip" class="el-upload__tip">提示：文件后缀名必须为xls或xlsx（即Excel格式），文件大小不得大于2M</div>
            </el-upload>
            <div class="el-step-footer">
                <el-button type="primary" size="small" @click="next(2)">下一步</el-button>
                <el-button type="primary" size="small" plain @click="closeDialog">取&nbsp;&nbsp;&nbsp;消</el-button>
            </div>
        </div>
        <div class="el-step-content" v-if="activeStep==2">
            <div class="el-step-title">校验成功290条，校验失败4条</div>
            <avue-crud 
            :option="optionFail" 
            :table-loading="loading"
            :page.sync="page" 
            :data="dataFail"
            @on-load="onLoadFail">
                <template slot="header">
                    <div class="el-import-list-header">
                        校验失败数据列表 
                        <el-link type="primary" :underline="false" size="small">（{{page.total}}）条</el-link>
                        <el-link type="primary" :underline="false" size="small" style="float:right;">下载失败文件</el-link>
                    </div>
                </template>
                <template slot-scope="{row}" slot="errorTip">
                    <el-link type="danger" class="el-link-slot" :underline="false">{{row.errorTip}}</el-link>
                </template>
            </avue-crud>
            <!--  -->
            <avue-crud 
            :option="optionSuccess" 
            :table-loading="loading"
            :page.sync="page" 
            :data="dataSuccess"
            @on-load="onLoadSuccess">
                <template slot="header">
                    <div class="el-import-list-header">
                        校验成功数据列表 
                        <el-link type="primary" :underline="false" size="small">（{{page.total}}）条</el-link>
                    </div>
                </template>
            </avue-crud>
            <div class="el-step-footer">
                <el-button type="primary" size="small" @click="next(1)">返回重新上传</el-button>
                <el-button type="primary" size="small" plain @click="next(3)">&nbsp;&nbsp;&nbsp;授&nbsp;&nbsp;&nbsp;权&nbsp;&nbsp;&nbsp;</el-button>
            </div>
        </div>
        <div class="el-step-content" v-if="activeStep==3">
            <div class="el-download-action"> 
                <div class="el-download-action-icon"><i class="el-icon-success"></i></div>
                <h3>授权成功！</h3>
                <div class="download-tip">授权用户200</div>
            </div>
            <div class="el-step-footer">
                <el-button size="small" type="primary" @click="closeDialog">&nbsp;&nbsp;&nbsp;完&nbsp;&nbsp;&nbsp;成&nbsp;&nbsp;&nbsp;</el-button>
            </div>
        </div>
    </el-dialog>
</template>
<script>
export default {
    name:"batch-authorization",
    props:{
       showBatch:{
           type:Boolean,
           default:""
       } 
    },
    data(){
        return{
            activeStep:1,
            fileList:[],
            loading:false,
            page: {
                pageSize: 10,
                currentPage: 1,
                total:1
            },
            dataSuccess:[{
                account:'ZLM100081',
                email:'504642566@qq.com',
                phoneNumber:'187029816789',
                name:'张铭',
                department:'业务发展部',
                accountStatus:'启用'
            }],
            dataFail:[{
                errorTip:'该用户账号不存在，excel表格中第5行',
                account:'ZLM100081',
                email:'504642566@qq.com',
                phoneNumber:'187029816789',
                name:'张铭',
                department:'业务发展部',
                accountStatus:'启用'
            }],
            optionSuccess:{
                calcHeight: 100,
                searchShow: true,
                searchMenuSpan: 6,
                border: true,
                index: false,
                viewBtn: false,
                addBtn:false,
                refreshBtn:false,
                columnBtn:false,
                header:false,
                menu:false,
                column: [
                    {
                        label: '用户账号',
                        prop: 'account'
                    },
                    {
                        label: '邮箱',
                        prop: 'email'
                    },
                    {
                        label: '手机号码',
                        prop: 'phoneNumber'
                    },
                    {
                        label: '姓名',
                        prop: 'name'
                    },
                    {
                        label: '部门',
                        prop: 'department'
                    },
                    {
                        label: '用户状态',
                        prop: 'accountStatus'
                    }
                ]
            },
            optionFail:{
                calcHeight: 210,
                searchShow: true,
                searchMenuSpan: 6,
                border: true,
                index: true,
                indexLabel: '序号',
                viewBtn: false,
                addBtn:false,
                refreshBtn:false,
                columnBtn:false,
                header:false,
                menu:false,
                column: [
                    {
                        label: '错误提示',
                        prop: 'errorTip',
                        slot:true
                    },
                    {
                        label: '用户账号',
                        prop: 'account'
                    },
                    {
                        label: '邮箱',
                        prop: 'email'
                    },
                    {
                        label: '手机号码',
                        prop: 'phoneNumber'
                    },
                    {
                        label: '姓名',
                        prop: 'name'
                    },
                    {
                        label: '部门',
                        prop: 'department'
                    },
                    {
                        label: '用户状态',
                        prop: 'accountStatus'
                    }
                ]
            },
        }
    },
    methods:{
        handlePreview(){},
        handleRemove(){},
        beforeRemove(){},
        handleExceed(){},
        next(step){
            this.activeStep=step;
        },
        closeDialog(){
            this.$emit("close");
            this.activeStep=1;
        }
    }
}
</script>
<style lang="scss" scoped>
.el-step-content{
    width: 100%;
    margin:1rem 0;
    .el-step-title{
        text-align: center;
    }
    .el-download-action{
       text-align: center;
       margin-bottom: 1rem;
       &-icon{
            i{
                color: #078ecf;
                font-size: 40px;
                padding:0 30px;
            }
        }
    }
    .el-upload__tip{
       margin-top: 1rem;
    }
    .el-import-list-header{
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #9da7b0;
        .el-link{
            vertical-align: initial;
        }
        a{
            color: #078ecf;
            margin-right: 10px;
        }
    }
}
.download-tip{
    font-size: 12px;
    color: #9da7b0;
}
.el-link-slot{
    font-size: 12px;
}
</style>
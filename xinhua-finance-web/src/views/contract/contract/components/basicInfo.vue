<!--基本信息--->
<template>
    <basic-container>
        <div class="el-basic-title">基本信息</div>
        <div  class="el-basic-content">
            <el-form size="small" ref="basicInfo" label-position="left" label-width="110px" :model="basicInfo" :inline="true" :rules="basicInfoRules">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="合约名称" prop="conName">
                            <el-input placeholder="合约名称" v-model="basicInfo.conName"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="合约范围" prop="conLimit">
                            <el-radio-group v-model="basicInfo.conLimit">
                                <el-radio v-for="(item,i) in conLimitList" :key="i" :label="item.dictKey">{{item.dictValue}}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="合约类型" prop="conType">
                            <el-select placeholder="合约类型" v-model="basicInfo.conType">
                                <el-option v-for="(item,i) in conTypeList" :key="i" :label="item.dictValue" :value="item.dictKey"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="合约状态" prop="conState">
                             <el-radio-group v-model="basicInfo.conState">
                                <el-radio v-for="(item,i) in conStateList" :key="i" :label="item.dictKey">{{item.dictValue}}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="所属部门" prop="deptId">
                            <el-select placeholder="所属部门" v-model="basicInfo.deptId">
                                <el-option v-for="(item,i) in dicDataList" :key="i" :label="item.title" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="客户名称" prop="custName">
                            <el-input v-model="basicInfo.custName" disabled placeholder="请选择客户" style="width: 100%;">
                                <el-button slot="append" icon="el-icon-plus" size="mini" @click="addSuperUnit"></el-button>
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                 <el-row>
                    <el-col :span="12">
                        <el-form-item label="签约乙方" prop="conPartb">
                            <el-input placeholder="签约乙方" v-model="basicInfo.conPartb"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="签约状态" prop="signState">
                            <el-select placeholder="签约状态" v-model="basicInfo.signState">
                                <el-option v-for="(item,i) in signStateList" :key="i" :label="item.dictValue" :value="item.dictKey"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="签约类型" prop="signType">
                            <el-select placeholder="签约类型" v-model="basicInfo.signType">
                                <el-option v-for="(item,i) in signTypeList" :key="i" :label="item.dictValue" :value="item.dictKey"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="续约状态" prop="renewalState">
                             <el-select placeholder="续约状态" v-model="basicInfo.renewalState">
                                <el-option v-for="(item,i) in renewalStateList" :key="i" :label="item.dictValue" :value="item.dictKey"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="签约日期" prop="signDate">
                            <el-date-picker 
                                placeholder="签约日期"
                                type="date"
                                value-format="yyyy-MM-dd"
                                format="yyyy-MM-dd"
                                v-model="basicInfo.signDate">
                            </el-date-picker>
                            </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="合约起止日期" prop="contractTime">
                             <el-date-picker
                                v-model="basicInfo.contractTime"
                                type="daterange"
                                value-format="yyyy-MM-dd"
                                format="yyyy-MM-dd"
                                range-separator="至"
                                start-placeholder="合约起始时间"
                                end-placeholder="合约终止时间">
                                </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="合约金额" prop="conAmount">
                             <el-input placeholder="合约金额" type="number" v-model.number="basicInfo.conAmount">
                                <template slot="append">元</template>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="合约当年期金额" prop="conCurrAmount">
                            <el-input placeholder="合约当年期金额" type="number" v-model.number="basicInfo.conCurrAmount">
                                <template slot="append">元</template>
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <el-form-item label="具体服务要求" prop="conDemand">
                            <el-input 
                            placeholder="具体服务要求" 
                            type="textarea" 
                            v-model="basicInfo.conDemand"
                            :autosize="{minRows:4,maxRows:6}"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <el-form-item label="合约附件">
                           <el-upload
                                action="/api/cnfic-resource/oss/file/putFile"
                                :headers="headerObj"
                                :on-preview="handlePreview"
                                :on-remove="handleRemove"
                                :before-remove="beforeRemove"
                                :before-upload="onBeforeUpload"
                                :on-success="handleSuccess"
                                multiple
                                :on-exceed="handleExceed"
                                accept=".pdf, .doc, .docx, .rar, .zip"
                                :limit="5"
                                :file-list="basicInfo.fileList">
                                <el-button size="small" type="primary">添加附件</el-button>
                                合同附件支持上传.docx .doc .pdf .rar .zip格式，最多支持5个附件，单个附件不大于2M
                                </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="合约联系人">
                           <el-button icon="el-icon-plus" class="add-contacts" @click="openLinkPersonDialog">选择联系人({{basicInfo.contactsNumber}})</el-button>
                           <avue-crud
                            :option="option"
                            :data="basicInfo.custContract"
                           ></avue-crud>
                        </el-form-item>
                    </el-col>
                </el-row>
                 <el-row>
                    <el-col :span="24">
                        <el-form-item class="el-step-footer">
                            <el-button type="primary" @click="saveBasic">保&nbsp;&nbsp;&nbsp;存</el-button>
                             <el-button type="primary" plain @click="next">下一步</el-button>
                        </el-form-item>
                    </el-col>
                 </el-row>
            </el-form>
        </div>
        <customer-dialog ref="customerDialog" :customerDialogFlag="customerDialogFlag" @acceptSuperUnit="acceptSuperUnit"
                     @cancelCustomerDialog="cancelCustomerDialog"></customer-dialog>
        <link-person-dialog ref="linkPersonDialog"
                        :showLinkPersonDialog="showLinkPersonDialog"
                        :customerId="basicInfo.custId"
                        @chooseLinkPerson="chooseLinkPerson"
                        @cancelLinkPersonDialog="cancelLinkPersonDialog"/>
    </basic-container>
</template>
<script>
import {getDictByCode} from "@/api/system/dict";
import customerDialog from "@/views/contract/customer/customerDialog";
import linkPersonDialog from "@/views/contract/userAccount/linkPersonDialog";
import {getContractList} from "@/api/contract/user/orguser";
import {getDept} from "@/api/system/dept";
import {getToken} from '@/util/auth';
import website from "@/config/website";
export default {
    name:"basic-info",
    components: {
      customerDialog,
      linkPersonDialog
    },
    props:{
      dicDataList:[],
      basicInfo:{}
    },
    data(){
        return{
            conLimitList:[],//合约范围
            conTypeList:[],//合约类型
            conStateList:[],//合约状态
            signStateList:[],//签约状态
            signTypeList:[],//签约类型
            renewalStateList:[],//续约状态
            customerDialogFlag:false,
            showLinkPersonDialog:false,
            headerObj: {
                "Authorization": `Basic ${Base64.encode(`${website.clientId}:${website.clientSecret}`)}`,
                "TongTech-Auth": 'Bearer ' + getToken(),
                "Blade-Auth": 'Bearer ' + getToken()
            },//附件上传请求头参数
            basicInfoRules:{
                conName:[{required: true,message: '请输入合约名称',trigger: "blur"}],
                conLimit:[{required: true,message: '请选择合约范围',trigger: "change"}],
                conType:[{required: true,message: '请选择合约类型',trigger: "change"}],
                conState:[{required: true,message: '请选择合约状态',trigger: "change"}],
                deptId:[{required: true,message: '请选择所属部门',trigger: "change"}],
                custName:[{required: true,message: '请选择客户',trigger: "change"}],
                signState:[{required: true,message: '请选择签约状态',trigger: "change"}],
                signType:[{required: true,message: '请选择签约类型',trigger: "change"}],
                renewalState:[{required: true,message: '请选择续约状态',trigger: "change"}],
                signDate:[{required: true,message: '请选择签约日期',trigger: "blur"}],
                contractTime:[{required: true,message: '请选择合约起止日期',trigger: "blur"}],
            },
            option:{
                calcHeight: 210,
                searchShow: true,
                searchMenuSpan: 6,
                tip: false,
                border: true,
                index: false,
                header:false,
                menu:false,
                selection: false,
                column:[
                    {
                        prop:"contactPerson",
                        label:"联系人",
                    },
                    {
                        prop:"contactType",
                        label:"联系人类型",
                    },
                    {
                        prop:"contactPhone",
                        label:"联系方式",
                    },
                    {
                        prop:"contactEmail",
                        label:"邮箱",
                    },
                    {
                        prop:"contactDept",
                        label:"部门",
                    },
                    {
                        prop:"contactJob",
                        label:"职务",
                    },
                     {
                        prop:"custManager",
                        label:"客户经理",
                    },
                    {
                        prop:"custOrgName",
                        label:"所属机构",
                    }
                ]
            }
        }
    },
    mounted(){
      this.onLoad();
    },
    methods:{
      handlePreview(){},
      handleRemove(file){
        let index = this.basicInfo.fileList.findIndex( fileItem => {
            return fileItem.uid === file.uid
        })
        this.basicInfo.fileList.splice(index, 1)
      },
      beforeRemove(){},
      handleSuccess(response, file, fileList){
          this.basicInfo.fileList = fileList;
      },
      //上传之前校验方法
      onBeforeUpload(file){
        var fileName = file.name;
        var filextension=fileName.substring(fileName.lastIndexOf("."),fileName.length);
        filextension=filextension.toLowerCase();
        var typeRight=true;
        if((filextension!='.pdf')&&(filextension!='.doc')&&(filextension!='.docx')&&(filextension!='.zip')&&(filextension!='.rar')){
          typeRight = false;
          this.$message.error('合同附件仅支持上传.doc .docx .pdf .rar .zip格式');
        }
        const isLt1M = file.size / 1024 / 1024 < 2;
        if (!isLt1M) {
            this.$message.error('上传文件大小不能超过 2MB!');
        }
        return typeRight && isLt1M;
      },
      //附件超出个数方法
      handleExceed(){
          this.$message.error('最多支持5个附件');
      },
      saveBasic(){
        this.$refs['basicInfo'].validate((valid)=>{
          if(valid){
            this.$emit("saveBasic");
          }else{
            this.$message.error('请输入必填字段');
          }
        })
      },
      next(){
          this.$emit("update",2);
      },
      //上级单位弹窗
      addSuperUnit() {
        this.customerDialogFlag = true
      },
      //接受选择客户
      acceptSuperUnit(row) {
        this.basicInfo.custName = row.custName;
        this.basicInfo.custId = row.id;
        this.data = [];
        //查询联系人信息，如果只有一个联系人，直接放到联系人列表里
        getContractList(row.id).then(res => {
          this.basicInfo.contactsNumber = res.data.data.length;
          if(res.data.data.length==1){
            this.basicInfo.custContract = res.data.data;
            getDept(this.basicInfo.custContract[0].custOrg).then(r => {
              this.basicInfo.custContract.custOrgName = r.data.data.deptName;
            },error =>{
              this.$message.error('所属机构未找到');
            });
          }
        })
      },
      cancelCustomerDialog() {
        this.customerDialogFlag = false
      },
      //打开联系人选择页面
      openLinkPersonDialog() {
        if(this.basicInfo.custId == ""){
          this.$message.error('请先选择客户');
          return;
        }
        this.showLinkPersonDialog = true;
      },
      chooseLinkPerson(row) {
        this.basicInfo.custContract = [];
        this.basicInfo.custContract.push(row);
        this.basicInfo.custContractId = row.id;
      },
      cancelLinkPersonDialog() {
        this.showLinkPersonDialog = false;
      },
      onLoad() {
        getDictByCode(this.CONSTANT.CONTRACT_SCOPE).then(res => {
          this.conLimitList=res.data.data
        });
        getDictByCode(this.CONSTANT.CONTRACT_TYPE).then(res => {
          this.conTypeList=res.data.data
        });
        getDictByCode(this.CONSTANT.CONTRACT_STATE).then(res => {
          this.conStateList=res.data.data
        });
        getDictByCode(this.CONSTANT.SIGN_STATE).then(res => {
          this.signStateList=res.data.data
        });
        getDictByCode(this.CONSTANT.SIGN_TYPE).then(res => {
          this.signTypeList=res.data.data
        });
        getDictByCode(this.CONSTANT.RENEWAL_STATE).then(res => {
          this.renewalStateList=res.data.data
        });
      }
    }
}
</script>

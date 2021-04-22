<template>
  <el-dialog
    :title="title"
    :visible.sync="addEmailOrPhoneDialogFlag"
    @close="closeDialog"
  >
    <el-form :model="dynamicValidateForm" ref="dynamicValidateForm" label-width="100px" >

      <el-form-item
        v-for="(domain, index) in dynamicValidateForm.domains"
        :label="obj.label"
        :key="domain.key"
        :prop="'domains.' + index + '.value'"
        :rules="rules"
      >
        <el-col :span="11">
        <el-input  v-model="domain.value" :maxlength="maxlength" show-word-limit></el-input>
        </el-col>
        <el-col :span="1"></el-col>
        <el-col :span="10">
        <el-button @click.prevent="removeDomain(domain)">删除</el-button>
        </el-col>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('dynamicValidateForm')">提交</el-button>
        <el-button @click="addDomain">新增{{obj.label}}</el-button>
        <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
  export default {
    name: "addEmailOrPhone",
    props: {
      email:"",
      emailOrPhoneData:{
        type:Object,
        default:()=>({})
      }
    },
    data(){
      return {
        rules:[],
        maxlength:10,
        addEmailOrPhoneDialogFlag:false,
        title:"",
        obj:{},
        rowData:"",
        dynamicValidateForm:{
          domains: [{
            value: ''
          }],
        },
      }
    },
    methods: {
      closeDialog() {
        this.addEmailOrPhoneDialogFlag=false;
        this.resetForm("dynamicValidateForm");
        this.dynamicValidateForm={
          domains: []
        };
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var arrValue=[];
            this.dynamicValidateForm.domains.forEach(item=>{
              arrValue.push(item.value);
            })
            for (const key in arrValue) {
              if(arrValue[key]==""){
                arrValue.splice(key,1);
              }
            }
            this.obj.label=="联系方式"?this.rowData.contactPhone=arrValue.join(","):this.rowData.contactEmail=arrValue.join(",");
            this.closeDialog();
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.dynamicValidateForm={
          domains: [{
            value: ''
          }]
        };
        // this.$refs[formName].resetFields();
      },
      removeDomain(item) {
        var index = this.dynamicValidateForm.domains.indexOf(item)
        if (index !== -1) {
          this.dynamicValidateForm.domains.splice(index, 1)
        }
      },
      addDomain() {
        this.dynamicValidateForm.domains.push({
          value: '',
          key: Date.now()
        });
      },
      openType(column,row){
        this.addEmailOrPhoneDialogFlag=true;
        this.obj=column;
        this.rowData=row;
        this.title=`新增${this.obj.label}`;
        this.dynamicValidateForm.domains=[];
        if (column.label=='联系方式'){
          row.contactPhone.split(",").forEach(item=>{
            this.dynamicValidateForm.domains.push({
              value: item
            });
          })
        }else {
          row.contactEmail.split(",").forEach(item=>{
            this.dynamicValidateForm.domains.push({
              value: item
            });
          })
        }
        //验证
        if (column.label=='联系方式'){
          this.rules=[{
            required: true, message:`${this.obj.label}不能为空`, trigger: 'blur'
          }];
          this.maxlength=20;
        }else {
          this.rules=[
            { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
            {
              required: true, message:`${this.obj.label}不能为空`, trigger: 'blur'
            }
          ];
          this.maxlength=50;
        }

      }
    }
    }

</script>

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
      addEmailOrPhoneDialogFlag: {
        type: Boolean,
        default: false
      },
      emailOrPhoneData:{
        type:Object,
        default:()=>({})
      }
    },
    data(){
      return {
        title:"",
        rules:[],
        maxlength:10,
        obj:{},
        dynamicValidateForm:{
          domains: [{
            value: ''
          }],
        },
      }
    },
    methods: {
      closeDialog() {
        this.dynamicValidateForm.domains=[{
          value:''
        }];
        this.$emit("cancelEmailOrPhoneDialog");
      },
      submitForm(formName) {


        this.$refs[formName].validate((valid) => {
          if (valid) {
            var arrValue=[];
            this.dynamicValidateForm.domains.forEach(item=>{
              arrValue.push(item.value);
            })
            this.obj.arrValue=arrValue.join(",");
            this.$emit("accept",this.obj)
            this.closeDialog();
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.dynamicValidateForm.domains=[{
          value:''
        }];
        // this.$refs[formName].resetFields();
      },
      removeDomain(item) {
        var index = this.dynamicValidateForm.domains.indexOf(item)
        if (index !== -1) {
          this.dynamicValidateForm.domains.splice(index, 1)
        }
      },
      addDomain() {
        if (this.dynamicValidateForm.domains&&this.dynamicValidateForm.domains.length>4){
          this.$message.warning(`最多添加5个${this.obj.label}`)
          return;
        }
        this.dynamicValidateForm.domains.push({
          value: '',
          key: Date.now()
        });
      },
      openType(data){
        if (data.value){
          this.dynamicValidateForm.domains=[];
          data.value.split(",").forEach(item=>{
            this.dynamicValidateForm.domains.push({
              value:item,
            });
          })

        }
        this.obj=data;
        this.title=`新增${this.obj.label}`;
        if (data.label!='邮件'){
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

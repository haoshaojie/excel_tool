<template>
  <div class="login-container">
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
             class="demo-ruleForm login-page">
      <el-form-item label="账号" prop="account">
        <el-input v-model.number="ruleForm.account"></el-input>
      </el-form-item>
      <el-form-item label="原始密码" prop="oldPass">
        <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="pass">
        <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">修改密码</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: "index",
    data() {
      var checkAge = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入账号'));
        }
      };
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入新密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        ruleForm: {
          pass: '',
          checkPass: '',
          age: ''
        },
        rules: {
          account: [
            {required: true, message: '请输入账号', trigger: 'blur'},
            {validator: checkAge, trigger: 'blur'}
          ],
          oldPass: [
            {required: true, message: '请输入原始密码', trigger: 'blur'}
          ],
          pass: [
            {validator: validatePass, trigger: 'blur'},
            {required: true, message: '请输入新密码', trigger: 'blur'}
          ],
          checkPass: [
            {validator: validatePass2, trigger: 'blur'},
            {required: true, message: '请输入确认密码', trigger: 'blur'}
          ],

        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<style scoped>
  .login-container {
    display: flex;
    align-items: center;
    position: relative;
    width: 100%;
    height: 100%;
    margin: 0 auto;
    background: url("http://www.17sucai.com/preview/242158/2015-01-10/%E7%99%BB%E5%BD%95/images/cloud.jpg") 0 bottom repeat-x #049ec4;
    animation: animate-cloud 20s linear infinite;
  }

  .login-page {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    margin: 180px auto;
    width: 30%;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login-form {
    margin: 10px 0;

  i {
    color: #333;
  }

  .el-form-item__content {
    width: 100%;
  }

  .el-form-item {
    margin-bottom: 12px;
  }

  .el-input {

  input {
    padding-bottom: 10px;
    text-indent: 5px;
    background: transparent;
    border: none;
    border-radius: 0;
    color: #333;
    border-bottom: 1px solid rgb(235, 237, 242);
  }

  .el-input__prefix {

  i {
    padding: 0 5px;
    font-size: 16px !important;
  }

  }
  }
  }
</style>

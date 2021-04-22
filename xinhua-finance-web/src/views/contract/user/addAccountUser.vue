<template>
  <el-dialog
    title="生成账号信息"
    :visible.sync="addAccountUserFlag"
    width="60%" center
    @close="cancelDialog">
    <el-form ref="form" :model="form" label-width="80px">

      <el-form-item label="姓名">
        <el-input v-model="form.userName"></el-input>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="手机号">
            <el-input v-model="form.userPhone"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱">
            <el-input v-model="form.userEmail"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item >
        <el-button type="primary" @click="confirm">确认</el-button>
        <el-button @click="cancelDialog">取消</el-button>
      </el-form-item>
    </el-form>

  </el-dialog>
</template>

<script>
  export default {
    name: "addAccountUser",
    props: {
      addAccountUserFlag: {
        type: Boolean,
        default: false
      },
    },
    data(){
      return {
        form:{
          userName: '',
          userPhone: '',
          userEmail: '',
        }
      }
    },
    methods:{
      cancelDialog(){
       this.$emit("cancelDialog")
      },
      confirm(){
        console.log("confirm")
      }
    }
  }
</script>

<style scoped>

</style>

<!--密码重置的弹窗-->
<template>
  <el-dialog
    title="重置密码"
    center
    :visible.sync="showResetPasswordDialog"
    @close="closeDialog">
    <p>确认将该账号重置为初始密码吗？</p>
    <template>
      <el-checkbox v-model="email" v-if="hasEmail">发送邮件通知</el-checkbox>
      <el-checkbox v-model="phone" v-if="hasPhone">发送短信通知</el-checkbox>
    </template>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleResetPassword">确 定</el-button>
      <el-button @click="showResetPasswordDialog = false">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import {resetPassword} from "@/api/contract/userAccount/account";

export default {
  name: "reset-password-dialog",
  props: {
    showResetPasswordDialog: {
      type: Boolean,
      default: false
    },
    accountId: {
      type: String,
      default: ""
    },
    hasPhone: {
      type: Boolean,
      default: false
    },
    hasEmail: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      email: false,
      phone: false,
    }
  },
  mounted() {
  },
  methods: {
    handleResetPassword() {
      if (this.resetPasswordData) {
        resetPassword({
          email: this.email,
          phone: this.phone,
          accId: this.accountId
        }).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.closeDialog();
        }, error => {
          window.console.log(error);
        });
      }
    },
    closeDialog() {
      this.email = false;
      this.phone = false;
      this.$emit("close");
    },
  }
}
</script>
<style lang="scss">

</style>

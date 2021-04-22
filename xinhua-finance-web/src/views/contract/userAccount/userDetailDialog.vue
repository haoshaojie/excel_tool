<!--用户账号详情-->
<template>
  <el-dialog
    title="用户账号详情"
    :visible.sync="showDetail"
    @close="closeDialog">
    <enterprise-account-detail ref="enterpriseAccountDetail" v-if="type=='enterprise'"/>
    <personalAccountDetail ref="personalAccountDetail"
                           :personal-account-detail="personalAccountDetail"
                           v-else/>
  </el-dialog>
</template>
<script>
import enterpriseAccountDetail from "./enterpriseAccountDetail";
import personalAccountDetail from "./personalAccountDetail";


export default {
  name: "user-detail-dialog",
  components: {
    enterpriseAccountDetail,
    personalAccountDetail
  },
  props: {
    showDetail: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: "enterprise"
    },
    enterpriseDetail: {
      type: Object,
      default: null,
    },
    personalAccountDetail: {
      type: Object,
      default: null,
    }
  },
  data() {
    return {}
  },
  methods: {
    closeDialog() {
      this.$emit("close");
    }
  }
}
</script>
<style lang="scss">
  .el-basic-detail {
    width: 100%;
    background-color: #FFFFFF;
    color: #222222;
    tr td {
      width: 25%;
      height: 40px;
      box-sizing: border-box;
      border: 1px solid #D7D9E2;
      line-height: 36px;
      font-size: 13px !important;
      text-align: center;
    }

    tr td:nth-child(odd) {
      font-weight: bold;
      background-color: #E3F2FD;
    }
  }

  .el-table-title {
    margin: 1rem 0;
    font-size: 14px;
    color: #333;
  }
</style>

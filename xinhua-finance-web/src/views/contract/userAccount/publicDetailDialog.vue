<!--公共账号详情的弹窗-->
<template>
  <el-dialog
    title="公共账号详情"
    :visible.sync="showPublicDetailDialog"
    @close="closeDialog">
    <table class="el-basic-detail">
      <tr>
        <td>账号</td>
        <td>xhcj_z9id715g</td>
        <td>并发</td>
        <td>504642566@qq.com</td>
      </tr>
      <tr>
        <td>有效IP</td>
        <td colspan="3">GRYQ00005</td>
      </tr>
      <tr>
        <td>客户编号</td>
        <td>未认证</td>
        <td>客户名称</td>
        <td>未认证</td>
      </tr>
      <tr>
        <td>创建人</td>
        <td>未认证</td>
        <td>创建时间</td>
        <td>未认证</td>
      </tr>
      <tr>
        <td>最后修改人</td>
        <td>未认证</td>
        <td>最后修改时间</td>
        <td>未认证</td>
      </tr>
    </table>
    <div class="el-table-title">联系人信息</div>
    <avue-crud
      :option="option"
      :data="data"
    ></avue-crud>
    <span slot="footer" class="dialog-footer">
      <el-button @click="showPublicDetailDialog = false">关 闭</el-button>
    </span>
  </el-dialog>
</template>
<script>

export default {
  name: "public-detail-dialog",
  props: {
    showPublicDetailDialog: {
      type: Boolean,
      default: false
    },
  },
  data() {
    return {
      loading: false,
      option: {
        refreshBtn: false,
        columnBtn: false,
        header: false,
        border: true,
        menu: false,
        column: [
          {
            label: '联系人',
            prop: 'contractCode'
          },
          {
            label: '联系人类型',
            prop: 'contractName'
          },
          {
            label: '联系方式',
            prop: 'amount'
          },
          {
            label: '邮箱',
            prop: 'contractStatus'
          },
          {
            label: '部门',
            prop: 'contractStatus'
          },
          {
            label: '职务',
            prop: 'contractStatus'
          },
          {
            label: '客户经理',
            prop: 'contractStatus'
          },
          {
            label: '所属机构',
            prop: 'contractStatus'
          },
        ]
      },
      data: [],
    }
  },
  mounted() {
  },
  methods: {

    closeDialog() {
      this.$emit("close");
    },
  }
}
</script>
<style lang="scss">

</style>

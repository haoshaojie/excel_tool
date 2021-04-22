<template>
  <el-dialog
    title="title"
    :visible.sync="superUnitDialog"
    :show-close=false
    width="80%" center
  >
    上级单位弹窗

    <span slot="footer" style="text-align: center" class="dialog-footer">
      <el-button @click="">取 消</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    name: "superUnitDialg",
    props: {
      superUnitDialog: {
        type: Boolean,
        default: false
      }
    },
  }

</script>

<style scoped>

</style>

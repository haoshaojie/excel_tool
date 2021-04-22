<template>
  <el-row>
    <el-col>
      <transferTable
        :checkbox="true"
        :dataList="leftList"
        :columns="columns"
        @selectionChange="selectionChange"
      ></transferTable>
    </el-col>
  </el-row>
</template>
<script>
import transferTable from "./transfer-table";
export default {
  components: { transferTable },
  props: {
    leftList: [],
    rightList: [],
    columns: [],
    rightColumns: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
    };
  },
  methods: {
    toLeftAll() {
      if (this.rightList.length > 0) {
        this.leftList.unshift(...this.rightList);
        this.rightList.splice(0, this.rightList.length);
        this.$emit("transferChange", this.rightList, this.leftList);
      }
    },
    toRightAll() {
      if (this.leftList.length > 0) {
        this.rightList.unshift(...this.leftList);
        this.leftList.splice(0, this.leftList.length);
        this.$emit("transferChange", this.rightList, this.leftList);
      }
    },
    rowClick(row, type, index) {
      if (type === "right") {
        this.leftList.unshift(row);
        this.rightList.splice(index, 1);
      } else if (type === "left") {
        this.rightList.unshift(row);
        this.leftList.splice(index, 1);
      }
      this.$emit("transferChange", this.rightList, this.leftList);
    },
    selectionChange(selection){
      this.$emit("selectionChange", selection);
    }
  },
};
</script>
<style lang="scss">
.el-arrow-button {
  margin-top: 20px;
  div {
    margin-bottom: 10px;
  }
}
</style>

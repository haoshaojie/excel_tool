<template>
  <el-dialog title="处理"
             append-to-body
             :visible.sync="dealBox"
             @close="closeDialog"
             width="50%">
    <avue-form ref="form" v-model="obj" :option="option" @submit="submit">
      <template slot-scope="scope" slot="menuForm">
        <el-button @click="closeDialog">取消</el-button>
      </template>
    </avue-form>
  </el-dialog>
</template>

<script>
  import {update} from "@/api/contract/goods/infogoods";

  export default {
    name: "infoGoodsDialog",
    props: {
      dealBox: {
        type: Boolean,
        default: false
      },
      infoIds: {},
      reportIds: {},
      id:{type: Number},
      type:{type: Number}
    },
    data() {
      return {
        obj: {},
      }
    },
    methods: {
      closeDialog() {
        this.obj.disposeSuggest = null;
        this.id = null;
        this.$emit("close", false);
      },
      submit(form, done) {
        console.log(this.id,"++",this.type);
        form.infoIds = this.infoIds;
        form.reportIds = this.reportIds;
        if(this.type ===1 ){
          form.infoIds = this.id;
          form.reportIds = "";
        }else if(this.type === 2){
          form.infoIds = "";
          form.reportIds = this.id;
        }
        update(form.infoIds,form.reportIds,form.disposeSuggest).then(() => {
          done();
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.closeDialog();
        }, error => {
          window.console.log(error);
        });
      },
    },
    computed: {
      option() {
        return {
          labelPosition: 'left',
          labelSuffix: '：',
          labelWidth: 120,
          gutter: 0,
          menuBtn: true,
          submitBtn: true,
          submitText: '确定',
          emptyBtn: false,
          emptyText: '清空',
          menuPosition: 'center',
          detail: false,
          column: [{
            type: 'textarea',
            label: '处理意见',
            span: 24,
            display: true,
            prop: 'disposeSuggest',
            maxlength: 200,
            required: true,
            rules: [
              {
                required: true,
                message: '请输入处理意见'
              }
            ]
          }]
        }
      }
    },
  }
</script>

<style scoped>

</style>

<template>
  <el-dialog title="添加"
             append-to-body
             :visible.sync="propertyBox"
             @close="closeDialog"

             width="90%"
             hight="80%">
    <template>
      <el-form :inline="true" :model="formPro" size='mini' class="demo-form-inline">
        <el-form-item label="属性名称">
          <el-input v-model="formPro.propName" placeholder="属性名称"></el-input>
        </el-form-item>
        <el-form-item label="属性编码">
          <el-input v-model="formPro.propCode" placeholder="属性编码"></el-input>
        </el-form-item>
        <el-form-item label="属性类型">
          <el-select v-model="formPro.propType" placeholder="属性类型">
            <el-option
              v-for="item in propTypeList"
              :key="item.dictKey"
              :label="item.dictValue"
              :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <transfer :leftList="initList"
                :rightList="proList"
                :columns="columns" @transferChange="transferChange"></transfer>
    </template>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" :disabled="isDisabled" @click="save">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import transfer from "@/components/transfer/index";
  import {add, getListAdd} from "@/api/contract/product/category-property";
  import {getDictByCode} from "@/api/system/dict";

  export default {
    name: "category-property-dialog",
    components: {transfer},
    props: {
      propertyBox: {
        type: Boolean,
        default: false
      },
      cateId: {
        type: Number
      },
      page: {}
    },
    data() {
      return {
        propTypeList: [],
        showTypeList: [],
        formPro: {},
        isDisabled: false,
        columns: [{
          prop: "propName",
          label: "属性名称"
        }, {
          prop: "propCode",
          label: "属性编码",
        }, {
          prop: "propType",
          label: "属性类型",
          formatter: (row) => {
            this.propTypeList.forEach(item => {
              if (item.dictKey == row.propType) {
                row.propType = item.dictValue;
                return item.dictValue;
              }
            });
            return row.propType;
          }
        }, {
          prop: "showType",
          label: "表现形式",
          formatter: (row) => {
            this.showTypeList.forEach(item => {
              if (item.dictKey == row.showType) {
                row.showType = item.dictValue;
                return item.dictValue;
              }
            });
            return row.showType;
          }
        }],
        initList: [],
        proList: []
      }
    },
    computed: {
      ids() {
        let ids = [];
        this.proList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      },
    },
    methods: {
      closeDialog() {
        this.initList = [];
        this.proList = [];
        this.formPro = {};
        this.propertyBox = false;
        this.$emit("close", false);
      },
      transferChange(rigth, left) {
        this.initList = left;
        this.proList = rigth;
      },
      onSubmit() {
        this.onLoad();
      },
      save() {
        this.isDisabled = true;
        const row = {};
        if (this.ids == null || this.ids == "") {
          if (this.proList.length === 0) {
            this.$message.warning("请选择至少一条数据");
            this.isDisabled = false;
            return;
          }
        }
        row.propIds = this.ids;
        row.cateId = this.cateId;
        console.log(row);
        add(row).then(() => {
          this.onLoad();
          this.$emit("onLoad", this.page);
          this.proList = [];
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.closeDialog();
        }, error => {
          window.console.log(error);
          // loading();
        });
        this.isDisabled = false;
      },
      onLoad(params = {}) {
        console.log("加载数据", params);
        getDictByCode(this.CONSTANT.PROP_TYPE).then(res => {
          const data = res.data.data;
          this.propTypeList = data;
        });
        getDictByCode(this.CONSTANT.SHOW_TYPE).then(res => {
          const data = res.data.data;
          this.showTypeList = data;
        });
        this.formPro.cateId = this.cateId;
        this.formPro.exclude = this.ids;
        getListAdd(Object.assign(this.formPro, params)).then(res => {
          const data = res.data.data;
          this.initList = data;
        });
      },
    }
  }
</script>

<style scoped>

</style>

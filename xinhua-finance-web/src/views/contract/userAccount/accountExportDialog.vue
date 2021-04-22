<template>
  <el-dialog
    title="导出"
    :visible.sync="exportDialogFlag"
    width="80%" center
    @close="cancelDialog">
    <el-form ref="form" :model="form" label-width="180px">
      <el-form-item label="导出信息选择">
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <div style="margin: 15px 0;"></div>
        <el-checkbox-group v-model="form.exportField">
          <el-checkbox v-for="item in columns" :label="item.prop" :key="item.prop">{{item.label}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-divider></el-divider>
      <div v-if="type == 'personal'">
        <el-form-item>
          <el-checkbox v-model="form.orderChecked">订单信息</el-checkbox>
        </el-form-item>
      </div>
      <div v-if="type=='enterprise'">
        <el-form-item>
          <el-checkbox v-model="form.contractChecked">合约信息（合约编码、合约名称、合约状态、合约起止时间）</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.productChecked">产品信息（账号访问产品权限列表）</el-checkbox>
        </el-form-item>
      </div>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="exportData">确 定</el-button>
      <el-button @click="cancelDialog">取 消</el-button>
    </span>
  </el-dialog>
</template>

<script>
import {exportData} from "@/api/contract/customer/orgcustomer";
import qs from 'qs'

const PERSONAL_EXPORT_URL = "/api/cnfic-contract-manage/personalaccount/export-personalaccount?";
const ENTERPRISE_EXPORT_URL = "/api/cnfic-contract-manage/enterpriseaccount/export-enterpriseaccount?";

export default {
  name: "account-export-dialog",
  props: {
    exportDialogFlag: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: "enterprise",
    },
    exportColumns: {
      type: Array,
      default: () => [],
    },
  },
  mounted() {
    for (let i = 0; i < this.columns.length; i++) {
      this.column.push(this.columns[i].value);
    }
  },
  data() {
    return {
      form: {
        exportField: [],
        fieldName: [],
        orderChecked: false,
        contractChecked: false,
        productChecked: false,
      },
      checkAll: false,
      isIndeterminate: true,
      column: [],
      columns: [
        {
          label: '客户编码',
          prop: 'custCode'
        }, {
          label: '客户名称',
          prop: 'custName'
        }, {
          label: '上级单位',
          prop: 'superUnitName'
        }, {
          label: '所属行业',
          prop: 'custIndustryName'
        }, {
          label: '所属地域',
          prop: 'regionName'
        }, {
          label: '客户类型',
          prop: 'custTypeName'
        }, {
          label: '详细地址',
          prop: 'custAddress'
        }, {
          label: '邮政编码',
          prop: 'postcode'
        }, {
          label: '公司电话',
          prop: 'comPhone'
        }, {
          label: '公司网址',
          prop: 'comWebsite'
        }, {
          label: '客户状态',
          prop: 'custStateName'
        },
      ],
    };
  },
  created() {
    this.columns = this.exportColumns;
    this.checkAll = false;
    this.form = {
      exportField: [],
      fieldName: [],
      orderChecked: false,
      contractChecked: false,
      productChecked: false,
    };
  },
  methods: {
    cancelDialog() {
      this.checkAll = false;
      this.form = {
        exportField: [],
        orderChecked: false,
        contractChecked: false,
        productChecked: false,
      };
      this.$emit('close');
    },
    exportData() {
      var form = this.form;
      let checkFlag = false, url = "";
      if (this.type == 'personal') {
        checkFlag = form.exportField.length == 0 && !form.orderChecked;
        url = PERSONAL_EXPORT_URL;
      } else {
        checkFlag = form.exportField.length == 0 && !form.contractChecked && !form.productChecked;
        url = ENTERPRISE_EXPORT_URL;
      }
      if (checkFlag) {
        this.$message({
          message: "请选择要导出的列",
          type: "error"
        });
        return;
      }
      window.open(url + qs.stringify(this.form));
      this.cancelDialog();
    },
    handleCheckAllChange(val) {
      console.log("this.form.exportField", this.form.exportField)
      this.form.exportField = val ? this.columns.map(item => item.prop) : [];
      this.form.fieldName = val ? this.columns.map(item => item.label) : [];
      if (val) {
        this.form.orderChecked = true;
        this.form.contractChecked = true;
        this.form.productChecked = true;
      } else {
        this.form.orderChecked = false;
        this.form.contractChecked = false;
        this.form.productChecked = false;
      }
      this.isIndeterminate = false;
    },

  }
};
</script>

<!--公共账号新增/编辑的表单-->
<template>
  <basic-container>
    <el-dialog
      :title="publicFormTitle"
      :visible.sync="showPublicFormDialog"
      @close="closePublicFormDialog">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="客户名称">
          <el-row>
            <el-col :span="20">
              <el-input v-model="form.superUnitName" disabled placeholder="请选择客户"/>
            </el-col>
            <el-col :span="4">
              <i class="el-icon-plus" style="font-size: 14px; color: #409EFF;cursor:pointer"
                 @click="showCustomerData">选择客户</i>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="有效IP">
          <el-button size="medium" icon="el-icon-circle-plus" type="primary" @click="addIpInput">添加IP</el-button>
          <el-row v-for="(item,index) in ipContainer">
            <el-col :span="20">
              <el-input placeholder="请输入IP"/>
            </el-col>
            <el-col :span="4">
              <i class="el-icon-remove" style="font-size: 40px; color: #F56C6C;cursor:pointer"
                 @click="removeIpInput(index)"/>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="并发">
          <el-input-number v-model="form.num8" controls-position="right" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="联系人">
          <el-row>
            <el-col :span="20">
              <avue-crud
                :option="option"
                :data="data"/>
            </el-col>
            <el-col :span="4" v-if="showContractIcon">
              <i class="el-icon-plus" style="font-size: 14px; color: #409EFF;cursor:pointer"
                 @click="openLinkPersonDialog">选择联系人</i>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="publicFormSubmit">保存</el-button>
          <el-button type="primary" @click="closePublicFormDialog">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <customer-dialog ref="customerDialog"
                     :customerDialogFlag="customerDialogFlag"
                     @acceptSuperUnit="acceptSuperUnit"
                     @cancelCustomerDialog="cancelCustomerDialog"/>
    <link-person-dialog ref="linkPersonDialog"
                        :showLinkPersonDialog="showLinkPersonDialog"
                        :customerId="this.form.custId"
                        @chooseLinkPerson="chooseLinkPerson"
                        @cancelLinkPersonDialog="cancelLinkPersonDialog"/>
  </basic-container>
</template>
<script>
import customerDialog from "../customer/customerDialog";
import linkPersonDialog from "./linkPersonDialog";
import {getDept} from "@/api/system/dept";


export default {
  components: {customerDialog, linkPersonDialog},
  name: "public-form-dialog",
  props: {
    showPublicFormDialog: {
      type: Boolean,
      default: false
    },
    publicFormTitle: {
      type: String,
      default: ''
    },
    publicFormData: {
      type: Object,
      default: null
    },
  },
  data() {
    return {
      customerDialogFlag: false,
      showLinkPersonDialog: false,
      showContractIcon: false,
      chooseLink: {},
      form: {
        custId: '',
        name: "",
        region: '',
        date1: '',
        date2: '',
        desc: '',
        num8: '',
        superUnitName: '',
        superUnit: '',
        concatId: '',
      },
      ipContainer: [],
      option: {
        refreshBtn: false,
        columnBtn: false,
        header: false,
        border: true,
        menu: false,
        column: [
          {
            label: '联系人',
            prop: 'contactPerson'
          },
          {
            label: '联系人类型',
            prop: 'contactType'
          },
          {
            label: '联系方式',
            prop: 'contactPhone',
          },
          {
            label: '邮箱',
            prop: 'contactEmail',
          },
          {
            label: '部门',
            prop: 'contactDept',
          },
          {
            label: '职务',
            prop: 'contactJob',
          },
          {
            label: '客户经理',
            prop: 'custManager',
          },
          {
            label: '所属机构',
            prop: 'custOrgName',
          }
        ]
      },
      data: [],
    }
  },
  created() {
    console.log("publicFormData", this.publicFormData)
  },
  methods: {
    openLinkPersonDialog() {
      this.showLinkPersonDialog = true;
    },
    chooseLinkPerson(row) {
      this.data = [];
      this.chooseLink = row;
      this.data.push(this.chooseLink);
      console.log("chooseLink", this.chooseLink)
      this.form.concatId = row.id;
    },
    cancelLinkPersonDialog() {
      this.showLinkPersonDialog = false;
    },
    acceptSuperUnit(row) {
      this.form.superUnitName = row.custName;
      this.form.custId = row.id;
      this.data = [];
      this.chooseLink = null;
      this.showContractIcon = true;
    },
    cancelCustomerDialog() {
      this.customerDialogFlag = false;
    },
    showCustomerData() {
      this.customerDialogFlag = true
    },
    removeIpInput(index) {
      this.ipContainer.splice(index, 1)
    },
    addIpInput() {
      this.ipContainer.push({});
    },
    publicFormSubmit() {

    },
    closePublicFormDialog() {
      this.form = {};
      this.showContractIcon = false;
      this.ipContainer = [];
      this.data = [];
      console.log("this.showContractIcon", this.showContractIcon)
      this.$emit("close");
    },
  }
}
</script>
<style lang="scss">

</style>

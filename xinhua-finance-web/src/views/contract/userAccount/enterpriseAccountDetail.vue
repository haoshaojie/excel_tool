<!--企业账号详情-->
<template>
  <basic-container>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="基本信息" name="basic">
        <div class="el-table-title">账号信息</div>
        <table class="el-basic-detail">
          <tr>
            <td>姓名</td>
            <td>张明明</td>
            <td>用户账号</td>
            <td>504642566@qq.com</td>
          </tr>
          <tr>
            <td>邮箱</td>
            <td>姓名</td>
            <td>手机号码</td>
            <td>姓名</td>
          </tr>
          <tr>
            <td>昵称</td>
            <td>姓名</td>
            <td>客户编码</td>
            <td>姓名</td>
          </tr>
          <tr>
            <td>客户名称</td>
            <td>姓名</td>
            <td>实名认证</td>
            <td>姓名</td>
          </tr>
          <tr>
            <td>人脸识别</td>
            <td>姓名</td>
            <td>邀请码</td>
            <td>姓名</td>
          </tr>
        </table>
        <div class="el-table-title">状态信息</div>
        <table class="el-basic-detail">
          <tr>
            <td>用户编码</td>
            <td>张明明</td>
            <td>用户状态</td>
            <td>504642566@qq.com</td>
          </tr>
          <tr>
            <td>账号状态</td>
            <td>姓名</td>
            <td>账号锁定原因</td>
            <td>姓名</td>
          </tr>
          <tr>
            <td>创建时间</td>
            <td>姓名</td>
            <td>创建人</td>
            <td>姓名</td>
          </tr>
          <tr>
            <td>最后修改时间</td>
            <td>姓名</td>
            <td>最后修改人</td>
            <td>姓名</td>
          </tr>
        </table>
      </el-tab-pane>
      <el-tab-pane label="合约信息" name="contract">
        <div class="el-table-title">合约数量：200</div>
        <avue-crud
          :option="option"
          :data="data"
        ></avue-crud>
      </el-tab-pane>
      <el-tab-pane label="产品权限" name="product">
        <div class="el-table-title">合约数量：200</div>
        <avue-crud
          :option="productOption"
          :data="productData"
        >
          <template slot-scope="{row}" slot="productName">
            <el-button type="text" size="mini" @click="openDetail">{{row.productName}}</el-button>
          </template>
        </avue-crud>
        <el-dialog title="" :visible.sync="productDialog" width="30%" append-to-body>
          <avue-crud
            :option="detailOption"
            :data="detailData"
          ></avue-crud>
        </el-dialog>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>
<script>
import {getDetail, listContract, listProduct} from "@/api/contract/userAccount/enterpriseAccount";

export default {
  name: "enterprise-account-detail",
  data() {
    return {
      activeTab: "basic",
      productDialog: false,
      option: {
        addBtn: false,
        editBtn: false,
        delBtn: false,
        refreshBtn: false,
        columnBtn: false,
        header: false,
        border: true,
        menu: false,
        column: [
          {
            label: '合约编号',
            prop: 'contractCode'
          },
          {
            label: '合约名称',
            prop: 'contractName'
          },
          {
            label: '合约金额（元）',
            prop: 'amount'
          },
          {
            label: '合约状态',
            prop: 'contractStatus'
          },
          {
            label: '合约起始日期',
            prop: 'startTime',
            type: 'date',
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd'
          },
          {
            label: '合约终止日期',
            prop: 'endTime',
            type: 'date',
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd'
          }
        ]
      },
      data: [],
      productOption: {
        addBtn: false,
        editBtn: false,
        delBtn: false,
        refreshBtn: false,
        columnBtn: false,
        header: false,
        border: true,
        menu: false,
        column: [
          {
            label: '产品编码',
            prop: 'productCode'
          },
          {
            label: '产品名称',
            prop: 'productName',
            slot: true
          },
          {
            label: '所属部门',
            prop: 'productDepart'
          },
          {
            label: '起始日期',
            prop: 'startTime',
            type: 'date',
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd'
          },
          {
            label: '终止日期',
            prop: 'endTime',
            type: 'date',
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd'
          }
        ]
      },
      productData: [
        {
          productCode: "SD987711",
          productName: "中国-东盟金融服务终端",
          productDepart: "新华丝路",
          startTime: "2020-01-15 12:00:00",
          endTime: "2021-06-30 23:59:59"
        }
      ],
      detailOption: {
        addBtn: false,
        editBtn: false,
        delBtn: false,
        refreshBtn: false,
        columnBtn: false,
        header: false,
        border: true,
        menu: false,
        column: [
          {
            label: '合约编号',
            prop: 'contractCode'
          },
          {
            label: '合约名称',
            prop: 'contractName'
          },
          {
            label: '起始日期',
            prop: 'startTime',
            type: 'date',
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd'
          },
          {
            label: '终止日期',
            prop: 'endTime',
            type: 'date',
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd'
          }
        ]
      },
      detailData: []
    }
  },
  methods: {
    openDetail() {
      this.productDialog = true;
    }
  }
}
</script>

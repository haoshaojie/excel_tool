<!--个人账号详情-->
<template>
  <basic-container>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="基本信息" name="basic">
        <div class="el-table-title">账号信息</div>
        <table class="el-basic-detail">
          <tr>
            <td>邮箱</td>
            <td colspan="3">{{personalAccountDetail.userEmail}}</td>
          </tr>
          <tr>
            <td>手机号码</td>
            <td>{{personalAccountDetail.userPhone}}</td>
            <td>姓名</td>
            <td>{{personalAccountDetail.userName}}</td>
          </tr>
          <tr>
            <td>实名认证</td>
            <td>{{personalAccountDetail.RealNameAuth == 0?"未认证":"已认证" }}</td>
            <td>人脸识别</td>
            <td>{{personalAccountDetail.faceRecognition==0?"未认证":"已认证"}}</td>
          </tr>
          <tr>
            <td>邀请码</td>
            <td colspan="3">{{personalAccountDetail.inviteCode}}</td>
          </tr>
        </table>
        <div class="el-table-title">其他信息</div>
        <table class="el-basic-detail">
          <tr>
            <td>注册时间</td>
            <td>{{personalAccountDetail.createTime}}</td>
            <td>注册渠道</td>
            <td>
              {{personalAccountDetail.regAppChannelType == 1?"电脑web":""}}
              {{personalAccountDetail.regAppChannelType == 2?"手机app":""}}
              {{personalAccountDetail.regAppChannelType == 3?"h5端":""}}
              {{personalAccountDetail.regAppChannelType == 4?"其他":""}}
            </td>
          </tr>
          <tr>
            <td>是否邀请注册</td>
            <td>{{personalAccountDetail.isInvited==0?"否":"是"}}</td>
            <td>邀请人</td>
            <td>{{personalAccountDetail.inviter}}</td>
          </tr>
          <tr>
            <td>账号状态</td>
            <td>
              {{personalAccountDetail.accStatus ==1?"正常":""}}
              {{personalAccountDetail.accStatus ==2?"锁定":""}}
              {{personalAccountDetail.accStatus ==3?"删除":""}}
            </td>
            <td>账号锁定原因</td>
            <td>{{personalAccountDetail.lockReason}}</td>
          </tr>
          <tr>
            <td>最后修改时间</td>
            <td>{{personalAccountDetail.updateTime}}</td>
            <td>最后修改人</td>
            <td>{{personalAccountDetail.updatePerson}}</td>
          </tr>
        </table>
      </el-tab-pane>
      <el-tab-pane label="订单信息" name="orderInfo">
        <avue-crud
          :table-loading="loading"
          :option="option"
          :data="data"
          :page.sync="page"
          @current-change="currentChange"
          @size-change="sizeChange"
          @on-load="orderPageQuery"
        ></avue-crud>
      </el-tab-pane>
      <el-tab-pane label="用户信息" name="userInfo">
        <table class="el-basic-detail">
          <tr>
            <td>用户编码</td>
            <td>{{personalUserDetail.userNo}}</td>
            <td>用户昵称</td>
            <td>{{personalUserDetail.userNickname}}</td>
          </tr>
          <tr>
            <td>手机号码</td>
            <td>{{personalUserDetail.userPhone}}</td>
            <td>邮箱</td>
            <td>{{personalUserDetail.userEmail}}</td>
          </tr>
          <tr>
            <td>姓名</td>
            <td>{{personalUserDetail.userName}}</td>
            <td>出生年月</td>
            <td>{{personalUserDetail.userBirthday}}</td>
          </tr>
          <tr>
            <td>注册渠道</td>
            <td>{{personalUserDetail.registChannel}}</td>
            <td>注册时间</td>
            <td>{{personalUserDetail.registerTime}}</td>
          </tr>
          <tr>
            <td>最后修改人</td>
            <td>{{personalUserDetail.updateUserName}}</td>
            <td>最后修改时间</td>
            <td>{{personalUserDetail.updateTime}}</td>
          </tr>
        </table>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>
<script>
import {getUserDetail, listOrder} from "@/api/contract/userAccount/personaluser";

export default {
  name: "personal-account-detail",
  props: {
    personalAccountDetail: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      activeTab: "basic",
      loading: true,
      productDialog: false,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 1
      },
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
            label: '订单号',
            prop: 'orderNo'
          },
          {
            label: '商品名称',
            prop: 'goodsName'
          },
          {
            label: '订购时间',
            prop: 'expiredDate',
            type: 'date',
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd'
          },
          {
            label: '到期时间',
            prop: 'orderTime',
            type: 'date',
            format: 'yyyy-MM-dd',
            valueFormat: 'yyyy-MM-dd'
          }
        ]
      },
      data: [
        {
          orderCode: "2020091817200001",
          productName: "一年会员",
          orderTime: "2020-09-18 17:20",
          endTime: "2021-09-18 17:20"
        }
      ],
      personalUserDetail: {},
    }
  },
  watch: {
    personalAccountDetail: {
      handler: function (newVal, oldVal) {
        this.page = {
          pageSize: 10,
          currentPage: 1,
          total: 0
        };
        this.orderPageQuery(this.page);
        this.userDetailQuery();
      },
      immediate: true
    }
  },
  methods: {
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
    },
    userDetailQuery() {
      getUserDetail(this.personalAccountDetail.accId).then(res => {
        this.personalUserDetail = res.data.data;
      }, error => {
        window.console.log(error);
      });
    },
    orderPageQuery(page) {
      this.loading = true;
      listOrder(page.currentPage, page.pageSize, this.personalAccountDetail.userPhone, this.personalAccountDetail.userEmail).then(res => {
        const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
        this.loading = false;
      });
    }
  }
}
</script>

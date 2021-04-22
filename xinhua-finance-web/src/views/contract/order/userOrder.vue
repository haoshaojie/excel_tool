<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="primary" name="excelBtn"
                   size="small"
                   icon="el-icon-download"
                   plain
                   v-if="permission.userorder_excel"
                   @click="exportExcel">导出
        </el-button>
        <!--        <template slot-scope="scope" slot="isDeletedForm">-->
        <!--          <el-tag>12121231</el-tag>-->
        <!--        </template>-->
        <template slot-scope="{disabled}" slot="isDeletedLabel">
          <span>姓名&nbsp;&nbsp;</span>
          <el-input :disabled="disabled" v-model="form.isDeleted"></el-input>
        </template>
      </template>
      <!--      <template slot-scope="scope" slot="menu">-->
      <!--        <el-button type="text" size="small" icon="el-icon-view"-->
      <!--                   v-if="permission.userorder_view"-->
      <!--                   @click.native="detailView(scope.row)">查看2-->
      <!--        </el-button>-->
      <!--      </template>-->
      <template slot="infoForm" slot-scope="scope">
        <!--        <div style="border-bottom: 1px solid #eee;height: 40px;line-height:40px;">商品信息</div>-->
        <avue-crud :option="infoOption" :data="infoData">
          <template slot="header">
            <div
              style="font-size: 16px;border-bottom: 1px solid #eee;height: 40px;line-height:40px;margin-bottom: 10px;">
              商品信息
            </div>
          </template>
        </avue-crud>
      </template>
    </avue-crud>
    <user-order-view ref="refViewBox" :viewBox="viewBox" :id="id" @close="closeDialog"></user-order-view>
  </basic-container>
</template>

<script>
  import {exportData, getDetail, getList} from "@/api/contract/order/userOrder";
  import {mapGetters} from "vuex";
  import userOrderView from "./userOrderView";
  import {getDictByCode} from "@/api/system/dict";

  export default {
    components: {userOrderView},
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        viewBox: false,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        appCodeDic: null,
        group: [],
        option: {
          height: 'auto',
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,
          addBtn: false,
          editBtn: false,
          delBtn: false,
          excelBtn: false,
          labelWidth: "30%",
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          label: '基础信息',
          // icon: 'el-icon-edit-outline',
          column: [
            // {
            //   label: "基础信息",
            //   prop: "isDeleted",
            //   span: 24,
            //   hide: true,
            //   formslot: true,
            //   labelslot: true,
            //   order: 100,
            //   labelWidth: "100px"
            // },
            {
              label: "手机号码",
              prop: "phoneNumber",
              search: true,
              display: true
            },
            {
              label: "邮箱",
              prop: "mailbox",
              search: true,
              display: true
            },
            {
              label: "订单编码",
              prop: "orderNo",
              order: 9,
              searchOrder: 9,
              search: true,
              display: true
            },
            {
              label: "产品名称",
              prop: "goodsName",
              search: true,
              searchOrder: 8,
              hide: true,
              display: false,
            },
            {
              label: "产品编码",
              prop: "goodsNo",
              search: true,
              searchOrder: 7,
              hide: true,
              display: false,
            },
            {
              label: "订单金额（元）",
              prop: "orderAmount",
              order: 8,
              display: true
            },
            {
              label: "订单来源",
              prop: "appCode",
              order: 7,
              search: true,
              display: true,
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.APP_CODE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              rules: [{
                required: true,
                message: "请选择表现方式",
                trigger: "blur"
              }]
            },

            {
              label: "订购时间",
              prop: "orderTime",
              order: 6,
              display: true
            },
            {
              label: "同步时间",
              prop: "createTime",
              search: true,
              searchOrder: 10,
              span: 12,
              format: 'yyyy-MM-dd HH:mm:ss',                  // 这是组件展示的日期格式
              valueFormat: 'yyyy-MM-dd HH:mm:ss',        // 这是组件value值的格式
              type: "datetime",
              searchRange: true,
              addDisplay: false, //新增时是否显示
              editDisplay: false, //编辑时是否显示
              viewDisplay: true, //详情时是否显示
            }, {
              labelWidth: 0,
              label: '',
              prop: 'info',
              span: 24,
              hide: true,
              formslot: true,
            }
          ],
        },
        infoOption: {
          menu: false,
          addBtn: false,
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          searchIcon: false,
          // label: '客户经理信息',
          // icon: 'el-icon-edit-outline',
          column: [
            {
              label: '商品编码',
              prop: 'goodsNo',
            },
            {
              label: '商品名称',
              prop: 'goodsName',
            },
            {
              label: '商品金额',
              prop: 'goodsPrice',
            },
            {
              label: '到期时间',
              prop: 'expiredDate',
            }
          ]
        },
        data: []
      };
    },
    computed: {
      ...
        mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.userorder_add, false),
          viewBtn: this.vaildData(this.permission.userorder_view, false),
          editBtn: this.vaildData(this.permission.userorder_excel, false)
        };
      },
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      },
      infoData() {
        return this.form.info || []
      }
    },
    methods: {
      closeDialog(val) {
        this.viewBox = val;
        this.$emit("close", false);
      },
      beforeOpen(done, type) {
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
            this.form.appCode = Number.parseInt(this.form.appCode);
            this.form.info = res.data.data.userOrderGoodsList;
          });
        }
        done();
      }
      ,
      searchReset() {
        this.query = {};
        this.onLoad(this.page);
      }
      ,
      searchChange(params, done) {
        this.query = params;
        if (params.createTime) {
          params.createTimeBegin = params.createTime[0];// + " 00:00:00";
          params.createTimeEnd = params.createTime[1];// + " 23:59:59";
          params.createTime = null;
        }
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done();
      }
      ,
      selectionChange(list) {
        this.selectionList = list;
      }
      ,
      selectionClear() {
        this.selectionList = [];
        this.$refs.crud.toggleSelection();
      }
      ,
      currentChange(currentPage) {
        this.page.currentPage = currentPage;
      }
      ,
      sizeChange(pageSize) {
        this.page.pageSize = pageSize;
      }
      ,
      onLoad(page, params = {}) {
        this.loading = true;
        this.appCodeDic = new Map();
        getDictByCode(this.CONSTANT.APP_CODE).then(res => {
          console.log("初始化字典", res.data.data);
          //组装字典
          for (let i = 0; i < res.data.data.length; i++) {
            this.appCodeDic.set(res.data.data[i].dictKey + "", res.data.data[i].dictValue);
          }
        });
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          for (let i = 0; i < this.data.length; i++) {
            if (this.data[i].appCode && this.data[i].appCode != "") {
              this.data[i].appCode = Number.parseInt(this.data[i].appCode);
            }
            // this.data[i].phoneNumber = this.data[i].phoneNumber.replace(/^(\d{3})\d{4}(\d{4})$/, '$1****$2');
          }
          this.loading = false;
          this.selectionClear();
        });
      },
      detailView(row) {
        this.viewBox = true;
      },
      exportExcel() {
        console.log("导出");

        exportData(this.query).then(res => {
          let downData = res.data.data;
          for (let i = 0; i < downData.length; i++) {
            console.log("字典map", this.appCodeDic);
            downData[i].appCodeName = this.appCodeDic.get(downData[i].appCode + "");
            // downData[i].phoneNumber = downData[i].phoneNumber.replace(/^(\d{3})\d{4}(\d{4})$/, '$1****$2');
          }
          this.$export.excel({
            title: "用户订单信息",
            columns: [
              {
                label: "手机号码",
                prop: "phoneNumber",
              },
              {
                label: "邮箱",
                prop: "mailbox",
              },
              {
                label: "订单编码",
                prop: "orderNo",
                order: 9,
              },
              {
                label: "订单金额（元）",
                prop: "orderAmount",
                order: 8,
                display: true
              },
              {
                label: "订单来源",
                prop: "appCodeName",
                order: 7,
              },
              {
                label: "订购时间",
                prop: "orderTime",
                order: 6,
              },
              {
                label: "同步时间",
                prop: "createTime",
                searchOrder: 10,
              },
              {
                label: '商品编码',
                prop: 'goodsNo',
              },
              {
                label: '商品名称',
                prop: 'goodsName',
              },
              {
                label: '商品金额',
                prop: 'goodsPrice',
              },
              {
                label: '到期时间',
                prop: 'expiredDate',
              }],
            data: downData,
          });
        })
      },
    }
  };
</script>

<style lang="scss" scoped>
  .el-icon-arrow-right:before {
    content: "\e6e0";
    display: none;
  }

  .el-collapse-item__header {
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    align-items: center;
    height: 48px;
    line-height: 48px;
    background-color: #FFF;
    color: #303133;
    cursor: none;
    border-bottom: 1px solid #EBEEF5;
    font-size: 13px;
    font-weight: 500;
    transition: border-bottom-color .3s;
    outline: 0;
    pointer-events: none;
  }

  .el-collapse-item__wrap {
    will-change: height;
    background-color: #FFF;
    overflow: hidden;
    /* -webkit-box-sizing: border-box; */
    box-sizing: border-box;
    border-bottom: 1px solid #EBEEF5;
    display: block;
    /*pointer-events: none;*/
  }

  /* [style] {
    display: block;
  } */
</style>

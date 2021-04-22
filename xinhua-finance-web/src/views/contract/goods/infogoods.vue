<template>
  <basic-container>

    <avue-tabs :option="tabsOption" @change="handleChange"></avue-tabs>
    <span v-if="stateTabs.prop==='1'">
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               :before-close="beforeClose"
               v-model="form"
               ref="crud"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.infogoods_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button type="primary" name="excelBtn"
                   size="small"
                   icon="el-icon-download"
                   plain
                   v-if="permission.infogoods_excel"
                   @click="exportExcel">导出
        </el-button>
      </template>
    </avue-crud>
  </span>
    <span v-else-if="stateTabs.prop==='2'">
      <avue-crud :option="option"
                 :table-loading="loading"
                 :data="data"
                 :page="page"
                 :permission="permissionList"
                 :before-open="beforeOpen"
                 v-model="form"
                 ref="crud"
                 @row-update="rowUpdate"
                 @row-save="rowSave"
                 @row-del="rowDel"
                 @search-change="searchChange"
                 @search-reset="searchReset"
                 @selection-change="selectionChange"
                 @current-change="currentChange"
                 @size-change="sizeChange"
                 @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.infogoods_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button type="primary" name="ExcelBtn"
                   size="small"
                   icon="el-icon-download"
                   plain
                   v-if="permission.infogoods_excel"
                   @click="exportExcel">导出
        </el-button>
          <el-button type="primary" name="dealBtn"
                     size="small"
                     icon="el-icon-check"
                     plain
                     v-if="permission.infogoods_deal"
                     @click="dealGoods">处理
        </el-button>
      </template>
        <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small" icon="el-icon-check"
                   v-if="permission.infogoods_deal"
                   @click.native="dealGoodsOne(scope.row)">处理
        </el-button>
      </template>
    </avue-crud>
    </span>
    <infoGoods-dialog :dealBox="dealBox" :infoIds="infoIds" :reportIds="reportIds" :id="id" :type="type"
                      @close="closeDialog"></infoGoods-dialog>
  </basic-container>
</template>

<script>
  import {exportData, getDetail, getList, remove, update} from "@/api/contract/goods/infogoods";
  import {mapGetters} from "vuex";
  import infoGoodsDialog from "./infogoods-dialog";

  export default {
    components: {infoGoodsDialog},
    data() {
      return {
        form: {},
        query: {},
        dealBox: false,
        id: {},
        type: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        stateTabs: {},
        tabsOption: {
          column: [{
            icon: 'el-icon-info',
            label: '正常',
            prop: '1',
          }, {
            icon: 'el-icon-warning',
            label: '异常',
            prop: '2',
          }]
        },
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
          excelBtn: false,
          // dateBtn:true,
          labelWidth: 110,
          dialogWidth: "40%",
          column: [
            {
              label: "标题",
              prop: "goodsTitle",
              search: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入标题",
                trigger: "blur"
              }]
            },
            {
              label: "价格（元）",
              prop: "price",
              span: 24,
              formatter: (row, value) => {
                if (row.state === this.CONSTANT.GOODS_STATE_VALUE.STATE_ABNORMAL) {
                  return "<div style='color:red'>" + value + "</div>";
                } else {
                  return value;
                }

              },
              rules: [{
                required: true,
                message: "请输入价格",
                trigger: "blur"
              }]
            },

            {
              label: "上线时间",
              prop: "onlineTime",
              search: true,
              span: 24,
              format: 'yyyy-MM-dd HH:mm:ss',                  // 这是组件展示的日期格式
              valueFormat: 'yyyy-MM-dd HH:mm:ss',        // 这是组件value值的格式
              type: "datetime",
              searchRange: true,
              startPlaceholder: '开始时间',
              endPlaceholder: '结束时间',
            },
            {
              label: "渠道",
              prop: "source",
              search: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入渠道",
                trigger: "blur"
              }]
            },
            {
              label: "作者",
              prop: "author",
              span: 24,
              rules: [{
                required: true,
                message: "请输入作者",
                trigger: "blur"
              }]
            },
            {
              label: "类型",
              prop: "type",
              search: true,
              span: 24,
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.GOODS_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              rules: [{
                required: true,
                message: "请输入编辑者",
                trigger: "blur"
              }]
            },
            {
              label: "副标题",
              prop: "subTitle",
              hide: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入副标题",
                trigger: "blur"
              }]
            },
            {
              label: "摘要",
              prop: "abstractContent",
              type: "textarea",
              hide: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入摘要",
                trigger: "blur"
              }]
            },
            /*    {
                  label: "编辑者",
                  prop: "editor",
                  hide: true,
                  display: false,
                  rules: [{
                    required: true,
                    message: "请输入编辑者",
                    trigger: "blur"
                  }]
                },*/

            /*   {
                 label: "是否原创0否1是",
                 prop: "isOriginal",
                 hide: true,
                 display: false,
                 rules: [{
                   required: false,
                   message: "请输入是否原创0否1是",
                   trigger: "blur"
                 }]
               },*/


            {
              label: "同步时间",
              prop: "synTime",
              hide: true,
              display: false,
              rules: [{
                required: true,
                message: "请输入同步时间",
                trigger: "blur"
              }]
            },
            // {
            //   label: "商品状态",
            //   prop: "state",
            //   hide: true,
            //   display: false,
            //   rules: [{
            //     required: true,
            //     message: "请输入商品状态",
            //     trigger: "blur"
            //   }]
            // },
            {
              label: "处理人",
              prop: "disposeUserName",
              hide: true,
              display: true,
              span: 24
            },
            {
              label: "处理时间",
              prop: "disposeTime",
              hide: true,
              display: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入处理时间",
                trigger: "blur"
              }]
            },
            {
              label: "处理意见",
              prop: "disposeSuggest",
              hide: true,
              display: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入处理意见",
                trigger: "blur"
              }]
            },
            /*{
              label: "失效时间",
              prop: "invalidTime",
              hide: true,
              display: false,
              rules: [{
                required: true,
                message: "请输入失效时间",
                trigger: "blur"
              }]
            },*/
          ]
        },
        data: []
      };
    },
    watch: {},
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.infogoods_add, false),
          viewBtn: this.vaildData(this.permission.infogoods_view, false),
          delBtn: this.vaildData(this.permission.infogoods_delete, false),
          editBtn: this.vaildData(this.permission.infogoods_edit, false),
          excelBtn: this.vaildData(this.permission.infogoods_excel, false),
          dealBtn: this.vaildData(this.permission.infogoods_deal, false),
        };
      },
      infoIds() {
        let infoIds = [];
        this.selectionList.forEach(ele => {
          if (ele.type === this.CONSTANT.GOODS_TYPE_VALUE.TYPE_INFO) {
            infoIds.push(ele.id);
          }
        });
        return infoIds.join(",");
      },
      reportIds() {
        let reportIds = [];
        this.selectionList.forEach(ele => {
          if (ele.type === this.CONSTANT.GOODS_TYPE_VALUE.TYPE_REPORT) {
            reportIds.push(ele.id);
          }
        });
        return reportIds.join(",");
      },
    },
    created() {
      this.stateTabs = this.tabsOption.column[0];

    },
    methods: {
      closeDialog(val) {
        this.onLoad(this.page);
        this.dealBox = val;
      },
      handleChange(column) {
        this.stateTabs = column;
        this.onLoad(this.page);
      },
      rowSave(row, done, loading) {
        add(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowUpdate(row, index, done, loading) {
        update(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowDel(row) {
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            if (row.type === this.CONSTANT.GOODS_TYPE_VALUE.TYPE_INFO) {
              return remove(row.id, "");
            } else if (row.type === this.CONSTANT.GOODS_TYPE_VALUE.TYPE_REPORT) {
              return remove("", row.id);
            }

          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
          });
      },
      handleDelete() {
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return remove(this.infoIds, this.reportIds);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
            this.$refs.crud.toggleSelection();
          });
      },
      disposeInfoDisplay() {
        if (this.form.disposeTime == null || this.form.disposeTime == "") {
          console.log(this.form.disposeTime, "隐藏");
          this.findObject(this.option.column, 'disposeTime').display = false;
          this.findObject(this.option.column, 'disposeUserName').display = false;
          this.findObject(this.option.column, 'disposeSuggest').display = false;
        } else {
          console.log(this.form.disposeTime, "显示");
          this.findObject(this.option.column, 'disposeTime').display = true;
          this.findObject(this.option.column, 'disposeUserName').display = true;
          this.findObject(this.option.column, 'disposeSuggest').display = true;
        }
      },
      beforeOpen(done, type) {
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id, this.form.type).then(res => {
            this.form = res.data.data;
            this.disposeInfoDisplay();
          });
        }
        this.disposeInfoDisplay();
        console.log(this.findObject(this.option.column, 'disposeTime').display, "是否显示");
        done();
      },
      searchReset() {
        this.query = {};
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        if (params.onlineTime) {
          params.onlineTimeBegin = params.onlineTime[0];// + " 00:00:00";
          params.onlineTimeEnd = params.onlineTime[1];// + " 23:59:59";
          params.onlineTime = null;
        }
        this.query = params;
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done();
      },
      selectionChange(list) {
        this.selectionList = list;
      },
      selectionClear() {
        this.selectionList = [];
        this.$refs.crud.toggleSelection();
      },
      currentChange(currentPage) {
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize) {
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        this.query.state = this.stateTabs.prop;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
      exportExcel() {
        exportData(this.query).then(res => {
          let downData = res.data.data;
          for (let i = 0; i < downData.length; i++) {
            if (downData[i].type == this.CONSTANT.GOODS_TYPE_VALUE.TYPE_INFO) {
              downData[i].type = "资讯";
            } else {
              downData[i].type = "研报";
            }
          }
          this.$export.excel({
            title: "单篇商品",
            columns: this.option.column,
            data: downData,
          });
        })
      },
      dealGoods() {
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        this.$confirm("确定将选择数据做相同处理?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.dealBox = true;
          })
          .catch((error) => {
            console.log(error);
          })
      },
      dealGoodsOne(row) {
        this.id = row.id;
        this.type = row.type;
        this.dealBox = true;
      },
    }
  };
</script>

<style>
</style>

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
        <el-button type="warning"
                   size="small"
                   plain
                   icon="el-icon-download"
                   @click="handleExport">导出
        </el-button>
      </template>
      <template slot-scope="" slot="createTimeSearch">
        <el-date-picker
          v-model="createTime"
          type="daterange"
          value-format="yyyy-MM-dd"
          format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/contract/user/personaluser";
  import {validateNumber,validatePhone,validateEmail,validateTelephone} from "@/api/tool/validate";
  import website from "@/config/website";
  import {getToken} from '@/util/auth';
  import {mapGetters} from "vuex";
  import qs from 'qs'
  export default {
    name:"personal-user",
    data() {
      return {
        form: {},
        query: {},
        createTime: [],
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          height: 'auto',
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: true,
          viewBtn: true,
          addBtn: false,
          delBtn: false,
          selection: true,
          column: [
            {
              label: "主键id 雪花算法产生",
              prop: "id",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            },
            {
              label: "用户编号",
              prop: "userNo",
              editDisabled:true,
            },
            {
              label: "用户昵称",
              prop: "userNickname",
              search:true,
              searchOrder:6,
              editDisabled:true,
            },
            {
              label: "手机号码",
              prop: "userPhone",
              search:true,
              searchOrder:5,
              rules: [{
                required: true,
                message: "请输入邮箱",
                trigger: "blur"
              },{ validator: validatePhone, trigger: 'blur' }]
            },
            {
              label: "邮箱",
              prop: "userEmail",
              search:true,
              searchOrder:4,
              rules: [{
                required: true,
                message: "请输入邮箱",
                trigger: "blur"
              } ,{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
                { min: 0, max: 50, message: '最长限50个字', trigger: 'blur' }]
            },
            {
              label: "姓名",
              prop: "userName",
              search:true,
              searchOrder:3,
              editDisabled:true,
            },

            {
              label: "出生年月",
              prop: "userBirthday",
              editDisabled:true,
            },{
              label: "注册渠道",
              prop: "registChannel",
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.REGIST_CHANNEL),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              search:true,
              editDisabled:true,
            },
            {
              label: "注册时间",
              prop: "createTime",
              editDisabled:true,
              searchslot: true,
              type: 'date',
              format: 'yyyy-MM-dd',
              valueFormat: 'yyyy-MM-dd',
              search: true,
              searchOrder:1,
            },

          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.personaluser_add, false),
          viewBtn: this.vaildData(this.permission.personaluser_view, false),
          delBtn: this.vaildData(this.permission.personaluser_delete, false),
          editBtn: this.vaildData(this.permission.personaluser_edit, false)
        };
      },
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    methods: {
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
            return remove(row.id);
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
            return remove(this.ids);
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
      beforeOpen(done, type) {
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
          });
        }
        done();
      },
      searchReset() {
        this.createTime=[];
        this.query = {};
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        this.query = params;
        this.page.currentPage = 1;
        if (this.createTime) {
          this.query.createStartTime = this.createTime[0];
          this.query.createEndTime = this.createTime[1];
        }
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
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.data.forEach(item=>{
            if (item.registChannel) {
              item.registChannel=parseInt(item.registChannel);
            }
          })
          this.loading = false;
          this.selectionClear();
        });
      },
      handleExport(){
        Object.assign(this.form,this.query);
        window.open(`/api/cnfic-contract-manage/personaluser/export?${qs.stringify(this.form)}&${website.tokenHeader}=${getToken()}`);
      }
    }
  };
</script>

<style>
</style>

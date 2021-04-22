<template>
  <basic-container>
    <avue-crud :before-open="beforeOpen"
               :data="data"
               :option="option"
               :page="page"
               :permission="permissionList"
               :table-loading="loading"
               @current-change="currentChange"
               @on-load="onLoad"
               @row-del="rowDel"
               @row-save="rowSave"
               @row-update="rowUpdate"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @size-change="sizeChange"
               ref="crud"
               v-model="form">
      <template slot="menuLeft">
        <el-button @click="handleDelete"
                   icon="el-icon-delete"
                   plain
                   size="small"
                   type="danger"
                   v-if="permission.partner_delete">删 除
        </el-button>
      </template>
      <!--      <template slot-scope="{row}"
                      slot="parType">
              <el-tag>{{ row.parTypeName }}</el-tag>
            </template> -->
      <template slot="menu" slot-scope="scope">
        <el-button @click="viewPartnerProduct(scope.row)"
                   icon="el-icon-view"
                   size="small" type="text">查看产品
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getDeptTree} from "@/api/system/dept";
  import {regionTree} from "@/api/base/region";
  import {add, getDetail, getList, remove, update} from "@/api/partner/partner";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        parTypeDict: [],  // 合作商类型数据字典
        option: {
          height: 'auto',
          calcHeight: 210,
          labelWidth: 130,
          searchLabelWidth: 100,
          menuWidth: 300,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
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
              label: "合作商编码",
              prop: "parCode",
              search: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              showWordLimit: true,
              maxlength: 30
            },
            {
              label: "合作商名称",
              prop: "parName",
              search: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              showWordLimit: true,
              maxlength: 30
            },
            {
              label: "合作商类型",
              prop: "parType",
              search: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              type: 'select',
              props: {
                label: 'dictValue',
                value: 'dictKey'
              },
              dicUrl: this.getDicValue("partner_type")
            },
            {
              label: "所属地区",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              prop: "areaName"
            },
            {
              label: "合作商联系人",
              prop: "leaderPerson",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              showWordLimit: true,
              maxlength: 10,
              rules: [{
                required: true,
                message: "请输入合作商联系人",
                trigger: "blur"
              }]
            },
            {
              label: "创建人",
              prop: "createUserName",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            },
            {
              label: "创建时间",
              prop: "createTime",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            },
            {
              label: "最后更改人",
              prop: "updateUserName",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            },
            {
              label: "最后更改时间",
              prop: "updateTime",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            }
          ],
          group: [
            {
              label: '客户经理信息',
              prop: 'jbxx',
              // icon: 'el-icon-edit-outline',
              column: [
                {
                  label: '客户经理',
                  prop: 'managerName',
                  showWordLimit: true,
                  maxlength: 30
                },
                {
                  label: "所属机构",
                  prop: "managerOrg",
                  type: "tree",
                  multiple: false,
                  dicData: [],
                  props: {
                    label: "title"
                  },
                  slot: true,
                  checkStrictly: true
                }
              ]
            },
            {
              label: '合作商信息',
              prop: 'hzsxx',
              // icon: 'el-icon-edit-outline',
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
                  label: "合作商编码",
                  prop: "parCode",
                  showWordLimit: true,
                  maxlength: 30,
                  rules: [{
                    required: true,
                    message: "请输入合作商编码",
                    trigger: "blur"
                  }]
                },
                {
                  label: "合作商名称",
                  prop: "parName",
                  showWordLimit: true,
                  maxlength: 30,
                  rules: [{
                    required: true,
                    message: "请输入合作商名称",
                    trigger: "blur"
                  }]
                },
                {
                  label: "合作商类型",
                  prop: "parType",
                  type: 'select',
                  dicUrl: this.getDicValue("partner_type"),
                  props: {
                    label: 'dictValue',
                    value: 'dictKey'
                  },
                  rules: [{
                    required: true,
                    message: "请输入合作商类型",
                    trigger: "blur"
                  }]
                },
                {
                  label: "所属地区",
                  prop: "area",
                  type: 'tree',
                  dicData: [],
                  leafOnly: true,
                  viewDisplay: false,
                  props: {
                    label: 'title',
                    value: 'value'
                  },
                  multiple: false,
                  slot: true,
                  checkStrictly: true,
                  // lazy: true,
                  // treeLoad: function (node, resolve) {
                  //   console.log("加载数", node.data.data.id);
                  //   const parentCode = (node.level === 0) ? "00" : node.data.id;
                  //   getLazyTree(parentCode).then(res => {
                  //     resolve(res.data.data.map(item => {
                  //       return {
                  //         ...item,
                  //         leaf: !item.hasChildren
                  //       }
                  //     }))
                  //   });
                  // },
                  rules: []
                },
                {
                  label: "所属地区",
                  prop: "areaName",
                  editDisplay: false,
                  addDisplay: false
                },
                {
                  label: "合作商联系人",
                  prop: "leaderPerson",
                  showWordLimit: true,
                  maxlength: 10,
                  rules: [{
                    required: true,
                    message: "请输入合作商联系人",
                    trigger: "blur"
                  }]
                },
                {
                  label: "合作商联系电话",
                  prop: "userTelphone",
                  hide: true,
                  rules: [{
                    required: true,
                    message: "请输入合作商联系电话",
                    trigger: "blur"
                  }, {pattern: /(^([0-9]{3,4}-)?[0-9]{7,8}$)|(^1[0-9]{10}$)/, message: '请输入正确的联系电话'}]
                },
                {
                  label: "邮箱",
                  prop: "userEmail",
                  hide: true,
                  rules: [{
                    pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/,
                    message: '请输入正确的邮箱'
                  }]
                },
                {
                  label: "固话",
                  prop: "userPhone",
                  hide: true,
                  rules: [{pattern: /^([0-9]{3,4}-)?[0-9]{7,8}$/, message: '请输入正确的固话'}]
                },
                {
                  label: "地址",
                  prop: "address",
                  span: 24,
                  hide: true,
                  showWordLimit: true,
                  maxlength: 50,
                  rules: []
                },
                {
                  label: "备注信息",
                  prop: "remark",
                  type: 'textarea',
                  span: 24,
                  hide: true,
                  showWordLimit: true,
                  maxlength: 100,
                  rules: []
                },
                {
                  label: "创建人",
                  prop: "createUser",
                  addDisplay: false,
                  editDisplay: false,
                  viewDisplay: false
                },
                {
                  label: "创建时间",
                  prop: "createTime",
                  addDisplay: false,
                  editDisplay: false,
                  viewDisplay: false
                },
                {
                  label: "最后更改人",
                  prop: "updateUser",
                  addDisplay: false,
                  editDisplay: false,
                  viewDisplay: false
                },
                {
                  label: "最后更改时间",
                  prop: "updateTime",
                  addDisplay: false,
                  editDisplay: false,
                  viewDisplay: false
                }
              ]
            }
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.partner_add, false),
          viewBtn: this.vaildData(this.permission.partner_view, false),
          delBtn: this.vaildData(this.permission.partner_delete, false),
          editBtn: this.vaildData(this.permission.partner_edit, false)
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
        console.log(row);
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
        console.info(row);
        row.areaName = encodeURI(row.areaName);
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
        this.$confirm("是否确定删除（若合作商有关联产品则无法删除）?", {
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
            // this.form = res.data.data;
          });
        }
        done();
      },
      searchReset() {
        this.query = {};
        this.onLoad(this.page);
      },
      searchChange(params, done) {
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

        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          decodeURI(data);
          this.page.total = data.total;
          console.info(data.records);
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });

        // 初始化部门数据
        getDeptTree('000000').then(res => {
          const column = this.findObject(this.option.group[0].column, "managerOrg");
          column.dicData = res.data.data;
        });
        //初始化
        regionTree(3).then(res => {
          console.log("加载初始区域")
          const column = this.findObject(this.option.group[1].column, "area");
          column.dicData = res.data.data;
        });
      },
      // 查看合作商产品
      viewPartnerProduct(row) {
        this.$router.push({path: '/partner/partnerProduct', query: {id: row.id}})
      },
    }
  };
</script>

<style>
</style>

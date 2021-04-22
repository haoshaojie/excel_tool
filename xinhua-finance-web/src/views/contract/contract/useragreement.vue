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
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @refresh-change="refreshChange"
               :upload-before="uploadBefore"
               :upload-delete="uploadDelete"
               @on-load="onLoad">
      <template slot-scope="{disabled,size}" slot="createTimeSearch">
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
      <template slot-scope="{disabled,size}" slot="agreementSignTimeSearch">
        <el-date-picker
          v-model="agreementSignTime"
          type="daterange"
          value-format="yyyy-MM-dd"
          format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </template>
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.useragreement_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot="userProtocolFile" slot-scope="scope">
        <span v-if="scope.row.protocolFile !== undefined && scope.row.protocolFile.length >0 ">
        <el-button type="text"
                   size="small"
                   icon="el-icon-document-copy"
                   v-if="permission.useragreement_view"
                   @click.stop="viewAgreementDocument(scope.row)">
          查看协议文件
        </el-button>
        </span>
      </template>
      <template slot="agreementNo" slot-scope="scope">
        <el-button type="text"
                   size="small"
                   v-if="permission.useragreement_view"
                   class="agreementNoClass"
                   @click.stop="handleView(scope.row, index)">{{scope.row.agreementNo}}
        </el-button>
      </template>
    </avue-crud>
    <userProtocolFilePerview :openDialog="openDialog" :protocolFileList="protocolFileList"
                             @close="closeDialog"></userProtocolFilePerview>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/contract/contract/useragreement";
  import {mapGetters} from "vuex";
  import {fileType} from "@/util/util";
  import userProtocolFilePerview from "./userProtocolFilePerview";

  export default {
    components: {userProtocolFilePerview},
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        openDialog: false,
        //协议文件列表
        protocolFileList: [],
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        agreementSignTime: [],
        agreementTimeArrInit: [],
        createTime: [],
        selectionList: [],
        option: {
          height: 'auto',
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          editBtn: true,
          border: true,
          index: false,
          viewBtn: true,
          viewBtnText: '查看合约',
          delBtn: false,
          selection: true,
          labelWidth: 130,
          menu: true,//是否显示操作菜单栏
          menuWidth: 180,//操作菜单栏的宽度
          column: [
            {
              label: "主键id 雪花算法产生",
              prop: "id",
              hide: true,
              display: false, //在查看，新增，编辑页面是否显示
              rules: [{
                required: true,
                message: "请输入主键id 雪花算法产生",
                trigger: "blur"
              }]
            },
            {
              label: "协议编码",
              prop: "agreementNo",
              display: false, //在查看，新增，编辑页面是否显示
              rules: [{
                required: true,
                message: "请输入协议编码",
                trigger: "blur"
              }],
              slot: true
            },
            {
              label: "协议名称",
              span: 12,
              prop: "agreementName",
              search: true,
              display: true,
              showWordLimit: true,
              maxlength: "30",
              rules: [
                {required: true, message: "请输入协议名称", trigger: "blur"},
                {min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur'}
              ],
              order: 7
            },
            {
              label: "协议类型",
              span: 12,
              prop: "agreementType",
              search: true,
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.AGREEMENT_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              order: 6
            },
            {
              label: "用户协议文件",
              hide: true,
              display: true, //在查看，新增，编辑页面是否显示
              prop: "protocolFile",
              type: "upload",
              showFileList: true,
              multiple: true,
              span: 24,
              limit: 10,
              order: 2,
              tip: ' 仅支持上传doc/docx/pdf/zip/rar/png/jpg文件,文件个数不超过10个,单文件大小不超过10M',
              accept: '.doc,.docx,.pdf,.zip,.rar,.jpg,.png',
              action: '/api/cnfic-resource/oss/file/putFile',
              propsHttp: {
                res: 'data',
                name: "originalName",
                url: "link"
              }
            },
            {
              label: "协议内容",
              span: 24,
              prop: "agreementContent",
              type: "textarea",
              showWordLimit: true,
              hide: true,
              minRows: 3,
              maxRows: 5,
              maxlength: 5000,
              order: 1
            },
            {
              label: "协议状态",
              search: true,
              display: false, //在查看，新增，编辑页面是否显示
              prop: "agreementState",
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.AGREEMENT_STATE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              width: 80,
            },
            {
              label: "金额(元)",
              prop: "amount",
              order: 5,
              span: 12,
              rules: [
                {
                  pattern: /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
                  message: '请输入正确格式,可保留两位小数'
                }
              ],
            },
            {
              label: "期限(月)",
              prop: "termOfMonth",
              display: false,
              width: 80,
            },
            {
              label: "有效期",
              prop: 'agreementTimeArr',
              type: 'daterange',
              format: 'yyyy-MM-dd',
              valueFormat: 'yyyy-MM-dd',
              search: false,
              hide: true,
              slot: true,
              span: 12,
              order: 3
            },
            {
              label: "签订时间",
              prop: "agreementSignTime",
              searchslot: true,
              type: 'date',
              format: 'yyyy-MM-dd',
              valueFormat: 'yyyy-MM-dd',
              search: true,
              span: 12,
              order: 4
            },
            {
              label: "创建时间",
              prop: "createTime",
              searchslot: true,
              search: true,
              display: false
            },
            {
              label: '用户协议文件',
              prop: 'userProtocolFile',
              display: false,
              slot: true
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
          addBtn: this.vaildData(this.permission.useragreement_add, false),
          viewBtn: this.vaildData(this.permission.useragreement_view, false),
          delBtn: this.vaildData(this.permission.useragreement_delete, false),
          editBtn: this.vaildData(this.permission.useragreement_edit, false)
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
      uploadBefore(file, done, loading, column) {
        //如果你想修改file文件,由于上传的file是只读文件，必须复制新的file才可以修改名字，完后赋值到done函数里,如果不修改的话直接写done()即可
        if ((file.size / 1024) < (1024 * 10)) {
          if (fileType(file.name, this)) {
            done();
          } else {
            loading();
          }
        } else {
          loading();
          this.$message.warning("附件大小限10M以内");
        }
      },
      handleView: function (row, index) {
        this.$refs.crud.rowView(row, index);
      },
      rowSave(row, done, loading) {
        if (row.agreementTimeArr.length == 2) {
          row.agreementStartTime = row.agreementTimeArr[0];
          row.agreementEndTime = row.agreementTimeArr[1];
        }
        row.protocolFile.forEach(item => {
          item.fileName = item.label;
          item.fileUrl = item.value;
        });
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
        if (row.agreementTimeArr.length == 2) {
          row.agreementStartTime = row.agreementTimeArr[0];
          row.agreementEndTime = row.agreementTimeArr[1];
        }
        row.protocolFile.forEach(item => {
          item.fileName = item.label;
          item.fileUrl = item.value;
        });
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
      viewAgreementDocument(row) {
        getDetail(row.id).then(res => {
          this.protocolFileList = res.data.data.protocolFile;
          console.log(11, this.protocolFileList);
          this.openDialog = true;
        })
      },
      closeDialog(val) {
        this.openDialog = val;
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
          .then((res) => {
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
            this.agreementTimeArrInit[0] = res.data.data.agreementStartTime;
            this.agreementTimeArrInit[1] = res.data.data.agreementEndTime;
            res.data.data.protocolFile.forEach(item => {
              item.label = item.fileName;
              item.value = item.fileUrl;
            });
            this.form = res.data.data;
            this.form.agreementTimeArr = this.agreementTimeArrInit;
          });
        }
        done();
      },
      searchReset() {
        this.query = {};
        this.agreementSignTime = '';
        this.createTime = '';
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        this.query = params;
        if (this.agreementSignTime) {
          this.query.agreementSignStartTime = this.agreementSignTime[0];
          this.query.agreementSignEndTime = this.agreementSignTime[1];
        }
        if (this.createTime) {
          this.query.createStartTime = this.createTime[0];
          this.query.createEndTime = this.createTime[1];
        }
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
      refreshChange() {
        this.onLoad(this.page, this.query);
      },
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
    }
  };
</script>

<style scoped>
  .agreementNoClass {
    font-weight: normal;
  }
</style>

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
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.partner_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button type="success"
                   size="small"
                   plain
                   icon="el-icon-upload2"
                   @click="handleUpload">导入
        </el-button>
        <el-button type="warning"
                   size="small"
                   plain
                   icon="el-icon-download"
                   @click="handleExport">导出
        </el-button>
      </template>
      <template slot-scope="{row}" slot="custIndustrys2Search">
        <el-select v-model="form.selectCustIndustrys" multiple placeholder="请选择">
          <el-option
            v-for="item in custs"
            :key="item.value"
            :label="item.title"
            :value="item.value">
          </el-option>
        </el-select>
      </template>
      <template slot-scope="{row}" slot="custProvinceQuerySearch">
        <el-cascader
          v-model="form.custProvince"
          :options="custProvinceQuerys"
          :props="{
              checkStrictly: true,
              label: 'title',
              value: 'value'
           }"
          clearable></el-cascader>
      </template>
      <template slot-scope="{row}" slot="custIndustrysForm">
        <avue-cascader v-model="form.custIndustrys" :props="custProps" check-strictly
                       :dic="custIndustrysArr"></avue-cascader>
      </template>
      <template slot-scope="{row}" slot="custCode">
        <el-button
          type="text"
          size="mini"
          @click="detailDialog(row)">
          {{row.custCode}}
        </el-button>
      </template>
      <template slot-scope="" slot="custNameForm">
        <div  class="custNamewWrapper">
          <el-input v-model="form.custName" @change="verifCustName" placeholder="请输入客户名称"  />
          <el-button :type="xinhuaVerifIcon" size="small" type="primary" icon="el-icon-search" @click="openXinhuaVerifFlag" >新华信用</el-button>
          <span v-if="form.isCheck">
          <el-tooltip class="item" effect="dark" content="已验证" placement="top-start">
            <i  style="color: #85ce61;font-size: 200%;vertical-align:-18%"  class="iconfont iconicon_safety"  />
          </el-tooltip>
          </span>
          <span v-if="!form.isCheck">
          <el-tooltip class="item" effect="dark" content="未验证" placement="top-start">
            <i  style="color: red;font-size: 200%;vertical-align:-18%"  class="el-icon-circle-close"  />
          </el-tooltip>
          </span>
        </div>
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
      <!--<template slot="custName" slot-scope="scope">-->
        <!--<avue-text-ellipsis  text="《华盛顿自由灯塔报》几天前报道称，美国国防部官员透露中国近日进行第十次东风-41洲际导弹的试射活动，这次试射中东风-41导弹投射了多个弹头并成功命中中国西部靶场目标。"  :height="40" :width="100" use-tooltip="true" placement="top">-->
          <!--<small slot="more">...</small>-->
        <!--</avue-text-ellipsis>-->
      <!--</template>-->
      <template slot-scope="" slot="updateTimeSearch">
        <el-date-picker
          v-model="updateTime"
          type="daterange"
          value-format="yyyy-MM-dd"
          format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </template>
      <template slot-scope="scope" slot="superUnitForm">
        <div  class="custNamewWrapper">
          <el-input v-model="form.superUnitName" disabled placeholder="请添加上级单位" >
            <el-button slot="append" icon="el-icon-plus" size="mini" @click="addSuperUnit(scope.row.id)">请选择上级单位</el-button>
          </el-input>
        </div>
      </template>
      <template slot-scope="{}" slot="buttonForm">
        <el-button size="small" type="text" @click="openContractDialog" icon="el-icon-download">导入联系人</el-button>
      </template>
      <template slot-scope="{}" slot="orgContactsForm">

         <el-table
           :data="tableData"
           stripe
           border
           size="small"
         >
           <el-table-column width="60px" fixed>
             <template slot="header" slot-scope="">
               <el-button type="primary" icon="el-icon-plus" size="mini" circle @click="addContact"></el-button>
             </template>
             <template slot-scope="scope">
               <el-button type="danger" icon="el-icon-delete" size="mini" circle
                          @click="delContact(scope)"></el-button>
             </template>
           </el-table-column>
           <el-table-column v-if="false"
                            width="100px"
                            prop="id"
                            label="id">
             <template slot-scope="scope" >
               <el-input  placeholder="id" v-model="scope.row.id"></el-input>
             </template>
           </el-table-column>
           <el-table-column
             width="180px"
             prop="contactPerson"
             label="联系人名称">
             <template slot-scope="scope">
               <el-input placeholder="联系人"  v-model="scope.row.contactPerson" maxlength="5" show-word-limit
               ></el-input>
             </template>
           </el-table-column>
           <el-table-column
             width="180px"
             prop="contactType"
             label="联系人类型">
             <template slot-scope="scope">
               <el-input placeholder="联系人类型" v-model="scope.row.contactType" maxlength="10" show-word-limit ></el-input>
             </template>
           </el-table-column>
           <el-table-column
             width="280px"
             prop="contactPhone"
             label="联系方式">
             <template slot-scope="scope">
               <el-input placeholder="联系方式" :disabled="true" v-model="scope.row.contactPhone">
                 <el-button slot="append" icon="el-icon-plus" size="mini"
                            @click="openEmailOrPhone(scope.column,scope.row);"></el-button>
               </el-input>
             </template>
           </el-table-column>
           <el-table-column
             prop="contactEmail"
             width="280px"
             label="邮箱">
             <template slot-scope="scope">
               <el-input placeholder="邮箱" :disabled="true" v-model="scope.row.contactEmail">
                 <el-button disable slot="append" icon="el-icon-plus" size="mini"
                            @click="openEmailOrPhone(scope.column,scope.row);"></el-button>
               </el-input>
             </template>
           </el-table-column>
           <el-table-column
             prop="contactDept"
             width="150px"
             label="部门">
             <template slot-scope="scope">
               <el-input placeholder="部门" maxlength="10" v-model="scope.row.contactDept" show-word-limit></el-input>
             </template>
           </el-table-column>
           <el-table-column
             width="150px"
             prop="contactJob"
             label="职务">
             <template slot-scope="scope">
               <el-input placeholder="职务" maxlength="10" v-model="scope.row.contactJob" show-word-limit></el-input>
             </template>
           </el-table-column>
           <el-table-column
             width="150px"
             prop="custManager"
             label="客户经理">
             <template slot-scope="scope">
               <el-input placeholder="客户经理" maxlength="5" v-model="scope.row.custManager" show-word-limit></el-input>
             </template>
           </el-table-column>
           <el-table-column
             width="180px"
             prop="custOrg"
             label="所属机构">
             <template slot-scope="scope">
               <el-cascader
                 v-model="scope.row.custOrg"
                 placeholder="所属机构"
                 :options="treeData"
                 :props="defaultProps"
                 :show-all-levels="false"
                 clearable
                 filterable>
               </el-cascader>
             </template>
           </el-table-column>
         </el-table>
      </template>
    </avue-crud>
    <detail-org-customer-dialog ref="detailDialog" :id="detailId" :show="showDetailDialog"
                                @cancel="closeDetailDialog"></detail-org-customer-dialog>
    <xinhua-verif-dialog :enterpriseInfo="enterpriseInfo" ref="xinhuaVerifDialog" :custName="form.custName" :xinhuaVerifFlag="xinhuaVerifFlag"
                         @cancel="cancelDialog"></xinhua-verif-dialog>
    <customer-upload-dialog ref="customerUploadDialog" :uploadDialogFlag="uploadDialogFlag"
                            @cancelUploadDialog="cancelUploadDialog"></customer-upload-dialog>
    <customer-export-dialog ref="customerExportDialog" :exportDialogFlag="exportDialogFlag"
                            @cancel="cancelExportDialog"></customer-export-dialog>
    <constract-dialog ref="constractDialog" :constractDialogFlag="constractDialogFlag"
                      @cancelConstractDialog="cancelConstractDialog"></constract-dialog>
    <add-email-or-phone ref="addEmailOrPhone" :emailOrPhoneData="emailOrPhoneData"></add-email-or-phone>
    <customer-dialog ref="customerDialog" :id="superUnitId" :customerDialogFlag="customerDialogFlag" @acceptSuperUnit="acceptSuperUnit"
                     @cancelCustomerDialog="cancelCustomerDialog"></customer-dialog>
  </basic-container>
</template>

<script>
  import {getDeptTree} from "@/api/system/dept";
  import {validateNumber,validatePhone,validateEmail,validateTelephone} from "@/api/tool/validate";
  import {getLazyTree,regionTree} from "@/api/base/region";
  import {getList, getDetail, add, update, remove, queryALl,verifCustName,custTree} from "@/api/contract/customer/orgcustomer";
  import {removeContact,findAllByCustId} from "@/api/contract/customer/contract";
  import {mapGetters} from "vuex";
  import detailOrgCustomerDialog from "./detailOrgCustomerDialog";
  import xinhuaVerifDialog from "./xinhuaVerifDialog";
  import customerUploadDialog from "./customerUploadDialog";
  import customerExportDialog from "./customerExportDialog";
  import constractDialog from "./constractDialog";
  import addEmailOrPhone from "./addEmailOrPhone";
  import customerDialog from "./customerDialog";
  export default {
    components: {
      customerDialog,
      addEmailOrPhone,
      constractDialog,
      customerUploadDialog, xinhuaVerifDialog, detailOrgCustomerDialog, customerExportDialog
    },
    data() {
      return {
        formData:{},
        value1: [],
        custProps: {
          label: 'title',
          value: 'id'
        },
        custs: [],
        custProvinceQuerys: [],
        regions: [],
        custIndustrysArr: [],
        dicCreateAccountData: [{
          label: '全部',
          value: null
        }, {
          label: '否',
          value: 0
        }, {
          label: '是',
          value: 1
        }],
        defaultProps: {
          children: 'children',
          label: 'title',
          value: "id",
          emitPath: false
        },
        showDetailDialog: false,
        customerDialogFlag: false,
        superUnitId: '',
        detailId: "",
        emailOrPhoneData: {},
        enterpriseInfo: {},
        inputVal: '',
        xinhuaVerifFlag: false,
        uploadDialogFlag: false,
        exportDialogFlag: false,
        constractDialogFlag: false,
        createTime: [],
        updateTime: [],
        superUnit: '',
        obj: {
          dynamic: [{
            input: 1,
            select: 1,
            radio: 1,
          }, {
            input: 2,
            select: 2,
            radio: 1,
          }]
        },
        tableData: [],
        treeData: [],
        form: {},
        xinhuaVerifIcon: "info",
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
          calcHeight: 210,
          searchLabelWidth: 100,
          menuWidth: 100,
          labelWidth: 100,
          searchShow: true,
          searchMenuSpan: 6,
          align: 'center',
          menuAlign: 'center',
          tip: false,
          border: true,
          index: false,
          delBtn:false,
          viewBtn: false,
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
              label: "客户编码",
              prop: "custCode",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              search: true,
              order: 2,
              slot:true
            },
            {
              label: "客户名称",
              prop: "custName",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              search: true,
              overHidden: true,
              searchOrder: 10,

            },
            {
              label: "所属地域",
              prop: "regionName",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
            },
            {
              label: "所属地域",
              prop: "custProvinceQuery",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              checkStrictly: true,
              search: true,
              searchslot: true,
              hide: true,
            },
            {
              label: "行业分类",
              prop: "custIndustrys2",
              searchslot: true,
              search: true,
              hide: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false
            },
            {
              label: "行业分类",
              prop: "custIndustryName",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              checkStrictly: true,
            },
            {
              label: "客户类型",
              prop: "custType",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              search: true,
              type: "select",
              rules: [{
                required: true,
                message: "请输入客户类型",
                trigger: "blur"
              }],
              dicUrl: this.getDicValue(this.CONSTANT.CUST_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "客户状态",
              prop: "custState",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
              search: true,
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.CUST_STATE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "公司电话",
              prop: "comPhone",
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,

            },
            {
              label: "创建时间",
              prop: "createTime",
              searchslot: true,
              type: 'date',
              format: 'yyyy-MM-dd',
              valueFormat: 'yyyy-MM-dd',
              search: true,
              span: 12,
              order: 4,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
            },
            {
              label: "最后修改时间",
              prop: "updateTime",
              searchslot: true,
              type: 'date',
              format: 'yyyy-MM-dd',
              valueFormat: 'yyyy-MM-dd',
              search: true,
              span: 12,
              order: 4,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
            }
          ],
          group: [
            {
              label: '客户基本信息',
              prop: 'khjbxx',
              icon: 'el-icon-edit-outline',
              column: [
                {
                  label: '客户名称',
                  suffixIcon: 'el-icon-tickets',
                  prop: 'custName',
                  formslot:true,
                  type:'input',
                  rules: [
                    { required: true,message: "请输入客户名称",trigger: "blur",},
                    { min: 1, max: 30, message: '最长限30个字', trigger: 'blur' }
                    ]
                },
                {
                  label: "客户类型",
                  prop: "custType",
                  type: "select",
                  rules: [{required: true, message: "请选择客户类型", trigger: "blur"}],
                  dicUrl: this.getDicValue(this.CONSTANT.CUST_TYPE),
                  props: {
                    label: "dictValue",
                    value: "dictKey"
                  },
                },
                {
                  label: '上级单位',
                  prop: 'superUnit',
                  formslot: true,

                },
                {
                  label: "所属行业",
                  prop: "custIndustrys",
                  formslot: true,
                  rules: [{
                    required: true,
                    message: "请选择所属行业",
                    trigger: "blur"
                  }],
                  row: true
                },
                {
                  label: "所属地域",
                  prop: "custProvince",
                  type: 'cascader',
                  props: {
                    label: 'title',
                    value: 'value'
                  },
                  rules: [],
                  checkStrictly: true,
                  filterable: true
                },
                {
                  label: '详细地址',
                  prop: 'custAddress',
                  row: true,
                  rules: [
                    {min: 0, max: 100, message: '最长限50个字', trigger: 'blur'}
                  ]
                },
                {
                  label: '邮政编码',
                  prop: 'postcode',
                  rules: [
                    {min: 0, max: 10, message: '最长限10个数字', trigger: 'blur'},
                    {validator: validateNumber, trigger: 'blur'},
                  ]
                },
                {
                  label: '公司电话',
                  prop: 'comPhone',
                  hide: true,
                  rules: [{min: 0, max: 20, message: '最长限20个字', trigger: 'blur'},
                  ]
                },
                {
                  label: '公司传真',
                  prop: 'comFax',
                  rules: [{min: 0, max: 20, message: '最长限20个字', trigger: 'blur'},
                           {validator: validateNumber, trigger: 'blur'},
                  ]
                },
                {
                  label: '公司网址',
                  prop: 'comWebsite',
                  rules: [{min: 0, max: 200, message: '最长限200个字', trigger: 'blur'}]
                },
                {
                  label: "客户状态",
                  prop: "custState",
                  type: "select",
                  dicUrl: this.getDicValue(this.CONSTANT.CUST_STATE),
                  props: {
                    label: "dictValue",
                    value: "dictKey"
                  },
                },
                {
                  label: "业务机会状态",
                  prop: "businessChance",
                  type: "select",
                  dicUrl: this.getDicValue(this.CONSTANT.BUSINESS_CHANCE),
                  props: {
                    label: "dictValue",
                    value: "dictKey"
                  },
                },
                {
                  label: '业务描述',
                  prop: 'businessDesc',
                  type: 'textarea',
                  span: 24,
                  minRows: 4,
                  maxRow: 7,
                  rules: [{min: 0, max: 200, message: '最长限200个字', trigger: 'blur'}]
                },
              ]
            },
            {
              label: '客户管理活动',
              prop: 'kehlhd',
              icon: 'el-icon-edit-outline',
              column: [
                {
                  label: '',
                  prop: 'orgActivities',
                  type: 'dynamic',
                  span: 20,
                  children: {
                    align: 'center',
                    headerAlign: 'center',
                    rowAdd: (done) => {
                      done();
                    },
                    rowDel: (row, done) => {
                      done();
                    },
                    column: [{
                      label: "活动类型",
                      prop: "activityType",
                      type: "select",
                      dicUrl: this.getDicValue(this.CONSTANT.ACTIVITY_TYPE),
                      props: {
                        label: "dictValue",
                        value: "dictKey"
                      },
                    }, {
                      label: "具体内容",
                      prop: "activityContent",
                      maxlength:100,
                      showWordLimit: true
                    }]
                  }
                }
              ]
            },
            {
              label: '联系人信息',
              prop: 'lxrxx',
              icon: 'el-icon-edit-outline',
              column: [
                {
                  label: '',
                  prop: "button",
                  span: 24,
                  formslot: true
                },
                {
                  label: '联系人信息',
                  span: 24,
                  prop: 'orgContacts',
                  formslot: true
                }
              ]
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
      addContact() {
        this.tableData.push({
          contactPerson: "",
          contactType: "",
          contactPhone: "",
          contactEmail: "",
          contactDept: "",
          contactJob: "",
          custManager: "",
          custOrg: ""
        });
      },
      delContact(scope) {
        var contactId=scope.row.id;
        if (contactId) {
          removeContact(contactId).then(res=>{
            this.tableData.splice(scope.$index,1);
          }).catch(e => {
            console.log(e)
          });
        }
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      //客户信息详情弹窗
      detailDialog(row) {
        this.id = row.id;
        this.showDetailDialog = true;
        this.$refs.detailDialog.initData(row.id);
      },
      closeDetailDialog() {
        this.showDetailDialog = false;
      },
      rowSave(row, done, loading) {

        if (this.tableData.length==0){
          loading();
          this.$message.warning("至少添加一个联系人")
          return;
        }else {
          var tipFlag=false;
          this.tableData.forEach(item=>{
            if (!item.custOrg) {
              tipFlag=true;
              this.$message.warning("机构必填")
            }
            if (!item.custManager) {
              tipFlag=true;
              this.$message.warning("客户经理必填")
            }
          })
          if (tipFlag){
            loading();
            return;
          }
        }

        if (row.custProvince&&Array.isArray(row.custProvince)){
          row.custProvince=row.custProvince.join(",")
        }
        row.orgContacts = this.tableData;
        add(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          loading();
        });
      },
      rowUpdate(row, index, done, loading) {
        if (this.tableData.length==0){
          loading();
          this.$message.warning("至少添加一个联系人")
          return;
        }else {
          var tipFlag=false;
          this.tableData.forEach(item=>{
            if (!item.custOrg) {
              tipFlag=true;
              this.$message.warning("机构必填")
            }
            if (!item.custManager) {
              tipFlag=true;
              this.$message.warning("客户经理必填")
            }
          })
          if (tipFlag){
            loading();
            return;
          }
        }
        row.orgContacts = this.tableData;
        if (row.custProvince&&Array.isArray(row.custProvince)){
          row.custProvince=row.custProvince.join(",")
        }
        update(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
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
        this.$confirm("是否确定删除（若客户有关联其他数据则无法删除）?", {
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
        //加载行业级联树
        custTree(3).then(res => {
          this.custIndustrysArr = res.data.data;
        })
        this.tableData = [];
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = {...res.data.data};
            this.form.orgActivities.forEach(item => {
              item.activityType = parseInt(item.activityType)
            });
            //解决下拉字段类型不一样
            this.form.orgContacts.forEach(item => {
              item.custOrg = item.custOrg.toString();
            });
            this.tableData = this.form.orgContacts;
            this.tableData.forEach(item => {
              item.custOrg = item.custOrg.toString();
            })
            if (this.form.custState) {
              this.form.custState = parseInt(this.form.custState)
            }
            if (this.form.custType) {
              this.form.custType = parseInt(this.form.custType)
            }
            if (this.form.businessChance) {
              this.form.businessChance = parseInt(this.form.businessChance)
            }
          });
        }else {
          for(var key in this.form){
            delete this.form[key];
          }
        }
        done();
      },
      searchReset() {
        this.createTime=[];
        this.updateTime=[];
        this.query = {};
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        this.query.custIndustrys = this.form.selectCustIndustrys;
        if (this.form.custProvince){
          this.query.custProvince = this.form.custProvince.join(",");
        }

        if (this.updateTime) {
          this.query.updateTimeStartTime = this.updateTime[0];
          this.query.updateTimeEndTime = this.updateTime[1];
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
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          //处理客户状态回显
          this.data.forEach(item=>{
            if (item.custState){
              item.custState=parseInt(item.custState)
            }
            if (item.custType) {
              item.custType = parseInt(item.custType)
            }
          })
          this.loading = false;
          this.selectionClear();
        });
        // 初始化行业数据查询数据
        custTree(1).then(res => {
          this.custs = res.data.data;
        })
        // 初始化部门数据
        getDeptTree('000000').then(res => {
          this.treeData = res.data.data;
        });
      },
      openXinhuaVerifFlag() {
        this.xinhuaVerifFlag = true;
      },
      cancelDialog(row) {
        if (row.company_name){
          this.form.custName=row.company_name;
          this.form.custAddress=row.address;
          this.form.isCheck=true;
        }
        this.xinhuaVerifFlag = false
      },
      handleUpload() {
        this.uploadDialogFlag = true;
      },
      handleExport() {
        this.exportDialogFlag = true;
      },
      cancelUploadDialog() {

        this.uploadDialogFlag = false;
      },
      cancelExportDialog() {
        this.exportDialogFlag = false;
      },
      openContractDialog() {
        this.constractDialogFlag = true;
      },
      cancelConstractDialog() {
        findAllByCustId(this.form.id).then(item=>{
          this.tableData=item.data.data;
        })
        this.constractDialogFlag = false;
      },
      //上级单位弹窗
      addSuperUnit(id) {
        this.superUnitId=id;
        this.customerDialogFlag = true
      },
      cancelCustomerDialog() {
        this.customerDialogFlag = false
      },
      //接受上级单位
      acceptSuperUnit(row) {
        this.form.superUnitName = row.custName;
        this.form.superUnit = row.id;
      },
      openEmailOrPhone(column, row) {
        this.emailOrPhoneData = row;
        this.$refs.addEmailOrPhone.openType(column, row)
      },
      verifCustName(custName) {
        this.form.isCheck=false;
        verifCustName(custName).then(res => {
          if (res.data.data.createTime) {
            this.$alert(`${custName}客户名称已存在`, '名称已存在', {
              confirmButtonText: '确定',
            });
          }
        });
      },
    },
    mounted() {
      //只加载两级
      regionTree(2).then(res => {
        this.regions = res.data.data;
        // var custProvince = this.findObject(this.option.column, 'custProvinceQuery')
        this.custProvinceQuerys = res.data.data;
        var groupCustProvince = this.findObject(this.option.group, 'custProvince')
        groupCustProvince.dicData = res.data.data;

      })
    }
  };
</script>

<style lang="scss">
  .el-table th {
    background-color: #f5f7fa;
  }
  .custNamewWrapper{
    display: flex;
    flex-direction: row;
  }
</style>

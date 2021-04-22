<template>
  <el-dialog
    :title="title"
    :visible.sync="addOrEditDialog"
    :show-close="false"
    width="80%" center
  >
    <avue-form v-model="obj" :option="option" @submit="save" ></avue-form>

    <span slot="footer" style="text-align: center" class="dialog-footer">
      <el-button @click="cancelDialog">取 消</el-button>
    </span>
  </el-dialog>

</template>

<script>
  import baseInfo from "./eltabs/baseInfo"
  import {queryALl, add} from "@/api/contract/customer/orgcustomer";
  import {getLazyTree} from "@/api/base/region";
  import {getDeptTree} from "@/api/system/dept";

  export default {
    components: {
      baseInfo,
    },
    mounted(){
      // 初始化部门数据
      getDeptTree('000000').then(res => {
        console.log(this.option)
        console.log(this.option.column.contactPerson)
        const column = this.findObject(this.option.column.contactPerson, "custOrg");
        console.log(column)
        column.dicData = res.data.data;
      });
    },
    name: "add-org-customer-dialog",
    props: {
      addOrEditDialog: {
        type: Boolean,
        default: false
      }
    },
    methods: {
      cancelDialog() {
        this.$emit('cancel');
      },
    },
    data() {
      return {
        title: "编辑",
        superUnitDialog: false,
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
        form: {
          title: '客户基本信息',
        },
        option: {
          submitText: '完成',
          column: [{
            label: "",
            labelWidth: 50,
            type: 'title',
            prop: "title",
            span: 24,
            styles: {
              fontSize: '24px'
            }
          },
            {
              label: '客户名称',
              suffixIcon: 'el-icon-tickets',
              prop: 'custName',
              span: 8,
              rules: [{
                required: true,
                message: "请输入客户名称",
                trigger: "blur"
              }]
            },
            {
              label: "客户类型",
              prop: "custType",
              type: "select",
              rules: [{
                required: true,
                message: "请输入客户名称",
                trigger: "blur"
              }],
              dicUrl: this.getDicValue(this.CONSTANT.CUST_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              span: 8,
              row: true
            },
            {
              label: "上级单位",
              prop: "superUnit",
              click: ({value, column}) => {
                this.$message.success('查看控制台')
                console.log('点击事件', value, column)
                this.superUnitDialog = true;
              },
              dicData: [{
                label: '真',
                value: 'true'
              }, {
                label: '假',
                value: 'false'
              }],
              span: 8,
            },
            {
              label: "所属行业",
              prop: "custIndustrys",
              type: "cascader",
              checkStrictly: true,
              props: {
                label: 'name',
                value: 'code'
              },
              lazy: true,
              lazyLoad(node, resolve) {
                console.log(node.value)
                let stop_level = 2;
                let level = node.level;
                let data = node.data || {}
                let code = data.code;
                let list = [];
                let callback = () => {
                  resolve((list || []).map(ele => {
                    return Object.assign(ele, {
                      leaf: level >= stop_level
                    })
                  }));
                }
                if (level == 0) {
                  queryALl().then(res => {
                    list = res.data.data;
                    callback()
                  })
                } else {

                  queryALl(node.value).then(res => {
                    list = res.data.data;
                    callback()
                  })
                }
              },
              span: 8,
              row: true
            },
            {
              label: "所属地域",
              prop: "region",
              type: 'tree',
              span: 8,
              leafOnly: true,
              viewDisplay: false,
              props: {
                label: 'title',
                value: 'value'
              },
              lazy: true,
              treeLoad: function (node, resolve) {
                const parentCode = (node.level === 0) ? "00" : node.data.id;
                getLazyTree(parentCode).then(res => {
                  resolve(res.data.data.map(item => {
                    return {
                      ...item,
                      leaf: !item.hasChildren
                    }
                  }))
                });
              },
              rules: []
            },
            {
              label: '详细地址',
              prop: 'custAddress',
              span: 8,
              row: true
            },
            {
              label: '邮政编码',
              prop: 'postcode',
              span: 8,
            },
            {
              label: '公司电话',
              prop: 'comPhone',
              span: 8,
              row: true
            },
            {
              label: '公司传真',
              prop: 'comFax',
              span: 8,
            },
            {
              label: '公司网址',
              prop: 'comWebsite',
              span: 8,
              row: true
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
              span: 8,
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
              span: 8,
              row: true
            },
            {
              label: '业务描述',
              prop: 'businessDesc',
              type: 'textarea',
              minRows: 10,
              maxlength: 200,
              span: 16,
              showWordLimit: true
            },
            {
              label: '客户管理活动',
              prop: 'orgActivities',
              type: 'dynamic',
              span: 16,
              children: {
                align: 'center',
                headerAlign: 'center',
                rowAdd: (done) => {
                  this.$message.success('新增回调');
                  done({
                    input: '默认值'
                  });
                },
                rowDel: (row, done) => {
                  this.$message.success('删除回调' + JSON.stringify(row));
                  done();
                },
                column: [{
                  label: "活动类型",
                  prop: "activityType",
                  type: "select",
                  // rules: [{
                  //   required: true,
                  //   message: "请输入客户名称",
                  //   trigger: "blur"
                  // }],
                  dicUrl: this.getDicValue(this.CONSTANT.ACTIVITY_TYPE),
                  props: {
                    label: "dictValue",
                    value: "dictKey"
                  },
                  span: 8,
                }, {
                  label: "用户名",
                  prop: "activityContent",
                  // rules: [{
                  //   required: true,
                  //   message: "请输入用户名",
                  //   trigger: "blur"
                  // }]
                }]
              }
            },
            {
              label: '联系人信息',
              prop: 'orgContacts',
              type: 'dynamic',
              span: 16,
              children: {
                align: 'center',
                headerAlign: 'center',
                rowAdd: (done) => {
                  this.$message.success('新增回调');
                  done({
                    input: '默认值'
                  });
                },
                rowDel: (row, done) => {
                  this.$message.success('删除回调' + JSON.stringify(row));
                  done();
                },
                column: [{
                  // width: 200,
                  label: '联系人名称',
                  prop: "contactPerson",
                },
                  {
                    // width: 200,
                    label: '联系人类型',
                    prop: "contactType",
                  }, {
                    // width: 200,
                    label: '联系方式',
                    prop: "contactPhone",
                  }, {
                    // width: 200,
                    label: '邮箱',
                    prop: "contactEmail",
                  }, {
                    // width: 200,
                    label: '部门',
                    prop: "contactDept",
                  }, {
                    // width: 200,
                    label: '职务',
                    prop: "contactJob",
                  },
                  {
                    // width: 200,
                    label: '客户经理',
                    prop: "custManager",
                  },
                  {
                    label: "所属机构",
                    prop: "custOrg",
                    type: "tree",
                    multiple: false,
                    props: {
                      label: "title",
                    },
                    slot: true,
                    checkStrictly: true
                  }]
              }
            }


          ]
        }
      }
    },
    methods: {
      cancelDialog() {
        this.$emit('cancel');
      },
      save(obj, done) {
        console.log(obj)
        var res = add(obj);
        done();
      },
    }
  };
</script>

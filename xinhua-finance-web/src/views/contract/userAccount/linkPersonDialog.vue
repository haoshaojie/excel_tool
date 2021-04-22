<template>
  <el-dialog
    title="选择联系人"
    :visible.sync="showLinkPersonDialog"
    @close="closeLinkPersonDialog"
    width="80%"
    center>
    <avue-crud ref="crud"
               :option="option"
               :table-loading="loading"
               @on-load="onLoad"
               :data="data" @row-click="handleRowClick">
    </avue-crud>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="chooseLinkPerson()">确 定</el-button>
      <el-button @click="closeLinkPersonDialog">取 消</el-button>
    </span>
  </el-dialog>
</template>

<script>
import {getContractList} from "@/api/contract/user/orguser";
import {listById} from "@/api/system/dept";


export default {
  name: "link-person-dialog",
  props: {
    showLinkPersonDialog: {
      type: Boolean,
      default: false
    },
    customerId: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      loading: false,
      chooseLink: {},
      data: [],
      option: {
        highlightCurrentRow: true,
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
      depts: [],
      custData: [],
    }
  },
  computed: {
    ids() {
      let ids = [];
      this.custData.forEach(ele => {
        ids.push(ele.custOrg);
      });
      return ids.join(",");
    }
  },
  watch: {
    customerId: {
      handler: function (newVal, oldVal) {
        this.onLoad();
      },
      immediate: true
    }
  },
  methods: {
    closeLinkPersonDialog() {
      this.$emit("cancelLinkPersonDialog");
    },
    onLoad() {
      this.loading = true;
      if (typeof this.customerId == "string" && this.customerId.length > 0)
        getContractList(this.customerId).then(res => {
          //获取机构数据
          this.custData = res.data.data;
          listById(this.ids).then(r => {
            this.depts = r.data.data;
            this.depts.forEach(item => {
              this.custData.forEach(cust => {
                if (item.id == cust.custOrg) {
                  cust.custOrgName = item.deptName
                }
              })
            })
            //赋值列表数据
            this.data = this.custData;
            this.loading = false;
          }, error => {
            //赋值列表数据
            this.data = this.custData;
            this.loading = false;
          })
        }, error => {
          window.console.log(error);
        });
    },
    handleRowClick(row, event, column) {
      this.chooseLink = row;
    },
    chooseLinkPerson() {
      this.depts = [];
      this.$emit("chooseLinkPerson", this.chooseLink);
      this.$emit("cancelLinkPersonDialog");
    },
  }
}
</script>

<style scoped>

</style>

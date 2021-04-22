<template>
  <basic-container>
    <avue-crud v-show="!showAdd"
      :option="option"
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
      <template slot="prodName" slot-scope="scope">
        <el-link type="primary" @click.native="rowView(scope.row)">{{scope.row.prodName}}</el-link>
        <el-tag size="small" v-if="scope.row.prodState == 1" effect="plain" style="float:right;">{{scope.row.prodStateName}}</el-tag>
        <el-tag size="small" v-if="scope.row.prodState == 2" effect="plain" type="warning" style="float:right;">{{scope.row.prodStateName}}</el-tag>
        <el-tag size="small" v-if="scope.row.prodState == 3" effect="plain" type="info" style="float:right;">{{scope.row.prodStateName}}</el-tag>
        <el-tag size="small" v-if="scope.row.prodState == 4" effect="plain" type="success" style="float:right;">{{scope.row.prodStateName}}</el-tag>
      </template>
      <template  slot-scope="{}" slot='createTimeSearch'>
        <el-date-picker
          v-model="createTime"
          type="datetimerange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['12:00:00']">
        </el-date-picker>
      </template> 
      <template  slot-scope="{}" slot='updateTimeSearch'>
        <el-date-picker
          v-model="updateTime"
          type="datetimerange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['12:00:00']">
        </el-date-picker>
      </template>      
      <template slot="menuLeft">
        <el-button type="primary" size="small"
          icon="el-icon-plus"
          v-if="permission.product_add"
          @click="handleAdd"
          >新 增</el-button>
            <!-- @click="handleAdd" -->
        <el-button type="primary"
          size="small"
          icon="el-icon-sell"
          v-if="permission.product_added"
          @click="handleState(1)">上 架
        </el-button>
        <el-button type="primary"
          size="small"
          icon="el-icon-sold-out"
          v-if="permission.product_expired"
          @click="handleState(2)">下 架
        </el-button>
        <el-button type="primary"
          size="small"
          v-if="permission.product_export"
          icon="el-icon-download"
          @click="handleExport">导出
        </el-button>
        <el-button type="danger"
          size="small"
          icon="el-icon-delete"
          v-if="permission.product_delete"
          @click="handleDelete">删 除
        </el-button>
        
      </template>
     <template slot-scope="scope" slot="menu">
      <el-button v-if="permission.product_edit" icon="el-icon-edit-outline" type="text" size="small" @click.native="rowEdit(scope.row)">编辑</el-button>
      <el-button v-if="permission.product_added && (scope.row.prodState == 2 || scope.row.prodState == 3)" icon="el-icon-sell" type="text" size="small" @click.native="rowState(scope.row.id,1)">上架</el-button>
      <el-button v-if="permission.product_expired && scope.row.prodState == 1" icon="el-icon-sold-out" type="text" size="small" @click.native="rowState(scope.row.id,2)">下架</el-button>
      <el-button v-if="permission.product_delete" icon="el-icon-delete" type="text" size="small" @click.native="rowDel(scope.row)" style='color: #E02020;'>删除</el-button>
      <el-button v-if="permission.product_view" icon="el-icon-reading" type="text" size="small" @click.native="rowView(scope.row)">详情</el-button>
    </template>
    </avue-crud>
    <product-add v-if="showAdd" @back="back" :id="id" :is-view="isView" ></product-add>
  </basic-container>
</template>

<script>
  import {getList, updateState, remove, exportData} from "@/api/contract/product/product";
  import productAdd from './product/product-add';
  import {getDeptTree} from "@/api/system/dept";
  import {mapGetters} from "vuex";
  import website from '@/config/website';
  import {getToken} from '@/util/auth';
  export default {
    components:{productAdd},
    data() {
      return {
        showAdd:false,
        id:'',
        isView:false,
        form: {},
        query: {},
        createTime:[],
        updateTime:[],
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          menuWidth:280,
          calcHeight: 210,
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: false,
          addBtn:false,
          editBtn:false,
          delBtn:false,
          menuFixed:false,
          selection: true,
          column: [
            {
              label: "主键id 雪花算法产生",
              prop: "id",
              hide: true
            },
            {
              label: "产品名称",
              width: 240,
              prop: "prodName",
              slot:true,
              fixed: true,
              search: true,
              searchOrder:8
            },
            {
              label: "产品编码",
              prop: "prodCode",
              fixed: true,
              search: true,
              searchOrder:7
            },
            
            {
              label: "所属部门",
              prop: "propDept",
              type: "select",
              search: true,
              searchOrder:4,
              searchFilterable:true,
              dicData: [],
              props: {
                label: "title",
                value: "value"
              }
            },
            {
              label: "应用分类",
              prop: "prodType",
              search: true,
              searchOrder:3,
              type: "select",
              searchFilterable:true,
              dicUrl: this.getDicValue(this.CONSTANT.PRODUCT_TYPE),
              props: {
                label: "dictValue",
                value: "dictKey"
              }
            },
            {
              label: "发布范围",
              prop: "releaseScope",
              search: true,
              searchOrder:6,
              type: "select",
              searchFilterable:true,
              dicUrl: this.getDicValue(this.CONSTANT.PRODUCT_RANGE),
              props: {
                label: "dictValue",
                value: "dictKey"
              }
            },
            {
              label: "创建时间",
              prop: "createTime",
              width: 150,
              search: true,
              searchOrder:2,
              searchslot: true,
              searchSpan:12,
              type: 'datetimerange',
              row:true,
              startPlaceholder: '时间日期开始范围自定义',
              endPlaceholder: '时间日期结束范围自定义'
            },
            {
              label: "创建人",
              prop: "createUserName"
            },
            {
              label: "修改时间",
              prop: "updateTime",
              width: 150,
              search: true,
              searchOrder:1,
              searchslot: true,
              type: 'datetimerange',
              searchSpan:12,
              row:true,
              startPlaceholder: '时间日期开始范围自定义',
              endPlaceholder: '时间日期结束范围自定义'
            },
            {
              label: "更新人",
              prop: "updateUserName"
            },
            {
              label: "上架日期",
              prop: "addedDate",
              width: 150,
            },
            {
              label: "下架日期",
              prop: "expiredDate",
              width: 150,
            },
            {
              label: "产品状态",
              prop: "prodState",
              search: true,
              searchOrder:5,
              type: "select",
              dicUrl: this.getDicValue(this.CONSTANT.PRODUCT_STATE),
              props: {
                label: "dictValue",
                value: "dictKey"
              }
            }
          ]
        },
        data: []
      };
    },
    watch: {
      'form.tenantId'() {
        if (this.form.tenantId !== '') {
          getDeptTree(this.form.tenantId).then(res => {
            const column = this.findObject(this.option.column, "propDept");
            column.dicData = res.data.data;
          });
        }
      },
    },
    computed: {
      ...mapGetters(["permission"]),
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    methods: {
      handleExport(){
        (this.query)
        this.$confirm("是否导出产品数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          let url = exportData();
          window.open(url+`?${website.tokenHeader}=${getToken()}`);
        });
      },
      back(){
        this.showAdd=false;
        this.onLoad(this.page);
      },
      rowState(ids, state){
        this.$confirm(state===1?"确定将选择数据上架?":"确定将选择数据下架?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          return updateState(ids,state);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        });
      },
      handleState(state){
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        for(let i =0; i<this.selectionList.length; i++){
          if(this.selectionList[i].prodState === 4){
            this.$message.warning("草稿数据不能进行上下架操作!");
            return;
          }
        }
        this.rowState(this.ids,state);
      },
      handleAdd(){
        this.showAdd=true;
        this.isView=false;
        this.id="";
        //this.$router.push({path: '/contract/product/productitemAdd'});
      },
      rowView(row) {
        console.log('view',row.id)
        this.id=row.id;
        this.isView=true;
        this.showAdd=true
      },
      rowEdit(row) {
        console.log('edit',row.id)
        this.id=row.id;
        this.isView=false;
        this.showAdd=true;
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
      searchReset() {
        this.query = {};
        this.createTime=[]
        this.updateTime=[]
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        this.query = params;
        this.page.currentPage = 1;
        if(this.createTime && this.createTime.length > 0){
          this.query.createStartTime=this.createTime[0] 
          this.query.createEndTime=this.createTime[1] 
        }
        if(this.updateTime && this.updateTime.length > 0){
          this.query.updateStartTime=this.updateTime[0] 
          this.query.updateEndTime=this.updateTime[1] 
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
          this.loading = false;
          this.selectionClear();
        });
        getDeptTree(this.form.tenantId).then(res => {
          const column = this.findObject(this.option.column, "propDept");
          column.dicData = res.data.data;
        });
      }
    }
  };
</script>

<style>
</style>

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
      <template slot="propName" slot-scope="scope">
        <el-link type="primary" @click.native="rowView(scope.row)">{{scope.row.propName}}</el-link>
        <el-tag size="small" v-if="scope.row.itemState == 1" effect="plain" style="float:right;">{{scope.row.itemStateName}}</el-tag>
        <el-tag size="small" v-if="scope.row.itemState == 2" effect="plain" type="warning" style="float:right;">{{scope.row.itemStateName}}</el-tag>
        <el-tag size="small" v-if="scope.row.itemState == 3" effect="plain" type="info" style="float:right;">{{scope.row.itemStateName}}</el-tag>
        <el-tag size="small" v-if="scope.row.itemState == 4" effect="plain" type="success" style="float:right;">{{scope.row.itemStateName}}</el-tag>
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
          <el-button type="primary"
            size="small"
            icon="el-icon-plus"
            v-if="permission.productitem_add"
            @click="handleAdd"
            >新 增</el-button>
            <!-- @click="handleAdd" -->
        <el-button type="primary"
                   size="small"
                   icon="el-icon-sell"
                   v-if="permission.productitem_added"
                    @click="handleItemState(1)">上 架
        </el-button>
        <el-button type="primary"
                   size="small"
                   icon="el-icon-sold-out"
                   v-if="permission.productitem_expired"
                   @click="handleItemState(2)">下 架
        </el-button>
        <el-button type="primary"
                   size="small"
                   v-if="permission.productitem_export"
                   icon="el-icon-download"
                   @click="handleExport">导出
        </el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   v-if="permission.productitem_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
     <template slot-scope="scope" slot="menu">
      <el-button v-if="permission.productitem_edit" icon="el-icon-edit-outline" type="text" size="small" @click.native="rowEdit(scope.row)">编辑</el-button>
      <el-button v-if="permission.productitem_added && (scope.row.itemState == 2 || scope.row.itemState == 3)" icon="el-icon-sell" type="text" size="small" @click.native="rowItemState(scope.row.id,1)">上架</el-button>
      <el-button v-if="permission.productitem_expired && scope.row.itemState == 1" icon="el-icon-sold-out" type="text" size="small" @click.native="rowItemState(scope.row.id,2)">下架</el-button>
      <el-button v-if="permission.productitem_delete" icon="el-icon-delete" type="text" size="small" @click.native="rowDel(scope.row)" style='color: #E02020;'>删除</el-button>
      <el-button v-if="permission.productitem_view" icon="el-icon-reading" type="text" size="small" @click.native="rowView(scope.row)">详情</el-button>
    </template>
    </avue-crud>
    <productitem-add v-if="showAdd" @back="back" :id="id" :is-view="isView" ></productitem-add>
  </basic-container>
  
</template>


<script>
  import productitemAdd from './productItem/productitem-add';
  import {getProdAddedList} from "@/api/contract/product/product";
  import {getList, remove,updateItemState,exportData} from "@/api/contract/product/productitem";
  import {selectItems} from "@/api/contract/product/category";
  import {mapGetters} from "vuex";
  import {getDeptTree} from "@/api/system/dept";
  import website from '@/config/website';
  import {getToken} from '@/util/auth';
  export default {
    components:{
      productitemAdd,
    },
    data() {
      return {
        showAdd:false,
        id:'',
        isView:false,
        form: {},
        createTime:[],
        updateTime:[],
        query: {},
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
          refreshBtn: false,
          columnBtn: false,
          searchShowBtn: false,
          searchLabelWidth:100,
          column: [
            {
              label: "id",
              prop: "id",
              hide: true
            },
            {
              label: "产品项名称",
              prop: "propName",
              slot:true,
              width: 240,
              searchOrder:8,
              fixed: true,
              search: true
            },
            {
              label: "产品项编码",
              prop: "propCode",
              fixed: true,
              search: true,
              searchOrder:9,
              width: 100
            },
            {
              label: "所属部门",
              prop: "propDept",
              type: "select",
              search: true,
              searchOrder:2,
              searchFilterable:true,
              dicData: [],
              props: {
                label: "title",
                value: "value"
              }
            },
            {
              label: "产品类型",
              prop: "cateId",
              search: true,
              searchOrder:6,
              type: "select",
              dicData: [],
              searchFilterable:true,
              props: {
                label: "cateName",
                value: "id"
              }
            },
            {
              label: "发布应用",
              prop: "prodId",
              search: true,
              hide:true,
              searchOrder:5,
              type: "select",
              searchFilterable:true,
              dicData: [],
              props: {
                label: "prodName",
                value: "id"
              }
            },
            {
              label: "发布范围",
              prop: "releaseScope",
              search: true,
              searchOrder:7,
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
              searchOrder:3,
              searchslot: true,
              type: 'datetimerange',
              searchSpan:12,
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
              prop: "itemState",
              search: true,
              searchOrder:4,
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
          selectItems().then(res => {
            const column = this.findObject(this.option.column, "cateId");
            column.dicData = res.data.data;
          });
          getProdAddedList().then(res => {
            const column = this.findObject(this.option.column, "prodId");
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
        this.$confirm("是否导出产品项数据?", "提示", {
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
      rowItemState(ids, itemState){
        this.$confirm(itemState===1?"确定将选择数据上架?":"确定将选择数据下架?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
            return updateItemState(ids,itemState);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
          });
      },
      handleItemState(itemState){
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        for(let i =0; i<this.selectionList.length;i++){
          if(this.selectionList[i].itemState === 4){
            this.$message.warning("草稿数据不能进行上下架操作!");
            return false;
          }
        }
        this.rowItemState(this.ids,itemState);
        
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
            console.log(row)
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
        selectItems().then(res => {
          const column = this.findObject(this.option.column, "cateId");
          column.dicData = res.data.data;
        });
        getProdAddedList().then(res => {
          const column = this.findObject(this.option.column, "prodId");
          column.dicData = res.data.data;
        });
      }
    }
  };
</script>

<style>
</style>

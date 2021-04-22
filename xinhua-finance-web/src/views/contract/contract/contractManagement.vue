<!--合约管理-->
<template>
    <basic-container>
        <avue-crud
                v-show="!showAdd"
               :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               :search.sync="searchCondition"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @refresh-change="refreshChange"
               @on-load="onLoad"
               @select="select"
               @select-all="selectAll">
               <template slot="menuLeft">
                 <el-button 
                      type="primary" 
                      size="small"
                      icon="el-icon-plus"
                      @click="addHandle">
                      创建合约
                    </el-button>
                  <el-button 
                      type="danger" 
                      size="small"
                      icon="el-icon-delete"
                      plain
                      @click="handleRemove">
                      删除
                    </el-button>
                    <el-button 
                      type="warning"
                      size="small"
                      plain
                      icon="el-icon-download"
                      @click="handleExport">
                      导出
                    </el-button>
                </template>
                <template slot="search" slot-scope="{}">
                  <div class="solt">
                    <div class="solt-search">
                        <label  class="el-form-item__label"> 客户经理:</label>
                        <el-input placeholder="客户经理"  size="small" v-model="form.manager"></el-input>
                    </div>
                    <div class="solt-search">
                        <label class="el-form-item__label" > 创建时间:</label>
                        <el-date-picker
                          size="small"
                          v-model="form.createTime"
                          type="datetimerange"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          format="yyyy-MM-dd HH:mm:ss"
                          range-separator="至"
                          start-placeholder="开始时间"
                          end-placeholder="结束时间">
                        </el-date-picker>
                  </div>
                  </div>
                </template>
                <template slot-scope="{type,size}" slot="menu">
                  <el-button  :size="size" :type="type">编辑</el-button>
                  <el-button  :size="size" :type="type">绑定权限</el-button>
                </template>
                <template slot="conNo" slot-scope="{row}">
                  <el-button type="text" size="mini" @click="getDetail(row)">{{row.conNo}}</el-button>
                </template>
        </avue-crud>
        <add-contract v-if="showAdd" @addBack="addBack" :dicDataList="dicDataList"></add-contract>
        <contract-manageDetail-dailog ref="contractManageDetailDailog"></contract-manageDetail-dailog>
        <contract-export-dialog ref="customerExportDialog" :exportDialogFlag="exportDialogFlag"
                :searchCondition="searchCondition" :selectedRows="selections" @cancel="cancelExportDialog">
        </contract-export-dialog>
    </basic-container>
</template>
<script>
import {getPage, removeContract} from "@/api/contract/contract/contract";
import {mapGetters} from "vuex";
import {getDeptTree} from "@/api/system/dept";
import addContract from './addContract';
import {getStore} from "@/util/store";
import {getUser} from "@/api/system/user";
import contractManageDetailDailog from './components/contractManageDetailDailog';
import contractExportDialog from "./components/contractExportDialog";

export default {
    components: { 
      addContract,
      contractManageDetailDailog,
      contractExportDialog
    },
    data(){
        return{
            showAdd:false,//true:新增窗口展示，列表页隐藏
            exportDialogFlag: false,
            form: {},
            query: {},
            dicDataList: [],//当前登录人的数据权限部门
            loading: true,
            page: {
                pageSize: 10,
                currentPage: 1,
                total: 0
            },
            data:[],
            option: {
                calcHeight: 210,
                searchShow: true,
                searchMenuSpan: 6,
                tip: false,
                border: true,
                index: false,
                viewBtn:false,
                addBtn:false,
                editBtn:false,
                delBtn:false,
                selection: true,
                column:[
                    {
                        label: "合约编码",
                        prop: "conNo",
                        search:true,
                        searchOrder:6,
                        display: false,
                        slot:true
                    },
                    {
                        label: "合约名称",
                        prop: "conName",
                        search:true,
                        searchOrder:7,
                        display: false
                    },
                    {
                        label: "客户名称",
                        prop: "custName",
                        search:true,
                        searchOrder:5,
                        display: false
                    },
                    {
                        label: "所属部门",
                        prop: "deptId",
                        search:true,
                        searchOrder:4,
                        type:"select",
                        searchFilterable:true,
                        display: false,
                        dicData: [],
                        props: {
                          label: "title",
                          value: "value"
                        }
                    },
                    {
                        label: "合约类型",
                        prop: "conType",
                        type:"select",
                        search:true,
                        searchOrder:2,
                        display: false,
                        props: {
                          label: 'dictValue',
                          value: 'dictKey'
                        },
                        dicUrl: this.getDicValue("contract_type")
                    },
                    {
                        label: "合约状态",
                        prop: "conState",
                        type:"select",
                        search:true,
                        searchOrder:3,
                        display: false,
                        props: {
                          label: 'dictValue',
                          value: 'dictKey'
                        },
                        dicUrl: this.getDicValue("contract_state")
                    },
                    {
                        label: "合约金额",
                        prop: "conAmount",
                        display: false
                    },
                    {
                        label: "合约起始日期",
                        prop: "conStartTime",
                        type: "date",
                        format: "yyyy-MM-dd hh:mm:ss",
                        valueFormat: "yyyy-MM-dd hh:mm:ss",
                        display: false
                    },
                    {
                        label: "合约起始日期",
                        prop: "conEndTime",
                        type: "date",
                        format: "yyyy-MM-dd hh:mm:ss",
                        valueFormat: "yyyy-MM-dd hh:mm:ss",
                        display: false
                    }
                ]
            },
          selections:{},
          searchCondition:{}
        }
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.contract_add, false),
          editButton: this.vaildData(this.permission.contract_edit, false),
          bindBtn: this.vaildData(this.permission.contract_bind, false)
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
    mounted(){
      this.getCurrDicData();
    },
    methods: {
      addContract(){
      },
      addHandle(){
        this.showAdd=true;
      },
      addBack(){
        this.showAdd=false;
        this.onLoad(this.page);
      },
      handleRemove(){
        let params = {};
        params.contractRemoveIds = [];
        for (var id in this.selections) {
          params.contractRemoveIds.push(""+id);
        }
        if(params.contractRemoveIds.length <= 0) {
          this.$message({
            type: 'warning',
            message: '未选择合约信息！'
          });
          return ;
        }
        this.$confirm("是否确定删除（绑定产品和用户的合约不可删除）?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          title:"删除合约",
          type: "warning"
        }).then(() => {
          removeContract(params).then((result)=>{
            // 1 根据返回结果如果删除成功 弹出提示框
            console.log(result);
            if(result.data.data.length > 0) {
              // 批量删除有不可删除信息
              this.$confirm(result.data.msg, {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                title:"删除合约",
                type: "warning",
                dangerouslyUseHTMLString:true
              }).catch(()=>{});
            } else {
              this.$message({
                type: 'success',
                message: result.data.msg
              });
            }
          });
        }).catch(()=>{
        });
      },
      handleExport(){
        this.exportDialogFlag = true;
      },
      cancelExportDialog() {
        this.exportDialogFlag = false;
      },
      getDetail(row){
        this.$refs.contractManageDetailDailog.openDalog(row);
      },
      // rowSave(row, done, loading) {
      //   add(row).then(() => {
      //     done();
      //     this.onLoad(this.page);
      //     this.$message({
      //       type: "success",
      //       message: "操作成功!"
      //     });
      //   }, error => {
      //     window.console.log(error);
      //     loading();
      //   });
      // },
      // rowUpdate(row, index, done, loading) {
      //   update(row).then(() => {
      //     done();
      //     this.onLoad(this.page);
      //     this.$message({
      //       type: "success",
      //       message: "操作成功!"
      //     });
      //   }, error => {
      //     window.console.log(error);
      //     loading();
      //   });
      // },
      // rowDel(row) {
      //   this.$confirm("确定将选择数据删除?", {
      //     confirmButtonText: "确定",
      //     cancelButtonText: "取消",
      //     type: "warning"
      //   })
      //     .then(() => {
      //       return remove(row.id);
      //     })
      //     .then(() => {
      //       this.onLoad(this.page);
      //       this.$message({
      //         type: "success",
      //         message: "操作成功!"
      //       });
      //     });
      // },
      // handleDelete() {
      //   if (this.selectionList.length === 0) {
      //     this.$message.warning("请选择至少一条数据");
      //     return;
      //   }
      //   this.$confirm("确定将选择数据删除?", {
      //     confirmButtonText: "确定",
      //     cancelButtonText: "取消",
      //     type: "warning"
      //   })
      //     .then(() => {
      //       return remove(this.ids);
      //     })
      //     .then(() => {
      //       this.onLoad(this.page);
      //       this.$message({
      //         type: "success",
      //         message: "操作成功!"
      //       });
      //       this.$refs.crud.toggleSelection();
      //     });
      // },
      beforeOpen(done, type) {
        if (["edit", "view"].includes(type)) {
          this.getDetail(this.form.id).then(res => {
            this.form = res.data.data;
          });
        }
        done();
      },
      searchReset() {
        this.query = {};
        this.form.createTime="";
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        console.log(params);
        this.query = params;
        this.query.custManager = this.form.manager;
        if(this.form.createTime){
          this.query.startCreateTime = this.form.createTime[0];
          this.query.endCreateTime = this.form.createTime[1];
        }
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done();
      },
      selectionChange(list) {
        this.selectionList = list;
      },
      checkRemove(selection, row) {
        // 判断是否取消选中
        let hasRemoveFlag = true;
        for(var i = 0; i < selection.length; i++) {
          if(selection[i].id == row.id) {
            hasRemoveFlag = false;
            break;
          }
        }
        return hasRemoveFlag;
      },
      selectAll(selection){
        for(let i = 0; i < selection.length; i++) {
          this.select(selection, selection[i]);
        }
      },
      select(selection, row){
        // 判断是否取消选中
        let hasRemoveFlag = this.checkRemove(selection, row);
        if(hasRemoveFlag) {
          delete this.selections[row.id];
        } else {
          this.selections[row.id] = row;
        }
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
      //获取当前登录人的数据权限部门信息
      getCurrDicData(){
        getDeptTree(this.form.tenantId).then(res => {
          //所有的部门
          const allDep = res.data.data;
          //当前登录人的信息
          const userInfo= getStore({name: 'userInfo'}) || [];
          getUser(userInfo.userId).then(res => {
            const curruser = res.data.data;
            //用户部门数据权限字符串
            const userdataStr = curruser.dataAuthority;
            //用户部门数据权限数组
            const arr = userdataStr.split(",");
            //遍历获取当前登录人可访问的部门
            var dicData = [];
            for(var i=0,len = allDep.length;i<len;i++){
              if(arr.indexOf(allDep[i].id)>=0){
                dicData.push(allDep[i]);
              }
            }
            this.dicDataList = dicData;
            const column = this.findObject(this.option.column,"deptId");
            column.dicData = dicData;
          });
        });
      },
      onLoad(page, params = {}) {
        this.loading = false;
        getPage(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
        // getDeptTree(this.form.tenantId).then(res => {
        //   //所有的部门
        //   const allDep = res.data.data;
        //   //当前登录人的信息
        //   const userInfo= getStore({name: 'userInfo'}) || [];
        //   getUser(userInfo.userId).then(res => {
        //     const curruser = res.data.data;
        //     //用户部门数据权限字符串
        //     const userdataStr = curruser.dataAuthority;
        //     //用户部门数据权限数组
        //     const arr = userdataStr.split(",");
        //     //遍历获取当前登录人可访问的部门
        //     var dicData = [];
        //     for(var i=0,len = allDep.length;i<len;i++){
        //       if(arr.indexOf(allDep[i].id)>=0){
        //         dicData.push(allDep[i]);
        //       }
        //     }
        //     const column = this.findObject(this.option.column,"deptId");
        //     column.dicData = dicData;
        //   });
        // });
      }
    }
}
</script>
<style lang="scss">
.solt{
  display: flex;
  width: 50%;
.solt-search{
  padding-left:10px;
  padding-right:10px; 
  width:100%;
  display: flex;
  .el-form-item__label{
    width: 110px;
  }
}
}
</style>

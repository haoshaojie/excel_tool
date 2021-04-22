<!--创建合约的用户信息信息-->
<template>
    <basic-container>
         <el-button 
            type="primary" 
            size="small"
            @click="showAssociation">
            关联用户协议
        </el-button>
        <div class="el-agreement-form">
            <el-form :inline="true"  size="small" label-position="left" :model="userAgreement">
                <el-form-item v-for="(item,index) in userAgreement.itemValues" :key="index" :label="`用户协议${index+1}`">
                    <el-input :placeholder="`用户协议${index+1}`" v-model="item.agreementName" readonly="true"></el-input>
                    <el-button  icon="el-icon-delete" circle @click="deleteItem(item,index)"></el-button>
                </el-form-item>
            </el-form>
        </div>
         <div class="el-step-footer">
            <el-button type="primary" size="small" plain @click="back">上一步</el-button>
            <el-button type="primary" size="small" @click="save">保&nbsp;&nbsp;&nbsp;存</el-button>
            <el-button type="primary" size="small" plain @click="next">下一步</el-button>
        </div>
        <!-- 关联用户协议 -->
        <el-dialog 
        title="用户协议"
        width="60%"
        :visible.sync="showAgreement"
        center>
            <avue-crud  
            ref="crud"
            v-model="form"
            :option="option"
            :data="data"
            :page.sync="page"
            :table-loading="loading"
            @size-change="sizeChange"
            @current-change="currentChange"
            @refresh-change="refreshChange"
            @search-change="searchChange"
            @on-load="onLoad"
            @select="select"
            @select-all="selectAll"
            >
            <template slot="agreementFile" slot-scope="scope">
               <span v-if="scope.row.protocolFile !== undefined && scope.row.protocolFile.length >0 ">
                  <el-button type="text"
                             size="small"
                             @click.stop="viewAgreementDocument(scope.row)">查看附件
                  </el-button>
                </span>
            </template>
            </avue-crud>
            <userProtocolFilePerview :openDialog="openDialog" :protocolFileList="protocolFileList"
                                   @close="closeDialog"></userProtocolFilePerview>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="sure" size="small">确 认</el-button>
                <el-button  @click="showAgreement=false" size="small">取 消</el-button>
            </div>
        </el-dialog>
    </basic-container>
</template>
<script>

import {getList, getDetail} from "@/api/contract/contract/useragreement";
import userProtocolFilePerview from "../userProtocolFilePerview";

export default {
    name:"user-agreement-info",
    components: {userProtocolFilePerview},
    props:['userAgreementInfo'],
    mounted: function(){
        this.userAgreement.itemValues= this.userAgreementInfo;
    },
    data(){
        return{
            showAgreement:false,
            userAgreement:{
                itemValues:[]
            },
            protocolFileList: [],
            selectionList: [],
            temSelectionList:[],
            query:{},
            page: {
                currentPage: 1,
                total: 0,
                pageSizes: [10, 20, 30, 40, 50, 100],
                pageSize: 10
              },
            loading: false,
            openDialog: false,
            option:{
                calcHeight: 210,
                searchMenuSpan:6,
                searchShow: true,
                tip: false,
                border: true,
                index: false,
                viewBtn:false,
                addBtn:false,
                editBtn:false,
                delBtn:true,
                menu:false,
                header:false,
                selection: true,
                column:[
                    {
                      label: "主键id 雪花算法产生",
                      prop: "id",
                      hide: true,
                      display: false, //在查看，新增，编辑页面是否显示
                    },
                    {
                        label:"协议编码",
                        prop:"agreementNo",
                        search:true
                    },
                     {
                        label:"协议名称",
                        prop:"agreementName"
                    },
                     {
                        label:"协议类型",
                        prop:"agreementType",
                        type:"select",
                        dicUrl: this.getDicValue(this.CONSTANT.AGREEMENT_TYPE),
                        props: {
                          label: "dictValue",
                          value: "dictKey"
                        },
                        search:true
                    },
                     {
                        label:"协议附件",
                        prop:"agreementFile",
                        slot:true
                    }
                ]
            },
            data:[]
        }
    },
    methods:{
         back(){
            this.$emit("update",2);
        },
        save(){
          this.$emit("saveUserAgreementInfo",this.userAgreement.itemValues);
        },
        next(){
            this.$emit("update",4);
        },
        sure(){
            this.userAgreement.itemValues=[];
            this.showAgreement=false;
            this.saveSelectList(this.temSelectionList);
            this.selectionList.forEach(item=>{
                this.userAgreement.itemValues.push(item);
            });
        },
        showAssociation() {
          this.showAgreement=true;
          this.temSelectionList = [];
          this.refreshChange();
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
        saveSelectList(selection){
          // 如果当前选项为空，并且表格有选择数据
          if(this.selectionList.length <= 0) {
            this.selectionList = selection;
            return ;
          }
          // 合并处理数组集合
          let swapObj = {};
          this.selectionList.forEach(function(value,index){
            swapObj[value.id] = value;
          });
          selection.forEach(function(value,index){
            swapObj[value.id] = value;
          });
          this.selectionList = [];
          for(let id in swapObj) {
            this.selectionList.push(swapObj[id]);
          }
        },
        selectAll(selection){
          for(let i = 0; i < selection.length; i++) {
            this.select(selection, selection[i]);
          }
        },
        select(selection, row){ // 解决记忆选项
          // 判断是否取消选中
          let hasRemoveFlag = this.checkRemove(selection, row);
          // 取消选中更新临时存储对象
          if(hasRemoveFlag) {
            let idx = -1;
            this.temSelectionList.forEach(function(value,index){
              if(value.id == row.id) {
                idx = index;
              }
            });
            this.temSelectionList = this.temSelectionList.slice(0,idx).concat(this.temSelectionList.splice(idx+1,this.temSelectionList.length));
          } else {
            // 否则 追加进入临时对象， 并且防止重复追加
            let isRepeat = false;
            for(let i = 0; i < this.temSelectionList.length; i++) {
              if(this.temSelectionList[i].id == row.id) {
                isRepeat = true;
                break;
              }
            }
            if(!isRepeat) {
              this.temSelectionList.push(row);
            }
          }
        },
        deleteItem(item,index){
            this.userAgreement.itemValues.splice(index,1);
            this.selectionClear(item,index);
        },
        selectionClear(item,index) {
            this.selectionList.splice(index,1);
        },
        /**表格按钮事件**/
        viewAgreementDocument(row) {
          event.stopPropagation();
          getDetail(row.id).then(res => {
            this.protocolFileList = res.data.data.protocolFile;
            this.openDialog = true;
          })
        },
        closeDialog(val) {
          this.openDialog = val;
        },
        /**检索相关事件**/
        searchChange(params,done) {
          this.query = params;
          this.page.currentPage = 1;
          this.onLoad(this.page, params);
          event.stopPropagation();
          done();
        },
        /**表格分页相关事件**/
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
          });
      }
    }
}
</script>
<style lang="scss">
.el-agreement-form{
    margin-top: 1rem;
    .el-form--inline .el-form-item{
        width: 100%;
        display: flex;
        .el-form-item__content{
            width: calc( 100% - 300px );
            display: flex;
        }
        .el-button{
            margin-left: 10px;
        }
    }
}
</style>

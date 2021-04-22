
<template>
  <el-dialog
  title="查看协议文件"
  :visible.sync="openDialog"
  @close="closeDialog"
  width="70%"
  append-to-body
  :before-close="handleClose">
    <div slot="title">查看协议文件<el-link @click="clickFile" :underline="false" type="primary" style="float:right;margin: 3px 30px 0 0;">下 载</el-link> </div>
    <el-tabs v-model="activeName">
      <el-tab-pane lazy :label="item.fileName" :name="index" v-for="(item,index) in protocolFileListCopy" :key="index">
        <div style="width: 100%;min-height: 600px;">
          <iframe :id="'ifame'+index" style="width: 100%; min-height: 600px;frameborder:0px;border:0px;" :src="getFileUrl(item.fileUrl)"></iframe>
        </div>
      </el-tab-pane>
    </el-tabs>
</el-dialog>
</template>
  <script>
    export default {
      props:{
        openDialog:{
          type:Boolean,
          default:false
        },
        protocolFileList:''
      },
      data() {
        return {
          protocolFileListCopy:[],
          activeName: 0
        };
      },
      methods: {
        handleClick(tab, event) {
          console.log('ifame'+tab.name)
          document.getElementById('ifame'+tab.name).contentWindow.location.reload(true);
        },
        closeDialog(){
          this.$emit("close",false);
        },
        getFileUrl(url){
          console.log("地市",url.replace('/download','/preview'));
           return url.replace('/download','/preview');
        },
        clickFile() {
          window.open(this.protocolFileListCopy[this.activeName].fileUrl);
        }
      },
      mounted() {
      },
      watch:{
        protocolFileList(val){
          this.protocolFileListCopy=val;
        }
      }
    };
  </script>




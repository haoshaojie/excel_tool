<template>
  <el-form ref="picForm" :model="picInfo" label-width="100px" class="demo-form-inline"  size="mini">
    <el-form-item label="产品主图">
      <el-upload
        class="avatar-uploader"
        :disabled="isView"
        :action="putfile_url"
        :headers="headers"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload">
        <img v-if="picInfo.proImage" :src="picInfo.proImage" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </el-form-item>
    <el-form-item label="产品图片">
      <el-upload
        :action="putfile_url"
        :disabled="isView"
        list-type="picture-card"
        :headers="headers"
        :on-success="handlePictureSuccess"
        :on-preview="handlePictureCardPreview"
        :before-upload="beforeAvatarUpload"
        :on-remove="handleRemove"
        :file-list="picInfo.fileLists">
        <i class="el-icon-plus"></i>
      </el-upload>
    </el-form-item>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
    <el-form-item label="产品描述">
      <el-input type="textarea" :disabled="isView" v-model="picInfo.itemDesc" rows="5" maxlength='500' show-word-limit='true'></el-input>
    </el-form-item>
  </el-form>
</template>
<script>
import {getToken} from '@/util/auth'
import website from '@/config/website';
import {Base64} from 'js-base64';
import {imagesByProdId} from "@/api/contract/product/productitem";
export default {
  name:"picInfos",
  props:{
    isView:false,
    picInfo:{
      prodDesc:'',
      itemDesc:'',
      fileLists:[]
    }
  },
  data(){
      return {
        dialogVisible:false,
        dialogImageUrl:'',
        productType:1, // 1产品类型；2产品项；3组成产品；
        putfile_url: "/api/cnfic-resource/oss/file/putFile",
        headers:{},
        
      }
  },
  mounted() {
    console.log("挂载",this.picInfo);
  },
  methods:{
    showData(data){
      this.picInfo.itemDesc = data.itemDesc;
      this.picInfo.proImage = data.proImage;
      this.$forceUpdate()
      imagesByProdId(data.id, this.productType).then(res =>{
        let data = res.data.data;
        data.forEach(item =>{
          item.name= item.fileName; 
          item.url= item.imageUrl;
        })
        this.picInfo.fileLists = data;
      })
    },
    handleAvatarSuccess(res, file) { // 产品图片上传成功方法
      console.log(123,file)
      this.picInfo.proImage =  file.response.data.link;
    },
    beforeAvatarUpload(file) { // 产品图片上传校验方法
      this.headers['Authorization'] = `Basic ${Base64.encode(`${website.clientId}:${website.clientSecret}`)}`;
      this.headers['TongTech-Auth'] = 'bearer ' + getToken()
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt10M = file.size / 1024 / 1024 < this.CONSTANT.SINGLE_FILE_SIZE;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG、PNG 格式!');
      }
      if (!isLt10M) {
        this.$message.error('上传头像图片大小不能超过 10MB!');
      }
      return isJPG && isLt10M;
    },
    handlePictureSuccess(response, file, fileList){
      this.picInfo.fileLists = fileList;
      console.log(fileList)
    },
    handleRemove(file, fileList) { // 删除以删除图片
      this.picInfo.fileLists = fileList;
    },
    handlePictureCardPreview(file) { // 显示图片
      console.log(123,file)
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    }
  }
}
</script>
<style lang="scss">
.demo-form-inline{
  color:red;
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 148px;
    height: 148px;
    line-height: 148px;
    text-align: center;
  }
  .avatar {
    width: 148px;
    height: 148px;
    display: block;
  }

}
</style>
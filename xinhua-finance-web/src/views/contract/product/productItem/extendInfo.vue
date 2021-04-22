<template>
  <el-row>
    <el-col span="12">
      <el-form ref="extendForm" :model="extendInfo" label-width="100px" class="demo-form-inline"  size="small">
        <el-button type="primary" size="small" v-if="!isView" @click="openDialog" class="add-btn">添加扩展属性</el-button>
        <el-form-item v-for="(item, i) in extendInfo.itemValues" :key='i' :label="item.propName" :prop="'itemValues.'+i+'.propValue'"
          :rules="item.isRequired==1?[{ required: true, message:`请输入${item.propName}`, trigger: 'blur' }]:''">
          <el-row v-if="item.showType == 1" style="display: flex;">
            <el-input v-model="item.propValue" :disabled="isView" placeholder="请输入" size="small"></el-input>
            <el-button icon="el-icon-remove-outline" type="text" v-if="!isView" @click="extendInfoRm(i)" style="margin-left: 10px; color: #E02020;">删除</el-button>
          </el-row>
          <el-row v-if="item.showType == 2" style="display: flex;">
            <el-select style="width:100%" :disabled="isView" v-model="item.propValue" placeholder="请选择" size="small">
              <el-option v-for="(im,t) in item.options" :key="t" :label="im.propValue" :value="im.propValue"></el-option>
            </el-select>
            <el-button icon="el-icon-remove-outline" type="text" v-if="!isView" @click="extendInfoRm(i)" style="margin-left: 10px; color: #E02020;">删除</el-button>
          </el-row>
        </el-form-item>
        <extend-pro-dialog 
              dialogTitle="添加扩展属性" 
              :dialogForm="dialogForm" 
              :proDialog="openProDialog" 
              @result="extendInfoAdd" 
              @cancel="cancelDialog">
        </extend-pro-dialog>
      </el-form>
    </el-col>
  </el-row>
</template>
<script>
import extendProDialog from "./extendProDialog";
import {itemValueByProdId} from "@/api/contract/product/productitem";
import {getListByCate} from "@/api/contract/product/property";
import bus from '@/assets/js/event-bus';
export default {
  components:{extendProDialog},
  name:"extendInfo",
  props:{
    isView:false,
    extendInfo:{
    }
  },
  data(){
      return {
        dialogForm:{
          propType:2,
          exclude:'',
        },
        cateId:'',
        excludeIds:[],
        openProDialog:false,
      }
  },
  mounted(){
    bus.$on('cateIdBus',data =>{
      this.cateId = data;
      this.initExtendInfo();
    })
  },
  methods:{
    showData(data){
      this.cateId = data.cateId?data.cateId:'';
      itemValueByProdId(data.id).then(res =>{
        let data = res.data.data;
        console.log("extend",data)
        this.extendInfo.itemValues.push(...data);
        data.forEach(item => {
          this.excludeIds.push(item.propId);
        });
        this.dialogForm.exclude = this.excludeIds.join(',');
        })
    },
    initExtendInfo(){
      this.extendInfo.itemValues=[];
      // 查询 当前类型先的属性已经属性待选值
      getListByCate({propType:this.dialogForm.propType,cateId:this.cateId}).then(res => {
        const data = res.data.data;
        this.extendInfoAdd(data)
      });
    },
    cancelDialog(){
      this.openProDialog = false
    },
    openDialog(){
      if(this.cateId ==''){
        this.$message.warning('请先选择产品类型');
      }else{
        this.openProDialog = true
      }
    },
    extendInfoAdd(data) {
      if(data){
        data.forEach(item => {
          this.excludeIds.push(item.id);
          item.propId = item.id;
        });
        this.extendInfo.itemValues.push(...data);
        console.log(123,this.extendInfo.itemValues)
        this.dialogForm.exclude = this.excludeIds.join(',');
      }
      this.openProDialog = false;
    },
    extendInfoRm(index){
      this.extendInfo.itemValues.splice(index, 1);
      this.excludeIds.splice(index, 1);
      this.dialogForm.exclude = this.excludeIds.join(',');
    }
  }
}
</script>
<style lang="scss" scoped>
.add-btn{
  margin-bottom: 1rem;
}
</style>
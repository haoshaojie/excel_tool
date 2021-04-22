<!--导入的弹窗-->
<template>
  <el-dialog
    title="批量导入"
    :visible.sync="uploadDialogFlag"
    @close="closeDialog"
  >
    <basic-container>
      <el-steps :active="activeStep" align-center>
        <el-step title="上传文件"></el-step>
        <el-step title="数据校验"></el-step>
        <el-step title="数据入库"></el-step>
      </el-steps>
      <template>
        <div class="el-import-step" v-if="activeStep==1">
          <div class="el-import-step-download">
            <div class="el-import-step-download-icon">
              <i class="el-icon-download"></i>
            </div>
            <div class="el-import-step-download-desc">
              <div>导入模板文件下载</div>
              <div class="download-tip">请按照数据模板的格式准备导入数据，模板中的表头名称不可更改，表头行不能删除</div>
              <div>
                <el-link type="primary" :underline="false" @click="exportTemplate">下载模板</el-link>
              </div>
            </div>
          </div>
          <div class="el-import-step-download">
            <div class="el-import-step-download-icon">
              <i class="el-icon-upload"></i>
            </div>
            <div class="el-import-step-download-desc">
              <div>上传填好的导入模板</div>
              <div class="download-tip">文件后缀名必须为xls或xlsx（即Excel格式），文件大小不得大于2M</div>
              <el-upload
                multiple
                accept=".xls, .xlsx"
                :headers="headerObj"
                action="/api/cnfic-contract-manage/orgcustomer/importCustomerFile"
                :on-success="handleChange"
                :before-upload="checkFileSize"
                :file-list="fileList">
                <el-link type="primary" :underline="false">上传文件</el-link>
              </el-upload>
            </div>
          </div>
          <div class="el-import-step-footer">
            <el-button size="small" type="primary" @click="next(2)">&nbsp;&nbsp;&nbsp;下一步&nbsp;&nbsp;&nbsp;</el-button>
            <el-button size="small" @click="closeDialog">&nbsp;&nbsp;&nbsp;取&nbsp;&nbsp;&nbsp;消&nbsp;&nbsp;&nbsp;
            </el-button>
          </div>
        </div>
        <div class="el-import-step" v-if="activeStep==2">
          <avue-crud
            :option="optionSuccess"
            :table-loading="loading"
            :page.sync="passPage"
            :data="dataSuccess"
            @on-load="onLoadSuccess">
            <template slot="header">
              <div class="el-import-list-header">
                校验成功数据列表
                <el-link type="primary" :underline="false" size="small">{{passPage.total}}条</el-link>
              </div>
            </template>
          </avue-crud>
          <!-- <div class="el-import-list-pagination">
            <el-pagination
              background
              @size-change="handlePassSizeChange"
              @current-change="handlePassCurrentChange"
              :current-page="passPage.currentPage"
              :page-size="passPage.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="passPage.total">
            </el-pagination>
          </div> -->
          <avue-crud
          :option="optionFail"
          :table-loading="loading"
          :page.sync="failPage"
          :data="dataFail"
          @on-load="onLoadFail">
          <template slot="header">
          <div class="el-import-list-header">
          校验失败数据列表
          <el-link type="primary" :underline="false" size="small">{{failPage.total}}条</el-link>
          <el-link type="primary" :underline="false" size="small" @click="exportCheckfaildata">下载失败数据列表</el-link>
          </div>
          </template>
          </avue-crud>
          <!-- <div class="el-import-list-pagination">
          <el-pagination
          background
          @size-change="handleFailSizeChange"
          @current-change="handleFailCurrentChange"
          :current-page="failPage.currentPage"
          :page-size="failPage.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="failPage.total">
          </el-pagination>
          </div> -->
          <div class="el-import-step-footer">
            <el-button size="small" type="primary" @click="next(1)">返回重新上传</el-button>
            <el-button size="small" type="primary" @click="next(3)">忽略异常数据导入</el-button>
            <el-button size="small" @click="closeDialog">&nbsp;&nbsp;&nbsp;取&nbsp;&nbsp;&nbsp;消&nbsp;&nbsp;&nbsp;
            </el-button>
          </div>
        </div>
        <div class="el-import-step" v-if="activeStep==3">
          <div class="el-import-step-three">
            <div class="el-import-step-download-icon"><i class="el-icon-success"></i></div>
            <h3>数据导入成功</h3>
            <div class="download-tip">导入数据{{submitPassNum}}条</div>
          </div>
          <div class="el-import-step-footer">
            <el-button size="small" type="primary" @click="closeDialog">&nbsp;&nbsp;&nbsp;完&nbsp;&nbsp;&nbsp;成&nbsp;&nbsp;&nbsp;</el-button>
          </div>
        </div>
      </template>
    </basic-container>
  </el-dialog>
</template>
<script>
  import {checkpassdata, submitpassdata} from "@/api/contract/customer/orgcustomer";
  import website from "@/config/website";
  import {getToken} from '@/util/auth';

  export default {
    name: "customer-upload-dialog",
    props: {
      uploadDialogFlag: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        loading: false,
        activeStep: 1,
        fileList: [],
        headerObj: {
          "Authorization": `Basic ${Base64.encode(`${website.clientId}:${website.clientSecret}`)}`,
          "TongTech-Auth": 'Bearer ' + getToken(),
          "Blade-Auth": 'Bearer ' + getToken()
        },//附件上传请求头参数
        passPage: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        failPage: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        dataSuccess: [{
          custName: '张三',
          custTypeName: '利率债_金融债_IBABS',
          custStateName: 'XHCJ001',
          businessChanceName: '中央国债登记结算有限责任公司',
          businessDesc: '行情数据',
          repeatTip: '提示信息',
        }],
        dataFail: [{
          custName: '张三',
          custTypeName: '利率债_金融债_IBABS',
          custStateName: 'XHCJ001',
          businessChanceName: '中央国债登记结算有限责任公司',
          businessDesc: '行情数据',
          msg:"",
        }],
        optionSuccess: {
          calcHeight: 100,
          searchShow: true,
          searchMenuSpan: 6,
          border: true,
          index: false,
          viewBtn: false,
          addBtn: false,
          refreshBtn: false,
          columnBtn: false,
          menu: false,
          column: [{
            label: '客户名称',
            prop: 'custName'
          },
          {
            label: '客户类型',
            prop: 'custTypeName'
          },
          {
            label: '客户状态',
            prop: 'custStateName'
          }, {
            label: '业务机会状态',
            prop: 'businessChanceName'
          },
          {
            label: '业务描述',
            prop: 'businessDesc'
          },{
            label: '提示信息',
            prop: 'repeatTip'
          }]
        },
        optionFail: {
          calcHeight: 210,
          searchShow: true,
          searchMenuSpan: 6,
          border: true,
          index: false,
          viewBtn: false,
          addBtn: false,
          refreshBtn: false,
          columnBtn: false,
          menu: false,
          column: [{
            label: '客户名称',
            prop: 'custName'
          },
            {
              label: '客户类型',
              prop: 'custTypeName'
            },
            {
              label: '客户状态',
              prop: 'custStateName'
            }, {
              label: '业务机会状态',
              prop: 'businessChanceName'
            },
            {
              label: '业务描述',
              prop: 'businessDesc'
            },
            {
              label: '错误提示',
              prop: 'msg'
            }
          ]
        },
        redisKey: "",
        submitPassNum: 0,
        fileMaxSize: 2 * 1024 * 1024,
      }
    },
    mounted() {
    },
    methods: {
      checkFileSize(file) {
        if (file && file.size > this.fileMaxSize) {
          this.$message.error('文件后缀名必须为xls或xlsx（即Excel格式），文件大小不得大于2M');
          this.fileList = [];
          return false;
        }
        return true;
      },
      exportCheckfaildata() {
        // window.open(`/api/cnfic-contract-manage/blackwhitelist/export-checkfaildata?${website.tokenHeader}=${getToken()}&failRedisKey=${this.failRedisKey}`);
        window.open(`/api/cnfic-contract-manage/orgcustomer/downloadCustomerTemplate?${website.tokenHeader}=${getToken()}&key=${this.redisKey}`);

      },
      exportTemplate() {
        window.open(`/api/cnfic-contract-manage/orgcustomer/downloadCustomerTemplate?${website.tokenHeader}=${getToken()}`);

      },
      closeDialog() {
        this.next(1);
        this.$emit("cancelUploadDialog");
      },
      handleChange(response, file, fileList) {
        if (response) {
          this.redisKey = response.data;
        }
      },
      onLoadSuccess() {
        this.loading = true;
        const filter = {
          "redisKey": this.redisKey,
          "isSuccess": true,
        };
        checkpassdata(this.passPage.currentPage, this.passPage.pageSize, filter).then(res => {
          const data = res.data.data;
          this.passPage.total = data.total;
          this.dataSuccess = data.records;
          console.log(this.dataSuccess)
          this.loading = false;
        });
      },
      handlePassSizeChange(size) {
        this.passPage.pageSize = size;
        this.onLoadSuccess();
      },
      handlePassCurrentChange(page) {
        this.passPage.currentPage = page;
        this.onLoadSuccess();
      },
      onLoadFail() {
        this.loading = true;
        const filter = {
          "redisKey": this.redisKey,
          "isSuccess": false,
        };
        checkpassdata(this.failPage.currentPage, this.failPage.pageSize, filter).then(res => {
          const data = res.data.data;
          this.failPage.total = data.total;
          this.dataFail = data.records;
          this.loading = false;
        });
      },
      handleFailSizeChange(size) {
        this.failPage.pageSize = size;
        this.onLoadFail();
      },
      handleFailCurrentChange(page) {
        this.failPage.currentPage = page;
        this.onLoadFail();
      },
      submitpassdata() {
        this.loading = true;
        const filter = {"redisKey": this.redisKey};
        submitpassdata(filter).then(res => {
          this.submitPassNum = res.data.data;
        }, error => {
          window.console.log(error);
        });
      },
      next(step) {
        this.activeStep = step;
        if (step == 3 && this.passPage.total != 0) {
          this.submitpassdata();
        }
      }
    }
  }
</script>
<style lang="scss">
  .el-import-step {
    margin-top: 1rem;
    width: 100%;
    &-download:first-child {
      border-bottom: 1px solid #d9d9db;
    }
    &-download {
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 1rem 0;
      &-icon {
        i {
          color: #078ecf;
          font-size: 40px;
          padding: 0 30px;
        }
      }
      &-desc {
        width: 70%;
        div {
          padding: 5px 0;
        }
        div:first-child {
          font-size: 14px;
        }
        .el-link.el-link--primary {
          color: #078ecf;
        }
        .download-tip {
          font-size: 12px;
          color: #9da7b0;
        }
      }
    }
    &-footer {
      text-align: center;
      width: 100%;
      margin-top: 1rem;
      .el-button--small {
        margin: 0 1rem;
      }
    }
    &-three {
      text-align: center;
      h3, div {
        width: 100%;
      }
    }
    .avue-crud__menu {
      min-height: 30px;
    }
    .avue-crud__pagination {
      display: none;
    }
    .el-import-list-header {
      height: 40px;
      line-height: 40px;
      font-size: 14px;
      color: #9da7b0;
      .el-link {
        vertical-align: initial;
      }
      a {
        color: #078ecf;
        margin: 0 10px;
      }
    }
    .el-import-list-pagination {
      text-align: center;
      width: 100%;
      margin-top: 1rem;
    }
  }
</style>

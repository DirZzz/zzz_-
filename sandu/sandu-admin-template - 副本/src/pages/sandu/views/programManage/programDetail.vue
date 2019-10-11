<template>
  <div class="program-detail">
    <div class="main-body">
      <h1>
        <span @click="toList">小程序管理后台</span>
        <i class="el-icon-arrow-right"></i>
        <span>{{title}}</span>
      </h1>
      <div class="no-info-box" v-if="edit != 'info'">
        <div class="base-info-box">
          <div class="title">基础信息</div>
          <div class="base-info">
            <el-form
              :model="baseInfo"
              :inline="true"
              :rules="rules"
              ref="ruleForm"
              label-width="150px"
              label-position="left"
              class="demo-ruleForm"
            >
              <el-form-item label="小程序名称：" prop="name">
                <el-input v-model.trim="baseInfo.name" :disabled="disabled" maxlength="20"></el-input>
              </el-form-item>
              <el-form-item label="所属企业：" prop="companyId">
                <el-select
                  v-model.trim="baseInfo.companyId"
                  :disabled="disabled"
                  clearable
                  filterable
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in companyOptions"
                    :key="item.type"
                    :label="item.name"
                    :value="item.type"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="小程序类型：" prop="type">
                <el-select
                  v-model.trim="baseInfo.type"
                  clearable
                  :disabled="typeDisabled"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in miniTypeOptions"
                    :key="item.type"
                    :label="item.name"
                    :value="item.type"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="APPID：" prop="appId">
                <el-input
                  v-model.trim="baseInfo.appId"
                  autocomplete="off"
                  :disabled="disabled"
                  maxlength="50"
                ></el-input>
              </el-form-item>
              <el-form-item label="appSecret：" prop="appSecret">
                <el-input
                  type="text"
                  :disabled="disabled"
                  autocomplete="off"
                  v-model.trim="baseInfo.appSecret"
                  maxlength="50"
                ></el-input>
              </el-form-item>
              <el-form-item label="商户号：">
                <el-input v-model.trim="baseInfo.mchId" :disabled="disabled" maxlength="50"></el-input>
              </el-form-item>
              <el-form-item label="密钥：">
                <el-input v-model.trim="baseInfo.secret" :disabled="disabled" maxlength="50"></el-input>
              </el-form-item>
              <el-form-item label="商户证书：">
                <div class="upload-box">
                  <span v-if="baseInfo.mchCredit" style="margin-right:10px;">已上传</span>
                  <span
                    class="seeMchCredit"
                    v-if="baseInfo.mchCredit && edit != 'info'"
                    @click="deletedMchCredit"
                  >删除</span>
                  <el-upload
                    :action="uploadUrl"
                    :headers="headerToken"
                    :show-file-list="false"
                    :disabled="uploadDisabled"
                    :on-success="handleAvatarSuccess"
                  >
                    <el-button
                      type="primary"
                      :disabled="baseInfo.mchCredit?uploadDisabled:false"
                      v-if=" edit != 'info'"
                    >上传</el-button>
                  </el-upload>
                </div>
              </el-form-item>
            </el-form>
            <div class="tip">
              <i class="el-icon-info" color="red">&nbsp;</i>提示：&nbsp;
              <span>*</span>&nbsp;为必填项
            </div>
          </div>
        </div>
        <div class="template-box">
          <div class="title">模板信息</div>
          <div class="template-content">
            <div class="add-template">
              <el-button type="primary" @click="addMessage" :disabled="disabled">添加模板信息</el-button>
            </div>
             <div class="template-item-box" v-for="(messagesItem,index) in baseInfo.messages" :key="index">
              <div class="template-item">
                  <el-select
                    v-model="messagesItem.templateId"
                    :disabled="disabled"
                    placeholder="请选择消息类型"
                  >
                    <el-option
                      v-for="item in templateOptions"
                      :key="item.type"
                      :label="item.name"
                      :value="item.type"
                    ></el-option>
                  </el-select>
                  <el-input
                    v-model.trim="messagesItem.msgId"
                    :disabled="disabled"
                    placeholder="请输入消息ID"
                    class="msgId"
                  ></el-input>
                  <span
                    class="deteled-template"
                    @click="deletedMessage(index)"
                    v-if="edit != 'info'"
                  >删除</span>
                </div>
            </div>
            <div class="clear"></div>
          </div>
          <div class="option-btn">
            <el-button type="primary" @click="cancelAdd" v-if="edit != 'info'">取消</el-button>
            <el-button type="primary" @click="confirmMsg">确定</el-button>
          </div>
        </div>
      </div>
      <div class="info-box" v-else>
        <div class="base-info-box">
          <div class="title">基础信息</div>
          <div class="base-info">
            <el-row>
              <el-col :span="12">
                <div class="base-info-item">
                  <div class="info-name mustFill">小程序名称：</div>
                  <div class="info-detail">{{baseInfo.name}}</div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="base-info-item">
                  <div class="info-name mustFill">所属企业：</div>
                  <div v-for="item in companyOptions" :key="item.type">
                    <div class="info-detail" v-if="item.type ==baseInfo.companyId">{{item.name}}</div>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="base-info-item">
                  <div class="info-name mustFill">小程序类型：</div>
                  <div v-for="item in miniTypeOptions" :key="item.type">
                    <div class="info-detail" v-if="item.type ==baseInfo.type">{{item.name}}</div>
                  </div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="base-info-item">
                  <div class="info-name mustFill">APPID：</div>
                  <div class="info-detail">{{baseInfo.appId}}</div>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="base-info-item">
                  <div class="info-name mustFill">APPSecret：</div>
                  <div class="info-detail">{{baseInfo.appSecret}}</div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="base-info-item">
                  <div class="info-name">商户号：</div>
                  <div class="info-detail" v-if="baseInfo.mchId">{{baseInfo.mchId}}</div>
                  <div class="info-detai" v-else>暂无信息</div>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="base-info-item">
                  <div class="info-name">密钥：</div>
                  <div class="info-detail" v-if="baseInfo.secret">{{baseInfo.secret}}</div>
                  <div class="info-detai" v-else>暂无信息</div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="base-info-item">
                  <div class="info-name">商户证书：</div>
                  <div class="upload-box" v-if="baseInfo.mchCredit">
                    <span v-if="baseInfo.mchCredit" style="margin-right:10px;">已上传</span>
                    <span
                      class="seeMchCredit"
                      v-if="baseInfo.mchCredit && edit != 'info'"
                      @click="deletedMchCredit"
                    >删除</span>
                    <el-upload
                      :action="uploadUrl"
                      :headers="headerToken"
                      :show-file-list="false"
                      :disabled="uploadDisabled"
                      :on-success="handleAvatarSuccess"
                      v-if="edit != 'info'"
                    >
                      <el-button
                        type="primary"
                        :disabled="baseInfo.mchCredit?uploadDisabled:false"
                      >上传</el-button>
                    </el-upload>
                  </div>
                  <div class="info-detail" v-else>未上传</div>
                </div>
              </el-col>
            </el-row>
            <div class="tip">
              <i class="el-icon-info" color="red">&nbsp;</i>提示：&nbsp;
              <span>*</span>&nbsp;为必填项
            </div>
          </div>
        </div>
        <div class="template-box">
          <div class="title">模板信息</div>
          <div class="template-content">
            <div class="add-template">
              <el-button type="primary" @click="addMessage" :disabled="disabled">添加模板信息</el-button>
            </div>
            <div class="template-item-box" v-for="(messagesItem,index) in baseInfo.messages" :key="index">
              <div class="template-item">
                  <el-select
                    v-model="messagesItem.templateId"
                    :disabled="disabled"
                    placeholder="请选择消息类型"
                  >
                    <el-option
                      v-for="item in templateOptions"
                      :key="item.type"
                      :label="item.name"
                      :value="item.type"
                    ></el-option>
                  </el-select>
                  <el-input
                    v-model.trim="messagesItem.msgId"
                    :disabled="disabled"
                    placeholder="请输入消息ID"
                    class="msgId"
                  ></el-input>
                  <span
                    class="deteled-template"
                    @click="deletedMessage(index)"
                    v-if="edit != 'info'"
                  >删除</span>
                </div>
            </div>
              <div class="clear"></div>
          </div>
          <div class="option-btn">
            <el-button type="primary" @click="cancelAdd" v-if="edit != 'info'">取消</el-button>
            <el-button type="primary" @click="confirmMsg">确定</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import qs from "qs";
export default {
  data() {
    return {
      uploadUrl: "",
      disabled: false,
      uploadDisabled: false,
      typeDisabled: false,
      sourceBaseUrl: "",
      edit: -1,
      title: "",
      headerToken: {
        Authorization: qs.parse(sessionStorage.getItem("loginUser")).token
      },
      baseInfo: {
        name: "",
        companyId: "",
        type: "",
        mchId: "",
        appId: "",
        appSecret: "",
        secret: "",
        mchCredit: "",
        messages: []
      },
      fileList: [],
      templateOptions: [],
      companyOptions: [],
      miniTypeOptions: [],
      saveFlag: true,
      msgFlag: false,
      rules: {
        name: [
          { required: true, message: "请输入小程序名称", trigger: "blur" }
        ],
        companyId: [
          { required: true, message: "请选择所属企业", trigger: "blur" }
        ],
        type: [
          { required: true, message: "请选择小程序类型", trigger: "blur" }
        ],
        appId: [{ required: true, message: "请输入appId", trigger: "blur" }],
        appSecret: [
          { required: true, message: "请输入appSecret", trigger: "blur" }
        ]
      }
    };
  },
  components: {},
  created() {
    this.getMiniList();
    this.getTempList();
    this.getCompList();
    // 判断还是编辑还是详情
    if (this.$route.query.id) {
      this.id = this.$route.query.id;
      this.infoInit(this.id);
    }
    this.edit = this.$route.query.edit;
    switch (this.edit) {
      case "info":
        this.disabled = true;
        this.title = "详情";
        break;
      case "edit":
        this.title = "编辑";
        break;

      case "add":
        this.title = "新增";
        break;
    }
    let basePath = process.env;
    (this.sourceBaseUrl = basePath.sourceBaseUrl),
      (this.uploadUrl = `${basePath.systemUrl}/v1/mini/back/fileUpload`);
  },
  methods: {
    // 初始化信息
    infoInit(id) {
      this.API.getProgramInfo({ id: id }).then(res => {
        if (res.success) {
          this.baseInfo = res.obj;
          this.typeDisabled = true;
          if (this.baseInfo.mchCredit) {
            this.uploadDisabled = true;
          }
        }
      });
    },
    // 消息类型列表
    getTempList() {
      this.API.getTempList().then(res => {
        if (res.success) {
          this.templateOptions = res.obj;
        }
      });
    },
    // 企业列表
    getCompList() {
      this.API.getCompList().then(res => {
        if (res.success) {
          this.companyOptions = res.obj;
        }
      });
    },
    // 获取小程序类型列表
    getMiniList() {
      this.API.getMiniBackList().then(res => {
        if (res.success) {
          this.miniTypeOptions = res.obj;
        }
      });
    },
    // 添加模板信息
    addMessage() {
      let emptyArr = {
        templateId: "",
        msgId: ""
      };
      this.baseInfo.messages.push(emptyArr);
    },
    // 删除模板信息
    deletedMessage(index) {
      this.baseInfo.messages.splice(index, 1);
    },
    // 上传商户证书成功后
    handleAvatarSuccess(e) {
      if (e.success) {
        // this.baseInfo.mchCredit = e.obj.path;
        this.$set(this.baseInfo, "mchCredit", e.obj.path);
        this.uploadDisabled = true;
      }
    },
    // 确定
    confirmMsg() {
      // 校验信息是否填写完整
      if (this.baseInfo.messages.length != 0) {
        for(let i=0;i<this.baseInfo.messages.length;i++){
          if(!this.baseInfo.messages[i].templateId || !this.baseInfo.messages[i].msgId){
               this.$message({
              message: "请填写完整模板信息",
              type: "warning"
            });
            this.msgFlag = true;
            this.saveFlag = false;
            break;
          } else {
            this.msgFlag = false;
            this.saveFlag = true;
          }
        }
      }
      if (this.edit != "info") {
        this.$refs["ruleForm"].validate(valid => {
          if (valid) {
            if (this.saveFlag) {
              // 确认
              let params = {
                name: this.baseInfo.name,
                companyId: this.baseInfo.companyId,
                type: this.baseInfo.type,
                appId: this.baseInfo.appId,
                appSecret: this.baseInfo.appSecret
              };
              if (this.baseInfo.secret != "") {
                params.secret = this.baseInfo.secret;
              }
              if (this.baseInfo.mchId != "") {
                params.mchId = this.baseInfo.mchId;
              }
              if (this.baseInfo.mchCredit != "") {
                params.mchCredit = this.baseInfo.mchCredit;
              }
              if (this.baseInfo.messages.length != 0) {
                params.messages = this.baseInfo.messages;
              }
              if (this.edit == "edit") {
                params.id = this.id;
                this.miniUpdate(params); // 编辑更新
              }
              if (this.edit == "add") {
                this.addMini(params); // 新增
              }
            }
          } else {
            this.$message({
              message: "请填写完整必填项",
              type: "warning"
            });
            return false;
          }
        });
      } else {
        // 详情
        this.$router.push({ path: "/programManage/programManage" });
      }
    },
    // 编辑小程序信息
    miniUpdate(params) {
      this.API.programUpdate(params).then(res => {
        if (res.success) {
          this.$message({
            message: "编辑成功",
            type: "success"
          });
          this.$router.push({ path: "/programManage/programManage" });
        } else {
          this.$message({
            message: res.message,
            type: "error"
          });
        }
      });
    },
    // 新增小程序
    addMini(params) {
      this.API.addProgram(params).then(res => {
        if (res.success) {
          this.$message({
            message: "新增成功",
            type: "success"
          });
          this.$router.push({ path: "/programManage/programManage" });
        } else {
          this.$message({
            message: res.message,
            type: "error"
          });
        }
      });
    },
    cancelAdd() {
      this.$router.go(-1);
    },
    deletedMchCredit() {
      this.baseInfo.mchCredit = "";
      this.uploadDisabled = false;
    },
    toList() {
      this.$router.push({ path: "/programManage/programManage" });
    }
  }
};
</script>
<style lang='scss'>
.program-detail {
  .main-body {
    margin: 20px;
    background: #fff;
    min-height: 860px;

    h1 {
      border-bottom: 1px solid #eee;
      text-align: left;
      font-weight: bold;
      padding: 20px 20px 15px 20px;
      color: #333;
      span {
        color: #333;
        font-weight: bold;
        cursor: pointer;
      }
    }
    .title {
      font-weight: bold;
      font-size: 16px;
      height: 16px;
      line-height: 18px;
      text-align: left;
      padding-left: 15px;
      margin-bottom: 20px;
      border-left: 4px solid #3399ff;
    }
    .base-info-box {
      padding: 30px 20px;
      .base-info {
        width: 80%;
        margin: 0 auto;
        .base-info-item {
          display: flex;
          align-items: center;
          margin-bottom: 50px;
          justify-content: start;
          position: relative;
          .info-name {
            width: 200px;
            text-align: left;
            padding-left: 12px;
            position: relative;
          }

          .upload-box div:first-child {
            display: flex;
            flex-direction: row-reverse;
          }
        }
      }
    }
    .mustFill:before {
      position: absolute;
      content: "*";
      top: 4px;
      left: 0;
      width: 24px;
      height: 24px;
      color: red;
    }
    .template-box {
      padding: 30px 20px;
      .option-btn {
        width: 90%;
        margin: 30px 0;
        .el-button {
          margin-right: 30px;
        }
      }
      .template-content {
        width: 80%;
        margin: 0 auto;
        .template-item {
          display: flex;
          justify-content: start;
          align-items: center;
          margin-bottom: 30px;
          .deteled-template {
            margin-left: 5px;
            color: #208dfb;
            cursor: pointer;
          }
          .el-input,
          .el-select {
            width: 170px;
            margin-right: 10px;
          }
        }
      }

      .add-template {
        text-align: left;
        margin-bottom: 30px;
      }
    }
    .tip {
      font-size: 14px;
      color: #999;
      text-align: left;
      margin-left: 15px;
      display: flex;
      align-items: center;
      span {
        font-size: 20px;
        color: red;
        line-height: 23px;
        height: 14px;
      }
    }
    .upload-box {
      display: flex;
      align-items: center;
      cursor: pointer;
      font-size: 14px;
      .seeMchCredit {
        color: #208dfb;
        margin-right: 10px;
      }
    }
    .el-icon-info {
      color: #f7c05a;
    }
    .el-input.is-disabled .el-input__inner {
      color: #676869 !important;
    }
  }
  .info-detail {
    color: #333;
    font-size: 16px;
    text-align: left;
    width: 250px;
    line-height: 22px;
  }
  .el-row {
    display: flex !important;
    align-items: center;
  }
  .el-form--inline .el-form-item {
    display: inline-block;
    margin-right: 10px;
    vertical-align: top;
    width: 48%;
    text-align: left;
    margin-bottom: 50px;
  }
  .el-input,
  .el-select {
    width: 200px;
  }
  .msgId {
    position: relative;
  }
  .msg-tip {
    position: absolute;
    bottom: 12px;
    left: 180px;
    font-size: 12px;
    color: #f56c6c;
  }
  .no-msg {
    .el-input__inner {
      border-color: #f56c6c;
    }
  }
  .template-item-box{
    width: 50%;
    display: inline-block;
    float: left;
  }
  .clear{
    clear: both;
  }
}
</style>
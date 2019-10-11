<template>
  <div>
    <div class="mask" @click.self="togglePopups">
      <div class="formRows">
        <p class="titles">
          {{$store.state.content.isAdd?"新增":"编辑"}}
          <i class="el-icon-close" @click="togglePopups"></i>
        </p>
        <el-form
          :model="data"
          label-width="106px"
          style="text-align:left"
          :rules="rules"
          :ref="ruleForm"
        >
          <template v-if="$store.state.content.type == 'banner'">
            <el-form-item label="banner名称" prop="name" key="1">
              <el-input v-model="data.name"></el-input>
            </el-form-item>
            <el-form-item label="跳转链接" prop="url">
              <el-input v-model="data.url"></el-input>
            </el-form-item>
            <el-form-item label="banner图片" prop="picId" key="3">
              <el-upload
                class="avatar-uploader"
                :action="uploadBannerImgUrl"
                :headers="headerToken"
                :show-file-list="false"
                v-loading="loading"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="data.imgUrl" :src="data.imgUrl" class="avatar">
                <img
                  v-else-if="data.bannerPicPath"
                  :src="BASE_URL.sourceBaseUrl+data.bannerPicPath"
                  class="avatar"
                >
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="排序" prop="ordering">
              <el-input
                v-model="data.ordering"
                style="width:200px"
                type="number"
                oninput="this.value=this.value.replace(/\D/g,'')"
              ></el-input>
            </el-form-item>
          </template>
          <template v-else-if="$store.state.content.type == 'indexPopups'">
            <el-form-item label="弹窗名称" prop="dialogName" key="11">
              <el-input v-model="data.dialogName"></el-input>
            </el-form-item>
            <el-form-item label="跳转链接" prop="url">
              <el-input v-model="data.url"></el-input>
            </el-form-item>
            <el-form-item label="弹窗图片" prop="picId" key="33">
              <el-upload
                class="avatar-uploader"
                :action="uploadBannerImgUrl"
                :headers="headerToken"
                :show-file-list="false"
                v-loading="loading"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="data.imgUrl" :src="data.imgUrl" class="avatar">
                <img
                  v-else-if="data.picPath"
                  :src="BASE_URL.sourceBaseUrl+data.picPath"
                  class="avatar"
                >
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="平台" prop="platform">
              <el-select v-model="data.platform" multiple placeholder="请选择平台">
                <el-option
                  v-for="item in platforms"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </template>
          <template v-else-if="$store.state.content.type == 'sharingActivities'">
            <el-form-item label="对应banner" prop="bannerName" key="111">
              <el-select v-model="data.bannerName" placeholder="请选择">
                <el-option
                  v-for="item in bannerList"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="活动分享文案" prop="shareText" key="222">
              <el-input v-model="data.shareText"></el-input>
            </el-form-item>
            <el-form-item label="活动分享图片" prop="picId" key="333">
              <el-upload
                class="avatar-uploader"
                :action="uploadBannerImgUrl"
                :headers="headerToken"
                :show-file-list="false"
                v-loading="loading"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="data.imgUrl" :src="data.imgUrl" class="avatar">
                <img
                  v-else-if="data.picPath"
                  :src="BASE_URL.sourceBaseUrl+data.picPath"
                  class="avatar"
                >
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </template>
          <el-form-item>
            <el-button type="primary" @click="submit">确定</el-button>
            <el-button @click="togglePopups">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    let stringEmpty = (rule, value, callback) => {
      if (value == undefined || value.trim() === "") {
        callback(new Error("不能输入空值！"));
      } else {
        callback();
      }
    };
    let no0 = (rule, value, callback) => {
      if (value == 0) {
        callback(new Error("请输入正整数"));
      } else {
        callback();
      }
    };
    let maxLength4 = (rule, value, callback) => {
      if (typeof value == "number" ? value > 9999 : value.length > 4) {
        callback(new Error("不能输入超过四位数的数字"));
      } else {
        callback();
      }
    };
    return {
      platforms: [
        { value: 10, label: "随选网" },
        { value: 20, label: "报价小程序" }
      ],
      data: { ordering: undefined, platform: [] },
      ruleForm: "ruleForm",
      uploadBannerImgUrl:
        this.BASE_URL.systemUrl + "/v1/sxw/banner/uploadBannerImg",
      headerToken: {
        Authorization: sessionStorage.getItem("token")
      },
      loading: false,
      rules: {
        name: [
          { required: true, message: "请输入banner名称", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        url: [
          { required: true, message: "请输入跳转链接", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        dialogName: [
          { required: true, message: "请输入弹窗名称", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        bannerName: [
          { required: true, message: "请选择对应banner", trigger: "change" }
        ],
        shareText: [
          { required: true, message: "请输入活动分享的文案", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        picId: [{ required: true, message: "请上传图片", trigger: "change" }],
        ordering: [
          { required: true, message: "请输入正整数", trigger: "blur" },
          { validator: no0, trigger: "blur" },
          { validator: maxLength4, trigger: "blur" }
        ],
        platform: [{ required: true, message: "请选择平台", trigger: "blur" }]
      },
      bannerList: []
    };
  },
  created() {
    this.data = this.$store.state.content.popupData;
    if (this.data.showInPlatform)
      this.$set(
        this.data,
        "platform",
        this.data.showInPlatform.split(",").map(e => Number(e))
      );
    if (this.$store.state.content.isAdd) this.data = {};
    this.$store.state.content.type == "sharingActivities"
      ? this.getBannerList()
      : void 0;
  },
  methods: {
    // 打开/关闭弹窗
    togglePopups() {
      this.$store.commit("content_togglePopups");
      this.$refs[this.ruleForm].clearValidate();
    },
    // 获取banner列表数据
    getBannerList() {
      let fData = new FormData();
      fData.append("positionCode", "xz:home:banner:interior");
      fData.append("page", 1);
      fData.append("limit", 99999);
      this.API.getBannerList(fData).then(res => {
        this.bannerList = res.datalist;
      });
    },
    // 提交
    submit() {
      this.$refs[this.ruleForm].validate(valid => {
        if (!valid) return false;
        let fData = new FormData();
        fData.append("positionCode", "xz:home:banner:interior");
        (() => {
          switch (this.$store.state.content.type) {
            case "banner":
              fData.append("bannerName", this.data.name);
              fData.append("url", this.data.url);
              fData.append("picId", this.data.picId);
              fData.append("bannerId", this.data.id);
              fData.append("ordering", this.data.ordering);
              if (this.$store.state.content.isAdd) {
                sessionStorage.setItem("platformCode", "selectDecoration");
                return this.API.addBanner(fData);
              } else return this.API.updateBanner(fData);
            case "indexPopups":
              fData.append("dialogName", this.data.dialogName);
              fData.append("url", this.data.url);
              fData.append("dialogImageResPicId", this.data.picId);
              fData.append("showInPlatform", this.data.platform.join(","));
              if (this.$store.state.content.isAdd)
                return this.API.addDialog(fData);
              else {
                fData.append("Id", this.data.id);
                return this.API.updateDialog(fData);
              }
              break;
            case "sharingActivities":
              let bData = this.bannerList.filter(item => {
                return item.name === this.data.bannerName;
              });
              fData.append("bannerId", bData[0].id);
              fData.append("bannerName", this.data.bannerName);
              fData.append("shareText", this.data.shareText);
              fData.append("ShareImgResPicId", this.data.picId);
              if (this.$store.state.content.isAdd)
                return this.API.addShare(fData);
              else {
                fData.append("Id", this.data.id);
                return this.API.updateShare(fData);
              }
              break;
          }
        })().then(res => {
          this.$message({
            message: res.message,
            type: res.success ? "success" : "error"
          });
          if (res.success) {
            this.$store.commit("content_triggerGetBannerList");
            this.togglePopups();
          }
        });
      });
    },
    // 上传图片成功
    handleAvatarSuccess(res, file) {
      this.data.imgUrl = URL.createObjectURL(file.raw);
      this.$set(this.data, "picId", res.obj);
      // this.data.picId = res.obj;
      this.loading = false;
    },
    // 上传图片前
    beforeAvatarUpload(file) {
      this.loading = true;
      const isLt4M = file.size / 1024 / 1024 < 4;
      if (!isLt4M) {
        this.$message.error("上传的图片大小不能超过 4MB!");
        this.loading = false;
      }
      return isLt4M;
    }
  }
};
</script>

<style lang="scss" scoped>
.mask {
  background-color: rgba(00, 00, 00, 0.5);
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100vh;
  z-index: 10;
  .rows {
    width: 25%;
    padding-top: 50px;
    position: absolute;
    text-align: center;
    background: #fff;
    left: 35%;
    top: 50%;
    height: 205px;
    margin-top: -102.5px;
    border-radius: 5px;
    .titles {
      height: 50px;
      width: 100%;
      border-bottom: 1px solid #eaeaea;
      position: absolute;
      left: 0;
      top: 0;
      font-size: 18px;
      line-height: 50px;
      i {
        width: 50px;
        height: 50px;
        position: absolute;
        right: 0;
        top: 0;
        line-height: 50px;
        cursor: pointer;
      }
    }
    .bodys {
      width: 100%;
      border-bottom: 1px solid #eaeaea;
      padding: 40px 0;
    }
    .btns {
      padding: 15px 20px;
      text-align: right;
      font-size: 17px;
    }
  }
  .formRows {
    min-width: 800px;
    width: 50%;
    position: absolute;
    left: 50%;
    top: 48%;
    transform: translate(-50%, -50%);
    padding: 0 20px;
    border-radius: 5px;
    background: #fff;
    // height: 540px;
    .titles {
      height: 50px;
      width: 100%;
      border-bottom: 1px solid #eaeaea;
      font-size: 18px;
      line-height: 50px;
      margin-bottom: 20px;
      position: relative;
      i {
        width: 50px;
        height: 50px;
        position: absolute;
        right: -20px;
        top: 0;
        line-height: 50px;
        cursor: pointer;
        text-align: center;
      }
    }
  }
}

.avatar-uploader {
  text-align: left;
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 300px;
    height: 240px;
    line-height: 240px;
    text-align: center;
  }
  .avatar {
    width: 300px;
    height: auto;
    max-height: 800px;
    display: block;
  }
  .el-icon-plus {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    position: relative;
  }
  .el-icon-plus:hover {
    border-color: #409eff;
  }
  .el-upload {
    vertical-align: top;
  }
}
</style>

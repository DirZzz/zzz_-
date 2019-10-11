<template>
  <div class="personalSet">
    <div class="shade-box-mask" v-show="shadeMask"></div>
    <el-form label-width="110px">
      <el-form-item label="账号：">
        <el-input class="account" disabled v-model="userInfo.nickName"></el-input>
      </el-form-item>

      <el-form-item label="头像：">
        <el-col :span="3" class="marRight" style="width: 94px;">
          <el-upload
            class="imgPosition"
            :action="userLogoUploadUrl"
            :data="userLogoUploadParam"
            :headers="headers"
            :on-preview="handlePictureCardPreview"
            list-type="picture-card"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :on-remove="handleRemove"
          >
            <i class="el-icon-plus" style="vertical-align: 23px"></i>
            <img v-if="imageUrl" :src=" imageUrl" width="90" height="90" alt />
            <!--<img  v-if="img.imagePath" :src="BASE_URL.sourceBaseUrl + img.picPath">-->
          </el-upload>
        </el-col>
        <el-col :span="20" style="color: #999">文件格式JPG，JPEG，PNG文件大小4M以内，尺寸1:1</el-col>
        <el-dialog :visible.sync="dialogVisible">
          <img width="100%" :src="dialogImageUrl" alt />
        </el-dialog>
      </el-form-item>

      <el-form-item label="名称：">
        <el-input class="account" maxlength="20" v-model="userInfo.userName"></el-input>
      </el-form-item>

      <el-form-item label="性别：">
        <el-radio-group v-model="userInfo.sex" class="mw">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="区域：" class="areaMarginRight">
        <!---------------------------------------------------->
        <el-col :span="3">
          <el-select
            v-model="citySelect.country"
            @change="getCountryCode(citySelect.country)"
            clearable
            placeholder="请选择国"
          >
            <el-option
              v-for="(item, index) in countrySelect"
              :key="item.areaCode"
              :label="item.areaName"
              :value="item.areaCode"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select
            v-model="citySelect.provide"
            @change="getProvideCode(citySelect.provide)"
            clearable
            placeholder="请选择省"
          >
            <el-option
              v-for="(item, index) in provideSelect"
              :key="item.areaCode"
              :label="item.areaName"
              :value="item.areaCode"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select
            clearable
            v-model="citySelect.city"
            @change="getCityCode(citySelect.city)"
            placeholder="请选择市"
          >
            <el-option
              v-for="(item,index) in citySelects"
              :key="item.areaCode"
              :label="item.areaName"
              :value="item.areaCode"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select
            clearable
            v-model="citySelect.area"
            @change="getAreaCode(citySelect.area)"
            placeholder="请选择区域"
          >
            <el-option
              v-for="(item,index) in areaSelect"
              :key="item.areaCode"
              :label="item.areaName"
              :value="item.areaCode"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="3" v-if="streetShow">
          <el-select
            clearable
            v-model="citySelect.origin"
            @change="getOriginCode(citySelect.origin)"
            placeholder="请选择街道"
          >
            <el-option
              v-for="(item,index) in originSelect"
              :key="item.areaCode"
              :label="item.areaName"
              :value="item.areaCode"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="3" v-else>
          <span class="streetSel">不限</span>
        </el-col>
        <!---------------------------------------------------->
      </el-form-item>

      <el-form-item label="详细地址：">
        <el-input class="detalAddress" maxlength="50" v-model="userInfo.address"></el-input>
      </el-form-item>

      <el-form-item label="邮箱：">
        <el-input class="account" @blur="emailMobile" v-model="userInfo.email"></el-input>
        <span v-if="okEmail" style="color:#FF6419;" class="el-icon-success"></span>
        <p v-if="isEmail" style="color:red;">请输入正确的邮箱地址</p>
      </el-form-item>
    </el-form>
    <div class="btnFrame">
      <el-button round type="primary" @click="updateAccount">保存</el-button>
    </div>
    <loginPackageTimeout :title="tryoutPackage" :text="packageText"></loginPackageTimeout>
  </div>
</template>

<script>
import qs from "qs";
import loginPackageTimeout from "../../packageUpdate/loginPackageTimeout";

export default {
  data() {
    return {
      tryoutPackage: "", //试用套餐或正式套餐
      packageText: "",
      mw: 1,
      fileList2: [],
      dialogImageUrl: "",
      dialogVisible: false,
      streetShow: true,
      phone: "",
      detailAddress: "",
      mail: "",
      userInfo: {},
      //区域选择-----------
      search: {
        franchiserName: "",
        phone: "",
        contactName: "",
        country: "",
        provide: "",
        city: "",
        area: "",
        origin: ""
      },
      citySelect: {
        country: "",
        provide: "",
        city: "",
        area: "",
        origin: ""
      },
      cityCode: "",
      countrySelect: [],
      provideSelect: [],
      citySelects: [],
      areaSelect: [],
      originSelect: [],
      uploadImgId: "",
      imageUrl: "",
      userLogoUploadParam: {
        fileKey: "system.sysUser.pic",
        platform: "web",
        module: "product",
        type: "image"
      },
      userLogoUploadUrl: "",
      //区域选择 结束------------
      emailTxt: "",
      okEmail: false,
      nameHint: false,
      mailboxHint: false,
      isEmail: false,
      shadeMask: false
    };
  },
  created() {
    this.getArea("root", "国");
    this.getUserInfo();
    this.packageTimeout();
    this.userLogoUploadUrl = this.BASE_URL.uploadUrl;
  },
  mounted() {
    this.packageTimeout();
  },
  computed: {
    headers() {
      return {
        //上传图片时传token给后台
        Authorization: qs.parse(sessionStorage.getItem("loginUser")).token
      };
    }
  },
  methods: {
    // 套餐到期标识
    packageTimeout() {
      //1.账号是否到期
      // if(sessionStorage.getItem('exceptionCode')=='10010040'){  //账号过期
      //     this.$store.dispatch('dialog',{isShow:true,name:'loginPackageTimeout'});
      //     this.tryoutPackage='您的账号有效期已到';
      // }
      if (
        sessionStorage.getItem("leftTime") &&
        Number(sessionStorage.getItem("leftTime")) <= 5
      ) {
        if (sessionStorage.getItem("firstLoginToday") === "true") {
          // true,就是当天首次登录，为false，不是当天首次登录
          this.$store.dispatch("dialog", {
            isShow: true,
            name: "loginPackageTimeout"
          });
          this.tryoutPackage = `您的账号还剩${sessionStorage.getItem(
            "leftTime"
          )}天到期`;
          this.packageText = `可联系账号提供者进行延期`;
        } else {
          this.$store.dispatch("dialog", {
            isShow: false,
            name: "loginPackageTimeout"
          });
          this.tryoutPackage = `您的账号还剩${sessionStorage.getItem(
            "leftTime"
          )}天到期`;
          this.packageText = `可联系账号提供者进行延期`;
        }
        return;
      }
      //2.套餐是否到期
      if (
        sessionStorage.getItem("serviceType") == "0" &&
        sessionStorage.getItem("maturityFlag") == "true"
      ) {
        //正式套餐 过期
        setTimeout(() => {
          this.$store.dispatch("dialog", {
            isShow: true,
            name: "loginPackageTimeout"
          });
        }, 1000);
        this.tryoutPackage = `您的正式套餐“${sessionStorage.getItem(
          "serviceName"
        )}”已到期`;
        this.packageText = `可联系账号提供者进行续费`;
        this.shadeMask = true;
        return;
      } else if (
        sessionStorage.getItem("serviceType") == "0" &&
        sessionStorage.getItem("tipsFlag") == "true"
      ) {
        //正式套餐 不过期 只剩下小于等于5天
        setTimeout(() => {
          this.$store.dispatch("dialog", {
            isShow: true,
            name: "loginPackageTimeout"
          });
        }, 1000);
        this.tryoutPackage = `您的正式套餐“${sessionStorage.getItem(
          "serviceName"
        )}”还有${sessionStorage.getItem("remainingDays")}天到期`;
        this.packageText = `可联系账号提供者进行续费`;
        return;
      } else if (
        sessionStorage.getItem("serviceType") == "1" &&
        sessionStorage.getItem("maturityFlag") == "true"
      ) {
        //试用套餐 过期
        setTimeout(() => {
          this.$store.dispatch("dialog", {
            isShow: true,
            name: "loginPackageTimeout"
          });
        }, 1000);
        this.tryoutPackage = `您的试用套餐“${sessionStorage.getItem(
          "serviceName"
        )}”已到期`;
        this.packageText = `可联系三度进行延期或直接购买正式套餐`;
        this.shadeMask = true;
        return;
      } else if (
        sessionStorage.getItem("serviceType") == "1" &&
        sessionStorage.getItem("tipsFlag") == "true"
      ) {
        //试用套餐 不过期 只剩下小于等于5天
        setTimeout(() => {
          this.$store.dispatch("dialog", {
            isShow: true,
            name: "loginPackageTimeout"
          });
        }, 1000);
        this.tryoutPackage = `您的试用套餐“${sessionStorage.getItem(
          "serviceName"
        )}”还有${sessionStorage.getItem("remainingDays")}天到期`;
        this.packageText = `可联系三度进行延期或直接购买正式套餐`;
        return;
      }
    },
    //邮箱
    emailMobile() {
      if (this.userInfo.email) {
        let reg = new RegExp(
          "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"
        );
        if (!reg.test(this.userInfo.email)) {
          this.isEmail = true;
          this.okEmail = false;
          return false;
        } else {
          this.okEmail = true;
          this.isEmail = false;
          return true;
        }
      }
    },
    // 区域选择 结束---------- streetCodeName
    handleAvatarSuccess(res, file) {
      console.log(res, "img");
      this.imageUrl = res.data.url;
      console.log(this.imageUrl, "url");
      this.uploadImgId = res.data.resId;
    },

    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isPNG = file.type === "image/png";
      const isGIF = file.type === "image/gif";
      const isLt2M = file.size / 1024 / 1024 < 4;

      if (!isJPG && !isPNG && !isGIF) {
        this.$message.error("上传图片只能是 GIF/JPG/PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 4M!");
      }
      return (isJPG || isPNG || isGIF) && isLt2M;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    // 区域选择-------------
    getArea(code, type) {
      this.API.areaList({ areaCode: code }).then(res => {
        if (res) {
          if (type == "国") {
            this.countrySelect = res.datalist;
          }
          if (type == "省") {
            this.provideSelect = res.datalist;
          }
          if (type == "市") {
            this.citySelects = res.datalist;
          }
          if (type == "区") {
            this.areaSelect = res.datalist;
          }
          if (type == "街道") {
            this.originSelect = res.datalist;

            if (!this.originSelect || this.originSelect.length == 0) {
              this.streetShow = false;
              // this.originSelect = [{areaCode: '', areaName: '不限'}]
            } else {
              this.streetShow = true;
            }
          }
        }
      });
    },
    getCountryCode(code) {
      this.citySelect.provide = "";
      this.citySelect.city = "";
      this.citySelect.area = "";
      this.citySelect.origin = "";
      this.provideSelect = [];
      this.citySelects = [];
      this.areaSelect = [];
      this.originSelect = [];
      this.getArea(code, "省");
    },
    getProvideCode(code) {
      this.citySelect.city = "";
      this.citySelect.area = "";
      this.citySelect.origin = "";
      this.citySelects = [];
      this.areaSelect = [];
      this.originSelect = [];
      this.getArea(code, "市");
    },
    getCityCode(code) {
      this.citySelect.area = "";
      this.citySelect.origin = "";
      this.areaSelect = [];
      this.originSelect = [];
      this.getArea(code, "区");
    },
    getAreaCode(code) {
      this.citySelect.origin = "";
      this.originSelect = [];
      this.getArea(code, "街道");
    },
    getOriginCode(code) {},
    // 区域选择 结束---------- streetCodeName
    getUserInfo() {
      this.API.getUserDetailInfo({
        token: sessionStorage.getItem("token")
      }).then(res => {
        console.log(res);
        if (res) {
          this.userInfo = res.obj;
          this.imageUrl = this.BASE_URL.sourceBaseUrl + res.obj.picPath;
          this.uploadImgId = res.obj.picId;

          if (res.obj.countryCode) {
            this.citySelect.country = res.obj.countryCode;
            this.getArea(res.obj.countryCode, "省");
          }
          if (res.obj.provinceCode) {
            this.citySelect.provide = res.obj.provinceCode;
            this.getArea(res.obj.provinceCode, "市");
          }
          if (res.obj.cityCode) {
            this.citySelect.city = res.obj.cityCode;
            this.getArea(res.obj.cityCode, "区");
          }
          if (res.obj.areaCode) {
            this.citySelect.area = res.obj.areaCode;
            this.getArea(res.obj.areaCode, "街道");
          }
          if (res.obj.streetCode) {
            this.citySelect.origin = res.obj.streetCode;
          }
        }
      });
    },
    updateAccount() {
      let cond = {
        address: this.userInfo.address, //详细地址 ,
        areaCode: this.citySelect.area, //区编码 ,
        cityCode: this.citySelect.city, //市编码 ,
        email: this.userInfo.email, //Email ,
        phone: this.userInfo.mobile, //手机号 ,
        picId: this.uploadImgId, //头像Id ,
        countryCode: this.citySelect.country,
        provinceCode: this.citySelect.provide, //省编码 ,
        sex: this.userInfo.sex, //性别:1男,2女 ,
        streetCode: this.citySelect.origin, //街道编码
        username: this.userInfo.userName,
        token: sessionStorage.getItem("token")
      };

      // if(!this.emailMobile()) {
      //     return
      // }
      this.API.setEditUser(cond).then(res => {
        if (res) {
          this.getUserInfo();
          this.$message.success("信息更新成功");
        }
      });
    }
  },
  components: {
    loginPackageTimeout
  }
};
</script>

<style scoped lang="scss">
.personalSet {
  background: #fff;
  padding: 20px;
}
.imgPosition {
  position: relative;
  img {
    position: absolute;
    top: 0;
    left: 0;
    border-radius: 6px;
    z-index: 0;
  }
}
.shade-box-mask {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 2000;
  width: 100%;
  height: 100%;
  background-color: white;
  opacity: 0.4;
}
.btnFrame {
  padding-top: 25px;
  border-top: solid 1px #ddd;
  .el-button {
    width: 133px;
    height: 40px;
    margin-left: 121px;
  }
}
.account {
  width: 321px;
}
.detalAddress {
  width: 501px;
}
.mw {
  .el-radio {
    margin-right: 15px;
  }
}
.areaMarginRight {
  .el-col {
    margin-right: 15px;
  }
}
</style>
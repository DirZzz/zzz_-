<template>
  <transition name="fade">
    <div>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header class="detailToEdit">
          <h1>详情</h1>
          <el-button @click="gotoEdit" v-if="isPer('texture:edit')" round>编 辑</el-button>
        </header>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">材质球信息</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>

        <div>
          <el-form label-width="110px" class="demo-ruleForm">
            <el-row>
              <el-col :span="7">
                <el-form-item label="材质编号：">{{texture.textureCode}}</el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="品牌名称：">{{texture.brandName}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="7">
                <el-form-item label="材质名称：">{{texture.textureName}}</el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="材质型号：">{{texture.modelNumber}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item label="材质规格：">
                  长/cm {{texture.fileWidth}}
                  <span
                    style="margin-left: 40px;"
                  >宽/cm {{texture.fileLength}}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="7">
                <el-form-item label="材质材料：">{{texture.textureLabel || '无'}}</el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="材质细分类：">{{texture.classificationName || '无'}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="7">
                <el-form-item label="色系：">{{texture.colorSystemName || '无'}}</el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item
                  labelWidth="200"
                  label="材质表面属性："
                >{{texture.textureAttrValueName || '无'}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item label="最后修改时间：">{{texture.gmtModified}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item label="贴图类型：">{{texture.textureballFileId == 0 ? '贴图' : '材质球'}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item label="材质图片：">
                  <img :src="BASE_URL.sourceBaseUrl+texture.thumbnailPath" width="90" height="90">
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item label="备注：">
                  <div class="productRemark">{{texture.remark || '无'}}</div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">贴图规则</el-col>
              <el-col :span="22" class="rightLine">
                <hr>
              </el-col>
            </el-row>
            <el-row>
              <el-form-item label="法线贴图：">
                <!--BASE_URL.sourceBaseUrl+-->
                <img
                  v-if="texture.normalPicPath"
                  :src="texture.normalPicPath"
                  width="90"
                  height="90"
                >
                <span v-else>无</span>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="法线参数：">{{texture.normalParam || '无'}}</el-form-item>
            </el-row>
          </el-form>
        </div>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">材质球文件</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>
        <el-form label-width="130px" v-if="texture.ballName" class="demo-ruleForm">
          <el-row>
            <el-col :span="10">
              <el-form-item label="材质球文件：">
                <a
                  style="color: #666; display: inline-block"
                  :href="BASE_URL.sourceBaseUrl+texture.textureBallPath"
                >{{texture.ballName}}</a>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form label-width="130px" class="demo-ruleForm">
          <el-row>
            <el-col :span="10">
              <el-form-item label="材质球处理状态：">
                <span v-if="texture.transStatus === 'ING'">转化中</span>
                <span v-if="texture.transStatus === 'SUCCESS'">转化成功</span>
                <span v-if="texture.transStatus === 'FAIL'">转化失败</span>
                <span v-if="texture.transStatus === 'NONE'">未处理</span>
                <span v-if="!texture.transStatus">无</span>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>
  </transition>
</template>

<script>
import { mapState } from "vuex";
import qs from "qs";
export default {
  data() {
    return {
      nav: [{ nav: "贴图库", url: "/texture" }, { nav: "贴图详情", url: "" }],
      texture: {}
    };
  },
  created() {
    this.textureId = this.$route.params.textureId;
    this.getTextureDetail();
  },
  methods: {
    isPer(per) {
      let permiss = qs.parse(sessionStorage.getItem("loginUser"));
      return this.isMainPer(per, permiss);
    },
    getTextureDetail() {
      this.API.GetTexture(this.textureId).then(res => {
        this.texture = res.data;
      });
    },
    gotoEdit() {
      this.$router.push({ path: `/texture/${this.textureId}/edit` });
    },
    /* 上传图片 */
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    /* 上传图片 */
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          alert("submit!");
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  },
  computed: mapState({
    productId: state => state.productId
  })
};
</script>

<style lang="scss" scoped>
@import "../../../assets/css/mixin";

.addProduct {
  background: #fff;
  box-sizing: padding-box;
  padding: 20px;
  border-radius: $borderRadius;
  margin-bottom: 20px;
  header {
    h1 {
      font-weight: normal;
      font-size: 18px;
      padding-bottom: 15px;
      color: $greyOverButton;
      border-bottom: solid 1px $greyBorderEEE;
    }
  }
  p {
    height: 30px;
    line-height: 30px;
    padding-top: 30px;
    font-size: 14px;
    color: #666;
    span {
      height: 20px;
      line-height: 20px;
      display: inline-block;
      padding-right: 5px;
      color: red;
      font-weight: bold;
      font-size: 20px;
      vertical-align: -6px;
    }
  }

  .selectWidth {
    width: 305px;
  }
  .textareaWidth {
    width: 705px;
    height: 130px;
    background-color: #eee;
  }
}

.el-select .el-input {
  width: 130px;
  margin-right: -10px;
}

.input-with-select .el-input-group__append {
  background-color: #fff;
  width: 130px;
}

/*上传图片样式 */
.avatar-uploader {
  background: #eee;
  box-sizing: border-box;
  padding: 15px 0 0 15px;
  border-radius: 4px;
}
.detailToEdit {
  position: relative;
  .el-button {
    position: absolute;
    top: -8px;
    right: 0;
    border: solid 1px #ff6419;
    color: #ff6419;
  }
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 90px;
  height: 90px;
  line-height: 90px;
  text-align: center;
}

.avatar {
  width: 90px;
  height: 90px;
  display: block;
}

/*上传图片样式 end*/
</style>
<style>
.el-upload-list__item img {
  width: 90px;
  height: 90px;
}

.el-upload-list__item-thumbnail {
  width: 90px !important;
  height: 90px !important;
}

.el-upload-list--picture-card .el-upload-list__item {
  overflow: hidden;
  background-color: #fff;
  border: 1px solid #c0ccda;
  border-radius: 6px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 90px;
  height: 90px;
  margin: 0 8px 8px 0;
  display: inline-block;
}

.el-upload--picture-card {
  background-color: #fbfdff;
  border: 1px dashed #c0ccda;
  border-radius: 6px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 90px;
  height: 90px;
  line-height: 88px;
  vertical-align: top;
}

.el-progress--circle .el-progress__text {
  position: absolute;
  top: 50%;
  left: 0;
  width: 100%;
  text-align: center;
  margin: 0;
  -webkit-transform: translate(0, -50%);
  transform: translate(0, -50%);
}

.el-progress--circle .el-progress__text i {
  vertical-align: middle;
  display: inline-block;
}

.is-uploading {
  width: 90px;
  height: 90px;
}

.el-upload-list--picture-card .el-progress {
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%) scale(0.6);
  transform: translate(-50%, -50%) scale(0.6);
  bottom: auto;
  width: 126px;
  height: 126px;
}
</style>


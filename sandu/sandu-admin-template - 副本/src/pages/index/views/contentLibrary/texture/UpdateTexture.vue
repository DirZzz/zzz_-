<template>
  <transition name="fade">
    <div>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header>
          <h1>编辑材质贴图</h1>
        </header>
        <p>
          <span>*</span>为必填项
        </p>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">材质球信息</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>

        <div>
          <el-form
            :model="texture"
            :rules="rules"
            ref="ruleForm"
            label-width="130px"
            class="demo-ruleForm"
          >
            <!--:rules="rules" ref="ruleForm"-->
            <el-row>
              <el-col :span="8">
                <el-form-item label="材质编号：" prop>{{texture.textureCode}}</el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="14">
              <el-col :span="7">
                <el-form-item label="材质名称：" prop="textureName">
                  <input maxlength="50" v-model="texture.textureName" class="formWidth inputText">
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="材质型号：" style="margin-bottom: 25px">
                  <!-- prop="modelNumber"-->
                  <input class="inputText" v-model="texture.modelNumber" maxlength="20">
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="14">
              <el-col :span="7">
                <el-form-item label="产品品牌：">
                  <el-select v-model="texture.brandId" style="width: 250px">
                    <el-option
                      v-for="brand in brandList"
                      :key="brand.id"
                      :label="brand.name"
                      :value="brand.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="贴图类型：">{{texture.textureballFileId == 0 ? '贴图' : '材质球'}}</el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="14">
              <el-col :span="7">
                <el-form-item label="材质(材料)：" prop="texture">
                  <el-select
                    filterable
                    v-model="texture.texture"
                    class="formWidth"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in texturesDrop"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <!--prop="textureAttrValue"-->
                <el-form-item label="材质表面属性：">
                  <el-select
                    filterable
                    class="formWidth"
                    v-model="texture.textureAttrValue"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in textureAttrsDrop"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="14">
              <el-col :span="7">
                <el-form-item label="长度(cm)：" prop="fileWidth">
                  <input
                    maxlength="6"
                    oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                    v-model="texture.fileWidth"
                    class="formWidth inputText2"
                  >
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="宽度(cm)：" prop="fileLength">
                  <input
                    maxlength="6"
                    oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                    v-model="texture.fileLength"
                    class="formWidth inputText2"
                  >
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="14">
              <el-col :span="7">
                <el-form-item label="材质细分类：" prop="classification">
                  <el-select
                    style="width: 250px"
                    v-model="texture.classification"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in classificationList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="色系" prop="colorSystem">
                  <el-select
                    style="width: 250px"
                    v-model="texture.colorSystem"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in colorSystemList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="8">
                <el-form-item label="最后修改时间：">{{texture.gmtModified}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-form-item label="材质图片：">
                <single-image-upload
                  :on-success="handleTextureImgSuccess"
                  style="width: 708px"
                  @deleteClick="deleteTextureImage"
                  :image-url="textureImageUrl"
                ></single-image-upload>
              </el-form-item>
            </el-row>

            <el-row>
              <el-col>
                <el-form-item label="备注：">
                  <textarea
                    maxlength="200"
                    style="width: 700px"
                    class="productDesc"
                    :rows="6"
                    placeholder="请输入内容"
                    v-model="texture.remark"
                  ></textarea>
                  <p style="padding-top: 10px">此处最多允许填写200字</p>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-form-item label="法线贴图：">
                <single-image-upload
                  :on-success="handleNormalImgSuccess"
                  style="width: 708px"
                  @deleteClick="deleteNormalImage"
                  :image-url="normalImageUrl"
                ></single-image-upload>
              </el-form-item>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="法线参数：" prop>
                  <input
                    v-model="texture.normalParam"
                    class="formWidth sizes"
                    oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                  >
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">材质球文件</el-col>
              <el-col :span="22" class="rightLine">
                <hr>
              </el-col>
            </el-row>

            <el-row>
              <el-col>
                <el-form-item label="材质球文件：">
                  <ul class="modelNameShow">
                    <li>
                      <el-upload
                        style="display: inline-block"
                        :action="BASE_URL.modelUploadUrl  +'/ball'"
                        :show-file-list="false"
                        :on-success="handleTextureSuccess"
                        :before-upload="beforeTextureUpload"
                      >
                        <el-button
                          style="background-color: #FF6419;border-radius: 20px;color: #fff;margin-left: 10px"
                        >选择文件</el-button>
                      </el-upload>
                      <span v-if="!fileShow" title="点击下载">
                        <a
                          style="color: #666; display: inline-block"
                          :href="BASE_URL.sourceBaseUrl+texture.textureBallPath"
                        >{{texture.ballName}}</a>
                      </span>
                      <span v-else style="color: #666; display: inline-block">{{fileShow}}</span>
                    </li>
                  </ul>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item>
              <el-button round class="confirmBtn" type="primary" @click="submitForm('ruleForm')">确定</el-button>
              <el-button round class="cancelBtnBg" @click="resetForm()">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
// import ElButton from '../../../../node_modules/element-ui/packages/button/src/button.vue'
import SingleImageUpload from "../../base/upload/SingleImageUpload.vue";

export default {
  components: {
    SingleImageUpload
  },
  data() {
    return {
      nav: [{ nav: "贴图库", url: "/texture" }, { nav: "贴图编辑", url: "" }],
      textureBallPath: "",
      fileShow: "",
      textureValue: "",
      texture: {
        texture: 0
      },
      textureType: [
        {
          value: 0,
          label: "贴图"
        },
        {
          value: 1,
          label: "材质球"
        }
      ],
      textureId: 0,
      texturesDrop: [],
      textureAttrsDrop: [],
      textureImageUrl: "",
      textureImageId: 0,
      classificationList: [],
      colorSystemList: [],
      brandList: [],
      normalImageUrl: "",
      normalPicId: 0,
      rules: {
        textureName: [
          { required: true, message: "请输入贴图名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 50 个字符内", trigger: "blur" }
        ],
        // modelNumber:[
        //   {required: true, message: '请输入材质型号', trigger: 'blur'}
        //   ],
        remark: [
          { min: 1, max: 200, message: "长度在 200 个字符内", trigger: "blur" }
        ],
        fileWidth: [
          { required: true, max: 5, message: "请输入贴图宽度", trigger: "blur" }
        ],
        fileLength: [
          { required: true, message: "请输入贴图长度", trigger: "blur" }
        ],
        textureAttrValue: [
          { required: true, message: "请输选择材质表面属性", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    chTexture(val) {
      this.textureValue = val;
    },
    /* 上传图片 */
    handleTextureImgSuccess(resp) {
      this.textureImageUrl = resp.data.url;
      this.textureImageId = resp.data.resId;
    },

    handleNormalImgSuccess(resp) {
      this.normalImageUrl = resp.data.url;
      this.normalPicId = resp.data.resId;
    },
    deleteTextureImage() {
      this.textureImageUrl = "";
    },
    // 材质球上传文件
    handleTextureSuccess(resp, file) {
      this.textureBallPath = resp.data.url;
      this.texture.textureballFileId = null;
      var index = resp.data.url.lastIndexOf("/");
      var str = resp.data.url.substring(index + 1, resp.data.url.length);
      this.fileShow = str;
    },

    // 获取品牌下拉框
    getBrandList() {
      this.API.ProductBrandDrop({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        this.brandList = res.data;
      });
    },
    beforeTextureUpload(resp, file) {
      let assetbundle = resp.name.substring(resp.name.lastIndexOf(".") + 1);
      let sdu = resp.name.substring(resp.name.lastIndexOf(".") + 1);
      let ext = sdu === "3du" || sdu === "3DU";
      let asset = assetbundle === "assetbundle";
      if (!ext && !asset) {
        this.$message.error("上传文件只能是 assetbundle 或者 3DU 格式!");
        return false;
      }
      return ext || asset;
    },
    deleteNormalImage() {
      this.normalImageUrl = "";
      this.normalPicId = "";
    },
    getDetail() {
      this.API.GetTexture(this.textureId).then(resp => {
        resp.data.texture = resp.data.texture = resp.data.texture
          ? parseInt(resp.data.texture)
          : 0;
        this.texture = resp.data;
        this.textureImageUrl =
          this.BASE_URL.sourceBaseUrl + this.texture.thumbnailPath;
        this.normalImageUrl = this.texture.normalPicPath;
        this.normalPicId = this.texture.normalPicId;
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("确定更新贴图", "提示", {
            cancelButtonClass: "cancelButtonClass",
            confirmButtonClass: "confirmButtonClass",
            center: true,
            type: "info"
          })
            .then(() => {
              let params = Object.assign({}, this.texture);
              params.thumbnailPath = this.textureImageUrl.replace(
                this.BASE_URL.sourceBaseUrl,
                ""
              );
              params.normalPicId = this.normalPicId;
              params.textureId = this.textureId;
              params.thumbnailId = this.textureImageId;
              params.companyId = sessionStorage.getItem("companyID");
              params.textureBallPath = this.textureBallPath.replace(
                this.BASE_URL.sourceBaseUrl,
                ""
              );
              for (let key in params) {
                // if (!params[key] && params[key] == 0 )  params[key] == '' || params[key] == null || params[key] == ''
                if (!params[key] && params[key] == 0) {
                  delete params[key];
                }
              }
              this.API.EditTexture(params).then(res => {
                if (res) {
                  this.$message.success("更新贴图成功");
                  this.$router.push(`/texture`);
                }
              });
            })
            .catch(() => {
              this.$router.push(`/texture`);
            });
        }
      });
    },
    resetForm(formName) {
      this.$router.push(`/texture`);
      this.getDetail();
    },
    getTexturesDrop() {
      this.API.ProductTextureDrop().then(resp => {
        this.texturesDrop = resp.data;
      });
    },
    getTextureAttrsDrop() {
      this.API.textureAttr().then(resp => {
        this.textureAttrsDrop = resp.data;
      });
    },
    initClassificationList() {
      this.API2.getClassification().then(res => {
        this.classificationList = res.data;
      });
    },
    initColorSystemList() {
      this.API2.getColorSystem().then(res => {
        this.colorSystemList = res.data;
      });
    }
  },
  mounted() {
    this.textureId = this.$route.params.textureId;
    this.getDetail();
    this.getTexturesDrop();
    this.getTextureAttrsDrop();
    this.getBrandList();
    this.initClassificationList();
    this.initColorSystemList();
  }
};
</script>

<style lang="scss" scoped>
@import "../../../assets/css/mixin";

.sizes {
  border: solid 1px #ddd;
  height: 40px;
  border-radius: 6px;
  padding-left: 10px;
}
.formWidth {
  width: 250px !important;
}
.addProduct {
  background: #fff;
  box-sizing: padding-box;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: $borderRadius;
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
</style>


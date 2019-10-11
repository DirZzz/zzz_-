<template>
  <transition name="fade">
    <div>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header>
          <h1>添加贴图</h1>
        </header>
        <p>
          <span>*</span>为必填项
        </p>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">贴图基础信息</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>

        <div>
          <el-form
            :model="texture"
            :rules="rules"
            ref="ruleForm"
            label-width="120px"
            class="demo-ruleForm"
          >
            <!--:rules="rules" ref="ruleForm"-->
            <el-row :gutter="14">
              <el-col :span="7">
                <el-form-item label="材质名称" style="margin-bottom: 25px" prop="textureName">
                  <input class="inputText" v-model="texture.textureName" maxlength="50">
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="材质型号" style="margin-bottom: 25px">
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
                <el-form-item label="长度（cm）" prop="fileLength">
                  <input
                    maxlength="6"
                    class="sizes"
                    v-model="texture.fileWidth"
                    oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                  >
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="14">
              <el-col :span="7">
                <el-form-item label="材质(材料)" prop>
                  <el-select
                    style="width: 250px"
                    v-model="texture.texture"
                    filterable
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in textureDrop"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="宽度（cm）" prop="fileWidth">
                  <input
                    maxlength="6"
                    v-model="texture.fileLength"
                    class="sizes"
                    oninput="value=value.replace(/[^\d]/g,'')"
                  >
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="14">
              <el-col :span="7">
                <el-form-item label="材质细分类" prop="classification">
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
                <!-- prop="textureAttrValue"-->
                <el-form-item label="材质表面属性">
                  <el-select
                    filterable
                    style="width: 250px"
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

            <el-row class="starReq">
              <i>*</i>
              <el-form-item label="贴图图片" prop="textureImageUrl">
                <single-image-upload
                  :on-success="handleTextureImgSuccess"
                  :delete-image="deleteimg"
                  @deleteClick="textureImageUrl = ''"
                  :image-url="textureImageUrl"
                  :modelName="modelName"
                ></single-image-upload>
                <span style="color: red; font-size: 12px" v-if="!textureImageUrl">请上传图片</span>
              </el-form-item>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item label="备注" prop="remark">
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

            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">贴图规则</el-col>
              <el-col :span="22" class="rightLine">
                <hr>
              </el-col>
            </el-row>

            <el-row>
              <el-form-item label="法线贴图" style="border-bottom: 1px solid #eee">
                <single-image-upload
                  @delete="parentDelete"
                  :on-success="handleNormalImgSuccess"
                  @deleteClick="normalImageUrl = ''"
                  :image-url="normalImageUrl"
                ></single-image-upload>
              </el-form-item>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="法线参数" prop>
                  <input
                    maxlength="6"
                    v-model="texture.normalParam"
                    class="sizes"
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
                      <span>{{fileShow}}</span>
                      <el-upload
                        :action="BASE_URL.modelUploadUrl  +'/ball'"
                        :show-file-list="false"
                        :on-success="handleTextureSuccess"
                        :before-upload="beforeTextureUpload"
                      >
                        <el-button
                          style="background-color: #FF6419;border-radius: 20px;color: #fff;margin-left: 10px"
                        >选择文件</el-button>
                      </el-upload>
                    </li>
                  </ul>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item>
              <el-button
                type="primary"
                class="confirmBtn marRight"
                round
                @click="submitForm('ruleForm')"
              >确定</el-button>
              <el-button @click="resetForm('ruleForm')" round class="cancelBtnBg">取消</el-button>
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
  data() {
    return {
      nav: [{ nav: "贴图库", url: "/texture" }, { nav: "新增贴图", url: "" }],
      modelName: "material",
      textureBallPath: "",
      fileShow: "",
      textureType: [
        {
          value: "texture",
          label: "贴图"
        },
        {
          value: "ball",
          label: "材质球"
        }
      ],
      texture: {},
      textureId: 0,
      // 上传图片文件列表
      textureImageUrl: "",
      textureImageId: 0,
      normalImageUrl: "",
      normalPicId: null,
      textureAttrsDrop: [],
      classificationList: [],
      colorSystemList: [],
      brandList: [],
      textureDrop: [],
      rules: {
        textureName: [
          { required: true, message: "请输入贴图名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 50 个字符内", trigger: "blur" }
        ],
        remark: [
          { min: 1, max: 200, message: "长度在 200 个字符内", trigger: "blur" }
        ],
        fileWidth: [
          { required: true, message: "请输入贴图宽度", trigger: "blur" }
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
  components: { SingleImageUpload },
  methods: {
    deleteimg() {},
    // 材质球上传文件
    handleTextureSuccess(resp, file) {
      this.textureBallPath = resp.data.url;
      var index = resp.data.url.lastIndexOf("/");
      var str = resp.data.url.substring(index + 1, resp.data.url.length);
      this.fileShow = str;
    },
    beforeTextureUpload(resp, file) {
      let assetbundle = resp.name.substring(resp.name.lastIndexOf(".") + 1);
      let sdu = resp.name
        .substring(resp.name.lastIndexOf(".") + 1)
        .toUpperCase();
      let ext = sdu === "3DU";
      let asset = assetbundle === "assetbundle";
      if (!ext && !asset) {
        this.$message.error("上传文件只能是 assetbundle 或者 3DU 格式!");
        return false;
      }
      return ext || asset;
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
    // 获取品牌下拉框
    getBrandList() {
      this.API.ProductBrandDrop({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        this.brandList = res.data;
      });
    },
    parentDelete() {
      this.normalImageUrl = "";
      this.normalPicId = "";
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid && this.textureImageUrl) {
          let params = Object.assign({}, this.texture);
          params.companyId = sessionStorage.getItem("companyID");
          params.thumbnailPath = this.textureImageUrl.replace(
            this.BASE_URL.sourceBaseUrl,
            ""
          );
          params.normalPicId = this.normalPicId;
          params.thumbnailId = this.textureImageId;
          params.textureBallPath = this.textureBallPath.replace(
            this.BASE_URL.sourceBaseUrl,
            ""
          );
          for (let key in params) {
            if (
              params[key] == "" ||
              params[key] == undefined ||
              params[key] == null
            ) {
              delete params[key];
            }
          }
          this.$confirm("确定录入贴图?", "提示", {
            confirmButtonText: "确定",
            type: "info",
            center: true
          }).then(() => {
            this.API.AddTexture(params).then(res => {
              if (res) {
                this.$message.success("贴图录入成功");
                this.$router.replace("/texture");
              }
            });
          });
        }
      });
    },
    resetForm() {
      this.$router.push("/texture");
    },
    getTexturesDrop() {
      this.API.textureCaiLiao().then(resp => {
        this.textureDrop = resp.data;
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
    this.getTextureAttrsDrop();
    this.getTexturesDrop();
    this.getBrandList();
    this.initClassificationList();
    this.initColorSystemList();
  }
};
</script>
<style lang="scss">
.tAttr .el-form-item__error {
  padding-left: 100px !important;
}
</style>
<style lang="scss" scoped>
@import "../../../assets/css/mixin";

.starReq {
  position: relative;
  i {
    color: red;
    left: 22px;
    top: 13px;
    font-style: normal;
    position: absolute;
  }
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

.sizes {
  width: 240px;
  border: solid 1px #ddd;
  height: 40px;
  border-radius: 6px;
  padding-left: 10px;
}
</style>

<style>
.errorLabel .el-form-item__error {
  left: 100px !important;
}
</style>

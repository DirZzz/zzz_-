<template>
  <transition name="fade">
    <div>
      <el-dialog
        title="添加运营分类信息"
        :visible.sync="infoClassifyVisible"
        class="info-ypop-up"
        width="800px"
      >
        <el-row>
          <el-col :span="24">选择分类：</el-col>
        </el-row>
        <el-row class="Classify-list">
          <el-col :span="3">
            <p
              v-for="(item, index) in productCategorylist1"
              :key="index"
              @click="selectType(index, 1)"
              :class="indexList.index1 == index ? 'p-active' : ''"
            >
              {{item.name}}
              <span>></span>
            </p>
          </el-col>
          <el-col :span="5">
            <p
              v-for="(item, index) in productCategorylist2"
              @click="selectType(index, 2)"
              :key="index"
              :class="indexList.index2 == index ? 'p-active' : ''"
            >
              {{item.name}}
              <span>></span>
            </p>
          </el-col>
          <el-col :span="5">
            <p
              v-for="(item, index) in productCategorylist3"
              @click="selectType(index, 3)"
              :key="index"
              :class="indexList.index3 == index ? 'p-active' : ''"
            >
              {{item.name}}
              <span>></span>
            </p>
          </el-col>
          <el-col :span="5">
            <p
              v-for="(item, index) in productCategorylist4"
              @click="selectType(index, 4)"
              :key="index"
              :class="indexList.index4 == index ? 'p-active' : ''"
            >
              {{item.name}}
              <span>></span>
            </p>
          </el-col>
          <el-col :span="6">
            <el-checkbox-group v-model="checkProductList">
              <el-checkbox
                v-for="(item, index) in productCategorylist5"
                :label="item.id"
                :key="index"
                :checked="item.active"
                @change="selectType(index, 5)"
              >{{item.name}}</el-checkbox>
              <!--<el-checkbox :checked="true">备选项1</el-checkbox>-->
            </el-checkbox-group>
          </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
          <el-button round type="primary" @click="verifyProductClass">确 定</el-button>
          <el-button round @click="infoClassifyVisible = false">取 消</el-button>
        </div>
      </el-dialog>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header>
          <h1>产品复制</h1>
        </header>
        <p class="addProduct_p">
          <span>*</span>为必填项
        </p>
        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">产品基础信息</el-col>
          <el-col :span="22" class="rightLine">
            <hr />
          </el-col>
        </el-row>
        <div>
          <el-form
            :model="product"
            :rules="rules"
            ref="ruleForm"
            label-width="100px"
            class="demo-ruleForm"
          >
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品编号：">{{product.code}}</el-form-item>
              </el-col>
            </el-row>
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品名称：" prop="name">
                  <el-input class="formWidth" v-model="product.name"></el-input>
                  <!--<el-input class="formWidth" clearable v-model="product.name"></el-input> &lt;!&ndash;product.name&ndash;&gt;-->
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="产品分类：" prop="categoryCodes">
                  <el-input
                    style="width:321px"
                    placeholder="请输入内容"
                    v-model="categoryCodeName"
                    :disabled="true"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品品牌：" prop="brandId">
                  <el-select
                    v-model="brandValueStr"
                    filterable
                    clearable
                    class="formWidth"
                    @change="selectedBrandID"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in productBrand"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品型号：" prop="modelNumber">
                  <!--<el-input class="formWidth" clearable v-model="product.modelNumber"/> -->
                  <!--product.modelNumber-->
                  <!--v-model="product.modelNumber"-->
                  <el-autocomplete
                    style="width: 302px"
                    v-model="product.modelNumber"
                    :fetch-suggestions="querySearchAsync"
                    placeholder="请输入内容"
                    @select="handleSelect"
                  ></el-autocomplete>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品价格：">
                  <input
                    placeholder="请输入内容"
                    v-model="product.price"
                    class="sizes"
                    style="width: 200px"
                  />
                  <!--<el-select-->
                  <!--v-model="product.saleUnitId"-->
                  <!--clearable-->
                  <!--style="background:#eee; width: 110px; "-->
                  <!--placeholder="请选择"-->
                  <!--&gt;-->
                  <!--<el-option-->
                  <!--v-for="item in unitList"-->
                  <!--:key="item.id"-->
                  <!--:label="item.name"-->
                  <!--:value="item.id"-->
                  <!--&gt;</el-option>-->
                  <!--</el-select>-->
                  <span>{{selectSaleUnionName}}</span>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="装修单价：" label-width="120">
                  <el-input
                    placeholder="请输入内容"
                    v-model="product.decorationPrice"
                    class="input-with-select"
                    style="width: 200px"
                    @blur="verifydecorationPrice"
                  >
                    <template slot="append">{{product.valuationUnit}}</template>
                  </el-input>
                  <p v-show="this.decorationPriceWhos" class="verify">{{this.decorationPriceTxt}}</p>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品风格：">
                  <el-select
                    multiple
                    clearable
                    v-model="product.baseStyleId"
                    class="formWidth"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in baseProductStyle"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="产品规格：">
                  <el-input class="formWidth" clearable v-model="product.spec" maxlength="50"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12" style="position:relative;max-height: 70px;">
                <span class="star" style="position: absolute;color: #ff6419;top: 12px;left: 5px">*</span>
                <el-form-item label="贴图长度：">
                  <el-input
                    class="formWidth"
                    maxlength="6"
                    v-model="texture.fileWidth"
                    oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                  >
                    <template slot="append">cm</template>
                  </el-input>
                  <p v-show="lengthWhos" class="verify">{{productLengthTxt}}</p>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="position:relative;max-height: 70px;">
                <span class="star" style="position: absolute;color: #ff6419;top: 12px;left: 10px">*</span>
                <el-form-item label="贴图宽度：">
                  <el-input
                    class="formWidth"
                    maxlength="6"
                    v-model="texture.fileLength"
                    oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                  >
                    <template slot="append">cm</template>
                  </el-input>
                  <p v-show="widthWhos" class="verify">{{productWidthTxt}}</p>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="一石多面产品：" label-width="120">
                  <el-select v-model="product.isComplexParquet" style="width: 310px">
                    <el-option label="是" :value="1"></el-option>
                    <el-option label="否" :value="0"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="goods-operating-info">
              <el-col :span="24">
                <h2 class="title">
                  <span class="star" style="left: -20px">*</span>运营分类信息
                  <el-tooltip
                    class="item"
                    effect="dark"
                    content="运营分类主要是用于PC端、小程序、随选网产品筛选"
                    placement="top"
                  >
                    <div class="tip">？</div>
                  </el-tooltip>：
                </h2>
                <span @click="productCategoryTree">添加</span>
              </el-col>
            </el-row>
            <el-row
              class="goods-operating-infos"
              v-show="GoodsOperatingList"
              v-for="(item, index) in productClassList"
              :key="index"
              v-if="item.list.length > 0"
            >
              <div class="headline">{{item.name}}：</div>
              <div class="content">
                <p v-for="(items, index) in item.list" :key="index">{{items}}</p>
              </div>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="24">
                <div class="productPic">
                  <span>*</span>产品图片
                  <el-tooltip effect="dark" placement="right">
                    <div slot="content">
                      建议上传图片像素不超
                      <br />过2M,尺寸比例1:1
                    </div>
                    <el-button type="text" class="tooltip-btn">?</el-button>
                  </el-tooltip>：
                </div>
                <el-form-item>
                  <ul class="uploadPic">
                    <li>
                      <el-upload
                        class="avatar-uploader"
                        :action="BASE_URL.uploadUrl"
                        :file-list="fileList2"
                        :show-file-list="false"
                        :headers="headerToken"
                        :data="uploadParams"
                        list-type="picture-card"
                        multiple
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload"
                      >
                        <!--<i class="el-icon-plus avatar-uploader-icon"></i>-->
                        <div class="avatar-uploader-icon">
                          <div class="across"></div>
                          <div class="vertical"></div>
                        </div>
                        <p class="img-text">上传图片</p>
                      </el-upload>
                    </li>
                    <li
                      class="uploadImgList"
                      v-for="(file, index) in fileList2"
                      :key="index"
                      v-dragging="{ item: file, list: fileList2, group: 'file' }"
                    >
                      <span
                        class="posDelete"
                        v-show="showDeletes[index]"
                        @click="deleteUploadImg(index)"
                      ></span>
                      <img style="border-radius: 8px;" width="90" height="90" :src="file.url" />
                      <span v-show="file.id == defaultPicId" class="upload-mainpic__con">主缩略图</span>
                      <span
                        v-show="showDeletes[index]"
                        @click="setDefaultPic(file,index)"
                        class="upload-notmainpic__con"
                      >设置为缩略图</span>
                    </li>
                  </ul>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="材质(材料)：">
                  <el-select v-model="texture.texture" class="formWidth" placeholder="请选择">
                    <el-option
                      v-for="(item,index) in texturesDrop"
                      :key="index"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <!--prop="textureAttrValue"-->
                <el-form-item label="表面属性：">
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

            <el-row class="formFrameWidth">
              <el-col :span="24">
                <el-form-item label="产品描述：">
                  <el-input
                    type="textarea"
                    :rows="6"
                    maxlength="200"
                    placeholder="请输入内容"
                    v-model="product.desc"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row v-if="product.isComplexParquet==1">
              <el-form-item>
                <el-button
                  type="primary"
                  size="mini"
                  :disabled="addGroup.length > 39"
                  @click="addT"
                >增加材质贴图</el-button>
              </el-form-item>
            </el-row>

            <el-row class="addGroup" v-for="(item,index) in addGroup" :key="item.textureCode">
              <div
                @click="getIndex(index)"
                v-if="product.isComplexParquet==1||(product.isComplexParquet!=1&&index==0)"
              >
                <el-col :span="7">
                  <el-form-item>
                    <span>材质图片:</span>
                    <!--@deleteClick="deleteTextureImage"-->
                    <single-image-upload
                      style="display: inline-block"
                      :on-success="handleTextureImgSuccess"
                      :delete-image="deleteimg"
                      @deleteClick="item.thumbnailPath = ''"
                      :image-url="item.thumbnailPath"
                      :modelName="item.modelName"
                    ></single-image-upload>
                  </el-form-item>
                </el-col>

                <el-col :span="7">
                  <el-form-item>
                    <span>
                      法线贴图
                      <el-tooltip
                        class="item"
                        effect="dark"
                        content="法线贴图用于表现模型凹凸细节的贴图，使物体表面立体效果更加丰富"
                        placement="top"
                      >
                        <div class="tip">？</div>
                      </el-tooltip>:
                    </span>
                    <!--@deleteClick="deleteNormalImage"-->
                    <single-image-upload
                      @delete="parentDelete"
                      :on-success="handleNormalImgSuccess"
                      @deleteClick="item.normalPicPath = '';item.normalPicId=0"
                      :image-url="item.normalPicPath"
                    ></single-image-upload>
                  </el-form-item>
                </el-col>

                <el-col :span="4">
                  <el-form-item>
                    <span>
                      法线参数
                      <el-tooltip
                        class="item"
                        effect="dark"
                        content="法线凹凸纹理的深浅大小，如：1，2,3...."
                        placement="top"
                      >
                        <div class="tip">？</div>
                      </el-tooltip>:
                    </span>
                    <el-input
                      clearable
                      v-model="item.normalParam"
                      maxlength="6"
                      oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="1" class="last" v-if="addGroup.length > 1">
                  <b @click="deleteTexture(index)">
                    <b class="el-icon-delete"></b> 删除
                  </b>
                </el-col>
              </div>
            </el-row>

            <!--硬装产品-->
            <hardProduct
              @connectModel="hardConnectModel('edit')"
              @handleAddModelTexture="addModelTexture"
              v-if="isHard"
            ></hardProduct>

            <el-form-item style="margin-top: 20px">
              <el-button
                round
                class="confirmBtn marRiFght"
                type="primary"
                @click="submitForm('ruleForm')"
              >确定</el-button>
              <el-button class="cancelBtnBg" round @click="resetForm('ruleForm')">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </transition>
</template>
<script>
import ElRow from "element-ui/packages/row/src/row";
import { PointTwo } from "../../../filters/data";
import { PointNum } from "../../../filters/data";
import hardProductMinixs from "../../../filters/hardProductMinixs";
import hardProduct from "./conponents/hardProduct"; //硬装产品
import addHardProduct from "./conponents/addHardProduct"; //硬装产品
import setProductInfo from "./conponents/setProductInfo"; //硬装产品
import SingleImageUpload from "../../base/upload/SingleImageUpload.vue";
import qs from "qs";
import myVue from "@/filters/bus";
export default {
  components: {
    ElRow,
    hardProduct,
    addHardProduct,
    setProductInfo,
    SingleImageUpload
  },
  mixins: [hardProductMinixs],
  data() {
    return {
      imgRowIndex: "",
      isComplexParquet: 0,
      textrueList: [],
      addGroup: [
        {
          modelName: "material",
          textureId: 0,
          normalParam: "0",
          normalPicId: 0,
          thumbnailId: 0,
          normalPicPath: "",
          thumbnailPath: ""
        }
      ],

      nav: [{ nav: "产品库", url: "/product" }, { nav: "产品复制", url: "" }],
      headerToken: {
        Authorization: qs.parse(sessionStorage.getItem("loginUser")).token
      },
      page: 1,
      total: 0,
      smallType: "",
      restaurants: [],
      texturesDrop: [],
      textureAttrsDrop: [],
      textureId: "",
      state4: "",
      timeout: null,
      checked: true,
      modelList: {},
      infoClassifyVisible: false,
      isReset: false,
      GoodsOperatingList: [],
      productCategorylist1: [], // 产品分类总列表
      productCategorylist2: [], // 产品分类第2层列表
      productCategorylist3: [], // 产品分类第3层列表
      productCategorylist4: [], // 产品分类第4层列表
      productCategorylist5: [], // 产品分类第5层列表
      checkProductList: [], // 最终选择的产品分类列表
      productClassList: [], // 选择完产品分类，所需要展示在页面上的分类列表
      productClassLists: [], // 零时保存产品分类列表
      productClassIdLists: [], // 产品分类的ID列表
      indexList: { index1: 0, index2: 0, index3: 0, index4: 0, index5: 0 }, // 每一层分类的index
      showProductClassList: [], // 页面加载保存处理前的产品分类数据
      productHeightTxt: "",
      productWidthTxt: "",
      productLengthTxt: "",
      heightWhos: false,
      lengthWhos: false,
      widthWhos: false,
      showTextureDeletes: [],
      showDeletes: [],
      area: [],
      visible: {
        isModel: false,
        isTexture: false
      },
      decorationPrice: "",
      decorationPriceTxt: "",
      decorationPriceWhos: false,
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
      assModelTextureDia: {
        // 关联模型贴图的dialog的数据
        query: {
          page: 0,
          limit: 10,
          companyId: sessionStorage.getItem("companyID")
        },
        datas: [],
        tempSelectedModelTextures: [],
        areaId: 0,
        index: 0,
        dialogModelTextureVisible: false
      },
      assModelDia: {
        // 关联模型 模型列表窗口的数据
        dialogModelVisible: false,
        query: {
          page: 0,
          limit: 10,
          isUsed: 0,
          companyId: sessionStorage.getItem("companyID"),
          transStatus: "SUCCESS",
          modelType: ""
        },
        datas: []
      },
      assModelInfo: {
        // 关联模型信息
        modelName: "",
        modelCode: "",
        thumbPicPath: ""
      },
      assTextureDia: {
        dialogAddTextureVisible: false,
        texture: {
          id: 0
        },
        query: {
          companyId: sessionStorage.getItem("companyID")
        }
      },

      selectedProps: [], // 选中的属性
      editProID: "",
      uploadParams: {
        // 上传的信息
        platform: "web",
        module: "product",
        type: "image"
      },
      multipleSelection: [],
      selectedTextureId: "", // 材质ID
      selectStyleId: "", // 产品风格ID
      selectedSeriesId: "", // 产品系列ID
      selectedBrandId: "", // 产品品牌ID
      selectCategoryCode: "", // 产品分类编码
      unitList: [], // 数字字典
      input5: "",
      select: "",
      baseTextarea: "",
      prodTextarea: "",
      advicePriceInput1: "",
      advicePriceInput2: "",
      productCategoryDrop: [], // 获取分类下拉框
      productCategory: "",
      productSeries: [], // 产品系列
      seriesValue: "",
      productStyle: [], // 产品款式
      StyleValue: "",
      productTexture: [], // 产品材质
      TextureValue: "",
      baseProductStyle: [], // 产品风格
      brandValue: "",
      baseValue: "",
      TextureValue2: "", // 产品材质
      props: {
        // 产品分类属性名
        label: "name",
        value: "keyCode",
        disabled: "checkModel",
        children: "children"
      },
      getProductPorps: [], // 产品属性
      // 上传图片文件列表
      textureImageUrl: "",
      //normalImageUrl: '',
      imageUrl: "",
      fileList2: [],
      productBrand: [],
      productPropBO: [],
      textureAttrDrop: [],
      textureTextures: [],
      categoryCodeName: "",
      brandValueStr: "",
      product: {
        productSpec: "",
        categoryCodes: [],
        baseStyleId: []
      }, // 产品更新
      texture: {},
      // 品牌结束
      defaultPicId: 0,
      modelId: -1,
      TextureListData: [],
      ProductTextureDrop: [],
      selectSaleUnionName: "元",
      // 验证表单规则
      rules: {
        name: [
          { required: true, message: "请输入产品名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 50 个字符", trigger: "blur" }
        ],
        // productSeriesStr:[
        //     {required: true, message: '请选择产品系列', trigger: 'blur'},
        //     {min: 1, max: 40, message: '长度在 40 个字符', trigger: 'blur'}
        // ],
        modelNumber: [
          {
            required: true,
            message: "请选择产品型号",
            trigger: ["blur", "change"]
          },
          { min: 1, max: 40, message: "长度在 40 个字符", trigger: "blur" }
          //   { required: true, message: '请输入活动名称', trigger: 'blur' },
          //   { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        // categoryCodes: [
        //   {required: true, message: '请选择产品分类', trigger: 'change'}
        // ],
        brandId: [
          { required: true, message: "请选择产品品牌", trigger: "change" }
        ]
        // length:[
        //   { validator: verifyLength, trigger: 'blur' }
        // ],
        // width: [
        //   { validator: verifyWidth, trigger: 'blur' }
        // ],
        // height: [
        //   { validator: verifyHeight, trigger: 'blur' }
        // ]
      }
    };
  },
  mounted() {
    this.$store.commit("breadcrumb3", "产品编辑");
    this.$dragging.$on("dragged", ({ value }) => {
      //拖拽事件 触发
      this.defaultPicId = value.list[0].id;
    });
  },
  methods: {
    addT() {
      let ObjJSON = {
        modelName: "material",
        textureId: 0,
        normalParam: "0",
        normalPicId: 0,
        thumbnailId: 0,
        normalPicPath: "",
        thumbnailPath: ""
      };
      this.addGroup.push(ObjJSON);
    },
    deleteTexture(index) {
      this.addGroup.splice(index, 1);
    },
    verifydecorationPrice() {
      if (
        !PointTwo(this.product.decorationPrice) &&
        this.product.decorationPrice
      ) {
        this.decorationPriceTxt = "请输装修单价为正整数或小数点后两位数";
        this.decorationPriceWhos = true;
        this.product.decorationPrice = "";
      } else {
        this.decorationPriceWhos = false;
      }
    },
    changeUrl(url) {
      let arrUrl = url.split("//");
      let start = arrUrl[1].indexOf("/");
      let relUrl = arrUrl[1].substring(start); //stop省略，截取从start开始到结尾的所有字符

      if (relUrl.indexOf("?") != -1) {
        relUrl = relUrl.split("?")[0];
      }
      return relUrl;
    },
    deleteNormalImage() {
      this.normalImageUrl = "";
      this.normalPicId = "";
    },
    /* 上传图片 */
    getIndex(index) {
      this.imgRowIndex = index;
    },
    handleTextureImgSuccess(resp) {
      this.addGroup[this.imgRowIndex].thumbnailPath = resp.data.url;
      this.addGroup[this.imgRowIndex].thumbnailId = resp.data.resId;

      this.API.textureAttr().then(resp => {
        this.textureAttrsDrop = resp.data;
      });
    },
    parentDelete() {
      this.addGroup[this.imgRowIndex].normalPicPath = "";
      this.addGroup[this.imgRowIndex].normalPicId = 0;
    },
    deleteimg() {
      this.addGroup[this.imgRowIndex].thumbnailPath = "";
      this.addGroup[this.imgRowIndex].thumbnailId = "";
    },
    handleNormalImgSuccess(resp) {
      this.addGroup[this.imgRowIndex].normalPicPath = resp.data.url;
      this.addGroup[this.imgRowIndex].normalPicId = resp.data.resId;
    },
    deleteTextureImage() {
      this.textureImageUrl = "";
    },
    handleCurrentChange(val) {
      this.page = val;
      this.assModelTextureDia.query.page = val - 1;
      this.ListModelTexture();
    },
    handleSizeChange(val) {
      this.page = 1;
      this.assModelTextureDia.query.page = 0;
      this.assModelTextureDia.query.limit = val;
      this.ListModelTexture();
    },
    addBrandId() {
      this.selectedBrandId = this.product.brandId;
    },
    /*页面加载后把请求后没有处理过的数据（this.showProductClassList）处理成需要的结构，在页面创建前调用*/
    disposeProductClassList() {
      let arr = [];
      let idArr = [];
      for (let i = 0; i < this.showProductClassList.length; i++) {
        let obj = {};
        obj.name = this.showProductClassList[i].parentName;
        obj.list = [];
        arr.push(obj);
        idArr.push(this.showProductClassList[i].id);
      }
      for (let i = 0; i < arr.length; i++) {
        for (let j = i + 1; j < arr.length; j++) {
          if (arr[i].name == arr[j].name) {
            arr.splice(j, 1);
            j--;
          }
        }
      }
      for (let i = 0; i < this.showProductClassList.length; i++) {
        for (let j = 0; j < arr.length; j++) {
          if (this.showProductClassList[i].parentName == arr[j].name) {
            arr[j].list.push(this.showProductClassList[i].name);
          }
        }
      }
      this.productClassList = arr;
      this.checkProductList = idArr;
    },
    /*回显产品分类信息*/
    showProductCategory() {
      this.API.AddProductCategory().then(res => {
        console.log(res.data);
        if (res.data) {
          this.productCategorylist1 = res.data;
          let initArr = [];
          this.productCategorylist1.map((resInit, initIndex) => {
            if (resInit.code == "jiancai") {
              initArr.push(resInit);
            }
          });
          this.productCategorylist1 = initArr;

          this.productCategorylist1.map(res => {
            let firstArr = [];
            res.children.map((reschild, reschildIndex) => {
              if (reschild.code == "qiangm") {
                let arr = [];
                reschild.children.map((reschildren, index) => {
                  if (
                    reschildren.code == "qiangz" ||
                    reschildren.code == "qiangzhi" ||
                    reschildren.code == "tul" ||
                    reschildren.code == "qiangs" ||
                    reschildren.code == "qiangb" ||
                    reschildren.code == "yaox"
                  ) {
                    arr.push(reschildren);
                  }
                });
                this.productCategorylist1[0].children[
                  reschildIndex
                ].children = arr;
                firstArr.push(reschild);
              }
              if (reschild.code == "dim") {
                let arr = [];
                reschild.children.map((reschildren, index) => {
                  if (
                    reschildren.code == "diz" ||
                    reschildren.code == "dib" ||
                    reschildren.code == "dis" ||
                    reschildren.code == "tijx" ||
                    reschildren.code == "bodx"
                  ) {
                    arr.push(reschildren);
                  }
                });
                this.productCategorylist1[0].children[
                  reschildIndex
                ].children = arr;
                firstArr.push(reschild);
              }
            });
            this.productCategorylist1[0].children = firstArr;
          });

          if (this.checkProductList.length > 0) {
            for (let i = 0; i < this.productCategorylist1.length; i++) {
              for (
                let j = 0;
                j < this.productCategorylist1[i].children.length;
                j++
              ) {
                for (
                  let k = 0;
                  k < this.productCategorylist1[i].children[j].children.length;
                  k++
                ) {
                  if (
                    this.productCategorylist1[i].children[j].children[k]
                      .children
                  ) {
                    for (
                      let l = 0;
                      l <
                      this.productCategorylist1[i].children[j].children[k]
                        .children.length;
                      l++
                    ) {
                      if (
                        this.productCategorylist1[i].children[j].children[k]
                          .children[l].children
                      ) {
                        for (
                          let n = 0;
                          n <
                          this.productCategorylist1[i].children[j].children[k]
                            .children[l].children.length;
                          n++
                        ) {
                          if (
                            this.checkProductList[0] ==
                            this.productCategorylist1[i].children[j].children[k]
                              .children[l].children[n].id
                          ) {
                            this.productCategorylist5 = this.productCategorylist1[
                              i
                            ].children[j].children[k].children[l].children;
                            this.productCategorylist4 = this.productCategorylist1[
                              i
                            ].children[j].children[k].children;
                            this.productCategorylist3 = this.productCategorylist1[
                              i
                            ].children[j].children;
                            this.productCategorylist2 = this.productCategorylist1[
                              i
                            ].children;
                            this.indexList = {
                              index1: i,
                              index2: j,
                              index3: k,
                              index4: l,
                              index5: n
                            };
                            for (
                              let a = 0;
                              a < this.productCategorylist4.length;
                              a++
                            ) {
                              let arr = {
                                name: this.productCategorylist4[a].name,
                                list: []
                              };
                              this.productClassLists.push(arr);
                            }
                          }
                          for (
                            let t = 0;
                            t < this.checkProductList.length;
                            t++
                          ) {
                            if (
                              this.checkProductList[t] ==
                              this.productCategorylist1[i].children[j].children[
                                k
                              ].children[l].children[n].id
                            ) {
                              this.productClassLists[
                                this.indexList.index4
                              ].list.push(
                                this.productCategorylist1[i].children[j]
                                  .children[k].children[l].children[n].name
                              );
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            console.log(this.productClassLists);
          }
        }
      });
    },
    /*点击添加，弹出产品分类信息弹窗*/
    productCategoryTree() {
      this.infoClassifyVisible = true;
    },
    /*
     * 分类树结构点击筛选出所选择目录下的分类，
     * i: 所属分类树的层级；
     * index: 所选择当前分类层级下的下标；
     * 在i=3的时候（第三层级），创建一个数组this.productClassIdLists列表用于存储将要显示在页面上的品牌类和名称
     * 在i=5的是后（最后一层），把已选品牌下的种类名称添加到this.productClassIdLists下每个对应品牌下的list
     * */
    selectType(index, i) {
      if (i == 1) {
        if (this.indexList.index1 != index) {
          this.productCategorylist2 = [];
          this.productCategorylist3 = [];
          this.productCategorylist4 = [];
          this.productCategorylist5 = [];
          this.isReset = true;
        }
        this.indexList.index1 = index;
        this.productCategorylist2 = this.productCategorylist1[index].children;
      }
      if (i == 2) {
        if (this.indexList.index2 != index) {
          this.productCategorylist3 = [];
          this.productCategorylist4 = [];
          this.productCategorylist5 = [];
          this.isReset = true;
        }
        this.indexList.index2 = index;
        this.productCategorylist3 = this.productCategorylist2[index].children;
      }
      if (i == 3) {
        console.log(this.productCategorylist3[index]);
        if (this.indexList.index3 != index) {
          this.productCategorylist4 = [];
          this.productCategorylist5 = [];
          this.isReset = true;
        }
        this.indexList.index3 = index;
        this.productCategorylist4 = this.productCategorylist3[index].children;
      }
      if (i == 4) {
        console.log(this.indexList.index4, index);
        if (this.indexList.index4 != index) {
          this.productCategorylist5 = [];
        }
        this.indexList.index4 = index;
        this.productCategorylist5 = this.productCategorylist4[index].children;
      }
      if (i == 5) {
        this.productClassIdLists = [];
        if (this.isReset) {
          this.productClassLists = [];
          this.checkProductList = [];
          this.isReset = false;
        }
        console.log(this.checkProductList);
        console.log(this.indexList.index3);
        // if (this.indexList.index3) {}
        for (let j = 0; j < this.productCategorylist4.length; j++) {
          let arr = { name: this.productCategorylist4[j].name, list: [] };
          this.productClassLists.push(arr);
        }
        for (let j = 0; j < this.checkProductList.length; j++) {
          this.productClassIdLists.push(this.checkProductList[j].id);
          if (this.productCategorylist5[index].id == this.checkProductList[j]) {
            this.productClassLists[this.indexList.index4].list.push(
              this.productCategorylist5[index].name
            );
            return;
          }
        }
        let k = this.productClassLists[this.indexList.index4].list.indexOf(
          this.productCategorylist5[index].name
        );
        this.productClassLists[this.indexList.index4].list.splice(k, 1);
      }
    },
    /*分类树赛选完后点击确认*/
    verifyProductClass() {
      this.productClassList = this.productClassLists;
      this.infoClassifyVisible = false;
    },

    /*点击添加，弹出产品分类信息弹窗*/
    productCategoryTree() {
      this.infoClassifyVisible = true;
      this.API.AddProductCategory().then(res => {
        if (res.data) {
          this.productCategorylist1 = res.data;

          let initArr = [];
          this.productCategorylist1.map((resInit, initIndex) => {
            if (resInit.code == "jiancai") {
              initArr.push(resInit);
            }
          });
          this.productCategorylist1 = initArr;

          this.productCategorylist1.map(res => {
            let firstArr = [];
            res.children.map((reschild, reschildIndex) => {
              if (reschild.code == "qiangm") {
                let arr = [];
                reschild.children.map((reschildren, index) => {
                  if (
                    reschildren.code == "qiangz" ||
                    reschildren.code == "qiangzhi" ||
                    reschildren.code == "tul" ||
                    reschildren.code == "qiangs" ||
                    reschildren.code == "qiangb" ||
                    reschildren.code == "yaox"
                  ) {
                    arr.push(reschildren);
                  }
                });
                this.productCategorylist1[0].children[
                  reschildIndex
                ].children = arr;
                firstArr.push(reschild);
              }
              if (reschild.code == "dim") {
                let arr = [];
                reschild.children.map((reschildren, index) => {
                  if (
                    reschildren.code == "diz" ||
                    reschildren.code == "dib" ||
                    reschildren.code == "dis" ||
                    reschildren.code == "tijx" ||
                    reschildren.code == "bodx"
                  ) {
                    arr.push(reschildren);
                  }
                });
                this.productCategorylist1[0].children[
                  reschildIndex
                ].children = arr;
                firstArr.push(reschild);
              }
            });
            this.productCategorylist1[0].children = firstArr;
          });
        }
      });
    },
    // querySearchSeries(queryString,cb){
    // this.API.ProductSeriesDrop().then((res) => {
    //   if(res.data.length){
    //       console.log(res.data, 'this.productSeries')
    //             let productSeries = this.productSeries;
    //             let results = queryString ? productSeries.filter(this.createStateFilter(queryString)) : productSeries;
    //             clearTimeout(this.timeout);
    //             this.timeout = setTimeout(() => {
    //                 cb(res.data);
    //             }, 1000 * Math.random());
    //   }

    // })
    // },

    querySearchAsync(queryString, cb) {
      this.API.modelNumberRecommand({
        modelNumber: this.product.modelNumber,
        brandId: this.selectedBrandId,
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        if (res.data.length) {
          //console.log(res.data, "this.recommandData");
          let restaurants = this.restaurants;
          let results = queryString
            ? restaurants.filter(this.createStateFilter(queryString))
            : restaurants;
          clearTimeout(this.timeout);
          this.timeout = setTimeout(() => {
            cb(res.data);
          }, 1000 * Math.random());
        } else {
          setTimeout(() => {
            cb([{ value: "暂无数据" }]);
          }, 1000 * Math.random());
        }
      });
    },
    createStateFilter(queryString) {
      return state => {
        return (
          state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        );
      };
    },
    handleSelect(item) {
      //this.$set(this.product, 'modelNumber', item.value)
    },

    getRecommand() {
      this.API.modelNumberRecommand({
        modelNumber: this.ruleForm.productTypeCodeStr,
        brandId: this.selectedBrandId,
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        if (res.data.length) {
          console.log(res.data, "this.recommandData");
          this.recommandData = res.data;
        }
      });
    },

    loadAll() {
      return this.recommandData;
    },
    verifyLength() {
      console.log(this.product.length);
      if (!PointTwo(this.product.length)) {
        this.productLengthTxt = "请输产品长度为正整数或小数点后两位数";
        this.product.length = "";
        this.lengthWhos = true;
      } else {
        this.lengthWhos = false;
      }
    },
    verifyWidth() {
      // let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
      // if (!i.test(this.product.width)) {
      //   this.productWidthTxt = '请输产品宽度为正整数或小数点后两位数';
      //   this.widthWhos = true;
      //    this.product.width = '';
      // } else {
      //   this.widthWhos = false;
      // }
      if (!PointTwo(this.product.width)) {
        this.productWidthTxt = "请输产品宽度度为正整数或小数点后两位数";
        this.product.width = "";
        this.widthWhos = true;
      } else {
        this.widthWhos = false;
      }
    },
    verifyHeight() {
      //  let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
      //   if (!i.test(this.product.height)) {
      //     this.productHeightTxt = '请输产品高度为正整数或小数点后两位数';
      //     this.heightWhos = true;
      //     this.product.height = '';
      //   } else {
      //     this.heightWhos = false;
      //   }
      if (!PointTwo(this.product.height)) {
        this.productHeightTxt = "请输产品高度为正整数或小数点后两位数";
        this.product.height = "";
        this.heightWhos = true;
      } else {
        this.heightWhos = false;
      }
    },
    resetTextrue() {
      this.assModelTextureDia.query = {
        page: 0,
        limit: 10,
        companyId: sessionStorage.getItem("companyID")
      };
    },
    deleteAreaTexture(item, j) {
      item.areaTextures.splice(j, 1);
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    selectedChk(id) {
      return this.assModelTextureDia.tempSelectedModelTextures.includes(id);
    },
    // 关联贴图
    handleReAssTexture() {
      this.assTextureDia.dialogAddTextureVisible = true;
      this.assTextureDia.query.queryType = "success";
      this.API.ListTexture(this.assTextureDia.query).then(res => {
        this.TextureListData = res.list;
      });
    },
    // 选用贴图
    handleAssocateTextureConfirm(row) {
      this.assTextureDia.dialogAddTextureVisible = false;
      this.assTextureDia.query.texture = "";
      this.assTextureDia.query.textureAttr = "";
      this.assTextureDia.query.textureType = "";
      this.assTextureDia.query.textureName = "";
      this.assTextureDia.query.modelNumber = "";
      this.assTextureDia.query.textureCode = "";
      this.product.texture = {
        name: row.textureName,
        code: row.textureCode,
        picPath: row.thumbnailPath,
        id: row.textureId
      };
      console.log(this.product.texture);
      this.visible.isTexture = true;
    },
    beforeClose() {
      this.assTextureDia.dialogAddTextureVisible = false;
      this.assTextureDia.query.texture = "";
      this.assTextureDia.query.textureAttr = "";
      this.assTextureDia.query.textureType = "";
      this.assTextureDia.query.textureName = "";
      this.assTextureDia.query.modelNumber = "";
      this.assTextureDia.query.textureCode = "";
    },
    modelClose() {
      this.assModelTextureDia.dialogModelTextureVisible = false;
      this.assModelTextureDia.query.textureAttr = "";
      this.assModelTextureDia.query.texture = "";
      this.assModelTextureDia.query.textureName = "";
      this.assModelTextureDia.query.textureCode = "";
      this.assModelTextureDia.query.textureType = "";
      this.assModelTextureDia.query.modelNumber = "";
    },
    // 材质材料
    getProductTextureDrop() {
      this.API.textureCaiLiao().then(res => {
        this.ProductTextureDrop = res.data;
      });
    },
    // 上传贴图搜索
    searchTexture() {
      this.assTextureDia.query.queryType = "success";
      this.API.ListTexture(this.assTextureDia.query).then(res => {
        this.TextureListData = res.list;
      });
    },
    // 移除贴图
    handleRemoveTexture() {
      this.product.texture = {};
      this.assTextureDia.texture = {};
      this.visible.isTexture = true;
    },
    // 移除模型
    handleRemoveModel() {
      this.visible.isModel = true;
      this.assModelInfo = {};
      this.area = [];
      this.modelId = -1;
    },
    //       handleModelTextureAddConfirm() { // click the model texture button an then invoke this method
    //         /* this.assModelTextureDia.dialogModelTextureVisible = false
    //         let tempSelectedModelTextures = this.assModelTextureDia.tempSelectedModelTextures
    // //        this.area[this.modelTexture.index].isDefault = 1
    //         this.area[this.assModelTextureDia.index].areaTextures = this.assModelTextureDia.datas
    //           .filter(it => tempSelectedModelTextures.includes(it.textureId))
    //           .map(it => Object.assign(it, {id: it.textureId, thumbnailPath: it.thumbnailPath, affectPrice: 0})) */
    //         // 把选择的贴图列表放入到区域的地方
    //         this.assModelTextureDia.dialogModelTextureVisible = false;
    //         this.area[this.assModelTextureDia.index].areaTextures = this.multipleSelection.map(it => ({
    //           id: it.textureId,
    //           thumbnailPath: it.thumbnailPath,
    //           affectPrice: 0,
    //           deleteBtnVisible: false
    //         }));
    //         /* this.area[this.assModelTextureDia.index].areaTextures = this.assModelTextureDia.datas
    //            .filter(it => tempSelectedModelTextures.includes(it.textureId))
    //            .map(it => Object.assign(it, {id: it.textureId, thumbnailPath: it.thumbnailPath, affectPrice: 0})) */
    //       },
    handleModelTextureAddConfirm() {
      if (sessionStorage.getItem("addProductType") == "soft") {
        //之前编辑软装的逻辑 不改动
        this.assModelTextureDia.dialogModelTextureVisible = false;
        this.area[
          this.assModelTextureDia.index
        ].areaTextures = this.multipleSelection.map(it => ({
          id: it.textureId,
          thumbnailPath: it.thumbnailPath,
          affectPrice: 0,
          deleteBtnVisible: false
        }));
      } else {
        //硬装产品逻辑
        this.$store.dispatch("addTextTure", this.multipleSelection);
        this.assModelTextureDia.dialogModelTextureVisible = false;

        this.gettersHardModelList.map((item, index) => {
          item.modelTextureInfo.map((res, childIndex) => {
            if (res.areaId == this.gettersAreaId) {
              //res.rels = [...res.rels,...this.multipleSelection];

              res.rels.push(...this.multipleSelection);
              //添加一个图片地址属性
              res.rels.map(pic => {
                if (pic.thumbnailPath) {
                  pic.picPath = pic.thumbnailPath;
                }
              });
            }
          });
        });
        this.$store.dispatch("setHardModel", this.gettersHardModelList);
        console.log(this.gettersHardModelList);
      }
    },
    handleReAssModelConfirm(row) {
      // 重新关联模型确认
      if (row.modelId) {
        this.modelId = row.modelId;
        this.assModelInfo = row;
        this.modelList = row;
        //           this.API.modelTextureDetail({
        //             modelId: row.modelId
        //           }).then((res) => {
        //
        //             console.log(res.data);
        //             this.area = res.data.map(it => {
        //                 console.log(it);
        //                 Object.assign({}, it, {
        //                 areaTextures: it.rels.map(tit => Object.assign({}, {
        //                   thumbnailPath: tit.picPath,
        //                  deleteBtnVisible: false
        //                 }, tit))
        //               })}
        //             )
        //             this.assModelDia.query.modelCode = ''
        //             this.assModelDia.query.author = ''
        //             this.assModelDia.query.modelName = ''
        // //            this.relateModelInfo.isShow = true
        //           })
        this.assModelDia.dialogModelVisible = false;
      }
    },
    handleClose() {
      this.assModelDia.dialogModelVisible = false;
      this.assModelDia.query.modelCode = "";
      this.assModelDia.query.author = "";
      this.assModelDia.query.modelName = "";
    },
    handleReAssoscateModel() {
      // 点击重新关联模型
      this.assModelDia.dialogModelVisible = true;
      this.assModelDia.query.categoryCodes = this.selectCategoryCode;
      this.API.ListModel(this.assModelDia.query).then(res => {
        this.assModelDia.datas = res.list;
      });
    },
    handleAddModelTexture(area, index) {
      // 点击添加区域贴图
      this.assModelTextureDia.dialogModelTextureVisible = true;
      this.assModelTextureDia.areaId = area.id;
      this.assModelTextureDia.index = index;
      this.ListModelTexture();
      //        this.area[index].push({price: 10, defaultThumb: true})
    },
    ListModelTexture(type) {
      // 获取模型材质
      type == 1 ? (this.assModelTextureDia.query.page = 0) : "";
      this.assModelTextureDia.query.queryType = "success";
      this.API.ListTexture(this.assModelTextureDia.query).then(res => {
        this.assModelTextureDia.datas = res.list;
        this.total = res.total;
        console.log(res.list);
      });
    },
    selectPropValues(val, index, item) {
      // 选产品属性
      this.productPropBO[index] = val;
      this.productPropBO[index].parent = {
        code: item.code,
        id: item.id,
        name: item.name
      };
    },
    // 设置为主缩略图
    setDefault(imgs) {},
    // 获取产品属性
    // getProductProps() {
    //   this.selectedProps = []
    //   if (this.selectCategoryCode) {
    //     this.API.ProductProps({
    //       code: this.selectCategoryCode
    //     }).then((res) => {
    //       this.getProductPorps = res.data
    //     })
    //   }
    // },
    // 上传图片
    getProduct() {
      this.API.ProductDetail({
        productId: this.editProID,
        platformType: "library"
      }).then(res => {
        this.textureId = res.data.materialPicIds;
        this.product.isComplexParquet = res.data.isComplexParquet;
        this.getDetail();
        if (res) {
          // 硬装部分需要加的
          if (res.data.hardProductModelInfo) {
            let toatalProps = [];
            this.$store.dispatch("setHardModel", res.data.hardProductModelInfo); //赋值 setPropsId
            res.data.hardProductModelInfo.map(item => {
              //回显已选择的规格
              let propIdsBox = [];
              // propIdsBox.push({propIds:})
              item.props.map(prop => {
                propIdsBox.push(prop.id);
              });
              toatalProps.push({ propIds: propIdsBox, modelId: item.id });
            });
            console.log(toatalProps);
            this.$store.dispatch("setPropsId", toatalProps);
            this.$store.dispatch("setHardModel", res.data.hardProductModelInfo);
            console.log(res.data.hardProductModelInfo);
          }

          //---------------------------------------
          //this.smallType=res.data.smallType;
          this.selectCategoryCode = res.data.smallType;
          this.selectedBrandId = res.data.brandId;
          this.brandValueStr = res.data.brandId;
          this.visible.isModel = res.data.checkIsModel;
          this.modelList = res.data.model;
          this.getProductPorps = res.data.props;
          this.showProductClassList = res.data.categoryNodes;
          this.disposeProductClassList();
          this.getSelectSaleUnionName(res.data.smallType).then(filter => {
            this.selectSaleUnionName = filter.length
              ? filter[filter.length - 1].name
              : this.unitList[0].name;
          });
          // this.selectCategoryCode = res.data.categoryCodes[res.data.categoryCodes.length - 1];
          this.categoryCodeName = res.data.attrNames;
          this.assModelDia.query.modelType = res.data.smallType;
          res.data.brandId = res.data.brandId ? parseInt(res.data.brandId) : "";
          res.data.seriesId = res.data.seriesId
            ? parseInt(res.data.seriesId)
            : "";
          res.data.modelingId = res.data.modelingId
            ? parseInt(res.data.modelingId)
            : "";
          res.data.proStyleId = res.data.proStyleId
            ? parseInt(res.data.proStyleId)
            : "";
          res.data.saleUnitId = res.data.saleUnitId
            ? parseInt(res.data.saleUnitId)
            : "";
          res.data.baseStyleId = res.data.baseStyleId
            ? res.data.baseStyleId
            : [];
          this.product = res.data;
          console.log(res.data, " res data");
          if (this.product.model) {
            this.modelId = this.product.model.modelId;
            this.assModelInfo = this.product.model;
          }
          this.assTextureDia.texture = this.product.texture;
          if (this.product.defaultPicId) {
            this.defaultPicId = this.product.defaultPicId;
          }
          // if (res.data.modelTextureInfo) {
          //   this.area = res.data.modelTextureInfo.map(it =>
          //     Object.assign({}, it, {
          //       areaTextures:
          //         it.areaTextures &&
          //         it.areaTextures.map(tit =>
          //           Object.assign(
          //             {},
          //             {
          //               thumbnailPath: tit.picPath,
          //               deleteBtnVisible: false
          //             },
          //             tit
          //           )
          //         )
          //     })
          //   );
          // }
          // this.getProductPorps = res.data.props;
          this.$nextTick(() => {
            if (this.product.props) {
              this.selectedProps = this.product.props.map((item, index) => ({
                id: item.id
              }));
              this.productPropBO = this.product.props;
              console.log(this.productPropBO);
            }
          });
          // this.fileList2 = res.data.picInfos.map(res => {
          //   return {
          //     url: res.path,
          //     id: res.id
          //   };
          // });

          //获取硬装规格信息
          this.API.ProductProps({
            code: this.selectCategoryCode
          }).then(res => {
            this.$store.dispatch("saveProductPorps", res.data);
          });
        }
      });
    },
    getSelectSaleUnionName(smallType) {
      return new Promise((resolve, reject) => {
        let filter = this.unitList.filter(e => {
          if (e.suitableCodes) return e.suitableCodes.indexOf(smallType) != -1;
        });
        resolve(filter);
      });
    },
    // 材质选择ID
    selectedTexture(val) {
      this.selectedTextureId = val;
    },
    // 产品款式选择ID
    selectStyleID(val) {
      this.selectStyleId = val;
    },
    // 产品系列选择ID
    selectedSeriesID(val) {
      this.selectedSeriesId = val;
    },
    // 品牌选择ID
    selectedBrandID(val) {
      this.product.brandId = val;
      this.selectedBrandId = val;
    },
    // 产品分类编号
    // selectCategoryNO(val) {
    //       console.log(this.product.categoryCodes);
    //   // this.selectCategoryCode = val[val.length - 1]
    //   this.productPropBO = []
    //   this.API.modelTexture({
    //     code: this.selectCategoryCode
    //   }).then((res) => {
    //     this.visible.isModel = res.data == 1
    //     this.visible.isTexture = res.data == 2
    //   })
    //   this.getProductProps()
    // },
    /* 上传图片 */
    handleAvatarSuccess(res, file) {
      // this.imageUrl = URL.createObjectURL(file.raw);
      this.fileList2.push({
        url: res.data.url,
        id: res.data.resId
      });
      if (this.fileList2) {
        if (this.fileList2[1]) {
          this.defaultPicId = this.defaultPicId;
        } else if (this.fileList2[0]) {
          this.defaultPicId = this.fileList2[0].id;
        }
      }
    },
    handleChange(file, fileList) {},
    deleteUploadImg(index) {
      this.fileList2.splice(index, 1);
      this.defaultPicId = "";
    },
    showTextureDeleteEnter(index) {
      console.log(index);
      this.$set(this.showTextureDeletes, index, true);
      console.log(this.showTextureDeletes, "dd");
    },
    showTextureDeleteOut(index) {
      this.$set(this.showTextureDeletes, index, false);
      console.log(this.showTextureDeletes, "222");
    },
    showDelete(index) {
      this.$set(this.showDeletes, index, true);
    },
    showDelete2(index) {
      this.$set(this.showDeletes, index, false);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isPNG = file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 10;

      if (!isJPG && !isPNG) {
        this.$message.error("上传图片只能是 JPG 或 PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("图片大小不能超过 10MB!");
      }
      return (isJPG || isPNG) && isLt2M;
    },

    /* 上传图片 结束 */
    // 产品系列下拉框
    getProductSeriesList() {
      this.API.ProductSeriesDrop().then(res => {
        this.productSeries = res.data;
      });
    },
    setDefaultPic(file, index) {
      this.defaultPicId = file.id;
      this.fileList2.splice(index, 1);
      this.fileList2.unshift(file);
    },
    textureAttrDropList() {
      this.API.textureAttr().then(res => {
        this.textureAttrDrop = res.data;
      });
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
    getDetail() {
      // materialPicIds
      let tid = this.textureId.split(",");
      if (tid) {
        tid.map((res, index) => {
          if (index > 0 && this.product.isComplexParquet != 1) return;
          this.API.GetTexture(res).then(resp => {
            // console.log(resp, "details.....................");
            resp.data.textureId = res;
            resp.data.texture && resp.data.texture != 0
              ? (resp.data.texture = parseInt(resp.data.texture))
              : (resp.data.texture = "");
            this.texture = resp.data;
            resp.data.modelName = "material";
            resp.data.thumbnailPath.indexOf("http") > -1
              ? ""
              : (resp.data.thumbnailPath =
                  this.BASE_URL.sourceBaseUrl + resp.data.thumbnailPath); //是否有域名
            this.textrueList.push(resp.data);
            //this.addGroup = this.textrueList;
            //console.log(this.addGroup, "addGroup addGroup");
          });
        });
      }
    },
    textureTexturesDrop() {
      this.API.textureCaiLiao().then(res => {
        this.textureTextures = res.data;
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let modelTextureInfos = [];
          // this.area.forEach(it => {
          //   it.areaTextures &&
          //     it.areaTextures.forEach((tit, index) => {
          //       modelTextureInfos.push({
          //         affectPrice: tit.affectPrice,
          //         areaId: it.id,
          //         isDefault: it.isDefault === index ? 1 : 0,
          //         textureId: tit.id
          //       });
          //     });
          // });
          let that = this;

          let formDatas = {
            categoryIds: that.checkProductList, // 运营分类ID
            advicePrice: that.product.advicePrice, //  (number, optional): 建议售价 ,
            modelingId: that.product.modelingId, //  (integer, optional): 产品款式 ,
            brandId: that.product.brandId, //  (integer): 品牌ID ,
            productSmallType: that.assModelDia.query.modelType,
            categoryCode: that.assModelDia.query.modelType, // this.product.categoryCodes[2], //  (string): 所属分类编码 ,
            code: "", //  (string): 产品编码 ,
            desc: that.product.desc, //  (string, optional): 产品描述 ,
            addProductType: "soft", //  (string, optional): 编辑类型, ,
            length: that.product.length, // 长
            width: that.product.width, // 宽
            height: that.product.height, // 高
            userId: sessionStorage.getItem("userId"),
            modelNumber: that.product.modelNumber,
            name: that.product.name, //  (string): 产品名称 ,
            price: that.product.price, //  (number, optional): 售价 ,
            decorationPrice: that.product.decorationPrice
              ? that.product.decorationPrice
              : "", //  装修单价 ,
            saleUnionId: that.product.saleUnitId, //  (integer, optional): 售价单位 ,
            seriesId: that.product.seriesId, //  (integer, optional): 产品系列 ,
            baseStyleId: that.product.baseStyleId, //  (integer, optional): 产品风格,
            picIds: that.fileList2.map(item => item.id).join(","),
            defaultPicId: that.defaultPicId,
            props: that.productPropBO, // 产品属性
            productSpec: that.product.spec,
            isCreatedTexture: 1,
            isComplexParquet: this.product.isComplexParquet
          };
          let formTextureData = {
            companyId: sessionStorage.getItem("companyID"),
            /*textureId : this.textureId,*/
            fileLength: this.texture.fileLength,
            fileWidth: this.texture.fileWidth,
            texture: this.texture.texture ? this.texture.texture : 0,
            textureAttrValue: this.texture.textureAttrValue,
            /*normalParam : this.texture.normalParam,
                            thumbnailPath : this.textureImageUrl.replace(this.BASE_URL.sourceBaseUrl, ''),
                            normalPicId : this.normalPicId,
                            thumbnailId : this.textureImageId,*/
            texturePicInfos: this.addGroup
          };

          for (let key in formDatas) {
            if (
              formDatas[key] == "" ||
              formDatas[key] == undefined ||
              formDatas[key] == null
            ) {
              delete formDatas[key];
            }
          }
          //校验
          if (
            this.texture.fileLength == "" ||
            this.texture.fileLength == null ||
            this.texture.fileLength == undefined
          ) {
            this.$message.error("请填写宽度");
            return;
          }
          if (
            this.texture.fileWidth == "" ||
            this.texture.fileWidth == null ||
            this.texture.fileWidth == undefined
          ) {
            this.$message.error("请填写长度");
            return;
          }
          if (!formDatas.categoryIds) {
            this.$message.error("请填写运营分类信息");
            return;
          }
          if (this.fileList2.length == 0) {
            this.$message.error("请上传产品图片");
            return;
          }
          // if(this.textureImageUrl==""||this.textureImageUrl==null||this.textureImageUrl==undefined){  this.$message.error('请上传贴图图片');return;}

          if (this.addGroup) {
            let flags = false;
            this.addGroup.map(res => {
              if (
                res.thumbnailPath == "" ||
                res.thumbnailPath == null ||
                res.thumbnailPath == undefined
              ) {
                flags = true;
              }
            });
            if (flags) {
              this.$message.error("请上传贴图图片");
              return;
            }
          }

          this.$confirm("确定复制产品", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            cancelButtonClass: "cancelButtonClass",
            confirmButtonClass: "confirmButtonClass",
            type: "info",
            center: true
          })
            .then(() => {
              this.addGroup &&
                this.addGroup.map(res => {
                  res.thumbnailPath = this.changeUrl(res.thumbnailPath);
                });
              let formDatass = {
                productAdd: formDatas,
                resTextureAdd: formTextureData
              };
              let jsonData = JSON.stringify(formDatass);
              console.log(this.addGroup, "this.addGroup ddd");
              if (sessionStorage.getItem("addProductType") == "soft") {
                //之前的逻辑 软装
                this.API.AddProductTexture(jsonData).then(res => {
                  if (res) {
                    this.$message.success("复制产品成功");
                    this.$router.push("/product");
                  }
                });
              } else {
                //现在的硬装 逻辑
                this.getHardParam();
                formDatas.productSKUInfos = this.gettersSKUInfos; // SKU信息
                this.API.AddProductTexture(formDatas).then(res => {
                  if (res) {
                    this.$message.success("复制产品成功");
                    this.$router.push("/product");
                  }
                });
              }
            })
            .catch(() => {
              //this.$router.push('/product')
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      // this.$refs[formName].resetFields();
      this.$router.push("/product");
      this.$refs[formName].resetFields();
    },
    // submitForm() {
    //   let modelTextureInfos = []
    //   this.area.forEach(it => {
    //     it.areaTextures.forEach((tit, index) => {
    //       modelTextureInfos.push(
    //         {
    //           affectPrice: tit.affectPrice,
    //           areaId: it.id,
    //           isDefault: it.isDefault === index ? 1 : 0,
    //           textureId: tit.id
    //         }
    //       )
    //     })
    //   })
    //   let that = this
    //   let formDatas = {
    //     categoryIds: this.productClassIdLists,
    //     advicePrice: that.product.advicePrice, //  (number, optional): 建议售价 ,
    //     modelingId: that.product.modelingId, //  (integer, optional): 产品款式 ,
    //     brandId: that.product.brandId, //  (integer): 品牌ID ,
    //     // categoryCode: that.selectCategoryCode, // this.product.categoryCodes[2], //  (string): 所属分类编码 ,
    //     code: that.product.code, //  (string): 产品编码 ,
    //     desc: that.product.desc, //  (string, optional): 产品描述 ,
    //     editType: 'libraryEdit', //  (string, optional): 编辑类型, ,
    //     length: that.product.length, // 长
    //     width: that.product.width, // 宽
    //     height: that.product.height, // 高
    //     id: that.editProID, //  (integer): 产品id ,
    //     userId: sessionStorage.getItem('userId'),
    //     modelNumber: that.product.modelNumber,
    //     name: that.product.name, //  (string): 产品名称 ,
    //     price: that.product.price, //  (number, optional): 售价 ,
    //     saleUnionId: that.product.saleUnitId, //  (integer, optional): 售价单位 ,
    //     seriesId: that.product.seriesId, //  (integer, optional): 产品系列 ,
    //     baseStyleId: that.product.baseStyleId, //  (integer, optional): 产品风格,
    //     modelTextureInfos: modelTextureInfos,
    //     modelId: that.modelId,
    //     picIds: that.fileList2.map(item => item.id).join(','),
    //     defaultPicId: that.defaultPicId,
    //     textureId: that.assTextureDia.texture ? (that.assTextureDia.texture.id ? that.assTextureDia.texture.id : -1) : -1,
    //     props: that.productPropBO // 产品属性
    //   }
    //   for (let key in formDatas) {
    //     if (formDatas[key] == '' || formDatas[key] == undefined || formDatas[key] == null) {
    //       delete formDatas[key]
    //     }
    //   }
    //   this.$confirm('确定更新产品', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     cancelButtonClass: 'cancelButtonClass',
    //     confirmButtonClass: 'confirmButtonClass',
    //     type: 'info',
    //     center: true
    //   }).then(() => {
    //     this.API.UpdateProductLibrary(formDatas).then((res) => {
    //       if (res) {
    //         this.$message.success('更新产品成功')
    //         this.$router.push('/product')
    //       }
    //     })
    //   }).catch(() => {
    //     this.$router.push('/product')
    //   })
    // },
    // resetForm(formName) {
    //   this.$router.push('/product')
    //   this.$refs[formName].resetFields()
    // },
    // 产品分类下拉框
    // getProductCategory() {
    //   // this.API.ProductCategory().then((res) => {
    //   //   this.productCategoryDrop = res.data
    //   // })
    //     this.API.ProductCategory().then((res) => {
    //         this.productCategoryDrop = res.data
    //         console.log(res.data, ' category')
    //     })
    // },
    // 获取产品款式下拉框
    getProductStyleDrop() {
      this.API.ProductStyleDrop().then(res => {
        this.productStyle = res.data;
      });
    },
    // 获取产品品牌下拉框
    getProductBrand() {
      this.API.ProductBrandDrop({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        this.productBrand = res.data;
      });
    },
    // 产品风格下拉框
    getBaseProductStyleDrop() {
      this.API.BaseProductStyleDrop().then(res => {
        this.baseProductStyle = res.data;
      });
    },
    // 获取数字字典，常用单位
    getDisctionaryUnit() {
      this.API.DisctionaryUnit().then(res => {
        this.unitList = res.data;
      });
    }
  },
  created() {
    this.editProID = this.$route.params.proID;
    this.showProductCategory();
    this.getProductBrand();
    this.getProductSeriesList();
    this.getProductStyleDrop();
    this.getBaseProductStyleDrop();
    this.getDisctionaryUnit();
    this.getProduct();
    this.textureTexturesDrop();
    this.getTexturesDrop();
    this.getTextureAttrsDrop();
  }
  // watch: {
  //   'product.categoryCodes': {
  //     handler: function (val, oldVal) {
  //       this.selectCategoryNO(val)
  //     }
  //   }
  // }
};
</script>

<style lang="scss" scoped>
@import "../../../assets/css/mixin";
.addGroup {
  margin-left: 100px;
  margin-bottom: 30px;
  padding-left: 10px;
  width: 1100px;
  background: #f5f5f5;
  position: relative;
  .el-col {
    width: 330px;
    height: 130px;
    &.last {
      width: 60px;
      height: 24px;
      top: 55px;
      position: absolute;
      right: 0;
      cursor: pointer;
    }
  }
  span {
    position: absolute;
    top: 45px;
    left: -100px;
  }
  .el-input {
    margin-top: 45px;
    //margin-left: -30px;
  }
}
.selectRadio {
}

.textureSearch {
  .el-col {
    margin-bottom: 15px;
  }
  .el-input,
  .el-select {
    width: 100%;
  }
}
.verify {
  color: red;
}
.mainPic {
  width: 56px;
  position: absolute;
  top: 4px;
  left: 0;
  padding-left: 6px;
  text-align: left;
  height: 22px !important;
  line-height: 22px;
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
  z-index: 111;
  color: #fff;
  font-size: 12px;
  background: #ff3f3f;
}
.info-ypop-up {
  .el-col-2 {
    height: 36px;
    line-height: 36px;
    span {
      display: inline-block;
      width: 100%;
      text-align: center;
      cursor: pointer;
      color: #ff6419;
    }
  }
  .Classify-list {
    border-top: 1px solid #dddddd;
    border-right: 1px solid #dddddd;
    .el-col-6 {
      overflow: auto;
      height: 360px;
      border-bottom: 1px solid #dddddd;
      border-left: 1px solid #dddddd;
      .el-checkbox {
        display: block;
        margin: 0;
        padding: 10px 20px;
        height: 12px;
        line-height: 12px;
        color: #000000;
      }
      p {
        padding: 10px 20px;
        height: 12px;
        line-height: 12px;
        display: flex;
        justify-content: space-between;
        cursor: pointer;
        color: #000000;
        &:hover {
          color: #ff6419;
        }
      }
      .p-active {
        color: #ff6419;
      }
    }
    .el-col-3,
    .el-col-4,
    .el-col-5 {
      overflow: auto;
      height: 360px;
      border-bottom: 1px solid #dddddd;
      border-left: 1px solid #dddddd;
      p {
        padding: 10px 20px;
        height: 12px;
        line-height: 12px;
        display: flex;
        justify-content: space-between;
        cursor: pointer;
        color: #000000;
        &:hover {
          color: #ff6419;
        }
      }
      .p-active {
        color: #ff6419;
      }
    }
  }
  .el-col-24 {
    height: 30px;
    margin: 0 auto 10px;
    line-height: 30px;
  }
}
.uploadTexture2 {
  margin-top: 20px;
  background: #f2f2f2;
  .itemsPos {
    height: 90px;
    width: 90px;
    display: inline-block;
    margin: 10px;
    position: relative;
    .selected {
      position: absolute;
      z-index: 12;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
    }
    .textureImg {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      z-index: 8;
    }
    .chks {
      opacity: 0;
      border: solid red 1px;
      height: 90px;
      width: 90px;
      z-index: 10;
    }
  }
}

.uploadPic {
  margin-right: 25px;
  background: #eee;
  padding-top: 15px;
  box-sizing: border-box;
  width: 720px;
  li {
    font-size: 12px;
    margin: 5px;
    height: 90px;
    width: 90px;
    overflow: hidden;
    display: inline-block;
    background-color: #fff;
    border-radius: 8px;
    cursor: pointer;
    &:hover {
      .upload-notmainpic__con,
      .posDelete {
        display: inline-block !important;
      }
    }
  }
}

.uploadImgList {
  width: 90px;
  text-align: center;
  position: relative;
  img {
    object-fit: contain;
  }
}
.upload-notmainpic__con {
  position: relative;
  top: -45px;
  background: rgba(0, 0, 0, 0.7);
  width: 90px;
  color: #fff;
  display: none;
  line-height: 22px;
  border-radius: 0 0 8px 8px;
  cursor: pointer;
  z-index: 100000;
}
.posDelete {
  position: absolute;
  background: url("../../../assets/images/icons/delete3.png") 0 center no-repeat;
  width: 17px;
  height: 16px;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  line-height: 40px;
  color: #fff;
  font-size: 20px;
  border-radius: 6px;
  text-align: center;
  z-index: 10;
}
.sizes {
  width: 240px;
  border: solid 1px #ddd;
  height: 40px;
  border-radius: 6px;
  padding-left: 10px;
}
.content {
  display: inline-block;

  .el-col-22 {
    line-height: 40px;
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
  }
  .el-col-24 {
    padding: 5px 0 5px 20px;
    height: 40px;
    line-height: 30px;
    h2 {
      display: inline-block;
      font-size: 14px;
      width: 110px;
      text-align: right;
    }
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
    span {
      color: #ff6419;
      margin-left: 10px;
      cursor: pointer;
    }
  }
}
.goods-operating-info {
  margin-bottom: 20px;
  .title {
    position: relative;
    .star {
      color: #ff6419;
      position: absolute;
      left: -6px;
    }
  }
  .el-col-2 {
    font-size: 14px;
    width: 110px;
    text-align: right;
    line-height: 40px;
    margin-left: 20px;
  }
  .el-col-22 {
    line-height: 40px;
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
  }
  .el-col-24 {
    padding: 5px 0 5px 20px;
    height: 40px;
    line-height: 30px;
    h2 {
      display: inline-block;
      font-size: 14px;
      //width: 110px;
      text-align: right;
    }
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
    span {
      color: #ff6419;
      margin-left: 10px;
      cursor: pointer;
    }
  }
}
.goods-operating-infos {
  display: flex;
  justify-content: left;
  .headline {
    font-size: 14px;
    min-width: 110px;
    text-align: right;
    line-height: 40px;
    margin-left: 20px;
  }
  .content {
    display: inline-block;
    line-height: 40px;
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
  }
  .el-col-22 {
    padding: 5px 0 5px 20px;
    height: 40px;
    line-height: 30px;
    h2 {
      display: inline-block;
      font-size: 14px;
      width: 110px;
      text-align: right;
    }
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
    span {
      color: #ff6419;
      margin-left: 10px;
      cursor: pointer;
    }
  }
}

.upload-mainpic__con {
  position: relative;
  top: -45px;
  background-color: #ff641e;
  width: 90px;
  color: #fff;
  display: inline-block;
  line-height: 22px;
  border-radius: 0 0 8px 8px;
}

.modelTextureFrame {
  border: solid 1px #e3e3e3;
  border-radius: 4px;
  margin-left: 100px;
  margin-bottom: 30px;
  background: #fff;
  .modelTextList {
    background: #f2f2f2;
  }
  .addDelModel {
    text-align: right;
    line-height: 150px;
    .el-button:last-of-type {
      margin-right: 30px;
    }
  }
  .currentModel {
    box-sizing: padding-box;
  }
  .selectedModel {
    display: inline-block;
    box-sizing: padding-box;
    vertical-align: 40px;
    position: relative;
    width: 70%;
    div {
      position: absolute;
      margin-left: 20px;
      width: 100%;
      p {
        line-height: 30px;
        color: #666;
      }
    }
  }
  .modelPic {
    text-align: left;
    margin-top: 20px;
    margin-left: 15px;
    vertical-align: middle;
    display: inline-block;
    position: relative;
    img {
      border-radius: 6px;
      height: 90px;
      width: 90px;
      padding: 12px;
      border: dashed 1px #e3e3e3;
    }
  }
  .textureTitle {
    border-radius: 4px;
    margin: 15px;
    background: #fff;
    .title,
    .delete {
      height: 36px;
      border-bottom: solid 1px #e3e3e3;
    }
    .title {
      line-height: 36px;
      padding-left: 15px;
    }
    .delete {
      text-align: center;
      border-left: solid 1px #e3e3e3;
      img {
        margin-top: 7px;
      }
    }
  }
  .radioStyle {
    -webkit-appearance: none;
    height: 16px;
    width: 16px;
    border-radius: 8px;
  }
  .uploadTexture {
    background: #fff;
    height: 156px;
    padding: 20px;
    .el-col {
      width: 90px;
      .el-input {
        height: 28px;
        text-align: center !important;
      }
    }
    .priceOffset {
      display: block;
      color: #666;
      padding-top: 12px;
      font-size: 12px;
      text-align: center;
    }
    .uploadButton {
      margin-left: 10px;
      border: dashed 1px #e3e3e3;
      border-radius: 4px;
      text-align: center;
      width: 90px;
      height: 90px;
      display: table-cell;
      vertical-align: middle;
      margin-bottom: 50px;
      &:first-of-type {
        background: #fff;
      }
      background: #f6f6f6;
      div {
        width: 90px;
        height: 90px;
        margin-bottom: 8px;
        position: relative;
        .el-radio {
          position: absolute;
          right: -7px;
          top: 6px;
        }
        img {
          position: absolute;
          left: 50%;
          right: 50%;
          transform: translate(-50%, 50%);
        }
      }
      p {
        line-height: normal;
        height: 30px;
        .addPlug {
          width: 20px;
          height: 20px;
        }
      }
      img {
        width: 48px;
        height: 48px;
      }
    }
  }
}

.propTitle-content {
  display: flex;
  justify-content: left;
  h2 {
    min-width: 100px;
    text-align: right;
    font-size: 14px;
    line-height: 40px;
  }
  span {
    width: 100px;
    font-size: 14px;
    line-height: 40px;
    margin: 0 10px;
  }
}

.propTitle {
  display: block;
  font-size: 14px;
  padding: 0 15px 15px 30px;
  border-bottom: solid 1px #dedede;
  margin-bottom: 15px;
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
  .addProduct_p {
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
  // padding: 15px 0 0 15px;
  border-radius: 4px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader-icon {
  position: relative;
  background-color: #ff6419;
  width: 20px;
  height: 20px;
  margin: 20px auto 10px;
  border-radius: 50%;
  .across {
    position: absolute;
    top: 9px;
    left: 2px;
    background-color: #fff;
    width: 16px;
    height: 2px;
  }
  .vertical {
    position: absolute;
    top: 2px;
    left: 9px;
    background-color: #fff;
    width: 2px;
    height: 16px;
  }
}

.img-text {
  width: 100%;
  height: 20px;
  font-size: 14px;
  line-height: 20px;
  color: #999;
}

.avatar {
  width: 90px;
  height: 90px;
  display: block;
}

.demo-ruleForm {
  .el-select {
    width: 100%;
  }
  .el-cascader {
    width: 100%;
  }
}
.tip {
  display: inline-block;
  width: 16px;
  height: 16px;
  color: #fff;
  font-size: 14px;
  line-height: 16px;
  text-align: center;
  background-color: #ff6419;
  border-radius: 100%;
  margin-left: 0;
  cursor: pointer;
}
.productPic {
  width: 150px;
  position: absolute;
  left: 0;
  span {
    color: #ff6419;
    margin-right: 5px;
  }
  .tooltip-btn {
    display: inline-block;
    border: 1px solid #ff6419;
    border-radius: 100%;
    width: 18px;
    height: 18px;
    line-height: 18px;
    text-align: center;
    cursor: pointer;
    padding: 0;
  }
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
  line-height: 90px;
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
.pageFrame {
  background: #fff;
  height: 110px;
  padding-top: 40px;
  box-sizing: border-box;
  text-align: center;
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


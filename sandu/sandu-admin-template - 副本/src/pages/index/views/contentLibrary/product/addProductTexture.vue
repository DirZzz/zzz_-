<template>
  <div class="addProductTextture">
    <!--弹出框-->
    <el-dialog
      title="添加运营分类信息"
      :visible.sync="infoClassifyVisible"
      class="info-ypop-up"
      width="800px"
    >
      <el-row>
        <!--<el-col :span="20">-->
        <!--<el-input v-model="seekClassify" size="medium" placeholder="请输入内容"></el-input>-->
        <!--</el-col>-->
        <!--<el-col :span="2">-->
        <!--<span>搜索</span>-->
        <!--</el-col>-->
        <!--<el-col :span="2">-->
        <!--<span>重置</span>-->
        <!--</el-col>-->
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
            :key="index"
            @click="selectType(index, 2)"
            :class="indexList.index2 == index ? 'p-active' : ''"
          >
            {{item.name}}
            <span>></span>
          </p>
        </el-col>
        <el-col :span="5">
          <p
            v-for="(item, index) in productCategorylist3"
            :key="index"
            @click="selectType(index, 3)"
            :class="indexList.index3 == index ? 'p-active' : ''"
          >
            {{item.name}}
            <span>></span>
          </p>
        </el-col>
        <el-col :span="5">
          <p
            v-for="(item, index) in productCategorylist4"
            :key="index"
            @click="selectType(index, 4)"
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
              :label="item"
              :key="index"
              @change="selectType(index, 5)"
            >{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="verifyProductClass">确 定</el-button>
        <el-button round @click="infoClassifyVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <!--弹出框-->
    <div>
      <v-nav :nav="nav"></v-nav>
    </div>
    <transition name="fade">
      <div class="addProduct">
        <header>
          <h1 v-if="!editProID">新增产品</h1>
          <h1 v-else>编辑产品</h1>
        </header>
        <p class="ptop">
          <span>*</span>为必填项
        </p>
        <div class="productBaseInfo">产品基础信息</div>
        <div class="productBaseItem">
          <el-form
            :model="ruleForm"
            :rules="rules"
            ref="ruleForm"
            label-width="165px"
            class="demo-ruleForm"
          >
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品名称：" prop="productNameStr">
                  <el-input class="formWidth" clearable v-model="ruleForm.productNameStr"></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="产品分类：" prop="productCategoryForm">
                  <el-cascader
                    @change="selectCategoryNO"
                    @active-item-change="handleItemChange"
                    v-model="ruleForm.productCategoryForm"
                    placeholder="请选择"
                    :props="props"
                    class="formWidth"
                    clearable
                    :options="productCategoryDrop"
                    filterable
                  ></el-cascader>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品品牌：" prop="brandValueStr">
                  <el-select
                    v-model="ruleForm.brandValueStr"
                    filterable
                    clearable
                    @change="selectedBrandID"
                    class="formWidth"
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
                <el-form-item label="产品型号：" prop="productTypeCodeStr">
                  <!--<el-input-->
                  <!--style="width: 320px;"-->
                  <!--v-model="ruleForm.productTypeCodeStr"-->
                  <!--placeholder="请输入内容"></el-input>-->
                  <el-autocomplete
                    style="width: 320px;"
                    v-model="ruleForm.productTypeCodeStr"
                    :fetch-suggestions="querySearchAsync"
                    @select="handleSelect"
                  ></el-autocomplete>
                  <!-- @select="handleSelect"-->
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品价格：">
                  <el-input
                    placeholder="请输入内容"
                    v-model.number="advicePriceInput2"
                    class="sizes"
                    style="width: 200px"
                  ></el-input>
                  <span>{{selectSaleUnionName}}</span>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="装修单价：">
                  <el-input
                    placeholder="请输入内容"
                    v-model="decorationPrice"
                    class="input-with-select"
                    style="width: 200px"
                    @blur="verifydecorationPrice"
                  >
                    <template slot="append">{{this.valuationUnit}}</template>
                  </el-input>
                  <p v-show="decorationPriceWhos" class="verify">{{this.decorationPriceTxt}}</p>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <span class="star" style="position: absolute;color: #ff6419;top: 12px;left: 50px">*</span>
                <el-form-item label="贴图长度(cm)">
                  <el-input
                    class="formWidth"
                    clearable
                    v-model="ruleForm.fileLength"
                    maxlength="6"
                    oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                  ></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <span class="star" style="position: absolute;color: #ff6419;top: 12px;right: 390px">*</span>
                <el-form-item label="贴图宽度(cm)">
                  <el-input
                    class="formWidth"
                    clearable
                    v-model="ruleForm.fileWidth"
                    maxlength="6"
                    oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品规格：">
                  <el-input
                    class="formWidth"
                    clearable
                    v-model="ruleForm.productSpec"
                    maxlength="50"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品风格：">
                  <el-select
                          multiple
                          v-model="baseValue"
                          @change="selectFenge"
                          class="formWidth"
                          clearable
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
            </el-row>
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="一石多面产品：">
                  <el-select v-model="isComplexParquet" style="width: 310px">
                    <el-option label="是" :value="1"></el-option>
                    <el-option label="否" :value="0"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="goods-operating-info">
              <el-col :span="24">
                <span class="star" style="position: absolute;color: #ff6419;top: 5px;left: 0">*</span>
                <h2 class="title">
                  运营分类信息
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
              :key="index"
              v-for="(item, index) in productClassList"
              v-if="item.list.length > 0"
            >
              <div class="headline">{{item.name}}：</div>
              <div class="content">
                <p v-for="(items, index) in item.list" :key="index">{{items}}</p>
              </div>
            </el-row>

            <el-row class="formFrameWidth">
              <div class="productPic">
                <span>*</span>产品图片
                <el-tooltip effect="dark" placement="right">
                  <div slot="content">
                    建议上传图片像素不超
                    <br>过2M,尺寸比例1:1
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
                    >
                      <img
                        src="../../../assets/images/icons/delete3.png"
                        width="17"
                        height="16"
                        alt
                      >
                    </span>
                    <img
                      style="border-radius: 8px;object-fit: contain"
                      width="90"
                      height="90"
                      :src="file.url"
                    >
                    <span
                      v-show="file.id == (defaultPicId || fileList2[0].id)"
                      class="upload-mainpic__con"
                    >主缩略图</span>
                    <span
                      v-show="showDeletes[index]"
                      @click="setDefaultPic(file,index)"
                      class="upload-notmainpic__con"
                    >设置为缩略图</span>
                  </li>
                </ul>
              </el-form-item>
              <!--<p v-if="!fileList2.length" class="uploadTip">请上传图片</p>-->
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="材质(材料)" prop>
                  <el-select
                    style="width: 250px"
                    v-model="ruleForm.texture"
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
              <el-col :span="12">
                <el-form-item label="材质表面属性">
                  <el-select
                    filterable
                    style="width: 250px"
                    v-model="ruleForm.textureAttrValue"
                    placeholder="请选择"
                  >
                    <el-option
                      v-for="item in textureAttrDrop"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-form-item label="产品描述：">
                <textarea
                  class="productDesc"
                  maxlength="200"
                  type="textarea"
                  :rows="6"
                  style="width: 777px"
                  placeholder="请输入内容"
                  v-model="prodTextarea"
                ></textarea>
              </el-form-item>
            </el-row>

            <el-row v-if="isComplexParquet ==1">
              <el-form-item>
                <el-button
                  type="primary"
                  size="mini"
                  :disabled="addGroup.length > 39"
                  @click="addT"
                >增加材质贴图</el-button>
              </el-form-item>
            </el-row>

            <el-row class="addGroup" v-for="(item,index) in addGroup" :key="index">
              <div v-if="isComplexParquet==1||(isComplexParquet!=1&&index==0)">
                <!--<span class="star" style="position: absolute;color: #ff6419;top: 175px;left: 16px">*</span>-->
                <div @click="getIndex(index)">
                  <el-col :span="4">
                    <el-form-item prop="textureImageUrl">
                      <span>材质图片:</span>
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

                  <el-col :span="4">
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
                      <single-image-upload
                        @delete="parentDelete"
                        :on-success="handleNormalImgSuccess"
                        @deleteClick="item.normalImageUrl = '';item.normalPicId = ''"
                        :image-url="item.normalImageUrl"
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
              </div>
            </el-row>

            <div class="splitLine"></div>
            <el-form-item>
              <el-button type="primary" class="confirmBtn" round @click="submitForm('ruleForm')">录入</el-button>
              <el-button round class="cancelBtnBg marRight" @click="resetForm('ruleForm')">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </transition>
  </div>
</template>
<script type="es6">
import hardProduct from "./conponents/hardProduct"; //硬装产品
import addHardProduct from "./conponents/addHardProduct"; //硬装产品
import setProductInfo from "./conponents/setProductInfo"; //硬装产品
import hardProductMinixs from "../../../filters/hardProductMinixs";
import { PointTwo } from "../../../filters/data";
import { mapGetters } from "Vuex";
import SingleImageUpload from "../../base/upload/SingleImageUpload.vue";
import qs from "qs";

export default {
  mixins: [hardProductMinixs],
  components: {
    hardProduct,
    addHardProduct,
    setProductInfo,
    SingleImageUpload
  },

  data() {
    return {
      addGroup: [
        {
          modelName: "material",
          textureId: 0,
          normalParam: "0",
          normalPicId: 0,
          thumbnailId: 0,
          normalImageUrl: "",
          thumbnailPath: ""
        }
      ],
      nav: [{ nav: "产品库", url: "/product" }, { nav: "新增产品", url: "" }],
      isComplexParquet: 1,
      headerToken: {
        Authorization: qs.parse(sessionStorage.getItem("loginUser")).token
      },
      totalPage: 0,
      page: 0,
      textureDrop: [],
      textureAttrsDrop: [],

      productSKUInfos: [],
      tableNameList: [],
      restaurants: [],
      state4: "",
      timeout: null,
      ProductPorpsName: [],
      recommandData: [],
      imgInfoList: [], // 添加贴图弹框内的贴图信息表格
      upImgTable: [
        { img: "", code: "", name: "", texture: "123", type: "" },
        { img: "", code: "", name: "", texture: "123", type: "" },
        { img: "", code: "", name: "", texture: "123", type: "" },
        { img: "", code: "", name: "", texture: "123", type: "" },
        { img: "", code: "", name: "", texture: "123", type: "" }
      ],
      addImgForm: {},
      materials: [], // 材质选择类型列表
      surface: [], // 材质选择表面属性列表
      UpImgForm: {
        texture: "",
        surface: "",
        type: "",
        modelNumber: "",
        imgName: "",
        imgCode: ""
      },
      seekClassify: "",
      infoClassifyVisible: false,
      imgVisible: false,
      imgDatailsVisible: false,
      addModelVisible: false,
      modelDatailsVisible: false,
      total: 0, // 弹窗分页
      isImg: null, // 判断是否为模型或者贴图
      SKUId: [],
      SKUIndex: "",
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
      GoodsOperatingList: [],
      infoCheckList: [],
      getProductPorps: [], // 产品属性
      sizeInfoList: [], // 产品规格信息列表
      popupModelCode: "",
      //popupModelAuthor: '',
      popupModelNum: "", //产品模型型号参数
      popupModelName: "",
      isAddModel: true,
      isUpImg: false,
      addModelVisibleTxt: "添加模型",
      // modelIdList: [], // 用于判断模型是否已经被选
      isDefaultList: [], // 模型材质贴图里，当选择的不是默认贴图时为0，默认为1
      popupType: "", // 弹窗类型，用于判断选择贴图属于贴图还是模型
      popupImageUrl: "",
      uploadImgIndex: -1, // 上传贴图选中区域下标
      modelImgInfo: {}, // 选中的模型贴图详情
      modelImgList: [], // 选中模型，贴图区域列表
      modelTable: [], // 添加模型弹窗模型信息列表
      SKUInfo: [], // SKU信息列表
      SKUInfoHeader: [], // SKU信息动态表头
      imgIndexShow: -1,
      indexShow: -1,
      allocation: [],
      productHeightTxt: "",
      productWidthTxt: "",
      productLengthTxt: "",
      onePic: "",
      textureLoading: true,
      modelListLoading: true,
      uploadPic: false,
      relateModelInfo: {
        isShow: false,
        modelName: "",
        modelCode: "",
        length: "",
        width: "",
        height: ""
      },
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
      relateTexture: {
        isShow: false,
        textureId: 0,
        textureName: "",
        textureCode: ""
      },
      modelTexture: {
        tempSelectedModelTextures: [],
        selectedModelTextures: [],
        areaId: 0,
        index: 0
      },
      assTextureDia: {
        query: {
          page: 0,
          limit: 10,
          companyId: sessionStorage.getItem("companyID")
        }
      },
      assModelDia: {
        query: {
          limit: 10,
          isUsed: 0,
          companyId: sessionStorage.getItem("companyID"),
          transStatus: "SUCCESS"
        }
      },
      assModelTextureDia: {
        query: {
          page: 1,
          limit: 10,
          companyId: sessionStorage.getItem("companyID")
        }
      },
      dialogAddTextureVisible: false,
      dmFormTexture: {},
      dtFormTexture: {},
      dialogModelVisible: false, // 弹框模型列表
      modelListData: [], // 模型列表
      TextureListData: [], // 贴图列表
      formLabelAlign: {
        name: "",
        author: "",
        code: ""
      },
      formTexture: {
        texture: "",
        textureAttr: "",
        textureName: "",
        textureCode: ""
      },
      query: {
        page: 0,
        limit: 10
      },
      area: [],
      num1: 0,
      modelRadio: "",
      distrAllotList: [], // 分配渠道
      selectedProps: [], // 选中的属性
      editProID: "",
      uploadParams: {
        platform: "web",
        module: "product",
        type: "image"
      },
      advicePriceInput1: "",
      advicePriceInput2: "",
      textureAttrDrop: [], // 贴图表面属性
      ProductTextureDrop: [], // 材质材料
      selectedTextureId: "", // 材质ID
      selectStyleId: "", // 产品风格ID
      selectedSeriesId: "", // 产品系列ID
      selectedBrandId: "", // 产品品牌ID
      selectCategoryCode: "", // 产品分类编码
      unitList: [], // 数字字典
      isCreatedTexture: "",
      textureName: "",
      fileLength: "",
      fileWidth: "",
      texture: "",
      textureBallPath: "",
      textureAttrValue: "",
      normalParam: "",
      selectSaleUnionId: "",
      selectSaleUnionName: "元",
      baseTextarea: "",
      prodTextarea: "",
      productCategoryDrop: [], // 获取分类下拉框
      newproductCategoryDrop: [], //新分类下拉框
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
        children: "children"
      },
      // 上传图片文件列表
      imageUrl: "",
      fileList2: [],
      ruleForm: {
        // 验证表单
        productSpec: "",
        productNameStr: "",
        productCategoryForm: [],
        productTypeCodeStr: "",
        brandValueStr: "",
        productSeriesStr: "",
        productStyleStr: "",
        productLength: "",
        productWidth: "",
        productHeight: ""
      },
      // 验证表单规则
      rules: {
        productNameStr: [
          { required: true, message: "请输入产品名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 50 个字符", trigger: "blur" }
        ],
        productTypeCodeStr: [
          { required: true, message: "请选择产品型号", trigger: ["blur","change"] },
          { min: 1, max: 40, message: "长度在 40 个字符", trigger: "blur" }
        ],
        productCategoryForm: [
          { required: true, message: "请选择产品分类", trigger: "change" }
        ],
        brandValueStr: [
          { required: true, message: "请选择产品品牌", trigger: "change" }
        ]
      },
      decorationPrice: "",
      decorationPriceTxt: "",
      decorationPriceWhos: false,
      valuationUnit: "元/个",
      // 品牌
      productBrand: [],
      productPropBO: [],
      isTexture: false, // 贴图
      isModel: false, // 模型
      defaultPicId: 0,
      modelId: 0,
      normalImageUrl: "",
      showDeletes: [],
      multipleSelection: [],
      heightWhos: false,
      lengthWhos: false,
      widthWhos: false,
      typeArr: [],
      imgRowIndex: 0
    };
  },
  computed: {
    ...mapGetters([
      "gettersHardModelList",
      "gettersAreaId",
      "gettersSetPropsId",
      "gettersModelId",
      "gettersSKUInfos"
    ]),
    isHard() {
      return sessionStorage.getItem("addProductType") == "hard" ? true : false;
    }
    /*getImgUrl(){
                let url = ''
                if(this.addGroup){
                    this.addGroup.map((res) => {
                        if(res.thumbnailPath) {
                            url = this.BASE_URL.sourceBaseUrl+res.thumbnailPath
                        }
                    })
                }
                console.log(this.BASE_URL.sourceBaseUrl+url, 'urls')
            }*/
  },
  methods: {
    addT() {
      let ObjJSON = {
        modelName: "material",
        textureId: 0,
        normalParam: "0",
        normalPicId: 0,
        thumbnailId: 0,
        normalImageUrl: "",
        thumbnailPath: ""
      };
      this.addGroup.push(ObjJSON);
    },
    deleteTexture(index) {
      this.addGroup.splice(index, 1);
    },
    handleNormalImgSuccess(resp) {
      this.addGroup[this.imgRowIndex].normalImageUrl = resp.data.url; // ? resp.data.url.replace(this.BASE_URL.sourceBaseUrl, '') : ''
      this.addGroup[this.imgRowIndex].normalPicId = resp.data.resId;
    },
    parentDelete() {
      this.addGroup[this.imgRowIndex].normalImageUrl = "";
      this.addGroup[this.imgRowIndex].normalPicId = "";
    },
    deleteimg() {
      this.addGroup[this.imgRowIndex].thumbnailPath = "";
      this.addGroup[this.imgRowIndex].thumbnailId = "";
    },
    /* 上传图片 */
    getIndex(index) {
      this.imgRowIndex = index;
    },
    handleTextureImgSuccess(resp) {
      this.addGroup[this.imgRowIndex].thumbnailPath = resp.data.url; // ? resp.data.url.replace(this.BASE_URL.sourceBaseUrl, '') : ''
      this.addGroup[this.imgRowIndex].thumbnailId = resp.data.resId;

      this.API.textureAttr().then(resp => {
        this.textureAttrsDrop = resp.data;
      });
    },
    getTexturesDrop() {
      this.API.textureCaiLiao().then(resp => {
        this.textureDrop = resp.data;
      });
    },
    pageFrameChange(val) {
      this.page = val;
      this.assTextureDia.query.page = val;
      this.searchTexture();
    },
    handleSizeChange(val) {
      this.page = 1;
      this.assTextureDia.query.page = 1;
      this.assTextureDia.query.limit = val;
      this.searchTexture();
    },
    getAllocation() {},
    /*移除当前一条SKU信息表*/
    querySearchAsync(queryString, cb) {
      this.API.modelNumberRecommand({
        modelNumber: this.ruleForm.productTypeCodeStr,
        brandId: this.selectedBrandId,
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        if (res.data.length) {
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
    verifydecorationPrice() {
      if (!PointTwo(this.decorationPrice) && this.decorationPrice) {
        this.decorationPriceTxt = "请输装修单价为正整数或小数点后两位数";
        this.decorationPriceWhos = true;
        this.decorationPrice = "";
      } else {
        this.decorationPriceWhos = false;
      }
    },
    createStateFilter(queryString) {
      return state => {
        return (
          state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        );
      };
    },
    handleSelect(item) {},
    handleCurrentChange(val) {
      this.seekIng(val);
    },
    getRecommand() {
      this.API.modelNumberRecommand({
        modelNumber: this.ruleForm.productTypeCodeStr,
        brandId: this.selectedBrandId,
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        if (res.data.length) {
          this.recommandData = res.data;
        }
      });
    },

    loadAll() {
      return this.recommandData;
    },
    handleSelect(item) {},
    deleteSKU(scope) {
      for (let item in scope.row) {
        if (item.substring(0, 4) == "list") {
          this.isitem(scope.row[item]);
        }
      }
      this.SKUInfo.splice(scope.$index, 1);
      this.SKUId.splice(scope.$index, 1);
    },
    isitem(item) {
      let cout = 0;
      for (let i = 0; i < this.SKUInfo.length - 1; i++) {
        for (let items in this.SKUInfo[i]) {
          if (item == this.SKUInfo[i][items]) {
            //  cout++;
          }
        }
      }
      if (cout == 0) {
        for (let i = 0; i < this.infoCheckList.length; i++) {
          for (let j = 0; j < this.infoCheckList[i].length; j++) {
            if (this.infoCheckList[i][j].name == item) {
              this.infoCheckList[i].splice(j, 1);
            }
          }
        }
      }
    },
    /*选中规格信息，生成对应选中的SKU信息*/
    infoChageBox(itemIndex, itemsIndex, id) {
      // this.ProductPorpsName.push(this.getProductPorps[itemIndex].name);
      this.tableNameList = [];

      var k = [];
      let arr = [];
      for (let i = 0; i < this.infoCheckList.length; i++) {
        if (this.infoCheckList[i].length > 0) {
          arr.push(this.infoCheckList[i]);

          k.push(this.getProductPorps[i].name);

          // this.ProductPorpsName.push(this.getProductPorps[i].name)
          // if (this.ProductPorpsName.length == 0) {
          //     this.ProductPorpsName.push(this.getProductPorps[i].name)
          // } else {
          //     for (let j = 0; j < this.ProductPorpsName.length; j++) {
          //         if (!this.ProductPorpsName[j] == this.getProductPorps[i].name) {
          //             // this.ProductPorpsName[j] = this.getProductPorps[i].name
          //             this.ProductPorpsName.push(this.getProductPorps[i].name)
          //         }
          //     }
          // }
        }
      }
      if (arr.length == 0) {
        this.SKUInfo = [];
        return;
      }
      if (arr.length > 1) {
        this.typeArr = this.doExchange(arr);
        //  let dataArr=[];
        let dataArr = [];
        for (let i = 0; i < this.typeArr.length; i++) {
          let string = this.typeArr[i];
          let stringArr = string.split("|");
          let ArrLength = stringArr.length;
          if (i == 0) {
            for (let y = 0; y < ArrLength; y++) {
              let tabName = {};
              tabName.name = k[y];
              tabName.key = "list" + y;
              tabName.id = this.getProductPorps[y]["id"];
              this.tableNameList.push(tabName);
            }
          }
          let dataObj = {};
          for (let y = 0; y < ArrLength; y++) {
            // dataObj[this.getProductPorps[y]['cn']]=stringArr[y]
            dataObj["list" + y] = stringArr[y];
            dataObj.textureId = "";
            dataObj.thumbnailPath = "";
            dataObj.modelId = "";
            dataObj.modelTextureInfos = "";
          }
          dataArr.push(dataObj);
        }
        this.SKUInfo = dataArr;
        let newDtatarr = this.deepClone(dataArr);

        // console.log(newDtatarr,'2132123')
        let SKUId = [];
        // for (let i = 0; i < this.SKUInfo.length; i++) {
        //     for (let j in this.SKUInfo[i]) {
        //         this.SKUInfo[i][j] = this.SKUInfo[i][j].split(':')[0]
        //     }
        // }
        for (let i = 0; i < newDtatarr.length; i++) {
          let idArr = [];
          for (let j in newDtatarr[i]) {
            if (newDtatarr[i][j].indexOf(":") > 0) {
              let id = newDtatarr[i][j].split(":")[1];
              idArr.push(id);
              this.SKUInfo[i][j] = newDtatarr[i][j].split(":")[0];
              // let id = dataArr[i][j].slice(dataArr[i][j].indexOf(':') + 1);
              // idArr.push(id);
            }
          }
          SKUId.push(idArr);
        }
        this.SKUId = SKUId;
        //
        // console.log(this.SKUInfo, 'info');
        // console.log(SKUId, 'id');
        // console.log(newDtatarr,'sfsd')
      } else {
        this.typeArr = this.doExchange(arr);
        let dataArr = [];
        let tabName = {};
        if (this.typeArr.length > this.SKUInfo.length) {
          for (let i = 0; i < this.typeArr.length; i++) {
            if (this.SKUInfo[i]) {
              if (this.SKUInfo[i].textureId != null) {
                let obj = {};
                obj.list0 = this.typeArr[i].name + ":" + this.typeArr[i].id;
                obj.id = this.typeArr[i].id;
                obj.textureId = this.SKUInfo[i].textureId;
                obj.thumbnailPath = this.SKUInfo[i].thumbnailPath;
                obj.modelId = "";
                obj.modelTextureInfos = [];
                dataArr.push(obj);
                console.log(this.SKUInfo[i], "skuinfoiiitx");
              } else {
                let obj = {};
                obj.list0 = this.typeArr[i].name + ":" + this.typeArr[i].id;
                obj.id = this.typeArr[i].id;
                obj.textureId = this.typeArr[i].textureId;
                obj.thumbnailPath = this.typeArr[i].thumbnailPath;
                obj.modelId = "";
                obj.modelTextureInfos = [];
                dataArr.push(obj);
              }
            } else {
              let obj = {};
              obj.list0 = this.typeArr[i].name + ":" + this.typeArr[i].id;
              obj.id = this.typeArr[i].id;
              obj.textureId = this.typeArr[i].textureId;
              obj.thumbnailPath = this.typeArr[i].thumbnailPath;
              obj.modelId = "";
              obj.modelTextureInfos = [];
              dataArr.push(obj);
            }
          }
        } else {
          console.log(id);
          dataArr = this.SKUInfo;
          for (let i = 0; i < dataArr.length; i++) {
            if (this.SKUInfo[i].id == id) {
              dataArr.splice(i, 1);
            }
          }
        }
        // for (let i = 0; i < this.typeArr.length; i++) {
        //     let obj = {};
        //     obj.list0 = this.typeArr[i].name + ':' + this.typeArr[i].id;
        //     obj.id = this.typeArr[i].id;
        //     obj.textureId = '';
        //     obj.thumbnailPath = '';
        //     obj.modelId = '';
        //     obj.modelTextureInfos = [];
        //     dataArr.push(obj);
        // }
        tabName.key = "list0";
        tabName.name = this.getProductPorps[itemIndex].name;
        this.tableNameList.push(tabName);
        this.SKUInfo = dataArr;
        // console.log(this.SKUInfo, 2);
        for (let i = 0; i < this.SKUInfo.length; i++) {
          // console.log(this.SKUInfo[i], 'i');
          this.SKUInfo[i].list0 = this.SKUInfo[i].list0.split(":")[0];
        }
        let SKUId = [];
        for (let i = 0; i < dataArr.length; i++) {
          let idArr = [];
          // console.log(dataArr[i]);
          idArr.push(dataArr[i].id);
          SKUId.push(idArr);
        }
        this.SKUId = SKUId;
        // console.log(SKUId, 'id1')
      }
    },
    deepClone(data) {
      var type = this.getType(data);
      var obj;
      if (type === "array") {
        obj = [];
      } else if (type === "object") {
        obj = {};
      } else {
        //不再具有下一层次
        return data;
      }
      if (type === "array") {
        for (var i = 0, len = data.length; i < len; i++) {
          obj.push(this.deepClone(data[i]));
        }
      } else if (type === "object") {
        for (var key in data) {
          obj[key] = this.deepClone(data[key]);
        }
      }
      return obj;
    },
    getType(obj) {
      //tostring会返回对应不同的标签的构造函数
      var toString = Object.prototype.toString;
      var map = {
        "[object Boolean]": "boolean",
        "[object Number]": "number",
        "[object String]": "string",
        "[object Function]": "function",
        "[object Array]": "array",
        "[object Date]": "date",
        "[object RegExp]": "regExp",
        "[object Undefined]": "undefined",
        "[object Null]": "null",
        "[object Object]": "object"
      };
      if (obj instanceof Element) {
        return "element";
      }
      return map[toString.call(obj)];
    },
    doExchange(arr) {
      var len = arr.length;
      // 当数组大于等于2个的时候
      if (len >= 2) {
        // 第一个数组的长度
        var len1 = arr[0].length;
        // 第二个数组的长度
        var len2 = arr[1].length;
        // 2个数组产生的组合数
        var lenBoth = len1 * len2;
        //  申明一个新数组,做数据暂存
        var items = new Array(lenBoth);
        // 申明新数组的索引
        var index = 0;
        // 2层嵌套循环,将组合放到新数组中
        for (var i = 0; i < len1; i++) {
          for (var j = 0; j < len2; j++) {
            if (arr[0][i].name) {
              items[index] =
                arr[0][i].name +
                ":" +
                arr[0][i].id +
                "|" +
                arr[1][j].name +
                ":" +
                arr[1][j].id;
            } else {
              items[index] =
                arr[0][i] + "|" + arr[1][j].name + ":" + arr[1][j].id;
            }
            index++;
          }
        }
        // 将新组合的数组并到原数组中
        var newArr = new Array(len - 1);
        for (var i = 2; i < arr.length; i++) {
          newArr[i - 1] = arr[i];
        }
        newArr[0] = items;
        // 执行回调
        return this.doExchange(newArr);
      } else {
        return arr[0];
      }
    },
    /*分类树赛选完后点击确认*/
    verifyProductClass() {
      this.productClassList = this.productClassLists;
      this.infoClassifyVisible = false;
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
        }
        this.indexList.index1 = index;
        this.productCategorylist2 = this.productCategorylist1[index].children;
      }
      if (i == 2) {
        if (this.indexList.index2 != index) {
          this.productCategorylist3 = [];
          this.productCategorylist4 = [];
          this.productCategorylist5 = [];
        }
        this.indexList.index2 = index;
        this.productCategorylist3 = this.productCategorylist2[index].children;
      }
      if (i == 3) {
        if (this.indexList.index3 != index) {
          this.productCategorylist4 = [];
          this.productCategorylist5 = [];
        }
        this.productClassLists = [];
        this.indexList.index3 = index;
        this.productCategorylist4 = this.productCategorylist3[index].children;
        for (let j = 0; j < this.productCategorylist4.length; j++) {
          let arr = { name: this.productCategorylist4[j].name, list: [] };
          this.productClassLists.push(arr);
        }
      }
      if (i == 4) {
        if (this.indexList.index4 != index) {
          this.productCategorylist5 = [];
        }
        this.indexList.index4 = index;
        this.productCategorylist5 = this.productCategorylist4[index].children;
      }
      if (i == 5) {
        this.productClassIdLists = [];
        for (let j = 0; j < this.checkProductList.length; j++) {
          this.productClassIdLists.push(this.checkProductList[j].id);
          if (
            this.productCategorylist5[index].name ==
            this.checkProductList[j].name
          ) {
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
              if (reschild.code == "cz") {
                this.productCategorylist1[0].children[reschildIndex].children =
                  reschild.children;
                firstArr.push(reschild);
              } else if (reschild.code == "db") {
                this.productCategorylist1[0].children[reschildIndex].children =
                  reschild.children;
                firstArr.push(reschild);
              } else if (reschild.code == "dals") {
                this.productCategorylist1[0].children[reschildIndex].children =
                  reschild.children;
                firstArr.push(reschild);
              }
            });
            this.productCategorylist1[0].children = firstArr;
          });
        }
      });
    },
    showUpImg() {
      this.isUpImg = false;
      this.addModelVisibleTxt = "添加模型";
    },
    /*上传贴图表格*/
    upImg() {},
    handleAvatarSuccess(res, file) {
      this.popupImageUrl = URL.createObjectURL(file.raw);
    },
    verifyLength() {
      //  let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
      //   if (!i.test(this.ruleForm.productLength)) {
      //     this.productLengthTxt = '请输产品长度为正整数或小数点后两位数';
      //     this.lengthWhos = true;
      //      this.ruleForm.productLength = '';
      //   } else {
      //     this.lengthWhos = false
      //   }
      console.log(this.ruleForm.productLength);

      if (!PointTwo(this.ruleForm.productLength)) {
        this.productLengthTxt = "请输产品宽度为正整数或小数点后两位数";
        this.lengthWhos = true;
        this.ruleForm.productLength = "";
      } else {
        this.lengthWhos = false;
      }
    },
    verifyWidth() {
      //  let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
      // if (!i.test(this.ruleForm.productWidth)) {
      //  this.productWidthTxt = '请输产品宽度为正整数或小数点后两位数';
      //   this.widthWhos = true;
      //     this.ruleForm.productWidth = '';
      // } else {
      //   this.widthWhos = false;
      // }

      if (!PointTwo(this.ruleForm.productWidth)) {
        this.productWidthTxt = "请输产品宽度为正整数或小数点后两位数";
        this.widthWhos = true;
        this.ruleForm.productWidth = "";
      } else {
        this.widthWhos = false;
      }
    },
    verifyHeight() {
      // let i = /^[0-9]\d*$/ && /^((?:-?1)|(?:-?[0-9]\d*))(?:\.\d{1,2})?$/;
      // if (!i.test(this.ruleForm.productHeight)) {
      //  this.productHeightTxt = '请输产品高度为正整数或小数点后两位数';
      //   this.heightWhos = true;
      //    this.ruleForm.productHeight   = '';
      // } else {
      //   this.heightWhos = false
      // }
      if (!PointTwo(this.ruleForm.productHeight)) {
        this.productHeightTxt = "请输产品高度为正整数或小数点后两位数";
        this.heightWhos = true;
        this.ruleForm.productHeight = "";
      } else {
        this.heightWhos = false;
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleSelectionChange2(val) {
      //硬装全选选择
    },
    deleteAreaTexture(item, j) {
      item.areaTextures.splice(j, 1);
    },
    resetModelCondition() {
      this.assModelDia.query = {
        limit: 10,
        isUsed: 0,
        companyId: sessionStorage.getItem("companyID"),
        transStatus: "SUCCESS"
      };
    },
    resetTextrue() {
      this.assModelTextureDia.query = {
        page: 0,
        limit: 10,
        companyId: sessionStorage.getItem("companyID")
      };
    },
    resetTextrue2() {
      this.assTextureDia.query = {
        limit: 10,
        companyId: sessionStorage.getItem("companyID")
      };
    },
    handleRemoveTexture() {
      this.relateTexture = {};
    },
    // 关闭弹窗时关闭清空选择贴图
    clearSelectModelTexture() {
      this.modelTexture.tempSelectedModelTextures = [];
    },
    // 删除模型
    removeModel() {
      this.relateModelInfo = {};
      this.modelId = 0;
    },
    resetSearch() {
      // 关闭对话时清空搜索条件内容
      this.assTextureDia.query.textureAttr = "";
      this.assTextureDia.query.texture = "";
      this.assTextureDia.query.textureName = "";
      this.assTextureDia.query.textureCode = "";
      this.assTextureDia.query.textureType = "";
      this.assTextureDia.query.modelNumber = "";
    },
    resetSearchModel() {
      // this.assModelDia.query = {}
      this.assModelDia.query.modelCode = "";
      this.assModelDia.query.modelModelNum = "";
      this.assModelDia.query.modelName = "";
    },
    toback() {
      let back_url = localStorage.getItem("from");
      this.$router.push({ path: back_url });
    },
    parsePercentage(val) {
      return parseInt(val, 10);
    },
    selectedChk(id) {
      return this.modelTexture.tempSelectedModelTextures.includes(id);
    },
    onImgVisible() {
      this.addImgForm.textureType = "";
      this.addImgForm.materials = "";
      this.addImgForm.modelNumber = "";
      this.addImgForm.surface = "";
      this.addImgForm.textureCode = "";
      this.addImgForm.textureName = "";
    },
    // 搜索贴图
    seekIng(page) {
      let obj = {
        textureType:
          this.addImgForm.textureType && this.addImgForm.textureType.length > 0
            ? this.addImgForm.textureType
            : undefined,
        //   materials: (this.addImgForm.materials) ? this.addImgForm.materials : undefined,
        texture: this.addImgForm.materials
          ? this.addImgForm.materials
          : undefined,
        modelNumber:
          this.addImgForm.modelNumber && this.addImgForm.modelNumber.length > 0
            ? this.addImgForm.modelNumber
            : undefined,
        textureAttr: this.addImgForm.surface
          ? this.addImgForm.surface
          : undefined,
        textureCode:
          this.addImgForm.textureCode && this.addImgForm.textureCode.length > 0
            ? this.addImgForm.textureCode
            : undefined,
        textureName:
          this.addImgForm.textureName && this.addImgForm.textureName.length > 0
            ? this.addImgForm.textureName
            : undefined,
        limit: 10,
        page: page,
        companyId: sessionStorage.getItem("companyID"),
        queryType: "success"
      };
      this.API.ListTexture(obj).then(res => {
        if (res) {
          this.total = res.total;
          this.imgInfoList = res.list;
        }
      });
    },
    // 选择贴图
    selectImg(scope) {
      if (this.popupType == "贴图") {
        this.SKUInfo[this.SKUIndex].textureId = scope.row.textureId;
        this.SKUInfo[this.SKUIndex].thumbnailPath = scope.row.thumbnailPath;
      }
      if (this.popupType == "模型") {
        let obj = {
          affectPrice: 0,
          id: scope.row.textureId,
          picPath: scope.row.thumbnailPath
        };
        this.modelImgList[this.uploadImgIndex].areaTextures.push(obj);
      }
      this.imgVisible = false;
    },
    // 设置当前图片为默认图
    selectImage(index, imgIndex) {
      this.modelImgList[index].defalutTextureId = this.modelImgList[
        index
      ].areaTextures[imgIndex].id;
    },
    //删除贴图
    deleteImg(index, imgIndex) {
      let i = this.modelImgList[index].areaTextures.indexOf(
        this.modelImgList[index].areaTextures[imgIndex]
      );
      this.modelImgList[index].areaTextures.splice(i, 1);
    },
    // 模型添加贴图
    uploadIndex(index) {
      this.uploadImgIndex = index;
      this.imgVisible = true;
      this.assTextureDia.query.queryType = "success";
      this.API.ListTexture(this.assTextureDia.query).then(res => {
        if (res) {
          this.total = res.total;
          this.imgInfoList = res.list;
        }
      });
      this.API.ProductTextureDrop().then(res => {
        this.materials = res.data;
      });
      this.API.textureAttr().then(res => {
        this.surface = res.data;
      });
    },
    /*确认使用模型*/
    affirmModel(scope) {
      if (scope == "取消") {
        this.isAddModel = true;
        return;
      }
      this.showAddModel(scope);
      this.SKUInfo[this.SKUIndex].modelId = this.modelImgInfo.modelId;
      this.SKUInfo[
        this.SKUIndex
      ].thumbnailPath = this.modelImgInfo.thumbPicPath;
      let arr = [];
      for (let i = 0; i < this.modelImgList.length; i++) {
        for (let j = 0; j < this.modelImgList[i].areaTextures.length; j++) {
          let obj = {
            affectPrice: this.modelImgList[i].areaTextures[j].affectPrice,
            areaId: this.modelImgList[i].id,
            isDefault: this.isDefaultList[i],
            textureId: this.modelImgList[i].areaTextures[j].id
          };
          arr.push(obj);
        }
      }
      this.SKUInfo[this.SKUIndex].modelTextureInfos = arr;
      this.addModelVisible = false;
      this.isAddModel = true;
      // console.log(this.SKUInfo);
    },
    /*选用模型打开关联贴图，取消回到模型表格*/
    showAddModel(scope) {
      this.isAddModel = false;
      this.isDefaultList = [];
      this.modelImgInfo = {
        thumbPicPath: scope.row.thumbPicPath,
        modelCode: scope.row.modelCode,
        modelName: scope.row.modelName,
        modelId: scope.row.modelId
      };
      // this.API.modelTextureDetail({modelId: scope.row.modelId}).then((res) => {
      //     if (res.data) {
      //         this.modelImgList = res.data;
      //         for (let i = 0; i < this.modelImgList.length; i++) {
      //             this.isDefaultList.push(1);
      //         }
      //         // console.log(this.isDefaultList);
      //     }
      // });
    },
    // 搜索模型
    seekModel() {
      let obj = {
        modelCode:
          this.popupModelCode && this.popupModelCode.length > 0
            ? this.popupModelCode
            : undefined,
        modelModelNum:
          this.popupModelNum && this.popupModelNum.length > 0
            ? this.popupModelNum
            : undefined,
        modelName:
          this.popupModelName && this.popupModelName.length > 0
            ? this.popupModelName
            : undefined,
        isUsed: 0,
        companyId: sessionStorage.getItem("companyID"),
        modelType: this.selectCategoryCode,
        // categoryCodes: 'dis',
        transStatus: "success"
      };
      this.API.ListModel(obj).then(res => {
        let arr = [];
        for (let i = 0; i < this.SKUInfo.length; i++) {
          if (this.SKUInfo[i].modelId != "") {
            arr.push(this.SKUInfo[i].modelId);
          }
        }
        let arrList = res.list;
        for (let i = 0; i < arrList.length; i++) {
          for (let j = 0; j < arr.length; j++) {
            if (arrList[i].modelId == arr[j]) {
              arrList[i].isShow = 1;
            }
          }
        }
        this.modelTable = arrList;
      });
    },
    // SKU添加贴图
    SKUTexture(scope, type) {
      this.popupType = type;
      this.SKUIndex = scope.$index;
      if (type == "模型") {
        this.addModelVisible = true;
        let obj = {
          isUsed: 0,
          companyId: sessionStorage.getItem("companyID"),
          modelType: this.selectCategoryCode,
          // categoryCodes: 'dis',
          transStatus: "success"
        };
        let arr = [];
        for (let i = 0; i < this.SKUInfo.length; i++) {
          if (this.SKUInfo[i].modelId != "") {
            arr.push(this.SKUInfo[i].modelId);
          }
        }
        this.API.ListModel(obj).then(res => {
          let arrList = res.list;
          for (let i = 0; i < arrList.length; i++) {
            for (let j = 0; j < arr.length; j++) {
              if (arrList[i].modelId == arr[j]) {
                arrList[i].isShow = 1;
              }
            }
          }
          this.modelTable = arrList;
        });
      }
      if (type == "贴图") {
        this.imgVisible = true;
        this.assTextureDia.query.queryType = "success";
        this.API.ListTexture(this.assTextureDia.query).then(res => {
          if (res) {
            this.total = res.total;
            this.imgInfoList = res.list;
          }
        });
        this.API.ProductTextureDrop().then(res => {
          this.materials = res.data;
        });
        this.API.textureAttr().then(res => {
          this.surface = res.data;
        });
      }
    },
    // 关联贴图
    handleAssocateTexture() {
      this.dialogAddTextureVisible = true;
      this.textureLoading = true;
      this.assTextureDia.query.queryType = "success";
      this.API.ListTexture(this.assTextureDia.query).then(res => {
        if (res) {
          this.textureLoading = false;
          this.TextureListData = res.list ? res.list : [];
        }
        if (res.code == 204) {
          this.textureLoading = false;
        }
      });
    },
    // 选用贴图
    handleAssocateTextureConfirm(row) {
      this.dialogAddTextureVisible = false;
      this.relateTexture = Object.assign({}, { isShow: true }, row);
      //   console.log(this.relateTexture);
      // this.SKUInfo[this.SKUIndex].textureId = this.relateTexture.textureId;
      // this.SKUInfo[this.SKUIndex].thumbnailPath = this.relateTexture.thumbnailPath;
      //   console.log(this.SKUInfo[this.SKUIndex]);
    },
    // 材质材料
    getProductTextureDrop() {
      this.API.textureCaiLiao().then(res => {
        this.ProductTextureDrop = res.data;
      });
    },
    // 贴图表面属性
    textureAttrDropList() {
      this.API.textureAttr().then(res => {
        this.textureAttrDrop = res.data;
      });
    },
    // 上传贴图搜索
    searchTexture() {
      this.assTextureDia.query.queryType = "success";
      this.API.ListTexture(this.assTextureDia.query).then(res => {
        this.TextureListData = res.list ? res.list : [];
      });
    },
    handleAddArea() {
      // 添加区域
      // this.area.push([])
    },
    handleAddModelTexture(area, index) {
      this.dialogModelTextureVisible = true;
      this.modelTexture.areaId = area.id;
      this.modelTexture.index = index;
      this.assModelTextureDia.query.queryType = "success";
      this.API.ListTexture(this.assModelTextureDia.query).then(res => {
        this.TextureListData = res.list ? res.list : [];
      });
      //        this.area[index].push({price: 10, defaultThumb: true})
    },
    handleChange(value) {},
    selectPropValues(val, index, item) {
      this.productPropBO[index] = val;
      this.productPropBO[index].parent = {
        code: item.code,
        id: item.id,
        name: item.name
      };
    },
    // 获取产品属性
    getProductProps() {
      let arr = [];
      this.infoCheckList = [];
      this.selectedProps = [];
      if (this.selectCategoryCode) {
        this.API.ProductProps({
          code: this.selectCategoryCode
        }).then(res => {
          this.getProductPorps = res.data;
          this.$store.dispatch("saveProductPorps", res.data);
          console.log(res.data);
          for (let i = 0; i < res.data.length; i++) {
            this.infoCheckList.push(arr);
          }
          if (res.data.length == 0) {
            this.isModel = true;
          } else {
            this.isModel = false;
          }
        });
      }
    },
    /* 上传图片 */
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
      this.selectedBrandId = val;
    },
    handleItemChange(val) {},
    // 产品分类编号
    selectCategoryNO(val) {
      this.$store.dispatch("setHardModel", []);
      this.SKUInfo = [];
      this.selectCategoryCode = val[val.length - 1];
      let filter = this.unitList.filter(e => {
        if (e.suitableCodes)
          return e.suitableCodes.indexOf(this.selectCategoryCode) != -1;
      });
      this.selectSaleUnionId = filter.length
        ? filter[filter.length - 1].id
        : this.unitList[0].id;
      this.selectSaleUnionName = filter.length
        ? filter[filter.length - 1].name
        : this.unitList[0].name;
      this.productPropBO = [];
      this.isModelImg();
      this.getProductProps();
      this.relateModelInfo = {
        isShow: false
      };
      this.relateTexture = {
        isShow: false
      };

      this.valuationUnit = "元/个";
      this.unitList.map(res=>{
          if (res.suitableCodes && res.suitableCodes.indexOf(this.selectCategoryCode + ",") != -1) {
              this.valuationUnit = res.name;
          }
      });
    },
    // 判断模型和贴图
    isModelImg() {
      this.API.isModel({ code: this.selectCategoryCode }).then(res => {
        this.isImg = res.data;
      });
    },
    //  产品风格
    selectFenge(val) {
      this.selectFengeId = val;
    },
    handleAvatarSuccess(res, file) {
      // this.imageUrl = URL.createObjectURL(file.raw);
      this.fileList2.push({
        url: res.data.url,
        id: res.data.resId
      });
      this.onePic = this.fileList2[0].id;
    },
    deleteUploadImg(index) {
      this.fileList2.splice(index, 1);
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

    submitForm(formName) {
      this.getHardParam();
      console.log(this.gettersSKUInfos);
      this.$refs[formName].validate(valid => {
        if (valid) {
          let modelTextureInfos = [];
          this.area.forEach(it => {
            // 组装后台需要的材质参数
            it.areaTextures &&
              it.areaTextures.forEach((tit, index) => {
                modelTextureInfos.push({
                  affectPrice: tit.affectPrice,
                  areaId: it.id,
                  isDefault: it.isDefault === index ? 1 : 0,
                  textureId: tit.textureId
                });
              });
          });
          let productSKUInfos = [];
          // console.log(this.SKUInfo[0].textureId, 'this.SKUInfo');
          for (let i = 0; i < this.SKUInfo.length; i++) {
            let propIdsObj = {
              propIds: this.SKUId[i],
              modelId:
                this.SKUInfo[i].modelId && this.SKUInfo[i].modelId != ""
                  ? this.SKUInfo[i].modelId
                  : null,
              modelTextureInfos:
                this.SKUInfo[i].modelTextureInfos &&
                this.SKUInfo[i].modelTextureInfos.length > 0
                  ? this.SKUInfo[i].modelTextureInfos
                  : null,
              textureId:
                this.SKUInfo[i].textureId && this.SKUInfo[i].textureId != ""
                  ? this.SKUInfo[i].textureId
                  : null
            };
            productSKUInfos.push(propIdsObj);
          }
          let formDatas = {
            categoryIds: this.productClassIdLists, // 运营分类信息
            productSKUInfos:
              sessionStorage.getItem("addProductType") == "hard"
                ? this.gettersSKUInfos
                : productSKUInfos, // SKU信息
            addProductType: "soft",
            modelId: this.modelId,
            advicePrice: this.advicePriceInput1, // (number, optional): 建议售价 ,
            modelingId: this.selectStyleId, //  (integer, optional): 产品款式 ,
            brandId: this.selectedBrandId, //  (integer): 品牌ID ,
            productSmallType: this.selectCategoryCode, //  (string): 所属分类编码 ,
            categoryCode: this.selectCategoryCode, //  (string): 所属分类编码 ,
            code: "", //  (string): 产品编码 ,
            desc: this.prodTextarea, //  (string, optional): 产品描述 ,
            modelNumber: this.ruleForm.productTypeCodeStr, //  (string, optional): 产品型号 ,
            name: this.ruleForm.productNameStr, //  (string): 产品名称 ,this.productName
            length: this.ruleForm.productLength, // 长
            width: this.ruleForm.productWidth, // 宽
            height: this.ruleForm.productHeight, // 高
            platformType: this.distrAllotList.map(type => type).join(","), //  (string, optional): (多选框)要分配的平台类型;渠道:2b/线上:2c ,
            price: this.advicePriceInput2, //  (number, optional): 售价 ,
            decorationPrice: this.decorationPrice, //  装修单价 ,
            userId: sessionStorage.getItem("userId"),
            saleUnionId: this.selectSaleUnionId, //  (integer, optional): 售价单位 ,
            seriesId: this.selectedSeriesId, //  (integer, optional): 产品系列 ,
            baseStyleId: this.selectFengeId, //  (integer, optional): 产品风格
            picIds: this.fileList2.map(item => item.id).join(","),
            defaultPicId: this.defaultPicId || this.onePic, // 没有设置主缩略图时 this.fileList2[0].id 默认第一张为主缩略图
            modelTextureInfos: modelTextureInfos,
            textureId: this.relateTexture.textureId,
            props: this.productPropBO,
            productSpec: this.ruleForm.productSpec,
            isCreatedTexture: 1,
            isComplexParquet: this.isComplexParquet
          };

          let formTextureData = {
            companyId: sessionStorage.getItem("companyID"),
            textureName: this.ruleForm.productNameStr,
            fileLength: this.ruleForm.fileLength,
            fileWidth: this.ruleForm.fileWidth,
            texture: this.ruleForm.texture,
            textureAttrValue: this.ruleForm.textureAttrValue,
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
          //提交检验
          if (
            this.ruleForm.fileLength == "" ||
            this.ruleForm.fileLength == null ||
            this.ruleForm.fileLength == undefined
          ) {
            this.$message.error("请填写长度");
            return;
          }
          if (
            this.ruleForm.fileWidth == "" ||
            this.ruleForm.fileWidth == null ||
            this.ruleForm.fileWidth == undefined
          ) {
            this.$message.error("请填写宽度");
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
          this.$confirm("确定录入产品?", "提示", {
            confirmButtonText: "确定",
            type: "info",
            cancelButtonClass: "cancelButtonClass",
            confirmButtonClass: "confirmButtonClass",
            center: true
          }).then(() => {
            this.addGroup &&
              this.addGroup.map(res => {
                res.thumbnailPath = this.changeUrl(res.thumbnailPath);
              });
            let formDatass = {
              productAdd: formDatas,
              resTextureAdd: formTextureData
            };
            let jsonData = JSON.stringify(formDatass);
            this.API.AddProductTexture(jsonData).then(res => {
              if (res) {
                this.$message.success("产品录入成功");
                this.$router.replace("/product");
              } else {
                this.$message.error(res.message);
              }
            });
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.$router.push("/product");
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
    // 产品分类下拉框
    getProductCategory() {
      // this.$nextTick(() => {
      //     this.productCategoryDrop = this.cases
      //     console.log(this.cases, ' case ')
      // })
      this.API.ProductCategory().then(res => {
        this.productCategoryDrop = res.data;
        this.productCategoryDrop.map(res => {
          let firstArr = [];
          res.children.map((reschild, reschildIndex) => {
            if (reschild.keyCode == "qiangm") {
              let arr = [];
              reschild.children.map((reschildren, index) => {
                if (
                  reschildren.keyCode == "qiangz" ||
                  reschildren.keyCode == "qiangzhi" ||
                  reschildren.keyCode == "tul" ||
                  reschildren.keyCode == "qiangs" ||
                  reschildren.keyCode == "qiangb" ||
                  reschildren.keyCode == "yaox"
                ) {
                  arr.push(reschildren);
                }
              });
              this.productCategoryDrop[0].children[
                reschildIndex
              ].children = arr;
              firstArr.push(reschild);
            }
            if (reschild.keyCode == "dim") {
              let arr = [];
              reschild.children.map((reschildren, index) => {
                if (
                  reschildren.keyCode == "diz" ||
                  reschildren.keyCode == "dib" ||
                  reschildren.keyCode == "dis" ||
                  reschildren.keyCode == "tijx" ||
                  reschildren.keyCode == "bodx"
                ) {
                  arr.push(reschildren);
                }
              });
              this.productCategoryDrop[0].children[
                reschildIndex
              ].children = arr;
              firstArr.push(reschild);
            }
            if (reschild.keyCode == "cz") {
              // let arr = [];
              // reschild.children.map((reschildren, index) => {
              //     if (reschildren.keyCode == 'diz' || reschildren.keyCode == 'dib'
              //         || reschildren.keyCode == 'dis' || reschildren.keyCode == 'tijx'
              //         || reschildren.keyCode == 'bodx') {
              //         arr.push(reschildren)
              //     }
              // })
              // this.productCategoryDrop[0].children[reschildIndex].children = arr;
              firstArr.push(reschild);
            }
            if (reschild.keyCode == "dals") {
              firstArr.push(reschild);
            }
            if (reschild.keyCode == "db") {
              firstArr.push(reschild);
            }
          });
          this.productCategoryDrop[0].children = firstArr;
        });
      });
    },
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
    },
    // 模型和贴图
    modelAndTexture() {
      if (this.selectCategoryCode) {
        this.API.modelTexture({
          code: this.selectCategoryCode
        }).then(res => {
          if (res.data === 1) {
            this.isModel = true;
          } else {
            this.isModel = false;
          }

          if (res.data === 2) {
            this.isTexture = true;
          } else {
            this.isTexture = false;
          }
        });
      }
    },
    showModelList() {
      this.dialogModelVisible = true;
      this.assModelDia.query.categoryCodes = this.selectCategoryCode;
      this.assModelDia.query.modelType = this.selectCategoryCode;
      this.modelListLoading = true;
      this.API.ListModel(this.assModelDia.query).then(res => {
        if (res) {
          this.modelListLoading = false;
          this.modelListData = res.list;
        }
        if (res.code == 204) {
          this.modelListLoading = false;
        }
      });
    },
    searchTextureImg(type) {
      type == 1 ? (this.page = 1) : "";
      this.assModelTextureDia.query.queryType = "success";
      this.API.ListTexture(this.assModelTextureDia.query).then(res => {
        this.TextureListData = res.list ? res.list : [];
      });
    },
    // 模型选用之后吧模型区域和模型材质放入
    modelSelectUse(row) {
      if (row.modelId) {
        this.modelId = row.modelId;
        this.dialogModelVisible = false;
        this.relateModelInfo.isShow = true;
        this.relateModelInfo.modelName = row.modelName;
        this.relateModelInfo.modelCode = row.modelCode;
        this.relateModelInfo.picPath = row.thumbPicPath;
        this.relateModelInfo.length = row.length;
        this.relateModelInfo.width = row.width;
        this.relateModelInfo.height = row.height;
        // this.API.modelTextureDetail({
        //   modelId: row.modelId
        // }).then((res) => {
        //   this.area = res.data.map(it => Object.assign({}, it, {
        //     isDefault: 0,
        //     areaTextures: it.areaTextures.map(tit => Object.assign({}, {
        //       thumbnailPath: tit.picPath,
        //       deleteBtnVisible: false,
        //       textureId: tit.id,
        //     }, tit))
        //   }))
        //   this.dialogModelVisible = false
        //   this.relateModelInfo.isShow = true
        //   this.relateModelInfo.modelName = row.modelName
        //   this.relateModelInfo.modelCode = row.modelCode
        //   this.relateModelInfo.picPath = row.thumbPicPath
        //   this.relateModelInfo.length = row.length;
        //   this.relateModelInfo.width = row.width;
        //   this.relateModelInfo.height = row.height;
        // })
      }
    },
    // 贴图选用
    textureSelectUse(row) {
      if (row.modelId) {
        this.API.modelTextureDetail({
          modelId: row.modelId
        }).then(res => {
          this.area = res.data;
          this.dialogAddTextureVisible = false;
        });
      }
    }
  },
  created() {
    this.editProID = this.$route.params.proID;
    this.getProductTextureDrop();
    this.textureAttrDropList();
    this.modelAndTexture();
    this.getProductCategory();
    this.getProductBrand();
    this.getProductSeriesList();
    this.getProductStyleDrop();
    this.getBaseProductStyleDrop();
    this.getDisctionaryUnit();
    this.getRecommand();
  },
  mounted() {
    // this.getTextureAttrsDrop()
    this.getTexturesDrop();
    this.restaurants = this.loadAll();
    //拖拽事件
    this.$dragging.$on("dragged", ({ value }) => {
      //拖拽事件 触发
      this.defaultPicId = value.list[0].id;
    });
  }
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
.nonImg {
  width: 90px;
  height: 90px;
  line-height: 90px;
  text-align: center;
  background-color: #f5f5f5;
  display: inline-block;
  margin-right: 10px;
}

.model-img-txt {
  .el-form-item__label {
  }
}

.add-model-ypop-up {
  .model-list {
    .el-col-12 {
      margin-bottom: 20px;
      .el-input {
        width: 220px;
        margin-left: 10px;
      }
      .el-button {
        width: 133px;
        height: 40px;
        margin-left: 70px;
        background-color: #ff6419;
        color: #fff;
        font-size: 16px;
      }
    }
    .el-table {
      max-height: 460px;
      overflow: auto;
      .model-choose {
        border: none;
        background-color: #ff6419;
        color: #fff;
      }
      .model-choose1 {
        border: none;
        background-color: #999999;
        color: #fff;
      }
    }
  }
  .img-list {
    max-height: 520px;
    overflow: auto;
    background-color: #fafafa;
    padding-top: 10px;
    .first {
      border: 1px solid #dddddd;
      padding: 20px;
      display: flex;
      justify-content: left;
      margin-bottom: 20px;
      img {
        width: 90px;
        height: 90px;
        margin-right: 20px;
      }
      div {
        p {
          height: 36px;
          line-height: 36px;
          margin-top: 5px;
          color: #666666;
          span {
            color: #333333;
            margin-left: 10px;
          }
        }
      }
    }
    .el-col-24 {
      background-color: #fff;
      margin-top: 10px;
      border: 1px solid #dddddd;
      h1 {
        height: 30px;
        font-size: 14px;
        line-height: 30px;
        padding-left: 20px;
        border-bottom: 1px solid #dddddd;
      }
      .up-img {
        display: flex;
        justify-content: left;
        padding: 10px 20px;
        .img-price {
          background-color: #fafafa;
        }
        .first-up-img {
          width: 90px;
          height: 118px;
          border: 1px solid #dddddd;
          overflow: hidden;
          margin-right: 20px;
          p {
            height: 20px;
            line-height: 28px;
            text-align: center;
          }
          div {
            width: 90px;
            height: 90px;
            box-sizing: border-box;
            border-bottom: 1px solid #dddddd;
            overflow: hidden;
            .add-img {
              display: block;
              width: 21px;
              height: 20px;
              margin: 26px auto 5px;
              background: url("../../../assets/images/icons/uploadPlug.png");
            }
          }
        }
        .item-img {
          width: 90px;
          height: 118px;
          margin-left: 10px;
          border: 1px solid #dddddd;
          overflow: hidden;
          position: relative;
          .img-price1 {
            cursor: pointer;
            width: 100%;
            height: 20px;
            position: absolute;
            top: 70px;
            color: #ffffff;
            background-color: #333333;
            line-height: 20px;
            text-align: center;
            font-size: 12px;
          }
          .img-price {
            width: 100%;
            height: 20px;
            position: absolute;
            top: 70px;
            color: #ffffff;
            background-color: #333333;
            line-height: 20px;
            text-align: center;
            font-size: 12px;
          }
          .img-delete {
            position: absolute;
            top: 35px;
            left: 35px;
            width: 20px;
            height: 20px;
            background: url("../../../assets/images/icons/delete3.png")
              no-repeat;
            background-size: cover;
          }
          img {
            display: block;
            width: 90px;
            height: 90px;
            box-sizing: border-box;
            border-bottom: 1px solid #dddddd;
          }
        }
      }
    }
  }
  .row-footer {
    padding-top: 20px;
    .el-col-24 {
      display: flex;
      justify-content: flex-end;
      .add-model-confirm {
        background-color: #ff6419;
        color: #fff;
        border: none;
      }
      .add-model-cancel {
        background-color: #ffdbc9;
        color: #ff6419;
        border: none;
      }
    }
  }
  .add-img-box {
    .el-form-item {
      .el-select {
        width: 200px;
      }
      .seek {
        width: 133px;
        height: 40px;
        margin-left: 100px;
        margin-right: 20px;
        font-size: 16px;
      }
      .reset {
        font-size: 16px;
        color: #ff6419;
      }
    }
    .el-table {
      max-height: 350px;
      overflow: auto;
    }
  }
  .add-model-box {
    .progress-bar {
      position: relative;
      width: 100%;
      margin-bottom: 20px;
      .model-status {
        display: inline-block;
        margin-left: 80px;
        width: 30px;
        height: 30px;
        background: url("../../../assets/images/icons/status2.png");
      }
      .model-status-active {
        display: inline-block;
        margin-left: 80px;
        width: 30px;
        height: 30px;
        background: url("../../../assets/images/icons/status1.png");
      }
      .link-line {
        position: absolute;
        top: 12.5px;
        left: 110px;
        display: inline-block;
        width: 400px;
        height: 3px;
        background-color: #dddddd;
      }
      .link-line-active {
        position: absolute;
        top: 12.5px;
        left: 110px;
        display: inline-block;
        width: 400px;
        height: 3px;
        background-color: #ff6419;
      }
      .img-status {
        display: inline-block;
        margin-left: 395px;
        width: 30px;
        height: 30px;
        background: url("../../../assets/images/icons/status1.png");
      }
      .img-status-active {
        display: inline-block;
        margin-left: 395px;
        width: 30px;
        height: 30px;
        background: url("../../../assets/images/icons/status2.png");
      }
      p {
        color: #333333;
        .model-txt {
          margin-left: 65px;
        }
        .img-txt {
          margin-left: 370px;
        }
      }
    }
  }
}

.model-datails-ypop-up {
  .el-button {
    background-color: #ffdbc9;
    border: none;
    color: #ff6419;
  }
  .el-row {
    max-height: 450px;
    overflow: auto;
  }
  .el-col-24 {
    display: flex;
    justify-content: left;
    width: 100%;
    border: 1px solid #dddddd;
    margin-top: 20px;
    padding: 20px 0;
    div {
      width: 90px;
      height: 110px;
      margin-left: 10px;
      margin-right: 20px;
      p {
        margin-top: 32px;
      }
    }
    .model-img {
      margin-right: 0;
      img {
        display: block;
        width: 90px;
        height: 90px;
        border: 1px solid #dddddd;
        box-sizing: border-box;
      }
      span {
        display: block;
        width: 90px;
        height: 20px;
        line-height: 20px;
        text-align: center;
      }
    }
  }
  .model-datails-content {
    display: flex;
    justify-content: left;
    width: 100%;
    border: 1px solid #dddddd;
    padding: 20px 0;
    box-sizing: border-box;
    img {
      margin: 0 20px;
      width: 90px;
      height: 90px;
    }
    div {
      margin-top: 10px;
      p {
        height: 36px;
        line-height: 36px;
      }
    }
  }
}

.img-datails-ypop-up {
  .el-button {
    background-color: #ffdbc9;
    border: none;
    color: #ff6419;
  }
  .img-datails-content {
    display: flex;
    justify-content: left;
    width: 100%;
    img {
      margin: 0 20px;
      width: 90px;
      height: 90px;
    }
    div {
      margin-top: 10px;
      p {
        height: 36px;
        line-height: 36px;
      }
    }
  }
}

.img-ypop-up {
  .el-pagination {
    margin-top: 20px;
    text-align: center;
  }
  .el-form-item {
    display: inline-block;
  }
  .el-input {
    width: 217px;
  }
  .el-button {
    width: 133px;
    height: 40px;
    font-size: 16px;
    font-weight: bold;
  }
  .el-table {
    height: 500px;
    overflow: auto;
    .set-img {
      width: 55px;
      height: 26px;
      font-size: 14px;
      text-align: center;
      background-color: #ff6419;
      border: none;
      color: #fff;
      border-radius: 4px;
    }
    .set-img-active {
      background-color: #ffcab0;
      width: 55px;
      height: 26px;
      font-size: 14px;
      text-align: center;
      border: none;
      color: #fff;
      border-radius: 4px;
    }
  }
  .nonImg {
    width: 90px;
    height: 90px;
    line-height: 90px;
    text-align: center;
    background-color: #f5f5f5;
    display: inline-block;
    margin-right: 10px;
  }
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

.uploadTip {
  color: red;
  margin-left: 100px;
  margin-bottom: 15px;
  margin-top: -16px;
}

.left {
  position: relative;
  &:before {
    content: "";
    background: #fff;
    position: absolute;
    width: 16px;
    height: 16px;
    bottom: 14px;
  }
}

.uploadImgList {
  width: 90px;
  text-align: center;
  margin: 5px 0 5px 20px;
  position: relative;
}

.posDelete {
  position: absolute;
  width: 40px;
  height: 40px;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  line-height: 40px;
  color: #fff;
  font-size: 20px;
  border-radius: 6px;
  text-align: center;
  display: none;
}

.posDelete2 {
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
  display: none;
}

.selectCHKS {
  margin-top: 20px;
  background: #f2f2f2;
  .item {
    height: 90px;
    width: 90px;
    display: inline-block;
    margin: 10px;
    position: relative;
    .chks {
      opacity: 0;
      border: solid red 1px;
      height: 90px;
      width: 90px;
      z-index: 10;
    }
    .selected {
      position: absolute;
      z-index: 12;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
    }
    .imgPos {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      z-index: 8;
    }
  }
}

.productBaseItem {
  .el-form-item {
    margin-bottom: 30px;
  }
}

.modelTextureFrame {
  border: solid 1px #e3e3e3;
  border-radius: 4px;
  margin-left: 100px;
  margin-bottom: 30px;

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
  .uploadTexture {
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
          right: 5px;
          top: 8px;
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

.propTitle {
  display: block;
  font-size: 14px;
  padding: 0 15px 15px 30px;
  border-bottom: solid 1px #dedede;
  margin-bottom: 15px;
}

.selectAllotFrame {
  border-top: solid 1px #e3e3e3;
  border-bottom: solid 1px #e3e3e3;
  padding-top: 15px;
  margin-bottom: 25px;
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
  .ptop {
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
  .specification-info {
    .SKU-table-header {
      padding: 0;
      height: 30px;
    }
    .el-checkbox-group {
      display: inline-block;
      margin-left: 5px;
    }
    .el-col-24 {
      border-top: 1px solid #dddddd;
      height: 74px;
      line-height: 74px;
      h2 {
        display: inline-block;
        font-size: 14px;
        width: 110px;
        text-align: right;
      }
      .el-radio {
        margin-left: 20px;
      }
    }
    .el-col-2 {
      margin-bottom: 20px;
      font-size: 14px;
      width: 110px;
      text-align: right;
    }
    .el-col-21,
    .el-col-20 {
      padding-left: 10px;
      .el-checkbox-group {
        .el-checkbox {
          width: 180px;
          margin: 0 0 20px 0;
        }
      }
      .el-table {
        margin: 0 0 20px 0;
        span {
          color: #ff6419;
          cursor: pointer;
        }
      }
    }
  }
  .productBaseInfo {
    height: 40px;
    line-height: 40px;
    background-color: #f6f6f6;
    border-left: solid 4px $leaveButtonStatus;
    color: $color666;
    font-size: 16px;
    padding-left: 10px;
    font-weight: bold;
    margin-bottom: 30px;
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

.uploadPic {
  margin-right: 25px;
  background: #eee;
  padding-top: 15px;
  box-sizing: padding-box;
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
    &:hover {
      .upload-notmainpic__con,
      .posDelete,
      .posDelete2 {
        display: inline-block !important;
      }
    }
  }
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
.sizes {
  width: 240px;
  height: 40px;
  border-radius: 6px;
  padding-left: 10px;
}
.demo-ruleForm {
  .el-select {
    width: 100%;
  }
  .el-cascader {
    width: 100%;
  }
}

.avatar {
  width: 90px;
  height: 90px;
  display: block;
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
}

.pos {
  margin-bottom: 12px;
  min-height: 80px;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
  .nav-top {
    margin-top: 35px;
  }

  ul {
    display: flex;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    font-size: 13px;
    color: #626262;
    li {
      padding: 0 20px;
      line-height: 80px;
      font-weight: bold;
      b {
        display: inline-block;
        background: $btnOrange;
        height: 40px;
        line-height: 40px;
        padding: 0 20px;
        color: #fff;
        border-radius: 20px;
      }
    }
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
  text-align: right;
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
<style lang="scss">
.addProductTextture {
  .el-upload-list__item img {
    width: 90px;
    height: 90px;
  }

  .modelNameStyle {
    white-space: nowrap;
    height: 50px;
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
}
</style>



<template>
  <transition name="fade">
    <div>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header>
          <h1>产品详情</h1>
        </header>
        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">产品基础信息</el-col>
          <el-col :span="22" class="rightLine"><hr></el-col>
        </el-row>
        <div>
          <el-form label-width="165px" class="demo-ruleForm">
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品编码：">
                  {{productBaseDetail.code}}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品名称：">
                  {{productBaseDetail.name}}
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品型号：">
                  <b v-if="productBaseDetail.modelNumber">{{productBaseDetail.modelNumber}}</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品分类：">
                  <b v-if="productBaseDetail.attrNames">{{productBaseDetail.attrNames}}</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品品牌：">
                  {{productBaseDetail.brandName}}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品系列：">
                  <b v-if="productBaseDetail.seriesName">{{productBaseDetail.seriesName}}</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品款式：">
                  <b v-if="productBaseDetail.modelingName">{{productBaseDetail.modelingName}}</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="长：">
                  <b v-if="productBaseDetail.length">{{productBaseDetail.length}} cm</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="宽：">
                  <b v-if="productBaseDetail.width">{{productBaseDetail.width}} cm</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="高：">
                  <b v-if="productBaseDetail.height">{{productBaseDetail.height}} cm</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品规格：">
                  {{productBaseDetail.spec}}
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="是否有特效：">
              <span>{{productBaseDetail.isSpecialEffects ? '是' : '无'}}</span>
            </el-form-item>
            <el-row :gutter="24" v-if="productBaseDetail.isBedsideDimensions == 1">
              <el-col :span="8">
                <el-form-item label="床沿尺寸：">
                  <b v-if="productBaseDetail.bedsideDimensions">{{productBaseDetail.bedsideDimensions}} cm</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="" v-if="productBaseDetail.ceilingCrossSectionDataDTOList" v-for="(item,index) in productBaseDetail.ceilingCrossSectionDataDTOList">
                <el-col :span="8">
                    <el-form-item :label="'截面数据'+(++index)+'：'"><span class="beakSpan">{{item.ceilingInfo}}</span></el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item :label="'灯带数据'+index+'：'"><span class="beakSpan">{{item.lightInfo}}</span></el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="适用面积："><span class="beakSpan">{{item.applyArea}}</span></el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="24" v-if="productBaseDetail.proCtopsBO">
              <el-col :span="8">
                  <el-form-item label="挡水边(靠墙)截面数据：">{{productBaseDetail.proCtopsBO.crossSectionDataForWall}}</el-form-item>
              </el-col>
              <el-col :span="8">
                  <el-form-item label="挡水边(外)截面数据：">{{productBaseDetail.proCtopsBO.crossSectionDataForOut}}</el-form-item>
              </el-col>
              <el-row :gutter="24" v-if="productBaseDetail.proCtopsBO">
              </el-row>
              <el-col :span="8">
                  <el-form-item label="挡水边(靠墙)材质 :">
                      <div class="imgRows">
                        <img v-if="productBaseDetail.proCtopsBO.texturePicPathForWall" :src="BASE_URL.sourceBaseUrl+productBaseDetail.proCtopsBO.texturePicPathForWall">
                      </div>
                  </el-form-item>
              </el-col>
            
              <el-col :span="8">
                  <el-form-item label="挡水边(外)材质 :">
                      <div class="imgRows">
                      
                      <img v-if="productBaseDetail.proCtopsBO.texturePicPathForOut" :src="BASE_URL.sourceBaseUrl+productBaseDetail.proCtopsBO.texturePicPathForOut">
                      </div>
                  </el-form-item>
              </el-col>
            </el-row>
            <div v-if="productBaseDetail.props"
                 style="margin:1px 1px 20px 1px; width: 800px; padding-top:15px; background: #f8f8f8">
              <h2 class="propTitle"><span class="el-icon-setting"></span>产品规格信息</h2>
              <el-row :gutter="24">
                <el-col :span="24" :key="index" v-for="(item,index) in productBaseDetail.props" >
                  <el-form-item :label="item.parent.name + '：'">
                    {{item.name}}
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            
            <!--<el-row>-->
            <!--<el-col>-->
            <!--<el-form-item label="产品规格：">-->
            <!--无-->
            <!--</el-form-item>-->
            <!--</el-col>-->
            <!--</el-row>-->
            <el-row :gutter="24" class="splineFrame" v-if="productClassList && productClassList.length > 0">
              <el-col :span="2" class="productBaseInfo">关联产品运营信息</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>
            <el-row class="goods-operating-infos" v-for="(item, index) in productClassList" :key="index" v-if="item.list.length > 0">
              <div class="headline">
                {{item.name}}：
              </div>
              <div class="content">
                <p v-for="(items, index) in item.list" :key="index">{{items}}</p>
              </div>
            </el-row>
            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">产品描述信息</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品风格：">
                  <span v-if="productBaseDetail.baseStyleName">
                    {{productBaseDetail.baseStyleName.join('，')}}
                  </span>
                  <span v-else>—无—</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <!--<el-col :span="8">-->
              <!--<el-form-item label="建议售价：">-->
              <!--<b v-if="productBaseDetail.advicePrice">{{productBaseDetail.advicePrice}} 元</b>-->
                  <!--<b v-else>—无—</b>-->
                <!--</el-form-item>-->
              <!--</el-col>-->
              <el-col :span="12">
                <el-form-item label="原价：">
                  <b v-if="productBaseDetail.price">{{productBaseDetail.price}} 元</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="装修单价：">
                  <b v-if="productBaseDetail.decorationPrice">{{productBaseDetail.decorationPrice }} {{productBaseDetail.valuationUnit}} </b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="产品图片：">
                  <ul class="showPic">
                    <li :key="index" v-for="(item,index) in fileList2">
                      <img :src="item.path" alt="">
                      <span v-if="item.id == productBaseDetail.defaultPicId">主缩略图</span>
                    </li>
                  </ul>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品描述：">
                  <div class="productRemark">
                    {{productBaseDetail.desc}}
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24" class="splineFrame" v-if="productBaseDetail.model">
              <el-col :span="2" class="productBaseInfo">模型相关信息</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>
            <el-row :gutter="24" v-if="productBaseDetail.model" class="mtFrame">
              <el-form-item label="模型和贴图：">
                <el-col :span="21">
                  <el-row class="useModel">
                    <el-col :span="3">
                      <div v-if="productBaseDetail.model.thumbPicPath"><img
                              :src="BASE_URL.sourceBaseUrl+productBaseDetail.model.thumbPicPath"/></div>
                      <div v-else class="noimg">无图片</div>
                    </el-col>
                    <el-col :span="18" class="modelTextureName">
                      <el-col> 模型名称: {{productBaseDetail.model.modelName}}</el-col>
                      <el-col> 模型编号: {{productBaseDetail.model.modelCode}}</el-col>
                      <p>模型的规格信息：长 {{productBaseDetail.model.length}} X 宽 {{productBaseDetail.model.width}} X 高 {{productBaseDetail.model.height}}</p>
                    </el-col>
                  </el-row>
                  <el-row :gutter="24" v-if="productBaseDetail.modelTextureInfo">
                    <el-col class="modelTextNo"  v-for="it in productBaseDetail.modelTextureInfo" :key="it.id" >
                      <div v-if="it.areaTextures">
                        <el-col :span="3" class="areaNo">
                          <el-col class="nameWidth">{{it.name}}：</el-col>
                          <el-col class="price">影响价格 / 元：</el-col>
                        </el-col>
                        <el-col class="modelTextureFrame" v-for="jt in it.areaTextures" :key="jt.id">
                          <el-col v-if="it.defalutTextureId === jt.id" class="mainPic">主缩略图</el-col>
                          <el-col class="imgArea"><img :src="BASE_URL.sourceBaseUrl+jt.picPath" width="90" height="90"/></el-col>
                          <!--<el-col class="noImgArea" v-show="!jt.picPath">暂无图片</el-col>-->
                          <el-col class="affectPrice">{{jt.affectPrice}}</el-col>
                        </el-col>
                      </div>
                    </el-col>
                  </el-row>
                </el-col>
              </el-form-item>
            </el-row>
            <el-row :gutter="24" class="splineFrame"  v-if="productBaseDetail.texture">
              <el-col :span="2" class="productBaseInfo">贴图相关信息</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>
            <el-row class="mleft" v-if="productBaseDetail.texture && productBaseDetail.texture.id">
              <el-form-item label="模型和贴图：">
                <el-col :span="2" class="modelTextureImg">
                  <img :src="BASE_URL.sourceBaseUrl+productBaseDetail.texture.picPath" height="90" width="90"/>
                </el-col>
                <el-col :span="10" class="modelTextureName">
                  <el-col> 贴图名称: {{productBaseDetail.texture.name}}</el-col>
                  <el-col> 贴图编号: {{productBaseDetail.texture.code}}</el-col>
                </el-col>
              </el-form-item>
            </el-row>
            <!--<div class="splitLine"></div>
            <el-row :gutter="24" v-if="productBaseDetail.channelAllotStatus || productBaseDetail.onlineAllotStatus">
              <el-col :span="12">
                <el-form-item label="分配渠道：">
                  <span v-if="productBaseDetail.channelAllotStatus">{{productBaseDetail.channelAllotStatus === 0 ? '渠道未分配' : '渠道已分配'}}</span>
                  <span v-if="productBaseDetail.onlineAllotStatus"> | {{productBaseDetail.onlineAllotStatus === 0 ? '线上未分配' : '线上已分配'}}</span>
                </el-form-item>
              </el-col>
            </el-row>-->
          </el-form>
          <!--硬装产品模块-->
          <hardProductDetail :productBaseDetail="productBaseDetail"></hardProductDetail>
          <!--查看规格详情-->
          <lookProductInfo></lookProductInfo>
        </div>
      </div>
    </div>
  </transition>
</template>
<script>
    import {mapState} from 'vuex'
    import ElCol from 'element-ui/packages/col/src/col'
    import hardProductDetail from './conponents/hardProductDetail' //硬装详情页面
    import lookProductInfo from './conponents/lookProductInfo'

    export default {
        components: {ElCol,hardProductDetail,lookProductInfo},
        data () {
            return {
                nav: [
                    { nav: "产品库", url: "/product" },
                    { nav: "产品详情", url: "" }
                ],
                productBaseDetail: {
                    model: {}
                }, //  产品详情
                productClassList: [], // 选择完产品分类，所需要展示在页面上的分类列表
                productClassLists: [], // 零时保存产品分类列表
                fileList2: [],
                productDetailID: ''
            }
        },
        created () {
            this.productDetailID = this.$route.params.proID
            this.getProductDetail()
        },
        mounted () {
            this.$store.commit('breadcrumb3', '产品详情')
        },
        methods: {
            /*页面加载后把请求后没有处理过的数据（this.showProductClassList）处理成需要的结构，在页面创建前调用*/
            disposeProductClassList() {
                let arr = [];
                if(this.showProductClassList){
                  for (let i = 0; i < this.showProductClassList.length; i++) {
                      let obj = {};
                      obj.name = this.showProductClassList[i].parentName;
                      obj.list = [];
                      arr.push(obj);
                  }

                }
                for (let i = 0; i < arr.length; i++) {
                    for (let j = i + 1; j < arr.length; j++) {
                        if (arr[i].name == arr[j].name) {
                            arr.splice(j, 1);
                            j--
                        }
                    }
                }
                if(this.showProductClassList){
                  for (let i = 0; i < this.showProductClassList.length; i++) {
                      for (let j = 0; j < arr.length; j++) {
                          if (this.showProductClassList[i].parentName == arr[j].name) {
                              arr[j].list.push(this.showProductClassList[i].name)
                          }
                      }
                  }
                }
                this.productClassList = arr;
            },
            getProductDetail () {
                this.API.ProductDetail({
                    productId: this.productDetailID,
                    platformType: 'library'
                }).then((res) => {
                    this.productBaseDetail = res.data
                    this.fileList2 = res.data.picInfos;
                    this.showProductClassList = res.data.categoryNodes;
                    this.disposeProductClassList();
                })
            },
            /* 上传图片 */
            submitForm (formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        alert('submit!')
                    } else {
                        return false
                    }
                })
            },
            resetForm (formName) {
                this.$refs[formName].resetFields()
            }
        },
        computed: mapState({
            productId: state => state.productId
        })
    }
</script>
<style lang="scss" scoped>
  @import "../../../assets/css/mixin";
  .productImgShow {
    position: relative;
    span {
      position: absolute;
    }
  }
  .beakSpan{
    display: inline-block;
    width: 100%;
    word-break: break-all;
  }
  .mtFrame {
    padding-left: 20px;
    min-width: 1000px;
  }
  .goods-operating-infos{
    display: flex;
    justify-content: left;
    .headline{
      font-size: 14px;
      min-width: 110px;
      text-align: right;
      line-height: 40px;
      margin-left: 20px;
    }
    .content{
      display: inline-block;
      line-height: 40px;
      p{
        display: inline-block;
        padding: 0 12px 0 12px;
        height: 12px;
        line-height: 12px;
        color: #333333;
        border-right: 1px solid #dddddd;
      }
    }
    .el-col-24{
      padding: 5px 0 5px 20px;
      height: 40px;
      line-height: 30px;
      h2{
        display: inline-block;
        font-size: 14px;
        width: 110px;
        text-align: right;
      }
      p{
        display: inline-block;
        padding: 0 12px 0 12px;
        height: 12px;
        line-height: 12px;
        color: #333333;
        border-right: 1px solid #dddddd;
      }
      span{
        color: #ff6419;
        margin-left: 10px;
        cursor:pointer
      }
    }
  }
  .mainPic {
    width: 76px;
    position: absolute;
    top: 4px;
    left: 12px;
    padding-left: 6px;
    text-align: left;
    height: 22px;
    line-height: 22px;
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
    z-index: 111;
    color: #fff;
    font-size: 12px;
    background: #ff3f3f;
  }
  .demo-ruleForm{
    b{
      font-weight: normal;
    }
  }
  .useModel {
    height: 110px;
    margin-bottom: 15px;
    box-sizing: padding-box;
    img {
      height: 90px;
      width: 90px;
      border: dashed 1px #e3e3e3;
      margin: 10px;
    }
    .noimg {
      margin: 10px;
      height: 90px;
      line-height: 90px;
      text-align: center;
      width: 90px;
      border: dashed 1px #e3e3e3;
    }
  }
  .modelTextureName {
    min-width: 400px;
    padding-top: 5px;
    .el-col {
      line-height: 35px;
      color: #666;
    }
  }
  .areaNo {
    font-size: 12px;
    height: 110px;
    position: relative;
    .price {
      position: absolute;
      bottom: 0;
    }
    .nameWidth {
      width: 110px;
    }
  }
  .modelTextNo {
    border-bottom: solid 1px #e3e3e3;
    margin-bottom: 20px;
  }
  .modelTextureFrame {
    position: relative;
    width: 90px;
    margin-right: 10px;
    .noImgArea{
      border: dashed 1px #e3e3e3;
      height: 90px;
      width: 90px;
      background: #f6f6f6;
      line-height: 90px;
      text-align: center;
      overflow: hidden;
    }
    .imgArea {
      border: dashed 1px #e3e3e3;
      height: 90px;
      width: 90px;
      background: #f6f6f6;
      position: relative;
      overflow: hidden;
      img {
        height: 50px;
        width: 50px;
        position: absolute;
        left: 50%;
        right: 50%;
        border-radius: 6px;
        transform: translate(-50%, 34%);
        border: dashed 1px #666;
        overflow: hidden;
      }
    }
    .affectPrice {
      height: 30px;
      line-height: 30px;
      text-align: center;
      width: 90px;
      font-weight: bold;
      color: #666;
    }
  }
  .showPic {
    background: #f2f2f2;
    border-radius: 4px;
    padding-left: 15px;
    li {
      display: inline-block;
      margin-right: 15px;
      margin-top: 15px;
      position: relative;
      height: 100px;
      width: 100px;
      overflow: hidden;
      border-radius: 6px;
      border: dashed 1px #d7dce5;
      background-color: #fff;
      img {
        height: 100px;
        width: 100px;
        object-fit: contain;
      }
      span {
        position: absolute;
        top: 4px;
        background: #ff3f3f;
        border-bottom-right-radius: 14px;
        border-top-right-radius: 14px;
        width: 70px;
        height: 26px;
        font-size: 12px;
        line-height: 26px;
        text-align: center;
        color: #fff;
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
  .addProduct {
    background: #fff;
    box-sizing: padding-box;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: $borderRadius;
    header {
      position: relative;
      overflow: hidden;
      h1 {
        font-weight: normal;
        font-size: 18px;
        padding-bottom: 15px;
        color: $greyOverButton;
      }
      p{
        position: absolute;
        top: 12px;
        left: 100px;
        width: 100%;
        height: 0;
        padding: 0;
        border: 1px solid $greyBorderEEE;
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
  .modelTextureImg {
    width: 90px;
    height: 90px;
    margin-right: 25px;
    border: dashed 1px #ddd;
    border-radius: 6px;
    overflow: hidden;
  }
  .modelTextureFrame {
    margin-left: 15px;
  }
  .el-select .el-input {
    width: 130px;
    margin-right: -10px;
  }
  .mleft {
    margin-left: 15px;
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
.imgRows{
  width: 90px;
  height: 90px;
}
div.el-form-item__content{
  word-break: break-all;
}
.imgRows img{
  width: 100%;
  height: 100%;
  border-radius: 4px;
}
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
    transform: translate(0, -50%)
  }
  .el-progress--circle .el-progress__text i {
    vertical-align: middle;
    display: inline-block
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

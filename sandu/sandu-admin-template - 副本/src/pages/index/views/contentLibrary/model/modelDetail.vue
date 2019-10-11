<template>
  <transition name="fade">
    <div>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header>
          <h1>模型详情</h1>
        </header>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">模型基础信息</el-col>
          <el-col :span="22" class="rightLine"><hr></el-col>
        </el-row>

        <div>
          <el-form label-width="120px" class="demo-ruleForm">
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="模型编号：">
                  {{model.modelCode}}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="模型作者：">
                  {{model.author}}
                </el-form-item>
              </el-col>

            </el-row>

            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="最后修改时间：">
                  {{model.updateDate}}
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="模型分类：">
                  {{model.typeNames}}
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="模型名称：">
                  {{model.modelName}}
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="模型型号：">
                  {{model.modelModelNum}}
                </el-form-item>
              </el-col>

            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="模型缩略图：">
                  <img style="cursor: pointer" title="点击查看大图" :src="BASE_URL.sourceBaseUrl+model.thumbPicPath" @click="showBigImg" width="100" height="100" v-if="model.thumbPicPath"/>
                  <div class="noImg" v-else> 无缩略图</div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="模型描述：">
                  {{model.modelDesc || '无'}}
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">模型3d文件</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>

            <el-row>
              <el-form-item label="模型3d文件：">
                {{model.file3duPath ? model.file3duPath.slice(model.file3duPath.lastIndexOf('/') + 1) : '无' }}
                <el-button type="primary" v-if="model.file3duPath" round @click="download">下载模型</el-button>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="模型处理状态：">
                {{transResult}}
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="模型规格：">
                <span
                  v-if="model.width && model.length && model.height">长 {{model.length}} X 宽 {{model.width}} X 高 {{model.height}}</span>
                <span v-else>无</span>
              </el-form-item>
            </el-row>

            <!--材质贴图区域-->
            <el-row :gutter="24" class="splineFrame" v-if="model.modelTextureInfo && model.modelTextureInfo.length > 0">
              <el-col :span="2" class="productBaseInfo">材质贴图区域</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>
            <el-row class="texture">
              <el-col :span="24" class="texture-area" v-if="model.modelTextureInfo && model.modelTextureInfo.length > 0">
                <div class="texture-area-item" v-for="(item, index) in model.modelTextureInfo">
                  <div class="header">
                    <div>
                      <span class="texture-code">{{Number(item.areaCode) + 1}}</span>
                      <span class="texture-name">材质区域名称：{{item.areaName}}</span>
                      <span class="setUV">UV(cm):
                        <em>长</em><el-input class="texture-name" size="mini" disabled v-model="item.height"></el-input>
                        <em>宽</em><el-input class="texture-name" size="mini" disabled v-model="item.width"></el-input>
                      </span>
                      <!--<span>初始材质：长20&nbsp;&nbsp;宽20</span>-->
                    </div>
                  </div>
                  <div class="content">
                    <div class="content-item"
                         v-for="(ImgItem, imgIndex) in item.rels">
                      <span class="default-icon" v-if="ImgItem.defaultFlag">主缩略图</span>
                    <template>
                        <el-popover
                          placement="top-start"
                          width="300"
                          trigger="hover"
                        >
                        <div>
                          <p>材质编码:{{item.rels[imgIndex].textureCode}}</p>
                          <p>材质型号:{{item.rels[imgIndex].modelNumber}}</p>
                          <p>材质名称:{{item.rels[imgIndex].textureName}}</p>

                        </div>
                          <img slot="reference" :src="BASE_URL.sourceBaseUrl + (ImgItem.thumbnailPath || ImgItem.picPath)">
                            </el-popover>
                    </template>
                     <!-- <img :src="BASE_URL.sourceBaseUrl + ImgItem.picPath"> -->
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
            <!--材质贴图区域-->
          </el-form>

        </div>
      </div>
      <el-dialog :visible.sync="showBigImageVisible">
        <img :src="BASE_URL.sourceBaseUrl+model.thumbPicPath" width="100%"/>
      </el-dialog>
    </div>
  </transition>
</template>

<script>
  import { mapState } from 'vuex'
  // import ElButton from '../../../../node_modules/element-ui/packages/button/src/button.vue'

  export default {
    // components: {ElButton},
    data () {
      return {
          nav: [
              { nav: "模型库", url: "/model" },
              { nav: "模型详情", url: "" }
          ],
        textureList: [],
        showBigImageVisible: false,
        money: '',
        input5: '',
        value: '',
        select: '',
        textarea: '',
        /* 模型基础信息 */
        productBaseDetail: {}, //  模型详情
        /* 模型基础信息 end */
        productDetailID: '',
        transResult: '',
        model: {}
      }
    },
    created () {
      this.modelId = this.$route.params.modelId
      this.getModelDetail()
    },
    mounted () {
    },
    methods: {
      showBigImg () {
        this.showBigImageVisible = true;
      },
      download () {
        window.location.href = this.BASE_URL.sourceBaseUrl + this.model.file3duPath
      },
      getModelDetail () {
        this.API.GetModel(this.modelId).then((res) => {
          this.model = res.data;
          this.model.modelTextureInfo.sort((a, b) => {
              return a.areaCode - b.areaCode;
          });
          this.model.typeNames = this.model.typeNames.join(',')
          let trans = this.model.transStatus
          if (trans === 'ING') {
            this.transResult = '转化中'
          }
          if (trans === 'SUCCESS') {
            this.transResult = '转化成功'
          }
          if (trans === 'FAIL') {
            this.transResult = '转化失败'
          }
          if (trans === 'HANDLE') {
            this.transResult = '需要手动处理'
          }
        })
      },
      /* 上传图片 */
      handleAvatarSuccess (res, file) {
        this.imageUrl = URL.createObjectURL(file.raw)
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
    .texture{
      padding: 0 30px;
      .texture-btn{
        margin-bottom: 20px;
      }
      .texture-area{
        background-color: #f6f6f6;
        padding: 0 10px 10px 10px;
        .texture-area-item{
          background-color: #ffffff;
          margin-top: 10px;
          .header{
            line-height: 28px;
            padding: 10px;
            border-bottom: 2px solid #f6f6f6;
            display: flex;
            justify-content: space-between;
            div{
              .texture-code{
                margin-right: 10px;
                color: #ff6419;
              }
              .texture-name{
                width: 100px;
                margin-right: 20px;
              }
              span{
                color: #666;
              }
              .setUV{
                em{
                  font-style: normal;
                  display: inline-block;
                  width: 40px;
                  border: 1px solid #ddd;
                  border-radius: 4px;
                  height: 28px;
                  line-height: 28px;
                  text-align: center;
                  box-sizing: border-box;
                  vertical-align: middle;
                  border-right: none;
                }
              }
            }
            span{
              color: #FF6419;
              cursor:pointer;
            }
          }
          .content{
            padding: 15px 15px 0 15px;
            display: flex;
            justify-content: left;
            //height: 105px;
            flex-wrap: wrap;
            .content-item{
              width: 90px;
              height: 90px;
              overflow: hidden;
              margin-right: 10px;
              margin-bottom: 15px;
              position: relative;
              .default-icon{
                position: absolute;
                top: 5px;
                left: 0;
                width: 65px;
                height: 20px;
                line-height: 20px;
                font-size: 8px;
                color: #ffffff;
                text-align: center;
                background-color: #FF6419;
                border-bottom-right-radius: 10px;
                border-top-right-radius: 10px;
              }
              .default-select{
                position: absolute;
                bottom: -20px;
                left: 0;
                width: 100%;
                height: 20px;
                text-align: center;
                background-color: #FF6419;
                color: #ffffff;
                cursor: pointer;
                transition: all 0.3s linear;
              }
              .default-select-active{
                position: absolute;
                bottom: 0px;
                left: 0;
                width: 100%;
                height: 20px;
                text-align: center;
                background-color: #FF6419;
                color: #ffffff;
                cursor: pointer;
                transition: all 0.3s linear;
              }
              .content-item-delete{
                position: absolute;
                display: block;
                top: 36px;
                left: 36px;
                width: 18px;
                height: 16px;
                background: url("../../../assets/images/icons/delete3.png");
              }
              img{
                height: 90px;
                width: 90px;
                border-radius: 5px;
                border: 1px solid #eee;
                box-sizing: border-box;
                display: block;
              }
            }
          }
        }
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
  .noImg{
    display: inline-block;
    background: #f2f2f2;
    height: 80px;
    width: 80px;
    line-height: 80px;
    text-align: center;
    font-size: 12px;
    color: #999;
    border: solid 1px #e3e3e3;
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


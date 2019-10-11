<template>
  <transition name="fade">
    <div>
      <!--<pos-nav></pos-nav>-->
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header>
          <h1>详情</h1>
        </header>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">产品基础信息</el-col>
          <el-col :span="22" class="rightLine"><hr></el-col>
        </el-row>

        <div>
          <el-form label-width="120px" class="demo-ruleForm">
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品组合编码：">
                  {{productBaseDetail.code}}
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品组合名称：">
                  {{productBaseDetail.name}}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="品牌：">
                  {{productBaseDetail.brandName}}
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品组合型号：">
                  {{productBaseDetail.groupNumber}}
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="适用房间：">
                  {{productBaseDetail.houseType}}
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="适用房间面积：">
                  {{productBaseDetail.areaValue}} m&sup2;
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品组合类型：">
                  {{productBaseDetail.groupType}}
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="产品风格：">
                  {{productBaseDetail.styleName}}
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="建议售价：">
                  {{productBaseDetail.advicePrice}} 元
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="原价：">
                  {{productBaseDetail.price}} 元
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
                <el-form-item label="描述：">
                  <div class="productRemark">
                    {{productBaseDetail.desc}}
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">产品组合信息</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>

             <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="产品组合图片：">
                  <el-table
                    height="400"
                    :data="productGroupPic"
                    style="" class="prouctGroupPic">
                    <el-table-column
                      prop="code"
                      label="产品编码"
                      width="180">
                    </el-table-column>
                    <el-table-column
                      prop="pic"
                      width="90"
                      align="center"
                      label="产品信息">
                      <template slot-scope="scope">
                          <img width="80" v-if="scope.row.pic" :src="scope.row.pic" alt="">
                      </template>
                    </el-table-column>
                    <el-table-column
                      align="left"
                      prop="name">
                      <template slot-scope="scope">
                        <p v-if="scope.row.name"  style="padding:0">{{scope.row.name}}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      align="center"
                      label="是否为主产品"
                      prop="main">
                      <template slot-scope="scope">
                        <p>{{scope.row.main ? '是' : '否'}}</p>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-form-item>
              </el-col>
            </el-row>

          </el-form>

        </div>
      </div>
    </div>
  </transition>
</template>

<script>
  import { mapState } from 'vuex'
  import ElCol from 'element-ui/packages/col/src/col'

  export default {
    components: {ElCol},
    data () {
      return {
          nav: [
              { nav: "产品组合", url: "/product" },
              { nav: "产品组合详情", url: "" }
          ],
        productGroupPic: [],
        productBaseDetail: {}, //  产品详情
        fileList2: [],
        productDetailID: ''
      }
    },
      computed: mapState({
          activeName: state => state.activeName,
      }),
    created () {
      this.productDetailID = this.$route.params.proID
      this.getProductDetail()
    },
    mounted () {
      this.$store.commit('breadcrumb3', '产品详情')
    },
    methods: {
      getProductDetail () {
        this.API.ProductGroupDetail({
          groupId: this.productDetailID,
          platformType: 'library'
        }).then((res) => {
          console.log(res, 'groupData')
          this.productBaseDetail = res.data
          this.productGroupPic = res.data.products
          this.fileList2 = res.data.picInfos
        })
      },
      /* 上传图片 */
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!')
          } else {
            console.log('error submit!!')
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

  .mtFrame {
    padding-left: 20px;
    min-width: 1000px;
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

  .useModel {
    border-bottom: solid 1px #e3e3e3;
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
    padding-top: 20px;
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
    padding-top: 15px;
    border-radius: 4px;
    padding-left: 15px;
    li {
      display: inline-block;
      margin-right: 15px;
      position: relative;
      height: 100px;
      width: 100px;
      overflow: hidden;
      border-radius: 6px;
      border: dashed 1px #d7dce5;
      img {
        height: 100px;
        width: 100px;
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
<style lang="scss">
  .el-upload-list__item img {
    width: 90px;
    height: 90px;
  }
  .prouctGroupPic {
    border-left: solid 1px #f2f2f2;
  }
  .prouctGroupPic thead {
    height: 30px !important;
    background: #ff6419;
  }

  .prouctGroupPic th {
    height: 30px !important;
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


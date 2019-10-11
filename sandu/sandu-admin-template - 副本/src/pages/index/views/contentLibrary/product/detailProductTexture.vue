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
          <el-form label-width="100px" class="demo-ruleForm">
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
                <el-form-item label="产品型号：">
                  <b v-if="productBaseDetail.modelNumber">{{productBaseDetail.modelNumber}}</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品价格：">
                  <b v-if="productBaseDetail.price">{{productBaseDetail.price}} 元</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="装修单价：">
                  <b v-if="productBaseDetail.decorationPrice">{{productBaseDetail.decorationPrice }} {{productBaseDetail.valuationUnit}} </b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="产品风格：">
                  <b v-if="productBaseDetail.baseStyleName">
                    {{productBaseDetail.baseStyleName.join(',')}}</b>
                  <b v-else>—无—</b>
                </el-form-item>
              </el-col>  
            </el-row>
            
            
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="长度cm：">
                  {{texture.fileWidth}}
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="宽度cm：">
                  {{texture.fileLength}}
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="产品规格：">
                  {{productBaseDetail.spec}}
                </el-form-item>
              </el-col>
            </el-row>

             <el-row>
               <el-col :span="8">
                 <el-form-item label="一石多面：">
                   {{productBaseDetail.isComplexParquet == 1 ? '是' : '否'}}
                 </el-form-item>
               </el-col>
             </el-row>
            <el-form-item label="运营分类：">
            <el-row class="goods-operating-infos" v-for="(item, index) in productClassList" :key="index" v-if="item.list.length > 0">
              <div class="headline">
                {{item.name}}：
              </div>
              <div class="content">
                <p v-for="(items, index) in item.list" :key="index">{{items}}</p>
              </div>
            </el-row>
            </el-form-item>


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

           <!-- <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="材质图片：">
                  <img :src="BASE_URL.sourceBaseUrl+texture.thumbnailPath" width="90" height="90"/>
                </el-form-item>
              </el-col>
            </el-row>-->



            <el-row  :gutter="24">
              <el-col :span="8">
                <el-form-item  label="材质材料：">
                  {{texture.textureLabel || '无'}}
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="表面属性：">
                   {{texture.textureAttrValueName || '无'}}
                </el-form-item>
              </el-col>
            </el-row>
<!--
            <el-row :gutter="24">
              <el-col :span="12">
              <el-form-item label="法线贴图："> &lt;!&ndash;BASE_URL.sourceBaseUrl+&ndash;&gt;
                <img v-if="texture.normalPicPath" :src="texture.normalPicPath" width="90" height="90"/>
                <span v-else>无</span>
              </el-form-item>
              </el-col>
            </el-row>

            <el-row  :gutter="24">
              <el-form-item label="法线参数：">
                {{texture.normalParam || '无'}}
              </el-form-item>
            </el-row>-->


            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="产品描述：">
                  <div class="productRemark">
                    {{productBaseDetail.desc || '无'}}
                  </div>
                </el-form-item>
              </el-col>
              
            </el-row>

            <el-row>
              <el-form-item label="贴图信息：" style="margin: 0; padding:0"> </el-form-item>
            </el-row>

            <el-row class="addGroup" v-for="(item,index) in addGroup" :key="index">
              <div v-if="productBaseDetail.isComplexParquet==1||(productBaseDetail.isComplexParquet!=1&&index==0)">

                <el-col :span="7">
                  <el-form-item >
                    <span >材质图片:</span>
                    <img v-if="item.thumbnailPath" :src="item.thumbnailPath" width="90"  height="90" alt="">
                    <p v-else  class="noImg">无图</p>
                  </el-form-item>
                </el-col>

                <el-col :span="7">
                  <el-form-item>
                    <span>法线贴图:</span>
                    <!--@deleteClick="deleteNormalImage"-->
                    <img v-if="item.normalPicPath" :src="item.normalPicPath" width="90"  height="90" alt="">
                    <div v-else  class="noImg">无图</div>
                  </el-form-item>
                </el-col>

                <el-col :span="4">
                  <el-form-item>
                    <span>法线参数:</span>
                    <b>{{item.normalParam || '无'}}</b>
                  </el-form-item>
                </el-col>
              </div>
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
  import hardProductDetail from './conponents/hardProductDetail'//硬装详情页面
  import lookProductInfo from './conponents/lookProductInfo'
  export default {
    components: {ElCol,hardProductDetail,lookProductInfo},
    data () {
      return {
          nav: [
              { nav: "产品库", url: "/product" },
              { nav: "产品详情", url: "" }
          ],
          textrueList:[],
          addGroup:[],
        productBaseDetail: {
          model: {}
        }, //  产品详情
          productClassList: [], // 选择完产品分类，所需要展示在页面上的分类列表
          productClassLists: [], // 零时保存产品分类列表.
          textureId:'',
        fileList2: [],
        productDetailID: '',
        texture:{},
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
            for (let i = 0; i < this.showProductClassList.length; i++) {
                let obj = {};
                obj.name = this.showProductClassList[i].parentName;
                obj.list = [];
                arr.push(obj);
            }
            for (let i = 0; i < arr.length; i++) {
                for (let j = i + 1; j < arr.length; j++) {
                    if (arr[i].name == arr[j].name) {
                        arr.splice(j, 1);
                        j--
                    }
                }
            }
            for (let i = 0; i < this.showProductClassList.length; i++) {
                for (let j = 0; j < arr.length; j++) {
                    if (this.showProductClassList[i].parentName == arr[j].name) {
                        arr[j].list.push(this.showProductClassList[i].name)
                    }
                }
            }
            this.productClassList = arr;
        },
        getTextureDetail () {

        this.API.GetTexture(this.textureId).then((res) => {
          this.texture = res.data
        })
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
          this.textureId = res.data.materialPicIds;
            let tid = this.textureId.split(",");
            if(tid){
             tid.map((res,index) => {
                    if(index>0&&this.productBaseDetail.isComplexParquet!=1) return;
                    this.API.GetTexture(res).then(resp => {
                        resp.data.textureId= res
                        resp.data.texture = resp.data.texture = resp.data.texture ? parseInt(resp.data.texture) : 0
                        this.texture = resp.data;
                        resp.data.thumbnailPath.indexOf('http')>-1 ? '' : resp.data.thumbnailPath=this.BASE_URL.sourceBaseUrl + resp.data.thumbnailPath;//是否有域名
                        this.textrueList.push(resp.data)
                        this.addGroup = this.textrueList;

                    })

                })

            }
          //this.getTextureDetail()
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
  .addGroup{
    margin-left: 100px;
    margin-bottom: 10px;
    padding-left: 10px;
    width: 1000px;
    background: #f5f5f5;
    position: relative;
    .el-col{
      width:330px;
      height: 130px;
      &.last{
        width: 60px;
        height: 24px;
        top: 55px;
        position: absolute;
        right: 0;
        cursor: pointer;
      }
    }
    .noImg{
      width: 90px;
      height: 90px;
      background: rgba(0,0,0,0.15);
      font-size: 22px;
      line-height: 90px;
      text-align: center;
      margin-top: 20px;
      color: rgba(0,0,0,0.1);
      border:solid 1px #e8e8e8;
    }
    img{
      margin-top: 20px;
    }
    b{
      display: inline-block;
      margin-top: 45px;
    }
    span{
      position: absolute;
      top: 45px;
      left: -100px;
    }
    .el-input{
      margin-top: 45px;
      margin-left: -30px;
    }
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


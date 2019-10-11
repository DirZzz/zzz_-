<template>
  <transition name="fade">
    <div>
      <pos-nav></pos-nav>
      <div class="addProduct">
        <header>
          <h1>编辑产品</h1>
        </header>
        <p class="addProduct_p"><span>*</span>为必填项</p>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">产品组合基础信息</el-col>
          <el-col :span="22" class="rightLine"><hr></el-col>
        </el-row>

        <div>
          <el-form :model="product" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品组合编号：">
                  {{product.code}}
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品组合名称：" prop="name">
                  <el-input class="formWidth" v-model="product.name"/> <!--product.name-->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品组合类型：">
                  <el-select
                    v-model="product.groupValue"
                    class="formWidth"
                    placeholder="请选择">
                    <el-option
                      v-for="item in productGroupTypeDrop"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品组合型号：" prop="groupNumber">
                  <el-input class="formWidth" v-model="product.groupNumber"/>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="品牌：" prop="brandId">
                  <el-select
                    v-model="product.brandId"
                    class="formWidth"
                    placeholder="请选择">
                    <el-option
                      v-for="item in productBrand"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="适用房间：">
                  <el-select
                    v-model="product.houseValue"
                    @change="product.areaValue = ''"
                    class="formWidth"
                    placeholder="请选择适用房间">
                    <el-option
                      v-for="item in fitRoom"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="适用房间面积：">
                  <el-select
                    v-model="product.areaValue"
                    class="formWidth"
                    placeholder="请选择适用房间面积">
                    <el-option
                      v-for="item in productRoomArea"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>

            </el-row>

            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">产品描述信息</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="产品风格：">
                  <el-select
                    v-model="product.baseStyleId"
                    class="formWidth"
                    placeholder="请选择">
                    <el-option
                      v-for="item in baseProductStyle"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="建议售价：">
                  <el-input
                    placeholder="请输入内容"
                    v-model="product.advicePrice"
                    style="width: 300px"
                  >
                  </el-input>
                  元
                  <p v-if="product.advicePrice < 0" style="color: red">请输入大于等于0的数字</p>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="原价：">
                  <el-input
                    placeholder="请输入内容"
                    v-model="product.price"
                    class="input-with-select"
                    style="width: 300px"
                  >
                  </el-input>
                  元
                  <p v-if="product.price < 0" style="color: red">请输入大于等于0的数字</p>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="24">
                <el-form-item label="产品图片：">
                  <ul class="uploadPic">
                    <li>
                      <el-upload
                        class="avatar-uploader"
                        :action='BASE_URL.uploadUrl'
                        :file-list="fileList2"
                        :show-file-list="false"
                        :data="uploadParams"
                        list-type="picture-card"
                        multiple
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload">
                        <!--<i class="el-icon-plus avatar-uploader-icon"></i>-->
                        <div class="avatar-uploader-icon">
                          <div class="across"></div>
                          <div class="vertical"></div>
                        </div>
                        <p class="img-text">上传图片</p>
                      </el-upload>
                    </li>
                    <li class="uploadImgList" @mouseenter="showDelete(index)" @mouseout="showDelete2(index)"
                        v-for="(file, index) in fileList2" :key="index"
                    >
                      <span class="posDelete" v-if="showDeletes[index]" @mouseenter="showDelete(index)"
                            @click="deleteUploadImg(index)"></span>
                      <img style="border-radius: 8px;" @mouseenter="showDelete(index)" width="90" height="90"
                           :src="file.url"/>
                      <span v-if="file.id==defaultPicId" @mouseenter="showDelete(index)" class="upload-mainpic__con">主缩略图</span>
                      <span v-if="showDeletes[index]" @click="setDefaultPic(file)" @mouseenter="showDelete(index)"
                            class="upload-notmainpic__con">设置为缩略图</span>
                    </li>
                  </ul>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="24">
                <el-form-item label="产品描述：">
                  <el-input
                    type="textarea"
                    :rows="6"
                    placeholder="请输入内容"
                    v-model="product.desc">
                  </el-input>
                  <p>此处最多允许填写200字</p>
                </el-form-item>
              </el-col>
            </el-row>

            <!--<div class="productBaseInfo">产品组合相关信息</div>-->
            <!--<el-row :gutter="24">-->
              <!--<el-col :span="24">-->
                <!--<el-form-item label="组合文件：">-->
                  <!--<el-button round size="samll" class="el-button&#45;&#45;primary">重新上传-->
                  <!--</el-button>-->
                <!--</el-form-item>-->
              <!--</el-col>-->
            <!--</el-row>-->
            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">产品组合相关信息</el-col>
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
            <!--模型贴图区域-->
            <el-form-item>
              <el-button round class="confirmBtn marRight" type="primary" @click="submitForm('ruleForm')">确定</el-button>
              <el-button class="cancelBtnBg" round @click="resetForm('ruleForm')">取消</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!--模型贴图区域 start-->
      </div>
    </div>
  </transition>
</template>

<script>
  import ElRow from 'element-ui/packages/row/src/row'

  export default {
    components: {ElRow},
    data () {
      return {
        productGroupPic: [],
        productRoomArea: [],
        roomArea: 0,
        fitRoom: [],
        fitRoomArea: 0,
        showDeletes: [],
        area: [],
        selectedProps: [], // 选中的属性
        editProID: '',
        uploadParams: {
          platform: 'web',
          module: 'product',
          type: 'image'
        },
        selectStyleId: '', // 产品风格ID
        selectedSeriesId: '', // 产品系列ID
        selectedBrandId: '', // 产品品牌ID
        unitList: [], // 数字字典
        select: '',
        advicePriceInput1: '',
        advicePriceInput2: '',
        productCategory: '',
        seriesValue: '',
        productStyle: [], // 产品款式
        StyleValue: '',
        baseProductStyle: [], // 产品风格
        brandValue: '',
        baseValue: '',
        props: { // 产品分类属性名
          label: 'name',
          value: 'code',
          children: 'children'
        },
        // 上传图片文件列表
        imageUrl: '',
        fileList2: [],
        productBrand: [],
        product: {
          // categoryCodes: []
        }, // 产品更新
        // 品牌结束
        defaultPicId: 0,
        modelId: 0,
        // 验证表单规则
        rules: {
          name: [
            {required: true, message: '请输入产品组合名称', trigger: 'blur'},
            {min: 1, max: 40, message: '长度在 40 个字符', trigger: 'blur'}
          ],
          groupNumber: [
            {required: true, message: '请输入产品组合型号', trigger: 'blur'}
          ],
          brandId: [
            {required: true, message: '请选择产品品牌', trigger: 'change'}
          ]
        },
        // 20180327
        productGroupTypeDrop: [], // 获取产品组合类型下拉框
        productGroupTypeSelect: ''
      }
    },
    watch: {
      'product.houseValue': function (val) {
        this.selectRoomArea(val)
      }
    },
    mounted () {
      this.$store.commit('breadcrumb3', '产品编辑')
    },
    activated () {
      this.$store.commit('breadcrumb3', '产品编辑')
    },
    methods: {
      // 20180327 产品组合类型
      ProductGroupType () { // 下拉框产品分类
        this.API.ProductGroupType().then((res) => {
          this.productGroupTypeDrop = res.data
        })
      },
      // 试用房间
      getFitTypeHose () {
        this.API.FitTypeHose().then((res) => {
          this.fitRoom = res.data
          console.log(res, 'FitTypeHose')
        })
      },
      selectRoomArea (val) {
        // this.product.areaValue = 0
        if (val) {
          this.API.FitRoomArea({value: val}).then((res) => {
            if (res) {
              this.productRoomArea = res.data
            }
          })
        }
      },

      // 设置为主缩略图
      setDefault (imgs) {
        console.log(imgs, '主缩略图')
      },

      // 上传图片
      getProduct () {
        this.API.ProductGroupDetail({
          groupId: this.editProID,
          platformType: 'library'
        }).then((res) => {
          console.log(res.data, 'resdata')
          if (res) {
            res.data.brandId = parseInt(res.data.brandId)
            res.data.seriesId = parseInt(res.data.seriesId)
            res.data.modelingId = parseInt(res.data.modelingId)
            res.data.proStyleId = parseInt(res.data.proStyleId)
            res.data.baseStyleId = parseInt(res.data.styleValue)
            res.data.areaValue = parseInt(res.data.areaValue)
            this.product = res.data
            if (this.product.defaultPicId) {
              this.defaultPicId = this.product.defaultPicId
            }
            this.productGroupPic = res.data.products
            // if (res.data.modelTextureInfo) {
            //   this.area = res.data.modelTextureInfo.map(it => Object.assign({}, it, {areaTextures: it.areaTextures.map(tit => Object.assign({}, {thumbnailPath: tit.picPath}, tit))}))
            // }
            // this.getProductPorps = res.data.props;
            this.fileList2 = res.data.picInfos.map(res => {
              return {
                url: res.path,
                id: res.id
              }
            })
          }
        })
      },
      // 材质选择ID

      // 产品款式选择ID
      selectStyleID (val) {
        this.selectStyleId = val
      },
      // 产品系列选择ID
      selectedSeriesID (val) {
        this.selectedSeriesId = val
      },
      // 品牌选择ID
      selectedBrandID (val) {
        this.selectedBrandId = val
      },
      // 产品分类编号

      /* 上传图片 */
      handleAvatarSuccess (res, file) {
        // this.imageUrl = URL.createObjectURL(file.raw);
        this.fileList2.push({
          url: res.data.url,
          id: res.data.resId
        })
      },
      handleChange (file, fileList) {
        console.log(fileList, 'filelist')
      },
      deleteUploadImg (index) {
        this.fileList2.splice(index,1)
      },
      showDelete (index) {
        this.$set(this.showDeletes, index, true)
      },
      showDelete2 (index) {
        this.$set(this.showDeletes, index, false)
      },
      beforeAvatarUpload (file) {
        // const isJPG = file.type === 'image/jpeg'
        // const isLt2M = file.size / 1024 / 1024 < 15
        //
        // if (!isJPG) {
        //   this.$message.error('上传图片只能是 JPG 格式!')
        // }
        // if (!isLt2M) {
        //   this.$message.error('图片大小不能超过 15MB!')
        // }
        // return isJPG && isLt2M
        const isJPG = file.type === 'image/jpeg'
        const isPNG = file.type === 'image/png'
        const isLt2M = file.size / 1024 / 1024 < 15

        if (!isJPG && !isPNG) {
          this.$message.error('上传图片只能是 JPG 或 PNG 格式!')
        }
        if (!isLt2M) {
          this.$message.error('图片大小不能超过 15MB!')
        }
        return (isJPG || isPNG) && isLt2M
      },

      /* 上传图片 结束 */
      // 产品系列下拉框

      setDefaultPic (file) {
        this.defaultPicId = file.id
      },

      submitForm (formName) {
        /*
        "advicePrice": 0,
        "areaValue": 0,
        "brandId": 0,
        "desc": "string",
        "groupNumber": "string",
        "groupValue": 0,
        "houseValue": 0,
        "id": 0,
        "name": "string",
        "picId": 0,
        "picIds": "string",
        "platformType": "string",
        "price": 0
        * */
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let formDatas = {
              advicePrice: this.product.advicePrice, //  (number, optional): 建议售价 ,
              price: this.product.price, //  (number, optional): 售价 ,
              brandId: this.product.brandId, //  (integer): 品牌ID ,
              areaValue: this.product.areaValue,
              groupNumber: this.product.groupNumber,
              groupValue: this.product.groupValue,
              houseValue: this.product.houseValue,
              code: this.product.code, //  (string): 产品编码 ,
              desc: this.product.desc, //  (string, optional): 产品描述 ,
              platformType: 'library', //  (string, optional): 编辑类型, ,
              id: this.editProID, //  (integer): 产品id ,
              userId: sessionStorage.getItem('userId'),
              name: this.product.name, //  (string): 产品名称 ,
              saleUnionId: this.product.saleUnionId, //  (integer, optional): 售价单位 ,
              styleId: this.product.baseStyleId, //  (integer, optional): 产品风格,
              picIds: this.fileList2.map(item => item.id).join(','),
              picId: this.defaultPicId
            }
            for (let key in formDatas) {
              if (formDatas[key] == '' || formDatas[key] == undefined || formDatas[key] == null) {
                delete formDatas[key]
              }
            }
            this.$confirm('确定更新产品组合', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              cancelButtonClass: 'cancelButtonClass',
              confirmButtonClass: 'confirmButtonClass',
              type: 'info',
              center: true
            }).then(() => {
              this.API.ProductGroupEdit(formDatas).then((res) => {
                if (res) {
                  this.$message.success('更新产品组合成功')
                  this.$store.commit('tabChange', 'second')
                  this.$router.push('/product')
                }
              })
            }).catch(() => {
              this.$store.commit('tabChange', 'second')
              this.$router.push('/product')
            })
          }
        })

      },
      resetForm (formName) {
        this.$store.commit('tabChange', 'second')
        this.$router.push('/product')
        this.$refs[formName].resetFields()
      },

      // 获取产品款式下拉框
      getProductStyleDrop () {
        this.API.ProductStyleDrop().then((res) => {
          this.productStyle = res.data
        })
      },
      // 获取产品品牌下拉框
      getProductBrand () {
        this.API.ProductBrandDrop({
          companyId: sessionStorage.getItem('companyID')
        }).then((res) => {
          this.productBrand = res.data
        })
      },
      // 产品风格下拉框
      getBaseProductStyleDrop () {
        this.API.BaseProductStyleDrop().then((res) => {
          this.baseProductStyle = res.data
        })
      },
      // 获取数字字典，常用单位
      getDisctionaryUnit () {
        this.API.DisctionaryUnit().then((res) => {
          this.unitList = res.data
        })
      }
    },
    created () {
      this.editProID = this.$route.params.proID
      this.getProductBrand()
      this.getProductStyleDrop()
      this.getBaseProductStyleDrop()
      this.getDisctionaryUnit()
      this.getProduct()
      // 产品组合
      this.ProductGroupType()
      this.getFitTypeHose()
    }
  }
</script>

<style lang="scss" scoped>
  @import "../../../assets/css/mixin";

  .selectRadio {

  }

  .uploadPic {
    background: #eee;
    padding: 15px 0 0 15px;
    box-sizing: padding-box;
    li {
      font-size: 12px;
      margin: 5px;
      height: 90px;
      width: 90px;
      overflow: hidden;
      display: inline-block;
    }
  }

  .uploadImgList {
    width: 90px;
    text-align: center;
    position: relative
  }

  .posDelete {
    position: absolute;
    background: url('../../../assets/images/icons/delete3.png') 0 center no-repeat;
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
  }

  .upload-mainpic__con {
    position: relative;
    top: -45px;
    background-color: #ff641e;
    width: 90px;
    color: #fff;
    display: inline-block;
    line-height: 22px;
    border-radius: 0 0 8px 8px
  }

  .upload-notmainpic__con {
    position: relative;
    top: -45px;
    background: rgba(0, 0, 0, 0.7);
    width: 90px;
    color: #fff;
    display: inline-block;
    line-height: 22px;
    border-radius: 0 0 8px 8px;
    &:hover {
      cursor: pointer;
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
      vertical-align: 20px;
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
      .title, .delete {
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
      background: #fff;
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
    background-color: #FF6419;
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


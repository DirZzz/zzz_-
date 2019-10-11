<template>
  <transition name="fade">
    <div>
      <pos-nav></pos-nav>
      <div class="addProduct">
        <header>
          <h1>编辑产品</h1>
        </header>
        <p><span>*</span>为必填项</p>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">产品基础信息</el-col>
          <el-col :span="22" class="rightLine"><hr></el-col>
        </el-row>

        <div>
          <el-form :model="product" label-width="120px" class="demo-ruleForm"> <!--:rules="rules" ref="ruleForm"-->
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品组合编码：">
                  {{product.code}}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品组合名称：">
                  {{product.name}}
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="产品品牌：">
                  {{product.brandName}}
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="产品组合型号：">
                  {{product.groupNumber}}
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="适用房间：">
                  {{product.houseType}}
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="适用房间面积：">
                  {{product.areaValue}} m &sup2;
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="产品组合类型：">
                  {{product.groupType}}
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24" class="splineFrame">
              <el-col :span="2" class="productBaseInfo">产品描述信息</el-col>
              <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col :span="12">
                <el-form-item label="建议售价">
                  <el-input placeholder="请输入内容" v-model="product.advicePrice"
                            class="input-with-select formWidth"></el-input>
                  元
                  <p v-if="product.advicePrice < 0" style="color: red">请输入大于等于0的数字</p>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="实际标准价">
                  <el-input placeholder="请输入内容" :maxlength="8" v-model="product.price" class="formWidth"></el-input>
                  元
                  <p v-if="product.price < 0" style="color: red">请输入大于等于0的数字</p>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col>
                <el-form-item label="产品图片">
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
                        <i class="el-icon-plus avatar-uploader-icon"></i>
                      </el-upload>
                    </li>
                    <li class="uploadImgList" @mouseenter="showDelete(index)" @mouseout="showDelete2(index)"
                        v-for="(file, index) in fileList2" :key="index"
                    >
                    <span class="posDelete" v-if="showDeletes[index]" @mouseenter="showDelete(index)"
                          @click="deleteUploadImg(index)"></span>
                      <img style="border-radius: 8px;" @mouseenter="showDelete(index)" width="90" height="90"
                           :src="file.url"/>
                      <span v-if="file.id == defaultPicId" @mouseenter="showDelete(index)" class="upload-mainpic__con">主缩略图</span>
                      <span v-if="showDeletes[index]" @click="setDefaultPic(file)" @mouseenter="showDelete(index)"
                            class="upload-notmainpic__con">设置为缩略图</span>
                    </li>
                  </ul>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="formFrameWidth">
              <el-col>
                <el-form-item label="产品描述">
                  <el-input
                    type="textarea"
                    :rows="6"
                    placeholder="请输入内容"
                    v-model="product.desc">
                  </el-input>
                  <p style="padding-top: 10px">此处最多允许填写200字</p>
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
            <div class="splitLine"></div>
            <el-form-item>
              <el-button round class="confirmBtn" type="primary" @click="submitForm('ruleForm')">确定</el-button>
              <el-button round class="cancelBtnBg" @click="resetForm('ruleForm')">取消</el-button>
            </el-form-item>
          </el-form>

        </div>
      </div>
    </div>
  </transition>
</template>

<script>
  export default {
    data () {
      return {
        productGroupPic:[],
        showDeletes: [],
        editProID: 0,
        uploadParams: {
          platform: 'web',
          module: 'product',
          type: 'image'
        },
        selectStyleId: '', // 产品风格ID
        unitList: [], // 数字字典
        input5: '',
        selectUnit: '',
        baseTextarea: '',
        prodTextarea: '',
        advicePriceInput1: '',
        advicePriceInput2: '',
        productCategory: '',
        seriesValue: '',
        StyleValue: '',
        TextureValue: '',
        baseProductStyle: [], // 产品风格
        brandValue: '',
        baseValue: '',
        TextureValue2: '', // 产品材质
        props: { // 产品分类属性名
          label: 'name',
          value: 'code',
          children: 'children'
        },
        // 上传图片文件列表
        imageUrl: '',
        fileList2: [],
        productBrand: [],
        defaultPicId: 0,
        product: {} // 产品更新
        // 品牌结束
      }
    },
    methods: {
      // 材质选择ID
      selectedTexture (val) {
        this.selectedTextureId = val
      },
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
      selectCategoryNO (val) {
        this.selectCategoryCode = val[val.length - 1]
      },
      setDefaultPic (file) {
        this.defaultPicId = file.id
      },
      /* 上传图片 */
      handleAvatarSuccess (res, file) {
        // this.imageUrl = URL.createObjectURL(file.raw);
        this.fileList2.push({
          url: res.data.url,
          id: res.data.resId
        })
      },
      getProductDetail () {
        this.API.ProductGroupDetail({
          groupId: this.editProID,
          platformType: 'online'
        }).then((res) => {
          this.productGroupPic = res.data.products
          this.product = res.data
          if (this.product.defaultPicId) {
            this.defaultPicId = this.product.defaultPicId
          }
          this.fileList2 = res.data.picInfos.map(res => {
            return {
              url: res.path,
              id: res.id
            }
          })
        })
      },
      beforeAvatarUpload (file) {
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
      // 删除上传图片
      deleteUploadImg (index) {
        this.fileList2.splice(index,1)
      },
      showDelete (index) {
        this.$set(this.showDeletes, index, true)
      },
      showDelete2 (index) {
        this.$set(this.showDeletes, index, false)
      },
      /* 上传图片 结束 */
      // 产品系列下拉框
      getProductSeriesList () {
        this.API.ProductSeriesDrop().then((res) => {
          this.productSeries = res.data
        })
      },
      submitForm () {
        let formDatas = {
          /*
          * advicePrice (number, optional): 建议售价 ,
            areaValue (integer, optional): 使用区域ID ,
            brandId (integer, optional): 品牌ID ,
            desc (string, optional): 组合描述 ,
            groupNumber (string, optional): 组合型号 ,
            groupValue (integer, optional): 组合类型ID ,
            houseValue (integer, optional): 使用房间id ,
            id (integer, optional),
            name (string, optional): 组合名称 ,
            picId (integer, optional): 缩略图 ,
            picIds (string, optional): 图片集合 ,
            platformType (string, optional): 编辑组合所在的平台,channel/online/library ,
            price (number, optional): 价格
          * */

          advicePrice: this.product.advicePrice, //  (number, optional): 建议售价 ,
          desc: this.product.desc, //  (string, optional): 产品描述 ,
          id: this.editProID, //  (integer): 产品id ,
          userId: sessionStorage.getItem('userId'),
          price: this.product.price, //  (number, optional): 售价 ,
          baseStyleId: parseInt(this.product.baseStyleId), //  (integer, optional): 产品风格
          picId: this.defaultPicId,
          picIds: this.fileList2.map(item => item.id).join(','),
          saleUnitId: this.product.saleUnitId, //  (integer, optional): 售价单位 ,
          platformType: 'online'
        }
        for (let key in formDatas) {
          if (formDatas[key] == '' || formDatas[key] == undefined || formDatas[key] == null) {
            delete formDatas[key]
          }
        }

        this.$confirm('确定更新产品', '提示', {
          cancelButtonClass: 'cancelButtonClass',
          confirmButtonClass: 'confirmButtonClass',
          center: true,
          type: 'info'
        }).then(() => {
          this.API.ProductGroupOnlineEdit(formDatas).then((res) => {
            if (res) {
              this.$message.success('更新产品成功')
              this.$router.push('/online/product')
            }
          })
        }).catch(() => {
          this.$router.push('/online/product')
        })
      },
      resetForm (formName) {
        this.$router.push('/online/product')
        this.$refs[formName].resetFields()
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
          console.log(res)
          this.unitList = res.data
        })
      }
    },
    created () {
      this.editProID = this.$route.params.proID
      this.getProductDetail()
      this.getProductSeriesList()
      this.getBaseProductStyleDrop()
      this.getDisctionaryUnit()
    }
  }
</script>

<style lang="scss" scoped>
  @import "../../../assets/css/mixin";

  .propTitle {
    display: block;
    font-size: 14px;
    padding: 0 15px 15px 30px;
    border-bottom: solid 1px #dedede;
    margin-bottom: 15px;
  }

  .formWidth {
    margin-right: 10px;
    width: 280px !important;
    display: inline-block;
  }

  .addProduct {
    background: #fff;
    box-sizing: padding-box;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: $borderRadius;
    header {
      h1 {
        font-weight: bold;
        font-size: 20px;
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

  .uploadImgList {
    width: 90px;
    text-align: center;
    position: relative;
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

  .input-with-select .el-input-group__append {
    background-color: #fff;
    width: 130px;
  }

  .uploadPic {
    background: #eee;
    padding-top: 15px;
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
    font-size: 20px;
    color: #fff;
    width: 20px;
    height: 20px;
    line-height: 20px;
    text-align: center;
    position: relative;
    background: #ff6419;
    border-radius: 20px;
    z-index: 11;
    vertical-align: -3px;
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
    position: relative;
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

  .el-form-item {
    margin-bottom: 10px;
  }
</style>


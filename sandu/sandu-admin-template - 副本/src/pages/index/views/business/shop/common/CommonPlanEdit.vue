<template>
  <transition name="fade">
    <div class="addProduct">
      <header>
        <h1>编辑方案</h1>
      </header>
      <p><span>*</span>为必填项</p>
      <div>
        <el-row :gutter="24">
          <el-col :span="15">
            <el-form :model="planInfo" :rules="rules" ref="planInfo" label-width="100px" class="demo-ruleForm">
              <el-row>
                <el-col :span="11">
                  <el-form-item label="方案名称：" prop="planName">
                    <el-input v-model="planInfo.planName"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="方案编号：">
                    {{planInfo.planCode}}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="来源：" prop="productNameForm">
                    {{planInfo.origin}}
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="完成时间：">
                    {{planInfo.completeDate}}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="品牌：" prop="brandId">
                    <el-select v-model="planInfo.brandId" placeholder="请选择">
                      <el-option
                        v-for="item in brands"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="设计师：">
                    {{planInfo.designer}}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="方案类型：">
                    智能方案
                  </el-form-item>
                </el-col>

                <el-col :span="11">
                  <el-form-item label="风格：" prop="designStyleId">
                    <el-select v-model="planInfo.designStyleId" class="selectWidth" placeholder="请选择">
                      <el-option
                        v-for="item in styles"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="11">
                <el-col :span="21">
                  <el-form-item label="方案描述：" prop="planDesc">
                    <el-input
                      class="textareaWidth"
                      type="textarea"
                      :rows="6"
                      placeholder="请输入内容"
                      v-model="planInfo.planDesc">
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="11">
                <el-col :span="21">
                  <el-form-item label="备注：">
                    <el-input
                      class="textareaWidth"
                      type="textarea"
                      :rows="6"
                      placeholder="请输入内容"
                      v-model="planInfo.remarks">
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item>
                <el-button type="primary" @click="submitForm('planInfo')">保存</el-button>
                <el-button @click="resetForm('planInfo')">重置</el-button>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="9">
            <img width="500" height="300"/>
            <br/>
            <el-button type="primary">重新上传</el-button>
          </el-col>
        </el-row>
      </div>
    </div>
  </transition>
</template>

<script>
  export default {
    data () {
      return {
        planInfo: {
          remarks: ''
        },
        styles: [], // 产品风格
        fileList2: [
          {
            name: 'food.jpeg',
            url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
          }
        ],
        // 验证表单规则
        rules: {
          planName: [
            {required: true, message: '必填', trigger: 'blur'},
            {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
          ],
          styleId: [
            {required: true, message: '必填', trigger: 'blur'},
            {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
          ],
          branId: [
            {required: true, message: '必填', trigger: 'blur'}
          ],
          planDesc: [
            {required: true, message: '必填', trigger: 'blur'}
          ]
        },
        // 品牌
        brands: []
        // 品牌结束
      }
    },
    methods: {

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

      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const params = {
              brandIds: [this.planInfo.brandId],
              styleId: this.planInfo.designStyleId,
              picId: '1000000',
              picPath: '/temp/path',
              planDesc: this.planInfo.planDesc,
              remarks: this.planInfo.remarks,
              planName: this.planInfo.planName,
              planId: this.planId
            }
            for (let key in params) {
              if (params[key] == '' || params[key] == undefined || params[key] == null) {
                delete params[key];
              }
            }
            /* this.API.CommonDesignPlanEdit(params).then((res) => {
              this.$message({
                type: 'success',
                message: '保存成功'
              })
              this.$router.push(`/plan/common/${this.planId}`)
            }) */
          } else {
            return false
          }
        })
      },

      resetForm (formName) {
        this.$refs[formName].resetFields()
      },
      // 产品风格下拉框
      getBaseProductStyleDrop () {
        this.API.DesignPlanStyles().then((res) => {
          this.styles = res.data
        })
      },
      getBaseInfo () {
        this.planId = this.$route.params.planId
        this.API.CommonDesignPlanBaseInfo(this.planId).then((resp) => {
          resp.data.brandId = parseInt(resp.data.brandId)
          this.planInfo = resp.data
        })
      },
      getBrands () {
        this.API.ProductBrandDrop().then((resp) => {
          this.brands = resp.data
        })
      }
    },
    mounted () {
      this.getBaseInfo()
      this.getBrands()
      this.getBaseProductStyleDrop()
    }
  }
</script>

<style lang="scss" scoped>
  @import "../../../../assets/css/mixin";

  .addProduct {
    background: #fff;
    box-sizing: padding-box;
    padding: 20px;
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
      width: 100%;
      height: 130px;
      background-color: #eee;
    }
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


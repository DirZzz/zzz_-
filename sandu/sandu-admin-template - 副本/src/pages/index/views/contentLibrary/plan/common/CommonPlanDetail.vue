<template>
  <div>
    <!-- 智能方案详情页面 -->
    <pos-nav></pos-nav>
    <div class="planDetailFrame">
      <transition name="fade">
        <el-button round type="primary" v-if="activeName==='first'" @click="gotoAddProduct"><i class="el-icon-edit"></i> 编辑</el-button>
      </transition>
      <el-tabs v-model="activeName" style="margin-top: 15px" @tab-click="handleClick">
        <el-tab-pane label="基础信息" name="first">
          <div class="planLibBaseMsg">
            <el-col :span="12">
              <ul>
                <li><span>方案名称：</span> <b>{{planInfo.planName || '—无—'}}</b></li>
                <li><span>方案编号：</span> <b>{{planInfo.planCode || '—无—'}}</b></li>
                <li><span>来源：</span> <b>显示方案来源</b></li>
                <li><span>完成时间：</span> <b>{{planInfo.completeDate || '—无—'}}</b></li>
                <li><span>品牌：</span> <b>{{planInfo.brandName || '—无—'}}</b></li>
                <li><span>设计师：</span> <b>{{planInfo.designer || '—无—'}}</b></li>
                <li><span>方案类型：</span> <b>普通方案</b></li>
                <li><span>风格：</span> <b>{{planInfo.designStyleName || '—无—'}}</b></li>
                <li><span>方案描述：</span> <b class="remarks">{{planInfo.remark || '—无—'}}</b></li>
                <li><span>备注：</span> <b class="remarks">{{planInfo.planDesc || '—无—'}}</b></li>
                <li><span>分配情况：</span> <b class="remarks">{{planInfo.distributionStatus === '2b' ?  '渠道管理' : '线上管理'}}</b></li>
              </ul>
            </el-col>
            <el-col :span="12">
              <div class="showImage"><img :src="planInfo.picPath" width="600" height="400" alt=""></div>
            </el-col>
          </div>
        </el-tab-pane>
        <el-tab-pane label="产品列表" name="second">
          <el-row :gutter="10">
            <el-col>
              <!--产品类别:-->
              <category-tree v-model="planProductList.query.categoryIds" style="margin-right: 20px"></category-tree>
              <!--是否未公开产品:-->
              <is-open-product-select v-model="planProductList.query.secrecy" style="margin-right: 20px"></is-open-product-select>
              <!--是否显示:-->
              <is-show-select v-model="planProductList.query.displayStatus" style="margin-right: 20px"></is-show-select>
              <el-button type="primary" @click="listPlanProducts" class="button-style">搜索</el-button>
              <el-button type="primary" @click="handleProductReset" class="button-style">重置</el-button>
            </el-col>
          </el-row>
          <plan-product :products="planProductList.products"></plan-product>
          <!--分页组件-->
          <div class="pageFrame">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="planProductList.query.page"
              :page-sizes="[5, 10, 20]"
              :page-size="planProductList.query.limit"
              layout="total, sizes, prev, pager, next, jumper"
              :total="planProductList.total">
            </el-pagination>
          </div>
          <!-- 产品列表结束 -->
        </el-tab-pane>
        <el-tab-pane label="效果图" name="third">
          <el-row :gutter="24" class="rowpic">
            <el-col :span="3">照片级效果图</el-col>
            <el-col :span="21">
              <el-col :span="4" class="setDefaultPic" v-for="(pic,index) in effectDiagram.pics" :key="index">
                <img v-if="pic.picPath" :src="pic.picPath" width="312" height="176"/>
                <span v-else>无图片</span>
                <ul>
                  <li>设为缩略图</li>
                  <li>禁用</li>
                  <li>删除</li>
                </ul>
              </el-col>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="rowpic">
            <el-col :span="3">单点720全景图</el-col>
            <el-col :span="21">
              <el-col :span="4" class="setDefaultPic" v-for="(pic,index) in effectDiagram.single720Pic" :key="index">
                <img v-if="pic.picPath" :src="pic.picPath" width="312" height="176"/>
                <img v-else src="../../../../assets/images/no_img.jpg" width="312" height="176" alt="">
                <ul>
                  <li>设为缩略图</li>
                  <li>禁用</li>
                  <li>删除</li>
                </ul>
              </el-col>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="rowpic">
            <el-col :span="3">多点720全景图</el-col>
            <el-col :span="21">
              <el-col :span="4" class="setDefaultPic" v-for="(pic,index) in effectDiagram.multi720Pic" :key="index">
                <img v-if="pic.picPath" :src="pic.picPath" width="312" height="176"/>
                <img v-else src="../../../../assets/images/no_img.jpg" width="312" height="176" alt="">
                <ul>
                  <li>设为缩略图</li>
                  <li>禁用</li>
                  <li>删除</li>
                </ul>
              </el-col>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="rowpic">
            <el-col :span="3">漫游视频</el-col>
            <el-col :span="21">
              <el-col :span="4" class="setDefaultPic" v-for="(pic,index) in effectDiagram.videoPic" :key="index">
                <img v-if="pic.picPath" :src="pic.picPath" width="312" height="176"/>
                <img v-else src="../../../../assets/images/no_img.jpg" width="312" height="176" alt="">
                <ul>
                  <li>设为缩略图</li>
                  <li>禁用</li>
                  <li>删除</li>
                </ul>
              </el-col>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="设计理念" name="four">
          <vue-editor useCustomImageHandler :editorOptions="editorSettings"   v-model="textarea3" class="editor"  @imageAdded="handleImageAdded"></vue-editor>
          <div class="footerBtn">
            <el-button round class="confirmBtn" type="primary" size="small" @click="saveContent">保存</el-button>
            <el-button round class="confirmBtn" size="small" @click="cancelSave">取消</el-button>
          </div>
          <!--富文本html区域-->
          <div v-show="false" v-html="textarea3" ref="editorContent"></div>
          <!--富文本html区域-->
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  import PlanProduct from '../../../base/plan/PlanProducts.vue'
  import CategoryTree from '../../../base/category/CategoryTree.vue'
  import IsShowSelect from '../../../base/plan/IsShowSelect.vue'
  import IsOpenProductSelect from '../../../base/plan/IsOpenProductSelect.vue'

  import { VueEditor,Quill } from "vue2-editor";
  import { ImageDrop } from 'quill-image-drop-module'
  import ImageResize from 'quill-image-resize-module'
  Quill.register('modules/imageDrop', ImageDrop)
  Quill.register('modules/imageResize', ImageResize)

  export default {
    components: {
      IsOpenProductSelect,
      IsShowSelect,
      CategoryTree,
      PlanProduct,
        VueEditor
    },
    data () {
      return {
          editorSettings: {
              modules: {
                  imageDrop: true,
                  imageResize: {}
              }
          },
          introducedPicIds: [], // 富文本图片ID
          introducedPicImg: [], // 富文本图片信息
          textarea3: "",
        showHide: '',
        activeName: 'first', // tabs 默认激活项
        planId: 0,
        planInfo: {},
        effectDiagram: {},
        planProductList: {
          products: [],   // 智能方案列表
          total: 0,
          query: {
            page: 1,
            limit: 10,
            categoryIds: []
          }
        }
      }
    },
    methods: {
        getContent(){
            this.API.getPlanTextarea({
                id:this.planId
            }).then((res) => {
                this.textarea3= res.data
            })
        },
        saveContent() {
            this.API.planTextarea({
                id:this.planId,
                content:this.textarea3
            }).then((res) => {
                if(res) {
                    this.$message.success('保存成功')
                }
            })
        },
        cancelSave() {
            this.$router.push('/plan')
        },
        //获取富文本的图片id
        acquireIntroducedPicId() {
            let img = this.$refs.editorContent.getElementsByTagName("img");
            let id = [];
            if (img.length > 0) {
                for (let i = 0; i < img.length; i++) {
                    for (let j = 0; j < this.introducedPicImg.length; j++) {
                        if (
                            decodeURI(
                                img[i].src.substring(this.BASE_URL.sourceBaseUrl.length)
                            ) == decodeURI(this.introducedPicImg[j].picPath)
                        ) {
                            id.push(this.introducedPicImg[j].id);
                        }
                    }
                }
            }
            this.introducedPicIds = id;
        },
        handleImageAdded(file, Editor, cursorLocation, resetUploader) {
            const isLt4M = file.size / 1024 / 1024 < 4;
            if (!isLt4M) {
                this.$message({
                    type: 'error',
                    message: '文件大小4M以内!'
                });
                return;
            }
            let formData = new FormData();
            formData.append("file", file);
            formData.append("businessId ", sessionStorage.getItem("shopId"));
            this.API.blogpicUp(formData).then(res => {
                if (res.success) {
                    let url = this.BASE_URL.sourceBaseUrl + res.obj.picPath; // Get url from response
                    if (res.obj.id) {
                        this.introducedPicImg.push(res.obj);
                    }
                    Editor.insertEmbed(cursorLocation, "image", url);
                    resetUploader();
                } else {
                    this.$message({
                        type: "error",
                        message: res.message
                    });
                }
            });
        },
      handleClick (tab, event) {
      },
      handleProductSearch () {

      },
      handleSelectionChange (val) {
        this.multipleSelection = val
      },
      // 分页模块
      handleSizeChange: function (size) {
        this.planProductList.query.limit = size
        this.listPlanProducts()
      },
      toggleStatus (row) {
        const params = {
          planId: this.planId,
          productId: row.id,
          status: row.displayStatus
        }
        this.API.CommonPlanToggleProductDisplay(params).then(resp => {
          this.listPlanProducts()
        })
      },
      handleCurrentChange: function (currentPage) {
        this.planProductList.query.page = currentPage
        this.listPlanProducts()
      },
      getBaseInfo () {
        this.API.CommonDesignPlanBaseInfo(this.planId)
          .then((resp) => {
            this.planInfo = resp.data
          })
      },
      getEffectDiagram () {
        this.API.GetCommonEffectDiagram(this.planId).then(resp => {
          this.effectDiagram = resp.data
        })
      },
      listPlanProducts () {
        const params = {
          page: this.planProductList.query.page,
          limit: this.planProductList.query.limit,
          planId: this.planId,
          categoryCode: this.planProductList.query.categoryIds[this.planProductList.query.categoryIds.length - 1],
          secrecy: this.planProductList.query.secrecy,
          displayStatus: this.planProductList.query.displayStatus
        }
        this.API.ListCommonPlanProducts(params).then(resp => {
          this.planProductList.products = resp.data
          this.planProductList.total = resp.total
        })
      },
      gotoAddProduct () {
        this.$router.push(`/plan/common/${this.planId}/edit`)
      },
      handleProductReset () {
        this.planProductList.query = {
          page: 1,
          limit: 10,
          categoryIds: []
        }
      }
    },
    created () {
      this.planId = this.$route.params.planId
      this.getBaseInfo()
      this.getEffectDiagram()
      this.listPlanProducts()
        this.getContent()
    }
  }
</script>

<style lang="scss" scoped>
  @import "../../../../assets/css/mixin";
  .editor{
    margin:30px 0 30px 30px;
    width:80%;
    height: 500px;
  }
  .footerBtn{
    padding: 30px;
    text-align: right;
    width:80%;
    .el-button{
      width: 100px;
    }
  }
  .planDetailFrame {
    padding: 20px;
    box-sizing: padding-box;
    background: #fff;
    border-radius: $borderRadius;
    .right {
      overflow: hidden;
      float: right;
    }
  }
  .button-style{
    width: 130px;
    height: 40px;
    border-radius: 20px;
    color: #ff6419;
    font-size: 16px;
    border: 1px solid #ff6419;
    background-color: #fff;
  }
  .pageFrame {
    background: #fff;
    height: 110px;
    padding-top: 40px;
    box-sizing: border-box;
    text-align: center;
  }

  .planLibBaseMsg {
    ul {
      min-width: 780px;
      padding-top: 20px;
      li {
        font-size: 14px;
        display: inline-block;
        margin-bottom: 46px;
        white-space: nowrap;
        span {
          display: inline-block;
          width: 80px;
          vertical-align: top;
        }
        b {
          display: inline-block;
          font-weight: normal;
          min-width: 280px;
          &.remarks {
            white-space: normal;
            max-width: 650px;
            min-width: 380px;
            word-wrap: break-word;
          }
        }
      }
    }
  }
  .rowpic{
    border-bottom: solid 1px #e3e3e3;
    margin-bottom: 25px;
  }
  .setDefaultPic{
    position: relative;
    margin-bottom: 25px;
    margin-left: 15px;
    width: 312px;
    height: 176px;
    ul{
      position: absolute;
      width: 100%;
      bottom: 0;
      background: rgba(0,0,0,.5);
      height: 26px;
      text-align: center;
      li{
        display: inline-block;
        text-align: center;
        line-height: 26px;
        font-size: 12px;
        color: #fff;
        width: 25%;
        &:first-of-type{
          width: 40%;
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
    margin-top: 90px;
    margin-bottom: 30px;
    width: 100%;
  }

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }

  .showImage {
    /*img {*/
    /*width: 100%;*/
    /*}*/
  }

  .showClass {
    margin-bottom: 10px;
  }
</style>

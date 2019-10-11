<template>
  <div class="add_project_case">
    <v-nav :nav="nav"></v-nav>
    <div class="content">
      <div class="header">{{headerTxt}}</div>
      <div class="edit" v-show="!isDatails">
        <span style="font-weight: bold"><span class="hint">*</span>为必填项 </span>
        <div class="titles">房屋信息</div>
        <div class="edit-title" style="display:inline-block;width:49%;">
          <span>户型：</span>
          <el-input-number
            v-model="paramsData.bedroomNum"
            size="medium"
            style="width:120px"
            :min="1"
            :max="10"
          ></el-input-number>
          <span style="padding:0 10px">室</span>
          <el-input-number
            v-model="paramsData.livingRoomNum"
            size="medium"
            style="width:120px"
            :min="1"
            :max="10"
          ></el-input-number>
          <span style="padding:0 10px">厅</span>
        </div>
        <div class="edit-title" style="display:inline-block;width:49%;">
          <span>使用面积：</span>
          <el-input v-model="paramsData.houseArea" size="medium" style="width:100px" maxlength="4"></el-input>
          <span>m²</span>
        </div>
        <div class="edit-title" style="display:inline-block;width:49%;">
          <span>花费：</span>
          <el-input v-model="paramsData.houseCost" size="medium" style="width:100px" maxlength="4"></el-input>
          <span>万（元）</span>
        </div>
        <div class="edit-title" style="display:inline-block;width:49%;">
          <span>风格：</span>
          <el-select v-model="paramsData.designStyle" filterable clearable placeholder="请选择方案风格">
            <el-option v-for="item in planStyles" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </div>
        <div class="titles">文章内容</div>
        <div class="edit-title">
          <span class="planSize">方案(<span class="hint">非必填</span>) ：</span>
          <div class="avatar-uploader" @click="selectPlan">
            <div class="img-list-item" v-if="paramsData.planCover">
              <span
                @click.stop="deleteImg('planCover');paramsData.planUuId=null;paramsData.planId=null;"
              ></span>
              <img :src="BASE_URL.sourceBaseUrl+paramsData.planCover" class="shop-cover-img">
            </div>
            <div v-else class="add-img"></div>
          </div>
        </div>
        <div class="edit-title">
          <span class="planSize">户型：</span>
          <el-upload
            class="avatar-uploader"
            :action="BASE_URL.uploadUrl"
            :headers="token"
            :data="{
              fileKey: 'company.shop.houseTypePic',
              platform: 'web',
              module: 'product',
            }"
            :show-file-list="false"
            :on-success="uploadHouseTypePic"
            :before-upload="upload"
          >
            <div class="img-list-item" v-if="paramsData.houseTypePic">
              <span @click.stop="deleteImg('houseTypePic')"></span>
              <img :src="paramsData.houseTypePic" class="shop-cover-img">
            </div>
            <div class="img-list-item" v-else-if="paramsData.houseTypePicPath">
              <span @click.stop="deleteImg('houseTypePic')"></span>
              <img :src="BASE_URL.sourceBaseUrl+paramsData.houseTypePicPath" class="shop-cover-img">
            </div>
            <div v-else class="add-img"></div>
          </el-upload>
        </div>
        <div class="edit-title">
          <span class="planSize"><span class="hint">*</span>封面图：</span>
          <el-upload
            class="avatar-uploader"
            :action="BASE_URL.uploadUrl"
            :headers="token"
            :data="{
              fileKey: 'company.shop.coverPic',
              platform: 'web',
              module: 'product',
            }"
            :show-file-list="false"
            :on-success="uploadCoverPic"
            :before-upload="upload"
          >
            <div class="img-list-item" v-if="paramsData.coverPic">
              <span @click.stop="deleteImg('coverPic')"></span>
              <img :src="paramsData.coverPic" class="shop-cover-img">
            </div>
            <div class="img-list-item" v-else-if="paramsData.coverPicPath">
              <span @click.stop="deleteImg('coverPic')"></span>
              <img :src="BASE_URL.sourceBaseUrl+paramsData.coverPicPath" class="shop-cover-img">
            </div>
            <div v-else class="add-img"></div>
          </el-upload>
        </div>

        <div class="edit-title">
          <span class="planSize"><span class="hint">*</span>标题：</span>
          <el-input
            v-model="paramsData.title"
            placeholder="请输入标题"
            class="contacts"
            size="medium"
            :maxlength="30"
          ></el-input>
        </div>
        <div style="display:flex;justify-content:left;word-break:break-all;">
          <span class="planSize"><span class="hint">*</span>图文案例说明：</span>
          <vue-editor
            :editorOptions="editorSettings"
            :multiple="true"
            useCustomImageHandler
            @image-added="handleImageAdded"
            @change="onQuillChange"
            v-model="paramsData.content"
            class="editor"
          ></vue-editor>
        </div>
      </div>
      <div class="footer" v-show="!isDatails">
        <el-button type="primary" round class="reset" @click="operation('取消')">取消</el-button>
        <el-button type="primary" round class="save" @click="operation('保存')">保存</el-button>
      </div>
      <div v-html="paramsData.content" ref="editorContent" v-show="false"></div>
      <div class="edit" v-show="isDatails">
        <div class="titles">房屋信息</div>
        <div class="edit-title" style="display:inline-block;width:49%;">
          <span>户型：</span>
          <span style="padding:0 10px">{{paramsData.bedroomNum}}室</span>
          <span style="padding:0 10px">{{paramsData.livingRoomNum}}厅</span>
        </div>
        <div class="edit-title" style="display:inline-block;width:49%;">
          <span>使用面积：{{paramsData.houseArea}}m²</span>
        </div>
        <div class="edit-title" style="display:inline-block;width:49%;">
          <span>花费：{{paramsData.houseCost}}万（元）</span>
        </div>
        <div class="edit-title" style="display:inline-block;width:49%;">
          <span>风格：{{paramsData.designStyle}}</span>
        </div>
        <div class="titles">文章内容</div>
        <div class="edit-title">
          <span>添加方案：</span>
          <div class="img-list-item" v-if="paramsData.planCover">
            <img :src="BASE_URL.sourceBaseUrl+paramsData.planCover" class="shop-cover-img">
          </div>
        </div>
        <div class="edit-title">
          <span>户型：</span>
          <div class="img-list-item" v-if="paramsData.houseTypePicPath">
            <img :src="BASE_URL.sourceBaseUrl+paramsData.houseTypePicPath" class="shop-cover-img">
          </div>
        </div>
        <div class="edit-title">
          <span>封面图：</span>
          <div class="img-list-item" v-if="paramsData.coverPicPath">
            <img :src="BASE_URL.sourceBaseUrl+paramsData.coverPicPath" class="shop-cover-img">
          </div>
        </div>
        <div>
          <span>标题：</span>
          <span>{{paramsData.title}}</span>
        </div>
        <div class="editor-content">
          <span>富文本内容：</span>
          <div v-html="paramsData.content"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
//import { Quill, VueEditor } from "vue2-editor";
import VueEditor from '@/components/vueEditor/vueEditor'
import Quill from "quill";
import { ImageDrop } from "quill-image-drop-module";
import ImageResize from "quill-image-resize-module";
import {container, ImageExtend,QuillWatch} from 'quill-image-extend-module'

Quill.register("modules/imageDrop", ImageDrop);
Quill.register("modules/imageResize", ImageResize);
Quill.register('modules/ImageExtend', ImageExtend)
export default {
  name: "add_project_case",
  components: {
    VueEditor
  },
  data() {
    return {
      token: { Authorization: sessionStorage.getItem("token") },
      editorSettings: {
        modules: {
          imageDrop: false,
          imageResize: {},
          ImageExtend:{
              loading: false,
              name:'file',
              change: (xhr, formData) => {
                  xhr.setRequestHeader('Authorization',sessionStorage.getItem('token'));
                  formData.append("type", "image");
                  formData.append("platform", "web");
                  formData.append("module", "product");
                  formData.append("fileKey", "company.shop.casePic");
              },
              action: 'upload-error',   //拖拽功能 去掉
              start: (value) => {

              },
              response: (res) =>{
                  return res.data.url
              }
          }
        }
      },
      nav: [
        { nav: "店铺管理", url: "/business/shop" },
        {
          nav: "工程案例",
          url: "/business/projectCase?shopType=" + this.$route.query.shopType
        },
        { nav: "", url: "/business/addProjectCase" }
      ],
      paramsData: {
        planCover: undefined,
        houseTypePic: undefined,
        coverPic: undefined,
        designStyle: undefined,
        title: "",
        content: "",
        resPics: [] // 保存上传后的图片信息
      },
      isDatails: true,
      headerTxt: "",
      isClick: true,
      planStyles: [
        "现代简约",
        "现代奢华",
        "北欧风情",
        "工业风",
        "法式宫廷",
        "英伦风",
        "简欧",
        "地中海",
        "新古典",
        "美式古典",
        "美式田园",
        "中式古典",
        "新中式",
        "东南亚",
        "日韩风",
        "混搭",
        "其他"
      ]
    };
  },
  created() {
    let flag = true;
    if (this.$store.state.shop.paramsData) {
      this.paramsData = this.$store.state.shop.paramsData;
      this.$store.commit("SET_paramsData", null);
      flag = false;
    }
    if(this.$store.state.shop.checkPlan){
      this.paramsData.planId = this.$store.state.shop.checkPlan.planId;
      this.paramsData.planUuId = this.$store.state.shop.checkPlan.uuid;
      this.paramsData.planCoverId = this.$store.state.shop.checkPlan.picId;
      this.paramsData.planCover = this.$store.state.shop.checkPlan.picPath;
      this.$store.commit("SET_checkPlan", null);
    }
    if (sessionStorage.getItem("isProjectEdit") == "编辑") {
      if (flag) this.getProjectInfo();
      this.isDatails = false;
      this.headerTxt = "工程案例编辑";
      this.nav[2].nav = "工程案例编辑";
    } else if (sessionStorage.getItem("isProjectEdit") == "详情") {
      if (flag) this.getProjectInfo();
      this.isDatails = true;
      this.headerTxt = "工程案例详情";
      this.nav[2].nav = "工程案例详情";
    } else {
      this.isDatails = false;
      this.headerTxt = "新增工程案例";
      this.nav[2].nav = "新增工程案例";
    }
  },
  mounted(){

  },
  methods: {
    selectPlan() {
      this.$store.commit("SET_paramsData", this.paramsData);
      this.$router.push({ path: "/business/shop/select_Plan" });
    },
    onQuillChange(file){
        console.log(file,'file')
    },
    async handleImageAdded(file,fileName, Editor, cursorLocation, resetUploader) {
        const loading = this.$loading({
            lock: true,
            text: "Loading",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)"
        });
        let formData = new FormData();
        formData.append("file",file,fileName);
        formData.append("type", "image");
        formData.append("platform", "web");
        formData.append("module", "product");
        formData.append("fileKey", "company.shop.casePic");
        await this.API.upLoadImg(formData).then(res => {
            if (res.code == 200) {
                res.obj = {};
                loading.close();
                res.obj.id = res.data.resId;
                res.obj.picPath = res.data.url;
                let url = res.data.url; // Get url from response
                if (res.data.resId) {
                    this.paramsData.resPics.push(res.obj);
                    Editor.insertEmbed(cursorLocation, "image", url);
                    resetUploader();
                }
            } else {
                this.$message({
                    type: "error",
                    message: res.message
                });
            }
        });
    },
    /*验证标题和内容*/
    verify() {
      if (!this.paramsData.coverPicId) {
        this.$message({
          type: "error",
          message: "请添加封面图!"
        });
        return true;
      } else if (
        this.paramsData.title.replace(/\s|\xA0/g, "").length == 0 ||
        this.paramsData.title.replace(/\s|\xA0/g, "").length > 30
      ) {
        this.$message({
          type: "error",
          message: "案例标题不能为空; 案例标题长度限1-30个字符"
        });
        return true;
      } else if (
        !this.$refs.editorContent.children[0] ||
        this.$refs.editorContent.children[0].innerHTML.replace(/\s|\xA0/g, "")
          .length == 0
      ) {
        this.$message({
          type: "error",
          message: "案例内容不能为空"
        });
        return true;
      } else {
        return false;
      }
    },
    deleteImg(field) {
      this.$set(this.paramsData, field, null);
      this.$set(this.paramsData, field + "Id", null);
      this.$set(this.paramsData, field + "Path", null);
    },
    /*上传LOGO成功回调*/
    uploadHouseTypePic(res, file) {
      if (res.code == 200) {
        this.$set(this.paramsData, "houseTypePic", res.data.url);
        this.$set(this.paramsData, "houseTypePicId", res.data.resId);
      } else {
        this.$message({
          type: "error",
          message: res.message
        });
      }
    },
    uploadCoverPic(res, file) {
      if (res.code == 200) {
        this.$set(this.paramsData, "coverPic", res.data.url);
        this.$set(this.paramsData, "coverPicId", res.data.resId);
      } else {
        this.$message({
          type: "error",
          message: res.message
        });
      }
    },
    /*上传图片前验证图片回调*/
    upload(file) {
      const isJPG =
        file.name.split(".")[1].toUpperCase() === "GIF" ||
        "JPG" ||
        "JPEG" ||
        "PNG";
      const isLt4M = file.size / 1024 / 1024 < 4;
      if (!isJPG) {
        this.$message.error("文件格式GIF，JPG，JPEG，PNG!");
        return false;
      } else if (!isLt4M) {
        this.$message.error("文件大小4M以内!");
        return false;
      } else {
        return true;
      }
    },
    /*保存，取消*/
    operation(type) {
      if (type == "保存") {
        if (!this.isClick) {
          this.$store.commit("SET_paramsData", null);
          return;
        }
        this.isClick = false;
        setTimeout(() => {
          this.isClick = true;
        }, 1000);
        if (this.verify()) {
          return false;
        }
        let img = this.$refs.editorContent.getElementsByTagName("img");
        let id = [];
        if (img.length > 0) {
          for (let i = 0; i < img.length; i++) {
            for (let j = 0; j < this.paramsData.resPics.length; j++) {
              if (
                img[i].src.includes(this.BASE_URL.sourceBaseUrl) &&
                this.paramsData.resPics[j].picPath.includes(
                  this.BASE_URL.sourceBaseUrl
                )
              ) {
                if (
                  decodeURI(img[i].src) ==
                  decodeURI(this.paramsData.resPics[j].picPath)
                ) {
                  id.push(this.paramsData.resPics[j].id);
                }
              } else {
                if (
                  decodeURI(
                    img[i].src.substring(this.BASE_URL.sourceBaseUrl.length)
                  ) == decodeURI(this.paramsData.resPics[j].picPath)
                ) {
                  id.push(this.paramsData.resPics[j].id);
                }
              }
            }
          }
        }
        if (sessionStorage.getItem("isProjectEdit") == "编辑") {
          this.API.editProjectCase(
            Object.assign(this.paramsData, {
              caseId: sessionStorage.getItem("caseId"), // 案例id
              caseTitle: this.paramsData.title, // 案例标题
              picIds: id.length > 0 ? id.join(",") : undefined,
              shopId: sessionStorage.getItem("shopId"),
              shopType: this.$route.query.shopType
            })
          ).then(res => {
            if (res.success) {
              this.$router.push({
                path: "/business/projectCase",
                query: { shopType: this.$route.query.shopType }
              });
            }
            this.$message({
              type: res.success ? "success" : "error",
              message: `编辑案例${res.success ? "成功" : "失败"}!`
            });
          });
        }
        if (sessionStorage.getItem("isProjectEdit") == "新增") {
          this.API.addProjectCase(
            Object.assign(
              {
                caseTitle: this.paramsData.title, // 案例标题
                picIds: id.length > 0 ? id.join(",") : undefined,
                shopId: sessionStorage.getItem("shopId"),
                shopType: this.$route.query.shopType
              },
              this.paramsData
            )
          ).then(res => {
            if (res.success) {
              this.$router.push({
                path: "/business/projectCase",
                query: { shopType: this.$route.query.shopType }
              });
            }
            this.$message({
              type: res.success ? "success" : "error",
              message: `新增案例${res.success ? "成功" : "失败"}!`
            });
          });
        }
      }
      if (type == "取消") {
        this.$router.push({
          path: "/business/projectCase",
          query: { shopType: this.$route.query.shopType }
        });
      }
    },
    getProjectInfo() {
      this.API.projectCaseDatails({
        caseId: sessionStorage.getItem("caseId")
      }).then(res => {
        if (res.success) {
          this.paramsData = res.obj;
          this.$set(this.paramsData, "title", res.obj.caseTitle);
          this.picIds = res.obj.picIds;
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../../../assets/css/mixin";
.content {
  background-color: #ffffff;
  .header {
    height: 76px;
    font-size: 20px;
    color: #333333;
    border-bottom: 1px solid #eeeeee;
    line-height: 76px;
    padding-left: 20px;
  }
  .edit {
    padding: 20px;
    .titles {
      font-weight: 700;
      border-left: 3px solid #ff6419;
      padding-left: 10px;
      margin: 20px 0;
      font-size: 16px;
    }
    .hint {
      color: #ff6419;
      margin-right: 5px;
    }
    .planSize{
       display: inline-block;
       min-width: 130px;
      text-align: right;
      margin-right: 20px;
    }
    .edit-title {
      color: #333333;
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      .contacts {
        width: 500px;
      }
    }
    .editor-content {
      margin-top: 20px;
      display: flex;
      justify-content: left;
    }
  }
  .editor {
    display: block;
    height: 820px;
    width: 1000px;
  }
  .footer {
    padding: 50px 20px 20px 30px;
    .reset {
      background-color: #454545;
      border: solid 1px #454545;
    }
    .el-button {
      width: 130px;
      height: 40px;
    }
  }
  .avatar-uploader {
    display: inline-block;
    vertical-align: bottom;
    width: 90px;
    height: 90px;
    background-color: #ffffff;
    border-radius: 4px;
    border: solid 1px #dddddd;
    margin-right: 20px;
  }
  .add-img {
    width: 22px;
    height: 22px;
    margin: 34px 34px;
    background: url("../../../assets/images/icons/uploadPlug.png") no-repeat 0 0;
  }
  .img-text {
    color: #999999;
  }
  .img-list-item {
    display: inline-block;
    position: relative;
    span {
      position: absolute;
      top: 36px;
      left: 50%;
      transform: translateX(-50%);
      display: block;
      width: 18px;
      height: 16px;
      background: url("../../../assets/images/icons/delete3.png");
    }
    .shop-cover-img {
      height: 90px;
      border-radius: 4px;
      margin-right: 10px;
      vertical-align: bottom;
    }
  }
  .img-text {
    color: #999999;
  }
}
</style>

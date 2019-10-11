<!--新增博文页面-->
<template>
  <div class="addblogpage">
    <v-nav :nav="nav"></v-nav>
    <div class="addblog">
      <el-main>
        <div class="contentHeader">
          <h1 class="title">新增博文栏目</h1>
        </div>
        <el-form class="titleRow clear">
          <el-col class="startitle" :span="8">
            <span class="star">标题：</span>
          </el-col>
          <el-col class="titleInput" :span="16">
            <el-input v-model="blogTitle" maxlength="30" placeholder="请输入博文标题,30个字以内"></el-input>
            <p style="height:20px;color:red;" v-show="CheckTitle">博文标题不能为空</p>
          </el-col>
        </el-form>
        <el-form class="clear contentRow">
          <el-col class="contentTitle" :span="8">
            <span class="star">内容：</span>
          </el-col>
          <el-col class="editBlog" :span="16">
            <vue-editor
              useCustomImageHandler
              :editorOptions="editorSettings"
              v-model="textarea3"
              class="editor"
              @image-added="handleImageAdded"
            ></vue-editor>
            <p style="height:20px;color:red;margin-top:60px;" v-show="CheckContent">博文内容不能为空</p>
          </el-col>
        </el-form>
        <el-form class="clear titleRow">
          <el-col class="startitle" :span="8">
            <span class="star">封面图：</span>
          </el-col>
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
            <div class="img-list-item" v-if="coverPic">
              <span @click.stop="deleteImg('coverPic')"></span>
              <img :src="coverPic" class="shop-cover-img">
            </div>
            <div v-else class="add-img"></div>
          </el-upload>
          <p style="height:20px;color:red;" v-show="CheckCoverPicId">封面图不能为空</p>
        </el-form>
        <div class="footer">
          <el-button round class="lastBtn" @click="SaveBlog" :disabled="disBtn">保存</el-button>
          <el-button round class="lastBtn close" @click="Close">取消</el-button>
        </div>
      </el-main>
    </div>
    <!--富文本html区域-->
    <div v-show="false" v-html="textarea3" ref="editorContent"></div>
    <!--富文本html区域-->
  </div>
</template>

<script type="text/ecmascript-6">
// import { Quill, VueEditor } from "vue2-editor";
import Quill from "quill";
import VueEditor from '@/components/vueEditor/vueEditor'
import { ImageDrop } from "quill-image-drop-module";
import ImageResize from "quill-image-resize-module";
import {container, ImageExtend,QuillWatch} from 'quill-image-extend-module'

Quill.register("modules/imageDrop", ImageDrop);
Quill.register("modules/imageResize", ImageResize);
Quill.register('modules/ImageExtend', ImageExtend)
export default {
  data() {
    return {
      token: { Authorization: sessionStorage.getItem("token") },
      disBtn: false,
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
              action: 'upload-error',
              start: (value) => {

              },
              response: (res) =>{
                  return res.data.url
              }
          }
        }
      },
      introducedPicIds: [], // 富文本图片ID
      introducedPicImg: [], // 富文本图片信息
      CheckCoverPicId: false,
      CheckTitle: false,
      CheckContent: false,
      coverPic: "",
      textarea3: "",
      blogTitle: "",
      nav: [
        { nav: "店铺管理", url: "/business/shop" },
        {
          nav: "博文栏目",
          url: "/business/blog?shopType=" + this.$route.query.shopType
        },
        { nav: "新增博文栏目", url: "/business/blogOperation" }
      ]
    };
  },
  components: {
    VueEditor
  },
  methods: {
    //获取富文本的图片id
    acquireIntroducedPicId() {
      let img = this.$refs.editorContent.getElementsByTagName("img");
      let id = [];
      if (img.length > 0) {
        for (let i = 0; i < img.length; i++) {
          for (let j = 0; j < this.introducedPicImg.length; j++) {
            if (
              img[i].src.includes(this.BASE_URL.sourceBaseUrl) &&
              this.introducedPicImg[j].picPath.includes(
                this.BASE_URL.sourceBaseUrl
              )
            ) {
              if (
                decodeURI(img[i].src) ==
                decodeURI(this.introducedPicImg[j].picPath)
              ) {
                id.push(this.introducedPicImg[j].id);
              }
            } else {
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
      }
      this.introducedPicIds = id;
    },
    handleImageAdded(file,fileName, Editor, cursorLocation, resetUploader) {
      console.log(file);
      // if(file.size > 4000000){
      //    this.$message({
      //       type: "error",
      //       message: '上传的图片不能大于4M'
      //     });
      //     return
      // }
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      let formData = new FormData();
      formData.append("file", file,fileName);
      formData.append("fileKey", "company.shop.articlePic");
      formData.append("platform", "web");
      formData.append("module", "product");
      formData.append("type", "image");
      formData.append("businessId ", sessionStorage.getItem("shopId"));
      this.API.upLoadImg(formData).then(res => {
        if (res.code == 200) {
          loading.close();
          res.obj = {};
          res.obj.id = res.data.resId;
          res.obj.picPath = res.data.url;
          let url = res.data.url; // Get url from response
          if (res.data.resId) {
            this.introducedPicImg.push(res.obj);
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
    //取消保存
    Close() {
      this.$router.push({
        path: `/business/blog`,
        query: { shopType: this.$route.query.shopType }
      });
    },
    uploadCoverPic(res, file) {
      if (res.code == 200) {
        this.coverPic = res.data.url;
        this.coverPicId = res.data.resId;
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
    deleteImg(field) {
      this.paramsData[field] = null;
      this.paramsData[field + "Id"] = null;
      this.paramsData[field + "Path"] = null;
    },
    SaveBlog() {
      let flag = false;
      if (this.blogTitle == "" && this.textarea3 == "") {
        this.CheckTitle = true;
        this.CheckContent = true;
        flag=true;
      } else if (this.blogTitle == "") {
        this.CheckTitle = true;
        this.CheckContent = false;
        flag=true;
      } else if (this.textarea3 == "") {
        this.CheckTitle = false;
        this.CheckContent = true;
        flag=true;
      } else {
        this.CheckTitle = false;
        this.CheckContent = false;
      }
      if (this.coverPicId) this.CheckCoverPicId = false;
      else {this.CheckCoverPicId = true;flag=true;}
      if (flag) {
        return;
      }
      this.disBtn = true;
      this.acquireIntroducedPicId();
      console.log(this.introducedPicIds); //获取富文本编辑器的图片id
      this.API.addblog({
        content: this.textarea3,
        picIds: this.introducedPicIds.join(","),
        shopId: sessionStorage.getItem("shopId"),
        title: this.blogTitle,
        shopType: this.$route.query.shopType,
        coverPicId: this.coverPicId
      }).then(res => {
        console.log(res);
        if (res.success) {
          this.$message({
            type: "success",
            message: "新增博文成功!"
          });
          this.disBtn = false;
          this.$router.push({
            path: `/business/blog`,
            query: { shopType: this.$route.query.shopType }
          });
        } else {
          this.$message({
            type: "error",
            message: `新增博文失败：${res.message}!`
          });
          this.disBtn = false;
        }
      });
    }
  }
};
</script>

<style scoped lang="scss">
.addblogpage {
  .addblog {
    .el-main {
      background: #ffffff;
      position: relative;
      .contentHeader {
        font-size: 20px;
        &::after {
          content: "";
          width: 100%;
          height: 1px;
          background: #ccc;
          position: absolute;
          left: 0;
          top: 80px;
        }
      }
    }
    .titleRow {
      margin: 40px 0 0 80px;
      line-height: 40px;
      .startitle {
        width: 76px;
        .star {
          position: relative;
          &::after {
            content: "*";
            color: red;
            position: absolute;
            top: -11px;
            left: -6px;
          }
        }
      }
      .titleInput {
        width: 30%;
        word-wrap: break-word;
      }
    }
    .contentRow {
      margin: 20px 0 0 80px;
      min-height: 415px;
      .contentTitle {
        width: 76px;
        .star {
          position: relative;
          &::after {
            content: "*";
            color: red;
            position: absolute;
            top: 2px;
            left: -6px;
          }
        }
      }
      .editBlog {
        width: 90%;
        word-wrap: break-word;
        .editor {
          display: block;
          height: 330px;
        }
      }
    }
    .footer {
      margin: 30px 0 10px 120px;
      .lastBtn {
        width: 120px;
        color: #ffffff;
        background: #ff6419;
      }
      .close {
        background: white;
        color: #666666;
      }
    }
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
  background: url("../../../../assets/images/icons/uploadPlug.png") no-repeat 0
    0;
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
    background: url("../../../../assets/images/icons/delete3.png");
  }
  .shop-cover-img {
    height: 90px;
    border-radius: 4px;
    vertical-align: bottom;
  }
}
.img-text {
  color: #999999;
}
</style>

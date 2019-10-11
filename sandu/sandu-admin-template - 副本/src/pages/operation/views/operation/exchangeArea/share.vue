<template>
  <div>
    <!-- 列表页 -->
    <div v-if="pageStatus==0">
      <headers></headers>
      <searchBar class="t_join" :searchs="searchs" :onSearch="getList" :pageData="pageData"></searchBar>
      <el-row class="t_row">
        <el-button
          type="primary"
          @click="$router.push({path:'/operation/exchangeArea/carryArticles'})"
        >搬运文章</el-button>
        <el-button type="primary" @click="pageStatus=1;formData={blockTypeValueKey:'share'}">添加文章</el-button>
      </el-row>
      <el-row class="t_row t_join">
        <el-table :data="pageList" :header-cell-style="rowStyle" ref="refTable">
          <el-table-column prop="id" label="ID" align="center">
            <template slot-scope="scope">{{ scope.row.id }}</template>
          </el-table-column>
          <el-table-column prop="title" label="标题" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.title }}</template>
          </el-table-column>
          <el-table-column prop="shareType" label="类型" align="center">
            <template slot-scope="scope">{{ scope.row.shareType==0?'博文':'案例' }}</template>
          </el-table-column>
          <el-table-column prop="status" label="状态" align="center">
            <template slot-scope="scope">{{ statusStr[scope.row.status] }}</template>
          </el-table-column>
          <el-table-column prop="articleSource" label="来源" align="center">
            <template slot-scope="scope">{{ scope.row.articleSource==0?'运营':'搬运' }}</template>
          </el-table-column>
          <el-table-column prop="publishUserName" label="作者" align="center">
            <template slot-scope="scope">{{ scope.row.author }}</template>
          </el-table-column>
          <el-table-column prop="publishTime" label="动态时间" align="center">
            <template slot-scope="scope">{{ scope.row.publishTime }}</template>
          </el-table-column>
          <el-table-column prop="viewCount" label="参与人数(真)" align="center">
            <template slot-scope="scope">{{ scope.row.viewCount }}</template>
          </el-table-column>
          <el-table-column prop="userReplyCount" label="用户回复数" align="center">
            <template slot-scope="scope">{{ scope.row.userReplyCount||"-" }}</template>
          </el-table-column>
          <el-table-column prop="id" label="操作" align="center" width="270px">
            <template slot-scope="scope">
              <div v-if="scope.row.status!=2">
                <el-button
                  type="text"
                  @click="edit(scope.row,{status:1})"
                  v-if="scope.row.status==0"
                >发布</el-button>
                <el-button type="text" @click="edit(scope.row,{status:0})" v-else>取消发布</el-button>
                <el-button
                  type="text"
                  @click="editUserTag(scope.row)"
                  v-if="scope.row.shareType==0"
                >编辑</el-button>
                <el-button
                  type="text"
                  @click="editUserTag(scope.row)"
                  v-if="scope.row.shareType==1"
                >修改</el-button>
                <el-button type="text" @click="removeUserTag(scope.row.id)" key="1">删除</el-button>
                <el-button type="text" @click="comments(scope.row.id,true)">回复</el-button>
                <el-button type="text" @click="comments(scope.row.id)">评论管理</el-button>
              </div>
              <div slot-scope="scope" v-else>
                <el-button type="text" @click="removeUserTag(scope.row.id)" key="2">删除</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <el-row style="margin-top:20px;text-align:right">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="pageData.page"
            :page-sizes="[10, 20, 30, 40]"
            :page-size.sync="pageData.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total.sync="pageData.total"
          ></el-pagination>
        </el-row>
      </el-row>
    </div>
    <!-- 新增 / 编辑页 -->
    <div v-else-if="pageStatus==1 || pageStatus==2">
      <div class="t_row">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>
            <a @click.stop="pageStatus=0">文章管理</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <a>{{pageStatus==1?"新增":(formData.shareType==0?"编辑":"修改")}}</a>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <el-form
        class="t_row t_join"
        :model="formData"
        label-width="106px"
        style="text-align:left"
        ref="ruleForm"
        :rules="rules"
      >
        <el-form-item label="选择文章类型" prop="shareType" v-if="pageStatus==1">
          <!-- <div style="display:inline-block" @click.stop.prevent="watchShareType"> -->
          <div style="display:inline-block">
            <el-radio v-model="formData.shareType" :label="0">博文</el-radio>
            <el-radio v-model="formData.shareType" :label="1">案例</el-radio>
          </div>
        </el-form-item>
        <el-form-item label="当前文章类型" v-else>
          <div>{{formData.shareType==0?"博文":"案例"}}</div>
        </el-form-item>
        <template v-if="formData.shareType==1">
          <div class="titles">房屋信息</div>
          <el-form-item label="户型:" prop="bedroomNum">
            <el-input-number
              v-model="formData.bedroomNum"
              size="medium"
              style="width:120px"
              :min="1"
              :max="10"
            ></el-input-number>
            <span style="padding:0 10px">室</span>
            <el-input-number
              v-model="formData.livingRoomNum"
              size="medium"
              style="width:120px"
              :min="1"
              :max="10"
            ></el-input-number>
            <span style="padding:0 10px">厅</span>
          </el-form-item>
          <el-form-item label="使用面积：" prop="houseArea">
            <el-input
              v-model="formData.houseArea"
              type="number"
              size="medium"
              style="width:100px"
              oninput="if(value.length>4)value=value.slice(0,4)"
            ></el-input>
            <span>m²</span>
          </el-form-item>
          <el-form-item label="花费：" prop="houseCost">
            <el-input
              v-model="formData.houseCost"
              type="number"
              size="medium"
              style="width:100px"
              oninput="if(value.length>4)value=value.slice(0,4)"
            ></el-input>
            <span>万（元）</span>
          </el-form-item>
          <el-form-item label="风格：" prop="houseStyle">
            <el-select v-model="formData.houseStyle" filterable clearable placeholder="请选择方案风格">
              <el-option v-for="item in planStyles" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <div class="titles">文章内容</div>
          <el-form-item label="方案：" prop="planId">
            <div class="avatar-uploader" @click="selectPlan">
              <i class="avatar-uploader-icon el-icon-plus" v-if="!formData.planPicUrl"></i>
              <div v-else>
                <img class="avatar" :src="BASE_URL.sourceBaseUrl+formData.planPicUrl">
                <i
                  class="el-icon-error"
                  style="font-size:20px"
                  @click.stop="deleteImg('plan');formData.planUuId=0;"
                ></i>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="户型：" prop="houseCoverId">
            <el-upload
              v-loading="loadingHouse"
              class="avatar-uploader"
              :action="uploadImgUrlAction"
              :headers="headerToken"
              :data="{platform:'operationPlatform',module:'interactive',type:'image'}"
              :show-file-list="false"
              :on-success="uploadHouseTypePic"
              :before-upload="beforeAvatarUploadForHouse"
            >
              <div v-if="!formData.housePicUrl">
                <i class="avatar-uploader-icon el-icon-plus"></i>
              </div>
              <div v-else>
                <img class="avatar" :src="BASE_URL.sourceBaseUrl+formData.housePicUrl">
                <i class="el-icon-error" style="font-size:20px" @click.stop="deleteImg('house')"></i>
              </div>
            </el-upload>
          </el-form-item>
          <!-- <el-form-item label="封面图" prop="coverPicId">
            <el-upload
              v-loading="loadingCover"
              class="avatar-uploader"
              :action="uploadImgUrlAction"
              :headers="headerToken"
              :data="{platform:'operationPlatform',module:'interactive',type:'image'}"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <div v-if="!formData.coverPicPath">
                <i class="avatar-uploader-icon el-icon-plus"></i>
              </div>
              <div v-else>
                <img class="avatar" :src="BASE_URL.sourceBaseUrl+formData.coverPicPath">
                <i class="el-icon-error" style="font-size:20px" @click.stop="deleteImg('coverPic')"></i>
              </div>
            </el-upload>
            <pre
              style="text-align:left;line-height:26px;padding-left:10px;color:#999;display:inline-block"
            >尺寸宽度为750px，高度不限，最佳视觉展示效果
为750*422px</pre>
          </el-form-item>-->
        </template>
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="formData.title" maxlength="30"></el-input>
        </el-form-item>
        <el-form-item label="文章内容" prop="jsonContent">
          <vue-editor
            :editorOptions="{ modules: {
                imageDrop: true,
                imageResize: {}
              }}"
            v-model="formData.jsonContent"
            class="editor"
            useCustomImageHandler
            @imageAdded="handleImageAdded"
          ></vue-editor>
        </el-form-item>
        <el-form-item label="封面图" prop="coverPicPath" v-if="formData.articleSource!=1" key="2">
          <el-upload
            v-loading="loadingCover"
            class="avatar-uploader"
            :action="uploadImgUrlAction"
            :headers="headerToken"
            :data="{platform:'operationPlatform',module:'interactive',type:'image'}"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <div v-if="!formData.coverPicPath">
              <i class="avatar-uploader-icon el-icon-plus"></i>
            </div>
            <div v-else>
              <img class="avatar" :src="BASE_URL.sourceBaseUrl+formData.coverPicPath">
              <i class="el-icon-error" style="font-size:20px" @click.stop="deleteImg('coverPic')"></i>
            </div>
          </el-upload>
          <pre
            style="text-align:left;line-height:26px;padding-left:10px;color:#999;display:inline-block"
          >尺寸宽度为750px，高度不限，最佳视觉展示效果
为750*422px</pre>
        </el-form-item>
        <el-form-item label="封面图" prop="coverPicPath" v-else key="2">
          <el-upload
            v-loading="loadingCover"
            class="avatar-uploader"
            :action="BASE_URL.uploadUrl"
            :headers="headerToken"
            :data="{fileKey:'exchange.share',platform:'web',module:'product'}"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <div v-if="!formData.coverPicPath">
              <i class="avatar-uploader-icon el-icon-plus"></i>
            </div>
            <div v-else>
              <img
                class="avatar"
                :src="(formData.coverPicPath.indexOf('://')==-1?BASE_URL.sourceBaseUrl:'')+formData.coverPicPath"
              >
              <i class="el-icon-error" style="font-size:20px" @click.stop="deleteImg('coverPic')"></i>
            </div>
          </el-upload>
          <pre
            style="text-align:left;line-height:26px;padding-left:10px;color:#999;display:inline-block"
          >尺寸宽度为750px，高度不限，最佳视觉展示效果
为750*422px</pre>
        </el-form-item>
        <el-form-item label="浏览人数(假)" prop="virtualViewNum" style="width:400px">
          <el-input
            v-model="formData.virtualViewNum"
            type="number"
            oninput="if(value.length>5)value=value.slice(0,5)"
          ></el-input>
        </el-form-item>
        <el-form-item label="收藏人数(假)" prop="virtualFavoriteNum" style="width:400px">
          <el-input
            v-model="formData.virtualFavoriteNum"
            type="number"
            oninput="if(value.length>5)value=value.slice(0,5)"
          ></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author" style="width:400px">
          <span v-if="pageStatus==2&&formData.author&&formData.creatorType==0">{{formData.author}}</span>
          <el-select v-else v-model="formData.author" placeholder="请选择">
            <el-option
              v-for="item in robotList"
              :key="item.id"
              :label="item.robotName"
              :value="item.robotName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="validate" v-loading="submitLoading">确定</el-button>
          <el-button @click="pageStatus=0">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { Quill, VueEditor } from "vue2-editor";
import { ImageDrop } from "quill-image-drop-module";
import ImageResize from "quill-image-resize-module";
Quill.register("modules/imageDrop", ImageDrop);
Quill.register("modules/imageResize", ImageResize);

import headers from "./../../content/headers";
import searchBar from "./../../userTagsMessage/searchBar";
export default {
  components: {
    headers,
    searchBar,
    VueEditor
  },
  data() {
    let stringEmpty = (rule, value, callback) => {
      if (value.trim() === "") {
        callback(new Error("不能输入空值！"));
      } else {
        callback();
      }
    };
    return {
      submitLoading: false,
      loadingHouse: false,
      loadingCover: false,
      uploadImgUrlAction:
        this.BASE_URL.unionapiUrl + "/v1/union/supplydemandpic/uploadrespic",
      headerToken: {
        Authorization: sessionStorage.getItem("token")
      },
      searchs: [
        {
          fields: "title",
          placeholder: "输入信息标题",
          type: "text"
        },
        {
          fields: ["startTime", "endTime"],
          placeholder: "输入发布时间",
          type: "datetimerange"
        },
        {
          fields: "status",
          placeholder: "选择状态",
          type: "select",
          options: [
            { label: "未发布", value: 0 },
            { label: "已发布", value: 1 },
            { label: "原文已删除", value: 2 }
          ]
        }
      ],
      rules: {
        title: [
          { required: true, message: "请输入标题", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        content: [
          { required: true, message: "请输入内容", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        picIds: [{ required: true, message: "请上传图片", trigger: "change" }],
        jsonContent: [
          { required: true, message: "请输入文章内容", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        coverPicPath: [
          { required: true, message: "请上传封面图", trigger: "change" }
        ],
        shareType: [
          { required: true, message: "请输入文章类型", trigger: "blur" }
        ],
        planType: [
          { required: true, message: "请输入方案类型", trigger: "blur" }
        ],
        planCoverId: [
          { required: true, message: "请输入方案封面图", trigger: "blur" }
        ],
        author: [{ required: true, message: "请输入作者", trigger: "blur" }]
      },
      pageData: { page: 1, limit: 10, blockTypeValueKey: "share" },
      pageList: [],
      pageStatus: 0,
      isShowPopups: false,
      formData: { blockTypeValueKey: "share", author: "" },
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
      ],
      statusStr: ["未发布", "已发布", "原文已删除"],
      robotList: []
    };
  },
  watch: {
    "formData.author": function(nVal) {
      let newList = this.robotList.filter(e => e.robotName == nVal);
      this.formData.createUserId = newList.length == 0 ? 0 : newList[0].id;
      if (this.pageStatus == 1) this.formData.creatorType = 1;
    }
  },
  created() {
    this.getList();
    this.getOpertionData();
    if (this.$store.state.exchangeArea.formData) {
      this.formData = this.$store.state.exchangeArea.formData;
      this.$store.commit("SET_formData", null);
      this.pageStatus = 2;
    }
    if (this.$store.state.exchangeArea.checkPlan) {
      this.formData.planId = this.$store.state.exchangeArea.checkPlan.planId;
      this.formData.planUuId = this.$store.state.exchangeArea.checkPlan.uuid;
      this.formData.planCoverId = this.$store.state.exchangeArea.checkPlan.picId;
      this.formData.planPicUrl = this.$store.state.exchangeArea.checkPlan.picPath;
      this.formData.planType = this.$store.state.exchangeArea.checkPlan.planType;
      this.$store.commit("SET_checkPlan", null);
    }
  },
  methods: {
    getOpertionData() {
      this.API.opertionRobotData().then(res => {
        this.robotList = res.datalist || [];
      });
    },
    watchShareType() {
      if (this.formData.shareType != undefined) {
        this.$confirm(
          "此操作将切换文章类型，之前的内容将不被保存, 是否继续？",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        )
          .then(() => {
            this.$set(
              this.formData,
              "shareType",
              this.formData.shareType ? 0 : 1
            );
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消切换文章类型!"
            });
          });
      }
    },
    selectPlan() {
      this.$store.commit("SET_formData", this.formData);
      this.$router.push({
        path: "./shop/select_Plan"
      });
    },
    // 获取列表数据
    getList() {
      this.API.getInteractiveZoneTopicList(this.pageData).then(res => {
        if (res.success) {
          this.pageList = res.datalist;
          this.pageData.total = res.totalCount;
        }
      });
    },
    // 进入编辑页
    editUserTag(data) {
      this.API.getInteractiveZoneTopicDetail(data.id).then(res => {
        if (res.success) {
          this.pageStatus = 2;
          this.formData = res.obj;
          this.$set(
            this.formData,
            "virtualFavoriteNum",
            res.obj.collectVirtualCount
          );
          this.$set(this.formData, "virtualViewNum", res.obj.viewVirtualCount);
        } else {
          this.$message({
            message: res.message,
            type: res.success ? "success" : "error"
          });
        }
      });
    },
    // 进入评论管理
    comments(id, isAdd = false) {
      this.$router.push({ path: "/operation/exchangeArea/comments" });
      this.$store.commit("SET_commentsDate", {
        businessId: id,
        noImg: true,
        isAdd: isAdd,
        type: "share"
      });
    },
    // 删除操作
    removeUserTag(id) {
      this.$confirm("此操作将删除该条记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.API.updateInteractiveZoneTopic({ id: id, isDeleted: 1 }).then(
            res => {
              this.$message({
                message: res.success ? "删除成功!" : "删除失败!",
                type: res.success ? "success" : "error"
              });
              if (res.success) {
                this.pageStatus = 0;
                this.getList();
              }
            }
          );
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    // 校验
    validate() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return false;
        this.submit();
      });
    },
    // 编辑提示
    edit(data, edit) {
      this.$confirm(`是否${edit.status == 0 ? "取消" : ""}发布?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        edit && (this.formData = Object.assign(data, edit));
        this.submit();
      });
    },
    // 新增或编辑
    submit() {
      if (!this.submitLoading)
        (() => {
          this.submitLoading = true;
          this.formData.picIds = undefined;
          if (!this.formData.id)
            return this.API.addInteractiveZoneTopic(this.formData);
          else return this.API.updateInteractiveZoneTopic(this.formData);
        })().then(res => {
          this.$message({
            message: res.message,
            type: res.success ? "success" : "error"
          });
          if (res.success) {
            this.pageStatus = 0;
            this.getList();
          }
          this.submitLoading = false;
        });
    },
    deleteImg(field) {
      this.$set(this.formData, field, "");
      this.$set(this.formData, field + "Path", "");
      this.$set(this.formData, field + "Id", 0);
      this.$set(this.formData, field + "Img", "");
      this.$set(this.formData, field + "PicUrl", "");
      this.$set(this.formData, field + "CoverId", 0);
    },
    handleImageAdded(file, Editor, cursorLocation, resetUploader) {
      // const isLt4M = file.size / 1024 / 1024 < 4;
      // if (!isLt4M) {
      //   this.$message({
      //     type: "error",
      //     message: "图片大小要在4M以内!"
      //   });
      //   return;
      // }
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      let fDate = new FormData();
      fDate.append("file", file);
      fDate.append("type", "image");
      fDate.append("platform", "web");
      fDate.append("module", "product");
      fDate.append("fileKey", "operation.exchangeArea.share");
      this.API.upLoadImg(fDate).then(res => {
        if (res.code == 200) {
          res.obj = {};
          loading.close();
          res.obj.id = res.data.resId;
          res.obj.picPath = res.data.url;
          let url = res.data.url; // Get url from response
          if (res.data.resId) {
            if (this.formData.editorContentImg instanceof Array)
              this.formData.editorContentImg.push(res.obj);
            else this.formData.editorContentImg = [res.obj];
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
    // 上传图片成功
    uploadHouseTypePic(res, file) {
      this.$set(this.formData, "houseCoverId", res.obj.resId);
      this.$set(this.formData, "housePicUrl", res.obj.url);
      this.loadingHouse = false;
    },
    handleAvatarSuccess(res, file) {
      let obj = this.formData.articleSource != 1 ? res.obj : res.data;
      this.$set(this.formData, "coverPicId", obj.resId);
      this.$set(this.formData, "coverPicPath", obj.url);
      this.loadingCover = false;
    },
    // 上传图片前
    beforeAvatarUpload(file) {
      const isImg = /(jpeg|png|jpg|gif)/.test(file.type);
      if (!isImg) {
        this.$message.error("只能上传图片!");
        return false;
      }
      this.loadingCover = true;
      return true;
    },
    beforeAvatarUploadForHouse(file) {
      const isImg = /(jpeg|png|jpg|gif)/.test(file.type);
      if (!isImg) {
        this.$message.error("只能上传图片!");
        return false;
      }
      this.loadingHouse = true;
      return true;
    },
    // 分页模块f
    handleSizeChange: function(size) {
      this.pageData.limit = size;
      this.pageData.page = 1;
      this.pageList = [];
      this.getList();
    },
    handleCurrentChange: function(currentPage) {
      this.pageData.page = currentPage;
      this.getList();
    },
    rowStyle({ row, rowIndex }) {
      if (rowIndex === 0) {
        return {
          height: "30px!important",
          background: "#f5f5f5",
          color: "#666",
          lineHeight: "30px!important"
        };
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.t_row {
  margin: 20px 20px 0;
  padding: 20px 30px;
  background-color: #fff;
  text-align: left;
}
.t_join {
  margin-top: -20px;
}
.avatar-uploader {
  text-align: left;
  display: inline-block;
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 300px;
    height: 240px;
    line-height: 240px;
    text-align: center;
  }
  .avatar {
    width: 300px;
    height: auto;
    max-height: 800px;
    display: inline-block;
  }
  .el-icon-plus {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    position: relative;
    vertical-align: top;
  }
  .el-icon-plus:hover {
    border-color: #409eff;
  }
  .el-upload {
    vertical-align: top;
    text-align: left;
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
    // background: url("../../../assets/images/icons/uploadPlug.png") no-repeat 0 0;
  }
  .img-list-item {
    display: inline-block;
    position: relative;
    span {
      position: absolute;
      top: 36px;
      left: 82px;
      display: block;
      width: 18px;
      height: 16px;
      // background: url("../../../assets/images/icons/delete3.png");
    }
    .shop-cover-img {
      height: 90px;
      border-radius: 4px;
      margin-right: 10px;
      vertical-align: bottom;
    }
  }
}
.titles {
  font-weight: 700;
  border-left: 3px solid #555;
  padding-left: 10px;
  margin: 20px 0;
  font-size: 16px;
}
.editor {
  display: block;
  width: 1000px;
}
</style>

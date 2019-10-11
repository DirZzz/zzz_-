<template>
  <div>
    <!-- 列表页 -->
    <div v-if="pageStatus==0">
      <headers></headers>
      <searchBar class="t_join" :searchs="searchs" :onSearch="getList" :pageData="pageData"></searchBar>
      <!-- <el-row class="t_row">
        <el-button
          type="primary"
          @click="pageStatus=1;formData={blockTypeValueKey:'designReform'}"
        >新增</el-button>
      </el-row>-->
      <el-row class="t_row">
        <el-table :data="pageList" :header-cell-style="rowStyle" ref="refTable">
          <el-table-column prop="id" label="ID" align="center">
            <template slot-scope="scope">{{ scope.row.id }}</template>
          </el-table-column>
          <el-table-column prop="content" label="内容" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.content }}</template>
          </el-table-column>
          <el-table-column prop="designType" label="添加方案/户型" align="center">
            <template slot-scope="scope">{{ scope.row.designType }}</template>
          </el-table-column>
          <el-table-column prop="publishUserName" label="发布人" align="center">
            <template slot-scope="scope">{{ scope.row.publishUserName }}</template>
          </el-table-column>
          <el-table-column prop="publishTime" label="发布时间" align="center">
            <template slot-scope="scope">{{ scope.row.publishTime }}</template>
          </el-table-column>
          <el-table-column prop="userReplyCount" label="用户回复数" align="center">
            <template slot-scope="scope">{{ scope.row.userReplyCount||"-" }}</template>
          </el-table-column>
          <el-table-column prop="id" label="操作" align="center" width="250px">
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="edit(scope.row,{isTop:Date.parse(new Date())})"
                v-if="scope.row.isTop==0"
              >置顶</el-button>
              <el-button type="text" @click="edit(scope.row,{isTop:0})" v-else>取消置顶</el-button>
              <el-button type="text" @click="comments(scope.row.id)">评论管理</el-button>
              <el-button type="text" @click="editUserTag(scope.row)">编辑</el-button>
              <el-button type="text" @click="removeUserTag(scope.row.id)">删除</el-button>
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
            <a @click.stop="pageStatus=0">设计改造管理</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <a>{{pageStatus==1?"新增":"编辑"}}</a>
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
        <!-- <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" maxlength="30"></el-input>
        </el-form-item>-->
        <el-form-item label="内容" prop="content">
          <el-input
            type="textarea"
            v-model="formData.content"
            maxlength="100"
            :autosize="{ minRows: 4}"
          ></el-input>
        </el-form-item>
        <el-form-item label="方案" prop="planId" v-if="formData.planPicUrl">
          <div class="avatar-uploader">
            <div>
              <img class="avatar" :src="BASE_URL.sourceBaseUrl+formData.planPicUrl">
              <i class="el-icon-error" style="font-size:20px" @click.stop="removePlan"></i>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="户型" prop="housePicUrl" v-if="formData.housePicUrl">
          <!-- <el-upload
            class="avatar-uploader"
            :action="uploadImgUrlAction"
            :headers="headerToken"
            :data="{platform:'operationPlatform',module:'interactive',type:'image'}"
            :show-file-list="false"
            :on-success="uploadHouseTypePic"
            :before-upload="beforeAvatarUpload"
          >
          </el-upload>-->
          <div class="avatar-uploader">
            <div>
              <img class="avatar" :src="BASE_URL.sourceBaseUrl+formData.housePicUrl">
              <i
                class="el-icon-error"
                style="font-size:20px"
                @click.stop="deleteImg('housePicUrl')"
              ></i>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="收藏量(假)" prop="virtualFavoriteNum" style="width:400px">
          <el-input
            v-model="formData.virtualFavoriteNum"
            type="number"
            oninput="if(value.length>5)value=value.slice(0,5)"
          ></el-input>
        </el-form-item>
        <el-form-item label="浏览量(假)" prop="virtualViewNum" style="width:400px">
          <el-input
            v-model="formData.virtualViewNum"
            type="number"
            oninput="if(value.length>5)value=value.slice(0,5)"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item label="作者" prop="author" style="width:400px">
          <span v-if="pageStatus==2&&formData.author&&formData.creatorType==0">{{formData.author}}</span>
          <el-select v-else v-model="formData.author" placeholder="请选择">
            <el-option
              v-for="item in robotList"
              :key="item.id"
              :label="item.robotName"
              :value="item.robotName"
            ></el-option>
          </el-select>
        </el-form-item>-->
        <el-form-item label="发布人" prop="publishUserName" v-if="pageStatus==2" style="width:400px">
          <!-- <el-input v-model="formData.publishUserName" type="text" :disabled="true"></el-input> -->
          {{formData.publishUserName||"-"}}
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="validate">确定</el-button>
          <el-button @click="pageStatus=0">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import headers from "./../../content/headers";
import searchBar from "./../../userTagsMessage/searchBar";
export default {
  components: {
    headers,
    searchBar
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
      dialogImageUrl: "",
      dialogVisible: false,
      uploadImgUrlAction:
        this.BASE_URL.unionapiUrl + "/v1/union/supplydemandpic/uploadrespic",
      headerToken: {
        Authorization: sessionStorage.getItem("token")
      },
      searchs: [
        {
          fields: "content",
          placeholder: "请输入内容",
          type: "text"
        },
        {
          fields: ["startTime", "endTime"],
          placeholder: "输入发布时间",
          type: "datetimerange"
        }
      ],
      rules: {
        title: [
          { required: true, message: "请输入标题", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        // content: [
        //   { required: true, message: "请输入内容", trigger: "blur" },
        //   { validator: stringEmpty, trigger: "blur" }
        // ],
        picIds: [{ required: true, message: "请上传图片", trigger: "change" }],
        jsonContent: [
          { required: true, message: "请输入富文本内容", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        coverPicPath: [
          { required: true, message: "请上传封面图", trigger: "change" }
        ],
        shareType: [
          { required: true, message: "请输入大咖分享类型", trigger: "blur" }
        ],
        livingRoomNum: [
          { required: true, message: "请输入客厅数量", trigger: "blur" }
        ],
        bedroomNum: [
          { required: true, message: "请输入卧室数量", trigger: "blur" }
        ],
        houseArea: [{ required: true, message: "请输入面积", trigger: "blur" }],
        houseCost: [{ required: true, message: "请输入花费", trigger: "blur" }],
        houseStytle: [
          { required: true, message: "请输入风格", trigger: "blur" }
        ],
        planType: [
          { required: true, message: "请输入方案类型", trigger: "blur" }
        ],
        planCoverId: [
          { required: true, message: "请输入方案封面图", trigger: "blur" }
        ],
        houseCoverId: [
          { required: true, message: "请输入户型封面图", trigger: "blur" }
        ],
        author: [{ required: true, message: "请选择作者", trigger: "blur" }]
      },
      pageData: { page: 1, limit: 10, blockTypeValueKey: "designReform" },
      pageList: [],
      pageStatus: 0,
      isShowPopups: false,
      formData: { blockTypeValueKey: "designReform", author: "" },
      robotList: []
    };
  },
  // watch: {
  //   "formData.author": function(nVal) {
  //     let newList = this.robotList.filter(e => e.robotName == nVal);
  //     this.formData.createUserId = newList.length == 0 ? 0 : newList[0].id;
  //     this.formData.creatorType = 1;
  //   }
  // },
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
      this.$store.commit("SET_checkPlan", null);
    }
  },
  methods: {
    getOpertionData() {
      this.API.opertionRobotData().then(res => {
        this.robotList = res.datalist || [];
      });
    },
    selectPlan() {
      this.$store.commit("SET_formData", this.formData);
      this.$router.push({
        path: "./shop/select_Plan"
      });
    },
    deleteImg(field) {
      this.$set(this.formData, field, "");
      this.$set(this.formData, field + "Path", "");
      this.$set(this.formData, field + "Id", 0);
      this.$set(this.formData, field + "Img", "");
      this.$set(this.formData, "houseCoverId", 0);
      this.$set(this.formData, "houseId", 0);
    },
    removePlan() {
      this.$set(this.formData, "planId", 0);
      this.$set(this.formData, "planPicUrl", "");
      this.$set(this.formData, "planCoverId", 0);
      this.$set(this.formData, "planPicUrl", "");
    },
    // 获取列表数据
    getList() {
      this.searchs.forEach(item => {
        if (item.fields instanceof Array) {
          item.data &&
            item.data.forEach((e, i) => {
              this.pageData[item.fields[i]] = e;
            });
        } else this.pageData[item.fields] = item.data;
      });
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
    comments(id) {
      this.$router.push({ path: "/operation/exchangeArea/comments" });
      this.$store.commit("SET_commentsDate", {
        businessId: id,
        type: "designReform"
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
      this.$confirm(`是否${edit.isTop == 0 ? "取消" : ""}置顶?`, "提示", {
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
      (() => {
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
      });
    },
    uploadHouseTypePic(res, file) {
      this.$set(this.formData, "houseCoverId", res.obj.resId);
      setTimeout(() => {
        this.$set(this.formData, "housePicUrl", res.obj.url);
      }, 200);
    },
    // 上传图片前
    beforeAvatarUpload(file) {
      const isImg = /(jpeg|png|jpg|gif)/.test(file.type);
      if (!isImg) {
        this.$message.error("只能上传图片!");
        return false;
      }
      // const isLt4M = file.size / 1024 / 1024 < 4;
      // if (!isLt4M) {
      //   this.$message.error("上传的图片大小不能超过 4MB!");
      //   return isLt4M;
      // }
      return true;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
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
  }
}
</style>

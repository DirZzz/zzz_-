<template>
  <div>
    <!-- 列表页 -->
    <div v-if="pageStatus==0">
      <headers></headers>
      <searchBar class="t_join" :searchs="searchs" :onSearch="getList" :pageData="pageData"></searchBar>
      <el-row class="t_row">
        <el-button type="primary" @click="pageStatus=1;formData={blockTypeValueKey:'topic'}">发布话题</el-button>
      </el-row>
      <el-row class="t_row t_join">
        <el-table :data="pageList" :header-cell-style="rowStyle" ref="refTable">
          <el-table-column prop="id" label="ID" align="center">
            <template slot-scope="scope">{{ scope.row.id }}</template>
          </el-table-column>
          <el-table-column prop="title" label="话题名称" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.title }}</template>
          </el-table-column>
          <el-table-column prop="ordering" label="排序" align="center" width="136px">
            <template slot-scope="scope">
              <div v-if="!isEditOrdering[scope.row.id]">
                <span>{{ scope.row.ordering }}</span>
                <img
                  class="bianji_ico"
                  src="../../../assets/images/icon/icon_edit.png"
                  @click="$set(isEditOrdering,scope.row.id,true)"
                >
              </div>
              <div v-else style="display:flex;align-items:center">
                <input
                  v-model="scope.row.ordering"
                  class="bianji_input"
                  maxlength="4"
                  oninput="this.value=this.value.replace(/[^0-9]/g,'')"
                >
                <el-button-group>
                  <el-button
                    class="bianji_button"
                    style="border-radius:0px;border-left:none"
                    @click="formData=scope.row;submit();$set(isEditOrdering,scope.row.id,false)"
                  >
                    <img src="../../../assets/images/icon/icon_ok.png">
                  </el-button>
                  <el-button class="bianji_button" @click="$set(isEditOrdering,scope.row.id,false)">
                    <img src="../../../assets/images/icon/icon_close.png">
                  </el-button>
                </el-button-group>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" align="center">
            <template slot-scope="scope">{{ getStatusStr(scope.row.status) }}</template>
          </el-table-column>
          <el-table-column prop="publishUserName" label="发布人" align="center">
            <template slot-scope="scope">{{ scope.row.publishUserName }}</template>
          </el-table-column>
          <el-table-column prop="publishTime" label="发布时间" align="center">
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
              <el-button
                type="text"
                @click="edit(scope.row,{status:1})"
                v-if="scope.row.status==0"
              >发布</el-button>
              <el-button type="text" @click="edit(scope.row,{status:0})" v-else>取消发布</el-button>
              <el-button type="text" @click="comments(scope.row.id,true)">回复</el-button>
              <el-button type="text" @click="comments(scope.row.id)">评论管理</el-button>
              <el-button type="text" @click="editUserTag(scope.row)">编辑</el-button>
              <el-button
                type="text"
                v-if="scope.row.status==0"
                @click="removeUserTag(scope.row.id)"
              >删除</el-button>
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
            <a @click.stop="pageStatus=0">话题管理</a>
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
        <el-form-item label="话题主题" prop="title">
          <el-input v-model="formData.title" maxlength="30"></el-input>
        </el-form-item>
        <el-form-item label="详情" prop="content">
          <el-input
            type="textarea"
            v-model="formData.content"
            maxlength="1000"
            :autosize="{ minRows: 4}"
          ></el-input>
        </el-form-item>
        <el-form-item label="图片" prop="picIds">
          <el-upload
            class="avatar-uploader"
            :action="uploadImgUrlAction"
            :headers="headerToken"
            :data="{platform:'operationPlatform',module:'topic',type:'image'}"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :on-remove="handleRemove"
          >
            <div v-if="!formData.pics||formData.pics.length == 0" v-loading="loading">
              <i class="avatar-uploader-icon el-icon-plus"></i>
            </div>
            <div v-else v-loading="loading">
              <img class="avatar" :src="formData.pics[0].url">
              <i class="el-icon-error" style="font-size:20px" @click.stop="handleRemove"></i>
            </div>
          </el-upload>
          <pre
            style="text-align:left;line-height:26px;padding-left:10px;color:#999;display:inline-block;vertical-align:text-top"
          >尺寸宽度为750px，高度不限，最佳视觉展示效果
为750*422px</pre>
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
        <el-form-item label="排序" prop="ordering" v-if="pageStatus==2" style="width:400px">
          <el-input
            v-model="formData.ordering"
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
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" @click="validate" v-loading="submitLoading">确定</el-button>
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
      submitLoading: false,
      loading: false,
      isEditOrdering: {},
      dialogImageUrl: "",
      dialogVisible: false,
      uploadImgUrlAction:
        this.BASE_URL.unionapiUrl + "/v1/union/supplydemandpic/uploadrespic",
      headerToken: {
        Authorization: sessionStorage.getItem("token")
      },
      searchs: [
        {
          fields: "title",
          placeholder: "输入话题名称",
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
        content: [
          { required: true, message: "请输入内容", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
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
      pageData: { page: 1, limit: 10, blockTypeValueKey: "topic" },
      pageList: [],
      pageStatus: 0,
      isShowPopups: false,
      formData: { blockTypeValueKey: "topic", author: "" },
      robotList: []
    };
  },
  // watch: {
  //   "formData.author": function(nVal) {
  //     let newList = this.robotList.filter(e => e.robotName == nVal);
  //     this.formData.createUserId = newList.length == 0 ? 0 : newList[0].id;
  //     if (this.pageStatus == 1) this.formData.creatorType = 1;
  //   }
  // },
  created() {
    this.getList();
    this.getOpertionData();
  },
  methods: {
    getStatusStr(status) {
      switch (status) {
        case 0:
          return "未发布";
        case 1:
          return "已发布";
        case 2:
          return "已删除";
      }
    },
    getOpertionData() {
      this.API.opertionRobotData().then(res => {
        this.robotList = res.datalist || [];
      });
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

          if (this.formData.picIds) {
            this.formData.picIdsArr = this.formData.picIds.split(",");
            this.formData.pics = this.formData.pics.map(e => {
              return { id: e.id, url: this.BASE_URL.sourceBaseUrl + e.url };
            });
          }
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
        type: "topic",
        isAdd: isAdd
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
          this.formData.coverPicId = undefined;
          this.formData.coverPicPath = undefined;
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
    // 上传图片成功
    handleAvatarSuccess(res, file) {
      this.$set(this.formData, "pics", [
        { id: res.obj.resId, url: this.BASE_URL.sourceBaseUrl + res.obj.url }
      ]);
      this.loading = false;
      this.$set(this.formData, "picIds", `${res.obj.resId}`);
    },
    // 上传图片前
    beforeAvatarUpload(file) {
      const isImg = /(jpeg|png|jpg|gif)/.test(file.type);
      if (!isImg) {
        this.$message.error("只能上传图片!");
        return false;
      }
      this.loading = true;
      return true;
    },
    handleRemove() {
      this.formData.pics.pop();
      this.formData.picIds = "0";
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
  }
  .el-icon-plus:hover {
    border-color: #409eff;
  }
  .el-upload {
    vertical-align: top;
  }
}
.bianji_ico {
  width: 16px;
  height: 16px;
  vertical-align: middle;
  cursor: pointer;
}
.bianji_input {
  width: 50px;
  height: 32px;
  background-color: #f5f5f5;
  border-radius: 4px 0px 0px 4px;
  border: solid 1px #d9d9d9;
  padding: 0 6px;
}
.bianji_button {
  width: 32px;
  height: 32px;
  background-color: #fff;

  border: solid 1px #d9d9d9;
  padding: 0 6px;
}
</style>

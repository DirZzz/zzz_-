<template>
  <div>
    <el-row class="topBar">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <a @click="navigatorBack">返回上一页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item>评论管理</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <el-row class="topBar">
      <div style="display:flex;justify-content:space-between;align-items:center;">
        <span>评论管理</span>
        <el-button
          icon="el-icon-refresh"
          size="small"
          type="primary"
          plain
          @click="navigatorBack"
        >返回</el-button>
      </div>
    </el-row>
    <!-- 列表页 -->
    <div v-if="pageStatus==0">
      <headers title="评论查询"></headers>
      <searchBar class="t_join" :searchs="searchs" :onSearch="onSearch" :pageDate="pageData"></searchBar>
      <headers title="评论列表">
        <el-button
          style="margin:10px"
          size="small"
          type="primary"
          @click="deleteAll"
          :disabled="selectData.length==0"
        >批量删除</el-button>
      </headers>
      <el-row class="t_row t_join">
        <el-table
          :data="pageList"
          :header-cell-style="rowStyle"
          ref="refTable"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column type="index" width="50" label="序号"></el-table-column>
          <el-table-column prop="content" label="评论内容" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.content }}</template>
          </el-table-column>
          <el-table-column prop="gmtCreate" label="评论时间" align="center">
            <template slot-scope="scope">{{ scope.row.gmtCreate }}</template>
          </el-table-column>
          <el-table-column prop="userName" label="评论人" align="center">
            <template slot-scope="scope">{{ scope.row.userName }}</template>
          </el-table-column>
          <el-table-column prop="likeCount" label="点赞人数(真)" align="center">
            <template slot-scope="scope">{{ scope.row.likeCount }}</template>
          </el-table-column>
          <el-table-column prop="id" label="操作" align="center" width="250px">
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="edit(scope.row,{isTop:Date.parse(new Date())})"
                v-if="scope.row.isTop==0"
              >置顶</el-button>
              <el-button type="text" @click="edit(scope.row,{isTop:0})" v-else>取消置顶</el-button>
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
      <headers :title="`评论管理>${pageStatus==1?'新增':'编辑'}`"></headers>
      <el-form
        class="t_row t_join"
        :model="formData"
        label-width="106px"
        style="text-align:left"
        ref="ruleForm"
        :rules="rules"
      >
        <!-- <el-form-item label="评论时间" prop="gmtCreate">
          <el-date-picker
            v-model="formData.gmtCreate"
            type="datetime"
            placeholder="选择评论时间"
            :disabled="pageStatus==2"
            :picker-options="{disabledDate}"
            :maxTime="(new Date()).getHours()+':'+(new Date()).getMinutes()"
          ></el-date-picker>
        </el-form-item>-->
        <el-form-item label="评论内容" prop="content">
          <el-input
            type="textarea"
            v-model="formData.content"
            :maxlength="$store.state.exchangeArea.commentsDate.type=='topic'?1000:500"
            :autosize="{ minRows: 4}"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="图片"
          prop="picIds"
          v-if="!this.$store.state.exchangeArea.commentsDate.noImg"
        >
          <el-upload
            :action="uploadImgUrlAction"
            list-type="picture-card"
            :file-list="formData.pics"
            :headers="headerToken"
            :data="{platform:'operationPlatform',module:'interactive',type:'image'}"
            :on-success="handleAvatarSuccess"
            :on-preview="handlePictureCardPreview"
            :before-upload="beforeAvatarUpload"
            :on-remove="handleRemove"
            :disabled="imgLoading"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt>
          </el-dialog>
        </el-form-item>
        <el-form-item label="方案" prop style="width:400px" v-if="formData.planDetails">
          <div class="avatar-uploader">
            <img class="avatar" :src="BASE_URL.sourceBaseUrl+formData.planDetails.coverPath">
          </div>
        </el-form-item>
        <el-form-item label="点赞数(真)" prop="likeNum" style="width:400px" v-if="pageStatus==2">
          <el-input v-model="formData.likeNum" type="number" :disabled="true" maxlength="8"></el-input>
        </el-form-item>
        <el-form-item label="点赞数(假)" prop="virtualLikeNum" style="width:400px">
          <el-input
            v-model="formData.virtualLikeNum"
            type="number"
            oninput="if(value.length>5)value=value.slice(0,5)"
          ></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="userName" style="width:400px">
          <span
            v-if="pageStatus==2&&formData.userName&&formData.creatorType==0"
          >{{formData.userName}}</span>
          <el-select v-else v-model="formData.userName" placeholder="请选择">
            <el-option
              v-for="item in robotList"
              :key="item.id"
              :label="item.robotName"
              :value="item.robotName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="validate">确定</el-button>
          <el-button @click="back">返回</el-button>
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
      imgLoading: false,
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
          placeholder: "输入评论内容",
          type: "text"
        },
        {
          fields: ["startTime", "endTime"],
          placeholder: "输入发布时间",
          type: "datetimerange"
        }
      ],
      rules: {
        userName: [{ required: true, message: "请选择作者", trigger: "blur" }]
      },
      pageData: {
        page: 1,
        limit: 10,
        businessId: this.$store.state.exchangeArea.commentsDate.businessId
      },
      type: this.$store.state.exchangeArea.commentsDate.type,
      isAdd: this.$store.state.exchangeArea.commentsDate.isAdd,
      pageList: [],
      pageStatus: this.$store.state.exchangeArea.commentsDate.isAdd ? 1 : 0,
      isShowPopups: false,
      formData: {
        topicId: this.$store.state.exchangeArea.commentsDate.businessId,
        author: ""
      },
      selectData: [],
      robotList: []
    };
  },
  watch: {
    "formData.userName": function(nVal) {
      let newList = this.robotList.filter(e => e.robotName == nVal);
      this.formData.replyUserId = newList.length == 0 ? 0 : newList[0].id;
      if (this.pageStatus == 1) this.formData.creatorType = 1;
    }
  },
  created() {
    this.getList();
    this.getOpertionData();
  },
  methods: {
    navigatorBack() {
      this.$router.go(-1);
    },
    back() {
      this.isAdd ? this.navigatorBack() : (this.pageStatus = 0);
    },
    getOpertionData() {
      this.API.opertionRobotData().then(res => {
        this.robotList = res.datalist || [];
      });
    },
    disabledDate(date) {
      return date.getTime() > Date.now();
    },
    // 查询方法
    onSearch(isReset) {
      if (isReset)
        this.searchs = this.searchs.map(function(e, index) {
          if (e.data) e.data = null;
          return e;
        });
      this.getList();
    },
    getList() {
      this.searchs.forEach(item => {
        if (item.fields instanceof Array) {
          item.data &&
            item.data.forEach((e, i) => {
              this.pageData[item.fields[i]] = e;
            });
        } else this.pageData[item.fields] = item.data;
      });
      this.API.getInteractiveZoneReplyDetail(this.pageData).then(res => {
        if (res.success) {
          this.pageList = res.datalist;
          this.pageData.total = res.totalCount;
        }
      });
    },
    editUserTag(data) {
      this.formData = data;
      this.$set(this.formData, "likeNum", data.likeCount);
      this.$set(this.formData, "virtualLikeNum", data.likeVirtualCount);

      this.formData.picIdsArr = this.formData.picIds
        .split(",")
        .filter(e => e != 0);
      this.formData.pics =
        this.formData.pics &&
        this.formData.pics.map(e => {
          if (e.url && e.url.indexOf("://") != -1)
            return { id: e.id, url: e.url };
          else return { id: e.id, url: this.BASE_URL.sourceBaseUrl + e.url };
        });
      this.pageStatus = 2;
    },
    removeUserTag(id) {
      this.$confirm("此操作将删除该条记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.API.updateInteractiveZoneReplyTopic({
            id: id,
            isDeleted: 1
          }).then(res => {
            this.$message({
              message: res.success ? "删除成功!" : "删除失败!",
              type: res.success ? "success" : "error"
            });
            if (res.success) {
              this.pageStatus = 0;
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    handleSelectionChange(e) {
      this.selectData = e;
    },
    deleteAll() {
      this.$confirm("是否删除选中的信息?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let promises = this.selectData.map(e => {
            return this.API.updateInteractiveZoneReplyTopic({
              id: e.id,
              isDeleted: 1
            });
          });
          Promise.all(promises).then(res => {
            this.$message({
              message: res[0].success ? "删除成功!" : "删除失败!",
              type: res[0].success ? "success" : "error"
            });
            if (res[0].success) {
              this.pageStatus = 0;
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    validate() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return false;
        this.submit();
      });
    },
    edit(data, edit) {
      this.$confirm(`是否${edit.status == 0 ? "取消" : ""}置顶?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        edit && (this.formData = Object.assign(data, edit));
        this.submit();
      });
    },
    submit() {
      (() => {
        if (!this.formData.id) {
          this.formData.blockTypeValueKey = this.type;
          return this.API.addInteractiveZoneReplyTopic(this.formData);
        } else return this.API.updateInteractiveZoneReplyTopic(this.formData);
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
    // 上传图片成功
    handleAvatarSuccess(res, file) {
      this.formData.picIdsArr
        ? this.formData.picIdsArr.push(res.obj.resId)
        : (this.formData.picIdsArr = [res.obj.resId]);
      this.$set(this.formData, "picIds", this.formData.picIdsArr.join(","));
      this.imgLoading = false;
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
      if (this.formData.picIdsArr.length >= (this.type == "topic" ? 6 : 3)) {
        this.$message.error(
          `上传的图片数量不得超过${this.type == "topic" ? 6 : 3}张!`
        );
        return false;
      }
      this.imgLoading = true;
      return true;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleRemove(file, fileList) {
      let id = file.id;
      if (file.response) id = file.response.obj.resId;
      if (id) {
        this.formData.picIdsArr = this.formData.picIdsArr.filter(e => {
          return e != id;
        });
      }
      this.formData.picIds = this.formData.picIdsArr.join(",");
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
.topBar {
  padding: 20px 30px;
  background-color: #fff;
  text-align: left;
}
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
    display: block;
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
</style>

<template>
  <div>
    <div>
      <headers></headers>
      <el-row class="t_row">
        <el-button type="primary" @click="showMsgBox=true;pageStatus=1;formData={}">添加账号</el-button>
      </el-row>
      <el-row class="t_row t_join">
        <el-table
          :data="pageList"
          :header-cell-style="rowStyle"
          ref="refTable"
          style="text-align:left"
        >
          <el-table-column prop="id" label="ID" align="center">
            <template slot-scope="scope">{{ scope.row.id }}</template>
          </el-table-column>
          <el-table-column prop="robotName" label="昵称" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">{{ scope.row.robotName }}</template>
          </el-table-column>
          <el-table-column prop="headPicPath" label="头像" align="center" width="120px">
            <template slot-scope="scope">
              <img style="width:100px" :src="BASE_URL.sourceBaseUrl+scope.row.headPicPath">
            </template>
          </el-table-column>
          <el-table-column prop="gmtCreate" label="发布时间" align="center">
            <template slot-scope="scope">{{ scope.row.gmtCreate }}</template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="250px">
            <template slot-scope="scope">
              <el-button type="text" @click="edit(scope.row)">编辑</el-button>
              <el-button type="text" @click="remove(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row style="margin-top:20px;text-align:right">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="pageData.page"
            :page-sizes="[10, 20, 30, 40, 50]"
            :page-size.sync="pageData.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total.sync="pageData.total"
          ></el-pagination>
        </el-row>
      </el-row>
      <el-dialog :title="pageStatus==1?'添加账号':'编辑账号'" :visible.sync="showMsgBox" width="40%">
        <el-form ref="ruleForm" :model="formData" :rules="rules" label-width="58px">
          <el-form-item label="昵称" prop="robotName" style="width:300px">
            <el-input v-model="formData.robotName" maxlength="10"></el-input>
          </el-form-item>
          <el-form-item label="图片" prop="headPicPath">
            <el-upload
              class="avatar-uploader"
              :action="uploadImgUrlAction"
              :headers="headerToken"
              :data="{platform:'operationPlatform',module:'robotManage',type:'image'}"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              :on-remove="handleRemove"
            >
              <div v-loading="imgLoading">
                <div v-if="!formData.headPicPath">
                  <i class="avatar-uploader-icon el-icon-plus"></i>
                </div>
                <div v-else>
                  <img class="avatar" :src="BASE_URL.sourceBaseUrl+formData.headPicPath">
                  <i class="el-icon-error" style="font-size:20px" @click.stop="handleRemove"></i>
                </div>
              </div>
            </el-upload>
          </el-form-item>
          <el-form-item style="text-align:left">
            <el-button type="primary" @click="validate()">确定</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
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
      showMsgBox: false,
      uploadImgUrlAction:
        this.BASE_URL.unionapiUrl + "/v1/union/supplydemandpic/uploadrespic",
      headerToken: {
        Authorization: sessionStorage.getItem("token")
      },
      pageStatus: 0,

      rules: {
        robotName: [
          { required: true, message: "请输入昵称", trigger: "blur" },
          { validator: stringEmpty, trigger: "blur" }
        ],
        headPicPath: [
          { required: true, message: "请上传头像", trigger: "blur" }
        ]
      },
      pageList: [],
      pageData: { page: 1, pageSize: 10 },
      formData: {}
    };
  },
  created() {
    this.getList();
    this.submit = this.debouncer(this.submit);
  },
  methods: {
    // 防抖
    debouncer(fn, wait = 50) {
      let timer = 0;
      return function(...args) {
        if (!timer) fn.apply(this, args);
        clearTimeout(timer);
        timer = setTimeout(() => (timer = 0), wait);
      };
    },
    getList() {
      this.API.opertionRobotData("getList", this.pageData).then(res => {
        this.pageList = res.datalist || [];
        this.pageData.total = res.totalCount;
      });
    },
    edit(data) {
      this.formData = { ...data };
      this.pageStatus = 2;
      this.showMsgBox = true;
    },
    remove(id) {
      this.formData = { id, isDeleted: 1 };
      this.pageStatus = 2;
      this.$confirm("此操作将删除该条记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.submit("删除成功!");
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    submit(message) {
      if (this.imgLoading) return;
      this.API.opertionRobotData(
        this.pageStatus == 1 ? "add" : "update",
        this.formData,
        "post"
      ).then(res => {
        this.$message({
          message: (res.success && message) || res.message,
          type: res.success ? "success" : "error"
        });
        this.getList();
        this.showMsgBox = false;
      });
    },
    // 校验表单
    validate(flag) {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return false;
        if(!flag) this.submit();
      });
    },
    // 上传图片成功
    handleAvatarSuccess(res, file) {
      this.$set(this.formData, "headPicPath", res.obj.url);
      this.validate(1);
      this.imgLoading = false;
    },
    // 上传图片前
    beforeAvatarUpload(file) {
      this.imgLoading = true;
      const isImg = /(jpeg|png|jpg|gif)/.test(file.type);
      if (!isImg) {
        this.$message.error("只能上传图片!");
        return false;
      }
      return true;
    },
    handleRemove() {
      this.$set(this.formData, "headPicPath", undefined);
    },
    // 分页模块f
    handleSizeChange: function(size) {
      this.pageData.pageSize = size;
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

<style lang="scss">
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
    width: 150px;
    height: 150px;
    line-height: 150px;
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
</style>

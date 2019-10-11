<template>
  <div class="offline-activity">
    <div class="main-body">
      <div class="title">线下活动</div>
      <div class="usermanage-table-box">
        <div class="option-btn">
          <div class="option-btn-left">
            <el-button type="primary" @click="addActivity">新增活动</el-button>
          </div>
        </div>
        <div class="table">
          <el-table border v-loading="loading" :data="activityList" style="width: 100%">
            <el-table-column label="序号" align="center">
              <template slot-scope="scope">
                <div>{{page.pageSize*(page.currentPage-1)+(scope.$index+1)}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="activityName" align="center" label="活动名称" width="160">
              <template slot-scope="scope">
                <div class="activityName" :title="scope.row.activityName">{{scope.row.activityName}}</div>
              </template>
            </el-table-column>
            <el-table-column label="活动图片" align="center">
              <template slot-scope="scope">
                <img class="coverPic" :src="sourceBaseUrl+scope.row.coverPicId" alt />
              </template>
            </el-table-column>
            <el-table-column prop="companyName" align="center" label="参加活动企业"></el-table-column>
            <el-table-column prop="brandName" align="center" label="参加活动品牌"></el-table-column>
            <el-table-column label="活动有效期" align="center" width="180">
              <template slot-scope="scope">
                <div>{{scope.row.startTime}} - {{scope.row.endTime}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="gmtCreate" align="center" label="添加活动时间"></el-table-column>
            <!-- <el-table-column  align="center" label="链接"></el-table-column> -->
            <el-table-column prop="trueNum" align="center" label="已参与人数"></el-table-column>
            <el-table-column align="center" label="可购买/总数">
              <template slot-scope="scope">{{scope.row.notPayNum}}/{{scope.row.trueNum}}</template>
            </el-table-column>
            <el-table-column prop="status" align="center" label="活动状态"></el-table-column>

            <el-table-column label="操作" align="center" width="250">
              <template slot-scope="scope">
                <el-button type="text" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                <el-button type="text" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                <el-button type="text" @click="handleSetting(scope.$index, scope.row)">设置</el-button>
                <el-button type="text" @click="preview(scope.$index, scope.row)">预览</el-button>
                <el-button
                  type="text"
                  v-clipboard:copy="scope.row.copyUrl"
                  v-clipboard:success="onCopy"
                  v-clipboard:error="onError"
                >复制活动链接</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-box">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="page.currentPage"
              :page-sizes="[10, 20, 30, 40,50]"
              :page-size="page.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="page.total"
            ></el-pagination>
          </div>
        </div>
      </div>
    </div>
    <!-- 设置 -->

    <el-dialog
      title="设置"
      :close-on-click-modal="false"
      width="35%"
      center
      :visible.sync="settingDialog"
    >
      <el-form :inline="true">
        <el-form-item label="用户信息是否同步至商家">
          <el-radio-group v-model="isGiveToCompany">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item label="用户信息单价">
          <el-input type="number" min="1" v-model="price">
            <template slot="suffix">条/元</template>
          </el-input>
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="settingDialog = false">取 消</el-button>
        <el-button type="primary" @click="settingSave">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 预览 -->
    <el-dialog
      title="预览"
      :close-on-click-modal="false"
      width="35%"
      center
      :visible.sync="previewDialog"
    >
      <div class="preview-wrap">
        <img src="../../assets/images/icon/nav.png" alt />
        <div class="pre-content">
          <div class="image-box">
            <img :src="preView.coverPicId" class="image-bg" alt />
          </div>

          <div class="pre-bottom">
            <div class="bottom-left">
              <div class="person-num">
                <img src="../../assets/images/icon/ren_user.png" alt />
                <span :title="preView.num" class="num-box">{{preView.num}}</span>
                <span>人</span>
              </div>
              <div>已参与人数</div>
            </div>
            <div class="bottom-right">
              <img :src="preView.joinPicPath" class="right-bg" alt />
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      sourceBaseUrl: "",
      activityList: [],
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      loading: true,
      settingDialog: false,
      previewDialog: false,
      isGiveToCompany: 1,
      settingId: null,
      preView: {
        coverPicId: "",
        joinPicPath: "",
        num: ""
      }
    };
  },
  created() {
    this.getActivityList();
    let basePath = process.env;
    this.sourceBaseUrl = basePath.sourceBaseUrl;
  },
  components: {},
  methods: {
    addActivity() {
      this.$router.push({
        path: "/offlineActivity/addActivity",
        query: { type: "add" }
      });
    },
    // 获取列表
    getActivityList() {
      this.loading = true;
      this.API.getActivityList({
        limit: this.page.pageSize,
        start: this.page.currentPage
      }).then(res => {
        if (res.success) {
          this.loading = false;
          res.datalist.forEach(item => {
            item.startTime = item.startTime.split(" ")[0];
            item.endTime = item.endTime.split(" ")[0];
            item.gmtCreate = item.gmtCreate.split(" ")[0];
          });
          this.activityList = res.datalist;
          this.page.total = res.totalCount;
        }
      });
    },
    handleEdit(index, row) {
      let id = row.id;
      this.$router.push({
        path: "/offlineActivity/addActivity",
        query: { id: id, type: "edit" }
      });
    },
    handleDelete(index, row) {
      let self = this;
      let params = {
        id: row.id
      };
      this.$confirm(`确认删除${row.activityName}活动吗`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        self.API.deleteActivity(params).then(res => {
          if (res.success) {
            self.$message({
              showClose: true,
              type: "success",
              message: "删除成功!"
            });
            self.getActivityList();
          }
        });
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getActivityList();
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.getActivityList();
    },
    handleSetting(index, row) {
      this.API.setInfo({ id: row.id }).then(res => {
        this.isGiveToCompany = res.obj.isGiveToCompany;
      });
      this.settingDialog = true;
      this.settingId = row.id;
    },
    //
    settingSave(id) {
      // 设置确认
      this.API.set({
        id: this.settingId,
        isGiveToCompany: this.isGiveToCompany
      }).then(res => {
        if (res.success) {
          this.$message.success("设置成功");
          this.settingDialog = false;
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    preview(index, row) {
      this.API.getDetailInfo({ id: row.id }).then(res => {
        if (res.success) {
          this.preView.coverPicId = this.sourceBaseUrl + res.obj.coverPicId;
          (this.preView.joinPicPath = this.sourceBaseUrl + res.obj.joinPicPath),
            (this.preView.num = res.obj.selfDefineNum + res.obj.trueNum);
          this.previewDialog = true;
        }
      });
    },
    onCopy() {
      this.$message.success("复制活动链接成功!");
    },
    onError() {
      this.$message.warning("复制活动链接失败!");
    }
  },
  watch: {}
};
</script>
<style lang='scss' scoped>
.offline-activity {
  padding: 30px;
  .main-body {
    min-height: 860px;
    background: #fff;
    .title {
      font-size: 18px;
      color: #222;
      padding: 15px 30px;
      border-bottom: 1px solid #eee;
      text-align: left;
    }
    .usermanage-table-box {
      padding: 30px;
      .option-btn {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .option-btn-left {
          display: flex;
        }
      }
      .table {
        margin-top: 30px;
      }
    }
  }
  .pagination-box {
    margin-top: 30px;
  }
  .coverPic {
    max-width: 100px;
    height: 100px;
    object-fit: contain;
  }
  .activityName {
    width: 150px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .preview-wrap {
    display: flex;
    flex-direction: column;
    width: 375px;
    justify-content: center;
    align-items: center;
    margin: 0 auto;
    .pre-content {
      width: 375px;
      height: 667px;
      position: relative;
      overflow-y: scroll;
      .image-box {
        min-height: 606px;
      }
      .image-bg {
        width: 100%;

        object-fit: contain;
        height: auto;
        background: burlywood;
        display: block;
      }
      .pre-bottom {
        width: 370px;
        height: 60px;
        background: pink;
        position: sticky;
        z-index: 999;
        bottom: 0;
        left: 0;
        display: flex;
        .bottom-left {
          img {
            width: 25px;
            height: 20px;
            margin-right: 5px;
          }
          .person-num {
            font-size: 20px;
            padding-bottom: 5px;
            .num-box {
              max-width: 50px;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
              display: inline-block;
            }
          }
          color: #333;
          width: 30%;
          height: 60px;
          background-color: #ffffff;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
        }
        .bottom-right {
          width: 70%;
          height: 60px;
          background: red;
          overflow: hidden;
          img {
            height: 100%;
            width: 100%;
            object-fit: cover;
          }
        }
      }
    }
  }
}
</style>
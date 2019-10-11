<template>
  <section class="maps">
    <!--工具条-->
      <el-form :inline="true">
        <div class="grid-content bg-purple-dark el-col">
          <el-row :gutter="24" style="margin:0;padding:20px">
            <div class="input_round">
              <el-form>
                <el-col :span="3" class="searchInput">
                  <el-input v-model="query.modelNumber" placeholder="请输入材质型号"></el-input>
                </el-col>
                <el-col :span="3" class="searchInput">
                  <el-input v-model="query.textureName" placeholder="请输入贴图名称"></el-input>
                </el-col>
                <el-col :span="3" class="searchInput">
                  <el-input v-model="query.textureCode" placeholder="请输入贴图编号"></el-input>
                </el-col>
                <el-col :span="3" class="searchInput">
                  <input
                    oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                    v-model="query.length"
                    placeholder="请输入长度"
                    class="sizes"
                    maxlength="6"
                  >
                </el-col>
                <el-col :span="3" class="searchInput">
                  <input
                    v-model="query.width"
                    oninput="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                    placeholder="请输入宽度"
                    class="sizes"
                    maxlength="6"
                  >
                </el-col>
                <el-col :span="5"></el-col>
              </el-form>
            </div>
          </el-row>
          <el-row :gulter="24" style="border-bottom:1px solid #ccc;padding:0 32px 20px 32px;margin:0;">
            <el-col :span="3" style="width: 196px">
              <template>
                <!--<span class="labelColor">材质表面属性</span>-->
                <el-select
                  v-model="query.textureAttr"
                  @change="getTextureList(1)"
                  clearable
                  placeholder="请选择材质表面属性"
                >
                  <el-option
                    v-for="item in textureAttrsDrop"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </template>
            </el-col>
            <el-col :span="3" style="margin-left: 25px;width: 196px">
              <el-select
                v-model="query.transStatus"
                @change="getTextureList(1)"
                clearable
                placeholder="请选择材质球转化状态"
              >
                <el-option
                  v-for="item in statusDeal"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>

            <el-col :span="3" style="margin-left: 25px;width: 196px">
              <el-select
                v-model="query.textureType"
                style="width: 194px"
                @change="getTextureList(1)"
                clearable
                placeholder="请选择材质类型"
              >
                <el-option
                  v-for="item in textureType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span='3' style="margin-left: 25px;width: 196px">
              <el-select
                      filterable
                      clearable
                      @change="getTextureList(1)"
                      style="width: 196px"
                      v-model="query.texture"
                      placeholder="请选择材质材料">
                <el-option
                        v-for="item in texturesDrop"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="3" style="margin-left: 20px;width: 196px">
              <el-select
                      filterable
                      clearable
                      @change="getTextureList(1)"
                      v-model="query.Classification"
                      style="width: 196px"
                      placeholder="请选择细分类">
                <el-option
                        v-for="item in classificationList"
                        style="width: 196px"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="3" style="margin-left: 20px">
              <el-select
                v-model="query.secrecyFlag"
                style="width: 194px"
                @change="getTextureList(1)"
                clearable
                placeholder="请选择公开状态"
              >
                <el-option label="公开" :value="1"></el-option>
                <el-option label="未公开" :value="0"></el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row style="padding: 20px 32px;display: table;text-align: center;width: 100%">
            <div style="display:inline-block;">
              <el-button round @click="getTextureList(1)" class="UnifiedsearchBtn" type="primary">搜索</el-button>
              <el-button round @click="clearQuery" class="UnifiedsearchBtn">重置</el-button>
            </div>
            <el-button
              round
              class="orangeBtn UnifiedsearchBtn"
              v-if="isPer('texture:add')"
              plain
              type="primary"
              style="width: 110px;float:right;margin-left: 20px"
              @click="handleBatchAdd"
            >批量新增贴图</el-button>
            <el-button
              round
              class="orangeBtn UnifiedsearchBtn"
              v-if="isPer('texture:add')"
              plain
              type="primary"
              style="width: 110px;float:right;"
              @click="handleAdd"
            >新增贴图</el-button>
          </el-row>
        </div>
      </el-form>

    <!--列表-->
    <div class="tableFilter" v-if="isPer('texture:del')" style="clear: both">
      <!--<span class="el-icon-delete"></span>-->
      <span @click="handleBatchDelete" class="delete">批量删除</span>
      <span @click="ProductSecrecy(1)" class="open">批量公开</span>
      <span @click="ProductSecrecy(0)" class="offOpen">批量取消公开</span>
      <el-button size="small" type="text" @click="refreshData" class="refresh">刷新</el-button>
    </div>
    <el-table
      :data="list"
      v-loading="textureLoading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(255, 255, 255, 0.8)"
      :default-sort="{prop: 'gmtModified', order: 'descending'}"
      @selection-change="selsChange"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="textureCode" label="编号" align="center" width="120">
        <template slot-scope="scope">
          <div v-if="scope.row.textureCode">{{scope.row.textureCode}}</div>
          <div v-else>———</div>
        </template>
      </el-table-column>
      <el-table-column prop="pic" label="缩略图" width="140">
        <template slot-scope="scope">
          <div v-if="scope.row.thumbnailPath">
            <img
              style="cursor:pointer;"
              @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.thumbnailPath)"
              :src="BASE_URL.sourceBaseUrl+scope.row.thumbnailPath"
              width="90"
              height="90"
            >
          </div>
          <div class="nonImg" v-else>暂无图片</div>
        </template>
      </el-table-column>
      <el-table-column prop="textureName" label="材质球/贴图名称" align="center" width="160">
        <template slot-scope="scope">
          <div
            v-if="scope.row.textureName"
            style="text-align: center; display: inline-block"
            class="remarkOverEllipsis"
          >{{scope.row.textureName}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="贴图尺寸" width="160">
        <template slot-scope="scope">
          <div>{{scope.row.fileWidth + 'cm &times; ' + scope.row.fileLength + 'cm'}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="textureAttrValueName" label="材质表面属性" align="center" width="150">
        <template slot-scope="scope">
          <p v-if="scope.row.textureAttrValueName">{{scope.row.textureAttrValueName}}</p>
          <p v-else>———</p>
        </template>
      </el-table-column>
      <el-table-column prop="modelNumber" label="材质型号">
        <template slot-scope="scope">
          <div class="modifyTime" v-if="scope.row.modelNumber">{{scope.row.modelNumber}}</div>
          <div v-else>———</div>
        </template>
      </el-table-column>
      <el-table-column prop="textureballFileId" label="贴图类型" align="center" width="100">
        <template slot-scope="scope">
          <div class="modifyTime">{{scope.row.textureballFileId == 0 ? '贴图' : '材质球'}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="transStatus" label="材质球转化状态" align="center" width="100">
        <template slot-scope="scope">
          <div v-if="scope.row.transStatus">
            <span v-if="scope.row.transStatus === 'ING'">转化中</span>
            <span v-if="scope.row.transStatus === 'SUCCESS'">转化成功</span>
            <span v-if="scope.row.transStatus === 'FAIL'">转化失败</span>
            <span v-if="scope.row.transStatus === 'NONE'">未处理</span>
          </div>
          <div v-else>———</div>
        </template>
      </el-table-column>
      <el-table-column prop="gmtModified" width="180" align="center" label="最新修改时间" sortable>
        <template slot-scope="scope">
          <div class="modifyTime" v-if="scope.row.gmtModified">{{scope.row.gmtModified}}</div>
          <div v-else class="modifyTime">———</div>
        </template>
      </el-table-column>
      <el-table-column prop="secrecyFlag" class="remarkOverEllipsis" label="公开状态">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.secrecyFlag"
            active-color="#f16321"
            :active-value="1"
            :inactive-value="0"
            @change="ProductSecrecy(scope.row.secrecyFlag,[scope.row.textureId])"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" v-if="isPer('texture:view')" @click="handleDetail(scope.row)">详情</el-button>
          <el-button type="text" v-if="isPer('texture:edit')" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" v-if="isPer('texture:del')" @click="handleDel(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <div class="pagination">
      <el-pagination
        :current-page.sync="query.page"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[10, 50, 100, 200,500]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </div>
    <img-pre-view ref="sonParams"></img-pre-view>

    <!--批量新增贴图-->
    <batchAdd ref="batchAdd"></batchAdd>
    <!--导入文件加载过程-->
    <fileLoading ref="fileLoading"></fileLoading>
  </section>
</template>

<script>
import qs from "qs";
import ImgPreView from "@/components/Img-preview";
import batchAdd from "./components/batchAdd";
import fileLoading from "./components/fileLoading";
export default {
  data() {
    return {
      textureLoading: true,
      list: [],
      texturesDrop: [],
      classificationList: [],
      query: {
        page: 1,
        limit: 10,
        transStatus: "",
        textureType: "",
        secrecyFlag: undefined,
        width: undefined,
        length: undefined,
        companyId: sessionStorage.getItem("companyID")
      },
      total: 0,
      sels: [],
      textureAttrsDrop: [],
      textureType: [
        {
          value: "texture",
          label: "贴图"
        },
        {
          value: "ball",
          label: "材质球"
        }
      ],
      statusDeal: [
        {
          value: "ING",
          label: "转化中"
        },
        {
          value: "SUCCESS",
          label: "转化成功"
        },
        {
          value: "FAIL",
          label: "转化失败"
        }
        //, {
        //   value: 'HANDLE',
        //   label: '需人工处理'
        // }
      ]
    };
  },
  components: {
    ImgPreView,
    batchAdd,
    fileLoading
  },
  activated() {
    this.getTextureList();
    this.getTextureAttrsDrop();
    this.getTexturesDrop();
    this.initClassificationList();
  },
  methods: {
    getTexturesDrop() {
        this.API.ProductTextureDrop().then(resp => {
            this.texturesDrop = resp.data;
        });
    },
    initClassificationList() {
        this.API2.getClassification().then(res => {
            this.classificationList = res.data;
        });
    },
    //图片预览事件
    ImgView(ImgURL) {
      this.$refs.sonParams.sonFun({ ImgURL: ImgURL, ImgShow: true });
    },
    isPer(per) {
      let permiss = qs.parse(sessionStorage.getItem("loginUser"));
      return this.isMainPer(per, permiss);
    },
    refreshData() {
      this.$nextTick(function() {
        this.textureLoading = true;
        this.getTextureList();
      });
    },
    getTextureAttrsDrop() {
      this.API.textureAttr().then(resp => {
        this.textureAttrsDrop = resp.data;
      });
    },
    getTextureList(type) {
      this.textureLoading = true;
      type == 1 ? (this.query.page = 1) : "";
      let obj = { companyId: sessionStorage.getItem("companyID") };
      const params = Object.assign({}, this.query, obj);
      this.API.ListTexture(params).then(resp => {
        if (resp) {
          this.list = resp.list;
          this.total = resp.total;
        }
        this.textureLoading = false;
      });
    },
    handleAdd() {
      localStorage.setItem("breadcrumb3", "新增材质");
      this.$router.push("/texture/add");
    },
    handleBatchAdd() {
      //批量新增贴图
      this.$refs.batchAdd.isShow(true);
    },
    handleDetail(row) {
      localStorage.setItem("breadcrumb3", "贴图详情");
      this.$store.commit("breadcrumb3", "贴图详情");
      this.$router.push(`/texture/${row.textureId}`);
    },
    handleEdit(row) {
      localStorage.setItem("breadcrumb3", "材质编辑");
      this.$router.push(`/texture/${row.textureId}/edit`);
    },
    selsChange: function(sels) {
      this.sels = sels;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 分页模块
    handleSizeChange(val) {
      this.query.limit = val;
      this.getTextureList();
    },
    handleCurrentChange(val) {
      this.textureLoading = true;
      this.query.page = val;
      this.getTextureList();
    },
    clearQuery() {
      this.query = {
        page: 1,
        limit: 10,
        companyId: sessionStorage.getItem("companyID")
      };
      this.getTextureList();
    },
    // 删除
    handleDel(row) {
      this.$confirm("确认删除该记录吗?", "提示", {
        cancelButtonClass: "cancelButtonClass",
        confirmButtonClass: "confirmButtonClass",
        center: true,
        type: "warning"
      }).then(() => {
        this.API.DeleteTexture(row.textureId).then(resp => {
          console.log(typeof resp.code);
          if (resp.code == 204) {
            console.log(1);
            this.$message({
              type: "error",
              message: resp.message
            });
          } else {
            this.$message({
              type: "success",
              message: "删除成功"
            });
          }
          this.getTextureList();
        });
      });
    },
    handleBatchDelete() {
      if (this.sels.length > 0) {
        this.$confirm("确认删除记录吗?", "提示", {
          type: "warning"
        }).then(() => {
          this.API.DeleteTexture(
            this.sels.map(it => it.textureId).join(",")
          ).then(resp => {
            this.$message.success("删除成功");
            this.getTextureList();
          });
        });
      } else {
        this.$message.error("请至少选择一条记录");
      }
    },
    ProductSecrecy(operationType, ids) {
      if (!ids && this.sels.length == 0) {
        this.$message.error(
          `请选择需要${
            operationType == 0 ? "取消" : ""
          }公开的材质数据，再进行批量操作。`
        );
        return false;
      }
      this.$confirm(
        `确定${operationType == 0 ? "取消" : ""}公开选中材质贴图？`,
        "提示",
        {
          type: "warning"
        }
      )
        .then(() => {
          let fd = new FormData();
          fd.append("ids", ids || this.sels.map(it => it.textureId).join(","));
          fd.append("operationType", operationType);
          this.API2.updateSecrecyFlag(fd).then(resp => {
            this.$message({
              type: resp.code == 200 ? "success" : "warning",
              message:
                resp.code == 200
                  ? `${operationType == 0 ? "取消" : ""}公开成功`
                  : resp.message
            });
          });
        })
        .finally(() => this.getTextureList());
    }
  }
};
</script>
<style scoped lang="scss">
@import "../../../assets/css/mixin.scss";
.modifyTime {
  width: 80px;
  display: inline-block;
  text-align: center;
}
.searchInput {
  width: 220px;
}
.searchButton {
  font-weight: bold;
  border: 1px solid #ff6419;
  color: #ff6419;
  width: 110px;
}
.sizes {
  border: solid 1px #ddd;
  width: 196px;
  height: 40px;
  border-radius: 4px;
  padding-left: 10px;
  margin-right: 20px;
  box-sizing: border-box;
  &::placeholder{
    color: #999;
  }
}
.nonImg {
  display: inline-block;
  background: #f2f2f2;
  height: 90px;
  width: 90px;
  line-height: 90px;
  text-align: center;
  font-size: 12px;
  color: #999;
  border: solid 1px #e3e3e3;
}
.button {
  text-align: left !important;
}
.operatonBtn {
  float: left;
  margin-left: 5px !important;
  display: block;
  font-size: 14px;
}
.maps {
  position: relative;
  margin-bottom: 20px;
  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }

  .el-col {
    border-radius: 4px;
  }

  .bg-purple-dark {
    background: $bg-color-white;
  }

  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }

  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .input_round {
    border-radius: 30px;
  }
  .tableFilter {
    position: relative;
    font-size: 14px;
    top: 10px;
    padding: 20px;
    background: $bg-color-white;
    border-radius: 4px 4px;
    cursor: pointer;
    .refresh {
      font-size: 14px;
      margin-left: 12px;
      padding-left: 25px;
      color: #666;
      background: url("../../../assets/images/icons/reflesh.png") 0 center
        no-repeat;
      &:hover {
        font-size: 14px;
        color: $overButtonOrange;
        background: url("../../../assets/images/icons/reflesh2.png") 0 center
          no-repeat;
      }
    }
    .open {
      display: inline-block;
      line-height: 30px;
      margin-left: 12px;
      padding-left: 25px;
      height: 30px;
      background: url("../../../assets/images/icons/secrecy.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/secrecy2.png") 0 center
          no-repeat;
        color: $overButtonOrange;
      }
    }
    .offOpen {
      display: inline-block;
      line-height: 30px;
      margin-left: 12px;
      padding-left: 25px;
      height: 30px;
      background: url("../../../assets/images/icons/batchSecrecy.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/batchSecrecy2.png") 0
          center no-repeat;
        color: $overButtonOrange;
      }
    }
    .delete {
      display: inline-block;
      line-height: 30px;
      height: 30px;
      margin-left: 12px;
      padding-left: 25px;
      color: #666;
      background: url("../../../assets/images/icons/delete.png") 0 center
        no-repeat;
      &:hover {
        background: url("../../../assets/images/icons/delete2.png") 0 center
          no-repeat;
        color: $overButtonOrange;
      }
    }
  }

  .el-button--small {
    padding: 6px 12px;
    margin: 10px 0 0 0;
  }
  .pagination {
    background: #fff;
    height: 110px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    text-align: center;
    padding-top: 40px;
    .block {
      margin-top: 24px;
      text-align: center;
    }
  }
  .addMaps,
  .editMaps,
  .DetailMaps {
    position: absolute;
    left: -20px;
    top: -20px;
    padding: 20px;
    min-width: 100%;
    height: 100%;
    background: #e4e4e4;
    z-index: 99;
    .titel {
      line-height: 80px;
    }
    .breadcrumb {
      width: 100%;
      height: 80px;
      text-indent: 20px;
      background: #fff;
      line-height: 80px;
    }
    .orangeBtn {
      width: 133px;
      margin: 20px 20px 0 0;
      color: #fff;
      background: $font-color-orange;
      &:hover {
        background: #ff7a39;
      }
      &:active {
        background: #fa580a;
      }
      .iconBack {
      }
    }
    .contentWrap {
      width: 97.6%;
      height: 100%;
      margin-top: 10px;
      padding: 20px;
      background: #fff;
      .titel {
        font-size: 20px;
        line-height: 36px;
        font-weight: bold;
      }
      .hr {
        width: 97.6%;
        padding: 0 20px;
        border-bottom: 1px solid #eee;
      }
      .describe {
        margin-top: 40px;
        font-size: 14px;
        i {
          color: red;
          margin-right: 10px;
          vertical-align: middle;
        }
      }
      .mapsInfo {
        margin-top: 10px;
        margin-bottom: 30px;
        height: 40px;
        background: #f6f6f6;
        line-height: 40px;
        i {
          display: inline-block;
          width: 6px;
          height: 40px;
          background: red;
          vertical-align: middle;
        }
        span {
          font-size: 16px;
          font-weight: bold;
          margin-left: 10px;
        }
      }
      .size-w,
      .size-h {
        display: inline-block;
      }
      .mapsSelect {
        display: block;
      }
      .desc {
        max-width: 960px;
        height: 130px;
      }
      .blackBtn {
        color: $bg-color-white;
        background: $greyButton;
      }
    }
  }
}
</style>


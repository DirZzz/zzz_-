<template>
  <div>
    <div class="main-body">
      <h1>小程序管理后台</h1>
      <div class="search-wrap">
        <div class="search-box">
          <div class="search-item">
            <div class="label-name">小程序名称：</div>
            <el-input v-model="miniName"></el-input>
          </div>
          <div class="search-item">
            <div class="label-name">所属企业：</div>
            <el-input v-model="comName"></el-input>
          </div>
          <div class="search-item">
            <div class="label-name">小程序类型：</div>
            <el-select v-model="miniType" clearable placeholder="请选择">
              <el-option
                v-for="item in miniTypeOptions"
                :key="item.type"
                :label="item.name"
                :value="item.type"
              ></el-option>
            </el-select>
          </div>
        </div>
        <div class="search-btn">
          <el-button type="primary" @click="searchMini">搜索</el-button>
          <el-button type="primary" @click="resetMini">重置</el-button>
        </div>
      </div>
      <div class="table-box">
        <div class="table-option-btn">
          <el-button type="primary" @click="addProgram">新增</el-button>
          <el-button type="primary" @click="excelMiniList">导出</el-button>
        </div>
        <el-table ref='multipleTable' v-loading="loading" :data="miniList" style="width: 100%">
          <el-table-column label="序号" width="180" align="center">
             <template slot-scope="scope">
              <div>{{(page.currentPage-1)*page.pageSize+(scope.$index+1)}}</div>
            </template>
          </el-table-column>
          <el-table-column label="小程序名称" width="180" align="center">
              <template slot-scope="scope">
              <div :title="scope.row.name" class="mini-name">{{scope.row.name}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="company" label="所属企业" align="center"></el-table-column>
          <el-table-column prop="typeName" label="所属类型" align="center">
            
          </el-table-column>
          <el-table-column label="appID" class-name="appid" align="center" width="260">
              <template slot-scope="scope">
              <div :title="scope.row.appId" class="mini-appID">{{scope.row.appId}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="gmtCreate" label="创建时间" align="center"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                @click="editMini(scope.$index, scope.row)"
                v-if="permission('noPermission')"
              >编辑</el-button>
              <el-button
                size="mini"
                type="text"
                @click="detailMini(scope.$index, scope.row)"
                v-if="permission('noPermission')"
              >详情</el-button>
              <el-button
                size="mini"
                type="text"
                @click="deleteMini(scope.$index, scope.row)"
                v-if="permission('noPermission')"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-box">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="page.currentPage"
            :page-sizes="[10, 20, 30,40, 50]"
            :page-size="page.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="page.total"
          ></el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      miniName: "",
      comName: "",
      miniType: "",
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      miniTypeOptions: [],
      miniList: []
    };
  },
  created() {
    console.log(this.permission);
    this.getPagramList();
    this.getMiniList();
  },
  components: {},
  methods: {
    // 获取微信小程序列表
    getPagramList() {
      this.loading = true;
      let params = {
        limit: this.page.pageSize,
        start: this.page.currentPage
      };
      if (this.miniName) {
        params.miniName = this.miniName;
      }
      if (this.comName) {
        params.comName = this.comName;
      }
      if (this.miniType) {
        params.miniType = this.miniType;
      }
      this.API.getPagramList(params).then(res => {
        if (res.success) {
          this.loading = false;
          this.miniList = res.datalist;
          this.page.total = res.totalCount;
        }
      });
    },
    // 搜索微信小程序
    searchMini() {
      this.page.currentPage = 1;
      this.getPagramList();
    },
    // 获取小程序类型列表
    getMiniList() {
      this.API.getMiniBackList().then(res => {
        if (res.success) {
          this.miniTypeOptions = res.obj;
        }
      });
    },
    // 新增
    addProgram() {
      this.$router.push({ path: "/programManage/programDetail",query:{edit:'add'}});
    },
    // 导出
    excelMiniList() {
      let params = {
        start: this.page.currentPage,
        limit: this.page.pageSize
      };
      if (this.miniName) {
        params.miniName = this.miniName;
      } 
      if(this.comName) {
        params.comName = this.comName;
      }
      if (this.miniType) {
        params.miniType = this.miniType;
      }
      this.API.exportListExcel(params).then(res => {
        let url = window.URL.createObjectURL(res);
        let link = document.createElement('a')
        link.style.display = 'none'
        link.href = url;
        link.setAttribute('download', '小程序信息表.xls')
        document.body.appendChild(link)
        link.click()
      });
    },
    // 重置
    resetMini() {
      this.miniName = "";
      this.comName = "";
      this.miniType = "";
      this.page.currentPage = 1;
      this.getPagramList();
    },
    // 编辑
    editMini(index, row) {
      let id = row.id;
      this.$router.push({
        path: "/programManage/programDetail",
        query: { id: id, edit: 'edit' }
      });
    },
    // 详情
    detailMini(index, row) {
      let id = row.id;
      this.$router.push({
        path: "/programManage/programDetail",
        query: { id: id, edit: 'info' }
      });
    },
    // 删除
    deleteMini(index, row) {
      let self = this;
      let params = {
        id: row.id
      };
      this.$confirm(`确认删除${row.name}的小程序吗`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        console.log(1111);
        self.API.deletedMini(params).then(res => {
          console.log(res);
          if (res.success) {
            self.$message({
              showClose: true,
              type: "success",
              message: "删除成功!"
            });
            self.getPagramList();
          }
        });
      });
    },
    // 每页条数改变
    handleSizeChange(val) {
      this.page.pageSize = val;
      console.log(this.page.pageSize)
      this.getPagramList();
    },
    // 改变页码
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.getPagramList();
      this.$refs.multipleTable.bodyWrapper.scrollTop = 0;
    }
  }
};
</script>
<style lang='scss' scoped>
.main-body {
  margin: 20px;
  background: #fff;
  min-height: 860px;
  position: relative;
  h1 {
    border-bottom: 1px solid #eee;
    text-align: left;
    font-weight: bold;
    padding: 20px 20px 15px 20px;
  }
  .search-wrap {
    padding: 20px;
    display: flex;

    .search-box {
      display: flex;
      width: 70%;
      justify-content: space-around;
      .search-item {
        width: 300px;
        display: flex;
        align-items: center;
            justify-content: start;
        margin-right: 30px;
        .label-name {
          width:200px;
          text-align: left;
        }
      }
    }
    .search-btn {
      display: flex;
      margin-left: 5%;
      button {
        margin-left: 30px;
      }
    }
  }
  .table-box {
    padding: 20px;
    .table-option-btn {
      display: flex;
      margin-bottom: 30px;
      button {
        margin-right: 15px;
      }
    }
  }
  .pagination-box {
    margin-top: 30px;
    display: flex;
    width: 100%;
    justify-content: center;
  }
  .appid{
    .cell{
      overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    }
  }
  .mini-name{
    width: 170px;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
  .mini-appID{
    width: 235px;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
  
}

</style>
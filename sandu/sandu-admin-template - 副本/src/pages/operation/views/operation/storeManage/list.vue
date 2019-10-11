<template>
  <div class="customerFrame">
    <div class="customerTitle">
      <span>同城店铺</span>
    </div>
    <div class="searchFrame">
      <el-row class="selectSearchCond">
        <el-select v-model="customerStatus" multiple collapse-tags @change="search" clearable placeholder="店铺类型">
          <el-option
            v-for="(item,index) in shopList"
            :key="index"
            :label="item.name"
            :value="item.value"
          ></el-option>
        </el-select>

        <el-input v-model="queryParam" placeholder="店铺名称"></el-input>

        <el-select v-model="releaseStatus" @change="search" clearable placeholder="发布状态">
          <el-option
            v-for="(item,index) in releaseList"
            :key="index"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <el-select v-model="userType" clearable placeholder="用户类型" @change="search">
          <el-option
                  v-for="(item,index) in userList"
                  :key="index"
                  :label="item.name"
                  :value="item.value"
          ></el-option>
        </el-select>
        <areaSelect ref="areaSelect" @provinceCode="provinceChange" @cityCode="cityChange" @areaCode="areaChange"></areaSelect>
      </el-row>
      <el-row class="margintop">
        <el-select v-model="shopType" clearable placeholder="企业店铺或个人店铺" @change="search">
          <el-option
                  v-for="(item,index) in shopTypeList"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
          ></el-option>
        </el-select>
      </el-row>
    </div>
    <div class="btnFrame clearfix">
      <p>
        <el-button round type="primary" size="small" @click="search">搜索</el-button>
        <el-button round @click="resetForm" size="small">重置</el-button>
      </p>
    </div>
    <div class="tableList">
      <el-table
        :header-cell-style="rowStyle"
        :data="tableData"
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.8)"
        style="width: 100%"
        align="center"
      >
        <el-table-column label="序号" type="index" align="center"></el-table-column>

        <el-table-column label="店铺名称" header-align="center" align="center">
          <template slot-scope="scope">{{ scope.row.shopName }}</template>
        </el-table-column>

        <el-table-column label="logo" header-align="center" align="center">
          <template slot-scope="scope" v-if="scope.row.logoPicPath">
            <img width="120px" height="80px;" :src="imgurl+scope.row.logoPicPath">
          </template>
        </el-table-column>
        <el-table-column label="区域" header-align="center" align="center">
          <template slot-scope="scope">{{ scope.row.areaInfoName }}</template>
        </el-table-column>
        <el-table-column label="用户类型" header-align="center" align="center">
          <template slot-scope="scope">{{ scope.row.userTypeStr }}</template>
        </el-table-column>
        <el-table-column label="所属企业" header-align="center" align="center">
          <template slot-scope="scope">{{ scope.row.companyName }}</template>
        </el-table-column>

        <el-table-column label="店铺类型" header-align="center" align="center">
          <template slot-scope="scope">{{ scope.row.businessTypeName }}</template>
        </el-table-column>

        <el-table-column label="店铺评分" header-align="center" align="center">
          <!-- <template slot-scope="scope">{{ scope.row.businessTypeName }}</template> -->
          <template
            slot-scope="scope"
          >{{scope.row.enableScore == 10 ? scope.row.automateScore : scope.row.handScore }}</template>
        </el-table-column>

        <el-table-column label="动态时间" header-align="center" align="center">
          <template slot-scope="scope">{{ scope.row.gmtModified }}</template>
        </el-table-column>

        <el-table-column label="操作" header-align="center" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="toDetail(scope.row.id)">详情</el-button>
            <el-button
              type="text"
              @click="toRelease(scope.row)"
            >{{scope.row.isRelease==1 ? '取消发布' : '发布' }}</el-button>
            <!-- <el-button type="text" @click="toEdit(scope.row.id)">编辑</el-button> -->
            <el-button
              type="text"
              @click="toDel(scope.row.id)"
              v-if="permission('operation:storedelete:delete')"
            >删除</el-button>
            <el-button
              type="text"
              @click="toTop(scope.row.id,scope.row.recommendedTime > 0?0:1)"
              v-if="permission('operation:storeManage:top')"
            >{{ scope.row.recommendedTime == 0 ? '置顶':'取消置顶'}}</el-button>
            <el-button
              type="text"
              @click="toReflresh(scope.row.id,scope.row.recommendedTime > 0?0:1)"
            >刷新</el-button>
            <el-button
              type="text"
              @click="dialogFormShow(scope.row,scope.row.id)"
            >设置评分</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <div class="pageFrame">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="query.page"
          :page-sizes="[5,10, 50, 100, 200,500]"
          :page-size="query.limit"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        ></el-pagination>
      </div>

      <!-- 评分弹窗组件 -->
      <el-dialog :visible.sync="dialogFormVisible" width="620px">
        <div slot="title" class="title">设置评分</div>
        <div class="diabox">
          <div class="box">
            <p class="text">请选择店铺评分：</p>
            <div class="radioButton">
              <el-radio v-model="radio" label="10">
                自动评分
                <span class="gradeExplain">
                  （当前店铺评分为
                  <i>{{grade}}</i>）
                </span>
              </el-radio>
              <el-radio class="radioMt" v-model="radio" label="20">
                手动修改
                <i class="gradeExplain">（分值1.0-5.0评分，支持小数点后两位）</i>
              </el-radio>
              <div class="gradeBox radioMt" v-if="radio==20">
                修改店铺评分为：
                <el-input type="number" maxlength="5" @change="check_price" v-model="number" style="width:80px"></el-input>
              </div>
              <p class="remind" v-if="number !='' && remind && radio==20">请输入1.0~5.0之间的评分,仅支持小数点后两位</p>
            </div>
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogFormConfirm()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "coustomer",
  data() {
    return {
      imgurl: process.env.sourceBaseUrl,
      total: 0, // 总页数
      mobile: "",
      customerStatus: [], // 客户状态
      queryParam: "",
      shopList: [
        // {value:0, name:'未分配'},
        // {value: 1, name: '已分配'},
        // {value: 2, name: '跟进中'},
        // {value: 3, name: '跟进完成'},
        // {value: 4, name: '无效'}
      ],
      loading: true,
      streetShow: true,
      provide: [],
      provideSelect: [],
      citySelects: [],
      areaSelect: [],
      originSelect: [],
      dialogFormVisible: false,
      radio: "10",
      number: "",
      remind: false,
      grade: "",
      radioId:'',
      radioHandScore:'',
      //区域选择 结束------------
      tableData: [],
      listTableData: [],
      query: {
        page: 1,
        limit: 10
      },
      releaseStatus: "",
      releaseList: [
        { label: "已发布", value: 1 },
        { label: "未发布", value: 0 }
      ],
      userList:[],
      userType:'',
      shopType:'',
      cityParams:{
          provinceCode:'',
          cityCode :'',
          areaCode :''
      },
      shopTypeList:[
          { label:'企业店铺',value:1},
          { label:'个人店铺',value:2}
      ]
    };
  },
  created() {
    this.getListData();
    this.categoryDroplist();
    this.getUserType();
  },
  methods: {
    rowStyle({ row, rowIndex }) {
      if (rowIndex === 0) {
        return {
          height: "60px!important",
          background: "#f5f5f5",
          color: "#666"
        };
      }
    },
    search(){
        this.query.page=1;
        this.getListData();
    },
    getUserType(){
        this.API.userTypeList({ att1:0 }).then(res=>{
            this.userList=res.list;
        })
    },
    categoryDroplist() {
      this.API.getShopList().then(res => {
        this.shopList = res.data;
      });
    },
    getOriginCode(code) {},
    // 区域选择 结束---------- streetCodeName
    // 分类列表
    getListData() {
      let queryCondition = Object.assign({}, this.query, {
        businessType: this.customerStatus && this.customerStatus.join(','),
        queryParam: this.queryParam,
          isRelease: this.releaseStatus,
          provinceCode: this.cityParams.provinceCode,
          cityCode: this.cityParams.cityCode,
          areaCode : this.cityParams.areaCode,
          userType: this.userType,
          shopTypes:this.shopType
      });
      // 删除空参数
      for (let key in queryCondition) {
        if (
          queryCondition[key] === "" ||
          queryCondition[key] === undefined ||
          queryCondition[key] === null
        ) {
          delete queryCondition[key];
        }
      }
      setTimeout(() => {
        this.loading = false;

        this.API.querycompanyshop(queryCondition).then(res => {
          this.tableData = res.list;
          this.total = res.total;
        });
      }, 1000);
    },
    // 分页模块
    handleSizeChange: function(size) {
      this.query.limit = size;
      this.getListData();
    },
    handleCurrentChange: function(currentPage) {
      this.query.page = currentPage;
      this.loading = true;
      this.getListData();
    },

    resetForm() {
      this.cityParams = {};
      this.mobile = "";
      this.customerStatus = "";
      this.queryParam = "";
      this.releaseStatus = "";
      this.userType = "";
      this.shopType = "";
      this.query.page=1;
      this.$refs.areaSelect.reset();
      this.getListData();
    },
    // 详情
    toDetail(objectId) {
      this.$router.push(`/storeManage/detail/${objectId}`);
    },
    toEdit(objectId) {
      this.$router.push(`/storeManage/edit/${objectId}`);
    },
    toReflresh(objectId, topId) {
      this.API.refreshStoreById({
        companyshopId: objectId,
        topId: topId
      }).then(res => {
        if (res.code === 200) {
          this.getListData();
          this.$message.success("刷新成功！");
        }
      });
    },
    toDel(objectId) {
      this.$confirm("此操作将删除内容，是否继续", "提示", {
        confirmButtonText: "确定删除",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.API.deletecompanyshopById({
            companyshopId: objectId
          }).then(res => {
            if (res.code === 200) {
              this.getListData();
              this.$message.success("删除成功！");
            }
          });
        })
        .catch(() => {
          this.$message({ type: "info", message: "已取消删除" });
        });
    },
    toTop(objectId, topId) {
      this.API.companyshopToTopById({
        companyshopId: objectId,
        topId: topId
      }).then(res => {
        if (res.code === 200) {
          this.getListData();
          this.$message.success("操作成功！");
        }
      });
    },
    dialogFormShow(row, rowId) {
      //记录索引
      this.listIndex = rowId;
      //记录数据
      this.grade = row.automateScore;
      //显示弹窗
      this.dialogFormVisible = true;
      if((this.radioId == this.listIndex) && this.radio == 20){
        this.radio = '20'
        this.number = this.radioHandScore
      }else{
        this.radio = '10',
        this.number = ''
      }
    },
    dialogFormConfirm() {
      this.radio == 10 ? (this.number = "") : this.grade;
      if (this.radio == 10 ){
        this.getModifyStoreScore()
      } else if (this.radio == 20 && this.number && this.number >= 1 && this.number <= 5 && this.number.length<5){
        this.getModifyStoreScore()
      } else{
        this.$message.error('请输入有效数值！');
        return
      }
    },
    getModifyStoreScore(){
        this.API.modifyStoreScore({
            enableScore: this.radio,
            handScore: this.number,
            shopId: this.listIndex
        }).then(res => {
            this.dialogFormVisible = false;
            this.remind = false; 
            this.radioHandScore = this.number;
            this.radioId = this.listIndex;
            this.getListData();
        }).catch(res => {
            this.$message.error(res.message);
        });
    },
    // 输入框做限制
    check_price: function() {
      var number = "" + this.number;
      var regex = /(^[1-4]([\.][\d]{1,2})?$|^[5]([\.][0]{1,2}$)?$)/;
      if (regex.test(number)) {
        if (number.length == 1) {
          this.number += ".00";
        } else if (number.length == 3) {
          this.number += "0";
        }
        this.remind = false;
      } else {
        this.remind = true
      }
    },
    toRelease(item) {
      this.API.setRelease({
        isRelease: item.isRelease == 0 ? 1 : 0,
        shopId: item.id
      }).then(res => {
        if (res.success) {
          this.$message.success(res.message);
          this.getListData();
        } else {
          this.$message.error(res.message);
        }
      });
    },
    provinceChange(val){
        this.query.page=1;
        this.cityParams.provinceCode=val;
        this.cityParams.cityCode='';
        this.cityParams.areaCode=''
        this.getListData();
    },
    cityChange(val){
        this.query.page=1;
        this.cityParams.cityCode=val;
        this.cityParams.areaCode=''
        this.getListData();
    },
    areaChange(val){
        this.query.page=1;
        this.cityParams.areaCode=val;
        this.getListData();
    },
  } //end methods
};
</script>

<style lang="scss" scoped>
/*@import "../../assets/css/mixin.scss";*/
.customerFrame {
  margin: 20px;
  .tableList {
    .title {
      padding-bottom: 20px;
      border-bottom: 1px solid #e8e8e8;
    }
  }
}
.changeColor {
  color: #ff2323;
}

.customerTitle {
  text-align: left;
  background: #fff;
  height: 58px;
  line-height: 58px;
  font-size: 18px;
  color: #333;
  border-bottom: solid 1px #ddd;
  span {
    display: inline-block;
    padding-left: 20px;
  }
}

.searchFrame {
  background: #fff;
  padding: 20px;
  text-align: left;
}

.areaStyle {
  margin-top: 20px;
  .el-select {
    width: 216px;
    margin-right: 10px;
  }
}

.selectSearchCond {
  display: flex;
  .el-select,
  .el-input {
    width: 216px;
    margin-right: 10px;
  }
}
.margintop{
  margin-top: 20px;
}

.btnFrame {
  padding: 10px 0;
  text-align: center;
  border-top: solid 1px #ddd;
  background: #fff;
  font-size: 18px;
  color: #333;
  p {
    text-align: center;
    padding-bottom: 8px;
    padding-top: 8px;
  }
}

.tableList {
  margin-top: 20px;
  padding: 20px;
  background: #fff;
  text-align: left;
  .diabox {
    // padding-bottom: 40px;
    .box {
      .text {
        display: inline-block;
        width: 130px;
        text-align: right;
        font-size: 14px;
        color: #333;
        vertical-align: top;
      }
      .radioButton {
        display: inline-block;
        width: 400px;
        .radioMt {
          margin-top: 24px;
        }
        .gradeBox {
          margin-left: 24px;
        }
        .gradeExplain {
          color: #999999;
        }
      }
      .remind {
        display: inline-block;
        color: #ff2323;
        margin: 20px 0 0 24px;
      }
    }
  }
  .dialog-footer {
    padding-top: 10px;
    border-top: 1px solid #e8e8e8
  }
}

.pageFrame {
  background: #fff;
  height: 110px;
  padding-top: 40px;
  box-sizing: border-box;
  text-align: center;
}
</style>

<template>
  <div class="plan-box">
    <!-- 头部筛选框 -->
    <div class="header">
      <!-- 切换空间 -->
      <div class="title nav" style="border-bottom: 1px solid #f5f5f5;">
        <span :class="planType==1?'active':''" @click="switchTab(1)">单空间</span>
        <el-divider direction="vertical"></el-divider>
        <span :class="planType==2?'active':''" @click="switchTab(2)">全屋</span>
      </div>
      <div class="filtrate-box">
        <div class="filtrate-item">
          <span>方案ID：</span>
          <el-input v-model="parameter.planId" maxlength="20" size="small" placeholder="请输入方案ID"></el-input>
        </div>
        <div class="filtrate-item">
          <span>方案名称：</span>
          <el-input v-model="parameter.planName" maxlength="50" size="small" placeholder="请输入方案名称"></el-input>
        </div>
        <div class="filtrate-item">
          <span>所属企业：</span>
          <el-input
            v-model="parameter.companyName"
            maxlength="50"
            size="small"
            placeholder="请输入所属企业"
          ></el-input>
        </div>
        <div class="filtrate-item">
          <span>店铺信息：</span>
          <el-input v-model="parameter.shopName" maxlength="50" size="small" placeholder="请输入店铺信息"></el-input>
        </div>
        <div class="filtrate-item" v-if="planType==1">
          <span>空间类型：</span>
          <el-select v-model="parameter.spaceTypeId" size="small" placeholder="请选择" clearable>
            <el-option
              v-for="(item, index) in spaceTypeList"
              :key="index"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </div>
        <div class="filtrate-item">
          <span>风格：</span>
          <el-select v-model="parameter.planGroupStyleId" size="small" placeholder="请选择" clearable>
            <el-option
              v-for="(item, index) in styleList"
              :key="index"
              :label="item.styleName"
              :value="item.groupId"
            ></el-option>
          </el-select>
        </div>
        <div class="filtrate-item">
          <span>是否推荐：</span>
          <el-select v-model="parameter.showInSXWFlag" size="small" placeholder="请选择" clearable>
            <el-option
              v-for="item in showInSXWFlagList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </div>
        <div class="filtrate-item">
          <span>首页展示：</span>
          <el-select
            v-model="parameter.showInSXWIndexFlag"
            size="small"
            placeholder="请选择"
            clearable
          >
            <el-option
              v-for="item in showDataList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </div>
        <!-- #409EFF -->
        <div class="filtrate-item">
          <div class="btn" @click="filtrate">筛选</div>
          <div class="btn" @click="reset">重置</div>
        </div>
      </div>
    </div>
    <!-- 数据表格 -->
    <div class="table">
      <el-table
        v-loading="loading"
        empty-text="暂无数据"
        :data="tableData"
        :header-cell-style="headerStyle"
        style="width: 100%"
      >
        <el-table-column prop="planId" label="ID" width="80"></el-table-column>
        <el-table-column prop="picPath" label="缩略图" width="70">
          <template slot-scope="scope">
            <img :src="sourceBaseUrl + scope.row.picPath" alt class="img">
          </template>
        </el-table-column>
        <el-table-column prop="planName" label="名称">
          <template slot-scope="scope">
            <span>{{scope.row.planName}}</span>
            <i class="el-icon-edit-outline" @click="operation(scope, '方案名称修改')"></i>
          </template>
        </el-table-column>
        <el-table-column prop="spaceTypeName" label="空间类型" width="80">
          <template slot-scope="scope">
            <span>{{planType==1?scope.row.spaceTypeName:"全屋"}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="designStyleName" label="风格" width="120"></el-table-column>
        <el-table-column prop="completeDate" label="修改时间" width="150"></el-table-column>
        <el-table-column prop="origin" label="方案来源" width="80">
          <template slot-scope="scope">
            <span v-if="planType==1">{{scope.row.origin == 'deliver'?'企业交付': scope.row.origin == 'diy'? '内部制作' : '分享方案'}}</span>
            <span v-else>{{scope.row.origin}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="companyName" label="所属企业"></el-table-column>
        <el-table-column prop="shopName" label="店铺信息"></el-table-column>
        <el-table-column prop="showInSXWIndexFlag" label="首页展示" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.showInSXWIndexFlag"
              @change="operation(scope, '首页展示')"
              active-color="#409EFF"
              inactive-color="#999"
            ></el-switch>
            <div class="switchMark" v-show="planType==2 && !scope.row.coverPicStatus" @click="setCover(scope.row,1)"></div>
          </template>
        </el-table-column>
        <el-table-column label="浏览/点赞/收藏" width="120">
          <template slot-scope="scope">
            <span>
              {{tableData[scope.$index].viewCount?tableData[scope.$index].viewCount:0}} /
              {{tableData[scope.$index].likeCount?tableData[scope.$index].likeCount:0}} /
              {{tableData[scope.$index].collectionCount?tableData[scope.$index].collectionCount:0}}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="sortInSXWIndex" label="排序" width="80">
          <template slot-scope="scope">
            <span>{{scope.row.sortInSXWIndex}}</span>
            <i class="el-icon-edit-outline" @click="operation(scope, '排序')"></i>
          </template>
        </el-table-column>
        <el-table-column prop="showInSXWFlag" label="是否推荐" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.showInSXWFlag"
              @change="operation(scope, '是否推荐')"
              active-color="#409EFF"
              inactive-color="#999"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="110">
          <template slot-scope="scope">
            <div class="operation-btn" @click="operation(scope, '店铺信息修改')">店铺信息修改</div>
            <div class="operation-btn" v-if="planType==2" @click="setCover(scope.row)">设置封面图</div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="parameter.page"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="10"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>
    <!-- 弹窗 -->
    <el-dialog :title="dialogObj.title" :visible.sync="dialogObj.dialogVisible" width="30%">
      <div class="plan-message" v-if="dialogObj.title == '店铺信息修改'">
        <span>店铺信息：</span>
        <el-select v-model="editParameter.shopIn720Page" size="small" placeholder="请选择">
          <el-option
            v-for="(item, index) in shopMessageList"
            :key="index"
            :label="item.shopName"
            :value="item.id"
          ></el-option>
        </el-select>
      </div>
      <div class="plan-message" v-if="dialogObj.title == '方案名称修改'">
        <span>方案名称：</span>
        <el-input v-model="editParameter.planName" maxlength="50" size="small" placeholder="请输入内容"></el-input>
      </div>
      <div class="plan-message" v-if="dialogObj.title == '排序'">
        <span>排序：</span>
        <el-input
          v-model="editParameter.sortInSXWIndex"
          maxlength="50"
          size="small"
          placeholder="请输入内容"
        ></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogObj.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </span>
    </el-dialog>
    <!--设置封面图-->
    <el-dialog
            class="setCoverDialog"
            title="设置封面图"
            center
            :visible.sync="setCoverDialog"
            width="576px"
            :before-close="handleClose">
      <div class="content">
          <ul>
            <li v-for="(item,index) in coverList" :key="index">
              <span>{{item.picTypeInfo}}：</span>
              <img :src="BASE_URL.sourceBaseUrl+item.picPath" alt="">
              <em @click="setCoverId(item,index)" :class="{'active':item.checkStatus}">{{ item.checkStatus ? '取消封面图' : '设置封面图' }}</em>
            </li>
          </ul>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">保存</el-button>
        <el-button @click="setCoverDialog = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {map} from 'lodash'
export default {
  data() {
    return {
      planType: 1,
      setCoverDialog: false,
      loading: true,
      headerStyle: { backgroundColor: "#f5f5f5" },
      sourceBaseUrl: "",
      spaceTypeList: [],
      styleList: [],
      showDataList: [{ label: "是", value: 1 }, { label: "否", value: 0 }],
      showInSXWFlagList: [{ label: "是", value: 1 }, { label: "否", value: 0 }],
      shopMessageList: [],
      parameter: {
        managerSXW: true,
        planId: undefined, // 方案ID
        planName: undefined, // 方案名称
        companyId: undefined, // 公司id
        companyName: undefined, // 企业名称
        shopName: undefined, //店铺信息
        spaceTypeId: undefined, // 对应空间类型id
        planGroupStyleId: undefined, // 方案风格ID
        showInSXWFlag: undefined, //是否推荐，1是，0否，‘’全部
        showInSXWIndexFlag: undefined, //是否推荐，1是，0否，‘’全部
        page: 1, // 页数
        limit: 10 // 单页加载数量
      },
      tableData: [],
      total: 0,
      dialogObj: {
        // 弹框内信息
        dialogVisible: false,
        title: ""
      },
      editParameter: {
        planId: "",
        planName: "",
        showInSXWIndexFlag: false, // 是否展示首页
        showInSXWFlag: false, //是否推荐
        sortInSXWIndex: "", //随选网首页排序
        shopIn720Page: "" //店铺id
      },
       coverList:[],
       coverIdList:[],
       currentItem:'',
       currentIdList:[],
        isAuto:false,  //是否自动开启
    };
  },
  created() {
    this.init();
  },
  methods: {
    switchTab(type) {
      this.planType = type;
      this.parameter = {
        managerSXW: true,
        planId: undefined, // 方案ID
        planName: undefined, // 方案名称
        companyId: undefined, // 公司id
        companyName: undefined, // 企业名称
        shopName: undefined, //店铺信息
        spaceTypeId: undefined, // 对应空间类型id
        planGroupStyleId: undefined, // 方案风格ID
        showInSXWFlag: undefined, //是否推荐，1是，0否，‘’全部
        showInSXWIndexFlag: undefined, //是否推荐，1是，0否，‘’全部
        page: 1, // 页数
        limit: 10 // 单页加载数量
      };
      this.init();
    },
    setCoverId(item,index){
       !item.checkStatus ? this.currentIdList.unshift(item) : this.currentIdList=this.currentIdList.filter((res=>{ return res.picId!=item.picId }));
       item.checkStatus=!item.checkStatus;
       this.$set(this.coverList,index,this.coverList[index]);
    },
    setCover(item,isAuto){
       this.coverList=[];
       this.currentItem=item;
       this.isAuto=isAuto;
       this.API.getCoverPicInfoList({fullHousePlanId:item.planId}).then(res=>{
           if(res.success){
               this.coverList=res.list;
               this.currentIdList=res.list.filter((dto=>{ return dto.checkStatus }));
               this.setCoverDialog=true;
           }
       })
    },
    save(){
        let coverIdList=map(this.currentIdList,'picId');
        if(coverIdList.length!=3){
            this.$message.error('只能设置3张封面图');
            return
        }
        this.API.modifyFullHousePlanCoverPics({
            fullHousePlanId:this.currentItem.planId,
            coverPicIds :coverIdList.join(',')
        }).then(res=>{
            if(res.success){
                this.$message.success('封面图设置成功');
                this.setCoverDialog=false;
                if(this.isAuto){
                    this.editParameter.showInSXWIndexFlag=true;
                    for (let key in this.editParameter){
                        this.editParameter[key]=this.currentItem[key];
                    }
                    this.editParameter.showInSXWIndexFlag=true;
                    this.API.StorePlanEditByFullHouse(this.editParameter)
                }
                this.getStoreplan();
            }
        })
    },
    /**初始化数据 */
    init() {
      this.sourceBaseUrl = this.BASE_URL.sourceBaseUrl;
      this.getTtpe();
      this.getStoreplan();
    },
    handleClose(){
        this.setCoverDialog=false
    },
    operation(item, type) {
      console.log(item);
      this.editParameter = {
        planId: item.row.planId,
        planName: item.row.planName,
        showInSXWIndexFlag:
          this.planType == 1
            ? item.row.showInSXWIndexFlag
            : item.row.showInSXWIndexFlag == 1,
        showInSXWFlag:
          this.planType == 1
            ? item.row.showInSXWFlag
            : item.row.showInSXWFlag == 1,
        sortInSXWIndex: item.row.sortInSXWIndex,
        shopIn720Page: item.row.shopIn720Page,
        companyId: item.row.companyId
      };
      if (type == "店铺信息修改") {
        this.shopMessage(item.row.planId);
      }
      if (type == "是否推荐" || type == "首页展示") {  //0 没有设置封面图
          this.confirm();
      } else {
        this.dialogObj = { dialogVisible: true, title: type };
      }
    },
    /**获取当前店铺信息 */
    shopMessage(planId) {
      this.API.shopMessage({ planId: planId, planType: this.planType }).then(
        res => {
          if (res.list) {
            this.shopMessageList = res.list;
          }
        }
      );
    },
    /**确定编辑 */
    confirm() {
      if (this.dialogObj.dialogVisible && this.dialogObj.title == "排序") {
        let regu = /^[1-9]\d*$/;
        if (!regu.test(this.editParameter.sortInSXWIndex)) {
          this.$message({
            message: "请输入正整数",
            type: "error"
          });
          return;
        }
      }
      (this.planType == 1
        ? this.API.StorePlanEdit(this.editParameter)
        : this.API.StorePlanEditByFullHouse(this.editParameter)
      ).then(res => {
        if (res.success) {
          this.dialogObj = { dialogVisible: false, title: "" };
        }
        this.getStoreplan();
        this.$message({
          message: res.success ? "保存成功" : res.message,
          type: res.success ? "success" : "error"
        });
      });
    },
    /**重置 */
    reset() {
      this.parameter.planId = undefined;
      this.parameter.planName = undefined;
      this.parameter.companyName = undefined;
      this.parameter.shopName = undefined;
      this.parameter.spaceTypeId = undefined;
      this.parameter.planGroupStyleId = undefined;
      this.parameter.showInSXWFlag = undefined;
      this.parameter.showInSXWIndexFlag = undefined;
      this.getStoreplan();
    },
    /**每页展示数量 */
    handleSizeChange(val) {
      this.parameter.limit = val;
      this.getStoreplan();
    },
    /**分页跳转 */
    handleCurrentChange(val) {
      this.parameter.page = val;
      this.getStoreplan();
    },
    /**获取空间类型与风格数据 */
    getTtpe() {
      //空间类型
      this.API.getHouse().then(res => {
        if (res.data) this.spaceTypeList = res.data;
      });
      //风格
      this.API.getStyle().then(res => {
        if (res.data) this.styleList = res.data;
      });
    },
    filtrate() {
      this.parameter.page = 1;
      this.getStoreplan();
    },
    /**获取列表数据 */
    getStoreplan() {
      this.loading = true;
      let obj = this.parameter;
      obj.queryName = obj.planName;
      obj.showInSXWFlag = obj.showInSXWFlag == 2 ? "" : obj.showInSXWFlag;
      obj.showInSXWIndexFlag =
        obj.showInSXWIndexFlag == 2 ? "" : obj.showInSXWIndexFlag;
      obj.spaceTypeId = obj.spaceTypeId == 0 ? "" : obj.spaceTypeId;
      obj.planGroupStyleId =
        obj.planGroupStyleId == 0 ? "" : obj.planGroupStyleId;
      (this.planType == 1
        ? this.API.getStoreplan(obj)
        : this.API.getStoreplanByfullHouse(obj)
      )
        .then(res => {
          if (this.planType == 1) {
            if (res && res.data && res.data.list) {
              res.data.list.forEach(item => {
                item.showInSXWIndexFlag =
                  item.showInSXWIndexFlag == 1 ? true : false;
                item.showInSXWFlag = item.showInSXWFlag == 1 ? true : false;
              });
              this.tableData = res.data.list;
              this.total = res.data.total;
            } else {
              this.tableData = [];
              this.total = 0;
            }
          } else {
            if (res && res.data) {
              res.data.forEach(item => {
                item.showInSXWIndexFlag =
                  item.showInSXWIndexFlag == 1 ? true : false;
                item.showInSXWFlag = item.showInSXWFlag == 1 ? true : false;
              });
              this.tableData = res.data;
              this.total = res.total;
            } else {
              this.tableData = [];
              this.total = 0;
            }
          }
        })
        .finally(() => {
          this.loading = false;
        });
    }
  }
};
</script>

<style scoped lang="scss">
.plan-box {
  padding: 20px;
  .header {
    background-color: #fff;
    border-radius: 5px;
    .title {
      padding: 20px;
      text-align: left;
      border-bottom: 1px solid #f5f5f5;
    }
    .nav {
      span {
        font-weight: 700;
        cursor: pointer;
      }
      .active {
        color: #409eff;
      }
    }
    .filtrate-box {
      padding: 20px;
      font-size: 14px;
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
      .filtrate-item {
        height: 30px;
        line-height: 30px;
        margin: 0 20px 20px 0;
        display: flex;
        justify-content: space-between;
        span {
          width: 80px;
          text-align: right;
        }
        .el-input,
        .el-select {
          width: 150px;
        }
        .btn {
          width: 60px;
          height: 30px;
          line-height: 30px;
          border-radius: 5px;
          margin: 0 20px 20px 0;
          text-align: center;
          color: #fff;
          background-color: #409eff;
          cursor: pointer;
        }
      }
    }
  }
  .table {
    margin-top: 20px;
    border-radius: 5px;
    padding: 20px;
    background-color: #fff;
    .img {
      display: block;
      margin: 0 auto;
      width: 40px;
      height: 40px;
    }
    .operation-btn {
      color: #409eff;
      cursor: pointer;
    }
  }
  .el-pagination {
    background-color: #fff;
    padding: 20px;
    text-align: right;
  }
  .plan-message {
    display: flex;
    justify-content: center;
    height: 30px;
    span {
      width: 100px;
      line-height: 30px;
    }
  }
  .switchMark{
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
  }
  .setCoverDialog{
    .content{
       ul{
         width: 100%;
         height: 600px;
         overflow-y: scroll;
         li{
           margin-bottom: 20px;
           span{
             display: inline-block;
             width: 180px;
             font-size: 16px;
           }
           img{
             display: inline-block;
             width: 180px;
             height: 120px;
             vertical-align: middle;
             margin: 0 10px;
           }
           em{
             color: #409eff;
             cursor: pointer;
             &:active{
               opacity: .6;
             }
           }
           .active{
             color: #999;
           }
         }
       }
    }
  }
}
</style>
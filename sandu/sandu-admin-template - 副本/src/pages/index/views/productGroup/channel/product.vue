<template>
  <transition name="fade">
    <section class="products">
      <!--工具条-->
      <el-row>
        <el-form :inline="true">
          <div class="grid-content bg-purple-dark minWidth">
            <el-col :span="3">
              <el-select ref="productCates"
                         placeholder="请选择产品组合类型"
                         clearable
                         @change="getProductGroupList"
                         v-model="productGroupType">
                <el-option
                  v-for="(item,index) in productGroupTypeDrop"
                  :key="index"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <el-select v-model="allotValue" clearable @change="getProductGroupList" placeholder="请选择上架情况"
                         >
                <el-option
                  v-for="(item,index) in shelfStateDrop"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <span class="labelColor"></span>
              <el-select v-model="isOpen" clearable @change="getProductGroupList" placeholder="请选择公开状态">
                <el-option
                  v-for="item in openDisclosed"
                  :key="item.name"
                  :label="item.name"
                  :value="item.isON">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <el-input v-model="productInputGroupName" placeholder="请输入组合名称"></el-input>
            </el-col>
            <el-col :span="3">
              <el-input v-model="productInputGroupCode" placeholder="请输入组合编号"></el-input>
            </el-col>

            <el-col :span="1">
              <el-button class="searchBtn marRight" round @click="getProductGroupList">搜索</el-button>
            </el-col>
            <!--:disabled="productInputGroupName == '' && productCode == '' && allotValue == '' && selectedType == ''"-->
            <el-col :span="1">
              <el-button class="searchBtn" @click="resetInput" round>重置</el-button>
            </el-col>
          </div>
        </el-form>
      </el-row>
      <div class="splitLine2"></div>
      <div class="tableFilter">
        <ul>
          <li @click="batchPush" v-if="isPer('biz:product:group:exhibition')">
            <el-button type="text" class="upload2"> 批量上架</el-button>
          </li>
          <li @click="batchPull" v-if="isPer('biz:product:group:exhibition')">
            <el-button type="text" class="download">批量下架</el-button>
          </li>
          <li>
            <el-button size="small" type="text" @click="refreshList" class="refresh">刷新</el-button>
          </li>
        </ul>
      </div>

      <el-table
        :data="productGroupList"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.8)"
        @selection-change="handleSelectionChange"
        tooltip-effect="dark"
        :default-sort="{prop: 'allotTime', order: 'descending'}"
      >
        <el-table-column
          type="selection"
          label="全选"
          width="55" title="全选">
        </el-table-column>
        <el-table-column prop="groupCode" label="产品组合编号" sortable>
        </el-table-column>
        <el-table-column prop="picPath" label="产品组合缩略图">
          <template slot-scope="scope">
            <div v-if="scope.row.picPath">
              <img style="cursor:pointer;" @click="ImgView(scope.row.picPath)" :src="scope.row.picPath" width="90" height="90">
            </div>
            <div class="nonImg" v-else> 暂无图片</div>
          </template>
        </el-table-column>
        <el-table-column prop="groupName" label="产品组合名称" align="center">
        </el-table-column>
        <el-table-column prop="brandName" label="品牌" align="center">
        </el-table-column>
        <el-table-column prop="compositeTypeName" label="产品组合类型" align="center">
          <template slot-scope="scope">
            <p v-if="scope.row.compositeTypeName">{{scope.row.compositeTypeName.replace(/\//g, '>')}}</p>
          </template>
        </el-table-column>

        <el-table-column prop="putAwayStatus2b" label="上架状态" align="center" width="150">
          <template slot-scope="scope">
            {{scope.row.putStatus2b == 0 ? '未上架' : '已上架'}}
          </template>
        </el-table-column>
        <el-table-column prop="allotTime" label="录入时间" width="110" align="center" sortable>
          <template slot-scope="scope">
            <p style="width: 80px; text-align: center" v-if="scope.row.allotTime">{{scope.row.allotTime}}</p>
            <p v-else>———</p>
          </template>
        </el-table-column>
        <el-table-column prop="secrecy" label="公开状态" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.secrecy == 0 ? scope.row.statusSwitchTxt = '公开' : scope.row.statusSwitchTxt = '未公开'}}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" class="btnGroup" align="center" width="220">
          <template slot-scope="scope">
            <div style="padding-right: 10px">
              <span v-if="scope.row.putStatus2b == 0">
                <el-button size="small" class="operatonBtn"
                           v-if="isPer('biz:product:group:exhibition')"
                           @click="SingleBush(scope.row)">上架</el-button>
              </span>
              <span v-else>
                <el-button size="small" class="operatonBtn"
                           v-if="isPer('biz:product:group:exhibition')"
                           @click="SingleBush2(scope.row)">下架</el-button>
              </span>
              <el-button size="small" class="operatonBtn"
                         v-if="isPer('biz:product:group:edit')"
                         @click="goToEdit(scope.row)">编辑
              </el-button>
              <el-button size="small" class="operatonBtn"
                         v-if="isPer('biz:product:group:view')"
                         @click="showDetailProducts(scope.row)">详情
              </el-button>
              <!--<el-button type="small" v-if="isPer('biz:product:group:del')" size="small" @click="handleDel(scope.row)">删除</el-button>-->
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <div class="pageFrame">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="query.page"
          :page-sizes="[10, 50, 100, 200,500]"
          :page-size="query.limit"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
      <!--分配界面-->
      <div v-show="dialogAllot" class="dialogDiv">
        <div class="checkboxFrom">
          <h1>选择分配对象</h1>
          <div>
            <el-checkbox-group v-model="checkAllot">
              <el-checkbox label="2b" value="2b">渠道管理</el-checkbox>
              <el-checkbox label="2c" value="2c">线上管理</el-checkbox>
            </el-checkbox-group>
          </div>
          <p>
            <el-button type="primary" @click="sureAllot" style="width: 80px" class="confirmButtonClass" size="medium">
              确定
            </el-button>
            <el-button @click="cancelAllot" style="width: 80px" class="cancelButtonClass" size="medium">取消</el-button>
          </p>
        </div>
      </div>

      <el-dialog
        title="系统提示"
        :visible.sync="dialogPushPull"
        width="20%"
        center>
        <div style="text-align: center">确定批量上架选中产品组合？</div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogPushPull = false" style="width: 80px" class="cancelButtonClass" size="medium">取
            消
          </el-button>
          <el-button type="primary" @click="sureBatchPush" style="width: 80px" class="confirmButtonClass"
                     size="medium">确 定
          </el-button>
        </div>
      </el-dialog>

      <el-dialog
        title="系统提示"
        :visible.sync="dialogPull"
        width="20%"
        center>
        <div style="text-align: center">确定批量下架选中产品组合？</div>
        <span slot="footer" class="dialog-footer">
                <el-button @click="dialogPull = false" style="width: 80px" class="cancelButtonClass"
                           size="medium">取 消</el-button>
                <el-button type="primary" @click="sureBatchPull" style="width: 80px" class="confirmButtonClass"
                           size="medium">确 定</el-button>
              </span>
      </el-dialog>
    <img-pre-view ref="sonParams"></img-pre-view>
    </section>
  </transition>
</template>

<script>
  import upload from '../../../views/public/upload/upload'
  import qs from 'qs'
import ImgPreView from '@/components/Img-preview'
  export default {
    data () {
      return {
        dialogPull: false,
        dialogPushPull: false,
        productGroupType: '', // 产品组合类型
        allotStatus: '', //  allotStatus
        loading: true,
        productInputGroupCode: '',
        productInputGroupName: '',
        value1: true,
        inactiveValue: '',
        isOpen: '',
        productCates: '',
        total: 0,
        query: {
          page: 1,
          limit: 10,
          companyId: sessionStorage.getItem('companyID')
        },
        selectedIds: [],
        selectedType: '',
        openDisclosed: [
          {name: '公开', isON: 0},
          {name: '未公开', isON: 1}
        ],
        rowid: [], // 单条数据分配时，获取id
        Switch: [],
        currentPage: 0, // 当前页
        pageSize: 5, // 页大小
        productGroupTypeDrop: [], // 获取产品组合类型下拉框
        props: {
          label: 'name',
          value: 'code',
          children: 'children'
        },
        allot: [
          {label: '渠道管理', value: 'channel'},
          {label: '线上管理', value: 'online'},
          {label: '未分配', value: 'nonAllot'}
        ],
        shelfStateDrop: [
          {label: '已上架', value: 1},
          {label: '未上架', value: 0}
        ],
        allotValue: '',
        checkAllot: [],
        dialogAllot: false,
        showAddProducts: false,
        showEditProducts: false,
        productGroupList: [],
        multipleSelection: [],
        dialogVisible: false,
        statusSwitch: '',
        submitBtnGreyBg: false // 提交按钮为灰色
      }
    },
    created () {
      this.getProductGroupList()
      this.ProductGroupType()
    },
    mounted () {
      this.getProductGroupList()
    },
    methods: {
    //图片预览事件
      ImgView(ImgURL){
       this.$refs.sonParams.sonFun({ImgURL:ImgURL,ImgShow:true});
     }, 
      isPer (per) {
        let permiss = qs.parse(sessionStorage.getItem('loginUser'))
        return this.isMainPer(per, permiss)
      },
      refreshList () {
        this.$nextTick(function () {
          this.loading = true
          this.getProductGroupList()
        })
      },
      selectedCategoryDrop (value) {
        this.selectedType = value // value[value.length - 1]
      },
      batchPull () { // 批量下架
        if (this.multipleSelection.length > 0) {
          this.dialogPull = true
        } else {
          this.$message.error('请选择一条或多条数据再做批量下架')
        }
      },
      batchPush () {
        if (this.multipleSelection.length > 0) {
          this.dialogPushPull = true
        } else {
          this.$message.error('请选择一条或多条数据再做批量上架')
        }
      },
      SingleBush (row) {
        if (row.id) {
          this.$confirm('确定要上架产品组合？', '提示', {
            confirmButtonText: '确定',
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            type: 'warning',
            center: true
          }).then(() => {
            this.API.ProductGroupPutawayChannel({
              groupIds: [row.id],
              platformType: '2b',
              putStatus: 1
            }).then((res) => {
              this.$message.success('上架成功')
              this.getProductGroupList()
            })
            this.dialogPushPull = false
          })
        }
      },

      SingleBush2 (row) {
        if (row.id) {
          this.$confirm('确定要下架产品组合？', '提示', {
            confirmButtonText: '确定',
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            type: 'warning',
            center: true
          }).then(() => {
            this.API.ProductGroupPutawayChannel({
              groupIds: [row.id],
              platformType: '2b',
              putStatus: 0
            }).then((res) => {
              this.$message.success('下架成功')
              this.getProductGroupList()
            })
            this.dialogPushPull = false
          })
        }
      },

      sureBatchPush () {
        // 批量上架
        let ids = []
        this.multipleSelection.map(function (value) {
          ids.push(value.id)
        })
        if (ids.length > 0) {
          this.$confirm('确定要上架选中产品组合？', '提示', {
            confirmButtonText: '确定',
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            type: 'warning',
            center: true
          }).then(() => {
            this.API.ProductGroupPutawayChannel({
              groupIds: ids,
              platformType: '2b',
              putStatus: 1
            }).then((res) => {
              this.$message.success('上架成功')
              this.getProductGroupList()
            })
            this.dialogPushPull = false
          })
        } else {
          this.$message.error('上架失败')
        }
      },
      // 批量下架
      sureBatchPull () {
        let ids = []
        this.multipleSelection.map(function (value) {
          ids.push(value.id)
        })
        if (ids.length > 0) {
          this.$confirm('确定要下架选中的产品组合？', '提示', {
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            confirmButtonText: '确定',
            type: 'warning',
            center: true
          }).then(() => {
            this.API.ProductGroupPutawayChannel({
              groupIds: ids,
              platformType: '2b',
              putStatus: 0
            }).then((res) => {
              this.$message.success('下架成功')
              this.getProductGroupList()
            })
            this.dialogPull = false
          })
        } else {
          this.$message.error('批量下架失败')
        }
      },
      // 取消公开
      ProductCancelSecrecy () {
        let ids = []
        this.multipleSelection.map(function (value) {
          ids.push(value.id)
        })
        if (ids.length > 0) {
          this.$confirm('确定取消公开么', '提示', {
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            confirmButtonText: '确定',
            type: 'warning',
            center: true
          }).then(() => {
            this.API.ProductSecrecy({
              groupIds: ids,
              productSecrecyStatus: 0
            }).then((res) => {
              this.$message.success('已取消公开')
              this.getProductGroupList()
            })
          })
        } else {
          this.$message.error('请选择需要批量取消公开的产品再做操作')
        }
      },
      // 公开
      ProductSecrecy () {
        let ids = []
        this.multipleSelection.map(function (value) {
          ids.push(value.id)
        })
        if (ids.length > 0) {
          this.$confirm('确定公开选中产品？', '提示', {
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            confirmButtonText: '确定',
            type: 'warning',
            center: true
          }).then(() => {
            this.API.ProductSecrecy({
              groupIds: ids,
              productSecrecyStatus: 1
            }).then((res) => {
              this.$message.success('产品已公开')
              this.getProductGroupList()
            })
          })
        } else {
          this.$message.error('请选择需要公开的产品再做操作')
        }
      },
      // 取消分配
      cancelAllot () {
        this.dialogAllot = false
      },
      showBatch () {
        // if (this.multipleSelection.filter(item => !item.modelCode).length > 0) {
        //   this.$message.error('选择中记录中没有模型或贴图，不能做分配')
        //   return
        // }
        if (this.multipleSelection.length > 0) {
          this.dialogAllot = true
        } else {
          this.$message.error('请选择一条或多条数据再做分配')
        }
      },

      sureAllot () {
        if (this.multipleSelection.length > 0) {
          this.batchAllot()  // 批量分配
        } else {
          this.singleAllot() // 单条数据分配，
        }
      },
      singleAllot () {
        this.API.ProductGroupAllot({
          groupIds: [this.rowid],
          platformType: this.checkAllot.join(',')
        }).then((res) => {
          this.dialogAllot = false
          this.$message.success('分配成功')
          this.getProductGroupList()
        })
      },
      batchAllot () {
        // 批量分配
        let ids = []
        this.multipleSelection.map(value => {
          ids.push(value.id)
        })
        if (ids.length > 0) {
          this.$confirm('确定要分配么', '提示', {
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            confirmButtonText: '确定',
            type: 'warning',
            center: true
          }).then(() => {
            this.API.ProductGroupAllot({
              groupIds: ids,
              platformType: this.checkAllot.join(',')
            }).then((res) => {
              if (res) {
                this.getProductGroupList()
                this.$message.success('分配成功')
              }
            })
            this.dialogAllot = false
          })
        } else {
          this.$message.error('请选择一条或多条数据再做分配')
        }
      },
      resetInput () {
        this.productInputGroupName = ''
        this.productInputGroupCode = ''
        this.allotValue = ''
        this.productGroupType = ''
        this.isOpen = ''
          this.getProductGroupList();
      },
      ProductGroupType () { // 下拉框产品分类
        this.API.ProductGroupType().then((res) => {
          this.productGroupTypeDrop = res.data
        })
      },
      getProductGroupList () {
        this.loading = true
        let queryCondition = Object.assign({}, this.query, {
          compositeType: this.productGroupType,
          putawayState: this.allotValue,
          groupName: this.productInputGroupName,
          groupCode: this.productInputGroupCode,
          secrecy: this.isOpen
        })
        for (let key in queryCondition) {
          if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
            delete queryCondition[key]
          }
        }
        this.API.getChannelProductGroupList(queryCondition).then((res) => { // 产品组合列表
          if (res.code == 204 || res.code == 400) {
            this.loading = false
          }
          if (res) {
            this.loading = false
            this.productGroupList = res.list
            this.total = res.total
          } else {
            this.productGroupList = []
            this.total = 0
          }

        })
      },

      changeSwitch (row) {
        if (row) {
          if (row.secrecy) {
            row.statusSwitchTxt = '公开'
            this.API.ProductSecrecy({
              groupIds: [row.id],
              productSecrecyStatus: 1
            }).then((res) => {

            })
          } else {
            row.statusSwitchTxt = '未公开'
            this.API.ProductSecrecy({
              groupIds: [row.id],
              productSecrecyStatus: 0
            }).then((res) => {

            })
          }
        }
      },
      toggleSelection (rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row)
          })
        } else {
          this.$refs.multipleTable.clearSelection()
        }
      },
      showDetailProducts (row) {
        if (row) {
          localStorage.setItem('breadcrumb3', '产品组合详情')
          this.$store.commit('breadcrumb3', '产品组合详情')
          this.$router.push({path: `/channel/productGroupDetail/${row.id}`})
        }
      },
      // 跳到产品编辑页面
      goToEdit (index) {
        if (index) {
          localStorage.setItem('breadcrumb3', '产品组合编辑')
          this.$router.replace(`/channel/productGroupEdit/${index.id}`)
        }
      },
      handleSelectionChange (val) {
        this.multipleSelection = val
      },
      // 分页模块
      handleSizeChange: function (size) {
        // this.pageSize = size;
        this.query.limit = size
        this.getProductGroupList()
      },
      handleCurrentChange: function (currentPage) {
        // this.currentPage = currentPage;
        this.query.page = currentPage
        this.loading = true
        this.getProductGroupList()
      }
    },
    components: {
      upload,
      ImgPreView
    }
  }
</script>

<style scoped lang="scss">
  @import "../../../assets/css/mixin.scss";

  .minWidth {
    padding-left: 30px !important;
    min-width: 600px;
  }

  .dialogDiv {
    background: rgba(0, 0, 0, 0.3);
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 111;
  }

  .checkboxFrom {
    position: absolute;
    background: #fff;
    width: 420px;
    height: 240px;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    border-radius: $borderRadius;
    h1 {
      padding: 10px;
      font-size: 14px;
      position: relative;
      border-bottom: solid 1px $greyBorder;
      span {
        position: absolute;
        right: 0;
      }
    }
    div {
      text-align: center;
      line-height: 130px;
    }
    p {
      text-align: center;
    }
  }

  .operatonBtn {
    font-size: 14px;
  }

  .products {
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
      padding: 20px 0;
      .selectStyle {
        width: 200px;
        margin-left: 6px;
      }
      .el-col{
        margin-right: 25px;
      }
    }
    .nonImg {
      background: #f2f2f2;
      height: 90px;
      width: 90px;
      line-height: 90px;
      text-align: center;
      font-size: 12px;
      display: inline-block;
      color: #999;
      border: solid 1px #e3e3e3;
    }
    .row-bg {
      padding: 10px 0;
      background-color: #f9fafc;
    }
    .input_round {
      position: relative;
      li {
        display: inline-block;
        margin-right: 25px;
        .search {
          width: 130px;
          color: $buttonOrange;
          border: solid 1px $btnOrange
        }
      }
    }
    .tableFilter {
      height: 80px;
      line-height: 80px;
      padding: 0 20px;
      display: block;
      background: #fff;
      border-radius: 4px 4px;
      li {
        display: inline-block;
        padding: 0 10px;
        line-height: normal;
        color: #666;
        cursor: pointer;
        .refresh {
          font-size: 14px;
          padding: 20px 30px;
          color: #666;
          background: url('../../../assets/images/icons/reflesh.png') 0 center no-repeat;
          &:hover {
            font-size: 14px;
            color: #ff6419;
            background: url('../../../assets/images/icons/reflesh2.png') 0 center no-repeat;
          }
        }
        .out {
          color: #666;
          padding-left: 25px;
          background: url('../../../assets/images/icons/delete.png') 0 center no-repeat;
          &:hover {
            color: #ff6419;
            background: url('../../../assets/images/icons/delete2.png') 0 center no-repeat;
          }
        }

        .download {
          color: #666;
          padding-left: 25px;
          background: url('../../../assets/images/icons/pull.png') 0 center no-repeat;
          &:hover {
            color: #ff6419;
            background: url('../../../assets/images/icons/pull2.png') 0 center no-repeat;
          }
        }

        .upload2 {
          padding-left: 25px;
          color: #666;
          background: url('../../../assets/images/icons/push.png') 0 center no-repeat;
          &:hover {
            color: #ff6419;
            background: url('../../../assets/images/icons/push2.png') 0 center no-repeat;
          }
        }
      }
    }

    .el-button--small {
      display: inline-block;
      padding: 6px 12px;
      margin: 10px 0 0 0;
    }
    .pagination {
      position: relative;
      bottom: 20px;
      margin: 20px 0;
      height: 80px;
      background: $bg-color-white;
      border-radius: 0 0 4px 4px;
      .block {
        margin-top: 24px;
        text-align: center;
      }
    }
  }

  .addProducts, .editProducts, .DetailProducts {
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
      .input-item {
        display: inline-block;
      }
      .size-w, .size-h {
        display: inline-block;
      }
      .mapsSelect {
        display: block;
      }
      .desc {
        max-width: 886px;
        height: 130px;
        textarea {
          background: #f6f6f6;
        }
      }
      .blackBtn {
        color: $bg-color-white;
        background: $greyButton;
      }
    }
  }

  .pageFrame {
    background: #fff;
    height: 110px;
    padding-top: 40px;
    box-sizing: border-box;
    text-align: center;
  }

  .inactive-icon-class {
    background: #5daf34;
    border: solid 1px #ddd !important;
  }

  .active-icon-class {
    background: #5daf34;
    border: solid 1px #ddd !important;
  }

</style>
<style>
  .el-table td {
    padding-left: 20px;
  }

  .el-table th {
    height: 60px;
    background: #f3f3f3;
    padding-left: 20px;
    text-align: center
  }

  .el-tabs__nav {
    margin-left: 30px;
  }

  .el-tabs__item {
    height: 50px;
    line-height: 50px;
  }
</style>

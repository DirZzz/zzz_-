<template>
  <transition name="fade">
    <section class="products">
      <!--工具条-->
      <el-row>
        <el-form :inline="true">
          <el-row class="grid-content bg-purple-dark minWidth">
            <el-col :span="3">
              <el-select ref="productCates" placeholder="请选择产品组合类型" clearable @change="getProductGroupList"
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
              <span class="labelColor"></span>
              <!--<el-select v-model="allotValue"  clearable @change="getProductGroupList" placeholder="请选择上架情况">
                <el-option
                  v-for="(item,index) in allot"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>-->

              <el-select @change="getProductGroupList" clearable v-model="query.platformId" placeholder="请选择上架情况">
                <el-option
                        v-for="(item, index) in platformList"
                        :key="index"
                        :label="item.name"
                        :value="item.id">
                </el-option>
                <el-option value="0" label="未上架"></el-option>
              </el-select>

            </el-col>
            <el-col :span="3">
              <el-select v-model="isOpen"  clearable @change="getProductGroupList"  placeholder="请选择公开状态">
                <el-option
                  v-for="item in openDisclosed"
                  :key="item.name"
                  :label="item.name"
                  :value="item.isON">
                </el-option>
              </el-select>
            </el-col >
            <el-col :span="3">
              <el-input v-model="productInputGroupName" placeholder="请输入组合名称"></el-input>
            </el-col>
            <el-col :span="3">
              <el-input v-model="productInputGroupCode" placeholder="请输入组合编号"></el-input>
            </el-col>
          </el-row>
          <el-row class="searchBtnFrame">
              <el-button type="primary" round @click="getProductGroupList">搜索</el-button>
            <!--:disabled="productInputGroupName == '' && productCode == '' && allotValue == '' && selectedType == ''"-->
              <el-button class="searchBtn" @click="resetInput" round>重置</el-button>
          </el-row>
        </el-form>
      </el-row>
      <!--列表-->
      <div class="tableFilter">
        <ul>
          <li @click="batchShelf" class="share" v-if="isPer('product:group:exhibition')"><span>批量上架</span></li>
          <li @click="batchDelete" class="out" v-if="isPer('product:group:del')"><span>批量删除</span></li>
          <li @click="ProductSecrecy()" class="open" v-if="isPer('product:group:manage')"><span> 批量公开</span></li>
          <li @click="ProductCancelSecrecy()" class="offOpen" v-if="isPer('product:group:manage')"><span> 批量取消公开</span></li>
          <li @click="refreshList" class="refresh"><span> 刷新</span></li>
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
        :default-sort="{prop: 'createTime', order: 'descending'}"
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
            <p v-if="scope.row.compositeTypeName">{{scope.row.compositeTypeName.replace(/\//g,'>')}}</p>
            <p v-else>———</p>
          </template>
        </el-table-column>

        <el-table-column prop="putStatusNames" label="上架情况" align="center" width="150">
          <template slot-scope="scope">
            <div v-if="scope.row.putStatusNames" v-for="(item,index) in scope.row.putStatusNames" :key="index">
              <p>{{item}}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="录入时间" width="110" align="center" sortable>
          <template slot-scope="scope">
            <p style="width: 80px; text-align: center" v-if="scope.row.createTime">{{scope.row.createTime}}</p>
            <p v-else>———</p>
          </template>
        </el-table-column>
        <el-table-column prop="secrecy" label="公开状态" align="center">
          <template slot-scope="scope">
            <el-switch class="switchStyle"
                       v-model="scope.row.secrecy"
                       active-color="#ff6419"
                       inactive-icon-class="inactive-icon-class"
                       active-icon-class="active-icon-class"
                       :active-value="0"
                       inactive-color="#ddd"
                       :inactive-value="1"
                       @change="changeSwitch(scope.row)"
            >
            </el-switch>
            <p>{{scope.row.secrecy == 0 ? scope.row.statusSwitchTxt = '公开' : scope.row.statusSwitchTxt = '未公开'}}</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" class="btnGroup" align="center" width="160">
          <template slot-scope="scope">
            <div>
              <el-button type="text" size="mini" style="margin-left: 0" v-if="isPer('product:group:view')"  @click="showDetailProducts(scope.row)">详情</el-button>
              <el-button  type="text" size="mini" style="margin-left: 0" v-if="isPer('product:group:edit')" @click="goToEdit(scope.row)">编辑</el-button>
            </div>
            <div>
              <el-button type="text" size="mini" style="margin-left: 0" v-if="isPer('product:group:del')"  @click="handleDel(scope.row)">删除</el-button>
            <!--<span class="operatonBtnTxt" v-if="isPer('product:group:manage')" @click="batchSingle(scope.row)">分配</span>-->
            <!--<span class="operatonBtnTxt" v-if="isPer('product:group:del')"  @click="handleDel(scope.row)">公开</span>-->
              <el-button type="text" size="mini" style="margin-left: 0" v-if="isPer('product:group:exhibition')" @click="singleShelf(scope.row)">上架管理</el-button>
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
      <img-pre-view ref="sonParams"></img-pre-view>

      <el-dialog
              title="上下架管理"
              :visible.sync="shelfDialogVisible"
              width="540px"
              top="30vh"
      >
        <div>
          <el-checkbox-group style="width:400px;margin-left: 40px" v-model="shelf.targetPlatform">
            <el-checkbox  style="width:120px;margin-left: 30px" v-for="(ds, index) in platformList" :key="index" :value="parseInt(ds.id)" :label="parseInt(ds.id)">{{ds.name}}
            </el-checkbox>
          </el-checkbox-group>
        </div>
        <span slot="footer" class="dialog-footer">
              <el-button round type="primary" style="width: 80px" size="medium" class="confirmButtonClass" @click="shelfConfirm">确 定</el-button>
              <el-button round type="info"  style="width: 80px" size="medium" class="cancelButtonClass" @click="shelfDialogVisible = false">取 消</el-button>
            </span>
      </el-dialog>
    </section>
  </transition>
</template>

<script>
  import upload from '../../../views/public/upload/upload'
  import qs from 'qs'
  import ImgPreView from '@/components/Img-preview'
  import { mapState } from 'vuex'
  export default {
    data () {
      return {
          platformList:[],  // 上架平台列表
          shelf:{
              targetPlatform: []
          },
          productShelfParams:{  // 产品上下架
              groupIds:[],
              platformIds: ""
          },
          shelfDialogVisible:false,
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
        this.getShelfPlatformList()
    },
    mounted () {
      this.getProductGroupList()
        this.tabsSelect = this.$store.state.activeName
    },
      activated(){
          this.tabsSelect = this.$store.state.activeName
      },
    methods: {
        // 获取上架平台列表
        getShelfPlatformList(){
            this.API.platformShelf({
                menuType:0
            }).then((res) => {
                if(res) {
                    this.platformList = res.data
                }
            })
        },
        // 产品批量上下架
        batchShelf() {
            if(this.multipleSelection.length === 0){
                this.$message.error('请选择需要上架的产品')
                return
            }
           /* if(this.multipleSelection.filter(item => !item.modelCode).length > 0) {
                this.$message.error('选择中记录中没有模型或贴图，不能做上架')
                return
            }*/
            this.shelf.targetPlatform=[]
            this.shelfDialogVisible = true
        },
        shelfConfirm(){
            if(this.multipleSelection.length > 0) {
                this.productBatchShelf()
            } else {
                this.singleShelfConfirm()
            }
        },
        singleShelf(row){
            console.log(row, 'aaaaa')
            /*if (!row.modelCode) {
                this.$message.error('没有模型或贴图，不能做上架')
                return
            }*/
            if(row.platformIds) {
                let arrs = row.platformIds.split(',')
                arrs = arrs.map(id => {return +id})
                this.shelf.targetPlatform = arrs
            }
            this.singleProductId=[]
            this.shelfDialogVisible  = true
            this.singleProductId.push(row.id)
        },
        productBatchShelf() {

            // this.$confirm('确定要上架么?', '提示', {
            //     cancelButtonClass: 'cancelButtonClass',
            //     confirmButtonClass: 'confirmButtonClass',
            //     type: 'info',
            //     center: true
            // }).then(res => {
            let ids = []
            if(this.multipleSelection) {
                this.multipleSelection.map(function (value) {
                    ids.push(value.id)
                })
            }
            if(this.shelf.targetPlatform) {
                this.productShelfParams.platformIds = this.shelf.targetPlatform.join(',')
            }
            this.productShelfParams.groupIds = ids
            this.API.productGroupShelf(this.productShelfParams).then((res) => {
                this.shelfDialogVisible = false
                this.getProductGroupList()
                this.$message.success('批量上架成功')
            })
            //})

        },
        singleShelfConfirm() {
            // this.$confirm('确定要上架么?', '提示', {
            //     cancelButtonClass: 'cancelButtonClass',
            //     confirmButtonClass: 'confirmButtonClass',
            //     type: 'info',
            //     center: true
            // }).then((res) => {
            this.productShelfParams.groupIds = []
            if(this.shelf.targetPlatform) {
                this.productShelfParams.platformIds = this.shelf.targetPlatform.join(',')
            }
            this.productShelfParams.groupIds=this.singleProductId
            this.API.productGroupShelf(this.productShelfParams).then((res) => {
                this.shelfDialogVisible = false
                this.getProductGroupList()
                this.$message.success('上架成功')
            })
            //})

        },
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
            this.API.ProductGroupSecrecy({
              groupIds: ids,
              secrecy: 1
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
            this.API.ProductGroupSecrecy({
              groupIds: ids,
              secrecy: 0
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
      // 单条数据分配
      batchSingle (row) {
        // if (!row.modelCode) {
        //   this.$message.error('没有模型或贴图，不能做分配')
        //   return
        // }
        this.dialogAllot = true
        this.rowid = row.id
        this.checkAllot = [...(row.status2b ? ['2b'] : []), ...(row.status2c ? ['2c'] : [])] // 分配回显状态
      },
      sureAllot () {
        if (this.multipleSelection.length > 0) {
          this.batchAllot()  // 批量分配
        } else {
          this.singleAllot() // 单条数据分配，
        }
      },
      singleAllot () {
        console.log(this.rowid, 'this.rowid')
        console.log(this.checkAllot.join(','), 'this.checkAllot.join')
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
          console.log(ids,'ids')
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
          this.query.platformId = ''
        this.productGroupType = ''
        this.isOpen = ''
        this.getProductGroupList()
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
          allotState: this.allotValue,
          groupName: this.productInputGroupName,
          groupCode: this.productInputGroupCode,
          secrecy: this.isOpen
        })
        for (let key in queryCondition) {
          if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
            delete queryCondition[key]
          }
        }
        this.API.getLibraryProductGroupList(queryCondition).then((res) => { // 产品组合列表
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
            this.API.ProductGroupSecrecy({
              groupIds: [row.id],
              secrecy: 1
            }).then((res) => {

            })
          } else {
            row.statusSwitchTxt = '未公开'
            this.API.ProductGroupSecrecy({
              groupIds: [row.id],
              secrecy: 0
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
          this.$router.push({path: `/productGroupDetail/${row.id}`})
        }
      },
      // 跳到产品编辑页面
      goToEdit (index) {
        if (index) {
          localStorage.setItem('breadcrumb3', '产品组合编辑')
          this.$router.replace(`/productGroupEdit/${index.id}`)
        }
      },
      handleSelectionChange (val) {
        this.multipleSelection = val
      },
      // 删除一条数据
      handleDel (row) {
        this.$confirm('确认删除选中的组合吗?', '提示', {
          cancelButtonClass: 'cancelButtonClass',
          confirmButtonClass: 'confirmButtonClass',
          type: 'warning',
          center: true
        }).then(() => {
          this.API.ProductGroupDelete({
            groupIds: row.id
          }).then((res) => {
            if(res) {
              this.$message.success('删除成功')
              this.getProductGroupList()
            }
          })
        })
      },
      batchDelete () { // 批量删除
        let ids = []
        this.multipleSelection.map(function (value) {
          ids.push(value.id)
        })
        if (ids.length > 0) {
          this.$confirm('确认批量删除选中的组合吗?', '提示', {
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            type: 'warning',
            center: true
          }).then(() => {
            this.API.ProductGroupDelete({
              groupIds: ids.join(',')
            }).then(() => {
              this.$message.success('删除成功')
              this.getProductGroupList()
            })
          })
        } else {
          this.$message.error('请选择一条或多条数据再删除')
        }
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
      computed: mapState({
          activeName: state => state.activeName,
      }),
    components: {
      upload,
      ImgPreView
    }
  }
</script>

<style scoped lang="scss">
  @import "../../../assets/css/mixin.scss";

  .minWidth {
    padding-left: 30px!important;
    min-width: 600px;
  }
  .operatonBtnTxt{
    color: #FF6419;
    cursor: pointer;
    padding-right: 20px;
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

  .operatonBtn {
    font-size: 14px;
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

  .products {
    position: relative;
    margin-bottom:10px;
    .el-row {
      margin-bottom: 10px;
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
      .el-col {
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
      color: #999;
      display: inline-block;
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
        margin-right: 15px;
        .search {
          width: 130px;
          color: $buttonOrange;
          border: solid 1px $btnOrange
        }
      }
    }
    .tableFilter {
      height: 80px;
      font-size: 14px;
      line-height: 80px;
      padding: 0 30px;
      display: block;
      background: #fff;
      border-radius: 4px 4px;
      .share {
        height: 46px;
        line-height: 46px;
        padding-left: 25px;
        background: url('../../../assets/images/icons/allot.png') 0 center no-repeat;
        &:hover {
          background: url('../../../assets/images/icons/allot2.png') 0 center no-repeat;
        }
      }
      .out {
        height: 46px;
        line-height: 46px;
        padding-left: 25px;
        background: url('../../../assets/images/icons/delete.png') 0 center no-repeat;
        &:hover {
          background: url('../../../assets/images/icons/delete2.png') 0 center no-repeat;
        }
      }
      .open {
        height: 46px;
        line-height: 46px;
        padding-left: 25px;
        background: url('../../../assets/images/icons/secrecy.png') 0 center no-repeat;
        &:hover {
          background: url('../../../assets/images/icons/secrecy2.png') 0 center no-repeat;
        }
      }
      .offOpen {
        height: 46px;
        line-height: 46px;
        padding-left: 25px;
        background: url('../../../assets/images/icons/batchSecrecy.png') 0 center no-repeat;
        &:hover {
          background: url('../../../assets/images/icons/batchSecrecy2.png') 0 center no-repeat;
        }
      }
      .refresh {
        height: 46px;
        line-height: 46px;
        padding-left: 25px;
        background: url('../../../assets/images/icons/reflesh.png') 0 center no-repeat;
        &:hover {
          background: url('../../../assets/images/icons/reflesh2.png') 0 center no-repeat;
        }
      }
      li {
        display: inline-block;
        margin-right: 22px;

        color: #666;
        cursor: pointer;
        &:hover {
          color: $overButtonOrange;
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
    padding-left: 20px;text-align: center
  }
  .el-tabs__nav {
    margin-left: 30px;
  }
  .el-tabs__item{
    height: 50px;
    line-height: 50px;
  }
</style>

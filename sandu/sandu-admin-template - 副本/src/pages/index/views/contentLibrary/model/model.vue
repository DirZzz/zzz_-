<template>
  <div>
    <div class="topSearch">
      <el-row :gutter="24" class="upBox">
        <el-col :span="3">
          <el-cascader @change="page('reset')" ref="modelType" clearable
                       v-model="query.categoryCodes"
                       placeholder="请选择模型分类"
                       :options="categoryTree"
                       :props="{
                          label: 'name',
                          value:'keyCode',
                          children: 'children'
                        }"
                       filterable
          ></el-cascader>
        </el-col>
        <el-col :span="3">
          <el-select v-model="query.transStatus" clearable @change="page('reset')" placeholder="请选择状态处理">
            <el-option
              v-for="item in statusDeal"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select v-model="query.isRelevancyProduct" clearable @change="page('reset')" placeholder="是否关联产品">
            <el-option
                    v-for="item in isRelevancyProductList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-input class="inputSearchKey"
                    placeholder="请输入模型名称"
                    v-model="query.modelName"
                    clearable>
          </el-input>
        </el-col>
        <el-col :span="3">
          <el-input class="inputSearchKey"
                    placeholder="请输入模型编号"
                    v-model="query.modelCode"
                    clearable>
          </el-input>
        </el-col>
        <el-col :span="3">
          <el-input class="inputSearchKey"
                    placeholder="请输模型型号"
                    v-model="query.modelModelNum"
                    clearable>
          </el-input>
        </el-col>
      </el-row>
      <el-row :gutter="24" class="downBox">
          <div style="display:inline-block;">
             <el-button round type="primary" class="searchNavBtn"  @click="page('reset')">搜索</el-button>
            <el-button round class="searchNavBtn" @click="clearInput">重置</el-button>
          </div>
           <el-button   style="float:right;" round  class="searchNavBtn el-button--primary txtColor" plain v-if="isPer('model:add')"
                     @click="toNewModel">新增模型
          </el-button>

       
      </el-row>
    </div>
    <div class="modelList">
      <div class="deleteBar">
        <el-button type="text" @click="batchDelete" v-if="isPer('model:del')" class="delete">批量删除</el-button>
        <el-button type="text" @click="handleBatchDeliver" v-if="isPer('model:delivery')" class="delivery">批量交付
        </el-button>
        <el-button size="small" type="text" @click="page" class="refresh">刷新</el-button>
        <a  :href="updataURL"
           style="outline: none; border: none">
          <el-tooltip content="下载模型编辑器" :popper-class="downloadEdit" placement="bottom" effect="light">
            <span class="downloadModel" id="downLoad"></span>
          </el-tooltip>
        </a>
      </div>
      <div>
        <div class="deleteDataFrame">
        </div>
        <el-table
          :data="models"
          style="width: 100%;"
          ref="multipleTable"
          v-loading="modelLoading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(255, 255, 255, 0.8)"
          @selection-change="handleSelectionChange"
          tooltip-effect="dark"
          :default-sort="{prop: 'updateDate', order: 'descending'}"
        >
          <el-table-column
            type="selection"
            label="全选"
            align="center"
            width="65" title="全选">
          </el-table-column>
          <el-table-column
            prop="modelCode"
            label="模型编号"
            width="180">
          </el-table-column>
          <el-table-column
            prop="modelThumb"
           label="模型缩略图"
            align="center"
            width="120">
            <template slot-scope="scope">
              <div v-if="scope.row.thumbPicPath">
                <img style="cursor:pointer;" @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.thumbPicPath)" :src="BASE_URL.sourceBaseUrl+scope.row.thumbPicPath" width="80">
              </div>
              <div class="nonImg" v-else> 无缩略图</div>
            </template>
          </el-table-column>

          <el-table-column
            prop="categoryNames"
            align="center"
            label="模型分类">
            <template slot-scope="scope">
              <div v-if="scope.row.categoryNames">
                {{scope.row.categoryNames.replace(/\,/g, '>')}}
              </div>
              <div v-else>———</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="modelName"
            align="center"
            label="模型名称">
            <template slot-scope="scope">
              <span class="modelName" v-if="scope.row.modelName">{{scope.row.modelName}}</span>
              <span v-else>———</span>
            </template>
          </el-table-column>

          <el-table-column
            prop="modelModelNum"
            align="center"
            label="模型型号">
            <template slot-scope="scope">
              <span class="modelName" v-if="scope.row.modelModelNum">{{scope.row.modelModelNum}}</span>
              <span v-else>———</span>
            </template>
          </el-table-column>

          <el-table-column
            prop="productName"
            center
            label="对应产品">
            <template slot-scope="scope">
              <div v-if="scope.row.concerProductId">
                <router-link :to="{path: '/detailProduct/'+scope.row.concerProductId}">{{scope.row.concerProductName}}
                </router-link>
              </div>
              <div v-else>———</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="transStatus"
            label="处理状态"
            center
          >
            <template slot-scope="scope">
              <div v-if="scope.row.transStatus">
                <span v-if="scope.row.transStatus === 'ING'">转化中</span>
                <span v-if="scope.row.transStatus === 'SUCCESS'">转化成功</span>
                <span v-if="scope.row.transStatus === 'FAIL'">转化失败</span>
                <span v-if="scope.row.transStatus === 'HANDLE'">需人工处理</span>
              </div>
              <div v-if="scope.row.transStatus=='NONE'">———</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="updateDate"
            label="最新修改时间"
            align="center"
            width="180px"
            sortable>
            <template slot-scope="scope">
              <p style="width: 80px;display: inline-block; text-align: center;" v-if="scope.row.updateDate">
                {{scope.row.updateDate}}</p>
              <div v-else style="text-align: center; display: inline-block; ">———</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="modelOrigin"
            label="模型来源"
            align="center"
          ></el-table-column>
          <!--<el-table-column-->
            <!--prop="modelDesc"-->
            <!--label="备注"-->
            <!--align="center"-->
          <!--&gt;-->
            <!--<template slot-scope="scope">-->
              <!--<div v-if="scope.row.modelDesc">{{scope.row.modelDesc}}</div>-->
              <!--<div v-else>———</div>-->
            <!--</template>-->
          <!--</el-table-column>-->
          <el-table-column
            prop="operation"
            label="操作" align="center" width="160"
          >
            <template slot-scope="scope">
              <div class="table-td-right">
                <el-button class="operatonBtn" type="text"
                           v-if="isPer('model:delivery') && scope.row.transStatus === 'SUCCESS' && scope.row.modelOrigin=== undefined"
                           @click="handleDeliver(scope.row)">交付
                </el-button>
                <el-button class="operatonBtn" type="text" v-if="isPer('model:edit')"
                           @click="handleEdit(scope.$index, scope.row)">编辑
                </el-button>

                <el-button class="operatonBtn" type="text" v-if="isPer('model:view')"
                           @click="handleDetail(scope.$index, scope.row)">详情
                </el-button>

                <el-button class="operatonBtn" type="text" v-if="isPer('model:del')"
                           @click="handleDelete(scope.$index, scope.row)">删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
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
      </div>
    </div>
    <el-dialog
      title="选择交付企业"
      :visible.sync="deliverDialogVisible"
      width="25%"
      top="30vh"
      center>
      <el-input
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
        v-model="deliverSearch">
      </el-input>
      <el-checkbox-group v-model="delivery.toCompanyId">
        <div class="deliver-box">
          <el-checkbox style="display: none"></el-checkbox>
          <el-checkbox class="deliver-box-line"
                       v-for="(c, index) in searchCompanys" :label="c.companyId" :key="c.companyId">
            {{c.companyName}}
          </el-checkbox>
        </div>

      </el-checkbox-group>
      <span slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="handleDeliverConfirm">确 定</el-button>
        <el-button round type="info" @click="deliverDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="选择交付企业"
      :visible.sync="batchDeliverDialogVisible"
      width="25%"
      top="30vh"
      center>
      <el-input
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
        v-model="batchDeliverSearch">
      </el-input>
      <el-checkbox-group v-model="delivery.toCompanyId">
        <div class="deliver-box">
          <el-checkbox style="display: none"></el-checkbox>
          <el-checkbox class="deliver-box-line"
                       v-for="(c, index) in batchSearchCompanys" :label="c.id" :key="c.id">
            {{c.name}}
          </el-checkbox>
        </div>

      </el-checkbox-group>
      <span slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="handleBatchDeliverConfirm">确 定</el-button>
        <el-button round type="info" @click="batchDeliverDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <router-view></router-view>
    <img-pre-view ref="sonParams"></img-pre-view>
  </div>
</template>

<script>
  import ElButton from 'element-ui/packages/button/src/button'
  import qs from 'qs'
  import ImgPreView from '@/components/Img-preview'
  export default {
    components: {
      ElButton,
      ImgPreView
    },
    data () {
      return {
          isRelevancyProductList:[
              { value:1,label:'是'},
              { value:0,label:'否'}
          ],
        updataURL:'',
        downloadEdit: 'downloadEdit',
        modelLoading: true,
        modelList: false,
        dialogTableVisible: false,
        selectStausDeal: '',
        query: {
          page: 0,
          limit: 10,
          companyId: sessionStorage.getItem('companyID'),
          transStatus: '',
          categoryCodes: [],
          isUsed: -1
        },
        total: 0,
        statusDeal: [
          {
            value: 'ING',
            label: '转化中'
          }, {
            value: 'SUCCESS',
            label: '已转化'
          }, {
            value: 'FAIL',
            label: '转化失败'
          }, {
            value: 'HANDLE',
            label: '需人工处理'
          }
        ],
        categoryTree: [],
        sels: [],
        models: [],
        deliverDialogVisible: false,
        batchDeliverDialogVisible: false,
        deliverSearch: '',
        batchDeliverSearch: '',
        delivery: {
          fromCompanyId: sessionStorage.getItem('companyID'),
          toCompanyId: []
        },
        searchCompanys: [],
        batchSearchCompanys: []
      }
    },
    mounted () {
      // this.page()
      // // this.showHide();
      // this.getCategoryTree();
      // this.UpData_Model();
    },
    activated () {
      this.$store.commit('breadcrumb3', '模型编辑')
      this.page()
      // this.showHide();
      this.getCategoryTree();
      this.UpData_Model();
    },
    methods: {
          //图片预览事件
      ImgView(ImgURL){
       this.$refs.sonParams.sonFun({ImgURL:ImgURL,ImgShow:true});
     }, 
      UpData_Model(){
       this.API.updatamodel({}).then((res)=>{
         this.updataURL = process.env.sourceBaseUrl + res.data;
         console.log(res)
       })
      },
      isPer (per) {
        let permiss = qs.parse(sessionStorage.getItem('loginUser'))
        return this.isMainPer(per, permiss)
      },
      page (type) {
        type=='reset' ?  this.query.page=0 : '';
        this.modelLoading = true
        let modelQuery = Object.assign({}, this.query)
        if (this.query.categoryCodes && this.query.categoryCodes.length > 0) {
          modelQuery.categoryCodes = this.query.categoryCodes.join(',')
        }
        for (let key in modelQuery) {
          if ((modelQuery[key] == '' || modelQuery[key] == undefined || modelQuery[key] == null)&&modelQuery[key]!=0) {
            delete modelQuery[key]
          }
        }
        if (this.query.categoryCodes) {
            modelQuery.modelType = this.query.categoryCodes[this.query.categoryCodes.length-1]
        }
        this.API.ListModel(modelQuery).then(resp => {
          if (resp.code == 204) {
            this.modelLoading = false
          }
          if (resp) {
            this.modelLoading = false
            this.models = resp.list
            this.total = resp.total
          } else {
            setTimeout(function () {
              this.modelLoading = false
            }, 3000)
          }
        })
      },
      getCategoryTree () {
        this.API.ProductCategory({
          type: 'model'
        }).then(resp => {
          if (resp) {
            this.categoryTree = resp.data
          }
        })
      },
      toNewModel () {
          localStorage.setItem('breadcrumb3', '模型新增')
          this.$store.commit('breadcrumb3', '模型新增')
        this.$router.push('/model/add')
      },
      clearInput () {
          this.selectStausDeal = ''
          this.query = {
              page: 0,
              limit: 10,
              companyId: sessionStorage.getItem('companyID'),
              isUsed: -1
          }
        let obj = {}
        obj.stopPropagation = () => {}
        this.$refs.modelType.clearValue(obj)
      },
      handleSelectionChange (val) {
        this.sels = val
      },
      // 模型详情和编辑及删除
      handleEdit (index, row) {
        if (row.transStatus == 'ING') {
          this.$message.error('转化中的模型不能编辑')
          return
        }
        localStorage.setItem('breadcrumb3', '模型编辑')
        this.$store.commit('breadcrumb3', '模型编辑')
        this.$router.push(`/model/${row.modelId}/edit`)
      },
      handleDetail (index, row) {
        localStorage.setItem('breadcrumb3', '模型详情')
        this.$store.commit('breadcrumb3', '模型详情')
        this.$router.push(`/model/${row.modelId}`)
      },
      handleDelete (index, row) {
        if (row.concerProductName) {
          this.$message.error('删除的模型中已关联产品，无法删除!')
          return
        }
        if (row.transStatus == 'ING') {
          this.$message.error('删除的模型正在转化中，无法删除!')
        } else {
          this.$confirm('此操作将永久删除, 是否继续?', '提示', {
            center: true,
            cancelButtonClass: 'cancelButtonClass',
            confirmButtonClass: 'confirmButtonClass',
            type: 'warning'
          }).then((index) => {
            this.API.DeleteModel(row.modelId).then(resp => {
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.page()
            })
          })
        }
      },
      // 选择删除
      batchDelete () {
        if (this.sels.length <= 0) {
          this.$message.error('请至少选择一条记录')
          return
        }
        if (this.sels.map(item => item.transStatus).includes('ING')) {
          this.$message.error('模型正在转化中，无法删除')
          return
        }
        if (this.sels.filter(item => item.concerProductName).length > 0) {
          this.$message.error('模型转化成功并已关联产品，无法删除')
          return
        }
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then((index) => {
          const modelIds = this.sels.map(sel => sel.modelId).join(',')
          this.API.DeleteModel(modelIds).then(resp => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.page()
          })
        })
      },
      // 分页事件
      handleSizeChange: function (size) {
        this.query.limit = size
        this.page()
      },
      handleCurrentChange: function (currentPage) {
        this.modelLoading = true
        this.query.page = currentPage
        this.page()
      },
      handleDeliver (row) {
        this.deliverSearch = ''
        this.deliverDialogVisible = true
        this.delivery.modelId = [row.modelId]
        this.API.ListModelCompanysWithDelivered({
          companyId: sessionStorage.getItem('companyID'),
          modelId: row.modelId
        }).then((res) => {
          this.companys = res.data
          this.disabledDeliverCompanyIds = this.companys.filter(it => it.delivered).map(it => it.companyId)
          this.delivery.toCompanyId = this.companys.filter(it => it.delivered).map(it => it.companyId)
          this.searchCompanys = this.companys
        })
      },
      handleBatchDeliver () {
        this.batchDeliverSearch = ''
        let ismodelOrigin = false;
        if(this.sels.some(item=>{
            if(item.modelOrigin){
               ismodelOrigin = true;
            }
        }))
        console.log(ismodelOrigin)
        if(ismodelOrigin){
          this.$message.error('选择的模型已经交付,无法交付')
           return
        }
        this.delivery.modelId = this.sels.map(it => it.modelId)
        console.log(this.sels )
        // if (this.sels.filter(it => it.modelOrigin !== '内部制作').length > 0) {
        //   this.$message.error('选择的模型包含交付过来的模型,无法交付')
        //   return
        // }
        if (this.delivery.modelId.length === 0) {
          this.$message.error('请至少选择一条记录')
          return
        }
        this.batchDeliverDialogVisible = true
        this.API.ListCompany({}).then((res) => {
          this.companys = res.data
          this.delivery.toCompanyId = []
          this.batchSearchCompanys = this.companys
        })
      },
      handleDeliverConfirm () {
//        if (this.delivery.toCompanyId.length <= 0) {
//          this.$message.error('请选择一个公司')
//          return
//        }
        this.$confirm('确定要交付吗?', '提示', {
          type: 'warning'
        }).then(() => {
          this.API.ModelDeliver(this.delivery).then((resp) => {
            this.deliverDialogVisible = false
            this.page()
            this.$message({
              type: 'success',
              message: '交付成功!'
            })
          })
        })
      },
      handleBatchDeliverConfirm () {
//        if (this.delivery.toCompanyId.length <= 0) {
//          this.$message.error('请选择一个公司')
//          return
//        }
        this.$confirm('确定要交付吗?', '提示', {
          type: 'warning'
        }).then(() => {
          this.API.BatchModelDeliver(this.delivery).then((resp) => {
            this.batchDeliverDialogVisible = false
            this.page()
            this.$message({
              type: 'success',
              message: '交付成功!'
            })
          })
        })
      }

    },
    watch: {
      deliverSearch (val) {
        if (val) {
          this.searchCompanys = this.companys
            .filter(c => c.companyName.includes(val))
        } else {
          this.searchCompanys = this.companys
        }
      },
      batchDeliverSearch (val) {
        if (val) {
          this.batchSearchCompanys = this.companys
            .filter(c => c.name.includes(val))
        } else {
          this.batchSearchCompanys = this.companys
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  @import "../../../assets/css/mixin";

  .deliver-box {
    margin-top: 20px;
    height: 200px;
    overflow-y: scroll;
  }

  .deliver-box-line {
    display: block;
  }

  .modelName {
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
  }

  .topSearch {
    box-sizing: border-box;
    border-radius: 4px;
    background-color: #fff;
    .upBox{
      border-bottom:1px solid #ccc;
      margin:0!important;
      padding:20px;
    }
    .downBox{
     padding:20px 42px ;
      display: table;
      text-align: center;
      width: 100%;
     .searchNavBtn{
       width: 100px;
       height: 32px;
       font-size: 12px;
       line-height: 8px;
     }
    }
  }

  .deleteDataFrame {
    .sureButton {
      text-align: center;
      padding: 10px;
    }
    .dialogTable {
      text-align: center;
    }
    .sureDeleteTitle {
      text-align: center;
      font-size: 14px;
      line-height: 40px;
      margin-top: -30px;
      border-top: solid 1px $greyBorder;
    }
  }

  .cancelButtonClass {
    width: 60px;
    border-radius: 20px !important;
    color: #fff !important;;
    background: #454545 !important;
  }

  .confirmButtonClass {
    width: 60px;
    border-radius: 20px !important;
  }

  .searchKey {
    position: relative;
    .inputSearchKey {
      width: 160px;
    }
    .searchButton {
      width: 110px;
      height: 40px;
      border: solid 1px #ff6419;
      color: #ff6419;
      margin-left: 20px;
    }
    .txtColor {
      color: #fff;
      width: 130px;
      position: absolute;
      right: 0;
    }
  }

  .modelList {
    background: #fff;
    border-radius: 4px;
    margin-top: 15px;
    box-sizing: border-box;
    color: #666;
    margin-bottom: 20px;
    .deleteBar {
      height: 80px;
      line-height: 80px;
      padding-left: 25px;
      position: relative;
      .downloadModel {
        background: url("../../../assets/images/icons/icon_00_nor.png") no-repeat;
        position: absolute;
        height: 30px;
        width: 30px;
        bottom: 25px;
        right: 48px;
        &:hover {
          cursor: pointer;
          background: url("../../../assets/images/icons/icon_00_sct.png") no-repeat;
        }
      }
      .refresh {
        padding: 20px 30px;
        font-size: 14px;
        color: #666;
        background: url('../../../assets/images/icons/reflesh.png') 0 center no-repeat;
        &:hover {
          color: #ff6419;
          background: url('../../../assets/images/icons/reflesh2.png') 0 center no-repeat;
        }
      }
      .delivery{
        padding-left: 25px;
        color: #666;
        background: url('../../../assets/images/icons/delivery.png') 0 center no-repeat;
        &:hover {
          color: #ff6419;
          background: url('../../../assets/images/icons/delivery1.png') 0 center no-repeat;
        }
      }
      .delete {
        padding-left: 25px;
        color: #666;
        background: url('../../../assets/images/icons/delete.png') 0 center no-repeat;
        &:hover {
          color: #ff6419;
          background: url('../../../assets/images/icons/delete2.png') 0 center no-repeat;
        }
      }
      i {
        margin-right: 5px;
      }
    }
  }

  .table-td-right {
    padding: 0;
    margin: 0 auto;
    display: flex;
    flex-wrap: wrap;
    text-align: center;
    width: 230px;
    .operatonBtn {
      /*margin-left: 0!important;*/
      margin: 0 0 5px 5px;
      display: inline-block;
      font-size: 14px;
    }
  }

  .pageFrame {
    height: 110px;
    padding-top: 40px;
    box-sizing: border-box;
    text-align: center;
  }

  .nonImg {
    display: inline-block;
    background: #f2f2f2;
    height: 80px;
    width: 80px;
    line-height: 80px;
    text-align: center;
    font-size: 12px;
    color: #999;
    border: solid 1px #e3e3e3;
  }

</style>
<style>
  .downloadEdit {
    color: #999 !important;
    padding: 5px !important;
    margin-top: 2px !important;
  }

  .el-table .cell {
    padding: 0;
  }
</style>

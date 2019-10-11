<template>
  <div class="goods recom-goods">
    <el-container>
      <el-header height="105px">
        <h1>热销商品管理</h1>
        <div class="header-seek">
            <el-button round type="primary" class="UnifiedsearchBtn"  style="width:120px;" plain size="medium" @click="showProDia">新增热销商品</el-button>
        </div>
      </el-header>
      <el-main>
        <div class="main-option">
          <span class="batch-push" @click="batchDelete()" v-if="isPer('miniapp:goods:exhibition')">批量删除</span>
          <span class="refurbish" @click="refurbish">刷新</span>
        </div>
        <el-table
                v-loading="loading"
                element-loading-text="拼命加载中"
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(255, 255, 255, 0.8)"
                @selection-change="handleSelectionChange"
                :data="goodsTable"
                style="width: 100%"
                :default-sort = "{prop: 'code', order: 'descending'}"
                ref="goodsTableRef">
          <el-table-column type="selection"></el-table-column>
          <el-table-column
                  prop="spuCode"
                  label="编号"
                  >
          </el-table-column>
          <el-table-column
                  prop="pic"
                  label="商品缩略图">
              <template slot-scope="scope">
                  <div v-if="scope.row.picPath">
                      <img :src="BASE_URL.sourceBaseUrl+scope.row.picPath" >
                  </div>
                  <div class="nonImg" v-else> 无缩略图 </div>
              </template>
          </el-table-column>
          <el-table-column
			      prop="spuName"
			      label="名称"  width="200">
			      <template slot-scope="scope">
			      		<el-tooltip :content="scope.row.spuName" placement="bottom" effect="light">
                  <div class="oneline">{{scope.row.spuName}}</div>
                </el-tooltip>
						</template>
			    </el-table-column>
				    
          <el-table-column label="商品分类">
            <template slot-scope="scope">
                <span>{{scope.row.bigType}}</span>
                <span>></span>
                <span>{{scope.row.smallType}}</span>
            </template>
          </el-table-column>
          <el-table-column
                  label="售价">
            <template slot-scope="scope">
            		<div v-if="scope.row.maxPrice>scope.row.minPrice">
            			<span>{{scope.row.minPrice}}</span>
	                <span>~</span>
	                <span>{{scope.row.maxPrice}}</span>
            		</div>
                <span v-else>{{scope.row.minPrice}}</span>
            </template>
          </el-table-column>
           <el-table-column
                  prop="totalInventory"
                  label="总库存">
          </el-table-column>
          <el-table-column
                  prop="isPresell" 
                  label="是否预售" 
                  width="80px">
              <template slot-scope="scope">
                  <div v-if="scope.row.isPresell == '1'" class="isPresell"></div>
                  <div class="noPresell" v-else></div>
              </template>
          </el-table-column>
          <el-table-column
                  prop="getTime"
                  label="获取时间"
                  width="150px">
          </el-table-column>
          <el-table-column label="操作">
              <template slot-scope="scope">
                  <el-button type="text" @click="handleDel(scope.row)">删除</el-button>
              </template>
          </el-table-column>
        </el-table>
        <div class="paging">
          <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :page-sizes="[20, 30, 40, 50]"
                  :page-size="20"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="total">
          </el-pagination>
        </div>
      </el-main>
    </el-container>
    <!--选择商品弹窗-->
		<el-dialog title="选择商品" :visible.sync="dialogTableVisible" class="dia-proAll">
			<div class="pro-dialog" >
				<div class="prokey-wrap">
					<el-input v-model="proName" placeholder="请输入商品名称" class="prokey-ipt"></el-input>
					<el-input v-model="proCode" placeholder="请输入商品编号" class="prokey-ipt"></el-input>
					<button data-v-e1eef154="" type="button" class="el-button searchBtn  el-button--default is-round" style="background:#ff6419;color:white;" @click="getGoodsList"><span>搜索</span></button>
					<button data-v-e1eef154="" type="button" class="el-button searchBtn  el-button--default is-round" @click="resetGoodsList"><span>重置</span></button>
				</div>
			  <el-table
		    :data="proAll"
		    border
		    style="width:100%"
		    height="460"
		    ref="proAllEle"
		    v-loading="proAllLoding"
		    @selection-change="handleDialogSelectionChange"
		    :row-key="getRowId">
		    	<el-table-column
			      type="selection"
			      width="55"
			      :reserve-selection="true">
			    </el-table-column> 
			    <el-table-column
			      prop="spuCode"
			      label="商品编号"
			      width="100px">
			    </el-table-column> 
          <el-table-column
                  prop="pic"
                  label="商品缩略图"
                  width="100px">
              <template slot-scope="scope">
                  <div v-if="scope.row.picPath" style="max-height:60px;">
                      <img :src="BASE_URL.sourceBaseUrl+scope.row.picPath" class="dia-pro-pic" style="margin-right:5px;">
                  </div>
                  <div class="nonImg" v-else> 无缩略图 </div>
              </template>
          </el-table-column>
          
          <el-table-column
            prop="spuName"
            label="名称"
            width="200px">
            <template slot-scope="scope">
					  		<el-tooltip :content="scope.row.spuName" placement="bottom" effect="light">
					        <div class="oneline">{{scope.row.spuName}}</div>
					      </el-tooltip>
						</template>
          </el-table-column>
          
          <el-table-column
                  label="商品分类">
              <template slot-scope="scope">
                  <span>{{scope.row.bigType}}</span>
                  <span>></span>
                  <span>{{scope.row.smallType}}</span>
              </template>
          </el-table-column>
          <el-table-column
                  label="售价">
              <template slot-scope="scope">
                  <div v-if="scope.row.maxPrice>scope.row.minPrice">
	            			<span>{{scope.row.minPrice}}</span>
		                <span>~</span>
		                <span>{{scope.row.maxPrice}}</span>
	            		</div>
	                <span v-else>{{scope.row.minPrice}}</span>
              </template>
          </el-table-column>
          <el-table-column
                  prop="totalInventory"
                  label="库存">
          </el-table-column>
           <el-table-column
                  prop="isPresell" 
                  label="是否预售" 
                  width="80px">
              <template slot-scope="scope">
                  <div v-if="scope.row.isPresell == '1'" class="isPresell"></div>
                  <div class="noPresell" v-else></div>
              </template>
          </el-table-column>
          <el-table-column
                  prop="getTime"
                  label="获取时间"
                  width="150px">
          </el-table-column>
			  </el-table>
			  <div class="pageination">
			    <el-pagination
			      @current-change="handleDiaChange"
			      :page-size="10"
			      layout="total,prev, pager, next, jumper"
			      :current-page="diaPage"
			      :total="proTotal">
			    </el-pagination>
			  </div>
			  <div class="dia-operate">
			  	<el-button slot="label" round size="small" type="primary" class="el-button--primary" @click="addProduct">添加</el-button>
			  	
			  	<el-button slot="label" round size="small" type="text" class="el-button--primary btn-cancel" @click="cancelSelect">取消</el-button>
			  </div>
			</div>
		</el-dialog>
        
    </div>
</template>

<script>
    // import ElButton from '../../../../node_modules/element-ui/packages/button/src/button.vue'
    import qs from 'qs'
    export default {
        name: "goods",
        data() {
            return {
              goodsTable: [],
              total: 0,
              loading: true,
              dialogTableVisible:false,
              allIdArr: [], // 批量处理请求的参数
              recommendListArguments: { // 请求商品列表参数
                  putaway: 1, // 上架情况：1表示已上架，0表示未上架
                  companyId: sessionStorage.getItem('companyID'), // 企业ID
                  pageNo: 1, // 当前页数
                  start: undefined,
                  pageSize: 20, // 每页显示商品数
              },
              proCode:'',
			        proName:'',
			        proAllLoding:true,
			        goodsListArguments: { // 请求商品列表参数
			            typeCode: undefined, // 商品类别code
			            childTypeCode: undefined, //商品小类code
			            putaway: undefined, // 上架情况：1表示已上架，0表示未上架
			            presell: undefined, // 是否预售：1表示预售商品，0表示非预售商品
			            spuName: undefined, // 商品名称
			            spuCode: undefined, // 商品编号
			            companyId: sessionStorage.getItem('companyID'), // 企业ID
			            pageNo: 1, // 当前页数
			            start: undefined,
			            pageSize: 10, // 每页显示商品数
			            bizType:2,
		          },
            checkedPage:1,
		        diaPage:1,
	        	dialogTableVisible:false,
	        	proKey:"",
	        	proTotal:0,
	        	proAll: [],
	        	recGoodsChecked:[],
	            proCheckedList:[],
	        	proShowChecked:[],//显示的选中页的内容
	        	proCheckedCount:0,
	        	showChecked:[],
	        	currDialogChecked:[]//输入框选中的内容,
          }
        },
        created() {
            this.getList();
        },
        methods: {
           getRowId(row){
      			return row.id;
      	   },
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            /*批量选择处理*/
            handleSelectionChange(val) {
              this.recGoodsChecked=val; 
            },
		        handleDel(row) {
		        this.$confirm('确认删除选中的产品吗?', '提示', {
		          cancelButtonClass: 'cancelButtonClass',
		          confirmButtonClass: 'confirmButtonClass',
		          type: 'warning',
		          center: true
		        }).then(() => {
		          this.API.deleteGoodsRecommendList({
		            goodsIds:row.id,
		            companyId:sessionStorage.getItem('companyID')
		           }).then((res) => {
		            this.$message.success('删除成功')
		            this.getList();
		          })
		        })
		      },
		      batchDelete () { // 批量删除
			      let ids=[]
			      this.recGoodsChecked.map(function (value) {
						  ids.push(value.id)
					  });
		        if (ids.length > 0) {
		          this.$confirm('确认批量删除选中的推荐产品吗?', '提示', {
		            cancelButtonClass: 'cancelButtonClass',
		            confirmButtonClass: 'confirmButtonClass',
		            type: 'warning',
		            center: true
		          }).then(() => {
		            this.API.deleteGoodsRecommendList({
		              goodsIds: ids.join(','),
		              companyId:sessionStorage.getItem('companyID')
		            }).then(() => {
		              this.$message.success('删除成功')
		              this.getList();
		            })
		          })
		        } else {
		          this.$message.error('请选择一条或多条数据再删除')
		        }
		      },
			    addProduct(){
		      	let ids = []
		        this.currDialogChecked.map(function (value) {
		          ids.push(value.id)
		        });
		        if (ids.length > 0) {
                  this.API.addGoodsRecommendList({
		            goodsIds:ids.join(','),
		            companyId:sessionStorage.getItem('companyID'),
		            shopId:0
		           }).then((res) => {
		            this.dialogTableVisible = false;
		            this.$refs.proAllEle.clearSelection();
		            this.$message.success('成功添加')
		            this.getList();
		            this.getGoodsList();
		          });
		        } else {
		          this.$message.error('请选择一条或多条数据再删除')
		        }
			      },
			      cancelSelect(){
			        this.dialogTableVisible = false;
			      },
            /*刷新*/
            refurbish() {
                this.recommendListArguments = {
                    companyId: sessionStorage.getItem('companyID'), // 企业ID
                    page: 1, // 当前页数
                    start: undefined,
                    limit: 20, // 每页显示商品数
                };
                this.getList();
            },
            showProDia(){
					  	this.dialogTableVisible = true;
                          var checkedList = this.proCheckedList;
                          this.getGoodsList();
					  },
            /*分页，每页显示条数*/
            handleSizeChange(val) {
                this.recommendListArguments.pageSize = val;
                this.getList();
            },
            /*点击翻页*/
            handleCurrentChange(val) {
                this.recommendListArguments.pageNo = val;
                this.getList();
            },
            handleDiaChange(val){
              this.goodsListArguments.pageNo = val;
		      		this.getGoodsList();
            },
            /*请求商品推荐列表数据*/
            getList() {
                this.API.getGoodsRecommendList(this.recommendListArguments).then((res) => {
                    this.goodsTable = res.data.list;
                    this.total = res.data.count;
                    this.loading = false;
                });
            },
            handleDialogSelectionChange(val){
              this.currDialogChecked = val;
            },
            getGoodsList(){
                    if (this.proName.replace(/\s/g, '') == '') {
                      this.goodsListArguments.spuName = undefined;
                    } else {
                      this.goodsListArguments.spuName = this.proName.replace(/\s/g, '');
                    }
                    if (this.proCode.replace(/\s/g, '') == '') {
                      this.goodsListArguments.spuCode = undefined;
                    } else {
                      this.goodsListArguments.spuCode = this.proCode.replace(/\s/g, '');
                    }
		        	//this.goodsListArguments.spuName= this.proName;
		        	//this.goodsListArguments.spuCode= this.proCode;
		        	this.proAllLoding = true;
		        	this.API.getAvailableGoodsList(this.goodsListArguments).then((res) => {
		        		this.proAllLoding = false;
		        		if(res.code==200){
		        		
				             this.proAll = res.data.list;
				             this.proTotal = res.data.count;
		        		}
	          	});
          	},
          	resetGoodsList(){
          		this.proName = '';
          		this.proCode = '';
          		// this.goodsListArguments = { // 请求商品列表参数
			    //         typeCode: undefined, // 商品类别code
			    //         childTypeCode: undefined, //商品小类code
			    //         putaway: undefined, // 上架情况：1表示已上架，0表示未上架
			    //         presell: undefined, // 是否预售：1表示预售商品，0表示非预售商品
			    //         spuName: undefined, // 商品名称
			    //         spuCode: undefined, // 商品编号
			    //         companyId: sessionStorage.getItem('companyID'), // 企业ID
			    //         pageNo: 1, // 当前页数
			    //         start: undefined,
			    //         pageSize: 10, // 每页显示商品数
                //         businessType:2
		        //   }
          		this.getGoodsList();
          	}
        },
        mounted(){
	      this.getGoodsList();
	    }
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/css/mixin";
    .goods{
        .el-header{
            background-color: #fff;
            padding: 0;
            h1{
                height: 58px;
                font-size: 18px;
                line-height: 58px;
                padding-left: 21px;
                border-bottom:1px solid $greyBorder;
            }
            .header-option{
                /*height: 56px;*/
                padding: 20px 0 0 20px;
                border-bottom:1px solid $greyBorder;
                .el-cascader{
                    margin-right: 18px;
                    width: 200px;
                    height: 36px;
                }
                .el-select{
                    margin-right: 18px;
                    width: 200px;
                    height: 36px;
                }
                .el-input{
                    display: inline-block;
                    margin-right: 18px;
                    margin-bottom: 20px;
                    width: 200px;
                    height: 36px;
                }
            }
            .header-seek{
                height: 46px;
                line-height: 46px;
                padding-left: 20px;
                font-size: 14px;
                .el-button{
                    border: solid 1px #dddddd;
                    font-weight: bold;
                    width: 100px;
                    height: 36px;
                }
            }
        }
        .el-main{
            background-color: #fff;
            margin-top: 20px;
            .main-option{
                height: 21px;
                margin-bottom: 20px;
                span{
                    display: inline-block;
                    padding: 0 0 0 25px;
                    height: 21px;
                    line-height: 20px;
                    margin-right: 20px;
                    color: #666666;
                    cursor:pointer;
                }
                .batch-push{
                    background: url("../../../assets/images/icons/push.png") no-repeat;
                    &:hover{
                        color: #ff6419;
                        background: url("../../../assets/images/icons/push2.png") no-repeat;
                    }
                }
                .batch-pull{
                    background: url("../../../assets/images/icons/pull.png") no-repeat;
                    &:hover{
                        color: #ff6419;
                        background: url("../../../assets/images/icons/pull2.png") no-repeat;
                    }
                }
                .refurbish{
                    background: url("../../../assets/images/icons/reflesh.png") no-repeat;
                    &:hover{
                        color: #ff6419;
                        background: url("../../../assets/images/icons/reflesh2.png") no-repeat;
                    }
                }
            }
            .isPresell{
                display: inline-block;
                width: 16px;
                height: 16px;
                background: url("../../../assets/images/icons/presell.png");
            }
            .noPresell{
                display: inline-block;
                width: 16px;
                height: 16px;
                background: url("../../../assets/images/icons/nopresell.png");
            }
            img{
                display: block;
                margin: 0 auto;
                width:90px;
                height: 90px;
            }
            .nonImg{
                width:90px;
                height: 90px;
                line-height: 90px;
                margin: 0 auto;
                text-align: center;
                background-color: #f5f5f5;
            }
            .paging{
                margin-top: 20px;
                text-align: center;
            }
        }
    }
    .product-info{
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		text-align: left;
		img{
			vertical-align: middle;
		}
	}
	.pro-dialog{
		.pageination{
			text-align: center;
			padding: 30px 0 6px;
		}
		.prokey-wrap{
			padding: 0 0 20px 0;
			>.prokey-ipt{
				width: 150px;
				margin-right: 20px;
			}
			>.prokey-sear{
				color: $btnOrange;
			}
		}
		.dia-operate{
			text-align: right;
			border-top: 1px solid #ccc;
			padding: 24px;
			margin-top: 20px;
			>.btn-cancel{
				background-color: #FCDCCF;
		    color: $btnOrange;
		    border: 1px solid #FCDCCF;
			}
		}
		.isPresell{
                display: inline-block;
                width: 16px;
                height: 16px;
                background: url("../../../assets/images/icons/presell.png");
            }
            .noPresell{
                display: inline-block;
                width: 16px;
                height: 16px;
                background: url("../../../assets/images/icons/nopresell.png");
            }
		.dia-pro-pic{
			width: 70px;
			height: 70px;
		}
	}
	.oneline{
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
</style>
<style>
	.dia-proAll .el-dialog{
		width: 53%;
	}
	.recom-goods .el-dialog__body{
		padding-bottom: 0;
	}
</style>
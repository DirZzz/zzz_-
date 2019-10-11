<template>
    <div class="goods">
        <el-container>
            <el-header height="210px">
                <h1>商品管理</h1>
                <div class="header-option">
                    <el-cascader
                            ref="goodsValue"
                            clearable
                            placeholder="商品分类"
                            size="medium"
                            v-model="cascaderValue"
                            :options="goodsTypeList"
                            @change="handleItemChange"
                            :props="goodsTypeProps"
                    ></el-cascader>
                    <el-select v-model="seekGoodsPutawayCase" placeholder="上架情况" size="medium" @change="seekGoodsPutaway" clearable>
                        <el-option
                                v-for="(item,index) in goodsPutaway"
                                :key="index"
                                :label="item.label" 
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <!--<el-select v-model="seekPresellCase" placeholder="是否预售" size="medium" @change="seekGoodsPresell" clearable>-->
                        <!--<el-option-->
                                <!--v-for="(item,index) in goodsPresell"-->
                                <!--:key="index"-->
                                <!--:label="item.label"-->
                                <!--:value="item.value">-->
                        <!--</el-option>-->
                    <!--</el-select>-->
                    <el-input v-model="seekGoodsNameCase" placeholder="商品名称" size="medium" @change="seekGoodsName" clearable></el-input>
                    <el-input v-model="seekGoodsCodeCase"
                              placeholder="商品编号"
                              size="medium" 
                              @change="seekGoodsCode"
                              clearable></el-input>

                              
                               <el-input v-model="productModelNumber"
                              placeholder="商品型号"
                              size="medium" 
                              @change="seekModelNumber"
                              clearable></el-input>
                    <el-select v-model="goodsListArguments.hasModelOrMaterial" placeholder="是否有模型/贴图" size="medium" @change="getGoodsList" clearable>
                        <el-option
                                v-for="(item,index) in moduleList"
                                :key="index"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </div>
                <div class="header-seek">
                    <el-button round type="primary" class="UnifiedsearchBtn" size="medium" @click="seekGoods">搜索</el-button>
                    <el-button round size="medium" class="reset UnifiedsearchBtn" @click="resetGoods">重置</el-button>
                </div>
            </el-header>
            <el-main>
                <div class="main-option">
                    <span class="batch-push" @click=" batchUpPutaway('上架')" v-if="isPer('miniapp:goods:exhibition')">批量上架</span>
                    <span class="batch-pull" @click=" batchUpPutaway('下架')" v-if="isPer('miniapp:goods:exhibition')">批量下架</span>
                    <span class="export"  @click="exportExcel" >导出Excel</span>
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
                        ref="goodsTableRef"
                        >
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column
                           prop="spuCode"
                            label="编号"
                            >
                              <template slot-scope="scope">
                                   <div>
                                     {{scope.row.spuCode}}
                                   </div>
                              </template>
                      </el-table-column>
                    <el-table-column
                            prop="pic"
                            label="商品">
                        <template slot-scope="scope">
                            <div class="picbox" v-if="scope.row.pic">
                                <img :src="BASE_URL.sourceBaseUrl+scope.row.pic">
                                <span class="temai" v-if="scope.row.isSpecialOffer"></span>
                                
                            </div>
                            <div class="nonImg" v-else><span class="temai" v-if="scope.row.isSpecialOffer"></span> 无缩略图</div>
                           
                        </template>
                       
                    </el-table-column>
                   <el-table-column
                            label="名称"
                            >
                             <template  slot-scope="scope">
                                 <div>
                                     <p>{{scope.row.spuName}}</p>
                                     <span class="yushou" v-if="scope.row.isPresell">新品</span>
                                 </div>
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
                            label="优惠价格">
                        <template slot-scope="scope">
                            <span v-if="scope.row.minPrice == scope.row.maxPrice">{{scope.row.minPrice}}</span>
                            <div v-else>
                                <span>{{scope.row.minPrice}}</span>
                                <span>~</span>
                                <span>{{scope.row.maxPrice}}</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="totalInventory"
                            label="库存">
                    </el-table-column>
                    <el-table-column
                            prop="modelOrMaterial"
                            label="是否有模型/贴图">
                    </el-table-column>
                    <!--<el-table-column-->
                            <!--prop="isPresell"-->
                            <!--label="是否预售"-->
                            <!--width="80">-->
                        <!--<template slot-scope="scope">-->
                            <!--<div v-if="scope.row.isPresell == '是'" class="isPresell"></div>-->
                            <!--&lt;!&ndash;<div class="noPresell" v-else></div>&ndash;&gt;-->
                        <!--</template>-->
                    <!--</el-table-column>-->
                    <!-- <el-table-column
                            prop="getTime"
                            label="获取时间"
                            sortable>
                    </el-table-column> -->
                    <el-table-column
                            prop="isPutaway"
                            label="上架情况">
                        <template slot-scope="scope">
                            <el-switch class="switchStyle"
                                       v-if="isPer('miniapp:goods:exhibition')"
                                       @change="downUpPutaway(scope)"
                                       v-model="scope.row.isPutaway"
                                       active-color="#ff6419"
                                       inactive-icon-class="inactive-icon-class"
                                       active-icon-class="active-icon-class"
                                       :active-value="1"
                                       inactive-color="#ddd"
                                       :inactive-value="0">
                            </el-switch>
                            <!--<el-switch class="switchStyle"-->
                                       <!--@change="downUpPutaway(scope)"-->
                                       <!--v-model="scope.row.isPutaway"-->
                                       <!--active-color="#ff6419"-->
                                       <!--inactive-icon-class="inactive-icon-class"-->
                                       <!--active-icon-class="active-icon-class"-->
                                       <!--:active-value="1"-->
                                       <!--inactive-color="#ddd"-->
                                       <!--:inactive-value="0"-->
                            <!--&gt;-->
                            <!--</el-switch>-->
                            <p>
                                {{scope.row.isPutaway == 1 ? scope.row.statusSwitchTxt = '上架' : scope.row.statusSwitchTxt = '未上架'}}
                            </p>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <!--<el-button size="small" @click="goGoodsEdit(scope)">编辑</el-button>-->
                            <el-button type="text" @click="goGoodsEdit(scope)" v-if="isPer('miniapp:goods:edit')">编辑</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="paging">
                    <el-pagination
                             :current-page.sync="pagenum"
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
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        name: "goods",
        data() {
            return {
                pagenum:1,
                isAllPutaway: false,
                goodsPutaway: [
                    {label: '上架', value: 1},
                    {label: '未上架', value: 0}
                ],
                isPutaway: '',
                goodsPresell: [
                    {label: '是', value: 1},
                    {label: '否', value: 0}
                ],
                goodsTypeList: [],
                goodsTypeProps: {
                    label: 'typeName',
                    value: 'typeCode',
                    children: 'childType'
                },
                seekGoodsNameCase: '',
                seekGoodsCodeCase: '',
                productModelNumber:'',
                seekGoodsClassify: '',
                seekGoodsPutawayCase: '',
                seekPresellCase: '',
                goodsTable: [],
                total: 0,
                page: 1,
                loading: true,
                idArr: [], // 上下架请求的参数
                allIdArr: [], // 批量处理请求的参数
                goodsListArguments: { // 请求商品列表参数
                    typeCode: undefined, // 商品类别code
                    childTypeCode: undefined, //商品小类code
                    putaway: undefined, // 上架情况：1表示已上架，0表示未上架
                    presell: undefined, // 是否预售：1表示预售商品，0表示非预售商品
                    name: undefined, // 商品名称
                    code: undefined, // 商品编号
                    productModelNumber:undefined,  //商品型号
                    hasModelOrMaterial:undefined,
                    companyId: sessionStorage.getItem('companyID'), // 企业ID
                    page: 1, // 当前页数
                    start: undefined,
                    limit: 20, // 每页显示商品数
                },
                moduleList:[
                    {label: '是', value: 1},
                    {label: '否', value: 0}
                ],
                cascaderValue:[]
            }
        },
        activated() {
            this.getGoodsList();
            this.getGoodsType();
        },
        methods: {
            /*权限设置*/
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            /*批量选择处理*/
            handleSelectionChange(val) {
                this.allIdArr = [];
                for (let i = 0; i < val.length; i++) {
                    this.allIdArr.push(val[i].id);
                }
            },
            /*批量上下架
            * type: 用于判断批量上架或者下架
            * */
            batchUpPutaway(type) {
                this.isAllPutaway = true;
                if (this.allIdArr.length == 0) {
                    this.$message({
                        showClose: true,
                        message: '请至少选择一条数据',
                        type: 'warning'
                    });
                    return;
                }
                if (type == '上架') {
                    this.$confirm('此操作将上架已选中的商品, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this. postDownPutaway(this.allIdArr);
                        this.$refs.goodsTableRef.clearSelection();
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消上架'
                        });
                    });
                }
                if (type == '下架') {
                    this.$confirm('此操作将下架已选中的商品, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.postUpPutaway(this.allIdArr);
                        this.$refs.goodsTableRef.clearSelection();
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消下架'
                        });
                    });
                }
            },
            /*上下架*/
            downUpPutaway(scope) {
                this.isAllPutaway = false;
                this.isPutaway = scope.row.isPutaway;
                this.idArr = [];
                this.idArr.push(scope.row.id);
                if (scope.row.isPutaway == 0) {
                    this.postUpPutaway(this.idArr);
                }
                if (scope.row.isPutaway == 1) {
                    this. postDownPutaway(this.idArr);
                }
            },
            /*下架请求
            * idArr:请求参数
            * */
            postDownPutaway(idArr) {
                this.API.postDownPutaway({
                    companyId: sessionStorage.getItem('companyID'),
                    ids: idArr
                }).then((res) => {
                    if (res.code == 200) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        });
                        if (this.isAllPutaway) {
                            this.refurbish();
                        }
                    }
                    if (res.code == 500) {
                        console.log(1321321)
                        // this.$refs.goodsTableRef.clearSelection();
                    }
                });
            },
            /*上架请求 JSON.stringify(this.idArr)*/
            postUpPutaway(idArr) {
                this.API.postUpPutaway({
                    companyId: sessionStorage.getItem('companyID'),
                    ids: idArr
                }).then((res) => {
                    console.log(res.code);
                    if (res.code == 200) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        });
                        if (this.isAllPutaway) {
                            this.refurbish();
                        }
                    }
                    // if (res.code == 500) {
                    //     console.log(1321321);
                    //     // isPutaway = 0;
                    //     // this.$refs.goodsTableRef.clearSelection();
                    // }
                });
            },
            /*刷新*/
            refurbish() {
                 this.getGoodsList();
                // this.goodsListArguments = { // 请求商品列表参数
                //     typeCode: undefined, // 商品类别code
                //     childTypeCode: undefined, //商品小类code
                //     putaway: undefined, // 上架情况：1表示已上架，0表示未上架
                //     presell: undefined, // 是否预售：1表示预售商品，0表示非预售商品
                //     name: undefined, // 商品名称
                //     code: undefined, // 商品编号
                //     companyId: sessionStorage.getItem('companyID'), // 企业ID
                //     page: this.page, // 当前页数
                //     start: undefined,
                //     limit: 20, // 每页显示商品数
                // };
                // this.getGoodsList();
            },
            exportExcel() {//导出excel
                if (this.allIdArr.length == 0) {
                    this.$message({
                        showClose: true,
                        message: '请至少选择一条数据',
                        type: 'warning'
                    });
                    return;
                }else {
                    let prarms={
                        companyId: sessionStorage.getItem('companyID'),
                        ids: this.allIdArr.join(",")
                    }
                    this.API.exportListExcelByGoods(prarms).then(res => {
                        let blob = new Blob([res], {
                            type: "application/vnd.ms-excel;charset=utf-8"
                        });
                        console.log(blob,'blob')
                        let url = URL.createObjectURL(blob);
                        console.log(url,'url')
                        let link = document.createElement("a");
                        link.style.display = "none";
                        link.href = url;
                        link.setAttribute("download", "商品信息导出.xls");
                        document.body.appendChild(link);
                        link.click();
                    });
                }
            },
            /*点击搜索*/
            seekGoods() {
                this.goodsListArguments.page = 1;
                this.getGoodsList();
                this.pagenum = 1;
               
            },
            /*点击重置*/
            resetGoods() {
                this.cascaderValue=[];
                this.goodsListArguments.childTypeCode = undefined;
                this.seekGoodsPutawayCase = '';
                this.goodsListArguments.putaway = undefined;
                this.seekPresellCase = '';
                this.goodsListArguments.presell = undefined;
                this.seekGoodsCodeCase = '';
                this.goodsListArguments.code = undefined;
                this.seekGoodsNameCase = '';
                this.goodsListArguments.name = undefined;
                this.productModelNumber ='';
                this.goodsListArguments.productModelNumber = undefined;
                this.goodsListArguments.hasModelOrMaterial=undefined;
                this.getGoodsList();
            },
            //商品型号
            seekModelNumber(){
                  if (this.productModelNumber.replace(/\s/g, '') == '') {
                    this.goodsListArguments.productModelNumber = undefined;
                } else {
                    this.goodsListArguments.productModelNumber = this.productModelNumber.replace(/\s/g, '');
                }
               // this.goodsListArguments.page = 1;
               // this.getGoodsList();
            },
            /*商品编码*/
            
            seekGoodsCode() {
                if (this.seekGoodsCodeCase.replace(/\s/g, '') == '') {
                    this.goodsListArguments.code = undefined;
                } else {
                    this.goodsListArguments.code = this.seekGoodsCodeCase.replace(/\s/g, '');
                }
              //  this.goodsListArguments.page = 1;
              //  this.getGoodsList();
            },
            /*商品名称*/
            seekGoodsName() {
                if (this.seekGoodsNameCase.replace(/\s/g, '') == '') {
                    this.goodsListArguments.name = undefined;
                } else {
                    this.goodsListArguments.name = this.seekGoodsNameCase.replace(/(^\s*)|(\s*$)/g, "");
                }
               // this.goodsListArguments.page = 1;
               // this.getGoodsList();
            },
            /*选择是否预售获取value*/
            seekGoodsPresell(val) {
                this.goodsListArguments.presell = val;
                //this.getGoodsList();
            },
            /*选择上下架获取value*/
            seekGoodsPutaway(val) {
                this.goodsListArguments.putaway = val;
                this.getGoodsList()
               // this.goodsListArguments.page = 1;
                //this.getGoodsList();
            },
            /*选择商品分类获取分类的code*/
            handleItemChange(val) {
                this.goodsListArguments.childTypeCode =  val[val.length-1];
                this.getGoodsList()
                // this.goodsListArguments.page = 1;
                //this.getGoodsList();
            },
            /*分页，每页显示条数*/
            handleSizeChange(val) {
                this.goodsListArguments.limit = val;
                this.getGoodsList();
            },
            /*点击翻页*/
            handleCurrentChange(val) {
                console.log(val);
                this.page = val;
                this.goodsListArguments.page = val;
                this.getGoodsList();
            },
            goGoodsEdit(scope) {
                sessionStorage.setItem('goodsId', scope.row.id);
                this.$router.push('/goodsEdit');
            },
            /*请求商品列表数据*/
            getGoodsList() {
                this.API.getGoodsList(this.goodsListArguments).then((res) => {
                    this.goodsTable = res.list;
                    this.total = res.total;
                    console.log(res)
                    this.loading = false;
                });
            },
            /*请求商品类型*/
            getGoodsType() {
                this.API.getGoodsType({companyId: sessionStorage.getItem('companyID')}).then((res) => {
                    this.goodsTypeList = res.list;
                })
            },
        },
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
                height: 76px;
                line-height: 76px;
                padding-left: 20px;
                font-size: 12px;
                text-align: center;
                .el-button{
                    width: 100px;
                    height: 32px;
                }
                .reset{
                    background: #f5f5f5;
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
                .export{
                    background: url("../../../assets/images/icons/allot.png") no-repeat;
                    &:hover{
                        color: #ff6419;
                        background: url("../../../assets/images/icons/allot2.png") no-repeat;
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
            .picbox{
                position: relative;
                 width:90px;
                height: 90px;
                margin: 0 auto;
                 img{
                    display: block;
                    margin: 0 auto;
                    width:90px;
                    height: 90px;
                     object-fit: contain;
                    }

            }
           .yushou{
               color: #ff6419;
               border: 1px solid #ff6419;
               width: 38px;
               height: 18px;
               line-height: 18px;
               text-align: center;
               display: inline-block;
              border-radius: 9px;
              font-size: 12px;
           }
            .nonImg{
                width:90px;
                height: 90px;
                line-height: 90px;
                margin: 0 auto;
                text-align: center;
                background-color: #f5f5f5;
                position: relative;
            }
            .temai{
                width: 18px;
                height: 32px;
                display: inline-block;
                position: absolute;
                top: 0;
                left: 6px;
                background: url('../../../assets/temai.png') no-repeat;
                background-size:  18px 32px;
            }
            .paging{
                margin-top: 20px;
                text-align: center;
            }
        }
    }
</style>
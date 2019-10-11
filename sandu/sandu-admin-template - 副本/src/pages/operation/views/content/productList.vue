<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>商品管理</span>
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">
                <el-input v-model="goodsListArguments.companyName" placeholder="请输入所属企业"></el-input>
                <el-input v-model="goodsListArguments.name" placeholder="请输入商品名称"></el-input>
                <el-input v-model="goodsListArguments.code" placeholder="请输入商品编号"></el-input>
                <el-input v-model="goodsListArguments.productModelNumber" placeholder="请输入商品型号"></el-input>
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

            </el-row>
        </div>
        <div class="btnFrame clearfix">
            <p>
                <el-button round type="primary" size="small" @click="search">搜索</el-button>
                <el-button round size="small" @click="reset">重置</el-button>
            </p>
        </div>
        <div class="tableList">
            <el-table
                    :header-cell-style="rowStyle"
                    :data="goodsTable"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%"
                    align="center"
            >
                <el-table-column label="序号" type="index" align="center"></el-table-column>
                <el-table-column label="所属企业" header-align="center" align="center">
                    <template slot-scope="scope">
                        <p>{{scope.row.companyName}}</p>
                    </template>
                </el-table-column>
                <el-table-column label="编号" header-align="center" align="center">
                    <template slot-scope="scope">
                        <p>{{scope.row.spuCode}}</p>
                    </template>
                </el-table-column>
                <el-table-column label="商品缩略图" header-align="center" align="center" width="120">
                    <template slot-scope="scope">
                        <img :src="BASE_URL.sourceBaseUrl+scope.row.pic" alt="" style="object-fit: contain;height: 100px">
                    </template>
                </el-table-column>

                <el-table-column label="商品名称" header-align="center" align="center">
                    <template slot-scope="scope">
                        <p>{{scope.row.spuName}}</p>
                    </template>
                </el-table-column>

                <el-table-column label="商品分类" header-align="center" align="center">
                    <template slot-scope="scope">
                        <p>{{scope.row.bigType}} > {{scope.row.smallType}}</p>
                    </template>
                </el-table-column>
                <el-table-column label="商品价格" header-align="center" align="center">
                    <template slot-scope="scope">
                        <p>{{scope.row.maxPrice}}</p>
                    </template>
                </el-table-column>
                <el-table-column label="排序" header-align="center" align="center">
                    <template slot-scope="scope">
                        <span>{{scope.row.sort}}</span>
                        <i class="el-icon-edit-outline" @click="setSort(scope.row)"></i>
                    </template>
                </el-table-column>

                <el-table-column label="操作" header-align="center" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" @click="$router.push(`/content/productDetail/${scope.row.id}`)">详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="goodsListArguments.page"
                        :page-sizes="[5,10, 50, 100, 200,500]"
                        :page-size="goodsListArguments.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total"
                ></el-pagination>
            </div>
        </div>
        <el-dialog title="排序" :visible.sync="sortDialog" width="30%">
            <div class="plan-message">
                <span>排序：</span>
                <el-input
                        v-model="sortNum"
                        maxlength="50"
                        size="small"
                        placeholder="请输入内容"
                ></el-input>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="sortDialog = false">取 消</el-button>
                <el-button type="primary" @click="confirm">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "productList",
        data() {
            return {
                loading:true,
                goodsTable: [],
                cascaderValue:'',
                sortDialog:false,
                sortNum:'',
                total:0,
                goodsTypeList: [],
                goodsTypeProps: {
                    label: 'name',
                    value: 'keyCode',
                    children: 'children'
                },
                goodsListArguments: { // 请求商品列表参数
                    typeCode: undefined, // 商品类别code
                    childTypeCode: undefined, //商品小类code
                    putaway: 1, // 上架情况：1表示已上架，0表示未上架
                    presell: undefined, // 是否预售：1表示预售商品，0表示非预售商品
                    name: undefined, // 商品名称
                    code: undefined, // 商品编号
                    productModelNumber: undefined,  //商品型号
                    hasModelOrMaterial: undefined,
                    companyId: undefined, // 企业ID
                    companyName: undefined, // 企业ID
                    page: 1, // 当前页数
                    start: undefined,
                    limit: 10, // 每页显示商品数
                    selectMode:1
                },
                currentSpuId:''

            };
        },
        created() {
            this.searchGoods();
            this.getGoodsType();
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
            /*请求商品类型*/
            getGoodsType() {
                this.API.productCategory().then((res) => {
                    this.goodsTypeList = res.data[0].children;
                })
            },
            setSort(item){
                this.sortDialog=true;
                this.currentSpuId=item.id;
                this.sortNum=item.sort;
            },
            handleItemChange(val) {
                if(val){
                    this.goodsListArguments.childTypeCode = val[val.length - 1];
                }else {
                    this.goodsListArguments.childTypeCode=''
                }
               this.search();
            },
            reset(){
                this.goodsListArguments={
                    page: 1, // 当前页数
                    putaway: 1,
                    selectMode:1,
                    limit: this.goodsListArguments.limit, // 每页显示商品数
                }
                this.cascaderValue='';
                this.searchGoods()
            },
            search(){
                this.goodsListArguments.page=1;
                this.searchGoods();
            },
            confirm(){
                this.API.goodSpuSortEdit(this.currentSpuId, this.sortNum).then(res => {
                    if (res && res.success) {
                        this.$message.success('设置成功');
                        this.searchGoods();
                        this.sortDialog = false
                    }else {
                        this.$message.error('设置失败');
                    }

                })
            },
            // 分页模块
            handleSizeChange: function(size) {
                this.goodsListArguments.page=1;
                this.goodsListArguments.limit=size;
                this.searchGoods();
            },
            handleCurrentChange: function(currentPage) {
                this.goodsListArguments.page=currentPage;
                this.searchGoods();
            },
            searchGoods: function () {
                this.loading=true;
                /*请求商品列表数据*/
                this.API.getGoodsList(this.goodsListArguments).then((res) => {
                    if (!res) {
                        return
                    }
                    this.goodsTable = res.list;
                    this.total = res.total;
                    this.loading = false;
                });
            }
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
    .plan-message{
        display: flex;
        span{
            display: inline-block;
            width: 80px;
            line-height: 32px;
        }
    }
</style>

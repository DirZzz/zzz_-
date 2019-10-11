<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>水刀管理</span>
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">
                <el-input placeholder="请输入水刀编码" clearable v-model="templateCode"> </el-input>
                <el-input placeholder="请输入水刀名称" clearable v-model="templateName"> </el-input>
                <el-select v-model="brandId" @change="getListData" clearable placeholder="请选择水刀品牌">
                    <el-option v-for="item in brandList"
                               :key="item.id"
                               :label="item.brandName"
                               :value="item.id"
                    > </el-option>
                </el-select>
                <el-select v-model="templateStatus" @change="getListData" clearable placeholder="请选择水刀状态">
                    <el-option v-for="item in templateStatusList"
                               :key="item.value"
                               :label="item.name"
                               :value="item.value"
                    > </el-option>
                </el-select>
                <el-select v-model="templateShapeValue" @change="getListData" clearable placeholder="请选择水刀形状">
                    <el-option v-for="item in templateShapeList"
                               :key="item.value"
                               :label="item.name"
                               :value="item.value"
                    > </el-option>
                </el-select>
            </el-row>
        </div>
        <div class="btnFrame clearfix">
            <p>
                <el-button round type="primary" class="UnifiedsearchBtn" @click="getListData">搜索</el-button>
                <el-button round class="searchBtn UnifiedsearchBtn" @click="resetForm">重置</el-button>
            </p>
            <p>
                <el-button round type="primary" class="UnifiedsearchBtn" @click="toAdd">新增</el-button>
            </p>
        </div>

        <div class="modelList">
            <div class="deleteBar">
                <el-button type="text" class="share" @click="upperandlowerstatusBachU">批量上架</el-button>
                <el-button type="text" class="download" @click="upperandlowerstatusBachL">批量下架</el-button>
                <el-button type="text" class="delete" @click="deleteBach">批量删除</el-button>
                <el-button size="small" type="text" @click="getListData" class="refresh">刷新</el-button>
            </div>
            <div class="tableList">
                <el-table
                        @selection-change="handleSelectionChange"
                        :data="tableData"
                        v-loading="loading"
                        element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(255, 255, 255, 0.8)"
                        style="width: 100%">

                    <el-table-column type="selection" width="55"></el-table-column>

                    <el-table-column label="水刀图片">
                        <template slot-scope="scope">
                            <div v-if="scope.row.picPath">
                                <img style="cursor:pointer;" @click="ImgView(BASE_URL.sourceBaseUrl+scope.row.picPath)" :src="BASE_URL.sourceBaseUrl+scope.row.picPath" width="90" height="90">
                            </div>
                            <div class="nonImg" v-else> 暂无图片</div>
                        </template>
                    </el-table-column>

                    <el-table-column label="水刀编码" prop="templateCode">
                        <template slot-scope="scope">
                            <span style="color: #FF6419" v-if="scope.row.templateCode">{{ scope.row.templateCode }}</span>
                            <span v-else>———</span>
                        </template>
                    </el-table-column>

                    <el-table-column label="水刀名称">
                        <template slot-scope="scope">{{ scope.row.templateName }}</template>
                    </el-table-column>

                    <el-table-column label="水刀品牌" prop="brandName">
                        <template slot-scope="scope">
                            <span v-if="scope.row.brandName">{{ scope.row.brandName }}</span>
                            <span v-else>———</span>
                        </template>
                    </el-table-column>

                    <el-table-column label="水刀长度">
                        <template slot-scope="scope">{{ scope.row.templateLength }}</template>
                    </el-table-column>


                    <el-table-column label="水刀宽度">
                        <template slot-scope="scope">{{ scope.row.templateWidth }}</template>
                    </el-table-column>

                    <el-table-column label="水刀状态">
                        <template slot-scope="scope">
                            {{ scope.row.templateStatus == 0 ? '未上架' : '已上架'}}
                        </template>
                    </el-table-column>

                    <el-table-column label="水刀形状">
                        <template slot-scope="scope">{{ scope.row.shapeName }}</template>
                    </el-table-column>

                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button type="text" v-if="scope.row.templateStatus == 0" @click="upperandlowerstatus(scope.row.basewaterjetId,1)">上架</el-button>
                            <el-button type="text" v-else-if="scope.row.templateStatus == 1" @click="upperandlowerstatus(scope.row.basewaterjetId,0)">下架</el-button>
                            <el-button type="text" v-if="scope.row.templateStatus == 0" @click="toEdit(scope.row.basewaterjetId)">编辑</el-button>
                            <el-breadcrumb/>
                            <el-button type="text" @click="toDetail(scope.row.basewaterjetId)">详情</el-button>
                            <el-button type="text" @click="toDel(scope.row.basewaterjetId)">删除</el-button>
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
                            :total="total">
                    </el-pagination>
                </div>
            </div>
        </div>
        <img-pre-view ref="sonParams"></img-pre-view>
    </div>
</template>

<script>
    import ImgPreView from '@/components/Img-preview'
    import ElBreadcrumb from "../../../../../../node_modules/element-ui/packages/breadcrumb/src/breadcrumb.vue";

    export default {
        components: {ElBreadcrumb},
        name: "coustomer",
        data() {
            return {
                total: 0, // 总页数
                templateCode:'',
                templateName:'',
                templateStatus: '',
                brandId:'',
                brandList:[],
                templateStatusList: [
                    {value: 0, name: '未上架'},
                    {value: 1, name: '已上架'}
                ],
                templateShapeValue:'',
                templateShapeList:[],
                loading: true,
                streetShow: true,
                tableData: [],
                listTableData: [],
                multipleSelection:[],
                query: {
                    page: 0,
                    limit: 10,
                }
            }
        },
        created() {
            //this.getListData();
            this.getBrandLIst();
            this.getTemplateShapes();
        },
        activated(){
            this.getListData();
        },
        methods: {
            // 列表
            getListData(){

                let queryCondition = Object.assign({}, this.query, {
                    templateCode: this.templateCode,
                    templateName: this.templateName,
                    templateStatus: this.templateStatus,
                    brandId: this.brandId,
                    templateShapeValue: this.templateShapeValue
                });

                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }

                this.API.waterKnifeList(queryCondition).then((res) => {
                    this.loading = false;
                    this.tableData = res.list;
                    this.total = res.total;
                })

            },
            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size;
                this.getListData()
            },
            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage;
                this.loading = true;
                this.getListData()
            },
            resetForm(){
                this.templateCode = ''
                this.templateName = ''
                this.templateStatus = ''
                this.brandId = ''
                this.templateShapeValue = ''
                this.getListData()
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            upperandlowerstatusBachU() {

                let arrs = []
                if(!this.multipleSelection.length) {
                    this.$message.error('请选择一条或多条数据再操作')
                    return
                }
                if(this.multipleSelection) {
                    this.multipleSelection.map((res) => {
                        arrs.push(res.basewaterjetId)
                    })
                }

                this.$confirm("确认上架选中的水刀？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {

                    let params = arrs.join(',');
                    this.shelfKnife(params,1);

                }).catch(() => {
                    this.$message({type: "info", message: "已取消"});
                });

            },
            upperandlowerstatusBachL() {
                if(!this.multipleSelection.length) {
                    this.$message.error('请选择一条或多条数据再操作')
                    return
                } else {
                    let arrs = []
                    this.multipleSelection.map((res) => {
                        arrs.push(res.basewaterjetId)
                    })
                    this.$confirm("确认下架选中的水刀？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }).then(() => {
                        let params = arrs.join(',');
                        this.shelfKnife(params,0);

                    }).catch(() => {
                        this.$message({type: "info", message: "已取消"});
                    });
                }

            },

            shelfKnife(ids,status){
                this.API.upperandlowerstatus({
                    basewaterjetIds:ids,
                    templateStatus:status
                }).then((res) => {
                    if(res.success){
                        this.$message({message: res.message, type: 'success'});
                    }else{
                        this.$message.error(res.message);
                    }
                    this.getListData();
                })
            },

            upperandlowerstatus(basewaterjetIds,templateStatus){
                if(templateStatus ==1){
                    this.$confirm("确认上架水刀？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }).then(()=> {
                        this.shelfKnife(basewaterjetIds,templateStatus)
                    })
                } else if(templateStatus ==0){
                    this.$confirm("确认下架水刀？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }).then(()=> {
                        this.shelfKnife(basewaterjetIds,templateStatus)
                    })
                }


            },
            deleteBach(){
                if(!this.multipleSelection.length) {
                    this.$message.error('请选择一条或多条数据再操作')
                    return
                }
                let arrs = []
                if(this.multipleSelection) {
                    this.multipleSelection.map((res) => {
                        arrs.push(res.basewaterjetId)
                    })
                }

                let params = arrs.join(',');
                this.toDel(params,"确认删除选择的水刀？");

            },
            toDel(basewaterjetIds,message) {

                if(!message){
                    message = "确认删除该水刀？";
                }

                this.$confirm(message, "提示", {
                    confirmButtonText: "确定删除",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {

                    this.API.deleteBasewaterjet({
                        basewaterjetIds:basewaterjetIds,
                    }).then((res) => {
                        if(res.success){
                            this.$message({message: res.message, type: 'success'});
                        }else{
                            this.$message.error(res.message);
                        }
                        this.getListData();
                    });

                }).catch(() => {
                    this.$message({type: "info", message: "已取消删除"});
                });
            },
            getBrandLIst(){
                this.brandList = {};

                let queryCondition = Object.assign({}, this.query, {});

                this.API.getBrandList(queryCondition).then((res) => {
                    console.log(res,"brandList");
                    this.brandList = res.list;
                })
            },
            getTemplateShapes(){
                this.templateShapeList = {};

                let queryCondition = Object.assign({}, this.query, {
                    type:'templateShape'
                });
                this.API.getTemplateShape(queryCondition).then((res) => {
                    console.log(res,"templateShapeList");
                    this.templateShapeList = res.datalist;
                })
            },
            //图片预览事件
            ImgView(ImgURL){
                this.$refs.sonParams.sonFun({ImgURL:ImgURL,ImgShow:true});
            },
            toAdd() {
                this.$router.push('/waterKnife/waterKnifeAdd');
            },
            // 详情
            toDetail(objectId) {
                this.$router.push(`/waterKnife/waterKnifeDetail/${objectId}`);
            },
            toEdit(basewaterjetId)  {
                console.log(basewaterjetId,"basewaterjetId");
                this.$router.push(`/waterKnife/waterKnifeEdit/${basewaterjetId}`);
            }
        },
        components: {
            ImgPreView
        },
    }
</script>

<style scoped lang="scss">
    .nonImg {
        background: #f2f2f2;
        height: 90px;
        width: 90px;
        display: inline-block;
        line-height: 90px;
        text-align: center;
        font-size: 12px;
        color: #999;
        border: solid 1px #e3e3e3;
    }
    .modelList {
        background: #fff;
        border-radius: 4px;
        margin-top: 20px;
        box-sizing: border-box;
        color: #666;
        margin-bottom: 10px;
        .deleteBar {
            height: 40px;
            padding-left: 25px;
            position: relative;
            .share {
                padding-left: 25px;
                color: #666;
                background: url('../../../assets/images/icons/allot.png') 0 center no-repeat;
                &:hover {
                    background: url('../../../assets/images/icons/allot2.png') 0 center no-repeat;
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
            .delete {
                padding-left: 25px;
                color: #666;
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
        }
    }
    .customerTitle {
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
    }
    .selectSearchCond {
        .el-select, .el-input {
            width: 216px;
            margin-right: 10px;
        }
    }
    .btnFrame {
        padding: 10px 0;
        text-align: center;
        border-top: solid 1px #ddd;
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        p {
            float: left;
            display: inline-block;
            width: 44%;
            text-align: right;
            &:first-of-type {
                width: 55%;
            }
        }
        .el-button {
            width: 130px;
        }
    }
    .tableList {
        background: #fff;
        margin: 15px 25px 25px 25px;
    }
    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
</style>
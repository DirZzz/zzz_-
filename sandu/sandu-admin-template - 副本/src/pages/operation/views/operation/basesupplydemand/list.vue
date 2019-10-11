<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>供求信息</span>
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">

                <!-- <el-select v-model="customerStatus" @change="getListData" clearable placeholder="类别">
                    <el-option v-for="(item,index) in categoryList"
                               :key="index"
                               :label="item.name"
                               :value="item.id"
                    ></el-option>
                </el-select> -->

                <el-cascader :options="categoryAlllist" v-model.trim="categoryId" clearable></el-cascader>

                <el-input v-model="queryParam" placeholder="信息标题、发布者账号" clearable></el-input>
                <el-date-picker
                        v-model="pickTime"
                        type="datetimerange"
                        range-separator="至"
                        value-format="yyyy-MM-dd:HH-mm-ss"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
                <el-select v-model="infoType" placeholder="请选择信息类型" clearable>
                    <el-option
                            v-for="item in infoArr"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-row>
            
        </div>
        <div class="btnFrame clearfix">
            <p>
                <el-button round type="primary" size="small" @click="batchDelete" class="batchDelete">批量删除</el-button>
                <el-button round type="primary" size="mini" @click="getListData">搜索</el-button>
                <el-button round size="mini" @click="resetForm">重置</el-button>
            </p>
            
        </div>
        <div class="tableList">
            <el-table
                    :data="tableData"
                    :header-cell-style="rowStyle"
                    v-loading="loading"
                    @selection-change="handleSelectionChange"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%"
                    align="center"
                    >
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column label="序号" type="index" align="center">
                    
                </el-table-column>

                <el-table-column label="信息标题" header-align="center" align="center">
                    <template slot-scope="scope"><span style="color: #409EFF;cursor: pointer" @click="toDetail(scope.row.id)">{{ scope.row.title }}</span></template>
                </el-table-column>
                    
                <el-table-column label="供应/需求" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.type== 1 ? '供应' : '需求'}}
                    </template>
                </el-table-column>
                    
                <el-table-column label="类型" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.supplyDemandCategoryName }}</template>
                </el-table-column>
          
                <el-table-column label="发布者昵称" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.creator }}</template>
                </el-table-column>

                    
                <el-table-column label="动态时间" header-align="center" align="center"> 
                    <template slot-scope="scope">{{ scope.row.gmtModified }}</template>
                </el-table-column>

                <el-table-column
                        label="操作" header-align="center" align="center">
                    <template slot-scope="scope">
                        <!--<el-button type="text" @click="toDetail(scope.row.id)">详情</el-button>-->
                        <el-button type="text" @click="toEdit(scope.row.id)" v-if="permission('operation:basesupplydemand:edit')">编辑</el-button>
                        <el-button type="text" @click="toDel(scope.row.id)" v-if="permission('operation:basesupplydemand:delete')">删除</el-button>
                        <el-button type="text" @click="toTop(scope.row.id,scope.row.recommendedTime > 0?0:1)" v-if="permission('operation:basesupplydemand:top')">{{ scope.row.recommendedTime == 0 ? '置顶':'取消置顶'}}</el-button>
                        <el-button type="text" @click="toReflresh(scope.row.id,scope.row.recommendedTime > 0?0:1)">刷新</el-button>
                        <el-button type="text" @click="$router.push({path:'/basesupplydemand/comment/list',query:{ businessId :scope.row.id}})">评论管理</el-button>
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
</template>

<script>
    export default {
        name: "coustomer",
        data() {
            return {
                categoryId:[],
                categoryAlllist:[],
                total: 0, // 总页数
                mobile: '',
                customerStatus: '', // 客户状态
                queryParam:'',
                categoryList: [
                ],
                loading: true,
                streetShow: true,
                tableData: [],
                listTableData: [],
                query: {
                    page: 1,
                    limit: 10,
                },
                pickTime:'',
                infoType:'',
                infoArr:[
                    {name:"供应",value:1},
                    {name:"需求",value:2}
                ],
                idBox:[]
            }
        },
        created() {
            this.getListData();
            this.categoryDroplist();
            this.categoryDroplist1();
        },
        methods: {
            batchDelete(){
                console.log(this.idBox,'this.idBox')
                if(this.idBox.length>0){
                    this.API.patchDeletesupplydemand({ids:this.idBox.join(',')}).then(res=>{
                        //if(res.success){
                            this.getListData();
                            this.$message.success('删除成功')
                        // }else {
                        //     this.$message.error(res.message)
                        // }
                    })
                }else {
                    this.$message.error('至少选择一个')
                }

            },
            handleSelectionChange(val){
                this.idBox=[];
                console.log(val)
                val.map(res=>{ this.idBox.push(res.id)});
            },
            categoryDroplist1(){
                this.API.queryAllCategoryList().then((res) => {
                    this.categoryAlllist = res.data
                })
            },
            categoryDroplist(){
                this.API.getCategoryList().then((res) => {
                    this.categoryList = res.data
                })
            },
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '60px!important',background:'#f5f5f5',color: '#666'}
                }
            },
            getOriginCode(code) {
            },
            // 区域选择 结束---------- streetCodeName
            // 分类列表
            getListData() {
                console.log(this.pickTime)
                if(this.pickTime){
                    this.query.startTime=this.pickTime[0];
                    this.query.endTime=this.pickTime[1]
                }else {
                    this.query.startTime='';
                    this.query.endTime='';
                }

                this.query.type=this.infoType;
                let queryCondition = Object.assign({}, this.query, {
                    supplyDemandCategoryId:this.categoryId?this.categoryId[1]:null,
                    queryParam:this.queryParam
                })
                // 删除空参数
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                setTimeout(() => {
                    this.loading = false;

                    this.API.queryBasesupplydemand(queryCondition).then(res => {
                     this.tableData = res.list;
                     this.total = res.total
                    });
                }, 1000);
            },
            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size
                this.getListData()
            },
            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage
                this.loading = true
                this.getListData()
            },

            resetForm() {
                this.citySelect = {}
                this.mobile = ''
                this.customerStatus = ''
                this.categoryId=[]
                this.queryParam = '';
                this.pickTime='';
                this.infoType='';
            },
            toReflresh(objectId,topId) {
                 this.API.refreshSupplyById({
                     basesupplydemandId:objectId,
                     topId:topId
                 }).then(res => {
                        if (res.code === 200) {
                            this.getListData();
                            this.$message.success("刷新成功！");
                        }
                    });
            },
            // 详情
            toDetail(objectId) {
                this.$router.push(`/basesupplydemand/detail/${objectId}`);
            },
            toEdit(objectId)  {
                this.$router.push(`/basesupplydemand/edit/${objectId}`);
            },
            toDel(objectId) {
                this.$confirm("此操作将删除内容，是否继续", "提示", {
                    confirmButtonText: "确定删除",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.API.deleteBasesupplydemandById({
                        basesupplydemandId:objectId
                    }).then(res => {
                        if (res.code === 200) {
                            this.getListData();
                            this.$message.success("删除成功！");
                        }
                    });
                }).catch(() => {
                    this.$message({type: "info", message: "已取消删除"});
                });
            },
            toTop(objectId,topId) {
                   this.API.baseSupplyToTopById({
                       basesupplydemandId:objectId,
                       topId:topId
                   }).then(res => {
                        if (res.code === 200) {
                            this.getListData();
                            this.$message.success("操作成功！");
                        }
                    });
            }
        } //end methods
    }
</script>

<style scoped lang="scss">
    /*@import "../../assets/css/mixin.scss";*/
    .customerFrame{
        margin:20px;
    }
    .changeColor {
        color: #ff2323;
    }

    .customerTitle {
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        text-align: left;
        border-bottom: solid 1px #ddd;
        span {
            display: inline-block;
            padding-left: 20px;
        }
    }
    .batchDelete{
        position: absolute;
        left: 16px;
    }
    .searchFrame {
        background: #fff;
        text-align: left;
        padding: 20px;
    }

    .areaStyle {
        margin-top: 20px;
        .el-select {
            width: 216px;
            margin-right: 10px;
        }
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
        font-size: 18px;
        color: #333;
        p {
            text-align: center;
            position: relative;
        }
    }

    .tableList {
        margin-top: 20px;
        padding:20px;
        background: #fff;
    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
</style>
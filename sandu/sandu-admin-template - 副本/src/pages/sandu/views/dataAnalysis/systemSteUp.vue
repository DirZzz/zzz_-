<template>
<div id="activity">
    <div class="main">
        <!--<div class="titles">方案数据</div>-->
        <div class="mainTop">
            <el-tabs v-model="activeName" style="padding-left: 15px; ">
                <el-tab-pane  v-if="isSetUp" label="企业小程序数据统计开关" name="first" ></el-tab-pane>
            </el-tabs>

            <!--搜索框-->
            <div class="selRows">
                <div class="selItems companyName">
                    <span class="label">企业名称：</span>
                    <div class="inputs">
                        <el-input
                                placeholder="请输入企业名称"
                                v-model="companyName"
                                clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems miniName">
                    <span class="label">小程序名称：</span>
                    <div class="inputs">
                        <el-input
                                placeholder="请输入小程序名称"
                                v-model="miniMane"
                                clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems status">
                    <span class="label">状态：</span>
                    <div class="inputs">
                        <el-select v-model="status" placeholder="开启">
                            <el-option label="开启" value=1></el-option>
                            <el-option label="关闭" value=0 ></el-option>
                        </el-select>
                    </div>
                </div>
                <div class="submitBtn" @click="queryList('搜索')">搜索</div>
                <div class="submitBtn"  @click="queryList('重置')">重置</div>
            </div>
        </div>

        <!--小程序统计开关展示-->
        <div class="mainBody">
            <div class="tableDataFrame">
                <el-table
                        :data="miniCompanyList"
                        :header-cell-style="rowStyle"
                        @selection-change="handleSelectionChange"
                        style="padding-top:20px"
                        element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(255, 255, 255, 0.8)"
                        tooltip-effect="dark"
                >
                    <el-table-column type="selection">
                    </el-table-column>
                    <el-table-column  type="index" width="80px" label="序号">
                    </el-table-column>
                    <el-table-column prop="feedbackTxt" label="企业小程序名"  >
                        <template slot-scope="scope">{{ scope.row.feedbackContent }}</template>
                    </el-table-column>
                    <el-table-column prop="time" label="企业名">
                        <template slot-scope="scope">{{ scope.row.gmtCreate }}</template>
                    </el-table-column>
                    <el-table-column prop="states" label="状态" >
                        <template slot-scope="scope">{{ scope.row.dealStatus?"开启":"关闭" }}</template>
                    </el-table-column>
                    <el-table-column label="操作" >
                        <template slot-scope="scope">
                            <el-button type="text" @click="showDel(scope.row.feedbackId)">开启数据统计</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pageCount">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="listPage"
                            :page-sizes="[10, 20, 30, 40]"
                            :page-size="getList.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="this.totalCount">
                    </el-pagination>

                </div>
            </div>
        </div>
    </div>
</div>
</template>


<script>
import 'echarts/lib/chart/line'
import 'echarts/lib/component/polar'
import qs from 'qs'
    export default {
        name:'activity',
        data() {
            return {
                activeName: "first",
                isSetUp: true,

                companyName:'',//企业名称
                miniMane:'',//小程序名称
                status:'',//开启状态

                totalCount:0,
                listPage:1,

                //小程序列表
                miniCompanyList:[],

                //列表查询入参
                queryListParam: {
                    companyName: '', // 企业名称
                    miniName: '', // 小程序名称
                    status: '', // 开启状态
                    page: 1, // 页码
                    limit: 10 // 每页条数
                },

            }
        },
        components: {
        },


        created(){
            this.getList();
        },
        methods: {
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '30px!important',background:'#f5f5f5',color: '#666',lineHeight:'80px!important'}
                }
            },
            handleSelectionChange (val) {
                this.multipleSelection = val
            },
            // 分页模块f
            handleSizeChange: function (size) {
                //this.query.limit = size
                this.queryListParam.limit = size;
                this.queryListParam.page=1;
                this.getList()
            },
            handleCurrentChange: function (currentPage) {
                //this.query.page = currentPage;
                this.queryListParam.page = currentPage;
                this.loading = true;
                this.getList()
            },
            //数据查询
            getList(){
                /*this.API.showFeedbackList(this.getList).then(res => {
                    console.log(res,'feedback res');
                    if (res.success){
                        this.listPage = 1;
                        this.miniCompanyList = res.datalist;
                        this.totalCount = res.totalCount;
                    }
                });*/
            },
            //装进我家用户数
            queryList(type){
                if (type==='搜索') {
                    //查询之前将原值置空
                    this.miniCompanyList = [];
                    this.totalCount = 0;

                    this.queryListParam.companyName=this.companyName;
                    this.queryListParam.miniName=this.miniName;
                    this.queryListParam.status=this.status;

                    this.sourceBaseUrl = this.BASE_URL.sourceBaseUrl;
                    this.getList();

                }
                if (type==='重置'){
                    this.queryListParam={
                        companyName: undefined, // 企业名称
                        miniName: undefined, // 小程序名称
                        status: undefined, // 开启状态
                        page: 1, // 页码
                        limit:10 // 每页条数
                    }
                    this.companyName= ''; // 企业名称
                    this.miniName= ''; // 小程序名称
                    this.status= ''; // 开启状态
                    this.page = '';
                    this.limit = '';
                    this.sourceBaseUrl = this.BASE_URL.sourceBaseUrl;
                    this.getList();
                }

            },
        }
    }
</script>


<style scoped lang="scss">
    .main {
        width: 100%;
        height: 100%;
        padding: 20px;
        font-size: 16px;
        color: #333333;
        .mainTop {
            width: 100%;
            background-color: #ffffff;
            border-radius: 2px;
            margin-bottom: 20px;
            .titles {
                height: 55px;
                border-bottom: 1px solid #eaeaea;
                padding: 0 30px;
                line-height: 55px;
                text-align: left;
            }
            .selRows {
                padding: 24px 30px;
                height: 80px;
                .submitBtn{
                    height: 32px;
                    line-height: 32px;
                    box-sizing: border-box;
                    background-color: #3399ff;
                    border-radius: 4px;
                    width: 64px;
                    float: left;
                    color: #fff;
                    font-size: 14px;
                    cursor: pointer;
                    margin-left: 30px;
                }
                .selItems {
                    height: 32px;
                    line-height: 32px;
                    box-sizing: border-box;
                    margin-right: 48px;
                    position: relative;
                    float: left;
                    .label {
                        text-align: right;
                        font-size: 14px;
                        height: 32px;
                        position: absolute;
                        left: 0;
                        top: 0;
                    }
                    .inputs {
                        display: block;
                        width: 200px;
                        height: 32px;
                        line-height: 32px;
                        box-sizing: border-box;
                    }
                    .el-select {
                        height: 32px;
                        line-height: 32px;
                    }
                }
                .companyName {
                    width: 262px;
                    padding-left: 62px;
                    label {
                        width: 62px;
                    }
                    .inputs {
                        background-color: #ffffff;
                        border-radius: 4px;
                        input {
                            display: block;
                            width: 100%;
                            height: 100%;
                            font-size: 14px;
                        }
                    }

                }
                .miniName {
                    width: 262px;
                    padding-left: 62px;
                    label {
                        width: 62px;
                    }
                    .inputs {
                        background-color: #ffffff;
                        border-radius: 4px;
                        input {
                            display: block;
                            width: 100%;
                            height: 100%;
                            font-size: 14px;
                        }
                    }

                }
                .status {
                    width: 248px;
                    padding-left: 48px;
                    label {
                        width: 48px;
                    }
                }
                .companyName, .miniName {
                    width: 276px;
                    padding-left: 100px;
                    label {
                        width: 100px;
                    }
                }
            }
            margin-bottom: 20px;
        }
    }

div{
    box-sizing: border-box;
}
.ttspan{
    display: inline-block;
    padding: 0 15px;
}

#activity{
    width: 100%;
    height: 100%;
    padding: 20px;
    font-size: 16px;
    color: #333;
    text-align: left;
    overflow-y: scroll;
    .mains{
        width: 100%;
        min-height: 100%;
        background: #fff;
        padding:0 20px;
        .titles{
            width: 100%;
            height: 55px;
            line-height: 55px;
            border-bottom: 1px solid #eaeaea;
        }
        .mainBody{
            padding-top: 30px;
            .rowTOp{
                margin-bottom: 30px;
                .el-form-item{
                    margin: 0;
                    float: right;
                    margin-left: 30px;
                }
            }
        }
    }
    .zzcs{
        width: 100%;
        height: 100%;
        position: fixed;
        left: 0;
        top: 0;
        z-index: 2000;
        background: rgba(0,0,0,.7);
        .rows{
            width: 25%;
            padding-top: 50px;
            position: absolute;
            text-align: center;
            background: #fff;
            left: 35%;
            top: 50%;
            height: 205px;
            margin-top: -102.5px;
            border-radius: 5px;
            .titles{
                height: 50px;
                width: 100%;
                border-bottom: 1px solid #eaeaea;
                position: absolute;
                left: 0;
                top: 0;
                font-size: 18px;
                line-height: 50px;
                i{
                    width: 50px;
                    height: 50px;
                    position: absolute;
                    right: 0;
                    top: 0;
                    line-height: 50px;
                    cursor: pointer;
                }
            }
            .bodys{
                width: 100%;
                border-bottom: 1px solid #eaeaea;
                padding: 40px 0;
            }
            .btns{
                padding: 15px 20px;
                text-align: right;
                font-size: 17px;
            }
        }
        .formRows{
            min-width: 610px;
            width: 50%;
            position: absolute;
            left: 25%;
            top: 40%;
            padding: 0 20px;
            border-radius: 5px;
            background: #fff;
            height: 318px;
            margin-top: -159px;
            .titles{
                height: 50px;
                width: 100%;
                border-bottom: 1px solid #eaeaea;
                font-size: 18px;
                line-height: 50px;
                margin-bottom: 20px;
                position: relative;
                i{
                    width: 50px;
                    height: 50px;
                    position: absolute;
                    right: -20px;
                    top: 0;
                    line-height: 50px;
                    cursor: pointer;
                    text-align: center;
                }
            }
        }
    }
}

</style>

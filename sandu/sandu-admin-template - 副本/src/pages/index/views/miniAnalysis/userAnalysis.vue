<template>
<div id="user">

    <div class="main">
        <div class="mainTop">
            <!--<div class="titles">用户统计</div>-->
            <el-tabs v-model="activeName" style="padding-left: 15px; " @tab-click="handleClick">
                <el-tab-pane label="活跃用户" name="first"  ></el-tab-pane>
                <el-tab-pane label="新增用户" name="second" ></el-tab-pane>
            </el-tabs>

            <div >
                <div class="radio">
                    <span class="label">日期：</span>
                    <div class="tab">
                        <span :class="radios=='昨天'?'active':''" @click="dataChange('昨天')" label="昨天">昨天</span>
                        <span :class="radios=='今天'?'active':''" @click="dataChange('今天')" label="今天">今天</span>
                        <span :class="radios=='7日'?'active':''" @click="dataChange('7日')" label="7日">7日</span>
                        <span :class="radios=='30日'?'active':''" @click="dataChange('30日')" label="30日">30日</span>
                        <span :class="radios=='每月'?'active':''" @click="dataChange('每月')" label="每月">每月</span>
                    </div>

                    <span class="label" style="padding-left: 50px">地区：</span>
                    <el-select v-model="areaName" placeholder="全部" @change="areaSelect">
                        <el-option
                                v-for="item in areaNameList"
                                :value="item.areaName">
                        </el-option>
                    </el-select>
                </div>
            </div>


        </div>


        <div class="mainBody">
            <div class="dataTable">
                <span class="label">汇总(合计)：</span>
                <div class="tableDataFrame" style=" width:400px">
                   <el-table :data="summarizeData"
                            :header-cell-style="rowStyle"
                            @selection-change="handleSelectionChange"
                            style="padding-top:20px"
                            element-loading-text="拼命加载中"
                            element-loading-spinner="el-icon-loading"
                            element-loading-background="rgba(255, 255, 255, 0.8)"
                            tooltip-effect="dark">
                        <!--isActiveUser===true ：活跃用户展示列-->
                        <!--isNewAddUser===true ：新增用户展示列-->
                        <!--isActiveUser===true || isNewAddUser===true ：活跃用户和新增用户都需要展示的列-->
                        <el-table-column prop="feedbackTxt" v-if="isActiveUser===true" label="活跃总人数">
                            <template slot-scope="scope">{{ scope.row.allActiveUser }}</template>
                        </el-table-column>
                        <el-table-column prop="states" v-if="isActiveUser===true" label="有效用户" >
                            <template slot-scope="scope">{{ scope.row.allEffectiveUser }}</template>
                        </el-table-column>

                        <el-table-column prop="states" v-if="isNewAddUser===true" label="新增人数" >
                            <template slot-scope="scope">{{ scope.row.allNewUserCount}}</template>
                        </el-table-column>

                        <el-table-column prop="states" v-if="isActiveUser===true " label="打开次数">
                            <template slot-scope="scope">{{ scope.row.allUseCount }}</template>
                        </el-table-column>

                        <el-table-column prop="states" v-if="isActiveUser===true" label="总用户数" >
                            <template slot-scope="scope">{{ scope.row.allActiveUserTotal }}</template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>

            <v-chart :options="option"/>

            <div class="dataTable">
                <div class="listTitle" style="color: red;text-align: left; font-size: 12px" v-if="this.isActiveUser===true">*&nbsp;  活跃用户：登录小程序的用户 &nbsp;&nbsp;&nbsp; 有效用户：有绑定手机号并且有登录的用户</div>
                <div class="listTitle" style="color: red;text-align: left; font-size: 12px" v-if="this.isNewAddUser===true">*&nbsp;  新增用户：首次登录小程序的用户</div>
                <div class="listTitle">数据明细
                    <div class="right">
                        <el-button @click="handleDownload" size="small" type="primary" :disabled="false">导出Excel</el-button>
                    </div>
                </div>
                <div class="tableDataFrame">
                    <el-table
                            :data="detailDataList"
                            :header-cell-style="rowStyle"
                            @selection-change="handleSelectionChange"
                            style="padding-top:20px"
                            element-loading-text="拼命加载中"
                            element-loading-spinner="el-icon-loading"
                            element-loading-background="rgba(255, 255, 255, 0.8)"
                            tooltip-effect="dark">
                       <!-- <el-table-column type="selection">
                        </el-table-column>
                        <el-table-column  type="index" width="50px" label="序号">
                        </el-table-column>-->
                        <el-table-column prop="time" label="时间" v-if="isNewAddUser===true || isActiveUser===true">
                            <template slot-scope="scope">{{ scope.row.date }}</template>
                        </el-table-column>
                        <el-table-column prop="states" label="活跃用户数" v-if="isActiveUser===true">
                            <template slot-scope="scope">{{ scope.row.activeUser}}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="有效客户"  v-if="isActiveUser===true">
                            <template slot-scope="scope">{{ scope.row.effectiveUser }}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="新增人数" v-if="isNewAddUser===true">
                            <template slot-scope="scope">{{scope.row.newUserCount }}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="打开次数" v-if=" isActiveUser===true">
                            <template slot-scope="scope">{{scope.row.useCount }}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="累计总用户" v-if="isActiveUser===true">
                            <template slot-scope="scope">{{scope.row.activeUserTotal }}</template>
                        </el-table-column>
                    </el-table>
                    <div class="pageCount">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page.sync="listPage"
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
                isActiveUser: true,//是活跃用户
                isNewAddUser:false,//是新增用户

                dateValues: "first",

                cityName:'',//市名称
                miniName:'',//小程序名称
                areaName:'',//地区名称
                radios:'昨天',

                summarizeData:[],//数据汇总
                detailDataList:[],//数据明细
                areaNameList:[],//地区数据
                multipleSelection:[],

                //列表查询入参
                queryList: {
                    dateType:'',//时间类型(以小时为单位0，以天为单位1)
                    userType:0,
                    areaCode:'',//地区编码
                    miniName: '', // 企业小程序名称(换成小程序app_id)?
                    start: 1, // 页码
                    limit: 10 // 每页条数
                },
                listPage:1,//当前页
                totalCount:0,//总数量

                option: {
                    title: {
                        text: '小程序用户统计'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data:["活跃用户","有效用户"]
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: []
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            name:'',
                            type:'line',
                            data:[]
                        },
                        {
                            name:'',
                            type:'line',
                            data:[]
                        },
                    ]
                },
            }
        },
        components: {
        },


        created(){
            this.getAreaList();
            this.getUserSum();
            this.getList();
            this.getUserAll();
            this.getExcel();
        },
        methods: {
            rowStyle({row, rowIndex}) {
                if (rowIndex === 0) {
                    return {
                        height: '30px!important',
                        background: '#f5f5f5',
                        color: '#666',
                        lineHeight: '80px!important'
                    }
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },

            // 导出excel 表
            handleDownload() {
                require.ensure([], () => {
                    //this.getExcel();
                    if (!this.multipleSelection.length){
                        this.$alert('无数据供导出！','提示',{
                            confirmButtonText: '确定',
                        });
                    }else{
                        const { export_json_to_excel } = require('@/vendor/Export2Excel');
                        if (this.isActiveUser){
                            const tHeader = ['时间','活跃用户数', '有效客户','打开次数', '累计总用户'];
                            const filterVal = ['date','activeUser', 'effectiveUser','useCount', 'activeUserTotal'];
                            const list = this.multipleSelection;
                            //alert(JSON.stringify(list));
                            const data = this.formatJson(filterVal, list);
                            //alert(JSON.stringify(data));
                            export_json_to_excel(tHeader, data, '活跃用户');
                        }else{
                            const tHeader = ['时间','新增人数'];
                            const filterVal = ['date','newUserCount'];
                            const list = this.multipleSelection;
                            //alert(JSON.stringify(list));
                            const data = this.formatJson(filterVal, list);
                            //alert(JSON.stringify(data));
                            export_json_to_excel(tHeader, data, '新增用户');
                        }
                    }
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },


            //数据查询
            getExcel() {
                this.API.getUserAnalysisList({
                    dateType:this.radios,
                    areaName:this.areaName,
                    userType:this.queryList.userType,
                    start:1,
                    limit:9999
                }).then((res)=>{
                    if(res.success){
                        //alert("xxx",JSON.stringify(res.datalist));
                        this.multipleSelection=res.datalist;
                    }
                })
            },

            // 分页模块f
            handleSizeChange: function (size) {
                //this.query.limit = size
                this.queryList.limit = size;
                this.queryList.start = 1;
                this.getList();
                this.listPage=1;
            },
            handleCurrentChange: function (currentPage) {
                //this.query.page = currentPage;
                this.queryList.start = currentPage;
                this.loading = true;
                this.getList();
                this.listPage=currentPage;
            },
            //数据查询
            getList() {
                this.API.getUserAnalysisList({
                    dateType:this.radios,
                    areaName:this.areaName,
                    userType:this.queryList.userType,
                    start:this.queryList.start,
                    limit:this.queryList.limit
                }).then((res)=>{
                    if(res.success){
                        this.totalCount=res.totalCount;
                        this.detailDataList=res.datalist;
                    }
                })
            },

            handleClick(tab, event) {
                if (tab.name === "first") {
                    this.activeName = "first";
                    this.isActiveUser = true;
                    this.isNewAddUser = false;
                    this.queryList.userType=0;
                    //将上次请求数据置零
                    this.areaName='';
                    this.totalCount=0;
                    this.multipleSelection=[];
                    this.detailDataList=[];
                    this.summarizeData=[];
                    this.option.legend.data=[];
                    this.option.xAxis.data=[];
                    this.option.series[0].data=[];
                    this.getAreaList();
                    this.getUserAll();
                    this.getList();
                    this.getExcel();
                }
                if (tab.name === "second") {
                    this.activeName = "second";
                    this.isActiveUser = false;
                    this.isNewAddUser = true;
                    this.queryList.userType=1;
                    this.areaName='';
                    //将上次请求数据置零
                    this.totalCount=0;
                    this.detailDataList=[];
                    this.summarizeData=[];
                    this.multipleSelection=[];
                    this.option.legend.data=[];
                    this.option.xAxis.data=[];
                    this.option.series[0].data=[];
                    this.option.series[1].data=[];
                    this.getAreaList();
                    this.getUserAll();
                    this.getList();
                    this.getExcel();
                }
            },

            //时间改变
            dataChange(dataType) {
                this.radios = dataType;

                //将上次请求数据置零
                this.totalCount=0;
                this.option.xAxis.data=[];
                this.option.series[0].data=[];
                this.option.series[1].data=[];
                this.detailDataList=[];
                this.summarizeData=[];
                this.multipleSelection=[];
                this.areaName='';

                this.getUserAll();
                this.getList();
                this.getUserSum();
                this.getAreaList();
                this.getExcel();
            },
            //获取地区名称列表
            getAreaList(){
                this.API.getAreaList({
                    dateType:this.radios,
                    userType:this.queryList.userType
                }).then((res)=>{
                    if(res.success){
                        this.areaNameList=res.datalist;
                    }
                })
            },

            areaSelect(){
                console.log(this.areaName);
                //将上次请求数据置零
                this.totalCount=0;
                this.multipleSelection=[];
                this.option.xAxis.data=[];
                this.option.series[0].data=[];
                this.option.series[1].data=[];
                this.detailDataList=[];
                this.getList();
                this.getUserAll();
                this.getUserSum();
                this.getExcel();
                // this.getAreaList();
            },

            //获取用户汇总
            getUserSum() {
                this.API.getUserAnalysisSum({
                    areaName:this.areaName,
                    dateType: this.radios,
                    userType:this.queryList.userType
                }).then((res) => {
                    if (res.success) {
                        this.summarizeData = res.datalist;
                    } else {
                        this.$message({
                            type: "info",
                            message: res.message
                        });
                    }
                })
            },

            //获取用户汇总
            getUserAll() {
                this.API.getUserAnalysisAll({
                    dateType:this.radios,
                    areaName:this.areaName,
                    userType:this.queryList.userType
                }).then((res) => {
                    if (res.success) {
                        if (this.queryList.userType===0){
                            this.option.legend.data=["活跃用户","有效用户"];
                            this.option.series[0].name="活跃用户";
                            this.option.series[1].name="有效用户";
                            for(var i = 0;i<res.datalist.length;i++){
                                this.option.xAxis.data.push(res.datalist[i].date);
                                this.option.series[0].data.push(res.datalist[i].activeUser);
                                this.option.series[1].data.push(res.datalist[i].effectiveUser);
                            }
                        }else{
                            this.option.legend.data=["新增用户"];
                            this.option.series[0].name="新增用户";
                            this.option.series[1].name="";
                            for(var i = 0;i<res.datalist.length;i++){
                                this.option.xAxis.data.push(res.datalist[i].date);
                                this.option.series[0].data.push(res.datalist[i].newUserCount);
                            }
                        }
                    } else {
                        this.$message({
                            type: "info",
                            message: res.message
                        });
                    }
                })
            },
        }
    }
</script>

<style scoped lang="scss">
    ::-webkit-scrollbar{
        display: none;
    }
    .radio{
        padding-left: 20px;
        box-sizing: border-box;
        padding-bottom: 20px;
        display: flex;
        align-items: center;
    }
    .main {
        width: 100%;
        height: 100%;
        font-size: 16px;
        color: #333333;
        background: #fff;
        .dataTable{
            padding: 0 20px;
            background: #fff;
        }
        .chart{
            padding: 20px;
            background: #fff;
        }
        .mainTop {
            width: 100%;
            background-color: #ffffff;
            border-radius: 2px;
            margin: 0 !important;
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
                .submitBtn {
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
                    margin-left: 20px;
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
                        left: -10Px;
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
                .miniName {
                    width: 262px;
                    padding-left: 150px;
                    label {
                        width: 150px;
                    }
                    .inputs {
                        background-color: #ffffff;
                        padding-left: 30px;
                        border-radius: 4px;
                        input {
                            padding-right: 40px;
                            display: block;
                            width: 100%;
                            height: 100%;
                            font-size: 14px;
                        }
                    }

                }
                .miniName {
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

.tab{
    display: flex;
    span{
        display: inline-block;
        vertical-align: top;
        width: 36px;
        line-height: 14px;
        padding: 12px 20px;
        background-color: #fff;
        border: 1px solid #ccc;
        text-align: center;
        cursor: pointer;
    }
    span+span{
        border-left: none;
    }
    span:hover{
        color: #ff6419;
    }
    span.active{
        background-color: #FF6419;
        color: #fff;
        border: #FF6419;
    }
}


.ttspan{
    display: inline-block;
    padding: 0 15px;
}
    .listTitle{
        font-size: 16px;
        color: #333;
        height: 56px;
        text-align: left;
        line-height: 56px;
        background: #fff;
        margin-top: 20px;
        border-bottom: solid 1px #e8e8e8;
        padding-left: 32px;
        position: relative;
        .right{
            @extend .exportExcel
        }
    }
    .exportExcel{
        position: absolute;
        right: 32px;
        top: 0;
        span{
            display: inline-block;
            text-align: center;
            font-size: 14px;
            width: 82px;
            height: 32px;
            line-height: 32px;
            background-color: $theme-color;
            color: #fff;
            border-radius: 4px;
        }
    }
#user{
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

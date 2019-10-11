<template>
<div id="activity">
    <div class="mains">
        <div class="mainTop">
            <!--<div class="titles" style="color:#FF0000">用户来源</div>-->
            <el-tabs v-model="activeName" style="padding-left: 15px; ">
                <el-tab-pane  v-if="isSource" label="用户来源" name="first" ></el-tab-pane>
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
                </div>
                <div class="radio2">
                    <el-tabs v-model="userTypeName" style="padding-left: 15px; " @tab-click="handleClick">
                        <el-tab-pane label="活跃用户" name="first"  ></el-tab-pane>
                        <el-tab-pane label="新增用户" name="second" ></el-tab-pane>
                    </el-tabs>
                </div>
            </div>
        </div>

        <div class="mainBody">

            <v-chart :options="option"/>

            <div class="dataTable">
                <div class="listTitle" style="color: red;text-align: left; font-size: 12px"  v-if="this.userType===0">*&nbsp;  名片：从电子名片入口登录的用户&nbsp;&nbsp;&nbsp;  其他：非电子名片入口登录的用户</div>
                <div class="listTitle" style="color: red;text-align: left; font-size: 12px"  v-if="this.userType===1">*&nbsp;  名片：从电子名片入口首次登录的用户&nbsp;&nbsp;&nbsp;  其他：非电子名片入口首次登录的用户</div>
                <div class="listTitle">数据明细
                    <div class="right">
                        <el-button @click="handleDownload" size="small" type="primary" :disabled="false">导出Excel</el-button>
                    </div>
                </div>
                <div class="tableDataFrame">
                    <el-table
                            :data="sumDataList"
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
                        <el-table-column prop="time" label="渠道来源">
                            <template slot-scope="scope">{{ scope.row.channelType }}</template>
                        </el-table-column>
                        <el-table-column prop="states" label="新增用户">
                            <template slot-scope="scope">{{ scope.row.allNewUserCount }}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="活跃用户" >
                            <template slot-scope="scope">{{scope.row.allActiveUserCount}}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="有效客户数" >
                            <template slot-scope="scope">{{scope.row.allEffectiveUserCount}}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="打开次数" >
                            <template slot-scope="scope">{{scope.row.allUseCount}}</template>
                        </el-table-column>
                    </el-table>
                    <!--<div class="pageCount">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="listPage"
                                :page-sizes="[10, 20, 30, 40]"
                                :page-size="getList.limit"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="this.totalCount">
                        </el-pagination>
                    </div>-->
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
                userTypeName:"first",
                activeName: "first",
                isSource: true,

                radios:'昨天',

                sumDataList:[],//数据汇总
                detailList:[],//数据明细
                multipleSelection:[],
                minList :[],
                cardList:[],
                qrList:[],
                activityList:[],
                sevenList:[],
                otherList:[],
                userType:0,


                //列表查询
                queryList: {
                    miniName: '', // 企业小程序名称
                    dateType:'',
                    page: 1, // 页码
                    limit: 10 // 每页条数
                },
                listPage:1,//当前页
                totalCount:0,

                //数据柱状图展示
                option: {
                    title: {
                        text: '用户来源'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data:["名片","其他"]
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
                            name:"",
                            type:'line',
                            data:[]
                        },
                        {
                            name:"",
                            type:'line',
                            data:[]
                        }
                    ]
                },
            }
        },
        components: {
        },


        created(){
            this.getList();
            this.getSum();
            this.getExcel();
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

            // 导出excel 表
            handleDownload() {
                require.ensure([], () => {
                    if (!this.multipleSelection.length){
                        this.$alert('无数据供导出！','提示',{
                            confirmButtonText: '确定',
                        });
                    }else{
                        const { export_json_to_excel } = require('@/vendor/Export2Excel');
                        const tHeader = ['渠道来源', '新增用户','活跃用户','有效客户数','打开次数'];
                        const filterVal = ['channelType', 'allNewUserCount','allActiveUserCount','allEffectiveUserCount','allUseCount'];
                        const list = this.multipleSelection;
                        //alert(JSON.stringify(list));
                        const data = this.formatJson(filterVal, list);
                        //alert(JSON.stringify(data));
                        export_json_to_excel(tHeader, data, '用户来源');
                    }
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },

            // 分页模块f
            handleSizeChange: function (size) {
                this.queryList.limit = size;
                this.queryList.start=1;
                this.getList()
            },
            handleCurrentChange: function (currentPage) {
                this.queryList.start = currentPage;
                this.loading = true;
                this.getList()
            },


            getExcel(){
                this.API.getSourceAnalysisSum({
                    dateType:this.radios
                }).then((res)=>{
                    if(res.success){
                        this.multipleSelection=res.datalist;
                    }
                })
            },

            //数据查询
            getList(){
                this.API.getSourceAnalysisList({
                    dateType:this.radios,
                    type:this.userType
                    }).then((res)=>{
                    if(res.success){

                        this.option.series[0].name="名片";

                        this.option.series[1].name="其他";

                        this.cardList=res.obj.cardList;

                        this.otherList=res.obj.otherList;
                        if (this.userType === 0) {

                            for (var i = 0;i<this.cardList.length;i++){
                                this.option.xAxis.data.push(this.cardList[i].date);
                                this.option.series[0].data.push(this.cardList[i].activeUserCount);
                            }

                            for (var i = 0;i<this.otherList.length;i++){
                                this.option.series[1].data.push(this.otherList[i].activeUserCount);
                            }
                        }else{

                            for (var i = 0;i<this.cardList.length;i++){
                                this.option.xAxis.data.push(this.cardList[i].date);
                                this.option.series[0].data.push(this.cardList[i].newUserCount);
                            }

                            for (var i = 0;i<this.otherList.length;i++){
                                this.option.series[1].data.push(this.otherList[i].newUserCount);
                            }
                        }
                    }
                })
            },

            getSum(){
                this.API.getSourceAnalysisSum({
                    dateType:this.radios
                }).then((res)=>{
                    if(res.success){
                        this.sumDataList=res.datalist;
                    }else {
                        this.$message({
                            type: "info",
                            message: res.message
                        });
                    }
                })
            },


            //时间改变
            dataChange(dataType){
                this.radios=dataType;
                this.userTypeName="first",
                this.userType=0,
                this.detailList=[];
                this.sumDataList=[];

                this.cardList=[];
                this.multipleSelection=[];
                this.otherList=[];
                this.option.xAxis.data=[];
                this.option.series[0].data=[];
                this.option.series[1].data=[];

                this.getSum();
                this.getList();
                this.getExcel();
            },

            //折线图变化
            handleClick(tab, event) {
                if (tab.name === "first") {
                    this.userTypeName = "first";
                    this.userType=0;
                    this.option.xAxis.data=[];
                    this.option.series[0].data=[];
                    this.option.series[1].data=[];
                    this.multipleSelection=[];
                    this.getList();
                    this.getExcel();
                }
                if (tab.name === "second") {
                    this.userTypeName = "second";
                    this.userType=1;
                    this.option.xAxis.data=[];
                    this.option.series[0].data=[];
                    this.option.series[1].data=[];
                    this.multipleSelection=[];
                    this.getList();
                    this.getExcel();
                }
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
.ttspan{
    display: inline-block;
    padding: 0 15px;
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

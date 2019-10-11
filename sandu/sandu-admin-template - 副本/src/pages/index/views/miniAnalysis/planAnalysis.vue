<template>
<div id="activity">
    <div class="main">
        <!--<div class="titles">方案数据</div>-->
        <div class="mainTop">

            <el-tabs v-model="activeName" style="padding-left: 15px; " @tab-click="handleClick">
                <el-tab-pane label="装进我家用户数" name="first" ></el-tab-pane>
                <el-tab-pane label="方案使用数据" name="second" ></el-tab-pane>
                <el-tab-pane label="热门方案" name="third" ></el-tab-pane>
            </el-tabs>

            <div >
                <div class="radio noOutline">
                    <span class="label">日期：</span>
                    <div class="tab">
                        <span :class="radios=='昨天'?'active':''" @click="dataChange('昨天')" label="昨天">昨天</span>
                        <span :class="radios=='今天'?'active':''" @click="dataChange('今天')" label="今天">今天</span>
                        <span :class="radios=='7日'?'active':''" @click="dataChange('7日')" label="7日">7日</span>
                        <span :class="radios=='30日'?'active':''" @click="dataChange('30日')" label="30日">30日</span>
                        <span :class="radios=='每月'?'active':''" @click="dataChange('每月')" label="每月">每月</span>
                    </div>
                </div>
            </div>

        </div>


        <div class="mainBody">
            <!--数据汇总Table-->
            <div class="dataTable" v-if="isInMyHomeUser===true || isPlanUseData===true">
                <span class="label">汇总(合计)：</span>
                <div class="tableDataFrame" style=" width:400px">
                    <el-table
                            :data="summarizeDataList"
                            :header-cell-style="rowStyle"
                            @selection-change="handleSelectionChange"
                            style="padding-top:20px"
                            element-loading-text="拼命加载中"
                            element-loading-spinner="el-icon-loading"
                            element-loading-background="rgba(255, 255, 255, 0.8)"
                            tooltip-effect="dark">
                        <el-table-column prop="allOnekeyUserCount" label="装进我家人数"  key="1" v-if="isInMyHomeUser">
                            <template slot-scope="scope">{{ scope.row.allOnekeyUserCount }}</template>
                        </el-table-column>
                        <el-table-column prop="allOnekeyCount" label="装进我家次数" key="5" v-if="isInMyHomeUser || isPlanUseData">
                            <template slot-scope="scope">{{scope.row.allOnekeyCount}}</template>
                        </el-table-column>
                        <el-table-column prop="allClickCount" label="点击方案次数" key="3" v-if="isPlanUseData">
                            <template slot-scope="scope">{{ scope.row.allClickCount }}</template>
                        </el-table-column>
                        <el-table-column prop="allClickUserCount" label="点击方案人数" key="4" v-if="isPlanUseData">
                            <template slot-scope="scope">{{scope.row.allClickUserCount}}</template>
                        </el-table-column>
                        <el-table-column prop="allEnterPanoramaCount" label="进入720次数" key="6" v-if="isPlanUseData">
                            <template slot-scope="scope">{{ scope.row.allEnterPanoramaCount }}</template>
                        </el-table-column>
                        <el-table-column prop="allCollectCount" label="方案收藏次数" key="2" v-if="isPlanUseData">
                            <template slot-scope="scope">{{scope.row.allCollectCount}}</template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>

            <!--图形展示-->
            <div class="chart1" v-if="isInMyHomeUser===true || isPlanUseData===true">
                <v-chart :options="option"/>
            </div>
            <div class="chart2" v-if="isHotPlan===true">
                <v-chart :options="option2"/>
            </div>

            <!--数据明细Table-->
            <div class="dataTable">
                <div class="listTitle" style="color: red;text-align: left; font-size: 12px" >*&nbsp;装进我家的人数：在小程序中进行装进我家的人数
                    &nbsp;&nbsp;&nbsp;点击方案的次数：在小程序中点击方案进入详情的次数</div>
                <div class="listTitle">数据明细
                    <div class="right">
                        <el-button @click="handleDownload" size="small" type="primary" :disabled="false">导出Excel</el-button>
                    </div>
                </div>
                <div class="tableDataFrame">
                    <el-table
                            :data="detailList"
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
                        <el-table-column  prop="time" label="时间" v-if="isInMyHomeUser===true || isPlanUseData===true">
                            <template slot-scope="scope">{{ scope.row.date }}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="方案编码" v-if="isHotPlan===true" >
                            <template slot-scope="scope">{{scope.row.designCode}}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="方案名称" v-if="isHotPlan===true" >
                            <template slot-scope="scope">{{scope.row.designName}}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="装进我家人数" key="a" v-if="isInMyHomeUser===true " >
                            <template slot-scope="scope">{{ scope.row.onekeyUserCount }}</template>
                        </el-table-column>
                        <el-table-column  prop="comment" label="装进我家次数" key="b" v-if="isInMyHomeUser===true || isPlanUseData===true || isHotPlan===true">
                            <template slot-scope="scope">{{scope.row.onekeyCount}}</template>
                        </el-table-column>
                        <el-table-column  prop="comment" label="点击方案次数" key="c" v-if="isPlanUseData===true">
                            <template slot-scope="scope">{{ scope.row.clickCount }}</template>
                        </el-table-column>
                        <el-table-column  prop="comment" label="点击方案人数" key="d" v-if="isPlanUseData===true">
                            <template slot-scope="scope">{{scope.row.clickUserCount}}</template>
                        </el-table-column>
                        <el-table-column  prop="comment" label="进入720次数" key="e" v-if="isPlanUseData===true || isHotPlan===true">
                            <template slot-scope="scope">{{scope.row.enterPanoramaCount}}</template>
                        </el-table-column>
                        <el-table-column  prop="comment" label="方案收藏次数" key="f" v-if="isPlanUseData===true" >
                            <template slot-scope="scope">{{scope.row.collectCount}}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="产品替换次数" key="g" v-if="isHotPlan===true" >
                            <template slot-scope="scope">{{scope.row.productReplaceCount}}</template>
                        </el-table-column>
                        <el-table-column prop="comment" label="点击次数" key="h" v-if="isHotPlan===true" >
                            <template slot-scope="scope">{{scope.row.clickCount}}</template>
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
                                :total="totalCount">
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
                isInMyHomeUser: true,//装进我家
                isPlanUseData:false,//方案使用
                isHotPlan:false,//热门方案

                radios:'昨天',

                summarizeDataList:[],//数据汇总
                detailList:[],//数据明细
                top10List:[],//热门方案前十
                multipleSelection:[],

                //列表查询
                queryList: {
                    dateType:'',//时间类型
                    type:0,//数据类型(0:装进我家;1:方案使用;2热门方案)
                    start: 1, // 当前页码
                    limit: 10 // 每页条数
                },
                listPage:1,//当前页
                totalCount:0,

                option: {
                    title: {
                        text: ''
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data:['']
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
                    ]
                },

                //柱状图
                option2:{
                    title: {
                        text: '热门方案点击次数Top30',
                    },
                    legend: {
                        data:["方案点击次数"]
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    xAxis: {
                        type: 'value'
                    },
                    yAxis: {
                        type: 'category',
                        data: []
                    },
                    series: [{
                        data: [],
                        type: 'bar'
                    }]
                },
            }
        },
        components: {
        },


        created(){
            this.getDesignPlanSum();
            this.getDesignPlanSum();
            this.getAll();
            this.getList();
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
                    if (!this.multipleSelection.length){
                        this.$alert('无数据供导出！','提示',{
                            confirmButtonText: '确定',
                        });
                    }else{
                        const { export_json_to_excel } = require('@/vendor/Export2Excel');
                        if (this.isInMyHomeUser){
                            const tHeader = ['时间','装进我家人数', '装进我家次数'];
                            const filterVal = ['date','onekeyUserCount', 'onekeyCount'];
                            const list = this.multipleSelection;
                            //alert(JSON.stringify(list));
                            const data = this.formatJson(filterVal, list);
                            //alert(JSON.stringify(data));
                            export_json_to_excel(tHeader, data, '装进我家数据');
                        }else if(this.isPlanUseData){
                            const tHeader = ['时间','点击方案次数','点击方案人数','装进我家次数','进入720次数','方案收藏次数'];
                            const filterVal = ['date','clickCount','clickUserCount','onekeyCount','enterPanoramaCount','collectCount'];
                            const list = this.multipleSelection;
                            //alert(JSON.stringify(list));
                            const data = this.formatJson(filterVal, list);
                            //alert(JSON.stringify(data));
                            export_json_to_excel(tHeader, data, '方案使用数据');
                        }else{
                            const tHeader = ['产品替换次数','方案编码','装进我家次数','进入720次数','点击次数','方案名称'];
                            const filterVal = ['productReplaceCount','designCode','onekeyUserCount','enterPanoramaCount','clickCount','designName'];
                            const list = this.multipleSelection;
                            //alert(JSON.stringify(list));
                            const data = this.formatJson(filterVal, list);
                            //alert(JSON.stringify(data));
                            export_json_to_excel(tHeader, data, '热门方案');
                        }
                    }
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },

            // 分页模块f
            handleSizeChange: function (size) {
                this.queryList.limit = size;
                this.queryList.start = 1;
                if (this.isHotPlan){
                    this.getHotDesign();
                }else{
                    this.getList();
                }
                this.listPage=1;
            },
            handleCurrentChange: function (currentPage) {
                this.queryList.start = currentPage;
                this.loading = true;
                if (this.isHotPlan){
                    this.getHotDesign();
                }else{
                    this.getList();
                }
                this.listPage=currentPage;
            },


            getExcel(){
                if (this.isHotPlan){
                    this.API.getHotDesignList({
                            dateType: this.radios,
                            type: this.queryList.type,
                            start: 1,
                            limit: 9999
                        }
                    ).then((res) => {
                        if (res.success) {
                            this.multipleSelection=res.datalist;
                        }
                    })
                }else{
                    this.API.getPlanAnalysisList({
                        dateType:this.radios,
                        type: this.queryList.type,
                        start: 1,
                        limit: 9999
                    }).then((res) => {
                        if (res.success) {
                            this.multipleSelection=res.datalist;
                        }
                    })
                }
            },


            //数据查询
            getList() {
                this.API.getPlanAnalysisList({
                    dateType:this.radios,
                    type: this.queryList.type,
                    start: this.queryList.start,
                    limit: this.queryList.limit
                }).then((res) => {
                    if (res.success) {
                        this.totalCount=res.totalCount;
                        this.detailList=res.datalist;
                    }else {
                        this.$message({
                            type: "info",
                            message: res.message
                        });
                    }
                })
            },

            getAll(){
                this.API.getPlanAnalysisAll({
                    dateType:this.radios,
                    type: this.queryList.type,
                    start: this.queryList.start,
                    limit: this.queryList.limit
                }).then((res) => {
                    if (res.success) {
                        if (this.queryList.type===0){//装进我家
                            this.option.legend.data=["装进我家人数"];
                            this.option.series[0].name="装进我家人数";
                            for (var i = 0; i < res.datalist.length; i++) {
                                this.option.xAxis.data.push(res.datalist[i].date);
                                this.option.series[0].data.push(res.datalist[i].onekeyUserCount);
                            }
                        }else{//方案使用情况
                            this.option.legend.data=["方案点击次数"];
                            this.option.series[0].name="方案点击次数";
                            for (var i = 0; i < res.datalist.length; i++) {
                                this.option.xAxis.data.push(res.datalist[i].date);
                                this.option.series[0].data.push(res.datalist[i].clickCount);
                            }
                        }
                    }else {
                        this.$message({
                            type: "info",
                            message: res.message
                        });
                    }
                })
            },

            handleClick(tab, event) {
                if (tab.name === "first") {
                    this.isInMyHomeUser = true;
                    this.isPlanUseData = false;
                    this.isHotPlan = false;
                    this.queryList.type=0;
                    this.detailList=[];
                    this.totalCount=0;
                    this.summarizeDataList=[];
                    this.option.xAxis.data=[];
                    this.option.series[0].data=[];
                    this.multipleSelection=[];
                    this.option.legend.data=[];
                    this.getDesignPlanSum();
                    this.getAll();
                    this.getList();
                    this.getExcel();
                }
                if (tab.name === "second") {
                    this.isInMyHomeUser = false;
                    this.isPlanUseData = true;
                    this.isHotPlan = false;
                    this.queryList.type=1;
                    this.summarizeDataList=[];
                    this.totalCount=0;
                    this.detailList=[];
                    this.option.xAxis.data=[];
                    this.option.series[0].data=[];
                    this.multipleSelection=[];
                    this.option.legend.data=[];
                    this.getDesignPlanSum();
                    this.getAll();
                    this.getList();
                    this.getExcel();
                }
                if (tab.name === "third") {
                    this.isInMyHomeUser = false;
                    this.isPlanUseData = false;
                    this.isHotPlan = true;
                    this.queryList.dateType = this.radios;
                    this.queryList.type = 2;//热门方案
                    this.totalCount=0;
                    this.detailList=[];
                    this.summarizeDataList=[];
                    this.top10List=[];
                    this.option2.yAxis.data=[];
                    this.option2.series[0].data=[];
                    this.multipleSelection=[];
                    this.getHotDesign();
                    this.getDesignTop10();
                    this.getExcel();
                }
            },

            //时间改变
            dataChange(dataType) {
                this.radios = dataType;
                this.queryList.start=1;
                this.listPage=1;
                if (this.isHotPlan){
                    //请求之前将上次请求结果置空
                    this.totalCount=0;
                    this.top10List=[];
                    this.detailList=[];
                    this.summarizeDataList=[];
                    this.option2.yAxis.data=[];
                    this.option2.series[0].data=[];
                    this.multipleSelection=[];
                    this.getDesignTop10();
                    this.getHotDesign();
                    this.getExcel();
                }else{
                    this.summarizeDataList=[];
                    this.detailList=[];
                    this.totalCount=0;
                    this.option.xAxis.data=[];
                    this.option.series[0].data=[];
                    this.multipleSelection=[];
                    this.getDesignPlanSum();
                    this.getAll();
                    this.getList();
                    this.getExcel();
                }
            },

            //获取热门方案
            getHotDesign() {
                this.API.getHotDesignList({
                        dateType: this.radios,
                        type: this.queryList.type,
                        start: this.queryList.start,
                        limit: this.queryList.limit
                    }
                ).then((res) => {
                    if (res.success) {
                        this.isHotPlan=true;
                        this.totalCount=res.totalCount;
                        this.detailList=res.datalist;
                    }else{
                        this.$message({
                            type: "info",
                            message: res.message
                        });
                    }
                })
            },

            //获取热门方案Top10
            getDesignTop10(){
                this.API.getHotDesignTop10({
                    dateType: this.radios,
                    type: this.queryList.type}
                ).then((res) => {
                    if (res.success) {
                        this.isHotPlan=true;
                        this.top10List=res.datalist;
                        for (var i = 0; i < this.top10List.length; i++) {
                            this.option2.yAxis.data.push(this.top10List[i].designName);
                            this.option2.series[0].data.push(this.top10List[i].clickCount);
                        }
                    }else{
                        this.$message({
                            type: "info",
                            message: res.message
                        });
                    }
                })
            },

            //方案使用数据汇总
            getDesignPlanSum() {
                this.API.getPlanAnalysisSum({
                    dateType:this.radios,
                    type: this.queryList.type
                }).then((res) => {
                    if (res.success) {
                        this.summarizeDataList = res.datalist;
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
        .chart1{
            padding: 20px;
            background: #fff;
        }
        .chart2{
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

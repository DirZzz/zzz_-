<template>
    <page class="pageStatisticsDetail">
        <div class="nav">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/dataAnalysis/pageStatistics',query:{} }">页面统计</el-breadcrumb-item>
                <el-breadcrumb-item>详情分析</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="detail-content">
            <div class="detail-item">
                <span>时间区间：</span>
                <el-date-picker
                        v-model="pickTime"
                        type="daterange"
                        format="yyyy 年 MM 月 dd 日"
                        value-format="yyyy-MM-dd"
                        :clearable="false"
                        unlink-panels
                        @change="timeChange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
            </div>
            <div class="detail-item" style="margin-left: 30px">
                <span>用户来源渠道：</span>
                <el-select v-model="userSource" clearable placeholder="请选择"  @change="channelChange">
                    <el-option
                            v-for="item in userOrigin"
                            :key="item.type"
                            :label="item.name"
                            :value="item.type">
                    </el-option>
                </el-select>
            </div>
            <div class="pageStatistics-item" style="margin-left:30px">
                <span>用户类别：</span>
                <el-select v-model="userType" clearable placeholder="请选择" @change="userTypeChange">
                    <el-option 
                        v-for="item in userTypeOrigin"
                        :key="item.type"
                        :label="item.name"
                        :value="item.type"
                    >   
                    </el-option>
                </el-select>
            </div>
        </div>
        <div class="chartBox">
            <v-chart style="width: 100%" :options="option"></v-chart>
        </div>
        <div class="excelBtn">
            <el-button type="primary" @click="exportExcel">导出 Excel</el-button>
        </div>
        <div class="pageStatistics-list">
            <el-table
                    :data="tableData"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%"
                    align="center"
            >
                <el-table-column prop="number" label="编号" align="center"></el-table-column>
                <el-table-column prop="dateValue" label="日期" align="center"></el-table-column>
                <el-table-column prop="pageProperty" label="前置页面" header-align="center" align="center"></el-table-column>
                <el-table-column prop="pagePropertyName" label="前置页面名称" header-align="center" align="center"></el-table-column>

                <!--<el-table-column prop="name" label="当前页面" header-align="center" align="center">-->

                <!--</el-table-column>-->

                <!--<el-table-column label="当前页面名称" header-align="center" align="center">-->

                <!--</el-table-column>-->

                <el-table-column  prop="pv" label="点击次数" header-align="center" align="center"></el-table-column>

                <el-table-column prop="uv" label="点击人数" header-align="center" align="center"></el-table-column>

                <el-table-column label="平均访问时长" header-align="center" align="center">
                    <template slot-scope="scope">
                        <span>{{scope.row.timeOnPage}}秒</span>
                    </template>
                </el-table-column>

                <el-table-column prop="bouncePv" label="跳出页面次数" header-align="center" align="center"></el-table-column>
                
                <el-table-column prop="bounceUv" label="跳出人数" header-align="center" align="center"></el-table-column>

                <el-table-column prop="bouncePercent" label="跳出率" header-align="center" align="center"></el-table-column>

                <!--<el-table-column label="退出率" header-align="center" align="center"></el-table-column>-->
            </el-table>
            <div class="pagination">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="currentPage"
                        :page-size="limit"
                        :page-sizes="[10, 20, 30, 40, 50]"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </page>
</template>

<script>
    import 'echarts/lib/chart/line'
    import 'echarts/lib/component/title'
    import 'echarts/lib/component/legend'
    import 'echarts/lib/component/polar'
    import 'echarts/lib/component/grid'
    import moment, { localeData } from 'moment'
    import { map } from 'lodash'
    export default {
        name: "detail",
        data(){
            return{
                currentPage:1,
                limit:10,
                radio:'1',
                pickTime:[],
                tableData:[],
                userSource:'',
                userType:'',
                userTypeOrigin:[
                    { name:'全部',type:''},
                    { name:'新用户',type:'1'},
                    { name:'老用户',type:'2'}
                ],
                appletType:'随选网',
                loading:true,
                userOrigin:[
                    { name:'全部',type:'' },
                    { name:'淘新闻',type:'taoxinwen' },
                    { name:'朋友圈',type:'wenxin' },
                    { name:'三度空间',type:'sandu' },
                    { name:'其他',type:'other' }
                ],
                total:0,
                option:{
                    tooltip:{
                        trigger: 'item', // 触发类型，默认数据触发，可选为：'axis' item
                        axisPointer: {
                            // 坐标轴指示器，坐标轴触发有效
                            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
                            label:'cross',
                            show:false
                        },
                        formatter:function (params){
                            let res
                            if(params.componentIndex == 5){
                                return res = `<div><p>${params.name}</p><p>${params.marker}${params.seriesName}:${params.value}%</p></div>`
                            }else{
                                return res =`<div><p>${params.name}</p><p>${params.marker}${params.seriesName}:${params.value}</p></div>`
                            }
                        }
                    },
                    xAxis: {
                        type: 'category',
                        data: [],
                        boundaryGap: false,
                        name: '日期',   // x轴名称
                    },
                    yAxis: {
                        type: 'value'
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    legend:{
                        data:['点击次数','点击人数','平均访问时长','跳出页面次数','跳出人数','跳出率'],
                        top:20
                    },
                    series: [{
                        name:'点击次数',
                        data: [],
                        type: 'line',
                        smooth: true,
                        metrics:['点击次数'],
                    },
                    {
                        name:'点击人数',
                        data: [],
                        type: 'line',
                        smooth: true
                    },
                    {
                        name:'平均访问时长',
                        data: [],
                        type: 'line',
                        smooth: true
                    },
                    {
                        name:'跳出页面次数',
                        data:[],
                        type:'line',
                        smooth:true
                    },
                    {
                        name:'跳出人数',
                        data:[],
                        type:'line',
                        smooth:true
                    },
                    {
                        name:'跳出率',
                        data:[],
                        type:'line',
                        smooth:true
                    }],
                    autoResize:true
                }
            }
        },
        created(){
            let lastPageData = JSON.parse(sessionStorage.getItem('lastPageData'))
            this.pickTime[0] = lastPageData.pickTime[0],
            this.pickTime[1] = lastPageData.pickTime[1],
            this.userSource = lastPageData.userSource,
            this.userType = lastPageData.userType,
            this.appletType = lastPageData.appletType
            // this.pickTime[0]=moment(new Date()).subtract(30, 'days').format("YYYY-MM-DD"); //之前一个月的时间
            // this.pickTime[1]=moment(new Date()).format("YYYY-MM-DD"); //获取当前的时间
            this.pageList();
        },
        mounted(){
        },
        methods:{
            pageList(){
                this.loading=true;
                this.API.pageDetailList({
                    startTime:moment(this.pickTime[0]).format("YYYY-MM-DD"),
                    endTime:moment(this.pickTime[1]).format("YYYY-MM-DD"),
                    appId:this.appletType==='随选网'?'wx42e6b214e6cdaed3':'wxf1113bc672fe7112',
                    channel:this.userSource,
                    userType:this.userType,
                    pageProperty:this.$route.query.id,
                    page:this.currentPage,
                    limit:this.limit
                }).then(res=>{
                    if(res.success){
                        this.loading=false;
                        this.tableData=res.datalist;
                        this.total=res.totalCount;
                        this.option.xAxis.data=map(res.datalist,'dateValue');
                        this.option.series[0].data=map(res.datalist,'pv');
                        this.option.series[1].data=map(res.datalist,'uv');
                        this.option.series[2].data=map(res.datalist,'timeOnPage');
                        this.option.series[3].data=map(res.datalist,'bouncePv') //跳出次数
                        this.option.series[4].data=map(res.datalist,'bounceUv') //跳出人数
                        this.option.series[5].data=res.datalist.map(e=>e.bouncePercent.replace('%',''))   //跳出率
                        //console.log(this.option.xAxis.data)
                    }else {
                        this.$message(res.message);
                    }
                })
            },
            exportExcel(){
                this.API.pageExportDtailListExcel({
                    startTime:moment(this.pickTime[0]).format("YYYY-MM-DD"),
                    endTime:moment(this.pickTime[1]).format("YYYY-MM-DD"),
                    appId:this.appletType==='随选网'?'wx42e6b214e6cdaed3':'wxf1113bc672fe7112',
                    channel:this.userSource,
                    userType:this.userType,
                    pageProperty:this.$route.query.id,
                    page:this.currentPage,
                    limit:this.limit
                }).then(res=>{
                    let url=window.URL.createObjectURL(res);
                    let link=document.createElement('a');
                    link.style.display="none";
                    link.href=url;
                    link.setAttribute('download','页面统计详情信息导出.xls');
                    document.body.appendChild(link);
                    link.click();
                })
            },
            channelChange(){
                this.pageList();
            },
            userTypeChange(){
                this.pageList();
            },
            userAppletTypeChange(){
                this.pageList();
            },
            handleSizeChange(val){
                this.limit=val;
                this.pageList();
            },
            handleCurrentChange(val){
                this.currentPage=val;
                this.pageList();
            },
            timeChange(){
                this.currentPage=1;
                this.pageList();
            }
        }
    }
</script>

<style scoped lang="scss">
     .pageStatisticsDetail{
         .detail-content{
              margin-top: 30px;
              display: flex;
         }
         .pagination{
             text-align: right;
             margin-top: 20px;
         }
         .excelBtn{
            text-align: right;
         }
     }

</style>
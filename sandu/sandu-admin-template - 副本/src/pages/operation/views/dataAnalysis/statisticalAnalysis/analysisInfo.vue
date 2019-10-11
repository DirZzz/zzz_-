<template>
    <page class="pageStatistics">
        <el-button type="primary" round @click="toLink('/dataAnalysis/statisticalAnalysis')">返回</el-button>
        <div class="pageStatistics-header">
            <div class="pageStatistics-item" style="width: 520px">
                <span>时间区间：</span>
                <el-date-picker
                        v-model="pickTime"
                        type="daterange"
                        format="yyyy 年 MM 月 dd 日"
                        value-format="yyyy-MM-dd"
                        :clearable="false"
                        @change="timeChange"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
            </div>
            <div class="pageStatistics-item">
                <span>渠道：</span>
                <el-select v-model="userSource" clearable placeholder="请选择" @change="channelChange">
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
            <div class="pageStatistics-item print">
                <el-button type="primary" round @click="exportExcel">导出</el-button>
            </div>
        </div>
        <el-radio-group v-model="selectType" @change='activeTypeChange'>
            <el-radio :label="item.key" v-for="item in activeTypeObj">{{item.val}}</el-radio>
        </el-radio-group>
        <div class="pageStatistics-list">
            <el-table
                    :data="tableData"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%"
                    align="center">
                <template v-for="col in cols">
                    <el-table-column :label="col.label" :prop="col.prop" align="center"></el-table-column>
                </template>
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
    import moment from 'moment'
    export default {
        name: "elementStatistics",
        data(){
            return{
                pickTime:[],
                activeTypeObj:[
                    {key: 0,val:'UV'},
                    {key: 1,val:'PV'},
                ],
                tableData: [
                    // {
                    //     node: "0051",
                    //     name: " 机库顶",
                    //     type: "UWB",
                    //     status: "正常",
                    //     coordinate: "12.21,34.45,34.6"
                    // },
                    // {
                    //     node: "0061",
                    //     name: "机库门",
                    //     type: "GPS",
                    //     status: "低电",
                    //     coordinate: "45.41,67.45,78.6"
                    // }
                ],
                cols: [
                    // { label: "节点编号", prop: "node"},
                    // { label: "名称", prop: "name" },
                    // { label: "类型", prop: "type"},
                    // { label: "坐标", prop: "coordinate" }
                ],
                currentPage:1,
                limit:10,
                userSource:'',
                userType:'',
                userTypeOrigin:[
                    { name:'全部',type:''},
                    { name:'新用户',type:'1'},
                    { name:'老用户',type:'2'}
                ],
                loading:true,
                userOrigin:[
                    { name:'全部',type:'' },
                    { name:'淘新闻',type:'taoxinwen' },
                    { name:'朋友圈',type:'wenxin' },
                    { name:'三度空间',type:'sandu' },
                    { name:'其他',type:'other' }
                ],
                total:0,
                selectType:0  //	0: 查uv  1: 查pv
            }
        },
        created(){
            this.pickTime[0]=moment(new Date()).subtract(30, 'days').format("YYYY-MM-DD"); //之前一个月的时间
            this.pickTime[1]=moment(new Date()).format("YYYY-MM-DD"); //获取当前的时间
            this.pageList();
        },
        methods:{
            pageList(){
                this.loading=true;
                this.API.statisticViewAnalysis({
                    startTime:moment(this.pickTime[0]).format("YYYY-MM-DD"),
                    endTime:moment(this.pickTime[1]).format("YYYY-MM-DD"),
                    selectType:this.selectType,
                    channel:this.userSource,
                    userType:this.userType,
                    funnelId:this.$route.query.id,
                    page:this.currentPage,
                    limit:this.limit
                }).then(res=>{
                    if(res.success){
                        this.loading=false;
                        this.tableData=[];
                        this.cols=[];
                        //c重装数组格式 表头
                        res.datalist[0].list.map((item,index)=>{
                            this.cols.push({
                                label:item,
                                prop:`prop_${index}`
                            })
                        });
                        res.datalist.splice(0,1); //删除标题
                        res.datalist.map((item,index)=>{
                            let obj={};
                            item.list.map((child,childIndex)=>{
                                obj[`prop_${childIndex}`]=child;
                            })
                            this.tableData.push(obj)
                        })
                        this.total=res.totalCount;
                    }else {
                        this.$message(res.message);
                    }
                })
            },
            exportExcel(){
                this.API.statisticExportListExcel({
                    startTime:moment(this.pickTime[0]).format("YYYY-MM-DD"),
                    endTime:moment(this.pickTime[1]).format("YYYY-MM-DD"),
                    channel:this.userSource,
                    userType:this.userType,
                    selectType:this.selectType,
                    funnelId:this.$route.query.id,
                    page:this.currentPage,
                    limit:this.limit
                }).then(res=>{
                    let url=window.URL.createObjectURL(res);
                    let link=document.createElement('a');
                    link.style.display="none";
                    link.href=url;
                    link.setAttribute('download','统计分析信息导出.xls');
                    document.body.appendChild(link);
                    link.click();
                })
            },
            activeTypeChange(){
                this.currentPage=1;
                this.pageList();
            },
            channelChange(){
                this.pageList();
            },
            userTypeChange(){
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
            },
            toLink(path,query){
                this.$router.push({path:path,query:query});
            },
        }
    }
</script>

<style scoped lang="scss">
    .pageStatistics{
        width: 100%;
        .pageStatistics-header{
            margin: 30px auto;
            height: 40px;
            .pageStatistics-item{
                float: left;
            }
            .print{
                float: right;
            }
        }
        .pagination{
            text-align: right;
            margin-top: 20px;
        }
    }
    .pageStatistics-list{
        margin-top: 10px;
    }
</style>
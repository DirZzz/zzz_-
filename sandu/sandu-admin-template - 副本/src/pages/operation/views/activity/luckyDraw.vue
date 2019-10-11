<template>
    <div id="activity">
        <div class="mains">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><span  @click="$router.push('/wheel')" class="toLi">幸运大转盘活动</span></el-breadcrumb-item>
                <el-breadcrumb-item>抽奖记录</el-breadcrumb-item>
            </el-breadcrumb>
            <p class="hei20"></p>
            <div class="mainBody">
                <div class="rowTOp">
                    <el-form :inline="true" size="mini" :model="params" class="demo-form-inline">
                        
                        <el-form-item label="用户ID">
                            <el-input v-model="params.openId" placeholder="用户ID" @change="changeFun('openId')" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="用户手机号">
                            <el-input v-model="params.mobile" placeholder="用户手机号" @change="changeFun('mobile')" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="昵称">
                            <el-input v-model="params.nickname" placeholder="昵称" @change="changeFun('nickname')" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="奖品">
                            <el-input v-model="params.prizeName" placeholder="奖品" @change="changeFun('prizeName')" clearable></el-input>
                        </el-form-item>
                        <el-form-item label="区域">
                            <areaSelect ref="areaRef" @provinceCode="provincesChange" @cityCode="citysChange" @areaCode="areasChange"></areaSelect>
                        </el-form-item>
                        <el-form-item label="是否中奖">
                            <el-select v-model="params.awardsStatus" placeholder="请选择" >
                                <el-option
                                v-for="item in optionObj2"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="状态">
                            <el-select v-model="params.deliverStatus" placeholder="请选择" >
                                <el-option
                                v-for="item in optionObj3"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="活动时间">
                            <el-date-picker
                                v-model="lotteryTime"
                                type="daterange"
                                @change="lotteryTimeFun"
                                range-separator="至"
                                :clearable='false'
                                value-format="yyyy-MM-dd"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit">查询</el-button>
                            <el-button @click="reset">重置</el-button>
                        </el-form-item>
                    </el-form>
                </div>
                <div class="tableDataFrame">
                    <el-table :data="dataList" :header-cell-style="rowStyle" element-loading-text="拼命加载中"
                              element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)"
                              tooltip-effect="dark">
                        <el-table-column prop="openId" label="微信信息" align="center" width="200px">
                            <template slot-scope="scope"><p>微信id:{{scope.row.openId}}</p><p>微信名称:{{scope.row.nickName}}</p></template>
                        </el-table-column>
                        <el-table-column prop="mobile" label="手机号码" align="center">
                            <template slot-scope="scope">{{scope.row.mobile}}</template>
                        </el-table-column>
                        <el-table-column prop="awardsStatus" label="活动状态" align="center" width="100px">
                            <template slot-scope="scope">
                                <span v-if="scope.row.awardsStatus==0">未中奖</span>
                                <span v-if="scope.row.awardsStatus==5">未领取</span>
                                <span v-if="scope.row.awardsStatus==10">待发货</span>
                                <span v-if="scope.row.awardsStatus==20">已发放</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="prizeName" label="奖品" align="center">
                            <template slot-scope="scope">{{scope.row.prizeName}}</template>
                        </el-table-column>
                        <el-table-column prop="deliverStatus" label="发货状态" align="center" width="100px">
                            <template slot-scope="scope">
                                <span v-if="scope.row.deliverStatus==0">未发货</span>
                                <span v-if="scope.row.deliverStatus==10">已发货</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="receiver" label="收货人" align="center">
                            <template slot-scope="scope">{{scope.row.receiver}}</template>
                        </el-table-column>
                        <el-table-column prop="provinceCityAreaName" label="区域" align="center">
                            <template slot-scope="scope">{{scope.row.provinceCityAreaName}}</template>
                        </el-table-column>
                        <el-table-column prop="address" label="用户地址" align="center">
                            <template slot-scope="scope">{{scope.row.provinceCityAreaName||''}}{{scope.row.address||''}}</template>
                        </el-table-column>
                        <el-table-column prop="lotteryTime" label="抽奖时间" align="center">
                            <template slot-scope="scope">{{scope.row.lotteryTime}}</template>
                        </el-table-column>
                    </el-table>
                    <div class="pageCount">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="params.pageNum"
                                :page-sizes="[10, 20, 30, 40]"
                                :page-size="params.pageSize"
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
    import areaSelect from '&/components/areaSelect'
    import qs from 'qs'
    export default {
        name:'activity',
        components:{
            areaSelect
        },
        data() {
            return {
                optionObj:[
                    {value:0,label:'未中奖'},
                    {value:1,label:'度币'},
                    {value:2,label:'现金'},
                    {value:3,label:'话费'},
                    {value:4,label:'资料'},
                    {value:5,label:'物品'}
                ], 
                optionObj2:[
                    {value:0,label:'未中奖'},
                    {value:5,label:'未领奖'},
                    {value:10,label:'待发放'},
                    {value:20,label:'已发放'},
                ],
                optionObj3:[
                    {value:0,label:'未发货'},
                    {value:10,label:'已发货'},
                ],
                dataList:[],
                lotteryTime:[],
                params:{
                    actId:'',
                    openId:undefined,
                    mobile:undefined,
                    nickname:undefined,
                    prizeName: undefined,
                    provinceCode:undefined,
                    cityCode:undefined,
                    areaCode:undefined,
                    lotteryTimeStart:undefined,
                    lotteryTimeEnd:undefined,
                    deliverStatus:undefined,
                    pageNum: 1,
                    pageSize: 10,
                },
                // pickerOptionsStart:{
                //     disabledDate(time) {
                //         return time.getTime() < Date.now() - 8.64e7;
                //     }
                // },
                pickerOptionsEnd:{
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7
                    }
                },
                totalCount:0,
            }
        },
        created(){
            this.params.actId = this.$route.query.id;
            this.getRecordPageList();
        },
        methods: {
            changeFun(key){
                !this.params[key]?this.params[key] = undefined:'';
            },
            lotteryTimeFun(v){
                this.params.lotteryTimeStart = v[0]+' 00:00:00';
                this.params.lotteryTimeEnd = v[1]+' 00:00:00';
            },
            onSubmit(e){
                this.params.pageNum = 1;
                this.getRecordPageList();
            },
            provincesChange(v){
                this.params.provinceCode = v;
            },
            citysChange(v){
                this.params.cityCode = v;
            },
            areasChange(v){
                this.params.areaCode = v;
            },
            reset(){
                this.$refs.areaRef.reset();
                this.lotteryTime = [];
                this.params = {
                    actId:this.$route.query.id,
                    openId:undefined,
                    mobile:undefined,
                    nickname:undefined,
                    prizeName: undefined,
                    provinceCode:undefined,
                    cityCode:undefined,
                    areaCode:undefined,
                    lotteryTimeStart:undefined,
                    lotteryTimeEnd:undefined,
                    deliverStatus:undefined,
                    pageNum: 1,
                    pageSize: 10,
                }
                
                this.getRecordPageList();
            },
            getRecordPageList(){
                this.API.getRecordPageList(this.params).then((res)=>{
                    if(res.success){
                        this.dataList = res.datalist;
                        this.totalCount = res.totalCount
                    }
                })
            },
            showErrorToast(txt){
                this.$notify.error({
                    title: '错误',
                    message: txt,
                    duration: 1000,
                });
            },
            showSuccessToast(txt){
                this.$notify({
                    title: '成功',
                    message: txt,
                    type: 'success',
                    duration: 2000
                });
            },
            showMessaget(txt){
                this.$message.error(txt);
            },
            rowStyle({row, rowIndex}) {
                if (rowIndex === 0) {
                    return {
                        height: "30px!important",
                        background: "#f5f5f5",
                        color: "#666",
                        lineHeight: "80px!important",
                    };
                }
            },
            toLink(url,query){
                this.$router.push({path:url,query: query})
            },
            // 分页模块f
            handleSizeChange: function (size) {
                //this.query.limit = size
                this.params.pageSize = size;
                this.params.pageNum=1;
                this.getRecordPageList()
            },
            handleCurrentChange: function (currentPage) {
                //this.query.page = currentPage;
                this.params.pageNum = currentPage;
                this.loading = true;
                this.getRecordPageList()
            },
        }
    }
</script>

<style scoped lang="scss">
    div{
        box-sizing: border-box;
    }
    .toLi{
        color: #409EFF;
        cursor: pointer;
    }
    .hei20{
        height: 20px;
        width: 100%;
        border-bottom: 1px solid #ededed;
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
            padding:20px;
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
                        margin-left: 30px;
                    }
                }
            }
        }
        
    }

</style>
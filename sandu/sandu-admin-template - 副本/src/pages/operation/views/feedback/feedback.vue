<template>
    <div class="main">
        <!--条件查询-->
        <div class="mainTop">
            <div class="titles">{{roters.name}}反馈管理</div>
            <div class="selRows">
                <div class="selItems phone" v-if="this.platformIds!==16">
                    <span class="label">手机号：</span>
                    <div class="inputs">
                        <el-input
                                placeholder="请输入手机号"
                                v-model="phone"
                                clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems wxnum" v-if="this.platformIds===16">
                    <span class="label">微信名称：</span>
                    <div class="inputs">
                        <el-input
                                placeholder="请输入微信昵称"
                                v-model="wxNickName"
                                clearable>
                        </el-input>
                    </div>
                </div>
                <div class="selItems state">
                    <span class="label">状态：</span>
                    <el-select v-model="dealStatus" placeholder="请选择">
                        <el-option
                        v-for="item in dealStatusList"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </div>
                <div class="selItems results">
                    <span class="label">点评结果：</span>
                    <el-select v-model="estimate" placeholder="请选择">
                        <el-option
                        v-for="item in estimateList"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </div>
                <div class="selItems companys" v-if="this.platformIds!==16" >
                    <span class="label">所属企业：</span>
                    <el-select v-model="companyName" placeholder="请选择">
                        <el-option
                        v-for="(item, index) in feedbackCompany"
                        :key="index"
                        :value="item.companyName">
                        </el-option>
                    </el-select>
                </div>

                <div class="submitBtn" @click="queryFeedbackList('搜索')">搜索</div>
                <div class="submitBtn"  @click="queryFeedbackList('重置')">重置</div>

            </div>
        </div>

        <!-- 返回信息列表 -->
        <div class="mainBody">
            <section>
                <div class="listTitle">反馈信息
                    <div class="right">
                        <el-button @click="handleDownload" size="small" type="primary" :disabled="!multipleSelection.length">导出Excel</el-button>
                    </div>
                </div>
                <div class="tableDataFrame">
                    <el-table
                        :data="feedbackList"
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
                    <el-table-column  type="index" width="50px" label="序号">
                    </el-table-column>
                    <el-table-column prop="username" label="用户信息"
                         :width="(platformIds===14)?'250px':'200px'">
                        <template slot-scope="scope">
                            <div class="userinfoDiv" v-if="platformIds===14 || platformIds===16 ">
                                <img :src="scope.row.headPicPath?(sourceBaseUrl+scope.row.headPicPath):'/static/img/default.4fce4ff.png'" alt="">
                                <p>用户ID：{{ scope.row.openId }}</p>
                                <p>微信名称：{{ scope.row.wxNickName }}</p>
                            </div>
                            <div class="userinfoDiv" style="padding: 0" v-if="platformIds!==14&& platformIds!==16 ">
                                <p>登录名：{{ scope.row.userName }}</p>
                                <p>手机号：{{ scope.row.phone }}</p>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="feedbackTxt" label="反馈内容"  width="300px">
                        <template slot-scope="scope">{{ scope.row.feedbackContent }}</template>
                    </el-table-column>
                    <el-table-column prop="time" label="反馈时间">
                        <template slot-scope="scope">{{ scope.row.gmtCreate }}</template>
                    </el-table-column>
                    <el-table-column prop="states" label="状态" width="70px">
                        <template slot-scope="scope">{{ scope.row.dealStatus?"已处理":"未处理" }}</template>
                    </el-table-column>
                    <el-table-column prop="comment" label="点评" width="70px">
                        <template slot-scope="scope">{{(scope.row.dealStatus==1 && scope.row.estimate!=undefined)?(scope.row.estimate===0?"满意":"不满意"):("未评价") }}</template>
                    </el-table-column>
                    <el-table-column prop="company" label="所属企业" v-if="this.platformIds!==16">
                        <template slot-scope="scope">{{ scope.row.companyName }}</template>
                    </el-table-column>
                    <el-table-column label="操作" width="150px">
                        <template slot-scope="scope">
                            <el-button type="text" @click="showDetailFunc(scope.row.feedbackId)" v-if="scope.row.dealStatus===0">回复</el-button>
                            <el-button type="text" @click="showDetailFunc(scope.row.feedbackId,'详情')">详情</el-button>
                            <el-button type="text" @click="showDel(scope.row.feedbackId)">删除</el-button>
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
            </section>
        </div>


        <!-- 查看/回复详情弹窗 -->
        <div class="formRows" v-if="!showBigImgflag&&showDetail">
            <div class="formBody">
                <div class="titles">{{isDetail?"回复":"详情"}}<i class="el-icon-close" @click="closeDetail"></i></div>
                <div class="bodys">
                    <div class="userRows">
                        <img :src="feedbackDetails.headPicPath?this.sourceBaseUrl+feedbackDetails.headPicPath:'/static/img/default.4fce4ff.png'" alt="">
                        <div>
                            <p>用户ID：{{feedbackDetails.userId}}</p>
                            <p>微信名称：{{feedbackDetails.wxNickName}}</p>
                        </div>
                        <div>
                            <p>联系电话：{{feedbackDetails.phone}}</p>
                            <p v-if="platformIds!==16">所属企业：{{feedbackDetails.companyName}}</p>
                        </div>
                    </div>
                    <div class="txtTitles">
                        反馈信息
                        <span>状态：<i :class="feedbackDetails.dealStatus?'grees':'reds'">{{feedbackDetails.dealStatus===1?'已处理':'未处理'}}</i></span>
                    </div>
                    <div class="feedbackRows">
                        <div class="items">
                            <span>反馈时间：</span>
                            {{feedbackDetails.gmtCreate}}
                        </div>
                        <div class="items" v-if="platformIds!==16" >
                            <span>企业：</span>
                            {{feedbackDetails.companyName}}
                        </div>
                        <div class="items txts">
                            <span>反馈内容：</span>
                            {{feedbackDetails.feedbackContent}}
                        </div>
                        <div class="items">
                            <span>问题截图：</span>
                            <div class="imgRows" v-if="feedbackDetails.feedbackPicPath.length>0">
                                <div class="imgItems" v-for="(item,index) in feedbackDetails.feedbackPicPath">
                                    <div class="zzcs">
                                        <div class="btns" @click="showImgFunc(index)">查看大图</div>
                                    </div>
                                    <img :src="sourceBaseUrl+item.resPath" alt="">
                                </div>
                                <!--<div class="imgItems"
                                     v-if="feedbackDetails.feedbackPicPath.length<4"
                                     v-for="(item2) in (4 - feedbackDetails.feedbackPicPath.length)">
                                    <img src="/static/img/default.4fce4ff.png" alt="">
                                </div>-->
                            </div>
                        </div>
                    </div>
                    <div class="jieguo" v-if="feedbackDetails.dealStatus===1">
                        <span>回复时间：</span>
                        {{feedbackDetails.gmtReply}}
                        <p>是否满意：
                            <span class="blues" v-if="feedbackDetails.dealStatus===1 &&(feedbackDetails.estimate===0||feedbackDetails.estimate===1)">{{feedbackDetails.estimate===0?'满意':'不满意'}}</span>
                        </p>
                    </div>
                    <div class="fkui" v-if="feedbackDetails.dealStatus===1">
                        <span class="txs">回复内容：</span>
                        <div class="rowTxs">
                            {{feedbackDetails.replyContent}}
                        </div>
                    </div>
                    <div class="fkui he156" v-if="feedbackDetails.dealStatus===0&&isDetail">
                        <span class="txs">回复：</span>
                        <div class="rowInp">
                            <textarea placeholder="请输入" maxlength="200" v-model="replyContent"></textarea>
                            <span class="counts">{{replyContent.length}}/200</span>
                        </div>
                    </div>
                </div>
                <div class="bnts" v-if="isDetail">
                    <el-button size="small" @click="closeDetail" v-if="feedbackDetails.dealStatus===0">取消</el-button>
                    <el-button type="primary"  size="small" @click="submitFunc" v-if="feedbackDetails.dealStatus===0&&isDetail">回复</el-button>
                </div>
            </div>
        </div>

        <!-- 查看大图组件 -->
        <showBigImg :flag="showBigImgflag" :obj="bigImgList" :indexActive="showBigImgIndex" :falseFlag="falseFlag" @closeImg="closeImg"></showBigImg>
        <!-- 删除反馈信息弹窗 -->
        <div class="delView" v-if="showDelView">
            <div class="delRows">
                <div class="titles">回复<i class="el-icon-close" @click="closeDel"></i></div>
                <div class="conts">确定要删除此条反馈内容？</div>
                <div class="bnts">
                    <el-button size="small" @click="closeDel">取消</el-button>
                    <el-button type="primary" size="small" @click="delFunc()">确认</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import showBigImg from './conponents/showBigImg';//查看大图
    export default {
        data(){
            return {
                platformIds:'',
                feedbackId:'',
                phone:'',
                openId:'',
                userName:'',
                companyId:'',
                companyName:'',
                wxNickName:'',
                feedbackContent:'',
                dealStatus:'',
                estimate:'',
                gmtCreate:'',
                userId:'',
                totalCount:'',

                roters:{},
                showBigImgflag:false,
                falseFlag: false,
                showBigImgIndex: 0,
                showDetail:false,
                bigImgList:[],
                showDelView: false,
                replyContent:'',
                sourceBaseUrl:'',

                //列表查询入参
                getList: {
                    platformId:'',//平台ID
                    phone: '', // 电话号码
                    estimate: '', // 是否满意
                    dealStatus: '', // 处理状态
                    companyName: '', // 企业名称
                    wxNickName: '', // 微信昵称
                    page: 1, // 页码
                    limit: 10 // 每页条数
                },
                listPage:1,
                //反馈恢复参数
                reply:{
                    id: undefined,
                    replyContent: undefined,
                    userId: undefined,
                },

                //反馈详情
                feedbackDetails:{},
                //反馈列表
                feedbackList:[],
                //stateList:[],

                //反馈企业
                feedbackCompany:[],

                //处理状态
                dealStatusList:[
                    {
                    value: 0,
                    label: '未处理'
                    }, {
                    value: 1,
                    label: '已处理'
                    }
                ],

                //处理结果
                estimateList:[
                    {
                        value: 0,
                        label: '满意'
                    }, {
                        value: 1,
                        label: '不满意'
                    }
                ],

                multipleSelection:[],
                isDetail:true
            }
        },
        watch:{
          '$route':function(e){
              //搜索之前重置搜索框
              this.queryFeedbackList("重置");
              this.userType = e.meta.type;
              this.roters = e;
              this.platformIds = e.meta.type;
              this.getList.platformId= this.platformIds;
              this.getList.page = 1;
              this.getList.limit = 10;
              this.getFeedbackCompany();
              this.getFeedbackList();
          },
        },
        components: {
            showBigImg
        },

        created(){
            //this.getFeedbackCompany();
        },

        methods:{
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
                    const { export_json_to_excel } = require('@/vendor/Export2Excel')
                    const tHeader = ['用户Id','联系电话', '反馈用户openId','反馈用户登录名', '企业Id','企业名称','微信昵称',
                        '处理状态(0:未处理；1:已处理)','点评结果(0:满意；1:不满意)','反馈时间','反馈内容']
                    const filterVal = ['userId','phone', 'openId','userName', 'companyId','companyName','wxNickName',
                        'dealStatus','estimate','gmtCreate','feedbackContent']
                    const list = this.multipleSelection;
                    //alert(JSON.stringify(list));
                    const data = this.formatJson(filterVal, list)
                    //alert(JSON.stringify(data));
                    export_json_to_excel(tHeader, data, '反馈信息')
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },
            // 分页模块f
            handleSizeChange: function (size) {
                //this.query.limit = size
                this.getList.limit = size;
                this.getList.page=1;
                this.getFeedbackList()
            },
            handleCurrentChange: function (currentPage) {
                //this.query.page = currentPage;
                this.getList.page = currentPage;
                this.loading = true;
                this.getFeedbackList()
            },


            //查询反馈企业
            getFeedbackCompany(){
                this.API.feedbackCompany().then(res => {
                    console.log(res,'feedback company');
                    if (res.success){
                        this.feedbackCompany = res.datalist;
                    }
                });
            },

            queryFeedbackList(type){
                if (type==='搜索') {
                    //查询之前将原值置空
                    this.feedbackList = [];
                    this.totalCount = 0;

                    this.getList.platformId=this.platformIds;
                    this.getList.phone=this.phone;
                    this.getList.estimate=this.estimate;
                    this.getList.dealStatus=this.dealStatus;
                    this.getList.companyName=this.companyName;
                    this.getList.wxNickName=this.wxNickName;

                    this.sourceBaseUrl = this.BASE_URL.sourceBaseUrl;
                    this.API.showFeedbackList(this.getList).then(res => {
                        console.log(res,'feedback res');
                        if (res.success){
                            this.listPage = 1;
                            this.feedbackList = res.datalist;
                            this.totalCount = res.totalCount;
                        }
                    });

                }
                if (type==='重置'){
                    this.getList={
                        platformId:this.platformIds,//平台ID
                        phone: undefined, // 电话号码
                        estimate: undefined, // 是否满意
                        dealStatus: undefined, // 处理状态
                        companyName: undefined, // 企业名称
                        wxNickName: undefined, // 微信昵称
                        page: 1, // 页码
                        limit:10 // 每页条数
                    }
                    this.platformId=this.platformIds,//平台ID
                    this.phone= ''; // 电话号码
                    this.estimate= ''; // 是否满意
                    this.dealStatus= ''; // 处理状态
                    this.companyName= ''; // 企业名称
                    this.wxNickName= ''; // 微信昵称
                    this.page = '';
                    this.limit = '';
                    this.sourceBaseUrl = this.BASE_URL.sourceBaseUrl;
                    this.API.showFeedbackList(this.getList).then(res => {
                        console.log(res,'feedback res');
                        if (res.success){
                            this.listPage = 1;
                            this.feedbackList = res.datalist;
                            this.totalCount = res.totalCount;
                        }
                    });
                }

            },

            /*获取反馈信息列表*/
            getFeedbackList(){
                //查询之前将原值置空
                this.feedbackList=[];
                this.totalCount = 0;
                //this.getList.page = '';
                //this.getList.limit = '';
                console.log('data----',this.getList);
                this.sourceBaseUrl = this.BASE_URL.sourceBaseUrl;
                let params = {
                    page : this.getList.page,
                    limit: this.getList.limit,
                    platformId:this.getList.platformId,
                    estimate:this.getList.estimate, // 是否满意
                    dealStatus:this.getList.dealStatus, // 处理状态
                    companyName:this.getList.companyName, // 企业名称
                    wxNickName:this.getList.wxNickName, // 微信昵称
                };

                this.API.showFeedbackList(params).then(res => {
                    console.log(res,'feedback res');
                    if (res.success){
                        this.listPage = params.page;
                        this.feedbackList = res.datalist;
                        this.totalCount = res.totalCount;
                    }
                });
            },

            // 查看大图
            showImgFunc(index){
                let list = [];
                this.feedbackDetails.feedbackPicPath.map(v=>{list.push(v.resPath)})
                this.bigImgList = list;
                this.showBigImgIndex = index
                this.showBigImgflag = true;
            },

            // 查看详情弹窗
            showDetailFunc(e,type){
                if(type=='详情'){
                    this.isDetail = false
                }else{
                    this.isDetail = true
                }
                this.showBigImgflag=false;
                this.showDetail = true;
                this.API.feedbackDetail({feedbackId:e}).then(res => {
                    if (res.success) {
                        this.feedbackDetails=res.obj;
                        //为了回复时取值
                        this.feedbackId=res.obj.feedbackId;
                        this.userId=res.obj.userId;
                    }
                });
                console.log(e);
                this.sourceBaseUrl = this.BASE_URL.sourceBaseUrl;
            },

            // 关闭查看大图
            closeImg(){
                this.showBigImgflag = false;
            },
            // 关闭查看详情
            closeDetail(){
                this.showDetail = false;
            },

            // 执行回复操作
            submitFunc(){
                this.reply.id=this.feedbackId;
                this.reply.replyContent=this.replyContent;
                this.reply.userId=this.userId;
                this.API.feedbackReply(this.reply).then(res => {
                    if (res.success) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        });
                        this.closeDetail();
                        this.getFeedbackList();
                        this.replyContent='';
                        this.reply.replyContent='';
                    } else {
                        this.$message({
                            type: 'error',
                            message: res.message
                        });
                    }
                });
                console.log(e);

            },
            // 关闭删除提示
            closeDel(e){
                this.showDelView = false;
            },
            // 展开删除提示
            showDel(e){
                this.feedbackId =e ;
                this.showDelView = true;
            },


            //执行删除操作
            delFunc(){
                this.API.feedbackDelete({feedbackId:this.feedbackId}).then(res => {
                        if (res.success) {
                            this.$message({
                                type: 'success',
                                message: res.message
                            });
                            this.getFeedbackList();
                        } else {
                            this.$message({
                                type: 'error',
                                message: res.message
                            });
                        }
                });
                console.log('删除成功！！！');
                this.showDelView = false;
            }
        },

    }
</script>

<style scoped lang="scss">
div{
    box-sizing: border-box;
}
.reds{
    color: #ff4956 !important;
}
.grees{
    color: #67c23a !important;
}
.blues{
    color: #3399ff !important;
}
@media screen and (max-width: 1620px) {
    .selItems{
        margin-right: 30px !important;
        .inputs{
            width: 180px !important;
        }
    }
    .phone{
        width: 242px !important;
    }
    .wxnum,.companys,.results{
        width: 256px !important;
    }
    .state{
        width: 228px !important;
    }
}
@media screen and (max-width: 1480px) {
    .selItems{
        margin-right: 20px !important;
        .inputs{
            width: 160px !important;
            font-size: 13px;
        }
    }
    .phone{
        width: 222px !important;
    }
    .wxnum,.companys,.results{
        width: 236px !important;
    }
    .state{
        width: 208px !important;
    }
}
@media screen and (max-width: 1390px) {
    .selRows{
        padding: 24px 10px!important;
    }
    .selItems{
        margin-right: 10px !important;
    }
}
.main{
    width: 100%;
    height: 100%;
    padding: 20px;
    font-size: 16px;
    color: #333333;
    .mainTop{
        width: 100%;
        background-color: #ffffff;
        border-radius: 2px;
        margin-bottom: 20px;
        .titles{
            height: 55px;
            border-bottom: 1px solid #eaeaea;
            padding: 0 30px;
            line-height: 55px;
            text-align: left;
        }
        .selRows{
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
                margin-left: 20px;
            }
            .selItems{
                height: 32px;
                line-height: 32px;
                box-sizing: border-box;
                margin-right: 48px;
                position: relative;
                float: left;
                .label{
                    font-size: 14px;
                    height: 32px;
                    position: absolute;
                    left: 0;
                    top: 0;
                }
                .inputs{
                    display: block;
                    width: 200px;
                    height: 32px;
                    line-height: 32px;
                    box-sizing: border-box;
                }
                .el-select{
                    height: 32px;
                    line-height: 32px;
                }
            }
            .wxnum{
                .inputs{
                    background-color: #ffffff;
                    border-radius: 4px;
                    input{
                        display: block;
                        width: 100%;
                        height: 100%;
                        font-size: 14px;
                    }
                }
            }
            .phone{
                width: 262px;
                padding-left: 62px;
                label{
                    width: 62px;
                }
                .inputs{
                    background-color: #ffffff;
                    border-radius: 4px;
                    input{
                        display: block;
                        width: 100%;
                        height: 100%;
                        font-size: 14px;
                    }
                }

            }
            .state{
                width: 248px;
                padding-left: 48px;
                label{
                    width: 48px;
                }
            }
            .wxnum,.companys,.results{
                width: 276px;
                padding-left: 76px;
                label{
                    width: 76px;
                }
            }
        }
        margin-bottom: 20px;
    }
    .mainBody{
        width: 100%;
        .tableDataFrame{
            color: #666;
        }
        .userinfoDiv{
            width: 100%;
            height: 44px;
            position: relative;
            padding-left: 54px;
            box-sizing: border-box;
            img{
                position: absolute;
                width: 44px;
                height: 44px;
                border-radius: 50%;
                top: 0;
                left: 0;
                z-index: 1;
            }
            p{
                font-size: 14px;
                line-height: 1;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
                &:first-child{
                    margin-bottom: 10px;
                }
            }
        }
    }
    .formRows{
        width: 100%;
        height: 100%;
        position: fixed;
        left:0;
        top: 0;
        background: rgba(0,0,0,0.7);
        z-index: 9999;
        .formBody{
            width: 620px;
            padding: 54px 0 52px;
            background: #fff;
            box-sizing: border-box;
            border-radius: 4px;
            position: absolute;
            left: 50%;
            top: 50%;
            transform:translate(-50%,-50%);
            .titles{
                width: 100%;
                height: 54px;
                line-height: 54px;
                box-sizing: border-box;
                padding: 0 24px;
                font-size: 16px;
                color: #333333;
                position: absolute;
                text-align: left;
                top: 0;
                left: 0;
                border-bottom: 1px solid #eaeaea;
                i{
                    position: absolute;
                    width: 54px;
                    height: 54px;
                    line-height: 54px;
                    text-align: center;
                    right: 0;
                    color: #8c8c8c;
                    font-size: 12px;
                    top: 0;
                    cursor: pointer;
                }
            }
            .bodys{
                width: 100%;
                padding:30px;
                font-size: 14px;
                text-align: left;
                .userRows{
                    width: 100%;
                    height: 80px;
	                background-color: #f5f5f5;
                    padding: 20px;
                    padding-left: 80px;
                    position: relative;
                    margin-bottom: 20px;
                    img{
                        position: absolute;
                        width: 48px;
                        height: 48px;
                        border-radius: 50%;
                        left: 20px;
                        top: 16px;
                    }
                    div{
                        width: 50%;
                        float: left;
                        height: 100%;
                        p{
                            width: 100%;
                            line-height: 1;
                            text-align: left;
                            &:first-child{
                                margin-bottom: 7px;
                            }
                        }
                    }
                }
                .txtTitles{
                    line-height: 1;
                    position: relative;
                    margin-bottom: 16px;
                    span{
                        position: absolute;
                        top: 0;
                        right: 0;
                    }
                }
                .feedbackRows{
                    width: 100%;
                    padding: 20px;
                    background-color: #f5f5f5;
                    margin-bottom: 20px;
                    .txts{
                        line-height: 20px;
                        display: block;
                        width: 70px;
                        text-align: left;
                        word-break: break-all;
                    }
                    .items{
                        width: 100%;
                        position: relative;
                        padding-left: 76px;
                        margin-bottom: 17px;
                        color: #333;
                        font-size: 14px;
                        min-height: 14px;
                        &:last-child{
                            margin: 0;
                        }
                        span{
                            text-align: right;
                            display: block;
                            width: 70px;
                            position: absolute;
                            left: 0;
                            top: 0;
                            color: #666666;
                        }
                        .imgRows{
                            height: 106px;
                            .imgItems{
                                width: 106px;
                                height: 106px;
                                float: left;
                                margin-right: 6px;
                                &:last-child{
                                    margin: 0;
                                }
                                position: relative;
                                &:hover .zzcs{
                                    opacity: 1;
                                }
                                img{
                                    width: 100%;
                                    height: 100%;
                                }
                                .zzcs{
                                    width: 100%;
                                    height: 100%;
                                    position: absolute;
                                    left: 0;
                                    top: 0;
                                    background: rgba(0,0,0,0.7);
                                    opacity: 0;
                                    transition-duration: .3s;
                                    .btns{
                                        width: 72px;
                                        height: 24px;
                                        text-align: center;
                                        line-height: 24px;
                                        cursor: pointer;
                                        color: #fff;
                                        font-size: 12px;
                                        position: absolute;
                                        left: 50%;
                                        top: 50%;
                                        margin-left: -36px;
                                        margin-top: -12px;
                                        background-color: #3399ff;
                                        border-radius: 12px;
                                    }
                                }
                            }
                        }
                    }
                }
                .jieguo{
                    margin-bottom: 16px;
                    height: 14px;
                    line-height: 1;
                    p{
                        width: 50%;
                        float: left;
                    }
                }
                .he156{
                    height: 156px;
                }
                .fkui{
                    width: 100%;
                    padding-left: 80px;
                    position: relative;
                    min-height: 24px;
                    line-height: 24px;
                    span.txs{
                        position: absolute;
                        left: 0;
                        top: 0;
                        color: #666666;
                    }
                    .rowTxs{
                        line-height: 20px;
                        word-wrap: break-word;
                    }
                    .rowInp{
                        width: 100%;
                        height: 100%;
                        background-color: #ffffff;
                        border-radius: 4px;
                        border: solid 1px #d9d9d9;
                        padding: 0 10px;
                        padding-bottom: 22px;
                        textarea{
                            max-width: 100%;
                            max-height: 100%;
                            min-width: 100%;
                            min-height: 100%;
                            line-height: 24px;
                            border: none;
                            font-size: 14px;
                            color: #333;
                            background: rgba(0,0,0,0);
                            resize:none;
                        }
                        .counts{
                            position: absolute;
                            right: 10px;
                            bottom: 10px;
                            font-size: 12px;
                            line-height: 1;
                            color: #999999;
                        }
                    }
                }
            }
            .bnts{
                width: 100%;
                text-align: right;
                height: 52px;
                line-height: 52px;
                position: absolute;
                left: 0;
                bottom: 0;
                border-top: 1px solid #eaeaea;
                padding-right: 20px;
            }
        }
    }
    .delView{
        width: 100%;
        height: 100%;
        position: fixed;
        left: 0;
        top: 0;
        background: rgba(0,0,0,0.7);
        z-index: 9999;
        .delRows{
            width: 620px;
            padding: 54px 0 52px;
            background: #fff;
            border-radius: 4px;
            position: absolute;
            left: 50%;
            top: 50%;
            transform:translate(-50%,-50%);
            .conts{
                padding: 40px 72px;
                font-size: 14px;
                line-height: 1;
                color: #333333;
            }
            .titles{
                width: 100%;
                height: 54px;
                line-height: 54px;
                box-sizing: border-box;
                padding: 0 24px;
                font-size: 16px;
                color: #333333;
                position: absolute;
                text-align: left;
                top: 0;
                left: 0;
                border-bottom: 1px solid #eaeaea;
                i{
                    position: absolute;
                    width: 54px;
                    height: 54px;
                    line-height: 54px;
                    text-align: center;
                    right: 0;
                    color: #8c8c8c;
                    font-size: 12px;
                    top: 0;
                    cursor: pointer;
                }
            }
            .bnts{
                width: 100%;
                text-align: right;
                height: 52px;
                line-height: 52px;
                position: absolute;
                left: 0;
                bottom: 0;
                border-top: 1px solid #eaeaea;
                padding-right: 20px;
            }
        }
    }
}
.decorCustomer{
    padding: 20px;
}
.contractContent{
    padding-top: 30px;
}
.rejectReson{
    width: 372px;
}
.leftTitle{
    vertical-align: top;
}
.pageCount{
    text-align: right;
    background: #fff;
    padding:20px;
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
.tableDataFrame{
    padding:0 20px;
    background: #fff;
}

.topBar{
    background: #fff;
    div{
        text-align: left;
    }
    .footer{
        margin-top: 20px;
        border-top: solid 1px #e8e8e8;
        text-align: center;
        padding:20px  0 ;
        display: block;
    }
    .barTitle{
        border-bottom: solid 1px #e8e8e8;
        padding-left: 32px;
        font-size: 16px;
        color: #333;
        height: 56px;
        line-height: 56px;
        position: relative;
        .right{
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
    }
}
.searchOption {
    padding-left: 32px;
    .el-select, .pickDate, .el-input ,.el-cascader{
        width: 220px;
        margin-top: 20px;
        margin-right: 10px;
    }
}
.queryBtn{
    height: 30px;
    width:62px;
    display: inline-block;
    text-align: center;
    line-height: 30px;
    border-radius: 4px;
    color: #fff;
    background: #3a8ee6;
    cursor: pointer;
    font-size: 14px;
    margin-left: 40px;
    margin-right: 10px;
}
.queryReset{
    height: 30px;
    width:62px;
    font-size: 14px;
    display: inline-block;
    text-align: center;
    line-height: 30px;
    border-radius: 4px;
    border:solid 1px #d9d9d9;
    color: #999;
    cursor: pointer;
}
.ownerInfo{
    margin-top: 20px;
    background: #fff;
    border-radius: 4px;
    height: 214px;
    text-align: left;
    ul{
        height: 64px;
        line-height: 64px;
        border-bottom: solid 1px #e8e8e8;
        li{

            display: inline-block;
            margin-left: 50px;
            margin-right: 50px;
        }
    }
    div{
        background: #fff;
        height: 150px;
    }
}
</style>

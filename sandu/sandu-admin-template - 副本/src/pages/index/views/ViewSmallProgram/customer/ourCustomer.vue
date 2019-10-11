<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>我的业主</span>
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">
                <el-input v-model="mobile" placeholder="手机号"> </el-input>
                <el-select v-model="customerStatus" @change="getCustomerListData" clearable placeholder="请选择状态">
                    <el-option v-for="(item,index) in customerStatusList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"
                    > </el-option>
                </el-select>
                <el-select v-model="customerActive"  @change="getCustomerListData" clearable placeholder="请选择业主类型">
                    <el-option v-for="(item,index) in customerActiveList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"
                    > </el-option>
                </el-select>
                <el-select v-model="follow"  @change="getCustomerListData" filterable clearable placeholder="请选择跟进人">
                    <el-option v-for="(item,index) in followIn"
                               :key="index"
                               :label="item.name"
                               :value="item.id"
                    >
                    </el-option>
                </el-select>
                <el-select v-model="selectDealer" filterable  @change="getCustomerListData" clearable placeholder="所属经销商企业">
                    <el-option v-for="(item,index) in dealerList"
                               :key="index"
                               :label="item.name"
                               :value="item.id"
                    > </el-option>
                </el-select>
            </el-row>
            <el-row class="areaStyle">
                <!---------------------------------------------------->

                <el-select v-model="citySelect.provide"
                           @change="getProvideCode(citySelect.provide)"
                           clearable
                           placeholder="请选择省">
                    <el-option
                            v-for="(item, index) in provide"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>

                <el-select clearable v-model="citySelect.city"
                           @change="getCityCode(citySelect.city)"
                           placeholder="请选择市">
                    <el-option
                            v-for="(item,index) in citySelects"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>

                <el-select clearable v-model="citySelect.area"
                           @change="getAreaCode(citySelect.area)"
                           placeholder="请选择区域">
                    <el-option
                            v-for="(item,index) in areaSelect"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>

                <el-select clearable  v-if="streetShow" v-model="citySelect.origin"
                           @change="getOriginCode(citySelect.origin)"
                           placeholder="请选择街道">
                    <el-option
                            v-for="(item,index) in originSelect"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
                <el-col :span="3" v-else>
                    <span class="streetSel">不限</span>
                </el-col>
                <!---------------------------------------------------->
            </el-row>
        </div>
        <div class="btnFrame">
            <el-button  round type="primary" class="UnifiedsearchBtn" @click="getCustomerListData">搜索</el-button>
            <el-button  round class="searchBtn UnifiedsearchBtn" @click="resetForm">重置</el-button>
        </div>
        <div class="tableList">
           <div style="padding: 10px 20px">
                <el-button round
                            type="primary"
                            class="addProBtn"
                            plain
                            @click.stop="exportCustomerData">导出
                </el-button>
            </div>     
            <el-table
                    :data="tableData"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%">

                <el-table-column
                        prop="nickName"
                        label="昵称"
                >
                </el-table-column>
                <el-table-column
                        prop="mobile"
                        label="手机号"
                >
                </el-table-column>
                <el-table-column
                        prop="address"
                        label="业主地址">
                </el-table-column>
                <el-table-column
                        prop="level"
                        label="业主等级">
                    <template slot-scope="scope">
                        <span v-if="scope.row.score >= 0 && scope.row.score <8" class="changeColor"><img src="../../../assets/images/icons/level.png" class="level">普通用户（{{scope.row.score}}分）</span>
                        <span v-if="scope.row.score >= 8 && scope.row.score <15" class="changeColor"><img src="../../../assets/images/icons/level.png" class="level">活跃业主（{{scope.row.score}}分）</span>
                        <span v-if="scope.row.score >= 15 && scope.row.score <25" class="changeColor"><img src="../../../assets/images/icons/level.png" class="level">潜在业主（{{scope.row.score}}分）</span>
                        <span v-if="scope.row.score >= 25" class="changeColor"><img src="../../../assets/images/icons/level.png" class="level">忠实业主（{{scope.row.score}}分）</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="alotStatus"
                        label="状态">
                    <template slot-scope="scope">
                        <span v-if="scope.row.alotStatus == 0">未分配</span>
                        <span v-if="scope.row.alotStatus == 1">已分配</span>
                        <span v-if="scope.row.alotStatus == 2">跟进中</span>
                        <span v-if="scope.row.alotStatus == 3">跟进完成</span>
                        <span v-if="scope.row.alotStatus == 4">无效</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="channelCompanyName"
                        label="所属经销商企业">
                </el-table-column>
                <el-table-column
                        prop="followUpUserName"
                        label="跟进人">
                    <template slot-scope="scope">
                        {{scope.row.followUpUserName}}
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" @click="gotoDetail(scope.row)" v-if="isPer('customer:agent:detail')">详情</el-button> <!-- scope.row.alotStatus == 1 && scope.row.followUpUserName-->
                        <el-button type="text" @click="followInfo(scope.row)" v-if="(scope.row.alotStatus == 1||(scope.row.alotStatus ==2&&scope.row.followUpUserId==myUserId)) && isPer('customer:agent:mange')" >跟进</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="query.start"
                        :page-sizes="[5,10, 50, 100, 200,500]"
                        :page-size="query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        <el-dialog class="dealerAllotDialog"
                   title="业主跟进"
                   :visible.sync="dialogAllotVisible"
                   width="540px">
            <div>
                <span class="mobi">业主手机号：</span><i>&nbsp;{{followDetialInfo.userInfo}}</i>
            </div>
            <div>
                <span>跟进状态：</span>
                <el-select v-model="followDetialInfo.alotStatusName" filterable placeholder="请选择跟进状态" @change="changeState">
                    <el-option v-for="(item,index) in followStatusList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"
                    > </el-option>
                </el-select>
            </div>
            <div class="followResut">
                <span>跟进结果：</span>
                <el-input type="textarea" v-model="followResult" style="width: 360px;"></el-input>
            </div>

            <span slot="footer" class="dialog-footer">
                <el-button round size="mini" type="primary" @click="followSure">确 认</el-button>
                <el-button round size="mini" class="cancelBg" @click="dialogAllotVisible = false">取 消</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        name: "coustomer",
        data(){
            return{
                myUserId:sessionStorage.getItem('userId'),
                inputDealer: '',
                dialogAllotVisible:false,
                total: 0, // 总页数
                value:'',
                followResult:'',
                followUserId:'',
                mobile:'',
                followStatus:'',
                followStatusList:[
                    {value:2, name:'跟进中'},
                    {value:3, name:'跟进完成'},
                    {value:4, name:'无效'},
                ],
                customerStatus:'', // 业主状态
                customerStatusList:[
                    // {value:0, name:'未分配'},
                    {value:1, name:'已分配'},
                    {value:2, name:'跟进中'},
                    {value:3, name:'跟进完成'},
                    {value:4, name:'无效'}
                ],
                customerActive:'', // 活跃业主
                customerActiveList:[
                    {value:0, name:'普通业主'},
                    {value:1, name:'活跃业主'},
                    {value:2, name:'潜在业主'},
                    {value:3, name:'忠实业主'}
                ],
                follow:'', // 跟进人
                selectDealer:'',// 选择经销商
                loading:true,
                streetShow:true,
                rowId:'',
                followId: '',
                //区域选择-----------
                search:{
                    franchiserName: '',
                    phone: '',
                    contactName: '',
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },
                citySelect:{
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },

                cityCode: '',
                provide:[],
                provideSelect:[],
                citySelects:[],
                areaSelect: [],
                originSelect: [],

                //区域选择 结束------------
                customerList:[],// 业主列表
                tableData: [],
                query: {
                    start: 1,
                    limit: 10,
                },
                // 经销商下拉框
                dealerList:[],
                // 跟进人列表
                followIn:[],
                followDetialInfo:[],
            }
        },
        methods:{
            isPer(per) {
                if (per) {
                    let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                    // console.log(permiss.permissions, 'isper')
                    return this.isMainPer(per, permiss)
                }
            },
            changeState(val){
                this.followDetialInfo.alotStatus=val;
            },
            // 区域选择-------------
            getArea(code, type){
                this.API.areaList({areaCode:code}).then((res => {
                    if(res) {
                        if(type == '省') {
                            this.provide = res.datalist
                        }
                        if(type == '市') {
                            this.citySelects = res.datalist
                        }
                        if(type == '区') {
                            this.areaSelect = res.datalist
                        }
                        if(type == '街道') {
                            this.originSelect = res.datalist

                            if(!this.originSelect || this.originSelect.length == 0)
                            {
                                this.streetShow =false
                                // this.originSelect = [{areaCode: '', areaName: '不限'}]
                            } else {
                                this.streetShow = true
                            }
                        }
                    }
                }))
            },
            getProvideCode(code) {
                this.citySelect.city =''
                this.citySelect.area =''
                this.citySelect.origin =''
                this.citySelects =[]
                this.areaSelect = []
                this.originSelect = []
                this.getArea(code,'市')
                this.getCustomerListData()
            },
            getCityCode (code){
                this.citySelect.area =''
                this.citySelect.origin =''
                this.areaSelect = []
                this.originSelect = []
                this.getArea(code,'区')
                this.getCustomerListData()
            },
            getAreaCode (code){
                this.citySelect.origin =''
                this.originSelect = []
                this.getArea(code,'街道')
                this.getCustomerListData()
            },
            getOriginCode (code){
            },
            // 区域选择 结束---------- streetCodeName
            // 业主列表
            getCustomerListData() {
                let queryCondition = Object.assign({}, this.query, {
                    mobile:this.mobile, //
                    level: this.customerActive,
                    alotStatus:this.customerStatus,
                    followUpUserId:this.follow,
                    channelCompanyId:this.selectDealer,
                    provinceCode:this.citySelect.provide,
                    cityCode:this.citySelect.city,
                    areaCode:this.citySelect.area,
                    streetCode:this.citySelect.origin
                })
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                this.API.ourDistributorList(queryCondition).then((res) => {
                    console.log(res)
                    if(res.data) {
                        this.tableData = res.data.list
                        this.total = res.data.total
                        this.loading = false
                    }
                })
            },

            exportCustomerData(){
                let queryCondition = Object.assign({}, {
                    mobile:this.mobile, //
                    level: this.customerActive,
                    alotStatus:this.customerStatus,
                    followUpUserId:this.follow,
                    channelCompanyId:this.selectDealer,
                    provinceCode:this.citySelect.provide,
                    cityCode:this.citySelect.city,
                    areaCode:this.citySelect.area,
                    streetCode:this.citySelect.origin,
                    phoneFlag:this.phoneFlag == 1 ? true : this.phoneFlag == 2 ? false: '',
                    exportType:1
                })
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                console.log(queryCondition);
                this.loadingTwo = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                });
                this.API.exportCustomerCheckApi(queryCondition).then((res) => {

                    if(res && res.code == 200) {
                        this.API.exportCustomerApi(queryCondition).then((result) => {
                            if(result&& result.size > 0) {
                                this.loadingTwo.close()
                                let url = window.URL.createObjectURL(result)
                                let link = document.createElement('a')
                                link.style.display = 'none'
                                link.href = url
                                link.setAttribute('download', '业主信息导出.xls')
                                document.body.appendChild(link)
                                link.click()
                            }else{
                                this.loadingTwo.close()
                                this.$message.error("没有数据需要导出!")         
                            }
                        })
                    }else{
                        this.loadingTwo.close()
                        this.$message.error(res.message) 
                    }
                })                
            },

            // 分页模块
            handleSizeChange: function (size) {
                console.log(size)
                this.query.limit = size
                this.getCustomerListData()
            },
            handleCurrentChange: function (currentPage) {
                console.log(currentPage)
                this.query.start = currentPage
                this.loading = true
                this.getCustomerListData()
            },
            // 获取经销商列表，
            getDealerList(){
                this.API.getComapnyIn({}).then((res) => {
                    console.log(res, 'dealer')
                    if(res) {
                        this.dealerList = res.data
                    }
                })
            },
            // 跟进人列表
            getFollow(){
                this.API.getFollowIn({}).then((res) => {
                    if(res) {
                        this.followIn = res.data
                    }
                })
            },
            resetForm(){
                this.citySelect ={}
                this.mobile = ''
                this.customerActive = ''
                this.customerStatus = ''
                this.follow = ''
                this.selectDealer = ''
            },
            // 详情
            gotoDetail(row) {
                console.log(row.id, 'id detail')
                this.$router.push(`/customerDetail/${row.id}`)
            },

            // 跟进
            // 跟进详情
            getFollowDetialInfo(row){
                this.API.getFollowDetail({id:row}).then((res) => {
                    this.followDetialInfo = res.data;
                    this.followDetialInfo.alotStatusName = '跟进中';//默认跟进中
                    this.followDetialInfo.alotStatus=2;
                    this.followResult=res.data.followUpResult;
                    this.followId = res.data.id
                    console.log(this.followId, 'followId')
                })
            },

            followInfo(row){
                this.dialogAllotVisible = true
                this.rowId = row.id
                this.followUserId = row.userId
                console.log(this.followUserId, 'this.followUserId')
                this.getFollowDetialInfo(this.rowId)
                console.log(this.rowId, 'this.rowId')
            },
            // 确认跟进
            followSure(){
                console.log(this.followUserId , 'this.followUserId ')
                this.API.updateFollowInfo({
                    id:this.followId,
                    followUserId:this.followUserId,
                    followUpResult: this.followResult,
                    alotStatus:this.followDetialInfo.alotStatus,
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    console.log(res, 'cc')
                    this.getCustomerListData()
                    this.$message.success('跟进成功');
                    this.dialogAllotVisible=false;
                })
            },
            // 跟进
        },

        activated(){
            this.getCustomerListData()
            this.getDealerList()
            this.getFollow()
            this.getArea(0,'省')
        },
    }
</script>

<style scoped lang="scss">
    @import "../../../assets/css/mixin.scss";
    .changeColor{
        color: #ff2323;
    }
    .customerTitle{
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        border-bottom: solid 1px #ddd;
        span{
            display: inline-block;
            padding-left: 20px;
        }
    }
    .btn-right{
        position: absolute;
        left: 20px;
        top: 20px;
        .addProBtn{
           width:120px;
           height:32px;
           font-size:12px;
           text-align:center;
           line-height: 8px;
        }
        .addProBtn1{
          height:32px;
          font-size:12px;
          text-align:center;
          line-height: 8px;
        }
    }

    .searchFrame{
        background: #fff;
        padding: 20px;
    }
    .areaStyle{
        margin-top: 20px;
        .el-select{
            width: 216px;
            margin-right: 10px;
        }
    }
    .selectSearchCond{
        .el-select,.el-input{
            width: 216px;
            margin-right: 10px;
        }

    }
    .btnFrame{
        padding:10px 20px;
        text-align: center;
        border-top: solid 1px #ddd;
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        .el-button{
            width: 130px;
        }
    }

    .setRule{
        margin-top: 20px;
        height: 76px;
        line-height: 76px;
        background: #fff;
        color: #FF6419;
        padding-left: 20px;
        span{
            display: inline-block;
            cursor: pointer;
        }
    }
    .tableList{
        margin-top: 20px;
        background: #fff;
    }
    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
    .level{
        vertical-align: -4px;
        margin-right: 4px;
    }
    .dealerAllotDialog{
        div{
            text-align: center;
            margin-bottom: 15px;
        }
        i{
            font-style: normal;
            display: inline-block;
            width: 360px;
            text-align: left;
        }
        .mobi{
            margin-left: -18px;
        }
        .el-select{
            background:#fafafa ;
            width: 360px;
            height: 36px;
        }
        .followResut{
            span{
                display: inline-block;
                vertical-align: top;
            }
        }
        .el-input{
            width: 360px;
        }
    }

</style>
<template>
    <div class="companyBaseMsg">
        <div class="companyBaseMsg-header">
            <h1>拼团活动</h1>
            <div class="header-screen">
                <el-select size="medium" v-model="activityState" class="screen-city-item"
                           @change="seekShopList('活动状态')" placeholder="活动状态"
                           :clearable=true
                >
                    <el-option label="草稿" :value="5">草稿</el-option>
                    <el-option label="未开始" :value="1">未开始</el-option>
                    <el-option label="进行中" :value="2">进行中</el-option>
                    <el-option label="已结束" :value="3">已结束</el-option>
                    <el-option label="已失效" :value="4">已失效</el-option>
                </el-select>
                <el-input class="header-screen-input" size="medium" placeholder="请输入活动名称" v-model="shopName" @change="seekShopList('活动名称')"></el-input>

                <el-select v-model="putawayMiniPro" multiple placeholder="上架小程序" @change="seekShopList('小程序')">
                    <el-option
                            v-for="item in putawayMiniProInfo"
                            :key="item.appId"
                            :label="item.minProName"
                            :value="item.appId">
                    </el-option>
                </el-select>

            </div>
            <div class="header-seek">
                <el-button type="primary" round class="pri UnifiedsearchBtn" size="medium" @click="ActivityList">搜索</el-button>
                <el-button  round class="seek UnifiedsearchBtn" size="medium" @click="seekShopList('重置')">重置</el-button>
                <el-button type="primary"
                           round
                           plain
                           class="add-shop "
                           size="small"
                           @click="addActivity()"
                           v-if="isPer('groupPurchaseActivity:add')"
                           >新增活动</el-button>
            </div>
        </div>
        <div class="companyBaseMsg-content">
            <el-table
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    :data="shopData"
                    style="width: 100%">

                <el-table-column type="index" label="序号" width="60"></el-table-column>

                <el-table-column
                        prop="shopName"
                        label="活动名称"
                        class="overlookName">
                    <template slot-scope="scope">
                        <el-tooltip
                                effect="light"
                                :content="scope.row.activityName"
                                placement="bottom"
                                :enterable="false"
                                :visible-arrow="false">
                            <span class="shop-name" @click="toDetail(scope.row.activityId)">{{scope.row.activityName}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="logoUrl"
                        label="产品名称"
                        class="overlookName"
                        >
                        <template slot-scope="scope">
                            <el-tooltip
                                effect="light"
                                :content="scope.row.spuName"
                                placement="bottom"
                                :enterable="false"
                                :visible-arrow="false">
                            <span class="product-name">{{scope.row.spuName}}</span>
                        </el-tooltip>
                        </template>
                </el-table-column>
                <el-table-column
                        prop="contactName"
                        label="活动有效时间"
                        >
                    <template slot-scope="scope">
                        <span class="product-name">{{scope.row.activityStartTime}}<br/>{{scope.row.activityEndTime}}</span>
                    </template>
                </el-table-column>
                

                <el-table-column
                        prop="contactPhone"
                        label="活动状态">
                    <template slot-scope="scope">
                        <span class="operation-name">{{scope.row.activityStatus==0?'草稿中':scope.row.activityStatus==1?'未开始':
                            scope.row.activityStatus==2?'进行中':scope.row.activityStatus==3?'已结束':scope.row.activityStatus==4?'已失效':''}}</span>
                    </template>
                </el-table-column>


                <el-table-column
                        prop="areaName"
                       :render-header="orderAmount">
                    <template slot-scope="scope">
                         <span class="operation-name" v-if="scope.row.totalOrderAmount">{{scope.row.totalOrderAmount}}</span>
                         <span class="operation-name" v-else>0</span>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="address"
                        :render-header="orderCount">
                    <template slot-scope="scope">
                        <span class="operation-name" v-if="scope.row.totalOrderCount">{{scope.row.totalOrderCount}}</span>
                        <span class="operation-name" v-else>0</span>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="release"
                        :render-header="orderPerson">
                    <template slot-scope="scope">
                        <span class="operation-name" v-if="scope.row.totalOrderPerson">{{scope.row.totalOrderPerson}}</span>
                        <span class="operation-name" v-else>0</span>
                    </template>
                </el-table-column>

                <el-table-column prop="putawayMinProName" label="上架小程序" width="140">
                    <template slot-scope="scope">
                        <div v-if="scope.row.putawayMinProName">
                            <p
                                    v-for="(item,index) in scope.row.putawayMinProName.split(',')"
                                    :key="index"
                            >{{item}}</p>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column
                    class="bowen"
                        width="320"
                        prop="operation"
                        label="操作 ">
                    <template slot-scope="scope" >
                       
                        <span class="operation"  @click="changeStatus(scope.row.activityId,1,'release')" v-if="scope.row.activityStatus == 0 && isPer('groupPurchaseActivity:manage')">发布
                            
                        </span>
                           <span class="operation"  @click="toDetail(scope.row.activityId)">查看</span>
                           <!-- <span class="operation"  @click="toDetailData(scope.row.activityId)">查看活动数据</span> -->
                            <span class="operation"  @click="toEdit(scope.row.activityId)" v-if="scope.row.activityStatus != 3 && scope.row.activityStatus != 4 && isPer('groupPurchaseActivity:edit')">编辑</span>
                            <span class="operation"  @click="changeStatus(scope.row.activityId,4,'invalid')" v-if="scope.row.activityStatus != 3 && scope.row.activityStatus != 4 
                            && scope.row.activityStatus != 0 && isPer('groupPurchaseActivity:manage')">使失效
                            <el-tooltip class="item" effect="light"  placement="bottom">
                                <div slot="content">“使失效”即立即结束且不可再编辑，未成团订单<br/>将自动关闭并退款，已成团订单仍需及时处理。
                                <br/>失效后的商品活动可删除"</div>
                                 <div class="tip">?</div>
                             </el-tooltip>
                            </span>
                            <span class="operation"  v-if="scope.row.activityStatus != 3 && scope.row.activityStatus != 4 
                            && scope.row.activityStatus != 0 && isPer('groupPurchaseActivity:manage')" v-clipboard:copy="scope.row.activityPath" v-clipboard:success="onCopy"
                              v-clipboard:error="onError">复制活动链接
                            <el-tooltip class="item" effect="light"  placement="bottom">
                                <div slot="content">你可以在首页管理模块将活动链接录入，<br/>
                                就可以通过首页banner跳转到拼团活动啦。</div>
                                 <div class="tip">?</div>
                             </el-tooltip>
                            </span>
                            <span class="operation"  @click="deleteActivity(scope.row.activityId,scope.row.totalOrderCount)" v-if="(scope.row.activityStatus == 3 || scope.row.activityStatus == 4 
                            || scope.row.activityStatus == 0 ) && isPer('groupPurchaseActivity:manage')">删除</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="footer-paging">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="page"
                    :page-sizes="[5, 10, 20, 30, 40]"
                    :page-size="10"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalCount">
            </el-pagination>
        </div>


                <el-dialog title="发布"
                    :visible.sync="shelfDialogVisible"
                    width="540px"
                    top="30vh">
                    <div style="color:black">确定要发布此活动吗？</div>
                    <div style="color:gray">说明：</div>
                    <div style="color:red">发布后这个活动不能再编辑商品相关信息，请检查配置无误。</div>
                    <span slot="footer" class="dialog-footer">
                        <el-button round type="primary" style="width: 80px" size="medium" class="confirmButtonClass"
                                    @click="shelfConfirm">确 定</el-button>
                        <el-button round type="info" style="width: 80px" size="medium" class="cancelButtonClass"
                                    @click="shelfDialogVisible = false">取 消</el-button>
                        </span>
                </el-dialog>

                <el-dialog title="提示"
                    :visible.sync="shanchu"
                    width="540px"
                    top="30vh">
                    <div style="color:black">该活动不允许删除。</div>
                    <div style="color:gray">说明：活动已有人参与，删除会造成活动数据丢失。</div>
                    <span slot="footer" class="dialog-footer">
                        <el-button round type="primary" style="width: 80px" size="medium" class="confirmButtonClass"
                                    @click="shanchu = false">知道了</el-button>
                        </span>
                </el-dialog>


                <el-dialog title="使失效"
                    :visible.sync="shixiao"
                    width="540px"
                    top="30vh">
                    <div style="color:black">确定要使此活动失效吗？</div>
                    <div style="color:gray">说明：</div>
                    <div style="color:red">1.进行中/未开始的商品活动一旦失效，活动立即结束且不可再编辑。</div>
                    <div style="color:gray">2.未开启模拟成团，未成团订单将自动关闭并退款；若已开启模拟成团，<br/>未成的团体将立即模拟成团。
                    <br/>3.已成团订单仍需及时处理。</div>
                    <span slot="footer" class="dialog-footer">
                        <el-button round type="primary" style="width: 80px" size="medium" class="confirmButtonClass"
                                    @click="shixiaoSure">确 定</el-button>
                        <el-button round type="info" style="width: 80px" size="medium" class="cancelButtonClass"
                                    @click="shixiao = false">取 消</el-button>
                        </span>
                </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs';

    export default {
        data () {
            return {
                putawayMiniProInfo: [],
                putawayMiniPro: [],
                putawayInfo: '',
                releaseActivityId:'',
                relaseActivityStatus:'',
                shelfDialogVisible: false,
                shixiao:false,
                shanchu:false,
                getShopId:'',
                selectBacklist:'',
                cancelBlacklistVisible:false,
                joinBacklistVisible:false,
                contactNameTop : -1,
                shopCode: '', // 店铺编码
                shopName: '', // 店铺名称
                contactsName: '', // 联系人姓名
                contactsPhone: '', // 联系人电话
                province: [], // 省份
                provinceCode: '',
                city: [], // 市
                cityCode: '',
                district: [], // 区
                districtCode: '',
                street: [], // 街道
                companyType: 0, // 企业类型
                streetCode: '',
                isRelease:'',//是否发布
                activityState:'', //活动状态
                shopData: [], // 店铺数据表格
                totalCount: 0, // 店铺数据总条数
                page: 1, // 页码
                limit: 10, // 每页条数
                getShopList: {
                    activityName:'',
                    activityStatus:'',
                    companyId:'',
                    putawayInfo:'',
                    page: 1, // 页码
                    limit: 10 // 每页条数
                }
            };
        },
        activated () {
            this.companyType = sessionStorage.getItem('companyType');
            this.ActivityList();
        },
        filters: {
           unique5(arr){
            var x = new Set(arr);
            return [...x].join('');
            }  
        },
        computed: {
            
        },
        methods: {
            getPutawayMiniProgram() {
                let companyId = sessionStorage.getItem('companyID');
                this.API.selectPutawayMiniProgram({
                    companyId: companyId
                }).then(res => {
                    this.putawayMiniProInfo = res.datalist;
                })
            },
            shelfConfirm() {
                
                this.API.changeActivityStatus({
                    activityId:this.releaseActivityId,
                    activityStatus:this.relaseActivityStatus,
                }).then(res => {
                    if (res.success == true) {
                        this.ActivityList();
                        this.$message.success("发布成功！");
                        this.shelfDialogVisible=false;
                    }
                });
                    
            },
            shixiaoSure() {
                this.API.changeActivityStatus({
                       activityId:this.releaseActivityId,
                       activityStatus:this.relaseActivityStatus,
                   }).then(res => {
                        if (res.success == true) {
                            this.ActivityList();
                            this.$message.success("操作成功！");
                            this.shixiao = false;
                        }
                    });
            },

            orderAmount(h, {column, $index}) {
                return h("div", {}, [
                    h("span", {
                        "class": {
                           
                        }
                    },'订单实付金额/￥'),
                h("el-tooltip", {
                        attrs: {
                            class: "wuyinpeiImg",
                            effect: "light",
                            content: "拼团活动带来的总付款金额",
                            placement: "bottom"
                        }
                    }, [
                        h("span", {},'?')
                    ])

                ])
            },
            orderCount(h, {column, $index}) {
                return h("div", {}, [
                    h("span", {
                        "class": {
                           
                        }
                    },'成团订单数'),
                h("el-tooltip", {
                        attrs: {
                            class: "wuyinpeiImg",
                            effect: "light",
                            content: "成交订单数",
                            placement: "bottom"
                        }
                    }, [
                        h("span", {},'?')
                    ])

                ])
            },
            orderPerson(h, {column, $index}) {
                return h("div", {}, [
                    h("span", {
                        "class": {
                           
                        }
                    },'成团人数'),
                h("el-tooltip", {
                        attrs: {
                            class: "wuyinpeiImg",
                            effect: "light",
                            content: "拼团成功的客户数",
                            placement: "bottom"
                        }
                    }, [
                        h("span", {},'?')
                    ])

                ])
            },
            onCopy() {
                this.$message.success("拷贝活动链接成功!");
            },
            onError() {
                this.$message.warning("拷贝活动链接失败!");
            },
            /*权限设置*/
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            addActivity(){
                this.$router.push('/assemActivity/add');
            },
            changeStatus(index,status,type) {
                this.releaseActivityId = index
                if(type == 'release'){
                    this.shelfDialogVisible = true;
                    this.relaseActivityStatus = status
                }else{
                    this.shixiao = true;
                    this.relaseActivityStatus = status
                }
                
            },
            toEdit(index){
                this.$router.push(`/assemActivity/edit/${index}`);
            },
            toDetail(index) {
                this.$router.push(`/assemActivity/basicInfo/${index}`);
            },
            toDetailData(index) {
                this.$router.push(`/assemActivity/data/${index}`);
            },
            /*删除操作*/
            deleteActivity(index,status) {
                if(status > 0) {
                    this.shanchu = true;
                    // this.$alert('该活动不允许删除','提示',{
                    //     confirmButtonText: '知道了',
                    //     message:"该活动不允许删除。（该活动已有人参与，删除会造成活动数据丢失。）",
                    //     type: 'warning'
                    // })
                }else{
                    this.$confirm('确定要删除此活动吗？','删除',{
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.API.deleteActivity({id: index}).then(res => {
                                if (res.success == true) {
                                    this.ActivityList();
                                    this.$message({
                                        type: 'success',
                                        message: "删除成功"
                                    });
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: res.message
                                    });
                                }
                        });
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });
                }
            },
            /*每页加载数据条数*/
            handleSizeChange(val) {
                this.getShopList.limit = val;
                this.limit = val;
                this.ActivityList();
            },
            /*分页跳转*/
            handleCurrentChange(val) {
                this.page = val;
                this.getShopList.page = val;
                this.ActivityList();
            },
            /*条件搜索店铺数据*/
            seekShopList(type) {
                if (type=='小程序') {
                    this.getShopList.putawayInfo = this.putawayMiniPro.join(",");
                    this.getShopList.page = 0;
                    this.getShopList.limit = 10;
                }
                if (type == '活动名称') {
                    this.getShopList.activityName = this.shopName;
                    this.getShopList.page = 0;
                    this.getShopList.limit = 10;
                }
                
                if (type == '重置') {
                    this.getShopList = {
                        activityName: undefined,
                        activityStatus:undefined,
                        putawayInfo:undefined,
                        page: 0, // 页码
                        limit: this.limit // 每页条数
                    }
                    this.activityState = '';
                    this.shopName = '';
                    this.putawayMiniPro = [];
                    this.ActivityList();
                }
                if(type == '活动状态') {
                    this.getShopList.activityStatus = this.activityState;
                    this.getShopList.page = 0;
                    this.getShopList.limit = 10;
                    this.ActivityList();
                } else {

                    this.ActivityList();
                }
            },
            /*请求店铺列表*/
            ActivityList() {
                this.getShopList.companyId= sessionStorage.getItem('companyID')
                this.getShopList.putawayInfo = this.putawayMiniPro.join(",")
                this.API.groupPurchaseActivityList(this.getShopList).then((res) => {
                    this.shopData = res.datalist;
                    this.totalCount = res.totalCount;
                });
            },
        },
        mounted(){
            this.getPutawayMiniProgram();
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/css/mixin";

    .companyBaseMsg{
        .companyBaseMsg-header{
            width: 100%;
            background-color: #ffffff;
            h1{
                width: 100%;
                height: 58px;
                font-size: 18px;
                line-height: 58px;
                padding-left: 20px;
                box-sizing: border-box;
                border-bottom: 1px solid #dddddd;
            }
            .header-screen{
                padding: 20px 10px 0 10px;
                border-bottom: 1px solid #dddddd;
                .header-screen-input{
                    width: 180px;
                    padding: 0 10px 20px 10px;
                }
                .screen-city-item{
                    width: 130px;
                    padding: 0 10px 20px 10px;
                }
                .screen-city{
                    line-height: 36px;
                }
            }
            .header-seek{
                padding: 20px 30px;
                text-align: center;
                .seek{
                    width: 100px;
                    background: #f5f5f5 ;
                }
                .pri{
                    width: 100px;
                    border: solid 1px #dddddd;
                }
                .add-shop{
                    float: right;
                }
            }
        }
        .companyBaseMsg-content{
            margin-top: 20px;
            padding: 20px;
            background-color: #ffffff;
            .name-class{
                color: red;
            }
            .operation{
                color: #ff6419;
                cursor:pointer;
                padding-left:10px;
            }
            .shop-name{
                width: 100%;
                border: none;
                color: #ff6419;
                cursor:pointer;
                overflow: hidden;
                text-overflow:ellipsis;
                white-space: nowrap;
            }
            .product-name{
                width: 100%;
                border: none;
                overflow: hidden;
                text-overflow:ellipsis;
                white-space: nowrap;
            }
            .operation-name{
                color: #333333;
                overflow: hidden;
                text-overflow:ellipsis;
                white-space: nowrap;

            }
        }
        .footer-paging{
            background-color: #ffffff;
            padding: 20px 40px;
            text-align: center;
        }
    }
    .tip{
        display: inline-block;
        width: 16px;
        height: 16px;
        color: gray;
        font-size: 14px;
        line-height: 16px;
        text-align: center;
        background-color:white;
        border-radius: 100%;
        margin-left: 0;
        border:1px solid gray;
        cursor: pointer;
    }
    .overlookName {
        font-size: 30rpx;
        word-break: break-all;/*允许在单词内换行*/
        text-align: left;
        color: #323232;
        line-height: 45rpx;
        text-overflow: -o-ellipsis-lastline;/*css3中webkit内核提供的一个方法类似ellipsis*/
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;/*自适应盒子*/
        -webkit-line-clamp: 1;/*此处为1行,如果是两行就改成2*/
        -webkit-box-orient: vertical;
    }

</style>


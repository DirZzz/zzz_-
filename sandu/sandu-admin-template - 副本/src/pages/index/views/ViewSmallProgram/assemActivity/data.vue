<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <el-tabs v-model="activeName" class="searchBg">
                <div class="order">
                     <template>
                    <el-tabs v-model="tagName"  @tab-click="handleClick">
                        <el-tab-pane label="活动基本信息" name="first"></el-tab-pane>
                        <el-tab-pane label="活动数据" name="second"></el-tab-pane>
                    </el-tabs>
                    </template>
                     
                     <div class="heading">
                         <div class="title1">订单实付金额：&emsp;<b style="font-size:22px;color:black" v-if="this.totalOrderAmount">￥{{this.totalOrderAmount}}</b>
                            <b style="font-size:22px;color:black" v-else>0</b>
                         </div>
                         &emsp;&emsp;&emsp;
                         <div class="title2"> 成团订单数：&emsp;<b style="font-size:22px;color:black">{{this.totalOrderCount}}</b></div>
                         &emsp;&emsp;&emsp;
                         <div class="title3">成团人数：&emsp;<b style="font-size:22px;color:black">{{this.totalOrderPerson}}</b></div>
                     </div>
                     
                    <div>
                        <span class="cut-off-rule"></span>
                        <el-table
                                border
                                :data="tableData"
                                element-loading-text="拼命加载中"
                                element-loading-spinner="el-icon-loading"
                                element-loading-background="rgba(255, 255, 255, 0.8)"
                                style="width: 97%;margin:20px">
                            <el-table-column type="index" label="序号" width="60"></el-table-column>
                            <el-table-column prop="telephone" label="团长手机号码"></el-table-column>
                            <el-table-column prop="joinNumber" label="已参团人数"></el-table-column>
                            <el-table-column prop="unjoinNumber" label="满团剩余人数"></el-table-column>
                            <el-table-column prop="openStatus" label="状态">
                                <template slot-scope="scope">
                                    <span>{{scope.row.openStatus==0?'等待成团':scope.row.openStatus==1?'拼团成功':scope.row.openStatus==2?'拼团失败':''}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                  <span class="operation" @click="getActivityOpenDetail(scope.row)" style="color:#ff6419;cursor:pointer">查看成团情况</span>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pageFrame">
                            <el-pagination
                                    @size-change="handleSizeChange"
                                    @current-change="handleCurrentChange"
                                    :current-page="query.page"
                                    :page-sizes="[10, 50, 100, 200,500]"
                                    :page-size="query.limit"
                                    layout="total, sizes, prev, pager, next, jumper"
                                    :total="total">
                            </el-pagination>
                        </div>
                    </div>



                    <!--确认对话框-->
                    <el-dialog title="查看成团情况" :visible.sync="sureDialogTableVisible">
                        <div class="childHeading">
                            成团状态:&emsp;<b style="color:black">{{this.assemStatus==0?'等待成团':this.assemStatus==1?'拼团成功':this.assemStatus==2?'拼团失败':''}}</b>
                            &emsp;&emsp;&emsp;
                            已参团人数:&emsp;<b style="color:black">{{this.joinPerson}}人</b>
                            &emsp;&emsp;&emsp;
                            满团剩余人数:&emsp;<b style="color:black">{{this.unjoinPerson}}人</b>
                        </div>
                        <el-table :data="sureProduct" border="">
                            <el-table-column
							      label="序号"
							      type="index"
							      width="100">
							   </el-table-column>
                            <el-table-column label="成团手机号码">
                                 <template slot-scope="scope">
                                    <span v-if="scope.row.userType==1">{{scope.row.telephone}}
                                    <img src="../../../assets/images/tuanzhang.png" v-if="scope.row.isMaster==1" />
                                    </span>
                                    <span v-else>虚拟团员</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="参团时间">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.userType==1">{{scope.row.gmtCreate}}</span>
                                    <span v-else>——</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="订单金额（元）">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.userType==1">{{scope.row.orderAmount}}</span>
                                    <span v-else>——</span>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pageFrame">
                            <el-pagination
                                    @current-change="handleDetailCurrentChange"
                                    :current-page="queryDetail.page"
                                    :page-size=10
                                    layout="total, prev, pager, next, jumper"
                                    :total="totalDetail">
                            </el-pagination>
                        </div>
                    </el-dialog>

                </div>
        </el-tabs>
    </div>

</template>

<script>
    import qs from 'qs'

    export default {
        data() {
            return {
                tagName:'second',
                orderSource: [{
                    value: '0',
                    label: '企业小程序'
                }, {
                    value: '1',
                    label: '随选网'
                }],
                orderSourceValue: '',

                allotShop: [{
                    value: 'ta',
                    label: '天安数码城店铺'
                }, {
                    value: 'ta2',
                    label: '天安数码城店铺2'
                }],
                nav: [
                    { nav: "拼团活动列表", url: "/assemActivity" },
                    { nav: "详情", }
                ],
                allotShopValue: '',
                joinPerson:'',
                unjoinPerson:'',
                totalOrderAmount:'',
                totalOrderCount:'',
                totalOrderPerson:'',
                assemStatus:'',
                wuliu: false,
                orderNum: false,
                orderNumLen: false,
                sureDialogTableVisible: false,
                finishedVisible: false,
                orderNumber: undefined,
                consignee: undefined,
                sendVisible: false,
                activeName: 'second',
                selectDate: '',
                loading: true,
                OrderStatus: '',
                tableData: [],
                mobile: '',
                logisticsCompany: '',
                trackingNumber: '',
                total: 0,
                totalDetail:0,
                productName: [],
                address: '',
                orderID: '',
                flagIndex:'',
                sureProduct: [],
                orderDetailMsg: {
                    logisticsCompany: '',
                    trackingNumber: ''
                },
                query: {
                    purchaseOpenId:this.$route.params.id,
                    page: 1,
                    limit: 10,
                },
                queryDetail: {
                    page:1,
                },
                reveice:''
            }
        },
        created() {
            this.getActivityDetail();
            this.getActivityOpen()
        },
        methods: {
            toBasicInfo(){
                let index = this.$route.params.id;
                this.$router.push(`/assemActivity/basicInfo/${index}`);
            },
            getActivityDetail(){
                this.API.getActivityDetail({
                    id: this.$route.params.id
                }).then(res => {
                        this.totalOrderAmount = res.obj.totalOrderAmount;
                        this.totalOrderCount = res.obj.totalOrderCount;
                        this.totalOrderPerson = res.obj.totalOrderPerson;
                });
            },
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '40px!important',padding:'0!important',fontWeight:'normal', background:'#f5f5f5',color: '#666'}
                }
            },
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            checkZmOrNum(zmnum){
                var zmnumReg=/^[0-9a-zA-Z]*$/;
                if(zmnum!="" && !zmnumReg.test(zmnum)){
                    alert("只能输入是字母或者数字,请重新输入");
                    return false;
                }
            },
            changewu() {
                if(this.logisticsCompany) {
                    this.wuliu = false;
                }
            },

            getActivityOpenDetail(row) {
                this.joinPerson=row.joinNumber;
                this.unjoinPerson=row.unjoinNumber;
                this.assemStatus=row.openStatus;
                this.getOpenDetailByOpenId(row.id); 
                this.sureDialogTableVisible = true
            },
   
            //获取开团信息
            getActivityOpen() {
                this.API.getActivityOpen(
                    this.query
                ).then((res) => {
                    this.tableData = res.datalist;
                    this.total = res.totalCount;
                })
            },
            //获取开团详细信息
            getOpenDetailByOpenId(index) {
                this.flagIndex = index;
                this.API.getOpenDetailByOpenId({
                    purchaseOpenId:index,
                    page:this.queryDetail.page
                }).then((res) => {
                    this.sureProduct = res.datalist;
                    this.totalDetail = res.totalCount;
                })
            },
            // 分页模块
            handleSizeChange: function (size) {
                // this.pageSize = size;
                this.query.limit = size
                this.getActivityOpen()
            },
            handleCurrentChange: function (currentPage) {
                // this.currentPage = currentPage;
                this.query.page = currentPage
                this.getActivityOpen()
            },
            handleDetailCurrentChange: function (currentPage) {
                this.queryDetail.page = currentPage
                this.getOpenDetailByOpenId(this.flagIndex)
            }
        },
        mounted() {
            this.getActivityDetail();
            this.getActivityOpen()
        }
    }
</script>

<style lang="scss" scoped>
    .productName {
        text-align: left;
        line-height: 24px;
    }

    .receiveAddress {
        margin-top: 30px;
        margin-bottom: 20px;
    }

    .sureButton {
        text-align: right;
        margin-top: 20px;
        border-top: solid 1px #ddd;
        padding-top: 20px;
        .el-button {
            width: 130px;
        }
    }
    .cut-off-rule {
            display: block;
            width: 100%;
            height: 1px;
            background-color: #eeeeee;
            margin: 10px 0 40px;
        }
        .headline {
            width: 100%;
            font-size: 18px;
            color: #333333;
        }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }


    .orderlistTop {
        background: #fff;
        padding: 20px;
    }

    .buttonSplitLine {
        border-top: solid 1px #ddd;
        margin: 25px 0;
        padding-top: 20px;
        text-align: center;
        .reset{
            background: #f5f5f5;
        }

    }

    .searchBg {
        background: #fff;
    }

    .orderListSearch {
        margin: 15px;
    }

    .buttonSplitLine {
        border-top: solid 1px #ddd;
        margin: 25px 0;
        padding-top: 20px;

    }
</style>
<style lang="scss">
    .el-tabs__nav {
        margin-left: 15px;
    }

    .el-radio-button__inner {
        background: #fafafa;
        border: none;
    }

    .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        background: #fff;
        border: none;
        color: #666;
        -webkit-box-shadow: none;
        box-shadow: none;
    }

    .heading{
         margin:0 20px;
         height: 120px;
         line-height: 120px;
         width:97%;
         color:gray;
    }
    .title1{
        display: inline-block;
        height: 120px;
        text-indent:25px;
        width:31%;
         background-color: #f3f3f3;   
    }
    .title2{
        display: inline-block;
        height: 120px;
        text-indent:25px;
        width:31%;
         background-color: #f3f3f3;   
    }
    .title3{
        display: inline-block;
        height: 120px;
        text-indent:25px;
        width:31%;
         background-color: #f3f3f3;   
    }
    .childHeading{
        margin:0 0 20px 0;
        height: 50px;
        line-height: 50px;
        text-indent:25px;
        font-weight:bold;
        color:gray;
        background-color: #f3f3f3;   
    }
    
</style>

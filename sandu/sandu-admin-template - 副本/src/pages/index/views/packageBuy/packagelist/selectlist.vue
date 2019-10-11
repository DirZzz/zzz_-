<template>
    <div class="selectShowData">
        <el-select v-model="value" @change="change(value)" clearable placeholder="适用用户">
            <el-option
                    v-for="item in userList"
                    :key="item.value"
                    :value="item.value"
                    :label="item.name">
            </el-option>
        </el-select>
        <el-table
                :data="selectList"
                style="width: 100%"
                v-loading="loading"
                element-loading-text="拼命加载中"
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(255, 255, 255, 0.8)"
                tooltip-effect="dark"
                :total="total">
            <el-table-column
                    prop="servicesCode"
                    label="序号">
            </el-table-column>
            <el-table-column
                    label="套餐名称"
                    prop="servicesName">
            </el-table-column>
            <el-table-column
                    label="描述"
                    prop="serviceDesc">
            </el-table-column>
            <el-table-column
                    label="适用用户"
                    prop="scopeNames">
                <template slot-scope="scope">
                    <div v-if="scope.row.scopeNames">
                        {{scope.row.scopeNames.join(',')}}
                    </div>
                    <div v-else>———</div>
                </template>
            </el-table-column>
            <el-table-column
                    label="促销价"
                    align="center"
                    width="290"
                    prop="priceList">
                <template slot-scope="scope">
                    <!--价格单元(0-年;1-月;2-日 class="priceShow"-->
                    <div class="priceShow" :key="index" v-for="(item,index) in scope.row.priceList">
                        {{item.duration}}
                        <span v-if="item.priceUnit == 0">年</span>
                        <span v-if="item.priceUnit == 1">月</span>
                        <span v-if="item.priceUnit == 2">日</span>
                        {{item.price}}元
                        <span v-if="item.giveDuration" class="addDay">送{{item.giveDuration}}天</span>
                        <p v-if="item.sanduCurrency || item.freeRenderDuration">
                            <span v-if="item.sanduCurrency">★ 赠送：{{item.sanduCurrency}}度币，</span>
                            <span v-if="item.freeRenderDuration">{{item.freeRenderDuration}}个月免费渲染</span>
                        </p>
                    </div>
                </template>
            </el-table-column>
            <!--<el-table-column
                    label="是否立即生效"
                    prop="effectiveStrategy">
                <template slot-scope="scope">
                    <div v-if="scope.row.effectiveStrategy">
                        {{scope.row.effectiveStrategy = 1 ? '使用时生效':'立即生效'}}
                    </div>
                </template>
            </el-table-column>-->
            <!--<el-table-column
                    label="其他"
                    prop="">
            </el-table-column>-->
            <el-table-column
                    label="操作"
            >
                <template slot-scope="scope">
                    <!--<el-button type="text" @click="viewFunc(scope.row)">功能详情</el-button>-->
                    <el-button type="text" @click="buy(scope.row)">购买</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--分页组件-->
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

        <el-dialog title="确认购买" width="740px" :close-on-click-modal="false" @close="closePay" :visible.sync="sureDialogFormVisible">
            <el-form style="padding:0 40px" label-width="100px">
                <el-row :gutter="24">
                    <el-form-item label="套餐名称：">
                        <el-col :span="10">
                            {{buyRow.servicesName}}
                        </el-col>
                    </el-form-item>
                    <el-form-item label="适用用户：">
                        <el-col :span="12" v-if="userValue">
                            {{userValue.join(',')}}
                        </el-col>
                    </el-form-item>
                    <el-form-item label="使用时长：" class="redStar">
                        <span class="star">*</span>
                        <el-col>
                            <el-radio-group v-model="selectPriceId" size="mini">
                                <el-radio class="mbottom marRight" border
                                          v-for="(item,index) in buyRow.servicesPriceVoList"
                                          :label="item.id" @change="getPriceId(item.id)"
                                          :key="index"
                                          :value="item.id"
                                >
                                    {{item.term}}{{item.price}}元
                                    <span class="red" v-if="item.giveDuration">送{{item.giveDuration}}天</span>
                                </el-radio>
                            </el-radio-group>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="购买数量：" class="redStar">
                        <span class="star">*</span>
                        <el-col :span="24">
                            <input class="numSize"
                                   min="1"
                                   oninput="value=value.replace(/[^\d]/g,'')"
                                   placeholder="请输入购买数量"
                                   maxlength="3"
                                   v-model="buyCount">
                            <span class="tipNum"> 只能输入数字</span>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="总价：">
                        <el-col :span="10" class="fontColor">
                            <span v-if="buyCount">{{priceSelect*buyCount}} 元</span>
                            <span v-else>{{priceSelect}} 元</span>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="支付方式：" class="redStar"  v-if="priceSelect>0">
                        <span class="star">*</span>
                        <!--支付方式(0-支付宝;1-微信;2-其他) *-->
                        <el-col :span="14">
                            <el-radio-group v-model="paySelect" @change="payMode" size="mini">
                                <el-radio label="0" style="margin-right: 8px">支付宝</el-radio>
                                <el-radio label="1" style="margin-right: 8px">微信</el-radio>
                                <!--<el-radio label="2">其他</el-radio>-->
                            </el-radio-group>
                        </el-col>
                    </el-form-item>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" :disabled="disabled" round size="small" @click="confirmBuyPackage">购
                    买
                </el-button>
                <el-button size="small" round @click="sureDialogFormVisible = false">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog title="功能详情" width="540px" height="400px" class="funcDetail" :visible.sync="dialogFuncVisible">
            <el-row style="height: 400px; overflow: hidden; overflow-y: auto">
                <el-col v-for="(item, index) in funcList" :key="index">
                    <div>{{item.platformName}} <img src="../../../assets/images/icons/selectStaus.png"> <span>特权</span>
                    </div>
                    <ul class="clearfix">
                        <li v-if="names" v-for="(names, idx) in item.funcNames" :key="idx">
                            <div>{{idx+1 < 10 ? '0'+(idx+1) : idx}}</div>
                            <p>{{names}}</p>
                        </li>
                    </ul>
                </el-col>
                <div style="display: none">三度云享家移动端 <img src="../../../assets/images/icons/selectStaus2.png"> <b>特权</b>
                </div>
                <ul style="display: none">
                    <li>
                        <span class="alltq">...</span>
                        <p>所有特权</p>
                    </li>
                </ul>
            </el-row>
        </el-dialog>
        <el-dialog  :close-on-click-modal="false" :title="paySelect == 0 ? '支付宝支付二维码': '微信支付二维码'" width="400px" height="400px" @close="closePay"
                   :visible.sync="codeImg">
            <!--<div><img :src="qrCodeUrl" alt=""></div>  https://qr.alipay.com/bax00977dnubkwak06q04004 -->
            <div class="codeFrame">
                <p>
                    <span v-if="paySelect == 0">请使用支付宝扫码支付</span>
                    <span v-if="paySelect == 1">请使用微信扫码支付</span>
                </p>
                <span class="payPrice" v-if="buyCount">￥{{priceSelect*buyCount}} 元</span>
                <div class="code">
                    <img src="../../../assets/images/icons/zhifupay.png" v-if="paySelect == 0">
                    <img src="../../../assets/images/icons/weixinpay.png" v-if="paySelect == 1">
                    <qrcode-vue :value="qrCodeUrl" :size="size" level="H"></qrcode-vue>
                </div>
                <span class="txt">三度云享家</span>
            </div>
        </el-dialog>
        <el-dialog title="支付成功" width="400px" top="25%" height="400px" :visible.sync="successDialog">
            <div class="successDialog">
                <span class="el-icon-success"></span>
                恭喜您，套餐购买成功
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import QrcodeVue from 'qrcode.vue'

    export default {
        data() {
            return {
                successDialog: false,
                userList: [],
                size: 200,
                selectList: [],
                codeImg: false,
                // ----------
                dialogFuncVisible: false, // 功能详情
                buyCount: '', //购买数量
                paySelect: '',
                priceSelect: '',
                selectPriceId: '',
                userTypeValue: '',
                buyRow: {},
                userValue: [],
                funcList: [],
                priceId: '',
                servicesId: '',
                qrCodeUrl: '',
                ws: '',
                stompClient: '',
                orderNo: '',
                //------------
                sureDialogFormVisible: false,
                loading: true,
                total: 0,
                query: {
                    page: 1,
                    limit: 10,
                    companyId: sessionStorage.getItem('companyID')
                }
            }
        },
        computed:{
            disabled(){
                if(this.buyCount>0 && this.selectPriceId!=''){
                    return false
                }else {
                    return true
                }
            }
        },
        created() {
            this.selectUser()
            this.getSelectList()
        },
        components: {
            QrcodeVue
        },
        destroyed() {
            clearInterval(this.ws)
        },
        methods: {
            websocket() { // 实时信息
                this.ws = setInterval(() => {
                    this.API.selectPackage({
                        orderNo: this.orderNo
                    }).then((res) => {
                        console.log(res, 'msg')
                        if (res) {
                            if (res.obj.status == 2) {
                                this.$message.error('支付失败')
                                clearInterval(this.ws)
                            }
                            if (res.obj.status == 1) {
                                this.codeImg = false
                                this.sureDialogFormVisible = false
                                //this.$message.success('支付成功')
                                this.successDialog = true
                                clearInterval(this.ws)
                            }
                        }
                    })
                }, 3000)
            },

            getPriceId(id) {
                this.buyRow.servicesPriceVoList.forEach(it => {
                        if (it.id === id) {
                            this.priceSelect = it.price
                        }
                    }
                )
                this.priceId = id
            },
            payMode(val) {
                console.log(val, 'pay')
            },
            buy(row) {
                this.selectPriceId='';
                this.sureDialogFormVisible = true;
                this.servicesId = row.servicesId
                this.API.packageDetail({
                    serviceId: row.servicesId,
                    // serviceId: 11,
                    userId: sessionStorage.getItem('userId'),
                    saleChannel: 3
                }).then((res) => {
                    if (!res.success) {
                        return;
                    }
                    this.buyRow = res.obj
                    if (this.buyRow.servicesPriceVoList) {
                        this.buyRow.servicesPriceVoList.map((item, index) => {
                            if (index === 0) {
                                //this.selectPriceId = item.price + '_selectPriceRadioGroup'
                                this.priceId = item.id
                            }
                        })
                    }
                })
                this.userValue = row.scopeNames
            },
            websocketMsg() { // 实时信息
                console.log(this.BASE_URL.systemUrl)
                this.ws = new WebSocket(`ws://system.ci.sanduspace.com/v1/services/payResponse`)
                //this.ws = new SockJS(this.BASE_URL.systemUrl+'/v1/services/payResponse')
                this.ws.onopen = () => {
                    // Web Socket 已连接上，使用 send() 方法发送数据
                    this.ws.send('Holle')
                    console.log('数据发送中...')
                }
                this.ws.onmessage = evt => {
                    // console.log('数据已接收...')

                }
                this.ws.onclose = function () {
                    // 关闭 websocket
                    console.log('连接已关闭...')
                }
            },
            confirmBuyPackage() {
                let buyCondition = {
                    serviceId: this.servicesId,// 套餐id *
                    priceId: this.priceId,  //套餐价格id *
                    // priceId: 3,  //套餐价格id *
                    payType: this.priceSelect==0 ? 2 : this.paySelect, // 支付方式(0-支付宝;1-微信;2-其他) *
                    // companyId: sessionStorage.getItem('companyID'), // 公司id *
                    companyId: sessionStorage.getItem('companyID'), // 公司id *
                    purchaseSource: 3,// 购买来源(1、三度官网，2、三度后台，3、商家后台，4、科创，5、抢工长)
                    businessType: 0,// 0-购买;1-续费;2-试用;3-代购
                    platformCode: 'merchantManage',//平台编码
                    purchaseAmount: this.buyCount,
                }
                if (!this.buyCount) {
                    this.$message.error('请输入购买数量')
                    return
                }
                if (!this.paySelect&& this.priceSelect>0) {
                    this.$message.error('请选择支付方式')
                    return
                }

                //去支付
                if(this.priceSelect==0){ //价格为0 去支付
                    this.paySelect=2;
                    this.API.confirmBuyTest(buyCondition).then(res=>{
                        if(res.success){
                            this.sureDialogFormVisible=false;
                            this.$message({
                                message: '恭喜你，购买成功',
                                type: 'success'
                            });
                        }else {
                            this.$message.error(res.message)
                        }
                    })
                }else {
                    this.API.confirmBuy(buyCondition).then(res => {
                        if (!res.success) {
                            this.$message.error(res.message)
                            return
                        }
                        let codeImg = JSON.parse(res.obj)
                        this.qrCodeUrl = codeImg.qrCodeUrl
                        this.orderNo = codeImg.payTradeNo
                        // this.websocketMsg()

                        if (this.qrCodeUrl) {
                            this.codeImg = true
                            this.websocket();
                        }
                    })
                }
            },
            closePay() {
                this.sureDialogFormVisible = true
                this.codeImg = false
                this.buyCount = ''
                this.paySelect = false
                this.priceSelect = ''
                this.sureDialogFormVisible = false
                clearInterval(this.ws)
            },
            viewFunc(row) {
                this.dialogFuncVisible = true
                this.getFuncList(row)
            },
            getFuncList(row) {
                this.API.funcDetail({
                    serviceId: row.servicesId
                }).then((res) => {
                    if (res) {
                        this.funcList = res.datalist
                    }
                })
            },
            selectUser() {
                this.API.getAllUserType({
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    if (res) {
                        this.userList = res.datalist
                    }
                })
            },
            // 分页模块
            handleSizeChange: function (size) {
                // this.pageSize = size;
                this.query.limit = size
                this.getSelectList()
            },
            handleCurrentChange: function (currentPage) {
                // this.currentPage = currentPage;
                this.query.page = currentPage
                this.loading = true
                this.getSelectList()
            },
            change(userTypeValue) {
                this.getSelectList(userTypeValue)
            },
            getSelectList(userTypeValue) {
                let queryCondition = Object.assign({}, this.query, {
                    userScope: userTypeValue,
                    saleChannel: 3
                })
                this.API.selectListAllService(queryCondition).then((res) => {
                    if (res) {
                        this.loading = false
                        if (res.datalist && res.datalist.length > 0) {
                            this.selectList = res.datalist
                        } else {
                            this.selectList = []
                        }
                        this.total = res.totalCount
                    }

                })
            }
        }

    }
</script>

<style lang="scss" scoped>
    @import '../../../assets/css/mixin';

    .dialog-footer {
        padding-top: 5px;
        text-align: center;
    }

    .redStar {
        position: relative;

    .star {
        position: absolute;
        left: -90px;
        top: 3px;
        color: red;
    }

    }
    .tipNum {
        color: #FF6419;
    }

    .fontColor {
        color: #FF6419;
    }

    .red {
        color: red;
    }

    .successDialog {
        color: #FF6419;
        text-align: center;
        font-size: 16px;

    span {
        color: #5daf34;
    }

    }
    .codeFrame {
        text-align: center;

    div {
        height: 200px;
        position: relative;

    img {
        position: absolute;
        top: 50%;
        left: 50%;
        width: 50px;
        background: #fff;
        border-radius: 8px;
        transform: translate(-50%, -50%);
        z-index: 11;
    }

    }
    .payPrice {
        font-size: 18px;
        display: inline-block;
        color: #FF6419;
        padding: 15px 0;
    }

    .txt {
        font-size: 12px;
        color: #999;
    }

    }

    .numSize {
        width: 200px;
        border-radius: 4px;
        border: solid 1px #ddd;
        height: 38px;
        padding-left: 10px;
        position: relative;
        z-index: 111;
    }

    .selectShowData {
        background: #fff;
        padding: 15px;

    .el-select {
        margin-bottom: 15px;
    }

    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .selectShowData {
        background: #fff;
        padding: 15px;
    }

    .el-select {
        margin-bottom: 15px;
    }

    .funcDetail {

    .el-row {
        margin-left: 6%;
    }

    div {
        font-weight: bold;
        font-size: 16px;
        margin-bottom: 10px;

    img {
        vertical-align: -4px;
    }

    span {
        color: #fac610;
    }

    b {
        color: #f43d2b
    }

    &:nth-of-type(2) {
        margin-top: 30px;
    }

    }
    ul {

    li {
        margin-right: 5px;
        display: inline-block;
        width: 80px;
        float: left;
        height: 80px;

    &:nth-child(odd) {
        background: #fafafa;
    }

    &:nth-child(even) {
        background: #fffbef;
    }

    .alltq {
        display: inline-block;
        margin-top: 12px;
        padding-left: 10px;
        font-size: 20px;
        font-weight: bold;
        color: #fac610;
    }

    p {
        margin: 0 10px 0 10px;
        font-size: 12px;
        font-weight: normal;
        line-height: 18px;
        color: #705c1f;
    }

    div {
        color: #fac610;
        font-size: 20px;
        font-family: Arial, "Helvetica Neue", Helvetica, sans-serif;
        margin: 8px 10px 0 10px;
    }

    }
    }
    }

    .priceShow {
        display: inline-block;
        min-height: 40px;
        line-height: 30px;
        border: solid 1px #ddd;
        margin-bottom: 5px;
        background: #f5f5f5;
        text-align: center;
        color: #FF6419;
        width: 260px;
        position: relative;

    p {
        color: red;
    }

    }
    .addDay {
        position: absolute;
        color: #fff;
        font-size: 12px;
        right: 0;
        top: 0;
        height: 16px;
        line-height: 16px;
        padding: 0 4px;
        background: red;
        /*border-top-left-radius: 10px;
        border-bottom-left-radius: 10px;*/
        &:before{
            content: '';
            position: absolute;
            left: -16px;
            width: 0;
            height: 0;
            border-width: 8px;
            border-style: solid;
            border-color:  transparent red transparent transparent;
        }
    }
</style>
<!--页面注释-->
<template>
    <div class="myDB">
        <header>
            <h1 class="title">我的度币</h1>
            <p class="money"><span class="moneylogo"></span>{{dubi}}<el-tooltip class="item" effect="dark" content="我的度币只能在平台进行使用，无法进行提现" placement="top">
            <span class="moneyhow"></span>
            </el-tooltip>
                </p>
            <el-button type="primary" round @click="goRecharge">充值度币</el-button>
        </header>
        <main>
            <el-tabs v-model="activeName" class="DBNav">
                <el-tab-pane v-if="showTab('check:consumeList:view','recharge')" label="消费记录" name="consume">
                    <consume></consume>
                </el-tab-pane>
                <el-tab-pane  v-if="showTab('check:rechargeList:view','consume')" label="充值记录" name="recharge">
                    <recharge ref="recharge"></recharge>
                </el-tab-pane>
            </el-tabs>
        </main>
        <el-dialog
                title="充值"
                :visible.sync="showRecharge"
                width="540px"
                top="30vh"
                class="chargeMode"
        >
            <p style="margin: 10px 0 0 40px;">充值金额(元)：
                <el-input v-model="priceNum"  
                          maxlength="5"
                          @keyup.native="number"
                          style="width:130px;"></el-input>
                <span class="huansuanImg"></span>度币：{{DBChange}}
            </p>
            <div>
                <el-form>
                    <el-form-item label="支付方式：" style="margin:10px 40px;">
                        <el-radio-group v-model="Priceform.payType" style="margin:0;">
                            <el-radio label="微信"></el-radio>
                            <el-radio label="支付宝" style="margin-left:80px!important;"></el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
          <el-button type="primary" round size="small" @click="goPay">确 定</el-button>
          <el-button @click="showRecharge = false" round size="small">取 消</el-button>
        </span>
        </el-dialog>
        <el-dialog
                title="支付"
                :visible.sync="showPayDialog"
                :before-close="ClearTimer"
                width="540px"
                top="30vh"
                class="chargeMode"
        >
            <div class="paybox">
                <p class="paytext" v-if="Priceform.payType=='微信'">请使用微信扫码支付</p>
                <p class="paytext" v-else>请使用支付宝扫码支付</p>
                <p class="paynum">￥{{priceNum}}.00</p>
                <!--<img width="200px" src="codeUrl" height="200px" style="background:#ccc;">-->
                <qrcode-vue :value="codeUrl" :size="size" level="H"></qrcode-vue>
                <p class="sandutext">三度云享家</p>
                <el-button round size="small" type="primary" v-on:click="refresh" plain>刷新二维码</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
    import Consume from './components/consume';
    import Recharge from './components/recharge';
    import QrcodeVue from 'qrcode.vue';
    import qs from 'qs';

    export default {
        watch: {

        },
        data() {
            return {
                timer:null,
                dubi: 0,
                activeName: 'consume',
                showRecharge: false,
                showPayDialog: false,
                priceNum: '',
                Priceform: {
                    payType: '微信'
                },
                DBChange: 0,
                codeUrl:'',
                orderNo : '',
                size:200,
                timer:'',
            }
        },
        methods: {
            showTab(permission,currentTab){
                let per = this.isPer(permission);
                if (!per){
                    this.activeName = currentTab
                }
                return per
            },
            isPer(per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            ClearTimer(){
                 clearInterval(this.timer);
                 this.timer = null;
                 this.showPayDialog = false;
            },
            goRecharge() {
                this.priceNum = '';
                this.DBChange = '';
                this.showRecharge = true;
            },
            RechargeCheckOut(){
              this.API.rechargeCheckOut({
                        orderNo: this.orderNo
                    }).then(res=>{
                       if(res.obj){
                           this.$message.success('充值成功!');
                           clearInterval(this.timer);
                            this.timer = null;
                            this.showPayDialog = false;
                            this.getUserDubiInfo();
                            this.$refs.recharge.getUserRechargeList(1);
                       }else{
                           console.log(res);
                       }
                    })
            },
            goPay() {
                if (this.priceNum == '' || this.priceNum.charAt(0) <= 0) {
                    this.$message.warning('请输入大于0的金额');
                    return
                }
                let formData = new FormData();
                formData.append("money",this.priceNum);
                formData.append("payType",this.Priceform.payType == '微信' ? 0 : 1);
                this.API.rechargeDubi(formData).then((res) =>{
                    clearInterval(this.timer);
                    this.timer = null;
                    let obj = res.obj;
                    this.codeUrl = JSON.parse(obj.payParam).qrCodeUrl;
                    this.orderNo = obj.orderNo;
                   this.timer = setInterval(this.RechargeCheckOut, 3000);
                })
                this.showPayDialog = true;
                this.showRecharge = false
            },
            number() {
                this.priceNum = this.priceNum.replace(/[^\ .\d]/g, '');
                this.priceNum = this.priceNum.replace('.', '');
                this.priceNum = this.priceNum.replace(/\s+/g,"");   
                this.DBChange = this.priceNum * 10;
            },
            getUserDubiInfo() {
                this.API.getUserDubiInfo().then((res) => {
                    if (res.success) {
                        if (res.obj.balanceAmount == undefined) {
                            this.dubi = 0;
                        } else {
                            this.dubi = res.obj.balanceAmount;
                        }
                    }
                })
            },
            refresh(){
                this.goPay();
            }
        },
        components: {
            Consume,
            Recharge,
            QrcodeVue
        },
        created() {
            this.getUserDubiInfo();
        },
        beforeDestroy(){
            clearInterval(this.timer);
            this.timer = null;
        }
    }
</script>

<style scoped lang="scss">
    header {
        background: #fffaf8;
        height: 76px;
        line-height: 76px;
        .title {
            font-size: 18px;
            color: #333333;
            font-weight: bold;
            display: inline-block;
            margin: 0 10px 0 20px;
        }
        .money {
            font-size: 18px;
            font-weight: bold;
            color: #ff6419;
            display: inline-block;
            .moneylogo, .moneyhow {
                display: inline-block;
                vertical-align: middle;
                width: 18px;
                height: 18px;
                background-size: 18px 18px;
                margin: 0 6px;
            }
            .moneylogo {
                background: url('../../../assets/images/icons/icon-dubi.png') no-repeat;
            }
            .moneyhow {
                background: url('../../../assets/images/icons/icon-shuoming.png') no-repeat;
                margin-right: 40px;
            }
        }
    }

    .DBNav {
        font-size: 14px !important;
    }

    .huansuanImg {
        width: 12px;
        height: 14px;
        background: url('../../../assets/images/icons/icon-huansuan.png') no-repeat;
        background-size: 12px 14px;
        display: inline-block;
        margin: 0 10px;
        vertical-align: middle;
    }

    .paybox {
        text-align: center;
        .paytext {
            color: #333333;
            font-size: 14px;
        }
        .paynum {
            color: #ff6419;
            font-size: 20px;
            margin: 20px 0;
        }
        .sandutext {
            color: #999999;
            font-size: 12px;
            margin-bottom: 20px;
        }
    }
</style>

<template>
    <el-dialog width="540px"
               top="15%"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :before-close="beforeClose"
               title="支付"
               :visible.sync="gettersPayCode">
        <div class="payFrame" >
            <div class="text">请使用{{gettersPackageParam.payType==1 ? '微信' : '支付宝'}}扫码支付</div>
            <p class="price" v-if="gettersBusinessType==1">￥{{gettersPackageParam.price}}</p>
            <p class="price" v-else-if="gettersBusinessType==4">￥{{gettersPackageParam.differPrice}}</p>
            <div class="payCode" v-if="gettersCodeUrl">
                <img src="../../assets/images/icons/zhifupay.png" v-if="gettersPackageParam.payType == 0">
                <img src="../../assets/images/icons/weixinpay.png" v-if="gettersPackageParam.payType == 1">
                <qrcode-vue :value="gettersCodeUrl.qrCodeUrl" :size="size" level="H"></qrcode-vue>
            </div>
            <p class="companyName">三度云享家</p>
            <p class="reflesh" @click.self="reflesh">刷新二维码</p>
        </div>
    </el-dialog>
</template>

<script>
    import { mapGetters,mapState} from "Vuex"
    import QrcodeVue from 'qrcode.vue'
    import myVue from '@/filters/bus'
    export default {
        data(){
            return{
                size: 200,
                timeOut:''
            }
        },
        computed:{
            ...mapGetters(['gettersPayCode','gettersCodeUrl','gettersPackageParam','gettersPriceId','gettersServiceId','gettersBusinessType','gettersUserId','gettersTimeOut']),
        },
        created(){
             myVue.$on('toPay',(e)=>{
                 this.reflesh()
             })
        },
        methods:{
            beforeClose(){
                clearTimeout(this.gettersTimeOut);
                this.$store.dispatch('dialog',{isShow:false,name:'payCode'});
            },
            reflesh(){
                //businessType 0-购买;1-续费;2-试用;3-代购 4-升级
                let loading=this.Loading.service({spinner:'el-icon-loading',text:'拼命加载中',fullscreen:false,background: 'rgba(255, 255, 255, 0.1)'});
                clearTimeout(this.gettersTimeOut);
                this.API.confirmPay({
                    serviceId:this.gettersServiceId,
                    priceId:this.gettersPriceId,
                    payType:this.gettersPackageParam.payType,
                    companyId:sessionStorage.getItem('companyID'),
                    userId:sessionStorage.getItem('userId'),
                    purchaseSource:0,
                    businessType:this.gettersBusinessType,
                    platformCode:'merchantManage',
                    accountUserId:this.gettersUserId,
                    purchaseAmount:1}).then(res=>{
                    if(res.success){
                        this.$store.dispatch('dialog',{isShow:false,name:'packagePurchase'});
                        setTimeout(()=> {
                            this.$store.dispatch('dialog',{isShow:true,name:'payCode'});
                        },300);
                        this.$store.dispatch('setCodeUrl',JSON.parse(res.obj));
                        this.requestOrder();
                    }else{
                        this.$message(res.message);
                    }
                    this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
                        loading.close();
                    });
                })
            },
            requestOrder(){ //连续请求
                this.API.selectPackage({orderNo:this.gettersCodeUrl.payTradeNo}).then(res=>{
                    if(res.obj.status=='1'){
                        setTimeout(()=> {
                            this.$store.dispatch('dialog',{isShow:false,name:'payCode'});
                            this.$notify({
                                title: '支付成功',
                                type: 'success',
                                duration:4000
                            });
                        },500);
                        clearTimeout(this.gettersTimeOut);
                    }else if(res.obj.status=='2'){
                        this.$store.dispatch('dialog',{isShow:false,name:'payCode'});
                        this.$notify.error({
                            title: '支付失败',
                            duration:4000
                        });
                        clearTimeout(this.gettersTimeOut);
                    }else if(res.obj.status=='0'){ //待支付
                        clearTimeout(this.gettersTimeOut);
                        this.$store.dispatch('setTimeOut',setTimeout(()=>{this.requestOrder();},3000))
                    }
                })
            }
        },
        components:{
            QrcodeVue
        }
    }
</script>

<style scoped lang="scss">
    .titleBg{
        background: #999;
        height: 40px;
        line-height: 40px;
        padding-left: 15px;
    }
    .payFrame{
        text-align: center;
        .text{
            font-size: 16px;
            color: #333333;
        }
        .price{
            color: #FF6419;
            font-size: 20px;
            padding-top: 10px;
            padding-bottom: 20px;
        }
        .companyName{
            padding-bottom: 20px;
            color: #666;
            font-size: 12px;
        }
        .reflesh{
            border: solid 1px #FF6419;
            color: #FF6419;
            font-size: 16px;
            height: 30px;
            line-height: 30px;
            display: inline-block;
            padding: 0 12px;
            border-radius: 20px;
            margin-bottom: 10px;
            cursor: pointer;
        }
        .payCode{
            position: relative;
            img{
                position: absolute;
                top: 50%;
                left: 50%;
                width: 50px;
                background: #fff;
                transform: translate(-50%,-50%);
                z-index: 10;
            }
        }
    }

</style>
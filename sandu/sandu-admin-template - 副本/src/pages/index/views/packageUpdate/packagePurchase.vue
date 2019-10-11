<template>
    <el-dialog
            title="套餐续费"
            :visible.sync="gettersPackagePurchase"
            :before-close="beforeClose"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            width="540px" top="20vh">
        <div class="packagePurchase">
            <!--<div class="title">套餐续费</div>-->
            <div class="content" v-if="gettersPackageList">
                <div class="content-header">
                    {{gettersPackageList.servicesName}}
                    <i class="ic-tip" @click="dialogTip=!dialogTip"></i>
                    <div class="dialog-tip" v-show="dialogTip">
                        <span></span>
                        <span></span>
                        <div class="dialog-title">三度云享家通用版套餐内容 : </div>
                        <div class="dialog-box">{{gettersPackageList.serviceDesc || '无'}}</div>
                        <!--<div class="dialog-box">PC通用版及移动端  1年使用期</div>-->
                        <p>使用套餐有相关问题请联系客服 </p>
                        <p>客服电话为：0755-23432434 </p>
                    </div>
                </div>
                <div class="content-choose">套餐年限选择: </div>
                <div style="overflow-x: scroll">
                    <ul :style="{ width:changeWidth }">
                        <li :class="{'active':item.active}" v-for="(item,key) in gettersPackageList.servicesPriceVoList" :key="key" @click="toggle(item,key)">
                            <span><em>{{item.price}}</em>元/{{item.term}}</span>
                            <i v-show="item.active"></i>
                            <!--<div class="ic_recommend" v-if="item.isRecommend"></div>-->
                        </li>
                    </ul>
                </div>
                <div class="content-text clearfix">
                    <div class="youhui fl">优惠：</div>
                    <div class="content-right fl" v-if="gettersPackageParam.giveDuration!=0||gettersPackageParam.sanduCurrency!=null||gettersPackageParam.freeRenderDuration!=null">
                        <p v-if="gettersPackageParam.giveDuration>0">1、赠送{{gettersPackageParam.giveDuration}}天使用时长</p>
                        <p v-if="gettersPackageParam.sanduCurrency>0">2、赠送{{gettersPackageParam.sanduCurrency}}度币</p>
                        <p v-if="gettersPackageParam.freeRenderDuration>0">3、赠送免费渲染时长{{gettersPackageParam.freeRenderDuration}}天</p>
                    </div>
                    <div v-else>暂无优惠</div>
                </div>
                <div class="pay-way clearfix" v-if="gettersPackageParam.differPrice>0||gettersBusinessType==1">
                    <div class="pay-text fl">支付方式:</div>
                    <div class="payBox fl" v-for="(item,key) in payList" :key="key" @click="choosePay(item)"><i :class="{'active':item.active}"></i>{{item.name}}</div>
                </div>
                <div class="payPrice"v-if="gettersBusinessType==1">应付：<em>￥{{gettersPackageParam.price}}</em></div>
                <div class="payPrice"v-else-if="gettersBusinessType==4">应付：<em>￥{{gettersPackageParam.differPrice}}</em></div>
                <input type="button" value="去支付" class="toPay" @click.self="pay">
            </div>
        </div>
    </el-dialog>

</template>

<script type="es6">
    import { mapActions,mapGetters,mapState } from 'Vuex'
    import myVue from '@/filters/bus'
    export default {
        name: "packagePurchase",
        data(){
            return{
                dialogTip:false,
                timeOut:'',
                payList:[ //支付方式
                    { name:'微信',active:true,payType:1 },
                    { name:'支付宝',active:false,payType:0 }
                ]
            }
        },
        computed:{
            ...mapGetters(['gettersPackageList','gettersPackageParam','gettersServiceId','gettersPackagePurchase','gettersPriceId','gettersCodeUrl','gettersTimeOut','gettersUserId','gettersBusinessType']),
            ...mapState(['timeout']),
            changeWidth(){
                return 180*this.gettersPackageList.servicesPriceVoList.length+'px';
            }
        },
        created(){

        },
        methods:{
            // ...mapActions(['packageAction']),
            beforeClose(){
                this.$store.dispatch('dialog',{isShow:false,name:'packagePurchase'});
            },
            toggle(item,index){
                console.log(item)
                this.gettersPackageList.servicesPriceVoList.map(res=>{
                    res.active=false
                });
                item.active=true;
                this.$set(this.gettersPackageList.servicesPriceVoList,index,this.gettersPackageList.servicesPriceVoList[index]);
                this.$store.dispatch('setPriceId',item.id);
                //改变信息变化
                this.$store.dispatch('setPackageParam',{giveDuration:item.giveDuration,sanduCurrency:item.sanduCurrency,freeRenderDuration:item.freeRenderDuration,price:item.price,differPrice:item.differPrice});
            },
            choosePay(item){
                this.payList.map(res=>{
                    res.active=false
                });
                item.active=true;
                this.$store.dispatch('setPackageParam',{payType:item.payType});
            },
            pay(){  //去支付
                if(this.gettersBusinessType==4){
                    if(this.gettersPackageParam.differPrice>0){
                        myVue.$emit('toPay');
                    }else {
                        this.API.confirmBuyTest({
                            serviceId:this.gettersServiceId,
                            priceId:this.gettersPriceId,
                            payType:2,
                            companyId:sessionStorage.getItem('companyID'),
                            userId:sessionStorage.getItem('userId'),
                            purchaseSource:0,
                            businessType:this.gettersBusinessType,
                            platformCode:'merchantManage',
                            accountUserId:this.gettersUserId,
                            purchaseAmount:1}).then(res=>{
                            if(res.success){
                                this.$store.dispatch('dialog',{isShow:false,name:'packagePurchase'});
                                this.$message({
                                    message: '恭喜你，购买成功',
                                    type: 'success'
                                });
                            }else {
                                this.$message.error(res.message)
                            }
                        })
                    }
                }else {
                    myVue.$emit('toPay');
                }

            }
        }
    }
</script>

<style lang='scss' scoped>
  .packagePurchase{
     .title{
         height: 40px;
         line-height: 40px;
         background-color: #fafafa;
         padding: 0 16px;
         font-size: 14px;
         color: #000000;
      }
     .content{
         .content-header{
             font-size: 18px;
             text-align: center;
             font-family: MicrosoftYaHei;
             color: #333333;
             position: relative;
             .ic-tip{
                display: inline-block;
                width: 18px;
                 height: 18px;
                 background: no-repeat center url('../../assets/images/ic_tip.png');
                 background-size: 100%;
                 vertical-align: middle;
             }
             .dialog-tip{
                 position: absolute;
                 right: 20px;
                 width: 244px;
                 background: #fff;
                 padding: 15px;
                 box-sizing: border-box;
                 border: 1px solid rgba(0,0,0,0.05);
                 font-size: 14px;
                 color: #333333;
                 text-align: left;
                 z-index: 11;
                 margin-top: 10px;
                 span:nth-child(1){
                     position: absolute;
                     top: -10px;
                     left: 82px;
                     width: 0;
                     height: 0;
                     border-left: 5px solid transparent;
                     border-right: 5px solid transparent;
                     border-bottom: 10px solid rgba(0,0,0,0.05);
                 }
                 span:nth-child(2){
                     position: absolute;
                     top: -8px;
                     left: 82px;
                     width: 0;
                     height: 0;
                     border-left: 5px solid transparent;
                     border-right: 5px solid transparent;
                     border-bottom: 10px solid #fff;
                 }
                 .dialog-title{
                     color: #333333;
                     font-size: 14px;
                 }
                 .dialog-box{
                     height: 30px;
                     line-height: 30px;
                     color: #333333;
                     font-size: 14px;
                     background-color: #f1f1f1;
                     margin-bottom: 5px;
                 }
                 p{
                     color: #999999;
                     font-size: 12px;
                     line-height: 16px;
                 }
             }
         }
         .content-choose{
             font-size: 14px;
             color: #333333;
             line-height: 32px;
             font-weight: bold;
             margin-top: 20px;
         }
         ul{
             margin-top: 10px;
             li{
                 width: 150px;
                 height: 81px;
                 line-height: 81px;
                 text-align: center;
                 border: solid 1px #dddddd;
                 position: relative;
                 display: inline-block;
                 span{
                     color: #333333;
                     font-size: 14px;
                     em{
                         color: #ff6419;
                         font-size: 20px;
                         font-style: normal;
                     }
                 }
                 i{
                     display: block;
                     width: 20px;
                     height: 20px;
                     background: no-repeat center url('../../assets/images/icons/ic_choose.png');
                     background-size: 100%;
                     position: absolute;
                     bottom: 0;
                     right: 0;
                 }
                 .ic_recommend{
                     position: absolute;
                     top: 0;
                     left: 0;
                     width: 35px;
                     height: 35px;
                     background: no-repeat center url('../../assets/images/icons/ic_recommend.png');
                     background-size: 100%;
                 }
             }
             .active{
                 border: 1px solid #ff6419;
             }
             li:nth-of-type(even){
                 margin-left: 20px;
                 margin-right: 20px;
             }
         }
         .content-text{
             margin-top: 30px;
             padding-bottom: 20px;
             border-bottom: 1px solid rgba(0,0,0,0.05);
             .youhui{
                 font-size: 14px;
                 color: #333333;
                 font-weight: bold;
             }
            .content-right{
                color: #666666;
                font-size: 14px;
                line-height: 24px;
                margin-left: 16px;
            }
         }
         .pay-way{
             color: #333333;
             font-size: 14px;
             height: 60px;
             line-height: 60px;
             .pay-text{
                 font-weight: bold;
             }
             .payBox{
                 margin-left: 20px;
                 margin-right: 80px;
                 cursor: pointer;
                 i{
                     display: inline-block;
                     width: 14px;
                     height: 14px;
                     background-color: #f5f5f5;
                     border: solid 1px #dddddd;
                     border-radius: 100%;
                     vertical-align: middle;
                     margin-right: 10px;
                 }
                 .active{
                     display: inline-block;
                     width: 15px;
                     height: 15px;
                     background: no-repeat center url("../../assets/images/icons/status2.png");
                     background-size: 100%;
                     border:0;
                 }
             }
         }
         .payPrice{
             height: 60px;
             line-height: 60px;
             color: #333333;
             font-size: 14px;
             font-weight: bold;
             border-top: 1px solid rgba(0,0,0,0.05);
             em{
                 color: #ff6419;
                 font-size: 20px;
                 font-style: normal;
                 font-weight: normal;
             }
         }
         .toPay{
             display: block;
             width: 200px;
             height: 36px;
             background-color: #ff6419;
             border-radius: 18px;
             margin: 0 auto;
             font-size: 16px;
             color: #fff;
         }
     }
  }
</style>
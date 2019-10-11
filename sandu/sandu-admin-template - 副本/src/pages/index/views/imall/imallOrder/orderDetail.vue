<template>
    <div v-loading="loadingVisible" :element-loading-text="GLOBAL.loadingText" >
        <v-nav :nav="nav"></v-nav>
        <el-row style="border-bottom:3px solid #eaeefb;background-color:#fff;padding:16px;">
            <el-col :span="12" style="text-align:left">
                <span style="line-height:30px;font-size:20px;color:#666;font-weight:bold;">订单详情</span>
            </el-col>
            <el-col :span="12" style="text-align:right;">
                <el-button @click="handleClickPrint()" type="text" size="small">打印</el-button>
                &nbsp;|
                <el-button v-if="orderData.status==1"  @click="handleClickAffirm()" type="text" size="small">确认</el-button>
                <el-button v-else-if="orderData.status==2" @click="handleClickSend()" type="text" size="small">发货</el-button>
                <el-button v-else-if="orderData.status==4" @click="complete" type="text" size="small">完成</el-button>
                <span v-else style="font-size:14px">{{formatOrderStatus(orderData.status)}}</span>

            </el-col>
        </el-row>
        <div style="background-color:#fff;padding:20px;">
            <fieldset class="field-title">
                <legend>订单信息</legend>
            </fieldset>
            
            <table class="table" border="1" cellspacing="0" cellpadding="0">
                <thead>
                    <tr class="table-head">
                        <td style="height:30px;inline-height:30px;width:130px">订单号</td>
                        <td width="120px">创建时间</td>
                        <td width="120px">订单总积分</td>
                        <td width="120px">商品总积分</td>
                        <td width="80px">配送费</td>
                        <td width="100px">订单状态</td>
                        <td width="100px">支付状态</td>
                        <td style="display:table-cell;vertical-align:middle;width:300px">
                            <span style="float:left;margin-left:10px;height:30px;line-height:30px">配送信息</span>
                            <span style="float:right;margin-right:10px;">
                                  <el-button v-clipboard:copy="teamLink" v-clipboard:success="onCopy" v-clipboard:error="onError" @click="copy(addressData)" type="text" size="small">复制</el-button>
                                &nbsp;|<el-button @click="handleClickUpdateAddress(addressData)" type="text" size="small">修改</el-button>
                            </span>
                        </td>
                        <td style="width:180px;">配送信息</td>
                    </tr>
                </thead>
                <tr class="order-info-data" >
                    <td>{{orderData.orderNo}}</td>
                    <td>{{orderData.gmtCreate}}</td>
                    <td>{{orderData.totalPoint}}</td>
                    <td>{{orderData.totalPoint}}</td>
                    <td>{{orderData.expressFee}}</td>
                    <td style="color:#FF6419">{{formatOrderStatus(orderData.status)}}</td>
                    <td style="color:#FF6419">{{formatOrderPaymentStatus(orderData.paymentStatus)}}</td>
                    <td>
                        <table>
                            <tr>
                                <td style="width:80px;">姓名：</td><td>{{addressData.consignee}}</td>
                            </tr>
                            <tr>
                                <td>电话：</td><td>{{addressData.mobile}}</td>
                            </tr>
                            <tr>
                                <td>邮政编码：</td><td>{{addressData.zipcode}}</td>
                            </tr>
                            <tr>
                                <td>省份：</td><td>{{addressData.provinceName}}</td>
                            </tr>
                            <tr>
                                <td>城市：</td><td>{{addressData.cityName}}</td>
                            </tr>
                            <tr>
                                <td>区域：</td><td>{{addressData.areaName}}</td>
                            </tr>
                            <tr>
                                <td>详细地址：</td><td>{{addressData.address}}</td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <table>
                            <tr>
                                <td style="width:80px;">快递公司:</td><td>{{orderData.expressCompay}}</td>
                            </tr>
                            <tr>
                                <td>运单号:</td><td>{{orderData.expressNo}}</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            
            <fieldset class="field-title">
                <legend>订单明细</legend>
            </fieldset>
            <table class="table" border="1" cellspacing="0" cellpadding="0">
                <thead>
                    <tr class="table-head">
                        <td style="height:30px;inline-height:30px;width:130px;">序号</td>
                        <!-- <td width="120px">编号</td> -->
                        <td>商品</td>
                        <td width="80px">数量</td>
                        <td width="80px">积分</td>
                        <td width="80px">合计</td>
                    </tr>
                </thead>
                <tr class="order-detail" style="padding:10px;" v-for="(item,index) in items" :key="index">
                    <td style="text-align:center">{{index+1}}</td>
                    <!-- <td style="text-align:center">{{item.code}}</td> -->
                    <td>
                        <img style="width:90px;height:90px;float:left" :src="item.itemSmall | filter"/>
                        <div style="float:left;margin:10px;">
                            <span class="gift-name">{{item.itemName}}</span>
                        </div>
                    </td>
                    <td style="text-align:right;">{{item.itemCount}}</td>
                    <td style="text-align:right;color:#FF6419">{{item.itemPoint}}</td>
                    <td style="text-align:right;color:#FF6419">{{item.itemCount*item.itemPoint}}</td>
                </tr>
            </table>

            <fieldset class="field-title">
                <legend>支付信息</legend>
            </fieldset>
            <table class="table" border="1" cellspacing="0" cellpadding="0">
                <thead>
                    <tr class="table-head">
                        <td style="height:30px;inline-height:30px;width:130px;">订单号</td>
                        <td width="120px">支付方式</td>
                        <td width="120px">支付积分</td>
                        <td width="160px">支付时间</td>
                        <td width="120px">支付状态</td>
                        <td>备注</td>
                    </tr>
                </thead>
                <tr class="order-detail" style="padding:10px;">
                    <td style="text-align:left">{{orderData.orderNo}}</td>
                    <td style="text-align:center">积分</td>
                    <td style="text-align:right">{{orderData.paymentPoint}}</td>
                    <td style="text-align:center">{{orderData.paymentDate}}</td>
                    <td style="text-align:center;color:#FF6419;">{{formatOrderPaymentStatus(orderData.paymentStatus)}}</td>
                    <td>{{orderData.remark}}</td>
                </tr>
            </table>

            <fieldset class="field-title">
                <legend>订单日志</legend>
            </fieldset>
            <table class="table" border="1" cellspacing="0" cellpadding="0">
                <thead>
                    <tr class="table-head">
                        <td style="height:30px;inline-height:30px;width:130px;">序号</td>
                        <td width="120px">操作人</td>
                        <td width="120px">操作人类型</td>
                        <td width="160px">操作时间</td>
                        <td>日志</td>
                    </tr>
                </thead>
                <tr class="order-detail" style="padding:10px;" v-for="(item,index) in logs" :key="index">
                    <td style="text-align:left">{{index+1}}</td>
                    <td style="text-align:center">{{item.creator}}</td>
                    <td style="text-align:center">{{formatCreatorType(item.creatorType)}}</td>
                    <td style="text-align:center;">{{item.gmtCreate}}</td>
                    <td style="text-align:left;">{{item.content}}</td>
                </tr>
            </table>
        </div>
        <!-- 发货 -->
        <dialogOrderSend ref="dialogOrderSend" ></dialogOrderSend>
        <!-- 打印 -->
        <dialogOrderPrint ref="dialogOrderPrint"></dialogOrderPrint>
        <!-- 确认 -->
        <dialogOrderAffirm ref="dialogOrderAffirm" ></dialogOrderAffirm>
        <!-- 修改配送信息 -->
        <dialogOrderAddress ref="dialogOrderAddress" ></dialogOrderAddress>
    </div>
</template>
<script>

import Breadcrumb from '@/components/Breadcrumb'
import imall from '@/api/imall'
import { formatOrderStatus,formatOrderPaymentStatus,formatCreatorType} from '@/utils/statusFormat'
import dialogOrderPrint from './components/orderPrint.vue'
import dialogOrderSend from './components/orderSend.vue'
import dialogOrderAffirm from './components/orderAffirm.vue'
import dialogOrderAddress from './components/orderAddress.vue'
import Vue from 'vue'
import vueClipboard from 'vue-clipboard2'
Vue.use(vueClipboard);
export default {
    name:'order_detail',
    components:{
        Breadcrumb
        ,dialogOrderPrint
        ,dialogOrderSend
        ,dialogOrderAffirm
        ,dialogOrderAddress
    },
    data() {
        return {
            copyContent:'',
            loadingVisible:true,
            orderId:this.$route.params.id,
            orderData:{},
            addressData:{address:'江苏省 宿迁市 宿城区千岛湖路1号',consignee:'张小天',mobile:'15051302105'},//快递信息
            items:[],//礼品信息
            logs:[],//
            nav: [
                { nav: "礼品订单列表", url: "/imall/imallOrder/orderList/10" },
                { nav: "订单详情", url: "/goodsEdit" }
            ],
        };
    },
    computed:{
        teamLink(){
            return this.copyContent
        }
    },
    provide(){
      return{
          refreshMethod:this.refreshMethod
      }
  },
  created() {
    this.getData()
    //this.getList()
    // fetchstructure().then((res=>{
    //   this.structureList=res.data.list
    // }))
  },
    filters:{
        filter(value){
            if(value){
                return process.env.sourceBaseUrl+"/"+value;
            }
        }
    },
  methods: {
    formatOrderStatus,formatOrderPaymentStatus,formatCreatorType
    ,refreshMethod(){//子页面调用刷新
      this.getData()
    },
    complete(){ //完成
        this.$confirm('此操作将完成该订单, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            this.mallApi.completeOrder({orderId:this.orderId}).then(res=>{
                if(res.code==200){
                    this.$message.success('完成订单');
                }
            })
        }).catch(() => {
            this.$message.error(res.result);
        });

      },
      onCopy(){
          this.$message({
              message:'复制成功！',
              type:'success'
          })
      },
      onError(){
          this.$message({
              message:'复制失败！',
              type:'error'
          })
      },
      getData() {
        this.loadingVisible=true
        var params={id:this.orderId}
          this.mallApi.getImallOrder(params).then(data => {
            this.orderData=data.data
            if (this.orderData.addressList!=null&&this.orderData.addressList.length>0){
                this.addressData=this.orderData.addressList[0];
            }
            this.items=this.orderData.itemList
            this.logs=this.orderData.logList
            this.loadingVisible = false
        })
    },
    handleClickSend(){//打开发货弹出框
      let dialog=this.$refs.dialogOrderSend;
      dialog.show(this.orderId);
    },
    handleClickAffirm(){//打开确认弹出框
      let dialog=this.$refs.dialogOrderAffirm;
      dialog.show(this.orderId);
    },
    handleClickPrint(){//打开打印弹出框
      let dialog=this.$refs.dialogOrderPrint;
      dialog.show(this.orderId);
    },
    handleClickUpdateAddress(address){
        let dialog=this.$refs.dialogOrderAddress;
        dialog.show(address);
    },
    copy(address){ //复制到剪切板
        this.copyContent=`姓名：${address.creator} 电话：${address.mobile} 地址：${address.provinceName}${address.cityName}${address.areaName}${address.address} 邮编：${address.zipcode}`;
    }
    
  }
};
</script>
<style type="text/css" scoped>
.order-info-data>td .el-row{
    padding:2px 0 5px 0;
}

.table-head{
    text-align:center;
    background-color:#FAFAFA;
    color:#AAA;
}

.table{
    width:100%;
    border:1px solid #bbb;
    border-collapse: collapse;
}

.field-title{
    margin-left:10px;
    margin-top:20px;
    margin-bottom:20px;
}

.order-detail>td{
    padding: 5px;
    font-size:14px;
}

.order-info-data>td{
    padding:10px;
    vertical-align:top;
    font-size: 14px;
}

.order-info-data>td td:nth-child(1){
    color:#999;
    font-size:13px;
}
.order-info-data>td td:nth-child(2){
    font-size:14px;
}
</style>
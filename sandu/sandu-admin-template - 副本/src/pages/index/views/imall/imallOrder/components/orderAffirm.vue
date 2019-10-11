<template>
    <!-- 确认订单 -->
    <el-dialog title="确认订单" :visible.sync="visible" width="650px" v-loading="loadingVisible" :element-loading-text="GLOBAL.loadingText">
        <div id="print">
        <el-row class="print-order">
            <el-col :span="6">
            <p>订单号：</p>
            <p>{{orderData.orderNo}}</p>
            </el-col>
            <el-col :span="6">
            <p>配送费：</p>
            <p>{{orderData.expressFee}}</p>
            </el-col>
            <el-col :span="6">
            <p>商品数量：</p>
            <p>{{orderData.totalCount}}</p>
            </el-col>
            <el-col :span="6">
            <p>订单总积分：</p>
            <p>{{orderData.totalPoint}}</p>
            </el-col>
        </el-row>
        <el-row class="print-order">
            <el-col :span="6">
                <p>下单日期：</p>
                <p>{{orderData.gmtCreate}}</p>
            </el-col>
            <el-col :span="18">
            <p>收货人：</p>
            <b>{{addressData.address}}, {{addressData.consignee}}, {{addressData.mobile}}</b>
            </el-col>
        </el-row>
        <table style="margin-top:15px;width:100%;border:1px solid #bbb;border-collapse: collapse;" border="1" cellspacing="0" cellpadding="0">
            <thead >
            <tr style="text-align:center;background-color:#e8e8e8;">
                <td style="height:30px;inline-height:30px;">商品</td>
                <td width="60px">积分</td>
                <td width="60px">数量</td>
            </tr>
            </thead>
            <tr v-for="(item,index) in items" :key="index">
            <td style="padding:10px;">
                <img style="width:90px;height:90px;float:left" :src="item.itemSmall | filter"/>
                <div style="float:left;margin:10px;">
                <span class="gift-name">{{item.itemName}}</span>
                </div>
            </td>
            <td style="text-align:right;padding:10px;">{{item.itemPoint}}</td>
            <td style="text-align:right;padding:10px;">{{item.itemCount}}</td>
            </tr>
        </table>
        </div>
        <div slot="footer" class="dialog-footer" v-loading="loadingVisible">
        <el-button ref="btnAffirm" :loading="btnAffirm_loading" :disabled="btnAffirm_disabled" @click="doAffirm()" type="primary" size="mini" round>确认无误</el-button>
        <!--<el-button ref="btnRefundment" :loading="btnRefundment_loading" :disabled="btnRefundment_disabled" @click="doRefundment()" size="mini" type="text">退 款</el-button>-->
        </div>
    </el-dialog>      
</template>

<script>
import imall from '@/api/imall'
import {print} from '@/utils/tools'
import store from '@/store'

export default{
    name:'orderPrint',
    data(){
        return {
            btnAffirm_loading:false,
            btnAffirm_disabled:false,
            btnRefundment_loading:false,btnRefundment_disabled:false,
            orderId:0,
            loadingVisible:true,
            visible:false,
            orderData:{}
            ,addressData:{address:'江苏省 宿迁市 宿城区千岛湖路1号',consignee:'张小天',mobile:'15051302105'}//快递信息
            ,items:[]//礼品信息
        }
        
    },
    filters:{
        filter(value){
            return process.env.sourceBaseUrl+"/"+value;
        }
    },
    created() {
        //this.getData()
        
    },
    mounted () {
        this.$nextTick(function () {
            let that = this
            
        })
    },
    inject:['refreshMethod'],
    methods:{
        show(id){
            this.visible=true
            this.orderId=id;
            this.loadingVisible = true
            var params={id:this.orderId}
            this.mallApi.getImallOrder(params).then(data => {
                this.orderData=data.data
                if (this.orderData.addressList!=null&&this.orderData.addressList.length>0){
                    this.addressData=this.orderData.addressList[0]
                }
                this.items=this.orderData.itemList
                this.loadingVisible = false
            })
        }
        ,doAffirm(){//确定
            let that=this
            that.btnAffirm_loading=true
            that.btnRefundment_disabled=true
            var params={orderId:this.orderId}
            this.mallApi.imallOrderAffirm(params).then(data => {
                //操作中
                //that.$refs.btnAffirm
                if (data.code==200){
                    that.$message.success("订单确认成功")
                    that.visible=false
                    that.refreshMethod()//回调父窗口刷新
                }else{
                    that.$message.warning("异常:"+data.message)
                }
                that.btnAffirm_loading=false
                that.btnRefundment_disabled=false
            })
        }
        ,doRefundment(){//退款
            let that=this
            var params={orderId:this.orderId}
            that.btnAffirm_disabled=true
            that.btnRefundment_loading=true
            this.mallApi.imallOrderRefundment(params).then(data => {
                //操作中
                if (data.code==200){
                    that.$message.success("退款成功")
                    that.visible=false
                    that.refreshMethod()//回调父窗口刷新
                }else{
                    that.$message.warning("异常:"+data.message)
                }
                that.btnAffirm_disabled=false
                that.btnRefundment_loading=false
            })
        }
    }
}
</script>
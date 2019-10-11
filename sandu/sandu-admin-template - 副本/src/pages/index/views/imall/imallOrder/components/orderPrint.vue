<template>
    <!-- 打印订单 -->
    <el-dialog title="打印订单" :visible.sync="visible" width="650px" v-loading="loadingVisible" :element-loading-text="GLOBAL.loadingText">
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
        <div slot="footer" class="dialog-footer">
        <el-button @click="doPrint()" type="primary" size="mini" round>打 印</el-button>
        <el-button @click.native="doClose()" size="mini" round>取 消</el-button>
        </div>
    </el-dialog>      
</template>

<script>
import imall from '@/api/imall'
import {tools} from '@/utils/tools'


export default{
    name:'orderPrint',
    data(){
        return {
            orderId:0,
            loadingVisible:true,
            visible:false,
            orderData:{}
            ,addressData:{address:'江苏省 宿迁市 宿城区千岛湖路1号',consignee:'张小天',mobile:'15051302105'}//快递信息
            ,items:[]//礼品信息
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
    filters:{
        filter(value){
            if(value){
                return process.env.sourceBaseUrl+"/"+value;
            }
        }
    },
    methods:{
        doPrint(){//打印
            tools.print('print');
        }
        ,show(id){
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
        ,doClose(){
            this.visible=false
        }
    }
}
</script>
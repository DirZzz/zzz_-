<template >
    <!-- 发货 -->
    <el-dialog title="订单发货" :visible.sync="visible" width="650px"  >
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
        <p style="padding:10px">
        收货人：<b>{{addressData.address}}, {{addressData.consignee}}, {{addressData.mobile}}</b>
        </p>
        <p>
            <el-form :inline="true" >
                <el-form-item label="物流公司：">
                <el-select v-model="expressCompay" style="width:180px" placeholder="物流公司">
                    <el-option
                    v-for="item in expressCompanys"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                    </el-option>
                </el-select>
                </el-form-item>
                <el-form-item label="快递单号：">
                <el-input v-model="expressNo" style="width:180px" placeholder="快递单号" maxlength="30"></el-input>
                </el-form-item>
            </el-form>
        </p>
        <p style="color:#FF6419">*请仔细填写物流公司及快递单号</p>
        <div slot="footer" class="dialog-footer" v-loading="loadingVisible" >
            <el-button ref="btnSend" @click="doSend()" type="primary" size="mini" round>确 定</el-button>
            <el-button @click="doClose()" size="mini" round>取 消</el-button>
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
            orderId:0
            ,visible:false
            ,loadingVisible:true
            ,orderData:{}
            ,addressData:{address:'江苏省 宿迁市 宿城区千岛湖路1号',consignee:'张小天',mobile:'15051302105'}//快递信息
            ,items:[]//礼品信息
            ,expressCompay:''
            ,expressNo:''
            ,expressCompanys:[
                {label:'顺丰快递',value:'顺丰快递'},
                {label:'圆通快递',value:'圆通快递'}
            ],
        }
        
    },
    filters:{
        filter(value){
            return process.env.sourceBaseUrl+"/"+value;
        }
    },
    inject:['refreshMethod'],
    created() {
        //this.getData()
    },
    mounted () {
        this.$nextTick(function () {
            let that = this
        })
    },
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
        ,doSend(){//发货
            let that=this
            if(that.expressCompay==''){
                that.$message.warning("请选择快递公司")
                return
            }
            if(that.expressNo==''){
                that.$message.warning("请输入快递单号")
                return
            }
            that.loadingVisible=true
            var params= {
                    orderId:this.orderId,
                    expressCompay:this.expressCompay,
                    expressNo:this.expressNo
                }
            this.mallApi.imallOrderSend(params).then(data => {
                //操作中
                //that.$refs.btnAffirm
                if (data.code==200){
                    that.$message.success("发货成功")
                    that.visible=false
                    that.refreshMethod()//回调父窗口刷新
                }else{
                    that.$message.warning("异常:"+data.message)
                }
                that.loadingVisible=false
            })
        }
        ,doClose(){//关闭
            this.visible=false
        }
    }
}
</script>
<template >
    <div v-loading="loading" :element-loading-text="GLOBAL.loadingText" style="margin-top:10px;margin-left:6px;margin-right:6px;">
        <el-row :gutter="12" style="line-height:48px;background-color:#fff;padding:10px">
            <el-col :span="6" >
                <div class="col">
                    <span class="totalFont">
                        <router-link style="display:block;" tag="span" :to="'/imall/imallOrder/orderList/0'">
                            {{totalData.noPay}}
                        </router-link>
                    </span>
                    <span>未付款</span>
                </div>
            </el-col>
            <el-col :span="6" >
                <div class="col">
                    <span class="totalFont">
                        <router-link style="display:block;" tag="span" :to="'/imall/imallOrder/orderList/1'">
                            {{totalData.awaitSend}}
                        </router-link>
                    </span>
                    <span>待确认</span>
                </div>
            </el-col>
            <el-col :span="6" >
                <div class="col">
                    <span class="totalFont">
                        <router-link style="display:block;" tag="span" :to="'/imall/imallOrder/orderList/4'">
                            {{totalData.awaitReceive}}
                        </router-link>
                    </span>
                    <span>待签收</span>
                </div>
            </el-col>
            <el-col :span="6" >
                <div class="col">
                    <span class="totalFont">
                        <router-link style="display:block;" tag="span" :to="'/imall/imallOrder/orderList/5'">
                            {{totalData.complete}}
                        </router-link>
                    </span>
                    <span>已完成</span>
                </div>
            </el-col>
        </el-row>

    </div>
</template>
<script>

    import Breadcrumb from '@/components/Breadcrumb'
    import imall from '@/api/imall'

    export default {
        name:'gift_create',
        components:{
            Breadcrumb
        },
        data() {
            return {
                totalData:{
                    noPay:0,//未付款
                    awaitSend:'0',//待发货
                    awaitReceive:0,//待签收
                    complete:0//完成
                },
                loading:false
            };
        },
        created() {
            this.shortcut(7);
        },
        methods: {
            shortcut(day){
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * day);
                this.getData(start, end);
            },
            getData(startDate,endDate) {
                let that=this;
                this.loading = true;
                var params={startDate:startDate,endDate:endDate};
                this.mallApi.imallOrderCollectStatusCount(params).then(data => {
                    if(data.list!=null){
                        for(var i=0;i<data.list.length;i++){
                            let item=data.list[i];
                            if (item.status==0){//待付款
                                that.totalData.noPay=item.total
                            }else if (item.status==1){//待发货
                                that.totalData.awaitSend=item.total
                            }else if (item.status==4){//待签收
                                that.totalData.awaitReceive=item.total
                            }else if (item.status==5){//待签收
                                that.totalData.complete=item.total
                            }

                        }
                    }

                    this.loading = false
                })
            }
        }
    };
</script>
<style type="text/css" scoped>
    .col{
        background-color:#fafafa;
        border-radius: 4px;
        padding:20px;
    }

    .totalFont{
        /* right:10px; */
        color: #ff6419;
        cursor:pointer;
        font-size:2.6em;
        display: block;
    }
</style>
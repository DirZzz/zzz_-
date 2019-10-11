<template>
    <div class="detailWheelAct">
        <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/wheel' }">幸运大转盘活动</el-breadcrumb-item>
            <el-breadcrumb-item>详情</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="detail-content">
            <el-form class="demo-form-inline">
                <el-form-item label="活动名称:">{{detailData.name}}</el-form-item>
                <el-form-item label="活动状态:">{{detailData.statusCode==0 ? '未开始': detailData.statusCode==10 ? '进行中' : detailData.statusCode==20 ? '已结束' : '无'}}</el-form-item>
                <el-form-item label="开始时间:">{{detailData.beginTime}}</el-form-item>
                <el-form-item label="结束时间:">{{detailData.endTime}}</el-form-item>
                <el-form-item label="抽奖条件:">{{award}}</el-form-item>
                <el-form-item label="规则:">{{detailData.rule}}</el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "detailWheelAct",
        data(){
            return{
                detailData:''
            }
        },
        computed:{
            award(){
                if(this.detailData.configItem==1){
                    return `每人每天默认次数:${this.detailData.lotteryNumPerDayDefalut}, 每人每天最大次数:${this.detailData.lotteryNumPerDayMax}`;
                }else if(this.detailData.configItem==2){
                    return `每人每天默认次数:${this.detailData.lotteryNumDefalut}, 每人每天最大次数:${this.detailData.lotteryNumMax}`;
                }
            }
        },
        created(){
            this.API.luckywheelDetail({actId:this.$route.query.id}).then(res=>{
                if(res.success){
                   this.detailData=res.obj;
                }else {
                    this.$message.error(res.message);
                }
            })
        }
    }
</script>

<style scoped lang="scss">
   .detailWheelAct{
       background-color: #fff;
       width: 100%;
       height: 100%;
       padding: 20px 30px;
       box-sizing: border-box;
       text-align: left;
       .breadcrumb{
           height: 40px;
           line-height: 40px;
       }
   }
</style>
<template>
    <el-dialog
            title="取消分配"
            :before-close="beforeClose"
            :visible.sync="cancelDistribution"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            width="540px" top="30vh">
        <div class="cancelDistribution">
             <div class="content">确定将该 <span>“{{channelCompanyName}}”</span>取消分配吗？</div>
             <div class="btn">
                 <el-button type="primary" @click="confirm" style="width: 80px" class="confirmButtonClass" size="medium">确 定</el-button>
                 <el-button @click="isShow('close')" style="width: 80px" class="cancelButtonClass" size="medium">取 消</el-button>
             </div>
        </div>
    </el-dialog>
</template>

<script>
    export default {
        name: "cancelDistribution",
        data(){
            return{
                cancelDistribution:false,
                channelCompanyId:'',
                channelCompanyName:''
            }
        },
        methods:{
            beforeClose(){
                this.cancelDistribution=false;
            },
            isShow(type,channelCompanyId,channelCompanyName){
                this.channelCompanyId=channelCompanyId;
                this.channelCompanyName=channelCompanyName;
                type=='show' ? this.cancelDistribution=true : this.cancelDistribution=false;
            },
            confirm(){
               this.API.cancelCustomer({companyId:this.channelCompanyId}).then(res=>{
                   if(res.code=='200'){
                       this.cancelDistribution=false;
                       this.$message({
                           message: '恭喜你，成功取消分配',
                           type: 'success'
                       });
                       this.$emit('refresh')
                   }else {
                       this.$message(res.message);
                   }
               })
            }
        }
    }
</script>

<style lang="scss" scoped>
   .cancelDistribution{
       .content{
           line-height: 36px;
           color: #333333;
           padding-bottom: 31px;
           span{
               color: #ff2323;
           }
       }
       .btn{
          display: flex;
          justify-content: flex-end;
           .cancelButtonClass{
               border: 0!important;
               background: #ffdbc9!important;
               color: #ff6419!important;
           }
       }
   }
</style>
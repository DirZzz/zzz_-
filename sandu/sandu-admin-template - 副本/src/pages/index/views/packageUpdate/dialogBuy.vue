<template>
    <el-dialog width="540px"
               top="20%"
               :before-close="beforeClose"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               title="套餐购买"
               :visible.sync="gettersDialogBuy">
        <div class="packageBuy" >
            <!--<div class="showTxt">您的账号使用期限还剩{{days}}天，请及时续费或升级</div>-->
            <div class="btn">
                <img src="../../assets/images/updateBtn1.png" alt="" @click.self="packageUpdate" :class="{'servicesTypeClass':gettersServicesType=='1'}" >
                <img src="../../assets/images/updateBtn2.png" alt="" @click.self="packageBuy" v-if="gettersServicesType!='1'">
            </div>
        </div>
    </el-dialog>
</template>

<script>
    import { mapGetters} from "Vuex"
    import myVue from '@/filters/bus'
    export default {
        computed:{
            ...mapGetters(['gettersDialogBuy','gettersUserId','gettersServicesType']),
        },
        methods:{
            packageUpdate(){ //套餐升级
                myVue.$emit('choosePackage');
            },
            packageBuy(){ //套餐购买
                this.$store.dispatch('goOnPackageAction',{vm:this,userId:this.gettersUserId});
            },
            beforeClose(){
                this.$store.dispatch('dialog',{isShow:false,name:'dialogBuy'});
            }
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
    .packageBuy{
        .showTxt{
            margin-left: 30px;
            margin-bottom: 30px;
            font-size: 16px;
        }
        .btn{
            text-align: center;
            img{
                cursor: pointer;
            }
            img:first-of-type{
                margin-right: 60px;
            }
            .servicesTypeClass{
                margin-right: 0!important;
            }
        }
    }

</style>
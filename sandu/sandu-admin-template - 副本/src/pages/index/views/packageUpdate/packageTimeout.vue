<template>
    <el-dialog width="540px"
               top="20%"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :before-close="beforeClose"
               title="提示"
               :visible.sync="gettersPackageTimeout">
        <div class="packageBuy">
            <div class="packageTimeoutTip">
                <div>您订购的{{tryoutPackage}}套餐已到期，<el-button round type="primary" size="mini" >{{title}}</el-button></div>
                <p>三度客服：0755-23895307</p>
            </div>
        </div>
    </el-dialog>
</template>

<script>
    import { mapGetters} from "Vuex"
    export default {
        props: {
            tryoutPackage:{
                type: String,
                default:null
            },
            title:{
                type:String,
                default:null
            }
        },
        computed:{
            ...mapGetters(['gettersPackageTimeout']),
        },
        methods:{
            beforeClose(){
                this.$store.dispatch('dialog',{isShow:false,name:'packageTimeout'});
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
        .packageTimeoutTip{
            padding-left: 50px;
            div{
                margin-bottom: 10px;
                font-size: 18px;
            }
           span{
               color: #FF6419;
           }
            p{
                color: #999;
            }
        }
    }

</style>
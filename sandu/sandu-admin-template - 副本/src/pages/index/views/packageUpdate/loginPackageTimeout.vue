<template>
    <el-dialog width="540px"
               top="20%"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :before-close="beforeClose"
               title="提示"
               :visible.sync="gettersLoginPackageTimeout">
        <div class="packageBuy">
            <div class="packageTimeoutTip">
                <div class="loginPackage-text">{{title}}</div>
                <div class="loginPackage-p">{{text}}</div>
                <el-button round type="primary" size="mini" @click="beforeClose">我知道了</el-button>
                <p>三度24小时客服：0755-23895307</p>
            </div>
        </div>
    </el-dialog>
</template>

<script>
    import { mapGetters} from "Vuex"
    export default {
        props: {
            title:{
                type:String,
                default:null
            },
            text:{
                type:String,
                default:null
            }
        },
        computed:{
            ...mapGetters(['gettersLoginPackageTimeout']),
        },
        methods:{
            beforeClose(){
                this.$store.dispatch('dialog',{isShow:false,name:'loginPackageTimeout'});
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
            div{
                margin-bottom: 10px;
                font-size: 18px;
            }
           span{
               color: #FF6419;
           }
            p{
                color: #999;
                text-align: right;
            }
            .el-button{
                width: 120px;
                height: 40px;
                font-size: 14px;
                display: block;
                margin: 20px auto;
            }
            .loginPackage-text{
                text-align: center;
                line-height: 32px;
                margin-bottom: 0;
                font-weight: bold;
            }
            .loginPackage-p{
                text-align: center;
                font-size: 14px;
                margin-bottom: 0;
                color: #999;
            }
        }
    }

</style>
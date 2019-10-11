<template>
    <el-dialog width="540px"
               top="20%"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               title="提示"
               :visible.sync="gettersDialogTimeoutPackage">
        <div class="packageBuy">
            <div class="packageTimeoutTip">
                <div>您的账号已到期，可联系账号提供者进行解决。</div>
                <p>三度客服：0755-23895307</p>
            </div>
        </div>
    </el-dialog>
</template>

<script>
    import { mapGetters } from "Vuex"
    export default {
        computed:{
            ...mapGetters(['gettersDialogTimeoutPackage'])
        },
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
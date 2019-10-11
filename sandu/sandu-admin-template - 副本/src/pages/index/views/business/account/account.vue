<template>
    <div class="account">
        <div class="account-header">
            <div class="header-nav">
                <span :class="isInterior? 'left-nav-active': 'left-nav'"
                      v-if="isPer('company:user:view')"
                      @click="selector('内部账号')">内部账号</span>
                <span :class="!isInterior? 'right-nav-active': 'right-nav'"
                      v-if="isPer('dealer:user:view') && (companyType == 1 || companyType == 2)"
                      @click="selector('经销商账号')">经销商账号</span>
            </div>
        </div>
        <interior-account v-show="isInterior"></interior-account>
        <company-account v-show="!isInterior"></company-account>

        <packagePurchase></packagePurchase>
        <choosePackage></choosePackage>
        <!--套餐升级继费对话框-->
        <dialog-buy></dialog-buy>
        <!--支付二维码-->
        <payCode></payCode>
    </div>
</template>

<script>
    import companyAccount from '../account/account_module/company_account'
    import interiorAccount from '../account/account_module/interior_account'
    import packagePurchase from  '../../packageUpdate/packagePurchase'
    import choosePackage from  '../../packageUpdate/choosePackage'
    import dialogBuy from '../../packageUpdate/dialogBuy'
    import payCode from '../../packageUpdate/payCode'
    import QrcodeVue from 'qrcode.vue'
    import qs from 'qs'
    export default {
        name: "account",
        data() {
            return {
                packagePurchase:false, //购买套餐弹窗
                choosePackage:true,   //选择套餐弹窗
                isInterior: true,
                companyType: sessionStorage.getItem('companyType'),
            };
        },
        created() {
            if (sessionStorage.getItem('isInterior') == '内部账号') {
                this.isInterior = true;
                return;
            }
            if (sessionStorage.getItem('isInterior') == '经销商账号') {
                this.isInterior = false;
                return;
            }
            sessionStorage.setItem('isInterior', '内部账号');
        },
        components: {
            companyAccount,
            interiorAccount,
            packagePurchase,
            choosePackage,
            dialogBuy,
            payCode
        },
        methods: {
            /*权限设置*/
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            selector(type) {
                sessionStorage.setItem('isInterior', type);
                if (type == '内部账号') {
                    this.isInterior = true;
                }
                if (type == '经销商账号') {
                    this.isInterior = false;
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/css/mixin";

    .account{
        .account-header{
            background-color: #ffffff;
            .header-nav{
                padding: 20px;
                font-size: 18px;
                color: #dddddd;
                border-bottom: 1px solid #dddddd;
                display: flex;
                justify-content: left;
                .left-nav{
                    color: #333333;
                    margin-right: 20px;
                    cursor:pointer;
                }
                .left-nav-active{
                    color: #ff6419;
                    margin-right: 20px;
                    cursor:pointer;
                }
                .right-nav{
                    color: #333333;
                    margin-left: 20px;
                    cursor:pointer;
                }
                .right-nav-active{
                    color: #ff6419;
                    margin-left: 20px;
                    cursor:pointer;
                }
            }
        }
    }

</style>
<template>
    <div class="edit-account">
        <v-nav :nav="nav"></v-nav>
        <div class="edit-account-content">
            <h1>{{headerName}}</h1>
            <h2 class="headline2">店铺信息</h2>
            <el-form>
                <div class="datails">
                    <div class="datails-left">
                        <div class="datails-left-item"><p>店铺名称：</p><span>{{datails.shopName}}</span></div>
                        <div class="datails-left-item"><p>联系人：</p><span>{{datails.contactName}}</span></div>
                        <div class="datails-left-item"><p>联系人电话：</p><span>{{datails.contactPhone}}</span></div>
                        <div class="datails-left-item"><p>区域：</p><span>{{datails.longAreaName}}</span></div>
                        <div class="datails-left-item"><p>店铺地址：</p><span>{{datails.shopAddress}}</span></div>
                        <div class="datails-buttom-item">
                            <p>LOGO：</p>
                            <img v-if="datails.logoPicPath" :src="datails.logoPicPath" alt="">
                            <div v-else class="headPicPath">暂无图片</div>
                        </div>
                        <div class="datails-buttom-item">
                            <p>店铺封面：</p>
                            <div v-for="item in datails.coverPicPath">
                                <img v-if="item" :src="item" :alt="item">
                                <div v-else class="headPicPath">暂无图片</div>
                            </div>
                            <div v-if="datails.coverPicPath.length <= 0" class="headPicPath">暂无图片</div>
                        </div>
                        <div class="details-shopIntroduced-item">
                            <span>店铺介绍：</span>
                            <div v-if="datails.shopIntroduced" v-html="datails.shopIntroduced"></div>
                            <div v-else></div>
                        </div>
                    </div>
                </div>
                <div class="footer">
                    <el-form-item>
                        <el-button round class="cancel" @click="cancel">取消</el-button>
                        <el-button round class="preserve" @click="claimShop()" v-if="isPer('shopOffline:dealer:receive')">确认认领</el-button>
                    </el-form-item>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>

    import qs from 'qs';

    export default {
        name: "shop-offline-detail",
        data() {
            return {
                nav: [
                    {nav: "线下门店管理", url: "/business/shopOffline"},
                    {nav: "认领", url: ""}
                ],
                headerName: "认领",
                datails: {
                    // 详情信息
                    id: "",
                    shopName: "",
                    contactName: "",
                    contactPhone: "",
                    longAreaName: "",
                    shopAddress: "",
                    logoPicPath: "",
                    coverPicPath: ""
                }
            };
        },
        created() {
            this.initView(this.$route.params.shopOfflineId);
        },
        methods: {
            isPer(per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            initView(shopOfflineId) {
                this.API.getShopOffline(shopOfflineId).then(res => {
                    this.datails = res.data;
                });
            },
            claimShop() {
                this.$confirm("一个账号只可以认领一个店铺，确定认领该店铺？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.API.claimShop(this.datails.id).then(res => {
                        if (res.success) {
                            this.$message({
                                type: "success",
                                message: res.message
                            });

                            // 返回列表页
                            setTimeout(() => {
                                this.cancel();
                            }, 300);
                        } else {
                            this.$message({
                                type: "error",
                                message: res.message
                            });
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消删除"
                    });
                });
            },
            cancel() {
                this.$router.push("/business/shopOffline");
            }
        }
    };
</script>

<style lang="scss" scoped>
    @import "../../../assets/css/mixin";

    .edit-account-content {
        background-color: #ffffff;
        padding: 40px 20px 20px 20px;

        h1 {
            font-size: 20px;
            height: 35px;
            border-bottom: 1px solid #dddddd;
            margin-bottom: 40px;
        }

        .required-hint {
            width: 100%;
            font-size: 14px;
            color: #666666;

            span {
                color: #ff3838;
            }
        }

        .headline2 {
            height: 40px;
            line-height: 40px;
            padding-left: 10px;
            font-size: 16px;
            margin: 10px 0 30px;
            border-left: 7px solid #ff6419;
            background-color: #f6f6f6;
        }

        .footer {
            width: 100%;
            border-top: 1px solid #dddddd;
            line-height: 80px;

            .el-form-item {
                width: 100%;
                margin-bottom: 0;
                margin-top: 20px;
                margin-left: 50px;

                .el-button {
                    width: 130px;
                    height: 40px;
                    border: none;
                    color: #fff;
                    font-size: 16px;
                    font-weight: bold;
                }

                .preserve {
                    background-color: #ff6419;
                }

                .cancel {
                    background-color: #454545;
                }
            }
        }

        .datails {
            display: flex;
            justify-content: left;
            margin-bottom: 30px;

            .datails-left {
                width: 80%;

                .datails-left-item {
                    height: 36px;
                    line-height: 36px;

                    p {
                        display: inline-block;
                        width: 110px;
                        text-align: right;
                        margin-right: 10px;
                        font-family: MicrosoftYaHei;
                        font-size: 14px;
                        font-weight: normal;
                        font-stretch: normal;
                        letter-spacing: 0px;
                        color: #666666;
                    }

                    span {
                        font-family: MicrosoftYaHei;
                        font-size: 14px;
                        font-weight: normal;
                        font-stretch: normal;
                        letter-spacing: 0px;
                        color: #333333;
                    }

                    .shop-item {
                        word-wrap: break-word;
                        word-break: break-all;
                    }
                }

                .datails-buttom-item {
                    display: flex;
                    justify-content: left;
                    line-height: 36px;
                    margin: 10px 0px;

                    p {
                        width: 110px;
                        text-align: right;
                        margin-right: 10px;
                        font-family: MicrosoftYaHei;
                        display: inline-block;
                        font-size: 14px;
                        font-weight: normal;
                        font-stretch: normal;
                        letter-spacing: 0px;
                        color: #666666;
                    }

                    img {
                        width: 90px;
                        height: 90px;
                        margin-right: 10px;
                    }
                }
                .details-shopIntroduced-item {
                    /*display: flex;*/
                    justify-content: left;
                    span {
                        width: 110px;
                        text-align: right;
                        margin-left: 40px;
                        font-size: 14px;
                        font-weight: normal;
                        font-stretch: normal;
                        letter-spacing: 0;
                        color: #666666;
                        vertical-align: top;
                    }

                    div {
                        width: 750px;
                        margin-left: 6px;
                        display: inline-block;
                    }
                }
            }
        }

        .headPicPath {
            width: 90px;
            height: 90px;
            line-height: 90px;
            text-align: center;
            background-color: #f6f6f6;
            margin-right: 20px;
        }
    }
</style>
<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <div class="planLibrary tabClass">
            <el-tabs v-model="activeName" @tab-click="changeTabs">
                <el-tab-pane label="智能方案" name="first">
                    <intelligent-plan v-if="!isShopPlanType" @getShopPlanType="getShopPlanType"></intelligent-plan>
                    <shop-fullhouse-plan v-else @getShopPlanType="getShopPlanType"></shop-fullhouse-plan>
                </el-tab-pane>
                <el-tab-pane label="普通方案" name="second">
                    <common-design-plan></common-design-plan>
                </el-tab-pane>
            </el-tabs>
        </div>

    </div>
</template>
<script> 
    import intelligentPlan from './oneKeyPlanList'
    import CommonDesignPlan from './common/CommonDesignPlan'
    import shopFullhousePlan from './shopPlancomponents/fullhouseShopPlan'
    import qs from 'qs'
    const shelfData = [
        {
            id: 'ONEKEY',
            name: '一键方案'
        },
        {
            id: 'OPEN',
            name: '公开方案'
        }
    ];

    export default {
        components: {
            intelligentPlan,
            CommonDesignPlan,
            shopFullhousePlan
        },
        data () {
            return {
                isShopPlanType:false,
                nav: [
                    { nav: "店铺管理", url: "/business/shop" },
                    { nav: "方案管理", url: "/business/shop" }
                ],
                shelfDialogVisible: false,
                shelf: {
                    shelfStatus: []
                },
                activeName: 'first',
                dialogOnekeySecercy: false,
                confirmShelfMsg: '确定要上架么?',
                shelfData: shelfData
            }
        },
        methods: {
            getShopPlanType(isShow) {
              console.log(isShow);
            this.isShopPlanType = isShow;
           },
            changeTabs(val) {
                sessionStorage.setItem('getTabName', val.label)
            },
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            refreshData () {
                this.$nextTick(function () {
                    this.channelPlanOneKeyPlan = true;
                    this.page()
                })
            },
            resetInput () {
                this.query = {
                    page: 0,
                    limit: 10,
                    companyId: sessionStorage.getItem('companyID')
                }
                this.page();
            },
            showSecrecyList (row) {
                if (row) {
                    this.dialogOnekeySecercy = true
                    this.productPlanId = row.planId
                    this.ProductSecrecyList()
                }
            },
            ProductSecrecyList () {
                this.API.ListOneKeyPlanProducts({
                    planId: this.productPlanId ,
                    secrecy: 0,
                    page: this.pages,
                    limit: this.limits
                }).then((res) => {
                    this.onekeySecrecyLoading = false
                    this.onekeySecrecy = res.data
                    this.secrecyTotal = res.total
                })
            },
            page () {
                this.API.ChannelOneKeyDesignPlanList(this.query).then((resp) => {
                    if (resp.code == 204 || resp.code >=500) {
                        this.channelPlanOneKeyPlan = false;
                    }
                    if (resp.data) {
                        this.channelPlanOneKeyPlan = false;
                        this.list = resp.data.list.map(it => {
                            if (it.shelfStatus) {
                                return Object.assign({}, it, {shelfStatus: it.shelfStatus.split(',')})
                            }
                            return it
                        })
                        this.total = resp.data.total
                    } else {
                        this.channelPlanOneKeyPlan = false;
                        this.list = []
                        this.total = 0
                    }
                })
            },
            handleSelectionChange (sels) {
                this.sels = sels
            },
            handleDetail (index, row) {
                localStorage.setItem('breadcrumb3', '方案详情');
                this.$store.commit('breadcrumb3', '方案详情')
                this.$router.push(`/shopIntelligent/${row.planId}`)
            },
            handleShelf (index, row) { // 方案上架 打开弹窗
                this.shelf.planId = row.planId
                this.shelfDialogVisible = true
                this.shelf.shelfStatus = row.shelfStatus ? row.shelfStatus : []
                if (row.containsSecrecyFlag === '0') {
                    this.confirmShelfMsg = '选定的方案包含未公开产品,确定要上架么?'
                }
                if(row.origin === 'share') {
                    this.shelfData = [
                        {
                            id: 'OPEN',
                            name: '公开方案'
                        }
                    ]
                } else{
                    this.shelfData = shelfData
                }
                this.batch = false
            },
            handleShelfConfirm () {
                this.$confirm(this.confirmShelfMsg, '提示', {
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    center: true,
                    type: 'warning',
                }).then(() => {
                    if (this.batch) {
                        const params = this.sels.map(sel => {
                            return {
                                planId: sel.planId,
                                shelfStatus: this.shelf.shelfStatus
                            }
                        })
                        this.API.ChannelOneKeyPlanBatchPushaway(params).then(resp => {
                            this.page()
                            this.shelfDialogVisible = false
                            this.$message({
                                message: '上架成功',
                                type: 'success'
                            })
                        })
                    } else {
                        this.API.ChannelOneKeyPlanPushaway(this.shelf).then(resp => {
                            this.page()
                            this.shelfDialogVisible = false
                            this.$message({
                                message: '上架成功',
                                type: 'success'
                            })
                        })
                    }

                })
            },
            // 未公开产品分页模块
            secrecySizeChange: function (size) {
                this.limits = size
                this.ProductSecrecyList()
            },
            secrecyCurrentChange: function (currentPage) {
                this.pages = currentPage
                this.ProductSecrecyList()
            },
            // 分页事件
            handleSizeChange: function (size) {
                this.query.limit = size
                this.page()
            },
            handleCurrentChange: function (currentPage) {
                this.channelPlanOneKeyPlan = true;
                this.query.page = currentPage
                this.page()
            },
            handleBatchPushaway () {
                if (this.sels.length <= 0) {
                    this.$message.error('请选择一条记录')
                    return
                }
                this.batch = true
                this.shelfDialogVisible = true
            }
            /*
              getPlatformList () {
                 this.API.PlatFormList({platformType: '2b'}).then(resp => {
                   this.platforms = resp.data
                 })
               }
              */
        },
        created () {
            this.page();
            sessionStorage.setItem('getTabName','智能方案');
        }
    }
</script>
<style lang="scss" scoped>
    @import "../../../assets/css/mixin";

    .planLibrary {
        box-sizing: padding-box;
        background: #fff;
        border-radius: $borderRadius;
    }
    .tabClass {
        margin-bottom: 20px;
    }
</style>
<style>
    .el-table th.hedStyle{
        height: 5px !important;
    }
    .el-tabs__item{
        height: 50px;
        line-height: 50px;
    }
    .el-tabs__nav {
        margin-left: 30px;
    }
</style>

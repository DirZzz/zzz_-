<template>
<div>
    <div  v-if="packageEditFromType==1">
    <el-dialog :visible.sync="packageEditDialog" width="620px" class="packageEdit"
        :title="packageEditFrom==1?'设置套餐':'套餐修改'" :append-to-body='true' :close-on-click-modal="false"
        :close-on-press-escape="false" :before-close="handleClose">
        <div class="packageEdit-content">
            <div class="package-title" v-if="packageEditFrom!=1">当前套餐 : {{packageData.servicesName || '无'}}</div>
            <div class="search-package">
                <span>搜索套餐 :</span>
                <el-input v-model="input" placeholder="请输入内容" style="width: 288px;margin-right: 16px"></el-input>
                <el-button type="primary" size="medium" @click="filterList()">查询</el-button>
            </div>
            <div class="packageList">
                <div class="package-item" v-for="(it,index) in serviceInfoList" :key="index">
                    <el-radio v-model="it.choosed" :label="it.servicesId" @change="changeFunc(index)">
                        {{it.servicesName}}</el-radio>
                    <div class="package-age">
                        <span :class="{'active':price.active}" v-for="(price, idx) in it.priceList" :key="idx"
                            @click="choosePrice(it.servicesId,price.id,index)">{{price.duration}}{{price.priceUnit==0 ? '年' : price.priceUnit==1 ? '月' : '日'}}</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn">
            <el-button size="medium" @click="cancel">取消</el-button>
            <el-button type="primary" size="medium" @click="saveService" :disabled="disabled" v-if="isNewAdd==4">保存
            </el-button>
            <el-button type="primary" size="medium" @click="confirm" :disabled="disabled" v-else>确定</el-button>
        </div>
    </el-dialog>
    </div>
    <div v-else-if="packageEditFromType==2">
    <el-dialog :visible.sync="packageEditDialog"  width="620px" class="packageEdit"
        :title="packageEditFrom==1?'设置套餐':'套餐修改'" :append-to-body='true' :close-on-click-modal="false"
        :close-on-press-escape="false" :before-close="handleClose">
        <div class="packageEdit-content">
            <div class="package-title" v-if="packageEditFrom!=1">当前套餐 : {{packageData.servicesName || '无'}}</div>
            <div class="search-package">
                <span>搜索套餐 :</span>
                <el-input v-model="input" placeholder="请输入内容" style="width: 288px;margin-right: 16px"></el-input>
                <el-button type="primary" size="medium" @click="filterList()">查询</el-button>
            </div>
            <div class="packageList">
                <div class="package-item" v-for="(it,index) in serviceInfoList" :key="index">
                    <el-radio v-model="it.choosed" :label="it.servicesId" @change="changeFunc(index)">
                        {{it.servicesName}}</el-radio>
                    <div class="package-age">
                        <span :class="{'active':price.active}" v-for="(price, idx) in it.priceList" :key="idx"
                            @click="choosePrice(it.servicesId,price.id,index)">{{price.duration}}{{price.priceUnit==0 ? '年' : price.priceUnit==1 ? '月' : '日'}}</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn">
            <el-button size="medium" @click="cancel">取消</el-button>
            <el-button type="primary" size="medium" @click="saveService" :disabled="disabled" v-if="isNewAdd==4">保存
            </el-button>
            <el-button type="primary" size="medium" @click="confirm" :disabled="disabled" v-else>确定</el-button>
        </div>
    </el-dialog>
    </div>
    <div v-else-if="packageEditFromType==3">
    <el-dialog :visible.sync="packageEditDialog"  width="620px" class="packageEdit"
        :title="packageEditFrom==1?'设置套餐':'套餐修改'" :append-to-body='true' :close-on-click-modal="false"
        :close-on-press-escape="false" :before-close="handleClose">
        <div class="packageEdit-content">
            <div class="package-title" v-if="packageEditFrom!=1">当前套餐 : {{packageData.servicesName || '无'}}</div>
            <div class="search-package">
                <span>搜索套餐 :</span>
                <el-input v-model="input" placeholder="请输入内容" style="width: 288px;margin-right: 16px"></el-input>
                <el-button type="primary" size="medium" @click="filterList()">查询</el-button>
            </div>
            <div class="packageList">
                <div class="package-item" v-for="(it,index) in serviceInfoList" :key="index">
                    <el-radio v-model="it.choosed" :label="it.servicesId" @change="changeFunc(index)">
                        {{it.servicesName}}</el-radio>
                    <div class="package-age">
                        <span :class="{'active':price.active}" v-for="(price, idx) in it.priceList" :key="idx"
                            @click="choosePrice(it.servicesId,price.id,index)">{{price.duration}}{{price.priceUnit==0 ? '年' : price.priceUnit==1 ? '月' : '日'}}</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn">
            <el-button size="medium" @click="cancel">取消</el-button>
            <el-button type="primary" size="medium" @click="saveService" :disabled="disabled" v-if="isNewAdd==4">保存
            </el-button>
            <el-button type="primary" size="medium" @click="confirm" :disabled="disabled" v-else>确定</el-button>
        </div>
    </el-dialog>
    </div>
    <div  v-else-if="packageEditFromType==4">
    <el-dialog :visible.sync="packageEditDialog" width="620px" class="packageEdit"
        :title="packageEditFrom==1?'设置套餐':'套餐修改'" :append-to-body='true' :close-on-click-modal="false"
        :close-on-press-escape="false" :before-close="handleClose">
        <div class="packageEdit-content">
            <div class="package-title" v-if="packageEditFrom!=1">当前套餐 : {{packageData.servicesName || '无'}}</div>
            <div class="search-package">
                <span>搜索套餐 :</span>
                <el-input v-model="input" placeholder="请输入内容" style="width: 288px;margin-right: 16px"></el-input>
                <el-button type="primary" size="medium" @click="filterList()">查询</el-button>
            </div>
            <div class="packageList">
                <div class="package-item" v-for="(it,index) in serviceInfoList" :key="index">
                    <el-radio v-model="it.choosed" :label="it.servicesId" @change="changeFunc(index)">
                        {{it.servicesName}}</el-radio>
                    <div class="package-age">
                        <span :class="{'active':price.active}" v-for="(price, idx) in it.priceList" :key="idx"
                            @click="choosePrice(it.servicesId,price.id,index)">{{price.duration}}{{price.priceUnit==0 ? '年' : price.priceUnit==1 ? '月' : '日'}}</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn">
            <el-button size="medium" @click="cancel">取消</el-button>
            <el-button type="primary" size="medium" @click="saveService" :disabled="disabled" v-if="isNewAdd==4">保存
            </el-button>
            <el-button type="primary" size="medium" @click="confirm" :disabled="disabled" v-else>确定</el-button>
        </div>
    </el-dialog>
    </div>
    <div  v-else>
    <el-dialog :visible.sync="packageEditDialog" width="620px" class="packageEdit"
        :title="packageEditFrom==1?'设置套餐':'套餐修改'" :append-to-body='true' :close-on-click-modal="false"
        :close-on-press-escape="false" :before-close="handleClose">
        <div class="packageEdit-content">
            <div class="package-title" v-if="packageEditFrom!=1">当前套餐 : {{packageData.servicesName || '无'}}</div>
            <div class="search-package">
                <span>搜索套餐 :</span>
                <el-input v-model="input" placeholder="请输入内容" style="width: 288px;margin-right: 16px"></el-input>
                <el-button type="primary" size="medium" @click="filterList()">查询</el-button>
            </div>
            <div class="packageList">
                <div class="package-item" v-for="(it,index) in serviceInfoList" :key="index">
                    <el-radio v-model="it.choosed" :label="it.servicesId" @change="changeFunc(index)">
                        {{it.servicesName}}</el-radio>
                    <div class="package-age">
                        <span :class="{'active':price.active}" v-for="(price, idx) in it.priceList" :key="idx"
                            @click="choosePrice(it.servicesId,price.id,index)">{{price.duration}}{{price.priceUnit==0 ? '年' : price.priceUnit==1 ? '月' : '日'}}</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn">
            <el-button size="medium" @click="cancel">取消</el-button>
            <el-button type="primary" size="medium" @click="saveService" :disabled="disabled" v-if="isNewAdd==4">保存
            </el-button>
            <el-button type="primary" size="medium" @click="confirm" :disabled="disabled" v-else>确定</el-button>
        </div>
    </el-dialog>
    </div>
</div>
</template>

<script>
    import minixs from '@s/minixs/index'
    export default {
        name: "packageEdit",
        mixins: [minixs],
        inject: ['reload'],
        data() {
            return {
                input: '',
                radio: '',
                serviceInfoListBak: [],
                servicesId: '',
                priceId: '',
                companyId: '',
                userId: '',
            }
        },
        computed: {
            disabled() {
                if (this.serviceInfoList.length > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        },
        methods: {
            cancel() {
                this.setDialog({
                    packageEditDialog: false
                });
                this.input = '';
                if (this.isNewAdd == 1) {
                    setTimeout(() => {
                        this.setDialog({
                            userMangeAdd: true,
                            isNewAdd: null
                        });
                    }, 300)
                } else if (this.isNewAdd == 2) {
                    setTimeout(() => {
                        this.setDialog({
                            userManageBatchAdd: true,
                            isNewAdd: null
                        });
                    }, 300)
                }
            },
            handleClose() {
                this.setDialog({
                    packageEditDialog: false
                });
                this.input = '';
                if (this.isNewAdd == 1) {
                    setTimeout(() => {
                        this.setDialog({
                            userMangeAdd: true,
                            isNewAdd: null
                        });
                    }, 300)
                } else if (this.isNewAdd == 2) {
                    setTimeout(() => {
                        this.setDialog({
                            userManageBatchAdd: true,
                            isNewAdd: null
                        });
                    }, 300)
                }
            },
            saveService() {
                this.serviceInfoList.forEach(it => {
                    it.choosed ? (this.servicesId = it.choosed) : '';
                    it.priceList.forEach((price) => {
                        price.active ? this.priceId = price.id : '';
                    })
                });
                if (!this.servicesId || !this.priceId) {
                    this.$message.error('请选择套餐');
                    return;
                }
                this.API.confirmBuyTest({
                    serviceId: parseInt(this.servicesId),
                    priceId: this.priceId,
                    payType: 2,
                    companyId: this.packageData.companyId || this.$route.query.id,
                    userId: sessionStorage.getItem('userId'),
                    purchaseSource: 2, //2-三度后台代购
                    businessType: 4, //升级
                    platformCode: 'merchantManage',
                    accountUserId: this.packageData.id,
                    purchaseAmount: 1,
                    innerFlag: true
                }).then(res => {
                    if (res.success) {
                        this.$message.success('恭喜你，购买成功');
                        this.setDialog({
                            packageEditDialog: false
                        });
                        this.reload();
                        this.$emit('refreshList');
                    } else {
                        this.$message.error(res.message);
                    }
                })
            },
            filterList() {

                if (this.serviceInfoList) {
                    if (this.input) {
                        this.serviceInfoListBak = this.serviceInfoList;
                        this.setServicelist({
                            res: this.serviceInfoList.filter(it => it.servicesName.indexOf(this.input) != -1),
                            reset: true
                        })
                    } else {
                        this.setServicelist({
                            res: this.serviceInfoListBak,
                            reset: true
                        })
                        return;
                    }
                }
            },
            changeFunc(i) {
                this.serviceInfoList.forEach(it => {
                    it.choosed = '';
                    it.priceList.forEach((price, index) => {
                        price.active = false;
                        this.$set(it.priceList, index, price)
                    })
                })
                this.serviceInfoList[i].choosed = this.serviceInfoList[i].servicesId;
                this.serviceInfoList[i].priceList[0].active = true;
                this.servicesId = this.serviceInfoList[i].servicesId
            },
            choosePrice(servicesId, priceId, i) {
                this.serviceInfoList.forEach(it => {
                    it.choosed = '';
                    it.priceList.forEach((price, index) => {
                        if (price.id === priceId && it.servicesId === servicesId) {
                            price.active = true;
                            this.$set(it.priceList, index, price)
                            this.servicesId = it.servicesId;
                        } else {
                            price.active = false;
                        }
                    })
                })
                this.serviceInfoList[i].choosed = this.serviceInfoList[i].servicesId;
            },
            confirm() {
                console.log('使用')
                this.serviceInfoList.forEach(it => {
                    it.choosed ? (this.servicesId = it.choosed) : '';
                });

                if (!this.servicesId) {
                    this.$message.error('请选择套餐');
                } else {
                    console.log(this.packageEditFrom)
                    if (this.packageEditFrom == 1) {
                        this.$emit('goUser', 1);
                        this.$emit('mealSure', this.servicesId)
                        this.setDialog({
                            packageEditDialog: false
                        });
                        console.log('執行到此')
                        return;
                    } else {
                        this.serviceInfoList.forEach(it => {
                            it.priceList.forEach((price) => {
                                price.active ? this.priceId = price.id : '';
                            })
                        })
                        this.setServiceInfo({
                            servicesId: this.servicesId,
                            priceId: this.priceId,
                            servicesName: this.serviceInfoList.filter(res => {
                                return res.servicesId == this.servicesId
                            })[0].servicesName
                        });
                        this.setDialog({
                            packageEditDialog: false
                        });
                        if (this.isNewAdd == 1) {
                            setTimeout(() => {
                                this.setDialog({
                                    userMangeAdd: true,
                                    isNewAdd: null
                                });
                            }, 300)
                        } else if (this.isNewAdd == 2) {
                            setTimeout(() => {
                                this.setDialog({
                                    userManageBatchAdd: true,
                                    isNewAdd: null
                                });
                            }, 300)
                        }
                    }
                }
            }
        },

    }
</script>

<style scoped lang="scss">
    .packageEdit {
        .packageEdit-content {
            width: 468px;
            margin: 0 auto;
            text-align: left;

            .package-title {
                font-size: 16px;
                color: #333;
                line-height: 40px;
            }

            .search-package {
                margin-top: 16px;

                span {
                    font-size: 16px;
                    color: #333;
                }
            }

            .packageList {
                width: 378px;
                height: 300px;
                background-color: #ffffff;
                border-radius: 4px;
                border: solid 1px #d9d9d9;
                overflow-y: scroll;
                margin: 20px 0 0 77px;
                padding: 20px;
                box-sizing: border-box;

                .package-item {
                    margin-bottom: 14px;

                    .package-age {
                        margin-left: 22px;
                        margin-top: 12px;

                        span {
                            display: inline-block;
                            width: 48px;
                            height: 24px;
                            background-color: #ffffff;
                            border-radius: 2px;
                            text-align: center;
                            line-height: 24px;
                            color: #666666;
                            margin-right: 16px;
                            cursor: pointer;
                        }

                        .active {
                            border: solid 1px #3399ff;
                            color: #3399ff;
                        }
                    }
                }
            }
        }

        .btn {
            text-align: right;
            margin-top: 50px;
        }
    }
</style>
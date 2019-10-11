<template>
    <div class="answerconfig">
        <headers :title="title"></headers>
        <div class="main">
            <div class="item">
                <span class="item_tit">调查时间：</span>
                <el-date-picker v-model="curdata.date" type="datetimerange"
                    :start-placeholder="curdata.startPlaceholder || '开始日期'"
                    :end-placeholder="curdata.endPlaceholder || '结束日期'" :value-format="curdata.format || 'yyyy-MM-dd'"
                    @change="getDate(curdata.date)">
                </el-date-picker>
            </div>
            <div class="item">
                <span class="item_tit">红包总金额：</span>
                <el-input style="width:100px;" v-model="mark.totalNum" type="number" placeholder="请输入内容"></el-input>
            </div>
            <div class="item">
                <span class="item_tit">每天发送红包个数：</span>
                <el-input style="width:100px;" v-model="mark.dayNum" type="number" placeholder="请输入内容"></el-input>
            </div>
            <div class="item">
                <span class="item_tit">发送红包金额：</span>
                <el-input style="width:150px;" v-model="mark.minAmount" type="number" placeholder="红包最小总金额"></el-input>~
                <el-input style="width:150px;" v-model="mark.maxAmount" type="number" placeholder="红包最大总金额"></el-input>
            </div>

            <div class="butbox">
                <el-button type="primary" style="width:100px" @click="save">确定</el-button>
                <el-button style="width:100px">取消</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import headers from "./../../content/headers";
    export default {
        components: {
            headers
        },
        data() {
            return {
                title: '红包金额配置',
                mark: {
                    beginTime: '',
                    endTime: '',
                    totalNum: '', // 红包总金额
                    dayNum: '', // 每日红包总个数
                    maxAmount: '', // 红包最大总金额
                    minAmount: '' // 红包最小总金额
                },
                curdata: {
                    date: '',
                    format: ''
                },
                curId: null
            }
        },
        created() {
            this.getData();
        },
        methods: {
            getData() {
                this.API.getRedPackageDetailInfo().then(res => {
                    if (res.success) {
                        this.mark.beginTime = res.obj.beginTime.substr(0, 10);
                        this.mark.endTime = res.obj.endTime.substr(0, 10);
                        this.mark.totalNum = res.obj.totalNum;
                        this.mark.dayNum = res.obj.dayNum;
                        this.mark.maxAmount = res.obj.maxAmount;
                        this.mark.minAmount = res.obj.minAmount;
                        this.curdata.startPlaceholder = res.obj.beginTime.substr(0, 10);
                        this.curdata.endPlaceholder = res.obj.endTime.substr(0, 10);
                        this.curId = res.obj.id || null;
                    }
                })
            },
            save() {
                this.mark.dayNum = parseInt(this.mark.dayNum);
                this.mark.totalNum = parseInt(this.mark.totalNum);
                this.mark.maxAmount = parseFloat((this.mark.maxAmount / 1).toFixed(2));
                this.mark.minAmount = parseFloat((this.mark.minAmount / 1).toFixed(2));
                this.mark.id = this.curId;
                if (this.mark.maxAmount<=10 && this.mark.minAmount>=0.3) {
                    this.API.questionnairsave(this.mark).then(res => {
                        if (res.success) {
                            this.$message({
                                showClose: true,
                                message: '红包配置成功！',
                                type: 'success'
                            });
                        } else {
                            this.$message({
                                message: res.message,
                                type: 'warning'
                            });
                        }
                    })
                } else {
                    this.$message({
                        message: '发送红包金额最低0.3元，最高10元！',
                        type: 'warning'
                    });
                }
            },
            getDate(val) {
                if (val) {
                    this.mark.beginTime = val[0].substr(0, 10);
                    this.mark.endTime = val[1].substr(0, 10);
                } else {
                    this.mark.beginTime = '';
                    this.mark.endTime = '';
                    this.curdata.startPlaceholder = '';
                    this.curdata.endPlaceholder ='';
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .answerconfig {
        .main {
            margin: 20px;
            padding: 20px 25px;
            background-color: #fff;

            .item {
                text-align: left;
                margin-bottom: 20px;

                .item_tit {
                    display: inline-block;
                    width: 150px;
                    text-align: left;
                }
            }
        }
    }
</style>
<template>
    <div class="answerinfo" @scroll="handScroll" ref='box'>
        <headers></headers>
        <div class="nav">
            <el-date-picker v-model="curdate" type="datetimerange" start-placeholder="开始日期" end-placeholder="结束日期"
                value-format='yyyy-MM-dd' :default-time="['00:00:00']" @change="getDate(curdate)"></el-date-picker>
            <div class="nav_but">
                <el-button @click="getDataList" size="small" type="primary">查询</el-button>
                <el-button @click="reset" size="small">重置</el-button>
            </div>
        </div>
        <div class="main">
            <div class="list_item" v-for="(item, index) in datalist.infors" :key='index'>
                <div class="item_title">{{item.askItem}}</div>
                <el-table border :data="item.answers" style="width: 100%">
                    <el-table-column prop="answerItem" label="选项" width="180">
                    </el-table-column>
                    <el-table-column prop="usedNum" label="小计" width="80">
                    </el-table-column>
                    <el-table-column prop="rate" label="比例">
                    </el-table-column>
                </el-table>
                <div class="tab_sum">
                    <span class="tabsum_tit">本题有效答题人数</span>
                    <span class="tabsum_value">{{item.usedNum}}</span>
                </div>
            </div>
            <div class="answerTwo">
                <div class="answerTwo_title">{{datalist.contentItem || ''}}</div>
                <el-button type="primary" @click="showAnswer">查看详细信息({{datalist.count || 0}})
                </el-button>
            </div>
        </div>
        <el-dialog :title="curAnswerTitle" :visible.sync="dialogTableVisible">
            <el-table border :data="gridData">
                <el-table-column type="index" label="序号" width="50"></el-table-column>
                <el-table-column property="gmtCreate" label="提交时间" width="200"></el-table-column>
                <el-table-column property="answerContent" label="答案" width="450"></el-table-column>
                <el-table-column fixed="right" align="center" label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button @click="handleClick(scope.row)" size="small" type="primary">查看答卷</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin-top:20px;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="pageData.page" :page-sizes="[5, 10, 20, 30]"
                :page-size="pageData.limit" layout="total, sizes, prev, pager, next, jumper"
                :total="pageData.totalCount">
            </el-pagination>
        </el-dialog>
        <checkanswer v-if="checkanswerShow" @closeCheckAnaser="closeCheckAnaser" :curitem="curitem"></checkanswer>
        <backTop v-if="backTopShow" @backTop="backTop"></backTop>
    </div>
</template>

<script>
    import headers from "./../../content/headers";
    import backTop from "./../backTop/index";
    import checkanswer from "./../checkanswer/index";
    export default {
        components: {
            headers,
            checkanswer,
            backTop
        },
        data() {
            return {
                dialogTableVisible: false,
                checkanswerShow: false,
                gridData: [],
                curitem: {},
                mark: {
                    begin: '',
                    end: ''
                },
                markanswer: {
                    begin: '',
                    end: '',
                    start: 1,
                    limit: 10
                },
                curdate: [],
                pageData: {
                    page: 1,
                    limit: 10,
                    totalCount: 0
                },
                datalist: {},

                curAnswerTitle: '',

                backTopShow: false
            }
        },
        activated() {
            this.checkanswerShow = false;
        },
        created() {
            this.getDataList()
        },
        methods: {
            backTop() {
                this.$refs.box.scrollTop = 0;
                this.backTopShow = false;
            },
            handScroll() {
                if (this.$refs.box.scrollTop >= 500) {
                    this.backTopShow = true;
                }
            },
            reset() {
                this.mark.begin = '';
                this.mark.end = '';
                this.markanswer.begin = '';
                this.markanswer.end = '';
                this.curdate = [];
                this.getDataList();
            },
            closeCheckAnaser() {
                this.checkanswerShow = false;
            },
            handleClick(item) {
                this.dialogTableVisible = false;
                this.checkanswerShow = true;
                this.curitem = item;
            },
            getDataList() {
                this.API.questionnaireInfo(this.mark).then(res => { // 答卷信息列表
                    if (res.success && res.obj) {
                        this.datalist = res.obj;
                        this.datalist.infors.map(item => {
                            item.answers.map(sitem => {
                                sitem.rate = (sitem.rate / 1).toFixed(2) + '%';
                            })
                        })
                    } else {
                        this.datalist = {};
                    }
                })
            },
            getanserList() {
                this.API.contentlist(this.markanswer).then(res => { // 问答题答案列表
                    if (res.success) {
                        this.gridData = res.datalist;
                        this.pageData.totalCount = res.totalCount;
                    }
                })
            },
            getDate(date) {
                if (date) {
                    this.mark.begin = date[0].substr(0, 10);
                    this.mark.end = date[1].substr(0, 10);
                    this.markanswer.begin = date[0].substr(0, 10);
                    this.markanswer.end = date[1].substr(0, 10);
                } else {
                    this.mark.begin = '';
                    this.mark.end = '';
                    this.markanswer.begin = '';
                    this.markanswer.end = '';
                }
            },
            showAnswer() {
                this.dialogTableVisible = true;
                this.curAnswerTitle = this.datalist.contentItem;
                this.getanserList();
            },
            handleSizeChange(val) {
                this.markanswer.limit = val;
                this.getanserList();
            },
            handleCurrentChange(val) {
                this.markanswer.start = val;
                this.getanserList();
            }
        }
    }
</script>

<style lang="scss" scoped>
    .answerinfo {
        width: 100%;
        height: 100%;
        overflow-y: scroll;

        .nav {
            background: #fff;
            text-align: left;
            margin: 20px;
            padding: 20px 25px;
            margin-top: -20px;

            .nav_but {
                margin-top: 15px;
                display: flex;
                justify-content: center;
            }
        }

        .main {
            margin: 20px;
            padding: 20px 25px;
            background-color: #fff;

            .list_item {
                width: 500px;
                margin-top: 20px;

                .item_title {
                    text-align: left;
                    margin-bottom: 15px;
                }

                .tab_sum {
                    width: 500px;
                    border: solid 1px #EBEEF5;
                    border-top: none;
                    text-align: left;
                    font-size: 0px;

                    .tabsum_tit,
                    .tabsum_value {
                        display: inline-block;
                        vertical-align: middle;
                        margin: 0;
                        padding: 0;
                        width: 180px;
                        height: 50px;
                        line-height: 50px;
                        text-align: left;
                        padding-left: 10px;
                        background-color: #f5f7fa;
                        font-size: 14px;
                        color: #606266;
                    }

                    .tabsum_value {
                        width: 80px;
                        background: #fff;
                        border-left: 1px solid #EBEEF5;
                    }
                }
            }

            .answerTwo {
                text-align: left;
                margin-top: 20px;

                .answerTwo_title {
                    text-align: left;
                    margin-bottom: 15px;
                }
            }
        }
    }
</style>
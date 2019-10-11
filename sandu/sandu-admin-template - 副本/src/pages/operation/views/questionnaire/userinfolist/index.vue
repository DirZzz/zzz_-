<template>
    <div class="userinfolist">
        <headers></headers>
        <div class="nav">
            <el-date-picker v-model="curdate" 
                type="datetimerange"
                start-placeholder="开始日期"
                end-placeholder="结束日期" 
                value-format='yyyy-MM-dd'
                :default-time="['00:00:00']"
                @change="getDate(curdate)"></el-date-picker>
            <div class="nav_but">
                <el-button @click="getDataList" size="small" type="primary">查询</el-button>
                <el-button @click="reset" size="small">重置</el-button>
            </div>
        </div>
        <div class="main">
            <div class="output">
                <el-button type="primary" size="medium" @click="exportExcel">导出数据</el-button>
            </div>
            <el-table border :data="gridData">
                <el-table-column property="userId" label="编号" width="200"></el-table-column>
                <el-table-column property="creator" label="用户昵称" width="300"></el-table-column>
                <el-table-column property="mobile" label="用户手机号" width="300"></el-table-column>
                <el-table-column property="redPacketAmount" label="领取红包金额"></el-table-column>
                <el-table-column property="gmtCreate" label="回答时间"></el-table-column>
                <el-table-column fixed="right" align="center" label="操作" width="300">
                    <template slot-scope="scope">
                        <el-button @click="handleClick(scope.row)" size="small" type="primary">查看答卷</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="block">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="pageData.page" :page-sizes="[5, 10, 20, 30]" :page-size="pageData.limit"
                    layout="total, sizes, prev, pager, next, jumper" :total="totalCount">
                </el-pagination>
            </div>
        </div>
        <checkanswer v-if="checkanswerShow" @closeCheckAnaser="closeCheckAnaser" :curitem="curitem"></checkanswer>
    </div>
</template>

<script>
    import headers from "./../../content/headers";
    import searchBar from "./../../userTagsMessage/searchBar";
    import checkanswer from "./../checkanswer/index";
    export default {
        components: {
            headers,
            checkanswer
        },
        data() {
            return {
                curdate: [],

                checkanswerShow: false,
                searchs: [{
                    fields: ["startTime", "endTime"],
                    placeholder: "输入发布时间",
                    type: "datetimerange"
                }],
                pageData: {
                    page: 1,
                    limit: 10,
                    blockTypeValueKey: "topic"
                },
                gridData: [],
                mark: {
                    begin: '',
                    end: '',
                    start: 1,
                    limit: 10
                },
                totalCount: 0,
                curitem: 0
            }
        },
        activated() {
            this.checkanswerShow = false;
        },
        created() {
            this.getDataList();
        },
        methods: {
            exportExcel() {
                this.API.getexportExcel(this.mark).then(result => {
                    if (result && result.size > 0) {
                        let url = window.URL.createObjectURL(result);
                        let link = document.createElement("a");
                        link.style.display = "none";
                        link.href = url;
                        link.setAttribute("download", "问卷用户信息列表导出.xls");
                        document.body.appendChild(link);
                        link.click();
                    } else {
                        this.$message.error("没有数据需要导出!");
                    }
                })
            },
            reset() {
                this.mark.begin ='';
                this.mark.end = '';
                this.curdate = [];
                this.getDataList();
            },
            handleClick(item) {
                this.checkanswerShow = true;
                this.curitem = item;
            },
            closeCheckAnaser() {
                this.checkanswerShow = false;
            },
            getDataList() {
                this.API.getansweruserlist(this.mark).then(res => {
                    if (res.success&&res.datalist) {
                        this.gridData = res.datalist;
                        this.gridData.map(item => {
                            item.mobile = item.mobile ? item.mobile : '--';
                            item.creator = item.creator ? item.creator : '匿名用户';
                        })
                        this.totalCount = res.totalCount;
                    }else {
                        this.gridData = [];
                        this.totalCount = 0;
                    }
                })
            },
            getDate(date) {
                if(date) {
                    this.mark.begin = date[0].substr(0, 10);
                    this.mark.end = date[1].substr(0, 10);
                }else {
                    this.mark.begin ='';
                    this.mark.end = '';
                }
            },
            getList() {

            },
            handleSizeChange(val) {
                this.mark.limit = val;
                this.getDataList();
            },
            handleCurrentChange(val) {
                this.mark.start = val;
                this.getDataList();
            }
        }
    }
</script>

<style lang="scss" scoped>
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
        .el-date-editor .el-range__close-icon{
            display: none;
        }
    }

    .main {
        margin: 20px;
        padding: 20px 25px;
        background-color: #fff;

        .output {
            text-align: left;
            margin-bottom: 20px;
        }

        .block {
            margin-top: 20px;
        }
    }
</style>
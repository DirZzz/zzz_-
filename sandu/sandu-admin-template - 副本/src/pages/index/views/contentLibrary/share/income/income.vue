<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <div class="incomePrice">
          <div>
              <h1>{{incomeShow.todayIncome || 0}}</h1>
              <p>今日收益（元）</p>
          </div>
          <div>
              <h1>{{incomeShow.waitWithdrawAmount || 0}}</h1>
              <p>未结算（元）</p>
          </div>
          <div>
              <h1>{{incomeShow.totalIncome || 0}}</h1>
              <p>历史收益（元）</p>
          </div>
        </div>
        <div class="listData">
            <el-row :gutter="24" class="splineFrame">
                <el-col :span="1" class="productBaseInfo">方案列表</el-col>
                <el-col :span="23" class="rightLine"><hr></el-col>
            </el-row>
            <div>
                <el-select placeholder="提现状态" clearable @change="incomeList" v-model="query.withdrawStatus">
                    <el-option value="0" label="未提现">未提现</el-option>
                    <el-option value="1" label="已提现">已提现</el-option>
                </el-select>
                <el-date-picker
                        v-model="query.startTime"
                        type="date"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="开始日期">
                </el-date-picker> --
                <el-date-picker
                        v-model="query.endTime"
                        type="date"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="结束日期">
                </el-date-picker>
                <el-input placeholder="方案编号" v-model="query.planCode" style="width: 200px"></el-input>
                <el-button size="small" round type="primary" class="UnifiedsearchBtn" @click="incomeList">搜索</el-button>
                <el-button size="small" round class="btnSy" @click="resetList">重置</el-button>
            </div>
            <div class="showData">
                <el-table
                        :data="list"
                        style="width: 100%;"
                        ref="multipleTable"
                        border
                        v-loading="planLoading"
                        element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(255, 255, 255, 0.8)"
                        tooltip-effect="dark"
                        :header-cell-style="rowStyle"
                > <!--:header-row-style="rowStyle"-->
                    <el-table-column
                            prop="planCode"
                            label="方案编号"
                            >
                    </el-table-column>
                    <el-table-column
                            label="购买用户"
                            prop="buyUserMobile"
                    >
                        <!--phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')-->
                        <template slot-scope="scope">
                            <div v-if="scope.row.buyUserMobile">
                                {{scope.row.buyUserMobile.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')}}
                            </div>
                            <div v-else>———</div>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="使用方式"
                            prop="designPlanUseType"
                    >
                    </el-table-column>
                    <el-table-column
                            label="时间"
                            prop="useTime"
                    >
                    </el-table-column>
                    <el-table-column
                            label="涉及金额（元）"
                            prop="planPrice"
                    >
                        <template slot-scope="scope">
                            {{scope.row.planPrice}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="可结算金额（元）"
                            prop="withdrawAmount"
                    >
                        <template slot-scope="scope">
                            {{scope.row.withdrawAmount}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="withdrawStatus"
                            label="提现状态"
                    >
                        <template slot-scope="scope">
                            <span v-if="scope.row.withdrawStatus == 0">未提现</span>
                            <span v-if="scope.row.withdrawStatus == 1">已提现</span>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pageFrame">
                    <el-pagination
                            @size-change="SizeChange"
                            @current-change="CurrentChange"
                            :current-page.sync="query.page"
                            :page-sizes="[10, 50, 100, 200,500]"
                            :page-size="query.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="total">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "income",
        data(){
            return{
                pickTime:'',
                total:0,
                planLoading: false,
                nav: [
                    { nav: "共享方案库", url: "/share/plan" },
                    { nav: "企业收益", url: "/share/plan" }
                ],
                list:[],
                pickerOptions2:'',
                incomeShow:{},
                query:{
                    companyId:sessionStorage.getItem('companyID'),
                    page:0,
                    limit:10,
                    endTime:'',
                    startTime:'',
                    planCode:''
                }
            }
        },
        created(){
            this.incomeList()
        },
        methods:{
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '30px!important',padding:'0px!important'}
                }
            },
            resetList(){
                this.query.endTime=''
                this.query.startTime=''
                this.query.withdrawStatus=''
                this.query.planCode= ''
                this.incomeList()
            },
            SizeChange (val) {
                this.query.limit = val
                this.incomeList()
            },
            CurrentChange (val) {
                this.query.page = val
                this.incomeList()
            },
            incomeList(){
                let query = new FormData()
                query.append("page", this.query.page);
                query.append("limit", this.query.limit);
                query.append("planCode", this.query.planCode);
                query.append("startTime", this.query.startTime);
                query.append("endTime", this.query.endTime == null ? '' : this.query.endTime);
                query.append("withdrawStatus", this.query.withdrawStatus == null ? '' : this.query.withdrawStatus);
                query.append("companyId", sessionStorage.getItem('companyID')); // sessionStorage.getItem('companyID')
                console.log(query,'query')
                this.API.companyIncome(query).then((res) => {
                    console.log(res,'res list')
                    this.incomeShow = res.data.CompanyDesignPlanIncomeAggregated
                    this.list = res.data.list
                    this.total = res.total
                })
            }
        }
    }
</script>

<style scoped lang="scss">
    .btnSy{
        width: 100px;
        margin-left: 15px;
        background: #f5f5f5;
    }
    .showData{
        margin-top: 21px;
    }
    .incomePrice{
        display: flex;
        justify-content: space-between;
        div{
            margin-right: 10px;
            width: 540px;
            padding-left: 124px;
            height: 200px;
            &:nth-of-type(1){
                background:#fff url("../../../../assets/images/icons/income_01.png") no-repeat;
                background-position: 77% 142px;
                h1{
                    color: #FF6419;
                }
            }
            &:nth-of-type(2){
                background:#fff url("../../../../assets/images/icons/income_02.png") no-repeat;
                background-position: 77% 142px;
                h1{
                    color: #4fc2b9;
                }
            }
            &:nth-of-type(3){
                margin-right: 0;
                background:#fff url("../../../../assets/images/icons/income_03.png") no-repeat;
                background-position: 77% 142px;
                h1{
                    color: #7a85a2;
                }
            }
            p{
                color: #a2a9b6;
                font-size: 16px;
                margin-top: 22px;
                padding-left: 8px;
            }
            h1{
                margin-top: 53px;
                font-size: 42px;
            }
        }
    }
    .listData{
        padding: 41px 36px;
        background: #fff;
        margin-top: 10px;
    }
    .pageFrame {
        height: 80px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
</style>
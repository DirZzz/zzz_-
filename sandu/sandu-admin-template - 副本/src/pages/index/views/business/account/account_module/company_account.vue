<template>
    <div class="account">
        <div class="account-header">
            <div class="seek-condition">
                <el-input size="medium" v-model="seekAccount" placeholder="请输入账号"></el-input>
                <el-input size="medium" v-model="seekPhone" placeholder="请输入手机号"></el-input>
                <el-input size="medium" v-model="seekName" placeholder="请输入昵称"></el-input>
                <el-select size="medium" clearable filterable v-model="companyCode" placeholder="请选择经销商企业">
                    <el-option
                            v-for="item in companyList"
                            :key="item.id"
                            :label="item.compnayName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </div>
            <div class="seek">
                <el-button size="medium" type="primary" class="UnifiedsearchBtn" round @click="seek('搜索')">搜索</el-button>
                <el-button size="medium" class="reset UnifiedsearchBtn" round @click="seek('重置')">重置</el-button>
            </div>
        </div>
        <div class="account-content">
            <el-table
                    :data="accountData"
                    style="width: 100%">
                <el-table-column
                        label="序号">
                    <template slot-scope="scope">
                        <span>{{scope.$index + 1}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="account"
                        label="账号">
                    <template slot-scope="scope">
                        <span class="operation-name" @click="goEditAccount(scope, '账号详情')">{{scope.row.account}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="companyName"
                        label="经销商企业名称">
                </el-table-column>
                <el-table-column
                        prop="mobile"
                        label="手机号">
                </el-table-column>
                <el-table-column
                        prop="nickName"
                        label="昵称">
                </el-table-column>
                <el-table-column
                        prop="serviceList"
                        label="拥有服务">
                    <template slot-scope="scope">
                        <span v-if="scope.row.serviceList.length > 0" v-for="(item, index) in scope.row.serviceList">
                            {{item.serviceName}}{{index == scope.row.serviceList.length - 1 ? '' : ','}}
                            <em class="service-time" v-if="item.serviceEndTime">到期时间：{{item.serviceEndTime}}</em>
                            <em class="service-time" v-else>该账号未激活</em>
                        </span>
                    </template>
                </el-table-column>
                <el-table-column
                        width="200"
                        prop="operation"
                        label="操作 ">
                    <template slot-scope="scope">
                        <span class="operation" @click="buyPackage(scope)" :class="{'buyPackage':scope.row.servicesFlag!=1||scope.row.isLoginBefore==0}">购买套餐</span>
                        <span class="operation" v-if="isPer('dealer:user:edit')" @click="goEditAccount(scope, '账号编辑')">编辑</span>
                        <span class="operation" v-if="isPer('dealer:user:manage')" @click="deleteAccount(scope)">删除</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="footer-paging">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="accountParameter.start"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="10"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalCount">
            </el-pagination>
        </div>
    </div>
</template>

<script>

    import qs from 'qs';
    export default {
        name: "account",
        data() {
            return {
                seekAccount: '', // 账号
                seekPhone: '', // 手机号
                seekName: '', // 昵称
                companyCode: '', // 经销商企业code
                companyList: [{name: 1, code: 2}], // 经销商企业列表
                accountData: [], // 账户数据表格
                totalCount: 0, // 店铺数据总条数
                accountParameter: { // 账号列表请求参数
                    companyId: sessionStorage.getItem('companyID'), // 企业id
                    franchiserId: undefined, // 经销商id
                    nickName: undefined, // 昵称
                    phone: undefined, // 手机号
                    userName: undefined, // 账号
                    start: 1, // 页码
                    limit: 10 // 每页条数
                }
            };
        },
        created() {
            this.getAccount();
            this.getDealerList();
            // console.log(qs.parse(sessionStorage.getItem('loginUser')).id);
        },
        methods: {
            /*权限设置*/
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            /*搜索*/
            seek(type) {
                if (type == '搜索') {
                    this.accountParameter.userName = this.seekAccount.length > 0 ? this.seekAccount : undefined;
                    this.accountParameter.phone = this.seekPhone.length > 0 ? this.seekPhone : undefined;
                    this.accountParameter.nickName = this.seekName.length > 0 ? this.seekName : undefined;
                    this.accountParameter.franchiserId = this.companyCode != '' || toString(this.companyCode).length > 0 ? this.companyCode : undefined;
                    this.getAccount();
                }
                if (type == '重置') {
                    this.seekAccount = '';
                    this.seekPhone = '';
                    this.seekName = '';
                    this.companyCode = '';
                    this.accountParameter.userName = undefined;
                    this.accountParameter.phone = undefined;
                    this.accountParameter.nickName = undefined;
                    this.accountParameter.franchiserId = undefined;
                    this.getAccount();
                }
            },
            /*进入编辑或详情*/
            goEditAccount(scope, type) {
                sessionStorage.setItem('routerPath', this.$route.path);
                sessionStorage.setItem('routerTxt', type);
                sessionStorage.setItem('userId', scope.row.userId);
                this.$router.push('/business/editAccount');
            },
            /*删除*/
            deleteAccount(scope) {
                this.$confirm('此操作将上次当前店铺信息，是否继续','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.getCompanyDelete({id: scope.row.userId}).then(res => {
                        if (res.success) {
                            this.$message({
                                type: 'success',
                                message: res.message
                            });
                            this.accountData.splice(scope.$index, 1);
                            this.getAccount();
                        } else {
                            this.$message({
                                type: 'error',
                                message: res.message
                            });
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            buyPackage(scope){
                if(scope.row.servicesFlag==1&&scope.row.isLoginBefore!=0){
                    this.$store.dispatch('setUserId',scope.row.userId);
                    this.$store.dispatch('setServicesType',scope.row.serviceList[0].servicesType);
                    this.$store.dispatch('dialog',{isShow:true,name:'dialogBuy'});
                }
            },
            /*每页加载数据条数*/
            handleSizeChange(val) {
                this.accountParameter.limit = val;
                this.getAccount();
            },
            /*分页跳转*/
            handleCurrentChange(val) {
                this.accountParameter.start = val;
                this.getAccount();
            },
            /*经销商企业*/
            getDealerList() {
                // this.API.getDealerList({companyId: sessionStorage.getItem('companyID')}).then((res) => {
                //     if (res.datalist) {
                //         this.companyList = res.datalist;
                //         console.log(this.companyList);
                //     }
                // })
                this.API.getDealerList({companyId: sessionStorage.getItem('companyID')}).then((res) => {
                    if (res.datalist) {
                        this.companyList = res.datalist;
                    }
                });
            },
            /*请求账号列表*/
            getAccount() {
                this.API.getCompanyAccount(this.accountParameter).then((res) => {
                    if (res.datalist) {
                        this.accountData = res.datalist;
                        this.totalCount = res.totalCount;
                    } else {
                        this.accountData = [];
                        this.totalCount = res.totalCount;
                    }
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../../assets/css/mixin";

    .account{
        .account-header{
            background-color: #ffffff;
            margin-bottom: 20px;
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
            .seek-condition{
                padding: 20px;
                border-bottom: 1px solid #dddddd;
                display: flex;
                justify-content: left;
                .el-input{
                    width: 200px;
                    margin-right: 20px;
                }
                .el-select{
                    width: 200px;
                    height: 36px;
                }
            }
            .seek{
                padding: 20px;
                  text-align: center;
                .el-button{
                    width: 100px;
                    margin-right: 10px;
                    border: solid 1px #dddddd;
                }
                .reset{
                    background: #f5f5f5;
                }
            }
        }
        .account-content{
            background-color: #ffffff;
            padding: 20px;
            .operation{
                color: #ff6419;
                margin-left: 10px;
                cursor:pointer;
            }
            .buyPackage{
                color: #999999;
            }
            .operation-name{
                color: #ff6419;
                cursor:pointer;
            }
            .service-time{
                color: #999;
                font-size: 14px;
                font-style: normal;
                margin-left: 10px;
            }
        }
        .footer-paging{
            width: 100%;
            background-color: #ffffff;
            padding: 20px 0;
            text-align: center;
        }
    }

</style>
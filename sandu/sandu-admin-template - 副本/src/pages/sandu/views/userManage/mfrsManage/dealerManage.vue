<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/userManage/mfrsManage' }">厂商管理</el-breadcrumb-item>
            <el-breadcrumb-item>经销商企业</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="main-header">
            <el-form :model="query" ref="search-form">
                <el-row>
                    <el-col :span="4">
                        <el-form-item prop="companyCode">
                            <el-input v-model="query.companyCode" clearable placeholder="请输入经销商编码"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item prop="companyName">
                            <el-input v-model="query.companyName" clearable placeholder="请输入经销商名称"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item prop="contactTelephone">
                            <el-input v-model="query.contactTelephone" clearable placeholder="请输入联系电话"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item prop="industry">
                            <belongIndustry @change="page"></belongIndustry>
                        </el-form-item>
                    </el-col>
                    <el-button type="primary" size="medium" round @click="page()">搜索</el-button>
                    <el-button size="medium" round @click="reset('search-form')">重置</el-button>
                </el-row>
            </el-form>
        </div>
        <div class="tableList">
            <el-button type="primary" v-if="permission('dealer.manage.add')" size="medium" round @click="toLink('/userManage/mfrsManage/dealerAdd',{pid:parentCompanyId})">新增
            </el-button>
            <el-table
                    :data="tableData"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    align="center"
                    style="width: 100%;margin-top: 20px">
                <el-table-column
                        type="index"
                        label="序号">
                </el-table-column>
                <el-table-column
                        prop="companyCode"
                        label="经销商编码">
                         <template slot-scope="scope">
                             <span @click="toLink('/userManage/mfrsManage/dealerInfo',{pid:scope.row.pid,id:scope.row.companyId})" class='txts'>{{scope.row.companyCode}}</span>
                        </template>
                </el-table-column>
                <el-table-column
                        prop="companyName"
                        label="经销商名称">
                </el-table-column>
                <el-table-column
                        prop="brands"
                        label="所属品牌">
                </el-table-column>
                <el-table-column
                        prop="contactTelephone"
                        label="联系电话">
                </el-table-column>
                <el-table-column
                        prop="companyIndustries"
                        label="所属行业">
                </el-table-column>
                <el-table-column
                        width="320"
                        align="center"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" v-if="permission('dealer.manage.update')" @click="toLink('/userManage/mfrsManage/dealerEdit',{pid:scope.row.pid,id:scope.row.companyId})">编辑</el-button>
                        <el-button type="text" v-if="permission('dealer.manage.del')" @click="doDeleteDealer(scope.row.companyId)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!--分页组件-->
        <div class="pageFrame">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="query.page"
                    :page-sizes="[10,20,30,50]"
                    :page-size="query.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </page>
</template>

<script>
    import minixs from '@s/minixs/index';

    export default {
        name: "dealerManage",
        mixins: [minixs],
        data() {
            return {
                parentCompanyId: null,
                loading: false,
                tableData: [],
                total: 0,
                query: {
                    page: 0,
                    limit: 10,
                    pid: null,
                    companyCode: null,
                    companyName: null,
                    contactTelephone: null,
                    industry: null
                },
                errorMsg: '系统错误，请稍后再试！'
            }
        },
        methods: {
            init() {
                this.parentCompanyId = this.$route.params.parentCompanyId;
                this.query.pid = this.parentCompanyId;
                this.page();
            },
            page(args) {
                // 行业
                this.query.industry = args ? args.industry.join(',') : null;

                this.API.listDealerManage(this.query).then((resp) => {
                    if (!resp || !resp.success) {
                        this.$message.error(resp ? (resp.message || this.errorMsg) : this.errorMsg);
                    } else {
                        this.total = resp.totalCount || 0;
                        this.tableData = resp.datalist || [];
                    }
                });
            },
            doDeleteDealer(companyId) {
                this.$confirm('确定删除经销商, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    center: true
                }).then(() => {
                    this.API.doDeleteDealer({companyId: companyId}).then((resp) => {
                        if (!resp || !resp.success) {
                            this.$message.error(resp ? (resp.message || this.errorMsg) : this.errorMsg);
                        } else {
                            this.page();
                        }
                    });
                }).catch(() => {
                    this.$message({type: 'info', message: '已取消删除'});
                });
            },
            reset(target) {
                this.$nextTick(() => {
                    this.$refs[target].resetFields();
                });
            },
            handleSizeChange(limit) {
                this.query.limit = limit;
                this.page();
            },
            handleCurrentChange(page) {
                this.query.page = page;
                this.page();
            }
        },
        activated() {
            this.init();
            
        }
    }
</script>

<style scoped lang="scss">
    .main-header {
        margin-top: 20px;
    }
    .txts{
        color: #409EFF;
        display: inline-block;
        cursor: pointer;
        width: 100%;
    }
    .pageFrame {
        text-align: right;
        margin-top: 20px;
    }
</style>
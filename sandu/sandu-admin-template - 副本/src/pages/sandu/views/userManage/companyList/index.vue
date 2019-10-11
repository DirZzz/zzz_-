<template>
    <page>
        <div class="main">
            <div class="main-header">
                <el-row>
                    <el-col :span="4">
                        <el-input v-model="formParam.companyCode" placeholder="请输入企业编码" clearable></el-input>
                    </el-col>
                    <el-col :span="4">
                        <el-input v-model="formParam.companyName" placeholder="请输入企业名称" clearable></el-input>
                    </el-col>
                    <el-col :span="4">
                        <el-input v-model="formParam.phone" placeholder="请输入手机号"></el-input>
                    </el-col>
                    <el-col :span="4">
                        <companyType @companyType="companyTypeChange" ref="companyType"></companyType>
                    </el-col>
                    <el-col :span="4">
                        <belongIndustry @change="industryChange" ref="belongIndustry"></belongIndustry>
                    </el-col>
                    <areaSelect @provinceCode="provinceChange" @cityCode="cityChange" @areaCode="areaChange"
                        @streetCode="streetChange" ref="areaSelect"></areaSelect>
                </el-row>
                <div class="btn">
                    <el-button type="primary" size="medium" round @click="search">搜索</el-button>
                    <el-button size="medium" round @click="reset">重置</el-button>

                </div>
                <div class="newbtn">
                    <el-button type="primary" size="medium" round class="newAdd"
                        v-if="permission('manufacturer.manage.add')" @click="toLink('/userManage/companyList/companyAdd')">
                        新增</el-button>
                </div>
            </div>

            <div class="tableList">
                <el-table :data="tableData.list" v-loading="loading" element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)"
                    align="center" style="width: 100%" >
                    <el-table-column type="index" label="序号">
                    </el-table-column>
                    <el-table-column prop="companyCode" label="企业编码">
                    </el-table-column>
                    <el-table-column prop="companyName" label="企业名称">
                        <template slot-scope="scope">
                            <span class="blues" @click="toLink('/userManage/companyList/companyInfo',{id:scope.row.id})">{{scope.row.companyName}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="手机号">
                    </el-table-column>
                    <el-table-column prop="companyType" label="公司类型">
                    </el-table-column>
                    <el-table-column label="所属行业">
                        <template slot-scope="scope">
                            <span v-for="(item,index) in scope.row.industryInfo" :key="index">{{item.name}} <em
                                    v-if="index!=scope.row.industryInfo.length-1">、</em></span>
                        </template>
                    </el-table-column>
                    <el-table-column label="区域">
                        <template slot-scope="scope">
                            <div>{{scope.row.provinceCode}}{{scope.row.cityCode }}{{scope.row.areaCode}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="companyAddress" label="详细地址">
                    </el-table-column>
                    <el-table-column width="320" align="center" label="操作">
                        <template slot-scope="scope">
                            <el-button type="text" v-if="permission('company.manage.update')"
                                @click="toLink('/userManage/companyList/companyEdit',{id:scope.row.id})">编辑</el-button>
                            <el-button type="text" v-if="permission('company.manage.user')"
                                @click="toLink('/userManage/companyList/userManage',{id:scope.row.id,businessType:scope.row.businessType})">
                                用户管理</el-button>
                            <el-button type="text" v-if="permission('company.manage.del')"
                                @click="deleteList(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page.sync="query.page" :page-sizes="[10,20,30,50]" :page-size="query.limit"
                    layout="total, sizes, prev, pager, next, jumper" :total="tableData.total">
                </el-pagination>
            </div>
        </div>
    </page>
</template>

<script>
    import mixins from '@s/minixs/index';
    export default {
        name: "index",
        mixins: [mixins],
        data() {
            return {
                loading: true,
                formParam: {
                    companyCode: '',
                    companyName: '',
                    phone: '',
                    provinceCode: '',
                    cityCode: '',
                    areaCode: '',
                    streetCode: '',
                    companyType: '',
                    industryInfo: '',
                },
                tableData: '',
                query: {
                    page: 1,
                    limit: 10,
                }
            }
        },
        activated() {
            this.comapnyList();
        },
        deactivated() {
            this.reset();
        },
        methods: {
            comapnyList() {
                this.API.comapnyInner({
                    page: this.query.page,
                    limit: this.query.limit,
                    companyName: this.formParam.companyName || undefined,
                    companyCode: this.formParam.companyCode || undefined,
                    provinceCode: this.formParam.provinceCode || undefined,
                    phone: this.formParam.phone || undefined,
                    cityCode: this.formParam.cityCode || undefined,
                    areaCode: this.formParam.areaCode || undefined,
                    streetCode: this.formParam.streetCode || undefined,
                    companyType: this.formParam.companyType || undefined,
                    industryValueStr: this.formParam.industryInfo || undefined,
                    listType: 2 //公司列表
                }).then(res => {
                    this.loading = false;
                    if (res.success) {
                        this.tableData = res;
                    } else {
                        this.$message.error(res.message);
                    }
                })
            },
            search() {
                this.query.page = 1;
                this.comapnyList()
            },
            reset() {
                for (let key in this.formParam) {
                    this.formParam[key] = ''
                }
                this.$refs.companyType.reset();
                this.$refs.areaSelect.reset();
                this.$refs.belongIndustry.reset();
                this.comapnyList()
            },
            companyTypeChange(val) {
                this.formParam.companyType = val;
            },
            provinceChange(code) {
                this.formParam.provinceCode = code;
            },
            cityChange(code) {
                this.formParam.cityCode = code;
            },
            areaChange(code) {
                this.formParam.areaCode = code;
            },
            streetChange(code) {
                this.formParam.streetCode = code;
            },
            handleSizeChange(val) {
                this.loading = true;
                this.query.limit = val;
                this.comapnyList()
            },
            handleCurrentChange(val) {
                this.loading = true;
                this.query.page = val;
                this.comapnyList()
            },
            industryChange(arr) {
                this.formParam.industryInfo = arr.industry.join(',');
            },
            deleteList(item) {
                this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.deleteComapnyInner({
                        id: item.id
                    }).then(res => {
                        if (res.success) {
                            this.$message.success('删除成功！')
                            this.comapnyList()
                        } else {
                            this.$message.error(res.message)
                        }
                    })
                }).catch(() => {})

            }
        }
    }
</script>

<style scoped lang="scss">
    .main {
        .tableList {
            width: 100%;
            height: 100%;
        }

        .newbtn {
            text-align: left;
            margin-bottom: 20px;
        }

        .pageFrame {
            margin-top: 20px;
            text-align: right;
        }

        .blues {
            color: #409EFF;
            cursor: pointer;
        }

        .btn {
            position: relative;
            text-align: center;
            margin-top: 20px;
            margin-bottom: 20px;

        }
    }
</style>
<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>电子名片统计数据</span>
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">
                <el-input v-model="formDate.nickName" placeholder="登录名" clearable> </el-input>
                <el-input v-model="formDate.mobile" placeholder="手机号" clearable> </el-input>
                <el-input v-model="formDate.userName" placeholder="昵称" clearable> </el-input>
                <el-select v-model="formDate.companyId" clearable placeholder="请选择企业" filterable>
                    <el-option v-for="(item,index) in companyList" :key="index" :label="item.companyName" :value="item.id"></el-option>
                </el-select>
            </el-row>
        </div>
        <div class="btnFrame">
            <el-button  round type="primary" class="UnifiedsearchBtn" @click="submit">搜索</el-button>
            <el-button  round class="searchBtn UnifiedsearchBtn" @click="resetForm">重置</el-button>
        </div>
        <div class="tableList">
           <div class="updetes">
                <el-button round type="primary" class="addProBtn" plain @click.stop="exportCustomerData">导出</el-button>
            </div>     
            <el-table :data="tableData" v-loading="loading" element-loading-text="拼命加载中" 
                @sort-change='sortChange'
                element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)" style="width: 100%">
                <el-table-column prop="nickName" label="登录名"></el-table-column>
                <el-table-column prop="userName" label="昵称"></el-table-column>
                <el-table-column prop="mobile" label="手机号"></el-table-column>
                <el-table-column prop="brandNameStr" label="品牌">
                    <template slot-scope="scope">
                        <el-popover
                            placement="top-start"
                            width="200"
                            trigger="hover"
                            :content="scope.row.brandNameStr">
                            <el-button slot="reference" class="hidenMoreSpan">{{ scope.row.brandNameStr }}</el-button>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column prop="companyName" label="企业名称"></el-table-column>
                <el-table-column prop="todayTransmitCount" label="今日转发次数" sortable='custom' width="135px"></el-table-column>
                <el-table-column prop="todayVisitCount" label="今日访问次数" sortable='custom' width="135px"></el-table-column>
                <el-table-column prop="todayVisitUser" label="今日访问人数" sortable='custom' width="135px"></el-table-column>
                <el-table-column prop="monthTransmitCount" label="当月转发次数" sortable='custom' width="135px"></el-table-column>
                <el-table-column prop="monthVisitCount" label="当月访问次数" sortable='custom' width="135px"></el-table-column>
                <el-table-column prop="monthVisitUser" label="当月访问人数" sortable='custom' width="135px"></el-table-column>
                <el-table-column prop="totalTransmitCount" label="总转发次数" sortable='custom' width="115px"></el-table-column>
                <el-table-column prop="totalVisitCount" label="总访问次数" sortable='custom' width="115px"></el-table-column>
                <el-table-column prop="totalVisitUser" label="总访问人数" sortable='custom' width="115px"></el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="formDate.curPage"
                        :page-sizes="[5,10, 50, 100, 200,500]"
                        :page-size="formDate.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        name: "coustomer",
        data(){
            return{
                myUserId:sessionStorage.getItem('userId'),
                tableData:[],
                loading:true,
                companyList:[],
                total: 0, // 总页数
                formDate:{userName:undefined, mobile:undefined, nickName:undefined, companyId:undefined, curPage:1, pageSize:10, order:undefined, sort:undefined},
                // kvObj:{todayTransmitCount:'ttr.count',todayVisitCount:'tar.count',todayVisitUser:'tar.userCount',monthTransmitCount:'mtr.count',monthVisitCount:'mar.count',monthVisitUser:'mar.userCount',totalTransmitCount:'tr.count',totalVisitCount:'ar.count',totalVisitUser:'ar.userCount'},
                kvObj:{todayTransmitCount:'todayTransmitCount',todayVisitCount:'todayVisitCount',todayVisitUser:'todayVisitUser',monthTransmitCount:'monthTransmitCount',monthVisitCount:'monthVisitCount',monthVisitUser:'monthVisitUser',totalTransmitCount:'totalTransmitCount',totalVisitCount:'totalVisitCount',totalVisitUser:'totalVisitUser'},
            }
        },
        activated(){
            this.init();
        },
        methods:{
            init(){
                this.getCustomerListData()
                this.getAllCompany();
            },
            getAllCompany(){
                this.API.getAllCompany().then(res=>{
                    if(res.success){
                        this.companyList = res.datalist
                    }else{
                        this.companyList = [];
                        this.$message.error(res.message);
                    }
                })
            },
            sortChange(r){
                this.formDate.order = this.kvObj[r.prop];
                switch(r.order){
                    case 'ascending':
                        this.formDate.sort = 'desc';
                        break;
                    case 'descending':
                        this.formDate.sort = 'asc';
                        break;
                    default:
                        this.formDate.order = undefined

                        this.formDate.sort = undefined
                        break;
                }
                this.getCustomerListData();
            },
            getCustomerListData() {
                this.loading = true
                this.API.getAllUserCardOfCompany(this.formDate).then(res=>{
                    this.loading = false;
                    if(res.success){
                        this.tableData = res.datalist;
                        this.total = res.totalCount;
                    }else{
                        this.tableData = [];
                        this.total = 0;
                    }
                })
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },
            exportCustomerData(){// 导出excel 表
                const loading = this.$loading({text: '正在导出...', spinner: 'el-icon-loading'});
                console.log(this.formDate)
                this.API.getAllUserCardOfCompany(this.formDate,true).then(res=>{
                    loading.close();
                    if(res.success){
                        require.ensure([], () => {
                            const { export_json_to_excel } = require('@/vendor/Export2Excel')
                            const tHeader = ['登录名','昵称', '手机号','品牌', '公司','今日转发次数','今日访问次数',
                                '今日访问人数','当月转发次数','当月访问次数','当月访问人数','总转发次数','总访问次数','总访问人数']
                            const filterVal = ['nickName','userName', 'mobile','brandNameStr', 'companyName','todayTransmitCount','todayVisitCount',
                                'todayVisitUser','monthTransmitCount','monthVisitCount','monthVisitUser','totalTransmitCount','totalVisitCount','totalVisitUser']
                            export_json_to_excel(tHeader, this.formatJson(filterVal, res.datalist), '电子名片统计数据')
                            this.$message.success('导出成功')
                        });  
                    }else{
                        this.$message.error('导出失败！')
                    }
                })
            },

            // 分页模块
            handleSizeChange: function (size) {
                console.log(size,'size')
                this.formDate.pageSize = size
                this.getCustomerListData()
            },
            handleCurrentChange: function (currentPage) {
                this.formDate.curPage = currentPage;
                this.formDate.pageSize ? this.formDate.pageSize : this.formDate.pageSize=10;
                this.getCustomerListData()
                console.log(this.formDate)
            },
            submit(){
                this.formDate.curPage = 1;
                this.getCustomerListData()
            },
            resetForm(){
                this.formDate = { userName:undefined, mobile:undefined, nickName:undefined, companyId:undefined, curPage:1, pageSize:10, order:undefined, sort:undefined}
                this.getCustomerListData()
            }
        },
        
    }
</script>

<style scoped lang="scss">
    .changeColor{
        color: #ff2323;
    }
    .customerTitle{
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        border-bottom: solid 1px #ddd;
        span{
            display: inline-block;
            padding-left: 20px;
        }
    }
    .btn-right{
        position: absolute;
        left: 20px;
        top: 20px;
        .addProBtn{
           width:120px;
           height:32px;
           font-size:12px;
           text-align:center;
           line-height: 8px;
        }
        .addProBtn1{
          height:32px;
          font-size:12px;
          text-align:center;
          line-height: 8px;
        }
    }

    .searchFrame{
        background: #fff;
        padding: 20px;
    }
    .areaStyle{
        margin-top: 20px;
        .el-select{
            width: 216px;
            margin-right: 10px;
        }
    }
    .selectSearchCond{
        .el-select,.el-input{
            width: 216px;
            margin-right: 10px;
        }

    }
    .hidenMoreSpan{
        width: 100%;
        height: 100%;
        display: block;
        padding: 0;
        background: rgba(0,0,0,0) !important;
        text-overflow: ellipsis;
        color: #606266 !important;
        overflow: hidden;
        white-space: nowrap;
        border: none;
    }
    .btnFrame{
        padding:10px 20px;
        text-align: center;
        border-top: solid 1px #ddd;
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        .el-button{
            width: 130px;
        }
    }

    .setRule{
        margin-top: 20px;
        height: 76px;
        line-height: 76px;
        background: #fff;
        color: #FF6419;
        padding-left: 20px;
        span{
            display: inline-block;
            cursor: pointer;
        }
    }
    .tableList{
        margin-top: 20px;
        background: #fff;
        .updetes{
            padding: 10px 10px;
        }
    }
    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
    .level{
        vertical-align: -4px;
        margin-right: 4px;
    }


</style>
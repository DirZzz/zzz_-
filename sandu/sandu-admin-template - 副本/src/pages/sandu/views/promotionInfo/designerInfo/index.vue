<template>
    <page>
        <header>设计师入驻信息</header>
        <div class="searchNav">
            <div class="search-area-item">
                <p>区域：</p>
                <areaSelect @provinceCode="provinceChange" @cityCode="cityChange" ref="areaSelect" :type="true"></areaSelect>
            </div>
            <div class="search-item">
                <el-select v-model="params.process" placeholder="请选择处理结果" clearable filterable>
                    <el-option
                            v-for="item in resultList"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
            <div class="search-item">
                <el-input v-model="params.mobile" placeholder="手机号码" clearable></el-input>
            </div>
        </div>
        <div class="btnFrame clearfix">
            <p>
                <el-button round type="primary" size="small" @click="search">搜索</el-button>
                <el-button round size="small" @click="reset">重置</el-button>
            </p>
        </div>
        <div class="btn">
            <div class="btn-item">
                <el-button @click="dealItem">批量修改为已处理</el-button>
                <el-button @click="deleteItem">批量删除</el-button>
            </div>
            <div class="btn-item">
                <el-button type="primary" @click="exportExcel">导出</el-button>
            </div>
        </div>
        <div class="tableList">
            <el-table
                    :header-cell-style="rowStyle"
                    :data="tableData"
                    v-loading="loading"
                    @selection-change="handleSelectionChange"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%"
                    align="center"
            >
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="序号" type="index"  align="center" ></el-table-column>
                <el-table-column label="类型" prop="businessTypeName" header-align="center" align="center"></el-table-column>
                <el-table-column label="姓名" prop="userName" header-align="center" align="center"></el-table-column>
                <el-table-column label="手机号码" prop="mobile" header-align="center" align="center"></el-table-column>
                <el-table-column label="城市" prop="cityName" header-align="center" align="center"></el-table-column>
                <el-table-column label="时间" prop="gmtCreated" header-align="center" align="center"></el-table-column>
                <el-table-column label="处理结果" prop="processInfo" header-align="center" align="center"></el-table-column>
                <el-table-column
                        label="操作" header-align="center" align="center" width="180">
                    <template slot-scope="scope">
                        <el-button type="text" v-if="scope.row.process==0" @click="dealItem(1,scope.row)">修改已处理</el-button>
                        <el-button type="text" @click="deleteItem(1,scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="query.page"
                        :page-sizes="[5,10, 50, 100, 200,500]"
                        :page-size="query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </page>
</template>

<script>
    export default {
        name: "index",
        data(){
            return{
                phone:'',
                result:'',
                loading:false,
                resultList:[
                    { name:'已处理',value:1 },
                    { name:'未处理',value:0 }
                ],
                tableData:[],
                query: {
                    page: 1,
                    limit: 10,
                },
                total:0,
                params:{
                    businessType:1,
                    process:undefined,
                    provinceCode:undefined,
                    cityCode:undefined,
                    mobile:undefined,
                    currentPage:1,
                    limit:10,
                },
                itemArr:[]
            }
        },
        created(){
            this.list();
        },
        methods:{
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '60px!important',background:'#f5f5f5',color: '#666'}
                }
            },
            list(){
                this.API.proprietorInfo(this.params).then(res=>{
                    if(res.success){
                        this.tableData=res.datalist;
                        this.total=res.totalCount;
                    }else {
                        this.$message.error(res.message)
                    }
                })
            },
            provinceChange(val){
                this.params.provinceCode=val;
            },
            cityChange(val){
                this.params.cityCode=val;
            },
            handleSizeChange(val){
                this.params.limit=val;
                this.list()
            },
            handleCurrentChange(val){
                this.params.currentPage=val;
                this.list()
            },
            handleSelectionChange(val){
                this.itemArr=[];
                val.map(res=>{
                    this.itemArr.push(res.id);
                })
            },
            search(){
                this.params.currentPage=1;
                this.list()
            },
            reset(){
                for (let key in this.params){
                    if(key !='businessType'&&key !='currentPage'&&key !='limit') this.params[key]=undefined;
                }
                this.$refs.areaSelect.reset();
                this.params.currentPage=1;
                this.list()
            },
            deleteItem(type,item){
                if( type==1 ){
                    this.itemArr=[];
                    this.itemArr.push(item.id);
                    this.deleteMethods();
                }else {
                    if(this.itemArr.length==0)return this.$message.error('至少选择一项');
                    this.deleteMethods();
                }

            },
            deleteMethods(){
                this.API.deletepPoprietorInfo({ids:this.itemArr.join(',')}).then(res=>{
                    if(res.success){
                        this.$message.success(res.message);
                        this.list()
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            dealItem(type,item){
                if(type==1){
                    this.itemArr=[];
                    this.itemArr.push(item.id);
                    this.dealMethods();
                }else {
                    if(this.itemArr.length==0)return this.$message.error('至少选择一项');
                    this.dealMethods();
                }
            },
            dealMethods(){
                this.API.updateProcess({ids:this.itemArr.join(','),process:1}).then(res=>{
                    if(res.success){
                        this.$message.success(res.message);
                        this.list()
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            exportExcel(){
                this.API.exportReportList({
                    businessType:this.params.businessType,
                    excelFlag:'design',
                    process:this.params.process,
                    provinceCode:this.params.provinceCode,
                    cityCode:this.params.cityCode,
                    mobile:this.params.mobile,
                }).then(res=>{
                    console.log(res)
                    let url = window.URL.createObjectURL(res)
                    let link = document.createElement('a')
                    link.style.display = 'none'
                    link.href = url;
                    link.setAttribute('download', '设计师入驻信息导出.xls')
                    document.body.appendChild(link)
                    link.click()
                })
            }
        }
    }
</script>

<style scoped lang="scss">
    header{
        height: 48px;
        line-height: 48px;
    }
    .searchNav{
        display: flex;
        padding-bottom: 20px;
        .search-item{
            width: 217px;
            margin-right: 26px;
        }
        .search-area-item{
            display: flex;
            align-items: center;
            p{
                text-align: left;
                font-size: 16px;
                width: 60px;
            }
        }
    }
    .btnFrame {
        padding: 10px 0;
        text-align: center;
        border-top: solid 1px #ddd;
        background: #fff;
        font-size: 18px;
        color: #333;
        p {
            text-align: center;
            padding-bottom:8px;
            padding-top: 8px;
        }

    }
    .btn{
        display: flex;
        justify-content: space-between;
        padding: 20px 0;
    }
    .searchFrame {
        background: #fff;
        text-align: left;
    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

</style>
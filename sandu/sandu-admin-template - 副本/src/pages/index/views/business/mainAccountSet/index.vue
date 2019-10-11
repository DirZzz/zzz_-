<template>
    <div class="mainAccountSet">
        <div class="mainAccountSet-header">主子账号管理</div>
        <div class="mainAccountSet-nav">
            <el-row :gutter="10">
                <el-col :span="4">
                    <el-input v-model="params.nickName" placeholder="请输入账号"></el-input>
                </el-col>
                <el-col :span="4">
                    <el-input v-model="params.mobile" placeholder="请输入手机号"></el-input>
                </el-col>
                <el-col :span="4">
                    <el-input v-model="params.userName" placeholder="请输入昵称"></el-input>
                </el-col>
                <el-col :span="4">
                    <el-select v-model="params.masterSonType" clearable placeholder="主子类型">
                        <el-option
                                v-for="item in accountType"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-col>
                <el-col :span="4">
                    <el-select v-model="params.userType" clearable placeholder="用户类型">
                        <el-option
                                v-for="item in userType"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-col>
            </el-row>
        </div>
        <div class="mainAccountSet-search">
            <el-button size="medium" type="primary" class="btn" round @click="search">搜索</el-button>
            <el-button size="medium" class="btn" round @click="reset">重置</el-button>
        </div>
        <div class="mainAccountSet-table">
            <el-table
                    :data="tableData"
                    style="width: 100%">
                <el-table-column width="80" type="index" label="序号">
                </el-table-column>
                <el-table-column
                        label="账号">
                     <template slot-scope="scope">
                         <span style="color: #ff6419;cursor: pointer" @click="editAccount(scope.row,0)">{{ scope.row.nickName}}</span>
                     </template>
                </el-table-column>
                <el-table-column
                        prop="mobile"
                        label="手机号">
                </el-table-column>
                <el-table-column
                        prop="userName"
                        label="昵称">
                </el-table-column>
                <el-table-column
                        prop="userTypeName"
                        label="用户类型">
                </el-table-column>
                <el-table-column label="主子类型">
                    <template slot-scope="scope">
                        <span v-if="scope.row.masterSonType==0">普通账号</span>
                        <span v-else-if="scope.row.masterSonType==1">子账号</span>
                        <span v-else-if="scope.row.masterSonType==2">主账号</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="address"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" @click="editAccount(scope.row,1)">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="mainAccountSet-pagination">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="params.page"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="params.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalCount">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: "index",
        data(){
            return{
                totalCount:0,
                params:{
                    nickName:'',
                    mobile:'',
                    userName:'',
                    userType:'',
                    masterSonType:'',
                    page:1,
                    limit:10,
                },
                tableData: [],
                userType: [],
                accountType:[
                    { name:'普通账号',value:0 },
                    { name:'子账号',value:1 },
                    { name:'主账号',value:2 },
                ]
            }
        },
        created(){
            this.pageList();
            this.getUserType();
        },
        methods:{
            pageList(){
                for (let key in this.params ){
                    if(typeof this.params[key]!='number'){
                        this.params[key] ? '' : delete this.params[key];
                    }
                }
                this.API2.masterSonUserList(this.params).then(res=>{
                    if(res.success){
                        this.tableData=res.datalist;
                        this.totalCount=res.totalCount;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            getUserType(){
                this.API2.getUserType({type: 'userType'}).then(res=>{
                    if(res.success){
                        this.userType=res.datalist;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            reset(){
                for (let key in this.params){
                    key =='page' || key=='limit' ? '' : this.params[key]='';
                }
                this.pageList();
            },
            search(){
                this.params.page=1;
                this.pageList()
            },
            editAccount(item,type){
                if(type==0){
                    sessionStorage.setItem('routerTxt','账号详情')
                }else {
                    sessionStorage.setItem('routerTxt','账号编辑');
                }
                sessionStorage.setItem('routerPath','/business/mainAccountSet');
                sessionStorage.setItem('isInterior',item.userType==3 ? '经销商账号' : '内部账号');//之前逻辑照搬
                this.$router.push({path:`/editMainAccount/${item.id}`});
            },
            handleSizeChange(val){
                this.params.limit=val;
                this.pageList()
            },
            handleCurrentChange(val){
                this.params.page=val;
                this.pageList()
            }
        }
    }
</script>

<style scoped lang="scss">
    @import "./style/mainAccountSet.scss";
</style>
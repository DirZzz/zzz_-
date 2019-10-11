<template>
    <el-dialog
            title="关联子账号"
            :visible.sync="relationAccount"
            width="990px"
            :before-close="handleClose">
        <el-form :inline="true" :model="formInline" style="text-align: center">
            <el-form-item label="登录名">
                <el-input v-model="formInline.nickName" placeholder=""></el-input>
            </el-form-item>
            <el-form-item label="手机号" >
                <el-input v-model="formInline.mobile" placeholder=""></el-input>
            </el-form-item>
            <el-form-item label="用户类型" >
                <el-select v-model="formInline.userType" clearable placeholder="请选择">
                    <el-option
                            v-for="item in userType"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="">
                <el-button type="primary" round style="margin-left: 20px" @click="search">查询</el-button>
            </el-form-item>
        </el-form>
        <el-row :gutter="24">
            <el-col :span="12">
                <div class="relationAccount-item">
                    <div class="relationAccount-header">
                        <span>选择子账号</span>
                        <em><el-button type="primary" round @click="setAccount('addSon')">添加</el-button></em>
                    </div>
                    <el-table
                            ref="multipleTable1"
                            :data="tableData1"
                            :header-cell-style="{background:'#fff'}"
                            :row-style="{ width:'100%'}"
                            style="width: 100%"
                            @selection-change="handleSelectionChange1">
                        <el-table-column
                                type="selection"
                                width="55">
                        </el-table-column>
                        <el-table-column
                                label="全选"
                                prop="nickName"
                                v-if="tableData1.length>0"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="mobile"
                                label=""
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="userTypeName"
                                label=""
                                width="120">
                        </el-table-column>
                    </el-table>
                    <div class="page-pagination">
                        <el-pagination
                                @size-change="handleSizeChange1"
                                @current-change="handleCurrentChange1"
                                :current-page="currentPage1"
                                :page-sizes="[10, 20, 30, 40]"
                                :page-size="pageSize1"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="totalCount1">
                        </el-pagination>
                    </div>

                </div>
            </el-col>
            <el-col :span="12">
                <div class="relationAccount-item">
                    <div class="relationAccount-header">
                        <span>取消子账号</span>
                        <em><el-button round @click="setAccount('removeSon')">取消</el-button></em>
                    </div>
                    <el-table
                            ref="multipleTable2"
                            :data="tableData2"
                            style="background-color: #fff"
                            :header-cell-style="{background:'#fff'}"
                            @selection-change="handleSelectionChange2">
                        <el-table-column
                                type="selection"
                                width="55">
                        </el-table-column>
                        <el-table-column
                                label="全选"
                                prop="nickName"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="mobile"
                                label=""
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="userTypeName"
                                label=""
                                width="120">
                        </el-table-column>
                    </el-table>
                    <div class="page-pagination">
                        <el-pagination
                                @size-change="handleSizeChange2"
                                @current-change="handleCurrentChange2"
                                :current-page="currentPage2"
                                :page-sizes="[10, 20, 30, 40]"
                                :page-size="pageSize2"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="totalCount2">
                        </el-pagination>
                    </div>

                </div>
            </el-col>
        </el-row>
    </el-dialog>

</template>

<script>
    import { map } from 'lodash'
    export default {
        name: "relationAccount",
        data(){
            return{
                currentPage1:1,
                currentPage2:1,
                pageSize1:10,
                pageSize2:10,
                totalCount1:0,
                totalCount2:0,
                relationAccount:false,
                input:'',
                value:'',
                options:[],
                formInline:{
                    masterUserId:'',
                    nickName:'',
                    mobile:'',
                    userType:'',
                    page:1,
                    limit:10,
                },
                tableData1: [],
                tableData2: [],
                userType: [],
                selectionList1:[],
                selectionList2:[]
            }
        },
        methods:{
            handleClose(){
                this.formInline.page=1;
                this.relationAccount=false;
                for (let key in this.formInline){
                    this.formInline[key]=''
                }
            },
            search(){
                this.formInline.page=1;
                this.listLeft();
                this.listRight();
            },
            toggle(type,item){
                this.relationAccount=type;
                this.formInline.masterUserId=item.id;
                if(this.relationAccount){
                   this.listLeft();
                   this.listRight();
                   this.getUserType();
                }
            },
            listLeft(){
                let obj={
                    page:this.currentPage1,
                    limit:this.pageSize1,
                }
                for (let key in this.formInline){
                    this.formInline[key] ? '' : delete this.formInline[key]
                }
                this.API.companySonUserList(Object.assign(this.formInline,obj)).then(res=>{
                    this.tableData1=res.datalist;
                    this.totalCount1=res.totalCount;
                })
            },
            listRight(){
                let obj={
                    page:this.currentPage2,
                    limit:this.pageSize2,
                }
                for (let key in this.formInline){
                    this.formInline[key] ? '' : delete this.formInline[key]
                }
                this.API.masterSonUserList(Object.assign(this.formInline,obj)).then(res=>{
                    this.tableData2=res.datalist;
                    this.totalCount2=res.totalCount;
                })
            },
            getUserType(){
                this.API.getUserType({type: 'userType'}).then(res=>{
                    if(res.success){
                        this.userType=res.datalist;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            setAccount(type){ //添加 取消账号
                let arr=type == 'addSon' ? this.selectionList1 : this.selectionList2;
                if(arr.length>0){
                    this.API.setMSType({
                        userIdList:map(arr,'id'),
                        msType:type,
                        masterUserId:this.formInline.masterUserId,
                    }).then(res=>{
                        this.$message.success(res.message);
                        this.listLeft();
                        this.listRight();
                    })
                }else {
                    this.$message.error('至少选择一个')
                }

            },
            handleSelectionChange1(val){
                this.selectionList1=val;
            },
            handleSelectionChange2(val){
                this.selectionList2=val;
            },
            handleCurrentChange1(val){
                this.currentPage1=val;
                this.listLeft();
            },
            handleCurrentChange2(val){
                this.currentPage2=val;
                this.listRight();
            },
            handleSizeChange1(val){
                this.currentPage1=1;
                this.pageSize1=val;
                this.listLeft();
            },
            handleSizeChange2(val){
                this.currentPage2=1;
                this.pageSize2=val;
                this.listRight();
            }
        }
    }
</script>

<style scoped lang="scss">
   .relationAccount-item{
       .relationAccount-header{
           background-color: #fafafa;
           height: 60px;
           line-height: 60px;
           position: relative;
           span{
               padding-left: 20px;
           }
           em{
               position: absolute;
               right: 20px;
               top: 0;
           }
       }
   }
    .page-pagination{
        margin-top: 20px;
        text-align: center;
    }
</style>
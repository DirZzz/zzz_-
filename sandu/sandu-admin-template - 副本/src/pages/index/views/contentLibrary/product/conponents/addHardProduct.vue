<template>
    <el-dialog title="关联模型" @close="resetSearchModel" :visible.sync="dialogModelVisible"   top="10vh" >
        <el-form class="textureSearch">
            <el-row :gutter="24">
                <el-col :span="12">
                    <el-input clearable placeholder="请输入模型编号" v-model="assModelDia.modelCode"></el-input>
                </el-col>
                <el-col :span="12">
                    <el-input clearable placeholder="请输入模型型号" v-model="assModelDia.modelModelNum"></el-input>
                </el-col>
            </el-row>

            <el-row :gutter="24">
                <el-col :span="12">
                    <el-input clearable placeholder="请输入模型名称" v-model="assModelDia.modelName"></el-input>
                </el-col>
                <el-col :span="12">
                    <el-form-item>
                        <el-button round class="searchBtn" @click="listModel">搜索</el-button>
                        <el-button round class="searchBtn" @click="resetModelCondition">重置</el-button>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div class="confirmAddBtn"><el-button round type="primary" class="connectModel" @click="confirmAdd">确认添加</el-button></div>
        <el-table :data="modelListData" 
             v-loading="modelListLoading" 
             height="500"  
             ref="multipleTable"    
             element-loading-text="拼命加载中"
             element-loading-spinner="el-icon-loading"
             element-loading-background="rgba(255, 255, 255, 0.8)" 
             @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column property="thumbPicPath" label="模型缩略图">
                <template slot-scope="scope">
                    <img :src="BASE_URL.sourceBaseUrl+scope.row.thumbPicPath" width="90" height="90" alt="">
                </template>
            </el-table-column>
            <el-table-column prop="modelCode" label="模型编号"></el-table-column>
            <el-table-column prop="modelName" label="模型名称"></el-table-column>
            <el-table-column prop="categoryNames" label="模型分类"></el-table-column>
            <el-table-column label="模型规格" width="80">
                <template slot-scope="scope">
                    长：{{scope.row.length}}宽：{{scope.row.width}}高：{{scope.row.height}}
                </template>
            </el-table-column>
            <el-table-column prop="modelModelNum" label="模型型号"></el-table-column>
            <!--<el-table-column label="操作">-->
            <!--<template slot-scope="scope">-->
            <!--<el-button type="primary" @click="modelSelectUse(scope.row)">选用</el-button>-->
            <!--</template>-->
            <!--</el-table-column>-->
        </el-table>
        <div class="pagePagination">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="assModelDia.page"
                    :page-sizes="[10, 50, 100, 200, 500]"
                    :page-size="assModelDia.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalPage">
            </el-pagination>
        </div>

    </el-dialog>
</template>

<script>
    import {mapGetters} from 'Vuex'

    export default {
        name: "addHardProduct",
        data(){
            return{
                modelListLoading:true,
                dialogModelVisible:false,
                modelListData:'',
                assModelDia:{
                    page:1,
                    limit: 10,
                    isUsed: 0,
                    companyId: sessionStorage.getItem('companyID'),
                    transStatus: 'SUCCESS',
                    chooseModelFlag:true,
                    modelCode:'',
                    modelModelNum:'',
                    modelName:'',
                    existModelIds:''
                },
                modelList:[],
                currentPage:1,
                totalPage:20,
            }
        },
        computed:{
            ...mapGetters(['gettersHardModelList'])
        },
        methods:{
            handleSizeChange(val){
                this.assModelDia.page=1;
                this.assModelDia.limit=val;
                this.listModel();
            },
            handleCurrentChange(val){
                this.assModelDia.page=val;
                this.listModel();
            },
            resetSearchModel(){
                this.dialogModelVisible=false;
                this.assModelDia.modelCode='';
                this.assModelDia.modelModelNum='';
                this.assModelDia.modelName='';
                this.modelList=[];
            },
            showModelList(modelType, existModelIds, usedModelIds) {
                modelType ? this.assModelDia.modelType =modelType : this.assModelDia.modelType='';
                existModelIds ? this.assModelDia.existModelIds=existModelIds.join(',') : this.assModelDia.existModelIds='';
                usedModelIds ? this.assModelDia.usedModelIds = usedModelIds.join(',') : this.assModelDia.usedModelIds = '';
                this.listModel();
            },
            listModel(){
                this.modelListLoading = true;
                this.API.ListModel(this.assModelDia).then((res) => {
                    if(res.code==200){
                        this.modelListLoading = false;
                        this.modelListData = res.list;
                        this.totalPage=res.total;
                        // this.$nextTick(()=>{
                        //     this.totalPage=Math.ceil(res.total/this.assModelDia.limit);
                        //     console.log(this.totalPage)
                        // })
                    }else if(res.code==204){
                        this.modelListLoading = false;
                        this.modelListData = [];
                        this.totalPage=0;
                    }else {
                        this.modelListLoading = false;
                    }
                })
            },
            isShow(type){
                type==true ?  this.dialogModelVisible=true: this.dialogModelVisible=false
            },
            resetModelCondition(){
                this.assModelDia.modelCode='';
                this.assModelDia.modelModelNum='';
                this.assModelDia.modelName='';
                this.listModel();
            },
            handleSelectionChange(val){
                this.modelList=val;
            },
            confirmAdd(){ //确认添加模型
                let flag=0;
                this.modelList=[...this.gettersHardModelList,...this.modelList];
                //console.log(this.modelList)
                this.modelList.map((item,index)=>{
                    item.mainModelFlag==1 ? item.mainProductFlag=true: '';
                    item.hasChecked=false;
                    item.active=false;
                });
                // console.log(this.modelList)
                this.modelList.some(res=>{return res.mainProductFlag}) ? flag=0 : flag=1;  //排序 有主模型的放入第一位
                if(flag==0){
                    this.modelList.sort((a,b)=>{
                        var asort = a.mainProductFlag ? 1 : 0;
                        var bsort = b.mainProductFlag ? 1 : 0;
                        // console.log(bsort-asort)
                        return bsort-asort
                    })
                }
                this.$store.dispatch('setHardModel',this.modelList);
                this.dialogModelVisible=false;
                console.log(this.modelList)
                this.$store.commit('AddShow',this);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .textureSearch {
        .el-col {
            margin-bottom: 15px;
        }
        .el-input, .el-select {
            width: 100%;
        }
    }
    .confirmAddBtn{
        height: 50px;
        position: relative;
        .connectModel{
            position: absolute;
            right: 0;
        }
    }
    .pagePagination{
        text-align: center;
        padding-top: 20px;
    }

</style>
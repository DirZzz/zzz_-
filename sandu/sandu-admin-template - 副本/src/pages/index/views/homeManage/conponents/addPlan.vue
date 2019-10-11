<template>
    <el-dialog
            :visible.sync="addplanDialog"
            width="990px"
            class="addPlan"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :before-close="handleClose">
        <div slot="title" class="title">{{title}}<em v-if="submitPlansList.length>0">（已选够{{submitPlansList.length}}个）</em></div>
        <el-row>
            <el-col :span="6">
                <el-select v-model="module.origin" placeholder="请选择方案来源" @change="search" clearable>
                    <el-option
                            v-for="(item,index) in planOrigin"
                            :key="index"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="6">
                <el-select v-model="module.currentBrand" placeholder="请选择品牌" @change="search" clearable>
                    <el-option
                            v-for="(item,index) in brandList"
                            :key="index"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="6">
                <el-select v-model="module.currentSpace" placeholder=" 请选择空间类型" @change="search" clearable>
                    <el-option
                            v-for="(item,index) in spaceList"
                            :key="index"
                            :label="item.name"
                            :value="item.name">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="6">
                <el-select v-model="module.chargeType" placeholder=" 请选择是否免费" @change="search" clearable>
                    <el-option
                            v-for="(item,index) in freeBox"
                            :key="index"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-col>
        </el-row>
        <el-row style="margin: 15px 0 20px 0">
            <el-col :span="6">
                <el-input style="width: 217px" v-model="module.plansName" placeholder="请输入方案名称" clearable></el-input>
            </el-col>
            <el-col :span="2">
                <el-button type="text" class="left" @click="search">搜索</el-button>
            </el-col>
            <el-col :span="4">
                <el-button type="text" class="cleanleft" @click="cleanSelect">重置筛选</el-button>
            </el-col>
        </el-row>
        <el-table
                :data="plansList"
                style="width: 100%">
            <el-table-column
                    type="index"
                    label="编号"
                    width="70">
            </el-table-column>
            <el-table-column
                    label="方案"
                    label-class-name="wordLeft"
                    width="380">
                <template slot-scope="scope">
                    <div class="goods-content">
                        <div class="goods-img">
                            <img :src="BASE_URL.sourceBaseUrl+scope.row.picPath" alt="">
                            <i :class="{'ic-select-active':scope.row.active}"></i>
                        </div>
                        <div class="goods-text">
                            <el-tooltip class="item" effect="dark" :content="scope.row.planName" placement="top" v-if="scope.row.planName.length>30">
                                <p>{{scope.row.planName}}</p>
                            </el-tooltip>
                            <p v-else>{{scope.row.planName}}</p>
                            <p>{{scope.row.designStyleName}} </p>
                            <p @click="look720(scope.row)"><i class="ic_look720"></i>查看720</p>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="brandName"
                    width="140"
                    label="品牌">
            </el-table-column>
            <el-table-column
                    prop="planPrice"
                    width="120"
                    label="版权费（度币）">
                <template slot-scope="scope">
                    <div v-if="scope.row.chargeType==0">免费</div>
                    <div v-else>{{scope.row.planPrice}}</div>
                </template>
            </el-table-column>
            <el-table-column
                    width="100"
                    label="来源">
                <template slot-scope="scope">
                    <div v-if="scope.row.origin=='diy'">内部制作</div>
                    <div v-else-if="scope.row.origin=='share'">分享方案</div>
                    <div v-else-if="scope.row.origin=='deliver'">企业交付</div>
                </template>
            </el-table-column>
            <el-table-column
                    label="操作">
                <template slot-scope="scope">
                    <div class="addBtn" @click="addGoodList(scope.row,scope.$index)" v-if="!scope.row.active">添加</div>
                    <div class="addBtn" @click="cancelGoodList(scope.row,scope.$index)" v-else>取消</div>
                </template>
            </el-table-column>
        </el-table>
        <div class="pageFrame">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-size="5"
                    layout="total,prev, pager, next, jumper"
                    :total="planTotalPage">
            </el-pagination>
        </div>

    </el-dialog>
</template>

<script>
    import { mapState,mapActions } from 'Vuex'
    export default {
        name: "addPlan",
        data(){
            return{
                title:'添加方案',
                currentPage:1,
                goodsCode:'',
                totalPage:'',
                planOrigin:[
                    {value:'deliver',label:'企业交付'},
                    {value:'diy',label:'内部制作'},
                    {value:'share',label:'分享方案'}],
                freeBox:[
                    {value:'0',label:'是' },
                    {value:'1',label:'否' }
                ],
                module:{
                    plansName:'',
                    chargeType:'',
                    currentSpace:'',
                    currentBrand:'',
                    origin:'',
                }

            }
        },
        computed:{
            ...mapState('homeManage',['addplanDialog','submitPlansList','plansList','planTotalPage','brandList','spaceList','isReplace','moduleType'])
        },
        created(){

        },
        methods:{
            ...mapActions('homeManage',['planDialog','setPlansList','setSubmitPlansList','setReplace']),
            handleCurrentChange(val){
                this.setPlansList({
                    vm:this,
                    page: val,
                    spaceCommonName:this.module.currentSpace,
                    brandId:this.module.currentBrand,
                    planName:this.module.plansName,
                    origin:this.module.origin,
                    chargeType:this.module.chargeType
                })
            },
            handleClose(){
                for (let key in this.module) {
                    this.module[key]='';
                }
                this.planDialog(false);
                this.currentPage=1;
                this.setPlansList({ vm:this,page:1,});
                this.setReplace({index:'',isReplace:false}); //清除替换
            },
            addGoodList(res,index){  //添加
                res.picAddress=res.picPath;
                if(this.isReplace){ //替换
                    this.setSubmitPlansList({res:res,type:2});
                    //this.$message.success('替换成功');
                    this.handleClose();
                }else { //添加
                    if(this.submitPlansList.length<10){
                        res.active=true;
                        Object.assign(this.plansList[index],res);
                        this.$set(this.plansList,index,this.plansList[index]);
                        this.setSubmitPlansList({res:res,type:1});//添加到新的数组 1添加
                    }else {
                        this.$message.error('已达到上限');
                    }
                }
            },
            cancelGoodList(res,index){ //取消
                res.active=false;
                Object.assign(this.plansList[index],res);
                this.setSubmitPlansList({res:res,type:0});//从新的数组里删除 0删除
            },
            search(){
                console.log(this.module.chargeType,'this.module.chargeType')
               this.setPlansList({
                   vm:this,
                   page: 1,
                   spaceCommonName:this.module.currentSpace,
                   brandId:this.module.currentBrand,
                   planName:this.module.plansName,
                   origin:this.module.origin,
                   chargeType:this.module.chargeType
               })
            },
            cleanSelect(){
                for (let key in this.module) {
                    this.module[key]='';
                }
                this.currentPage=1;
                this.setPlansList({ vm:this,page:1,});
            },
            look720(item){
                this.API.reViewPlanImges({planId:item.planId}).then(res=>{
                    window.open(res.data.vr720Single);
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    .addPlan{
        .title{
            em{
                color: #ff2323;
                font-style: normal;
            }
        }
        .pageFrame{
            text-align: right;
            margin-top: 20px;
        }
        .goods-content{
            display: flex;
            .goods-img{
                position: relative;
                width: 107px;
                height: 60px;
                img{
                    width: 107px;
                    height: 60px;
                }
                .ic-select-active{
                    position: absolute;
                    right: 0;
                    bottom: 0;
                    display: inline-block;
                    width: 20px;
                    height: 20px;
                    background: no-repeat center url("../../../assets/images/select_active.png");
                    background-size: 100%;
                }
            }

            .goods-text{
                color: #999;
                margin-left: 20px;
                p{
                    text-align: left;
                }
                p:nth-of-type(1){
                    width: 420px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    cursor: pointer;
                }
                p:nth-of-type(3){
                    color: #3970e2;
                    font-size: 14px;
                    cursor: pointer;
                    &:active{
                        opacity: .6;
                    }
                }
                .ic_look720{
                    display: inline-block;
                    width: 15px;
                    height: 15px;
                    background: no-repeat center url('../../../assets/images/chakan.png');
                    background-size: 94%;
                    vertical-align: sub;
                    margin-right: 3px;
                }
            }
        }
        .addBtn{
            color: #FF6419;
            font-size: 14px;
            cursor: pointer;
        }
    }
</style>
<style lang="scss">
    .addPlan{
        .wordLeft{
            text-align: left!important;
        }
    }

</style>

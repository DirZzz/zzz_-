<template>
    <div>
        <!--<v-nav :nav="nav"></v-nav>-->
        <div class="detailFrame">
            <div class="headTitle">详情</div>
            <div>
                <el-row :gutter="24" class="splineFrame">
                    <el-col :span="2" class="productBaseInfo">基本信息</el-col>
                    <el-col :span="22" class="rightLine"><hr></el-col>
                </el-row>
            </div>
            <div>
                <el-form :model="ruleForm" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                       <el-form-item label="店铺名称：" prop="shopName">
                           {{ruleForm.shopName}}
                        </el-form-item>   

                       <el-form-item label="所属企业：" prop="companyName">
                           {{ruleForm.companyName}}
                       </el-form-item>

                    <el-form-item label="区域信息：" prop="companyName">
                        {{ruleForm.areaInfoName }}
                    </el-form-item>

                    <el-form-item label="用户类型：" prop="companyName">
                        {{ruleForm.userTypeStr }}
                    </el-form-item>
                       
                        <el-form-item label="店铺logo：" prop="coverPicPaths">
                            <span  v-for = "(item, index) in ruleForm.coverPicPaths" :key="index">
                                <img width="300" :src="imgurl+item" />
                            </span>
                        </el-form-item>
                            
                        <el-form-item label="封面图片：" prop="coverResPaths">
                             <span  v-for = "(item, index) in ruleForm.coverResPaths" :key="index">
                                <img width="300" :src="imgurl+item" />
                            </span>
                        </el-form-item>
                        

                    <div class="splitLine"></div>
                    <el-form-item>
                        <el-button type="primary" class="confirmBtn" round @click="goBack()">返回</el-button>
                    </el-form-item>
                </el-form>             
                
            </div>
        </div>
    </div>
</template>

<script>

    export default {
        data(){
            return {
                imgurl:process.env.sourceBaseUrl,
                name:'',
                nav:[
                    {nav: '客户管理列表', url: '/template'},
                    {nav: '详情', url: ''}
                ],
                ruleForm: {}
            }
        },
        beforeRouteEnter (to, from, next) {
            next( vm => {
                vm.nav[0].url = from.path
            })
        },
        created(){
            this.API.getcompanyshopById({
                companyshopId:this.$route.params.companyshopId
            }).then(res=>{
                this.ruleForm = res.data;
            });
        },
        methods:{
            goBack(){
                this.$router.push("/storeManage/list");
            }
        },

    }
</script>

<style scoped lang="scss">
    .demo-ruleForm{
        .el-form-item{
            text-align: left;
        }
    }
    .detailFrame{
        background: #fff;
        padding: 20px;
        margin: 20px;
    }
    .headTitle{
        border-bottom: solid 1px #ddd;
        font-size: 20px;
        text-align: left;
        font-weight: normal;
        color: #666666;
        padding-bottom: 20px;
    }
    .baseInfo{
        padding-left: 40px;
        li{
            display: inline-block;
            width: 30%;
            margin-bottom: 25px;
        }
    }
    .genjin{
        li{
            margin-bottom: 25px;
        }
      span{
          display: inline-block;
          width: 120px;
          text-align: right;

      }
    }
</style>
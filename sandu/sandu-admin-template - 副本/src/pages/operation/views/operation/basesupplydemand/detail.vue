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
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="供求信息：" prop="type">
                                    {{ ruleForm.type== 1 ? '供应' : '需求'}}
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="户型：">
                                    {{ ruleForm.houseInfo | filter}}
                                </el-form-item>
                            </el-col>
                        </el-row>
                       <el-row :gutter="24">
                           <el-col :span="12">
                               <el-form-item label="发布者：" prop="creator">
                                   {{ruleForm.creator}}
                               </el-form-item>
                           </el-col>
                           <el-col :span="12">
                               <el-form-item label="方案：" prop="creator">
                                   {{ruleForm.planInfo | filter}}
                               </el-form-item>
                           </el-col>
                       </el-row>

                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="类别：" prop="supplyDemandCategoryName">
                                    {{ruleForm.supplyDemandCategoryName}}
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="图片：">
                                    <div class="showImg">
                                        <div class="imgBox" v-for="(item,index) in ruleForm.coverPicPaths" :key="index">
                                            <div class="active">
                                                <div class="btns" @click="lookBigpic(item)">查看大图</div>
                                            </div>
                                            <img :src="BASE_URL.sourceBaseUrl+item" class="avatar" >
                                        </div>
                                    </div>
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <el-form-item label="区域：" class="areaMarginRight">
                            {{ruleForm.provinceName}}{{ruleForm.cityName}}{{ruleForm.districtName}}{{ruleForm.streetName}}
                        </el-form-item>
                        <el-form-item label="地址：" prop="address">
                            {{ruleForm.address}}
                        </el-form-item>

                        <el-row :gutter="24">
                             <el-col :span="12">
                                 <el-form-item label="信息标题：" prop="title">
                                     {{ruleForm.title}}
                                 </el-form-item>
                             </el-col>
                            <el-col :span="12">
                                <el-form-item label="浏览量（假）：" prop="title">
                                    {{ruleForm.virtualViewCount || 0}}
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="信息描述：" prop="description">
                                    {{ruleForm.description}}
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="点赞数（假）：" prop="description">
                                    {{ruleForm.virtualLikeCount || 0}}
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="12">
                                <el-form-item label="浏览量(真)：" prop="description">
                                    {{ruleForm.viewCount || 0}}
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="点赞数(真)：" prop="description">
                                    {{ruleForm.likeCount || 0}}
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <!--<el-form-item label="封面图片：" prop="coverPicPaths">-->
                           <!---->
                            <!--<div class="coverImg">-->
                                <!--<img width="300" :src="imgurl+item" v-for = "(item, index) in ruleForm.coverPicPaths" :key="index"/>-->
                            <!--</div>-->
                        <!--</el-form-item>-->


                        <div class="splitLine"></div>
                        <el-form-item>
                            <el-button type="primary" class="confirmBtn" round @click="goBack()">返回</el-button>
                        </el-form-item>
                </el-form>
                <showBigImg :flag="showBigImgflag" :obj="bigImgList" :indexActive="0"  @closeImg="closeImg" :arrow="'never'"></showBigImg>
            </div>
        </div>
    </div>
</template>

<script>
    import showBigImg from '../../feedback/conponents/showBigImg'
    export default {
        components:{
            showBigImg
        },
        data(){
            return {
                imgurl:process.env.sourceBaseUrl,
                nav:[
                    {nav: '客户管理列表', url: '/template'},
                    {nav: '详情', url: ''}
                ],
                ruleForm: {},
                showBigImgflag:false,
                falseFlag:true,
                bigImgList:[]
            }
        },
        filters:{
            filter(obj){
                let arr=[];
                for (let key in obj){
                    arr.push(obj[key])
                }
                return arr.join(',')
            }
        },
        beforeRouteEnter (to, from, next) {
            next( vm => {
                vm.nav[0].url = from.path
            })
        },
        created(){
            this.getDetials()
        },
        methods:{
            lookBigpic(item){
                this.bigImgList=[];
                this.showBigImgflag=true;
                this.bigImgList.push(item);
            },
            closeImg(){
                this.showBigImgflag=false;
            },
            getDetials(){
                this.API.getBasesupplydemandById({
                    basesupplydemandId: this.$route.params.basesupplydemandId
                }).then(res=>{
                    this.ruleForm = res.data;
                    console.log(this.ruleForm.coverPicPaths)
                });
            },

            goBack(){
                this.$router.push("/basesupplydemand/list");
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
        margin-bottom: 20px;
        font-size: 20px;
        font-weight: normal;
        text-align: left;
        color: #666666;
        padding-bottom: 20px;
    }
    .coverImg{
       display: flex;
        flex-wrap: wrap;
       img{
           width: 200px;
           height: 200px;
           object-fit: contain;
           margin-right: 10px;
       }
    }
    .showImg{
        display: flex;
        img{
            width: 120px;
            height: 120px;
            border-radius: 6px;
            object-fit: contain;
        }
        .imgBox{
            position: relative;
            width: 120px;
            height: 120px;
            margin-right: 10px;
            &:hover .active{
                opacity: 1;
            }
        }
        .active{
            width: 100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
            background: rgba(0,0,0,0.7);
            opacity: 0;
            transition-duration: .3s;
            .btns{
                width: 72px;
                height: 24px;
                text-align: center;
                line-height: 24px;
                cursor: pointer;
                color: #fff;
                font-size: 12px;
                position: absolute;
                left: 50%;
                top: 50%;
                margin-left: -36px;
                margin-top: -12px;
                background-color: #3399ff;
                border-radius: 12px;
            }
        }
    }
    .flex-wrap{
        display: flex;
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
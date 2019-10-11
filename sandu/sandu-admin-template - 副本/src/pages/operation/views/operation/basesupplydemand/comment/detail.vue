<template>
    <div class="detail">
        <div class="nav">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/basesupplydemand/comment/list',query:{businessId:$route.query.businessId} }">评论管理</el-breadcrumb-item>
                <el-breadcrumb-item>详情</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="content">
            <el-form ref="ruleForm" label-width="120px" class="demo-ruleForm">
                <el-form-item label="发布者：" style="text-align: left">
                    <span>{{detailData.creator}}</span>
                </el-form-item>
                <el-form-item label="评论时间：" style="text-align: left">
                   <span>{{detailData.gmtCreate}}</span>
                </el-form-item>
                <el-form-item label="评论内容："  style="text-align: left">
                    <span>{{detailData.reviewsMsg}}</span>
                </el-form-item>
                <el-form-item label="方案：" style="text-align: left">
                    <span>{{detailData.planName || '无'}}</span>
                </el-form-item>
                <el-form-item label="户型：" style="text-align: left">
                    <span>{{detailData.houseName || '无'}}</span>
                </el-form-item>
                <el-form-item label="图片：">
                    <div class="showImg">
                        <div class="imgBox" v-for="(item,index) in imageUrl" :key="index">
                            <div class="active">
                                <div class="btns" @click="lookBigpic(item)">查看大图</div>
                            </div>
                            <img :src="BASE_URL.sourceBaseUrl+item.url" class="avatar" >
                        </div>
                    </div>
                </el-form-item>

                <el-form-item label="点赞数（真）：" style="text-align: left">
                    <span>{{detailData.likeCount || 0}}</span>
                </el-form-item>
                <el-form-item label="点赞数（假）：" style="text-align: left">
                    <span>{{detailData.likeVirtualCount || 0}}</span>
                </el-form-item>
            </el-form>
        </div>
        <showBigImg :flag="showBigImgflag" :obj="bigImgList" :indexActive="0"  @closeImg="closeImg" :arrow="'never'"></showBigImg>
    </div>

</template>

<script>
    import showBigImg from '../../../feedback/conponents/showBigImg'
    export default {
        components:{
            showBigImg
        },
        name: "detail",
        data(){
            return{
                imageUrl:[],
                bigImgList:[],
                detailData:'',
                showBigImgflag:false
            }
        },
        created(){
            this.API.demandDetail({id:this.$route.query.reviewsId}).then(res=>{
                if(res.success){
                    this.detailData=res.data;
                    for (let key in res.data.picMap) {
                        this.imageUrl.push({
                            url:res.data.picMap[key],
                            id:key
                        })
                    }
                }else {
                    this.$message.error(res.message);
                }
            })
        },
        methods:{
            closeImg(){
                this.showBigImgflag=false;
            },
            lookBigpic(item){
                this.bigImgList=[];
                this.showBigImgflag=true;
                this.bigImgList.push(item.url);
            }
        }
    }
</script>

<style scoped lang="scss">
    .detail{
        margin: 20px;
    }
    .nav {
        background: #fff;
        height: 58px;
        text-align: left;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        position: relative;
        border-bottom: solid 1px #ddd;
        padding-left: 16px;
    }
    .content{
        background-color: #fff;
    }
    .demo-ruleForm{
        margin-top: 20px;
    }
    .showImg{
        display: flex;
        flex-wrap: wrap;
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
            margin-bottom: 10px;
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
</style>
<style lang="scss">
    .detail{
        .nav{
            .el-breadcrumb{
                line-height: 58px;
            }
        }
    }
</style>
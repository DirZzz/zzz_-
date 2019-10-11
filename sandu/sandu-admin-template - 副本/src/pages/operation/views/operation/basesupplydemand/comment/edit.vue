<template>
    <div class="edit">
        <div class="nav">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/basesupplydemand/comment/list',query:{businessId:$route.query.businessId} }">评论管理</el-breadcrumb-item>
                <el-breadcrumb-item>编辑</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="content">
            <el-form :model="ruleForm" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                <el-form-item label="发布者：" style="text-align: left">
                    <el-input class="formWidth" type="text"  :disabled="true" clearable v-model.trim="ruleForm.creator" placeholder="请输入内容"></el-input>
                </el-form-item>
                <el-form-item label="评论时间：" style="text-align: left">
                    <el-date-picker
                            v-model="pickTime"
                            type="datetime"
                            :disabled="true"
                            value-format="yyyy-MM-dd:HH-mm-ss"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="评论内容：" style="text-align: left">
                    <el-input class="formWidth" type="textarea" clearable v-model.trim="ruleForm.content" maxlength="500" placeholder="请输入内容"></el-input>
                </el-form-item>

                <el-form-item label="方案：" class="planOption">
                    <el-tag
                            v-for="tag in planList"
                            :key="tag.name"
                            @close="handleClose(tag)"
                            closable>
                        {{tag.name}}
                    </el-tag>
                </el-form-item>
                <el-form-item label="户型：" class="planOption">
                    <el-tag
                            v-for="tag in houseList"
                            :key="tag.label"
                            @close="houseClose(tag)"
                            closable>
                        {{tag.label}}
                    </el-tag>
                </el-form-item>
                <!--<el-form-item label="图片：">-->
                    <!--<upload class="uploadBox">-->
                        <!--<div class="showImg">-->
                            <!--<img v-if="imageUrl" :src="imageUrl" class="avatar">-->
                            <!--<i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
                        <!--</div>-->
                    <!--</upload>-->
                <!--</el-form-item>-->
                <el-form-item label="图片：">
                    <div class="showImg">
                        <div class="imgBox" v-for="(item,index) in imageUrl" :key="index">
                            <div class="active">
                                <div class="btns" @click="lookBigpic(item)">查看大图</div>
                            </div>
                            <img :src="BASE_URL.sourceBaseUrl+item.url" class="avatar" >
                            <i class="ic_del" @click="deleteImg(index)"></i>
                        </div>
                    </div>
                </el-form-item>
                <el-form-item label="点赞数（真）：">
                    <el-input clearable :disabled="true" v-model="ruleForm.likeCount" placeholder="请输入内容"></el-input>
                </el-form-item>

                <el-form-item label="点赞数（假）：">
                    <input type="number" class="inputLike" v-model="ruleForm.likeVirtualCount" oninput="if(value.length>7) value=value.slice(0,7)" placeholder="请输入内容">
                    <!--<el-input class="formWidth" type="number" @mousewheel.native.prevent clearable v-model.trim="ruleForm.virtualLikeCount" oninput="if(value.length>7) value=value.slice(0,7)" placeholder="请输入内容"></el-input>-->
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" size="small" round @click="confirm">确定
                    </el-button>
                    <el-button round size="small" @click="$router.push({ path: '/basesupplydemand/comment/list',query:{businessId:$route.query.businessId}})">取消</el-button>
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
        name: "edit",
        data(){
            return{
                pickTime:'',
                imageUrl:[],
                ruleForm:{
                    content:'',
                    likeCount:'',
                    planName:'',
                    houseName:'',
                },
                currentPlan:[],
                planList:[],
                houseList:[],
                currentHouse:[],
                bigImgList:[],
                showBigImgflag:false
            }
        },
        created(){
            this.API.demandDetail({id:this.$route.query.reviewsId}).then(res=>{
                if(res.success){
                    this.ruleForm.content=res.data.reviewsMsg;
                    this.pickTime=res.data.gmtCreate;
                    this.ruleForm.houseName=res.data.houseName;
                    this.ruleForm.planName=res.data.planName;
                    this.ruleForm.creator=res.data.creator;
                    this.ruleForm.likeCount=res.data.likeCount || 0;
                    this.ruleForm.likeVirtualCount=res.data.likeVirtualCount || 0;

                    if(res.data.houseId){
                        this.currentHouse.push(res.data.houseId)
                        this.houseList.push({
                            label:res.data.houseName,
                            value: res.data.houseId
                        })
                    }
                    if(res.data.planId){
                        this.currentPlan.push(res.data.planId);
                        this.planList.push({
                            name:res.data.planName,
                            value: res.data.planId
                        })
                    }

                    if(res.data.picMap){
                        for (let key in res.data.picMap) {
                            this.imageUrl.push({
                                url:res.data.picMap[key],
                                id:key
                            })
                        }
                    }
                }else {
                    this.$message.error(res.message);
                }
            })
        },
        methods:{
            lookBigpic(item){
                this.bigImgList=[];
                this.showBigImgflag=true;
                this.bigImgList.push(item.url);
            },
            closeImg(){
                this.showBigImgflag=false;
            },
            handleClose(tag){
                let result=[];
                this.planList=this.planList.filter(res=>{ res.value!=tag.value  })
                this.planList && this.planList.map(res=>{ result.push(res.value) })
                result.length==0 ? result.push(0) : '';
                this.currentPlan=result;
            },
            houseClose(tag){
                let result=[];
                this.houseList=this.houseList.filter(res=>{ res.value!=tag.value  })
                this.houseList && this.houseList.map(res=>{ result.push(res.value) })
                result.length==0 ? result.push(0) : '';
                this.currentHouse=result;
            },
            confirm(){
                console.log(this.pickTime)
                let arr=[];
                this.imageUrl ? this.imageUrl.map(res=>{ arr.push(res.id) }) : '';
                this.API.demandEdit({
                    reviewsId:this.$route.query.reviewsId,
                    planId:this.currentPlan.join(',') || 0,
                    houseId:this.currentHouse.join(',') || 0,
                    content:this.ruleForm.content,
                    picIds:arr ? arr.join(',') : '',
                    likeCount:this.ruleForm.likeCount,
                    virtualLikeCount:this.ruleForm.likeVirtualCount || 0,
                }).then(res=>{
                   if(res.success){
                       this.$message.success('保存成功');
                       this.$router.push({path: '/basesupplydemand/comment/list',query:{businessId:this.$route.query.businessId}})
                   }else {
                       this.$message.error(res.message);
                   }
                })
            },
            deleteImg(index){
                this.imageUrl.splice(index,1);
            }
        }
    }
</script>

<style scoped lang="scss">
    .edit{
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
    .uploadBox{
        display: flex;
        align-items: center;
    }
    /*.showImg{*/
        /*width: 120px;*/
        /*height: 120px;*/
        /*border: 1px dashed #d9d9d9;*/
        /*border-radius: 6px;*/
        /*cursor: pointer;*/
    /*}*/
    .imgBox{
        position: relative;
        width: 120px;
        height: 120px;
        margin-right: 10px;
        margin-bottom: 10px;
        &:hover .active{
            opacity: 1;
        }
        .ic_del{
            display: block;
            position: absolute;
            width: 16px;
            height: 16px;
            background: no-repeat center url("../../../../assets/images/icon/ic_delete.png");
            background-size: 100%;
            right: 5px;
            top: 5px;
            cursor: pointer;
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
    .showImg{
        display: flex;
        flex-wrap: wrap;
        img{
            width: 120px;
            height: 120px;
            border-radius: 6px;
        }
    }
    .inputLike{
        background-color: #fff;
        background-image: none;
        border-radius: 4px;
        border: 1px solid #dcdfe6;
        box-sizing: border-box;
        color: #606266;
        display: inline-block;
        height: 40px;
        line-height: 40px;
        padding: 0 15px;
        width: 100%;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 120px;
        height: 120px;
        line-height: 120px;
        text-align: center;
    }
    .formWidth{
        width: 600px;
    }
</style>
<style lang="scss">
    .edit{
        .nav{
            .el-breadcrumb{
                line-height: 58px;
            }
        }
        .planOption{
            text-align: left;

        }
        .el-input .el-input--suffix{
            width: 600px;
        }

    }
</style>
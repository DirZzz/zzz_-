
<template>
<div class="main">
    <div class="mainTile">
        <div class="items" @click="handleClick">意见反馈</div>
        <div class="lines"></div>
        <div class="items active">我的反馈</div>
    </div>
    <div class="mainBody">
        <div class="listItems" v-for="(item,index) in feedbackData" :key="index">
            <!--未回复-->
            <p class="states indent oranges" v-if='item.dealStatus==0'>【状态】：待回复</p>
            <!--已回复-->
            <p class="states indent" v-if='item.dealStatus==1'>【状态】：已回复</p>
            <p class="backTimes" v-if='item.dealStatus==1'>反馈回复<span>{{item.gmtReply}}</span></p>
            <p class="backCont" v-if='item.dealStatus==1'>{{item.replyContent}}</p>
            <div class="feedbRows">
                <p>【反馈时间】：{{item.gmtCreate}}</p>
                <p class="cont">【问题描述】：{{item.feedbackContent}}</p>
                <p v-if="item.feedbackPicPath.length>0">【问题截图】：</p>
                <div class="imgRows" v-if="item.feedbackPicPath.length>0">
                    <div v-for="(imgItem,jndex) in item.feedbackPicPath" class="imgItems" :key="jndex">
                        <div class="zzcs">
                            <div class="btns" @click="showImgFuns(index,jndex)">查看大图</div>
                        </div>
                        <img :src="sourceBaseUrl+imgItem.resPath" alt="">
                    </div>
                    <!--<div v-if="item.feedbackPicPath.length<4" v-for="(hhh) in (4-item.feedbackPicPath.length)" class="imgItems">
                        <img src="./default.png">
                    </div>-->
                </div>
            </div>
            <div  class="comment">
                <span v-if="item.dealStatus===1 && item.estimate !==0 && item.estimate !==1" class="oranges">点评</span><!--未点评样式-->
                <span v-if="item.dealStatus===1 && (item.estimate ===0 ||item.estimate ===1)">点评：</span><!--已点评样式-->
                <span v-if="item.dealStatus===1 && item.estimate !==0 && item.estimate !==1" class="spanBtns" @click="commentFun(0,item.feedbackId)">满意</span>
                <span v-if="item.dealStatus===1 && item.estimate !==0 && item.estimate !==1" class="spanBtns" @click="commentFun(1,item.feedbackId)">不满意</span>
                <span class="oranges" v-if="item.dealStatus===1 &&(item.estimate===0||item.estimate===1)">{{item.estimate===0?'满意':'不满意'}}</span>
            </div>
        </div>
        <div class="noMore" v-if= " feedbackData.length===0 ">- 暂无数据 -</div>
        <div class="noMore" v-if= " feedbackData.length>=10 ">- 只显示最近十条信息 -</div>
    </div>
    <showBigImg :flag="showBigImgflag" :obj="bigImgList" :indexActive="showBigImgIndex" :falseFlag="falseFlag" @closeImg="closeImg"></showBigImg>
  </div>
</template>

<script>
import showBigImg from './conponents/showBigImg';//查看大图
  export default {
    data () {
      return {
          getUserId:sessionStorage.getItem('id'),
          feedbackId:'',      //反馈Id
          feedbackContent:'', //反馈内容
          feedbackTimes:'',   //反馈时间
          feedbackImg:[],     //反馈图片
          dealStatus:'',      //处理结果
          replyTimes:'',      //处理时间
          replyContent:'',    //处理内容
          estimateResult:'',  //评价结果
          feedbackData:[],    //反馈列表数据

          showBigImgflag: false,
          bigImgList:[],
          showBigImgIndex:0,
          falseFlag: false,
          sourceBaseUrl:'',


      }
    },
    components:{
        showBigImg
    },


    created () {
        this.getUserId = sessionStorage.getItem('id');
        this.getFeedbackList();
        this.sourceBaseUrl = this.BASE_URL.sourceBaseUrl;

    },
    mounted () {
    },

    methods: {

        /*我的反馈列表*/
        getFeedbackList() {
            this.API.myFeedbackList({userId:this.getUserId}).then((res)=>{
                if(res.success){
                    this.feedbackData = res.datalist;
                }
            });
        },

        /*意见反馈*/
        handleClick() {
            this.$router.push('/feedback');
        },

        showImgFuns(index,jndex){
            let imgList = [];
            this.feedbackData[index].feedbackPicPath.map(v=>{
                imgList.push(this.sourceBaseUrl+v.resPath);
            })
            this.bigImgList = imgList
            this.showBigImgIndex = jndex
            this.showBigImgflag = true
        },
        closeImg(){
            this.showBigImgflag = false
        },


        //答复结果评论
        commentFun(type,id){
            console.log(type,id);
            this.API.feedbackEstimate({
                id: id,
                estimate: type
            }).then(res => {
                if (res.success) {
                    this.$message({
                        type: 'success',
                        message: res.message
                    });
                    this.getFeedbackList()
                } else {
                    this.$message({
                        type: 'error',
                        message: res.message
                    });
                    this.getFeedbackList()
                }
            });
        },


    },
  }
</script>
<style lang="scss" scoped>

.oranges{
    color: #ff6419 !important;
}
.indent{
    text-indent: -6px;
}
.main{
    width: 100%;
    background: #fff;
    min-height: 100%;
    .noMore{
        height: 12px;
        font-size: 12px;
        line-height: 12px;
        color: #999999;
        text-align: center;
        margin-top: 5px;
    }
    .mainTile{
        width: 100%;
        height: 76px;
        border-bottom: 1px solid #dddddd;
        .items{
            padding: 0 20px;
            font-size: 18px;
            float: left;
            color: #333333;
            height: 100%;
            line-height: 76px;
            cursor: pointer;
            position: relative;
        }
        .items.active{
            color: #ff6419;
        }
        .lines{
            width: 21px;
            height: 100%;
            position: relative;
            float: left;
            &::after{
                content: '';
                width: 1px;
                position: absolute;
                height: 18px;
                left: 50%;
                top: 50%;
                margin-left: -.5px;
                margin-top: -9px;
                background-color: #dddddd;
            }
        }

    }
    .mainBody{
        width: 100%;
        padding: 36px 40px;
        box-sizing: border-box;
        .listItems{
            border-bottom: 1px solid #eaeaea;
            margin-bottom: 30px;
            p{
                line-height: 14px;
                font-size: 14px;
                color: #333333;
            }
            .backTimes{
                margin-bottom: 12px;
                span{
                    font-size: 12px;
                    color: #999999;
                    margin-left: 10px;
                }
            }
            .backCont{
                margin-bottom: 16px;
                line-height: 20px;
            }
            .states{
                color: #999999;
                margin-bottom: 20px;
            }
            .comment{
                height: 24px;
                font-size: 14px;
                line-height: 14px;
                margin-bottom: 30px;
                span{
                    display: block;
                    float: left;
                    margin-right: 10px;
                    line-height: 24px;
                }
                .spanBtns{
                    width: 64px;
                    height: 24px;
                    background-color: #ffffff;
                    border-radius: 12px;
                    border: solid 1px #e2e2e2;
                    cursor: pointer;
                    text-align: center;
                }
            }
            .feedbRows{
                width: 100%;
                box-sizing: border-box;
                background-color: #f8f8f8;
                padding: 20px;
                margin-bottom: 20px;
                p{
                    margin-bottom: 10px;
                    text-indent: -8px;
                }
                .cont{
                    line-height: 20px;
                }
                .imgRows{
                    height: 106px;
                    .imgItems{
                        width: 106px;
	                    height: 106px;
                        border-radius: 2px;
                        position: relative;
                        float: left;
                        margin-right: 6px;
                        img{
                            width: 100%;
                            height: 100%;
                        }
                        .zzcs{
                            position: absolute;
                            left: 0;
                            width: 100%;
                            height: 100%;
                            top: 0;
                            background: rgba(0,0,0,0.7);
                            opacity: 0;
                            transition-duration: .3S;
                            .btns{
                                position: absolute;
                                left: 50%;
                                top: 50%;
                                margin-left: -36px;
                                margin-top: -12px;
                                width: 72px;
                                height: 24px;
                                background-color: #ff6419;
                                border-radius: 12px;
                                font-size: 12px;
                                line-height: 24px;
                                text-align: center;
                                color: #ffffff;
                                cursor: pointer;
                            }
                        }
                        &:hover .zzcs{
                            opacity: 1;
                        }
                    }
                }
            }
        }

    }

}
</style>

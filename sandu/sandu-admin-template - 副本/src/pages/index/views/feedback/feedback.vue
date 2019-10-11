
<template>
<div class="main">
    <div class="mainTile">
        <div class="items active">意见反馈</div>
        <div class="lines"></div>
        <div class="items" @click="handleClick">我的反馈<span v-if="idR" class="idR"></span></div>
    </div>
    <div class="mainBody">
        <p class="mess"><span class="reds">*</span>为必填</p>
        <div class="formRows">
            <div class="rowTxt"><span class="reds">*</span>问题描述:</div>
            <div class="rowInp">
                <textarea placeholder="请输入" maxlength="200" v-model="describe"></textarea>
                <span class="counts">{{counts}}/200</span>
            </div>
            <template v-if="error">
                <el-alert :title="errorTxt" type="error" @close="closeToast"></el-alert>
            </template>
        </div>
        <div class="formRows">
            <div class="rowTxt">问题截图:</div>
            <div class="rowImg">
                <div class="itemsImg">
                    <el-upload class="avatar-uploader" :action='BASE_URL.uploadUrl'
                    :headers="headerToken" :data="uploadParams" :show-file-list="false"
                    :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                    <img v-if="imgList[0].src" :src="imgList[0].src" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <div class="zzcImg" v-if="imgList[0].src">
                        <span class="showImg" @click="showImgFun(0)">查看大图</span>
                        <span class="deleImg" @click="delImgFun(0)">删除</span>
                    </div>
                </div>
                <div class="itemsImg">
                    <el-upload class="avatar-uploader" :action='BASE_URL.uploadUrl'
                    :headers="headerToken" :data="uploadParams" :show-file-list="false"
                    :on-success="handleAvatarSuccess1" :before-upload="beforeAvatarUpload">
                    <img v-if="imgList[1].src" :src="imgList[1].src" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <div class="zzcImg" v-if="imgList[1].src">
                        <span class="showImg" @click="showImgFun(1)">查看大图</span>
                        <span class="deleImg" @click="delImgFun(1)">删除</span>
                    </div>
                </div>
                <div class="itemsImg">
                    <el-upload class="avatar-uploader" :action='BASE_URL.uploadUrl'
                    :headers="headerToken" :data="uploadParams" :show-file-list="false"
                    :on-success="handleAvatarSuccess2" :before-upload="beforeAvatarUpload">
                    <img v-if="imgList[2].src" :src="imgList[2].src" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <div class="zzcImg" v-if="imgList[2].src">
                        <span class="showImg" @click="showImgFun(2)">查看大图</span>
                        <span class="deleImg" @click="delImgFun(2)">删除</span>
                    </div>
                </div>
                <div class="itemsImg">
                    <el-upload class="avatar-uploader" :action='BASE_URL.uploadUrl'
                    :headers="headerToken" :data="uploadParams" :show-file-list="false"
                    :on-success="handleAvatarSuccess3" :before-upload="beforeAvatarUpload">
                    <img v-if="imgList[3].src" :src="imgList[3].src" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <div class="zzcImg" v-if="imgList[3].src">
                        <span class="showImg" @click="showImgFun(3)">查看大图</span>
                        <span class="deleImg" @click="delImgFun(3)">删除</span>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="submitBtn">
        <div class="btns" @click="submitFun">确认</div>
    </div>
    <showBigImg :flag="showBigImgflag" :obj="bigImgList" :indexActive="showBigImgIndex" :falseFlag="falseFlag" @closeImg="closeImg"></showBigImg>

  </div>
</template>

<script>
import qs from 'qs'
import showBigImg from './conponents/showBigImg';//查看大图
  export default {
    data () {
      return {
          mobile:'',
          getUserId:sessionStorage.getItem('id'),
          counts: 0,
          describe: '',
          idR:false,
          headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
          uploadParams: {
            fileKey:'sysuser.feedback.pic',
            platform: 'web',
            module: 'feedback',
            type: 'image'
          },
          falseFlag: false,
          imgList:[{src:'',id:''},{src:'',id:''},{src:'',id:''},{src:'',id:''}],
          imageUrl: '',
          bigImgList:[],
          error: false,
          showBigImgflag: false,
          showBigImgIndex: 0,
          errorTxt:'请输入问题描述！！！',
      }
    },

    created () {
      this.getUserId = sessionStorage.getItem('id');
      this.getUserMobile();
        this.API.isReadFeedback({userId:sessionStorage.getItem('id')}).then((res)=>{
            console.log(666666,res.message,res.success);
            this.idR = res.success;
        })
    },

    components:{
        showBigImg
    },
      // computed:{
      //     idR(){
      //         console.log(sessionStorage.getItem('idR'));
      //         return sessionStorage.getItem('idR')=="true"?true:false
      //     }
      // },
    methods: {
        /*获取用户电话号码*/
        getUserMobile(){
            this.API.getUserMobile({userId: this.getUserId}).then((res)=>{
                if(res.success){
                    this.mobile = res.obj;
                }
            });
        },
        handleClick() {
            this.$router.push('/myFeedback');
        },
        handleAvatarSuccess(res) {
            this.imgList[0].src = res.data.url;
            this.imgList[0].id = res.data.resId;
        },
        handleAvatarSuccess1(res) {
            this.imgList[1].src = res.data.url;
            this.imgList[1].id = res.data.resId;
        },
        handleAvatarSuccess2(res) {
            this.imgList[2].src = res.data.url;
            this.imgList[2].id = res.data.resId;
        },
        handleAvatarSuccess3(res) {
            this.imgList[3].src = res.data.url;
            this.imgList[3].id = res.data.resId;
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG)
                this.$message.error('上传头像图片只能是 JPG 格式!');
            if (!isLt2M)
                this.$message.error('上传头像图片大小不能超过 2MB!');
            return isJPG && isLt2M;
        },
        delImgFun(index){
            this.imgList[index].src='';
            this.imgList[index].id='';
        },
        showImgFun(index){
           // console.log(e.target.dataset.index);
           this.bigImgList = [];
           this.imgList.forEach((value,index)=> {
                if(value.src.length>0)
                    this.bigImgList.push(value.src);
            });
            this.showBigImgIndex = index;
            this.showBigImgflag = true;
        },

        /*提交我的反馈*/
        submitFun() {
            if(!this.describe){
                this.error = true;
                return;
            }

            let imgStr = '';
            this.imgList.forEach((value,index)=> {
                if(value.src.length>0)
                    imgStr+=value.id+',';
            });
            this.imageUrl = imgStr.substring(0,imgStr.length-1)//图片id列表拼接成字符串

            let obj = {
                feedbackContent:this.describe,
                //userId:sessionStorage.getItem("id"),
                userId:30230,
                phone: this.mobile,
                feedbackPics:this.imageUrl
            };

            this.$confirm("是否确定提交此反馈?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(() => {
                    this.API.feedbackAdd(obj).then(res => {
                        if (res.success) {
                            this.$message({
                                type: "success",
                                message: res.message
                            });
                            this.handleClick();
                        } else {
                            this.$message({
                                type: "error",
                                message: res.message
                            });
                        }
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消该操作"
                    });
                });
        },

        closeToast(){
            this.error = false;
        },
        closeImg(){
            this.showBigImgflag = false;
        }
    },
    watch: {  
        'describe': function(val){  
            this.describe = val.trim();
            this.counts = val.length;
        }  
    } 
  }
</script>
<style lang="scss" scoped>
    span.idR{
        position: absolute;
        border-radius: 50%;
        width: 7px;
        height: 7px;
        background: #f00;
        top:25px;
        right: 11px;
    }
.reds{
    color: #ff3838 !important;
}
.main{
    width: 100%;
    background: #fff;
    min-height: 100%;
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
        box-sizing: border-box;
        padding: 44px 88px;
        font-size: 14px;
        .mess{
            width: 100%;
            line-height: 1;
            color: #999999;
            margin-bottom: 18px;
        }
        .formRows{
            width: 100%;
            display: inline-block;
            font-size: 0;
            margin-bottom: 18px;
            .el-alert{
                margin-top: 20px;
            }
            div{
                float: left;
            }
            .rowTxt{
                width: 88px;
                height: 36px;
                font-size: 14px;
                line-height: 36px;
                color: #666666;
            }
            .rowInp{
                width: 712px;
                height: 180px;
                background-color: #fafafa;
                border-radius: 2px;
                border: solid 1px #dddddd;
                position: relative;
                padding: 7px 10px 22px 10px;
                box-sizing: border-box;
                textarea{
                    max-width: 100%;
                    max-height: 100%;
                    min-width: 100%;
                    min-height: 100%;
                    line-height: 24px;
                    font-size: 14px;
                    color: #333;
                    background: rgba(0,0,0,0);
                    resize:none;
                }
                .counts{
                    position: absolute;
                    right: 10px;
                    bottom: 10px;
                    font-size: 12px;
                    line-height: 1;
                    color: #999999;
                }
            }
            .rowImg{
                height: 168px;
                width: 712px;
                .itemsImg{
                    width: 168px;
                    height: 168px;
                    background-color: #fafafa;
                    border-radius: 2px;
                    border: solid 1px #dddddd;
                    float: left;
                    margin-right: 10px;
                    position: relative;
                    &:last-child{
                        margin: 0;
                    }
                    .zzcImg{
                        width: 100%;
                        height: 100%;
                        position: absolute;
                        left: 0;
                        right: 0;
                        opacity: 0;
                        background: rgba(0,0,0,.7);
                        transition-duration: .3s;
                        span{
                            display: inline-block;
                            width: 72px;
                            height: 24px;
                            line-height: 24px;
                            position: absolute;
                            background-color: #e2e2e2;
                            border-radius: 12px;
                            text-align: center;
                            font-size: 14px;
                            left: 50%;
                            top: 90px;
                            margin-left: -36px;
                            cursor: pointer;
                            color: #333333;
                            &:first-child{
                                background-color: #ff6419;
                                color: #fff;
                                top: 55px;
                            }
                        }
                    }
                    &:hover .zzcImg{
                        opacity: 1;
                    }
                }
            }
        }
    }
    .submitBtn{
        width: 100%;
        padding: 50px 170px;
        box-sizing: border-box;
        border-top: 1px solid #e8e8e8;
        height: 140px;
        .btns{
            width: 133px;
            height: 100%;
            line-height: 40px;
            background-color: #ff6419;
            border-radius: 20px;
            border: solid 1px #ff6419;
            color: #fff;
            text-align: center;
            font-size: 16px;
            cursor: pointer;
        }
    }
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 50px;
    color: #8c939d;
    background-color: rgba(0,0,0,0);
    margin: 0;
    width: 168px;
    height: 168px;
    line-height: 168px;
    text-align: center;
  }
  .avatar {
    width: 168px;
    height: 168px;
    display: block;
  }
</style>

<template>
    <div>
        <ul class="imageFrame clearfix">
            <li  v-if="fileList2" v-for="(img, index) in fileList2" :key="index">
                <div class="img">
                    <div class="uploadPic"  @click.event.stop="updateImg(img)">
                        <el-upload
                                class="upload-demo"
                                :action="BASE_URL.systemUrl + '/v1/banner/manage/miniprogram/pic/update'"
                                :data="bannerData"
                                :headers="headers"
                                list-type=""
                                :on-success="updateBanner"
                                :show-file-list="false"
                                :before-upload="beforeAvatarUpload"
                                :on-preview="handlePreview"
                                :on-remove="handleRemove"
                        >
                            <img  @mouseenter="showDeleteBtn(img,index)" v-if="img.imagePath" @mouseleave="hideDeleteBtn(img,index)" :src="BASE_URL.sourceBaseUrl + img.imagePath">
                            <span v-if="img.active && img.imagePath" @mouseenter="showDeleteBtn(img,index)" @mouseleave="hideDeleteBtn(img,index)" @click.prevent.stop="deleteImg(img,index)" class="delImg">删除</span>
                            <div class="plus el-icon-plus" v-if="img.imagePath == '' && index < 6" ></div>
                        </el-upload>
                    </div>
                </div>
                <p class="urlTitle">链接地址</p>
                <div class="url"><input type="text" v-model="img.skipPath" class="txtUrl" placeholder="请输入链接地址" > <el-button type="text"  @click="updateUrl(img)">保 存</el-button></div>
            </li>
            <!--  v-if="fileList2.length < 6"-->
            <li v-if="fileList2.length < 6">
                <div class="img">
                    <div class="uploadPic">
                        <el-upload
                                class="upload-demo"
                                :action="BASE_URL.systemUrl + '/v1/banner/manage/miniprogram/pic/add'"
                                :headers="headers"
                                list-type= ""
                                :show-file-list="false"
                                :on-success="uploadSuccess"
                                :before-upload="beforeAvatarUpload"
                                :on-preview="handlePreview"
                                :on-remove="handleRemove"
                                :data="banner"
                        >
                            <div class="plus el-icon-plus" ></div>
                        </el-upload>
                    </div>
                </div>
                <p class="urlTitle">链接地址</p>
                <div class="url"><input type="text" v-model="skipPath" class="txtUrl" placeholder="请输入链接地址"> <el-button type="text"  @click="AddUrl(skipPath)">保 存</el-button></div>
            </li>
        </ul>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
     data(){
         return {
             skipPath:'',
             bannerData:{},
             deleteBanner: false,
             fileList2:[],
             banner:{
                 type:2
             }
         }
     },
    computed:{
        headers(){
            return { //上传图片时传token给后台
                'Authorization' : qs.parse(sessionStorage.getItem('loginUser')).token,
            }
        }
    },
     methods:{
         AddUrl(path) {
             this.bannerData={
                 bannerAdId:img.bannerAdId,
                 imageId:img.imageId,
                 imagePath:img.imagePath,
                 skipPath:path ? path : ''
             }
         },
         updateImg(img) {
             this.bannerData={
                 bannerAdId:img.bannerAdId,
                 imageId:img.imageId,
                 imagePath:img.imagePath,
                 skipPath:img.skipPath ? img.skipPath : ''
             }

         },
         updateBanner () {
             this.getBannerList()
         },
         uploadSuccess(){
             this.getBannerList()
         },
         updateUrl(img) {
             // 更新跳转地址
             this.API.spIndexBannerUpdateUrl({
                 bannerAdId:img.bannerAdId,
                 imageId:img.imageId,
                 imagePath:img.imagePath,
                 skipPath:img.skipPath ? img.skipPath : ''
             }).then((res) => {
                if(res) {
                    this.$message.success('链接地址修改成功')
                }
             })
         },
         handleRemove(file, fileList) {
             console.log(file, fileList);
         },
         handlePreview(file) {
             console.log(file);
         },
         hideDeleteBtn(img,index){
             if(this.fileList2.length > 0) {
                 this.fileList2.map((it) => {
                     it.active = false
                 })
             }
             this.$set(this.fileList2, index, this.fileList2[index])
         },
         showDeleteBtn(img,index) {
             if(this.fileList2.length > 0) {
                 this.fileList2.map((it) => {
                     it.active = false
                 })
             }
             img.active = true
             this.$set(this.fileList2, index, this.fileList2[index])
         },
         deleteImg(img,index){
             img.imagePath = '';
             this.fileList2.splice(index,1);
             this.API.spIndexBannerDelete({
                 bannerAdId: img.bannerAdId
             }).then((res) => {

                 this.getBannerList()
                 this.$message.success('删除成功')
             })
         },
         // 获取banner列表
         getBannerList () {
             this.API.spIndexBannerList({
                 positionCode: 'company:home:page:top'
             }).then((banner) => {
                 if(banner) {
                     console.log(banner,'banners')
                     if(banner.obj){
                         this.fileList2 = banner.obj;
                     }
                     // if(this.fileList2) {
                     //     this.fileList2.map((item) => {
                     //         item.active = false
                     //     })
                     // }

                 }
             })
         },
         handleAvatarSuccess(res, file) {
             this.imageUrl = URL.createObjectURL(file.raw);
             this.uploadImgId = res.obj.id;
         },

         beforeAvatarUpload(file) {
             const isJPG = file.type === 'image/jpeg';
             const isPNG = file.type === 'image/png';
             const isGIF = file.type === 'image/gif';
             const isLt2M = file.size / 1024 / 1024 < 4;

             if (!isJPG && !isPNG && !isGIF) {
                 this.$message.error('上传图片只能是 GIF/JPG/PNG 格式!');
             }
             if (!isLt2M) {
                 this.$message.error('上传图片大小不能超过 4M');
             }
             return (isJPG || isPNG || isGIF) && isLt2M;
         }
     },
        created() {
         this.getBannerList()
        },
    }
</script>

<style scoped lang="scss">
    .imageFrame{
        display: block;
        background: #fff;
        padding:20px;
        li{
            margin-bottom: 20px;
            width: 510px;
            float: left;
            .img{
                background: #f5f5f5;
                width: 510px;
                height: 291px;
            }
            .urlTitle{
                height: 40px;
                line-height: 40px;
            }
            .url{
                height: 60px;
                line-height: 60px;
                background-color: #f5f5f5;
            }
        }
        li:nth-child(3n+2){
            margin-right: 20px;
            margin-left: 20px;
        }

    }
    .txtUrl{
        width: 80%;
        background: #f5f5f5;
        height: 60px;
        line-height:60px;
        padding-left: 5%;
        margin-right: 5%;
    }
    .uploadPic{
        position: relative;
        background: none;
        .upload-demo {
            position: absolute;
            width: 510px;
            height: 291px;
            text-align: center;
            z-index: 11;
        }

       .uploadImg{
           position: absolute;
           z-index: 10;
           top: 0;
           left: 0;
       }
        img{
            width: 510px;
            height: 291px;
        }
    }
    .delImg{
        color: #fff;
        position: absolute;
        top: 16px;
        right: 16px;
        font-size: 16px;
        text-shadow:0 0 15px #404040;
        z-index: 50;
    }
    .plus{
        font-size: 50px;
        color: #999;
        position: absolute;
        top: 50%;
        left: 50%;
        width: 50px;
        height: 50px;
        -webkit-transform: translate(-50%, -50%);
        transform: translate(-50%, -50%);
    }
    .avatar-uploader-icon {
        position: relative;
        background-color: #FF6419;
        width: 20px;
        height: 20px;
        margin: 38% auto;
        border-radius: 50%;
        .across {
            position: absolute;
            top: 9px;
            left: 2px;
            background-color: #fff;
            width: 16px;
            height: 2px;
        }
        .vertical {
            position: absolute;
            top: 2px;
            left: 9px;
            background-color: #fff;
            width: 2px;
            height: 16px;
        }
    }
</style>
<template>
    <div>
        <h1 class="Htitle">品牌图片</h1>
        <ul class="imageFrame clearfix">
            <li>
                <div class="img">
                    <div class="uploadPic">
                        <el-upload
                                class="upload-demo"
                                :action="BASE_URL.uploadUrl"
                                :data="uploadParams"
                                list-type=""
                                :headers="headers"
                                :show-file-list="false"
                                :on-success="uploadSuccess"
                                :before-upload="beforeAvatarUpload"
                                :on-remove="handleRemove"
                        >
                            <!-- v-if="img.picPath" <img  @mouseenter="showDeleteBtn(img)" v-if="img.imgurl.length" @mouseleave="hideDeleteBtn(img)" :src="img.imgurl">-->
                            <img  @mouseenter="showDeleteBtn()" v-if="getPicPath" @mouseleave="hideDeleteBtn()" :src="getPicPath">
<!--
                            <span v-if="deleteBanner" @mouseenter="showDeleteBtn()" @mouseleave="hideDeleteBtn()" @click.prevent.stop="deleteImg()" class="delImg el-icon-error"></span>
-->
                            <div class="plus el-icon-plus" v-if="!getPicPath"></div>
                        </el-upload>

                    </div>
                </div>
            </li>
        </ul>
        <div class="editor">
            <div class="title">
                品牌介绍
            </div>
            <textarea class="editorBox" v-model="introduce"></textarea>
            <!--<vue-editor-->
                    <!--useCustomImageHandler-->
                    <!--@change="wordSize"-->
                    <!--@imageAdded="handleImageAdded"-->
                    <!--v-model="introduce"-->
                    <!--class="editor"></vue-editor>-->
            <div class="savebutton"><el-button size="small" @click="upadateBrandIntro" :disabled="wordSize(introduce)" type="primary">保 存</el-button><span class="limitSize" v-if="wordSize(introduce)">字数最多为2000个字</span></div>
        </div>

        <!--详情界面-->
        <!--富文本html区域-->
        <div v-show="false" v-html="introduce" ref="editorContent"></div>
        <!--富文本html区域-->
    </div>
</template>

<script>
    import { VueEditor } from 'vue2-editor';
    import qs from 'qs'
    export default {
        name: 'brandIntro',
        data(){
            return {
                deleteBanner: false,
                introduce: '', // 富文本编辑内容
                introducedPicIds: [], // 富文本图片ID
                introducedPicImg: [], //富文本图片信息
                getPicPath:'',
                picId: '',
                uploadParams:{
                    fileKey:'shop.brand.brandPic',
                    platform: 'web',
                    module: 'product',
                    type: 'image'
                },
                fileList2:{},
            }
        },
         // watch:{
         //     'introduce':function (val) {
         //         console.log(val.length)
         //     }
         // },
        computed:{
            headers(){
                return { //上传图片时传token给后台
                    'Authorization' : qs.parse(sessionStorage.getItem('loginUser')).token
                }
            }
        },

        components: {
            VueEditor
        },
        methods:{
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            wordSize(val){
                if (val && val.length > 2000) {
                    return true
                } else {
                    return false
                }
            },

            uploadSuccess(file){
                this.picId = file.data.resId;
                this.getPicPath = file.data.url;
                console.log(this.getPicPath, 'getPicPath')
                // this.getBrandIntroDetail()
            },

            getBrandIntroDetail() {
                this.API.getBrandIntroDetail({
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    if(res.data) {
                        console.log(res.data, 'picId');
                        this.fileList2 = res.data;
                        this.picId = res.data.picId;
                        this.getPicPath = res.data.picPath.indexOf('http')>-1 ? res.data.picPath : this.BASE_URL.sourceBaseUrl+res.data.picPath;
                        if(res.data.introduce) {
                            this.introduce = res.data.introduce
                        }

                    } else {
                        this.$message.error('没有数据')
                    }
                })
            },

            // 更新
            upadateBrandIntro(){
                this.$confirm('确定更新吗?', '提示', {
                    confirmButtonText: '确定',
                    type: 'info',
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    center: true
                }).then(() => {
                    this.API.upadateBrandIntro({
                        companyId:sessionStorage.getItem('companyID'),
                        companyPicId: this.picId,
                        companyIntroduce: this.introduce
                    }).then((res) => {
                        this.$message.success('更新成功')
                        this.getBrandIntroDetail()
                    })
                })

            },
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },

            /*hideDeleteBtn(){
                this.fileList2.map((it) => {
                    it.active = false
                })
            },
            showDeleteBtn(img) {
                this.fileList2.map((it) => {
                    it.active = false
                })
                img.active = true
            },*/

            hideDeleteBtn(img){
                this.deleteBanner = false
            },
            showDeleteBtn(img) {
                this.deleteBanner = true
            },

            deleteImg(){
                this.picId = ''
                this.getPicPath = ''
                this.fileList2.picPath = ''
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
                    this.$message.error('上传图片大小不能超过 15M!');
                }
                return (isJPG || isPNG || isGIF) && isLt2M;
            },


            /*富文本上传图片*/
            handleImageAdded(file, Editor, cursorLocation, resetUploader) {
                if (!this.upload(file)) {return;};
                let formData = new FormData();
                formData.append('file', file);
                formData.append('businessId ', sessionStorage.getItem('shopId'));
                formData.append('type', 'introduced');
                this.API.upLoadImg(formData).then((res) => {
                    if (res.success) {
                        let url = this.BASE_URL.sourceBaseUrl + res.obj.picPath; // Get url from response
                        if (res.obj.id) {
                            this.introducedPicImg.push(res.obj);
                        }
                        Editor.insertEmbed(cursorLocation, 'image', url);
                        resetUploader();
                    } else {
                        this.$message({
                            type: 'error',
                            message: res.message
                        });
                    }

                });
            },
            /*获取富文本内存在的图片的id*/
            acquireIntroducedPicId() {
                let img = this.$refs.editorContent.getElementsByTagName("img");
                let id = [];
                if (img.length > 0) {
                    for (let i = 0; i < img.length; i++) {
                        for (let j = 0; j < this.introducedPicImg.length; j++) {
                            if (decodeURI(img[i].src.substring(this.BASE_URL.sourceBaseUrl.length)) == decodeURI(this.introducedPicImg[j].picPath)) {
                                id.push(this.introducedPicImg[j].id);
                            }
                        }
                    }
                }
                this.introducedPicIds = id;
            },

        },
        created() {
            this.getBrandIntroDetail()
        },
    }
</script>

<style scoped lang="scss">
    .savebutton{
        text-align: center;
    }
    .limitSize{
        color: #FF6419;
        display: inline-block;
        padding-left:10px;
    }
    .Htitle{
        background: #fff;
        font-size: 14px;
        padding-top: 15px;
        padding-left: 18px;
        font-weight: normal;
    }
    .editor {
        background: #fff;
        padding:0 10px 20px 10px;
        .title{
            padding:12px;
        }
    }
    .editorBox{
        width: 96.3%;
        height: 200px;
        border: 1px solid #ddd;
        color: #333;
        font-size: 16px;
        padding: 1%;
        background: #f5f5f5;
        line-height: 25px;
        margin-bottom: 20px;
        margin-left: 14px;
        margin-right: 14px;
    }
    .imageFrame{
        display: block;
        background: #fff;
        padding:20px 20px 0 20px;
        li{
            margin-bottom: 20px;
            width: 514px;
            float: left;
            margin-right: 20px;
            .img{
                background: #f5f5f5;
                width: 514px;
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

    }

    .uploadPic{
        position: relative;
        background: none;
        .upload-demo {
            position: absolute;
            width: 514px;
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
            width: 514px;
            height: 291px;
        }
    }
    .delImg{
        color: #999;
        position: absolute;
        top: 50%;
        left: 50%;
        font-size: 50px;
        -webkit-transform: translate(-50%, -50%);
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

</style>
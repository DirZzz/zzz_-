<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <transition name="fade">
            <div class="addProduct">
                <div class="headTitle">详情</div>

                <div>
                    <el-row :gutter="24" class="splineFrame">
                        <el-col :span="2" class="productBaseInfo">基本信息</el-col>
                        <el-col :span="22" class="rightLine">
                            <hr>
                        </el-col>
                    </el-row>

                </div>

                <div class="productBaseItem">
                    <el-form :model="ruleForm" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                        <el-row class="formFrameWidth">

                            <el-form-item label="水刀编码：" prop="templateCode">
                                {{ ruleForm.templateCode }}
                            </el-form-item>

                            <el-form-item label="水刀名称：" prop="templateName">
                                {{ ruleForm.templateName }}
                            </el-form-item>

                            <el-form-item label="水刀品牌：" prop="brandId">
                                {{ ruleForm.brandName }}
                            </el-form-item>

                            <el-form-item label="水刀长度(cm)：" prop="templateLength">
                                {{ ruleForm.templateLength }}
                            </el-form-item>

                            <el-form-item label="水刀宽度(cm)：" prop="templateWidth">
                                {{ ruleForm.templateWidth }}
                            </el-form-item>

                            <el-form-item label="水刀形状：" prop="templateShapeValue">
                                {{ ruleForm.shapeName }}
                            </el-form-item>

                            <el-form-item label="水刀图片：" prop="templatePicId">
                                <div v-if="ruleForm.picPath">
                                    <img :src="BASE_URL.sourceBaseUrl + picImgUrl" v-if="picImgUrl" class="logo-img">
                                </div>
                            </el-form-item>

                            <el-form-item label="CAD源文件：" prop="cadSourceFileId">
                                <a class="downloadFile" title="点击文件下载" :href="BASE_URL.sourceBaseUrl+ruleForm.cadFilePath" :download="ruleForm.fileName">
                                    <span class="el-icon-download" ></span>{{ ruleForm.fileName }}</a>
                            </el-form-item>

                            <el-form-item label="水刀描述：">
                                <div v-html="ruleForm.templateDescribe" class="shop-item"></div>
                            </el-form-item>

                        </el-row>

                        <div class="splitLine"></div>
                        <el-form-item>
                            <el-button type="primary" class="confirmBtn" round @click="goBack()">返回</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </transition>
    </div>
</template>

<script>

    import { VueEditor, Quill } from "vue2-editor";
    import { ImageDrop } from "quill-image-drop-module";
    import ImageResize from "quill-image-resize-module";
    import "@/assets/css/vueEditer.scss";
    Quill.register("modules/imageDrop", ImageDrop);
    Quill.register("modules/imageResize", ImageResize);

    export default {
        components: {
            VueEditor
        },
        data(){
            return {
                basewaterjetId:'',
                name:'',
                picImgUrl:'',
                fileshwoName:'',
                editorSettings: {
                    modules: {
                        imageDrop: true,
                        imageResize: {}
                    }
                },
                nav: [
                    {nav: '水刀模板管理', url: '/waterKnife'},
                    {nav: '水刀详情', url: ''}
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
            this.basewaterjetId =  this.$route.params.id;
            this.getInfo();
        },
        methods:{
            getInfo(){
                this.API.infoWaterKnife({
                    basewaterjetId:this.basewaterjetId
                }).then(res => {
                    console.log(res,'kinfe')
                    this.ruleForm = res.data;
                    this.picImgUrl = res.data.picPath;
                    this.fileshwoName = res.data.fileName;
                });
            },
            goBack(){
                this.$router.push("/content/waterKnife");
            },
            //图片预览事件
            ImgView(ImgURL){
                this.$refs.sonParams.sonFun({ImgURL:ImgURL,ImgShow:true});
            },
        },

    }
</script>

<style scoped lang="scss">
    .productBaseInfo {
        text-align: left;
        height: 20px;
        line-height: 20px;
         border-left: solid 4px #FF6419;
        color: #666;
        font-size: 16px;
        padding-left: 10px;
        margin-left: 15px;
    }
    .downloadFile{
        color: #FF6419;
        span{
            font-weight: bolder;
        }
    }
    .description{
        border: solid 1px #e8e8e8;
    }
    .shop-item {
        word-wrap: break-word;
        word-break: break-all;
        border: solid 1px #e8e8e8;
        padding:15px;
    }
    .addProduct{
        background: #fff;
        padding:30px;
    }
    .detailFrame{
        background: #fff;
        padding: 20px;
    }
    .headTitle{
        border-bottom: solid 1px #ddd;
        font-size: 20px;
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
    .logo-img {
        width: 90px;
        height: 90px;
        border-radius: 4px;
        margin-right: 20px;
        display: inline-block;
        vertical-align: bottom;
    }
</style>
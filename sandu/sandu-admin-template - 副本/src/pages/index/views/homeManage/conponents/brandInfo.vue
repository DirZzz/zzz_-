<template>
    <transition name="zoomIn">
        <div class="brandInfo">
            <div class="brandInfo-header">公司介绍<span>（*必填项）</span></div>
            <div class="brandInfo-content">
                <div class="showHome">
                    <span><em>*</em>是否显示首页：</span>
                    <el-radio-group v-model="companyData.isShowHome">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </div>

                <div class="brandInfo-title">
                    <span><em>*</em>标题：</span>
                    <input type="text" maxlength="6" v-model="companyData.title" class="brandInfo-input">
                    <span class="brandInfo-tip">不超过6个字，4个字为最佳展示效果</span>
                </div>
                <div class="more-info">
                    <div class="more-info-title">
                        <span><em>*</em>公司简介：</span>
                    </div>
                    <vue-editor
                            :editorOptions="editorSettings"
                            useCustomImageHandler
                            @image-added="handleImageAdded"
                            v-model="companyData.richContext"
                            class="editor"></vue-editor>
                </div>
                <div class="brandBox">
                    <div class="brand-item" v-for="(item,index) in companyData.configDetails" :key="index">
                        <div class="brand-item-header">品牌介绍</div>
                        <div class="brandInfo-title">
                            <span>品牌名称：</span>
                            <input type="text" maxlength="10" v-model="item.title" class="brandInfo-input">
                            <span class="brandInfo-tip">不超过10个字 </span>
                        </div>
                        <div class="more-info">
                            <div class="more-info-title">
                                <span>品牌介绍：</span>
                            </div>
                            <vue-editor
                                    :editorOptions="editorSettings"
                                    useCustomImageHandler
                                    @image-added="handleImageAdded"
                                    v-model="item.richContext"
                                    class="editor"></vue-editor>
                        </div>
                        <i class="ic-close" @click="deleteList(index)"></i>
                    </div>
                    <div class="add-brand" @click="addBrand">+ 添加品牌</div>
                </div>
            </div>
            <div class="bottom">
                <div class="btn" @click="saveBrandInfo">保存</div><span>(新增、修改、删除操作后，请及时保存。)</span>
            </div>
        </div>

    </transition>
</template>

<script>
    import upload from './upload'
    import { mapState,mapActions} from 'Vuex'
    import VueEditor from '@/components/vueEditor/vueEditor'
    import Quill from "quill";
    import { ImageDrop } from "quill-image-drop-module";
    import ImageResize from "quill-image-resize-module";
    import {container, ImageExtend,QuillWatch} from 'quill-image-extend-module'

    Quill.register("modules/imageDrop", ImageDrop);
    Quill.register("modules/imageResize", ImageResize);
    Quill.register('modules/ImageExtend', ImageExtend)
    export default {
        name: "brandInfo",
        components: {
            VueEditor,
            upload
        },
        data(){
            return{
                picPath:'',
                isShowHome:1,
                brandList:[{ brand: 1 }],
                editorSettings: {
                    modules: {
                        imageDrop: false,
                        imageResize: {},
                        ImageExtend:{
                            loading: false,
                            name:'file',
                            change: (xhr, formData) => {
                                xhr.setRequestHeader('Authorization',sessionStorage.getItem('token'));
                                formData.append("type", "image");
                                formData.append("platform", "web");
                                formData.append("module", "product");
                                formData.append("fileKey", "company.shop.casePic");
                            },
                            action: 'upload-error',   //拖拽功能 去掉
                            start: (value) => {

                            },
                            response: (res) =>{
                                return res.data.url
                            }
                        }
                    }
                }
            }
        },
        filters:{
            filter(val){
                if(val){
                    return val.indexOf('http')>-1 ? val : this.BASE_URL.sourceBaseUrl+val;
                }
            }
        },
        computed:{
            ...mapState('homeManage',['companyIntroduceVO','companyData']),

        },
        methods:{
            ...mapActions('homeManage',['setCompanySet','setSave']),
            handleAvatarSuccess(){

            },
            addBrand(){
                this.companyData.configDetails.push({title:'',richContext:''});
            },
            handleImageAdded(file,fileName, Editor, cursorLocation, resetUploader) {
                const isLt4M = file.size / 1024 / 1024 < 4;
                if (!isLt4M) {
                    this.$message({
                        type: 'error',
                        message: '文件大小4M以内!'
                    });
                    return;
                }
                let formData = new FormData();
                formData.append('file', file,fileName);
                formData.append('type', 'image');
                formData.append('platform', 'web');
                formData.append('module', 'product');
                formData.append('fileKey', 'company.shop.casePic');
                this.API.upLoadImg(formData).then((res) => {
                    if (res.code == 200) {
                        if (res.data.resId) {
                            //this.editorContentImg.push(res.data);
                            Editor.insertEmbed(cursorLocation, "image", res.data.url);
                            resetUploader();
                        }
                    } else {
                        this.$message({
                            type: "error",
                            message: res.message
                        });
                    }

                });
            },
            wordSize(){

            },
            deleteList(index){
                this.companyData.configDetails.splice(index,1);
            },
            uploadSuccess(res){
                this.picPath=res.data.url;
                Object.assign(this.companyIntroduceVO,{
                    picId:res.data.resId,
                    picPath:res.data.url
                })
                this.setCompanySet(this.companyIntroduceVO);
            },
            deleteImg(){
                this.picPath='';
                this.companyIntroduceVO.picPath="";
                this.companyIntroduceVO.picId="";
            },
            saveBrandInfo(){
                this.setSave({vm:this});
                // this.API.upadateBrandIntro({
                //     companyId:sessionStorage.getItem('companyID'),
                //     companyIntroduce:this.companyIntroduceVO.introduce,
                //     companyPicId:this.companyIntroduceVO.picId,
                //     richContext:this.companyIntroduceVO.richContext,
                //     introduceTitle:this.companyIntroduceVO.introduceTitle || '品牌介绍'
                // }).then(res=>{
                //     if(res.code==200){
                //         this.$message.success('保存成功');
                //     }else {
                //         this.$message.error(res.message);
                //     }
                // })
            }
        }
    }
</script>

<style scoped lang="scss">
    .brandInfo{
        width: 540px;
        background-color: #fff;
        margin-top: 142px;
        margin-left: 30px;
        .brandInfo-header{
            width: 100%;
            height: 40px;
            line-height: 40px;
            background-color: #fafafa;
            text-indent: 21px;
            color: #000000;
            font-size: 14px;
            span{
                color: #ff2323;
            }
        }
        .brandInfo-content{
            padding-bottom: 30px;
        }
        .brandInfo-title{
            height: 48px;
            line-height: 48px;
            text-indent: 40px;
            .brandInfo-tip{
                font-size: 14px;
                color: #999999;
                margin-left: 10px;
            }
            em{
                color: #ff2323;
                margin-right: 5px;
            }
        }
        .brandBox{
            border-top: 1px solid #f5f5f5;
            padding: 10px 0;
            .brand-item{
                position: relative;
               .brand-item-header{
                   text-indent: 40px;
                   font-size: 16px;
                   color: #000000;
                   line-height: 48px;
               }
                .ic-close{
                    display: inline-block;
                    position: absolute;
                    top: 10px;
                    right: 10px;
                    width: 20px;
                    height: 20px;
                    background: no-repeat center url("../../../assets/images/ic_delete.png");
                    background-size: 70%;
                    cursor: pointer;
                }
            }
            .add-brand{
                width: 460px;
                height: 40px;
                border: solid 1px #ff6419;
                font-size: 14px;
                text-align: center;
                line-height: 40px;
                color: #ff6419;
                margin: 0 auto;
            }
        }
        .showHome{
            margin-top: 20px;
            margin-left: 40px;
            span{
                line-height: 48px;
                em{
                    color: #ff2323;
                    margin-right: 5px;
                }
            }
        }
        .brandInfo-input{
            width: 180px;
            height: 36px;
            background-color: #fafafa;
            border-radius: 2px;
            border: solid 1px #dddddd;
            text-indent: 16px;
        }
        .brandInfo-img{
            margin-left: 40px;
            position: relative;
            span{
                line-height: 48px;
                em{
                    color: #ff2323;
                    margin-right: 5px;
                }
            }
            .upLoadimg{
                background-color: #fafafa;
                border-radius: 2px;
                border: solid 1px #dddddd;
                width: 460px;
                height: 160px;
                text-align: center;
                position: relative;
            }
            .default{
                p:nth-of-type(1){
                    color: #ff6449;
                    font-size: 14px;
                    margin-top: 56px;
                }
                p:nth-of-type(2){
                    color: #999999;
                    font-size: 12px;
                }
            }
            .avatar{
                width: 460px;
                height: 160px;
                position: relative;
                img{
                    width: 400px;
                    height: 160px;
                }
                .ic-close{
                    display: inline-block;
                    position: absolute;
                    top: -10px;
                    right: -10px;
                    width: 20px;
                    height: 20px;
                    background: no-repeat center url("../../../assets/images/ic_delete.png");
                    background-size: 70%;
                    cursor: pointer;
                }
                .btn{
                    width: 48px;
                    height: 24px;
                    line-height: 24px;
                    background-color: #dbdbdb;
                    border-radius: 4px;
                    color: #333;
                    font-size: 14px;
                    position: absolute;
                    left: 8px;
                    top: 8px;
                    text-align: center;
                    cursor: pointer;
                    &:hover{
                        background-color: #ff6419;
                        color: #fff;
                    }
                }
            }
        }
        .brandInfo-state{
            margin-left: 40px;
            span{
                line-height: 48px;
                em{
                    color: #ff2323;
                    margin-right: 5px;
                }
            }
            .brandInfo-textarea{
                display: block;
                width: 460px;
                color: #333;
                font-size: 14px;
                .el-textarea__inner{
                    width: 400px;
                    height: 160px;
                }
            }
        }
        .more-info{
            margin-left: 40px;
            margin-bottom: 30px;
            .more-info-title{
                line-height: 48px;
                color: #333333;
                font-size: 14px;
                span{
                    line-height: 48px;
                    em{
                        color: #ff2323;
                        margin-right: 5px;
                    }
                }
            }
            .editor{
                width: 460px;
            }
        }
        .bottom{
            height: 56px;
            border-top: 1px solid #dddddd;
            display: flex;
            span{
                color: #ff6449;
                margin-top: 18px;
                margin-left: 10px;
            }
            .btn{
                width: 60px;
                height: 30px;
                background-color: #ff6419;
                border-radius: 15px;
                color: #ffffff;
                font-size: 14px;
                line-height: 30px;
                text-align: center;
                margin-left: 40px;
                margin-top: 14px;
                cursor: pointer;
                &:active{
                    opacity: .6;
                }
            }
        }
    }
</style>
<style lang="scss">
    .brandInfo{
        .brandInfo-state{
            .el-textarea__inner{
                background-color: #fafafa!important;
            }
        }
        .ql-editor{
            max-height: 256px;
            overflow-y: scroll;
        }
        .ql-align-center{
            font-size: 14px;
        }
    }

</style>
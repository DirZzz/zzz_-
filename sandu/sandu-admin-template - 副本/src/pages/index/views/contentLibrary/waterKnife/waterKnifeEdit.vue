<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <transition name="fade">
            <div class="addProduct">
                <div class="headTitle">编辑</div>
                <p class="ptop"><span>*</span>为必填项</p>

                <div>
                    <el-row :gutter="24" class="splineFrame">
                        <el-col :span="2" class="productBaseInfo">基本信息</el-col>
                        <el-col :span="22" class="rightLine">
                            <hr>
                        </el-col>
                    </el-row>
                </div>

                <div class="productBaseItem">
                    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">

                        <el-form-item label="水刀名称：" prop="templateName">
                            <el-input class="formWidth" clearable v-model="ruleForm.templateName"
                                      placeholder="请输入内容"></el-input>
                        </el-form-item>

                        <el-form-item label="水刀品牌：" prop="brandId">
                            <el-select v-model="ruleForm.brandId" clearable placeholder="请选择水刀品牌">
                                <el-option v-for="item in brandList"
                                           :key="item.id"
                                           :label="item.brandName"
                                           :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="水刀形状：" prop="productNameStr">
                            <el-select v-model="ruleForm.templateShapeValue" clearable placeholder="请选择水刀形状">
                                <el-option v-for="item in templateShapeList"
                                           :key="item.value"
                                           :label="item.name"
                                           :value="item.value"
                                ></el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="水刀长度(cm)：" prop="productNameStr">
                            <el-input class="formWidth" clearable v-model="ruleForm.templateLength"
                                      maxlength="9"
                                      oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                                      placeholder="请输入内容"></el-input>
                        </el-form-item>


                        <el-form-item label="水刀宽度(cm)：" prop="productNameStr">
                            <el-input class="formWidth" clearable v-model="ruleForm.templateWidth"
                                      maxlength="9"
                                      oninput="value=value.replace(/[^\d]/g,'').replace(/^0{1,}/g,'')"
                                      placeholder="请输入内容"></el-input>
                        </el-form-item>

                        <el-form-item label="水刀图片：" class="logo">
                            <span class="star">*</span>
                            <el-upload class="avatar-uploader"
                                       :action="uploadImgUrl"
                                       :headers="headers"
                                       :show-file-list="false"
                                       :on-success="picSuccess"
                                       :before-upload="picBeforeUpload">
                                <div class="add-img"></div>
                            </el-upload>
                            <img :src="BASE_URL.sourceBaseUrl + picImgUrl" v-if="picImgUrl" class="logo-img">
                            <p class="img-text">文件格式JPG，PNG文件大小10M以内，尺寸1:1</p>
                        </el-form-item>

                        <el-form-item label="CAD源文件：" class="logo">
                            <span class="starCAD">*</span>
                            <el-upload
                                    :action="uploadFileUrl"
                                    :headers="headers"
                                    :show-file-list="false"
                                    :on-success="fileSuccess"
                                    :before-upload="fileBeforeUpload">
                                <el-button type="primary" round size="small">选择文件</el-button>
                                <span v-if="this.fileshwoName">{{fileshwoName}}</span>
                                <p class="img-text-1">文件格式支持：DXF</p>
                            </el-upload>
                        </el-form-item>

                        <el-form-item label="水刀描述：">
                            <vue-editor v-model="ruleForm.templateDescribe"
                                        :editorOptions="editorSettings"
                                        class="editor">
                            </vue-editor>
                        </el-form-item>

                        <div class="splitLine"></div>
                        <el-form-item>
                            <el-button type="primary" class="confirmBtn" round @click="submitForm('ruleForm')">确定
                            </el-button>
                            <el-button round class="cancelBtnBg marRight" @click="resetForm('ruleForm')">取消</el-button>
                        </el-form-item>
                    </el-form>
                </div>
                <!--富文本html区域-->
                <div v-show="false" v-html="ruleForm.introduce" ref="editorContent"></div>
                <!--富文本html区域-->
            </div>
        </transition>
    </div>
</template>

<script type="es6">
    import { VueEditor, Quill } from "vue2-editor";
    import { ImageDrop } from "quill-image-drop-module";
    import ImageResize from "quill-image-resize-module";
    import "@/assets/css/vueEditer.scss";
    Quill.register("modules/imageDrop", ImageDrop);
    Quill.register("modules/imageResize", ImageResize);
    import ElButton from 'element-ui/packages/button/src/button'
    import qs from 'qs'
    import ImgPreView from '@/components/Img-preview'

    export default {
        components: {
            VueEditor
        },
        data() {
            return {
                nav: [
                    {nav: '水刀模板管理', url: '/waterKnife'},
                    {nav: '编辑水刀', url: ''}
                ],
                basewaterjetId:'',
                editorSettings: {
                    modules: {
                        imageDrop: true,
                        imageResize: {}
                    }
                },
                templateShapeList:'',
                brandList:'',
                uploadImgUrl: '',
                picImgUrl:'',
                picImgId:'',
                uploadFileUrl: '',
                fileId: '',
                templatePicId:'',
                cadSourceFileId:'',
                fileshwoName:'',
                ruleForm: {
                    id: '',
                    templatePicId:'',
                    cadSourceFileId:''
                },
                rules: {
                    templateName: [
                        { required: true, message: "请输入水刀名称", trigger: "blur" },
                        { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
                    ],
                    brandId: [
                        {required: true, message: '请选择水刀品牌', trigger: 'change'}
                    ],
                }
            }
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.nav[0].url = from.path
            })
        },
        created() {
            this.basewaterjetId =  this.$route.params.id;
            this.getInfo();
            this.getBrandLIst();
            this.getTemplateShapes();
            this.uploadImgUrl = this.BASE_URL.baseUrl + "/v1/basewaterjet/img/upload";
            this.uploadFileUrl = this.BASE_URL.baseUrl + "/v1/basewaterjet/file/upload";
        },
        computed:{
            headers(){
                return { //上传图片时传token给后台
                    'Authorization' : qs.parse(sessionStorage.getItem('loginUser')).token
                }
            }
        },
        methods: {
            alertValue(e,strValue,decimalNum){
                e.quill.deleteText(decimalNum,1,strValue);//保留 strValue 的 前 decimalNum 位字符，
            },
            /*上传水刀图片成功回调*/
            picSuccess(res, file) {
                if (res.success) {
                    this.picImgUrl = res.obj.picPath;
                    this.ruleForm.templatePicId = res.obj.id;
                } else {
                    this.$message({
                        type: "error",
                        message: res.message
                    });
                }
            },
            picBeforeUpload(file) {
                const suffix = file.name.split(".")[1].toUpperCase();
                const isJPG = suffix === "JPG" || suffix === "JPEG" || suffix === "PNG";
                const isLt4M = (file.size / 1024 / 1024) < 10;
                if (!isJPG) {
                    this.$message.error("文件格式JPG，PNG，JPEG!");
                    return false;
                } else if (!isLt4M) {
                    this.$message.error("文件大小10M以内!");
                    return false;
                } else {
                    return true;
                }
            },
            fileSuccess(res, file) {
                if (res.success) {
                    this.ruleForm.cadSourceFileId = res.obj.id;
                } else {
                    this.$message({
                        type: "error",
                        message: res.message
                    });
                }
            },
            fileBeforeUpload(file) {
                const suffix = file.name.split(".")[1].toUpperCase();
                let isJPG = suffix === "DXF";
                //let isJPG = suffix === "DWG" || suffix === "DWT" || suffix === "BAK" || suffix === "DXF";
                if (!isJPG) {
                    //this.$message.error("文件格式DWG，DWT，BAK，DXF!");
                    this.$message.error("文件格式DXF!");
                    return false;
                } else {
                    this.fileshwoName = file.name;
                    return true;
                }

            },
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
            getBrandLIst(){
                this.brandList = {};

                let queryCondition = Object.assign({}, this.query, {});

                this.API.getBrandList(queryCondition).then((res) => {
                    console.log(res,"brandList");
                    this.brandList = res.list;
                })
            },
            getTemplateShapes(){
                this.templateShapeList = {};

                let queryCondition = Object.assign({}, this.query, {
                    type:'templateShape'
                });
                this.API.getTemplateShape(queryCondition).then((res) => {
                    console.log(res,"templateShapeList");
                    this.templateShapeList = res.datalist;
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let formDatas = this.ruleForm;
                        for (let key in formDatas) {
                            if (formDatas[key] == '' || formDatas[key] == undefined || formDatas[key] == null) {
                                delete formDatas[key]
                            }
                        }

                        if(this.ruleForm.templatePicId==""||this.ruleForm.templatePicId==null||this.ruleForm.templatePicId==undefined){
                            this.$message.error('请上传水刀图片');
                            return;
                        }
                        if(this.ruleForm.cadSourceFileId==""||this.ruleForm.cadSourceFileId==null||this.ruleForm.cadSourceFileId==undefined){
                            this.$message.error('请上传水刀CAD文件');
                            return;
                        }

                        let jsonData = JSON.stringify(formDatas)
                        this.$confirm('确定修改内容?', '提示', {
                            confirmButtonText: '确定',
                            type: 'info',
                            cancelButtonClass: 'cancelButtonClass',
                            confirmButtonClass: 'confirmButtonClass',
                            center: true
                        }).then(() => {
                            this.$message.success('修改成功');
                            let params = Object.assign({},this.ruleForm, {
                                id: this.sceneqrcodeinfoId
                            })
                            this.API.editWaterKnifeList(params).then(res => {
                                if(res){
                                    this.$message({ message: "成功更新内容", type: "success" });
                                    this.$router.push("/content/waterKnife");
                                }
                            });
                        })
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.$message({ message: "您取消了此次编辑", type: "warning" });
                this.$router.push("/content/waterKnife");
            }
        }
    }
</script>

<style lang="scss" scoped>
    .productBaseItem{
        .el-input,.el-select{
            width: 321px;
        }
    }
    .star{
        color: #ff6419;
        position: absolute;
        left: -89px;
    }
    .starCAD{
        color: #ff6419;
        position: absolute;
        left: -105px;
    }
    .editor {
        width: 850px;
    }
    .headTitle {
        border-bottom: solid 1px #ddd;
        font-size: 20px;
        font-weight: normal;
        color: #666666;
        padding-bottom: 20px;
    }

    .addProduct {
        background: #fff;
        box-sizing: padding-box;
        padding: 20px;
        margin-bottom: 20px;
        border-radius: 5px;
        .ptop {
            height: 30px;
            line-height: 30px;
            padding-top: 10px;
            font-size: 14px;
            color: #666;
            span {
                height: 20px;
                line-height: 20px;
                display: inline-block;
                padding-right: 5px;
                color: red;
                font-weight: bold;
                font-size: 20px;
                vertical-align: -6px;
            }
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
    .logo{
        position: relative;
    }
    .img-text {
        color: #999999;
    }
    .img-text-1 {
        color: #999999;
        text-align : left;
    }
    .add-img {
        width: 22px;
        height: 22px;
        margin: 34px 34px;
        background: url("../../../assets/images/icons/uploadPlug.png") no-repeat
        0 0;
    }
    .avatar-uploader {
        display: inline-block;
        vertical-align: bottom;
        width: 90px;
        height: 90px;
        background-color: #ffffff;
        border-radius: 4px;
        border: solid 1px #dddddd;
        margin-right: 20px;
    }
</style>


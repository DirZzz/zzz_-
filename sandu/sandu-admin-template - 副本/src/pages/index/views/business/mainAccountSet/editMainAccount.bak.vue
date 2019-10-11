<template>
    <div class="editMainAccount">
        <v-nav :nav="nav"></v-nav>
        <div class="editMainAccount-content">
            <div class="editMainAccount-header">账号编辑</div>
            <div class="editMainAccount-tip"><span>*</span>为必选项</div>
            <div class="editMainAccount-title"><span></span>账号信息</div>
            <el-form label-width="120px" :model="formInline">
                <div class="editMainAccount-form">
                    <div class="editMainAccount-form-left">
                        <el-form-item label="账号:" class="form-item">
                            <el-input v-model="formInline.userName"></el-input>
                        </el-form-item>
                        <el-form-item label="密码:" class="form-item">
                            <el-input v-model="formInline.password"></el-input>
                        </el-form-item>
                        <el-form-item label="确认密码:" class="form-item">
                            <el-input v-model="formInline.verifyPwd"></el-input>
                        </el-form-item>
                        <el-form-item label="昵称:" class="form-item">
                            <el-input v-model="formInline.nickName"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号:" class="form-item">
                            <el-input v-model="formInline.mobile"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱:" class="form-item">
                            <el-input v-model="formInline.email"></el-input>
                        </el-form-item>
                        <el-form-item label="区域:">
                            <areaSelect :sizeWidth="'140px'"></areaSelect>
                        </el-form-item>
                        <el-form-item label="详细地址:">
                            <el-input v-model="formInline.address"></el-input>
                        </el-form-item>
                    </div>
                    <div class="editMainAccount-form-right">
                        <el-form-item label="LOGO设置：" class="logo">
                            <div class="logo-item">
                                <img :src="BASE_URL.sourceBaseUrl + formInline.uploadUrl" v-if="formInline.uploadUrl" class="logo-img">
                                <div v-else class="headPicPath">暂无头像</div>
                                <span v-else class="logo-active"></span>
                                文件格式GIF，JPG，JPEG，PNG文件大小4M以内，尺寸1:1
                            </div>
                            <el-upload
                                    class="avatar-uploader"
                                    :action="uploadUrl"
                                    :headers="token"
                                    :show-file-list="false"
                                    :on-success="imgSuccess"
                                    :before-upload="imgUpload">
                                重新上传
                            </el-upload>
                        </el-form-item>
                        <el-form-item label="主子类型：">
                            <span>{{userInfo.masterSonType==0 ? '普通' : userInfo.masterSonType==1 ? '子' : '主'}}账号</span>
                        </el-form-item>
                        <el-form-item label="用户类型：">
                            <span>{{userInfo.userType | selectType}}</span>
                        </el-form-item>
                    </div>
                </div>
            </el-form>

            <div class="editMainAccount-btn">
                <el-button size="medium" type="primary"  round>保存</el-button>
                <el-button size="medium" round>重置</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import areaSelect from '@/components/areaSelect'
    export default {
        name: "editMainAccount",
        components:{
            areaSelect
        },
        data(){
            return{
                nav:[
                    {nav: '主子账号管理', url: '/business/mainAccountSet'},
                    {nav: '账号编辑', url: '/business/editAccount'}
                ],
                formInline:{
                    userName:'',
                    password:'',
                    verifyPwd:'',
                    nickName:'',
                    mobile:'',
                    email:'',
                    address:'',
                },
                userInfo:'',
                uploadUrl:'',
                token: {Authorization: sessionStorage.getItem('token')}, // 上传头像token
            }
        },
        filters:{
            selectType(type){
                let obj={
                    1:'内部用户',
                    2:'厂商',
                    3:'经销商',
                    4:'设计公司',
                    5:'设计师',
                    6:'装修公司',
                    7:'学校（培训机构）',
                    8:'装修公司',
                    9:'游客',
                    11:'中介',
                    13:'工长',
                    14:'独立经销商',
                    15:'定制柜门店',
                }
                for (let key in obj){
                    if(key==type){
                        return obj[key]
                    }
                }
            }
        },
        created(){
            this.API2.manageUserInfo({userId:this.$route.query.id}).then(res=>{
                 if(res.success){
                     for (let key in this.formInline){
                         this.formInline[key]=res.obj.userInfo[key];
                     }
                     this.userInfo=res.obj.userInfo;
                 }else {
                     this.$message.error(res.message);
                 }
            })
        },
        methods:{
            /*上传图片成功*/
            imgSuccess(res, file) {

            },
            /*上传图片验证*/
            imgUpload(file) {
                const isJPG = file.name.split('.')[1].toUpperCase() === 'GIF' || 'JPG' || 'JPEG' || 'PNG' ;
                const isLt500K = file.size / 1024 < 500;
                if (!isJPG) {
                    this.$message.error('文件格式GIF，JPG，JPEG，PNG!');
                }
                if (!isLt500K) {
                    this.$message.error('文件大小500K以内!');
                }
            },
        }
    }
</script>

<style scoped lang="scss">
    @import "./style/editMainAccount";
</style>
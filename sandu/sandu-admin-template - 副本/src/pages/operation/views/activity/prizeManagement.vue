<template>
    <div id="prizeManagement">
        <div class="mains">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><span @click="toLink('wheel')" class="toLi">幸运大转盘活动</span></el-breadcrumb-item>
                <el-breadcrumb-item>奖品管理</el-breadcrumb-item>
            </el-breadcrumb>
            <p class="hei20"></p>
            <div class="formBody">
                <div class="zzc" v-if="formInline.statusCode==20">
                    <div>活动已结束<span @click="toLink('wheel')">返回</span></div>
                </div>
                <el-form ref="form" :model="formInline" label-width="100px" size="mini">
                    <div class="titles">
                        <span>活动名称：{{formInline.actName}}</span>
                        <span>活动状态：{{formInline.statusCode==0 ? '未开启' :formInline.statusCode==10 ? '已开启' : '已结束'}}</span>
                    </div>
                    <div class="items" v-for="(item, index) in formInline.prizeList" :v-key="index">
                        <el-row :gutter="10">
                            <el-col :xs="16" :md="16">
                                <el-row>
                                    <el-col :xs="8" :md="8">
                                        <el-form-item label="奖品名称" required>
                                            <el-input v-model="item.name" maxlength="20" :disabled="formInline.statusCode==10"></el-input>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :xs="8" :md="8">
                                        <el-form-item label="奖品">
                                            <el-select v-model="item.type" placeholder="请选择">
                                                <el-option
                                                v-for="item in optionObj"
                                                :key="item.value"
                                                :label="item.label"
                                                :value="item.value">
                                                </el-option>
                                            </el-select>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :xs="8" :md="8" v-if="item.type==1||item.type==2">
                                        <el-form-item label="单个额度">
                                            <el-input-number v-model="item.value" :min="0" :step="1"></el-input-number>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <el-col :xs="8" :md="8">
                                        <div class="bfb">
                                        <el-form-item label="中奖率" required>
                                            <el-input-number v-model="item.probability" :min="0" :max="100" :step="0.01"></el-input-number>
                                        </el-form-item>
                                        </div>
                                    </el-col>
                                    <el-col :xs="8" :md="8">
                                        <el-form-item label="总数量">
                                            <el-input-number v-model="item.num" :min="0" :step="1"></el-input-number>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :xs="8" :md="8" v-if="item.type">
                                        <el-form-item label="每日最大数量">
                                            <el-input-number v-model="item.numPerDay" :min="0" :step="1"></el-input-number>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                            </el-col>
                            <el-col :xs="4" :md="4">
                                <div class="upImg">
                                <el-form-item label="上传图片" required>
                                    <el-upload class="avatar-uploader" :action='BASE_URL.uploadUrl'
                                    :headers="headerToken" :data="uploadParams[index]" :show-file-list="false"
                                    :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                                    <img v-if="item.img" :src="item.img" class="avatar">
                                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                                    </el-upload>
                                </el-form-item>
                                </div>
                            </el-col>
                            <el-col :xs="4" :md="4">
                                <el-form-item label="总剩余数量">{{item.remainNum}}</el-form-item>
                                <el-form-item label="今日剩余数量">{{item.todayRemainNum}}</el-form-item>
                            </el-col>
                        </el-row>
                    </div>
                    <div class="btns">
                        <el-button type="primary" @click="onSubmit" round>确认</el-button>
                        <el-button @click="toLink('wheel')" round>取消</el-button>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import qs from 'qs'

    export default {
        name:'prizeManagement',
        data() {
            return {
                formInline:{},
                activeType:0,
                dialogImageUrl:'',
                dialogVisible:false,
                uploadParams: [],
                formFlag:false,
                headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
                optionObj:[
                    {value:0,label:'未中奖'},
                    {value:1,label:'度币'},
                    {value:2,label:'现金'},
                    {value:3,label:'话费'},
                    {value:4,label:'资料'},
                    {value:5,label:'物品'}
                ]
            }
        },
        components: {
        },
        created(){
            this.init();
        },
        methods: {
            onSubmit(e){
                let vFlag = true;
                let typeFlag = true;
                let imgFlag = true;
                let probability = 0;
                let nameFlag = true;
                this.formInline.prizeList.map((v,i)=>{
                    if(!v.name){ vFlag = false;}
                    if(!v.img){ imgFlag = false;}
                    if(v.type==0){ typeFlag = false; }
                    if(v.name=='谢谢参与'){ nameFlag = false; }
                    probability+=v.probability;
                })
                if(!vFlag){
                    this.$message.error('奖品名称不能为空！')
                    return;
                }
                if(typeFlag){
                    this.$message.error('奖品中必须有一个未中奖的选项！')
                    return;
                }
                if(probability!=100&&nameFlag){
                    this.$message.error('奖品概率必须等于100%')
                    return;
                }else if(probability>100){
                    this.$message.error('奖品概率必须小于等于100%')
                    return;
                }
                if(!imgFlag){
                    this.$message.error('图片不能为空！')
                    return;
                }
                this.formFlag = true;
                this.API.savePrize({actId:this.formInline.actId,prizeList:this.formInline.prizeList}).then(res=>{
                    if(res.success){
                        this.$message.success('保存成功！')
                        this.$router.push({path:'wheel'})
                    }else{
                        this.$message.error(res.message)
                    }
                    
                })
            },
            init(){
                this.getPageList();
                
            },
            toLink(url,query){
                if(!this.formFlag){
                    this.$confirm('还未完成奖品设置，离开将自动删除该活动, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                    }).then(() => {
                        this.API.luckywheelDelete({actId:this.$route.query.id}).then(res=>{
                            if(res.success){
                                this.$message.success('删除成功！')
                                this.$router.push({path:url,query: query})
                            }
                        })
                        
                    })
                    return;
                }
                this.$router.push({path:url,query: query})
            },
            getPageList(){
                this.API.getPageList({actId:this.$route.query.id}).then(res=>{
                    if(res.success){
                        this.formInline = res.obj
                        if(res.obj.prizeList.length<=0){
                            for(let i=0;i<8;i++){
                                this.formInline.prizeList[i]={name:"",num:0,remainNum:0,todayRemainNum:0,numPerDay: 0,probability:0,type:0,value:0,img:""};
                                this.$set(res.obj.prizeList,i,res.obj.prizeList[i])
                            }
                        }else{
                            this.formFlag = true;
                        }
                        for(let i = 0;i<res.obj.prizeList.length;i++){
                            this.uploadParams[i] = {fileKey:'sysuser.feedback.pic', platform: 'web',module: 'feedback',type: 'image',msgId:i}
                        }
                    }
                })
            },
            handleAvatarSuccess(res){
                this.formInline.prizeList[res.msgId].img = res.data.url;
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isLt2M)
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                return isJPG && isLt2M;
            },
        }
    }
</script>

<style scoped lang="scss">
    div{
        box-sizing: border-box;
    }
    .toLi{
        color: #409EFF;
        cursor: pointer;
    }
    .hei20{
        height: 20px;
        width: 100%;
        border-bottom: 1px solid #ededed;
    }
    .ttspan{
        display: inline-block;
        padding: 0 15px;
    }
    ::-webkit-scrollbar{
        display: none;
    }
    #prizeManagement{
        width: 100%;
        height: 100%;
        padding: 20px;
        padding-right: 14px;
        font-size: 16px;
        color: #333;
        text-align: left;
        overflow-y: scroll;
        .mains{
            width: 100%;
            min-height: 100%;
            background: #fff;
            padding:20px;
            .formBody{
                padding: 20px;
                padding-top: 0;
                position: relative;
                .zzc{
                    width: 100%;
                    height: 100%;
                    background: rgba(255,255,255,.6);
                    position: absolute;
                    left: 0;
                    top: 0;
                    z-index: 66;
                    div{
                        position: absolute;
                        height: 30px;
                        line-height: 30px;
                        width: 100%;
                        top: 50%;
                        margin-top: -15px;
                        text-align: center;
                        font-size: 24px;
                        letter-spacing: 4px;
                        font-weight: bold;
                        color: #aaa;
                        span{
                            color: #409EFF;
                            cursor: pointer;
                            padding: 0 4px;
                        }
                    }
                    
                }
                .titles{
                    height: 50px;
                    line-height: 50px;
                    border-bottom: 1px solid #ededed;
                    span{
                        margin-right: 20px;
                        font-weight: bold;
                    }
                }
                .items{
                    padding-top: 10px;
                    border-bottom: 1px solid #ededed;
                }
                .items:last-child{
                    border-bottom: none;
                }
                .upImg{
                    width: 100%;
                    padding: 0;
                }
                .btns{
                    margin-top: 50px;
                }
            }
        }
    }

</style>
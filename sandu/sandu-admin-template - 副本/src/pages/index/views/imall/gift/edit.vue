<template>
    <div v-loading="loading" :element-loading-text="GLOBAL.loadingText">
         <v-nav :nav="nav"></v-nav>
        <el-form :model="form" :rules="rules" ref="ruleForm"  style="background-color:#fff;padding:10px;" size='mini' :v-model="formData" label-width="100px" >
            
            <span style="color:#FF0000">*</span><span>为必填项</span>
            <fieldset class="field-title">
                <legend>礼品基础信息</legend>
            </fieldset>
            <el-form-item label="礼品名称" prop="name">
                <el-input style="width:320px" placeholder="请输入礼品名称" v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="兑换说明">
                <el-input type="textarea" style="width:380px" :rows="4" v-model="form.explain" maxlength="100"></el-input>
                <!-- <span>{{form.explain.length}}/100</span> -->
            </el-form-item>
            <el-form-item label="礼品图片">
                <div class="img-list">
                    <div class="img-content" v-for="(item,index) in form.images" :key="index">
                        <img :src="item.fileName | filter">
                        <div class="name" >
                            <el-radio v-model="small" @change="changeSmall(item)" :label="index" small>缩略图</el-radio><br/>
                            <!--<el-radio v-model="cover" @change="changeCover(item)" :label="index" small>封面</el-radio><br/>-->
                        </div>
                        <!-- 删除icon -->
                        <div class="del">
                            <i @click="handleFileRemove(item,index)" class="el-icon-delete"></i>
                        </div>
                        <!-- 放大icon -->
                        <div class="layer" @click="handleFileEnlarge(item.url)">
                            <i class="el-icon-view"></i>
                        </div>
                    </div>
                    <!-- <div v-if="!pass && progress !== 0" class="img-content img-progress">
                        <el-progress type="circle" :percentage="progress" :status="proStatus"></el-progress>
                    </div>  -->
                    <el-upload
                        ref="upload"
                        :action="upload_url"
                        :data="imageData"
                        :show-file-list="false"
                        :headers="headerToken"
                        :before-upload="beforeUpload"
                        :on-success="onSuccess"
                        list-type="picture-card"
                        accept= ".jpg,.png,.bmp,.jpeg"
                        :multiple="true"
                        class="uploadImg"
                        v-if="form.images&&form.images.length<6"
                        >
                        <el-button type="primary">点击上传</el-button>
                    </el-upload>
                    <!-- <el-dialog title="" :visible.sync="isEnlargeImage" size="large" :modal-append-to-body="false" top="8%" width="60%">
                        <img @click="isEnlargeImage = false" style="width:100%;" :src="enlargeImage">
                    </el-dialog> -->
                </div>
            </el-form-item>
            
            <fieldset class="field-title">
                <legend>兑换信息</legend>
            </fieldset>

            <el-row >
                <el-col :span="8">
                    <el-form-item label="所需积分" prop="point">
                        <el-input style="width:180px" placeholder="" v-model="form.point"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8" >
                    <el-form-item label="库存" prop="inventory">
                        <el-input style="width:180px" v-model="form.inventory"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row >
                <el-col :span="8">
                    <el-form-item label="价值" prop="price">
                        <el-input style="width:180px" v-model="form.price"></el-input>
                        <span>元</span>
                    </el-form-item>
                </el-col>
                <el-col :span="8" >
                    <el-form-item label="配送费用" prop="expressFee">
                        <el-input style="width:180px" v-model="form.expressFee"></el-input>
                        <span>元</span>
                    </el-form-item>
                </el-col>
            </el-row>

            <fieldset class="field-title">
                <legend>礼品介绍</legend>
            </fieldset>
            <el-form-item label="礼品介绍">
                <el-input type="textarea" :rows="4" style="width:380px" v-model="form.introduce" maxlength="500"></el-input>
                <!-- <span>{{form.introduce.length}}/200</span> -->
            </el-form-item>
            <fieldset class="field-title">
            </fieldset>
            <el-form-item>
                <el-button style="width:120px;" @click="$router.back()" type="info" round>取  消</el-button>
                <el-button style="width:120px" type="primary" @click="save" round>保  存</el-button>
            </el-form-item>
        </el-form>    
    </div>
    
</template>
<script>

import Breadcrumb from '@/components/Breadcrumb'
import store from '@/store'
import qs from 'qs'
export default {
    name:'gift_create',
    components:{
        Breadcrumb
    },
    data() {
        // const validateName = (rule, value, callback) => {
        //     if (value.length==0) {
        //         callback(new Error('请输入礼品名称'))
        //     } else {
        //         callback()
        //     }
        // }
        return {
            headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
            giftId:this.$route.params.id,
            imageData:{order:0,cover:0,small:0,id:0},
            formData:new FormData(),
            uploadFile: "",
            upload_url:process.env.systemUrl+'/v1/gift/imageUpload',
            form: {
                id:0,
                name: "",
                explain: "",
                point:0,
                inventory:0,
                // inventoryNew:0,//新数量
                // inventoryOld:0,//历史数量
                price:0,
                expressFee:0,
                introduce:'',
                imagelist:[],
                small:-1,
                cover:-1
            },
            small:-1,
            cover:-1,
            nav: [
                { nav: "礼品管理", url: "/imall/gift/index" },
                { nav: "礼品编辑", url: "" }
            ],
            rules:{
                name: [
                    { required: true, message: '请输入礼品名称', trigger: 'blur' },
                    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
                ],
                point:[
                    { required: true, message: '请输入所需积分', trigger: 'blur' },
                    { pattern: /^[0-9]*[1-9][0-9]*$/, message: '只能输入正整数' }
                ],
                inventory:[
                    { required: true, message: '请输入库存',trigger: 'blur,change'},
                    { pattern: /^[0-9]*[1-9][0-9]*$/, message: '只能输入正整数' }
                ],
                price:[
                    { required: true, message: '请输入价值', trigger: 'blur' },
                    { validator:function(rule,value,callback){            //一层校验
                            if(value==0){
                                callback(new Error("请输入大于0数"));
                            }else{
                                callback();
                            }
                        }, trigger: 'blur'},
                    { pattern: /^\d+(?:\.\d{1,2})?$/, message: '输入带2位小数的数字' } ////2层校验
                ],
                expressFee:[
                    { required: true, message: '请输入配送费用', trigger: 'blur' },
                    { pattern: /^[0-9]/, message: '只能输入整数' }
                ],
            }
        };
    },
    created() {
        this.getData()
        // this.small=this.form.small
        // this.cover=this.form.cover
    },
    filters:{
        filter(value){
            if(value){
                return process.env.sourceBaseUrl+"/"+value;
            }
        }
    },
    // watch:{
    //     'small':'changeSmall',
    //     'cover':'changeCover'
    // },
    methods: {
        changeSmall(file){//缩略图修改
            let that=this
            var params={
                id:file.id,
                giftId:this.giftId
            };
            this.mallApi.updateGiftSmall(params).then(data => {
                if (data.code==200){
                    that.$message({
                        duration:1000,
                        center: true,
                        showClose: true,
                        message: '缩略图操作成功',
                        type: 'success'
                    });
                    //that.getData();
                }else{
                    that.$message({
                        duration:1000,
                        center: true,
                        showClose: true,
                        message: '缩略图操作异常',
                        type: 'warning'
                    });
                }
            })
        },
        changeCover(file){//封面修改
            let that=this
            var params={
                id:file.id,
                giftId:this.giftId
            };
            this.mallApi.updateGiftCover(params).then(data => {
                if (data.code==200){
                    that.$message({
                        duration:1000,
                        center: true,
                        showClose: true,
                        message: '封面操作成功',
                        type: 'success'
                    });
                    //that.getData();
                }else{
                    that.$message({
                        duration:1000,
                        center: true,
                        showClose: true,
                        message: '封面图操作异常',
                        type: 'warning'
                    });
                }
            })
        },
        beforeUpload(file){
                this.imageData.giftId=this.giftId;
                this.imageData.cover=0;
                this.imageData.small=0;
                if(this.form.images){
                    this.imageData.order=this.form.images.length+1;
                }
        },
        onSuccess(response, file, fileList){
            console.log(response.data)
            this.imageData={}
            this.imageData.fileName=response.data.name;
            this.imageData.id=response.data.id;
            this.form.images.push(this.imageData);
        },
        handleFileRemove(file,index){//删除图片
            console.log(file)
            console.log(this.form.images)
            let that = this;
            this.$confirm('删除此图片？','提示',{
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var params={
                    id:file.id,
                    fileName:this.fileName
                };
                this.mallApi.deleteGiftImageById(params).then(data => {
                    if (data.code==200){
                        that.$message({
                            duration:1000,
                            center: true,
                            showClose: true,
                            message: '删除成功',
                            type: 'success'
                        });
                        if (index==that.form.cover){
                            that.form.cover=-1;
                        }
                        console.log(index+':'+that.form.small);
                        if (index==that.form.small){
                            that.form.small=-1;
                        }
                        that.form.images.splice(index,1);

                        if(file.small==1){  //删除的是主缩略图
                            var params={
                                id:that.form.images[0].id,
                                giftId:this.giftId
                            };
                            this.mallApi.updateGiftSmall(params).then(data => {
                               console.log("自动更改缩略图成功")
                            })
                        }
                    }else{
                        that.$message({
                            duration:1000,
                            center: true,
                            showClose: true,
                            message: '删除异常',
                            type: 'warning'
                        });
                    }
                })
                
            }).catch((meg) => console.log(meg))
        },
        radioSmallClick(picSmall,index){
            if(this.form.picSmall==index){
                this.form.picSmall='';
            }
        },
        onChange(file, fileList){
            console.log(URL.createObjectURL(file.raw)) 
            this.form.images.push({
                url: URL.createObjectURL(file.raw),
                name:file.name,
                uid: file.uid
            })
            //URL.createObjectURL(item.raw)
        },
        getData() {
            this.loading = true
            let that=this;
            let id=this.$route.params.id;
            var params={id:id};
            this.mallApi.getGift(params).then(data => {
                that.form=data.data;
                // that.form.inventoryOld=that.form.inventory;
                // that.form.inventoryNew=parseInt(that.form.inventory);
                for(let [index,item] of that.form.images.entries()){
                    if (item.small==1){
                        that.small=index;
                    }
                    if (item.cover==1){
                        that.cover=index;
                    }
                }
                that.loading = false
            })
        },
        save(){
            this.$refs['ruleForm'].validate((valid) => {
                if(valid){
                    let that=this;
                    // this.form.inventory=this.form.inventoryNew-this.form.inventoryOld;
                    this.mallApi.updateGift(this.form).then(data => {
                        if (data.code == 200) {
                            this.$notify({
                                title: '成功',
                                message: data.message,
                                type: 'success',
                                duration: 1000
                            })
                            //this.$emit('childMethod', {})//调用父页面刷新
                            this.$router.push('/imall/gift/index')
                        } else {
                            this.$notify({
                                title: '失败',
                                message: data.message,
                                type: 'fail',
                                duration: 1000
                            })
                        }
                    }).catch(err => {})
                }
            })


        }
    }
};
</script>
<style type="text/css" scoped>
.el-upload--picture-card{
    width: 120px !important;
    height: 180px !important;
    line-height: 180px !important;
}
*{
	box-sizing: border-box;
}
.img-list{
	overflow:hidden;
	width:100%;
}
.img-list .img-content{
	float:left;
	text-align:left;
	position:relative;
	display:inline-block;
	width:120px;
	height:180px;
	padding:5px;
	margin:5px 20px 20px 0;
	border:1px solid #d1dbe5;
	border-radius:4px;
	transition:all .3s;
	box-shadow:0 2px 4px 0 rgba(0,0,0,.12), 0 0 6px 0 rgba(0,0,0,.04);
}
.img-list img{
    width:120px;
	height:120px;
}
.img-list .img-upload{
	float:left;
	width:120px;
	height:180px;
	display:table;
	text-align:center;
}
.img-list .uploader{
	width:100%;
	display:table-cell;
	vertical-align:middle;
}
.img-list .img-progress{
	text-align:center;
	padding-top:50px;
    display: none
}
.img-list .img-content img{
	display:block;
	width:100%;
	height:138px;
	margin:0 auto;
	border-radius:4px;
}

.img-list .img-content .name{
	margin-top:5px;
    line-height:22px;
}

.img-list .img-content .name>div{
	width:90%;
	text-overflow:ellipsis;
	overflow:hidden;
	
}
.img-list .img-content:hover .del,
.img-list .img-content:hover .layer{
	opacity:1;
}
.img-list .img-content:hover{
    border:1px solid #FF6419;
}
.img-list .img-content .del,
.img-list .img-content .layer{
	opacity:1;
	transition:all .3s;
}
.img-list .img-content .del{
	position:absolute;
	bottom:5px;
	right:10px;
	color:#FF6419;
	cursor:pointer;
	font-size:1.1em;
}
.img-list .img-content .layer{
	position:absolute;
	left:0;
	right:0;
	top:0;
	height:120px;
	color:#fff;
	text-align:center;
	z-index:5;
	background-color:rgba(0,0,0,.4);
    display: none
}
.img-list .img-content .layer i{
	font-size:1.6em;
	margin-top:80px;
}
</style>
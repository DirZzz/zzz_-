<template>
  
    <div>
        <v-nav :nav="nav"></v-nav>
        <el-form :model="form" :rules="rules" ref="ruleForm"   style="background-color:#fff;padding:10px;" :v-model="formData" label-width="100px" >
            
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
                <div class="isRequired">*</div>
                <div class="img-list">
                    <div class="img-content" v-for="(item,index) in imagelist" v-bind:key="index">
                        <img :src="item.url">
                        <div class="name" >
                            <el-radio v-model="picSmall" :label="index" small>缩略图</el-radio><br/>
                            <!--<el-radio v-model="picCover" :label="index" small>封面</el-radio><br/>-->
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
                     <el-upload
                        ref="upload"
                        :action="upload_url"
                        :data="form"
                        :show-file-list="false"
                        :on-change="onChange"
                        :auto-upload="false"
                        list-type="picture-card"
                        accept= ".jpg,.png,.bmp,.jpeg"
                        :multiple="true"
                        class="uploadImg"
                        v-if="this.imagelist&&this.imagelist.length<6"
                        >
                        <el-button type="primary">点击上传</el-button>
                    </el-upload>
                    
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
                        <el-input type="number" style="width:180px" v-model="form.inventory"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row >
                <el-col :span="8">
                    <el-form-item label="价值" prop="price">
                        <el-input type="number" style="width:180px" v-model="form.price"></el-input>
                        <span>元</span>
                    </el-form-item>
                </el-col>
                <el-col :span="8" >
                    <el-form-item label="配送费用" prop="expressFee">
                        <el-input type="number" style="width:180px" v-model="form.expressFee"></el-input>
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
import imall from '@/api/imall'
import store from '@/store'

export default {
    name:'gift_create',
    components:{
        Breadcrumb
    },
    data() {
        const validateName = (rule, value, callback) => {
            if (value.length==0) {
                callback(new Error('请输入礼品名称'))
            } else {
                callback()
            }
        }
        return {
            sw:0,
            picSmall:0,
            picCover:0,
            imagelist:[],
            formData:new FormData(),
            uploadFile: "",
            specList:[{color_id:'',style_id:''}],//规格
            upload_url:"aaa",
            giftRules: {
                name: [{ required: true, trigger: 'blur', validator: validateName }]
            },
            form: {
                name: "",
                explain: "",
                point:0,
                inventory:0,
                price:0,
                expressFee:0,
                introduce:''
            },
            nav: [
                { nav: "礼品管理", url: "/imall/gift/index" },
                { nav: "新增礼品", url: "" }
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
                    { required: true, message: '请输入库存', trigger: 'blur' },
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
  methods: {
    onSubmit() {
      console.log("submit!");
    },
    handleFileEnlarge(_url){//放大图片
        console.log(_url)
    },
    handleFileName(file,i){//修改名字
        console.log(file,i)
    },
    handleFileRemove(file,index){//删除图片

        if(!file.url){
            return false;
        }
        let that = this;
        
        this.$confirm('删除此图片？','提示',{
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            console.log(that.$refs.upload)
            that.$refs.upload.uploadFiles.splice(index,1)
            that.imagelist.splice(index,1)
            
            if (that.picSmall==index){
                that.picSmall=0;
            }
            if (that.picCover==index){
                that.picCover=0;
            }

            this.$message({
                type: 'success',
                message: '删除成功',
                onClose: () => {
                    that.imagelist.splice(index,1)
                }
            })
        }).catch((meg) => console.log(meg))
    },
    radioSmallClick(picSmall,index){
        if(this.picSmall==index){
            this.picSmall='';
        }
    },
    // addSpec(){
    //     var data={};
    //     this.specList.push(data);
    // },
    // delSpec(index){
    //     if(this.specList.length>1){
    //         this.specList.splice(index,1);
    //     }
    // },
    onChange(file, fileList){
        console.log(URL.createObjectURL(file.raw)) 
        this.imagelist.push({
            url: URL.createObjectURL(file.raw),
            name:file.name,
            uid: file.uid
        })
        //URL.createObjectURL(item.raw)
    },
    beforeUpload1(file) {
        
        this.imagelist.push({
            url: URL.createObjectURL(file.raw),
            name:file.name,
            uid: file.uid
        })
        console.log(file.url)
        //this.formData.append('file', file)
        //this.formData.append('id',123)
        return true
        //return true // false就是不自动上传，我后来试了发现都一样，都不会自动上传
   },
    save(){
        if(this.imagelist.length==0){
            this.$message.error("请上传图片");
            return
        }
        this.$refs['ruleForm'].validate((valid) => {
            if(valid){
                let that=this;
                this.formData=new FormData()
                this.formData.append("picSmall",this.picSmall)//缩略图
                this.formData.append("picCover",this.picCover)//封印
                this.formData.append("name",this.form.name)
                this.formData.append("explain",this.form.explain)
                this.formData.append("point",this.form.point)
                this.formData.append("inventory",this.form.inventory)
                this.formData.append("price",this.form.price)
                this.formData.append("expressFee",this.form.expressFee)
                this.formData.append("introduce",this.form.introduce)
                //this.formData.append("creator",this.$store.state.user.name)
                for(let [index, obj] of this.$refs.upload.uploadFiles.entries()){
                    this.formData.append('file', obj.raw)
                }
                if(this.sw==0){
                    this.sw=1;
                    this.mallApi.createGift(this.formData).then(data => {
                        if (data.code == 200) {
                            this.sw=0;
                            this.$notify({
                                title: '成功',
                                message: data.message,
                                type: 'success',
                                duration: 1000
                            })
                            //that.formData=new FormData();

                            that.$emit('childMethod', {})//调用父页面刷新
                            //that.$router.replace('/gift/create')
                            //this.router.push({path: '/gift/create'});
                            that.$router.push('/imall/gift/index')
                        } else {
                            this.sw=0;
                            this.$notify({
                                title: '失败',
                                message: data.message,
                                type: 'fail',
                                duration: 1000
                            })
                        }
                    }).catch(err => {})
                }

            }

        });
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
.el-form-item{
    position: relative;
}
.isRequired{
    position: absolute;
    left: -80px;
    top: 0;
    color: #FF0000;
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
	padding:2px;
	margin:5px 5px 5px 0;
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
	height:136px;
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
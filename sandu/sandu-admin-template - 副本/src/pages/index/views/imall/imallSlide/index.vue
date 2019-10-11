<template >
    <div v-loading="loading" :element-loading-text="GLOBAL.loadingText" style="background-color:#fff;padding:10px;margin-top:10px;">
        <fieldset class="field-title">
            <legend>轮播图设置</legend>
        </fieldset>
            
        <div class="img-list">
            <div class="img-content" v-for="(item,index) in images" :key="index">
                <img :src="item.fileName | filter">
                <!-- <div class="name" >
                    <el-radio v-model="small" @change="changeSmall(item)" :label="index" small>缩略图</el-radio><br/>
                    <el-radio v-model="cover" @change="changeCover(item)" :label="index" small>封面</el-radio><br/>
                </div> -->
                <!-- 删除icon -->
                <div class="del" v-if="isPer('points:mall:options:edit')">
                    <i @click="handleFileRemove(item,index)" class="el-icon-delete">删除</i>
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
                :headers="headerToken"
                :show-file-list="false"
                :before-upload="beforeUpload"
                :on-success="onSuccess"
                list-type="picture-card"
                accept= ".jpg,.png,.bmp,.jpeg"
                :multiple="true"
                class="uploadImg1"
                v-if="this.images.length<6&&isPer('points:mall:options:edit')"
                >
                <el-button type="primary">点击上传</el-button>
            </el-upload>
            <!-- <el-dialog title="" :visible.sync="isEnlargeImage" size="large" :modal-append-to-body="false" top="8%" width="60%">
                <img @click="isEnlargeImage = false" style="width:100%;" :src="enlargeImage">
            </el-dialog> -->
        </div>
    </div>
</template>
<script>

import Breadcrumb from '@/components/Breadcrumb'
import imall from '@/api/imall'
import store from '@/store'
import qs from 'qs';
export default {
    name:'gift_create',
    components:{
        Breadcrumb
    },
    data() {
        return {
            headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
            imageData:{order:0,id:0},
            formData:new FormData(),
            uploadFile: "",
            upload_url:process.env.systemUrl+'/v1/imallSlide/imageUpload',
            images:[]
        };
    },
    filters:{
        filter(value){
            return process.env.sourceBaseUrl+"/"+value;
        }
    },
    created() {
        this.getData()
        // this.small=this.form.small
        // this.cover=this.form.cover
    },
    // watch:{
    //     'small':'changeSmall',
    //     'cover':'changeCover'
    // },
    methods: {
        isPer (per) {
            let permiss = qs.parse(sessionStorage.getItem('loginUser'))
            return this.isMainPer(per, permiss)
        },
        beforeUpload(file){
            this.imageData.order=this.images.length+1;
            this.imageData.creator=store.getters.name
        },
        onSuccess(response, file, fileList){
            console.log(response.data)
            this.imageData=response.data;
            this.images.push(this.imageData);
            this.$message({
                duration:1000,
                center: true,
                showClose: true,
                message: '上传成功',
                type: 'success'
            });
        },
        handleFileRemove(file,index){//删除图片
            //console.log(file,i)
            // if(!file.fileimage){
            //     return false;
            // }
            let that = this;
            
            this.$confirm('删除此图片？','提示',{
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var params={id:file.id,fileName:file.fileName};
                imall.deleteSlideById(params).then(data => {
                    if (data.code==200){
                        that.$message({
                            duration:1000,
                            center: true,
                            showClose: true,
                            message: '删除成功',
                            type: 'success'
                        });
                        that.getData();
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

                that.images.splice(index,1)
                
                // if (that.picSmall==index){
                //     that.picSmall=0;
                // }
                // if (that.picCover==index){
                //     that.picCover=0;
                // }

                //this.$message.success("删除成功")
                
            }).catch((meg) => console.log(meg))
        },
        // onChange(file, fileList){
        //     console.log(URL.createObjectURL(file.raw)) 
        //     this.form.images.push({
        //         url: URL.createObjectURL(file.raw),
        //         name:file.name,
        //         uid: file.uid
        //     })
        //     //URL.createObjectURL(item.raw)
        // },
        getData() {
            this.loading = true
            let that=this;
            imall.getSlideList().then(data => {
                that.images=data.list
                that.loading = false
            })
        }
    }
};
</script>
<style type="text/css" scoped>
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
	width:180px;
	height:220px;
	padding:5px;
	margin:5px 20px 20px 0;
	border:1px solid #d1dbe5;
	border-radius:4px;
	transition:all .3s;
	box-shadow:0 2px 4px 0 rgba(0,0,0,.12), 0 0 6px 0 rgba(0,0,0,.04);
}
.img-list img{
    width:180px;
	height:180px;
}
.img-list .img-upload{
	float:left;
	width:180px;
	height:200px;
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
	height:180px;
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
	margin-top:8px;
    margin-left: 2px;
	/* right:10px; */
	color:#FF6419;
	cursor:pointer;
	font-size:0.9em;
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
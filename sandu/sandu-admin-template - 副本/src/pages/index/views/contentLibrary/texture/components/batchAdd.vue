<template>
    <div class="batchAdd">
        <el-dialog width="540px"
                   top="20%"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :before-close="beforeClose"
                   title="批量新增贴图"
                   :visible.sync="batchAdd">
            <div class="batchAddInfo">
                 <div class="batch-text">贴图表格导入：</div>
                 <div class="batchUpload">
                     <el-upload
                             class="upload-demo"
                             ref="upload"
                             :action="upLoadUrl"
                             :headers="headerToken"
                             :on-change="handlePreview"
                             :show-file-list="false"
                             :on-exceed="onExceed"
                             :auto-upload="false"
                             :on-success="onSuccess">
                         <el-button slot="trigger" type="primary" class="lookBtn">浏览</el-button>
                         <span>{{fileName}}</span>
                     </el-upload>
                     <input type="button" value="下载表格模板" class="downLoadBtn" @click="download">
                 </div>
            </div>
            <div class="btn">
                <input type="button" value="确定" class="confirm" @click.self="confirm">
                <input type="button" value="取消" class="cancel" @click.self="beforeClose">
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        name: "batchAdd",
        data(){
            return{
                headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
                upLoadUrl:process.env.baseUrl+'/v1/texture/import',
                batchAdd:false,
                fileName:'',
                file:'',
                sw:0
            }
        },
        methods:{
            isShow(type){
                this.batchAdd=type;
            },
            beforeClose(){
                this.batchAdd=false;
            },
            handlePreview(file) {
                console.log(file);
                if(file.name.indexOf('.xlsx')>-1||file.name.indexOf('.XLSX')>-1){
                    if(file.status){
                        this.fileName=file.name;
                        this.$store.dispatch('texttureName',file.name)
                        this.file=file.raw;
                    }
                }else {
                    this.$message.error("请上传正确的格式如.xlsx或.XLSX");
                }
            },
            onSuccess(file){
                 console.log(file)
            },
            onExceed(files, fileList){ //文件超出限制
               console.log(files,fileList)
            },
            confirm(){
                if(this.file){
                    this.isShow(false);
                    this.$store.dispatch('texttureIsstart',false);
                    this.$store.dispatch('fileLoadingDialog',{ isshow:true,vm:this});
                    if(this.sw==0){
                        this.sw=1;
                        let formdata=new FormData();
                        formdata.append('filename',this.fileName);
                        formdata.append('file',this.file);
                        this.API.textureImport(formdata).then(res=>{
                            this.sw=0;
                            this.$store.dispatch('texttureIsstart',true);
                            if(res.success){
                                this.batchAdd=false;
                            }else {
                                this.$message.error(res.message);
                                this.fileName='';
                                this.$store.dispatch('texttureName','');
                                this.file='';
                                this.$store.dispatch('fileLoadingDialog',{ isshow:false,vm:this});
                            }
                        })
                    }
                }else {
                    this.$message.error('请上传贴图文件');
                }

            },
            download(){
                window.location=process.env.sourceBaseUrl+'/textureTemplate.xlsx';
            }
        }
    }
</script>

<style lang="scss" scoped>
     .batchAdd{
         .batchAddInfo{
             display: flex;
             margin-left: 40px;
             padding-bottom: 20px;
             .batch-text{
                 color: #333333;
                 font-size: 14px;
                 line-height: 32px;
             }
             .batchUpload{
                 margin-left: 20px;
                 .upload-demo{
                     height: 30px;
                 }
                 .lookBtn{
                     width: 110px;
                     height: 30px;
                     border-radius: 15px;
                     color: #ffffff;
                     font-size: 14px;
                     line-height: 0;
                     &:hover{
                        background: #ff7a39;
                     }
                     &:active{
                         background: #fa580a;
                     }
                 }
                 span{
                     color: #ff6419;
                     margin-left: 10px;
                     font-size: 14px;
                 }
                 .downLoadBtn{
                     width: 110px;
                     height: 30px;
                     background-color: #f5f5f5;
                     border-radius: 15px;
                     border: solid 1px #dddddd;
                     color: #333333;
                     font-size: 14px;
                     margin-top: 20px;
                     cursor: pointer;
                     &:hover{
                         background: #ff7a39;
                         color: #fff;
                         border: 0;
                     }
                     &:active{
                         background: #fa580a;
                         color: #fff;
                         border: 0;
                     }
                 }
             }
         }
         .btn{
             border-top: 1px solid #ddd;
             padding-top: 20px;
             padding-bottom: 20px;
             position: relative;
             input{
                 width: 60px;
                 height: 30px;
                 background-color: #ff6419;
                 border-radius: 15px;
                 color: #fff;
                 font-size: 14px;
                 cursor: pointer;
                 &:hover{
                     background: #ff7a39;
                     color: #fff;
                 }
                 &:active{
                     background: #fa580a;
                     color: #fff;
                 }
             }
             .confirm{
                 position: absolute;
                 right: 80px;
             }
             .cancel{
                 background-color: #ffdbc9;
                 color: #ff6419;
                 position: absolute;
                 right: 0;
             }
         }
     }
</style>
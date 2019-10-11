<template>
    <div>
        <header class="header">
            <!-- <p class="navtext">推广信息/业主基础信息/编辑</p> -->
            <v-nav :nav="nav" class="navtext"></v-nav>
            <h1 class="edittitle">
                <span>新增信息</span>
                <div class="right">
                    <img src="../../assets/images/icon/icon_back.png" @click="toback()" style="cursor:pointer;">
                </div>
            </h1>
        </header>
        <section class="messageContent">
            <el-row :gutter="24">
                <el-col :span="12">
                    <ul>
                        <li>平&emsp;&emsp;台：<el-select size="small"></el-select></li>
                        <li>推送对象：<el-select size="small"></el-select></li>
                        <li>推送时间：
                            <el-date-picker
                                    class="pickDate"
                                    v-model="nextTime"
                                    type="date"
                                    size="small"
                                    value-format="yyyy-MM-dd 00:00:00"
                                    placeholder="选择时间">
                            </el-date-picker>
                        </li>
                        <li>推送方式：<el-select></el-select></li>
                        <li>消息标题：<el-select></el-select></li>
                        <li><b>消息内容：</b>
                            <el-input :rows="6"
                                      size="small"
                                      placeholder="请输入内容"
                                      type="textarea"
                                      style="width: 372px;">
                            </el-input>
                        </li>
                        <li>链接文字：
                            <el-input size="small" placeholder="请输入链接文字">
                                <template slot="prepend">Http://</template>
                            </el-input>
                        </li>
                        <li>目标链接：
                            <el-input size="small" placeholder="请输入目标链接地址">
                                <template slot="prepend">Http://</template>
                            </el-input>
                        </li>

                    </ul>
                </el-col>
                <el-col :span="12">
                    <ul>
                        <li><b>消息图片：</b><span class="isHasImg">无真相</span></li>
                        <li> &emsp; &emsp;
                            <el-upload
                                    class="upload-demo"
                                    action="https://jsonplaceholder.typicode.com/posts/"
                                    :file-list="fileList2"
                                    :show-file-list="false"
                                    :on-success="handleAvatarSuccess"
                                    :before-upload="beforeAvatarUpload"
                                    >
                                <el-button size="small" class="el-icon-upload2">点击上传</el-button>
                                <span slot="tip" class="el-upload__tip"> &nbsp;支持JPG、PNG格式，大小3M以内</span>
                            </el-upload>
                        </li>
                    </ul>
                </el-col>
            </el-row>
            <div class="buttonFrame">
                <el-button type="primary" size="small">保存</el-button>
                <el-button size="small">重置</el-button>
            </div>
        </section>

    </div>
</template>

<script>
    export default {
        name: "message-add",
        data(){
            return{
                nextTime:'',
                nav: [
                    {nav: '系统推送消息', url: 'message/systemMessage'},
                    {nav: '推送消息新增', url: ''},
                    {nav: '新增信息', url: ''}
                ],
                fileList2: [
                    {
                        name: 'food.jpeg',
                        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
                    },
                    {
                        name: 'food2.jpeg',
                        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
                    }
                    ]
            }
        },
        methods:{
            handleAvatarSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw);
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';
                const isLt2M = file.size / 1024 / 1024 < 3;

                if (!isJPG || !isPNG) {
                    this.$message.error('上传图片只能是 JPG 或 PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 3MB!');
                }
                return (isJPG || isPNG) && isLt2M;
            }
        }
    }
</script>

<style lang="scss" scoped>
    .header{
        width: 100%;
        height: 100px;
        background: white;
        .navtext{
            text-align: left;
            color: #999999;
            font-size: 14px;
            padding: 20px 0 0 32px;
        }
        .edittitle{
            text-align: left;
            font-size: 18px;
            color: #333333;
            padding: 20px 0 0 32px;
            position: relative;
            .right{
                position: absolute;
                right: 32px;
                top: 15px;
            }
        }
    }
    .messageContent{
        margin: 20px;
        padding:20px 0;
        text-align: left;
        background: #fff;
        display: block;
        height: 100%;
        min-height: 700px;
        ul{
            margin: 20px 144px;
        }
        li{
            font-size: 14px;
            color: #333;
            margin-bottom:25px;
            .el-date-picker,
            .el-select,
            .el-input{
                width: 372px;
            }
            b{
                vertical-align:top;
            }
        }
    }
    .isHasImg{
        display: inline-block;
        background: #f5f5f5;
        width: 372px;
        height: 220px;
        font-size: 32px;
        text-align: center;
        line-height: 220px;
        color: #e1e1e1;
        border: solid 1px #e8e8e8;
    }
    .buttonFrame{
        padding-top: 40px;
        padding-bottom:15px;
        border-top: solid 1px #e8e8e8;
        text-align: center;
        background: #fff;
    }
    .upload-demo{
        margin-left: 70px;
    }
</style>
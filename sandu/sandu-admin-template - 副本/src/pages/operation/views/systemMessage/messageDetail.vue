<template>
    <div>
        <header class="header">
            <!-- <p class="navtext">推广信息/业主基础信息/编辑</p> -->
            <v-nav :nav="nav" class="navtext"></v-nav>
            <h1 class="edittitle">
                <span>详情</span>
                <div class="right">
                    <img src="../../assets/images/icon/icon_back.png" @click="toback()" style="cursor:pointer;">
                </div>
            </h1>
        </header>
        <section class="messageContent">
            <ul>
                <li>平&emsp;&emsp;台：</li>
                <li>推送对象：</li>
                <li>推送时间：</li>
                <li>推送方式：</li>
                <li>消息标题：</li>
                <li>消息内容：</li>
                <li>链接文字：</li>
                <li>目标链接：</li>
                <li><b>消息图片：</b><span class="isHasImg">无真相</span></li>
            </ul>
        </section>
    </div>
</template>

<script>
    export default {
        name: "messageDetail",
        data(){
            return{
                nav: [
                    {nav: '系统推送消息', url: 'message/systemMessage'},
                    {nav: '推送消息管理', url: 'message/systemMessage'},
                    {nav: '详情', url: ''}
                ],
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
        padding:20px 144px;
        text-align: left;
        background: #fff;
        display: block;
        height: 100%;
        min-height: 700px;
        ul{
            margin-top: 20px;
        }
        li{
            font-size: 14px;
            color: #333;
            margin-bottom: 30px;
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
</style>
<template>
    <div class="checkanswer" @scroll="handScroll" ref='box'>
        <div class="back" @click="goback"><b>返回上一级</b>/查看答卷</div>
        <headers :title="title"></headers>
        <div class="info">
            <span>回答人: {{creator || '匿名用户'}}</span>
            <span>手机号：　{{mobile || '--'}}</span>
        </div>
        <div class="list_item" v-for="(item, index) in list" :key="index">
            <div class="title">
                问题{{index+1}}：　 {{item.askItem}}
            </div>
            <div class="answer">
                答案：　<span v-for="(sitem, sindex) in item.answers"
                    :key="sindex">{{item.askType==2?sitem.answerContent:sitem.answerItem+'　'}}</span>
            </div>
        </div>
        <backTop v-if="backTopShow" @backTop="backTop"></backTop>
    </div>
</template>

<script>
    import backTop from "./../backTop/index";
    import headers from "./../../content/headers";
    export default {
        components: {
            headers,
            backTop
        },
        props: {
            curitem: {}
        },
        data() {
            return {
                title: '查看答卷',
                list: [],
                backTopShow: false,
                mobile: null,
                creator: null
            }
        },
        created() {
            this.getDatalist();
        },
        methods: {
            backTop() {
                this.$refs.box.scrollTop = 0;
                this.backTopShow = false;
            },
            handScroll(){
                if(this.$refs.box.scrollTop>=500) {
                    this.backTopShow = true;
                }
            },
            getDatalist() {
                this.API.getNaireInfo({
                    userId: this.curitem.userId
                }).then(res => {
                    if (res.success) {
                        this.list = res.obj.infors;
                        this.mobile = res.obj.mobile || '--';
                        this.creator = res.obj.creator || '匿名用户';
                    }else {
                        this.mobile = '--';
                        this.creator = '匿名用户';
                    }
                })
            },
            goback() {
                this.$emit('closeCheckAnaser')
            }
        }
    }
</script>

<style lang="scss" scoped>
    .checkanswer {
        position: fixed;
        width: 100%;
        height: 100%;
        top: 68px;
        background: #fff;
        z-index: 10;
        overflow-y: auto;
        padding-bottom: 100px;

        .back {
            height: 40px;
            line-height: 40px;
            text-align: left;
            padding-left: 40px;
            background: #fff;
            color: #666;
            cursor: pointer;
            font-size: 14px;

            b {
                font-weight: bold;
                color: #333;
            }

            b:hover {
                color: #3399ff;
            }
        }

        .info {
            margin-top: 20px;
            text-align: left;

            span {
                color: #333;
                margin-left: 40px;
            }
        }

        .list_item {
            margin-top: 20px;

            .title,
            .answer {
                height: 40px;
                line-height: 40px;
                color: #333;
                padding-left: 40px;
                text-align: left;
            }
        }
    }
</style>
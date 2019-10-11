<template>
    <transition name="zoomIn">
        <div class="recommendPlan">
            <div class="recommend-header">导航设置<span>（*必填项）</span></div>
            <div class="recommend-content">
                <p>导航标题不超过6个字，4个字为最佳，上下移动可进行排序，图标建议尺寸：88x88像素</p>
                <div class="nav-item" v-for="(item,index) in navList" :key="index" v-dragging="{ item: item, list: navList, group: 'navbox' }">
                    <div class="nav-left">
                        <img :src="item.imageUrl" alt="">
                        <div class="default-pic" @click="useDefault(item,index)">点击使用默认图</div>
                        <upload @success="uploadSuccess">
                            <i @click="replace(index)">替换</i>
                        </upload>
                    </div>
                    <div class="nav-middle">
                        <span><em>*</em>标题：</span>
                        <input type="text" v-model="item.title" class="input-title" maxlength="6">
                    </div>
                    <div class="nav-right">
                        <el-switch
                                v-model="item.show"
                                active-color="#f16321"
                                inactive-color="#999999">
                        </el-switch>
                        <span>{{ item.show ? '显示在导航' : '不显示在导航'}}</span>
                    </div>
                </div>
            </div>
            <div class="bottom">
                <div class="btn" @click="save">保存</div><span>(新增、修改、删除操作后，请及时保存。)</span>
            </div>
        </div>
    </transition>
</template>

<script>
    import upload from './upload'
    import { mapState,mapActions} from 'Vuex'
    import {filter,findKey} from 'lodash'
    export default {
        name: "navSet",
        components:{
            upload
        },
        data(){
            return{
               currentIndex:0,
               imageBox:[
                   { navValue:1, imageUrl:this.BASE_URL.sourceBaseUrl+'/AA/c_basedesign/2019/5/7/17/product/baseProduct/model/20190507170622470.png' },
                   { navValue:2, imageUrl:this.BASE_URL.sourceBaseUrl+'/AA/c_basedesign/2019/5/7/17/product/baseProduct/model/20190507170731274.png' },
                   { navValue:3, imageUrl:this.BASE_URL.sourceBaseUrl+'/AA/c_basedesign/2019/5/7/17/product/baseProduct/model/20190507170822648.png' },
                   { navValue:4, imageUrl:this.BASE_URL.sourceBaseUrl+'/AA/c_basedesign/2019/5/7/17/product/baseProduct/model/20190507170839960.png' },
               ]
            }
        },
        created(){

        },
        watch:{
            moduleTitle(val){
                this.title=val;
            }
        },
        computed:{
            ...mapState('homeManage',['submitPlansList','navList','copyNavList'])
        },
        methods:{
            ...mapActions('homeManage',['setSave']),
            uploadSuccess(file){
               this.navList[this.currentIndex].imageUrl=file.data.url;
            },
            useDefault(item,index){
                this.navList[index].imageUrl=this.copyNavList[findKey(this.copyNavList,['navValue',item.navValue])].imageUrl;
            },
            replace(index){
                this.currentIndex=index;
            },
            save(){
                this.navList.every(res=>{ return res.title }) ?  this.setSave({vm:this}) : this.$message.error('请输入标题');
            }
        }
    }
</script>

<style scoped lang="scss">
    .recommendPlan{
        width: 560px;
        background-color: #fff;
        margin-top: 142px;
        margin-left: 30px;
        .recommend-header{
            width: 100%;
            height: 40px;
            line-height: 40px;
            background-color: #fafafa;
            text-indent: 21px;
            color: #000000;
            font-size: 14px;
            span{
                color: #ff2323;
            }
        }
        .recommend-content{
            padding: 20px 0;
           p{
               font-size: 12px;
               color: #999999;
               text-indent: 16px;
           }
            .nav-item{
                width: 505px;
                height: 126px;
                background-color: #ededed;
                border-radius: 2px;
                border: solid 1px #dddddd;
                display: flex;
                align-items: center;
                margin: 10px auto 0 auto;
                justify-content: space-around;
                .nav-left{
                    text-align: center;
                    position: relative;
                    img{
                        width: 66px;
                        height: 66px;
                    }
                    .default-pic{
                        color: #295ee4;
                        font-size: 12px;
                        cursor: pointer;
                    }
                    i{
                        display: inline-block;
                        width: 42px;
                        height: 20px;
                        background-color: #999999;
                        border-radius: 2px;
                        position: absolute;
                        top: 0;
                        font-style: normal;
                        color: #fff;
                        left: 9px;
                        font-size: 12px;
                        line-height: 20px;
                    }
                }
                .nav-middle{
                    span{
                        font-size: 14px;
                        em{
                            color: #ff2323;
                        }
                    }
                    .input-title{
                        width: 131px;
                        height: 36px;
                        background-color: #ffffff;
                        border-radius: 2px;
                        border: solid 1px #dddddd;
                        text-indent: 14px;
                    }
                }
                .nav-right{
                    width: 120px;
                    span{
                        font-size: 12px;
                    }
                }
            }

        }
        .bottom{
            height: 56px;
            border-top: 1px solid #dddddd;
            display: flex;
            span{
                color: #ff6449;
                margin-top: 18px;
                margin-left: 10px;
            }
            .btn{
                width: 60px;
                height: 30px;
                background-color: #ff6419;
                border-radius: 15px;
                color: #ffffff;
                font-size: 14px;
                line-height: 30px;
                text-align: center;
                margin-left: 40px;
                margin-top: 14px;
                cursor: pointer;
                &:active{
                    opacity: .6;
                }
            }
        }
    }
</style>
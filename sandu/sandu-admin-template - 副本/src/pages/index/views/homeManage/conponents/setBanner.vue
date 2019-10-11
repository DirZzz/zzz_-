<template xmlns:v-clipboard="http://www.w3.org/1999/xhtml">
    <transition name="zoomIn">
        <div class="setBanner">
            <div class="setBanner-header">Banner设置<span>（*必填项）</span></div>
            <div class="setBanner-content">
                <p>最多可添加10个banner，上下移动进行排序</p>
                <ul>
                    <li v-for="(item,index) in bannerList" :key="index" v-if="bannerList && bannerList.length<=10" v-dragging="{ item: item, list: bannerList, group: 'filek' }">
                        <div class="box">
                            <div class="upLoadImg">
                                <img :src="item.picAddress" alt="">
                                <upload @success="replaceSuccess">
                                    <div class="replace" @click="currentIndex=index">替换</div>
                                </upload>

                            </div>
                            <div class="left">
                                <span class="toPath" @mouseenter="mouseHover(item,index,true)" @mouseleave="mouseHover(item,index,false)">跳转链接<i>?</i>：</span>
                                <textarea class="inputPath" placeholder="请输入" v-model="item.linkToAddress"></textarea>
                                <div class="dialog-tip" v-show="item.dialogTip" @mouseenter="mouseHover(item,index,true)" @mouseleave="mouseHover(item,index,false)">
                                    <em></em>
                                    <em></em>
                                    <div class="dialog-state">说明：【跳转链接】是指点击banner跳转的页面链接地址</div>
                                    <div class="dialog-text">常用链接 :   </div>
                                    <div class="dialog-link" v-for="itemChild in linkList">
                                        <div class="dialog-name"><span>{{itemChild.title}}: </span><span v-clipboard:copy="itemChild.url" v-clipboard:success="onCopy" v-clipboard:error="onError">复制</span></div>
                                        <div class="dialog-address">{{itemChild.url}}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <i class="ic-close" @click="deleteList(index)"></i>
                    </li>
                    <div class="add-img" v-if="bannerList.length<10">
                        <upload @success="uploadSuccess">
                            <div class="default-upload">
                                <p>+添加一个Banner图</p>
                                <p>尺寸： 750×400 像素</p>
                            </div>
                        </upload>
                    </div>
                </ul>
                <div class="bottom">
                    <div class="btn" @click="save">保存</div><span>(新增、修改、删除操作后，请及时保存。)</span>
                </div>
            </div>
        </div>
     </transition>
</template>

<script>
    import upload from './upload'
    import { mapState,mapActions} from 'Vuex'
    export default {
        name: "setBanner",
        components:{
            upload
        },
        data(){
            return{
                linkList:[
                    {title:'商品分类' ,url:'/pages/house-goods/house-goods' },
                    {title:'新品', url:'/pages/goods-list/goods-list?type=presellList&code=root' },
                    {title:'特卖', url:'/pages/goods-list/goods-list?type=specialOfferl&code=root' },
                    {title:'推荐方案', url:'/pages/house-case/house-case' },
                    {title:'领券中心', url:'/pages/storeCoupon/storeCoupon' },
                    {title:'线下门店', url:'/pages/mini-offcial-web/mini-offcial-web' },
                    {title:'砍价免费拿', url:'/pages/activeKj/activeKj' },
                    {title:'超值拼团', url:'/pages/activePt/activePt' }
                ],
                currentIndex:'' //当前替换的index
            }
        },
        mounted(){
            this.$dragging.$on('dragged', (value) => {
                //console.log(value)
                //因为我这在排序的时候不需要做处理，所以空着就行
            })
        },
        computed:{
            ...mapState('homeManage',['bannerList'])
        },
        methods:{
            ...mapActions('homeManage',['setBannerList','setSave']),
            uploadSuccess(res){
                this.bannerList.length<10 ?  this.bannerList.push({
                    id:res.data.resId,
                    picAddress:res.data.url,
                    dialogTip:false,
                    linkToAddress:''
                }) : ''
                this.setBannerList(this.bannerList)
            },
            mouseHover(item,index,ishover){
                item.dialogTip=ishover;
                this.$set(this.bannerList,index,this.bannerList[index]);
            },
            replaceSuccess(res){
                res.data.id=res.data.resId;
                res.data.picAddress=res.data.url;
                Object.assign(this.bannerList[this.currentIndex],res.data);
                console.log(this.bannerList,'sss')
                this.setBannerList(this.bannerList);
            },
            deleteList(index){
                this.bannerList.splice(index,1);
                this.setBannerList(this.bannerList);
            },
            onCopy(){ //复制成功
                this.$message.success('复制成功');
            },
            onError(){ //复制失败
                this.$message.success('复制失败');
            },
            save(){
                if(this.bannerList.length<=0){ this.$message.error('请至少上传一张图片');return }
                this.setSave({vm:this});
            }
        }
    }
</script>

<style scoped lang="scss">
 .setBanner{
     width: 540px;
     background-color: #fff;
     margin-top: 142px;
     margin-left: 30px;
     .setBanner-header{
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
     .setBanner-content{
         p{
             color: #999999;
             font-size: 14px;
             line-height: 48px;
             text-indent: 39px;
         }
         ul{
             padding-bottom: 30px;
             li{
                 width: 460px;
                 height: 122px;
                 background-color: #fafafa;
                 border-radius: 2px;
                 border: solid 1px #dddddd;
                 padding: 16px;
                 box-sizing: border-box;
                 margin: 0 auto 10px auto;
                 position: relative;
                  .box{
                      display: flex;
                 }
                 .upLoadImg{
                     width: 160px;
                     height: 90px;
                     position: relative;
                     img{
                         width: 160px;
                         height: 90px;
                     }
                     .replace{
                         position: absolute;
                         top: 5px;
                         right: 5px;
                         width: 48px;
                         height: 24px;
                         line-height: 24px;
                         background-color: #dbdbdb;
                         border-radius: 4px;
                         font-size: 14px;
                         color: #333333;
                         text-align: center;
                         cursor: pointer;
                         &:hover{
                             background-color: #ff6419;
                             color: #fff;
                         }
                     }
                 }

                 .left{
                     margin-left: 16px;
                     position: relative;
                     .toPath{
                         display: inline-block;
                         height: 28px;
                         color: #666666;
                         font-size: 14px;
                         i{
                             font-style: normal;
                             display: inline-block;
                             width: 16px;
                             height: 16px;
                             border-radius: 100%;
                             line-height: 16px;
                             border: solid 1px #dddddd;
                             text-align: center;
                             margin: 0 5px;
                             cursor: pointer;
                             &:hover{
                                 color: #ff6419;
                                 border: solid 1px #ff6419;

                             }
                         }
                     }
                     .inputPath{
                         width: 259px;
                         height: 64px;
                         background-color: #ffffff;
                         border: solid 1px #e8e8e8;
                         padding: 10px;
                         box-sizing: border-box;
                         resize:none;
                         &::placeholder{
                             color: #999999;
                             font-size: 12px;
                         }
                     }
                     .dialog-tip{
                         width: 388px;
                         background-color: #ffffff;
                         position: absolute;
                         top: 26px;
                         left: 0;
                         padding: 14px;
                         box-sizing: border-box;
                         box-shadow: 0 4px 9px 0
                         rgba(39, 39, 39, 0.21);
                         border: solid 1px #dddddd;
                         z-index: 10;
                         em:nth-of-type(1){
                             position: absolute;
                             top: -10px;
                             left: 60px;
                             width:0;
                             height:0;
                             border-left: 8px solid transparent;
                             border-right: 8px solid transparent;
                             border-bottom: 8px solid #dddddd;
                         }
                         em:nth-of-type(2){
                             position: absolute;
                             top: -8px;
                             left: 60px;
                             width:0;
                             height:0;
                             border-left: 8px solid transparent;
                             border-right: 8px solid transparent;
                             border-bottom: 8px solid #fff;
                         }
                         .dialog-state{
                             color: #666666;
                             font-size: 14px;
                         }
                         .dialog-text{
                             margin: 8px 0;
                         }
                         .dialog-link{
                             width: 360px;
                             height: 71px;
                             background-color: #f1f1f1;
                             padding: 8px;
                             box-sizing: border-box;
                             margin: 0 auto;
                             .dialog-name{
                                 color: #333333;
                                 font-size: 14px;
                                 position: relative;
                                 span:nth-of-type(2){
                                     position: absolute;
                                     right: 0;
                                     color: #ff6419;
                                     font-size: 14px;
                                     cursor: pointer;
                                     &:active{
                                         opacity: .6;
                                     }
                                 }
                             }
                         }
                     }
                 }
                 .ic-close{
                     display: inline-block;
                     position: absolute;
                     top: -10px;
                     right: -10px;
                     width: 20px;
                     height: 20px;
                     background: no-repeat center url("../../../assets/images/ic_delete.png");
                     background-size: 70%;
                     cursor: pointer;
                 }
             }

             .add-img{
                 width: 460px;
                 height: 72px;
                 background-color: #fafafa;
                 border-radius: 2px;
                 border: solid 1px #dddddd;
                 padding: 20px 0;
                 box-sizing: border-box;
                 margin: 10px auto 0 auto;
                 .default-upload{
                     margin-left: 130px;
                     p:nth-of-type(1){
                         color: #ff6449;
                         text-align: center;
                         font-size: 14px;
                         line-height: 16px;
                     }
                     p:nth-of-type(2){
                         color: #999999;
                         text-align: center;
                         font-size: 12px;
                         line-height: 24px;
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
 }
</style>

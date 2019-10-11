<template>
    <el-dialog
            title="套餐选择"
            :before-close="beforeClose"
            :visible.sync="gettersChoosePackage"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            width="990px" top="20vh">
        <div class="choosePackage" v-if="choosePackageList.datalist">
            <div class="title">套餐选择</div>
            <el-carousel indicator-position="outside" :autoplay="false" @change="changePage" ref="swippers">
                <el-carousel-item v-for="item in totalCount" :key="item">
                    <ul class="clearfix">
                        <li v-for="(item,key) in choosePackageList.datalist" :key="key" class="fl">
                            <div class="package-bg">
                                <div class="packageName">{{item.servicesName}}</div>
                                <input type="button" class="toBuy" :class="{'active':item.active}" value="立即购买" @click.self="toBuy(item.id)">
                            </div>
                            <div class="tip-box">{{item.serviceDesc}}</div>
                        </li>
                    </ul>
                </el-carousel-item>
            </el-carousel>

        </div>
    </el-dialog>

</template>

<script>
    import { mapGetters} from "Vuex"
    import myVue from '@/filters/bus'
    export default {
        
        name: "choosePackage",
        data(){
            return{
                choosePackageList:[],
                isStart:false
            }
        },
        computed:{
            ...mapGetters(['gettersChoosePackage','gettersUserId']),
            totalCount(){
                if(this.choosePackageList.totalCount<=4){
                    return 1
                }else{
                    return Math.ceil(this.choosePackageList.totalCount/4)
                }
            }
        },
        created(){
            myVue.$on('choosePackage',(e)=>{
                this.main(1);
                this.isStart=true;
            })
        },
        mounted(){

        },
        methods:{
            setActiveItem(val){
             console.log(val)
            },
            main(pageNo){
                this.API.chooseList({ userId:this.gettersUserId,saleChannel:3,pageNo:pageNo,pageSize:4 }).then(res=>{
                    if(res.success){
                        this.choosePackageList=res;
                        if(this.isStart){
                            this.$store.dispatch('dialog',{ isShow:false,name:'dialogBuy'});
                            setTimeout(()=>{
                                this.$store.dispatch('dialog',{ isShow:true,name:'choosePackage'});
                            },200)
                        }
                    }else {
                        this.$message(res.message);
                    }

                })
            },
            toBuy(id){
                this.$store.dispatch('packageAction',{vm:this, id:id,userId:this.gettersUserId});
                // this.$store.dispatch('setServiceId',id);
            },
            changePage(pageNo){
                this.isStart ? '': this.main(pageNo+1);
                this.isStart=false;
            },
            beforeClose(){
               this.$store.dispatch('dialog',{isShow:false,name:'choosePackage'});
            },
        }
    }
</script>

<style lang='scss' scoped>
    .choosePackage{
        .title{
            color: #333333;
            font-size: 18px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
        }
        ul{
            li{
                width: 220px;
                height: 300px;
                border: 1px solid rgba(0,0,0,0.05);
                box-sizing: border-box;
                margin-left: 13px;
                .package-bg{
                    background-color: #fafafa;
                    padding: 30px;
                    min-height: 80px;
                    position: relative;
                    .packageName{
                        text-align: center;
                        color: #333333;
                        font-size: 16px;
                    }
                }

            }
            .toBuy{
                position: absolute;
                left: 50%;
                bottom: 20px;
                display: block;
                margin-left: -45px;
                width: 90px;
                height: 30px;
                line-height: 30px;
                background-color: #ffffff;
                border-radius: 15px;
                border: solid 1px #ff6419;
                color: #ff6419;
                font-size: 12px;
                cursor: pointer;
                &:hover{
                    background:#ff6419;
                    color: #fff;
                }
            }
            .active{
                background:#ff6419;
                color: #fff;
            }
            .tip-box{
                margin-top: 50px;
                padding: 0 20px;
                text-align: justify;
                word-break: break-all;
            }
        }
    }

</style>
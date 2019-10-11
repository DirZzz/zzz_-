<template>
    <transition name="zoomIn">
        <div class="hotAct">
            <div class="hotAct-header">热门活动<span>（*必填项）</span></div>
            <div class="hotAct-content">
                <div class="hotAct-goods">
                    <div class="hotAct-title">
                        <span><em>*</em>标题：</span>
                        <input type="text" maxlength="6" :value="moduleTitle" @input="changeInput" class="hotAct-input">
                        <span class="hotAct-tip">不超过6个字，4个字为最佳展示效果</span>
                    </div>
                    <div class="act-tip">最多不超过6个，上下移动可对其排序，图片尺寸建议为：335X240像素</div>
                    <ul>
                        <li v-for="(item,key) in actImgList" :key="key" v-dragging="{ item: item, list: actImgList, group: 'actList' }">
                            <span><em>*</em>请选择活动类型：</span>
                            <el-select v-model="selectArr[key]" placeholder="请选择" class="act-select" @change="selectChange" @visible-change="visibleChange(key)">
                                <el-option
                                        v-for="(actItem,index) in actList"
                                        :key="actItem.value"
                                        :label="actItem.name"
                                        :value="actItem.value">
                                </el-option>
                            </el-select>
                            <div class="upload-img">
                                <template v-if="item.miniProImage">
                                    <img :src="item.miniProImage" alt="" class="select-img">
                                    <div class="tap-default" @click="tapDefault(item,key)">点击使用默认图</div>
                                    <upload @success="uploadSuccess">
                                        <i @click="replace(key)">替换</i>
                                    </upload>
                                </template>
                                <img src="../../../assets/images/zhanweitu.png" alt="" class="default-img" v-else>
                            </div>
                            <i class="ic-close" @click="deleteAct(key)"></i>
                        </li>
                        <div class="add-act" @click="addAct">+添加活动</div>
                    </ul>
                </div>
            </div>
            <div class="bottom">
                <div class="btn" @click="save">保存</div><span>(新增、修改、删除操作后，请及时保存。)</span>
            </div>
        </div>
    </transition>
</template>

<script>
    import { mapState,mapActions} from 'Vuex'
    import {find,filter,map,union,findKey,cloneDeep,uniq,flatten} from 'lodash'
    import upload from './upload'
    export default {
        name: "hotAct",
        components:{
            upload
        },
        data(){
            return{
                title:'',
                actType:'',
                chooseAct:false,
                imgList:[
                    //{type:1}
                ],
                currentIndex:'',
                actIndex:'',
            }
        },
        created(){

        },
        mounted(){
            this.$dragging.$on('dragged',({value})=>{
                if(value.group=='actList'){
                    this.setSelectArr(map(value.list,'value'))
                }
            })
        },
        computed:{
            ...mapState('homeManage',['actImgList','actList','moduleTitle','moduleType','actDefault','selectArr']),
        },
        methods:{
            ...mapActions('homeManage',['setSave','setActList','setModuleTitle','setSelectArr','setDefauleAct']),
            changeInput(e){
                this.setModuleTitle(e.target.value);
            },
            deleteGoodslist(item){
                this.setSubmitPlansList({res:item,type:0});//从新的数组里删除 0删除
            },
            uploadSuccess(file){
                this.actImgList[this.actIndex].miniProImage=file.data.url;
            },
            replace(index){
                this.actIndex=index;
            },
            tapDefault(item,key){
                this.actImgList[key].miniProImage= filter(this.actDefault,['value',item.value])[0].miniProImage;
            },
            addAct(){
                this.actImgList.push({ isDefault:false });
                this.setActList(this.actImgList);
            },
            visibleChange(key){
                this.currentIndex=key;
            },
            selectChange(val){
                let mod=flatten(map(filter(this.moduleType,['type','act']),'configDetails')); //判断之前的模块的活动 不添加重复的
                let item=filter(this.actList,['value',parseInt(val)])[0];
                //判断之前活动模块是否添加了这个活动
                if(map(this.actImgList,'value').includes(val) || map(mod,'value').includes(val)){
                    this.$message.error('同一活动只能显示一次');
                   //选择第一个活动时 出现的默认清空
                    this.selectArr.length==1 ? this.setSelectArr([]) : this.setSelectArr(uniq(this.selectArr));
                }else {
                    this.actImgList.splice(this.currentIndex,1,item);
                    this.$set(this.actImgList,this.currentIndex,this.actImgList[this.currentIndex])
                }
            },
            deleteAct(index){
                this.actImgList.splice(index,1);
                this.setActList(this.actImgList);
                this.setSelectArr(map(this.actImgList,'value'));
                this.setDefauleAct();
            },
            save(){
                if(this.moduleTitle==null||this.moduleTitle==''){
                    this.$message.error('请输入标题');
                    return
                }else if(this.actImgList.every(res=>{ return res.value })){
                    this.setSave({vm:this,list:cloneDeep(this.actImgList),title:this.moduleTitle});
                }else {
                    this.$message.error('请选择活动');
                }
                //
            }
        }
    }
</script>

<style scoped lang="scss">
    .hotAct{
        width: 540px;
        background-color: #fff;
        margin-top: 142px;
        margin-left: 30px;
        .hotAct-header{
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
        .hotAct-content{
            .hotAct-title{
                height: 36px;
                line-height: 36px;
                text-indent: 40px;
                margin-top: 34px;
                .hotAct-tip{
                    font-size: 14px;
                    color: #999999;
                    margin-left: 10px;
                }
                em{
                    color: #ff2323;
                    margin-right: 5px;
                }
            }
            .act-tip{
                color: #999999;
                font-size: 14px;
                text-indent: 40px;
                line-height: 48px;
            }
            .act-type{
                text-indent: 40px;
                line-height: 48px;
                em{
                    color: #ff2323;
                    margin-right: 5px;
                }
            }
            .hotAct-input{
                width: 180px;
                height: 36px;
                background-color: #fafafa;
                border-radius: 2px;
                border: solid 1px #dddddd;
                text-indent: 16px;
            }
            .hotAct-goods{
                .hotAct-goods-text{
                    line-height: 48px;
                    span:nth-of-type(1){
                        color: #333333;
                        font-size: 14px;
                        margin-left: 40px;
                    }
                    span:nth-of-type(2){
                        color: #999999;
                        font-size: 14px;
                    }
                    em{
                        color: #ff2323;
                        margin-right: 5px;
                    }
                }
                ul{
                    padding-bottom: 30px;
                    li{
                        display: flex;
                        width: 461px;
                        height: 170px;
                        background-color: #fafafa;
                        border-radius: 2px;
                        border: solid 1px #dddddd;
                        align-items: center;
                        box-sizing: border-box;
                        padding: 10px;
                        margin:  0 auto 10px auto;
                        position: relative;
                        span{
                            font-size: 12px
                        }
                        em{
                            color: #ff2323;
                            margin-right: 5px;
                        }
                        .upload-img{
                            text-align: center;
                            width: 151px;
                            height: 108px;
                            background-color: #e9e9e9;
                            position: relative;
                            .default-img{
                                width: 38px;
                                height: 32px;
                                margin-top: 38px;
                            }
                            .select-img{
                                width: 151px;
                                height: 100%;
                            }
                            .tap-default{
                                color: #295ee4;
                                font-size: 12px;
                                cursor: pointer;
                                text-decoration: underline;
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
                                left: 0;
                                font-size: 12px;
                                line-height: 20px;
                                cursor: pointer;
                            }
                        }
                        .act-select{
                            width: 149px;
                            background-color: #ffffff;
                            margin: 0 16px;
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
                    .add-act{
                        width: 461px;
                        height: 60px;
                        background-color: #fafafa;
                        border-radius: 2px;
                        border: solid 1px #dddddd;
                        margin: 0 auto;
                        text-align: center;
                        line-height: 60px;
                        color: #ff6419;
                        cursor: pointer;
                    }
                }
/*                ul{
                    display: flex;
                    flex-wrap: wrap;
                    margin-left: 40px;
                    padding-bottom: 30px;
                    box-sizing: border-box;
                    li{
                        width: 300px;
                        height: 104px;
                        background-color: #fafafa;
                        border-radius: 2px;
                        position: relative;
                        margin-right: 10px;
                        margin-bottom: 10px;
                        .choose-act{
                            position: absolute;
                            left: 80px;
                            bottom: -46px;
                            width: 160px;
                            height: 78px;
                            background-color: #fff;
                            box-shadow: 0px 10px 20px 0px
                            rgba(153, 153, 153, 0.2);
                            border-radius: 4px;
                            border: solid 1px #d9d9d9;
                            p{
                                color: #333333;
                                font-size: 14px;
                                line-height: 36px;
                                text-align: center;
                                &:active{
                                    opacity: .6;
                                }
                            }
                        }
                        img{
                            width: 300px;
                            height: 104px;
                        }
                        .replace{
                            width: 48px;
                            height: 24px;
                            line-height: 24px;
                            background-color: #dbdbdb;
                            border-radius: 4px;
                            color: #333333;
                            font-size: 14px;
                            position: absolute;
                            left: 8px;
                            top: 8px;
                            text-align: center;
                            cursor: pointer;
                            &:hover{
                                background-color: #ff6419;
                                color: #fff;
                            }
                        }
                        .ic-close{
                            display: inline-block;
                            position: absolute;
                            top: -5px;
                            right: -5px;
                            width: 20px;
                            height: 20px;
                            background: no-repeat center url("../../../assets/images/ic_delete.png");
                            background-size: 70%;
                            cursor: pointer;
                        }
                    }
                    li:nth-child(4n){
                        margin-right: 0;
                    }
                    li:last-child{
                        line-height: 106px;
                        color: #ff6419;
                        text-align: center;
                        cursor: pointer;
                    }
                }*/
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
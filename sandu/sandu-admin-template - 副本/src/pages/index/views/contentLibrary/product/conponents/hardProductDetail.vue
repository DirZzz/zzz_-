<template>
    <div class="hardProduct">
        <div class="productBaseInfo">模型相关信息</div>
        <div class="tableBox">
            <div class="header-tab">
                <span>序号</span>
                <span>缩略图</span>
                <span>名称</span>
                <span>编号</span>
                <span>尺寸(cm)</span>
                <span>操作</span>
                <em></em>
            </div>
            <div class="noMsg" v-if="productBaseDetail.hardProductModelInfo.length==0">暂无数据</div>
            <div class="hardProduct-list">
                <ul>
                    <li v-for="(item,index) in productBaseDetail.hardProductModelInfo" :key="index">
                        <div class="hardProduct-top">
                            <span>{{index+1}}</span>
                            <span><img :src="item.thumbPicPath | filter" alt=""></span>
                            <span>{{item.modelName}}</span>
                            <span>{{item.modelCode}}</span>
                            <span>长:{{item.length}}     宽:{{item.width}}     高:{{item.height}}</span>
                            <div class="operation">
                                <p @click="lookInfo(item,index)">查看规格信息</p>
                            </div>
                            <div class="pullDownSection" @click="toggle(item,index)">
                                <div class="pullDownSection-content">
                                    <p v-if="!item.active">展开({{item.textureCount}})<strong></strong></p>
                                    <p v-else style="color: #999">收起({{item.textureCount}})<em></em></p>
                                </div>
                            </div>
                        </div>
                        <div class="hardProduct-bottom" v-show="item.active">
                            <div class="hardProductBox" v-for="(res,key) in item.modelTextureInfo" :key="key">
                                <div class="materialName">{{Number(res.areaCode)+1}}.材质区域名称：<span>{{res.areaName}}</span></div>
                                <div class="upload-pic">
                                    <div class="upload-pic-list" v-for="(texture,val) in res.rels" :key="val">
                                        <template>
                                            <el-popover
                                            placement="top-start"
                                            width="300"
                                            trigger="hover"
                                            >
                                            <div>
                                             <p>材质编码:{{res.rels[val].textureCode}}</p>
                                             <p>材质型号:{{res.rels[val].modelNumber}}</p> 
                                             <p>材质型号:{{res.rels[val].textureName}}</p>
                                            </div>
                                             <img slot="reference" :src="texture.picPath | filter" title="item">
                                            </el-popover>
                                        </template>
                                        <em v-if="val==0">主缩略图</em>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>


    </div>
</template>

<script>
    import { mapGetters } from 'Vuex'
    export default {
        name: "hardProductDetail",
        props:['productBaseDetail'],
        data(){
            return{

            }
        },
        computed:{

        },
        filters:{
            filter(val){
                return process.env.sourceBaseUrl+val;
            }
        },
        methods:{
            toggle(item,index){
                item.active=!item.active;
                this.$set(this.productBaseDetail.hardProductModelInfo,index,this.productBaseDetail.hardProductModelInfo[index]);
            },
            lookInfo(item,index){
                console.log(item);
                this.$store.dispatch('lookProductInfo',{modelId:item.id,modelCode:item.modelCode,textureList:item.props});
                this.$store.dispatch('dialogMdDetail',true);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .hardProduct{
        .productBaseInfo {
            height: 40px;
            line-height: 40px;
            background-color: #f6f6f6;
            border-left: 4px solid #ff6419;
            color: #666;
            font-size: 16px;
            padding-left: 10px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .noMsg{
            text-align: center;
            line-height: 40px;
            color: #999;
        }
        .hardProduct-header{
            height: 60px;
            position: relative;
            padding-left: 20px;
            .productText{
                color: #999999;
                font-size: 14px;
                position: relative;
                top: 15px;
                cursor: pointer;
                i{
                    display: inline-block;
                    width: 14px;
                    height: 14px;
                    background: no-repeat center url("../../../../assets/images/icons/cube.png");
                    background-size: 100%;
                    margin-right: 5px;
                    vertical-align: middle;
                }
                .active{
                    background: no-repeat center url("../../../../assets/images/icons/cube_sct.png");
                    background-size: 100%;
                }
            }
            .active{
                color: #ff6419;
            }
            .connectModel{
                position: absolute;
                right: 20px;
                top: 0;
            }
        }
        .tableBox{
            border: solid 1px #dddddd;
            border-bottom: 0;
            .header-tab{
                display: flex;
                height: 40px;
                line-height: 40px;
                background-color: #fafafa;
                border-radius: 4px 4px 0px 0px;
                border-bottom: solid 1px #dddddd;
                align-items: center;
                span{
                    flex: 1;
                    text-align: center;
                }
                em{
                    display: inline-block;
                    width: 80px;
                    height: 40px;
                }
                i{
                    display: inline-block;
                    width: 14px;
                    height: 14px;
                    background-color: #ffffff;
                    border: solid 1px #ff6419;
                    margin-left: 20px;
                    cursor: pointer;
                }
                .active{
                    background: no-repeat center url('../../../../assets/images/icons/delivery1.png');
                    background-size: 100%;
                }
            }
            .hardProduct-list{
                ul{
                    li{
                        .hardProduct-top{
                            display: flex;
                            height: 132px;
                            line-height: 132px;
                            align-items: center;
                            position: relative;
                            border-bottom: 1px solid #ddd;
                            z-index: 6;
                            i{
                                display: inline-block;
                                width: 14px;
                                height: 14px;
                                background-color: #ffffff;
                                border: solid 1px #ff6419;
                                margin-left: 20px;
                                cursor: pointer;
                            }
                            span,.operation{
                                flex: 1;
                                text-align: center;
                                position: relative;
                                img{
                                    position: absolute;
                                    top: 50%;
                                    left: 50%;
                                    transform: translate(-50%,-50%);
                                    width: 50px;
                                    height: 50px;
                                }
                                .disabledMainModel{
                                    color: #999999;
                                    font-size: 14px;
                                    line-height: 20px;
                                }
                            }
                            p{
                                line-height: 20px;
                                font-size: 14px;
                                color: #ff6419;
                                cursor: pointer;
                            }
                            .pullDownSection{
                                display: inline-block;
                                width: 80px;
                                height: 132px;
                                border-left: 1px solid #dddddd;
                                text-indent: 20px;
                                color: #ff6419;
                                position: relative;
                                cursor: pointer;
                                .pullDownSection-content{
                                    position: absolute;
                                    top: 50%;
                                    left: 50%;
                                    transform: translate(-50%,-50%);
                                    width: 80px;
                                }
                                strong{
                                    position: absolute;
                                    top: 20px;
                                    left: 50%;
                                    display: inline-block;
                                    width: 0;
                                    height: 0;
                                    border-left: 5px solid transparent;
                                    border-right: 5px solid transparent;
                                    border-top: 8px solid #ff6419;
                                }
                                em{
                                    position: absolute;
                                    top: 20px;
                                    left: 50%;
                                    display: inline-block;
                                    width: 0;
                                    height: 0;
                                    border-left: 5px solid transparent;
                                    border-right: 5px solid transparent;
                                    border-bottom: 8px solid #999;
                                }
                            }
                        }
                        .hardProduct-bottom{
                            position: relative;
                            z-index: 5;
                            border-top:1px solid #dddddd;
                            background-color: #fafafa;
                            padding: 20px;
                            .hardProductBox{
                                height: 178px;
                                background-color: #fff;
                                border:1px solid #dddddd;
                                padding: 20px;
                                box-sizing: border-box;
                                .materialName{
                                    font-size: 14px;
                                    color: #333333;
                                    span{
                                        width: 100px;
                                        background-color: #ffffff;
                                        border-radius: 4px;
                                        border: solid 1px #dddddd;
                                        color: #999999;
                                        font-size: 14px;
                                        padding: 2px 5px;
                                    }
                                    em{
                                        font-style: normal;
                                        width: 84px;
                                        background-color: #ffffff;
                                        border-radius: 15px;
                                        border: solid 1px #ff6419;
                                        color: #ff6419;
                                        padding: 5px 8px;
                                        margin-left: 40px;
                                        cursor: pointer;
                                    }
                                }
                                .upload-pic{
                                    display: flex;
                                    .upload-pic-box{
                                        height: 90px;
                                        width: 90px;
                                        border: 1px dotted #ddd;
                                        margin-top: 20px;
                                        border-radius: 4px;
                                        img{
                                            display: block;
                                            margin: 25px auto 0 auto;
                                            object-fit: contain;
                                        }
                                        p{
                                            text-align: center;
                                            line-height: 32px;
                                            color: #666666;
                                            font-size: 14px;
                                        }
                                    }
                                    .upload-pic-list{
                                        height: 90px;
                                        width: 90px;
                                        border: 1px dotted #ddd;
                                        margin-top: 20px;
                                        border-radius: 4px;
                                        position: relative;
                                        margin-left: 10px;
                                        img{
                                            position: absolute;
                                            top: 50%;
                                            left: 50%;
                                            transform: translate(-50%,-50%);
                                            width: 90px;
                                            height: 90px;
                                            //object-fit: none;
                                        }
                                        em{
                                            position: absolute;
                                            top: 6px;
                                            left: 0;
                                            width: 64px;
                                            background-color: #ff6419;
                                            border-radius: 0 10px 10px 0;
                                            color: #fff;
                                            font-size: 12px;
                                            line-height: 16px;
                                            font-style: normal;
                                            padding: 1px 0;
                                            text-indent: 5px;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

</style>
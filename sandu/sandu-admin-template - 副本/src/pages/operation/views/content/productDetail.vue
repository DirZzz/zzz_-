<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/content/productList' }">商品管理</el-breadcrumb-item>
            <el-breadcrumb-item>商品详情</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="goods-edit">
            <el-row :gutter="24" class="goods-message">
                <el-col :span="24">
                    <h1>商品信息</h1>
                    <p></p>
                </el-col>

                <el-form class="elFromItem"  label-width="120px">
                    <el-form-item label="产品名称：" required>
                            <span>{{goodsInfo.name}}</span>
                    </el-form-item>

                    <el-form-item  label="产品描述：" style="margin:30px 0">
                        <div v-html="goodsInfo.describe"></div>
                        <!--<el-input-->
                                <!--type="textarea"-->
                                <!--:rows="6"-->
                                <!--disabled-->
                                <!--placeholder="请输入内容"-->
                                <!--v-model="goodsInfo.describe">-->
                        <!--</el-input>-->
                    </el-form-item>

                    <el-form-item label="产品图片：">
                        <ul class="picbox">
                            <li class="uploadImgList" v-for="(file, index) in goodsInfo.picList" :key="index">
                                <img width="90" height="90" :src="BASE_URL.sourceBaseUrl+file.picPath"/>
                            </li>
                        </ul>
                    </el-form-item>
                </el-form>
            </el-row>

            <el-row class="specification">
                <el-col :span="24">
                    <h1>规格管理</h1>
                    <p></p>
                </el-col>
                <el-col class="specification-header" :span="23">
                    <span>总库存：{{goodsInfo.totalRepertory}}</span>
                    <span>说明：商品规格图尺寸:xxx X XXX 商品主图:xxx X xx 商品主图为商品详情页顶部展示的轮播图片，商品规格图为商品选择规格时展示的图片</span>
                </el-col>
                <el-col :span="24">
                    <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)" :data="goodsInfo.skuList" border style="width: 100%">
                        <el-table-column  prop="modelNumber" label="产品型号">
                            <template slot-scope="scope">
                                <span>{{scope.row.modelNumber}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column v-for="(item, index) in goodsInfo.tableHeads" :key="index" prop="attributes" :label="item" width="120">
                            <template slot-scope="scope">
                                <span>{{scope.row.attributes[index]}}</span>
                            </template>
                        </el-table-column>

                        <el-table-column width="100" prop="salePrice" label="原价">
                        </el-table-column>
                        <el-table-column prop="price" label="优惠价格">
                            <template slot-scope="scope">
                                <span>{{scope.row.price}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="price" label="装修单价">
                            <template slot-scope="scope">
                                {{scope.row.decorationPrice}} {{scope.row.valuationUnit}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="inventory" label="库存">
                            <template slot-scope="scope">
                               <span>{{scope.row.inventory}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column width="140" prop="spePic" label="主图/100*100">
                            <template slot-scope="scope">
                                <div v-show="scope.row.spePic">
                                    <img :src="scope.row.spePic" width="90" style="height: 90px;object-fit: contain">
                                </div>
                                <div class="nonImg" v-show="!scope.row.spePic"> 暂无图片 </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>

            <div class="footer">
                <el-button type="primary" @click="$router.push('/content/productList')">关闭</el-button>
            </div>
        </div>
    </page>
</template>
<script>
    import VueEditor from '@/components/vueEditor/vueEditor'
    export default {
        name: "goods-edit",
        components: {
            VueEditor
        },
        data() {
            return {
                loading:true,
                goodsInfo:[],
                editorSettings: {
                    modules: {
                        imageDrop: false,
                        imageResize: {},
                    }
                },


            };
        },
        created() {
            this.getGoodsEdit();
        },
        mounted(){

        },
        methods: {
            /*请求商品详情数据*/
            getGoodsEdit() {
                this.API.getGoodsEdit({
                    spuId: this.$route.params.id
                }).then(res => {
                    res.data.skuList.map(item=>{
                        item.spePic.indexOf('http')>-1 ? item.spePic :  item.spePic=this.BASE_URL.sourceBaseUrl+item.spePic;
                    })
                    this.loading = false;
                    if (res.data.mainPic) {
                        res.data.picList.unshift(res.data.mainPic); //把主图加入所有图片列表
                    }
                    this.goodsInfo = res.data;
                });
            }
        }
    };
</script>

<style lang="scss" scoped>

    .header-nav {
        width: 100%;
        height: 15px;
        margin-bottom: 10px;
    }
    .goods-edit {
        color: #666666;
        background-color: #fff;
        overflow: hidden;
        padding: 0 20px 20px 20px;
        .el-row {
            margin-bottom: 20px;
            h2 {
                display: inline-block;
                font-size: 14px;
                margin-right: 10px;
            }
            span {
                color: #333333;
            }
            .el-col-24 {
                overflow: hidden;
                position: relative;
                margin-bottom: 40px;
                h1 {
                    display: inline-block;
                    width: 60px;
                    height: 16px;
                    font-size: 14px;

                    line-height: 16px;
                    border-left: 6px solid #ff6419;
                    padding-left: 10px;
                }
                p {
                    position: absolute;
                    top: 8px;
                    left: 90px;
                    width: 1499px;
                    border-top: 1px solid #dddddd;
                    display: inline-block;
                }
            }
        }
        .elFromItem{
            padding-left: 15px;
            .el-form-item{
                margin: 0;
                padding:0
            }
        }
        .goods-message {
            margin-top: 40px;
            .starbox {
                position: relative;
                .star {
                    color: #ff6419;
                    position: absolute;
                    left: -10px;
                }
            }
            .el-col-20,
            .el-col-8 {
                position: relative;
                padding-left: 20px;
                margin-bottom: 20px;
                font-size: 14px;
                color: #666666;
                height: 36px;
                line-height: 36px;

                .el-input {
                    width: 300px;
                    height: 36px;
                }
                p {
                    position: absolute;
                    top: 40px;
                    left: 110px;
                    height: 14px;
                    line-height: 14px;
                    color: #ff6419;
                }
            }
            .shopPic {
                min-height: 130px;
                > h4 {
                    padding-left: 0;
                    display: inline-block;
                    font-size: 14px;
                    position: relative;
                    top: -50px;
                }
                .container-pic{
                    padding: 10px 0 10px 10px;
                    border: 1px solid #ddd;
                    background: #fafafa;
                    border-radius: 4px;
                    width: 850px;
                    min-height: 100px;
                    margin-top: 30px;
                    .pic-tip{
                        color: #999;
                        font-size:14px;
                        line-height: 16px;
                    }
                }
                .picbox {
                    width: 850px;
                    min-height: 100px;
                    align-items: center;
                    display: flex;
                    flex-wrap: wrap;
                    .uploadImgList {
                        width: 90px;
                        text-align: center;
                        position: relative;
                        overflow: hidden;
                        background-color: #fff;
                        &:hover{
                            .upload-notmainpic__con,.posDelete{
                                display: inline-block!important;
                            }
                        }
                        img{
                            object-fit: contain;
                            border-radius: 8px
                        }
                        .upload-notmainpic__con {
                            position: relative;
                            top: -45px;
                            background: rgba(0, 0, 0, 0.7);
                            width: 90px;
                            color: #fff;
                            display: inline-block;
                            line-height: 22px;
                            border-radius: 0 0 8px 8px;
                            cursor: pointer;
                        }
                        .upload-mainpic__con {
                            position: relative;
                            top: -45px;
                            background-color: #ff641e;
                            width: 90px;
                            color: #fff;
                            display: inline-block;
                            line-height: 22px;
                            border-radius: 0 0 8px 8px
                        }
                    }

                    .box {
                        border: 1px dashed #ccc;
                        background: #ffffff;
                        &:hover {
                            border: 1px dashed #ff6419;
                        }
                    }
                    li {
                        width: 90px;
                        height: 90px;
                        display: inline-block;
                        border-radius: 4px;
                        margin: 6px;
                        float: left;
                        .div1{
                            img {
                                object-fit: contain;
                                background-color: #fff;
                            }
                        }
                        .avatar-uploader {
                            user-select: none;
                            width: 90px;
                            height: 90px;
                            position: relative;
                            .avatar-uploader-icon {
                                display: inline-block;
                                width: 20px;
                                height: 20px;
                                background: #ff6419;
                                color: white;
                                font-size: 30px;
                                text-align: center;
                                line-height: 20px;
                                border-radius: 50%;
                                margin: 20px auto 0;
                            }
                            .text {
                                height: 30px;
                                width: 90px;
                                line-height: 30px;
                                text-align: center;
                                color: #666666;
                                display: inline-block;
                            }
                        }
                    }
                }
            }
            .shopDesc {
                height: 130px;
                display: flex;
                align-items: center;
                > h3 {
                    display: inline-block;
                    font-size: 14px;
                    margin-right: 12px;
                    margin-bottom: 100px;
                }
                .descText {
                    display: inline-block;
                    width: 760px;
                    font-size: 14px;
                    font-family:"微软雅黑";
                    ::-webkit-input-placeholder {
                        color: #999999;
                        font-size: 14px;
                        font-family:"微软雅黑";
                    }
                }
            }
        }
        .yushou {
            margin-left: 20px;
        }
        .timebox {
            margin-left: 110px;
        }
        .logistics-set {
            .el-col-8 {
                padding-left: 20px;
                margin-bottom: 20px;
                font-size: 14px;
                color: #666666;
                height: 147px;
                line-height: 36px;
                display: flex;
                justify-content: left;

                .el-input {
                    width: 220px;
                    height: 36px;
                    margin-left: 10px;
                }
                .EarInput {
                    width: 60px;
                    height: 36px;
                    margin-right: 10px;
                }
                .logistics-msg,
                .logistics-price {
                    display: inline-block;
                    margin-left: 10px;
                    height: 100%;

                    .price-input {
                        width: 110px;
                        height: 36px;
                        margin-right: 5px;
                    }
                    .el-radio {
                        position: relative;
                        display: block;
                        line-height: 36px;
                        margin: 0 0 10px 0;
                        color: #333333;
                        p {
                            position: absolute;
                            top: 40px;
                            left: 25px;
                            height: 14px;
                            line-height: 14px;
                            color: #ff6419;
                        }
                    }
                    .el-select {
                        width: 200px;
                        height: 36px;
                        margin-left: 10px;
                        color: #333333;
                    }
                }
            }
        }
        .wuliuSetUp {
            height: 170px;
            margin-bottom: 0;
            .el-col-8 {
                margin-bottom: 0;
                height: 100px;
            }
            .inputErali {
            }
            .orange {
                border-bottom: 1px solid #ff6419;
            }
        }
        .presell-set {
            .el-col-8,
            .el-col-16 {
                display: block;
                height: 148px;
                padding-left: 20px;

                div {
                    height: 36px;
                    line-height: 36px;
                    margin-bottom: 20px;
                    .el-checkbox {
                        margin-left: 10px;
                        color: #333333;
                    }
                    .el-input {
                        width: 200px;
                        height: 36px;
                        margin-left: 10px;
                    }
                    span {
                        margin-left: 10px;
                    }
                }
            }
            .el-col-8 {
                height: 30px;
            }
        }
        .specification {
            .specification-header {
                height: 36px;
                margin-left: 30px;
                display: flex;
                justify-content: space-between;
                color: #333333;
                line-height: 36px;
            }
            .avatar-uploader {
                display: inline-block;
                height: 15px;
                span {
                    color: #ff6419;
                }
            }
            .el-input {
                width: 200px;
                height: 36px;
            }
            .nonImg {
                width: 90px;
                height: 90px;
                line-height: 90px;
                text-align: center;
                background-color: #f5f5f5;
                margin: 0 auto 5px;
            }
        }
        .goods-introduce {
            .el-col-12 {
                height: 703px;
            }
        }
        .footer {
            width: 100%;
            height: 80px;
            border-top: 1px solid #dddddd;
            line-height: 80px;
            padding-left: 20px;
            .el-button {
                width: 130px;
                height: 40px;
                border: none;
                color: #fff;
                font-size: 16px;
                font-weight: bold;
            }
            .preserve {
                background-color: #ff6419;
            }
            .cancel {
                background-color: #454545;
            }
        }
    }
    .clear {
        zoom: 1;
    }
    .clear:after {
        content: "";
        display: block;
        clear: both;
    }
</style>
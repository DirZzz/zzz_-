<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <div class="shop">
            <!-- <h1 class="headline"><span style="color:#ff6419;cursor:pointer">活动基本信息</span>&emsp;&emsp;|&emsp;&emsp;<span style="cursor:pointer" @click="toData">活动数据</span></h1> -->
            <template>
            <el-tabs v-model="tagName"  @tab-click="handleClick">
                <el-tab-pane label="活动基本信息" name="first"></el-tab-pane>
                <el-tab-pane label="活动数据" name="second"></el-tab-pane>
            </el-tabs>
            </template>

            
            <div v-if="this.tagName == 'first'">            
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                    <el-form-item label="活动状态：" prop="shopName">
                        {{ruleForm.activityStatus==0?'草稿中':ruleForm.activityStatus==1?'未开始':
                            ruleForm.activityStatus==2?'进行中':ruleForm.activityStatus==3?'已结束':ruleForm.activityStatus==4?'已失效':''}}
                    </el-form-item>
                    <el-form-item label="活动名称：" prop="contactName">
                         {{ruleForm.activityName}}
                    </el-form-item>
                    <el-form-item label="活动时间：" prop="contactPhone">
                        {{ruleForm.activityStartTime}}&emsp;至&emsp;{{ruleForm.activityEndTime}}
                    </el-form-item>
                    <el-form-item label="参团人数：" prop="provinceCode">
                        {{ruleForm.totalNumber}}人
                    </el-form-item>
                    <el-form-item label="拼团有效期：" prop="shopAddress">
                        {{ruleForm.groupValidDay}}天&nbsp;
                        <c v-if="ruleForm.groupValidHour!=0">{{ruleForm.groupValidHour}}小时&nbsp;</c>
                        <c v-if="ruleForm.groupValidMinute!=0">{{ruleForm.groupValidMinute}}分钟&nbsp;</c>
                    </el-form-item>
                    

                    <el-form-item label="活动商品：" class="logo">
                        
                        <div class="product-info" style="background-color:#f3f3f3;width:700px;">
							<img :src="BASE_URL.sourceBaseUrl+ruleForm.productPath" width="40" height="40" class="propic"/>
							{{ruleForm.productText}}
				    </div>

                    <p	style="font-weight:bold">拼团价：</p>
                    <div  style="width:700px;">
					<el-table 
					:data="skuInfo"
                    border
					style="width:100%">		
						<el-table-column
						    v-for="(item ,index) in attributeNames"
							:key="index"
							:prop="item"
							:label="item"
                            v-if="attributeNames"							
							>
							<template slot-scope="scope">
								<div v-if="scope.row.attributeValueNames">
									{{scope.row.attributeValueNames[index]}}
								</div>
							</template>
						</el-table-column>

						<el-table-column
						label="优惠价（元）"
						>
						<template slot-scope="scope">
							{{scope.row.price?scope.row.price:'———'}}
						</template>
						</el-table-column>

						<el-table-column
						label="拼团价（元）"
						>
						<template slot-scope="scope">
                            {{scope.row.activityPrice}}
						</template>
						</el-table-column>

						<el-table-column
						prop="qty"
						label="库存"
						></el-table-column>

                        <!-- <el-table-column
						prop="useQty"
						label="剩余库存"
						></el-table-column> -->
					</el-table>
                    </div>

                    </el-form-item>

                     <el-form-item label="商品限购：" class="coverPic">
                        {{ruleForm.purchaseLimitAmount}}&nbsp;件/人
                    </el-form-item>

                    <el-form-item label="团购设置：" class="coverPic">
                        {{ruleForm.gatherFlag==0?'不开启凑团':'开启凑团'}}<br/>
                        {{ruleForm.virtualFlag==0?'不开启模拟成团':'开启模拟成团'}}<br/>
                        {{ruleForm.couponFlag==0?'不可叠加使用优惠券':'可叠加使用优惠券'}}
                    </el-form-item>

                    <el-form-item label="备注：" class="coverPic">
                       {{ruleForm.remark}}
                    </el-form-item>
                </el-form>
            </div>

            <div v-if="this.tagName == 'second'">

                <div class="heading1">
                         <div class="title1">订单实付金额：&emsp;<b style="font-size:22px;color:black" v-if="this.totalOrderAmount">￥{{this.totalOrderAmount}}</b>
                            <b style="font-size:22px;color:black" v-else>0</b>
                         </div>
                         &emsp;&emsp;&emsp;
                         <div class="title2"> 成团订单数：&emsp;<b style="font-size:22px;color:black">{{this.totalOrderCount}}</b></div>
                         &emsp;&emsp;&emsp;
                         <div class="title3">成团人数：&emsp;<b style="font-size:22px;color:black">{{this.totalOrderPerson}}</b></div>
                     </div>
                     
                    <div>
                        <span class="cut-off-rule"></span>
                        <el-table
                                border
                                :data="tableData"
                                element-loading-text="拼命加载中"
                                element-loading-spinner="el-icon-loading"
                                element-loading-background="rgba(255, 255, 255, 0.8)"
                                style="width: 97%;margin:20px">
                            <el-table-column type="index" label="序号" width="60"></el-table-column>
                            <el-table-column prop="telephone" label="团长手机号码"></el-table-column>
                            <el-table-column prop="joinNumber" label="已参团人数"></el-table-column>
                            <el-table-column prop="unjoinNumber" label="满团剩余人数"></el-table-column>
                            <el-table-column prop="openStatus" label="状态">
                                <template slot-scope="scope">
                                    <span>{{scope.row.openStatus==0?'等待成团':scope.row.openStatus==1?'拼团成功':scope.row.openStatus==2?'拼团失败':''}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                  <span class="operation" @click="getActivityOpenDetail(scope.row)" style="color:#ff6419;cursor:pointer">查看成团情况</span>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pageFrame">
                            <el-pagination
                                    @size-change="handleSizeChange"
                                    @current-change="handleCurrentChange"
                                    :current-page="query.page"
                                    :page-sizes="[10, 50, 100, 200,500]"
                                    :page-size="query.limit"
                                    layout="total, sizes, prev, pager, next, jumper"
                                    :total="total">
                            </el-pagination>
                        </div>
                    </div>



                    <!--确认对话框-->
                    <el-dialog title="查看成团情况" :visible.sync="sureDialogTableVisible">
                        <div class="childHeading">
                            成团状态:&emsp;<b style="color:black">{{this.assemStatus==0?'等待成团':this.assemStatus==1?'拼团成功':this.assemStatus==2?'拼团失败':''}}</b>
                            &emsp;&emsp;&emsp;
                            已参团人数:&emsp;<b style="color:black">{{this.joinPerson}}人</b>
                            &emsp;&emsp;&emsp;
                            满团剩余人数:&emsp;<b style="color:black">{{this.unjoinPerson}}人</b>
                        </div>
                        <el-table :data="sureProduct" border="">
                            <el-table-column
							      label="序号"
							      type="index"
							      width="100">
							   </el-table-column>
                            <el-table-column label="成团手机号码">
                                 <template slot-scope="scope">
                                    <div v-if="scope.row.userType==1" style="display:inline-block;text-align:left;width:140px">{{scope.row.telephone}}
                                    <img src="../../../assets/images/tuanzhang.png" v-if="scope.row.isMaster==1" />
                                    </div>
                                    <span v-else>虚拟团员</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="参团时间">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.userType==1">{{scope.row.gmtCreate}}</span>
                                    <span v-else>——</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="订单金额（元）">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.userType==1">{{scope.row.orderAmount}}</span>
                                    <span v-else>——</span>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pageFrame">
                            <el-pagination
                                    @current-change="handleDetailCurrentChange"
                                    :current-page="queryDetail.page"
                                    :page-size=10
                                    layout="total, prev, pager, next, jumper"
                                    :total="totalDetail">
                            </el-pagination>
                        </div>
                    </el-dialog>
            </div>
            
        </div>
    </div>
</template>

<script>
    import {Quill, VueEditor} from "vue2-editor";
    import {ImageDrop} from "quill-image-drop-module";
    import ImageResize from "quill-image-resize-module";
    import "@/assets/css/vueEditer.scss";
    import myMap from "@/components/map";

    Quill.register("modules/imageDrop", ImageDrop);
    Quill.register("modules/imageResize", ImageResize);
    export default {
        name: "add-shop",
        components: {
            VueEditor,
            myMap
        },
        data() {
            return {
                tagName:'first',
                getPoint:{
                    longitude:'',//经度
                    latitude:''////纬度
                },
                upLogoParam:{
                    fileKey:'company.shop.logoPic',
                    platform: 'web',
                    module: 'product',
                    type: 'image'
                },
                upCoverParam:{
                    fileKey:'company.shop.coverPic',
                    platform: 'web',
                    module: 'product',
                    type: 'image'
                },
                productImgURL:'',
                productText:'',
                skuInfo:[],
                attributeNames:'',
                attributeValueNames:'',
                activityId:'',
                spuId: "",
                showMixNum: false,
                showMaxNum: false,
                mixPrice: "",
                maxPrice: "",
                isInit: sessionStorage.getItem("routerTxt") == "新增店铺" ? true : false,
                isShopDetails: false,
                headerTxt: "",
                token: { Authorization: sessionStorage.getItem("token") },
                nav: [
                    { nav: "拼团活动列表", url: "/assemActivity" },
                    { nav: "详情", }
                ],
                editorSettings: {
                    modules: {
                        imageDrop: true,
                        imageResize: {}
                    }
                },
                ruleForm: {
                    id:"",
                    activityEndTime: "",
                    activityName: "",
                    activityStartTime:"",
                    activityStatus: "",
                    couponFlag: "",
                    creator: "",
                    gatherFlag:"",
                    productPath:"",
                    groupValidDay: "",
                    groupValidHour: "",
                    groupValidMinute: "",
                    purchaseLimitAmount: "",
                    spuId: "",
                    totalNumber: "",
                    virtualFlag: "",
                    remark:"",
                },
                rules: {
                    // 表单验证
                    // shopName: [
                    //     { required: true, message: "请输入店铺名称", trigger: "blur" },
                    //     { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
                    // ],
                    // shopAddress: [
                    //     { required: true, message: "请输入详细地址", trigger: "blur" },
                    //     {
                    //         min: 1,
                    //         max: 100,
                    //         message: "长度在 1 到 100 个字符",
                    //         trigger: "blur"
                    //     }
                    // ],
                    // contactName: [
                    //     { required: true, message: "请输入联系人姓名", trigger: "blur" },
                    //     { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
                    // ],
                    // contactPhone: [
                    //     { required: true, message: "请输入联系人电话", trigger: "blur" },
                    //     { validator: this.validPhone, trigger: "blur" }
                    // ],
                    // provinceCode: [
                    //     { required: true, message: "请选择区域", trigger: "blur" },
                    //     { validator: this.validArea, trigger: "blur" }
                    // ]
                },
                province: [], // 省
                city: [], // 市
                district: [], // 区
                street: [], // 街道
                provinceName: "",
                districtName: "",
                streetName: "",
                cityName: "",
                townName: "",
                workStyleShow: false,
                workStyleShowSeek: "", // 搜索
                showWorkStyleList: [], // 需要显示在页面的工作类型列表
                checkWorkStyleList: [], // 已选工作类型列表
                checkWorkStyleLists: [], // 预留已选工作类型列表，用于编辑页面选择工作类型点击取消后所选的类型的还原
                workStyleList: [], // 工作类型列表
                workStyle: "", //工作种类名称
                workStyleCode: "", // 请求工作类型参数
                upLogoImgUrl: "", // 上传Logo图片url
                upCoverImgUrl: "", // 上传封面图图片url
                logoImgUrl: "", // logo图片url
                logoImgUrlId: "", // logo图片id
                coverImgUrl: [], // 封面图片url
                coverImgUrlIndex: -1,
                coverImgUrlId: [], // 封面图片id
                publishChannel: [], // 发布渠道列表
                shopDatailsList: {}, // 店铺详情
                allotShopValue: '',
                joinPerson:'',
                unjoinPerson:'',
                totalOrderAmount:'',
                totalOrderCount:'',
                totalOrderPerson:'',
                assemStatus:'',
                uwliu: false,
                orderNum: false,
                orderNumLen: false,
                sureDialogTableVisible: false,
                finishedVisible: false,
                orderNumber: undefined,
                consignee: undefined,
                sendVisible: false,
                activeName: 'second',
                selectDate: '',
                loading: true,
                OrderStatus: '',
                tableData: [],
                mobile: '',
                logisticsCompany: '',
                trackingNumber: '',
                total: 0,
                totalDetail:0,
                productName: [],
                address: '',
                orderID: '',
                flagIndex:'',
                sureProduct: [],
                orderDetailMsg: {
                    logisticsCompany: '',
                    trackingNumber: ''
                },
                query: {
                    purchaseOpenId:this.$route.params.id,
                    page: 1,
                    limit: 10,
                },
                queryDetail: {
                    page:1,
                },
                reveice:''

            };
        },
        created() {
            this.getActivityDetail(this.$route.params.id);
            this.getActivityOpen()
            
        },
        computed: {
            showPrice(){
                if(sessionStorage.getItem('companyType')==6||sessionStorage.getItem('companyType')==4) {
                    return true;
                }else{
                    return false;
                }
            }
        },
        methods: {
            getActivityOpenDetail(row) {
                this.joinPerson=row.joinNumber;
                this.unjoinPerson=row.unjoinNumber;
                this.assemStatus=row.openStatus;
                this.getOpenDetailByOpenId(row.id); 
                this.sureDialogTableVisible = true
            },
             //获取开团信息
            getActivityOpen() {
                this.API.getActivityOpen(
                    this.query
                ).then((res) => {
                    this.tableData = res.datalist;
                    this.total = res.totalCount;
                })
            },
            //获取开团详细信息
            getOpenDetailByOpenId(index) {
                this.flagIndex = index;
                this.API.getOpenDetailByOpenId({
                    limit:10,
                    purchaseOpenId:index,
                    page:this.queryDetail.page
                }).then((res) => {
                    this.sureProduct = res.datalist;
                    this.totalDetail = res.totalCount;
                })
            },
            // 分页模块
            handleSizeChange: function (size) {
                // this.pageSize = size;
                this.query.limit = size
                this.getActivityOpen()
            },
            handleCurrentChange: function (currentPage) {
                // this.currentPage = currentPage;
                this.query.page = currentPage
                this.getActivityOpen()
            },
            handleDetailCurrentChange: function (currentPage) {
                this.queryDetail.page = currentPage
                this.getOpenDetailByOpenId(this.flagIndex)
            },
            handleClick(tab, event) {
               
      },
            toData(){
                let index = this.$route.params.id;
                this.$router.push(`/assemActivity/data/${index}`);
            },
            getGroupGoodsDetail() {
                this.API.getGroupGoodsDetail(
                    {
                        purchaseActivityId : this.activityId,
                        spuId : this.spuId
                    }
                ).then(res => {
                    this.attributeNames = [];
                    this.skuInfo = res.obj;
                    if(res.obj[0].attributeNames != null){
					this.attributeNames = res.obj[0].attributeNames.split(',');
				  }
                    this.attributeValueNames=[];
				    res.obj.map(item=>{
					if(item.attributeValueNames!=null){
						item.attributeValueNames =  item.attributeValueNames.split(',');  
					  }
					return item
				  })
                })
            },

            getSkuDetail(index){
			  this.API.getSkuDetail({
				  spuId : index
			  }).then((res) => {
				  this.skuInfo = res.obj;
				  this.attributeNames = res.obj[0].attributeNames.split(',');
				  //this.attributeValueNames = 
				  this.attributeValueNames=[];
				  res.obj.map(item=>{
					  item.activityPrice = '';
					item.attributeValueNames =  item.attributeValueNames.split(',');
					return item
				  })
			  })
          },
          
            getActivityDetail(index){
                this.API.getActivityDetail({
                    id: this.$route.params.id
                }).then(res => {
                        this.ruleForm = res.obj;
                        this.spuId = res.obj.spuId;
                        this.activityId = res.obj.activityId;
                        this.totalOrderAmount = res.obj.totalOrderAmount;
                        this.totalOrderCount = res.obj.totalOrderCount;
                        this.totalOrderPerson = res.obj.totalOrderPerson;
                        this.getGroupGoodsDetail();
                        // this.getSkuDetail(45750);
                });
            },
            
        }
    };
</script>

<style lang="scss" scoped>
    .shop {
        padding: 20px 40px 20px 40px;
        background-color: #ffffff;

        .nav {
            width: 100%;
            height: 15px;
            margin-bottom: 10px;
        }
        .headline {
            width: 100%;
            font-size: 18px;
            color: #333333;
        }
        .cut-off-rule {
            display: block;
            width: 100%;
            height: 1px;
            background-color: #eeeeee;
            margin: 10px 0 40px;
        }
        .required-hint {
            width: 100%;
            font-size: 14px;
            color: #666666;
            span {
                color: #ff3838;
            }
        }
        .headline2 {
            width: 100%;
            height: 40px;
            line-height: 40px;
            padding-left: 10px;
            font-size: 16px;
            margin: 10px 0 30px;
            color: #333333;
            border-left: 7px solid #ff6419;
            background-color: #f6f6f6;
        }

        .demo-ruleForm {
            overflow: hidden;
            padding-left: 20px;
            .publish {
                color: #333333;
            }
            .editor {
                width: 850px;
            }
            .el-form-item {
                margin-bottom: 20px;
            }
            .shop-name {
                width: 500px;
            }
            .contacts {
                width: 320px;
            }
            .area {
                position: relative;
                color: #999999;
                span {
                    position: absolute;
                    top: 0;
                    left: -63px;
                    color: #ff6419;
                }
                .el-select {
                    width: 150px;
                }
            }
            .work-style {
                p {
                    color: #ff6419;
                    cursor: pointer;
                }
                div {
                    display: flex;
                    justify-content: left;
                    font-size: 14px;
                    color: #333333;
                    flex-wrap: wrap;
                    margin-bottom: 10px;
                    .work-style-item {
                        display: flex;
                        justify-content: space-between;
                        min-width: 94px;
                        height: 30px;
                        line-height: 30px;
                        background-color: #fafafa;
                        border-radius: 4px;
                        border: solid 1px #dddddd;
                        padding: 0 10px;
                        margin-right: 10px;
                        a {
                            display: inline-block;
                            width: 18px;
                            height: 18px;
                            line-height: 18px;
                            text-align: center;
                            border-radius: 50%;
                            margin-top: 6px;
                            background-color: #ffffff;
                            border: solid 1px #dddddd;
                            cursor: pointer;
                        }
                    }
                }
            }
            .logo,
            .shop-cover {
                .logo-img {
                    width: 90px;
                    height: 90px;
                    border-radius: 4px;
                    margin-right: 20px;
                    display: inline-block;
                    vertical-align: bottom;
                }
                .img-list {
                    display: inline-block;
                    .img-list-item {
                        display: inline-block;
                        position: relative;
                        span {
                            position: absolute;
                            top: 36px;
                            left: 82px;
                            display: block;
                            width: 18px;
                            height: 16px;
                            background: url("../../../assets/images/icons/delete3.png");
                        }
                        .shop-cover-img {
                            width: 180px;
                            height: 90px;
                            border-radius: 4px;
                            margin-right: 10px;
                            vertical-align: bottom;
                        }
                    }
                }
                .img-text {
                    color: #999999;
                }
                .add-img {
                    width: 22px;
                    height: 22px;
                    margin: 34px 34px;
                    background: url("../../../assets/images/icons/uploadPlug.png") no-repeat
                    0 0;
                }
                .avatar-uploader {
                    display: inline-block;
                    vertical-align: bottom;
                    width: 90px;
                    height: 90px;
                    background-color: #ffffff;
                    border-radius: 4px;
                    border: solid 1px #dddddd;
                    margin-right: 20px;
                }
            }
        }
        .footer {
            height: 80px;
            border-top: 1px solid #eeeeee;
            padding-left: 80px;
            .el-button {
                width: 130px;
                height: 40px;
                margin-top: 40px;
                margin-right: 40px;
            }
            .reset {
                background-color: #454545;
                border: solid 1px #454545;
            }
        }

        .pop-up {
            .workStyleShowSeek {
                width: 400px;
                margin: 0 20px 20px 20px;
            }
            span {
                color: #ff6419;
                cursor: pointer;
            }
            p {
                padding-left: 20px;
                margin-bottom: 10px;
            }
            .pop-up-content {
                width: 440px;
                height: 340px;
                padding: 10px;
                margin-left: 20px;
                border: solid 1px #dddddd;
            }
            .dialog-footer {
                // border-top: 1px solid #dddddd;
                height: 56px;
                line-height: 56px;
            }
        }
        /********************店铺详情*********************/
        .shop-datails {
            padding: 0 20px;
            margin-bottom: 20px;
            font-size: 14px;
            .shop-datails-item {
                min-height: 30px;
                line-height: 30px;
                margin-bottom: 20px;
                display: flex;
                justify-content: left;
                .shop-item {
                    word-wrap: break-word;
                    word-break: break-all;
                }
                p {
                    min-width: 100px;
                    text-align: right;
                    margin-right: 20px;
                }
                .logoImgUrl {
                    display: inline-block;
                    width: 90px;
                    height: 90px;
                }
                .shop-logo-active {
                    display: inline-block;
                    width: 90px;
                    height: 90px;
                    border: 1px solid #dddddd;
                    line-height: 90px;
                    text-align: center;
                }
                .cover-img-list {
                    .cover-img-url {
                        display: inline-block;
                        width: 180px;
                        height: 90px;
                        margin-right: 10px;
                    }
                }
                .shop-cover-active {
                    display: inline-block;
                    width: 180px;
                    height: 90px;
                    border: 1px solid #dddddd;
                    line-height: 90px;
                    text-align: center;
                }
                div {
                    display: flex;
                    justify-content: left;
                    font-size: 14px;
                    color: #333333;
                    flex-wrap: wrap;
                    margin-bottom: 10px;
                    .work-style-item {
                        display: flex;
                        justify-content: space-around;
                        height: 30px;
                        line-height: 30px;
                        background-color: #fafafa;
                        border-radius: 4px;
                        border: solid 1px #dddddd;
                        padding: 0 10px;
                        margin-right: 10px;
                        a {
                            width: 7px;
                            height: 7px;
                            background-color: #999999;
                            border-radius: 50%;
                            margin-top: 12px;
                            margin-right: 10px;
                        }
                    }
                }
            }
        }
    }

    .product-info{
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		text-align: left;
		width: 100%;
		img{
			vertical-align: middle;
			width:70px;
			height: 70px;
		}
	}

    .cover-img-url {
                        display: inline-block;
                        width: 180px;
                        height: 90px;
                        margin-right: 10px;
                    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .sureButton {
        text-align: right;
        margin-top: 20px;
        border-top: solid 1px #ddd;
        padding-top: 20px;
        .el-button {
            width: 130px;
        }
    }

    .heading1{
         margin:0 20px;
         height: 120px;
         line-height: 120px;
         width:97%;
         color:gray;
    }
    .title1{
        display: inline-block;
        height: 120px;
        text-indent:25px;
        width:31%;
         background-color: #f3f3f3;   
    }
    .title2{
        display: inline-block;
        height: 120px;
        text-indent:25px;
        width:31%;
         background-color: #f3f3f3;   
    }
    .title3{
        display: inline-block;
        height: 120px;
        text-indent:25px;
        width:31%;
         background-color: #f3f3f3;   
    }
    .childHeading{
        margin:0 0 20px 0;
        height: 50px;
        line-height: 50px;
        text-indent:25px;
        font-weight:bold;
        color:gray;
        background-color: #f3f3f3;   
    }

    //  .el-dialog__wrapper{
    //      .el-dialog{
    //          .el-dialog__header{
    //              background: red!important;
    //          }
    //      }
    //  }
</style>
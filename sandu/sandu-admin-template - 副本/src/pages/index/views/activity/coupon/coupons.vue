<template>
	<div>
        <el-tabs v-model="activeName" class="searchBg" @tab-click="changeState">
            <el-tab-pane label="优惠券管理" name="0">
            	<common-coupon-list></common-coupon-list>
            </el-tab-pane>

            </el-tab-pane>
             <el-tab-pane label="热销商品" class="hotShop" name="4" >
               <hot-shop></hot-shop>
            </el-tab-pane>
           </el-tabs>
    </div>
</template>

<script>
    import qs from 'qs'
	import CommonCouponList from './couponListComp.vue'
    import HotShop from '../../activity/product/recommends'
    export default {
	    	components: {
		      CommonCouponList,
              HotShop
		    },
        data() {
            return {
                activeName: '0',
                selectDate: '',
                loading: true,
                OrderStatus: '',
                tableData: [],
                total: 0,
                query: {
                    page: 1,
                    limit: 10,
                },
                // goodsListArguments:{
                // 	couponState:1,//1:未开始 2:进行中 3:已失效
				// 	companyId: sessionStorage.getItem('companyID')// 企业ID
                // },
                activeRow:{},
                delDialog:false,
                stopDialog:false
            }
        },
        created() {
            //this.getOrderList()
        },
        methods: {
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            delCoupon(row){
            	this.delDialog = true;
            	this.activeRow = row;
            },
            stopCoupon(row){
            	this.stopDialog =true;
            	this.activeRow = row;
            },
            // toEditCoupon(id){
            // 	this.$router.push(`/couponEdit/${id}`)
            // },
            changeState(tab) {
            	console.log(tab)
                // this.couponState = tab.index;
                console.log("fat:"+this.couponState)
            },
		    	// 	toAddCoupon () {
		    	// 		localStorage.setItem('breadcrumb3', '新增');
		    	// 		sessionStorage.setItem("pathTitle","全部优惠券列表")
		        // 	this.$router.push('/couponAdd');
	      		// },
            // 分页模块
            handleSizeChange: function (size) {
                // this.pageSize = size;
                this.query.limit = size
                //this.getOrderList()
            },
            handleCurrentChange: function (currentPage) {
                // this.currentPage = currentPage;
                this.query.page = currentPage
                this.loading = true
                //this.getOrderList()
            }
        },
        mounted() {
            //this.getOrderList();
        }
    }
</script>

<style lang="scss" scoped>
	@import "../../../assets/css/mixin";
	.productName {
        text-align: left;
        line-height: 24px;
    }

    .receiveAddress {
        margin-top: 30px;
        margin-bottom: 20px;
    }

    .sureButton {
        text-align: right;
        margin-top: 20px;
        border-top: solid 1px #ddd;
        padding-top: 20px;
        /*.el-button {
            width: 130px;
        }*/
    }
   .hotShop{
       color: red;
   }
    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .grey {
        background: #f5f5f5;
        &:hover {
            background: #FF6419;
            color: #fff !important;
        }
    }

    .orderlistTop {
        background: #fff;
        padding: 20px;
    }

    .buttonSplitLine {
        border-top: solid 1px #ddd;
        margin: 25px 0;
        padding-top: 20px;
        .el-button {
            color: #FF6419;
        }
    }

    .searchBg {
        background: #fff;
    }

    .orderListSearch {
        margin: 15px;
    }

    .buttonSplitLine {
        border-top: solid 1px #ddd;
        margin: 25px 0;
        padding-top: 20px;
        .el-button {
            color: #FF6419;
        }
    }
	
	
	.searchBg{
		background-color: #fff;
	}
    .el-tabs__nav {
        margin-left: 15px;
        line-height: 58px!important;
    	padding-left: 21px!important;
    }

    .el-radio-button__inner {
        background: #fafafa;
        border: none;
    }

    .el-radio-button__orig-radio:checked + .el-radio-button__inner {
        background: #fff;
        border: none;
        color: #666;
        -webkit-box-shadow: none;
        box-shadow: none;
    }
    
    /*优惠券*/
    .btn-cancel{
			background-color: #FCDCCF;
	    color: $btnOrange;
	    border: 1px solid #FCDCCF;
		}
    .coupons-wrap{
    	.btn-cancel{
				background-color: #FCDCCF;
		    color: $btnOrange;
		    border: 1px solid #FCDCCF;
			}
			.dia-single{
				padding-left: 40px;
			}
    }
    
</style>
<style lang="scss">
	.coupons-wrap{
		.el-tabs__header{
			padding: 0 15px;
    	margin:0 0px 20px!important;
    	position: relative;
    	/*border-bottom: 20px solid #e4e4e4;*/
    	&:after{
    		content: '';
    		display: block;
    		width:120%;
    		height: 20px;
    		background-color: #e4e4e4;
    		position: absolute;
    		left: 0;
    		bottom: -20px;
    	}
  	}
  	.el-tabs__nav {
      margin-left: 15px;
      line-height: 58px!important;
    	/*padding-left: 21px!important;*/
    }
    .box-padding{
    	/*padding:0 20px 20px;*/
    	padding:20px 20px 20px;
    }
  }
</style>
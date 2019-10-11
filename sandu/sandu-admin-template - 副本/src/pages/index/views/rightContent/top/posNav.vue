<template>
  <div class="pos">
    <ul style="padding-left: 10px; font-weight: bold">
      <li @click="toback" style="cursor: pointer" class="normal-back"><img style="vertical-align: -6px;" src="../../../assets/images/icons/topback.png">返回</li>
      <li @click="toback" style="cursor: pointer" class="coupon-back">返回上一级</li>
      <li class="pos-line">|</li>
      <li><span class="pos-tip">当前位置：</span> <router-link :to="toBackList" class="pos-parent">{{posNavTitle}} </router-link> > <span class="pos-cur">{{breadcrumb3}}</span></li>
    </ul>
  </div>
</template>
<script>
    import { mapState } from 'vuex';
    export default {
        data () {
            return {
                toBackList: '',
                posNavTitle: '',
            }
        },
        methods: {
            getPath () {
                this.toBackList = sessionStorage.getItem('getPath')
                console.log(this.toBackList, 'this.toBackList')
                this.posNavTitle = sessionStorage.getItem('pathTitle')
            },
            toback () {
                // let back_url = localStorage.getItem('from');
                // this.$router.push({ path: back_url })
                this.$router.go(-1)
            }
        },
        mounted () {
            this.getPath()
        },
        computed: mapState({
            breadcrumb1: state => localStorage.getItem('breadcrumb1'), // state.breadcrumb1,
            breadcrumb2: state => localStorage.getItem('breadcrumb2'), // state.breadcrumb2,
            breadcrumb3: state => localStorage.getItem('breadcrumb3') // state.breadcrumb3
        })
    };
</script>
<style lang="scss" scoped>
    @import "../../../assets/css/mixin";
    .fontNoWeight{
        font-weight: normal;
    }

    .pos
    {
        margin-bottom: 12px;
        min-height: 80px;
        background: #fff;
        border-radius: 6px;
        box-shadow: 0 0 8px rgba(0,0,0,0.1);
        .nav-top {
            margin-top: 35px;
        }
        ul{
            font-size: 13px;
            color:#626262;
            li {
                padding:0 10px;
                line-height: 80px;
                display: inline-block;
                b{
                    display: inline-block;
                    background: $btnOrange;
                    height: 40px;
                    line-height: 40px;
                    padding:0 20px;
                    color:#fff;
                    border-radius: 20px;
                }
                .pos-cur{
                	color: #999;
                }
            }
            .pos-line{
            	padding:0;
            	color: #ddd
            }
            .pos-parent{
            	color: #666
            }
            .coupon-back{
	          	display: none;
	          }
        }
    }
    
    /*优惠券界面的面包屑*/
   .coupon-page{
   	.pos{
      margin-bottom: 10px;
	    height: 15px;
	    min-height: 15px;
	    background: transparent;
	    border-radius: 6px;
	    -webkit-box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
	    box-shadow: none;
      .nav-top {
          margin-top: 35px;
      }
      ul{
          font-size: 13px;
          /*color:#626262;*/
          color: $btnOrange;
          .normal-back{
          	display: none;
          }
          .coupon-back{
          	display: inline-block;
          }
          li {
              padding:0 1px;
              line-height: 20px;
              display: inline-block;
              .pos-tip{
              	display: none;
              }
              b{
                  display: inline-block;
                  background: $btnOrange;
                  height: 40px;
                  line-height: 40px;
                  padding:0 20px;
                  color:#fff;
                  border-radius: 20px;
              }
              .pos-cur{
              	color: #626262;
              	padding-left: 2px;
              }
              .pos-parent{
	            	color: $btnOrange;
	            	padding-right: 2px;
	            }
          }
          .pos-line{
          	color: $btnOrange
          }
      }
    }
   }
    
</style>

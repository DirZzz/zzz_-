<template>
    <div>
        <el-row style="background-color:#fff">
            <el-col class="topTabNav">
                <router-link to="/imall/imallOrder/collect" style="padding:10px" class="tabtext" v-if="isPer('points:mall:order:view')">
                    <span>积分订单汇总</span>
                </router-link><span class="sp"></span>
                <router-link to="/imall/imallOrder/orderList/10" style="padding:10px" class="tabtext" :class="{'router-link-active':routerLinkActive}" v-if="isPer('points:mall:gift:view')">
                    <span>礼品订单列表</span>
                </router-link><span class="sp"></span>
                <router-link to="/imall/gift/index" style="padding:10px" class="tabtext" v-if="isPer('points:mall:gift:view')">
                    <span>礼品管理</span>
                </router-link><span class="sp"></span>
                <router-link to="/imall/imallSlide/index" style="padding:10px" class="tabtext" v-if="isPer('points:mall:options:view')">
                    <span>积分商城设置</span>
                </router-link>
            </el-col>
        </el-row>
        <el-row style="height:100%;margin-top: 2px">
            <el-col>
                <router-view ></router-view>
            </el-col>
        </el-row>
    </div>
</template>
<script>
    import qs from 'qs'
	export default {
        inject: ['reload'],
	    data(){
	        return{
                routerLinkActive:false
            }
        },
        methods:{
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
        },
        watch:{
            $route(to,from){
                if(to.name=='orderList'){
                    this.routerLinkActive=true;
                }else {
                    this.routerLinkActive=false;
                }
                //this.reload()
            }
        },
        created(){
	        
        }
    }
</script>

<style rel="stylesheet/css" lang="css" scoped>

    .router-link-active{
        color: #FF6419!important;
        font-weight:normal;
    }
    .tabtext{
        padding: 10px;
        color: #333;
        font-size: 18px;
    }
    .el-button.is-plain:hover, .el-button.is-plain:focus{
        color: #FF6419;

    }
</style>

<style lang="scss" scoped>
    .topTabNav{
        padding:18px;
        .sp{
            display: inline-block;
            width: 1px;
            font-weight: normal;
            vertical-align: -2px;
            border-right: solid 1px #ddd;
            height: 16px;
        }
    }
</style>
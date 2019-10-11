<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: item.path }" v-for="(item,index) in userManageTabList" :key="index">{{item.name}}</el-breadcrumb-item>
        </el-breadcrumb>
        <el-tabs v-model="tabType" type="card" @tab-click="handleClick" class="tabBox">
            <el-tab-pane label="厂商用户" name="first">
                <mfrsUser v-if="mfrsUserShow" ref="mfrsUser" :businessType="businessType"></mfrsUser>
            </el-tab-pane>
            <el-tab-pane label="经销商用户" name="second">
                <dealerUser v-if="!mfrsUserShow" ref="dealerUser" :businessType="businessType"></dealerUser>
            </el-tab-pane>
        </el-tabs>
    </page>
</template>

<script>
    import { mapState,mapActions} from 'Vuex'
    import { merge,uniq,map,find,filter } from 'lodash'
    import minixs from '@s/minixs/index'
    import mfrsUser from './components/mfrsUser'
    import dealerUser from './components/dealerUser'
    export default {
        name: "userManage",
        minixs:[minixs],
        components:{
            dealerUser,
            mfrsUser
        },
        data(){
            return{
                tabType: 'first',
                businessType:'',
                userListData:'',
                mfrsUserShow: true,
                activeName: 'first'
            }
        },
        activated(){
            let arr=[{
                path:'/userManage/mfrsManage',
                name:'厂商列表'
            },{
                path:'',
                name:'用户管理',
                oldPath:`/userManage/mfrsManage/userManage?id=${this.$route.query.id}`
            }];
            this.setTabList(arr);
            
            this.$route.query.tabType?this.tabType=this.$route.query.tabType:'';
            this.setUserMethod(this.tabType=="second"?'franchiser':'vendor');
            this.$nextTick(()=>{
                this.$refs[this.tabType=="second"?'dealerUser':'mfrsUser'].getlist(this.tabType=="second"?'franchiser':'vendor',this.$route.query.id)//经销商用户
                return;
            })
        },
        created(){
            this.businessType = this.$route.query.businessType
        },
        mounted(){
        },
        computed:{
             ...mapState('userManage',['userManageTabList','userMethod']),
        },
        beforeRouteLeave(to, from, next) {
            console.log('離開廠商頁面')
            next();
        },
        methods:{
            ...mapActions('userManage',['setTabList','setUserMethod','setUserType']),
            handleClick(e) {
                if(e.name=="second"){
                    this.mfrsUserShow = false;
                    setTimeout(()=>{
                         this.setUserMethod('franchiser');
                         this.$refs.dealerUser.getlist('franchiser',this.$route.query.id)//经销商用户
                    },100)
                }else if(e.name=="first"){
                    this.mfrsUserShow = true;
                    setTimeout(()=>{
                         this.setUserMethod('vendor');
                         this.$refs.mfrsUser.getlist('vendor',this.$route.query.id)//厂商用户
                    },100)
                }
            },
            reset(){
                this.$refs.dealerUser.reset();
                this.$refs.mfrsUser.reset();
            },
        },
        deactivated(){
            this.reset();
        }
    }
</script>

<style scoped lang="scss">
    .tabBox{
        margin-top: 20px;
    }
</style>
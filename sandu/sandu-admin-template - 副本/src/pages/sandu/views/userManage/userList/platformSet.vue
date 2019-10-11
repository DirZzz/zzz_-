<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: item.path }" v-for="(item,index) in userManageTabList" :key="index">{{item.name}}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="platformSet">
            <el-transfer
                    class="transferBox"
                    v-model="arrVal"
                    @change="transferChange"
                    :titles="['配置账号权限', '取消账号权限']"
                    :button-texts="['取消','确定']"
                    :props="{
                      key: 'id',
                      label: 'name'
                    }"
                    :data="platformArr">
            </el-transfer>
        </div>
    </page>
</template>

<script>
    import { mapState,mapActions} from 'Vuex'
    export default {
        name: "platformSet",
        data(){
            return{
                arrVal:[],
                platformArr:[]
            }
        },
        computed:{
            ...mapState('userManage',['userManageTabList'])
        },
        created(){
            this.userManageTabList.push({
                path:'',
                name:'平台设置'
            });
            this.userManageTabList.map((res,index)=>{
                if(index<this.userManageTabList.length-1){
                    res.oldPath ? res.path=res.oldPath : '';
                }
            });
            this.setTabList(this.userManageTabList);
            this.API.platformList({ userId:this.$route.query.userId }).then(res=>{
                if(res.success){
                    this.platformArr=res.obj;
                }else {
                    this.$message.error(res.message);
                }
            });
            this.myPlatformSet();
        },
        methods:{
            ...mapActions('userManage',['setTabList']),
            myPlatformSet(){
                this.API.platformSet({ userId:this.$route.query.userId }).then(res=>{
                    if(res.success){
                        this.arrVal=res.obj.platforms || [];
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            transferChange(val){
                this.API.platformUpdate({
                    userId:this.$route.query.userId,
                    platforms:this.arrVal
                }).then(res=>{
                    if(res.success){
                        this.$message.success( '操作成功')
                    }else {
                        this.$message.error(res.message);
                    }
                })
            }
        }
    }
</script>

<style lang="scss">
    .platformSet{
        margin-top: 80px;
        .transferBox{
            display: inline-block;
            position: absolute;
            margin-left: 280px;
        }
        .el-transfer-panel{
            width: 250px;
            height: 400px;
        }
        .el-transfer-panel__list{
            height: 400px;
        }
    }

</style>
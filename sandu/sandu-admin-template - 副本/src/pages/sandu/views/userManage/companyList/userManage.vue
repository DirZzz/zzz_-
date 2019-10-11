<template>
    <page>
        <div class="main">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: item.path }" v-for="(item,index) in userManageTabList" :key="index">{{item.name}}</el-breadcrumb-item>
            </el-breadcrumb>
            <div class="main-header">
                <el-row>
                    <el-col :span="4">
                        <el-input v-model="formParam.nickName" placeholder="请输入登录名" clearable></el-input>
                    </el-col>
                    <el-col :span="4">
                        <el-input v-model="formParam.Mobile" placeholder="请输入手机号" clearable></el-input>
                    </el-col>
                    <el-col :span="4">
                        <el-input v-model="formParam.userName" placeholder="请输入昵称" clearable></el-input>
                    </el-col>
                    <el-col :span="4">
                        <el-input v-model="formParam.servicesName" placeholder="请输入套餐名称" clearable></el-input>
                    </el-col>
                    <el-col :span="4">
                        <accountType ref="accountType" @change="accountChange"></accountType>
                    </el-col>
                    <el-col :span="4">
                        <userType ref="userType" @change="userChange" :type="'reset'"></userType>
                    </el-col>
                </el-row>
                <el-row>
                    <el-date-picker
                            v-model="timeArr"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                    <el-select v-model="selectValue" clearable placeholder="主子类型">
                        <el-option
                                v-for="item in accountType"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-row>
                <div class="btn">
                    <el-button type="primary" size="medium" round @click="search">搜索</el-button>
                    <el-button size="medium" round @click="reset">重置</el-button>
                </div>
            </div>
            <userList @handleSelectionChange="handleSelectionChange" @showBatch="showBatch" :userdata="userListData" :businessType='businessType' :loading='loading' :page="page" @refresh="getlist(userMethod,$route.query.id)" @pageChange="pageChange" @sizeChange="sizeChange" @resetLoad="resetLoad"></userList>
           <batch v-if="batchShow"  @pwdClose="pwdClose"
                @pwdSure="pwdSure"></batch>
            <packageEdit v-if="comPackShow" ref="packageEdit" @mealSure="mealSure"></packageEdit>
        </div>
    </page>
</template>

<script>
    import {filter, find, map, merge, uniq} from 'lodash'
    import minixs from '@s/minixs/index'
    import userList from '@s/components/userList/userList'
    import batch from './../batch/index';
    import { mapState,mapActions} from 'Vuex'
    import packageEdit from '@s/components/userList/packageEdit'
    export default {
        name: "index",
        mixins:[minixs],
        components:{
            userList,
            batch,
            packageEdit
        },
        data(){
            return{
                timeArr:'',
                selectValue:'',
                userListData:'',
                page:1,
                limit:10,
                businessType:'',
                loading:true,
                formParam:{
                    nickName:'',
                    Mobile:'',
                    userName:'',
                    servicesName:'',
                    userType:'',
                    effectiveBegin:'',
                    effectiveEnd:'',
                    companyId:''
                },
                multipleSelection:[],
                batchShow: false,
                comPackShow: false,
                accountType:[
                    { name:'普通账号',value:0 },
                    { name:'子账号',value:1 },
                    { name:'主账号',value:2 },
                ]
            }
        },
        computed:{
            ...mapState('userManage',['userManageTabList','userMethod'])
        },
        activated(){
            this.setUserMethod('company');
            let arr=[{
                path:'/userManage/companyList',
                name:'公司列表'
            },{
                path:'',
                name:'用户管理',
                oldPath:`/userManage/companyList/userManage?id=${this.$route.query.id}`
            }];
            this.setTabList(arr);
            this.getlist(this.userMethod,this.$route.query.id);
            this.setUserTypeFun();
        },
        created(){
            this.businessType = this.$route.query.businessType;
            this.comPackShow= true;
        },
         deactivated() {
            this.comPackShow=false;
        },
        methods:{
            ...mapActions('userManage',['setTabList','setUserMethod','setUserType']),
           showBatch(type) {
                if (this.multipleSelection.length > 0) {
                    let arrm= this.multipleSelection;
                    let boolUserType = true;
                    if (type == 1) {
                        function arr(n) {
                            if(n>0){
                                if(arrm[n].userType==arrm[n-1].userType) {
                                    boolUserType = true;
                                    return arr(n-1);
                                }else {
                                    boolUserType = false;
                                    return;
                                }
                            }
                        }
                        arr(arrm.length-1);
                        if(!boolUserType) {
                            this.$message.error('只能批量修改统一用户类型的账号！');
                        }else{
                            let item = {
                                userType: arrm[0].userType,
                                fromType: 0
                            };
                            this.setServicelist({
                                vm: this,
                                item: item
                            });
                            this.setDialog({ packageEditDialog:true, packageEditFrom: 1, packageEditFromType: 4});
                            this.setIsNewAdd(3)
                        }
                        
                    }else {
                        this.batchShow = true;
                    }
                } else {
                    this.$message.error('请至少勾选一个用户');
                }
            },
            mealClose() {
                this.batchShow = false;
            },
            mealSure(id) {
                 let mark = {
                    servicesId: id,
                    userIds: ""
                }
                 var arrm = this.multipleSelection;
                function arr(n) {
                    return n == 0 ? mark.userIds += arrm[0].id : mark.userIds += arrm[n].id +
                        ',' + arr(n - 1);
                }
                arr(arrm.length - 1);
                this.API.batchModify(mark).then(res=>{
                    if (res.success) {
                        this.$message.success('设置成功！')
                        this.getlist(this.userMethod,this.$route.query.id)
                        this.pwdClose();
                    } else {
                        this.$message.error(res.message);
                    }
                })
            },
            pwdClose() {
                this.batchShow = false;
            },
            pwdSure(obj) {
                let mark = {
                    random: obj.random,
                    password: obj.password,
                    userIds: ""
                }
                var arrm = this.multipleSelection;

                function arr(n) {
                    return n == 0 ? mark.userIds += arrm[0].id : mark.userIds += arrm[n].id +
                        ',' + arr(n - 1);
                }
                arr(arrm.length - 1);
                this.API.batchModifyPassword(mark).then(result => {
                     if (result && result.size > 0) {
                      this.$message.success("保存成功!")
                      let url = window.URL.createObjectURL(result)
                      let link = document.createElement('a')
                      link.style.display = 'none'
                      link.href = url
                      link.setAttribute('download', '修改密码.xls')
                      document.body.appendChild(link)
                      link.click()
                      this.pwdClose()
                  } else {
                      this.loadingTwo.close()
                      this.$message.error("没有数据需要导出!")
                  }
                })
            },
            handleSelectionChange(val) {
               this.multipleSelection = val;
            },
            sizeChange(val){
                this.limit=val;
                this.loading=true;
                this.getlist(this.userMethod,this.$route.query.id);
            },
            pageChange(val){
                this.page=val;
                this.loading=true;
                this.getlist(this.userMethod,this.$route.query.id);
            },
            search(){
                this.page = 1;
                this.loading=true;
                this.getlist(this.userMethod,this.$route.query.id);
            },
            resetLoad(){
                this.getlist(this.userMethod,this.$route.query.id);
            },
            setUserTypeFun(){
                let companyId = this.$route.query.id?this.$route.query.id:this.$route.query.companyId;
                let data = {vm:this,companyId:companyId}
                this.setUserType(data);
            },
            reset(){ //重置
                for(let key in this.formParam){
                    this.formParam[key]=null;
                }
                this.$refs.accountType.reset();
                this.$refs.userType.reset();
                this.timeArr='';
                this.selectValue='';
                this.loading=true;
                this.getlist(this.userMethod,this.$route.query.id);

            }
        }
    }
</script>

<style scoped lang="scss">
    .main{
        .main-header{
            margin-top: 20px;
        }
        .btn{
            text-align: center;
        }
    }
    .newAdd{
        margin-bottom: 20px;
    }
</style>
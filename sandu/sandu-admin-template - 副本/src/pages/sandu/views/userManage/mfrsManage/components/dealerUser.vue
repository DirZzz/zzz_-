<template>
    <div class="main">
        <div class="main-header">
            <el-row>
                <el-col :span="4">
                    <el-input v-model="formParam.nickName" placeholder="请输入登录名"></el-input>
                </el-col>
                <el-col :span="4">
                    <el-input v-model="formParam.Mobile" placeholder="请输入手机号"></el-input>
                </el-col>
                <el-col :span="4">
                    <el-input v-model="formParam.userName" placeholder="请输入昵称"></el-input>
                </el-col>
                <el-col :span="4">
                    <el-input v-model="formParam.servicesName" placeholder="请输入套餐名称"></el-input>
                </el-col>
                <el-col :span="4">
                    <accountType @change="accountChange" ref="accountType"></accountType>
                </el-col>
                <el-col :span="4">
                    <franchiserType @change="franchiserTypeChange" ref="franchiserType"></franchiserType>
                </el-col>
            </el-row>
            <el-row>
                <el-date-picker v-model="timeArr" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                                range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
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
        <userList @handleSelectionChange="handleSelectionChange" @showBatch="showBatch" :userdata="userListData"
            :page="page" :businessType='businessType' :loading="loading" @refresh="getlist(userMethod,$route.query.id)"
            @pageChange="pageChange" @sizeChange="sizeChange" @pageReset="pageReset" @resetLoad="resetLoad"></userList>
        <batch v-if="batchShow"  @pwdClose="pwdClose" @pwdSure="pwdSure"></batch>
         <packageEdit  v-if="dealUserCnmShow" ref="packageEdit" @mealSure="mealSure"></packageEdit>
    </div>
</template>

<script>
    import userList from '@s/components/userList/userList'
    import belongIndustry from '@s/components/commonSelect/belongIndustry' //所属行业
    import minixs from '@s/minixs/index'
    import batch from './../../batch/index';
    import packageEdit from '@s/components/userList/packageEdit'
    export default {
        name: "dealerUser",
        props: ['userData', 'businessType'],
        mixins: [minixs],
        components: {
            userList,
            belongIndustry,
            batch,
            packageEdit
        },
        data() {
            return {
                loading: true,
                timeArr: [],
                userListData: '',
                selectValue: '',
                page: 1,
                limit: 10,
                formParam: {
                    nickName: '',
                    Mobile: '',
                    userName: '',
                    servicesName: '',
                    userType: '',
                    useType: null,
                    effectiveBegin: '',
                    effectiveEnd: '',
                    companyId: '',
                    franchiserId: ''
                },
                multipleSelection: [],
                batchShow: false,
                dealUserCnmShow: false,
                accountType:[
                    { name:'普通账号',value:0 },
                    { name:'子账号',value:1 },
                    { name:'主账号',value:2 },
                ]
            }
        },
        created() {
              this.dealUserCnmShow = true;
        },
        deactivated() {
            console.log('離開頁面')
            this.dealUserCnmShow=false;
        },
        methods: {
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
                            this.setDialog({ packageEditDialog:true, packageEditFrom: 1, packageEditFromType: 2});
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
                console.log(val, 'parent')
                this.multipleSelection = val;
                console.log(this.multipleSelection)
            },
            search() {
                this.loading = true;
                this.page = 1;
                this.getlist(this.userMethod, this.$route.query.id);
            },
            resetLoad(){
                this.getlist(this.userMethod, this.$route.query.id);
            },
            sizeChange(val) {
                this.page = 1;
                this.limit = val;
                this.loading = true;
                this.getlist(this.userMethod, this.$route.query.id);
            },
            pageChange(val) {
                this.page = val;
                this.loading = true;
                this.getlist(this.userMethod, this.$route.query.id);
            },
            resetLoad(){
                this.getlist(this.userMethod,this.$route.query.id);
            },
            reset() { //重置
                for (let key in this.formParam) {
                    this.formParam[key] = null;
                }
                this.$refs.accountType.reset();
                this.$refs.franchiserType.reset();
                this.timeArr = '';
                this.selectValue = '';
                this.loading = true;
                this.getlist(this.userMethod, this.$route.query.id);
            },
            franchiserTypeChange(val) {
                this.formParam.franchiserId = val;
            }
        }
    }
</script>

<style scoped lang="scss">
    .main {
        margin-top: 20px;

        .btn {
            text-align: center;
            margin-bottom: 20px;
        }
    }
</style>
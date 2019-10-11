<template>
    <div class="addRule">
        <v-nav :nav="nav"></v-nav>
        <div class="addRule-content">
            <div class="header"><span>新增</span></div>
            <div class="addRule-search">
                <div class="addRule-company">
                    <span class="text">所属经销商企业：</span>
                    <el-select v-model="belongValue" filterable clearable placeholder="所属经销商企业" @change="chooseConpany">
                        <el-option v-for="item in conpanyList"
                                   :key="item.id"
                                   :label="item.companyName"
                                   :value="item.id"
                        ></el-option>
                    </el-select>
                </div>
                <addArea ref="addArea" @closeMd="closeMd"></addArea>
            </div>
            <div class="saveBtn">
                <el-button type="primary" @click="save" style="width: 120px" class="confirmButtonClass" size="medium">保 存</el-button>
                <el-button @click="$router.go(-1)" style="width:120px" class="cancelButtonClass" size="medium">取 消</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import addArea from './components/addArea'
    import city from '@/filters/city'
    export default {
        name: "rules",
        mixins:[city],
        components: {
            addArea
        },
        data() {
            return {
                isDistribution:true,
                loading:false,
                belongValue:'',
                tableData: [],
                allCode: [],
                conpanyList:'',
                setTimeout:"",
                nav:[
                    {nav: '业主管理列表', url: '/customer'},
                    {nav: '设置分配规则', url: '/rules'},
                    {nav: '新增规则', url: '/addRule'},
                ]
            }
        },
        created() {
            sessionStorage.setItem('channelCompanyId','');
            this.API.companyList({companyId:sessionStorage.getItem('companyID')}).then(res=>{
                res.code=='200' ? this.conpanyList=res.data : this.$message(res.message);
            })
        },
        methods: {
            save(){
                if(this.isDistribution){
                    this.belongValue ? this.$refs.addArea.save(): this.$message.error("请选择经销商企业");
                }else {
                    this.$message.error("该经销商已设置分配规则");
                }

            },
            closeMd(){
                this.setTimeout=setTimeout(()=>{
                    this.$router.push('/rules');
                },1500);
            },
            chooseConpany(val){
                this.isDistribution=true;
                this.API.companyListRule({companyId:val}).then(res=>{
                    if(res.code==200){
                        this.$nextTick(()=>{
                            this.$refs.addArea.getChannelCompanyList(res.data ? res.data.allCode:[],val);
                        })
                    }else if(res.code==204){ //没有数据
                        this.$nextTick(()=>{
                            this.$refs.addArea.getChannelCompanyList(res.data ? res.data.allCode:[],val);
                        })
                        this.isDistribution=false;
                        this.$message.error(res.message);
                    }else {
                       this.$message.error(res.message);
                    }
                })
            }
        },
        destroyed(){
            clearTimeout(this.setTimeout);
        }
    }
</script>

<style lang="scss" scoped>
    .addRule {
        .nav{
            padding-top: 10px;
        }
        .addRule-content{
            background-color: #fff;
            padding: 0 20px;
            .header{
                padding: 20px 30px;
                color: #333;
                font-size: 20px;
                line-height: 36px;
                position: relative;
                border-bottom: 1px solid #ddd;
                font-weight: bold;
                .addRule{
                    height: 36px;
                    position: absolute;
                    right: 0;
                }
            }
            .addRule-company{
                padding-top: 20px;
                color: #666666;
                font-size: 14px;
                .input-company{
                    height: 36px;
                    line-height: 36px;
                    background-color: #fafafa;
                    border-radius: 2px;
                    border: solid 1px #e8e8e8;
                    width: 360px;
                    text-indent: 16px;
                    &::placeholder{
                        color: #999;
                    }
                }
            }
            .saveBtn{
               border-top: 1px solid #ddd;
               padding: 40px 0;
            }

        }

    }

</style>
<template>
    <div class="detailFrame">
        <v-nav :nav="nav"></v-nav>
        <div class="headTitle">业主详情</div>
        <div>
            <el-row :gutter="24" class="splineFrame">
                <el-col :span="2" class="productBaseInfo">基本信息</el-col>
                <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>
        </div>
        <div>
            <ul class="baseInfo">
                <li>手机号：{{detailInfo.mobile || '---无---'}}</li>
                <li><img src="../../../assets/images/icons/level.png" style="margin-right:4px;vertical-align: -3px">业主等级：
                    {{detailInfo.level==0 ? '普通用户': detailInfo.level==1 ? '活跃业主': detailInfo.level==2 ? '潜在业主' : '忠实业主'}}
                    ({{detailInfo.score}}分)
                </li>
                <li>状态：{{detailInfo.alotStatusName || '---无---'}}</li>
                <li>业主地址：{{detailInfo.address || '---无---'}}</li>
            </ul>
        </div>

        <div>
            <el-row :gutter="24" class="splineFrame">
                <el-col :span="2" class="productBaseInfo">跟进信息</el-col>
                <el-col :span="22" class="rightLine"><hr></el-col>
            </el-row>
        </div>
        <div>
            <ul class="genjin">
                <li><span>所属经销商企业：</span>{{detailInfo.channelCompanyName || '---无---'}}</li>
                <li><span>跟进人：</span>{{detailInfo.followUpUserName || '---无---'}}</li>
                <li><span>跟进结果：</span>{{detailInfo.followUpResult || '---无---'}}</li>
            </ul>
        </div>
    </div>
</template>

<script>
    export default {
        data(){
            return {
                name:'',
                detailId:'',
                detailInfo:[],
                nav:[
                    {nav: '业主管理列表', url: '/ourCustomer'},
                    {nav: '详情', url: ''}
                ]
            }
        },
        beforeRouteEnter (to, from, next) {

            next( vm => {
                vm.nav[0].url=from.path
            })
        },
        methods:{
            getDetailInfo(){
                this.API.customerDetail({
                    id:this.detailId
                }).then((res) => {
                    console.log(res, 'details')
                    this.detailInfo = res.data
                })
            }
        },
        created(){
            this.detailId = this.$route.params.id
            this.getDetailInfo()
        }
    }
</script>

<style scoped lang="scss">
    .detailFrame{
        background: #fff;
        padding: 20px;
    }
    .headTitle{
        border-bottom: solid 1px #ddd;
        height: 76px;
        line-height: 76px;
        font-size: 20px;
        font-weight: normal;
        color: #666666;
    }
    .baseInfo{
        padding-left: 40px;
        li{
            display: inline-block;
            width: 30%;
            margin-bottom: 25px;
        }
    }
    .genjin{
        li{
            margin-bottom: 25px;
        }
      span{
          display: inline-block;
          width: 120px;
          text-align: right;

      }
    }
</style>
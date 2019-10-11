<template>
    <div class="rulesDetail">
        <v-nav :nav="nav"></v-nav>
        <div class="rulesDetail-content">
            <div class="header"><span>详情</span></div>
            <div class="conpany"><span>所属经销商企业：</span><em>{{$route.query.channelCompanyName}}</em></div>
            <div class="areaBox">
                <div class="left">分配区域：</div>
                <div class="right">
                    <div class="address-box" v-for="(item,key) in addressList" :key="key"><i></i>{{item.provinceName}}{{item.cityName}}{{item.areaCodeName}}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "rulesDetail",
        components: {

        },
        data() {
            return {
                addressList: [],
                nav:[
                    {nav: '业主管理列表', url: '/customer'},
                    {nav: '设置分配规则', url: '/rules'},
                    {nav: '设置分配详情', url: ''},
                ]
            }
        },
        created() {
            this.API.customerRule({companyId:this.$route.params.channelCompanyId}).then(res=>{
                if(res.code==200){
                    res.data ? this.addressList=res.data.allCode : ''
                }else if(res.code==204){ //没有数据

                }else {
                    this.$message(res.message);
                }
            })
        },
        methods: {

        },
    }
</script>

<style lang="scss" scoped>
    .rulesDetail {
        .nav{
            padding-top: 10px;
        }
        .rulesDetail-content{
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
            }
            .conpany{
                color: #666;
                font-size: 14px;
                padding: 20px 0;
                em{
                    color: #333;
                    font-style: normal;
                }
            }
            .areaBox{
                .left{
                    display: inline-block;
                    vertical-align: top;
                    color: #666;
                    font-size: 14px;
                }
                .right{
                    display: inline-block;
                    .address-box{
                        padding-bottom: 10px;
                        i{
                            display: inline-block;
                            width: 20px;
                            height: 20px;
                            background: no-repeat center url('../../../assets/images/icons/qi.png');
                            background-size: 100%;
                            vertical-align: middle;
                            margin-right: 10px;
                        }
                    }
                }
            }
        }

    }

</style>
<template>
    <div>
        <ul class="tabChange">
            <li @click="topage(item)" :class="{'active': item.active}" v-for="(item,index) in tab" :key="index"><span>{{item.name}}</span></li>
        </ul>
        <router-view></router-view>
    </div>
</template>

<script>
    export default {
        data(){
            return {
                tab:[
                    {name:'个人设置', path: '/personal/personalSet',active:true},
                    {name:'修改密码', path: '/personal/updatePassword',active:false},
                    {name:'修改手机号', path: '/personal/updateMobile',active:false}
                ]
            }
        },
        methods:{
            topage(item) {
                this.tab.map((it) => {
                    it.active = false
                })
                item.active =true
                this.$router.push(item.path)
            }
        },
        mounted(){
            this.$router.push('/personal/personalSet')
        }
    }
</script>

<style scoped lang="scss">
    .tabChange{
        background: #fff;
        height: 60px;
        display: block;
        line-height: 60px;
        border-bottom: solid 1px #ddd;
        li{
            display: inline-block;
            height: 18px;
            line-height: 18px;
            width: 114px;
            text-align: center;
            cursor: pointer;
            border-right: solid 1px #ddd;
            &:last-child{
                border-right: none;
            }
            &.active{
                color: #FF6419;
            }
        }
    }

</style>
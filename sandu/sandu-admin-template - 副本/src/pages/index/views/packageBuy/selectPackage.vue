<template>
    <div>
        <header class="Htitle"><img src="../../assets/images/icons/selectStaus.png" alt="">套餐购买</header>
        <div class="list">
           <ul class="selectTab">
               <li :class="{'active': tab.active}" v-for="(tab,index) in tabselect" :key="index"  @click="toggle(tab)">{{tab.title}}</li>
           </ul>
        </div>
        <router-view></router-view>
    </div>
</template>

<script>
    export default {
        data () {
            return{
                loading:'',
                tabselect:[
                    {title:'可选套餐',active:true},
                    {title:'已购套餐',active:false},
                    {title:'消费记录',active:false},
                ],
                options: [{
                    value: '选项1',
                    label: '选项1'
                }, {
                    value: '选项2',
                    label: '选项2'
                }],
                value: ''
            }
        },

        methods :{
            toggle(tab){
                // this.changeStyle(tab)
                this.$router.push('/packagelist')
                switch (tab.title) {
                    case '可选套餐': this.$router.push('/packagelist');break;
                    case '已购套餐': this.$router.push('/buylist');break;
                    case '消费记录': this.$router.push('/record');break;
                }
            },
        },
        watch: {
            $route(to, from) {
               if(to.name == 'buylist') {
                   this.tabselect.map((item) => {
                       item.active = false;
                   })
                   this.tabselect[1].active =true

               } else if(to.name == 'packagelist') {
                    this.tabselect.map((item) => {
                        item.active = false;
                    })
                    this.tabselect[0].active = true
                } else {
                   this.tabselect.map((item) => {
                       item.active = false;
                   })
                   this.tabselect[2].active = true
               }
            }
        },
        created(){
            this.$router.push('/packagelist')
        },
       component:{

       }
    }
</script>

<style lang="scss" scoped>
    .Htitle {
        height: 40px;
        line-height: 40px;
        background-color: #fafafa;
        img{
            vertical-align: -5px;
            margin: 0 10px;
        }
    }
    .list{
        background: #fff;
        .selectTab{
            padding:20px 0;
            border-bottom: solid 1px #ddd;
            li{
                display: inline-block;
                text-align: center;
                height: 18px;
                width:124px;
                border-right: solid 1px #ddd;
                cursor: pointer;
                &.active{
                    color: #FF6419;
                }
            }
        }
    }
</style>
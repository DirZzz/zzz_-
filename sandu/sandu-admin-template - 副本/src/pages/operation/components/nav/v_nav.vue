<template>
    <div class="nav">
        <!-- <span @click="back('返回')">返回上一级</span>
        | -->
        <span
                v-for="(item, index) in nav"
                :key="index"
                v-if="index != nav.length - 1"
                @click="back(index)">{{item.nav}}&nbsp;&nbsp;{{index != nav.length - 1 ? '/' : ''}}</span>
        <p>{{nav[nav.length-1].nav}}</p>
    </div>
</template>

<script>
    export default {
        name: "v-nav",
        props:['nav'],
        methods: {
            back(type) {
                if (type == '返回') {
                    this.$router.push(this.nav[this.nav.length-2].url);
                } else {
                    this.$router.push(this.nav[type].url);
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .nav{
            text-align: left;
            color: #999999;
            font-size: 14px;
            padding: 20px 0;
        span{
            padding: 0 5px;
            cursor:pointer;
        }
        p{
            color: #666666;
            padding: 0 5px;
            display: inline-block;
        }
    }

</style>

<template>

<div class="showImgRows" v-if="flag">
    <div class="closeImg">
        <i class="el-icon-close" @click="closeImg"></i>
    </div>
    <el-carousel indicator-position="outside" height="100%" arrow="always" :initial-index="indexActive" :loop='falseFlag' :autoplay='falseFlag' >
        <el-carousel-item v-for="(item,index) in obj" :key="index">
        <img :src="item" alt="">
        </el-carousel-item>
    </el-carousel>
</div>
</template>

<script>
import qs from 'qs'
  export default {
    name:'showBigImg',
    data () {
      return {
          sourceBaseUrl:'',
      }

    },
    props:['flag','obj','indexActive','falseFlag'],
    created () {

    },
    mounted () {
    },
    methods: {
        closeImg(){
            this.$emit('closeImg')
        }
    },
  }
</script>
<style lang="scss" scoped>
.showImgRows{
    position: fixed;
    width: 100%;
    height: 100%;
    padding-top: 50px;
    top: 0;
    left: 0;
    box-sizing: border-box;
    background: rgba(0,0,0,.7);
    z-index: 999;
    .el-carousel{
        width: 100%;
        height: 100%;
        overflow: hidden;
        img{
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            margin: auto;
            max-width: 75%;
        }
    }
    .closeImg{
        width: 100%;
        height: 50px;
        background: rgba(0,0,0,.6);
        position: absolute;
        top: 0;
        left: 0;
        z-index: 99999;
        i{
            width: 50px;
            height: 50px;
            font-size: 12px;
            color: #fff;
            cursor: pointer;
            position: absolute;
            line-height: 50px;
            top: 0;
            right: 0;
            text-align: center;
        }
    }
}

</style>

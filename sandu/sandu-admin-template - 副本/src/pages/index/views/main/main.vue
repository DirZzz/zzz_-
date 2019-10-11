
<template>
  <div class="main">
    <section class="main-content" ref="contentScroll" @scroll="handleScroll">
      <div class="right-aside">
        <keep-alive>
           <router-view v-if="$route.meta.keepAlive"></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive"></router-view>
      </div>
      <transition name="fade">
         <div class="common-back-top" v-show="isBackTop" @click="backTop"></div>
      </transition>
    </section>
  </div>
</template>

<script>
  import addEnterprise from '../BusinessManagement/AddEnterprise'

  export default {
    name: 'my-main',
    components: {
      addEnterprise
    },
    data(){
        return{
            isBackTop:false
        }
    },
    mounted(){

    },
    watch:{
       $route(to,from){
           this.$nextTick(()=>{
               this.$refs.contentScroll.scrollTop=0;
           });
       }
    },
    methods:{
        handleScroll(e){
            if(e.target.scrollTop>250){
                this.isBackTop=true;
            }else {
                this.isBackTop=false;
            }
        },
        backTop(){
            this.$refs.contentScroll.scrollTop=0;
        }
    }
  }
</script>
<style lang='scss' scoped>
  .main-content {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    margin-top: 60px;
    width: 100%;
    height:  calc(100% - 60px);
    overflow-y: scroll;
  }
  .empty{
    width: 100%;
    height: 60px;
  }
  .right-aside {
    position: absolute;
    bottom: 10px;
    top: 10px;
    right: 10px;
    left: 258px;
    border-radius: 6px;
    height: 100%;
    /*background: #fff;*/
    //background: red;
  }
  //公共返回顶部
  .common-back-top{
    position: fixed;
    right: 20px;
    top: 720px;
    width: 50px;
    height: 50px;
    background: no-repeat center url('../../assets/images/back-top-normal.png');
    background-size: 100%;
    cursor: pointer;
    z-index: 110;
    &:hover{
      opacity: .5;
    }
  }
  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.8s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */
  {
    opacity: 0;
  }
</style>

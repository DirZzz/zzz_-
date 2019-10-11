<!--子路由入口-->
<template>
 <div class="page">
     <keep-alive>
         <router-view v-if="$route.meta.keepAlive"></router-view>
     </keep-alive>
     <router-view v-if="!$route.meta.keepAlive"></router-view>
 </div>
</template>

<script type="text/ecmascript-6">
export default {
 data() {
     return {

     }
 },
 methods: {

 }
}
</script>

<style scoped lang="scss">
</style>

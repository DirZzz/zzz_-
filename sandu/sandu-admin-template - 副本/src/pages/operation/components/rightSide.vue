<template>
    <div class="rightSide" ref="warper">
        <router-view></router-view>
    </div>
</template>

<script>
    import { mapGetters,mapActions } from 'Vuex'
    export default {
        name: "rightSide",
        data(){
            return{
                scrollTop: 0,
            }
        },
        mounted(){
            // this.$nextTick(()=>{
            //     this.setRightSide(this.$refs.warper);
            //     this.$refs.warper.addEventListener('scroll',this.handelscroll)
            // })
        },
        watch:{
            $route(to,from){
                this.$nextTick(()=>{
                    this.$refs.warper.scrollTop=0;
                });
            }
        },
        methods:{
            ...mapActions(['setRightSide']),
            handelscroll(){
                this.$nextTick(()=>{
                    this.scrollTop = this.$refs.warper.scrollTop;
                })
            },
            backTop(){
                this.$refs.warper.scrollTop=0;
            }
        }
    }
</script>

<style scoped>
  .rightSide{
      flex: 1;
      height: 100%;
      overflow: hidden;
      overflow-y: scroll;
  }
</style>
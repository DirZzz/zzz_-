<template>
    <div class="leftSide">
         <div class="menu">
             <!--导航菜单-折叠功能-->
             <aside  :class="collapsed ? 'menu-collapsed':'menu-expanded'">
                 <!--单个激活 并以 index 作为 path 进行路由跳转-->
                 <el-menu unique-opened  @open="isActive" @close="clearClass"   :router="true" v-show="!collapsed"   background-color="#ffffff" text-color="#999999" active-text-color="#ffffff" @select="select">
                     <!--动态路由到子组件 将不可见的路径隐藏-->
                     <template v-for="(item,index) in $router.options.routes"   v-if="permission(item.permission)">
                         <el-submenu :index="index+''"  :key="index"  v-if="!item.leaf">
                             <!--用el ui 的title进行solt分发菜单-->
                             <template slot="title"><img :src="item.routeIndex==currentIndex ? item.iconSel : item.icon" alt="" >{{item.name}}</template>
                             <!--item.name和item.children.name来配置菜单栏和子菜单栏的名称-->
                             <el-menu-item v-for="child in item.children"  class="childitem"  :index="child.path" :key="child.path"  v-if="permission(child.permission)" > <span :class="{'active':child.path==currentPath}">{{child.name}}</span></el-menu-item>
                         </el-submenu>
                         <el-menu-item v-if="item.leaf" :key="item.path" :index="item.path" :class="{'el-menu-item-active':item.path==currentPath}"><img :src="item.path==currentPath ? item.iconSel : item.icon" alt="">{{item.name}}</el-menu-item>
                     </template>
                 </el-menu>
               </aside>
         </div>
    </div>
</template>

<script>
    export default {
        name: "leftSide",
        created(){

        },
        data(){
            return {
                currentPath:'',
                collapsed:false,
                currentIndex:100,
            }
        },
        methods:{
            select(indexPath){
                this.currentPath=indexPath;
                //console.log(indexPath)
               // console.log(this.$router.options.routes)
            },
            isActive(val){
               this.currentIndex=val;
             // console.log(val)
            },
            clearClass(){
                this.currentIndex=100;
            }

        }
    }
</script>

<style lang="scss" scoped>
    .leftSide{
        background: white;
        width: 230px;
        z-index: 10;
        height: 100%;
        user-select: none;
        box-shadow: 1px 2px 4px rgba(0, 0, 0,0.2);
        .menu{

            ul{
                border-right: none;

                li{
                    text-align: center;
                    cursor: pointer;
                    .isActive{
                        color: $theme-color!important;
                    }
                   // border-bottom: 1px solid #404040;
                   .childitem{
                       background: #f5f5f5!important;
                        min-width:180px!important;
                        position: relative;
                       color:#999999!important;
                        .active{
                            height: 20px;
                            padding: 12px;
                            line-height:0px;
                            color:#fff;
                            display:inline-block;
                            background:$theme-color;
                            border-radius: 4px;
                        }
                   }
                   .childitem:hover{
                        background: rgb(204,204,204)!important;
                   }
                    img{
                        margin-right: 8px;
                        margin-top: -2px;
                    }
                }
            }
        }
    }
</style>

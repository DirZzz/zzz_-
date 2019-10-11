<template>
  <div class="leftSide">
    <div class="menu">
      <!--导航菜单-折叠功能-->
      <aside :class="collapsed ? 'menu-collapsed':'menu-expanded'">
        <!--单个激活 并以 index 作为 path 进行路由跳转-->
        <el-menu
          unique-opened
          @open="isActive"
          @close="clearClass"
          :router="true"
          v-show="!collapsed"
          background-color="#ffffff"
          text-color="#999999"
          active-text-color="#409EFF"
          @select="select"
          :default-openeds="[currentPath]"
        >
          <!--动态路由到子组件 将不可见的路径隐藏-->
          <template
            v-if="permission(item.permission)"
            v-for="item in $router.options.routes[$store.state.routerIndex].children"
          >
            <el-submenu :index="item.path" :key="item.path" v-if="item.children">
              <!--用el ui 的title进行solt分发菜单-->
              <template slot="title">
                <img :src="item.routeIndex==currentPath ? item.iconSel : item.icon" alt>
                {{item.name}}
              </template>
              <!--item.name和item.children.name来配置菜单栏和子菜单栏的名称-->
              <el-menu-item
                v-if="permission(child.permission)"
                v-for="child in item.children"
                class="childitem"
                :index="child.path"
                :key="child.path"
              >
                <span :class="{'active':child.path==currentSubPath}">{{child.name}}</span>
              </el-menu-item>
            </el-submenu>
            <el-menu-item
              v-if="!item.children"
              :key="item.path"
              :index="item.path"
              :class="{'el-menu-item-active':item.path==currentSubPath}"
            >
              <img :src="item.path==currentSubPath ? item.iconSel : item.icon" alt>
              {{item.name}}
            </el-menu-item>
          </template>
        </el-menu>
      </aside>
    </div>
  </div>
</template>

<script>
export default {
  name: "leftSide",
  data() {
    return {
      currentSubPath: sessionStorage.getItem("currentSubPath") || "",
      collapsed: false,
      currentPath: sessionStorage.getItem("currentPath") || ""
    };
  },
  methods: {
    select(indexPath) {
      sessionStorage.setItem("currentSubPath", indexPath);
      this.currentSubPath = indexPath;
    },
    isActive(val) {
      sessionStorage.setItem("currentPath", val);
      this.currentPath = val;
    },
    clearClass() {
      this.currentPath = 100;
      sessionStorage.setItem("currentPath", "");
      sessionStorage.setItem("currentSubPath", "");
    }
  }
};
</script>

<style lang="scss" scoped>
.leftSide {
  background: white;
  width: 200px;
  z-index: 10;
  height: 100%;
  user-select: none;
  box-shadow: 1px 2px 4px rgba(0, 0, 0, 0.2);
  .menu {
    ul {
      border-right: none;

      li {
        text-align: center;
        cursor: pointer;
        .isActive {
          color: $theme-color !important;
        }
        // border-bottom: 1px solid #404040;
        .childitem {
          background: #f5f5f5 !important;
          min-width: 180px !important;
          position: relative;
          color: #999999 !important;
          .active {
            height: 20px;
            padding: 12px;
            line-height: 0px;
            color: #fff;
            display: inline-block;
            background: $theme-color;
            border-radius: 4px;
          }
        }
        .childitem:hover {
          background: rgb(204, 204, 204) !important;
        }
        img {
          margin-right: 8px;
          margin-top: -2px;
        }
      }
    }
  }
}
</style>

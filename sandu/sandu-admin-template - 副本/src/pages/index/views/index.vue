
<template>
  <div id="app">
    <div>
      <header-top></header-top>
    </div>
    <div class="left-aside">
      <div class="header-empty"></div>
      <div id="leftMenus">
        <ul class="tree" v-for="(item, index) in data" :key="index">
          <left-menus :data="item"></left-menus>
        </ul>
      </div>
    </div>
    <div class="right-aside">
      <mains></mains>
    </div>
  </div>
</template>
<script>
import leftMenus from "../views/menus/leftMenu";
import headerTop from "../views/public/header";
import mains from "../views/main/main";
import store from "../store";
import qs from "qs";
export default {
  name: "app",
  inject: ["reload"],
  data() {
    return {
      data: [],
      nowShow: "",
      parentsName: {}
    };
  },
  created() {
    // 控制装修报价菜单栏是否显示
    this.API2.showQuoted({
      companyId: sessionStorage.getItem("companyID")
    })
      .then(res => {
        if (res.obj) {
          let loginUser = qs.parse(sessionStorage.getItem("loginUser"));
          loginUser.permissions[Object.keys(loginUser.permissions).length] =
            "product:quptePlan";
          sessionStorage.setItem("loginUser", qs.stringify(loginUser));
        }
      })
      .finally(() => {
        this.setLeftMenus();
      });
  },
  methods: {
    setLeftMenus() {
      this.data = [];
      let router = store.getters.addRouters;
      this.parentsName = store.getters.parentsName;
      this.firstMenus();
      this.loopMenus(router);
      // console.log(router)
      // console.log(this.parentsName)
    },

    addChildren(arr, name, obj) {
      //console.log('addChildren')
      arr.forEach((item, index) => {
        let parentsName = item.name;
        if (parentsName == name) {
          item.children.push(obj);
        } else {
          if (item.children) {
            let hasChildren = item.children.length > 0 ? true : false;
            this.addChildren(item.children, name, obj);
          }
        }
      });
    },
    firstMenus() {
      //console.log('firstMenus')
      let obj = this.parentsName;
      obj.map((item, index) => {
        item.currentName = this.$store.state.menus.menus;
        this.data.push(item);
      });
    },

    loopMenus(data) {
      //console.log('loopMenus')
      data.forEach((item, index) => {
        let parentName = item.parentName;
        item.currentName = this.$store.state.menus.menus;
        if (!item.children) {
          item.children = [];
        }
        if (parentName) {
          this.addChildren(this.data, parentName, item);
        }
        if (item.children) {
          return this.loopMenus(item.children);
        }
      });
    }
  },
  components: {
    mains,
    headerTop, // 顶部
    leftMenus // 左边菜单
  }
};
</script>
<style>
[v-cloak] {
  display: none;
}
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  overflow: hidden;

  background: #f0f0f0;
}
.right-aside {
  position: absolute;
  top: 10px;
  left: 0;
  right: 10px;
  height: 100%;
}
.header-empty {
  height: 60px;
  width: 238px;
}
.left-aside {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  bottom: 0;
  background: #454545;
  width: 238px;
  overflow: hidden;
  overflow-y: scroll;
  z-index: 10;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.4s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */
 {
  opacity: 0;
}
/*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
.left-aside::-webkit-scrollbar {
  width: 10px;
  height: 16px;
  background-color: #f5f5f5;
}
/*定义滚动条的轨道，内阴影及圆角*/
.left-aside::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  background-color: #f5f5f5;
}
/*定义滑块，内阴影及圆角*/
.left-aside::-webkit-scrollbar-thumb {
  /*width: 10px;*/
  height: 20px;
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  background-color: rgba(0, 0, 0, 0.4);
}
.left-aside::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 0, 0, 0.6);
  cursor: pointer;
}
</style>
<style>
/*.el-tabs__item {*/
/*font-size: 18px;*/
/*}*/
.el-tabs__nav-wrap::after {
  height: 1px;
  background-color: #dddddd;
}
.el-popper[x-placement^="bottom"] {
  border: 1px solid #ddd;
}
.el-select-dropdown__item {
  color: #999;
}
.el-table td {
  text-align: center;
  padding-left: 0 !important;
}
.el-table th {
  height: 60px !important;
  background: #f3f3f3;
  text-align: center;
  padding-left: 0 !important;
}
</style>

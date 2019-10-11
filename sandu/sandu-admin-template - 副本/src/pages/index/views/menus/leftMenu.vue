<template>
  <li @click.prevent.stop="nodeClick(data)">
    <router-link
      :class="listenshowpage1==data.name?'active':'one'"
      :to="{path: data.path}"
      v-if="isPer(data.permission) && data.isView == true"
    >
      <img
        :src="data.active ? data.iconSel : data.icon"
        v-if="data.children && data.icon"
        width="12"
        style="margin-right: 10px"
        height="12"
        alt
      >
      <span :class="{'title':data.active}">{{data.title}}</span>
      <i v-if="data.icon" :class="{'arrow': data.icon,'arrowActive':data.active}"></i>
      <i v-else :class="{'arrow2': !data.icon}"></i>
    </router-link>
    <ul id="leftMenus" v-show="open">
      <!--:class="{'subMenusBg': item.isShow , 'parentMenus': !data.icon}"-->
      <m-tree
        v-for="(item,index) in data.children"
        :class="{'subMenusBg': !item.icon}"
        v-if="item.isShow"
        :key="index"
        :data="item"
      ></m-tree>
    </ul>
  </li>
</template>
<script>
import qs from "qs";
export default {
  name: "mTree",
  props: ["data"],
  data() {
    return {
      open: false
    };
  },
  methods: {
    isPer(per) {
      if (per) {
        let permiss = qs.parse(sessionStorage.getItem("loginUser"));
        // console.log(permiss.permissions, 'isper')
        return this.isMainPer(per, permiss);
      }
    },
    nodeClick(path) {
      console.log(path)
      if(path.name=='survey'){ //如果点击时概况直接跳转 特殊
        this.$router.push('/survey');
      }
      this.$store.dispatch("SetMenus", path.name);
      this.isPer(path.permission);
      sessionStorage.setItem("pathTitle", path.title);
      sessionStorage.setItem("getPath", path.path);
      this.open = !this.open;

      //父级高亮
      this.$store.state.platform.parentsName.map(res => {
        path.parentName == res.name
          ? (res.active = true)
          : (res.active = false);
        this.$set(
          this.$store.state.platform.parentsName,
          0,
          this.$store.state.platform.parentsName[0]
        );
      });
    }
  },
  computed: {
    classObject: function() {
      return {};
    },
    listenshowpage1() {
      return this.$store.state.menus.menus;
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../assets/css/style";
/***********************/
.left-icon {
  display: inline-block;
  width: 12px;
  height: 12px;
}
.subMenusBg {
  padding-left: -45px;
  text-indent: 25px;
  background: #393939;
}
.subMenusActive {
  background: rgba(255, 100, 25, 0.4);
  border-left: solid 3px #ff6419;
  color: #fff;
}
.parentMenus {
  padding-left: 0;
  text-indent: 0;
}

.click {
  background: rgba(255, 100, 25, 0.4) !important;
}
.title {
  color: #ff6419;
}
#leftMenus ul li {
  line-height: 60px;
  position: relative;
  a {
    display: block;
    font-size: 14px;
    height: 60px;
    line-height: 60px;
    color: #999;
    cursor: pointer;
    padding-left: 50px;
    &.one {
      // 一级菜单样式
      padding-left: 48px;
      position: relative;
      &:first-of-type {
        border-bottom: solid #404040 1px;
      }
      .arrow {
        content: "";
        position: absolute;
        margin-left: 15px;
        top: 26px;
        width: 0;
        height: 0;
        border-width: 6px;
        border-style: solid;
        border-color: #666 transparent transparent transparent;
        &.arrowActive {
          content: "";
          position: absolute;
          margin-left: 15px;
          top: 26px;
          width: 0;
          height: 0;
          border-width: 6px;
          border-style: solid;
          border-color: #ff6419 transparent transparent transparent;
          //border-color: transparent transparent #ff6419 transparent;
        }
      }
      .arrow2 {
        content: "";
        position: absolute;
        margin-left: 15px;
        top: 24px;
        width: 0;
        height: 0;
        border-width: 6px;
        border-style: solid;
        border-color: transparent transparent transparent #666;
        &.arrowActive2 {
          content: "";
          position: absolute;
          margin-left: 15px;
          top: 20px;
          width: 0;
          height: 0;
          border-width: 6px;
          border-style: solid;
          border-color: transparent transparent transparent #ff6419;
        }
      }
      &:hover {
        background: rgba(255, 100, 25, 0.4);
        border-left: solid 3px #ff6419;
        color: #fff;
        .arrow {
          border-color: #fff transparent transparent transparent;
        }
        .arrow2 {
          border-color: transparent transparent transparent #fff;
        }
      }
    }
    &.active {
      background: rgba(255, 100, 25, 0.4);
      border-left: solid 3px #ff6419;
      color: #fff;
      .arrow {
        border-color: #ff6419 transparent transparent transparent;
      }
      .arrow2 {
        content: "";
        position: absolute;
        margin-left: 15px;
        top: 24px;
        width: 0;
        height: 0;
        border-width: 6px;
        border-style: solid;
        //border-color: transparent transparent transparent #666;
        border-color: transparent transparent transparent #fff;
      }
    }
  }
}
</style>

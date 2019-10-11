<template>
    <div>
        <ul class="tabChange">
            <li @click="topage(item)" :class="{'active': item.active}" v-for="(item,index) in tab" :key="index">
                <span>{{item.name}}</span>
            </li>
        </ul>
        <router-view></router-view>
    </div>
</template>

<script>
import qs from "qs";
export default {
  data() {
    return {
      tab: [
        { name: "首页轮播", path: "/indexPageSet", active: true },
        { name: "品牌介绍", path: "/brandIntro", active: false },
        { name: "特卖商品", path: "/spesell", active: false },
        { name: "新品", path: "/presell", active: false }
      ]
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
    topage(item) {
      this.tab.map(it => {
        it.active = false;
      });
      item.active = true;
      this.$router.push(item.path);
    }
  },
  mounted() {
    this.$router.push("/indexPageSet");
  }
};
</script>

<style scoped lang="scss">
.tabChange {
  margin-bottom: 20px;
  background: #fff;
  height: 60px;
  display: block;
  line-height: 60px;
  border-bottom: solid 1px #ddd;
  li {
    display: inline-block;
    height: 18px;
      font-size: 18px;
    line-height: 18px;
    width: 114px;
    text-align: center;
    cursor: pointer;
    border-right: solid 1px #ddd;
    &:last-child {
      border-right: none;
    }
    &.active {
      color: #ff6419;
    }
  }
}
</style>
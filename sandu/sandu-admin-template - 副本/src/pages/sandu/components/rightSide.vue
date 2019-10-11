<template>
  <div class="rightSide" ref="warper">
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive&&resetRefresh"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive&&resetRefresh"></router-view>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "Vuex";
export default {
  name: "rightSide",
  provide() {
    return {
      reload: this.reload
    };
  },
  data() {
    return {
      scrollTop: 0,
      resetRefresh: true
    };
  },
  mounted() {},
  methods: {
    ...mapActions(["setRightSide"]),
    handelscroll() {
      this.$nextTick(() => {
        this.scrollTop = this.$refs.warper.scrollTop;
      });
    },
    backTop() {
      this.$refs.warper.scrollTop = 0;
    },
    reload() {
      this.resetRefresh = false;
      this.$nextTick(function() {
        this.resetRefresh = true;
      });
    }
  }
};
</script>

<style scoped>
.rightSide {
  flex: 1;
  height: 100%;
  overflow: auto;
}
</style>
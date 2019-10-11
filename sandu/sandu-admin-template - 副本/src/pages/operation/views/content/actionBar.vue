<template>
  <div class="actionBar" :style="isShowActionBar?'display:block':''">
    <el-button type="primary" @click="togglePopups">新增</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isShowActionBar: true
    };
  },
  methods: {
    togglePopups() {
      this.$store.commit("content_togglePopups");
      this.$store.commit("content_setIsAdd", true);
    }
  }
};
</script>


<style lang="scss" scoped>
.actionBar {
  display: none;
  margin: 20px 20px 0;
  padding: 20px 20px 0;
  background-color: #fff;
  text-align: left;
}
</style>

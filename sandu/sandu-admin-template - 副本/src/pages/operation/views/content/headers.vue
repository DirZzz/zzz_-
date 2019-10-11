<template>
  <div class="title">
    <div>{{title || $route.name}}</div>
    <slot></slot>
  </div>
</template>

<script>
export default {
  props:["title"]
};
</script>

<style lang="scss" scoped>
.title {
  height: 55px;
  border-bottom: 1px solid #eaeaea;
  padding: 0 30px;
  line-height: 55px;
  text-align: left;
  margin: 20px;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
}
.t_row {
  margin: 20px 20px 0;
  padding: 20px 30px;
  background-color: #fff;
  text-align: left;
}
.t_join {
  margin-top: -20px;
}
</style>

<template>
  <div>
    <headers></headers>
    <actionBar></actionBar>
    <contents></contents>
    <popups v-if="$store.state.content.isShowPopups"></popups>
  </div>
</template>

<script>
import headers from "./headers";
import actionBar from "./actionBar";
import contents from "./contents";
import popups from "./popups";
export default {
  components: {
    headers,
    actionBar,
    contents,
    popups
  },
  created() {
    this.$store.commit(
      "content_setType",
      this.$route.path.substring(
        this.$route.path.lastIndexOf("/") + 1,
        this.$route.path.length
      )
    );
  },
  watch: {
    $route: function(_new, _old) {
      this.$store.commit(
        "content_setType",
        _new.path.substring(_new.path.lastIndexOf("/") + 1, _new.path.length)
      );
    }
  }
};
</script>

<style lang="scss" scoped>
</style>


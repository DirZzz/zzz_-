<template>
    <div class="page">
        <div class="page-content">
            <slot></slot>
        </div>
    </div>
</template>

<script>
    export default {
        name: "page"
    }
</script>

<style scoped lang="scss">
  .page{
      width: 100%;
      height: 100%;
      padding: 20px;
      .page-content{
          width: 100%;
          height: 100%;
          background-color: #fff;
          padding: 20px;
          overflow-y: scroll;
          overflow-x: hidden;
          text-align: left;
      }
  }
</style>
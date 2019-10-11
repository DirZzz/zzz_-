<template>
  <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page="query.page"
    :page-sizes="[5, 10, 20]"
    :page-size="query.limit"
    layout="total, sizes, prev, pager, next, jumper"
    :total="planProductList.total">
  </el-pagination>
</template>

<script>
  export default {
    name: 'Pager',
    props: {
      datas: Array,
      datasFun: Function
    },
    data () {
      return {
        query: {
          page: 0,
          limit: 10
        }
      }
    },
    methods: {
      handleSizeChange (size) {
        this.query.limit = size
        this.datasFun()
      },
      handleCurrentChange (cur) {
        this.query.page = cur
        this.datasFun()
      }
    }
  }
</script>

<style scoped lang="scss">
</style>

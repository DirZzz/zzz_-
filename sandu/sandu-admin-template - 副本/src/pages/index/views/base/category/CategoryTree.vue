<template>
  <span>
    <el-cascader placeholder="请选择产品类别" @change="handleChange" clearable :value="val" :options="categoryTree"
                 :props="{label: 'name', value: 'keyCode', children: 'children'}" filterable></el-cascader>
  </span>
</template>
<script>
  export default {
    name: 'CategoryTree',
    model: {
      prop: 'value',
      event: 'change'
    },
    props: ['value'],
    data () {
      return {
        val: this.value,
        categoryTree: []
      }
    },
    methods: {
      handleChange (val) {
        this.val = val
        this.$emit('change', val)
      }
    },
    mounted () {
      this.API.ProductCategory().then(resp => {
        this.categoryTree = resp.data
      })
    },
    watch: {
      value (val) {
        this.val = val
      }
    }
  }
</script>

<template>
  <el-select @change="handleChange" clearable :value="val" placeholder="包含未公开产品">
    <el-option
      v-for="item in containsNotOpenProduct"
      :key="item.code"
      :label="item.label"
      :value="item.code">
    </el-option>
  </el-select>
</template>

<script>
  export default {
    name: 'ContainsNotOpenProduct',
    model: {
      prop: 'value',
      event: 'change'
    },
    props: ['value'],
    data () {
      return {
        val: this.value,
        containsNotOpenProduct: [
          {
            code: '0',
            label: '是'
          },
          {
            code: '1',
            label: '否'
          }
        ]
      }
    },
    methods: {
      handleChange (val) {
        this.val = val
        this.$emit('change', val)
      }
    },
    watch: {
      value (val) {
        this.val = val
      }
    }
  }
</script>

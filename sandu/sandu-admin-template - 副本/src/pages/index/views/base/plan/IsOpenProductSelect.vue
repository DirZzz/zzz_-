<template>
  <el-select @change="handleChange" :value="val" clearable placeholder="请选择是否未公开产品">
    <el-option
      v-for="item in isOpens"
      :key="item.code"
      :label="item.label"
      :value="item.code">
    </el-option>
  </el-select>
</template>

<script>
  export default {
    name: 'IsOpenProductSelect',
    model: {
      prop: 'value',
      event: 'change'
    },
    props: ['value'],
    data () {
      return {
        val: this.value,
        isOpens: [
          {
            code: 0,
            label: '公开'
          },
          {
            code: 1,
            label: '不公开'
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

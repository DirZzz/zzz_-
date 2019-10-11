<template>
  <el-select @change="handleChange" clearable :value="val" placeholder="请选择是否显示">
    <el-option
      v-for="item in isShows"
      :key="item.code"
      :label="item.label"
      :value="item.code">
    </el-option>
  </el-select>
</template>

<script>
  export default {
    name: 'IsShowSelect',
    model: {
      prop: 'value',
      event: 'change'
    },
    props: ['value'],
    data () {
      return {
        val: this.value,
        isShows: [
          {
            code: 0,
            label: '显示'
          },
          {
            code: 1,
            label: '不显示'
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

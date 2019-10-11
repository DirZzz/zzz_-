<!--
 * @Descripttion: 搜索栏（筛选栏）
 * @version: 0.1
 * @Author: yangtian
 * @Date: 2019-05-30 16:38:27
 * @LastEditors: sueRimn
 * @LastEditTime: 2019-05-31 17:51:59
 -->
<template>
  <div v-if="searchs">
    <el-form v-model="searchs" :inline="true" class="searchBar" ref="refForm">
      <el-form-item :key="i" v-for="(item,i) in searchs" :label="item.label">
        <!-- 输入框 -->
        <el-input
          v-if="['text','number'].indexOf(item.type)!=-1"
          :type="item.type"
          v-model="item.data"
          :placeholder="item.placeholder"
          :style="item.style"
          :size="item.size"
        ></el-input>
        <!-- 选择器 -->
        <el-select
          v-if="item.type=='select'"
          v-model="item.data"
          :placeholder="item.placeholder"
          :style="item.style"
          clearable
          :size="item.size"
        >
          <el-option :key="i" v-for="(o,i) in item.options" :label="o.label" :value="o.value"></el-option>
        </el-select>
        <!-- 日期时间选择 -->
        <el-date-picker
          v-if="item.type=='datetime'"
          v-model="item.data"
          type="datetime"
          :placeholder="item.placeholder"
          :style="item.style"
          :value-format="item.format || 'yyyy-MM-dd'"
          :size="item.size"
        ></el-date-picker>
        <!-- 日期时间选择区间 -->
        <el-date-picker
          v-if="item.type=='datetimerange'"
          v-model="item.data"
          type="datetimerange"
          :default-time="['00:00:00']"
          :value-format="item.format || 'yyyy-MM-dd:HH-mm-ss'"
          :size="item.size"
        ></el-date-picker>
      </el-form-item>
      <el-form-item v-if="inline">
        <el-button type="primary" @click="search" size="small" :round="round">筛选</el-button>
        <el-button type="info" plain @click="reset" size="small" :round="round">重置</el-button>
      </el-form-item>
      <div class="btns" v-else>
        <el-button type="primary" @click="search" size="small" :round="round">筛选</el-button>
        <el-button type="info" plain @click="reset" size="small" :round="round">重置</el-button>
      </div>
    </el-form>
  </div>
</template>
    
<script>
export default {
  props: {
    inline: Boolean, //按钮栏设置-是否行内
    /**
     * 搜索栏设置
     * @fields 字段
     * @type 筛选框类型
     * @label 筛选框标题
     * @placeholder 筛选框提示
     * @style 筛选框样式
     * @options 选择器-子选项
     * @format 日期时间选择器-时间格式
     * @size 筛选框大小
     */
    searchs: Array,
    //搜索方法的传参
    pageParams: {
      type: Object,
      default: {
        page: 1,
        limit: 10
      }
    },
    onSearch: Function, //搜索方法
    round: Boolean
  },
  methods: {
    search() {
      if (this.pageParams) {
        this.$set(this.pageParams, "page", 1);
        this.searchs.forEach(item => {
          this.pageParams[item.fields] = item.data;
        });
      }
      this.onSearch();
    },
    reset() {
      this.$set(
        this.searchs,
        this.searchs.map((e, index) => {
          e.data = undefined;
          return e;
        })
      );
      this.search();
    }
  }
};
</script>

<style lang="scss" scoped>
.searchBar {
  text-align: left;
}
.btns {
  text-align: center;
  margin-top: 10px;
}
</style>
<style lang="scss">
input[type="number"] {
  -moz-appearance: textfield;
}
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>


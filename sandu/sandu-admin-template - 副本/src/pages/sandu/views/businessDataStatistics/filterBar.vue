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
          v-model="item.value"
          :placeholder="item.placeholder"
          :style="item.style"
          :size="item.size"
          :clearable="item.clearable==undefined?true:item.clearable"
          @change="search"
        ></el-input>
        <!-- 选择器 -->
        <el-select
          v-if="item.type=='select'"
          v-model="item.value"
          :placeholder="item.placeholder"
          :style="item.style"
          :clearable="item.clearable==undefined?true:item.clearable"
          :size="item.size"
          @change="search"
          :filterable="item.filterable"
        >
          <el-option :key="i" v-for="(e,i) in item.options" :label="e.label" :value="e.value"></el-option>
        </el-select>
        <!-- 日期范围选择 按钮形式 -->
        <el-radio-group
          v-if="item.type=='daterangebtn'"
          v-model="item.value"
          :size="item.size"
          @change="search"
        >
          <el-radio-button v-for="e in item.options" :label="e.value" :key="e.value">{{e.label}}</el-radio-button>
        </el-radio-group>
        <!-- 日期时间选择 -->
        <el-date-picker
          v-if="item.type=='datetime'"
          v-model="item.value"
          type="datetime"
          :placeholder="item.placeholder"
          :style="item.style"
          :value-format="item.format || 'yyyy-MM-dd'"
          :size="item.size"
          @change="search"
          :clearable="item.clearable==undefined?true:item.clearable"
        ></el-date-picker>
        <!-- 日期时间选择区间 -->
        <el-date-picker
          v-if="item.type=='datetimerange'"
          v-model="item.value"
          type="datetimerange"
          :default-time="['00:00:00']"
          :value-format="item.format || 'yyyy-MM-dd:HH-mm-ss'"
          :size="item.size"
          @change="search"
          :clearable="item.clearable==undefined?true:item.clearable"
        ></el-date-picker>
      </el-form-item>
      <template v-if="isHasBtn">
        <el-form-item v-if="inline">
          <el-button type="primary" @click="search" size="small" :round="btnRound">筛选</el-button>
          <el-button type="info" plain @click="reset" size="small" :round="btnRound">重置</el-button>
        </el-form-item>
        <div class="btns" v-else>
          <el-button type="primary" @click="search" size="small" :round="btnRound">筛选</el-button>
          <el-button type="info" plain @click="reset" size="small" :round="btnRound">重置</el-button>
        </div>
      </template>
    </el-form>
  </div>
</template>
    
<script>
export default {
  props: {
    /**
     * 搜索栏设置
     * @fields 字段
     * @value 真实值
     * @type 筛选框类型
     * @label 筛选框标题
     * @placeholder 筛选框提示
     * @style 筛选框样式
     * @clearable 是否可清空，不传默认为可以
     * @options 选择器-子选项
     * @options.label 选择器-子选项-显示文本
     * @options.value 选择器-子选项-选中值
     * @format 日期时间选择器-时间格式
     * @size 筛选框大小
     */
    searchs: Array,
    //搜索方法的传参
    pageParams: Object,
    onSearch: Function, //搜索方法
    btnRound: Boolean,
    isHasBtn: Boolean, // 是否有按钮栏
    inline: Boolean //按钮栏设置-是否行内
  },
  methods: {
    search() {
      if (this.pageParams) {
        this.searchs.forEach(item => {
          this.pageParams[item.fields] = item.value;
        });
      }
      this.onSearch();
    },
    reset() {
      this.$set(
        this.searchs,
        this.searchs.map((e, index) => {
          e.value = undefined;
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


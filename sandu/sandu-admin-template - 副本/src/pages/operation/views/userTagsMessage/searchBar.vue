<template>
  <div v-if="searchs">
    <el-form :inline="true" class="searchBar" ref="refForm">
      <el-form-item :key="index" v-for="(item,index) in searchs">
        <!-- 文本框 -->
        <el-input
          v-if="item.type=='text'"
          v-model="item.data"
          :placeholder="item.placeholder"
          :style="item.style || 'width:180px'"
          :type="item.type"
        ></el-input>
        <el-input
          v-if="item.type=='number'"
          v-model="item.data"
          :placeholder="item.placeholder"
          :style="item.style || 'width:180px'"
          type="text"
          oninput="this.value=this.value.replace(/[^0-9]/g,'');"
        ></el-input>
        <!-- 选择框 -->
        <el-select
          v-if="item.type=='select'"
          v-model="item.data"
          :placeholder="item.placeholder"
          :style="item.style || 'width:180px'"
          clearable
        >
          <el-option
            :key="option.label"
            v-for="option in item.options"
            :label="option.label"
            :value="option.value"
          ></el-option>
        </el-select>
        <!-- 日期选择 -->
        <el-date-picker
          v-if="item.type=='date'"
          v-model="item.data"
          type="date"
          :placeholder="item.placeholder"
          :style="item.style || 'width:180px'"
          :value-format="item.format || 'yyyy-MM-dd'"
        ></el-date-picker>
        <template v-if="item.type=='dateAfter'">
          <span>—</span>
          <el-date-picker
            v-model="item.data"
            type="date"
            :placeholder="item.placeholder"
            :style="item.style || 'width:180px'"
            :value-format="item.format || 'yyyy-MM-dd'"
          ></el-date-picker>
        </template>
        <!-- 日期时间选择 -->
        <el-date-picker
          v-if="item.type=='datetime'"
          v-model="item.data"
          type="datetime"
          :placeholder="item.placeholder"
          :style="item.style || 'width:180px'"
          :value-format="item.format || 'yyyy-MM-dd'"
        ></el-date-picker>
        <template v-if="item.type=='datetimeAfter'">
          <span>—</span>
          <el-date-picker
            v-model="item.data"
            type="datetime"
            :placeholder="item.placeholder"
            :style="item.style || 'width:180px'"
            :value-format="item.format || 'yyyy-MM-dd'"
          ></el-date-picker>
        </template>
        <!-- 日期时间选择区间 -->
        <el-date-picker
          v-if="item.type=='datetimerange'"
          v-model="item.data"
          type="datetimerange"
          :start-placeholder="item.startPlaceholder || '开始日期'"
          :end-placeholder="item.endPlaceholder || '结束日期'"
          :default-time="['00:00:00']"
          :value-format="item.format || 'yyyy-MM-dd:HH-mm-ss'"
          @change="getDate(item.data)"
        ></el-date-picker>
      </el-form-item>
      <div class="btns">
        <el-button type="primary" @click="search" size="mini">查询</el-button>
        <el-button @click="reset" size="mini">重置</el-button>
      </div>
    </el-form>
  </div>
</template>
    
<script>
export default {
  props: ["searchs", "onSearch", "pageData"],
  methods: {
    getDate(value) {
      this.$emit('getDate', value)
    },
    search() {
      if (this.pageData) {
        this.$set(this.pageData, "page", 1);
        this.searchs.forEach(item => {
          if (item.fields instanceof Array) {
            if (item.data)
              item.data.forEach((e, i) => {
                this.pageData[item.fields[i]] = e;
              });
            else
              item.fields.forEach((e, i) => {
                this.pageData[item.fields[i]] = undefined;
              });
          } else this.pageData[item.fields] = item.data;
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
  margin: 20px;
  padding: 20px 25px;
  background-color: #fff;
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


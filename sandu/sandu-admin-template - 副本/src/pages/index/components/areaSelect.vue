<template>
  <div class="select-area">
    <el-select clearable  @clear="clearProvinces" v-model="provincesVal" placeholder="请选择省" @change="provincesChange">
      <el-option
        v-for="item in provinces"
        :key="item.areaCode"
        :label="item.areaName"
        :value="item.areaCode"
      ></el-option>
    </el-select>
    <el-select clearable  @clear="clearCitys" v-model="citysVal" placeholder="请选择市" @change="citysChange">
      <el-option
        v-for="item in citys"
        :key="item.areaCode"
        :label="item.areaName"
        :value="item.areaCode"
      ></el-option>
    </el-select>
    <el-select clearable v-model="areasVal" placeholder="请选择区" @change="areasChange">
      <el-option
        v-for="item in areas"
        :key="item.areaCode"
        :label="item.areaName"
        :value="item.areaCode"
      ></el-option>
    </el-select>
  </div>
</template>

<script>
export default {
  name: "areaSelect",
  props: {
    index: null
  },
  data() {
    return {
      clearFlag:true,
      provinces: [],
      citys: [],
      areas: [],
      provincesVal: "",
      citysVal: "",
      areasVal: ""
    };
  },
  created() {
    this.getAreaList("0", 0);
  },
  methods: {
    getAreaList(areaCode, type) {
      this.API2.getAreaList({ areaCode: areaCode }).then(res => {
        switch (type) {
          case 0:
            if (res.success) this.provinces = res.datalist;
            break;
          case 1:
            if (res.success) this.citys = res.datalist;
            break;
          case 2:
            if (res.success) this.areas = res.datalist;
            break;
        }
      });
    },
    clearCode(){
      this.citys = [];
      this.areas = [];
    },
    provincesChange(val) {
      val ? this.getAreaList(val, 1) : "";
      if (this.index!=undefined) {
        let data = {
          val: val,
          index: this.index - 1
        };
        this.$emit("provinceCode", data);
      } else {
        this.$emit("provinceCode", val);
      }

      this.citysVal = "";
      this.areasVal = "";
    },
    citysChange(val) {
      val ? this.getAreaList(val, 2) : "";
      if (this.index) {
        let data = {
          val: val,
          index: this.index - 1
        };
        this.$emit("cityCode", data);
      } else {
        this.$emit("cityCode", val);
      }

      this.areasVal = "";
    },
    areasChange(val) {
      val ? this.getAreaList(val, 3) : "";
      if (this.index) {
        let data = {
          val: val,
          index: this.index - 1
        };
        this.$emit("areaCode", data);
      } else {
        this.$emit("areaCode", val);
      }
    },
    clearProvinces(){
      this.citys = [];
      this.areas = [];
    },
    clearCitys(){
      this.areas = [];
    },
    echoData(provincesVal, citysVal, areasVal) {
      //回显方法
      this.getAreaList(provincesVal, 1);
      this.getAreaList(citysVal, 2);
      this.getAreaList(areasVal, 3);
      this.provincesVal = provincesVal;
      this.citysVal = citysVal;
      this.areasVal = areasVal;
    },
    reset() {
      this.provincesVal = "";
      this.citysVal = "";
      this.areasVal = "";
      this.citys = [];
      this.areas = [];
    }
  }
};
</script>

<style scoped lang="scss">
.select-area {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
}
.el-select {
  margin-right: 25px;
  width: 210px;
}
.add-store-wrap {
  .select-box {
    .el-select {
      width: 105px;
    }
  }
}
.add-activity {
  .select-area {
    display: inline-block;
    width: auto;
    text-align: left;
  }
}
</style>
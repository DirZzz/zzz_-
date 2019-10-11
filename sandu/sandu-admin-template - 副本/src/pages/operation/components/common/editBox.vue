<template>
  <div>
    <div v-if="!isEdit">
      <span>{{ data }}</span>
      <img
        class="bianji_ico"
        src="../../assets/images/icon/icon_edit.png"
        @click="isEdit=true;editData=data"
      >
    </div>
    <div v-else style="display:flex;align-items:center">
      <input
        :type="inputType"
        v-model="editData"
        class="bianji_input"
        oninput="this.value=this.value.replace(this.regEx,'')"
        :maxlength="maxlength"
      >
      <el-button-group>
        <el-button
          class="bianji_button"
          style="border-radius:0px;border-left:none"
          @click="editFn({[editField]:editData,...moreFnParams});isEdit=false"
        >
          <img src="../../assets/images/icon/icon_ok.png">
        </el-button>
        <el-button class="bianji_button" @click="isEdit=false">
          <img src="../../assets/images/icon/icon_close.png">
        </el-button>
      </el-button-group>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // 展示内容
    data: [String, Number],
    regEx: RegExp,
    // 编辑方法
    editFn: Function,
    // 编辑字段
    editField: String,
    // 编辑方法额外需要的参数
    moreFnParams: Object,
    // 编辑框字符长度限制
    maxlength: Number,
    //输入框类型
    inputType:String
  },
  data() {
    return {
      // 是否编辑
      isEdit: false,
      editData: undefined
    };
  }
};
</script>

<style lang="scss" scoped>
.bianji_ico {
  width: 16px;
  height: 16px;
  vertical-align: middle;
  cursor: pointer;
}
.bianji_input {
  width: 50px;
  height: 32px;
  background-color: #f5f5f5;
  border-radius: 4px 0px 0px 4px;
  border: solid 1px #d9d9d9;
  padding: 0 6px;
}
.bianji_button {
  width: 32px;
  height: 32px;
  background-color: #fff;

  border: solid 1px #d9d9d9;
  padding: 0 6px;
}
</style>

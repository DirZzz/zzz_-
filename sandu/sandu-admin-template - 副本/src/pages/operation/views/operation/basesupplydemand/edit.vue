<template>
  <div>
    <!--<v-nav :nav="nav"></v-nav>-->
    <transition name="fade">
      <div class="addProduct">
        <div class="headTitle">编辑</div>
        <p class="ptop">
          <span>*</span>为必填项
        </p>

        <el-row :gutter="24" class="splineFrame">
          <el-col :span="2" class="productBaseInfo">基本信息</el-col>
          <el-col :span="22" class="rightLine">
            <hr>
          </el-col>
        </el-row>

        <div class="productBaseItem">
          <el-form
            :model="ruleForm"
            :rules="rules"
            ref="ruleForm"
            label-width="120px"
            class="demo-ruleForm"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="发布者：" prop="creator">
                  <el-input
                    class="formWidth"
                    clearable
                    v-model.trim="ruleForm.creator"
                    placeholder="请输入内容    "
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="户型：" class="planOption">
                  <el-tag
                    v-for="tag in houseList"
                    :key="tag.label"
                    @close="houseClose(tag)"
                    closable
                  >{{tag.label}}</el-tag>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="类别：" prop="supplyDemandCategoryId">
                  <el-cascader :options="categoryAlllist" v-model.trim="ruleForm.categoryId"></el-cascader>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="图片：">
                  <div class="showImg">
                    <div class="imgBox" v-for="(item,index) in ruleForm.coverPicPaths" :key="index">
                      <div class="active">
                        <div class="btns" @click="lookBigpic(item)">查看大图</div>
                      </div>
                      <img :src="BASE_URL.sourceBaseUrl+item" class="avatar">
                      <i class="ic_del" @click="deleteImg(index)"></i>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="区域：" class="areaMarginRight">
                  <!---------------------------------------------------->
                  <el-col :span="3">
                    <el-select
                      v-model="citySelect.provide"
                      @change="getProvideCode(citySelect.provide)"
                      clearable
                      placeholder="请选择省"
                    >
                      <el-option
                        v-for="(item, index) in provide"
                        :key="index"
                        :label="item.areaName"
                        :value="item.areaCode"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="3">
                    <el-select
                      clearable
                      v-model="citySelect.city"
                      @change="getCityCode(citySelect.city)"
                      placeholder="请选择市"
                    >
                      <el-option
                        v-for="(item,index) in citySelects"
                        :key="index"
                        :label="item.areaName"
                        :value="item.areaCode"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="3">
                    <el-select
                      clearable
                      v-model="citySelect.area"
                      @change="getAreaCode(citySelect.area)"
                      placeholder="请选择区域"
                    >
                      <el-option
                        v-for="(item,index) in areaSelect"
                        :key="index"
                        :label="item.areaName"
                        :value="item.areaCode"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="3" v-if="streetShow">
                    <el-select
                      clearable
                      v-model="citySelect.origin"
                      @change="getOriginCode(citySelect.origin)"
                      placeholder="请选择街道"
                    >
                      <el-option
                        v-for="(item,index) in originSelect"
                        :key="index"
                        :label="item.areaName"
                        :value="item.areaCode"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="3" v-else>
                    <span class="streetSel">不限</span>
                  </el-col>
                  <!---------------------------------------------------->
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="地址：" prop="address">
                  <el-input
                    class="formWidth"
                    clearable
                    v-model.trim="ruleForm.address"
                    placeholder="请输入内容"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="方案：" class="planOption">
                  <el-tag
                    v-for="tag in planList"
                    :key="tag.label"
                    @close="handleClose(tag)"
                    closable
                  >{{tag.label}}</el-tag>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="信息标题：" prop="title">
                  <el-input
                    class="formWidth"
                    clearable
                    v-model.trim="ruleForm.title"
                    placeholder="请输入内容"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="浏览量（真）：">
                  <el-input
                    class="formWidth"
                    type="number"
                    :disabled="true"
                    clearable
                    v-model.trim="ruleForm.viewCount"
                    placeholder="请输入内容"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="信息描述：" prop="description">
                  <el-input
                    class="formWidth"
                    clearable
                    v-model.trim="ruleForm.description"
                    placeholder="请输入内容"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="点赞数（真）：">
                  <el-input
                    class="formWidth"
                    type="number"
                    :disabled="true"
                    clearable
                    v-model.trim="ruleForm.likeCount"
                    placeholder="请输入内容"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="浏览量（假）：" prop="virtualViewCount">
                  <el-input
                    class="formWidth"
                    type="number"
                    @mousewheel.native.prevent
                    clearable
                    v-model.trim="ruleForm.virtualViewCount"
                    oninput="if(value.length>7) value=value.slice(0,7)"
                    placeholder="请输入内容"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="点赞数（假）：" prop="virtualLikeCount">
                  <el-input
                    class="formWidth"
                    type="number"
                    @mousewheel.native.prevent
                    clearable
                    v-model.trim="ruleForm.virtualLikeCount"
                    oninput="if(value.length>7) value=value.slice(0,7)"
                    placeholder="请输入内容"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <div class="splitLine"></div>
                <el-form-item>
                  <el-button type="primary" size="small" round @click="submitForm('ruleForm')">确定</el-button>
                  <el-button round size="small" @click="resetForm('ruleForm')">取消</el-button>
                </el-form-item>
              </el-col>
              <el-col :span="12" v-if="ruleForm.newDataFlag==0">
                <div>
                  <span style="font-size:14px;padding-right:12px;line-height:40px">是否添加至互动区的问答</span>
                  <el-switch v-model="ruleForm.moveDataFlag" :active-value="1" :inactive-value="0"></el-switch>
                </div>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
    </transition>
    <showBigImg
      :flag="showBigImgflag"
      :obj="bigImgList"
      :indexActive="0"
      @closeImg="closeImg"
      :arrow="'never'"
    ></showBigImg>
  </div>
</template>

<script type="es6">
import showBigImg from "../../feedback/conponents/showBigImg";
export default {
  components: {
    showBigImg
  },
  data() {
    return {
      options: [],
      streetShow: true,
      citySelect: {
        provide: "",
        city: "",
        area: "",
        origin: ""
      },
      provide: [],
      provideSelect: [],
      citySelects: [],
      areaSelect: [],
      originSelect: [],
      categoryAlllist: [],
      nav: [{ nav: "产品库", url: "/template" }, { nav: "编辑产品", url: "" }],
      typeList: [{ value: 1, name: "供应" }, { value: 2, name: "需求" }],
      ruleForm: {},
      brandSelect: [
        { id: 1, name: "大自然" },
        { id: 2, name: "圣象地板" },
        { id: 3, name: "简一" }
      ],
      // 验证表单规则
      rules: {
        creator: [
          { required: true, message: "请输入发布者名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 50 个字符", trigger: "blur" }
        ],
        virtualViewCount: [
          { validator: this.validateAcquaintance, trigger: "blur" }
        ],
        virtualLikeCount: [
          { validator: this.validateAcquaintance, trigger: "blur" }
        ],
        supplyDemandCategoryId: [
          { required: true, message: "请选择类别", trigger: "blur" }
        ],
        address: [{ required: true, message: "请输入地址", trigger: "blur" }],
        title: [
          { required: true, message: "请输入标题", trigger: "blur" },
          { min: 1, max: 15, message: "长度在 1 到 15 个字符", trigger: "blur" }
        ]
      },
      imageUrl: [],
      showBigImgflag: false,
      falseFlag: true,
      bigImgList: [],
      currentHouse: [],
      houseList: [],
      currentPlan: [],
      planList: []
    };
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.nav[0].url = from.path;
    });
  },
  created() {
    this.getArea(0, "省");
    this.categoryDroplist();
    this.getInfo();
  },
  methods: {
    handleClose(tag) {
      let result = [];
      this.planList = this.planList.filter(res => {
        res.value != tag.value;
      });
      this.planList &&
        this.planList.map(res => {
          result.push(res.value);
        });
      result.length == 0 ? result.push(0) : "";
      this.currentPlan = result;
    },
    houseClose(tag) {
      let result = [];
      this.houseList = this.houseList.filter(res => {
        res.value != tag.value;
      });
      this.houseList &&
        this.houseList.map(res => {
          result.push(res.value);
        });
      result.length == 0 ? result.push(0) : "";
      this.currentHouse = result;
    },
    lookBigpic(item) {
      this.bigImgList = [];
      this.showBigImgflag = true;
      this.bigImgList.push(item);
    },
    closeImg() {
      this.showBigImgflag = false;
    },
    validateAcquaintance(rule, value, callback) {
      if (value < 0) {
        callback(new Error("请输入正整数"));
      } else {
        callback();
      }
    },
    getInfo() {
      this.API.getBasesupplydemandById({
        basesupplydemandId: this.$route.params.basesupplydemandId
      }).then(res => {
        this.ruleForm = res.data;
        if (res) {
          if (res.data.province) {
            this.citySelect.provide = res.data.province;
            this.getArea(res.data.province, "市");
          }
          if (res.data.city) {
            this.citySelect.city = res.data.city;
            this.getArea(res.data.city, "区");
          }
          if (res.data.district) {
            this.citySelect.area = res.data.district;
            this.getArea(res.data.district, "街道");
          }
          if (res.data.district) {
            this.citySelect.origin = res.data.street;
          }
          if (res.data.houseInfo) {
            for (let key in res.data.houseInfo) {
              this.currentHouse.push(key);
              this.houseList.push({
                label: res.data.houseInfo[key],
                value: key
              });
            }
          }
          if (res.data.planInfo) {
            for (let key in res.data.planInfo) {
              this.currentPlan.push(key);
              this.planList.push({
                label: res.data.planInfo[key],
                value: key
              });
            }
          }
        }
      });
    },
    // 区域选择-------------
    getArea(code, type) {
      console.log(45);
      this.API.areaList({ areaCode: code }).then(res => {
        console.log(res);
        if (res) {
          if (type == "省") {
            this.provide = res.datalist;
          }
          if (type == "市") {
            this.citySelects = res.datalist;
          }
          if (type == "区") {
            this.areaSelect = res.datalist;
          }
          if (type == "街道") {
            this.originSelect = res.datalist;

            if (!this.originSelect || this.originSelect.length == 0) {
              this.streetShow = false;
            } else {
              this.streetShow = true;
            }
          }
        }
      });
    },
    deleteImg(index) {
      let arr = this.ruleForm.coverPicId.split(",");
      arr.splice(index, 1);
      this.ruleForm.coverPicPaths.splice(index, 1);
      this.ruleForm.coverPicId = arr.join(",");
    },
    getProvideCode(code) {
      this.citySelect.city = "";
      this.citySelect.area = "";
      this.citySelect.origin = "";
      this.citySelects = [];
      this.areaSelect = [];
      this.originSelect = [];
      this.getArea(code, "市");
    },
    getCityCode(code) {
      this.citySelect.area = "";
      this.citySelect.origin = "";
      this.areaSelect = [];
      this.originSelect = [];
      this.getArea(code, "区");
    },
    getAreaCode(code) {
      this.citySelect.origin = "";
      this.originSelect = [];
      this.getArea(code, "街道");
    },
    getOriginCode(code) {},
    // 区域选择 结束---------- streetCodeName
    categoryDroplist() {
      this.API.queryAllCategoryList().then(res => {
        this.categoryAlllist = res.data;
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let formDatas = this.ruleForm;
          for (let key in formDatas) {
            if (
              formDatas[key] == "" ||
              formDatas[key] == undefined ||
              formDatas[key] == null
            ) {
              delete formDatas[key];
            }
          }
          let jsonData = JSON.stringify(formDatas);
          this.$confirm("确定修改内容?", "提示", {
            confirmButtonText: "确定",
            type: "info",
            cancelButtonClass: "cancelButtonClass",
            confirmButtonClass: "confirmButtonClass",
            center: true
          }).then(() => {
            //this.$message.success('修改成功');
            let query = {
              province: this.citySelect.provide,
              city: this.citySelect.city,
              district: this.citySelect.area,
              street: this.citySelect.origin,
              type: formDatas.type,
              address: formDatas.address,
              categoryId: formDatas.categoryId,
              id: formDatas.id,
              creator: formDatas.creator,
              description: formDatas.description,
              title: formDatas.title,
              viewCount: formDatas.viewCount,
              virtualViewCount: formDatas.virtualViewCount,
              virtualLikeCount: formDatas.virtualLikeCount,
              likeCount: formDatas.likeCount,
              planName: formDatas.planName,
              houseName: formDatas.houseName,
              houseIds: this.currentHouse,
              planIds: this.currentPlan,
              coverPicId: formDatas.coverPicId ? formDatas.coverPicId : 0,
              moveDataFlag: formDatas.moveDataFlag
            };
            //提交数据
            this.API.updateBasesupplydemand(query).then(() => {
              this.$message({ message: "成功更新内容", type: "success" });
              this.$router.push("/basesupplydemand/list");
            });
          });
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();

      // this.$message({ message: "您取消了此次编辑", type: "warning" });
      this.$router.push("/basesupplydemand/list");
    }
  }
};
</script>

<style lang="scss" scoped>
/*@import "../../assets/css/mixin";*/
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
}
input[type="number"] {
  -moz-appearance: textfield;
}
.areaMarginRight {
  .el-col {
    margin-right: 15px;
  }
}
.productBaseItem {
  .el-input,
  .el-cascader {
    width: 250px;
  }
}
.headTitle {
  border-bottom: solid 1px #ddd;
  font-size: 20px;
  font-weight: normal;
  text-align: left;
  color: #666666;
  padding-bottom: 20px;
}
.flex-wrap {
  display: flex;
}
.showImg {
  display: flex;
  flex-wrap: wrap;
  img {
    width: 120px;
    height: 120px;
    border-radius: 6px;
  }
  .ic_del {
    display: block;
    position: absolute;
    width: 16px;
    height: 16px;
    background: no-repeat center
      url("../../../assets/images/icon/ic_delete.png");
    background-size: 100%;
    right: 5px;
    top: 5px;
    cursor: pointer;
    z-index: 11;
  }
  .imgBox {
    position: relative;
    width: 120px;
    height: 120px;
    margin-right: 10px;
    margin-bottom: 10px;
    &:hover .active {
      opacity: 1;
    }
  }
  .active {
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    background: rgba(0, 0, 0, 0.7);
    opacity: 0;
    transition-duration: 0.3s;
    z-index: 10;
    .btns {
      width: 72px;
      height: 24px;
      text-align: center;
      line-height: 24px;
      cursor: pointer;
      color: #fff;
      font-size: 12px;
      position: absolute;
      left: 50%;
      top: 50%;
      margin-left: -36px;
      margin-top: -12px;
      background-color: #3399ff;
      border-radius: 12px;
    }
  }
}
.addProduct {
  background: #fff;
  box-sizing: padding-box;
  padding: 20px;
  text-align: left;
  margin: 20px;
  border-radius: 5px;
  .ptop {
    height: 30px;
    line-height: 30px;
    padding-top: 10px;
    font-size: 14px;
    color: #666;
    span {
      height: 20px;
      line-height: 20px;
      display: inline-block;
      padding-right: 5px;
      color: red;
      font-weight: bold;
      font-size: 20px;
      vertical-align: -6px;
    }
  }
}
</style>


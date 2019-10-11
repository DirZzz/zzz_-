<template>
  <div class="add-activity">
    <div class="main-body">
      <div class="breadcrumb-box">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/offlineActivity' }">线下活动</el-breadcrumb-item>
          <el-breadcrumb-item>{{typeText}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="form-box">
        <el-form
          ref="ruleForm"
          :rules="rules"
          label-position="right"
          :model="activity"
          label-width="200px"
        >
          <el-form-item label="选择创建对象：" prop="businessType">
            <el-radio-group v-model="activity.businessType">
              <el-radio :label="1">厂商</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label-width="200px" label="选择厂商：" prop="companyId">
            <el-select
              clearable
              filterable
              v-model="activity.companyId"
              @change="changeCompanyId"
              @clear="clearCompanyId"
              placeholder="请选择厂商"
            >
              <el-option
                v-for="item in compList"
                :key="item.type"
                :label="item.name"
                :value="item.type"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="选择品牌：" prop="brandId" label-width="200px">
            <el-select
              clearable
              filterable
              v-model="activity.brandId"
              @focus="brandFocus"
              placeholder="请选择品牌"
            >
              <el-option
                v-for="item in brandList"
                :key="item.type"
                :label="item.name"
                :value="item.type"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="选择地区：" label-width="200px">
            <el-row v-for="(item,index) in cityItem" :key="index">
              <area-select
                :index="index+1"
                @provinceCode="getProvinceCode"
                @cityCode="getCityCode"
                @areaCode="getAreaCode"
                ref="areas"
              ></area-select>

              <div
                class="add-areaselect"
                v-if="index+1 == cityItem.length"
                @click="addAreaSelect"
              >继续添加</div>
              <div
                class="add-areaselect"
                v-if="cityItem.length != 1"
                @click="deleteCodeItem(index)"
              >删除</div>
            </el-row>
          </el-form-item>
          <el-form-item label="活动名称：" prop="activityName" label-width="200px">
            <el-input class="name-input" v-model="activity.activityName" minlength="1" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="上传活动背景图：" class="star" label-width="200px">
            <el-upload
              class="avatar-uploader"
              :action="uploadUrl"
              :show-file-list="false"
              :on-success="coverPicIdSuccess"
              :headers="headerToken"
            >
              <img
                v-if="activity.coverPicId"
                :src="sourceBaseUrl+activity.coverPicId"
                class="avatar"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <div class="el-form-item__error" v-if="!activity.coverPicId && addFlag">请上传活动背景图</div>
          </el-form-item>
          <el-form-item label="活动时间：" prop="startTime" label-width="200px">
            <el-date-picker
              v-model="date"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions"
              @change="dateChange"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="未参加活动按钮：" class="activity-btn-img star" label-width="200px">
            <el-upload
              class="btn-normal-upload"
              :action="uploadUrl"
              :show-file-list="false"
              :headers="headerToken"
              :on-success="unjoinPicSuccess"
            >
              <img v-if="unjoinPic" :src="sourceBaseUrl+unjoinPic" class="avatar" />
              <i v-else>添加图片</i>
            </el-upload>
            <div class="el-form-item__error" v-if="!activity.unjoinPicId && addFlag">请上传已参加活动按钮图片</div>
          </el-form-item>
          <el-form-item label="已参加活动按钮：" class="activity-btn-img star" label-width="200px">
            <el-upload
              class="btn-normal-upload"
              :action="uploadUrl"
              :headers="headerToken"
              :show-file-list="false"
              :on-success="joinPicSuccess"
            >
              <img v-if="joinPic" :src="sourceBaseUrl+joinPic" class="avatar" />
              <i v-else>添加图片</i>
            </el-upload>
            <div class="el-form-item__error" v-if="!activity.joinPicId && addFlag">请上传已参加活动按钮图片</div>
          </el-form-item>
          <el-form-item label="参与人数（假）：" label-width="200px">
            <el-input v-model="activity.selfDefineNum" type="number"></el-input>
          </el-form-item>
          <el-form-item prop="price" label="用户信息单价：">
            <el-input type="number" min="1" @change="priceCange(activity.price)"  v-model="activity.price">
              <template slot="suffix">条/元</template>
            </el-input>
          </el-form-item>
          <el-form-item label="是否包括厂商：">
            <el-radio-group v-model="activity.isContainCompany">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="参与活动门店：">
            <el-row class="store-list">
              <el-radio-group v-model="activity.isAllShop">
                <el-radio :label="1">全部门店</el-radio>
                <el-radio :label="0">指定门店</el-radio>
              </el-radio-group>
              <el-button
                type="primary"
                v-if="activity.isAllShop == 0"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="toAddStore"
              >添加门店</el-button>
            </el-row>
          </el-form-item>
          <el-row v-if="activity.isAllShop == 0">
            <el-table :data="storeList" border stripe style="width: 100%">
              <el-table-column prop="shopName" align="center" label="店铺名称"></el-table-column>
              <el-table-column align="center" label="店铺logo">
                <template slot-scope="scope">
                  <img class="shop-logo" :src="sourceBaseUrl+scope.row.picPath" alt />
                </template>123456
              </el-table-column>
              <el-table-column prop="contactName" align="center" label="联系人"></el-table-column>
              <el-table-column prop="contactPhone" align="center" label="联系人电话"></el-table-column>

              <el-table-column prop="address" align="center" label="区域">
                <template slot-scope="scope">
                  <div>{{scope.row.provinceName}}{{scope.row.cityName}}{{scope.row.areaName}}</div>
                </template>
              </el-table-column>
              <el-table-column prop="shopAddress" align="center" label="地址"></el-table-column>
              <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteStore(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
          <el-form-item>
            <div class="btn-box">
              <el-button class="submit-btn" @click="cancelAdd">取消</el-button>
              <el-button type="primary" class="submit-btn" @click="submitForm('ruleForm')">确定</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <!-- 指定门店 -->
    <el-dialog
      title="添加门店"
      :close-on-click-modal="false"
      width="70%"
      center
      :visible.sync="addStoreDialog"
    >
      <add-store
        :companyId="activity.companyId"
        @addStoreList="addStoreList"
        @cancelPage="cancelDialog"
      ></add-store>
    </el-dialog>
  </div>
</template>

<script>
import qs from "qs";
import areaSelect from "../../components/areaSelect.vue";
import addStore from "./addStore.vue";
export default {
  data() {
    return {
      addStoreDialog: false,
      type: "",
      typeText: "",
      date: [],
      provinceList: [],
      cityList: [],
      regionList: [],
      compList: [],
      brandList: [],
      activity: {
        activityName: "",
        companyId: "",
        brandId: "",
        coverPicId: "",
        startTime: "",
        endTime: "",
        unjoinPicId: "",
        joinPicId: "",
        selfDefineNum: "",
        cityCodes: [],
        isContainCompany: 1,
        businessType: 1,
        isAllShop: 1,
        shopIds: [],
        price: ""
      },
      cityItem: [
        {
        provinceCode: "",
        cityCode: "",
        areaCode: ""
        }
      ],
      selectionList: [],
      storeList: [],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() <= new Date().getTime() - 1000 * 3600 * 24;
        }
      },
      unjoinPic: "",
      joinPic: "",
      headerToken: {
        Authorization: qs.parse(sessionStorage.getItem("loginUser")).token
      },
      uploadUrl: "",
      sourceBaseUrl: "",

      labelPosition: "left",
      addFlag: false,
      rules: {
        activityName: [
          { required: true, message: "请输入活动名称", trigger: "blur" }
        ],
        price: [
          { required: true, message: "请输入用户信息单价", trigger: "blur" }
        ],
        companyId: [
          { required: true, message: "请选择厂商", trigger: ["blur", "change"] }
        ],
        brandId: [
          { required: true, message: "请选择品牌", trigger: ["blur", "change"] }
        ],
        businessType: [
          {
            required: true,
            message: "请选择创建对象",
            trigger: ["change"]
          }
        ],
        startTime: [
          { required: true, message: "请选择活动日期", trigger: "blur" }
        ]
      }
    };
  },
  components: {
    areaSelect,
    addStore
  },
  created() {
    // 初始化厂商列表
    this.getCompList();
    // 初始化图片路径
    let basePath = process.env;
    this.uploadUrl = basePath.systemUrl + "/v1/act4/underline/uploadPic";
    this.sourceBaseUrl = basePath.sourceBaseUrl;
    // 初始化标题
    this.type = this.$route.query.type;
    switch (this.type) {
      case "add":
        this.typeText = "新增活动";
        break;
      case "edit":
        this.typeText = "编辑活动";
        let id = this.$route.query.id;
        this.getDetailInfo(id);
        break;
    }
  },
  methods: {
    getProvinceCode({ val, index }) {
      this.cityItem[index].provinceCode = val;
      if(!val){
        this.cityItem[index].cityCode = '';
         this.cityItem[index].areaCode = '';
      }
    },
    getCityCode({ val, index }) {
      this.cityItem[index].cityCode = val;
      if(!val){
        this.cityItem[index].areaCode = '';
      }
    },
    getAreaCode({ val, index }) {
      this.cityItem[index].areaCode = val;
    },
    priceCange(value){
      if(value.length>5) 
      value=value.slice(0,5)
      this.activity.price = value;
    },
    // 添加地区选择
    addAreaSelect() {
      this.cityItem.push({
        provinceCode: "",
        cityCode: "",
        areaCode: ""
      });
    },
    // 删除地区选择
    deleteCodeItem(index) {
      let vm = this;
      this.cityItem.splice(index, 1);
      setTimeout(() => {
        this.echoArea(vm);
      }, 200);
    },
    getDetailInfo(id) {
      let vm = this;
      this.API.getDetailInfo({ id: id }).then(res => {
        if (res.success) {
          res.obj.startTime = res.obj.startTime.split(" ")[0];
          res.obj.endTime = res.obj.endTime.split(" ")[0];
          this.activity = res.obj;
          this.date.push(res.obj.startTime);
          this.date.push(res.obj.endTime);
          this.joinPic = res.obj.joinPicPath;
          this.unjoinPic = res.obj.unjoinPicPath;
          this.cityItem = res.obj.cityCodes;
          if(this.cityItem.length == 0){
             this.cityItem.push({provinceCode: "",cityCode: "",areaCode: ""})
          }
          this.storeList = res.obj.shopIds;
          setTimeout(() => {
            this.echoArea(vm);
          },800);
          this.getBrandList();
        }
      });
    },
    
    // 回显省市区
    echoArea(vm) {
      
      this.$refs.areas.forEach((e, i) => {
        console.log( vm.cityItem[i].provinceCode,
          vm.cityItem[i].cityCode,
          vm.cityItem[i].areaCode); 
        e.echoData(
          
          vm.cityItem[i].provinceCode,
          vm.cityItem[i].cityCode,
          vm.cityItem[i].areaCode
        );
      });
    },
    dateChange(date) {
      if (date) {
        this.activity.startTime = date[0];
        this.activity.endTime = date[1];
      } else {
        this.activity.startTime = "";
        this.activity.endTime = "";
      }
    },
    coverPicIdSuccess(e) {
      this.activity.coverPicId = e.data.picPath;
    },
    unjoinPicSuccess(e) {
      this.activity.unjoinPicId = e.data.picId;
      this.unjoinPic = e.data.picPath;
    },
    joinPicSuccess(e) {
      this.activity.joinPicId = e.data.picId;
      this.joinPic = e.data.picPath;
    },
    changeCompanyId(e) {
      if (this.activity.companyId) {
        this.brandList = [];
        this.activity.brandId = "";
        this.getBrandList();
      }
    },
    clearCompanyId() {
      this.brandList = [];
      this.activity.brandId = "";
    },
    brandFocus() {
      if (!this.activity.companyId) {
        this.$message({
          message: "请先选择厂商",
          type: "warning"
        });
      }
      if (this.activity.companyId && this.brandList.length == 0) {
        this.$message({
          message: "该厂商品牌为空，请重新选择厂商",
          type: "warning"
        });
      }
    },
    // 厂商列表
    getCompList() {
      this.API.getCompList({ businessType: 1 }).then(res => {
        if (res.success) {
          this.compList = res.obj;
        }
      });
    },
    // 品牌商列表
    getBrandList() {
      this.API.getBrandList({
        companyId: this.activity.companyId
      }).then(res => {
        if (res.success) {
          this.brandList = res.obj;
        }
      });
    },
    toAddStore() {
      this.addStoreDialog = true;
    },
    addStoreList(selectionList) {
      this.selectionList = selectionList;
      this.storeList.forEach(item => {
        this.selectionList.push(item.id);
      });
      this.getShopList();
      this.addStoreDialog = false;
    },
    getShopList() {
      this.API.getShopListByIds({
        shopIds: this.selectionList
      }).then(res => {
        if (res.success) {
          this.storeList = res.datalist;
        }
      });
    },
    deleteStore(index, row) {
      this.storeList.forEach((item, index) => {
        if (item.id == row.id) {
          this.storeList.splice(index, 1);
        }
      });
    },
    cancelDialog() {
      this.addStoreDialog = false;
    },
    cancelAdd() {
      this.$router.push("/offlineActivity");
    },
    submitForm(formName) {
      this.activity.shopIds = this.storeList;
      this.activity.shopIds = this.activity.shopIds.map(item => {
        return {
          id: item.id
        };
      });
      this.addFlag = true;
      if (
        !(
          this.activity.coverPicId &&
          this.activity.joinPicId &&
          this.activity.unjoinPicId
        )
      ) {
        this.$message({
          message: "请填写完整信息",
          type: "warning"
        });
      }
      if(this.activity.isAllShop == 0){
        if(this.activity.shopIds.length == 0){
          this.$message.warning('当前指定门店不能少于一家！');
          return;
        }
      }
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          if (this.type == "add") {
            this.addActivity();
          } else {
            this.updateInfo();
          }
        } else {
          this.$message({
            message: "请填写完整信息",
            type: "warning"
          });
          return false;
        }
      });
    },
    // 新增活动
    addActivity() {
      this.activity.cityCodes = this.cityItem;
      // 一定要同时选择省市区
      // if (this.activity.cityCodes[0].provinceCode!='') {
      //   let result = this.activity.cityCodes.every(
      //     item => item.provinceCode && item.cityCode && item.areaCode
      //   );
      //   if (!result) {
      //     this.$message.warning("请选择省市区！");
      //     return;
      //   }
      // }
      this.API.addActivity(this.activity).then(res => {
        if (res.success) {
          this.$message({
            message: "新增成功",
            type: "success"
          });
          this.$router.push({ path: "/offlineActivity" });
        } else {
          this.$message({
            message: res.message,
            type: "error"
          });
        }
      });
    },
    // 编辑更新活动
    updateInfo() {
      this.activity.cityCodes = this.activity.cityCodes.map(item => {
        return {
          provinceCode: item.provinceCode,
          cityCode: item.cityCode,
          areaCode: item.areaCode
        };
      });
      // 获取门店的id

      this.API.updateInfo(this.activity).then(res => {
        if (res.success) {
          this.$message({
            message: "编辑成功",
            type: "success"
          });
          this.$router.push({ path: "/offlineActivity" });
        } else {
          this.$message({
            message: res.message,
            type: "error"
          });
        }
      });
    }
  }
};
</script>
<style lang='scss'>
.add-activity {
  padding: 30px;

  .main-body {
    min-height: 860px;
    background: #fff;
    .title,
    .breadcrumb-box {
      font-size: 18px;
      padding: 15px;
      border-bottom: 1px solid #eee;
      text-align: left;
    }
    .el-row {
      text-align: left;
    }
    .el-breadcrumb__inner a,
    .el-breadcrumb__inner.is-link {
      font-size: 18px;
      color: #222;
      font-weight: normal;
    }
    .el-breadcrumb__item:last-child .el-breadcrumb__inner {
      font-size: 18px;
      color: #222;
      font-weight: normal;
    }
    .form-box {
      margin: 0 auto;
      margin-top: 30px;
      width: 80%;
      .el-input,
      .el-select {
        width: 200px;
      }
      .name-input{
        width: 250px;
      }
      .el-date-editor {
        width: 400px;
      }
      .el-form-item {
        display: flex;
        justify-content: start;
        align-items: center;
      }
      .el-form-item__label {
        font-size: 16px;
      }

      .choose-brand {
        margin-left: 80px;
      }
      .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
      }
      .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
        object-fit: cover;
      }
      .avatar {
        width: 178px;
        height: 178px;
        display: block;
        object-fit: cover;
      }

      .btn-normal-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 262px;
        height: 60px;
        .avatar {
          width: 262px;
          height: 60px;
          display: block;
          object-fit: cover;
        }

        i {
          width: 262px;
          height: 60px;
          line-height: 50px;
          text-align: center;
          display: inline-block;
        }
      }
    }
    .el-form-item__content {
      line-height: 60px;
    }
    .submit-btn {
      margin-left: 30px;
    }
    .star {
      position: relative;
      .el-form-item__label {
        padding-left: 8px;
      }
      &::before {
        content: "*";
        color: #f56c6c;
        margin-right: 4px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        // left: -2px;
        left: 47px;
        width: 14px;
        height: 14px;
      }
    }
  }
  .store-list {
    display: flex;
    align-items: center;
    button {
      margin-left: 30px;
    }
  }
  .shop-logo {
    height: 50px;
    max-width: 80px;
    object-fit: contain;
  }
  .add-areaselect {
    display: inline-block;
    cursor: pointer;
    color: #3399ff;
    width: 70px;
    text-align: left;
  }
  .btn-box {
    padding: 30px 0;
  }
}
</style>
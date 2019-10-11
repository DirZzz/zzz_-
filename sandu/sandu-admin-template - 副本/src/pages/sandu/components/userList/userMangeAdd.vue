<template>
  <div>
    <el-dialog
      :visible.sync="userMangeAdd"
      width="1150px"
      class="userMangeAdd"
      title="新增"
      :append-to-body="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="handleClose"
    >
      <div class="userManage-content">
        <el-form
          :label-position="labelPosition"
          label-width="160px"
          :model="formParam"
          :rules="rules"
          ref="ruleForm"
        >
          <el-row>
            <el-col :span="9">
              <el-form-item label="昵称:">
                <el-input v-model="formParam.userName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="手机号:">
                <el-input v-model="formParam.mobile" maxlength="11"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="新密码:">
                <el-input v-model="formParam.password"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="9" style="line-height: 40px;">
              <div>
                <el-button
                  type="primary"
                  size="medium"
                  @click="random()"
                  style="margin-left: 20px"
                >随机生成</el-button>
                <span style="margin-left: 10px">默认密码：123456</span>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="邮箱:">
                <el-input v-model="formParam.email"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="用户类型:" required>
                <userType @change="userTypeChange" :type="'add'" ref="userTypeCon"></userType>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="用户来源:">
                <userOrigin @change="userOriginChange" ref="userOrigin"></userOrigin>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="区域:">
            <areaSelect
              :hasCountry="false"
              @countryCode="countryChange"
              @provinceCode="provinceChange"
              @cityCode="cityChange"
              @areaCode="areaChange"
              @streetCode="streetChange"
              ref="areaSelect"
            ></areaSelect>
          </el-form-item>
          <el-form-item label="所属经销商企业:" v-if="userMethod=='franchiser'">
            <franchiserType @change="franchiserTypeChange" ref="franchiserType"></franchiserType>
          </el-form-item>

          <el-row>
            <el-col :span="16">
              <el-form-item label="详细地址:">
                <el-input v-model="formParam.address"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="设置套餐:">
                <el-input v-model="serviceInfo.servicesName" :readonly="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-button type="text" size="medium" round @click="setPackage">设置套餐</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item label="备注:">
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="不能超过200字"
                  v-model="formParam.remark"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div class="btn">
        <el-button type="primary" size="medium" round @click="save">保存</el-button>
        <el-button size="medium" round @click="toggleDialog(false)">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import minixs from "@s/minixs/index";
import md5 from "js-md5";
import { validateAlphone, validateEmail } from "@s/utils/validate";
export default {
  name: "userMangeAdd",
  mixins: [minixs],
  inject: ["reload"],
  data() {
    return {
      labelPosition: "right",
      serviceName: "",
      page: 1,
      limit: 10,
      formParam: {
        countryCode: "",
        provinceCode: "",
        cityCode: "",
        areaCode: "",
        streetCode: "",
        userName: "", //用户昵称
        email: "", //用户邮箱
        mobile: "", //用户邮箱
        userType: "", //用户类型   必填项
        password: "",
        userSource: "", //用户来源
        servicesId: "", //套餐id
        companyId: this.$route.query.id,
        userMethod: "", //企业类型）必填项  厂商：vendor 经销商：franchiser 用户列表：overAll
        franchiserId: "", //经销商id
        remark: "", //备注
        address: "" //地址
      },
      rules: {}
    };
  },
  created() {},
  methods: {
    countryChange(code) {
      this.formParam.countryCode = code;
    },
    provinceChange(code) {
      this.formParam.provinceCode = code;
    },
    cityChange(code) {
      this.formParam.cityCode = code;
    },
    areaChange(code) {
      this.formParam.areaCode = code;
    },
    streetChange(code) {
      this.formParam.streetCode = code;
    },
    random() {
      this.API.getRandomPassWord(null).then(res => {
        this.formParam.password = res;
      });
    },
    remove() {
      for (let key in this.formParam) {
        this.formParam[key] = null;
      }
      this.formParam.userType = "";
      this.userMethod == "vendor" ? (this.formParam.userType = 2) : "";
      this.userMethod == "franchiser" ? (this.formParam.userType = 3) : "";
    },
    handleClose() {
      this.setDialog({ userMangeAdd: false });
      this.remove();
      this.$refs.userTypeCon.echoData("");
      this.$refs.areaSelect.echoData("");
      this.$refs.userOrigin.echoData("");
    },
    toggleDialog(type) {
      this.setDialog({ userMangeAdd: type });
      if (type) {
        this.userMethod == "franchiser"
          ? (this.$route.query.businessType = 2)
          : "";
        this.userMethod == "vendor" ? (this.$route.query.businessType = 1) : "";
        this.setUserType({
          vm: this,
          companyType: this.$route.query.businessType,
          companyId: this.$route.query.id
        });
        for (let key in this.serviceInfo) {
          this.serviceInfo[key] = null;
        }
      }
      this.remove();
      type ? "" : this.$refs.userTypeCon.echoData("");
      type ? "" : this.$refs.areaSelect.echoData("");
      type ? "" : this.$refs.userOrigin.echoData("");
    },
    userTypeChange(val) {
      this.formParam.userType = val;
    },
    userOriginChange(val) {
      this.formParam.userSource = val;
    },
    franchiserTypeChange(val) {
      this.formParam.franchiserId = val;
    },
    setPackage() {
      //设置套餐
      this.setIsNewAdd(1);
      this.setDialog({ userMangeAdd: false });
      setTimeout(() => {
        let item = {
          companyId: this.$route.query.id,
          userType: this.formParam.userType
        };
        this.setServicelist({ vm: this, item: item });
        this.setDialog({ packageEditDialog: true, packageEditFrom: 0 });
      }, 300);
    },
    reset() {
      for (let key in this.formParam) {
        this.formParam[key] = null;
      }
      this.userMethod == "franchiser" ? this.$refs.franchiserType.reset() : "";
    },
    save() {
      if (this.formParam.mobile && !validateAlphone(this.formParam.mobile)) {
        this.$message.error("电话号码输入有误！");
        return;
      }
      if (this.formParam.email && !validateEmail(this.formParam.email)) {
        this.$message.error("邮箱输入有误！");
        return;
      }
      // this.userMethod=='vendor'?this.formParam.userType=2:'';
      // this.userMethod=='franchiser'?this.formParam.userType=3:'';
      if (!this.formParam.userType) {
        this.$message.error("请填写用户类型！");
        this.formParam.password = null;
        return;
      }
      this.formParam.servicesId = this.serviceInfo.servicesId;
      this.formParam.priceId = this.serviceInfo.priceId;
      //                this.formParam.password ? this.formParam.password=md5(md5('WeB'+this.formParam.password)) : this.formParam.password=md5(md5('WeB123456'));
      this.formParam.userMethod = this.userMethod;
      for (let key in this.formParam) {
        this.formParam[key] ? this.formParam[key] : delete this.formParam[key];
      }
      this.formParam.companyId = this.$route.query.id;
      this.API.addUser(this.formParam).then(res => {
        if (res.success) {
          this.$message.success("新增成功");
          this.reset();
          this.reload();
          this.toggleDialog(false);

          this.remove();
        } else {
          this.$message.error(res.message);
          this.remove();
        }
      });
    }
  }
};
</script>

<style scoped lang="scss">
.userManage-content {
  margin-top: 20px;
}
.btn {
  text-align: center;
  margin-top: 20px;
}
</style>
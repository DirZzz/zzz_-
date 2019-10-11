<template>
  <page>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: item.path }"
                          v-for="(item,index) in userManageTabList"
                          :key="index">{{item.name}}</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="edit-content">
      <el-form :label-position="labelPosition"
               label-width="160px"
               :model="formParam">
        <el-row :gutter="24">
          <el-col :span="10">
            <el-form-item label="登录名："
                          required>
              <el-input v-model="formParam.nickName"
                        :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="手机号：">
              <el-input v-model="formParam.mobile"
                        maxlength="11"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="10">
            <el-form-item label="昵称：">
              <el-input v-model="formParam.userName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="设置新密码：">
              <div style="display: flex">
                <el-input v-model="formParam.password"></el-input>
                <el-button type="primary"
                           size="medium"
                           @click="random()"
                           style="margin-left: 20px">随机生成</el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="10">
            <el-form-item label="邮箱：">
              <el-input v-model="formParam.email"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="用户来源：">
              <userOrigin @change="userOriginChange"
                          ref="userOrigin"></userOrigin>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="区域：">
          <areaSelect :hasCountry="false"
                      @countryCode="countryChange"
                      @provinceCode="provinceChange"
                      @cityCode="cityChange"
                      @areaCode="areaChange"
                      @streetCode="streetChange"
                      ref="areaSelect"></areaSelect>
        </el-form-item>
        <el-row :gutter="60">
          <el-col :span="10">
            <el-form-item label="详细地址：">
              <el-input v-model="formParam.address"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="用户类型："
                          required>
              <userType @change="userTypeChange"
                        :type="'add'"
                        ref="userTypeCon"></userType>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="10">
            <el-form-item label="首次登录时间：">
              <el-input v-model="firstLoginTime"
                        disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="到期时间：">
              <el-input v-model="failureTime"
                        disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="有效时长：">
              <el-input v-model="vaildTime"
                        disabled></el-input>
            </el-form-item>
          </el-col>
          <!--userType==3 经销商才会有的-->
          <el-col :span="10"
                  v-show="userType==3">
            <el-form-item label="所属经销商企业：">
              <franchiserType @change="franchiserTypeChange"
                              ref="franchiserType"></franchiserType>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="9">
            <el-form-item label="设置套餐:">
              <el-input v-model="serviceInfo.servicesName"
                        :readonly="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-button type="text"
                       size="medium"
                       round
                       @click="setPackage">设置套餐</el-button>
          </el-col>
        </el-row>
        <el-form-item label="备注：">
          <el-input class="textarea"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入内容"
                    v-model="formParam.remark"></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="10">
            <el-form-item label="暂停使用时间：">
              <el-date-picker v-model="formParam.accountFreezeTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              :picker-options="pickerOptionsEnd"
                              @change="timeChange"
                              placeholder="选择日期"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="开启使用时间：">
              <el-date-picker v-model="formParam.accountEnableTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              :picker-options="pickerOptionsStart"
                              placeholder="选择日期"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="btn">
      <el-button type="primary"
                 size="medium"
                 round
                 @click="save">保存</el-button>
      <el-button size="medium"
                 round
                 @click="back()">取消</el-button>
    </div>
    <packageEdit ref="packageEdit"></packageEdit>
  </page>
</template>

<script>
import { validateAlphone, validateEmail } from "@s/utils/validate";
import md5 from "js-md5";
import packageEdit from "@s/components/userList/packageEdit";
import minixs from "@s/minixs/index";
export default {
  name: "listEdit",
  mixins: [minixs],
  components: {
    packageEdit
  },
  data () {
    return {
      input: "",
      labelPosition: "right",
      userInfo: "",
      oldPassword: "",
      servicesFlag: false,
      userType: "",
      page: 1,
      vaildTime: "",
      firstLoginTime: "",
      failureTime: "",
      formParam: {
        id: this.$route.query.userId,
        nickName: "",
        mobile: "",
        userName: "",
        password: "",
        countryCode: "",
        provinceCode: "",
        cityCode: "",
        areaCode: "",
        streetCode: "",
        email: "",
        userSource: "",
        remark: "",
        userType: "", //用户类型   必填项
        address: "",
        franchiserId: "",
        accountFreezeTime: "",
        accountEnableTime: "",
        servicesId: "" //套餐id
      },
      pickerOptionsStart: {
        disabledDate: time => {
          //暂停使用时间大于此刻  开始启用时间要小于等于暂停使用时间
          let endDateVal = this.formParam.accountFreezeTime;
          if (endDateVal) {
            return (
              time.getTime() <= Date.now() - 8.64e7 ||
              time.getTime() <= new Date(endDateVal).getTime() - 8.64e7
            );
          } else {
            return time.getTime() <= Date.now() - 8.64e7;
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          //暂停使用时间 大于此刻
          let beginDateVal = this.formParam.accountEnableTime;
          if (beginDateVal) {
            return time.getTime() <= Date.now() - 8.64e7;
          } else {
            return time.getTime() <= Date.now() - 8.64e7;
          }
        }
      }
    };
  },
  mounted () {
    this.userManageTabList.push({
      path: "",
      name: "编辑"
    });

    this.page = this.$route.query.page;
    this.userManageTabList.map((res, index) => {
      if (index < this.userManageTabList.length - 1) {
        res.oldPath ? (res.path = res.oldPath) : "";
      }
    });
    this.setTabList(this.userManageTabList);
    this.API.editUserInfo({ userId: this.$route.query.userId }).then(res => {
      if (res.success) {
        for (let key in this.formParam) {
          this.formParam[key] = res.obj.userInfo[key];
        }
        this.formParam["password"] = "";
        this.oldPassword = res.obj.userInfo.password;
        this.userType = res.obj.userInfo.userType;
        this.$nextTick(() => {
          this.$refs.areaSelect.echoData({
            countryCode: res.obj.userInfo.countryCode,
            provinceCode: res.obj.userInfo.provinceCode,
            cityCode: res.obj.userInfo.cityCode,
            areaCode: res.obj.userInfo.areaCode,
            streetCode: res.obj.userInfo.streetCode
          }); //回显
          this.$refs.userOrigin.echoData(res.obj.userInfo.userSource); //回显
          this.$refs.userTypeCon.echoData(res.obj.userInfo.userType);
          this.$refs.franchiserType.echoData(
            res.obj.userInfo.businessAdministrationId
          );
        });
        this.servicesFlag = res.obj.userInfo.servicesFlag;
        this.vaildTime = res.obj.vaildTime;
        this.firstLoginTime = res.obj.userInfo.firstLoginTime;
        this.failureTime = res.obj.userInfo.failureTime;
        if (res.obj.serviceInfo) {
          this.setServiceInfo(res.obj.serviceInfo);
          this.setPackageData(res.obj.serviceInfo);
        }
      } else {
        this.$message.error(res.message);
      }
    });
  },
  methods: {
    timeChange () {
      this.formParam.accountEnableTime = "";
    },
    countryChange (code) {
      this.formParam.countryCode = code;
    },
    provinceChange (code) {
      this.formParam.provinceCode = code;
    },
    cityChange (code) {
      this.formParam.cityCode = code;
    },
    userTypeChange (val) {
      this.formParam.userType = val;
      this.$route.query.userType = val;
    },
    areaChange (code) {
      this.formParam.areaCode = code;
    },
    streetChange (code) {
      this.formParam.streetCode = code;
    },
    userOriginChange (val) {
      this.formParam.userSource = val;
    },
    setPackage () {
      //设置套餐
      let item = {
        companyId: this.$route.query.companyId,
        userType: this.$route.query.userType
      };
      this.setServicelist({ vm: this, item: item });
      this.setDialog({ packageEditDialog: true, packageEditFrom: 0 });
      this.setIsNewAdd(3);
    },
    back () {
      switch (this.userMethod) {
        case "vendor":
          this.$router.push(
            "/userManage/mfrsManage/userManage?id=" +
            this.$route.query.companyId
          );
          break;
        case "franchiser":
          this.$router.push(
            "/userManage/mfrsManage/userManage?id=" +
            this.$route.query.companyId
          );
          break;
        case "company":
          this.$router.push(
            "/userManage/companyList/userManage?id=" +
            this.$route.query.companyId
          );
          break;
        default:
          this.$router.push("/userManage/userList");
          break;
      }
    },
    random () {
      this.API.getRandomPassWord(null).then(res => {
        this.formParam.password = res;
      });
    },
    franchiserTypeChange (val) {
      this.formParam.franchiserId = val;
    },
    save () {
      if (this.formParam.mobile && !validateAlphone(this.formParam.mobile)) {
        this.$message.error("手机输入有误！");
        return;
      }
      if (this.formParam.email && !validateEmail(this.formParam.email)) {
        this.$message.error("邮箱输入有误！");
        return;
      }
      if (!this.formParam.userType) {
        this.$message.error("用户类型不能为空！");
        return;
      }
      this.formParam.password
        ? (this.formParam.password = md5(md5("WeB" + this.formParam.password)))
        : (this.formParam.password = this.oldPassword);
      this.formParam.userMethod = this.userMethod;
      this.formParam.servicesId = this.serviceInfo.servicesId;
      this.formParam.priceId = this.serviceInfo.priceId;
      this.API.saveEditUser(this.formParam).then(res => {
        if (res.success) {
          this.$message.success("操作成功");
          this.back();
        } else {
          this.$message.error(res.message);
        }
      });
    }
  }
};
</script>

<style scoped lang="scss">
.edit-content {
  margin-top: 30px;
  text-align: left;
}
.btn {
  margin-top: 40px;
  text-align: center;
}
.textarea {
  width: 610px;
  vertical-align: top;
}
</style>
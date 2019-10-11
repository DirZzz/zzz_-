<template>
  <div>
    <el-dialog :visible.sync="userManageBatchAdd"
               width="1150px"
               class="userMangeAdd"
               title="批量新增"
               :append-to-body="true"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :before-close="handleClose">
      <div class="userManage-content">
        <el-form :label-position="labelPosition"
                 label-width="160px"
                 :model="formParam"
                 :rules="rules"
                 ref="ruleForm">
          <el-row>
            <el-col :span="9">
              <el-form-item label="新增数量:"
                            required
                            prop="increaseNum">
                <el-input type="age"
                          v-model.number="formParam.increaseNum"
                          autocomplete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="用户类型:"
                            prop="userType"
                            required>
                <userType @change="userTypeChange"
                          :type="'add'"
                          :companyType="userMethod"
                          ref="userTypeCon"></userType>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="设置密码:">
                <el-input v-model="formParam.password"
                          :disabled="passDisabled"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3"
                    style="line-height: 40px;margin-left: 20px">
              <el-checkbox v-model="checked"
                           @change="passwordChange">随机生成</el-checkbox>

              <!--<span>默认密码：123456</span>-->
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="用户来源:">
                <userOrigin @change="userOriginChange"
                            ref="userOrigin"></userOrigin>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="区域:">
            <areaSelect :hasCountry="false"
                        @countryCode="countryChange"
                        @provinceCode="provinceChange"
                        @cityCode="cityChange"
                        @areaCode="areaChange"
                        @streetCode="streetChange"
                        ref="areaSelect"></areaSelect>
          </el-form-item>
          <el-form-item label="所属经销商企业:"
                        v-if="userMethod=='franchiser'">
            <franchiserType @change="franchiserTypeChange"
                            ref="franchiserType"></franchiserType>
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
                <el-input v-model="serviceInfo.servicesName"
                          :readonly="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-button type="text"
                         size="medium"
                         round
                         @click="setPackage(serviceInfo.servicesName,serviceInfo.priceId)">设置套餐</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div class="btn">
        <el-button type="primary"
                   size="medium"
                   round
                   @click="save"
                   :disabled="disabled">保存并导出excel</el-button>
        <el-button size="medium"
                   round
                   @click="toggleDialog(false)">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import minixs from "@s/minixs/index";
import md5 from "js-md5";
export default {
  name: "userMangeAdd",
  mixins: [minixs],
  inject: ["reload"],
  data () {
    return {
      labelPosition: "right",
      serviceName: "",
      page: 1,
      limit: 10,
      disabled: false,
      passDisabled: false,
      checked: false,
      formParam: {
        countryCode: "",
        provinceCode: "",
        cityCode: "",
        areaCode: "",
        streetCode: "",
        increaseNum: "", //创建人数
        userType: "", //用户类型   必填项
        password: "",
        userSource: "", //用户来源
        servicesId: "", //套餐id
        companyId: this.$route.query.id
          ? this.$route.query.id
          : this.$route.query.companyId,
        userMethod: "", //企业类型）必填项  厂商：vendor 经销商：franchiser 用户列表：overAll
        franchiserId: "", //经销商id
        address: "", //地址
        isRandom: "0" //随机生成
      },
      rules: {
        increaseNum: [
          { required: true, message: "请填入新增数量" },
          { type: "number", message: "必须为数字值" }
        ],
        userType: [{ required: true, message: "请填入用户类型" }]
      }
    };
  },
  created () {
    //this.formParam.companyId = this.$route.query.id?this.$route.query.id:this.$route.query.companyId;
  },
  methods: {
    countryChange (code) {
      this.formParam.countryCode = code;
    },
    provinceChange (code) {
      this.formParam.provinceCode = code;
    },
    cityChange (code) {
      this.formParam.cityCode = code;
    },
    areaChange (code) {
      this.formParam.areaCode = code;
    },
    streetChange (code) {
      this.formParam.streetCode = code;
    },
    passwordChange (val) {
      console.log(val);
      if (val) {
        this.formParam.password = "";
        this.passDisabled = true;
        this.formParam.isRandom = 1;
      } else {
        this.passDisabled = false;
        this.formParam.isRandom = 0;
      }
    },
    handleClose () {
      this.setDialog({ userManageBatchAdd: false });
      this.reset();
      this.userManageBatchAdd ? "" : this.$refs.ruleForm.resetFields();
      this.echoDataFun();
    },
    echoDataFun () {
      this.$refs.userTypeCon ? this.$refs.userTypeCon.echoData("") : "";
      this.$refs.areaSelect ? this.$refs.areaSelect.echoData("") : "";
      this.$refs.userOrigin ? this.$refs.userOrigin.echoData("") : "";
      this.$refs.franchiserType ? this.$refs.franchiserType.echoData("") : "";
    },
    toggleDialog (type) {
      this.setDialog({ userManageBatchAdd: type });
      if (type) {
        this.companyType == "franchiser"
          ? (this.$route.query.businessType = 2)
          : "";
        this.companyType == "vendor"
          ? (this.$route.query.businessType = 1)
          : "";
        this.setUserType({
          vm: this,
          companyType: this.$route.query.businessType,
          companyId: this.$route.query.id
        });
        for (let key in this.serviceInfo) {
          this.serviceInfo[key] = null;
        }
      } else {
        this.$refs.ruleForm.resetFields();
        this.echoDataFun();
      }
      this.reset();
    },
    userTypeChange (val) {
      this.formParam.userType = val;
    },
    franchiserTypeChange (val) {
      this.formParam.franchiserId = val;
    },
    userOriginChange (val) {
      this.formParam.userSource = val;
    },
    setPackage (name, pid) {
      //设置套餐
      this.setIsNewAdd(2);
      this.setDialog({ userManageBatchAdd: false, id: false });
      setTimeout(() => {
        let item = {
          companyId: this.$route.query.id
            ? this.$route.query.id
            : this.$route.query.companyId,
          userType: this.formParam.userType
        };
        this.setServicelist({ vm: this, item: item });
        this.setDialog({
          packageEditDialog: true,
          name: name,
          pid: pid
        });
      }, 300);
    },
    reset () {
      for (let key in this.formParam) {
        this.formParam[key] = null;
      }
      this.formParam.companyId = this.$route.query.id
        ? this.$route.query.id
        : this.$route.query.companyId;
      this.checked = false;
      this.passDisabled = false;
    },
    save () {
      if (!this.formParam.increaseNum) {
        this.$message.error("新增数量不能为空！");
        return;
      }
      if (!this.formParam.userType) {
        this.$message.error("用户类型不能为空！");
        return;
      }

      //                this.formParam.password ? this.formParam.password=md5(md5('WeB'+this.formParam.password)) : this.formParam.password=md5(md5('WeB123456'));
      this.formParam.userMethod = this.userMethod;
      this.formParam.servicesId = parseInt(this.serviceInfo.servicesId);
      this.formParam.priceId = this.serviceInfo.priceId;
      for (let key in this.formParam) {
        this.formParam[key] ? this.formParam[key] : delete this.formParam[key];
      }

      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          this.disabled = true;
          this.formParam.companyId = this.$route.query.id
            ? this.$route.query.id
            : this.$route.query.companyId;

          this.loadingTwo = this.$loading({
            lock: true,
            text: "Loading",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)"
          });

          this.API.batchAddUser(this.formParam).then(res => {
            console.log(res);
            console.log(res.type);

            var reader = new FileReader();
            //byte为blob对象
            console.log(reader.readAsBinaryString(res));

            if (res.type === "application/json") {
              this.loadingTwo.close();
              //                                this.$message.error(res.message);
              this.$message.error("批量新增用户失败");
              this.disabled = false;
            } else {
              this.loadingTwo.close();
              let url = window.URL.createObjectURL(res);
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.setAttribute("download", "批量新增用户信息表.xls");
              document.body.appendChild(link);
              link.click();

              this.disabled = false;
              this.reload();
              this.toggleDialog(false);
              this.reset();
            }
          });
        } else {
          this.formParam.password = null;
          return false;
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
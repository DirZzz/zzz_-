<template>
  <page>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/userManage/companyList' }">公司列表</el-breadcrumb-item>
      <el-breadcrumb-item>编辑</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="company-content">
      <el-form
        :label-position="labelPosition"
        label-width="160px"
        :model="formParam"
        :rules="rules"
        ref="ruleForm"
      >
        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="企业编码:">
              <el-input v-model="formParam.companyCode" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="企业名称:" required prop="companyName">
              <el-input v-model="formParam.companyName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客服QQ:">
              <el-input v-model="formParam.companyCustomerQq"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="区域:">
            <areaSelect
              @provinceCode="provinceChange"
              @cityCode="cityChange"
              @areaCode="areaChange"
              @streetCode="streetChange"
              ref="areaSelect"
            ></areaSelect>
          </el-form-item>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="合同号:">
              <el-input v-model="formParam.contractNumber"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同生效时间:">
              <el-date-picker
                v-model="formParam.contractEffectiveTime"
                type="datetime"
                value-format="yyyy-MM-dd:HH"
                placeholder="选择时间日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="企业网站:">
              <el-input v-model="formParam.companyUrl"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业LOGO:">
              <upload @success="successUpload">
                <img v-if="uploadData.url" :src="uploadData.url" class="avatar" />
                <i v-else class="el-icon-plus"></i>
              </upload>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="联系人姓名:">
              <el-input v-model="formParam.contactName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系人电话:">
              <el-input v-model="formParam.phone" maxlength="11"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="经营范围:">
              <el-input v-model="formParam.businessScope"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业标识:">
              <el-input v-model="formParam.companyIdentify"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="企业详细地址:">
              <el-input v-model="formParam.companyAddress"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="品牌网站:">
              <el-input v-model="formParam.companyDomainName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="方案公开提成率:">
              <el-input v-model="formParam.withdrawRate"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方案是否审核:">
              <el-select v-model="formParam.isExamine" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="60">
          <el-col :span="8">
            <el-form-item label="公司类型:" required prop="businessType">
              <companyType @companyType="companyTypeChange" ref="companyType" :type="1"></companyType>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合作企业:">
              <el-input v-model="formParam.cooperationCompany"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="企业介绍:">
          <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="formParam.companyDesc"></el-input>
        </el-form-item>
        <el-form-item label="备注:">
          <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="formParam.remark"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn">
      <el-button type="primary" size="medium" round @click="save">保存</el-button>
      <el-button size="medium" round>重置</el-button>
    </div>
  </page>
</template>

<script>
import {
  validateAlphone,
  validateURL,
  validateUnmber
} from "@s/utils/validate";
export default {
  name: "companyAdd",
  data() {
    return {
      labelPosition: "right",
      options: [
        {
          name: "是",
          value: 0
        },
        {
          name: "否",
          value: 1
        }
      ],
      uploadData: {},
      businessType: "",
      formParam: {
        id: this.$route.query.id,
        companyName: "", //企业名称
        companyCustomerQq: "", //客服QQ
        provinceCode: "",
        cityCode: "",
        areaCode: "",
        streetCode: "",
        contractNumber: "", //合同号
        contractEffectiveTime: "", //合同生效时间
        companyUrl: "", //企业网站
        phone: "", //联系电话
        businessScope: "", //经营范围
        companyIdentify: "", //企业标识
        companyAddress: "", //企业地址
        companyDomainName: "", //公司品牌网站域名
        isManage: "", // 是否开通小程序管理
        withdrawRate: "", //  方案提成率:0-1之间的小数，小数位后最多5位
        isExamine: "", //方案是否审核
        companyDesc: "", //企业介绍
        remark: "", //备注
        companyLogo: "", //logo
        businessType: "",
        cooperationCompany: ""
      },
      rules: {
        companyName: [
          { required: true, message: "请输入企业名称", trigger: "blur" }
        ],
        businessType: [
          { required: true, message: "请选择公司类型", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.API.editComapnyInner({ id: this.$route.query.id }).then(res => {
      if (res.success) {
        for (let key in res.data) {
          this.formParam[key] = res.data[key];
        }
        this.uploadData.url = res.data.companyLogoUrl
          ? this.BASE_URL.sourceBaseUrl + res.data.companyLogoUrl
          : "";
        this.$refs.areaSelect.echoData({
          provinceCode: res.data.provinceCode,
          cityCode: res.data.cityCode,
          areaCode: res.data.areaCode,
          streetCode: res.data.streetCode
        }); //回显
        this.$refs.companyType.echoData(res.data.businessType); //回显
      } else {
        this.$message.error(res.message);
      }
    });
  },
  methods: {
    save() {
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          if (
            this.formParam.companyCustomerQq &&
            !validateUnmber(this.formParam.companyCustomerQq)
          ) {
            this.$message.error("客服QQ输入有误！");
            return;
          }
          if (
            this.formParam.companyUrl &&
            !validateURL(this.formParam.companyUrl)
          ) {
            this.$message.error("企业网站无效！");
            return;
          }
          if (this.formParam.phone && !validateAlphone(this.formParam.phone)) {
            this.$message.error("联系人电话输入有误！");
            return;
          }
          // if(this.formParam.companyDomainName&&!validateURL(this.formParam.companyDomainName)){
          //     this.$message.error('品牌网站无效！')
          //     return;
          // }
          for (let key in this.formParam) {
            this.formParam[key]
              ? this.formParam[key]
              : delete this.formParam[key];
          }
          this.formParam["withDistributor"] = false; //true: 新增厂商/ false: 新增公司
          this.formParam["isExamine"] = this.formParam.isExamine
            ? this.formParam.isExamine
            : 0; //
          this.API.saveComapnyInner(this.formParam).then(res => {
            if (res.success) {
              this.$message.success("新增成功！");
              this.$router.push("/userManage/companyList");
            } else {
              this.$message.error(res.message);
            }
          });
        } else {
          return false;
        }
      });
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
    successUpload(res) {
      this.uploadData = res.data;
      this.formParam.companyLogo = res.data.resId;
    },
    companyTypeChange(val) {
      this.businessType = val;
      this.formParam.businessType = val;
    }
  }
};
</script>

<style scoped lang="scss">
.company-content {
  margin-top: 20px;
}
.btn {
  text-align: center;
}
</style>
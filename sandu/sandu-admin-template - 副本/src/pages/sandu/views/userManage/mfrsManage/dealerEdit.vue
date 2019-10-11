<template>
  <page>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/userManage/mfrsManage' }">厂商管理</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/userManage/mfrsManage/dealerManage/'+formLabelAlign.pid }">经销商企业</el-breadcrumb-item>
      <el-breadcrumb-item>修改</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="dealerAdd-content">
      <el-form :label-position="labelPosition"
               ref="form"
               label-width="260px"
               :model="formLabelAlign">
        <el-row>
          <el-col :span="10">
            <el-form-item label="经销商名称:"
                          required>
              <el-input clearable
                        v-model="formLabelAlign.companyName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="所属品牌:"
                          required>
              <el-select v-model="brandIds"
                         placeholder="请选择"
                         @change="brandIdsFunc">
                <el-option v-for="item in cabrand"
                           :key="item.brandId"
                           :label="item.brandName"
                           :value="item.brandId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="所属行业:"
                          required>
              <belongIndustry ref="belongIndustry"
                              @change="industryChange"
                              :linkType="true"
                              :pid="formLabelAlign.pid"></belongIndustry>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="企业可见产品范围:"
                          required>
              <produceRange :title="produceRangeTitle"
                            @change="produceRange"
                            ref="produceRange"></produceRange>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="10">
            <el-form-item label="联系人姓名:">
              <el-input clearable
                        v-model="formLabelAlign.contactName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="联系电话:">
              <el-input clearable
                        v-model="formLabelAlign.phone"
                        :maxlength="11"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="区域:">
            <areaSelect @provinceCode="provinceChange"
                        @cityCode="cityChange"
                        @areaCode="areaChange"
                        @streetCode="streetChange"
                        ref="areaSelect"></areaSelect>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="经营范围:">
              <el-input clearable
                        v-model="formLabelAlign.businessScope"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="邮箱:">
              <el-input clearable
                        v-model="formLabelAlign.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="企业详细地址:">
              <el-input clearable
                        v-model="formLabelAlign.companyAddress"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="厂商授权经销商可开通移动端数量:">
              <el-input clearable
                        v-model="formLabelAlign.mobileCount"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="厂商授权经销商可开通PC端数量:">
              <el-input clearable
                        v-model="formLabelAlign.pcCount"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="方案是否审核:">
              <el-select v-model="formLabelAlign.isExamine"
                         placeholder="请选择">
                <el-option v-for="item in isExamine"
                           :key="item.value"
                           :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="企业介绍:">
          <el-input type="textarea"
                    :rows="4"
                    placeholder="请输入内容"
                    v-model="formLabelAlign.companyDesc">
          </el-input>
        </el-form-item>
        <el-form-item label="备注:">
          <el-input type="textarea"
                    :rows="4"
                    placeholder="请输入内容"
                    v-model="formLabelAlign.remark">
          </el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn">
      <el-button type="primary"
                 size="medium"
                 round
                 @click="addDealer">保存</el-button>
      <el-button size="medium"
                 round
                 @click="remove">取消</el-button>
    </div>
  </page>
</template>

<script>
import minixs from "@s/minixs/index";
import {
  validateAlphone,
  validateEmail,
  validateUnmber
} from "@s/utils/validate";

export default {
  name: "dealerAdd",
  mixins: [minixs],
  data () {
    return {
      labelPosition: "right",
      radio: "1",
      textarea: "",
      options: [],
      cabrand: [],
      produceRangeTitle: "",
      isExamine: [
        {
          label: "否",
          value: 0
        },
        {
          label: "是",
          value: 1
        }
      ],
      brandIds: "",
      value: "",
      formLabelAlign: {}
    };
  },
  created () {
    this.init();
  },
  methods: {
    init () {
      this.formLabelAlign.pid = this.$route.query.pid;
      this.formLabelAlign.id = this.$route.query.id;
      this.getCabrand();
      this.getDealer();
    },
    getDealer () {
      this.API.getDealer({ companyId: this.$route.query.id }).then(res => {
        if (res.success) {
          console.log(res.obj);
          this.formLabelAlign = res.obj;
          this.formLabelAlign.pid = this.$route.query.pid;
          this.formLabelAlign.id = this.$route.query.id;
          this.$refs.belongIndustry.echoData(res.obj.companyIndustrys);
          this.$refs.areaSelect.echoData({
            provinceCode: res.obj.provinceCode,
            cityCode: res.obj.cityCode,
            areaCode: res.obj.areaCode,
            streetCode: res.obj.streetCode
          });
          this.$refs.produceRange.echoData(res.obj.productVisibilityRange);
          this.formLabelAlign.productVisibilityRange = res.obj.productVisibilityRange.join(
            ","
          );
          this.formLabelAlign.companyIndustrys = res.obj.companyIndustrys.join(
            ","
          );
          this.brandIds = res.obj.brandIds[0];
          this.produceRangeTitle = res.obj.productVisibilityRangeNames;
        }
      });
    },
    getCabrand () {
      this.API.cabrand({ companyId: this.$route.query.pid }).then(res => {
        if (res.success) {
          this.cabrand = res.datalist;
        }
      });
    },
    remove () {
      this.$router.push({
        path: "/userManage/mfrsManage/dealerManage/" + this.formLabelAlign.pid
      });
    },
    addDealer () {
      let data = this.formLabelAlign;
      if (!data.companyName) {
        this.error("经销商名称不能为空！");
        return;
      }
      if (!data.brandIds) {
        this.error("所属品牌不能为空！");
        return;
      }
      if (!data.companyIndustrys) {
        this.error("所属行业不能为空！");
        return;
      }
      if (!data.productVisibilityRange) {
        this.error("企业可见产品范围不能为空！");
        return;
      }
      if (data.phone && !validateAlphone(data.phone)) {
        this.error("电话号码输入有误！");
        return;
      }
      if (data.email && !validateEmail(data.email)) {
        this.error("邮箱输入有误！");
        return;
      }
      if (data.mobileCount && !validateUnmber(data.mobileCount)) {
        this.error("移动端数量只能输入数字");
        return;
      }
      if (data.pcCount && !validateUnmber(data.pcCount)) {
        this.error("PC端数量只能输入数字");
        return;
      }
      data.brandIds = this.brandIds;
      this.API.updateDealer(data).then(res => {
        if (res.success) {
          this.$message.success("修改经销商成功！");
          setTimeout(res => {
            this.backFun();
          }, 500);
        } else {
          this.error(res.message);
        }
      });
    },
    backFun () {
      this.$router.push({
        path: "/userManage/mfrsManage/dealerManage/" + this.formLabelAlign.pid
      });
    },
    error (txt) {
      this.$message.error(txt);
    },
    provinceChange (code) {
      this.formLabelAlign.provinceCode = code;
    },
    cityChange (code) {
      this.formLabelAlign.cityCode = code;
    },
    areaChange (code) {
      this.formLabelAlign.areaCode = code;
    },
    streetChange (code) {
      this.formLabelAlign.streetCode = code;
    },
    industryChange (obj) {
      this.produceRangeTitle = obj.range.categoryNames;
      this.formLabelAlign.productVisibilityRange = obj.range.categoryIds.join(
        ","
      );
      this.formLabelAlign.companyIndustrys = obj.industry.join(",");
    },
    produceRange (obj) {
      this.produceRangeTitle = obj.range.categoryNames;
      this.formLabelAlign.productVisibilityRange = obj.range.categoryIds.join(
        ","
      );
    },
    brandIdsFunc (code) {
      this.formLabelAlign.brandIds = code;
    }
  }
};
</script>

<style scoped lang="scss">
.dealerAdd-content {
  margin-top: 20px;
}
.btn {
  text-align: center;
}
</style>
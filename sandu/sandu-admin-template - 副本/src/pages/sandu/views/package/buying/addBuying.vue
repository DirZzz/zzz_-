<template>
    <div class="mains">
        <div class="mainBody">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/buying/list' }">企业代购列表</el-breadcrumb-item>
                <el-breadcrumb-item>新增企业代购</el-breadcrumb-item>
            </el-breadcrumb>
            <!--<button @click="this.listServiceOptions"> test button</button>-->
            <div class="formRows">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="200px" class="demo-ruleForm">
                    <el-form-item label="企业名称">
                        <el-select v-model="ruleForm.companyId" @change="listServiceOptions" placeholder="请选择"
                                   filterable>
                            <el-option v-if="companyOptions" v-for="item in companyOptions"
                                       :label="item.name"
                                       :key="item.id"
                                       :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="套餐选择">
                        <el-select v-model="ruleForm.serviceId" @change="listUserTypeOptions" clearable
                                   placeholder="请选择">

                            <el-option v-if="serviceOptions" v-for="item in serviceOptions"
                                       :label="item.name"
                                       :key="item.id"
                                       :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="使用人员类型">
                        <el-select v-model="ruleForm.userTypeId" @change="listServiceUseageOptions" clearable
                                   placeholder="请选择">
                            <el-option v-for="item in userTypeOptions" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="时长选择">
                        <el-select v-model="ruleForm.timeTypeId" placeholder="请选择" clearable>
                            <el-option v-for="item in useTimeOptions" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数量选择">
                        <el-input-number v-model="ruleForm.num" :min="0"></el-input-number>
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input type="textarea" v-model="ruleForm.remark" clearable></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')" round>立即创建</el-button>
                        <el-button @click="resetForm('ruleForm')" round>重置</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
import { at, cloneDeep, set, setWith } from "lodash";

export default {
  name: "packagelist",
  data() {
    return {
      imgurl: process.env.sourceBaseUrl,
      companyOptions: [],
      serviceOptions: [],
      userTypeOptions: [],
      useTimeOptions: [],
      ruleForm: {
        companyId: "",
        userTypeId: "",
        serviceId: "",
        timeTypeId: "",
        num: "",
        creator: "",
        remark: ""
      },
      rules: {}
    };
  },
  created() {
    this.init();
  },
  methods: {
    init(){
        this.listCompanyOptions();
        this.ruleForm.creator = sessionStorage.getItem('nickName')
    },
    submitForm(formName) {
      this.API.purchaseAccount(this.ruleForm).then(res=>{
          if(res.success){
              this.$message.success('新增企业代购成功！')
              setTimeout(res=>{this.backFun();},500)
          }else{
              this.$message.error(res.message);
          }
      });
    },
    backFun(){
        this.$router.push({path: '/buying/list'});
    },
    resetForm() {
      this.ruleForm = {
        companyId: "",
        userTypeId: "",
        serviceId: "",
        timeTypeId: "",
        num: "",
        creator: sessionStorage.getItem('nickName'),
        remark: ""
      };
    },
    listCompanyOptions() {
      this.API.listCompanyOptions().then(res => {
        if (res) {
          this.companyOptions = this.getOptions(
            { id: "id", name: "companyName" },
            res
          );
        }
      });
    },
    listServiceOptions() {
      this.API.listServiceOptions({ companyId: this.ruleForm.companyId }).then(
        res => {
          if (res) {
            this.serviceOptions = this.getOptions(
              { id: "id", name: "servicesName" },
              res
            );
          }
        }
      );
    },
    listUserTypeOptions() {
      this.API.listUserTypeOptions({
        servicesId: this.ruleForm.serviceId
      }).then(res => {
        if (res) {
          this.userTypeOptions = this.getOptions(
            { id: "id", name: "name" },
            res
          );
        }
      });
    },
    listServiceUseageOptions() {
      this.API.listServiceUseageOptions({
        companyId: this.ruleForm.companyId,
        servicesId: this.ruleForm.serviceId
      }).then(res => {
        if (res) {
          this.useTimeOptions = this.getOptions(
            { id: "id", name: "priceUnit" },
            res
          );
        }
      });
    },
    getOptions(mapper, list) {
      return list.map(r => {
        let tmp = {};
        for (let f in mapper) {
          setWith(tmp, f, r[mapper[f]]);
        }
        return tmp;
      });
    }
  }
};
</script>

<style scoped lang="scss">
.mains {
  width: 100%;
  height: 100%;
  padding: 20px;
  padding-right: 15px;
  overflow: hidden;
  .mainBody {
    overflow-y: scroll;
    background: #fff;
    width: 100%;
    height: 100%;
    padding: 20px;
    .formRows {
      margin-top: 20px;
      width: 100%;
      padding: 0 40px;
      width: 60%;
    }
  }
}
</style>
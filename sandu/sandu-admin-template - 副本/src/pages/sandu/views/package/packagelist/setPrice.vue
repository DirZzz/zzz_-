<template>
    <div class="mains">
        <div class="mainBody">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/packagelist/list' }">套餐列表</el-breadcrumb-item>
                <el-breadcrumb-item :to="{ path: '/packagelist/setPriceList?id='+formInline.id }">企业价格设置列表</el-breadcrumb-item>
                <el-breadcrumb-item>设置企业价格</el-breadcrumb-item>
            </el-breadcrumb>
            <div class="formRows"> 
                <div class="formTitle">
                    <el-form ref="form" :model="formInline" label-width="110px"  size="small" label-position="left">
                        <el-form-item label="选择企业名称：">
                            <el-select v-model="formInline.companyId" multiple filterable default-first-option collapse-tags style="width:250px">
                                <el-option v-for="item in companyList":key="item.id" :label="item.companyName.length>10?item.companyName.replace(item.companyName.substring(10,item.companyName.length),'...'):item.companyName" :value="item.id">
                                    <span>{{item.companyName}}</span>
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                </div>
                <div class="formTitle">
                    <p>价格设置：</p>
                </div>
                <el-form :model="item" label-width="0" class="demo-ruleForm lines magin30" size="small" v-for="(item,index) in formInline.prices">
                    <el-row :gutter="5">
                        <el-col :md="2" :lg="3" >
                            <el-form-item>
                                <el-input-number @change="priceFun(index)"  :min="0"  :precision="0"  :max="999999" v-model="item.price" placeholder="套餐价格"></el-input-number >
                                <span class="placeholders" v-if="priceFlag[index].flag" style="color:#f56c6c">价格不能小于等于0
                                    <span v-if="item.price<=0" style="color:#f56c6c">*</span>
                                </span>
                                <span class="placeholders" v-else>套餐价格<span style="color:#f56c6c">*</span></span>
                            </el-form-item>
                            
                        </el-col>
                        <el-col :md="1" :lg="1" ><p class="labels">元</p></el-col>
                        <el-col :md="2" :lg="3" >
                            <el-form-item>
                                <el-input-number :min="0" @change="durationFun(index)"  :precision="0"  :max="99"  placeholder="套餐时长" v-model="item.duration"></el-input-number >
                                <span class="placeholders" v-if="durationFlag[index].flag" style="color:#f56c6c">时长不能小于等于0
                                    <span style="color:#f56c6c">*</span>
                                </span>
                                <span class="placeholders" v-else>套餐时长<span style="color:#f56c6c">*</span></span>
                            </el-form-item>
                        </el-col>
                        <el-col :md="2" :lg="2" >
                            <el-form-item>
                                <el-select v-model="item.priceUnit" placeholder="年">
                                    <el-option label="年" value="0"></el-option>
                                    <el-option label="月" value="1"></el-option>
                                    <el-option label="日" value="2"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :md="2" :lg="3" >
                            <el-form-item>
                                <el-input-number :min="0"  :precision="0"  :max="9999" v-model="item.giveDuration" placeholder="赠送时长"></el-input-number >
                                <span class="placeholders">赠送时长</span>
                            </el-form-item>
                        </el-col>
                        <el-col :md="1" :lg="1" ><p class="labels">天</p></el-col>
                        <el-col :md="2" :lg="3" >
                            <el-form-item>
                                <el-input-number :min="0"  :precision="0"  :max="999999" v-model="item.sanduCurrency" placeholder="赠送度币"></el-input-number>
                                <span class="placeholders">赠送度币</span>
                            </el-form-item>
                        </el-col>
                        <el-col :md="2" :lg="3" >
                            <el-form-item>
                                <el-input-number :min="0"  :precision="0"  :max="99" v-model="item.freeRenderDuration" placeholder="免费渲染时长"></el-input-number>
                                <span class="placeholders">免费渲染时长</span>
                            </el-form-item>
                        </el-col>
                        <el-col :md="1" :lg="1" ><p class="labels">月</p></el-col>
                        <el-col :md="3" :lg="3" >
                            <el-form-item>
                                <el-button type="success" @click="addMore">添加</el-button>
                                <el-button type="danger" v-if="index>0"  @click="delMore(index)">删除</el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
                <div class="btnRows">
                    <el-row>
                        <el-button type="primary" @click="addCompanyPrice" round>确认</el-button>
                        <el-button @click="backFun" round>取消</el-button>
                    </el-row>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  name: "packagelist",
  data() {
    return {
      imgurl: process.env.sourceBaseUrl,
      companyList: [],
      priceFlag: [
        { flag: false },
        { flag: false },
        { flag: false },
        { flag: false },
        { flag: false }
      ],
      durationFlag: [
        { flag: false },
        { flag: false },
        { flag: false },
        { flag: false },
        { flag: false }
      ],
      formInline: {
        prices: [
          {
            duration: "",
            freeRenderDuration: "",
            giveDuration: "",
            price: "",
            priceUnit: "0",
            sanduCurrency: ""
          }
        ],
        companyName: "",
        companyId: "",
        id: "",
        servicesName: "1", //无用字段，勿删
        userScope: "1", //无用字段，勿删
        saleChannel: "1", //无用字段，勿删
        servicesType: "1", //无用字段，勿删
        modifier: sessionStorage.getItem("userId"), //无用字段，勿删
        creator: sessionStorage.getItem("userId")
      }
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.formInline.id = this.$route.query.id;
      this.API.getCompanyList().then(res => {
        this.companyList = res.obj;
      });
    },
    priceFun(i) {
      this.priceFlag[i].flag = !this.formInline.prices[i].price;
    },
    durationFun(i) {
      this.durationFlag[i].flag = !this.formInline.prices[i].duration;
    },
    addCompanyPrice() {
      let formInline = this.formInline;
      let flags = false;
      if (formInline.companyId.length<=0||!formInline.companyId) {
        this.$message.error("企业名称不能为空！");
        flags = true;
      }
      formInline.prices.map((v, i) => {
        if (v.price <= 0) {
          flags = true;
          this.priceFlag[i].flag = true;
        } else {
          this.priceFlag[i].flag = false;
        }
        if (v.duration <= 0) {
          flags = true;
          this.durationFlag[i].flag = true;
        } else {
          this.durationFlag[i].flag = false;
        }
      });
      if (flags) {
        return;
      }
      formInline.companyId = formInline.companyId? formInline.companyId.join(",") : "";
      this.API.addCompanyPrice(formInline).then(res => {
        if (res.success) {
          this.$message.success("价格添加成功！");
          setTimeout(res => {
            this.backFun();
          }, 500);
        } else {
          this.removeFun();
          formInline.companyId = Array.from(formInline.companyId);
          this.$message.error(res.message);
        }
      });
    },
    addMore() {
      if (this.formInline.prices.length >= 5) {
        this.$message({
          message: "最多添加5条数据",
          type: "warning"
        });
        return;
      }
      this.formInline.prices.push({
        duration: "",
        freeRenderDuration: "",
        giveDuration: "",
        price: "",
        priceUnit: "0",
        sanduCurrency: ""
      });
    },
    delMore(index) {
      this.formInline.prices.splice(index, 1);
      this.priceFlag[index].flag = false;
      this.durationFlag[index].flag = false;
    },
    backFun() {
      this.$router.push({
        path: "/packagelist/setPriceList?id=" + this.formInline.id
      });
    },
    removeFun() {
      this.formInline = {
        prices: [
          {
            duration: "",
            freeRenderDuration: "",
            giveDuration: "",
            price: "",
            priceUnit: "0",
            sanduCurrency: ""
          }
        ],
        companyName: "",
        companyId: "",
        servicesName: "1",
        userScope: "1",
        saleChannel: "1",
        servicesType: "1",
        id: this.$route.query.id,
        modifier: sessionStorage.getItem("userId"),
        creator: sessionStorage.getItem("userId")
      };
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
      padding: 0;
      .formTitle {
        margin-top: 20px;
        width: 310px;
        p {
          height: 32px;
          line-height: 32px;
          text-align: left;
          font-size: 14px;
          color: #606266;
          margin-bottom: 18px;
        }
      }
      .labels {
        height: 100%;
        line-height: 32px;
        text-align: left;
        font-size: 14px;
      }
      .btnRows {
        text-align: left;
        margin-top: 22px;
      }
      .placeholders {
        position: absolute;
        width: 100%;
        text-align: center;
        line-height: 18px;
        font-size: 12px;
        color: #999;
        bottom: -18px;
        left: 0;
      }
    }
  }
}
</style>
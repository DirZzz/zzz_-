
<template>
  <div class="loginFrame">
    <div v-if="loginFrame">
      <!--选择企业-->
      <div class="select-company" v-show="dialogVisible">
        <div class="content">
          <p>选择经销商企业</p>
          <img
            src="../../assets/images/me_icon_eval.png"
            @click="closeDialog"
            class="close-dialog-btn"
            alt
          >
          <div class="select">
            <div class="business-tip">*如搜索不到你所属的经销商企业，请点击右侧的新增经销商企业按钮进行新增</div>
            <div class="select-item">
              <span class="left">经销商所属企业：</span>
              <el-select filterable clearable v-model="companyValue" placeholder="请选择">
                <el-option
                  v-for="item in company"
                  :key="item.id"
                  :label="item.compnayName"
                  :value="item.id"
                ></el-option>
              </el-select>
              <span class="right" @click="confirm('新增经销商企业')">新增经销商企业</span>
            </div>
          </div>
          <el-button type="primary" round size="mini" @click="confirm('确定')">确定</el-button>
        </div>
      </div>
      <!--选择企业-->

      <!--登录-->
      <img src="../../assets/images/icons/logo.png" alt class="logo">
      <div class="loginFrom" v-show="loginShow">
        <h1>登录</h1>
        <el-form class="demo-ruleForm userInput">
          <el-form-item prop="userName" style="margin-top: 5px">
            <el-input placeholder="请输入用户名" v-model.string="userName" name="userName"></el-input>
          </el-form-item>
          <el-form-item prop="checkPass">
            <el-input
              type="password"
              placeholder="请输入密码"
              v-model.string="password"
              name="password"
              @keyup.enter.native="sureLogin"
            ></el-input>
          </el-form-item>
          <el-form-item style="position: relative;display: none">
            <el-input placeholder="请输入验证码" v-model.string="verifyWord" class="verify-word"></el-input>
            <img src class="verify-img">
          </el-form-item>
          <el-form-item class="loginButton">
            <el-button type="primary" @click.keyup.enter.native="sureLogin">登录</el-button>
          </el-form-item>
          <p class="forgot" @click="goLogin">忘记密码</p>
        </el-form>
      </div>
      <!--登录-->

      <!--忘记密码-->
      <div class="loginFrom" v-show="!loginShow">
        <h1>忘记密码</h1>
        <el-form class="demo-ruleForm userInput">
          <el-form-item class="input-item">
            <el-input placeholder="请输入手机号" v-model.string="phoneName" @blur="VerifyPhone"></el-input>
            <p v-show="phoneTxt.show" class="phoneTxt">{{phoneTxt.text}}</p>
          </el-form-item>
          <el-form-item>
            <el-input
              type="password"
              placeholder="请输入验证码"
              v-model.string="phoneWord"
              class="verify-word"
            ></el-input>
            <div class="set-verify" @click="phoneSecurityCode">{{phoneVerify}}</div>
          </el-form-item>
          <el-form-item>
            <el-input
              type="password"
              placeholder="请输入新密码"
              v-model.string="newPassword"
              @blur="VerifyPwd"
            ></el-input>
            <p v-show="pwdTxt.show" class="pwdTxt">{{pwdTxt.text}}</p>
          </el-form-item>
          <el-form-item class="loginButton">
            <el-button type="primary" @click="setPassword">确定</el-button>
          </el-form-item>
          <p class="go-login">
            已有账号，点击
            <span @click="goLogin">登录</span>
          </p>
        </el-form>
      </div>
      <!--忘记密码-->
    </div>

    <!--增加经销商-->
    <div
      v-if="dialogAddDealerVisible"
      style="background:#fff; position:absolute; width: 100%; height: 100%; "
    >
      <div class="leftFrameMenus">
        <div class="sanduLogo">
          <b>
            <img src="../../assets/LOGO.png" alt style="width: 29px; height: 32px">
          </b>
          <span>商家管理后台</span>
        </div>
        <div>
          <img src="../../assets/images/menus1_02.jpg" alt>
        </div>
      </div>
      <div class="addDealers mbottom">
        <div class="company">
          <header>新增经销商企业</header>
          <p class="need">
            <span>*</span>为必填项
          </p>
          <el-row :gutter="24" class="splineFrame">
            <el-col :span="2" class="productBaseInfo">经销商企业信息</el-col>
            <el-col :span="22" class="rightLine">
              <hr>
            </el-col>
          </el-row>
        </div>
        <div class="formFrame">
          <el-form
            label-width="140px"
            class="demo-ruleForm"
            :model="dealerAddMsg"
            :rules="rules"
            ref="ruleForm"
          >
            <el-row :gutter="12">
              <el-col :span="6">
                <el-form-item label="经销商企业名称：" prop="companyName">
                  <el-input
                    maxlength="25"
                    v-model="dealerAddMsg.companyName"
                    placeholder="请输入经销商企业名称"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <!--<el-row :gutter="12">
                <el-col :span="6">
                    <el-form-item label="产品可见范围：" prop="visibilityRange">
                        &lt;!&ndash;<el-select v-model="visibleProduct" placeholder="请选择产品可见范围">
                        </el-select>&ndash;&gt;
                        <el-input v-model="dealerAddMsg.visibilityRange" @focus="showVisiRange" placeholder="请选择产品可见范围"> </el-input>
                        <el-row :gutter="24" class="goods-operating-infos">

                        </el-row>
                    </el-form-item>
                </el-col>
            </el-row>-->

            <el-row :gutter="12">
              <el-col :span="10">
                <el-form-item label="产品可见范围：" prop="textareaChangeNameList">
                  <!--<el-select v-model="visibleProduct" placeholder="请选择产品可见范围">
                  </el-select>-->
                  <div class="arrowpos">
                    <el-input
                      class="slh"
                      :disabled="userType ==3"
                      v-model="dealerAddMsg.textareaChangeNameList"
                      @focus="productCategoryTree"
                      placeholder="请选择产品可见范围"
                    ></el-input>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col style="position: relative" :span="16" class="brandName">
                <!--<i class="bitian">*</i>-->
                <el-form-item label="品牌：" prop="valBrand">
                  <el-button type="text" :disabled="userType ==3" @click="addBrandDialog">添加</el-button>
                  <div>
                    <p v-for="(item, index) in addedBrands" :key="index">
                      {{item.brandName}}
                      <span v-if="userType !=3" @click="delBrand(index,item)">X</span>
                    </p>
                    <!--<b style="color: red; font-weight: normal" v-if="!addedBrand.length">请选择品牌</b>-->
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="12">
              <el-col :span="6">
                <el-form-item label="联系人姓名：">
                  <el-input v-model="dealerAddMsg.contactName" placeholder="请输入联系人姓名"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="12">
              <el-col :span="6">
                <el-form-item label="联系人电话：">
                  <el-input
                    v-model="dealerAddMsg.phone"
                    oninput="value=value.replace(/[^\d{1,}\.\d{1,}]/g,'')"
                    placeholder="请输入联系人电话"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24" style="position: relative;">
              <!--<span class="bitian">*</span>-->
              <el-form-item label="区域：">
                <!-- ------------------------------------------------ -->
                <el-col :span="3">
                  <el-select
                    v-model="citySelect.provide"
                    @change="getProvideCode(citySelect.provide)"
                    clearable
                    placeholder="请选择省"
                  >
                    <el-option
                      v-for="(item, index) in provide"
                      :key="item.areaCode"
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
                      :key="item.areaCode"
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
                      :key="item.areaCode"
                      :label="item.areaName"
                      :value="item.areaCode"
                    ></el-option>
                  </el-select>
                </el-col>

                <!--<el-col :span="3">
                  <el-select clearable v-model="citySelect.origin" placeholder="请选择街道">
                    <el-option
                            v-for="(item,index) in originSelect"
                            :key="index"
                            :label="item.areaName"
                            :value="item.areaName">
                    </el-option>
                  </el-select>
                </el-col>-->
                <el-col :span="3" v-if="streetShow">
                  <el-select
                    clearable
                    v-model="citySelect.origin"
                    @change="getOriginCode(citySelect.origin)"
                    placeholder="请选择街道"
                  >
                    <el-option
                      v-for="(item,index) in originSelect"
                      :key="item.areaCode"
                      :label="item.areaName"
                      :value="item.areaCode"
                    ></el-option>
                  </el-select>
                </el-col>
                <el-col :span="3" v-else>
                  <span class="streetSel">不限</span>
                </el-col>
              </el-form-item>
            </el-row>

            <el-row :gutter="12">
              <el-col :span="6">
                <el-form-item label="详细地址：">
                  <el-input v-model="dealerAddMsg.companyAddress" placeholder="请输入详细地址"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="12">
              <el-col :span="12">
                <el-form-item label="经营范围：" prop="ranges">
                  <el-input v-model="dealerAddMsg.businessScope" placeholder="请输入经营范围"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="企业介绍：">
                  <el-input
                    type="textarea"
                    rows="5"
                    v-model="dealerAddMsg.companyDesc"
                    placeholder="限制0-500字符"
                    max-length="500"
                    class="textareaInfo"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div class="buttonFrame">
            <el-button round class="cancelBtnBg bg" @click="resetForm('ruleForm')">重 置</el-button>
            <el-button
              round
              type="primary"
              class="confirmBtn"
              @click="confirmAddDealer('ruleForm')"
            >保 存</el-button>
          </div>
        </div>
        <el-dialog
          title="选择产品可见范围"
          :visible.sync="infoClassifyVisible"
          class="info-ypop-up"
          width="540px"
        >
          <el-row>
            <el-col :span="24">选择分类：</el-col>
          </el-row>
          <el-row class="Classify-list">
            <el-col>
              <div class="clearfix">
                <div class="float width">
                  <div v-for="(item, index) in listOne" :key="index">
                    <el-checkbox-group
                      style="display: inline-block"
                      v-model="firstActiveArr[index]"
                      @change="oneTree(index)"
                    >
                      <el-checkbox :checked="firstActiveArr[index]" :value="item.categoryId"></el-checkbox>
                    </el-checkbox-group>

                    <span
                      v-model="checkListOne"
                      @click="oneTree(index)"
                      style="cursor: pointer;"
                    >{{item.categoryName}}</span>
                  </div>
                </div>

                <div class="float width">
                  <div v-for="(two, jdx) in listTwo" :key="jdx">
                    <el-checkbox-group
                      style="display: inline-block"
                      v-model="secondActiveArr[secondIndex][jdx]"
                      @change="twoTree(jdx)"
                    >
                      <el-checkbox
                        :checked="secondActiveArr[secondIndex][jdx]"
                        :value="two.categoryId"
                      ></el-checkbox>
                    </el-checkbox-group>
                    <span
                      v-model="checkListTwo"
                      style="cursor: pointer;"
                      @click="twoTree(jdx)"
                    >{{two.categoryName}}</span>
                  </div>
                </div>

                <div class="float width">
                  <div>
                    <el-checkbox-group
                      v-for="(three, kdx) in listThree"
                      :key="kdx"
                      @change="listThreeVal(kdx)"
                      v-model="threeActiveArr[secondIndex][threeIndex][kdx]"
                    >
                      <el-checkbox
                        :checked="threeActiveArr[secondIndex][threeIndex][kdx]"
                        :value="three.categoryId"
                      >{{three.categoryName}}</el-checkbox>
                    </el-checkbox-group>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
          <div slot="footer" class="dialog-footer">
            <el-button round type="primary" @click="verifyProductClass">确 定</el-button>
            <el-button round @click="infoClassifyVisible = false">取 消</el-button>
          </div>
        </el-dialog>

        <el-dialog title="添加品牌" :visible.sync="batchBrandDialogVisible" width="25%" top="30vh">
          <el-input placeholder="请输入内容" prefix-icon="el-icon-search" v-model="batchBrandSearch"></el-input>
          <el-checkbox-group v-model="addBrand.brandId">
            <div class="add-box">
              <el-checkbox style="display: none"></el-checkbox>
              <el-checkbox
                class="addItem"
                @change="brandShow(brand)"
                v-for="(brand, index) in brandlist"
                :label="brand.brandName"
                :key="brand.brandId"
              >{{brand.brandName}}</el-checkbox>
            </div>
          </el-checkbox-group>
          <span slot="footer" class="dialog-footer">
            <el-button round type="primary" @click="handleBatchAddConfirm">确 定</el-button>
            <el-button round type="info" @click="batchBrandDialogVisible = false">取 消</el-button>
          </span>
        </el-dialog>
      </div>
    </div>
    <!--增加经销商 结束-->
  </div>
</template>

<script>
import qs from "qs";
import commonMethods from "@/filters/minixs";
import md5 from "js-md5";

export default {
  inject: ["reload"],
  mixins: [commonMethods],
  data() {
    return {
      streetShow: true,
      dialogAddDealerVisible: false,
      company: [],
      companyValue: "",
      dialogVisible: false,
      loginFrame: true,
      disabled: false,
      password: "",
      userName: "",
      verifyWord: "",
      loginShow: true,
      phoneName: "",
      phoneWord: "",
      newPassword: "",
      companyId: "",
      phoneVerify: "获取验证码",
      phoneTxt: { show: false, text: "请输入手机号" },
      pwdTxt: { show: false, text: "请输入6~12位密码" },

      lastTree: [],
      brandlist: [],
      batchBrandDialogVisible: false,
      batchBrandSearch: "",
      addBrand: {
        brandId: []
      },
      tempBrand: [],
      addedBrand: [],
      addedBrands: [],
      indexOne: 0,
      listOne: [],
      listTwo: [],
      listThree: [],
      dealerAddMsg: {
        companyName: "", // 经销商经销商企业名称
        textareaChangeNameList: "", // 经销商企业可见产品范围id
        provinceCode: "", //  经销商企业省编码
        cityCode: "", // 经销商企业市编码
        areaCode: "", // 经销商企业区编码
        streetCode: "", // 经销商企业街道编码
        companyAddress: "", // 经销商企业详细地址
        companyDesc: "", // 经销商企业介绍
        phone: "", // 经销商企业联系人电话
        contactName: "", //经销商企业联系人姓名
        brandIds: "", // 经销商企业品牌
        businessScope: "" //经销商企业经营范围
      },
      infoClassifyVisible: false,
      options: [],
      dealerCategoryList: [],
      checkListOne: [],
      checkListTwo: [],
      checkListThree: [],

      checkCategoryListOne: [],
      selectTwoIndex: -1,

      checkCategoryListTwo: [],
      mycheckCategoryListTwo: [],
      mycheckCategoryListThree: [],

      selectThreeIndex: -1,
      checkCategoryListThree: [],
      userType: "",
      //区域选择
      search: {
        franchiserName: "",
        phone: "",
        contactName: "",
        provide: "",
        city: "",
        area: "",
        origin: ""
      },
      citySelect: {
        provide: "",
        city: "",
        area: "",
        origin: ""
      },
      cityCode: "",
      provide: [],
      provideSelect: [],
      citySelects: [],
      areaSelect: [],
      originSelect: [],
      ruleForm: {
        // 验证表单
        brandName: "",
        companyName: "",
        contactName: "",
        phone: "",
        companyAddress: "",
        area: "",
        textareaChangeNameList: ""
      },
      // 验证表单规则
      rules: {
        // provinceCodes: [
        //     //{required: true, message: '请选择区域', trigger: 'blur'},
        //     {validator: this.validAreas, trigger: 'blur'}
        // ],
        companyName: [
          { required: true, message: "请输入经销商名称", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 50 个字符", trigger: "blur" }
        ],
        brandName: [],
        // contactName: [
        //     {required: true, message: '请输入联系人名称', trigger: 'blur'},
        //     {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}
        // ],
        ranges: [
          {
            validator: this.txtRange,
            trigger: "blur"
          }
        ],
        // phone: [
        //     {required: true, message: '请输入电话号码', trigger: 'change'},
        //     {
        //         validator: this.validPhone,
        //         trigger: 'blur'
        //     }
        // ],
        // area: [
        //     {required: true, message: '请选择区域', trigger: 'change'}
        // ],
        valBrand: [
          { required: true, validator: this.valBrand, trigger: "change" }
        ],
        // companyAddress: [
        //     {required: true, message: '请输入详细地址', trigger: 'blur'}
        // ],
        textareaChangeNameList: [
          { required: true, message: "请选择产品可见范围", trigger: "blur" }
        ]
      },
      checkAll: false,
      firstIndex: 0,
      secondIndex: 0,
      threeIndex: 0,
      firstActiveArr: [],
      secondActiveArr: [],
      threeActiveArr: [],
      textareaChangeList: "",
      // textareaChangeNameList:'',
      nowNameArr: [],
      sw: 0
    };
  },
  created() {},
  mounted() {
    let self = this;
    window.onpopstate = function() {
      //监听浏览器事件
      self.$router.push("/");
      self.dialogAddDealerVisible = false;
    };
  },
  destroyed() {
    window.location.reload();
  },
  methods: {
    closeDialog() {
      this.dialogVisible = false;
    },
    txtRange(rule, value, callback) {
      if (this.dealerAddMsg.businessScope.length > 50) {
        callback(new Error("经营范围在50字以内"));
      } else {
        callback();
      }
    },
    valBrand(rule, value, callback) {
      if (this.addedBrands.length == 0) {
        callback(new Error("请添加品牌"));
      } else {
        callback();
      }
    },
    confirm(type) {
      if (type == "确定") {
        if (this.companyValue == "") {
          this.$message.error("请选择经销商企业");
          return;
        }
        this.API.postCompanyConfirm(
          "franchiserId=" +
            this.companyValue +
            "&id=" +
            Number(qs.parse(sessionStorage.getItem("loginUser")).id)
        ).then(res => {
          if (res.success) {
            this.dialogVisible = false;
            this.login();
          }
        });
      }
      if (type == "新增经销商企业") {
        /*this.$router.push('/business/loginDealerAdd');*/
        window.history.pushState(null, null, "");
        this.dialogVisible = false;
        this.dialogAddDealerVisible = true;
      }
    },
    // 登录和忘记密码页面切换
    goLogin() {
      this.loginShow = !this.loginShow;
      this.phoneName = "";
      this.phoneWord = "";
      this.newPassword = "";
      this.phoneTxt.show = false;
      this.pwdTxt.show = false;
      this.phoneTxt.text = "请输入手机号";
    },
    // 保存修改
    setPassword() {
      if (
        this.phoneTxt.show ||
        this.pwdTxt.show ||
        this.phoneWord.length === 0
      ) {
        if (
          !this.phoneTxt.show &&
          !this.pwdTxt.show &&
          this.phoneWord.length === 0
        ) {
          this.$message({
            type: "error",
            message: "请输入验证码",
            duration: 1500
          });
        }
        return;
      }
      const result = md5("WeB" + this.newPassword);
      this.API.setPassword({
        phoneCode: this.phoneWord,
        password: md5(result),
        mobile: this.phoneName
      }).then(res => {
        if (res) {
          this.$message.error(res.msgId);
        }
      });
    },
    // 忘记密码获取验证码
    phoneSecurityCode() {
      if (this.phoneName.length === 0) {
        this.phoneTxt.show = true;
        this.phoneTxt.text = "请输入手机号";
        return;
      }
      if (!this.disabled) {
        return;
      }
      let i = 60;
      if (this.sw == 0) {
        this.sw = 1;
        this.API.phoneSecurityCode({
          phoneNumber: this.phoneName,
          functionType: 1
        }).then(res => {
          if (res.success) {
            let t = setInterval(() => {
              i--;
              this.phoneVerify = i + "s";
              this.disabled = false;
              if (i === 0) {
                clearInterval(t);
                this.phoneVerify = "获取验证码";
                this.disabled = true;
                this.sw = 0;
              }
            }, 1000);
          } else {
            this.sw = 0;
            this.$message.error(res.message);
          }
        });
      }
    },
    isLogin() {
      if (sessionStorage.getItem("loginUser")) {
        this.$message.success("你已经登录");
        this.$router.push("/index");
      } else {
        this.$router.push("/");
      }
    },
    // 验证手机格式
    VerifyPhone() {
      if (this.phoneName.length === 0) {
        this.phoneTxt.show = true;
        this.phoneTxt.text = "请输入手机号";
        return;
      }
      let mobile = new RegExp(
        "^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$"
      );
      if (!mobile.test(this.phoneName)) {
        this.phoneTxt.show = true;
        this.disabled = false;
        this.phoneTxt.text = "请输入正确的手机号";
      } else {
        this.phoneTxt.show = false;
        this.disabled = true;
      }
    },
    // 验证密码
    VerifyPwd() {
      let mobile = new RegExp("^[\\w]{6,12}$");
      if (!mobile.test(this.newPassword)) {
        this.pwdTxt.show = true;
        this.pwdTxt.text = "请输入6~12位密码";
      } else {
        this.pwdTxt.show = false;
      }
    },
    loginout() {
      let loginUser = sessionStorage.getItem("loginUser");
      let userMsg = qs.parse(loginUser);
      this.API.loginOut({
        Authorization: userMsg.token
      }).then(res => {
        this.$router.replace("/");
        sessionStorage.setItem("loginUser", "");
        loginUser = "";
      });
    },
    sureLogin() {
      if (this.userName === "" || this.userName === null) {
        this.$message({
          type: "error",
          message: "用户名不能为空",
          duration: 1500
        });
      } else if (this.password === "" || this.password === null) {
        this.$message({
          type: "error",
          message: "密码不能为空",
          duration: 1500
        });
      } else {
        this.tipmsg = false;
        this.login();
      }
    },
    /* 增加经销商*/

    /*选择区域校验*/
    validAreas(rule, value, callback) {
      if (
        this.citySelect.provide.length == 0 ||
        this.citySelect.city.length == 0 ||
        this.citySelect.area.length == 0 ||
        (this.originSelect.length > 0 && this.citySelect.origin.length == 0)
      ) {
        callback(new Error("请选择完整的区域"));
      } else {
        callback();
      }
    },
    /*电话号码验证*/
    validPhone(rule, value, callback) {
      let MobileRegex = /^1[3456789]\d{9}$/;
      let phoneRegex = /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/;
      if (phoneRegex.test(value) || MobileRegex.test(value)) {
        callback();
      } else {
        callback(new Error("手机号码格式不正确！"));
      }
    },
    /* oneTree(index) {
            // console.log(this.listOne)
            this.indexOne=index;
            this.listTwo = this.listOne[index].list;
            this.listThree = []
        },
        twoTree(index) {
            this.listThree = this.listOne[this.indexOne].list[index].list;
        },*/
    /* gotoPrev(){
             this.$router.push('/business/dealerBusiness')
         },*/
    addBrandDialog() {
      this.getBrandList();
      this.batchBrandDialogVisible = true;
    },
    getDefaultShow() {
      this.API.defaultShow({
        companyId: sessionStorage.getItem("companyID"),
        msgId: "1"
      }).then(res => {
        if (res) {
          this.dealerAddMsg.brandIds = res.obj.brandIds;
          this.textareaChangeList = res.obj.productVisibilityRange;
          let categoryName = [];
          if (res.obj.productVisibilityRangeNameList) {
            res.obj.productVisibilityRangeNameList.map(item => {
              categoryName.push(item.categoryName);
            });
          }

          this.dealerAddMsg.textareaChangeNameList = categoryName.join(",");
          this.addedBrand = res.obj.brandList;
          this.addedBrands = res.obj.brandList;
        }
      });
    },
    getDealerUserType() {
      console.log(qs.parse(sessionStorage.getItem("loginUser")).id);
      this.API.dealerUserType({
        id: qs.parse(sessionStorage.getItem("loginUser")).id
      }).then(res => {
        this.userType = res.obj.userType;
      });
    },
    handleBatchAddConfirm() {
      /*this.batchBrandDialogVisible = false
            let arr = [...new Set(this.tempBrand)]
            this.addedBrand = arr

            let arrIDS = []
            this.addedBrand.map((item) => {
                arrIDS.push(item.brandId)
                this.dealerAddMsg.brandIds = arrIDS.join(',')
            })*/

      // addBrand.brandId
      console.log(this.addedBrand);
      this.batchBrandDialogVisible = false;
      // let arr = [...new Set(this.tempBrand)];
      let arr = [];
      console.log(this.tempBrand, this.addBrand);
      for (let i = 0; i < this.addBrand.brandId.length; i++) {
        for (let j = 0; j < this.brandlist.length; j++) {
          if (this.addBrand.brandId[i] == this.brandlist[j].brandId) {
            arr.push(this.brandlist[j]);
          }
        }
      }
      this.addedBrands = arr;
      let arrIDS = [];
      this.addedBrands.map(item => {
        arrIDS.push(item.brandId);
        this.dealerAddMsg.brandIds = arrIDS.join(",");
      });
    },
    brandShow(brand) {
      this.tempBrand = [];
      if (brand) {
        this.tempBrand.push(brand);
      }
    },
    delBrand(index, item) {
      this.addedBrands.splice(index, 1);
      let arrs = [];
      this.addedBrands.map(item => {
        console.log(item.brandId);
        arrs.push(item.brandId);
      });
      this.dealerAddMsg.brandIds = arrs.join(",");
    },

    verifyProductClass() {
      let arr = [],
        newArr = [],
        newArrName = [];
      this.listOne.forEach((valueP, indexP) => {
        valueP.list.forEach((valueC, indexC) => {
          valueC.list.forEach((val, index) => {
            if (this.threeActiveArr[indexP][indexC][index]) {
              arr.push(val);
            }
          });
        });
      });

      if (arr.length === 0) {
        return;
      } else {
        this.infoClassifyVisible = false;
      }
      arr.forEach(value => {
        console.log(value, "cc");
        newArr.push(value.categoryId);
        newArrName.push(value.categoryName);
      });
      // console.log(newArrName.join(','), 'name sss')
      this.textareaChangeList = newArr.join(",");
      this.dealerAddMsg.textareaChangeNameList = newArrName.join(",");
    },
    productCategoryTree() {
      this.infoClassifyVisible = true;
      this.API.dealerCategory({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        console.log(res, "categorys company");
        this.listOne = res.datalist;
        let arr1 = [],
          arr2 = [],
          arr3 = [];
        this.listOne.forEach((value, index) => {
          arr1.push(false);
          arr2.push([]);
          arr3.push([]);
          value.list.forEach((val, count) => {
            arr3[index].push([]);
            val.list.forEach(valC => {
              arr3[index][count].push(false);
            });
            arr2[index].push(false);
          });
        });

        this.firstActiveArr = arr1;
        this.secondActiveArr = arr2;
        this.threeActiveArr = arr3;
      });
    },
    oneTree(index) {
      // this.indexOne=index;
      this.secondIndex = index;
      this.listTwo = this.listOne[index].list;
      if (this.firstActiveArr[index]) {
        this.secondActiveArr[this.secondIndex].forEach((value, index) => {
          this.secondActiveArr[this.secondIndex][index] = true;
        });
        this.threeActiveArr[this.secondIndex].forEach(value => {
          value.forEach((val, index) => {
            value[index] = true;
          });
        });
        this.threeActiveArr = this.threeActiveArr.concat();
        this.secondActiveArr = this.secondActiveArr.concat();
      } else {
        this.secondActiveArr[this.secondIndex].forEach((value, index) => {
          this.secondActiveArr[this.secondIndex][index] = false;
        });
        this.threeActiveArr[this.secondIndex].forEach(value => {
          value.forEach((val, index) => {
            value[index] = false;
          });
        });
        this.threeActiveArr = this.threeActiveArr.concat();
        this.secondActiveArr = this.secondActiveArr.concat();
      }
    },
    twoTree(index) {
      this.listThree = this.listOne[this.secondIndex].list[index].list;
      this.threeIndex = index;
      if (this.secondActiveArr[this.secondIndex][index]) {
        this.threeActiveArr[this.secondIndex][this.threeIndex].forEach(
          (value, index) => {
            this.threeActiveArr[this.secondIndex][this.threeIndex][
              index
            ] = true;
          }
        );
        this.threeActiveArr = this.threeActiveArr.concat();
      } else {
        this.threeActiveArr[this.secondIndex][this.threeIndex].forEach(
          (value, index) => {
            this.threeActiveArr[this.secondIndex][this.threeIndex][
              index
            ] = false;
          }
        );
        this.threeActiveArr = this.threeActiveArr.concat();
      }
    },
    listThreeVal(val) {
      console.log(val, "threeActiveArr");
      let arrs = [];
      if (this.threeActiveArr[val]) {
        console.log(this.threeActiveArr[val], "valsssssssssssss");
      }
      this.threeActiveArr.map((item, index) => {
        console.log(item.categoryName);
      });
    },
    resetForm(formName) {
      this.getDefaultShow();
      this.citySelect.provide = "";
      this.citySelect.city = "";
      this.citySelect.area = "";
      this.citySelect.origin = "";
      this.dealerAddMsg = {};
      //this.addedBrand = []
      this.$refs[formName].resetFields();
    },
    getBrandList() {
      this.API.baseBrand({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        console.log(res, "brands");
        this.brandlist = res.datalist;
      });
    },
    /*-----------------------区域选择------------------------------*/
    getArea(code, type) {
      this.API.areaList({ areaCode: code }).then(res => {
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
              // this.originSelect = [{areaCode: '', areaName: '不限'}]
            } else {
              this.streetShow = true;
            }
          }
        }
      });
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
    /*-----------------------区域选择结束-------------------------*/
    // 验证请求参数是非为空字符串，空返回undefined,非空返回原字符串
    isUndefined(string) {
      if (string.length > 0) {
        return string;
      } else {
        return undefined;
      }
    },
    // 添加经销商
    confirmAddDealer(formNames) {
      let addParams = {
        companyId: sessionStorage.getItem("companyID"),
        companyName: this.dealerAddMsg.companyName, // 经销商经销商企业名称
        productVisibilityRange: this.textareaChangeList, // 经销商企业可见产品范围id
        provinceCode: this.citySelect.provide, //  经销商企业省编码
        cityCode: this.citySelect.city, // 经销商企业市编码
        areaCode: this.citySelect.area, // 经销商企业区编码
        streetCode: this.citySelect.origin, // 经销商企业街道编码
        companyAddress: this.dealerAddMsg.companyAddress, // 经销商企业详细地址
        companyDesc: this.dealerAddMsg.companyDesc, // 经销商企业介绍
        phone: this.dealerAddMsg.phone, // 经销商企业联系人电话
        contactName: this.dealerAddMsg.contactName, //经销商企业联系人姓名
        brandIds: this.dealerAddMsg.brandIds, // 经销商企业品牌
        businessScope: this.dealerAddMsg.businessScope //经销商企业经营范围
      };
      this.$refs[formNames].validate(valid => {
        if (valid) {
          this.$confirm("确定新增经销商企业吗?", "提示", {
            confirmButtonText: "确定",
            type: "info",
            cancelButtonClass: "cancelButtonClass",
            confirmButtonClass: "confirmButtonClass",
            center: true
          }).then(() => {
            this.API.dealerAdd(addParams).then(res => {
              console.log(res, "dealer res");
              if (!res.success) {
                this.$message.error(res.message);
                return;
              }
              this.$message.success("添加成功");
              this.API.postCompanyConfirm(
                "franchiserId=" +
                  res.obj +
                  "&id=" +
                  Number(qs.parse(sessionStorage.getItem("loginUser")).id)
              ).then(res => {
                if (res.success) {
                  this.login();
                }
              });
              // this.$router.push('/')
            });
          });
        }
      });
    }
    /* 增加经销商 结束*/
  }
};
</script>
<style lang="scss" scoped>
@import "../../assets/css/mixin";
.streetSel {
  display: inline-block;
  width: 100px;
  height: 38px;
  border: solid 1px #ddd;
  position: relative;
  border-radius: 4px;
  padding-left: 15px;
  &:after {
    content: "";
    position: absolute;
    border-bottom: solid 1px #ddd;
    border-right: solid 1px #ddd;
    -webkit-transform: rotate(45deg);
    width: 8px;
    height: 8px;
    right: 10px;
    top: 12px;
  }
}
.close-dialog-btn {
  width: 25px;
  height: 25px;
  position: absolute;
  right: 10px;
  top: 10px;
}
.arrowpos {
  position: relative;
  width: 321px;
  &:after {
    position: absolute;
    content: "";
    top: 20px;
    right: -20px;
    width: 0;
    height: 0;
    border: 6px solid transparent;
    border-top-color: #999; //左箭头
  }
}
.slh {
  border: solid 1px #ddd;
  height: 40px;
  border-radius: 4px;
  padding: 0 15px;
  width: 321px; /*必须设置宽度*/
  overflow: hidden; /*溢出隐藏*/
  text-overflow: ellipsis; /*以省略号...显示*/
  white-space: nowrap; /*强制不换行*/
  position: relative;
}
.addDealers {
  margin-left: 245px;
  margin-top: 25px;
}
.leftFrameMenus {
  background: #363636;
  position: absolute;
  width: 238px;
  height: 100%;
}

.sanduLogo {
  background: #ff6419;
  text-align: center;
  width: 238px;
  height: 45px;
  padding-top: 15px;
  font-size: 22px;
  color: #fff;
  b {
    display: inline-block;
    vertical-align: -10px;
  }
  span {
    display: inline-block;
    vertical-align: middle;
  }
  img {
    width: 30px;
    margin-right: 10px;
  }
}
.loginFrame {
  background: url("../../assets/images/loginbg.png") center center no-repeat;
  background-size: 100%;
  height: 100%;
  .select-company {
    position: fixed;
    z-index: 99;
    width: 100%;
    height: 100%;
    background: rgba(69, 69, 69, 0.2);
    .el-button {
      margin-left: 450px;
      margin-top: 10px;
    }
    .content {
      position: relative;
      width: 540px;
      height: 256px;
      background-color: #ffffff;
      margin: 300px auto;
      p {
        background-color: #fafafa;
        color: #000000;
        padding: 14px;
      }
      .select {
        height: 160px;
        border-bottom: 1px solid #e8e8e8;
        overflow: hidden;
        .select-item {
          // margin-top: 50px;
          height: 40px;
          display: flex;
          justify-content: center;
          .left {
            line-height: 40px;
          }
          .right {
            display: inline-block;
            width: 125px;
            height: 40px;
            margin-left: 10px;
            line-height: 40px;
            text-align: center;
            background-color: #ffdbc9;
            border-radius: 4px;
            color: #ff6419;
            cursor: pointer;
          }
        }
      }
    }
  }
  .logo {
    position: absolute;
    left: 50%;
    top: 22%;
    transform: translate(-50%, -50%);
  }
  .loginFrom {
    background: #fff;
    width: 400px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    box-sizing: padding-box;
    h1 {
      text-align: center;
      font-size: 20px;
      padding: 40px 0 36px 0;
      border-bottom: solid 1px #eee;
    }
    .valCode {
      .el-col:first-of-type {
        width: 200px;
      }
      .el-col:nth-of-type(2) {
        background: #e0e0e0;
        width: 130px;
        border-radius: 4px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        color: #666666;
      }
    }
    .loginButton {
      text-align: center;
      width: 100%;
      .el-button {
        width: 100%;
        background: #454545;
        border: none;
        height: 42px;
        margin-top: 10px;
      }
    }
    .userInput {
      padding: 40px;
      position: relative;
      p {
        margin-top: 10px;
        line-height: 14px;
        font-size: 14px;
        color: #ff6419;
        text-align: center;
        z-index: 100;
      }
      .pwdTxt {
        position: absolute;
        top: 35px;
        width: 100%;
      }
      .phoneTxt {
        position: absolute;
        top: 35px;
        width: 100%;
      }
      .verify-word {
        width: 160px;
        height: 40px;
      }
      .go-login {
        margin-top: 23px;
        font-size: 14px;
        color: #666666;
        text-align: right;
        span {
          color: #ff6419;
          cursor: pointer;
        }
      }
      .verify-img {
        position: absolute;
        right: 0;
        width: 142px;
        height: 40px;
        vertical-align: middle;
      }
      .set-verify {
        cursor: pointer;
        position: absolute;
        right: 0;
        display: inline-block;
        background-color: #ff6419;
        text-align: center;
        color: #fff;
        border-radius: 5px;
        width: 142px;
        height: 40px;
        vertical-align: middle;
      }
    }
  }
  .forgot {
    text-align: center;
    color: #666;
    font-size: 12px;
    cursor: pointer;
  }
}
.tipmsg {
  color: red;
  position: absolute;
  height: 20px;
  font-size: 12px;
  left: 40px;
  top: 10px;
}

/*增加经销商样式 */
.bitian {
  position: absolute;
  color: red;
  left: 80px;
  top: 13px;
}
.business-tip {
  color: red;
  font-size: 14px;
  padding: 15px;
  margin-bottom: 30px;
  padding-left: 35px;
}
.add-box {
  margin-top: 20px;
  height: 200px;
  padding-left: 15px;
  padding-top: 10px;
  border: solid 1px #ddd;
  overflow-y: scroll;
}

.add-box-line {
  display: block;
}
.addItem {
  display: block;
  margin-left: 0;
}
.float {
  float: left;
}
.width {
  width: 30%;
  padding-top: 15px;
  padding-left: 15px;
  border-right: solid #ddd 1px;
  &:first-of-type {
    padding-left: 0;
  }
  &:last-of-type {
    border-right: none;
  }
}
.goods-operating-info {
  margin-bottom: 20px;
  .el-col-2 {
    font-size: 14px;
    width: 110px;
    text-align: right;
    line-height: 40px;
    margin-left: 20px;
  }
  .el-col-22 {
    line-height: 40px;
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
  }
  .el-col-24 {
    padding: 5px 0 5px 20px;
    height: 40px;
    line-height: 30px;
    h2 {
      display: inline-block;
      font-size: 14px;
      width: 110px;
      text-align: right;
    }
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
    span {
      color: #ff6419;
      margin-left: 10px;
      cursor: pointer;
    }
  }
}
.goods-operating-infos {
  .el-col-2 {
    font-size: 14px;
    width: 110px;
    text-align: right;
    line-height: 40px;
    margin-left: 20px;
  }
  .el-col-22 {
    line-height: 40px;
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
  }
  .el-col-24 {
    padding: 5px 0 5px 20px;
    height: 40px;
    line-height: 30px;
    h2 {
      display: inline-block;
      font-size: 14px;
      width: 110px;
      text-align: right;
    }
    p {
      display: inline-block;
      padding: 0 12px 0 12px;
      height: 12px;
      line-height: 12px;
      color: #333333;
      border-right: 1px solid #dddddd;
    }
    span {
      color: #ff6419;
      margin-left: 10px;
      cursor: pointer;
    }
  }
}

.Classify-list {
  border-top: 1px solid #dddddd;
  border-right: 1px solid #dddddd;
  .el-col-6 {
    overflow: auto;
    height: 360px;
    border-bottom: 1px solid #dddddd;
    border-left: 1px solid #dddddd;
    .el-checkbox {
      margin: 0;
      padding: 10px 20px;
      height: 12px;
      line-height: 12px;
      color: #000000;
    }
    p {
      padding: 10px 20px;
      height: 12px;
      line-height: 12px;
      cursor: pointer;
      color: #000000;
      &:hover {
        color: #ff6419;
      }
    }
    .p-active {
      color: #ff6419;
    }
  }
  .el-col-3,
  .el-col-4,
  .el-col-5 {
    overflow: auto;
    height: 360px;
    border-bottom: 1px solid #dddddd;
    border-left: 1px solid #dddddd;
    p {
      padding: 10px 20px;
      height: 12px;
      line-height: 12px;
      display: flex;
      justify-content: space-between;
      cursor: pointer;
      color: #000000;
      &:hover {
        color: #ff6419;
      }
    }
    .p-active {
      color: #ff6419;
    }
  }
}

.specification-info {
  .SKU-table-header {
    padding: 0;
    height: 30px;
  }
  .el-checkbox-group {
    display: inline-block;
    margin-left: 5px;
  }
  .el-col-24 {
    border-top: 1px solid #dddddd;
    height: 74px;
    line-height: 74px;
    h2 {
      display: inline-block;
      font-size: 14px;
      width: 110px;
      text-align: right;
    }
    .el-radio {
      margin-left: 20px;
    }
  }
  .el-col-2 {
    margin-bottom: 20px;
    font-size: 14px;
    width: 110px;
    text-align: right;
  }
  .el-col-21,
  .el-col-20 {
    padding-left: 10px;
    .el-checkbox-group {
      .el-checkbox {
        width: 180px;
        margin: 0 0 20px 0;
      }
    }
    .el-table {
      margin: 0 0 20px 0;
      span {
        color: #ff6419;
        cursor: pointer;
      }
    }
  }
}

.company {
  background: #fff;
  padding: 0 20px;
  header {
    border-bottom: solid 1px #ddd;
    padding-bottom: 15px;
    font-size: 18px;
    font-weight: bold;
    color: #666;
  }
  .need {
    margin-top: 20px;
    span {
      color: red;
    }
  }
}
.formFrame {
  padding: 0 40px;
  background: #fff;
  .el-form-item {
    margin-bottom: 18px;
  }
}
.bg {
  background-color: #454545;
  color: #fff;
}
.buttonFrame {
  background: #fff;
  padding-bottom: 40px;
  border-top: solid 1px #ddd;
  padding-top: 40px;
  padding-left: 120px;
}
.reBack {
  margin-bottom: 15px;
  b {
    cursor: pointer;
    color: #ff6419;
  }
}
.brandName {
  p {
    display: inline-block;
    height: 28px;
    line-height: 28px;
    padding: 0 25px;
    border-radius: 4px;
    border: solid 1px #ddd;
    background: #f5f5f5;
    position: relative;
    margin-right: 15px;
    color: #333;
    span {
      position: absolute;
      background: #fff;
      border: solid 1px #ddd;
      right: 4px;
      top: 5px;
      border-radius: 8px;
      width: 16px;
      height: 16px;
      font-size: 10px;
      text-align: center;
      line-height: 16px;
      color: #999;
      font-family: "Comic Sans MS";
      cursor: pointer;
      &:hover {
        background: #ff6419;
        color: #fff;
      }
    }
  }
}
</style>

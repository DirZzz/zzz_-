<template>
  <transition name="fade">
      <div>
          <v-nav :nav="nav"></v-nav>
          <div class="addProduct">
              <header>
                  <h1>编辑方案</h1>
              </header>
              <p><span>*</span>为必填项</p>
              <!--智能子方案-->
              <ul class="subPlanName"  v-show="planNameCombo != null && planNameCombo.length > 1">
                  <li v-for="(item,index) in planNameCombo" :key="index" >
                      <span :class="{'active': item.active}" @click="getDetialMessage(item,index)">{{item.spaceTypeName}} {{index + 1}} <b v-if="item.isPrimary == 0" class="mainPlan">主</b></span>
                  </li>
              </ul>
              <el-tabs v-model="activeName" @tab-click="handleClick">
                  <el-tab-pane label="基础信息" name="first" style="font-size: 14px">
                      <div>
                          <el-row :gutter="24">
                              <el-col :span="15">
                                  <el-form :model="planInfo" :rules="rules" ref="planInfo" label-width="100px"
                                           class="demo-ruleForm">
                                      <el-row>
                                          <el-col :span="11">
                                              <el-form-item label="方案名称：" prop="planName">
                                                  <el-input v-model="planInfo.planName" placeholder="请输入方案名称"></el-input>
                                              </el-form-item>
                                          </el-col>
                                          <el-col :span="11">
                                              <el-form-item label="方案ID：">
                                                  {{planInfo.planId}}
                                              </el-form-item>
                                          </el-col>
                                      </el-row>
                                      <el-row>
                                          <el-col :span="11">
                                              <el-form-item label="来源：" prop="productNameForm">
                                                  <span v-if="planInfo.origin === 'deliver'">企业交付</span>
                                                  <span v-else-if="planInfo.origin === 'share'">分享方案</span>
                                                  <span v-else>内部制作</span>
                                              </el-form-item>
                                          </el-col>
                                          <el-col :span="11">
                                              <el-form-item label="完成时间：">
                                                  {{planInfo.completeDate}}
                                              </el-form-item>
                                          </el-col>
                                      </el-row>
                                      <el-row>
                                          <el-col :span="11">
                                              <el-form-item label="品牌：">
                                                  <el-select v-model="planInfo.brandId" clearable style="width:100%" multiple
                                                             placeholder="请选择">
                                                      <el-option
                                                              v-for="item in brands"
                                                              :key="item.id"
                                                              :label="item.name"
                                                              :value="item.id">
                                                      </el-option>
                                                  </el-select>
                                              </el-form-item>
                                          </el-col>
                                          <el-col :span="11">
                                              <el-form-item label="设计师：">
                                                  {{planInfo.designer}}
                                              </el-form-item>
                                          </el-col>
                                      </el-row>
                                      <el-row>
                                          <el-col :span="11">
                                              <el-form-item label="方案类型：">
                                                  智能方案
                                              </el-form-item>
                                          </el-col>
                                          <el-col :span="10">
                                              <el-form-item label="风格：" prop="designStyleId">
                                                  <el-select v-model="planInfo.designStyleId" clearable class="selectWidth"
                                                             placeholder="请选择">
                                                      <el-option
                                                              v-for="item in styles"
                                                              :key="item.id"
                                                              :label="item.name"
                                                              :value="item.id">
                                                      </el-option>
                                                  </el-select>
                                              </el-form-item>
                                          </el-col>
                                      </el-row>

                                      <el-row>
                                          <el-col :span="10">
                                              <el-form-item label="适用面积：">
                                                  <el-select v-model="planInfo.applySpaceAreas" clearable multiple class="selectWidth"
                                                             placeholder="请选择适用面积">
                                                      <el-option
                                                              v-for="item in applyAreasList"
                                                              :key="item.id"
                                                              :label="item.name"
                                                              :value="item.id">
                                                      </el-option>
                                                  </el-select>
                                              </el-form-item>
                                          </el-col>
                                      </el-row>
                                      <el-row :gutter="24"  v-if="planInfo.isPrimary == 0  || planInfo.isPrimary == 2 ">
                                          <el-col :span="24" v-if="!detailPlanType">
                                              <el-form-item label="装修报价：" class="baojia">
                                                          <el-checkbox-group v-model="checkList"  @change="checkFun">
                                                              <div v-for="(item, index) in decoreArr" :key="index">
                                                                  <el-checkbox :label="item.name" @change="chg(index,item.active,item.name)"> </el-checkbox>
                                                                  <el-popover
                                                                          placement="bottom"
                                                                          trigger="hover"
                                                                  >
                                                              <div ref="reference" v-if="index == 0">人工费+管理费</div>
                                                              <div ref="reference" v-if="index == 1">辅材+人工费+管理费</div>
                                                              <div ref="reference" v-if="index == 2">主材+辅材+人工费+管理费</div>
                                                              <div ref="reference" v-if="index == 3">主材+辅材+人工费+管理费+部分软装</div>
                                                              <b slot="reference" style="cursor: pointer">?</b><i></i>
                                                          </el-popover>
                                                          <span style="font-size: 14px">：</span>
                                                          <input type="number" v-model="item.decoratePrice" placeholder="请输入" class="inputdemo" :class="{'disable':item.active}" :disabled="item.active">
                                                          <!--<el-input :disabled="item.active"-->
                                                                    <!--min="1"-->
                                                                    <!--maxlength="4"-->
                                                                    <!--type="number"-->
                                                                    <!--v-model="item.decoratePrice"-->
                                                                    <!--placeholder="请输入"-->
                                                                   <!--&gt; </el-input>-->
                                                          <span>&emsp; 元/平米（㎡）</span>
                                                      </div>
                                                  </el-checkbox-group>

                                              </el-form-item>
                                          </el-col>
                                      </el-row>
                                      <el-row :gutter="11">
                                          <el-col :span="21">
                                              <el-form-item label="方案描述：" prop="planDesc">
                                                  <el-input style="width: 100%"
                                                            class="textareaWidth"
                                                            type="textarea"
                                                            :rows="6"
                                                            maxlength="200"
                                                            placeholder="请输入内容"
                                                            v-model="planInfo.planDesc">
                                                  </el-input>
                                              </el-form-item>
                                          </el-col>
                                      </el-row>
                                      <el-row :gutter="11">
                                          <el-col :span="21">
                                              <el-form-item label="备注：">
                                                  <el-input
                                                          style="width: 100%"
                                                          class="textareaWidth"
                                                          type="textarea"
                                                          :rows="6"
                                                          placeholder="请输入内容"
                                                          v-model="planInfo.remark">
                                                  </el-input>
                                              </el-form-item>
                                          </el-col>
                                      </el-row>
                                      <el-form-item>
                                          <el-button type="primary" round class="confirmBtn" @click="submitForm('planInfo')">
                                              确定
                                          </el-button>
                                          <el-button @click="resetForm()" round class="cancelBtnBg">取消</el-button>
                                      </el-form-item>
                                  </el-form>
                              </el-col>
                              <el-col :span="9">
                                  <!--<el-upload-->
                                  <!--:action="BASE_URL.uploadUrl"-->
                                  <!--:show-file-list="false"-->
                                  <!--:data="uploadData"-->
                                  <!--:on-success="handleImageSuccess"-->
                                  <!--&gt;-->
                                  <img width="500" height="300" :src="planInfo.picPath">
                                  <!--BASE_URL.sourceBaseUrl+planInfo.picPath-->
                                  <!--<div>-->
                                  <!--<br>-->
                                  <!--<el-button type="primary" round class="confirmBtn">重新上传</el-button>-->
                                  <!--</div>-->
                                  <!--</el-upload>-->

                                  <br/>
                              </el-col>
                          </el-row>
                      </div>
                  </el-tab-pane>
                  <el-tab-pane label="设计理念" name="second">
                      <vue-editor useCustomImageHandler :editorOptions="editorSettings" v-model="textarea3" class="editor"
                                  @image-added="handleImageAdded"></vue-editor>
                      <div v-show="false" v-html="textarea3" ref="editorContent"></div>
                      <div class="footerBtn" style="margin-top: 20px">
                          <el-button round class="confirmBtn" type="primary" size="small" @click="saveContent">保存</el-button>
                          <el-button round class="confirmBtn"  size="small" @click="$router.go(-1)">取消</el-button>
                      </div>
                  </el-tab-pane>
              </el-tabs>

          </div>
      </div>
  </transition>
</template>

<script>
    // import {VueEditor, Quill} from "vue2-editor";
    import VueEditor from '@/components/vueEditor/vueEditor'
    import Quill from "quill";
    import {ImageDrop} from 'quill-image-drop-module'
    import ImageResize from 'quill-image-resize-module'
    import {container, ImageExtend, QuillWatch} from 'quill-image-extend-module'

    Quill.register('modules/imageDrop', ImageDrop)
    Quill.register('modules/imageResize', ImageResize)
    Quill.register('modules/ImageExtend', ImageExtend)
  export default {
      components: {
          VueEditor
      },
      data() {
          return {
              applyAreasList:[],
              editorSettings: {
                  modules: {
                      imageDrop: false,
                      imageResize: {},
                      ImageExtend:{
                          loading: false,
                          name:'file',
                          change: (xhr, formData) => {
                              xhr.setRequestHeader('Authorization',sessionStorage.getItem('token'));
                              formData.append("type", "image");
                              formData.append("platform", "web");
                              formData.append("module", "product");
                              formData.append("fileKey", "company.shop.casePic");
                          },
                          action: 'upload-error',   //拖拽功能 去掉
                          start: (value) => {

                          },
                          response: (res) =>{
                              return res.data.url
                          }
                      }
                  }
              },
              textarea3: "",
              nav: [
                  { nav: "方案库", url: `/plan` },
                  { nav: "方案编辑", url: "" }
              ],
              // 装修报价
              baojia:{
                  water:'',
                  half:'',
                  soft:'',
                  whole:'',
              },
              decoratePriceInfoList: [
                  {
                      decoratePrice: 0,
                      decoratePriceTypeValue: 0
                  }
              ],
              decoreArr:[],
              checkList:[],
              planStyleName: '',
              planInfo: {
                  brandId: []
              },
              styles: [], // 产品风格,
              uploadData: {
                  platform: 'web',
                  module: 'designplan',
                  type: 'image'
              },
              // 验证表单规则
              rules: {
                  planName: [
                      {required: true, message: '请输入方案名称', trigger: 'blur'},
                      {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                  ],
                  styleId: [
                      {required: true, message: '必填', trigger: 'blur'},
                      {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                  ],
                  designStyleId: [
                      {required: true, message: '请输入风格', trigger: 'blur'}
                  ]
              },
              // 品牌
              brands: [],
              planParams: {},
              // 品牌结束
              planNameCombo:[],
              subPlanNameCombo:[],
              activeName:'first',
              planId:this.$route.params.planId,
              introducedPicImg:[]
          }
      },
      computed: {
          detailPlanType() {
              if (localStorage.getItem('detailPlanType') == 3) {
                  return true;
              } else {
                  return false;
              }
          },
          filterDisable(){

          }
      },
      methods: {
          handleClick(){

          },
          saveContent() {
              this.API.planTextarea({
                  id:this.planId,
                  content:this.textarea3
              }).then((res) => {
                  if(res) {
                      this.$message.success('保存成功')
                  }
              })
          },
          async handleImageAdded(file,fileName, Editor, cursorLocation, resetUploader) {
              const isLt4M = file.size / 1024 / 1024 < 4;
              if (!isLt4M) {
                  this.$message({
                      type: 'error',
                      message: '文件大小4M以内!'
                  });
                  return;
              }
              let formData = new FormData();
              formData.append("file", file,fileName);
              formData.append("businessId ", sessionStorage.getItem("shopId"));
              await this.API.blogpicUp(formData).then(res => {
                  if (res.success) {
                      let url = this.BASE_URL.sourceBaseUrl + res.obj.picPath; // Get url from response
                      if (res.obj.id) {
                          this.introducedPicImg.push(res.obj);
                      }
                      Editor.insertEmbed(cursorLocation, "image", url);
                      resetUploader();
                  } else {
                      this.$message({
                          type: "error",
                          message: res.message
                      });
                  }
              });
          },
          getDetialMessage(item,index){
              this.planId=item.planId;
              this.planNameCombo.map(res=>{
                  res.active=false
              })
              item.active=true;
              this.$set(this.planNameCombo,index,this.planNameCombo[index]);

             this.getVueedit();
              this.getBrands()
              this.decoreate();
          },
          getVueedit(){
              this.API.getPlanTextarea({
                  id:this.planId
              }).then((res) => {
                  this.textarea3= res.data
              })
          },
          // 获取子方案的名称
          getSubPlanName(){
              this.API.OneKeyDesignPlanName({
                  planId:this.planId,
                  companyId:sessionStorage.getItem('companyID'),
              }).then((res) => {
                  this.planNameCombo = res.data;
                  this.planNameCombo.map((res,index)=>{
                      index==0 ? res.active=true : res.active=false
                  })
              })
          },
          inputValid(id, price){
              if(price < 0) {
                  this.$message.error('输入数字不能为负数')
                  return
              }
              // {id: 1, name: "半包"}, {id: 2, name: "全包"}, {id: 3, name: "包软装"}, {id: 4, name: "清水"}
            if(id == 1 && price < 200) {
                this.$message.error('半包价格不可以低于200元/平方')
                return
            }
            if(id==2 && price < 400) {
                this.$message.error('全包价格不可以低于400元/平方')
                return
            }

              if(id==3 && price < 400) {
                  this.$message.error('包软装价格不可以低于400元/平方')
                  return
              }

              if(id==4 && price < 1) {
                  this.$message.error('清水价格不可以低于1元/平方')
                  return
              }

          },
          checkFun(){
              console.log(this.checkList)
          },
          chg(index,value,name){
              this.decoreArr[index].active=!value;
              this.$set(this.decoreArr,index,this.decoreArr[index]);
              this.decoratePriceInfoList={
                  decoratePrice: 0,
                  decoratePriceTypeValue: name
              }
          },
          // 装修报价
          decoreate(){
              this.API.decoreatePrice().then((res) => {
                  this.decoreArr = res.data || [];
                  this.decoreArr.map(item=>{
                      item.active=true;
                      item.decoratePrice = 0;
                  });
                  this.getBaseInfo()
              })
          },
          handleImageSuccess(resp) {
              this.planInfo.picPath = resp.data.url
              this.planInfo.picId = resp.data.resId
          },
          getName(id) {
              console.log(id);
              let styleOne = this.styles.filter(item => item.id == id);
              this.planStyleName = styleOne[0].name;
          },
          submitForm(formName) {
              let flag=true;
              this.decoreArr.map(item=>{
                  switch (item.id){
                      case 1: if (flag && !item.active && item.decoratePrice<200){ this.$message.error('半包价格不可以低于200元/平方');flag=false} break;
                      case 2: if (flag && !item.active && item.decoratePrice<400){ this.$message.error('全包价格不可以低于400元/平方');flag=false } break;
                      case 3: if (flag && !item.active && item.decoratePrice<400){ this.$message.error('包软装价格不可以低于400元');flag=false } break;
                      case 4: if (flag && !item.active && item.decoratePrice<1){  this.$message.error('清水价格不可以低于1元/平方');flag=false; } break;
                  }
              });

              if(flag){
                  let arr=[];
                  this.decoreArr.map(res=>{
                      if(res.decoratePrice>0){
                          arr.push({
                              decoratePrice: this.checkList.includes(res.name) ? res.decoratePrice : 0,
                              decoratePriceTypeValue:res.id
                          })
                      }
                  });

                  this.$refs[formName].validate((valid) => {
                      if (valid) {
                          const params = {
                              brandIds: this.planInfo.brandId=='' ? [-1] : this.planInfo.brandId,
                              styleId: this.planInfo.designStyleId,
                              decoratePriceInfoList:arr,
                              picId: this.planInfo.picId,
                              picPath: this.planInfo.picPath ? this.planInfo.picPath.replace(this.BASE_URL.sourceBaseUrl, '') : '',
                              planDesc: this.planInfo.planDesc,
                              remark: this.planInfo.remark,
                              planName: this.planInfo.planName,
                              planId: parseInt(this.planId),
                              companyId: sessionStorage.getItem('companyID'),
                              applySpaceAreas:this.planInfo.applySpaceAreas.join(',')
                          }
                          for (let key in params) {
                              if (params[key] == '' || params[key] == undefined || params[key] == null) { // params[key] == '' || params[key] == undefined || params[key] == null
                                    if(params.planDesc == '' || params.applySpaceAreas == '') continue;
                                    delete params[key];
                              }
                          }
                          if (!params.decoratePriceInfoList) {
                              params.decoratePriceInfoList = [];
                          }
                          this.API.OneKeyDesignPlanEdit(params).then((res) => {
                              if (res.code === 200) {
                                  this.$message({
                                      type: 'success',
                                      message: '保存成功'
                                  });
                                  //  if (localStorage.getItem('detailPlanType') == 3) {
                                  //       localStorage.setItem('isNowPlanType', 'true');
                                  //   }
                                  //   } else {
                                  //       localStorage.setItem('isNowPlanType', 'false');
                                  //   }
                                  //this.$router.push(`/plan`);
                              }else {
                                  this.$message.error(res.message);
                              }

                          })


                      } else {
                          return false
                      }
                  })
              }

          },

          resetForm() {
              this.$router.go(-1);
          },
          // 产品风格下拉框
          getBaseProductStyleDrop() {
              this.API.BasePlanStyleDrop(this.planInfo.spaceTypeId).then((res) => {
                  this.styles = res.data
              })
          },
          //全屋方案 风格下拉框列表
          getfullhouseStyleDrop() {
              this.API.getfullhouseStyleList(this.planInfo.spaceTypeId).then((res) => {
                  this.styles = res.data
              })
          },
          getBaseInfo() {
                  this.API.OneKeyDesignPlanBaseInfo(this.planId).then((resp) => {
                      resp.data.brandId = resp.data.brandId ? resp.data.brandId.split(',').map(item => parseInt(item)) : []
                      this.planInfo = resp.data;
                      this.planInfo.applySpaceAreas=this.planInfo.applySpaceAreas && this.planInfo.applySpaceAreas.split(',').map(item=> { return parseInt(item) });
                      if(resp.data.decoratePriceInfoList) {
                          resp.data.decoratePriceInfoList.map((res) => {
                              this.decoreArr.map(item=>{
                                  if(res.decoratePriceTypeValue==item.id){
                                      item.decoratePrice=res.decoratePrice;
                                      item.name=res.decoratePriceTypeName;
                                      item.active=res.decoratePrice>0 ? false : true;
                                  }
                              })
                              if(res.decoratePrice>0){
                                  this.checkList.push(res.decoratePriceTypeName);
                              }

                          } )
                      }
                      this.getBaseProductStyleDrop();
                      this.getApplyAreasList();
                      this.planInfo.picPath = this.BASE_URL.sourceBaseUrl + this.planInfo.picPath
                  });
          },
          getBrands() {
              this.API.ProductBrandDrop({
                  companyId: sessionStorage.getItem('companyID')
              }).then((resp) => {
                  this.brands = resp.data
              })
          },
          getApplyAreasList(){
              this.API2.getApplyArea({type:this.planInfo.spaceTypeValueKey}).then(res=>{
                  this.applyAreasList=res.data;
              })
          },
          handleAvatarSuccess(response, file, fileList) {
              if (response.code === 200) {
                  this.planInfo.customizeVideoFilePath = response.data.url;
                  this.planInfo.customizeVideoFileId = response.data.resId;
              }
              console.log(this.planInfo.customizeVideoFilePath + "----------------------" + response.data.url)
          }
      },
      mounted() {
          this.getBrands()
          this.decoreate();
          this.getVueedit();
          this.getSubPlanName();
          // if (localStorage.getItem('detailPlanType') == 3) {
          //    localStorage.setItem('isNowPlanType', 'true');
          //  }
      }
  }
</script>

<style lang="scss" scoped>
    @import "../../../../assets/css/mixin";
    .subPlanName{
        /*border-bottom: solid 1px #eaeaea;*/
        background: #f5f7fa;
        margin: 20px 0;
        li{
            display: inline-block;
            padding:10px 8px;
            color: #666;
            position: relative;
            span{
                position: inherit;
                display: inline-block;
                padding:0 10px;
                height: 28px;
                line-height: 28px;
                font-size: 14px;
                border-radius: 4px;
                min-width: 75px;
                cursor: pointer;
                text-align: center;
                &.active{
                    border: solid 1px #ff6419;
                    color: #ff6419;
                    background-color: #fff;
                }
            }
        }
        li:nth-child(2n){
            &:after{
                content: '|';
                display: inline-block;
                margin-left: 14px;
            }
            &:before{
                content: '|';
                display: inline-block;
                margin-right: 14px;
            }
        }
        div{
            padding: 10px;
            background: #eaeaea;
            p{
                position: inherit;
                display: inline-block;
                padding:0 10px;
                height: 22px;
                line-height: 22px;
                font-size: 12px;
                cursor: pointer;
                &.active{
                    background: #eaeaea;
                    color: #ff6419;
                }
            }
        }
    }
    .mainPlan{
        background: #ff6419;
        color: #fff;
        display: inline-block;
        width: 18px;
        height: 18px;
        border-radius: 10px;
        font-size: 12px;
        line-height: 18px;
        text-align: center;
        position: inherit;
        left: 3px;
    }
    .baojia{
        font-size: 14px;

        .el-checkbox{
            margin: 0 10px;
        }

        div{
            .el-input{
                width: 200px;
                margin-left:3px;
                line-height: 34px;
                padding-left: 15px;
            }
            .last{
                margin-left: 12px;
            }
            margin-bottom: 15px;
            i{
                color: #333;
                font-size: 14px;
                font-style: normal;
            }
        }
        .inputdemo {
           height: 40px;
            line-height: 40px;
            box-sizing: border-box;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: #fff;
            text-indent: 16px;
        }
        .disable{
            background-color: #f5f7fa;
            border-color: #e4e7ed;
            color: #c0c4cc;
            cursor: not-allowed;
        }
        span{
            display: inline-block;
            color: #333;
            font-size: 14px;
        }
        b{
            display: inline-block;
            border: solid 1px #ddd;
            color: #999;
            font-size: 14px;
            margin-left: 4px;
            width: 16px;
            height: 16px;
            border-radius: 10px;
            line-height: 16px;
            text-align: center;
            font-weight: normal;
        }
    }
    .le{
        margin-left: -5px;
        border: solid 1px red;
    }
    .addProduct {
        background: #fff;
        box-sizing: padding-box;
        padding: 20px;
        border-radius: $borderRadius;
        header {
            h1 {
                font-weight: normal;
                font-size: 18px;
                padding-bottom: 15px;
                color: $greyOverButton;
                border-bottom: solid 1px $greyBorderEEE;
            }
        }
        p {
            height: 30px;
            line-height: 30px;
            padding-top: 30px;
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
        .productBaseInfo {
            height: 40px;
            line-height: 40px;
            background-color: #f6f6f6;
            border-left: solid 4px $leaveButtonStatus;
            color: $color666;
            font-size: 16px;
            padding-left: 10px;
            font-weight: bold;
            margin-bottom: 30px;
        }
        .selectWidth {
            width: 305px;
        }
        .textareaWidth {
            width: 705px;
            height: 130px;
            background-color: #eee;
            margin-bottom: 12px;
        }
    }
</style>

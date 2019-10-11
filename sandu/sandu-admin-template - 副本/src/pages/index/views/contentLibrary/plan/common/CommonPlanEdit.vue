<template>
  <transition name="fade">
    <div>
      <v-nav :nav="nav"></v-nav>
      <div class="addProduct">
        <header>
          <h1>编辑方案</h1>
        </header>
        <p><span>*</span>为必填项</p>
        <div>
          <el-row :gutter="24">
            <el-col :span="15">
              <el-form :model="planInfo" :rules="rules" ref="planInfo" label-width="100px" class="demo-ruleForm">
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="方案名称：" prop="planName">
                      <el-input v-model="planInfo.planName"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="11">
                    <el-form-item label="方案编号：">
                      {{planInfo.planCode}}
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="来源：" prop="productNameForm">
                      {{planInfo.origin}}
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
                    <el-form-item label="品牌：" prop="brandId">
                      <el-select style="width:100%"  clearable v-model="planInfo.brandId" multiple placeholder="请选择">
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
                      普通方案
                    </el-form-item>
                  </el-col>

                  <el-col :span="11">
                    <el-form-item label="风格：" prop="designStyleId">
                      <el-select v-model="planInfo.designStyleId" clearable  class="selectWidth" placeholder="请选择">
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
                <el-row :gutter="24">
                  <el-col :span="24">
                    <el-form-item label="装修报价：" class="baojia">
                      <el-checkbox-group v-model="checkList"  @change="check">
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

                          <el-input :disabled="item.active"
                                    min="1"
                                    maxlength="4"
                                    type="number"
                                    v-model.number="item.decoratePrice"

                                    placeholder="请输入"> </el-input>
                          <span>&emsp; 元/平米（㎡）</span>
                        </div>
                      </el-checkbox-group>

                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="11">
                  <el-col :span="21">
                    <el-form-item label="方案描述：" prop="remark">
                      <el-input
                        style="width:100%"
                        class="textareaWidth mbottom"
                        type="textarea"
                        :rows="6"
                        placeholder="请输入内容"
                        v-model="planInfo.remark">
                      </el-input>
                      <br>
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
                        v-model="planInfo.planDesc">
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item>
                  <el-button type="primary" @click="submitForm('planInfo')" round class="confirmBtn">确定</el-button>
                  <el-button @click="resetForm('planInfo')" round class="cancelBtnBg">取消</el-button>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="9">
              <el-upload
                :action="BASE_URL.uploadUrl"
                :show-file-list="false"
                :data="uploadData"
                :on-success="handleImageSuccess"
              >
                <img width="500" height="300" :src="planInfo.picPath">
                <div>
                  <br>
                  <el-button type="primary"  round class="confirmBtn">重新上传</el-button>
                </div>
              </el-upload>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
  export default {
    data () {
      return {
        nav: [
                  { nav: "方案库", url: `/plan` },
                  { nav: "方案编辑", url: "" }
              ],
        planInfo: {},
        styles: [], // 产品风格,
        uploadData: {
          platform: 'web',
          module: 'designplan',
          type: 'image'
        },
        // 验证表单规则
        rules: {
          planName: [
            {required: true, message: '必填', trigger: 'blur'},
            {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
          ],
          styleId: [
            {required: true, message: '必填', trigger: 'blur'},
            {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
          ],
          branId: [
            {required: true, message: '必填', trigger: 'blur'}
          ],
          remark: [
            {required: true, message: '必填', trigger: 'blur'}
          ]
        },
        // 品牌
        brands: [],
        // 品牌结束
          decoreArr:[],
          checkList:[],
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

      }
    },
    methods: {
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
                this.$message.error('全包价格不可以低于1元/平方')
                return
            }

        },
        check(){
            console.log(this.checkList)
        },
        chg(index,value,name){
            this.decoreArr[index].active=!value;
            this.decoratePriceInfoList={
                decoratePrice: 0,
                decoratePriceTypeValue: name
            }
        },
        // 装修报价
        decoreate(){
            console.log(2222)
            this.API.decoreatePrice().then((res) => {
                this.decoreArr = res.data;
                this.decoreArr.map(item=>{
                    item.active=true;
                    item.decoratePrice = 0;
                });
                this.getBaseInfo();
            })
        },
      submitForm (formName) {
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
            this.$refs[formName].validate((valid) => {
          if (valid) {

              let arr=[];
              this.decoreArr.map(res=>{
                  if(res.decoratePrice>0){
                      arr.push({
                          decoratePrice: this.checkList.includes(res.name) ? res.decoratePrice : 0,
                          decoratePriceTypeValue:res.id
                      })
                  }
              });

            const params = {
                decoratePriceInfoList:arr,
                brandIds: [this.planInfo.brandId],
                styleId: this.planInfo.designStyleId,
                picId: this.planInfo.picId,
                picPath: this.planInfo.picPath,
                planDesc: this.planInfo.planDesc,
                remark: this.planInfo.remark,
                planName: this.planInfo.planName,
                planId: this.planId
            }
            for (let key in params) {
              if (params[key] == '' || params[key] == undefined || params[key] == null) {
                delete params[key];
              }
            }
            this.API.CommonDesignPlanEdit(params).then((res) => {
              this.$message({
                type: 'success',
                message: '保存成功'
              })
              this.$router.push(`/plan`)
            })
          } else {
            return false
          }
        })
           }
      },

      resetForm (formName) {
        this.$refs[formName].resetFields()
      },
      // 产品风格下拉框
      getBaseProductStyleDrop () {
        this.API.DesignPlanStyles().then((res) => {
          this.styles = res.data
        })
      },
      getBaseInfo () {
            console.log(1111)
        this.planId = this.$route.params.planId
        this.API.CommonDesignPlanBaseInfo(this.planId).then((resp) => {
            this.planInfo = resp.data;
            resp.data.brandId = resp.data.brandId ? resp.data.brandId.split(',').map(item => parseInt(item)) : [];
            let priceArr=[]
            if(resp.data.decoratePriceInfoList) {
                resp.data.decoratePriceInfoList.map((res) => {
                    this.decoreArr.map(item=>{
                        if(res.decoratePriceTypeValue==item.id){
                            item.decoratePrice=res.decoratePrice;
                            item.name=res.decoratePriceTypeName;
                            item.active=res.decoratePrice>0 ? false : true;
                        }
                    })

                    console.log(this.decoreArr, 'priceobj')
                    // priceArr.push({
                    //     id:res.decoratePriceTypeValue,
                    //     name:res.decoratePriceTypeName,
                    //     decoratePrice:res.decoratePrice,
                    //     active:res.decoratePrice>0 ? false : true
                    // })
                    if(res.decoratePrice>0){
                        this.checkList.push(res.decoratePriceTypeName);
                    }

                } )
            }
            // if(priceArr.length){
            //
            //     this.decoreArr = priceArr
            // }
              //this.decoreArr = priceArr
        })
      },
      getBrands () {
        this.API.ProductBrandDrop({
          companyId: sessionStorage.getItem('companyID')
        }).then((resp) => {
          this.brands = resp.data
        })
      },
      handleImageSuccess (resp) {
        this.planInfo.picPath = resp.data.url;
        this.planInfo.picId = resp.data.resId
      }
    },
    mounted () {
      this.getBrands();
      this.decoreate();
      this.getBaseProductStyleDrop()
    }
  }
</script>

<style lang="scss" scoped>
  @import "../../../../assets/css/mixin";

  .addProduct {
    background: #fff;
    box-sizing: padding-box;
    padding: 20px;
    border-radius: $borderRadius;
    header {
      h1 {
        font-weight: bold;
        font-size: 20px;
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
    }
  }

</style>


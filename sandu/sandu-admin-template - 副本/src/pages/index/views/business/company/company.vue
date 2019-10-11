<template>
    <div class="company">
        <header>
            编辑企业
        </header>
        <p class="need"><span>*</span>为必填项</p>
        <el-row :gutter="24" class="splineFrame">
            <el-col :span="2" class="productBaseInfo">企业信息</el-col>
            <el-col :span="22" class="rightLine">
                <hr>
            </el-col>
        </el-row>
        <el-form label-width="140px" class="demo-ruleForm" :model="companyDetail" :rules="rules" ref="ruleForm">
            <el-row :gutter="12">
                <el-col :span="6">
                    <el-form-item label="企业名称：" prop="companyName">
                        <el-input v-model.trim="companyDetail.companyName" placeholder="请输入企业名称"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <!--/*设计公司不应看到所属行业字段  businessType=6*/-->
             <el-row :gutter="12" v-if="companyDetail.businessType==1 || companyDetail.businessType==9">
                <el-col :span="6">
                    <el-form-item label="所属行业：">
                        <input class="slh" v-model="industryNames" @click="choseIndustryShow" placeholder="请选择所属行业">
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="24" v-if="companyDetail.businessType ==1 || companyDetail.businessType ==9">
              <el-col :span="24">
                  <el-row :gutter="12">
                      <el-col :span="6">
                          <el-form-item label="产品可见范围：" prop="textareaChangeNameList">
                              <!--<el-select v-model="visibleProduct" placeholder="请选择产品可见范围">
                              </el-select>-->
                              <input class="slh" v-model="companyDetail.textareaChangeNameList" @focus="showProductCategoryTree" placeholder="请选择产品可见范围">
                              <el-row :gutter="24" class="goods-operating-infos">

                              </el-row>
                          </el-form-item>
                      </el-col>
                  </el-row>
                  <el-row :gutter="12">
                      <el-col :span="12" class="brandName">
                          <el-form-item class="mar0" label="品牌：" prop="valBrand">
                              <div class="brandName">
                                  <p v-for="item in addedBrand">{{item}}</p>
                              </div>
                          </el-form-item>
                      </el-col>
                  </el-row>
              </el-col>
            </el-row>
            <el-row :gutter="12">
                <el-col :span="6">
                    <el-form-item label="联系人姓名：" prop="contactName">
                        <el-input v-model.trim="companyDetail.contactName" placeholder="请输入联系人姓名"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="12">
                <el-col :span="6">
                    <el-form-item label="联系人电话：" prop="phone">
                        <el-input  v-model.nubmer="companyDetail.phone" placeholder="请输入联系人电话"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="24">
                    <el-form-item label="区域：">
                        <!---------------------------------------------------->

                        <el-col :span="3">
                            <el-select v-model="citySelect.provide"
                                       @change="getProvideCode(citySelect.provide)"
                                       clearable
                                       placeholder="请选择省">
                                <el-option
                                        v-for="(item, index) in provide"
                                        :key="item.areaCode"
                                        :label="item.areaName"
                                        :value="item.areaCode">
                                </el-option>
                            </el-select>
                        </el-col>

                        <el-col :span="3">
                            <el-select clearable v-model="citySelect.city"
                                       @change="getCityCode(citySelect.city)"
                                       placeholder="请选择市">
                                <el-option
                                        v-for="(item,index) in citySelects"
                                        :key="item.areaCode"
                                        :label="item.areaName"
                                        :value="item.areaCode">
                                </el-option>
                            </el-select>
                        </el-col>

                        <el-col :span="3">
                            <el-select clearable v-model="citySelect.area"
                                       @change="getAreaCode(citySelect.area)"
                                       placeholder="请选择区域">
                                <el-option
                                        v-for="(item,index) in areaSelect"
                                        :key="item.areaCode"
                                        :label="item.areaName"
                                        :value="item.areaCode">
                                </el-option>
                            </el-select>
                        </el-col>

                        <el-col :span="3" v-if="streetShow">
                            <el-select clearable v-model="citySelect.origin"
                                       @change="getOriginCode(citySelect.origin)"
                                       placeholder="请选择街道">
                                <el-option
                                        v-for="(item,index) in originSelect"
                                        :key="item.areaCode"
                                        :label="item.areaName"
                                        :value="item.areaCode">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="3" v-else>
                            <span class="streetSel">不限</span>
                        </el-col>
                        <!---------------------------------------------------->
                    </el-form-item>
            </el-row>

            <el-row :gutter="12">
                    <el-form-item label="详细地址：">
                        <el-input v-model.trim="companyDetail.companyAddress" style="width: 250px"  maxlength="32"  placeholder="请输入详细地址"></el-input>
                    </el-form-item>
            </el-row>

            <el-row :gutter="24">
                <el-col :span="24">
                    <el-form-item label="LOGO设置：">
                        <!--<el-upload
                                style="border: solid 1px #ddd; border-radius: 5px; position: relative; width:90px; height: 90px"
                                class="avatar-uploader"
                                action="https://jsonplaceholder.typicode.com/posts/"
                                :show-file-list="false"
                                :on-success="handleAvatarSuccess"
                                :before-upload="beforeAvatarUpload">
                            <img v-if="imageUrl" style="width:90px; height: 90px" :src="imageUrl" class="avatar">
                            <i v-else style="position: relative;top: 0;  width:90px; height: 90px" class="el-icon-plus avatar-uploader-icon uploadPlus"></i>
                        </el-upload>-->

                        <ul class="uploadPic">
                            <li style="margin: 5px 0 5px 0">
                                <el-upload
                                        class="avatar-uploader"
                                        :action="BASE_URL.systemUrl + BASE_URL.companyFactoryUploadLogo"
                                        :headers="headers"
                                        list-type="picture-card"
                                        :on-success="handleAvatarSuccess"
                                        :file-list="fileList2"
                                        :before-remove="deleteLogo"
                                        :before-upload="beforeAvatarUpload">
                                    <!--<i class="el-icon-plus avatar-uploader-icon"></i>-->
                                    <div class="avatar-uploader-icon">
                                        <div class="across"></div>
                                        <div class="vertical"></div>
                                    </div>
                                </el-upload>

                            </li>
                            <li style="width: 460px; font-size: 14px; color: #999">文件格式GIF，JPG，JPEG，PNG文件大小500k以内，尺寸500:500px</li>
                        </ul>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="12" v-if="companyDetail.businessType ==1">
                <el-col :span="8">
                    <el-form-item label="公司网站：">
                        <el-input v-model="companyDetail.companyUrl" placeholder="请输入公司网站"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="12" v-if="companyDetail.businessType ==1">
                <el-col :span="8">
                    <el-form-item label="经营范围：" prop="ranges">
                        <el-input v-model="companyDetail.businessScope" placeholder="请输入经营范围"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="12">
                <el-col :span="12">
                    <el-form-item label="企业介绍：">
                        <el-input type="textarea" placeholder="限制0~500个字符"  v-model="companyDetail.companyDesc" class="textareaInfo"> </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div class="buttonFrame">
            <el-button round class="cancelBtnBg bg" @click="resetFrom">重 置</el-button>
            <el-button round type="primary" class="confirmBtn" @click="updateFactoryMsg('ruleForm')">保 存</el-button>
        </div>

        <el-dialog title="选择产品可见范围" :visible.sync="infoClassifyVisible" class="info-ypop-up" width="540px">
            <el-row>
                <el-col :span="24">
                    选择分类：
                </el-col>
            </el-row>
            <el-row class="Classify-list">
                <el-col>
                    <div class="clearfix">
                        <div class="float width">
                            <div v-for="(item, index) in listOne" :key="index">
                                <el-checkbox-group style="display: inline-block"
                                                   v-model="firstActiveArr[index]"
                                                   @change="oneTree(index)">
                                    <el-checkbox :checked="firstActiveArr[index]" :value="item.categoryId"></el-checkbox>
                                </el-checkbox-group>

                                <span v-model="checkListOne"
                                      @click="oneTreeSpan(index)"
                                      style="cursor: pointer;">
                                        {{item.categoryName}}
                                </span>
                            </div>
                        </div>

                        <div class="float width">
                            <div v-for="(two, jdx) in listTwo" :key="jdx">
                                <el-checkbox-group
                                        style="display: inline-block"
                                        v-model="secondActiveArr[secondIndex][jdx]"
                                        @change="twoTree(jdx)">
                                    <el-checkbox :checked="secondActiveArr[secondIndex][jdx]" :value="two.categoryId" ></el-checkbox>
                                </el-checkbox-group>
                                <span v-model="checkListTwo"
                                      style="cursor: pointer;"
                                      @click="twoTreeSpan(jdx)">
                                        {{two.categoryName}}
                                    </span>
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
                                    <el-checkbox :checked="threeActiveArr[secondIndex][threeIndex][kdx]" :value="three.categoryId" >{{three.categoryName}}</el-checkbox>
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
        <!--所属行业弹框-->
        <el-dialog title="添加所属行业"  :visible.sync="choseIndustry"  class="info-ypop-up subtitle" width="540px">
            <div class="boxheader">
                <el-input type="text" :clearable="true" v-model="searchtype"></el-input>
                <el-button round class="searchbtn" @click="SearchType">搜索</el-button>
            </div>
            <p class="chosetype">选择分类：
                <span class="reset" @click="Reset">重置</span>
            </p>
            <div class="Industrybox">
                <el-checkbox-group v-model="modelList">
                    <el-checkbox v-for="(item,index) in industry_list" :key="index" class="checkbox" :label="item.value">{{item.name}}</el-checkbox>
                </el-checkbox-group>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button round type="primary" @click="IndustryToExtend">确 定</el-button>
                <el-button round @click="CloseChose">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        inject:['reload'],
        data() {
            return {
               closeIndustry:[],
                oldCategoryIds:[],
                oldIndusList:"",
                industryNames:'',//所属行业显示内容
                modelList:[],
                industry_list:[],
                searchtype:'',
                choseIndustry:false,
                streetShow:true,
                visibilityRange: '',
                visibilityName: [],
                visibleId:[],
                tempVisibleId:[],
                indexOne:0,
                listOne:[],
                listTwo:[],
                listThree:[],
                fileList2: [],
                checkListOne: [],
                checkListTwo: [],
                checkListThree:[],
                nowNameArr:[],

                lastTree:[],
                infoClassifyVisible:false,
                companyDetail:{},
                imageUrl: '',
                value: '',
                //区域选择-----------
                search:{
                    franchiserName: '',
                    phone: '',
                    contactName: '',
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },
                citySelect:{
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },
                addedBrand:[],
                brandId: '',
                cityCode: '',
                provide:[],
                provideSelect:[],
                citySelects:[],
                areaSelect: [],
                originSelect: [],

                //区域选择 结束------------
                uploadImgId: '',
                showImg: '',

                // 重置值，

                rules: {
                    // companyAddress: [
                    //     {required: true, message: '请输入详细地址', trigger: 'blur'},
                    //     {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}
                    //     // {
                    //     //     required: true,
                    //     //     validator: this.address,
                    //     //     trigger: 'blur'
                    //     // }
                    // ],
                    companyName: [
                        {required: true, message: '请输入经销商名称', trigger: 'blur'},
                        {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}
                    ],
                    brandName: [

                    ],
                    // contactName: [
                    //     {required: true, message: '请输入联系人名称', trigger: 'blur'},
                    //     {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}
                    // ],
                    /*companyDesc:[
                        {
                            validator: this.txtSize,
                            trigger: 'blur'
                        }
                    ],*/
                    ranges:[
                        {
                            validator: this.txtRange,
                            trigger: 'blur'
                        }
                    ],
                    phone: [
                        {message: '请输入电话号码', trigger: 'change'},
                        {
                            validator: this.validPhone,
                            trigger: 'blur'
                        }
                    ],
                    valBrand:[
                        {   required: true,
                            validator: this.valBrand,
                            trigger: 'change'
                        }
                    ],

                    textareaChangeNameList:[
                        {required: true, message: '请选择产品可见范围', trigger: 'blur'}
                    ],
                    // provinceCode: [
                    //     //{ required: true, message: '请选择区域', trigger: 'blur' },
                    //     { required: true, validator: this.validArea, trigger: 'blur'}
                    // ],
                },
                textareaChangeList: '',
                resetDetailVal:{},
                checkAll:false,
                firstIndex: 0,
                secondIndex: 0,
                threeIndex: 0,
                firstActiveArr: [],
                secondActiveArr: [],
                threeActiveArr: [],
                textareaChangeNameList: '',
                productVisibilityRangeNameId: []
            }
        },
        watch: {
            threeActiveArr(newVal) {
                let flag = false
                newVal.forEach((valueP, indexP) => {
                    valueP.forEach((valueC, indexC) => {
                        valueC.indexOf(false) == -1?flag = false:flag = true
                        this.$set(this.secondActiveArr[indexP], indexC, !flag)
                    })
                })
            }
        },
        filters: {
            filter(value) {
                return this.BASE_URL.sourceBaseUrl + value;
            }
        },

        computed:{
            headers(){
                return { //上传图片时传token给后台
                    'Authorization' : qs.parse(sessionStorage.getItem('loginUser')).token
                }
            }
        },
        methods: {

         //取消弹框
    CloseChose() {
      this.choseIndustry = false;
      this.modelList = this.oldIndusList;//回显一开始的行业列表
    },
    //重置行业列表
    Reset() {
      this.searchtype = "";
      this.GetIndustryList();
      // for(let i=0;i<this.industry_list.length;i++){
      //    this.industry_list[i].isCheck = false;
      //  }
      this.modelList = [];
      console.log(this.modelList);
    },

    //搜索行业列表
    SearchType() {
      console.log(this.searchtype);
      this.GetIndustryList(this.searchtype);
    },
    //行业弹框 确定接口
    IndustryToExtend() {
     console.log(typeof(this.textareaChangeList));
      console.log(typeof(this.modelList.toString()));
      this.API.industrytoextend({
        newIndustryValues:this.modelList?this.modelList.toString():undefined, 
       
       oldIndustryValues:this.oldIndusList?this.oldIndusList.toString():undefined,//this.oldIndusList?this.oldIndusList:undefined,//"",
       
        oldCategoryIds: this.textareaChangeList,
      }).then(res => {
        
        if (res.success) {
         
          this.textareaChangeList = res.obj.categoryIds.join();//把id赋到要保存的参数列表里面

          this.productVisibilityRangeNameId = res.obj.categoryIds.join();//把id赋进要勾勾的数组里面

           this.productCategoryTree()

          this.oldIndusList =res.obj.industryValues;//旧数组
      
          this.industryNames = res.obj.industryNames; //当前显示的行业名字

          this.companyDetail.textareaChangeNameList = res.obj.categoryNames;//确定后把可选范围的名字显示在可选范围框框

          this.choseIndustry = false; 
        } else {
          this.$message.error(res.message);
        }
      });
    },
    //获取所属行业列表
    GetIndustryList(value) {
      if (value) {
        this.API.getindustrylist({
          type: "industry",
          name: value
        }).then(res => {
          console.log(res.datalist);
          this.industry_list = res.datalist;
        });
      } else {
        this.API.getindustrylist({
          type: "industry"
        }).then(res => {
          console.log(res.datalist);
          this.industry_list = res.datalist;
        });
      }
    },

    choseIndustryShow() {
      this.choseIndustry = true;
    },

            /*选择区域校验*/
            address(rule, value, callback) {
                if (this.companyDetail.companyAddress == null || this.companyDetail.companyAddress.length == 0) {
                    callback(new Error('请输入详细地址'))
                } else {
                    callback();
                }
            },
            oneTreeSpan(index) {
                this.secondIndex = index
                this.threeIndex = 0
                this.listTwo = this.listOne[index].list;
                this.listThree = this.listOne[index].list[0].list
                // this.listThree = []
                // this.visibleId = []
            },
            twoTreeSpan(index) {
                this.threeIndex = index
                this.listThree = this.listOne[this.secondIndex].list[index].list;
                // this.lastTree.push(this.listThree)
                // this.visibleId = []
            },
            validArea(rule, value, callback) {
                if (this.citySelect.provide.length == 0
                    || this.citySelect.city.length == 0
                    || this.citySelect.area.length == 0
                    || (this.originSelect.length > 0 && this.citySelect.origin.length == 0)) {
                    callback(new Error('请选择完整的区域'))
                } else {
                    callback();
                }
            },

            /*txtSize(rule, value, callback){
                if (this.companyDetail.companyDesc.length > 500) {
                    callback(new Error('介绍信息在500字条以内'))
                } else {
                    callback();
                }
            },*/
            txtRange(rule, value, callback){
                if (this.companyDetail.businessScope.length > 50) {
                    callback(new Error('经营范围在50字以内'))
                } else {
                    callback();
                }
            },
            valBrand(rule, value, callback){
                if (this.addedBrand.length == 0) {
                    callback(new Error('请添加品牌'))
                } else {
                    callback();
                }
            },
            /*电话号码验证*/
            validPhone(rule, value, callback) {
                let mobileRegex = /^1[3456789]\d{9}$/;
                let phoneRegex = /^((0\d{2,3})([-]{0,1}))(\d{7,8})(-(\d{3,}))?$/;
                let fix400 =/^(4|8)00([-]{0,1})[0-9]{7}$/; // /^400[0-9]{7}/
                if (value==''||fix400.test(value) || phoneRegex.test(value) || mobileRegex.test(value)) { //
                    callback();
                } else {
                    callback(new Error('电话号码格式不正确！'))
                }

            },
            resetFrom() {
                this.$confirm('确定还原企业信息吗?', '提示', {
                    confirmButtonText: '确定',
                    type: 'info',
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    center: true
                }).then(() => {
                    this.getManuFacturerDetail ()
                    this.$message.success('企业信息已还原成功')
                })
            },
           getManuFacturerDetail () {
                this.API.manuFacturerDetail({
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    if(res) {
                      if(res.obj.companyIndustrys){
                        this.modelList = res.obj.companyIndustrys.split(",").map(item => {
                          return +item;
                        });
                        this.oldIndusList = this.modelList;
                        this.industryNames = res.obj.companyIndustryNames;
                      }
                        this.companyDetail = res.obj;
                        if(res.obj.provinceCode) {
                            this.citySelect.provide = res.obj.provinceCode
                            this.getArea(res.obj.provinceCode, '市')
                        }
                        if(res.obj.cityCode) {
                            this.citySelect.city = res.obj.cityCode
                            this.getArea(res.obj.cityCode, '区')
                        }
                        if(res.obj.areaCode) {
                            this.citySelect.area = res.obj.areaCode
                            this.getArea(res.obj.areaCode, '街道')
                        }
                        if(res.obj.streetCode) {
                            this.citySelect.origin = res.obj.streetCode
                        }

                        this.textareaChangeList = res.obj.productVisibilityRange
                        if(res.obj.productVisibilityRangeName)
                        {
                            this.companyDetail.textareaChangeNameList = res.obj.productVisibilityRangeName
                        }
                        if(res.obj.productVisibilityRange) {
                            this.productVisibilityRangeNameId = res.obj.productVisibilityRange.split(',')

                        }
                        if(res.obj.companyLogo) {
                            this.uploadImgId = res.obj.companyLogo
                        }
                        this.search.provide = res.obj.provinceCode
                        this.search.city = res.obj.cityCode
                        this.search.area = res.obj.areaCode
                        this.search.origin = res.obj.streetCode
                        this.showImg = res.obj.companyLogoPath
                        let $self = this
                        if(this.showImg) {
                            this.fileList2 = [{url: this.BASE_URL.sourceBaseUrl + $self.showImg}]
                        }
                        if(res.obj.brandName){
                            this.addedBrand = res.obj.brandName.split(',')
                        }
                        this.productCategoryTree()
                    }
                })
           },
            updateFactoryMsg(formName){
                let updateMsg = {
                    provinceCode: this.citySelect.provide,//  (string): 企业省编码 ,
                    cityCode: this.citySelect.city,//  (string): 企业市编码 ,
                    areaCode:this.citySelect.area,// (string): 企业区编码 ,
                    streetCode: this.citySelect.origin,//  (string): 企业街道编码
                    businessScope: this.companyDetail.businessScope,//  (string, optional): 企业经营范围 ,
                    companyAddress: this.companyDetail.companyAddress,//  (string): 企业详细地址 ,
                    companyDesc: this.companyDetail.companyDesc,//  (string, optional): 企业介绍 ,
                    companyLogo:this.uploadImgId ? this.uploadImgId : undefined ,//  (integer, optional): 企业logo ,
                    companyName: this.companyDetail.companyName,//  (string): 企业名称 ,
                    companyUrl: this.companyDetail.companyUrl,//  (string, optional): 企业网站 ,
                    contactName: this.companyDetail.contactName,//  (string): 企业联系人姓名 ,
                    id: sessionStorage.getItem('companyID'),//  (integer): 企业id ,
                    phone: this.companyDetail.phone,//  (string): 企业联系人电话 ,
                    productVisibilityRange: this.textareaChangeList, //企业可见产品范围id
                    companyIndustrys:this.modelList?this.modelList.toString():undefined, //企业所属行业ID
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(sessionStorage.getItem('companyType') != 1) {
                            this.API.nonFactoryUpate(updateMsg).then((res) => {
                                if(res.success) {
                                    this.$message.success('更新成功')
                                    this.getManuFacturerDetail()
                                }
                            }).catch((res) => {
                                this.$message.error(res.message)
                            })
                        } else {
                            this.API.updateFacturerMsg(updateMsg).then((res) => {
                                if(res.success) {
                                    this.$message.success('更新成功')
                                    this.getManuFacturerDetail()
                                }
                            }).catch((res) => {
                                this.$message.error(res.message)
                            })
                        }
                    }
                })
            },

            verifyProductClass() {
                let arr = [], newArr = [], newArrName = [] ;
                this.listOne.forEach((valueP, indexP) => {
                    valueP.list.forEach((valueC, indexC) => {
                        valueC.list.forEach((val, index) => {
                            if (this.threeActiveArr[indexP][indexC][index]) {
                                arr.push(val)
                            }
                        })
                    })
                })
                if (arr.length === 0) {
                    return
                } else {
                    this.infoClassifyVisible = false;
                }
                arr.forEach((value) => {
                    newArr.push(value.categoryId)
                    newArrName.push(value.categoryName)
                })
                this.textareaChangeList = newArr.join(',')
                this.companyDetail.textareaChangeNameList = newArrName.join(',')

            },
            showProductCategoryTree() {
                this.infoClassifyVisible = true;
            },
            productCategoryTree() {
                this.API.companyCategory({
                    companyId:sessionStorage.getItem('companyID')
                }).then((res) => {
                    this.listOne=res.datalist;
                    this.listTwo = this.listOne[0].list;
                    this.listThree = this.listOne[0].list[0].list
                    this.getActive(this.listOne, true)
                })
            },
            getActive(list, flag) {
                let arr1 = [], arr2 = [], arr3 = []
                if (flag) {
                    list.forEach((value, index) => {
                        arr1.push(false)
                        arr2.push([])
                        arr3.push([])
                        value.list.forEach((val, count) => {
                            arr3[index].push([])
                            val.list.forEach((valC) => {
                                if (this.productVisibilityRangeNameId.indexOf(valC.categoryId.toString()) != -1) {
                                    arr3[index][count].push(true)
                                } else {
                                    arr3[index][count].push(false)
                                }
                            })
                            arr2[index].push(false)
                        })
                    })
                } else {
                    list.forEach((value, index) => {
                        arr1.push(false)
                        arr2.push([])
                        arr3.push([])
                        value.list.forEach((val, count) => {
                            arr3[index].push([])
                            val.list.forEach((valC) => {
                                arr3[index][count].push(false)
                            })
                            arr2[index].push(false)
                        })
                    })
                }
                this.firstActiveArr = arr1
                this.secondActiveArr = arr2
                this.threeActiveArr = arr3
                this.threeActiveArr.forEach((value, indexP) => {
                    value.forEach((valueC, indexC) => {
                        valueC.forEach((val, index) => {
                            this.$set(this.threeActiveArr[indexP][indexC], index, val)
                        })
                    })
                })
                /*console.log(this.productVisibilityRangeNameId, 'productVisibilityRangeNameId')
                console.log(this.threeActiveArr, 'threeActiveArr')*/
            },
            oneTree(index) {
                // this.indexOne=index;
                this.secondIndex = index
                this.threeIndex = 0
                this.listTwo = this.listOne[index].list;
                this.listThree = this.listOne[index].list[0].list
                if (this.firstActiveArr[index]) {
                    this.secondActiveArr[this.secondIndex].forEach((value, index) => {
                        this.secondActiveArr[this.secondIndex][index] = true
                    })
                    this.threeActiveArr[this.secondIndex].forEach((value) => {
                        value.forEach((val, index) => {
                            value[index] = true
                        })
                    })
                    this.threeActiveArr = this.threeActiveArr.concat()
                    this.secondActiveArr = this.secondActiveArr.concat()
                } else {
                    this.secondActiveArr[this.secondIndex].forEach((value, index) => {
                        this.secondActiveArr[this.secondIndex][index] = false
                    })
                    this.threeActiveArr[this.secondIndex].forEach((value) => {
                        value.forEach((val, index) => {
                            value[index] = false
                        })
                    })
                    this.threeActiveArr = this.threeActiveArr.concat()
                    this.secondActiveArr = this.secondActiveArr.concat()
                }
            },
            twoTree(index) {
                this.listThree = this.listOne[this.secondIndex].list[index].list;
                this.threeIndex = index
                if (this.secondActiveArr[this.secondIndex][index]) {
                    this.threeActiveArr[this.secondIndex][this.threeIndex].forEach((value, index) => {
                        this.threeActiveArr[this.secondIndex][this.threeIndex][index] = true
                    })
                    this.threeActiveArr = this.threeActiveArr.concat()
                } else {
                    this.threeActiveArr[this.secondIndex][this.threeIndex].forEach((value, index) => {
                        this.threeActiveArr[this.secondIndex][this.threeIndex][index] = false
                    })
                    this.threeActiveArr = this.threeActiveArr.concat()
                }
            },
            listThreeVal(val) {
                let arrs = []
                if(this.threeActiveArr[val]) {
                }
                this.threeActiveArr.map((item,index) => {

                })
            },

            // 区域选择-------------
            getArea(code, type){
                this.API.areaList({areaCode:code}).then((res => {
                    if(res) {
                        if(type == '省') {
                            this.provide = res.datalist
                        }
                        if(type == '市') {
                            this.citySelects = res.datalist
                        }
                        if(type == '区') {
                            this.areaSelect = res.datalist
                        }
                        if(type == '街道') {
                            this.originSelect = res.datalist

                            if(!this.originSelect || this.originSelect.length == 0)
                            {
                                this.streetShow =false
                                // this.originSelect = [{areaCode: '', areaName: '不限'}]
                            } else {
                                this.streetShow = true
                            }
                        }
                    }
                }))
            },
            getProvideCode(code) {
                this.citySelect.city =''
                this.citySelect.area =''
                this.citySelect.origin =''
                this.citySelects =[]
                this.areaSelect = []
                this.originSelect = []
                this.getArea(code,'市')

            },
            getCityCode (code){
                this.citySelect.area =''
                this.citySelect.origin =''
                this.areaSelect = []
                this.originSelect = []
                this.getArea(code,'区')
            },
            getAreaCode (code){
                this.citySelect.origin =''
                this.originSelect = []
                this.getArea(code,'街道')
            },
            getOriginCode (code){

            },
            // 区域选择 结束---------- streetCodeName
            handleAvatarSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw);
                this.uploadImgId = res.obj.id;
            },
            deleteLogo() {
                this.uploadImgId = ''
                this.fileList2 = []
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';
                const isGIF = file.type === 'image/gif';
                const isLt2M = file.size / 1024 / 1024 < 0.5;

                if (!isJPG && !isPNG && !isGIF) {
                    this.$message.error('上传图片只能是 GIF/JPG/PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 500KB!');
                }
                return (isJPG || isPNG || isGIF) && isLt2M;
            }
        },
        created(){
            this.getArea(0,'省');
            this.GetIndustryList();;
        },
        mounted(){
            this.getManuFacturerDetail()
        }
    }
</script>

<style scoped lang="scss">

    .streetSel{
        display: inline-block;
        width: 100px;
        height: 38px;
        border: solid 1px #ddd;
        position: relative;
        border-radius: 4px;
        padding-left: 15px;
        &:after{
            content: '';
            position: absolute;
            border-bottom: solid 1px #ddd;
            border-right: solid 1px #ddd;
            -webkit-transform: rotate(45deg);
            width:8px;
            height: 8px;
            right: 10px;
            top: 12px;

        }
    }

    .slh{
        border: solid 1px #ddd;
        height: 40px;
        border-radius:4px;
        padding:0 15px;
        width: 80%;  /*必须设置宽度*/
        overflow: hidden;  /*溢出隐藏*/
        text-overflow: ellipsis; /*以省略号...显示*/
        white-space: nowrap;  /*强制不换行*/
    }
.boxheader {
  width: 460px;
  margin: 0 auto 20px auto;
  display: flex;
  .text {
    flex: 1;
  }
  .searchbtn {
    margin-left: 10px;
    flex-basis: 76px;
  }
}
.chosetype {
  width: 460px;
  margin: 0 auto 20px auto;
  .reset {
    color: #ff6419;
    cursor: pointer;
  }
}

.Industrybox {
  width: 460px;
  height: 360px;
  border: 1px solid #cccccc;
  margin: 0 auto;
  overflow: scroll;
  overflow-x: hidden;
  .el-checkbox-group {
    // width: 390px;
    // margin: 0 auto;
    // display: block;
    margin-left: 10px;
  }
  .checkbox {
    width: 144px;
    height: 36px;
    line-height: 36px;
    margin: 0;
    overflow: hidden; /*自动隐藏文字*/
    text-overflow: ellipsis; /*文字隐藏后添加省略号*/
    //overflow: hidden;
  }
}
    .brandName{
        p{
            display: inline-block;
            height: 28px;
            margin-right: 15px;
            line-height: 28px;
            padding:0 10px 0 25px;
            border-radius: 4px;
            border: solid 1px #ddd;
            background: #f5f5f5;
            position: relative;
            &:before{
                content: '';
                position: absolute;
                height: 8px;
                width:8px;
                background: #999;
                border-radius: 4px;
                left: 8px;
                top:10px;
            }
        }
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
    .addItem{
        display:block;
        margin-left: 0;

    }
    .float{
        float: left;
    }
    .width{
        width:30%;
        padding-top: 15px;
        padding-left: 15px;
        border-right: solid #ddd 1px;
        &:first-of-type{
            padding-left:0;
        }
        &:last-of-type{
            border-right:none
        }
    }


    .formFrame{
        padding:0 40px;
        background: #fff;
    }

  .company{
      background: #fff;
      padding:20px 40px 40px 40px;
      header{
          border-bottom: solid 1px #ddd;
          padding-bottom: 15px;
          font-size: 18px;
          font-weight: normal;
          color: #666;
      }
      .need{
          margin-top: 30px;
          span{
              color: red;
          }
      }
  }
  .buttonFrame{
      border-top: solid 1px #ddd;
      padding-top: 40px;
      padding-left: 120px;
  }

    /* 上传样式*/
  .uploadPlus{
      height: 90px;
      &:before{
          position: absolute;
          left: 50%;
          top: -50%;
          height: 90px;
          transform: translateX(-50%);

      }

  }
    .companyLogo{
        width: auto!important;
    }
  .bg{
      background-color: #454545;
      color: #fff;
  }
  /*上传图片样式 */
  .avatar-uploader {
      background: #eee;
      box-sizing: border-box;
      // padding: 15px 0 0 15px;
      border-radius: 4px;
  }
  .uploadImgList {
      width: 90px;
      text-align: center;
      margin: 5px 0 5px 20px;
      position: relative;
  }
  .uploadPic {
      box-sizing: padding-box;
      li {
          font-size: 12px;
          margin: 5px;
          height: 90px;
          width: 90px;
          overflow: hidden;
          display: inline-block;
      }
  }
  .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
  }

  .avatar-uploader-icon {
      position: relative;
      background-color: #FF6419;
      width: 20px;
      height: 20px;
      margin: 38% auto;
      border-radius: 50%;
      .across {
          position: absolute;
          top: 9px;
          left: 2px;
          background-color: #fff;
          width: 16px;
          height: 2px;
      }
      .vertical {
          position: absolute;
          top: 2px;
          left: 9px;
          background-color: #fff;
          width: 2px;
          height: 16px;
      }
  }
</style>
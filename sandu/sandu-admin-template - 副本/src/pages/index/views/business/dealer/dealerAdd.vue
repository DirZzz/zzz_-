<template>
    <div class="mbottom">
        <div class="reBack"><b @click="gotoPrev">返回上一级 | 经销商企业管理列表 > </b> 新增</div>
        <div class="company">
            <header>
                新增经销商企业
            </header>
            <p class="need"><span>*</span>为必填项</p>
            <el-row :gutter="24" class="splineFrame">
                <el-col :span="2" class="productBaseInfo">经销商企业信息</el-col>
                <el-col :span="22" class="rightLine">
                    <hr>
                </el-col>
            </el-row>
        </div>
        <div class="formFrame">
            <el-form label-width="140px" class="demo-ruleForm" :model="dealerAddMsg" :rules="rules" ref="ruleForm">
                <el-row :gutter="12">
                    <el-col :span="10">
                        <el-form-item label="经销商企业名称：" prop="companyName">
                            <el-input maxlength="25" v-model.trim="dealerAddMsg.companyName" placeholder="请输入经销商企业名称"> </el-input>
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

                 <el-row :gutter="12" v-show="showIndustry">
                    <el-col :span="6">
                        <el-form-item label="所属行业：">
                            <input class="slh" @click="ChoseIndustry" v-model="industryNames"  placeholder="请选择所属行业">
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="12"  v-show="showIndustry">
                    <el-col :span="10">
                        <el-form-item label="产品可见范围：" prop="textareaChangeNameList">
                            <!--<el-select v-model="visibleProduct" placeholder="请选择产品可见范围">
                            </el-select>-->
                            <div class="arrowpos">
                                <input class="slh" :disabled="userType ==3" v-model="dealerAddMsg.textareaChangeNameList" @focus="showProductCategoryTree" placeholder="请选择产品可见范围">
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
                                <p v-for="(item, index) in addedBrands">{{item.brandName}}<span v-if="userType !=3" @click="delBrand(index,item)">X</span></p>
                                <!--<b style="color: red; font-weight: normal" v-if="!addedBrand.length">请选择品牌</b>-->
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="联系人姓名：">
                            <el-input  v-model.trim="dealerAddMsg.contactName"  placeholder="请输入联系人姓名"> </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="联系人电话：">
                            <el-input v-model.trim="dealerAddMsg.phone" placeholder="请输入联系人电话"> </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24" style="position: relative;">
                    <!--<span class="bitian">*</span>-->
                    <el-form-item label="区域：" >
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
                            <el-select clearable v-model="citySelect.origin" @change="getOriginCode(citySelect.origin)" placeholder="请选择街道">
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
                    </el-form-item>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="详细地址：">
                            <el-input style="width: 501px" v-model.trim="dealerAddMsg.companyAddress"  maxlength="32"  placeholder="请输入详细地址"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="经营范围：" prop="ranges">
                            <el-input v-model="dealerAddMsg.businessScope" placeholder="请输入经营范围"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="企业介绍：">
                            <el-input type="textarea" rows="5" v-model="dealerAddMsg.companyDesc" placeholder="限制0-500字符" maxlength="500" class="textareaInfo"> </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div class="buttonFrame">
                <el-button round class="cancelBtnBg bg" @click="resetForm('ruleForm')">重 置</el-button>
                <el-button round type="primary" class="confirmBtn" @click="confirmAddDealer('ruleForm')">保 存</el-button>
            </div>
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
        <el-dialog title="添加所属行业" :visible.sync="choseIndustry" class="info-ypop-up subtitle" width="540px">
           <div class="boxheader">
                <el-input type="text" :clearable="true" v-model="searchtype"></el-input>
                <el-button round class="searchbtn" @click="SearchType">搜索</el-button>
            </div>
            <p class="chosetype">选择分类：
                <span class="reset" @click="Reset">重置</span>
            </p>
            <div class="Industrybox">
                <el-checkbox-group v-if="industry_list[0]" v-model="modelList">
                    <el-checkbox v-for="(item,index) in industry_list" :key="index" class="checkbox" :label="item.value">{{item.name}}</el-checkbox>
                </el-checkbox-group>
                  <p v-else  style="text-align:center;margin-top:160px;">暂无数据</p>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button round type="primary"  @click="IndustryToExtend" >确 定</el-button>
                <el-button round @click="closeIndustry" >取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog
                title="添加品牌"
                :visible.sync="batchBrandDialogVisible"
                width="25%"
                @close="clearCHK"
                top="30vh"
                >
            <!--<el-input-->
                    <!--placeholder="请输入内容"-->
                    <!--prefix-icon="el-icon-search"-->
                    <!--v-model="batchBrandSearch">-->
            <!--</el-input>-->
            <el-checkbox-group v-model="addBrand.brandId">
                <div class="add-box">
                    <el-checkbox style="display: none"></el-checkbox>
                    <el-checkbox class="addItem" @change="brandShow(brand)" v-for="(brand, index) in brandlist"
                                 :label="brand.brandId"
                                 :key="brand.brandId">
                        {{brand.brandName}}
                    </el-checkbox>
                </div>
            </el-checkbox-group>
            <span slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="handleBatchAddConfirm">确 定</el-button>
        <el-button round type="info" @click="batchBrandDialogVisible = false;">取 消</el-button>
      </span>
        </el-dialog>
    </div>
</template>

<script>
    import { omit, get } from 'lodash';
    import qs from 'qs'
    export default {
        data(){
            return {   
                searchtype:'', 
                industryNames:"",
                oldIndusList:[],
                industry_list:[],
                modelList:[],
                choseIndustry:false,
                streetShow: true,
                brandListChk:[],
                lastTree:[],
                brandlist:[],
                batchBrandDialogVisible:false,
                batchBrandSearch: '',
                addBrand: {
                    brandId:[]
                },
                isCheckBrandName:'',
                tempBrand:[],
                addedBrand:[],
                addedBrands: [],
                indexOne:0,
                listOne:[],
                listTwo:[],
                listThree:[],
                dealerAddMsg:{
                    companyName: '', // 经销商经销商企业名称
                    textareaChangeNameList: '', // 经销商企业可见产品范围id
                    provinceCode:'', //  经销商企业省编码
                    cityCode: '', // 经销商企业市编码
                    areaCode: '', // 经销商企业区编码
                    streetCode: '',  // 经销商企业街道编码
                    companyAddress: '', // 经销商企业详细地址
                    companyDesc: '', // 经销商企业介绍
                    phone: '', // 经销商企业联系人电话
                    contactName: '', //经销商企业联系人姓名
                    brandIds: '', // 经销商企业品牌
                    businessScope: '', //经销商企业经营范围

                },
                infoClassifyVisible: false,
                options: [],
                dealerCategoryList:[],
                checkListOne: [],
                checkListTwo: [],
                checkListThree:[],

                checkCategoryListOne:[],
                selectTwoIndex: -1,

                checkCategoryListTwo:[],
                mycheckCategoryListTwo:[],
                mycheckCategoryListThree:[],

                selectThreeIndex: -1,
                checkCategoryListThree:[],
                userType: '',
                //区域选择
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
                cityCode: '',
                provide:[],
                provideSelect:[],
                citySelects:[],
                areaSelect: [],
                originSelect: [],
                ruleForm: { // 验证表单
                    brandName: '',
                    companyName: '',
                    contactName: '',
                    phone: '',
                    companyAddress: '',
                    area: '',
                    textareaChangeNameList: ''
                },
                // 验证表单规则
                rules: {
                    // provinceCodes: [
                    //     //{required: true, message: '请选择区域', trigger: 'blur'},
                    //     {validator: this.validAreas, trigger: 'blur'}
                    // ],
                    companyName: [
                       /* {required: true, message: '请输入经销商名称', trigger: 'blur'},
                        {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}*/
                        {required: true, validator: this.cnLen, trigger: 'blur'}
                    ],
                    brandName: [

                    ],
                    // contactName: [
                    //     {required: true, message: '请输入联系人名称', trigger: 'blur'},
                    //     {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}
                    // ],
                    valBrand:[
                        {   required: true,
                            validator: this.valBrand,
                            trigger: 'change'
                        }
                    ],
                    // phone: [
                    //     {required: true, message: '请输入电话号码', trigger: 'change'},
                    //     {
                    //         validator: this.validPhone,
                    //         trigger: 'blur'
                    //     }
                    // ],
                    ranges:[
                        {
                            validator: this.txtRange,
                            trigger: 'blur'
                        }
                    ],
                   /* companyDesc:[
                        {
                            validator: this.txtSize,
                            trigger: 'blur'
                        }
                    ],*/
                    // area: [
                    //     {required: true, message: '请选择区域', trigger: 'change'}
                    // ],
                    // companyAddress: [
                    //     {required: true, message: '请输入详细地址', trigger: 'blur'}
                    // ],
                    textareaChangeNameList:[
                        {required: true, message: '请选择产品可见范围', trigger: 'blur'}
                    ]
                },
                checkAll:false,
                firstIndex: 0,
                secondIndex: 0,
                threeIndex: 0,
                firstActiveArr: [],
                secondActiveArr: [],
                threeActiveArr: [],
                textareaChangeList: '',
                textareaChangeNameList:'',
                productVisibilityRangeNameId:[],
                // textareaChangeNameList:'',
                nowNameArr:[],
                DelBrandIdArr:[]
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
        computed: {
            showIndustry(){
            if(sessionStorage.getItem('companyType')==2||sessionStorage.getItem('companyType')==1) {
                    return true;
                }else{
                    return false;
                }
            }  
        },
        methods:{
            //新增经销商 行业回显接口
            AddIndustry(){
             this.API.addindustry({
                companyId: sessionStorage.getItem('companyID'),
             }).then( res => {
                  console.log(res.obj.companyIndustryNames);
                  this.industryNames = res.obj.companyIndustryNames;
                  let arr = res.obj.companyIndustrys.split(',').map(item =>{
                      return +item;
                  })
                  console.log();
                  this.modelList = arr;
                  this.oldIndusList =arr;
             })
            },
             //重置行业列表
            Reset() {
                this.searchtype = "";
                this.GetChiserindustryList();
                // for(let i=0;i<this.industry_list.length;i++){
                //    this.industry_list[i].isCheck = false;
                //  }
                this.modelList = [];
                console.log(this.modelList);
                },
            //模糊查询经销商  行业 列表
            SearchType(){
              this.GetChiserindustryList(this.searchtype);
            },
  //行业确定接口
  IndustryToExtend() {
     console.log(typeof(this.textareaChangeList));
      console.log(typeof(this.modelList.toString()));
      this.API.industrytoextend({
       newIndustryValues:this.modelList[0]?this.modelList.toString():undefined, 
       
       oldIndustryValues:this.oldIndusList?this.oldIndusList.toString():undefined,//this.oldIndusList?this.oldIndusList:undefined,//"",
       
        oldCategoryIds: this.textareaChangeList?this.textareaChangeList:undefined,
      }).then(res => {
        
        if (res.success) {
         
          this.textareaChangeList = res.obj.categoryIds.join();//把id赋到要保存的参数列表里面

          this.productVisibilityRangeNameId = res.obj.categoryIds.join();//把id赋进要勾勾的数组里面

           this.productCategoryTree()

          this.oldIndusList =res.obj.industryValues;//旧数组
      
          this.industryNames = res.obj.industryNames; //当前显示的行业名字

          this.choseIndustry = false; 
          this.dealerAddMsg.textareaChangeNameList = res.obj.categoryNames
        } else {
          this.$message.error(res.message);
        }
      });
    },
             //关行业弹框
            closeIndustry(){
                this.choseIndustry = false;
                this.modelList = this.oldIndusList;
            },
            //获取经销商所属行业列表
            GetChiserindustryList(value){
           console.log(typeof(this.bussenessId));
           console.log(typeof(parseInt(sessionStorage.getItem('companyID'))));
                if(value){
            this.API.searchchiserindustrylist({
             pId:parseInt(sessionStorage.getItem('companyID')),
             name:this.searchtype
             }).then(res => {
              this.industry_list = res.obj;
             })
                }else{
                    //获取经销商所属行业列表
              this.API.getchiserindustrylist({
                 companyPId:parseInt(sessionStorage.getItem('companyID')),
                 companyId:this.bussenessId 
             }).then(res => {
                 console.log(res.obj);
                 this.industry_list = res.obj;
             })
                }
           
            },
            //显示弹框
            ChoseIndustry(){
                this.choseIndustry = true;
                this.GetChiserindustryList();
            },
            clearCHK(){
                this.brandListChk = []
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
            cnLens(value){
                 return value.replace(/[\u0391-\uFFE5]/g,'aa').length;  //先把中文替换成两个字节的英文，在计算长度
             },
            cnLen(rule, value, callback) {
                if(value) {
                    if (this.cnLens(value) > 100) {
                        callback(new Error('请输入的汉字不能超过25个字符'))
                    } else {
                        callback();
                    }
                }
                if(value.length == 0 || value.length == null){
                    callback(new Error('请输入经销商企业名称'))
                }
            } ,

            /*选择区域校验*/
            validAreas(rule, value, callback) {
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
                console.log(typeof this.dealerAddMsg.companyDesc, 'size')
                if (this.dealerAddMsg.companyDesc.length > 500) {
                    callback(new Error('介绍信息在500字以内'))
                } else {
                    callback();
                }
            },*/
            txtRange(rule, value, callback){
                if (this.dealerAddMsg.businessScope.length > 50) {
                    callback(new Error('经营范围在50字以内'))
                } else {
                    callback();
                }
            },
            /*电话号码验证*/
            validPhone(rule, value, callback) {
                let mobileRegex = /^1[345789]\d{9}$/;
                let phoneRegex = /^0(\d{2,4})([-]{0,1})\d{3,8}$/;
                let fix400 = /^[48]00[-]{0,1}\d{6,7}$/; // /^400[0-9]{7}/
                if (fix400.test(value) || phoneRegex.test(value) || mobileRegex.test(value)) { //
                    callback();
                } else {
                    callback(new Error('电话号码格式不正确！'))
                }
            },
            valBrand(rule, value, callback){
                if (this.addedBrands.length == 0) {
                    callback(new Error('请添加品牌'))
                } else {
                    callback();
                }
            },
            oneTree(index) {
                // console.log(this.listOne)
                this.indexOne=index;
                this.listTwo = this.listOne[index].list;
                this.listThree = []
            },
            twoTree(index) {
                this.listThree = this.listOne[this.indexOne].list[index].list;
            },
            gotoPrev(){
                this.$router.push('/business/dealerBusiness')
            },
            addBrandDialog(){
                this.brandlist =[]
                this.getBrandList()
                this.batchBrandDialogVisible = true
            },
            getDefaultShow(){
                this.API.defaultShow({
                    companyId: sessionStorage.getItem('companyID'),
                    msgId: '1'
                }).then((res) =>{
                    if(res) {
                        if(res.obj.brandIds) {
                            this.dealerAddMsg.brandIds = res.obj.brandIds
                        }
                        if(res.obj.productVisibilityRange) {
                            this.textareaChangeList = res.obj.productVisibilityRange
                        }
                        this.productCategoryTree()
                        let  categoryName=[]
                        if (res.obj.brandIds) {
                            for (let i = 0; i < res.obj.brandIds.split(',').length; i++) {
                                this.addBrand.brandId.push(Number(res.obj.brandIds.split(',')[i]));
                            }
                        }
                        if(res.obj.productVisibilityRange) {
                            this.productVisibilityRangeNameId = res.obj.productVisibilityRange.split(',')
                        }
                        if(res.obj.productVisibilityRangeNameList)
                        {
                            res.obj.productVisibilityRangeNameList.map((item) => {
                                categoryName.push(item.categoryName)
                            })
                        }

                        this.dealerAddMsg.textareaChangeNameList = categoryName.join(',')
                        this.addedBrand =res.obj.brandList
                        this.addedBrands = res.obj.brandList
                    }
                })
            },
            getDealerUserType(){
                this.API.dealerUserType({
                    id: qs.parse(sessionStorage.getItem('loginUser')).id
                }).then((res) => {
                    if(res.obj.userType) {
                        this.userType = res.obj.userType;
                    }
                })
            },
            handleBatchAddConfirm(){
                // addBrand.brandId
                this.batchBrandDialogVisible = false
                // let arr = [...new Set(this.tempBrand)];
                let arr = [];
                for (let i = 0 ; i < this.addBrand.brandId.length; i++) {
                    for (let j =0 ; j < this.brandlist.length; j++) {
                        if ( this.addBrand.brandId[i] == this.brandlist[j].brandId){
                            arr.push(this.brandlist[j]);
                        }
                    }
                }
                this.addedBrands = arr
                let arrIDS = []
                this.addedBrands.map((item) => {
                    arrIDS.push(item.brandId)
                    this.dealerAddMsg.brandIds = arrIDS.join(',')
                })
            },
            brandShow(brand){
                this.tempBrand = [];
                if(brand) {
                    this.tempBrand.push(brand)
                }
            },
            delBrand(index,item) {
                this.addedBrands.splice(index, 1)
                let arrs = []
                this.addedBrands.map((item) => {
                    arrs.push(item.brandId)
                })
                this.dealerAddMsg.brandIds = arrs.join(',')
            },

            verifyProductClass() {
                let arr = [], newArr = [], newArrName = [] ;
                // console.log(this.threeActiveArr, 'wq')
                // console.log('qw', this.listOne)
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
                this.dealerAddMsg.textareaChangeNameList = newArrName.join(',')

            },
            showProductCategoryTree() {
                this.infoClassifyVisible = true;
                console.log(this.listOne, 'one')
            },
            productCategoryTree() {
                this.API.dealerCategory({
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
                    //console.log(this.threeActiveArr[val], 'valsssssssssssss')

                }
                this.threeActiveArr.map((item,index) => {
                    //console.log(item.categoryName)
                })
            },
            resetForm(formName){
                this.citySelect.provide = ''
                this.citySelect.city = ''
                this.citySelect.area = ''
                this.citySelect.origin = ''
                this.dealerAddMsg = {}
                this.addedBrand = []
                this.$refs[formName].resetFields();
            },
            getBrandList(){
                this.API.baseBrand({
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    this.brandlist = res.datalist;
                })
            },
            /*-----------------------区域选择------------------------------*/
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
            /*-----------------------区域选择结束-------------------------*/

            // getDealerDetail(){
            //     this.editId = this.$route.params.bid
            //     if(this.editId) {
            //         this.API.dealerDetail({id:this.editId}).then((res) => {
            //             if(res.success) {
            //                 this.search.provide = res.obj.provinceCode
            //                 this.search.city = res.obj.cityCode
            //                 this.search.area = res.obj.areaCode
            //                 this.search.origin = res.obj.streetCode
            //                 this.productVisibilityRangeNameId = res.obj.productVisibilityRange.split(',')
            //
            //
            //             }
            //         })
            //     }
            // },
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
                    companyId: sessionStorage.getItem('companyID'),
                    companyName: this.dealerAddMsg.companyName, // 经销商经销商企业名称
                    productVisibilityRange: this.textareaChangeList, // 经销商企业可见产品范围id
                    provinceCode:this.citySelect.provide, //  经销商企业省编码
                    cityCode: this.citySelect.city, // 经销商企业市编码
                    areaCode: this.citySelect.area, // 经销商企业区编码
                    streetCode: this.citySelect.origin,  // 经销商企业街道编码
                    companyAddress: this.dealerAddMsg.companyAddress, // 经销商企业详细地址
                    companyDesc: this.dealerAddMsg.companyDesc, // 经销商企业介绍
                    phone: this.dealerAddMsg.phone, // 经销商企业联系人电话
                    contactName: this.dealerAddMsg.contactName, //经销商企业联系人姓名
                    brandIds: this.dealerAddMsg.brandIds, // 经销商企业品牌
                    businessScope: this.dealerAddMsg.businessScope, //经销商企业经营范围
                   companyIndustrys:this.modelList?this.modelList.toString():undefined, //所属行业ID
                }
                this.$refs[formNames].validate((valid) => {
                    if (valid) {
                        this.$confirm('确定新增经销商企业吗?', '提示', {
                            confirmButtonText: '确定',
                            type: 'info',
                            cancelButtonClass: 'cancelButtonClass',
                            confirmButtonClass: 'confirmButtonClass',
                            center: true
                        }).then(() => {
                            this.API.dealerAdd(addParams).then((res) => {
                                if(res.message =='存在相同的企业名称') {
                                    this.$message.error('添加失败，存在相同的企业名称')
                                    return
                                 } else {
                                    this.$message.success('添加成功')
                                    this.$router.push('/business/dealerBusiness')
                                }
                            })
                        })
                    }
                })

            }
        },

        created() {
            this.getBrandList()
            this.getArea(0,'省')
            this.getDefaultShow()
            this.getDealerUserType();
            this.AddIndustry();
        }
    }
</script>

<style scoped lang="scss">
    @import "../../../assets/css/mixin";
    .textareaInfo{
        width: 501px;
    }
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
    .arrowpos{
        position: relative;
        width: 321px;
        &:after{
            position: absolute;
            content: '';
            top: 20px;
            right: -20px;
            width:0;
            height:0;
            border: 6px solid transparent;
            border-top-color: #999;//向下箭头
        }
    }
    .slh{
        border: solid 1px #ddd;
        height: 40px;
        border-radius:4px;
        padding:0 15px;
        width:321px;  /*必须设置宽度*/
        overflow: hidden;  /*溢出隐藏*/
        text-overflow: ellipsis; /*以省略号...显示*/
        white-space: nowrap;  /*强制不换行*/
        position: relative;
    }
    .bitian{
        position: absolute;
        color: red;
        left: 80px;
        top: 13px;
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
        margin-left: 0!important;

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
    .goods-operating-info{
        margin-bottom: 20px;
        .el-col-2{
            font-size: 14px;
            width: 110px;
            text-align: right;
            line-height: 40px;
            margin-left: 20px;
        }
        .el-col-22{
            line-height: 40px;
            p{
                display: inline-block;
                padding: 0 12px 0 12px;
                height: 12px;
                line-height: 12px;
                color: #333333;
                border-right: 1px solid #dddddd;
            }
        }
        .el-col-24{
            padding: 5px 0 5px 20px;
            height: 40px;
            line-height: 30px;
            h2{
                display: inline-block;
                font-size: 14px;
                width: 110px;
                text-align: right;
            }
            p{
                display: inline-block;
                padding: 0 12px 0 12px;
                height: 12px;
                line-height: 12px;
                color: #333333;
                border-right: 1px solid #dddddd;
            }
            span{
                color: #ff6419;
                margin-left: 10px;
                cursor:pointer
            }
        }
    }
    .goods-operating-infos{
        .el-col-2{
            font-size: 14px;
            width: 110px;
            text-align: right;
            line-height: 40px;
            margin-left: 20px;
        }
        .el-col-22{
            line-height: 40px;
            p{
                display: inline-block;
                padding: 0 12px 0 12px;
                height: 12px;
                line-height: 12px;
                color: #333333;
                border-right: 1px solid #dddddd;
            }
        }
        .el-col-24{
            padding: 5px 0 5px 20px;
            height: 40px;
            line-height: 30px;
            h2{
                display: inline-block;
                font-size: 14px;
                width: 110px;
                text-align: right;
            }
            p{
                display: inline-block;
                padding: 0 12px 0 12px;
                height: 12px;
                line-height: 12px;
                color: #333333;
                border-right: 1px solid #dddddd;
            }
            span{
                color: #ff6419;
                margin-left: 10px;
                cursor:pointer
            }
        }
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

    .Classify-list{
        border-top: 1px solid #dddddd;
        border-right: 1px solid #dddddd;
        .el-col-6{
            overflow: auto;
            height: 360px;
            border-bottom: 1px solid #dddddd;
            border-left: 1px solid #dddddd;
            .el-checkbox{
                margin: 0;
                padding: 10px 20px;
                height: 12px;
                line-height: 12px;
                color: #000000;
            }
            p{
                padding: 10px 20px;
                height: 12px;
                line-height: 12px;
                cursor: pointer;
                color: #000000;
                &:hover{
                    color: #ff6419;
                }
            }
            .p-active{
                color: #ff6419;
            }
        }
        .el-col-3, .el-col-4, .el-col-5{
            overflow: auto;
            height: 360px;
            border-bottom: 1px solid #dddddd;
            border-left: 1px solid #dddddd;
            p{
                padding: 10px 20px;
                height: 12px;
                line-height: 12px;
                display: flex;
                justify-content: space-between;
                cursor: pointer;
                color: #000000;
                &:hover{
                    color: #ff6419;
                }
            }
            .p-active{
                color: #ff6419;
            }
        }
    }

    .specification-info{
        .SKU-table-header{
            padding: 0;
            height: 30px;
        }
        .el-checkbox-group{
            display: inline-block;
            margin-left: 5px;
        }
        .el-col-24{
            border-top: 1px solid #dddddd;
            height: 74px;
            line-height: 74px;
            h2{
                display: inline-block;
                font-size: 14px;
                width: 110px;
                text-align: right;
            }
            .el-radio{
                margin-left: 20px;
            }
        }
        .el-col-2{
            margin-bottom: 20px;
            font-size: 14px;
            width: 110px;
            text-align: right;

        }
        .el-col-21, .el-col-20{
            padding-left: 10px;
            .el-checkbox-group{
                .el-checkbox{
                    width: 180px;
                    margin: 0 0 20px 0;
                }
            }
            .el-table{
                margin: 0 0 20px 0;
                span{
                    color: #ff6419;
                    cursor:pointer
                }
            }
        }
    }

    .company{
        background: #fff;
        padding:40px;
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
    .formFrame{
        padding:0 40px;
        background: #fff;
    }
    .bg{
        background-color: #454545;
        color: #fff;
    }
    .buttonFrame{
        background: #fff;
        padding-bottom: 40px;
        border-top: solid 1px #ddd;
        padding-top: 40px;
        padding-left: 120px;
    }
    .reBack{
        margin-bottom: 15px;
        b{
            cursor: pointer;
            color: #FF6419;
            font-weight: normal;
        }
    }
    .brandName{
        p{
            display: inline-block;
            height: 28px;
            line-height: 28px;
            padding:0 25px;
            border-radius: 4px;
            border: solid 1px #ddd;
            background: #f5f5f5;
            position: relative;
            margin-right: 15px;
            color: #333;
            span{
                position: absolute;
                background: #fff;
                border: solid 1px #ddd;
                right: 4px;
                top:5px;
                border-radius: 8px;
                width: 16px;
                height: 16px;
                font-size: 10px;
                text-align: center;
                line-height: 16px;
                color: #999;
                font-family: "Comic Sans MS";
                cursor: pointer;
                &:hover{
                    background: #FF6419;
                    color: #fff;
                }
            }
        }
    }
</style>
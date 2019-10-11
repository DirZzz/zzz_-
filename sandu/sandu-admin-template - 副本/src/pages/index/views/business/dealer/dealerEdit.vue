<template>
    <div class="mbottom">
        <div class="reBack"><b @click="gotoBackList">返回上一级 | 经销商企业管理列表 > </b> 编辑</div>
        <div class="company">
            <header>
                编辑经销商企业
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
            <el-form label-width="140px" class="demo-ruleForm" :model="dealerDetails" :rules="rules" ref="ruleForm">
                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="经销商企业名称：" prop="companyName">
                            <el-input v-model.trim="dealerDetails.companyName" placeholder="请输入经销商企业名称"> </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                 <!-- <el-row :gutter="12">
                    <el-col :span="10">
                        <el-form-item label="所属行业：">
                              <input @click="ChoseIndustry" class="slh"  placeholder="请选择所属行业">
                        </el-form-item>
                    </el-col>
                </el-row> -->

                <el-row :gutter="12" v-show="showIndustry">
                    <el-col :span="10">
                        <el-form-item label="所属行业：">
                            <input @click="ChoseIndustry" v-model="industryNames"  class="slh"  placeholder="请选择所属行业">
                        </el-form-item>
                    </el-col>
                </el-row> 

                <el-row :gutter="12" v-show="showIndustry">
                    <el-col :span="10">
                        <el-form-item label="产品可见范围：" prop="productVisibilityRangeNameList">
                            <!--<el-select v-model="visibleProduct" placeholder="请选择产品可见范围">
                            </el-select>-->
                            <div class="arrowpos">
                                <input class="slh" :disabled="userType ==3" v-model="dealerDetails.productVisibilityRangeNameList" @focus="showProductCategoryTree" placeholder="请选择产品可见范围">
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24">
                    <el-col :span="16" class="brandName">
                        <el-form-item label="品牌：" prop="brand">
                            <el-button type="text" :disabled="userType==3" @click="addBrandDialog">添加</el-button>
                            <div>
                                <!--<p>{{dealerDetails.brandName}} <span @click="delBrand(index)">X</span></p>-->
                                <p v-for="(item, index) in addedBrands" :key="index">{{item.brandName}} <span v-if="userType!=3" @click="delBrand(index,item)">X</span></p>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="联系人姓名：" >
                            <el-input  v-model.trim="dealerDetails.contactName"  placeholder="请输入联系人姓名"> </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="联系人电话："  prop="phone">
                            <el-input v-model.trim="dealerDetails.phone"  placeholder="请输入联系人电话"> </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24" style="position: relative">
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

                        <el-col :span="3"  v-if="streetShow">
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
                    <el-col :span="6">
                        <el-form-item label="详细地址：">
                            <el-input style="width: 501px" v-model.trim="dealerDetails.companyAddress" maxlength="32" placeholder="请输入详细地址"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="经营范围：" prop="ranges">
                            <el-input maxlength="50" v-model="dealerDetails.businessScope" placeholder="请输入经营范围"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="企业介绍：">
                            <el-input type="textarea"  rows="5" v-model="dealerDetails.companyDesc" maxlength="500" placeholder="限制0-500字符" class="textareaInfo"> </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div class="buttonFrame">
                <el-button round class="cancelBtnBg bg" @click="resetFrom('ruleForm')">重 置</el-button>
                <el-button round type="primary" class="confirmBtn" @click="confirmUpdateDealer('ruleForm')">保 存</el-button>
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

        <el-dialog
                title="添加品牌"
                :visible.sync="batchBrandDialogVisible"
                width="25%"
                top="30vh"
        >
            <!--<el-input-->
                    <!--placeholder="请输入内容"-->
                    <!--prefix-icon="el-icon-search"-->
                    <!--v-model="batchBrandSearch">-->
            <!--</el-input>-->
            <el-checkbox-group v-model="addBrand.brandId">
                <div class="add-box">

                    <el-checkbox class="addItem" @change="brandShow(brand)" v-for="(brand, index) in brandlist"
                                 :label="brand.brandId"
                                 :key="brand.brandId">
                        {{brand.brandName}}
                    </el-checkbox>
                </div>
            </el-checkbox-group>
            <span slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="handleBatchAddConfirm">确 定</el-button>
        <el-button round type="info" @click="batchBrandDialogVisible = false">取 消</el-button>
      </span>
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
                <el-checkbox-group v-if="industry_list" v-model="modelList">
                    <el-checkbox  v-for="(item,index) in industry_list" :key="index" class="checkbox" :label="item.value">{{item.name}}</el-checkbox>
                </el-checkbox-group>
                 <p v-else  style="text-align:center;margin-top:160px;">暂无数据</p>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button round type="primary"  @click="IndustryToExtend">确 定</el-button>
                <el-button round @click="closeIndustry" >取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        inject:['reload'],
        data(){
            return {
                searchtype:'',
                oldIndusList:[],
                industryNames:"",
                industry_list:[],
                bussenessId:'',
                modelList:[],
                choseIndustry:false,
                streetShow: true,
                visibilityRange: '',
                visibilityName: [],
                visibleId:[],
                indexOne:0,
                listOne:[],
                listTwo:[],
                listThree:[],
                userType: '', // 用户类型

                checkListOne: [],
                checkListTwo: [],
                checkListThree:[],
                nowNameArr:[],

                lastTree:[],
                brandlist:[],
                batchBrandSearch: '',
                addBrand: {
                    brandId:[]
                },
                tempBrand:[],
                addedBrand:[],

                infoClassifyVisible: false,
                batchBrandDialogVisible:false,
                editId: '',
                value: '',
                dealerDetails:{},
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
                brandId: [],
                cityCode: '',
                provide:[],
                provideSelect:[],
                citySelects:[],
                areaSelect: [],
                originSelect: [],
                //区域选择 结束------------
                // 验证表单规则
                rules: {
                    companyName: [
                        /* {required: true, message: '请输入经销商名称', trigger: 'blur'},
                         {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}*/
                        {required: true, validator: this.cnLen, trigger: 'blur'}
                    ],
                    // contactName: [
                    //     {required: true, message: '请输入联系人名称', trigger: 'blur'},
                    //     {min: 1, max: 50, message: '长度在 50 个字符', trigger: 'blur'}
                    // ],
                    phone: [
                        {
                            validator: this.validPhone,
                            trigger: 'blur'
                        },
                        // { message: '请输入电话号码', trigger: 'change'} // required: true,
                    ],
                    ranges:[
                        {
                            validator: this.txtRange,
                            trigger: 'blur'
                        }
                    ],
                    brand:[
                        {   required: true,
                            validator: this.valBrand,
                            trigger: 'change'
                        }
                    ],
                   /* companyDesc:[
                        {
                            validator: this.txtSize,
                            trigger: 'blur'
                        }
                    ],*/
                    // companyAddress: [
                    //     {required: true, message: '请输入详细地址', trigger: 'blur'}
                    // ],
                    productVisibilityRangeNameList:[
                        {required: true, message: '请选择产品可见范围', trigger: 'blur'}
                    ],
                    // provinceCode: [
                    //     // { required: true, message: '请选择区域', trigger: 'blur' },
                    //     {required: true,validator: this.validArea, trigger: 'blur'}
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
                productVisibilityRangeNameId: [],
                addedBrands:[]
                // productVisibilityRangeNameList:''
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
          this.dealerDetails.productVisibilityRangeNameList = res.obj.categoryNames
        } else {
          this.$message.error(res.message);
        }
      });
    },
            //关闭行业弹框
            closeIndustry(){
                this.choseIndustry = false;
                 this.modelList = this.oldIndusList;//回显一开始的行业列表
            },
            
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
            ChoseIndustry(){
                this.choseIndustry = true;
                this.GetChiserindustryList();
            },
            cnLens(value){
                return value.replace(/[\u0391-\uFFE5]/g,'aa').length;  //先把中文替换成两个字节的英文，在计算长度
            },
            cnLen(rule, value, callback) {
                if(value) {
                    if (this.cnLens(value) > 100) {
                        callback(new Error('请输入的汉字不能超过50个字符'))
                    } else {
                        callback();
                    }
                }
                if(value.length == 0 || value.length == null){
                    callback(new Error('请输入经销商企业名称'))
                }
            } ,
            /*选择区域校验*/
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
            /*电话号码验证*/
            validPhone(rule, value, callback) {
                if(value){
                    let mobileRegex = /^1[345789]\d{9}$/;
                    let phoneRegex = /^0(\d{2,4})([-]{0,1})\d{3,8}$/;
                    let fix400 = /^[48]00[-]{0,1}\d{6,7}$/; // /^400[0-9]{7}/
                    if (fix400.test(value) || phoneRegex.test(value) || mobileRegex.test(value)) { //
                        callback();
                    } else {
                        callback(new Error('电话号码格式不正确！'))
                    }
                }else{
                    callback();
                }
            },
            /*txtSize(rule, value, callback){
                if(this.dealerDetails.companyDesc) {
                    if (this.dealerDetails.companyDesc.length > 500) {
                        callback(new Error('介绍信息在500字条以内'))
                    } else {
                        callback();
                    }
                }
            },*/
            // txtRange(rule, value, callback){
            //     if (this.dealerDetails.businessScope.length > 50) {
            //         callback(new Error('经营范围在50字以内'))
            //     } else {
            //         callback();
            //     }
            // },
            valBrand(rule, value, callback){
                if (this.addedBrands == '') {
                    callback(new Error('请添加品牌'))
                } else {
                    callback();
                }
            },
            /*getDefaultShow(){
                this.API.defaultShow({
                    companyId: sessionStorage.getItem('companyID'),
                    msgId: '1'
                }).then((res) =>{
                    if(res) {
                        this.dealerAddMsg.brandIds = res.obj.brandIds
                        this.textareaChangeList = res.obj.productVisibilityRange
                        let  categoryName=[]
                        res.obj.productVisibilityRangeNameList.map((item) => {
                            categoryName.push(item.categoryName)
                        })
                        this.dealerAddMsg.textareaChangeNameList = categoryName.join(',')
                        this.addedBrand =res.obj.brandList
                    }
                })
            },*/
            getDealerUserType(){
                this.API.dealerUserType({
                    id: qs.parse(sessionStorage.getItem('loginUser')).id
                }).then((res) => {
                    if(res) {
                        this.userType = res.obj.userType;
                    }
                })
            },
            gotoBackList(){
                this.$router.push('/business/dealerBusiness')
            },
            resetFrom(formName){

                this.$confirm('确定还原经销商企业信息吗?', '提示', {
                    confirmButtonText: '确定',
                    type: 'info',
                    cancelButtonClass: 'cancelButtonClass',
                    confirmButtonClass: 'confirmButtonClass',
                    center: true
                }).then(() => {
                    this.getDealerDetail()
                    this.$message.success('经销商企业信息已还原成功')
                    this.$refs[formName].resetFields();
                })

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
            addBrandDialog(){
                this.batchBrandDialogVisible = true
            },
            // 获取详情
            getDealerDetail(){
                this.editId = this.$route.params.bid
                if(this.editId) {
                    this.API.dealerDetail({id:this.editId}).then((res) => {
                        if(res.success) {
                            this.dealerDetails = res.obj
                            this.resetDetailVal = res.obj
                            this.bussenessId = res.obj.id;
                            if(res.obj.companyIndustrys){
                                this.modelList = res.obj.companyIndustrys.split(",").map(item => {
                                return +item;
                                });
                                this.oldIndusList = this.modelList;
                                this.industryNames = res.obj.companyIndustryNames;
                            }
                            
                            //this.getArea()
                            console.log(res.obj, 'vvv')

                            
                            if(res.obj.brandList) {
                                this.addedBrand = res.obj.brandList
                                this.addedBrands = res.obj.brandList
                            }
                            if (res.obj.brandIds) {

                                for (let i = 0; i < res.obj.brandIds.split(',').length; i++) {
                                    this.addBrand.brandId.push(Number(res.obj.brandIds.split(',')[i]));
                                }
                            }
                            if(res.obj.productVisibilityRange) {
                                this.textareaChangeList = res.obj.productVisibilityRange
                            }
                            if(res.obj.productVisibilityRangeNameList)
                            {
                                this.dealerDetails.productVisibilityRangeNameList = res.obj.productVisibilityRangeNameList.map((item) => {
                                    return item.categoryName
                                })
                            }
                            if(res.obj.productVisibilityRange) {
                                this.productVisibilityRangeNameId = res.obj.productVisibilityRange.split(',')
                            }

                            if(res.obj.brandIds) {
                                this.brandId = res.obj.brandIds.split(',')
                            } else {
                                this.addedBrand = res.obj.brandIds.split(',')
                                this.addedBrand.map((ids) => {
                                    this.brandId.push(ids.brandId)
                                })
                            }

                            this.productCategoryTree()
                            if(res.obj.productVisibilityRange) {
                                this.visibleId = res.obj.productVisibilityRange
                            }
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

                          /*  this.search.provide = res.obj.provinceCode
                            this.search.city = res.obj.cityCode
                            this.search.area = res.obj.areaCode
                            this.search.origin = res.obj.streetCode*/
                        }
                    })
                }
            },

            // 编辑参数
            confirmUpdateDealer(formName){
                let condition = {
                    provinceCode:this.citySelect.provide, //经销商企业省编码 ,
                    cityCode:this.citySelect.city, //经销商企业市编码 ,
                    areaCode:this.citySelect.area, //经销商企业区编码 ,
                    streetCode:this.citySelect.origin, //经销商企业街道编码,
                    brandIds: this.brandId.join(','), //经销商企业品牌 ,
                    businessScope:this.dealerDetails.businessScope, //经销商企业经营范围 ,
                    companyAddress:this.dealerDetails.companyAddress, //经销商企业详细地址 ,
                    companyDesc:this.dealerDetails.companyDesc, //经销商企业介绍 ,
                    companyName:this.dealerDetails.companyName, //经销商企业名称 ,
                    contactName:this.dealerDetails.contactName, //经销商企业联系人姓名 ,
                    id:this.editId, //经销商企业id ,
                    phone:this.dealerDetails.phone, //经销商企业联系人电话 ,
                    productVisibilityRange: this.textareaChangeList, //经销商企业可见产品范围id
                    companyIndustrys:this.modelList?this.modelList.toString():undefined, //所属行业ID
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.API.dealerEdit(condition).then((res) => {
                            if(res.success) {
                                if(this.userType !=3) {
                                    this.$message.success('编辑成功')
                                    this.gotoBackList()
                                }
                                this.$message.success('编辑成功')
                                this.getDealerDetail();
                            }
                        })

                    }
                })

            },

            handleBatchAddConfirm(){
                /*this.addedBrand = []
                this.batchBrandDialogVisible = false
                let arr = [...new Set(this.tempBrand)]
                this.addedBrand = arr
                let arrIDS = []
                this.addedBrand.map((item) => {
                    arrIDS.push(item.brandId)
                })
                this.brandId = arrIDS;
                console.log(this.brandId, 'this.brandId')*/

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
                    this.brandId = arrIDS
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
                this.brandId = arrs
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
                this.dealerDetails.productVisibilityRangeNameList = newArrName.join(',')

            },
            showProductCategoryTree() {
                this.infoClassifyVisible = true;
            },
            productCategoryTree() {
                this.API.dealerCategory({
                    companyId:sessionStorage.getItem('companyID')
                }).then((res) => {
                    this.listOne=res.datalist;
                    this.listOne[0] ? this.listTwo = this.listOne[0].list : '';
                    this.listThree = this.listOne[0].list[0].list;
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
            // 区域选择-------------
            /*getAreaList (){
                this.API.areaList({areaCode:0}).then((res => {
                    if(res) {
                        this.provide = res.datalist
                        this.getProvideCode(this.dealerDetails.provinceCodeName)  // 从列表点编辑进来时初始区域
                    }
                }))
            },*/
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
                console.log(code);
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
            // 区域选择 结束----------
            getBrandList(){
                this.API.baseBrand({
                    companyId: sessionStorage.getItem('companyID')
                }).then((res) => {
                    this.brandlist = res.datalist
                })
            },

        },
        mounted(){
            this.getDealerUserType()
        },
        created(){
            this.getDealerDetail();
            this.getBrandList()
            this.getArea(0,'省')
            this.getDealerUserType();
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
             border-top-color: #999;//左箭头
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

    .reBack{
        margin-bottom: 15px;
        b{
            color: #FF6419;
            cursor: pointer;
            font-weight: normal;
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
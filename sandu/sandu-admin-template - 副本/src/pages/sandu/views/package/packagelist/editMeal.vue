<template>
    <div class="mains">
        <div class="mainBody">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/packagelist/list' }">套餐列表</el-breadcrumb-item>
                <el-breadcrumb-item>编辑套餐</el-breadcrumb-item>
            </el-breadcrumb>
            <div class="formRows addMeal">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
                    <el-form-item label="套餐类型" required prop="servicesType">
                        <el-select v-model="ruleForm.servicesType" placeholder="请选择">
                            <el-option label="正式" value="0"></el-option>
                            <el-option label="试用" value="1"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="套餐名称" required prop="servicesName">
                        <el-input v-model="ruleForm.servicesName" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="套餐描述">
                        <el-input type="textarea" v-model="ruleForm.serviceDesc"></el-input>
                    </el-form-item>
                    <el-form-item label="用户类型" required prop="userScope">
                        <el-select v-model="ruleForm.userScope" placeholder="请选择">
                            <el-option v-for="item in list1" :label="item.name" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="销售渠道" required prop="saleChannel">
                        <el-select v-model="ruleForm.saleChannel" multiple filterable  default-first-option collapse-tags  placeholder="请选择渠道">
                            <el-option v-for="item in list2":key="item.value" :label="item.name" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item> 
                    <el-form-item label="价格设置" required>
                        <el-form :model="item" label-width="0" class="demo-ruleForm lines magin30" size="small" v-for="(item,index) in ruleForm.prices">
                            <el-row :gutter="5">
                                <el-col :md="2" :lg="3" >
                                    <el-form-item>
                                        <el-input-number @change="priceFun(index)"  :min="0" :precision="0"  :max="999999"  v-model="item.price" placeholder="套餐价格"></el-input-number >
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
                                        <el-input-number :min="0" :precision="0"  :max="9999"  v-model="item.giveDuration" placeholder="赠送时长"></el-input-number >
                                        <span class="placeholders">赠送时长</span>
                                    </el-form-item>
                                </el-col>
                                <el-col :md="1" :lg="1" ><p class="labels">天</p></el-col>
                                <el-col :md="2" :lg="3" >
                                    <el-form-item>
                                        <el-input-number :min="0" :precision="0"  :max="999999" v-model="item.sanduCurrency" placeholder="赠送度币"></el-input-number>
                                        <span class="placeholders">赠送度币</span>
                                    </el-form-item>
                                </el-col>
                                <el-col :md="2" :lg="3" >
                                    <el-form-item>
                                        <el-input-number :min="0" :precision="0"  :max="99" v-model="item.freeRenderDuration" placeholder="免费渲染时长"></el-input-number>
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
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input type="textarea" v-model="ruleForm.remark" clearable></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')" round>立即修改</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
import qs from 'qs'
export default {
  name: "packagelist",
  data() {
    return {
        imgurl: process.env.sourceBaseUrl,
        list1:[],
        list2:[],
        servicesId:'',
        ruleForm: {},
        priceFlag:[{flag:false},{flag:false},{flag:false},{flag:false},{flag:false}],
        durationFlag:[{flag:false},{flag:false},{flag:false},{flag:false},{flag:false}],
        loading:'',
        detail:{},
        rules: {
            servicesType:[{ required: true, message: '请输入套餐类型', trigger: 'change' }],
            servicesName:[{ required: true, message: '请输入套餐名称', trigger: 'blur' },{ required: true, message: '请输入套餐名称', trigger: 'change' }],
            userScope:[{ required: true, message: '请输入用户类型', trigger: 'change' }],
            saleChannel:[{ required: true, message: '请输入销售渠道', trigger: 'change' }],
        },
    };
  },
  created() {
      this.init();
  },
  methods: {
    init(){
        this.servicesId = this.$route.query.id;
        this.API.getDictionaryInfo({type:'userType'}).then((res)=>{ this.list1 = res.obj;})
        this.API.getDictionaryInfo({type:'saleChannel'}).then((res)=>{ this.list2 = res.obj;})
        this.getServiceDetail(this.$route.query.id);
    },
    priceFun(i){
        this.priceFlag[i].flag = !this.ruleForm.prices[i].price;
    },
    durationFun(i){
        this.durationFlag[i].flag = !this.ruleForm.prices[i].duration;
    },
    getServiceDetail(id){
        this.API.getServiceDetail({id: id}).then(res=>{
            let obj = res.obj;
            obj.servicesType=obj.servicesType.toString()
            obj.userScope=parseInt(obj.userScope);
            obj.saleChannel = obj.saleChannel.split(',')
            obj.saleChannel.map((v,index)=> { obj.saleChannel[index] = parseInt(v)})
            this.ruleForm = obj;
        })
    },
    loadingFunc(){
        this.loading = this.$loading({lock: true, text: 'Loading', spinner: 'el-icon-loading', customClass:'Loadings', background: 'rgba(250, 250, 250, 0.7)'});
    },
    submitForm(formName) {
        
        let ruleForm = this.ruleForm;
        let flags = false;
        ruleForm.prices.map((v,i)=>{
            if(v.price<=0){
                flags = true;
                this.priceFlag[i].flag = true;
            }else{
                this.priceFlag[i].flag = false;
            }
            if(v.duration<=0){
                flags = true;
                this.durationFlag[i].flag = true;
            }else{
                this.durationFlag[i].flag = false;
            }
        })
        this.$refs[formName].validate((valid) => {
            if(valid&!flags){
                this.loadingFunc();
                ruleForm.saleChannel = ruleForm.saleChannel?ruleForm.saleChannel.join(','):'';
                ruleForm.id = this.servicesId;
                ruleForm.modifier = sessionStorage.getItem('userId');
                this.API.updateMeal(ruleForm).then(res=>{
                    if(res.success){
                        this.$message.success('套餐修改成功！');
                        setTimeout(res=>{
                            this.$router.push({path: '/packagelist/list'});
                        },500)
                    }else{
                        this.$message.error(res.message);
                        this.empty();
                        ruleForm.saleChannel = Array.from(ruleForm.saleChannel);
                    }
                    this.loading.close();
                })
            }
        });
    },
    empty(){
        this.getServiceDetail(this.servicesId);
    },
    resetForm(formName) {
        this.getServiceDetail(this.$route.query.id);
    },
    addMore(){
          if(this.ruleForm.prices.length>=5){
              this.$message({
                message: '最多添加5条数据',
                type: 'warning'
            });
              return;
          }
          this.ruleForm.prices.push({"duration": '',"freeRenderDuration": '',"id":0,"giveDuration": '',"price": '',"priceUnit": "0","sanduCurrency": ''});
    },
    delMore(index){
        this.ruleForm.prices.splice(index,1)
        this.priceFlag[index].flag = false;
        this.durationFlag[index].flag = false;
    },
  } 
};
</script>

<style scoped lang="scss">
.reds{
    color: #f56c6c;
}
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
    .formRows{
        margin-top: 20px;
        width: 100%;
        padding: 0;
        width: 100%;
        .labels{
            height: 100%;
            line-height: 32px;
            text-align: center;
            font-size: 14px;
        }
        .placeholders{
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
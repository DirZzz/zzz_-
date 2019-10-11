<template >
    <!-- 发货 -->
    <el-dialog title="修改配送信息" :visible.sync="visible" width="650px"  >
        <el-form :rules="rules" ref="ruleForm" :model="form" label-width="100px">
            <el-form-item label="姓名：" prop="consignee">
                <el-input v-model="form.consignee"></el-input>
            </el-form-item>
            <el-form-item label="电话：" prop="mobile" style="margin-bottom: 0">
                <el-input v-model="form.mobile"></el-input>
            </el-form-item>
            <el-row style="font-size: 0">
                <span class="setWidth"><em>*</em>省：</span>
                <el-select v-model="citySelect.provide"
                           @change="getProvideCode"
                           style="width: 510px"
                           clearable placeholder="请选择省">
                    <el-option  v-for="(item, index) in provide"
                                :key="item.areaCode"
                                :label="item.areaName"
                                :value="item.areaCode">
                    </el-option>
                </el-select>
            </el-row>


            <el-row style="font-size: 0">
                <span class="setWidth"><em>*</em>市：</span>
                <el-select v-model="citySelect.city"
                           @change="getCityCode"
                           style="width: 510px"
                           clearable placeholder="请选择市">
                    <el-option   v-for="(item,index) in citySelects"
                                 :key="item.areaCode"
                                 :label="item.areaName"
                                 :value="item.areaCode">
                    </el-option>
                </el-select>
            </el-row>
            <el-row style="font-size: 0">
                <span class="setWidth"><em>*</em>区：</span>
                <el-select v-model="citySelect.area"
                           @change="getAreaCode"
                           style="width: 510px"
                           clearable placeholder="请选择区">
                    <el-option  v-for="(item, index) in areaSelect"
                                :key="item.areaCode"
                                :label="item.areaName"
                                :value="item.areaCode">
                    </el-option>
                </el-select>
            </el-row>
            <el-form-item label="详细地址：" prop="address" style="margin-top: 20px">
                <el-input type="textarea" v-model="form.address"></el-input>
            </el-form-item>
            <el-form-item label="邮编：">
                <el-input  v-model="form.zipcode" maxlength="6"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button ref="btnSend" @click="doSave()" type="primary" size="mini" round>确 定</el-button>
            <el-button @click="doClose()" size="mini" round>取 消</el-button>
        </div>
    </el-dialog>
</template>

<script>
import imall from '@/api/imall'
import {print} from '@/utils/tools'
import city from '@/filters/city'
import store from '@/store'
export default{
    name:'orderPrint',
    mixins:[city],
    data(){
        return {
            orderId:0
            ,visible:false
            ,loadingVisible:true
            ,form:{
                consignee:''
                ,mobile:''
                ,province:''
                ,city:''
                ,area:''
                ,address:''
                ,zipcode:''
            }
            ,expressCompanys:[
                {label:'顺丰快递',value:'顺丰快递'},
                {label:'圆通快递',value:'圆通快递'}
            ],
            citySelect:{
                provide: '',
                city: '',
                area: '',
                origin: ''
            },
            rules:{
                consignee: [
                    { required: true, message: '请输入姓名', trigger: 'blur' },
                    { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
                ],
                mobile:[
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { validator:function(rule,value,callback){            //一层校验
                            if(/^400-([0-9]){1}([0-9-]{7})$/.test(value)){
                                callback();
                            }else{
                                if(/^(13[0-9]|14[56789]|15[0-3,5-9]|16[6]|17[021345678]|18[0-9]|19[89])\d{8}$/.test(value)){
                                    callback();
                                }else {
                                    callback(new Error("请输入正确的电话号码"));
                                }
                            }

                        }, trigger: 'blur'},
                    // { pattern: /^(13[0-9]|14[56789]|15[0-3,5-9]|16[6]|17[021345678]|18[0-9]|19[89])\d{8}$/, message: '请输入正确的手机号' }
                    // ^400-([0-9]){1}([0-9-]{7})
                ],
                address:[
                    { required: true, message: '请输入详细地址', trigger: 'blur' },
                    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
                ]
            }
        }
        
    },
    inject:['refreshMethod'],
    created() {
        //this.getData()
    },
    mounted () {
        this.$nextTick(function () {
            let that = this
        })
    },
    methods:{
        show(addressData){
            this.visible=true
            if (addressData!=undefined){
                this.orderId=addressData.orderId;
                this.loadingVisible = true
                this.form=addressData;
                this.citySelect.provide=addressData.provinceName;
                this.citySelect.city=addressData.cityName;
                this.citySelect.area=addressData.areaName;
                this.loadingVisible = false
            }
            
        },
        doSave(){//发货
            this.$refs['ruleForm'].validate((valid) => {
                if(valid){
                    if(this.citySelect.provide==''){
                        this.$message.error('请输入省');
                        return
                    }else if(this.citySelect.city==''){
                        this.$message.error('请输入市');
                        return
                    }else if(this.citySelect.area==''){
                        this.$message.error('请输入市区');
                        return
                    }
                    let that=this
                    that.loadingVisible=true;
                    this.mallApi.updateImallOrderAddress(that.form).then(data => {
                        //操作中
                        //that.$refs.btnAffirm
                        if (data.code==200){
                            that.$message.success("修改配送地址成功")
                            that.visible=false
                            that.refreshMethod()//回调父窗口刷新
                        }else{
                            that.$message.warning("修改配送地址异常:"+data.message)
                        }
                        that.loadingVisible=false
                    })
                }
            });

        }
        ,doClose(){//关闭
            this.visible=false
        }
    }
}
</script>
<style lang="scss" scoped>
    .setWidth{
        display: inline-block;
        width: 80px;
        height: 40px;
        line-height: 40px;
        text-align: right;
        padding-right: 20px;
        margin-top: 20px;
        font-size: 14px;
        color: #606266;
        em{
            color: #FF0000;
            margin-right: 5px;
        }
    }
</style>
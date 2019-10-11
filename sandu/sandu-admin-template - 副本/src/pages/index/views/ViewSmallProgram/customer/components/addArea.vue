<template>
    <div>
        <div class="addArea"><span class="text">填写区域：</span><em @click="addAreaList">添加</em></div>
        <el-row class="citySection" v-for="(res,index) in areaList.allCode" :key="index">
            <el-col :span="6">
                <el-select v-model="res.provinceName"
                           @change="getChangeProvince"
                           @focus="getIndex(index)"
                           clearable placeholder="请选择省">
                    <el-option  v-for="item in provide"
                                :key="item.areaCode"
                                :label="item.areaName"
                                :value="item.areaCode">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="6" style="margin: 0 10px">
                <el-select v-model="res.cityName"
                           @change="getChangeCity"
                           @focus="getIndex(index)"
                           clearable placeholder="请选择市">
                    <el-option   v-for="item in citySelects"
                                 :key="item.areaCode"
                                 :label="item.areaName"
                                 :value="item.areaCode">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="6">
                <el-select v-model="res.areaCodeName"
                           @change="getChangeArea"
                           @focus="getIndex(index)"
                           clearable placeholder="请选择区">
                    <el-option  v-for="item in areaSelect"
                                :key="item.areaCode"
                                :label="item.areaName"
                                :value="item.areaCode">
                    </el-option>
                </el-select>
            </el-col>
            <i class="ic_del" v-if="index!=0" @click="deleteAreaList(index)"></i>
        </el-row>
    </div>

</template>

<script>
    import city from '@/filters/city'
    export default {
        name: "addArea",
        mixins:[city],
        data(){
            return{
                //添加区域
                index:0,
                areaList:{
                    allCode:[{
                        areaCode:'',
                        cityCode:'',
                        provinceCode:'',
                        id:-1
                    }],
                    channelCompanyId:sessionStorage.getItem('channelCompanyId'),
                    companyId:sessionStorage.getItem('companyID'),
                    userName:sessionStorage.getItem('userId')
                }
            }
        },
        created(){

        },
        methods:{
            addAreaList(){
                this.areaList.allCode.push({
                    areaCode:'',
                    cityCode:'',
                    provinceCode:'',
                    id:-1
                });
            },
            getChannelCompanyList(allCode,val){
                allCode.length>=1 ? this.areaList.allCode=[]:'';
                this.areaList.allCode=[...this.areaList.allCode,...allCode];
                val ? this.areaList.channelCompanyId=val:'';
            },
            deleteAreaList(index){
                this.areaList.allCode.splice(index,1);
            },
            getChangeProvince(val){
                this.citySelects =[];
                this.areaSelect = [];
                this.areaList.allCode[this.index].cityName='';
                this.areaList.allCode[this.index].areaCodeName='';
                this.areaList.allCode[this.index].provinceCode=val;
                this.getArea(val,'市');
            },
            getChangeCity(val){
                this.areaSelect = [];
                this.areaList.allCode[this.index].cityCode=val;
                this.getArea(val,'区');
            },
            getChangeArea(val){
                console.log(val)
                this.areaSelect = [];
                this.areaList.allCode[this.index].areaCode=val;
            },
            getIndex(index){
                this.index=index;
            },
            save(){
                console.log(this.areaList)
                this.API.addCompany(this.areaList).then(res=>{
                    if(res.code==200){
                        this.$message({
                            message: '恭喜你，操作成功',
                            type: 'success'
                        });
                        this.$emit('closeMd');
                    }else {
                        this.$message.error(res.message);
                    }
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    .citySection{
        padding-bottom: 20px;
        padding-left: 120px;
        .ic_del{
            display: inline-block;
            width: 18px;
            height: 18px;
            background: no-repeat center url("../../../../assets/images/icons/ic_del.png");
            background-size: 100%;
            position: relative;
            top: 10px;
            left: 10px;
        }
    }
    .addArea{
        padding: 20px 0;
        color: #333;
        cursor: pointer;
        em{
            font-style: normal;
            color: #ff6419;
        }
    }
    .text{
        display: inline-block;
        width: 120px;
        text-align: right;
    }
</style>
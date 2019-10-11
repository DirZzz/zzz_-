<template>
    <div class="mbottom">
        <div class="reBack"><b  @click="gotoBackList">返回上一级 | 经销商企业管理列表 > </b> 详情</div>
        <div class="company">
            <header>
                经销商企业详情
            </header>

            <el-row :gutter="24" class="splineFrame">
                <el-col :span="2" class="productBaseInfo">经销商企业信息</el-col>
                <el-col :span="22" class="rightLine">
                    <hr>
                </el-col>
            </el-row>
        </div>

        <div class="formFrame">
            <el-form label-width="130px" class="demo-ruleForm">
                <el-row :gutter="24">
                    <el-col :span="16">
                        <el-form-item label="经销商企业名称：">
                            {{dealerDetails.companyName}}
                        </el-form-item>
                    </el-col>
                </el-row>
                 <el-row :gutter="24">
                    <el-col :span="20" class="brandName" v-show="showIndustry">
                        <el-form-item label="所选行业：" >
                            <div>
                                <p v-for="item in companyIndustryNames">{{item}}</p>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24" v-show="showIndustry">
                    <el-col :span="20" class="brandName">
                        <el-form-item label="产品可见范围：">
                            <div>
                                <p v-for="item in visibilityRange">{{item}}</p>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24">
                    <el-col :span="12" class="brandName">
                        <el-form-item label="品牌：">
                            <div>
                                <p v-for="item in addedBrand">{{item}}</p>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="联系人姓名：">
                            {{dealerDetails.contactName}}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="联系人电话：">
                            {{dealerDetails.phone}}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24">
                    <el-form-item label="区域：">
                        {{dealerDetails.provinceCodeName}}{{dealerDetails.cityCodeName}}{{dealerDetails.areaCodeName}}
                    </el-form-item>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="详细地址：">
                            {{dealerDetails.companyAddress}}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="6">
                        <el-form-item label="经营范围：">
                            {{dealerDetails.businessScope || '暂无经营范围'}}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-form-item label="企业介绍：">
                            {{dealerDetails.companyDesc || '暂无介绍'}}
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>

        </div>

    </div>
</template>

<script>
    export default {
        data(){
            return {
                companyIndustryNames:[],
                editId: '',
                dealerDetails:{},
                addedBrand:[],
                visibilityRange:[]
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
        methods: {
            gotoBackList(){
                this.$router.push('/business/dealerBusiness')
            },
            // 获取详情
            getDealerDetail(){
                this.editId = this.$route.params.bid
                if(this.editId) {
                    this.API.dealerDetail({id:this.editId}).then((res) => {
                        console.log(res, 'detial')
                        if(res.success) {
                            this.dealerDetails = res.obj
                            console.log(this.dealerDetails, 'dealerDetails');
                            if(res.obj.companyIndustryNames){
                                this.companyIndustryNames = res.obj.companyIndustryNames.split(",");
                                
                            }
                            if(res.obj.brandList) {
                                res.obj.brandList.map((item) => {
                                    this.addedBrand.push(item.brandName)
                                    console.log(this.addedBrand, 'brandname..')
                                })
                            }
                            if (res.obj.productVisibilityRangeNameList) {
                                res.obj.productVisibilityRangeNameList.map((item) => {
                                    this.visibilityRange.push(item.categoryName)
                                    console.log(this.visibilityRange, 'visibilityRange..')
                                })
                            }
                        }
                    })
                }
            },
        },
        created() {
            this.getDealerDetail()
        },
    }
</script>

<style scoped lang="scss">
    .reBack{
        margin-bottom: 15px;
        b{
            color: #FF6419;
            cursor: pointer;
            font-weight: normal;
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
    .reBack{
        margin-bottom: 15px;
        b{
            color: #FF6419;
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
</style>
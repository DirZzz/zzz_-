<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>春节活动管理</span>
        </div>
        <div class="searchFrame">
            <el-button type="primary" @click="closeForm">新建活动</el-button>
            <el-button type="primary" @click="exportData" :disabled="!multipleSelection.length">导出数据</el-button>
        </div>
        <div class="tableList">
            <el-table
                    :data="tableData"
                    :header-cell-style="rowStyle"
                    @selection-change="handleSelectionChange"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%"
                    align="center"
                    >
                <el-table-column type="selection">
                </el-table-column>
                <!-- <el-table-column label="序号" type="index" align="center">
                </el-table-column> -->
                <el-table-column label="活动ID" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.id }}</template>
                </el-table-column>

                <el-table-column label="序号" type="index" align="center">
                    
                </el-table-column>

                <el-table-column label="活动名称" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.wheelName }}</template>
                </el-table-column>
                    
                <el-table-column label="拼图电影票数量" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.filmToalNum }}
                    </template>
                </el-table-column>
                    
                <el-table-column label="每日抽奖红包金额" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.rewardDayAmount }}</template>
                </el-table-column>
          
                <el-table-column label="抽奖红包总金额" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.rewardTotalAmount }}</template>
                    <!--<template slot-scope="scope">{{ scope.row.redPacketDayNum }}</template>-->
                </el-table-column>

                    
                <el-table-column label="每日签到红包金额" header-align="center" align="center">
                    <template slot-scope="scope">{{ scope.row.redPacketDayNum }}</template>
                </el-table-column>

                <el-table-column label="签到红包总金额" header-align="center" align="center"> 
                    <template slot-scope="scope">{{ scope.row.redPacketNum }}</template>
                    <!--<template slot-scope="scope">{{ scope.row.rewardTotalAmount }}</template>-->
                </el-table-column>


                <el-table-column label="活动起止时间" header-align="center" align="center"> 
                    <template slot-scope="scope">{{ scope.row.wheelBegin }}<br/>{{ scope.row.wheelEnd }}</template>
                </el-table-column>

                <el-table-column
                        label="操作" header-align="center" align="center">
                    <template slot-scope="scope">
                        <el-button size="mini" v-clipboard:copy="scope.row.activityPath" v-clipboard:success="onCopy" v-clipboard:error="onError">复制活动链接</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="query.page"
                        :page-sizes="[5,10, 50, 100, 200,500]"
                        :page-size="query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>



        <div class="zzcs" v-if="showForm">
        <div class="formRows">
            <p class="titles">新建活动<i class="el-icon-close" @click="closeForm"></i></p>
            <el-form ref="form" :model="formData" :rules="rules">
                <el-form-item label="关联转盘">
                    <el-select v-model="formData.wxActLuckyWheelId" @change="wheelChange" size="small" clearable
                               placeholder="请选择转盘活动">
                        <el-option v-for="item in wheelList"
                                   :key="item.wheelId"
                                   :label="item.wheelName"
                                   :value="item.wheelId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="活动名称">
                    {{this.activityName}}
                </el-form-item>
                <el-form-item label="活动起止时间">
                    <span v-if="this.beginTime">{{this.beginTime}} &emsp;至&emsp; {{this.endTime}}</span>
                </el-form-item>
                <el-form-item label="现金抽奖总金额">
                    {{this.rewardAmount}}
                </el-form-item>
                <el-form-item label="每日现金抽奖金额">
                    {{this.rewardDayAmount}}
                </el-form-item>
                <el-form-item label="拼图电影票数量" prop="filmToalNum">
                    <el-input v-model="formData.filmToalNum" maxlength="5"></el-input>
                </el-form-item>
                <el-form-item label="每日签到红包金额" prop="redPacketDayNum">
                    <el-input v-model="formData.redPacketDayNum" maxlength="8"></el-input>
                </el-form-item>
                <el-form-item label="签到红包总金额" prop="redPacketNum">
                    <el-input v-model="formData.redPacketNum" maxlength="8"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="cretedFunc">立即创建</el-button>
                    <el-button @click="closeForm">取消</el-button>
                </el-form-item>
            </el-form>
            
        </div>
    </div>

    </div>
</template>

<script>

    export default {
        name: "coustomer",
        data() {
            return {
                activityName:'',
                beginTime:'',
                endTime:'',
                rewardAmount:'',
                rewardDayAmount:'',
                formData:{
                    wxActLuckyWheelId:'',
                    filmToalNum:'',
                    redPacketDayNum:'',
                    redPacketNum:'',
                },
                wheelList:[],
                categoryId:[],
                categoryAlllist:[],
                total: 0, // 总页数
                mobile: '',
                customerStatus: '', // 客户状态
                queryParam:'',
                categoryList: [
                ],
                loading: true,
                streetShow: true,
                 showForm:false,
                tableData: [],
                listTableData: [],
                multipleSelection:[],
                query: {
                    page: 1,
                    limit: 10,
                },
                rules:{
                    filmToalNum:[
                        {required: true, message: '请输入电影票数量', trigger: 'blur'},
                        {validator: this.checkFilmNum, trigger: 'blur'}
                    ],
                    redPacketDayNum:[
                        {required: true, message: '请输入每日签到红包金额', trigger: 'blur'},
                        {validator: this.checkRedPacketDayNum, trigger: 'blur'}
                    ],
                    redPacketNum:[
                        {required: true, message: '请输入签到红包总金额', trigger: 'blur'},
                        {validator: this.checkRedPacketNum, trigger: 'blur'}
                    ]
                }
            }
        },

        
        created() {
            this.getListData();
            this.getWheelList();
        },
        methods: {
            onCopy() {
                console.log('heheheh11111111111e')
                this.$message.success("拷贝活动链接成功!");
            },
            onError() {
                console.log('hehehehe')
                this.$message.warning("拷贝活动链接失败!");
            },
            checkFilmNum(rule, value, callback) {
                let regu = /^[1-9]\d*$/;
                if (regu.test(value)) {
                    callback();
                } else {
                    callback(new Error("请输入1-9的正整数"));
                }
            },
            checkRedPacketDayNum(rule, value, callback) {
                let regu = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
                if (regu.test(value)) {
                    callback();
                } else {
                    callback(new Error("请输入正确的金额数(可保留两位小数)"));
                }
            },
            checkRedPacketNum(rule, value, callback) {
                let regu = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
                if (regu.test(value)) {
                    callback();
                } else {
                    callback(new Error("请输入正确的金额数(可保留两位小数)"));
                }
            },
            exportData() {
                    if(this.multipleSelection.length > 1){
                        this.$message.error('每次只能导出一个活动数据!')
                        return
                    }
                    let activityIds = []
                    let wheelIds = []
                    this.multipleSelection.map(function (value) {
                        activityIds.push(value.id)
                        wheelIds.push(value.wxActLuckyWheelId);
                    })
                this.loadingTwo = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                });
                    this.API.exportSpringActivity(
                        {
                            activityId : activityIds.join(','),
                            wheelId: wheelIds.join(',')
                        }
                    ).then((result) => {
                        if(result&& result.size > 0) {
                            this.loadingTwo.close()
                            let url = window.URL.createObjectURL(result)
                            let link = document.createElement('a')
                            link.style.display = 'none'
                            link.href = url
                            link.setAttribute('download', '春节活动数据导出.xls')
                            document.body.appendChild(link)
                            link.click()
                        }else{
                            this.loadingTwo.close()
                            this.$message.error("没有数据需要导出!")         
                        }
                    })
            },
            wheelChange() {
                this.API.getWheelInfo({id:this.formData.wxActLuckyWheelId}).then(res => {
                    if(res.code == 200) {
                        this.activityName = res.data.wheelName;
                        this.beginTime = res.data.wheelBegin;
                        this.endTime = res.data.wheelEnd;
                        this.rewardAmount = res.data.rewardTotalAmount;
                        this.rewardDayAmount = res.data.rewardDayAmount;
                    }else{
                        this.activityName = "";
                        this.beginTime = "";
                        this.endTime = "";
                        this.rewardAmount = "";
                        this.rewardDayAmount = "";
                        this.$message.error(res.message)
                    }
                });
            },
            getWheelList() {
                this.API.getWheelList().then(res => {
                    this.wheelList = res.data;
                });
            },
            handleSelectionChange (val) {
                this.multipleSelection = val
            },
            cretedFunc(){
                if(this.formData.wxActLuckyWheelId == null) {
                    this.$message.error("请填写正确的活动数据")
                    return false;
                }
                let regu = /^[1-9]\d*$/;
                let regu2 = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/;
                if(!regu.test(this.formData.filmToalNum)){
                    this.$message.error("请填写正确的活动数据")
                    return false;
                }
                if(!regu2.test(this.formData.redPacketDayNum) || !regu2.test(this.formData.redPacketNum)) {
                    this.$message.error("请填写正确的活动数据")
                    return false;
                }
                
                let data = {   
                    wxActLuckyWheelId:this.formData.wxActLuckyWheelId,
                    filmToalNum:this.formData.filmToalNum,
                    redPacketDayNum:this.formData.redPacketDayNum,
                    redPacketNum:this.formData.redPacketNum,
                    creator:sessionStorage.getItem('userId'),
                }
                // this.createActivity(data);
                this.API.addSpringActivity(data).then(res=>{
                    if(res.code == 200) {
                        this.closeForm();
                        this.$message.success('创建活动成功')
                        this.getListData();
                    }else{
                        this.$message.error(res.message)
                    }
                });
            },
            
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '60px!important',background:'#f5f5f5',color: '#666'}
                }
            },
            
            getListData() {
                let queryCondition = Object.assign({}, this.query, {
                    queryParam:this.queryParam
                })
                // 删除空参数
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                setTimeout(() => {
                    this.loading = false;

                    this.API.getSpringActivityList(queryCondition).then(res => {
                     this.tableData = res.data;
                     this.total = res.total
                    });
                }, 1000);
            },

            closeForm(){
                this.showForm = !this.showForm;
                this.formData={};
                this.activityName = '';
                this.beginTime = '';
                this.endTime = '';
                this.rewardAmount = '';
                this.rewardDayAmount = '';
            },
           
            
            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size
                this.getListData()
            },
            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage
                this.loading = true
                this.getListData()
            },

            
        } //end methods
    }
</script>

<style scoped lang="scss">
    /*@import "../../assets/css/mixin.scss";*/
    .customerFrame{
        margin:20px;
    }
    .changeColor {
        color: #ff2323;
    }

    .customerTitle {
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        text-align: left;
        border-bottom: solid 1px #ddd;
        span {
            display: inline-block;
            padding-left: 20px;
        }
    }

    .searchFrame {
        background: #fff;
        text-align: left;
        padding: 20px;
    }

    .areaStyle {
        margin-top: 20px;
        .el-select {
            width: 216px;
            margin-right: 10px;
        }
    }

    .selectSearchCond {
        .el-select, .el-input {
            width: 216px;
            margin-right: 10px;
        }
    }

    .btnFrame {
        padding: 10px 0;
        text-align: center;
        border-top: solid 1px #ddd;
        background: #fff;
        font-size: 18px;
        color: #333;
        p {
            text-align: center;
        }
    }

    .tableList {
        margin-top: 20px;
        padding:20px;
        background: #fff;
    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .zzcs{
        width: 100%;
        height: 100%;
        position: fixed;
        left: 0;
        top: 0;
        z-index: 2000;
        background: rgba(0,0,0,.7);
        .rows{
            width: 25%;
            padding-top: 50px;
            position: absolute;
            text-align: center;
            background: #fff;
            left: 35%;
            top: 50%;
            height: 205px;
            margin-top: -102.5px;
            border-radius: 5px;
            .titles{
                height: 50px;
                width: 100%;
                border-bottom: 1px solid #eaeaea;
                position: absolute;
                left: 0;
                top: 0;
                font-size: 18px;
                line-height: 50px;
                i{
                    width: 50px;
                    height: 50px;
                    position: absolute;
                    right: 0;
                    top: 0;
                    line-height: 50px;
                    cursor: pointer;
                }
            }
            .bodys{
                width: 100%;
                border-bottom: 1px solid #eaeaea;
                padding: 40px 0;
            }
            .btns{
                padding: 15px 20px;
                text-align: right;
                font-size: 17px;
            }
        }
        .formRows{
            height:750px !important;
            min-width: 610px;
            width: 50%;
            position: absolute;
            left: 25%;
            top: 30%;
            padding: 0 20px;
            border-radius: 5px;
            background: #fff;
            height: 318px;
            margin-top: -159px;
            .titles{
                height: 50px;
                width: 100%;
                border-bottom: 1px solid #eaeaea;
                font-size: 18px;
                line-height: 50px;
                margin-bottom: 20px;
                position: relative;
                i{
                    width: 50px;
                    height: 50px;
                    position: absolute;
                    right: -20px;
                    top: 0;
                    line-height: 50px;
                    cursor: pointer;
                    text-align: center;
                }
            }
        }
    }
</style>
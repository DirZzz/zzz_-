<template>
<div id="activity">
    <div class="mains">
        <div class="titles">活动管理</div>
        <div class="mainBody">
            <div class="rowTOp">
                <el-form :inline="true" :model="params" class="demo-form-inline">
                    <el-button type="primary" @click="closeForm">新建活动</el-button>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit">查询</el-button>
                    </el-form-item>
                    <el-form-item label="">
                        <el-input v-model="params.name" placeholder="请输入活动名称" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="活动状态">
                        <el-select v-model="params.statusCode" placeholder="全部">
                        <el-option label="全部" value=""></el-option>
                        <el-option label="未开始" value=0 ></el-option>
                        <el-option label="进行中" value=10 ></el-option>
                        <el-option label="已结束" value=20 ></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </div>
            <div class="tableDataFrame">
                <el-table :data="dataList" :header-cell-style="rowStyle" :border="borderBlo" element-loading-text="拼命加载中"
                     element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)"
                     tooltip-effect="dark">
                    <el-table-column prop="name" label="活动名称" align="center">
                        <template slot-scope="scope">{{scope.row.name}}</template>
                    </el-table-column>
                    <el-table-column prop="href" label="活动预算（元）" align="center">
                        <template slot-scope="scope">{{scope.row.totalAmount|toFix}}</template>
                    </el-table-column>
                    <el-table-column prop="beginTime" label="活动起止时间" align="center" width="420px">
                        <template slot-scope="scope">{{scope.row.beginTime}} ——— {{scope.row.endTime}}</template>
                    </el-table-column>
                    <el-table-column prop="statusCode" label="操作" align="center">
                        <template slot-scope="scope">
                            <el-button size="mini" v-clipboard:copy="scope.row.copyUrl" v-clipboard:success="onCopy">复制链接</el-button>
                            <span class="ttspan" v-if="!scope.row.statusCode">未开始</span>
                            <el-button v-if="scope.row.statusCode==10" size="mini" type="danger" @click="chooseActivity(scope.row.id)">结束活动</el-button>
                            <span class="ttspan" v-if="scope.row.statusCode==20">已结束</span>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pageCount">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="params.start"
                            :page-sizes="[10, 20, 30, 40]"
                            :page-size="params.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="this.totalCount">
                    </el-pagination>

                </div>
            </div>
        </div>
    </div>
    <div class="zzcs" v-if="showZzcs">
        <div class="rows">
            <p class="titles">提示 <i class="el-icon-close" @click="closeZzc"></i></p>
            <div class="bodys">确认结束该活动？</div>
            <div class="btns">
                <el-row>
                    <el-button  @click="closeZzc" size="mini">取消</el-button>
                    <el-button type="danger" @click="closeActivity" size="mini">确认结束</el-button>
                </el-row>
            </div>
        </div>
    </div>
    <div class="zzcs" v-if="showForm">
        <div class="formRows">
            <p class="titles">新建活动<i class="el-icon-close" @click="closeForm"></i></p>
            <el-form ref="form" :model="formData" label-width="80px">
                <el-form-item label="活动名称">
                    <el-input v-model="formData.actName" @change="actNameFun" maxlength=49 clearable></el-input>
                </el-form-item>
                <el-form-item label="活动金额">
                    <el-input-number  @change="actAmountFun" v-model="formData.actAmount" controls-position="right" :precision="2" :max="99999.99" :step="0.1" :min="0"></el-input-number> 元
                    <!-- <el-input v-model="formData.actAmount"></el-input> -->
                </el-form-item>
                <el-form-item label="活动时间">
                    <el-date-picker v-model="formData.beginTime" type="datetime" @change="beginTimeFun"
                    placeholder="请设置起始时间" align="right" :picker-options="pickerOptionsStart"></el-date-picker>
                     -- 至 --
                    <el-date-picker v-model="formData.endTime" type="datetime" @change="endTimeFun"
                    placeholder="请设置结束时间" align="right" :picker-options="pickerOptionsEnd"></el-date-picker>
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
import qs from 'qs'

    export default {
        name:'activity',
        data() {
            return {
                formInline:{
                    user: '',
                    region:'',
                },
                formData:{
                    actName:'',
                    actAmount:'',
                    beginTime:'',
                    endTime:'',
                },
                duration: 2000,
                showZzcs:false,
                showForm:false,
                borderBlo: true,
                closeId:'',
                dataList:[],
                params:{
                    name:'',
                    statusCode: '',
                    start: 1,
                    limit: 10,
                },
                rules: {
                    actName:'活动名称不能为空',
                    actAmount:'活动预算不能小于等于0',
                    beginTime:'活动开始时间不能为空',
                    endTime:'活动结束时间不能为空',
                },
                pickerOptionsStart:{
        		    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                },
                pickerOptionsEnd:{
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7
                    }
                },
                totalCount:0,
            }
        },
        components: {
        },
      
        filters:{
            toFix(val){
                return parseFloat(val).toFixed(2);
            }
        },
        computed:{
            userInputFlag() {
                let flag = true, title = ''
                for (let key in this.formData) {
                    if (!this.formData[key]) {
                        flag = false
                        title = this.rules[key]
                        break
                    }
                }
                return { flag, title }
            }
        },
        created(){
            this.getActivityList();
        },
        methods: {
            onSubmit(e){
                this.params.start = 1;
                this.getActivityList();
            },
            onCopy(e){
                this.$message({
                    message: '复制成功！！！',
                    type: 'success'
                });
            },
            actNameFun(){
                this.formData.actName = this.formData.actName.trim();
            },
            cretedFunc(){
                if (!this.userInputFlag.flag) {
                    
                    this.showErrorToast(this.userInputFlag.title)
                    return
                }
                let data = {    
                    actName:this.formData.actName,
                    actAmount:this.formData.actAmount,
                    beginTime:''+this.getNowTime(this.formData.beginTime),
                    endTime:''+this.getNowTime(this.formData.endTime),
                }
                console.log(data.actName.length);
                this.createActivity(data);
            },
            createActivity(data){
                this.API.createActivity(qs.stringify(data)).then(res=>{
                    if(res.success){
                        this.closeForm();
                        this.showSuccessToast('新建活动成功');
                        this.getActivityList();
                    }else{
                        this.showErrorToast(res.message);
                    }
                })
            },
            getActivityList(){
                let data = {};
                data.name = this.params.name;
                data.start = this.params.start;
                data.limit = this.params.limit;
                this.params.statusCode?(data.statusCode=parseInt(this.params.statusCode)):'';
                this.API.getActivityList(data).then((res)=>{
                    if(res.success){
                        this.dataList = res.datalist
                        this.totalCount = res.totalCount
                    }
                })
            },
            showErrorToast(txt){
                this.$notify.error({
                    title: '错误',
                    message: txt,
                    duration: this.duration,
                });
            },
            showSuccessToast(txt){
                this.$notify({
                    title: '成功',
                    message: txt,
                    type: 'success',
                    duration: 2000
                });
            },
            showMessaget(txt){
                this.$message.error(txt);
            },
            closeForm(){
                this.showForm = !this.showForm;
                this.formData.actName = '';
                this.formData.actAmount = '';
                this.formData.beginTime = '';
                this.formData.endTime = '';
            },
            getAddNum(val){
                return val<=9?("0"+val):val;
            },
            getNowTime(type){
                var date = type?(new Date(type)):(new Date());
                var year = date.getFullYear();
                var month = this.getAddNum(date.getMonth() + 1);
                var strDate = this.getAddNum(date.getDate());
                var hours = this.getAddNum(date.getHours());
                var minutes = this.getAddNum(date.getMinutes());
                var seconds = this.getAddNum(date.getSeconds());
                var currentdate = year + '-' + month + '-' + strDate + ' ' + hours + ':' + minutes + ':' + seconds;
                return currentdate;
            },

            closeActivity() {
                this.API.finishActivity(qs.stringify({actId: this.closeId})).then((res)=>{
                    if(res.success){
                        this.getActivityList();
                        this.closeZzc();
                        this.showSuccessToast('结束活动成功');
                    }else{
                        this.showErrorToast(res.message);
                    }
                })
            },
            chooseActivity(index){
                this.closeId = index;
                this.showZzcs = !this.showZzcs;
            },
            closeZzc(){
                this.showZzcs = !this.showZzcs;
            },
            rowStyle({row, rowIndex}) {
                if (rowIndex === 0) {
                    return {
                        height: "30px!important",
                        background: "#f5f5f5",
                        color: "#666",
                        lineHeight: "80px!important",
                    };
                }
            },
            indexMethod(index) {
                return index + 1;
            },
            beginTimeFun(res){
                let nowTime = new Date(Date.parse(this.getNowTime()));
                let flag = !this.formData.beginTime||this.formData.beginTime>=nowTime;
                if(!flag){
                    this.formData.beginTime = '';
                    this.showMessaget("开始时间必须大于等于当前时间！")
                    return;
                }
                this.pickerOptionsEnd = Object.assign({},this.pickerOptionsEnd,{
                    disabledDate: (time) => {
                        var beginTime = new Date(this.formData.beginTime);
                        var timts =this.formData.beginTime? beginTime.getTime(): Date.now() - 8.64e7;
                        return time.getTime() < timts;
                    }
                })
            },
            
            endTimeFun(res){
                let nowTime = new Date(Date.parse(this.getNowTime()));
                let flag = !this.formData.endTime||this.formData.endTime>=nowTime;
                if(!flag){
                    this.formData.endTime = '';
                    this.showMessaget("结束时间必须大于等于当前时间！")
                    return;
                }
                if(this.formData.beginTime==''){
                    this.formData.beginTime = this.getNowTime();
                }
                if(this.formData.beginTime>this.formData.endTime){
                    this.formData.endTime = '';
                    this.showMessaget("开始时间必须小于结束时间！")
                    return;
                }
                  
            },
            // 分页模块f
            handleSizeChange: function (size) {
                //this.query.limit = size
                this.params.limit = size;
                this.params.start=1;
                this.getActivityList()
            },
            handleCurrentChange: function (currentPage) {
                //this.query.page = currentPage;
                this.params.start = currentPage;
                this.loading = true;
                this.getActivityList()
            },
            actAmountFun(e){
                this.formData.actAmount = this.formData.actAmount.toFixed(2);
                console.log(666);
            }
        }
    }
</script>

<style scoped lang="scss">
div{
    box-sizing: border-box;
}
.ttspan{
    display: inline-block;
    padding: 0 15px;
}
#activity{
    width: 100%;
    height: 100%;
    padding: 20px;
    font-size: 16px;
    color: #333;
    text-align: left;
    overflow-y: scroll;
    .mains{
        width: 100%;
        min-height: 100%;
        background: #fff;
        padding:0 20px;
        .titles{
            width: 100%;
            height: 55px;
            line-height: 55px;
            border-bottom: 1px solid #eaeaea;
        }
        .mainBody{
            padding-top: 30px;
            .rowTOp{
                margin-bottom: 30px;
                .el-form-item{
                    margin: 0;
                    float: right;
                    margin-left: 30px;
                }
            }
        }
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
            min-width: 610px;
            width: 50%;
            position: absolute;
            left: 25%;
            top: 40%;
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
}
    
</style>
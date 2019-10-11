<template>
    <transition name="fade">
    <div class="decorCustomer">
        <header class="topBar">
            <div class="barTitle">
                <span>已签约客户查询</span>
            </div>
            <div class="searchOption">
                <el-input
                        placeholder="请输入业主姓名"
                        v-model="userName"
                        @blur="customerSignedList"
                        size="small" clearable style="width: 222px"
                        clearable>
                </el-input>
                <el-input
                        placeholder="请输入业主联系电话"
                        v-model="mobile"
                        @blur="customerSignedList"
                        size="small" clearable style="width: 222px"
                        clearable>
                </el-input>
                <el-select v-model="searchContractStatus" clearable @change="customerSignedList" size="small" placeholder="请选择合同状态">
                    <el-option
                            v-for="item in contractStatus"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>

                <el-select v-model="searchFinanceReceiptsStatus" clearable @change="customerSignedList" size="small" placeholder="请选择是否收款">
                    <el-option
                            v-for="item in financeReceiptsStatus"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
            <div class="footer">
                <span class="queryBtn" @click="customerSignedList">查询</span>
                <span class="queryReset" @click="resetFrom">重置</span>
            </div>
        </header>
        <section>
            <div class="listTitle">客户信息
                <div class="right">
                    <el-button @click="handleDownload" size="small" type="primary" :disabled="!multipleSelection.length">导出Excel</el-button>
                </div>
            </div>
            <div class="tableDataFrame">
                <el-table
                    :data="customerList"
                    v-loading="loading"
                    :header-cell-style="rowStyle"
                    style="padding-top:20px"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    @selection-change="handleSelectionChange"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    tooltip-effect="dark"
            >
                <el-table-column type="selection">
                </el-table-column>
                <el-table-column prop="listIndex" width="50px" label="序号">
                </el-table-column>
                <el-table-column prop="userName" label="名称"> </el-table-column>
                <el-table-column prop="mobile" label="联系电话"> </el-table-column>
                <el-table-column prop="cityName" label="城市"> </el-table-column>
                <el-table-column prop="companyName" label="签约装企"> </el-table-column>
                <el-table-column prop="contractFee" label="签约价格(元)"> </el-table-column>
                <el-table-column prop="contractStatus" label="合同状态">
                    <template slot-scope="scope">
                        <span v-if="scope.row.contractStatus==0">待上传</span>
                        <span v-if="scope.row.contractStatus==1">待审核</span>
                        <span v-if="scope.row.contractStatus==2">审核通过</span>
                    </template>
                </el-table-column>
                <el-table-column prop="contractUrl" label="合同">
                    <template slot-scope="scope">
                        <span v-if="scope.row.contractUrl"><a :href="downloadUrl+scope.row.contractUrl" style="color: #3a8ee6">查看</a></span>
                        <span v-else>———</span>
                    </template>
                </el-table-column>
                <el-table-column prop="serviceFee" label="服务费"> </el-table-column>
                <el-table-column prop="financeReceiptsStatusStr" label="是否已收款">
                </el-table-column>
                <el-table-column prop="name" label="操作" width="200px">
                    <template slot-scope="scope">
                        <el-button type="text" @click="uploadCon(scope.row.id)" v-if="scope.row.contractStatus==0&&permission('decorate:signedUp:contract')">上传合同</el-button>
                        <el-button type="text" @click="refundMod(scope.row.id)"  v-if="scope.row.financeReceiptsStatus==0&&permission('decorate:signedUp:manage')" >更改收款状态</el-button>
                    </template>
                </el-table-column>

            </el-table>
                <div class="pageCount">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page.sync="query.page"
                            :page-sizes="[10, 50, 100, 200,500]"
                            :page-size="query.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="total">
                    </el-pagination>
                </div>
            </div>
        </section>
        <el-dialog
                style="text-align: left;"
                title="上传合同"
                :visible.sync="uploadContract"
                width="620px">
            <div class="contractContent">
                <p><b><span>*</span> 上传合同：</b> {{fileShow}}</p>
                <p>
                    <el-upload
                            class="upload-demo"
                            :action="uploadUrl"
                            :show-file-list="false"
                            :before-upload="beforeUpload"
                            :on-success="handleSuccess"
                            accept=".pdf,.PDF,.doc,.DOC,.docx,.DOCX"
                            :data="uploadParams"
                            >
                        <el-button size="mini"><i class="el-icon-upload2"></i> 上传合同</el-button>
                        <div slot="tip" class="el-upload__tip uploadFileTip">{{uploadFileTip}}</div>
                    </el-upload>
                </p>
                <!--oninput="value=value.replace(/[^\d{1,}\.\d{1,}]/g,''); if(value<=0) value=1"-->
                <p><b><span>*</span> 签约价格(元)：</b>
                    <el-input size="small"
                              @input="calu"
                              v-model="uploadContractParams.contractFee"
                              placeholder="请输入签约价格">
                    </el-input>
                </p>
                <p><b><span>*</span> 服务费(元)：</b>
                    <el-input size="small"
                              oninput="value=value.replace(/[^\d{1,}\.\d{1,}]/g,''); if(value<=0) value=0"
                              v-model="uploadContractParams.serviceFee"
                              placeholder="请输入服务费">
                    </el-input></p>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button size="small" @click="closeDialogContract">取 消</el-button>
                <el-button size="small" type="primary" @click="confirmContract">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog
                style="text-align: left;"
                title="更改收款状态"
                :visible.sync="refundVisible"
                width="620px">
            <div class="contractContent">
                是否收款：
                <el-select v-model="isRefund" size="mini" placeholder="请选择收款状态">
                    <el-option
                            v-for="item in financeReceiptsStatus"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button size="small" @click="refundVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="isRefundModify">确 定</el-button>
            </div>
        </el-dialog>
    </div>
    </transition>
</template>

<script>
    import '../../utils/md5';

    export default {
        data(){
            return {
                downloadUrl:process.env.sourceBaseUrl,
                uploadUrl:process.env.baseUrl + '/v1/storage/uploadFile',
                loading:true,
                isRefund:'',
                multipleSelection:[],
                uploadSuccess:{
                    resId:'',
                    url:''
                },
                index:'',
                uploadContract:false,
                total:0,
                uploadContractParams:{
                    contractFee:0, //签约金额
                    id:'',
                    platformType:0, // 上传平台类型(0-商家后台上传;1-移动B端上传)
                    resFileId:'',//合同id ,
                    serviceFee:0 // 服务费
                },
                query:{
                    page:1,
                    limit:10
                },
                customerList:[],
                financeReceiptsStatus:[
                    {value:0,label:'待收款'},
                    {value:1,label:'已收款'},
                ],
                contractStatus: [
                    {value:0,label:'待上传'},
                    {value:1,label:'待审核'},
                    {value:2,label:'审核通过'}

                ],
                uploadFileTip:'只能上传word文件，且不超过5M',
                fileShow:'', // 上传文件名
                refundVisible:false,
                searchContractStatus:'',
                searchFinanceReceiptsStatus:'',
                userName: '',
                mobile: '',
                pickdate:'',

                pickerOptions1: {
                    shortcuts: [{
                        text: '今天',
                        onClick(picker) {
                            picker.$emit('pick', new Date());
                        }
                    }, {
                        text: '明天',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24);
                            picker.$emit('pick', date);
                        }
                    }, {
                        text: '后天',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24 * 2);
                            picker.$emit('pick', date);
                        }
                    }]
                },
                tableDataList:[],
                fileList:[{name:'',url:''}],
                rowId:''
            }
        },
        methods:{
            calu(){
                this.uploadContractParams.serviceFee = (this.uploadContractParams.contractFee * 0.05).toFixed(2)
            },
            // 导出excel 表
            handleDownload() {
                require.ensure([], () => {
                    const { export_json_to_excel } = require('@/vendor/Export2Excel')
                    const tHeader = ['序号', '名称', '联系电话','城市','签约装企','签约价格(元)','合同状态', '合同','服务费(签约价*5%)','是否已收款']
                    const filterVal = ['listIndex', 'userName', 'mobile','cityName','companyName','contractFee', 'contractStatus','contractId','serviceFee','financeReceiptsStatusStr']
                    const list = this.multipleSelection
                    const data = this.formatJson(filterVal, list)
                    export_json_to_excel(tHeader, data, '签约管理文件')
                })
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },
            //-------------------------------------------
            resetFrom(){
                this.searchContractStatus = ''
                this.searchFinanceReceiptsStatus= ''
                this.userName = ''
                this.mobile = ''
                this.customerSignedList()
            },
            rowStyle({ row, rowIndex}) {
                if (rowIndex === 0) {
                    return{height: '30px!important',background:'#f5f5f5',color: '#666',lineHeight:'80px!important'}
                }
            },
            uploadCon(id){
                this.rowId=id
                this.uploadContract = true
            },
            refundMod(id){
                this.rowId = id
                this.refundVisible=true;
            },
            isRefundModify(){
                this.API.updateRefundStatus({
                    financeReceiptsStatus: this.isRefund,
                    id: this.rowId
                }).then((res) => {
                    console.log(res)
                    this.customerSignedList()
                    this.refundVisible = false
                    this.$message.success('收款状态更新成功')
                })
            },
            // 上传合同
            /*
                contractFee (integer): 签约金额 ,
                id (integer, optional),
                platformType (integer): 上传平台类型(0-商家后台上传;1-移动B端上传) ,
                resFileId (integer): 合同id ,
                serviceFee (integer): 服务费
            */
            confirmContract(){
                this.uploadContract = false;
                if(!this.uploadContractParams.contractFee){
                    this.$message.error('请输入签约价格')
                    return
                }

                if(!this.uploadContractParams.serviceFee){
                    this.$message.error('请输入服务费用')
                    return
                }

                if(!this.fileShow){
                    this.$message.error('请上传合同')
                    return
                }

                let params = {
                    contractFee:Number(this.uploadContractParams.contractFee),
                    serviceFee:this.uploadContractParams.serviceFee,
                    id:this.rowId,
                    platformType:0,
                    resFileId:this.uploadSuccess.resId
                }
               this.API.uploadContractURL(params).then((res) => {
                   console.log(res)
                   if(res){
                       this.customerSignedList()
                       this.$message.success('合同上传成功')
                   }
               })
            },
            closeDialogContract(){
                this.uploadContract = false
                this.fileShow = ''
                this.uploadContractParams.contractFee = ''
                this.uploadContractParams.serviceFee = ''
            },

            customerSignedList(){
                let formDatas = Object.assign({},this.query,{
                    contractStatus:this.searchContractStatus,
                    financeReceiptsStatus:this.searchFinanceReceiptsStatus,
                    refundStatus:'',
                    companyId:'',
                    userName: this.userName,
                    mobile: this.mobile
                })

                for (let key in formDatas) {
                    if (formDatas[key]!==0 && formDatas[key]=='') {
                        delete formDatas[key]
                    }
                }
                this.API.signedUpList(formDatas).then((res)=>{
                    if(res.list){
                        res.list.map((item,index) => {
                            item.listIndex=index+1;
                        })
                        this.total = res.total
                        this.customerList = res.list
                    }else{
                        this.total = 0;
                        this.customerList = [];
                    }

                    this.loading=false
                })
            },
            handleSelectionChange (val) {
                this.multipleSelection = val
            },

            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size
                this.customerSignedList()
            },

            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage
                this.loading = true
                this.customerSignedList()
            },
            // 上传 doc 文件
            beforeUpload(file) {
                console.log(file,'files')
                const docx = file.type === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
                const doc = file.type === 'application/msword'
                const isLt2M = file.size / 1024 / 1024 < 5
                this.fileShow = file.name
                if (!(doc||docx)) {
                    this.uploadFileTip = '上传的文件只能是 doc 或 docx 格式!'
                    this.fileShow = ''
                }

                if (!isLt2M) {
                    this.uploadFileTip = '文件大小不能超过 5M!'
                    this.fileShow = ''
                }

                return (doc||docx) && isLt2M
            },
            handleSuccess(res){
                this.uploadSuccess = res.data
            },
            //------------------------- doc 文件
        },
        computed:{
            uploadParams(){
                return {'filekey': 'contract'}
            },
            /*serviceFee(){
              return this.uploadContractParams.contractFee * 0.05
            }*/
        },
        created(){
            this.customerSignedList()
        }
    }
</script>

<style scoped lang="scss">
    @import "../../assets/css/common.scss";
    .decorCustomer{
        padding:20px;
    }
    .exportExcel{
        position: absolute;
        right: 32px;
        top: 0;
        span{
            display: inline-block;
            text-align: center;
            font-size: 14px;
            width: 82px;
            height: 32px;
            line-height: 32px;
            background-color: $theme-color;
            color: #fff;
            border-radius: 4px;
        }
    }
    .listTitle{
        font-size: 16px;
        color: #333;
        height: 56px;
        text-align: left;
        line-height: 56px;
        background: #fff;
        margin-top: 20px;
        border-bottom: solid 1px #e8e8e8;
        padding-left: 32px;
        position: relative;
        .right{
            @extend .exportExcel
        }
    }
   .tableDataFrame{
       padding:0 20px;
       background: #fff;
   }


    .topBar{
        background: #fff;
        div{
            text-align: left;
        }
        .footer{
            margin-top: 20px;
            border-top: solid 1px #e8e8e8;
            text-align: center;
            padding:20px  0 ;
            display: block;
        }
        .barTitle{
            border-bottom: solid 1px #e8e8e8;
            padding-left: 32px;
            font-size: 16px;
            color: #333;
            height: 56px;
            line-height: 56px;
            position: relative;
            .right{
                @extend .exportExcel
            }
        }
    }
    .searchOption {
        padding-left: 32px;
        .el-select, .pickDate, .el-input ,.el-cascader{
            width: 220px;
            margin-top: 20px;
            margin-right: 10px;
        }
    }
    .queryBtn{
        height: 30px;
        width:62px;
        display: inline-block;
        text-align: center;
        line-height: 30px;
        border-radius: 4px;
        color: #fff;
        background: $theme-color;
        cursor: pointer;
        font-size: 14px;
        margin-left: 40px;
        margin-right: 10px;
    }
    .queryReset{
        height: 30px;
        width:62px;
        font-size: 14px;
        display: inline-block;
        text-align: center;
        line-height: 30px;
        border-radius: 4px;
        border:solid 1px #d9d9d9;
        color: #999;
        cursor: pointer;
    }
    .ownerInfo{
        margin-top: 20px;
        background: #fff;
        border-radius: 4px;
        height: 214px;
        text-align: left;
        ul{
            height: 64px;
            line-height: 64px;
            border-bottom: solid 1px #e8e8e8;
            li{

                display: inline-block;
                margin-left: 50px;
                margin-right: 50px;
            }
        }
        div{
            background: #fff;
            height: 150px;
        }
    }
    .contractContent{
        span{
            color: red;
        }
        b{
            display: inline-block;
            min-width: 100px;
            text-align: right;
            margin-right: 5px;
        }
        p{
            margin-bottom: 24px;
            margin-left: 30px;
            &:last-of-type{
                margin-bottom: 0;
            }
        }
        .el-input{
            width: 372px;
        }
    }
    .upload-demo{
        display: inline-block;
        margin-left: 112px;
    }
    .uploadFileTip{
        display: inline-block;
        margin-left: 12px;
    }
</style>
<style lang="scss">
    .el-dialog__header{
        border-bottom: solid 1px #e8e8e8;
    }
    .el-dialog__footer{
        border-top: solid 1px #e8e8e8;
    }
</style>
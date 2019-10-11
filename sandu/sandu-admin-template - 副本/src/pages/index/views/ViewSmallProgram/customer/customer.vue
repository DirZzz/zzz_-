<template>
    <div class="customerFrame">
        <div class="customerTitle">
            <span>业主管理</span>
        </div>
        <div class="searchFrame">
            <el-row class="selectSearchCond">
                <el-input v-model="mobile" placeholder="手机号"> </el-input>
                <el-select v-model="customerStatus" @change="getCustomerListData" clearable placeholder="请选择状态">
                    <el-option v-for="(item,index) in customerStatusList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"
                    > </el-option>
                </el-select>
                <el-select v-model="customerActive" @change="getCustomerListData" clearable placeholder="请选择业主类型">
                    <el-option v-for="(item,index) in customerActiveList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"
                    > </el-option>
                </el-select>
                <el-select v-model="follow" @change="getCustomerListData" filterable clearable placeholder="请选择跟进人">
                    <el-option v-for="(item,index) in followIn"
                               :key="index"
                               :label="item.name"
                               :value="item.id"
                    >
                    </el-option>
                </el-select>
                <el-select v-model="selectDealer" filterable  @change="getCustomerListData" clearable placeholder="所属经销商企业">
                    <el-option v-for="(item,index) in dealerList"
                               :key="index"
                               :label="item.name"
                               :value="item.id"
                    > </el-option>
                </el-select>
                <el-select v-model="phoneFlag" filterable @clear="clearNull" @change="getCustomerListData" clearable placeholder="请选择是否有效">
                    <el-option v-for="(item,index) in phoneFlagList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"
                    > </el-option>
                </el-select>
            </el-row>
            <el-row class="areaStyle">
                <!---------------------------------------------------->

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

                <el-select clearable  v-if="streetShow" v-model="citySelect.origin"
                           @change="getOriginCode(citySelect.origin)"
                           placeholder="请选择街道">
                    <el-option
                            v-for="(item,index) in originSelect"
                            :key="item.areaCode"
                            :label="item.areaName"
                            :value="item.areaCode">
                    </el-option>
                </el-select>
                <el-col :span="3" v-else>
                    <span class="streetSel">不限</span>
                </el-col>
                <!---------------------------------------------------->
                

            </el-row>
        </div>
        <div class="btnFrame">
            <el-button  round  type="primary" class="UnifiedsearchBtn" @click="getCustomerListData">搜索</el-button>
            <el-button  round class="searchBtn UnifiedsearchBtn" @click="resetForm">重置</el-button>
        </div>
        <div class="tableList">
            <div style="padding: 10px 20px">
                  <el-button round
                             type="primary"
                             class="addProBtn1"
                             plain
                             v-if="isPer('customer:rule:view')"
                             @click="gotoRule">设置分配规则
                  </el-button>
                  <el-button round
                             type="primary"
                             class="addProBtn"
                             plain
                             @click.stop="exportCustomerData">导出
                  </el-button>
                </div>
<!--         
            <div class="setRule" v-if="isPer('customer:rule:view')"><span @click="gotoRule">设置分配规则 > </span></div>
            <div>
               <el-button  class="exportCount"  type="text" @click.stop="exportCustomerData" >导出</el-button>
            </div> -->
            <el-table
                    :data="tableData"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    style="width: 100%">
                <el-table-column
                        prop="nickName"
                        label="昵称"
                >
                </el-table-column>
                <el-table-column
                        prop="mobile"
                        label="手机号"
                >
                </el-table-column>

                <el-table-column
                        prop="address"
                        label="业主地址">
                </el-table-column>
                <el-table-column
                        prop="level"
                        label="业主等级">
                    <template slot-scope="scope">
                        <span v-if="scope.row.score >= 0 && scope.row.score <8" class="changeColor"><img src="../../../assets/images/icons/level.png" class="level">普通用户（{{scope.row.score}}分）</span>
                        <span v-if="scope.row.score >= 8 && scope.row.score <15" class="changeColor"><img src="../../../assets/images/icons/level.png" class="level">活跃业主（{{scope.row.score}}分）</span>
                        <span v-if="scope.row.score >= 15 && scope.row.score <25" class="changeColor"><img src="../../../assets/images/icons/level.png" class="level">潜在业主（{{scope.row.score}}分）</span>
                        <span v-if="scope.row.score >= 25" class="changeColor"><img src="../../../assets/images/icons/level.png" class="level">忠实业主（{{scope.row.score}}分）</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="alotStatus"
                        label="状态">
                    <template slot-scope="scope">
                        <span v-if="scope.row.alotStatus == 0">未分配</span>
                        <span v-if="scope.row.alotStatus == 1">已分配</span>
                        <span v-if="scope.row.alotStatus == 2">跟进中</span>
                        <span v-if="scope.row.alotStatus == 3">跟进完成</span>
                        <span v-if="scope.row.alotStatus == 4">无效</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="channelCompanyName"
                        label="所属经销商企业">
                </el-table-column>
                <el-table-column
                        prop="followUpUserName"
                        label="跟进人">
                </el-table-column>
                <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" @click="gotoDetail(scope.row)" v-if="isPer('customer:factory:detail')">详情</el-button>
                        <el-button type="text" @click="createAllot(scope.row)" v-if="isPer('customer:factory:mange')" v-show="scope.row.alotStatus == 1 || scope.row.alotStatus == 2" >重新分配</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <div class="pageFrame">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page.sync="query.start"
                        :page-sizes="[5,10, 50, 100, 200,500]"
                        :page-size="query.limit"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>

        <el-dialog class="dealerAllotDialog"
                   title="分配业主"
                   :visible.sync="dialogAllotVisible"
                   width="540px">
            <div>
                <span>所属经销商：</span>
                <el-select v-model="inputDealer" filterable placeholder="所属经销商企业">
                    <el-option v-for="(item,index) in dealerList"
                               :key="index"
                               :label="item.name"
                               :value="item.id"
                    > </el-option>
                </el-select>
            </div>

            <span slot="footer" class="dialog-footer">
                <el-button round size="mini" type="primary" @click="allotDealer">确 认</el-button>
                <el-button round size="mini" class="cancelBg" @click="dialogAllotVisible = false">取 消</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs'
    import axios from 'axios'

    export default {
        name: "coustomer",
        data(){
            return{
                loadingTwo:'',
                inputDealer: '',
                dialogAllotVisible:false,
                total: 0, // 总页数
                value:'',
                mobile:'',
                phoneFlag:'',
                phoneFlagList: [
                    {value:1, name:'有效业主'},
                    {value:2, name:'无效业主'},
                ],
                customerStatus:'', // 业主状态
                customerStatusList:[
                    {value:0, name:'未分配'},
                    {value:1, name:'已分配'},
                    {value:2, name:'跟进中'},
                    {value:3, name:'跟进完成'},
                    {value:4, name:'无效'}
                ],
                customerActive:'', // 活跃业主
                customerActiveList:[
                    {value:0, name:'普通业主'},
                    {value:1, name:'活跃业主'},
                    {value:2, name:'潜在业主'},
                    {value:3, name:'忠实业主'}
                ],
                follow:'', // 跟进人
                selectDealer:'',// 选择经销商
                loading:true,
                streetShow:true,
                rowId:'',
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

                cityCode: '',
                provide:[],
                provideSelect:[],
                citySelects:[],
                areaSelect: [],
                originSelect: [],

                //区域选择 结束------------
                customerList:[],// 业主列表
                tableData: [],
                exportData: [],
                query: {
                    start: 1,
                    limit: 10,
                },
                // 经销商下拉框
                dealerList:[],
                // 跟进人列表
                followIn:[],
            }
        },
        methods:{
            clearNull(){
                this.phoneFlag = ''
            },
            isPer(per) {
                if (per) {
                    let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                    // console.log(permiss.permissions, 'isper')
                    return this.isMainPer(per, permiss)
                }
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
                this.getCustomerListData()
            },
            getCityCode (code){
                this.citySelect.area =''
                this.citySelect.origin =''
                this.areaSelect = []
                this.originSelect = []
                this.getArea(code,'区')
                this.getCustomerListData()
            },
            getAreaCode (code){
                this.citySelect.origin =''
                this.originSelect = []
                this.getArea(code,'街道')
                this.getCustomerListData()
            },
            getOriginCode (code){
            },
            // 区域选择 结束---------- streetCodeName
            // 业主列表
            getCustomerListData() {
                this.loading = true;
                let queryCondition = Object.assign({}, this.query, {
                    mobile:this.mobile, //
                    level: this.customerActive,
                    alotStatus:this.customerStatus,
                    followUpUserId:this.follow,
                    channelCompanyId:this.selectDealer,
                    provinceCode:this.citySelect.provide,
                    cityCode:this.citySelect.city,
                    areaCode:this.citySelect.area,
                    streetCode:this.citySelect.origin,
                    phoneFlag:this.phoneFlag == 1 ? true : this.phoneFlag == 2 ? false: ''
                })
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                this.API.getCustomerList(queryCondition).then((res) => {
                    console.log(res)
                    if(res.data) {
                        this.tableData = res.data.list
                        this.total = res.data.total
                        this.loading = false
                    }else{
                        this.loading = false;
                    }
                })
            },
            exportCustomerData(){
                let queryCondition = Object.assign({}, {
                    mobile:this.mobile, //
                    level: this.customerActive,
                    alotStatus:this.customerStatus,
                    followUpUserId:this.follow,
                    channelCompanyId:this.selectDealer,
                    provinceCode:this.citySelect.provide,
                    cityCode:this.citySelect.city,
                    areaCode:this.citySelect.area,
                    streetCode:this.citySelect.origin,
                    phoneFlag:this.phoneFlag == 1 ? true : this.phoneFlag == 2 ? false: '',
                    exportType:0
                })
                for (let key in queryCondition) {
                    if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                        delete queryCondition[key]
                    }
                }
                console.log(queryCondition);
                this.loadingTwo = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                });
                this.API.exportCustomerCheckApi(queryCondition).then((res) => {
                    console.log(res)
                    if(res && res.code == 200) {
                        this.API.exportCustomerApi(queryCondition).then((result) => {
                            console.log(result)
                            if(result&& result.size > 0) {
                                this.loadingTwo.close()
                                let url = window.URL.createObjectURL(result)
                                let link = document.createElement('a')
                                link.style.display = 'none'
                                link.href = url
                                link.setAttribute('download', '业主信息导出.xls')
                                document.body.appendChild(link)
                                link.click()
                            }else{
                                this.loadingTwo.close()
                                this.$message.error("没有数据需要导出!")         
                            }
                        })
                    }else{
                        this.loadingTwo.close()
                        this.$message.error(res.message) 
                    }
                })                
            },
             // 导出excel 表
            handleDownload() {
                this.loading = true;
                this.exportData=[];
                require.ensure([], () => {
                    let pageSize = 1000;
                    let totalPage = 0
                    console.log("total:"+this.total)
                    if(this.total < pageSize){
                        totalPage = 1;
                    }else{
                        totalPage =  this.total/pageSize
                        if(this.total % pageSize!=0){
                            totalPage++
                        } 
                    }
                    console.log("totalPage:"+totalPage)
                    //循环调用接口
                    for(let i = 1; i<= totalPage;i++){
                        this.query.start = i;
                        this.query.limit = pageSize;
                        let queryCondition = Object.assign({}, this.query, {
                            mobile:this.mobile, //
                            level: this.customerActive,
                            alotStatus:this.customerStatus,
                            followUpUserId:this.follow,
                            channelCompanyId:this.selectDealer,
                            provinceCode:this.citySelect.provide,
                            cityCode:this.citySelect.city,
                            areaCode:this.citySelect.area,
                            streetCode:this.citySelect.origin,
                            phoneFlag:this.phoneFlag == 1 ? true : this.phoneFlag == 2 ? false: ''
                        })
                        for (let key in queryCondition) {
                            if (queryCondition[key] === '' || queryCondition[key] === undefined || queryCondition[key] === null) {
                                delete queryCondition[key]
                            }
                        }
                        this.API.getCustomerList(queryCondition).then((res) => {
                            console.log(res)
                            if(res.data) {
                                for(let index in res.data.list){
                                   console.log("333333"+res.data.list[index])
                                   this.exportData.push(res.data.list[index])
                                }
                            }
                        })
                    }
                    console.log("22222"+this.exportData)
                    const list = this.exportData
                    console.log("1111111"+list)
                    const { export_json_to_excel } = require('@/vendor/Export2Excel')
                    const tHeader = ['昵称', '手机号', '业主地址','业主等级','状态','所属经销商企业','跟进人']
                    const filterVal = ['nickName', 'mobile', 'address','level','alotStatusName','channelCompanyName', 'followUpUserName']
                    const data = this.formatJson(filterVal, list)
                    export_json_to_excel(tHeader, data, '业主管理列表excel')
                    this.loading = false
                })
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            },

            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size
                this.getCustomerListData()
            },
            handleCurrentChange: function (currentPage) {
                this.query.start = currentPage
                this.loading = true
                this.getCustomerListData()
            },
            // 获取经销商列表，
            getDealerList(){
                this.API.getComapnyIn({}).then((res) => {
                    console.log(res, 'dealer')
                    if(res) {
                        this.dealerList = res.data
                    }
                })
            },
            // 跟进人列表
            getFollow(){
                this.API.getFollowIn({}).then((res) => {
                    if(res) {
                        this.followIn = res.data
                    }
                })
            },
            resetForm(){
                this.citySelect ={};
                this.mobile = '';
                this.customerActive = '';
                this.customerStatus = '';
                this.follow = '';
                this.selectDealer = '';
                this.phoneFlag = ''
                this.getCustomerListData()
            },
           // 分配
            // 重新分配
            createAllot(row){
                this.inputDealer='';
                this.dialogAllotVisible = true
                this.rowId = row.id
            },
            allotDealer(){
                let queryCondition = Object.assign({},{
                    id:this.rowId,
                    companyId: this.inputDealer,
                })
                for (let key in queryCondition) {
                    if (queryCondition[key] === 'userId') {
                        delete queryCondition[key]
                    }
                }
                this.API.reAllot(queryCondition).then((res) => {
                    console.log(res, 'res')
                    if(res.success) {
                        this.getCustomerListData()
                        this.$message.success('分配成功');
                        this.dialogAllotVisible=false;
                    } else {
                        this.$message.error('分配失败')
                    }
                })
            },

            //  详情
            gotoDetail(row) {
                console.log(row.id, 'id detail')
                this.$router.push(`/customerDetail/${row.id}`)
            },
            gotoRule(){
                this.$router.push('/rules')
            },
        },
        activated(){
            this.getCustomerListData()
            this.getDealerList()
            this.getFollow()
            this.getArea(0,'省')
        },
    }
</script>

<style scoped lang="scss">
    @import "../../../assets/css/mixin.scss";
    .customerTitle{
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        border-bottom: solid 1px #ddd;
        span{
            display: inline-block;
            padding-left: 20px;
        }
    }
    .changeColor{
        color: #ff2323;
    }
    .searchFrame{
        background: #fff;
        padding: 20px;
    }
    .areaStyle{
        margin-top: 20px;
        .el-select{
            width: 216px;
            margin-right: 10px;
        }
    }
    .selectSearchCond{
        .el-select,.el-input{
            width: 216px;
            margin-right: 10px;
        }

    }
    .btn-right{
        position: absolute;
        left: 20px;
        top: 20px;
        .addProBtn{
           width:120px;
           height:32px;
           font-size:12px;
           text-align:center;
           line-height: 8px;
        }
        .addProBtn1{
          height:32px;
          font-size:12px;
          text-align:center;
          line-height: 8px;
        }
      }
    .btnFrame{
        padding:10px 20px;
        text-align: center;
        border-top: solid 1px #ddd;
        background: #fff;
        height: 58px;
        line-height: 58px;
        font-size: 18px;
        color: #333;
        .el-button{
            width: 130px;
        }
    }

    .setRule{
        margin-top: 20px;
        height: 76px;
        line-height: 76px;
        background: #fff;
        color: #FF6419;
        padding-left: 20px;
        span{
            display: inline-block;
            cursor: pointer;
        }
    }
    .tableList{
        margin-top: 20px;
        background: #fff;
    }
    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }
    .level{
        vertical-align: -4px;
        margin-right: 4px;
    }
    .dealerAllotDialog{
        div{
            text-align: center;
            margin-bottom: 15px;
        }
        i{
            font-style: normal;
            display: inline-block;
            width: 360px;
            text-align: left;
        }
        .mobi{
            margin-left: -18px;
        }
        .el-select{
            background:#fafafa ;
            width: 360px;
            height: 36px;
        }
        .followResut{
            span{
                display: inline-block;
                vertical-align: top;
            }
        }
        .el-input{
            width: 360px;
        }
    }

</style>
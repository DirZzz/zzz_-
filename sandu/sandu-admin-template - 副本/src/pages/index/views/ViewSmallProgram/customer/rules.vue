<template>
    <div class="rules">
        <v-nav :nav="nav"></v-nav>
        <div class="rules-content">
            <div class="header"><span>设置分配规则</span><el-button type="primary" round plain    style="float:right;" slot="label" @click="$router.push('/addRule')" v-if="isPer('customer:rule:add')">新增规则</el-button></div>
            <div class="rule-search">
                <el-row class="selectSearchCond">
                    <el-col :span="4">
                        <el-select v-model="belongValue" filterable clearable placeholder="所属经销商企业" @change="chooseConpany">
                            <el-option v-for="item in conpanyList"
                                       :key="item.id"
                                       :label="item.companyName"
                                       :value="item.id"
                            ></el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="4">
                        <el-select v-model="citySelect.provide"
                                   @change="getProvideCode"
                                   clearable placeholder="请选择省">
                            <el-option  v-for="(item, index) in provide"
                                        :key="item.areaCode"
                                        :label="item.areaName"
                                        :value="item.areaCode">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="4">
                        <el-select v-model="citySelect.city"
                                   @change="getCityCode"
                                   clearable placeholder="请选择市">
                            <el-option   v-for="(item,index) in citySelects"
                                         :key="item.areaCode"
                                         :label="item.areaName"
                                         :value="item.areaCode">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="4">
                        <el-select v-model="citySelect.area"
                                   @change="getAreaCode"
                                   clearable placeholder="请选择区">
                            <el-option  v-for="(item, index) in areaSelect"
                                        :key="item.areaCode"
                                        :label="item.areaName"
                                        :value="item.areaCode">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="2">
                        <el-button class="searchBtn" round @click="getChannelProductList">搜索</el-button>
                    </el-col>
                    <el-col :span="1">
                        <el-button class="searchBtn" @click="resetInput" round>重置</el-button>
                    </el-col>
                </el-row>
            </div>
            <div class="table-list">
                <el-table :data="tableData"
                          style="width: 100%">
                    <el-table-column label="所属经销商企业" width="360">
                        <template slot-scope="scope">
                            <div class="channelCompanyName" @click="$router.push({path:`/rulesDetail/${scope.row.channelCompanyId}`,query:{channelCompanyName:scope.row.channelCompanyName}})">{{scope.row.channelCompanyName}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column label="跟进区域">
                        <template slot-scope="scope">
                                <div class="addressBox">
                                    <div class="addressList" v-for="(item,index) in scope.row.allCode" v-if="index<5">{{item.provinceName|| '无'}}{{item.cityName || ''}}{{item.areaCodeName || ''}}<em v-if="scope.row.allCode.length!=index+1">|</em></div>
                                    <div class="ellipsis" v-if="scope.row.allCode.length>5">...</div>
                                </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="360">
                        <template slot-scope="scope">
                            <div class="table-td-right">
                                <span class="operatonBtn" v-if="isPer('customer:rule:manage')" @click="goToEdit(scope.row)">分配区域</span>
                                <span class="operatonBtn" @click="SinglePush(scope.row)">取消分配</span>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <!--分页组件-->
        <div class="pageFrame">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="query.page"
                    :page-sizes="[10, 50, 100, 200,500]"
                    :page-size="query.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
        <cancelDistribution ref="cancelDistribution" @refresh="companyList"></cancelDistribution>
        <distributionArea ref="distributionArea" :conpanyList="conpanyList" @refresh="companyList"></distributionArea>
    </div>
</template>

<script>
    import qs from 'qs';
    import cancelDistribution from './components/cancelDistribution'
    import distributionArea from './components/distributionArea'
    import city from '@/filters/city'
    export default {
        name: "rules",
        mixins:[city],
        components: {
            cancelDistribution,
            distributionArea
        },
        data() {
            return {
                loading:false,
                tableData: [],
                conpanyList: [],
                channelCompanyId:'',
                query:{
                    page:1,
                    limit:10
                },
                total:0,
                belongValue:'',
                nav:[
                    {nav: '业主管理列表', url: '/customer'},
                    {nav: '设置分配规则', url: '/rules'}
                ],
                citySelect:{
                    provide: '',
                    city: '',
                    area: '',
                    origin: ''
                },
            }
        },
        created() {
           this.companyList();
           this.API.companyList({companyId:sessionStorage.getItem('companyID')}).then(res=>{
                 res.code=='200' ? this.conpanyList=res.data : this.$message(res.message);
           })
        },
        methods: {
            companyList(){
                this.API.allotRule({
                    companyId:sessionStorage.getItem('companyID'),
                    channelCompanyId:this.channelCompanyId || undefined,
                    provinceCode:this.citySelect.provide || undefined,
                    cityCode:this.citySelect.city || undefined,
                    areaCode:this.citySelect.area || undefined,
                    start:this.query.page,
                    limit:this.query.limit,
                }).then(res=>{
                    if(res.code==200){
                        this.tableData=res.data.list;
                        this.total=res.data.total;
                    }else if(res.code==204){
                        this.tableData=[];
                        this.total=0;
                    }else {
                        this.$message(res.message)
                    }
                });
            },
            isPer (per) {
                let permiss = qs.parse(sessionStorage.getItem('loginUser'))
                return this.isMainPer(per, permiss)
            },
            resetInput(){ //重置
               this.belongValue='';
               this.citySelect.provide='';
               this.citySelect.city='';
               this.citySelect.area='';
                this.channelCompanyId=undefined;
                this.companyList();
            },
            getChannelProductList(){ //搜索
               this.companyList();
            },
            chooseConpany(val){
                this.channelCompanyId=val;
                this.companyList();
            },
            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size;
                this.companyList();
            },
            handleCurrentChange: function (currentPage) {
                this.query.page = currentPage;
                this.companyList();
            },
            SinglePush(item){
                this.$nextTick(()=>{
                    this.$refs.cancelDistribution.isShow('show',item.channelCompanyId,item.channelCompanyName);
                })
            },
            goToEdit(item){
                this.$nextTick(()=>{
                    this.$refs.distributionArea.isShow('show',item.channelCompanyId,item.channelCompanyName);
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    .rules {
        .nav{
            padding-top: 10px;
        }
        .channelCompanyName{
            color: #ff6419;
            cursor: pointer;
        }
       .rules-content{
           background-color: #fff;
           padding: 0 20px;
           .header{
               padding: 20px 30px;
               color: #666666;
               font-size: 20px;
               line-height: 36px;
               position: relative;
               border-bottom: 1px solid #ddd;
               .addRule{
                   height: 36px;
                   position: absolute;
                   right: 0;
               }
           }
           .rule-search{
               padding: 21px 0;
           }
           .table-list{
               .table-td-right{
                   span:nth-child(1){
                       margin-right: 20px;
                   }
               }
                .operatonBtn{
                    color: #ff6419;
                    font-size: 14px;
                    cursor: pointer;
                }
           }
           .addressBox{
               display: flex;
               white-space: nowrap;
               position: relative;
               .addressList{
                   em{
                       padding: 0 20px;
                   }
               }
               .ellipsis{
                   display: flex;
                   flex: 1;
                   width: 100px;
               }
           }

       }
        .pageFrame{
            background: #fff;
            height: 110px;
            padding-top: 40px;
            box-sizing: border-box;
            text-align: center;
        }
    }

</style>
<template xmlns:v-clipboard="http://www.w3.org/1999/xhtml">
    <div class="activeListFrame">
        <div class="headerT">
            <h1>砍价活动</h1>
            <div class="header-screen">

                <el-select size="medium" v-model="actStatus" class="screen-city-item" placeholder="活动状态"
                           :clearable=true
                           @change="conditionalSearchList('活动状态')">
                    <el-option label="未开始" :value="0">未开始</el-option>
                    <el-option label="进行中" :value="10">进行中</el-option>
                    <el-option label="已结束" :value="20">已结束</el-option>
                </el-select>

                <el-input class="header-screen-input" size="medium"
                          placeholder="请输入活动名称"
                          v-model="actName"
                          @change="conditionalSearchList('活动名称')">
                </el-input>

                <el-select v-model="putawayMiniPro"
                           multiple placeholder="上架小程序"
                           @change="conditionalSearchList('小程序')">
                    <el-option
                            v-for="item in putawayMiniProInfo"
                            :key="item.appId"
                            :label="item.minProName"
                            :value="item.appId">
                    </el-option>
                </el-select>

            </div>

            <div class="header-seek">
                <el-button type="primary" round class="pri UnifiedsearchBtn" size="medium" @click="getActiveList">搜索</el-button>
                <el-button  round class="seek UnifiedsearchBtn" size="medium" @click="reset">重置</el-button>
                <el-button class="addActive" @click="activeAdd" type="primary" size="small" round plain>新增活动</el-button>
            </div>
        </div>


        <div class="listData">
            <el-table
                    :data="listdata"
                    style="width: 100%"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(255, 255, 255, 0.8)"
                    tooltip-effect="dark"
            >
                <!--<el-table-column
                        label="编号"
                        width="55">
                </el-table-column>-->
                <el-table-column
                        prop="actName"
                        label="活动名称">
                </el-table-column>
                <el-table-column
                        prop="productName"
                        label="产品名称">
                </el-table-column>
                <el-table-column
                        prop="begainTime"
                        label="启动时间">
                </el-table-column>
                <el-table-column
                        prop="endTime"
                        label="结束时间">
                </el-table-column>
                <el-table-column
                        prop="actJoinPeoples"
                        label="参与人数">
                </el-table-column>
                <el-table-column
                        prop="bargainSuccessfulPeoples"
                        label="成功人数">
                </el-table-column>
                <el-table-column
                        prop="actStatus"
                        :formatter="transformStatus"
                        label="状态">
                </el-table-column>
<!--                <el-table-column-->
<!--                        prop="appName"-->
<!--                        label="所属小程序">-->
<!--                </el-table-column>-->

                <el-table-column prop="putawayMinProName" label="上架小程序" >
                    <template slot-scope="scope">
                        <div v-if="scope.row.putawayMinProName">
                            <p
                                    v-for="(item,index) in scope.row.putawayMinProName.split(',')"
                                    :key="index"
                            >{{item}}</p>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="320px">
                    <template slot-scope="scope">
                        <span class="rowOper" v-if="scope.row.actStatus != 20" @click="editActive(scope.row.id)">编辑</span>
                        <span class="rowOper" v-if="scope.row.actStatus != 20" @click="setData(scope.row)">设置数据</span>
                        <span class="rowOper" @click="userData(scope.row)">用户数据</span>
                        <span class="rowOper" @click="statisticsData(scope.row.id)">统计数据</span><br/>
                        <span class="rowOper" v-if="scope.row.actStatus == 20" @click="recreate(scope.row.id)">重新创建</span>
                        <span class="rowOper" v-if="scope.row.actStatus != 20" v-clipboard:copy="scope.row.copyUrl" v-clipboard:success="onCopy"
                              v-clipboard:error="onError">复制内部链接</span>
                        <span class="rowOper" v-if="scope.row.actStatus != 20" @click="activeEnd(scope.row.id)">结束</span>
                        <span class="rowOper" v-if="scope.row.actStatus == 20" @click="deleteActive(scope.row.id)">删除</span>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pageFrame">
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
        <el-dialog title="结束" width="540px"
                   :close-on-click-modal="false"
                   :visible.sync="activeEndVisible">
            <p style="padding-left: 20px;">确定要结束此活动吗？</p>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" @click="activeEndVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="confirmActiveEnd">确 定</el-button>
              </span>
        </el-dialog>

        <el-dialog title="删除活动" width="540px"
                   :close-on-click-modal="false"
                   :visible.sync="activeDeleteVisible">
            <p style="padding-left: 20px;">确定要删除此活动吗？</p>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" @click="activeDeleteVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="confirmDeleteActive">确 定</el-button>
              </span>
        </el-dialog>

        <el-dialog title="设置数据" width="540px"
                   :close-on-click-modal="false"
                   :visible.sync="setDataVisible">
            <div class="setData">
                <header>
                    <el-checkbox v-model="checkBoxStatus" @change="checkChage"></el-checkbox>
                    领取人数设置：递增规则 - 每小时增加
                    <el-input  v-model="personal" maxlength="8" @change="checkPersonalNumber" :disabled="personalAbled" class="personal" size="mini"></el-input>
                    人<!--小程序公告文字设置-->
                    <p class="reportTxt">公告文字设置：
                        <el-button class="addText hander" type="text" :disabled="addAdText.length > 19"
                                   @click="addTextObj">添加
                        </el-button>
                        已添加{{addAdText.length}}条
                    </p>

                </header>
                <div class="adText">
                    <div v-for="(item,index) in addAdText" class="rowData">
                        <p>{{index+1}}</p>
                        <p>
                            <!--v-if="isEdit(item)"-->
                            <el-input :disabled="item._isAbled"  v-model="item.message" size="mini"></el-input>
                        </p>
                        <p>
                            <!--v-if="isSpanText(item)"-->
                            <span class="addText hander" v-model="item.currentOperation" @click="handlerMsg(item)">{{item.currentOperation}}</span>
                            <!-- <span v-if="!isEdit(item)" class="rowOper hander" @click="saveMsg(item)">保存</span>-->
                            <span class="addText hander" @click="removeMsg(item)">删除</span>
                        </p>
                    </div>
                    <!--<div v-if="messageQuery.total > 0">
                        <el-pagination
                                @current-change="handleMessageCurrentChange"
                                :current-page.sync="messageQuery.page"
                                :page-sizes="[5]"
                                :page-size="messageQuery.limit"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="messageQuery.total">
                        </el-pagination>
                    </div>-->
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" @click="setDataVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="confirmAddPersonNumbers">确 定</el-button>
              </span>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs'

    export default {
        name: "activelist",
        data() {
            return {
                putawayMiniProInfo: [],
                putawayMiniPro: [],
                putawayInfo: '',
                actName: '',
                actStatus: '',
                checkBoxStatus:false,
                appId: '',
                actId: '',
                addClick: false,
                personal: '',
                _checked: 0,
                personalAbled: true,
                addAdText: [],
                activeEndVisible: false,
                activeDeleteVisible: false,
                setDataVisible: false,
                listdata: [],
                total: 0,
                loading: false,
                activeId: '',
                query: {
                    actName: '',
                    actStatus: '',
                    putawayInfo: '',
                    limit: 10,
                    page: 1
                },
                messageQuery: {
                    limit: 5,
                    page: 1,
                    total: 0
                }
            }
        },
        methods: {
            getPutawayMiniProgram() {
                let companyId = sessionStorage.getItem('companyID');
                this.API.selectPutawayMiniProgram({
                    companyId: companyId
                }).then(res => {
                    this.putawayMiniProInfo = res.datalist;
                })
            },
            recreate(id){
                this.$router.push(`/activeAdd/${id}`)
            },
            checkPersonalNumber(){
                let re = /^[0-9]+.?[0-9]*/;
                if (re.test(this.personal)){
                    //校验是否含有小数点
                   if (this.personal.indexOf(".") > 0){
                       this.personal = '';
                       this.$message.warning("不能输入小数");
                   }
                }else{
                    this.personal = '';
                    this.$message.warning("请输入1-9的数字");
                }
            },
            onCopy() {
                this.$message.success("拷贝活动链接成功!");
            },
            onError() {
                this.$message.warning("拷贝活动链接失败!");
            },
            confirmAddPersonNumbers() {
                if (!this.personalAbled) {
                    //已经勾选,可以保存
                    this.$confirm(`确定设置领取人数吗?`, '提示', {
                        cancelButtonClass: 'cancelButtonClass',
                        confirmButtonClass: 'confirmButtonClass',
                        type: 'info',
                        center: true
                    }).then(() => {
                        let form = new FormData();
                        form.append('actId', this.actId);
                        form.append('sysDecreaseNum', this.personal);
                        this.API.AddPersonals(form).then(res => {
                            if (res.success) {
                                this.$message.success(res.message);
                                this.setDataVisible = false;
                                this.checkBoxStatus = false;
                                this.personalAbled = true;
                                this.getActiveList();
                            } else {
                                this.$message.warning(res.message)
                            }
                        })
                    })
                }else{
                    this.setDataVisible = false;
                }
            },
            checkChage(value) {
                if (value) {
                    this.personalAbled = false;
                } else {
                    this.personalAbled = true;
                }
            },
            removeMsg(item) {
                if (item.id === undefined){
                    this.getActBargainAwardMsgs(this.actId);
                    return;
                }
                let form = new FormData();
                form.append('awardMsgId', item.id); // 手机号
                this.API.removeActMsg(form).then(res => {
                    if (res.success) {
                        this.$message.success("删除成功");
                        this.getActBargainAwardMsgs(this.actId);
                    } else {
                        this.$message.warning("删除失败");
                    }
                })
            },
            handlerMsg(item) {
                if (item.currentOperation === '编辑') {
                    item._isAbled = false;
                    item.currentOperation = '保存';
                    item._isupdate = 1;
                } else {
                    if (item._isupdate == 1) {
                        //修改操作
                        this.editMsg(item);
                    } else {
                        //保存操作
                        this.saveMsg(item);
                    }
                }
            },
            isSpanText(item) {
                if (item.id === undefined) {
                    this.$set(item, 'currentOperation', '保存');
                } else {
                    this.$set(item, 'currentOperation', '编辑');
                }
                return true;
            },
            isEdit(item) {
                if (item.id === undefined) {
                    item._isAbled = false;
                } else {
                    item._isAbled = true;
                }
                return true;
            },
            editMsg(item) {
                let params = {
                    "id": item.id,
                    "message": item.message
                }
                this.API.editActMsg(params).then(res => {
                    if (res.success){
                        //刷新页面
                        this.getActBargainAwardMsgs(this.actId);
                        this.$message.success(res.message);
                    } else{
                        this.$message.warning(res.message);
                    }
                })
            },
            saveMsg(item) {
                console.log(item);
                let params = {
                    "actId": this.actId,
                    "message": item.message,
                    "appId": this.appId
                }
                this.createActmsg(params);
            },
            createActmsg(params) {
                this.API.saveActMSg(params).then(res => {
                    if (res.success) {
                        //刷新页面
                        this.getActBargainAwardMsgs(this.actId);
                        this.$message.success(res.message);
                    } else {
                        this.$message.warning(res.message);
                    }
                })
            },
            transformStatus(row, column, cellValue, index) {
                console.log(row)
                switch (row.actStatus) {
                    case 0 :
                        return "未开始";
                    case 10:
                        return "进行中";
                    case 20:
                        return "已结束";
                    default:
                        return "活动未开始";
                }
            },
            editActive(id) {
                this.$router.push(`/activeEdit/${id}`)
            },
            activeAdd() {
                this.$router.push(`/activeAdd/${1}`)
            },
            userData(row) {
                this.$router.push(`/activeUserData/${row.id}/${row.appId}`)
            },
            setData(row) {
                this.setDataVisible = true
                this.actId = row.id;
                this.appId = row.appId;
                this.personal = row.sysReduceNum;
                this.getActBargainAwardMsgs(this.actId);
            },
            statisticsData(id) {
                this.$router.push(`/statisticalData/${id}`)
            },
            addTextObj() {
                //判断当前是否有灭有保存的消息
                let exitsTxtNull = this.addAdText.some((item, index) =>{
                    if (item.txtContent === '' && item.currentOperation === '保存'){
                        return true;
                    }
                    return false;
                });

                if (exitsTxtNull){
                    this.$message.warning("您当前还有消息没有保存");
                    return;
                }

                this.addAdText.push({
                    txtContent: '',
                    currentOperation: '保存',
                    _isAbled: false,
                    _isupdate: 0,
                    message:'例如:**刚刚0元领取了诺克的台灯'
                })
            },
            // 分页模块
            handleSizeChange: function (size) {
                this.query.limit = size
                this.getActiveList()
            },
            handleCurrentChange(currentPage) {
                this.query.page = currentPage
                this.loading = true;
                this.getActiveList()
                this.loading = false;
            },
            handleMessageCurrentChange(currentPage) {
                //this.query.page = currentPage
                this.getActBargainAwardMsgs(this.actId);
            },
            /*条件搜索数据*/
            conditionalSearchList(type) {
                if (type=='小程序') {
                    this.query.putawayInfo = this.putawayMiniPro.join(",");
                    this.query.page = 1;
                    this.query.limit = 10;
                }
                if (type == '活动名称') {
                    this.query.actName = this.actName;
                    this.query.page = 1;
                    this.query.limit = 10;
                }
                if(type == '活动状态') {
                    this.query.actStatus = this.actStatus;
                    this.query.page = 1;
                    this.query.limit = 10;
                }

                this.getActiveList();
            },
            // 获取活动列表
            getActiveList() {
                let form = new FormData();
                form.append("actName",this.actName);
                form.append("actStatus",this.actStatus);
                form.append("putawayInfo",this.putawayMiniPro.join(","));
                form.append("page",this.query.page);
                form.append("limit",this.query.limit);
                this.API.activeList(form).then(res => {
                    this.listdata = res.datalist
                    this.total = res.totalCount
                })
            },
            activeEnd(id) {
                this.activeId = id
                this.activeEndVisible = true
            },
            confirmActiveEnd() {
                let fromData = new FormData();
                fromData.append('actId', this.activeId)
                this.API.activeTimeOut(fromData).then(res => {
                    this.getActiveList();
                    this.$message.success('活动已结束');
                    this.activeEndVisible = false;
                })
            },
            deleteActive(id) {
                this.activeId = id
                this.activeDeleteVisible = true
            },
            confirmDeleteActive() {
                let fromData = new FormData();
                fromData.append('actId', this.activeId)
                this.API.activeDelete(fromData).then((res) => {
                    this.activeDeleteVisible = false;
                    if (res.success){
                        this.$message.success(res.message);
                        this.getActiveList();
                    } else{
                        this.$message.success(res.message);
                    }
                });
            },
            getActBargainAwardMsgs(actId) {
                this.API.getActMsgList({"actId":actId}).then(res => {
                    if (res.datalist === undefined) {
                        this.addAdText = [];
                    } else {
                        this.addAdText = res.datalist;
                        for (var item of this.addAdText) {
                            this.$set(item, '_isAbled', true);
                            this.$set(item, '_isupdate', 1);
                            this.$set(item, 'currentOperation', '编辑');
                        }
                    }
                    this.messageQuery.total = res.totalCount;
                })
            },
            reset() {
                this.query = {
                    actName: undefined,
                    actStatus: undefined,
                    putawayInfo: undefined,
                    limit: this.query.limit,
                    page: this.query.page
                };
                this.actStatus = '';
                this.actName = '';
                this.putawayMiniPro = [];
                this.getActiveList();
            }
        },
        activated() {
            this.getActiveList();
            this.getPutawayMiniProgram();
        }
    }
</script>

<style lang="scss" scoped>

    .button-mapping{
        margin-right: 20px;
    }

    .addText{
        color: #FF6419;
        cursor: pointer;
    }
    .rowOper {
        color: #FF6419;
        cursor: pointer;
        margin-right: 20px;
    }

    .hander {
        cursor: pointer;
    }

    .personal {
        width: 120px;
    }

    .setData {
        margin: 0 auto;
        header {
            padding-left: 20px;
        }
        .reportTxt {
            margin-top: 15px;
            margin-bottom: 10px;
        }
        .rowData {
            background: #fff;
            margin: 0 20px;
            border-left: solid 1px #e8e8e8;
            border-right: solid 1px #e8e8e8;
            border-bottom: solid 1px #e8e8e8;
            height: 50px;
            line-height: 40px;
            &:first-of-type {
                border-top: solid 1px #e8e8e8;
            }
        }
        .adText {
            padding: 20px 0;
            background: #fafafa;
            max-height: 360px;
            overflow-y: scroll;
            white-space: nowrap;
            p {
                display: inline-block;
                padding: 6px 3%;
                height: 40px;
                border-right: solid 1px #e8e8e8;
                &:first-of-type {
                    width: 60px;
                    text-align: center;
                }
                &:last-of-type {
                    border-right: none;
                }
            }
            .el-input {
                width: 263px;
            }
        }
    }

    .pageFrame {
        background: #fff;
        height: 110px;
        padding-top: 40px;
        box-sizing: border-box;
        text-align: center;
    }

    .headerT {
        width: 100%;
        background-color: #ffffff;

        h1 {
            width: 100%;
            height: 58px;
            font-size: 18px;
            line-height: 58px;
            padding-left: 20px;
            box-sizing: border-box;
            border-bottom: 1px solid #dddddd;
        }

        .header-screen {
            padding: 20px 10px 0 10px;
            border-bottom: 1px solid #dddddd;

            .header-screen-input {
                width: 180px;
                padding: 0 10px 20px 10px;
            }

            .screen-city-item {
                width: 130px;
                padding: 0 10px 20px 10px;
            }
        }

        .header-seek {
            padding: 20px 30px;
            text-align: center;

            .seek {
                width: 100px;
                background: #f5f5f5;
            }

            .pri {
                width: 100px;
                border: solid 1px #dddddd;
            }

            .addActive {
                float: right;
            }

        }
    }

    .activeListFrame {
        background: #fff;
    }

    .listData {
        margin: 20px;
    }

</style>

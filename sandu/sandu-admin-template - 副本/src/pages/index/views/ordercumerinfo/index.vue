<template>
    <div class="ordercumerinfo">
        <div v-if="!editBoxShow" class="title">预约客户信息</div>
        <div style="padding: 10px">
            <div v-if="!editBoxShow" class="nav">
                <div class="nav_inpu_item">
                    <span>客户姓名：</span>
                    <el-input placeholder="请输入姓名" v-model="mark.userName">
                    </el-input>
                </div>
                <div class="nav_inpu_item">
                    <span>客户手机号：</span>
                    <el-input placeholder="请输入手机号" v-model="mark.mobile">
                    </el-input>
                </div>
                <div class="nav_inpu_item">
                    <span>预约时间：</span>
                    <el-date-picker v-model="value1" type="daterange" range-separator="至" start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </div>
                <div class="nav_inpu_item">
                    <span>标题：</span>
                    <el-input placeholder="请输入标题" v-model="mark.title">
                    </el-input>
                </div>
                <div class="nav_inpu_item">
                    <span>状态：</span>
                    <el-select v-model="mark.status" clearable placeholder="全部">
                        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </div>
            </div>

            <div style="margin-top:-10px">
                <span>客户来源：</span>
                <el-select v-model="putawayMiniPro" clearable placeholder="请选择客户来源">
                    <el-option v-for="item in putawayMiniProInfo"
                               :key="item.appId"
                               :label="item.minProName"
                               :value="item.appId">
                    </el-option>
                </el-select>
            </div>

            <div v-if="!editBoxShow" class="nav_but_box">
                <div class="nav_but search" @click="search">搜索</div>
                <div class="nav_but reset" @click="reSet">重置</div>
            </div>
            <div v-if="!editBoxShow" class="output">
                <span @click="exportExl">导出</span>
            </div>
            <div v-if="!editBoxShow" class="companyBaseMsg-content">
                <el-table element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
                          element-loading-background="rgba(255, 255, 255, 0.8)" :data="shopData" style="width: 100%">
                    <el-table-column type="index" label="序号" width="100">
                    </el-table-column>
                    <el-table-column prop="userName" label="客户姓名">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark" :content="scope.row.userName" placement="top-page" :enterable="false"
                                        :visible-arrow="false">
                                <span class="operation-name">{{scope.row.userName}}</span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column prop="mobile" label="手机号">
                        <template slot-scope="scope">
                            <span class="operation-name">{{scope.row.mobile}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="appointmentTime" label="预约时间">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark" :content="scope.row.appointmentTime" placement="top-start"
                                        :enterable="false" :visible-arrow="false">
                            <span class="operation-name">
                                {{scope.row.appointmentTime}}
                            </span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column prop="appointmentTitle" label="标题">
                        <template slot-scope="scope">
                            <span class="operation-name">{{scope.row.appointmentTitle}}</span>
                        </template>
                    </el-table-column>

                    <el-table-column prop="minProName" label="客户来源">
                        <template slot-scope="scope">
                            <span class="product-name">{{scope.row.minProName}}</span>
                            </el-tooltip>
                        </template>
                    </el-table-column>

                    <el-table-column prop="release" label="状态">
                        <template slot-scope="scope">
                        <span
                                class="operation-name">{{ scope.row.status == 0 ? '有效':scope.row.status == 1?'无效':'无' }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="remake" label="备注">
                        <template slot-scope="scope">
                            <span class="operation-name">{{ scope.row.remake}}</span>
                        </template>
                    </el-table-column>

                    <el-table-column class="bowen" width="120" prop="operation" label="操作 ">
                        <template slot-scope="scope">
                            <span class="operation" @click="showEdit(scope.row)">编辑</span>
                            <span class="operation" v-if="scope.row.isDeleted==0"
                                  @click="deleteItem(scope.row.id)">删除</span>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div v-if="!editBoxShow" class="footer-paging">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                               :current-page.sync="page" :page-sizes="[5, 10, 20, 30, 40]" :page-size="10"
                               layout="total, sizes, prev, pager, next, jumper" :total="totalCount">
                </el-pagination>
            </div>
            <div class="editBox" v-if="editBoxShow">
                <div class="navback">
                    <span @click="closeEdit" style="color:#ff6419; cursor: pointer;">返回上一级|预约客户信息></span>
                    <span>编辑</span>
                </div>
                <div class="title">客户信息编辑</div>
                <div class="eidt_main">
                    <div class="edit_item">
                        <div class="edit_item_tit">
                            <span style="color:red">*</span>
                            <span>客户名称：</span>
                        </div>
                        <div class="edit_item_value">{{editItem.userName}}</div>
                    </div>
                    <div class="edit_item">
                        <div class="edit_item_tit">
                            <span style="color:red">*</span>
                            <span>客户手机号：</span>
                        </div>
                        <div class="edit_item_value">{{editItem.mobile}}</div>
                    </div>
                    <div class="edit_item">
                        <div class="edit_item_tit">
                            <span style="color:red">*</span>
                            <span>预约时间：</span>
                        </div>
                        <div class="edit_item_value">{{editItem.appointmentTime}}</div>
                    </div>
                    <div class="edit_item">
                        <div class="edit_item_tit">
                            <span style="color:red">*</span>
                            <span>标题：</span>
                        </div>
                        <div class="edit_item_value">{{editItem.appointmentTitle}}</div>
                    </div>
                    <div class="edit_item">
                        <div class="edit_item_tit">
                            <span>状态：</span>
                        </div>
                        <el-select v-model="editMark.status" placeholder="请选择">
                            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                    <div class="edit_item">
                        <div class="edit_item_tit">
                            <span>备注：</span>
                        </div>
                        <span class="textarea_tit">{{editMark.remake.length}}/200</span>
                        <el-input type="textarea" maxlength="200" :rows="9" placeholder="请输入内容" v-model="editMark.remake">
                        </el-input>
                    </div>
                </div>
                <div class="edit_butBox">
                    <div class="edit_butBox_cao">
                        <div class="edit_but" @click="closeEdit">取消</div>
                        <div class="edit_but" @click="updateEdit">确定</div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                putawayMiniPro: '',
                putawayMiniProInfo: [],
                options: [{
                        value: 0,
                        label: '有效'
                    },
                    {
                        value: 1,
                        label: '无效'
                    }
                ],
                page: 0, // 页码
                limit: 10, // 每页条数
                totalCount: 0, // 店铺数据总条数
                value1: [],
                mark: {
                    page: 0, //当前页
                    limit: 10, //每页显示条数
                    userName: null, //用户名
                    mobile: null, //手机号
                    title: null, //标题
                    status: null, //状态 0,有效,1无效
                    appointmentTimeStart: null, //预约时间开始
                    appointmentTimeEnd: null, //预约时间结束
                    companyId: sessionStorage.getItem('companyID') / 1,
                    originAppId: null
                },
                shopData: [],

                editMark: {
                    status: null,
                    remake: ''
                },
                editBoxShow: false,
                editItem: {}
            }
        },
        created() {
            this.search();
            this.getPutawayMiniProgram();
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
            exportExl() {
                this.loadingTwo = this.$loading({
                    lock: true,
                    text: "Loading",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                });
                this.API.exportAppointment(this.mark).then(result => {
                    console.log(result)
                    if (result && result.size > 0) {
                        this.loadingTwo.close();
                        let url = window.URL.createObjectURL(result);
                        let link = document.createElement("a");
                        link.style.display = "none";
                        link.href = url;
                        link.setAttribute("download", "预约信息信息导出.xls");
                        document.body.appendChild(link);
                        link.click();
                    } else {
                        this.loadingTwo.close();
                        this.$message.error("没有数据需要导出!");
                    }
                })
            },
            deleteItem(id) {
                this.$confirm('此操作将删除该信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.API.removeAppointment({
                        id: id
                    }).then(res => {
                        if (res.success) {
                            this.search();
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            closeEdit() {
                this.editBoxShow = false;
                this.editMark = {
                    status: null,
                    remake: ''
                }
            },
            showEdit(item) {
                this.editItem = item;
                this.editBoxShow = true;
                this.editMark.remake = (this.editItem.remake ? this.editItem.remake : '');
                this.editMark.status = this.editItem.status;
            },
            updateEdit() {
                this.editMark.id = this.editItem.id;
                this.editMark.remake == '' ? this.editMark.remake = '' : this.editMark.remake = this.editMark.remake;
                this.API.updateAppointment(this.editMark).then(res => {
                    if (res.success) {
                        this.search();
                        this.closeEdit();
                    }
                })
            },
            search() {
                if (this.value1 instanceof Array) {
                    this.value1.map((item, index) => {
                        var d = new Date(item);
                        var youWant = d.getFullYear() + '-' + ((d.getMonth() + 1) < 10 ? '0' + (d.getMonth() +
                                    1) :
                                (d.getMonth() + 1)) + '-' + (d.getDate() < 10 ? '0' + d.getDate() : d
                        .getDate()) +
                            ' 00:00:00';
                        index == 0 ? this.mark.appointmentTimeStart = youWant : this.mark.appointmentTimeEnd =
                            youWant;
                    })
                } else {
                    this.mark.appointmentTimeStart = null;
                    this.mark.appointmentTimeEnd = null;
                }
                this.mark.originAppId = this.putawayMiniPro;
                this.API.getAppointmentList(this.mark).then(res => {
                    if (res.success) {
                        this.shopData = res.list;
                        this.totalCount = res.total;
                    }
                })
            },
            reSet() {
                this.value1 = [];
                this.putawayMiniPro = null,
                    this.mark = {
                    page: this.mark.page, //当前页
                    limit: this.mark.limit, //每页显示条数
                    userName: null, //用户名
                    mobile: null, //手机号
                    title: null, //标题
                    status: null, //状态 0,有效,1无效
                    appointmentTimeStart: null, //预约时间开始
                    appointmentTimeEnd: null, //预约时间结束
                    companyId: sessionStorage.getItem('companyID') / 1,
                    originAppId: null
                }
                this.search();
            },
            /*每页加载数据条数*/
            handleSizeChange(val) {
                this.mark.limit = val;
                this.limit = val;
                this.search();
            },
            /*分页跳转*/
            handleCurrentChange(val) {
                this.mark.page = val;
                this.page = val;
                this.search();
            },
        }
    }
</script>

<style lang="scss" scoped>
    @import '../../assets/css/style';

    .ordercumerinfo {
        position: relative;
        top: 50%;
        left: 50%;
        width: 99%;
        height: 98%;
        -webkit-transform: translate(-50%, -50%);
        transform: translate(-50%, -50%);
        background: #fff;
        /*padding: 10px;*/

        .editBox {
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 1;
            background: #fff;
            overflow-x: hidden;

            .navback {
                height: 40px;
                line-height: 40px;
                background: #f0f0f0;
            }

            .title {
                font-size: 18px;
                text-indent: 10px;
            }

            .eidt_main {
                width: 60%;
                margin-top: 30px;
                margin-left: 50px;
                position: relative;

                .edit_item {
                    overflow: hidden;
                    margin-bottom: 30px;
                    display: flex;
                    position: relative;

                    .edit_item_tit {
                        width: 100px;
                        height: 40px;
                        line-height: 40px;
                        text-align: right;
                        font-size: 14px;
                        color: #666;
                        margin-right: 20px;
                    }

                    .edit_item_value {
                        width: 300px;
                        height: 40px;
                        line-height: 40px;
                        background-color: #fafafa;
                        border-radius: 6px;
                        border: solid 1px #dddddd;
                        font-size: 14px;
                        color: #999;
                        padding-left: 10px;
                    }

                    .el-textarea {
                        width: 300px;
                    }

                    .textarea_tit {
                        position: absolute;
                        bottom: 5px;
                        left: 355px;
                        z-index: 1;
                    }
                }
            }

            .edit_butBox {
                margin-top: 50px;
                display: flex;
                width: 100%;
                justify-content: center;

                .edit_butBox_cao {
                    width: 85%;
                    border-top: solid 1px #eee;
                    padding-top: 40px;
                    padding-left: 94px;
                    .edit_but {
                        width: 133px;
                        height: 40px;
                        background-color: #ff6419;
                        border-radius: 20px;
                        border: solid 1px #ff6419;
                        display: inline-block;
                        line-height: 40px;
                        text-align: center;
                        color: #fff;
                        letter-spacing: 3px;
                        margin: 0 20px;
                        cursor: pointer;
                    }
                    .edit_but:first-of-type {
                         background-color: #454545;
                        border: solid 1px #454545;
                    }
                }
            }
        }

        .title {
            height: 80px;
            line-height: 80px;
            border-bottom: solid 1px #ccc;
            font-size: 24px;
            color: #333;
            text-indent: 10px;
        }

        .nav {
            height: 100px;
            display: flex;
            justify-content: flex-start;
            align-items: center;

            .nav_inpu_item {
                display: flex;
                align-items: center;
                justify-content: flex-start;
                margin-left: 15px;

                span {
                    display: inline-block;
                }

                .el-input,
                .el-date-editor,
                .el-select,
                    {
                    width: 180px;
                    vertical-align: top;
                }

                .el-date-editor {
                    width: 300px;
                }
            }

            .nav_inpu_item:first-of-type {
                margin-left: 0;
            }
        }

        .nav_but_box {
            padding: 20px 30px;
            height: 50px;
            line-height: 50px;
            text-align: center;

            .nav_but {
                /*padding: 4px 15px;*/
                display: inline-block;
                width: 96px;
                height: 36px;
                line-height: 36px;
                border-radius: 20px;
                text-align: center;
                color: #fff;
                background: #ff6419;
                margin: 0 8PX;
                cursor: pointer;
            }

            .search:hover {
                background: #ff8347;
            }

            .reset {
                background: #f5f5f5;
                color: #666;
                border: solid 1px #ddd;
            }

            .reset:hover {
                border: solid 1px rgb(255, 193, 163);
                color: #ff6419;
            }
        }

        .output {
            text-align: left;
            padding-left: 20px;
            span {
                color: #FF6419;
                background: rgb(255, 240, 232);
                border: solid 1px rgb(255, 193, 163);
                text-align: center;
                display: inline-block;
                width: 100px;
                height: 30px;
                line-height: 30px;
                border-radius: 20px;
                cursor: pointer;
            }

            span:hover {
                color: #fff;
                background: #ff6419;
            }
        }

        .companyBaseMsg-content {
            padding: 20px;
            background-color: #ffffff;

            .name-class {
                color: red;
            }

            .operation {
                color: #ff6419;
                cursor: pointer;
                padding-left: 10px;
            }

            .shop-name {
                width: 100%;
                border: none;
                color: #ff6419;
                cursor: pointer;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }

            .operation-name {
                color: #333333;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;

            }
        }

        .footer-paging {
            background-color: #ffffff;
            padding: 20px 40px;
            text-align: center;
        }

        .orangeBtn {
            width: 120px;
            height: 32px;
            font-size: 12px;
            text-align: center;
            line-height: 8px;
        }
    }
</style>
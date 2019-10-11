<template>
    <div class="order">
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="订单汇总" disabled name="first">配置管理</el-tab-pane>
            <el-tab-pane label="订单列表" name="second">
                <el-form class="orderListSearch">
                    <el-row :gutter="24">
                        <el-col :span="3">
                            <el-input placeholder="订单号"></el-input>
                        </el-col>
                        <el-col :span="3">
                            <el-select placeholder="订单状态">
                                <el-option>已经完成</el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="3">
                            <el-select placeholder="支付状态">
                                <el-option>已经完成</el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="6" style="max-width: 380px">
                            <el-date-picker
                                    v-model="selectDate"
                                    type="daterange"
                                    align="right"
                                    unlink-panels
                                    range-separator="至"
                                    start-placeholder="下单时间"
                                    end-placeholder="下单时间"
                                    :picker-options="pickerOptions2">
                            </el-date-picker>
                        </el-col>
                        <el-col :span="3">
                            <el-input placeholder="用户名"></el-input>
                        </el-col>
                        <el-col :span="3">
                            <el-input placeholder="收件人"></el-input>
                        </el-col>
                        <el-col :span="3">
                            <el-input placeholder="收件人电话"></el-input>
                        </el-col>
                    </el-row>
                    <el-row :gutter="24" class="buttonSplitLine">
                        <el-col :span="4">
                            <el-button round class="searchBtn " >搜索</el-button>
                            <el-button round class="searchBtn" >重置</el-button>
                        </el-col>
                    </el-row>
                </el-form>
                <div class="splitLineTop orderList">
                    <div>
                        <el-radio-group v-model="OrderStatus" class="OrderStatus">
                            <el-radio-button label="全部订单"></el-radio-button>
                            <el-radio-button label="待发货"></el-radio-button>
                            <el-radio-button label="待签收"></el-radio-button>
                            <el-radio-button label="已完成"></el-radio-button>
                            <el-radio-button label="已关闭"></el-radio-button>
                            <el-radio-button label="未支付"></el-radio-button>
                            <el-radio-button label="已支付"></el-radio-button>
                        </el-radio-group>
                    </div>
                    <table border="1" class="tableData" cellpadding="0" cellspacing="0">
                        <thead>
                          <tr>
                              <th class="thNo">编号</th>
                              <th style="text-align: left">商品</th>
                              <th class="thWidth">商品总金额</th>
                              <th class="thWidth">配送费</th>
                              <th class="thWidth">订单总金额</th>
                              <th class="thWidth">订单状态</th>
                              <th class="thWidth">支付状态</th>
                              <th class="thWidth">操作</th>
                          </tr>
                        </thead>
                    </table>
                    <table class="orderTable" v-for="(order, index) in 4"  border="1" cellpadding="0" cellspacing="0">
                        <thead>
                            <tr>
                                <th colspan="7" class="orderNo">订单号：123456789123456789 <span>用户名：SANYU</span></th>
                                <th class="leftNoBorder">时间：2018-08-08</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr >
                                <td class="thNo center">{{index + 1}}</td>
                                <td>
                                    <div v-for="n in 3"  class="product clear">
                                        <div>
                                            <img src="../../../assets/images/no_img.jpg" width="80" height="80" alt="">
                                        </div>
                                        <div>
                                            <p>asfadsfasdfasdfasdfasfadsfadsf</p>
                                            <p><span>颜色：</span>红色</p>
                                            <p><span>规格：</span>300cm * 80cm</p>
                                            <p>2222 * 2</p>
                                        </div>
                                    </div>
                                </td>
                                <td class="thWidth center">3</td>
                                <td class="thWidth center">4</td>
                                <td class="thWidth center">5</td>
                                <td class="thWidth center">6</td>
                                <td class="thWidth center">7</td>
                                <td class="thWidth center">
                                    <el-button type="text">发货</el-button><br>
                                    <el-button type="text" @click="viewDetail(row)">详情</el-button><br>
                                    <el-button type="text">打印</el-button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                activeName: 'second',
                selectDate: '',
                OrderStatus: '',
                tableData6: [{
                    id: '12987122',
                    name: '王小虎',
                    amount1: '234',
                    amount2: '3.2',
                    amount3: 10
                }, {
                    id: '12987123',
                    name: '王小虎',
                    amount1: '165',
                    amount2: '4.43',
                    amount3: 12
                }]
            }
        },
        methods: {
            handleClick () {},
            viewDetail (row) {
                this.$router.push('')
            }
        }
    }
</script>

<style lang="scss" scoped>
    .order {
        background: #fff;
    }
    .tableData{
        margin-top: 15px;
        background: #fafafa;
        border-collapse: collapse;
        border: solid 1px #dcdcdc;
        width: 100%;
        height: 50px;
        font-weight: normal;
        th{
            height: 50px;
            font-weight: normal;
            color: #666;
            &:nth-of-type(2){
                padding-left: 10px;
            }
        }
    }
    .thWidth{
        width: 150px;
    }
    .thNo{
        width: 60px;
    }
    .leftNoBorder{
        border-left: #fafafa 1px solid
    }
    .center{
        text-align: center;
    }
    .orderTable{
        margin-top: 15px;
        border-collapse: collapse;
        border: solid 1px #dcdcdc;
        width: 100%;
        height: 50px;
        font-weight: normal;
        thead{
            th{
                font-weight: normal;
                background: #fafafa;
                height: 40px;
                border-bottom: solid 1px #dcdcdc;
                &:nth-of-type(1){
                    padding-left: 10px;
                    text-align: left;
                }
                span{
                    display: inline-block;
                    margin-left: 57px;
                }
            }
        }
        .el-button{
            color: #409EFF;
        }
    }
    .product{
        border-top: solid 1px #ddd;
        &:nth-of-type(1){
            border-top:none
        }
        div{
            float: left;
            p{
                line-height: 22px;
                span{
                    color: #999;
                }
                &:last-of-type{
                    color: #FF6419;
                }
            }
            &:last-of-type{
                margin-left: 5px;
            }
        }

       img{
           margin: 5px;
       }
    }
    .clear{
        clear: both;
    }
    .orderList{
        padding:20px;
    }
    .OrderStatus{
        width: 100%;
        background: #fafafa;
    }
    .orderListSearch {
        margin-left: 15px;
        margin-bottom: 15px;
    }
    .buttonSplitLine{
        border-top: solid 1px #ddd;
        margin:25px 0;
        padding-top: 20px;
        .el-button{
            color: #FF6419;
        }
    }
</style>
<style lang="scss">
    .el-tabs__nav {
        margin-left: 15px;
    }
    .el-radio-button__inner{
        background: #fafafa;
        border: none;
    }
    .el-radio-button__orig-radio:checked + .el-radio-button__inner{
        background: #fff;
        border: none;
        color: #666;
        -webkit-box-shadow:none;
        box-shadow:none;
    }
</style>
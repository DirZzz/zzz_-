<template>
    <div>
        <v-nav :nav="nav"></v-nav>
        <div class="activeUserData">
            <header class="headerT">
                <h1>统计数据</h1>
            </header>
            <div class="dataShow">
                <el-date-picker
                        v-model="beginTime"
                        type="datetime"
                        @change="getData"
                        placeholder="选择开始日期时间">
                </el-date-picker>
                <el-date-picker
                        v-model="endTime"
                        type="datetime"
                        @change="getData"
                        placeholder="选择结束日期时间">
                </el-date-picker>
            </div>

            <div class="small">
                <canvas id="userChart" width="1488px" height="426px"></canvas>
            </div>
        </div>
    </div>
</template>

<script>
    import Chart from 'chart.js'
    export default {
        name: "userChart",
        mounted() {
            var chart = document.getElementById('userChart').getContext('2d')

            var myLineChart = new Chart(chart, {
                type: 'line',
                data: this.data,
                options: {
                    tooltips: {
                        cornerRadius: 0,
                        caretSize: 0,
                        xPadding: 16,
                        yPadding: 10,
                        backgroundColor: 'rgba(0, 150, 100, 0.9)',
                        titleFontStyle: 'normal',
                        titleMarginBottom: 15
                    }
                }
            })
        },
        created(){
            this.data = {
                //折线图需要为每个数据点设置一标签。这是显示在X轴上。
                labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
                //数据集（y轴数据范围随数据集合中的data中的最大或最小数据而动态改变的）
                datasets: [{
                    fillColor: 'rgba(220,220,220,0.5)',
                    strokeColor: 'rgba(220,220,220,1)',
                    pointColor: 'rgba(220,220,220,1)',
                    label: 'test1',
                    backgroundColor: 'rgba(225,10,10,0.3)',
                    borderColor: 'rgba(225,103,110,1)',
                    borderWidth: 1,
                    pointStrokeColor: '#fff',
                    pointStyle: 'crossRot',
                    data: [65, 59, 0, 81, 56, 10, 40, 22, 32, 54, 10, 30],
                    cubicInterpolationMode: 'monotone',
                    spanGaps: 'false',

                    //fill: 'false'
                    // fillColor: "rgba(220,220,220,0.5)", //背景填充色
                    // strokeColor: "rgba(220,220,220,1)", //路径颜色
                    // pointColor: "rgba(220,220,220,1)", //数据点颜色
                    // pointStrokeColor: "#fff", //数据点边框颜色
                    // data: [10, 59, 90, 81, 56, 55, 40] //对象数据
                }, {
                    fillColor: 'rgba(151,187,205,0.5)',
                    strokeColor: 'rgba(151,187,205,1)',
                    pointColor: 'rgba(151,187,205,1)',
                    pointStrokeColor: '#fff',
                    data: [28, 48, 40, 19, 96, 27, 200]
                }]
            }

            this.options = {
                scales: {
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value} °C'
                            }
                        }
                    ],
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .headerT {
        padding: 20px;
        border-bottom: solid 1px #e8e8e8;
        color: #666;
        h1: {
            font-size: 20px;
        }
    }

    .activeUserData {
        background: #fff;
    }

    .small {
        width: 1488px;
        height: 426px;
    }

</style>

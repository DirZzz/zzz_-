import VueChart from 'vue-chartjs'
const { reactiveProp } = VueChart.mixins

export default {
    extends: VueChart.HorizontalBar,
    mixins: [reactiveProp],
    data() {
        return {
            Colors: ['#60acfc', "#feb64d", "#32d3eb", "#ff7c7c", "#5bc49f", "#9287e7"],
            datasets: []
        }
    },
    mounted() {
        this.initChart()
    },
    methods: {
        initChart() {
            if (this.chartData && this.chartData.chartDatas instanceof Array) {
                this.datasets.push({
                    pointBackgroundColor: 'white',
                    borderWidth: 2,
                    backgroundColor: this.Colors[0],
                    data: this.chartData.chartDatas.filter(e => e[this.chartData.barChartValues[this.chartData.tabIndex]] != 0).map(e => e[this.chartData.barChartValues[this.chartData.tabIndex]]).filter(e => e != 0),
                })
                this.renderChart({
                    labels: this.chartData.chartDatas.filter(e => e[this.chartData.barChartValues[this.chartData.tabIndex]] != 0).map(e => this.chartData.barChartKeys[this.chartData.tabIndex].split('+').map(sub => e[sub]).join('_')),
                    datasets: this.datasets
                }, {
                        scales: {
                            yAxes: [{
                                ticks: {
                                    beginAtZero: true,
                                    stepSize: 1
                                },
                                gridLines: {
                                    display: true
                                }
                            }],
                            xAxes: [{
                                ticks: {
                                    beginAtZero: true,
                                    userCallback: function (label, index, labels) {
                                        if (Math.floor(label) === label) {
                                            return label;
                                        }
                                    }
                                },
                                gridLines: {
                                    display: false
                                }
                            }]
                        },
                        legend: {
                            display: false
                        }, responsive: true, maintainAspectRatio: false
                    })
            }
        }
    }
}
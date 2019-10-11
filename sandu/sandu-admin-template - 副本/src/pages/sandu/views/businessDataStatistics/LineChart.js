import VueChart from 'vue-chartjs'
const { reactiveProp } = VueChart.mixins

export default {
    extends: VueChart.Line,
    mixins: [reactiveProp],
    data() {
        return {
            borderColor: ['#60acfc', "#feb64d", "#32d3eb", "#ff7c7c", "#5bc49f", "#9287e7"],
            datasets: []
        }
    },
    mounted() {
        this.initChart()
    },
    methods: {
        initChart() {
            if (this.chartData && this.chartData.chartDatas instanceof Array) {
                let obj = this.chartData.detailLabel[this.chartData.tabIndex];
                for (const key in obj) {
                    if (obj.hasOwnProperty(key) && key != "startTime" && key != "id") {
                        const element = obj[key];
                        this.datasets.push({
                            pointBackgroundColor: 'white',
                            borderWidth: 2,
                            backgroundColor: "#0000",
                            label: element,
                            borderColor: this.borderColor[this.datasets.length],
                            data: this.chartData.chartDatas.map(e => e[key])
                        })
                    }
                }
                this.renderChart({
                    labels: this.chartData.chartDatas.map(e => e.startTime),
                    datasets: this.datasets
                }, {
                        scales: {
                            yAxes: [{
                                ticks: {
                                    beginAtZero: true,
                                    userCallback: function (label, index, labels) {
                                        if (Math.floor(label) === label) {
                                            return label;
                                        }
                                    }
                                },
                                gridLines: {
                                    display: true
                                }
                            }],
                            xAxes: [{
                                ticks: {
                                    beginAtZero: true,
                                    stepSize: 1
                                },
                                gridLines: {
                                    display: false
                                }
                            }]
                        }, responsive: true, maintainAspectRatio: false
                    })
            }
        }
    }
}
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
            if (this.chartData instanceof Array) {
                this.datasets = [{
                    pointBackgroundColor: 'white',
                    borderWidth: 3,
                    label: '参与活动用户',
                    backgroundColor: "#0000",
                    borderColor: this.borderColor[0],
                    data: this.chartData.map(e => e.total)
                }];
                this.renderChart({
                    labels: this.chartData.map(e => e.dateTime),
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
                                    stepSize: 1,
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
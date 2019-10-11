<script>
    import VueChartJs from 'vue-chartjs'
    import { Bar, Line } from 'vue-chartjs'
    export default {
        extends: Line,
        props: {
            reglist: { // 砍价人数
                type: Array,
                default () {
                    return []
                }
            },
            regsuccesslist: { // 砍价成功人数
                type: Array,
                default () {
                    return []
                }
            },
            cutlist: { // 砍价人数
                type: Array,
                default () {
                    return []
                }
            },
            labels:{
                type: Array,
                default () {
                    return []
                }
            }
        },
        data(){
            return{
                gradient: null,
                gradient2: null,
                gradient3:null,
                regList:[],
                cutList:[],
                regSuccessList:[],
            }
        },
        mounted () {
            //this.componentsInit()
        },
        watch: {
           labels () {
                this.beginRender()
            },
            /*cutList(){
              this.beginRender()
           },
           regSuccessList(){
              this.beginRender()
           }*/
        },
        methods: {
            componentsInit(b,c,d) {
                this.regList=b;
                this.cutList=c;
                this.regSuccessList=d;
                this.gradient = this.$refs.canvas.getContext('2d').createLinearGradient(0, 0, 0, 550)
                this.gradient2 = this.$refs.canvas.getContext('2d').createLinearGradient(0, 0, 0, 550)
                this.gradient3 = this.$refs.canvas.getContext('2d').createLinearGradient(0, 0, 0, 550)

                this.gradient.addColorStop(0, 'rgba(81,191,249, 0.1)')
                this.gradient.addColorStop(0.5, 'rgba(81,191,249, 0.1)');
                this.gradient.addColorStop(1, 'rgba(81,191,249, 0)');

                this.gradient2.addColorStop(0, 'rgba(96,196,12, 0.1)')
                this.gradient2.addColorStop(0.5, 'rgba(96,196,12, 0.1)');
                this.gradient2.addColorStop(1, 'rgba(96,196,12, 0)');

                this.gradient3.addColorStop(0, 'rgba(241,174,32, 0.1)')
                this.gradient3.addColorStop(0.5, 'rgba(241,174,32, 0.1)');
                this.gradient3.addColorStop(1, 'rgba(241,174,32, 0)');
                /*let timer = setInterval(() => {
                    if (this.labels.length != 0) { this.beginRender(); clearInterval(timer)  }
                }, 100)*/
            },
            beginRender() {
                let i = 0;
                console.log(this.regList);
                console.log(this.cutList);
                console.log(this.regSuccessList);
                this.renderChart({
                    labels:this.labels,
                    datasets: [
                        {
                            yaxisID:"reg",
                            label: '参与人数',
                            borderColor: 'rgb(81,191,249)',
                            pointBackgroundColor: 'rgba(81,191,149,0)',
                            borderWidth: 3,
                            pointBorderColor: 'rgba(81,191,149,0)',
                            backgroundColor: this.gradient,
                            data:this.regList,
                            //data:[1,1,2]
                            //data: [3, 50, 10, 40, 29, 4, 40,40, 39, 10, 40, 29,2, 10,40, 39, 10, 40, 29, 90, 40,10,30,20,5,40, 39, 10, 40, 29, 20, 40,40, 39, 10, 40, 29, 60, 40,40, 39, 10, 40, 29, 90, 40,10,50,20,5]
                        },
                        {
                            yaxisID:"cut",
                            label: '砍价人数',
                            borderColor: 'rgb(96,196,0)',
                            point:null,
                            pointBackgroundColor: 'rgba(96,196,0,0)',
                            borderWidth: 3,
                            pointBorderColor: 'rgba(81,191,149,0)',
                            backgroundColor: this.gradient2,
                            data:this.cutList,
                            //data: [3, 60, 10, 40, 29, 4, 40,40, 39, 10, 40, 29,2, 10,40, 39, 10, 40, 29, 90, 40,10,30,20,5,40, 39, 10, 40, 29, 20, 40,40, 39, 10, 40, 29, 60, 40,40, 39, 10, 40, 29, 90, 40,10,50,20,5]
                            //data:[0, 29, 40, 10, 19, 30, 50,0, 29, 40, 10, 19, 30, 10,0, 29, 40, 10, 19, 40, 30,19, 30, 30,15,40, 39, 10, 40, 29, 60, 40,40, 39, 10, 40, 29, 60, 40,40, 39, 10, 40, 29, 90, 40,10,50,20,5]
                        },
                        {
                            yaxisID:'regSuccess',
                            label: '砍价成功人数',
                            borderColor: 'rgb(241,174,32)',
                            pointBackgroundColor: 'rgba(241,174,32,0)',
                            borderWidth: 3,
                            pointBorderColor: 'rgba(81,191,149,0)',
                            backgroundColor: this.gradient3,
                            data:this.regSuccessList,
                            //data: [3, 80, 10, 40, 29, 4, 40,40, 39, 10, 40, 29,2, 10,40, 39, 10, 40, 29, 90, 40,10,30,20,5,40, 39, 10, 40, 29, 20, 40,40, 39, 10, 40, 29, 60, 40,40, 39, 10, 40, 29, 90, 40,10,50,20,5]
                            //data: [40, 39, 10, 40, 29, 60, 40,40, 39, 10, 40, 29, 60, 40,40, 39, 10, 40, 29, 90, 40,10,50,20,5,0, 29, 40, 10, 19, 30, 50,0, 29, 40, 10, 19, 30, 10,0, 29, 40, 10, 19, 40, 30,19, 30, 30,15]
                        }
                    ],

                }, {
                    responsive: true,
                    maintainAspectRatio: false,
                    /*title: {
                        display: true,
                        text: 'Chart.js Line Chart'
                    },*/
                    tooltips: {
                        mode: 'index',
                        pointBackgroundColor: 'rgba(81,191,149,1)',
                        intersect: false,
                        callbacks: {
                            labelColor: function (tooltipItem, chart) {
                                return {
                                    borderColor: 'rgb(81,191,149)',
                                    backgroundColor: 'rgb(81,191,149)'
                                }
                            },
                            labelTextColor: function (tooltipItem, chart) {
                                return '#fff';
                            }
                        }
                    },
                    hover: {
                        mode: 'nearest',
                        pointBackgroundColor: 'rgba(81,191,149,1)',
                        intersect: true
                    },
                    scales: {
                        xAxes: [{
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: 'Month'
                            }
                        }],
                        yAxes: [
                            {
                            id:'reg',
                            stacked:false,
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: '人数'
                            },
                            ticks:{
                                beginAtZero:true,
                                //tickMarkLength:10,
                                /*callback(lable,index,lables){
                                   console.log(lable,'reg');
                                },*/
                               /* max: 100,
                                min: 0,
                                stepSize: 10*/
                            }
                        },/*{
                                id:'cut',
                                stacked:true,
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: '人数'
                                },
                                ticks:{
                                    beginAtZero:true,
                                    tickMarkLength:10,
                                    callback(lable,index,lables){
                                        console.log(lable,'cut');
                                    },
                                    max: 100,
                                    min: 0,
                                    stepSize: 10
                                }
                        },{
                                id:'regSuccess',
                                stacked:true,
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: '人数'
                                },
                                ticks:{
                                    beginAtZero:true,
                                    tickMarkLength:10,
                                    callback(lable,index,lables){
                                        console.log(lable,'regSuccess');
                                    },
                                    max: 100,
                                    min: 0,
                                    stepSize: 10
                                }
                        }*/
                        ]},
                })
            }

        },
        created(){
            //this.beginRender();
        }
    }
</script>

<style lang="scss" scoped>
    .headerT{
        padding: 20px;
        border-bottom: solid 1px #e8e8e8;
        color: #666;
        h1{
            font-size: 20px;
        }
    }

    .activeUserData{
        background: #fff;
    }

</style>

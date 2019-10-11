<template>
    <div>
        <span>坐标位置：</span>
        <div id="addressPosition"></div>
        <!--<div class="autoPos" v-show="isPosition&&isInit">自动定位中...</div>-->
    </div>
</template>

<script>
    export default {
        name: "map",
        props:['isInit'],//是否初始化定位
        data(){
            return{
               isPosition:true,
                geolocation:'',
                mk:''
            }
        },
        mounted(){
            console.log(this.isInit)
            if(this.isInit){
                this.$nextTick(()=>{
                    let map = new BMap.Map("addressPosition");
                    map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
                    let local = new BMap.LocalSearch(map, {
                        renderOptions:{map: map}
                    });
                    local.search("");
                })
            }
        },
        methods:{
            searchPosition(address){
                let self=this;
                console.log(address)
                let map = new BMap.Map("addressPosition");
                this.$nextTick(()=>{
                    var local = new BMap.LocalSearch(map, {
                        renderOptions:{map: map}
                    });
                    local.setSearchCompleteCallback(function (searchResult) {
                        console.log(searchResult)
                        if(searchResult.getPoi){
                            var poi = searchResult.getPoi(0);
                            map.centerAndZoom(poi.point, 13);
                            self.$emit('getPointXY',poi.point);
                        }else {
                            self.$message.error("您输入的地址无法获取经纬度请查正后测试");
                        }
                    });
                    local.search(address);
                    map.enableScrollWheelZoom(true);
                })

            }
        },
        destroyed(){
            this.geolocation=undefined;
            this.mk=undefined;
        }
    }
</script>

<style lang="scss" scoped>
    #addressPosition{
        display: inline-block;
        width: 800px;
        height: 250px;
        margin-left: 8px;
        margin-bottom: 10px;
        vertical-align: top;
        background-color: #ddd;
    }
    span{
        display: inline-block;
        width: 108px;
        height: 40px;
        line-height: 40px;
        text-align: right;
        vertical-align: top;
    }
    .autoPos{
        color: #ff3838;
        margin-left: 120px;
        padding-bottom: 10px;
    }
</style>
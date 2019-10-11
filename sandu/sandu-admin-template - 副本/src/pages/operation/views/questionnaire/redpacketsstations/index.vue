<template>
    <div class="redpacket">
        <headers></headers>
        <div class="main">
            <div class="num">
                <span>已发送红包金额：<b>{{redPact.useAmount}}</b></span>
                <span>已发送红包个数：<b>{{redPact.useNum}}</b></span>
            </div>
            <el-table border :data="gridData">
                <el-table-column property="date" label="日期" ></el-table-column>
                <el-table-column property="receiveNum" label="领取人数" ></el-table-column>
                <el-table-column property="useDayAmount" label="红包金额" ></el-table-column>
                <el-table-column property="useDayNum" label="红包数量" ></el-table-column>
                <el-table-column property="unreceiveNum" label="未领取人数" ></el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
import headers from "./../../content/headers";
export default {
    components: {
            headers
    },
    data() {
        return {
            redPact:{
                useAmount:0, //已使用红包金额
                useNum:0 // 已使用红包个数
            },
             gridData: []
        }   
    },
    created() {
        this.API.countRedRecket().then(res=>{
            if(res.success) {
                this.redPact = res.obj;
            }
        })
         this.API.countRedRecketList({start:1,limit:1000}).then(res=>{
                if(res.success&&res.datalist) {
                    this.gridData =res.datalist;
                    this.gridData.map(item=>{
                        item.date= item.date.substr(0, 10);
                    })
                }
        })
    }
}
</script>

<style lang="scss" scoped>
      .main {
        margin: 20px;
        padding: 20px 25px;
        background-color: #fff;
        .num {
            height: 50px;
            line-height: 50px;
            text-align: left;
            margin-bottom: 20px;
            span{
                 color: #333;
                 margin-right: 40px;
                 b {
                     color: #3399ff;
                     font-weight: bold;
                 }
            }
        }
      }
</style>

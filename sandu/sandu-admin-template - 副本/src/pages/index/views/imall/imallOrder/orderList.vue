<!-- 订单列表 -->
<template>

<div id="tableView">
    <!--列表顶部搜索和工具条-->
    <el-form ref="form1" :inline="true" :model="frmQuery">
      <el-row class="search">
        <el-col>
          <el-input v-model="frmQuery.orderNo" style="width:160px" placeholder="订单号" maxlength="30"></el-input>
          <el-select v-model="frmQuery.status" style="width:120px" placeholder="订单状态">
            <el-option
                v-for="item in OrderStatusList()"
                :key="item.value"
                :label="item.label"
                :value="item.value">
                </el-option>
          </el-select>
          <el-date-picker
            v-model="frmQuery.startDate"
            type="datetime"
            placeholder="选择日期时间"
            :picker-options="pickerOptionsStart"
            style="width:200px">
          </el-date-picker> -
          <el-date-picker
            v-model="frmQuery.endDate"
            type="datetime"
            placeholder="选择日期时间"
            :picker-options="pickerOptionsEnd"
            style="width:200px">
          </el-date-picker>

          <el-input v-model="frmQuery.buyerNickName" style="width:120px" placeholder="用户名" maxlength="30"></el-input>
          <el-input v-model="frmQuery.consignee" style="width:120px" placeholder="收件人" maxlength="30"></el-input>
          <el-input v-model="frmQuery.mobile" style="width:120px" placeholder="收件人电话" maxlength="11"></el-input>
        </el-col>
      </el-row>
      <el-row class="search" style="text-align:center;margin-top:2px;">
        <el-col>
            <el-button @click="btnSearch()" type="primary" style="font-weight:bold;width:120px" round>搜索</el-button>
            <el-button @click="btnReset()" style="font-weight:bold;background-color:#F5F5F5;width:120px" round>重置</el-button>
        </el-col>
      </el-row>
    </el-form>
    <!-- 列表 -->
    <el-row type="flex" style="width:100%;margin-top:10px;">
      <table ref="table" class="el-table orderList"  style="width:100%;padding:10px;font-size:12px;border-spacing:0;" >
        <thead style="height:30px;line-height:30px;margin:10px 20px 0 10px;font-weight:400;background-color:#F5F5F5;color:#3c3c3c;text-align:center;border:1px solid #e8e8e8;">
          <td style="width:80px;">序号</td>
          <td>商品</td>
          <td style="width:90px;">商品总积分</td>
          <td style="width:90px;">订单总积分</td>
          <td style="width:90px;">订单状态</td>
          <td style="width:120px;">操作</td>
        </thead>
        <tbody class="orderList-tbody" style="overflow-y:auto;width:100%;padding:10px;font-size:12px;border-spacing:0;">
        <tr style="height:98%" v-if="tableData.length==0">
          <td style="text-align:center;">
            暂无数据
          </td>
        </tr>
        <template v-for="(item,index) in tableData">
          <tr style="background-color:#FAFAFA;">
            <td colspan="4" style="text-align:left;padding:10px;">
              订单号:<span style="margin-right:50px">{{item.orderNo}}</span>用户名:<span>{{item.buyerNickName}}</span>
            </td>
            <td colspan="2" style="text-align:right;padding:10px;">
              时间:<span>{{item.gmtCreate}}</span>
            </td>
          </tr>
          <tr class="tr-data">
            <td style="border-left:1px solid #F0F0F0;text-align:center;width:60px;">
              {{index+1}}
            </td>
            <td style="padding:10px;">
              <!-- <el-row v-for="(gift,index) in item.gifts" :key="index"> -->
              <el-row>  
                <el-col>
                  <img style="width:90px;height:90px;float:left" :src="item.itemSmall | filter"/>
                  <div style="float:left;margin:10px;">
                    <span class="gift-name">{{item.itemName}}</span>
                    <p> 积分：<span class="gift-amount">{{item.itemPoint}} * {{item.itemCount}}</span></p>
                  </div>
                </el-col>
              </el-row>
            </td>
            <td style="text-align:center;padding:10px;width:100px;">{{item.totalPoint}}</td>
            <td style="text-align:center;padding:10px;width:100px;">{{item.totalPoint}}</td>
            <td style="text-align:center;padding:10px;width:110px;">
              {{formatOrderStatus(item.status)}}
            </td>
            <td style="text-align:center;width:120px;">
              <el-button v-if="item.status==1&&isPer('points:mall:order:shipments')"  @click="handleClickAffirm(item.id)" type="text" size="small">确认</el-button>
              <el-button v-else-if="item.status==2&&isPer('points:mall:order:shipments')" @click="handleClickSend(item.id)" type="text" size="small">发货</el-button>
              <el-button v-else-if="item.status==4&&isPer('points:mall:order:shipments')" @click="complete(item.id)" type="text" size="small">完成</el-button>
              <router-link style="display:block;" :to="'/imall/imallOrder/orderDetail/'+item.id" v-if="isPer('points:mall:order:view')">
                <el-button type="text" size="small">详情</el-button>
              </router-link>
              <el-button @click="handleClickPrint(item.id)" type="text" size="small" v-if="isPer('points:mall:order:view')">打印</el-button>
            </td>
          </tr>
          <tr style="height:10px"></tr>
        </template>
        </tbody>
      </table>
    </el-row>
     <!--列表底部工具条和分页符-->
    <!--<el-row class="el-row-table-page" style="margin-top:2px;text-align:center" justify="end">-->
      <!--<el-col>-->
    <div class="pageFrame">
        <el-pagination 
          :layout="GLOBAL.table.layout"
          :page-sizes="GLOBAL.table.pageSizes"
          :page-size="frmQuery.limit"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="frmQuery.page"
          :total="total">
        </el-pagination>
    </div>
      <!--</el-col>-->
    <!--</el-row>-->
  <!-- 发货 -->
  <dialogOrderSend ref="dialogOrderSend"></dialogOrderSend>
  <!-- 打印 -->
  <dialogOrderPrint ref="dialogOrderPrint"></dialogOrderPrint>
  <!-- 确认 -->
  <dialogOrderAffirm ref="dialogOrderAffirm"></dialogOrderAffirm>
</div>

</template>
<script>
import imall from '@/api/imall'
import {formatOrderStatus,OrderStatusList} from '@/utils/statusFormat'
import dialogOrderPrint from './components/orderPrint.vue'
import dialogOrderSend from './components/orderSend.vue'
import dialogOrderAffirm from './components/orderAffirm.vue'
import qs from 'qs'
export default {
  name: 'order_list',
  components:{
    dialogOrderPrint,
    dialogOrderSend,
    dialogOrderAffirm
  },
  directives: {
    //waves
  },
  data() {
    return {
      fullHeight: document.documentElement.clientHeight,
      tableHeight:0,
      frmQuery:{
        orderNo:'',
        startDate:'',
        endDate:'',
        buyerNickName:'',
        consignee:'',
        mobile:'',
        status:10,
        limit:10,
        page:1,
        order:'',
        sort:''
      },
      tableData: [],
      total: null,
      listLoading: false,
      multipleSelection: [],
      pickerOptionsStart: {
            disabledDate: time => {
                let endDateVal = this.frmQuery.endDate;
                if (endDateVal) {
                    return time.getTime() > new Date(endDateVal).getTime();
                }
            }
        },
      pickerOptionsEnd: {
            disabledDate: time => {
                let beginDateVal = this.frmQuery.startDate;
                if (beginDateVal) {
                    return (
                        time.getTime() <
                        new Date(beginDateVal).getTime()
                    );
                }
            }
        },
    }
  },
  provide(){
      return{
          refreshMethod:this.refreshMethod
      }
  },
    filters:{
        filter(value){
            if(value){
                return process.env.sourceBaseUrl+"/"+value;
            }
        }
  },
  activated() {
      this.frmQuery.status=parseInt(this.$route.params.status);
      this.$route.params.status==10 ? this.frmQuery.status=null:'';//状态10查全部
       this.getData()
    //this.getList()
    // fetchstructure().then((res=>{
    //   this.structureList=res.data.list
    // }))
  },
  mounted () {
      this.frmQuery.status=parseInt(this.$route.params.status);
      this.$route.params.status==10 ? this.frmQuery.status=null:'';//状态10查全部
      this.getData()
  },
  methods: {
    formatOrderStatus,OrderStatusList,
    handleClickSend(id){//打开发货弹出框
      let dialog=this.$refs.dialogOrderSend;
      dialog.show(id);
    },
      isPer (per) {
          let permiss = qs.parse(sessionStorage.getItem('loginUser'))
          return this.isMainPer(per, permiss)
      },
    complete(id){ //完成
        this.mallApi.completeOrder({orderId:id}).then(res=>{
            if(res.code==200){
                this.$message.success('完成订单');
                this.refreshMethod();
            }else {
                this.$message.error(res.result);
            }
        })
    },
    handleClickAffirm(id){//打开确认弹出框
      let dialog=this.$refs.dialogOrderAffirm;
      dialog.show(id);
    },
    handleClickPrint(id){//打开打印弹出框
      let dialog=this.$refs.dialogOrderPrint;
      dialog.show(id);
    },
    btnSearch(){//查询
      this.frmQuery.page =1;
      this.getData()
    },
btnReset(){
      this.frmQuery={
        orderNo:'',
        startDate:'',
        endDate:'',
        buyerNickName:'',
        consignee:'',
        mobile:'',
        status:null,
        limit:10,
        page:1,
        order:'',
        sort:''
      }
    this.getData()
    },
    refreshMethod(){//子页面调用刷新
      this.getData()
    },
    handleSizeChange(limit){//页面数量改变
      this.frmQuery.limit=limit
      this.getData()
    },
    handleCurrentChange(page){//页数改变
      this.frmQuery.page=page
      this.getData()
    },
    tableSort(sort){//排序
      //sort.prop sort.order
      this.frmQuery.order=sort.prop;
      this.frmQuery.sort=sort.order=='descending'?'desc':'asc';
      this.getData();
    },
    getData() {
      this.listLoading = true
        this.mallApi.getImallOrderList(this.frmQuery).then(data => {
        if (data.code==204){
          this.tableData =[]
          this.listLoading = false    
          this.total=0
          return
        }
        this.tableData=data.list
        this.total=data.total
        this.listLoading = false
      })
    },
    timeFilter(time) {
      return Date.parse(new Date(time))
    },
    handleDelete(row) {
      this.$notify({
        title: '成功',
        message: '删除成功',
        type: 'success',
        duration: 2000
      })
      const index = this.list.indexOf(row)
      this.list.splice(index, 1)
    }
  }
}

</script>
<style type="text/css" scoped>
  .remend-input{
    width: 100px;
  }
  /* .el-table__expand-column{
    display: none !important;
  } */
  .tr-data>td{
    border-right:1px solid #F0F0F0;
  }
  .el-table>td{
    padding:10px;
  }
  .el-table>thead{
    border:1px solid #e8e8e8;
  }
  .gift-name{
    font-size:16px;
    font-weight:bold;
  }
  .gift-amount{
    font-size: 14px;
    color: #FF6419;
    font-weight:bold;
  }
  .print-order .el-col>p:nth-child(1){
    font-size: 15px;
  }
  .print-order .el-col>p:nth-child(2){
    font-weight:bold;
  }
  .el-dialog__body{
    padding:0px 20px !important;
  }

.orderList {
    /*设置相邻单元格的边框间的距离*/
    border-spacing: 0;
    /*表格设置合并边框模型*/
    border-collapse: collapse;
    text-align: center;
}
/*关键设置 tbody出现滚动条*/
.orderList .orderList-tbody {
    display: block;
}

.orderList thead,
.orderList-tbody tr {
    display: table;
    width: 100%;
    table-layout: fixed;
}
/*关键设置：滚动条默认宽度是16px 将thead的宽度减16px*/
.orderList thead {
    width: calc( 100% - 2.8em);
}


.orderList thead th {
    background: #ccc;
}
  .search{
    padding: 20px;
    background-color: #fff;
  }
  .pageFrame {
    background: #fff;
    height: 110px;
    padding-top: 40px;
    box-sizing: border-box;
    text-align: center;
  }
</style>

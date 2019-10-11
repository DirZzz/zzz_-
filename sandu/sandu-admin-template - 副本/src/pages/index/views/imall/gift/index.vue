<!-- 礼品管理 -->
<template>

<div id="tableView" @childMethod='parentMethod'>
    <!--列表顶部搜索和工具条-->
    <el-form ref="form1" :inline="true" :model="frmQuery">
      <el-row class="search">
        <el-col>
          <el-select v-model="frmQuery.isPutaway" placeholder="上架状态">
            <el-option label="全部" value=""></el-option>
            <el-option label="已上架" value="1"></el-option>
            <el-option label="未上架" value="0"></el-option>
          </el-select>
          <el-input v-model="frmQuery.name" style="width:160px" placeholder="礼品名称" maxlength="50"></el-input>
          <el-input v-model="frmQuery.code" style="width:160px" placeholder="礼品编号" maxlength="20"></el-input>
        </el-col>
      </el-row>
      <el-row class="search" style="margin-top:2px;text-align:center;">
        <el-col>
            <el-button @click="btnSearch()" type="primary" style="font-weight:bold;width:120px" round>搜索</el-button>
            <el-button @click="btnReset()" style="font-weight:bold;background-color:#F5F5F5;width:120px" round>重置</el-button>
        </el-col>
      </el-row>
      
      <el-row class="search" style="margin-top:10px;">
        <el-col :span="12">
          <el-dropdown type="info" szie="small" placement="bottom-start">
            <span class="el-dropdown-link">
              <el-button type="info" round  size="small" style="">批量操作<i class="el-icon-arrow-down el-icon--right"></i></el-button>
            </span>
            <el-dropdown-menu slot="dropdown" >
              <el-dropdown-item @click.native="updatePutawayBatch(1)"><i class="el-icon-upload2"></i> 批量上架</el-dropdown-item>
              <el-dropdown-item @click.native="updatePutawayBatch(0)"><i class="el-icon-download"></i> 批量下架</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <router-link :to="'/imall/gift/create'">
              <el-button size="small" type="primary" round>新增礼品</el-button> 
            </router-link>
        </el-col>
      </el-row>

    </el-form>
    <el-row class="search" type="flex" style="padding-bottom: 30px">
      <el-table ref="table" :data="tableData" border
        @sort-change="tableSort"
        @selection-change="tableSelectionChange"
        size="mini"
        v-loading="listLoading"
        :element-loading-text="GLOBAL.loadingText" 
        fit highlight-current-row style="width: 100%">
        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column prop="code" label="编号" sortable="custom"></el-table-column>
        <el-table-column label="缩略图" width="180">
          <template slot-scope="scope">
            <i v-if="scope.row.small==null" style="width:62px;height:62px;font-size:62px" class="el-icon-picture"></i>
            <img v-else style="width:62px;height:62px" :src="scope.row.small | filter"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" sortable="custom"></el-table-column>
        <el-table-column prop="price" label="礼品价格" sortable="custom" width="160"></el-table-column>
        <el-table-column prop="point" label="所需积分" sortable="custom" width="160"></el-table-column>
        <el-table-column prop="inventory" label="库存" sortable="custom" width="160"></el-table-column>
        <el-table-column prop="gmtCreate" label="录入时间" show-overflow-tooltip sortable="custom" width="180"></el-table-column>
        <el-table-column label="上架" width="160" v-if="isPer('points:mall:gift:edit')">
          <template slot-scope="scope1">
            <el-switch
              v-model="scope1.row.isPutaway"
              :active-text="formatStatus(scope1.row.isPutaway)"
              active-value="1"
              inactive-value="0"
              @change="updateStatus(scope1.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="160">
          <template slot-scope="scope">
            <router-link :to="'/imall/gift/edit/'+scope.row.id" v-if="isPer('points:mall:gift:edit')">
              <el-button type="text" size="small">编辑</el-button>
            </router-link>
            <el-button @click="handleDeleteClick(scope.row.id,scope.row.name)" v-if="isPer('points:mall:gift:edit')" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
     <!--列表底部工具条和分页符-->
    <el-row class="el-row-table-page" style="margin-top:1px;text-align:center" type="flex" justify="end">
      <el-col>
        <el-pagination 
          :layout="GLOBAL.table.layout"
          :page-sizes="GLOBAL.table.pageSizes"
          :page-size="frmQuery.limit"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="frmQuery.page"
          style="padding: 20px 0"
          :total="total">
        </el-pagination>
      </el-col>
    </el-row>

</div>
</template>
<script>
import imall from '@/api/imall'
import { parseTime } from '@/utils'
import store from '@/store'
import qs from 'qs'
export default {
  name: 'gift_list',
  directives: {
    //waves
  },
  data() {
    return {
      fullHeight: document.documentElement.clientHeight,
      tableHeight:300,
      frmQuery:{
        name:'',
        code:'',
        isPutaway:null,
        limit:10,
        page:1,
        order:'',
        sort:''
      },
      tableData: [],
      total: null,
      listLoading: true,
      status: {
        0: "草稿",
        1: "正常",
        7: "删除"
      },
      multipleSelection: []
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info',
        7: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    },
      filter(value){
          return process.env.sourceBaseUrl+"/"+value;
      }
  },
  activated() {
    this.getData()
  },
  mounted () {
      this.getData()
  },
  methods: {
      isPer (per) {
          let permiss = qs.parse(sessionStorage.getItem('loginUser'))
          return this.isMainPer(per, permiss)
      },
    updatePutawayBatch(putaway){
      let ids='',that=this;
      if(this.multipleSelection.length>0){
          for(var i=0;i<this.multipleSelection.length;i++){
              if(i==this.multipleSelection.length){break;}
              ids+=this.multipleSelection[i].id+',';
          }
          var txt=putaway==0?'下架':'上架';
          let params={
              ids:ids,
              putaway:putaway
          };
          this.mallApi.updateGiftPutaway(params).then(data => {
              if (data.code==200){
                  that.$message({
                      duration:1000,
                      center: true,
                      showClose: true,
                      message: '批量'+txt+'成功',
                      type: 'success'
                  });
                  that.getData()
              }else{
                  that.$message({
                      duration:1000,
                      center: true,
                      showClose: true,
                      message: '批量操作异常',
                      type: 'warning'
                  });
              }
          })
      }else {
          this.$message.warning('请选择一条或多条记录');
      }

    },
    tableSelectionChange(val) {
      this.multipleSelection = val;
    },
    btnSearch(){//查询
       this.frmQuery.page=1;
      this.getData()
    },
    btnReset(){//重置
      this.frmQuery={
        name:'',
        code:'',
        isPutaway:'',
        limit:10,
        page:1,
        order:'',
        sort:''
      }
        this.getData()
    },
    parentMethod:(params)=>{//子页面调用
      this.getData()
    },
    formatIsPutaway(val){
      console.log(val+"")
      return val+'';
      //return new Number(val);
    },
    formatStatus(status){
      const statusMap = {
        0: '未上架',
        1: '上架'
      }
      return statusMap[status]
    },
    updateStatus(row){//修改状态
      let putaway=row.isPutaway;
      let that=this;
      var status=putaway==0?'下架':'上架';
      var name=row.name;
      var params={
        ids:row.id,
        putaway:row.isPutaway
      };
        this.mallApi.updateGiftPutaway(params).then(data => {
        if (data.code==200){
          that.$message({
            duration:1000,
            center: true,
            showClose: true,
            message: name+':'+status,
            type: 'success'
          });
        }else{
          that.$message({
            duration:1000,
            center: true,
            showClose: true,
            message: '操作异常',
            type: 'warning'
          });
          row.isPutaway=putaway
        }
        
      })
    },
    handleDeleteClick(id,name){//删除
      let that = this;
            
      this.$confirm('删除？','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
      }).then(() => {
        var params={id:id};
          this.mallApi.deleteGift(params).then(data => {
            if (data.code==200){
              that.$message({
                duration:1000,
                center: true,
                showClose: true,
                message: name+':删除成功',
                type: 'success'
              });
              that.getData();
            }else{
              that.$message({
                duration:1000,
                center: true,
                showClose: true,
                message: name+':删除异常',
                type: 'warning'
              });
            }
          })
      }).catch((meg) => console.log(meg))
    },
    removeSelection(){

    },
    handleSizeChange(limit){//
      this.frmQuery.limit=limit
      this.getData()
    },
    handleCurrentChange(page){//页数
      this.frmQuery.page=page
      this.getData()
    },
    handleClick(){

    },
    tableSort(sort){//排序
    //sort.prop sort.order
      this.frmQuery.order=sort.prop;
      this.frmQuery.sort=sort.order=='descending'?'desc':'asc';
      this.getData();
    },
    getData() {
      this.listLoading = true

      // if(this.listQuery.startTime){
      //   this.listQuery.startTime=this.timeFilter(this.listQuery.startTime)
      // }
      // if(this.listQuery.endTime){
      //   this.listQuery.endTime=this.timeFilter(this.listQuery.endTime)
      // }
        this.mallApi.getGifts(this.frmQuery).then(data => {
        if (data.code==204){
          this.tableData =[]
          this.listLoading = false    
          this.total=0
          return
        }
        this.tableData=data.list
        for(var i=0;i<this.tableData.length;i++){
          if (this.tableData[i].isPutaway==1){
            this.tableData[i].isPutaway="1"
          }else{
            this.tableData[i].isPutaway="0"
          }
        }
        this.total=data.total
        this.listLoading = false
      })
    },

    timeFilter(time) {
      return Date.parse(new Date(time))
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          createArticle(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import ('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel(tHeader, data, 'table-list')
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}

</script>
<style type="text/css" scoped>
  .remend-input{
    width: 100px;
  }
  .search{
    padding: 20px;
    background-color: #fff;
  }
</style>

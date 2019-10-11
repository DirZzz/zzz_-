<template>
  <div class="plan-product">
    <el-table
      :data="products"
      class="pro-table"
      style="width: 100%;"
      ref="multipleTable"
      tooltip-effect="dark"
      :default-sort="{prop: 'id', order: 'descending'}"
    >
      <el-table-column prop="productCode" label="编号" align="center" width="160" sortable>
        <template slot-scope="scope" class="table-td-left">
          <div>{{scope.row.productCode}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="picPath" label="产品缩略图" width="120">
        <template slot-scope="scope">
          <img :src="scope.row.picPath" width="90" height="90"/>
        </template>
      </el-table-column>
      <el-table-column prop="categoryNames" label="类别">
        <template slot-scope="scope">
          <p v-if="scope.row.categoryNames">
            {{scope.row.categoryNames.replace(/\//g, '>')}}
          </p>
          <p v-else>暂无类别</p>
        </template>
      </el-table-column>
      <el-table-column prop="productName" label="名称">

      </el-table-column>
      <el-table-column prop="brandName" label="品牌">
        <template slot-scope="scope">
          <p v-if="scope.row.brandName">
            {{scope.row.brandName}}
          </p>
          <p v-else>暂无品牌</p>
        </template>
      </el-table-column>
      <el-table-column prop="typeCode" label="型号">
        <template slot-scope="scope">
          <p v-if="scope.row.modelNumber">
            {{scope.row.modelNumber}}
          </p>
          <p v-else>暂无型号</p>
        </template>
      </el-table-column>
     <!-- <el-table-column prop="size" label="规格">
        <template slot-scope="scope">
          <p v-if="scope.row.size">
            {{scope.row.size}}
          </p>
          <p v-else>暂无规格</p>
        </template>
      </el-table-column>-->
      <!--<el-table-column prop="status" label="上架状态" sortable>
        <template slot-scope="scope">
          <div v-if="scope.row.status2b">渠道已上架</div>
          <div v-if="scope.row.status2c">线上已上架</div>
        </template>
      </el-table-column>-->
      <el-table-column prop="usedCount" label="采用数量">
      </el-table-column>
      <el-table-column prop="companyName" label="所属公司">
        <template slot-scope="scope">
          <p v-if="scope.row.companyName">
            {{scope.row.companyName}}
          </p>
          <p v-else>暂无所属公司</p>
        </template>
      </el-table-column>
      <el-table-column prop="isOpen" align="center" width="110" label="是否公开">
        <template slot-scope="scope">
          <div>{{!scope.row.secrecy ? '是' : '否'}}</div>
        </template>
      </el-table-column>
   <!--   <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.displayStatus"
            @change="toggleStatus(scope.row)"
            active-text="显示"
            inactive-text="不显示"
            :active-value="0"
            :inactive-value="1"
          >
          </el-switch>
        </template>
      </el-table-column>-->
    </el-table>

  </div>
</template>

<script>
  export default {
    name: 'PlanProduct',
    props: {
      products: Array
    },
    data () {
      return {
      }
    },
    methods: {
      toggleStatus (row) {
        const params = {
          planId: this.$route.params.planId,
          productId: row.id,
          status: row.displayStatus
        }
        if (this.$route.fullPath.includes('onekey')) {
          this.API.OnekeyPlanToggleProductDisplay(params).then(resp => {
          })
        } else if (this.$route.fullPath.includes('common')) {
          this.API.CommonPlanToggleProductDisplay(params).then(resp => {
          })
        }
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
  .pro-table {
    margin-top: 20px;
  }
</style>
<style>
  .el-tabs__nav {
    margin-left: 30px;
  }
  .el-tabs__item{
    height: 50px;
    line-height: 50px;
  }
</style>

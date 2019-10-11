<template>
  <div>
    <el-table class="enterpriseTab"
      ref="multipleTable"
      :data="tableData3"
      tooltip-effect="dark"
      @selection-change="handleSelectionChange">
      <el-table-column
        prop="companyPost"
        label="企业编号"
        width="120">
        <template slot-scope="scope">{{ scope.row.date }}</template>
      </el-table-column>
      <el-table-column
        prop="companyName"
        label="企业名称"
        width="120">
      </el-table-column>
      <el-table-column
        prop="roleType"
        label="角色类型">
      </el-table-column>
      <el-table-column
        prop="status"
        label="生效状态">
      </el-table-column>
      <el-table-column
         prop="dateBegin"
         label="合作生效日期">
      </el-table-column>
      <el-table-column
        prop="dateEnd"
        label="合作结束日期"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        prop="backup"
        label="备注"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        prop="opeartion"
        label="操作">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">详情</el-button>
          <el-button type="text" size="small">编辑</el-button>
          <el-button type="text" size="small">暂停</el-button>
          <el-button type="text" size="small">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        tableData3: [{
          companyPost: '王小虎',
          companyName: '2016-05-03',
          roleType: '超级管理员',
          status: '生效中',
          dateBegin: '2018.1.8',
          dateEnd: '2018.9.10',
          backup: '上海市普陀区金沙江路 1518 弄',
          opeartion: ''
        }, {
          companyPost: '王小虎',
          companyName: '2016-05-03',
          roleType: '超级管理员',
          status: '生效中',
          dateBegin: '2018.1.8',
          dateEnd: '2018.9.10',
          backup: '上海市普陀区金沙江路 1518 弄',
          opeartion: ''
        }, {
          companyPost: '王小虎',
          companyName: '2016-05-03',
          roleType: '超级管理员',
          status: '生效中',
          dateBegin: '2018.1.8',
          dateEnd: '2018.9.10',
          backup: '上海市普陀区金沙江路 1518 弄',
          opeartion: ''
        }, {
          companyPost: '王小虎',
          companyName: '2016-05-03',
          roleType: '超级管理员',
          status: '生效中',
          dateBegin: '2018.1.8',
          dateEnd: '2018.9.10',
          backup: '上海市普陀区金沙江路 1518 弄',
          opeartion: ''
        }, {
          companyPost: '王小虎',
          companyName: '2016-05-03',
          roleType: '超级管理员',
          status: '生效中',
          dateBegin: '2018.1.8',
          dateEnd: '2018.9.10',
          backup: '上海市普陀区金沙江路 1518 弄',
          opeartion: ''
        }, {
          companyPost: '王小虎',
          companyName: '2016-05-03',
          roleType: '超级管理员',
          status: '生效中',
          dateBegin: '2018.1.8',
          dateEnd: '2018.9.10',
          backup: '上海市普陀区金沙江路 1518 弄',
          opeartion: ''
        }, {
          companyPost: '王小虎',
          companyName: '2016-05-03',
          roleType: '超级管理员',
          status: '生效中',
          dateBegin: '2018.1.8',
          dateEnd: '2018.9.10',
          backup: '上海市普陀区金沙江路 1518 弄',
          opeartion: ''
        }]
      };
    },

    methods: {
      toggleSelection (rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
      handleSelectionChange (val) {
        this.multipleSelection = val;
      }
    }
  };
</script>

<style scoped>
.enterpriseTab{
  width: 100%;
}
</style>

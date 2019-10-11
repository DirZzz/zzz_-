<template>
  <div class="companyFrame">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基础信息" name="first">
        <el-row :gutter="24" class="elRow">
          <el-col :span="12">
            <ul class="companyLogo">
              <li>企业Logo：</li>
              <li><img height="100" src="../../assets/sandu_logo.png" alt=""></li>
              <li><el-button round size="small" class="el-button--primary">重新上传</el-button></li>
            </ul>
          </el-col>
          <el-col :span="12">
            <ul class="companyBuesRange">
              <li><span>企业名称：</span> {{companyInfo.name}}</li>
              <li><span>经营范围：</span> {{companyInfo.categoryIds}}</li>
              <li><span>经营品牌：</span> {{companyInfo.brands}}</li>
            </ul>
          </el-col>
        </el-row>
        <el-row class="companyAutList">
          <h1>企业授权</h1>
          <el-table
            border
            :data="tableData"
            class="authTable"
            style="width: 100%">
            <el-table-column
              prop="authName"
            >
            </el-table-column>
            <el-table-column
              prop="total"
              label="总个数"
            >
            </el-table-column>
            <el-table-column
              prop="used"
              label="已使用"
              >
            </el-table-column>
            <el-table-column
              prop="unused"
              label="未使用">
            </el-table-column>
          </el-table>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="品牌网站设计" name="second">
        <el-row :gutter="24" class="elRow">
          <el-col :span="12">
            <ul class="companyLogo">
              <li>品牌Logo：</li>
              <li><img height="100" :src="companyInfo.logo" src="../../assets/sandu_logo.png" alt=""></li>
              <li><el-button round size="small" class="el-button--primary">重新上传</el-button></li>
            </ul>
          </el-col>
          <el-col :span="12">
            <ul class="companyBuesRange spanWidth">
              <li><span>企业经营品牌：</span> {{companyInfo.brands}}</li>
              <li><span>企业二级域名：</span> {{companyInfo.domain}} <el-button type="text" class="el-icon-edit" @click="setDomain">编辑</el-button></li>
              <li><span>企业QQ：</span> {{companyInfo.qq}} <el-button type="text" class="el-icon-edit" @click="editQQ">编辑</el-button></li>
            </ul>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="修改二级域名" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="企业二级域名" :label-width="formLabelWidth">
          <el-input v-model="form.name" auto-complete="off" style="width: 86%"></el-input> .3du-ci.cn
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogFormVisible = false" round>确 定</el-button>
        <el-button @click="dialogFormVisible = false" round>取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改企业QQ" :visible.sync="dialogQQVisible">
      <el-form :model="form">
        <el-form-item label="企业QQ" :label-width="formLabelWidth">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogQQVisible = false" round>确 定</el-button>
        <el-button @click="dialogQQVisible = false" round>取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  export default {
    data () {
      return {
        dialogQQVisible: false,
        dialogFormVisible: false,
        formLabelWidth: '120px',
        activeName: 'first',
        companyInfo: {},
        tableData: [{
          authName: '通用版',
          total: '22',
          used: '2',
          unused: '20'
        }, {
          authName: '通用版',
          total: '22',
          used: '2',
          unused: '20'
        }, {
          authName: '通用版',
          total: '22',
          used: '2',
          unused: '20'
        }, {
          authName: '通用版',
          total: '22',
          used: '2',
          unused: '20'
        }],
        form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        }
      };
    },
    methods: {
      handleClick (tab, event) {
      },
      setDomain () {
        this.dialogFormVisible = true;
      },
      editQQ () {
        this.dialogQQVisible = true;
      },
      getCompanyInfo () {
        this.API.getCompanyDetail({
          companyId: sessionStorage.getItem('companyID')
        }).then(res => {
          if (res) {
            this.companyInfo = res.data;
            alert(res.data.name)
          }
        })
      },
    },
    mounted () {
      this.getCompanyInfo();
    }
  }
</script>

<style lang="scss" scoped>
  .dialog-footer{
    text-align: center;
    .el-button{
      border:none;
      width: 100px;
      &:nth-of-type(2){
        background: #454545;
        color: #fff;
      }
    }
  }
.companyFrame {
  min-height: 800px;
  background: #fff;
  padding: 20px;
  box-sizing: padding-box;
  border-radius: 4px;
  .elRow{
    border-bottom: solid 1px #e3e3e3;
    margin-bottom: 15px;
  }
  }
.companyAutList{
  margin-top: 10px;
  h1{
    font-size: 14px;
    padding-top: 20px;
    padding-bottom: 15px
  }
}
  .companyLogo{
    margin-top: 40px;
    li{
      display: inline-block;
      vertical-align: middle;
      min-width:120px;
      padding-left: 25px;
    }
  }
  .companyBuesRange{
    border-left: solid 1px #e3e3e3;
    padding-left: 40px;
    padding-top: 40px;
    li{
      padding-bottom: 20px;
      position: relative;
      .el-button{
        position: absolute;
        right: 0;
      }
    }
  }
  .spanWidth{
    span{
      display: inline-block;
      width: 180px;
    }
  }
</style>
<style lang="scss">
  .authTable{
  th{
    text-align: center;
  }
  td{
    text-align: center;
  }
  }
</style>

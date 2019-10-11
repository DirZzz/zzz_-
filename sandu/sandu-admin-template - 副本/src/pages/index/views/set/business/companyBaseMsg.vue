<template>
  <div class="companyBaseMsg">
    <div class="companyBaseMsg-header">
      <h1>店铺管理</h1>
      <el-row class="header-screen">
        <el-col :span="3" class="header-screen-input">
          <el-input size="medium" placeholder="请输入店铺编码" v-model="storeCode" @change="seekStoreList('店铺编码')"></el-input>
        </el-col>
        <el-col :span="3" class="header-screen-input">
          <el-input size="medium" placeholder="请输入店铺名称" v-model="storeName" @change="seekStoreList('店铺名称')"></el-input>
        </el-col>
        <el-col :span="3" class="header-screen-input">
          <el-input size="medium" placeholder="请输入联系人姓名" v-model="contactsName"  @change="seekStoreList('联系人姓名')"></el-input>
        </el-col>
        <el-col :span="3" class="header-screen-input">
          <el-input size="medium" placeholder="请输入联系人电话" v-model="contactsPhone" @change="seekStoreList('联系人电话')"></el-input>
        </el-col>
        <el-col :span="10"class="screen-city">
          <el-row>
            <el-col :span="6" class="screen-city-item">
              <el-select v-model="provinceCode" size="medium" placeholder="请选择省" @change="seekStoreList('省')">
                <el-option
                        v-for="item in province"
                        :key="item.areaCode"
                        :label="item.areaName"
                        :value="item.areaCode">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6" class="screen-city-item">
              <el-select v-model="cityCode" size="medium" placeholder="请选择市" @change="seekStoreList('市')">
                <el-option
                        v-for="item in city"
                        :key="item.areaCode"
                        :label="item.areaName"
                        :value="item.areaCode">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6" class="screen-city-item">
              <el-select v-model="districtCode" size="medium" placeholder="请选择区" @change="seekStoreList('区')">
                <el-option
                        v-for="item in district"
                        :key="item.areaCode"
                        :label="item.areaName"
                        :value="item.areaCode">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6" class="screen-city-item">
              <el-select v-model="streetCode" size="medium" placeholder="请选择街道" @change="seekStoreList('街道')">
                <el-option
                        v-for="item in street"
                        :key="item.areaCode"
                        :label="item.areaName"
                        :value="item.areaCode">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      <div class="header-seek">
        <el-button type="primary" round class="seek" size="medium" @click="StoreList">搜索</el-button>
        <el-button type="primary" round class="seek" size="medium" @click="seekStoreList('重置')">重置</el-button>
        <el-button type="primary" round class="add-store" size="medium" @click="goStore('新增店铺')">新增店铺</el-button>
      </div>
    </div>
    <div class="companyBaseMsg-content">
      <el-table
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(255, 255, 255, 0.8)"
              :data="storeData"
              style="width: 100%">
        <el-table-column
                prop="shopCode"
                label="店铺编码"
                width="180">
        </el-table-column>
        <el-table-column
                prop="shopName"
                label="店铺名称"
                width="180">
          <template slot-scope="scope">
            <span class="operation-name" @click="goStore('店铺详情',scope)">{{scope.row.shopName}}</span>
          </template>
        </el-table-column>
        <el-table-column
                width="110"
                prop="contactName"
                label="联系人姓名">
        </el-table-column>
        <el-table-column
                width="110"
                prop="contactPhone"
                label="联系人电话">
        </el-table-column>
        <el-table-column
                prop="areaName"
                label="区域">
        </el-table-column>
        <el-table-column
                width="200"
                prop="issue"
                label="发布">
        </el-table-column>
        <el-table-column
                width="200"
                prop="operation"
                label="操作 ">
          <template slot-scope="scope">
            <span class="operation" @click="goStore('编辑店铺', scope)">编辑</span>
            <span class="operation" @click="deleteStore(scope)">删除</span>
            <span class="operation" @click="goStore('工程案例', scope)">工程案例</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="footer-paging">
      <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="start"
              :page-sizes="[10, 20, 30, 40]"
              :page-size="10"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalCount">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  // import qs from 'qs';
  export default {
    data () {
      return {
          storeCode: '', // 店铺编码
          storeName: '', // 店铺名称
          contactsName: '', // 联系人姓名
          contactsPhone: '', // 联系人电话
          province: [], // 省份
          provinceCode: '',
          city: [], // 市
          cityCode: '',
          district: [], // 区
          districtCode: '',
          street: [], // 街道
          streetCode: '',
          storeData: [], // 店铺数据表格
          totalCount: 0, // 店铺数据总条数
          start: 1, // 页码
          limit: 10, // 每页条数
          getStoreList: {
              // companyId: sessionStorage.getItem('companyID'), // 企业Id
              companyType: undefined, // 企业类型
              provinceCode: undefined, // 区域省编码
              cityCode: undefined, // 区域市编码
              areaCode: undefined, // 区域区编码
              streetCode: undefined, // 区域街道编码
              contactName: undefined, // 联系人姓名
              contactPhone: undefined, // 联系人电话
              shopCode: undefined, // 店铺编码
              shopName: undefined, // 店铺名称
              start: 1, // 页码
              limit: 10 // 每页条数
          }
      };
    },
    created () {
        this.StoreList();
        this.getAreaList(0, '省');
    },
    methods: {
        /*跳转页面
        * type: 用于判断跳转到编辑，新增，详情页面，根据type不同显示相应的内容
        * */
        goStore(type, scope) {
            sessionStorage.setItem('routerPath', this.$route.path);
            sessionStorage.setItem('routerTxt', type);
            if (type != '新增店铺') {
                sessionStorage.setItem('shopId', scope.row.scope);
            }
            this.$router.push('/business/addStore');
        },
        /*删除操作*/
        deleteStore(scope) {
          this.$confirm('此操作将上次当前店铺信息，是否继续','提示',{
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }),then(() => {
              this.$message({
                  type: 'success',
                  message: '删除成功!'
              });
          }).catch(() => {
              this.$message({
                  type: 'info',
                  message: '已取消删除'
              });
          });
        },
        /*每页加载数据条数*/
        handleSizeChange(val) {
            this.getStoreList.limit = val;
            this.limit = val;
        },
        /*分页跳转*/
        handleCurrentChange(val) {
            this.start = val;
            this.getStoreList.start = val - 1;
            this.StoreList();
        },
        /*条件搜索店铺数据*/
        seekStoreList(type) {
            if (type == '店铺编码') {
                this.getStoreList.shopCode = this.storeCode;
            }
            if (type == '店铺名称') {
                this.getStoreList.shopName = this.storeName;
            }
            if (type == '联系人姓名') {
                this.getStoreList.contactName = this.contactsName;
            }
            if (type == '联系人电话') {
                this.getStoreList.contactPhone = this.contactsPhone;
            }
            if (type == '省') {
                this.getStoreList.provinceCode = this.provinceCode;
                this.getAreaList(this.provinceCode, '市');
            }
            if (type == '市') {
                this.getStoreList.cityCode = this.cityCode;
                this.getAreaList(this.cityCode, '区');
            }
            if (type == '区') {
                this.getStoreList.areaCode = this.districtCode;
                this.getAreaList(this.districtCode, '街道');
            }
            if (type == '街道') {
                this.getStoreList.streetCode = this.streetCode;
            }
            if (type == '重置') {
               console.log(this.getStoreList);
               this.getStoreList = {
                   companyType: undefined, // 企业类型
                   provinceCode: undefined, // 区域省编码
                   cityCode: undefined, // 区域市编码
                   areaCode: undefined, // 区域区编码
                   streetCode: undefined, // 区域街道编码
                   contactName: undefined, // 联系人姓名
                   contactPhone: undefined, // 联系人电话
                   shopCode: undefined, // 店铺编码
                   shopName: undefined, // 店铺名称
                   start: this.start, // 页码
                   limit: this.limit // 每页条数
               }
                this.storeCode = '';
                this.storeName = '';
                this.contactsName = '';
                this.contactsPhone = '';
                this.provinceCode = '';
                this.cityCode = '';
                this.districtCode = '';
                this.streetCode = '';
                this.StoreList();
            }
        },
        /*请求店铺列表*/
        StoreList() {
            this.API.storeList(this.getStoreList).then((res) => {
                this.storeData = res.datalist;
                this.totalCount = res.totalCount;
            });
        },
        /*省市区联动*/
        getAreaList(areaCode, type) {
            this.API.areaList({areaCode:areaCode}).then((res) => {
                console.log(res);
                if (res.datalist) {
                    if (type == '省') {
                        this.province = res.datalist;
                    }
                    if (type == '市') {
                        this.city = res.datalist;
                    }
                    if (type == '区') {
                        this.district = res.datalist;
                    }
                    if (type == '街道') {
                        this.street = res.datalist;
                    }
                }
            })
        }
    }
  }
</script>

<style lang="scss" scoped>
  @import "../../../assets/css/mixin";

  .companyBaseMsg{
    .companyBaseMsg-header{
      width: 100%;
      background-color: #ffffff;
      h1{
        width: 100%;
        height: 58px;
        font-size: 18px;
        line-height: 58px;
        padding-left: 20px;
        box-sizing: border-box;
        border-bottom: 1px solid #dddddd;
      }
      .header-screen{
        width: 100%;
        padding: 20px 20px 0 20px;
        border-bottom: 1px solid #dddddd;
        .header-screen-input{
          width: 200px;
          padding: 0 10px 20px 10px;
        }
        .screen-city{
          line-height: 36px;
          .screen-city-item{
            width: 150px;
            padding: 0 10px 20px 10px;
          }
        }
      }
      .header-seek{
        padding: 20px 30px;
        .seek{
          width: 100px;
          border: solid 1px #dddddd;
          background-color: #f5f5f5;
          color: #ff6419;
        }
        .add-store{
          float: right;
        }
      }
    }
    .companyBaseMsg-content{
      margin-top: 20px;
      padding: 20px;
      background-color: #ffffff;
      .operation{
        color: #ff6419;
        margin-left: 10px;
        cursor:pointer;
      }
      .operation-name{
        color: #ff6419;
        cursor:pointer;
      }
    }
    .footer-paging{
      width: 100%;
      background-color: #ffffff;
      padding: 20px 40px;
      text-align: center;
    }
  }

</style>


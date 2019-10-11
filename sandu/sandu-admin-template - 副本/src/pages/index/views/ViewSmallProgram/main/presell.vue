<template>
    <!-- 特卖页面-->
    <div class="presell">
        <el-main>
            <!-- 添加 按钮 -->
            <div class="btnbox">
                <span class="addbtn" @click="SpesellList">添加</span>
            </div>

            <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)" style="width: 100%" :default-sort="{prop: 'code', order: 'descending'}" :data="spesell_mainList">
                <el-table-column label="编号">
                    <template slot-scope="scope">
                        <div>
                            {{scope.row.spuCode}}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="商品缩略图">
                    <template slot-scope="scope">
                        <div>
                            <div class="imgdiv" v-if="!scope.row.picPath">
                                无缩略图
                            </div>
                            <img width="90px" height="90px" v-else :src="   scope.row.picPath">
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="名称">
                    <template slot-scope="scope">
                        <div>
                            {{scope.row.spuName}}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="商品分类">
                    <template slot-scope="scope">
                        <div>
                            {{scope.row.bigType}}>{{scope.row.smallType}}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="原价">
                    <template slot-scope="scope">
                        <div v-if="!scope.row.minSalePrice==scope.row.maxSalePrice">
                            {{scope.row.minSalePrice}}~{{scope.row.maxSalePrice}}
                        </div>
                        <div v-else>
                            {{scope.row.minSalePrice}}

                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="优惠价">
                    <template slot-scope="scope">
                        <div v-if="!scope.row.minPrice==scope.row.maxPrice ">
                            {{scope.row.minPrice}}~ {{scope.row.maxPrice}}
                        </div>
                        <div v-else>
                            {{scope.row.minPrice}}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="库存">
                    <template slot-scope="scope">
                        <div>
                            {{scope.row.totalInventory}}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <div @click="DeletList(scope.row.spuId)" style="user-select: none;color:#ff6419;cursor:pointer;">
                            移除
                        </div>
                    </template>
                </el-table-column>

            </el-table>
        </el-main>
        <!-- 添加特卖 弹框 -->
        <el-dialog :visible.sync="addspesell" height="700px" width="990px" title="添加新品">
            <div class="diabox">
                <div class="header">
                    <span>
                        编号
                    </span>
                    <span>
                        商品缩略图
                    </span>
                    <span class="name">
                        名称
                    </span>
                    <span>
                        商品分类
                    </span>
                    <span>
                        原价
                    </span>
                    <span>
                        优惠价
                    </span>
                    <span>
                        库存
                    </span>
                    <span>
                        操作
                    </span>
                </div>
                <ul class="sellbox">
                    <div class="golistbox" v-if="isShowNone" v-loading="listloading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)" style="width: 100%" :default-sort="{prop: 'code', order: 'descending'}">
                        <div class="nonetextbox">
                            <h1 class="noneshop">暂无新品，请在商品管理页面设置预售商品</h1>
                            <p class="shoptext">设置方式: 在
                                <span class="golist" @click="$router.push('/goods')">商品管理列表</span> 中选择商品 → 编辑 → 勾选预售</p>
                        </div>
                    </div>
                    <li class="list" v-for="(item,index) in spesellList" :key="index" v-else>
                        <span>
                            <!--商品id-->
                            {{item.spuCode}}
                        </span>
                        <span>
                            <div class="imgdiv" v-if="!item.picPath">
                                无缩略图
                            </div>
                            <img width="60px" height="60px" :src="item.picPath" v-else>
                        </span>
                        <span class="name">
                            <!--商品名字-->
                            {{item.spuName}}
                        </span>
                        <span>
                            <!--商品分类-->
                            {{item.bigType}}>{{item.smallType}}
                        </span>
                        <span v-if="item.maxSalePrice==item.minSalePrice">
                            <!--原价-->

                            {{item.minSalePrice}}
                        </span>
                        <span v-else>
                            {{item.minSalePrice}}~{{item.maxSalePrice}}

                        </span>

                        <span v-if="item.maxPrice == item.minPrice ">
                            <!--优惠价-->
                            {{item.minPrice}}
                        </span>
                        <span v-else>
                            {{item.minPrice}}~ {{item.maxPrice}}
                        </span>

                        <span>
                            <!--库存-->
                            {{item.totalInventory}}
                        </span>
                        <span class="addbtn" @click="AddList(item.spuId,index)">添加</span>
                    </li>
                </ul>
            </div>
        </el-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
export default {
  data() {
    return {
      addspesell: false,
      loading: true,
      listloading: true,
      spesellList: [],
      isShowNone: true,
      spesell_mainList: []
    };
  },
  components: {},
  created() {
    this.SpesellmMainList();
  },
  methods: {
    AddList(id, index) {
      console.log(id, index);
      if (this.spesell_mainList.length >= 4) {
        this.$message({
          message: "主页显示不能超过四条!",
          type: "warning"
        });
        return;
      } else {
        this.API.presellupdatelist({
          spuId: id,
          state: 1
        }).then(res => {
          console.log(res);
          this.SpesellList();
          this.SpesellmMainList();
          this.$message({
            message: "添加成功!",
            type: "success"
          });
        });
      }
    },
    DeletList(id) {
      console.log(id);
      this.API.presellupdatelist({
        spuId: id,
        state: 0
      }).then(res => {
        console.log(res);
        this.$message({
          message: "移除成功!",
          type: "success"
        });
        this.SpesellmMainList();
      });
    },

    SpesellmMainList() {
      this.API.presellmainlist({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        this.spesell_mainList = res.list;
        console.log(res.list);
        this.loading = false;
        for (let i = 0; i < this.spesell_mainList.length; i++) {
          if (this.spesell_mainList[i].picPath) {
            this.spesell_mainList[i].picPath =
              process.env.sourceBaseUrl + this.spesell_mainList[i].picPath;
          }
        }
      });
    },

    SpesellList() {
      this.addspesell = true;
      this.API.preselllist({
        companyId: sessionStorage.getItem("companyID")
      }).then(res => {
        console.log(res.list);
        this.spesellList = res.list;
        if (this.spesellList[0]) {
          this.listloading = false;
          this.isShowNone = false;
        } else {
          this.listloading = false;
          this.isShowNone = true;
        }

        for (let i = 0; i < this.spesellList.length; i++) {
          if (this.spesellList[i].picPath) {
            this.spesellList[i].picPath =
              process.env.sourceBaseUrl + this.spesellList[i].picPath;
          }
        }
      });
    }
  }
};
</script>

<style scoped lang="scss">
.presell {
  .el-main {
    background: #ffffff;
    .btnbox {
      .addbtn {
        width: 100px;
        height: 32px;
        background: rgb(255, 240, 232);
        line-height: 32px;
        border-radius: 18px;
        text-align: center;
        color: #ff6419;
        border:1px solid rgb(255, 193, 163);
        display: inline-block;
        cursor: pointer;
        font-size: 12px;
      } 
       .addbtn:hover{
         background: #ff6419;
         color: #ffffff;
      }
    }
    .el-table {
      margin-top: 20px;
      .imgdiv {
        width: 90px;
        height: 90px;
        line-height: 90px;
        text-align: center;
        background: #f5f5f5;
        margin: 0 auto;
        font-size: 12px;
      }
    }
    .paging {
      margin-top: 20px;
      text-align: center;
    }
  }
  .diabox {
    position: relative;
    width: 900px;
    margin: 0 auto;
    &::after {
      content: "";
      position: absolute;
      bottom: 0px;
      left: 0;
      width: 100%;
      height: 1px;
      background: #ccc;
    }
    .header {
      margin: 0 auto;
      width: 900px;
      background: rgb(243, 243, 243);
      height: 40px;
      line-height: 40px;
      display: flex;
      text-align: center;
      border: 1px #ccc solid;
      border-bottom: none;
      font-weight: bold;
      > span {
        flex: 1;
        font-size: 14px;
        color: #909399;
      }
      .name {
        flex-basis: 40px;
      }
    }
    .sellbox {
      width: 900px;
      height: 630px;
      text-align: center;
      border: 1px #ccc solid;
      margin: 0 auto;
      overflow: scroll;
      overflow-x: hidden;
      border-bottom: none;
      position: relative;
      .list {
        min-height: 80px;
        text-align: center;
        width: 900px;
        display: flex;
        border-bottom: 1px #ccc solid;
        align-items: center;

        > span {
          flex: 1;
          .imgdiv {
            width: 60px;
            height: 60px;
            line-height: 60px;
            text-align: center;
            background: #f5f5f5;
            margin: 0 auto;
            font-size: 12px;
          }
          > img {
            vertical-align: middle;
          }
        }
        > .addbtn {
          color: #ff6419;
          cursor: pointer;
          user-select: none;
        }
        .name {
          flex-basis: 40px;
          line-height: normal;
          //  display: -webkit-box;
          //   -webkit-line-clamp: 3;
          //   -webkit-box-orient: vertical;
          //   overflow: hidden;
        }
      }
      .golistbox {
        line-height: normal;
        display: inline-block;
        margin-top: 266px;
        .nonetextbox {
          display: inline-block;
          text-align: left;
          .noneshop {
            font-size: 14px;
            color: black;
          }
          .shoptext {
            font-size: 14px;
            color: #666666;
            margin-top: 20px;
            .golist {
              color: #ff6419;
              cursor: pointer;
            }
          }
        }
      }
    }
  }
}
</style>

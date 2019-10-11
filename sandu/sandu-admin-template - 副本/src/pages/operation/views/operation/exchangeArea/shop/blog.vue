<template>
  <div class="blog">
    <v-nav :nav="nav"></v-nav>

    <div class="bolgcontent">
      <el-main>
        <div class="contentHeader clear">
          <h1 class="title">
            博文栏目
          </h1>
          <el-button type="success" class="addbtn" @click="OperationBlog({type:'新增'})" round>新增博文栏目</el-button>
        </div>

        <div class="contentList" v-if="blog_list[0]">
          <ul class="list" v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)" style="width: 100%" :default-sort="{prop: 'code', order: 'descending'}">
            <div class="box clear">
              <li v-for="(item,index) in blog_list" :key="index">
                <span class="img">
                  <img v-if="item.picPath" :src="BASE_URL.sourceBaseUrl+item.picPath" class="blogImg">
                  <img v-else class="indexblogImg" :src="require('../../../assets/images/idnexblogImg.png')">
                  <span :class="{'publish':true,'publish1':item.releaseStatus}"></span>
                </span>
                <div class="wrap">
                  <h2 class="subtitle">{{item.articleTitle}}</h2>
                  <p class="time">创建时间：{{item.createDate}}</p>
                  <div class="lastbox">
                    <span @click="OperationBlog({type:'详情',id:item.articleId})">详情</span>
                    <i class="line"></i>
                    <span @click="OperationBlog({type:'编辑',id:item.articleId})">编辑</span>
                    <i class="line"></i>
                    <span @click="OperationBlog({type:'删除',title:item.articleTitle,id:item.articleId})">删除</span>
                    <i class="line"></i>
                    <span v-if="!item.releaseStatus" @click="OperationBlog({type:'发布',title:item.articleTitle,id:item.articleId})">发布</span>
                    <span v-else @click="OperationBlog({type:'取消发布',title:item.articleTitle,id:item.articleId})">取消发布</span>
                  </div>
                </div>
              </li>
            </div>
          </ul>

          <div class="paging">
            <el-pagination :total="total" :current-page.sync="pageNum" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="[12, 24, 36, 48]" :page-size="12" layout="total, sizes, prev, pager, next, jumper">
            </el-pagination>
          </div>
        </div>
        <p v-else class="noneList"> 暂无数据</p>
      </el-main>

    </div>

    <!--删除博文弹框-->
    <el-dialog :visible.sync="deletDia" height="192px" width="540px" title="删除">
      <div>
        <p style="margin-left:30px">确认将该
          <span style="color:red;">{{diaName}}</span> 删除吗？</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="DeleteBlog('确定')">确 定</el-button>
        <el-button round @click="DeleteBlog('取消')">取 消</el-button>
      </div>
    </el-dialog>
    <!--发布博文弹框-->
    <el-dialog :visible.sync="publishDia" height="192px" width="540px" title="发布">
      <div>
        <p style="margin-left:30px">确认将该
          <span style="color:red;">{{diaName}}</span>发布到店铺吗？</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="PublishBlog({type:'确定',isPublish:1})">确 定</el-button>
        <el-button round @click="PublishBlog({type:'取消',isPublish:1})">取 消</el-button>
      </div>
    </el-dialog>
    <!--取消发布弹框-->
    <el-dialog :visible.sync="closepublishDia" height="192px" width="540px" title="取消发布">
      <div>
        <p style="margin-left:30px">确认将该
          <span style="color:red;">{{diaName}}</span>在店铺上取消发布吗？</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button round type="primary" @click="PublishBlog({type:'确定',isPublish:0})">确 定</el-button>
        <el-button round @click="PublishBlog({type:'取消',isPublish:0})">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  data() {
    return {
      limit: 12, //条数
      total: 0, //页数
      pageNum: 1,
      closepublishDia: false,
      articleId: "",
      loading: true,
      diaName: "",
      deletDia: false,
      publishDia: false,
      blog_list: [],
      nav: [
        { nav: "店铺管理", url: "/business/shop" },
        { nav: "博客栏目", url: "/business/blog" }
      ]
    };
  },
  components: {},
  created() {
    this.GetBlogList();
  },
  methods: {
    handleCurrentChange(val) {
      // console.log(val);
      this.pageNum = val;
      this.GetBlogList();
    },
    //改变每一页条数
    handleSizeChange(val) {
      this.limit = val;
      this.GetBlogList();
    },
    //删除博文接口
    DeleteBlog(val) {
      if (val == "确定") {
        this.API.deleteblog({
          articleId: this.articleId,
          shopType: this.$route.query.shopType
        }).then(res => {
          console.log(res);
          if (res.success) {
            this.$message({
              type: "success",
              message: "删除博文成功!"
            });
            this.GetBlogList();
            this.deletDia = false;
          } else {
            this.$message({
              type: "error",
              message: `删除失败:${res.message}!`
            });
          }
        });
      } else {
        this.deletDia = false;
      }
    },
    //发布博文接口
    PublishBlog(obj) {
      let isPublish = obj.isPublish;
      let val = obj.type;
      if (val == "确定") {
        this.API.publishblog({
          articleId: this.articleId,
          releaseStatus: isPublish,
          shopType: this.$route.query.shopType
        }).then(res => {
          console.log(res);
          if (isPublish) {
            this.$message({
              type: "success",
              message: "博文发布成功!"
            });
            this.publishDia = false;
          } else {
            this.$message({
              type: "success",
              message: "博文取消发布成功!"
            });
            this.closepublishDia = false;
          }
          this.GetBlogList();
        });
      } else {
        if (isPublish) {
          this.publishDia = false;
        } else {
          this.closepublishDia = false;
        }
      }
    },
    //博文首页列表接口
    GetBlogList() {
      this.API.getbloglist({
        limit: this.limit,
        page: this.pageNum,
        shopId: sessionStorage.getItem("shopId")
      }).then(res => {
        console.log(res.datalist);
        this.blog_list = res.datalist;
        this.loading = false;
        this.total = res.totalCount;
      });
    },

    //绑定到同一个事件  用传进来的type判断点击的是哪个文本事件
    OperationBlog(obj) {
      let type = obj.type;
      let title = obj.title;
      let id = obj.id;
      switch (type) {
        case "删除":
          this.deletDia = true;
          this.diaName = title;
          this.articleId = id;
          break;
        case "发布":
          this.publishDia = true;
          this.diaName = title;
          this.articleId = id;
          break;
        case "取消发布":
          this.closepublishDia = true;
          this.diaName = title;
          this.articleId = id;
          break;
        case "详情":
          this.$router.push({ path: `/business/blogOperation/${0}`,query:{shopType:this.$route.query.shopType} });
          sessionStorage.setItem("articleId", id);
          break;
        case "新增":
          this.$router.push({ path: `/business/blogOperation/${1}`,query:{shopType:this.$route.query.shopType} });
          break;
        case "编辑":
          this.$router.push({ path: `/business/blogOperation/${2}`,query:{shopType:this.$route.query.shopType} });
          sessionStorage.setItem("articleId", id);
          break;
        default:
          return;
          break;
      }
    }
  }
};
</script>

<style scoped lang="scss">
.el-main {
  background: #ffffff;
  position: relative;
  padding: 0 !important;
  .contentHeader {
    height: 38px;
    line-height: 38px;
    padding: 20px;
    border-bottom: 1px solid #ccc;
    .title {
      font-size: 20px;
      color: #666666;
      float: left;
    }
    .addbtn {
      background: #ff6419;
      border: none;
      float: right;
    }
  }
  .contentList {
    padding: 0;
    width: 100%;
    .list {
      .box {
        padding: 10px 0 10px 18px;
        min-height: 600px;
        li {
          width: 380px;
          height: 430px;
          background: #ffffff;
          float: left;
          margin: 10px;
          border: 1px solid #ccc;
          box-sizing: border-box;
          .img {
            width: 378px;
            height: 300px;
            display: inline-block;
            position: relative;
            .blogImg,.indexblogImg {
              width: 378px;
              height: 300px;
              object-fit: cover;
            }
            .publish {
              width: 18px;
              height: 55px;
              // background: url("../../../assets/images/icons/publish.png");
              background-size: 18px 56px;
              display: inline-block;
              position: absolute;
              top: 0;
              right: 20px;
              &.publish1 {
                // background: url("../../../assets/images/icons/publish1.png");
                background-size: 18px 56px;
              }
            }
          }
          .wrap {
            font-size: 14px;
            margin-left: 16px;
            .subtitle {
              color: #333333;
              font-size: 14px;
              margin: 10px 0;
            }
            .time {
              color: #999999;
            }
            .lastbox {
              margin-top: 30px;
              > span {
                color: #ff6419;
                margin-right: 14px;
                cursor: pointer;
              }
              .line {
                background: #dddddd;
                width: 1px;
                height: 10px;
                display: inline-block;
                margin-right: 14px;
              }
            }
          }
        }
      }
    }
  }
  .paging {
    text-align: center;
    padding: 30px 0 ;
  }
      .noneList {
      margin-top: 10px;
      font-size: 16px;
      text-align: center;
      height: 700px;
      line-height: 700px;
      color: #2c3e50;
      width: 100%;
      font-size: 30px;
    }
}
</style>

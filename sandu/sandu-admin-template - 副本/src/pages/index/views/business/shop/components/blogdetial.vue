<template>
  <div class="blogdetailpage">
    <v-nav :nav="nav"></v-nav>
    <div class="blogDetail">
      <el-main>
        <div class="contentHeader">
          <h1 class="title">详情</h1>
        </div>
        <el-form class="titleRow clear">
          <el-col class="starTitle" style="width:76px;" :span="8">
            <span class="star">标题：</span>
          </el-col>
          <el-col class="titleText" :span="16">{{detail_blog.title}}</el-col>
        </el-form>
        <el-form class="clear contentRow">
          <el-col class="contentTitle" :span="8">
            <span class="star">内容：</span>
          </el-col>
          <el-col class="contentText" :span="16">
            <span v-html="detail_blog.content"></span>
          </el-col>
        </el-form>
        <el-form class="titleRow clear">
          <el-col class="starTitle" style="width:76px;" :span="8">
            <span class="star">封面图：</span>
          </el-col>
          <el-col class="titleText" :span="16">
            <img :src="BASE_URL.sourceBaseUrl+detail_blog.coverPicPath">
          </el-col>
        </el-form>
      </el-main>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  data() {
    return {
      detail_blog: "",
      nav: [
        { nav: "店铺管理", url: "/business/shop" },
        {
          nav: "博文栏目",
          url: "/business/blog?shopType=" + this.$route.query.shopType
        },
        { nav: "详情", url: "/business/blogOperation" }
      ]
    };
  },
  created() {
    this.DetailBlog();
  },
  components: {},
  methods: {
    DetailBlog() {
      this.API.detailblog({
        articleId: sessionStorage.getItem("articleId")
      }).then(res => {
        console.log(res);
        this.detail_blog = res.obj;
      });
    }
  }
};
</script>

<style scoped lang="scss">
.blogdetailpage {
  .blogDetail {
    .el-main {
      background: #ffffff;
      position: relative;
      .contentHeader {
        font-size: 20px;
        &::after {
          content: "";
          width: 100%;
          height: 1px;
          background: #ccc;
          position: absolute;
          left: 0;
          top: 80px;
        }
      }
    }
    .titleRow {
      margin: 40px 0 0 80px;
      .starTitle {
        .star {
          position: relative;
          &::after {
            content: "*";
            color: red;
            position: absolute;
            top: 0;
            left: -6px;
          }
        }
      }
      .titleText {
        width: 90%;
        word-wrap: break-word;
      }
    }
    .contentRow {
      margin: 20px 0 0 80px;
      .contentTitle {
        width: 76px;
        .star {
          position: relative;
          &::after {
            content: "*";
            color: red;
            position: absolute;
            top: 0;
            left: -6px;
          }
        }
      }
      .contentText {
        width: 90%;
        word-wrap: break-word;
      }
    }
  }
}
</style>

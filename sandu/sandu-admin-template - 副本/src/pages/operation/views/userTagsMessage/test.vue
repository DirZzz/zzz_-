
<template>
  <div>
    <searchBar :searchs="searchs" :onSearch="getUserTagsList" :pageParams="pageData"></searchBar>
    <editBox :content="content"></editBox>
  </div>
</template>

<script>
import headers from "./../content/headers";
import searchBar from "&/components/common/seachBar";
import editBox from "&/components/common/editBox";
export default {
  components: {
    headers,
    searchBar,
    editBox
  },
  data() {
    return {
      searchs: [
        {
          fields: "labelName",
          label: "标签名称",
          placeholder: "请输入标签名称",
          type: "text"
        },
        {
          fields: "haha",
          type: "text"
        }
      ],
      pageData: { page: 1, limit: 10 },
      pageList: [],
      content: 1
    };
  },
  methods: {
    // 获取表格数据
    getUserTagsList() {
      this.loading = true;
      this.searchs.forEach(item => {
        this.pageData[item.fields] = item.data;
      });
      this.API.getUserTagsList(this.pageData).then(res => {
        if (res.success) {
          this.pageList = res.datalist.map((e, i) => {
            e.index = (this.pageData.page - 1) * this.pageData.limit + i + 1;
            return e;
          });
          this.pageData.total = res.totalCount;
        } else {
          this.pageList = [];
          this.pageData.total = 0;
        }
        this.loading = false;
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.t_row {
  margin: 20px 20px 0;
  padding: 20px 30px;
  background-color: #fff;
  text-align: left;
}
.t_join {
  margin-top: -20px;
}
</style>

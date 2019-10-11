<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/packagelist/list' }">套餐列表</el-breadcrumb-item>
            <el-breadcrumb-item>关联功能</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="mainBodys">
            <myTree :data2="data2" :servicesId="servicesId" @submitFun="update" ref="myTree" @delFun="update" @success="init"></myTree>
        </div>
    </page>
</template>

<script>
export default {
  name: "packagelist",
  data() {
    return {
      servicesId: "",
      data2: []
    };
  },
  created() {
      this.init();
  },
  methods: {
    init(){
        this.servicesId = this.$route.query.id;
        this.getFunctionList({ servicesId: this.servicesId }); //获取已有功能
    },
    getFunctionList(data) {
      this.API.getFunctionList(data).then(res => {
        this.data2 = res.obj;
      });
    },
    update(res){
        let data = { servicesId: this.servicesId , roleIds: res.roleIds, type: res.type};
        this.$refs.myTree.saveFunctionFun(res.type,this.API.saveFunction(data));
    },
  }
};
</script>

<style scoped lang="scss">
.mainBodys {
  width: 100%;
  padding: 20px 30px 0;
  position: relative;
}
</style>
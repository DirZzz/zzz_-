<template>
    <page>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: item.path }" v-for="(item,index) in userManageTabList" :key="index">{{item.name}}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="page-con">
            <myTree :data2="data2" @submitFun="update" ref="myTree" @delFun="update" @success="init"></myTree>
        </div>

    </page>
</template>

<script>
import { mapState, mapActions } from "Vuex";
export default {
  name: "setUser",
  data() {
    return {
        data2:'',
        roleIds:[],
    };
  },
  computed: {
    ...mapState("userManage", ["userManageTabList"])
  },
  created() {
    this.userManageTabList.push({
      path: "",
      name: "配置角色"
    });
    this.userManageTabList.map((res, index) => {
      if (index < this.userManageTabList.length - 1) {
        res.oldPath ? (res.path = res.oldPath) : "";
      }
    });
    this.setTabList(this.userManageTabList);
    this.init();
  },
  methods: {
    ...mapActions("userManage", ["setTabList"]),
    init(){
        this.API.getUserRole({userId:this.$route.query.userId}).then(res=>{
           this.data2 = res.obj;
           this.roleIds = [];
           res.obj.map(v=>{
               v.children.map(h=>{
                   this.roleIds.push(h.id+"");
               })
               this.roleIds = Array.from(new Set(this.roleIds));
           })
        })
    },
    update(res){
        let roleIds = '';
        console.log(this.roleIds);
        if(res.type){
            res.roleIds = res.roleIds.split(',');
            
            res.roleIds.forEach(item => {
                let n = this.roleIds.indexOf(item);
                console.log(n);
                n!=-1?this.roleIds.splice(n, 1):'';
            })
            roleIds = this.roleIds.join(',');
        }else{
            this.roleIds.map(v=>{ res.roleIds+=','+v; })
            roleIds = res.roleIds;
        }
        let data = { userId:parseInt(this.$route.query.userId) , roleIds: roleIds};
        this.$refs.myTree.saveFunctionFun(res.type,this.API.updateUserRole(data));
    },
  }
};
</script>

<style scoped lang="scss">
.page-con {
  margin-top: 20px;
}
</style>
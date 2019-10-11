<template>
    <el-row :gutter="20">
        <el-col :sm="24" :md="12">
            <p class="rowTitle">
                全部功能菜单<span class="btnSpan"><el-button type="primary" plain size="small" @click="submitFun">添加</el-button></span>
            </p>
            <el-tree
            ref="ref1"
            :data="data1"
            show-checkbox
            node-key="id"
            :props="defaultProps">
            </el-tree>
        </el-col>
        <el-col :sm="24" :md="12">
            <p class="rowTitle">
                已添功能菜单<span class="btnSpan"><el-button type="danger" plain size="small" @click="delFun">删除</el-button></span></p>
            <el-tree
            ref="ref2"
            :data="data2"
            show-checkbox
            node-key="id"
            :props="defaultProps">
            </el-tree>
        </el-col>
    </el-row>
</template>

<script>
/**time 2019-01-17
 * author YZF
 * 传参 servicesId,data2
 *      servicesId是获取全部功能菜单的参数,data2是已添加功能的数据列表由父组件调接口
 * 派发事件 submitFun,delFun,success
 *      submitFun点击添加事件
 *      delFun点击删除事件
 *      submitFun执行添加/删除成功后回调,具体业务在父组件执行,用于添加/删除成功后重新调已添加功能接口
 */
export default {
  name: "tree",
  props:['servicesId','data2'],
  data() {
    return {
        parentObj:[],
        checked:[false,false],
        checked1:false,
        checked2:false,
        data1: [],
        loading:'',
        defaultProps: { children: 'children', label: 'label'}
    };
  },
  created() {
      this.init();
  },
  methods: {
    init(){
        this.getFunctionList();//获取全部功能
    },
    getFunctionList(){
        this.API.getFunctionList().then((res)=>{
            this.data1 = res.obj;
        })
    },
    loadingFunc(){
        this.loading = this.$loading({lock: true, text: 'Loading', spinner: 'el-icon-loading', customClass:'Loadings', background: 'rgba(250, 250, 250, 0.7)'});
    },
    submitFun(){
        let roleList = this.$refs.ref1.getCheckedNodes(true);
        let roleIds = '';
        if(roleList.length<=0){
            this.$message.warning('请至少选择一个选项！');
            return;
        }
        roleList.map((v,i)=>{ roleIds+=i>=(roleList.length-1)?v.id:(v.id+','); })
        this.$emit('submitFun',{roleIds:roleIds,type:0});
    },
    delFun(){
        let roleList = this.$refs.ref2.getCheckedNodes(true);
        let roleIds = '';
        if(roleList.length<=0){
            this.$message.warning('请至少选择一个选项！');
            return;
        }
        roleList.map((v,i)=>{ roleIds+=i>=(roleList.length-1)?v.id:(v.id+','); })
        this.$emit('delFun',{roleIds:roleIds,type:1});
    },
    saveFunctionFun(type,api){
        this.loadingFunc();
        api.then((res)=>{
            this.loading.close();
            if(res.success){
                this.checked[type] = false;
                type?this.$refs.ref2.setCheckedKeys([]):this.$refs.ref1.setCheckedKeys([])
                this.$message.success((type?'删除':'添加')+'成功');
                this.init();
                this.$emit('success');
            }else{
                this.$message.error(res.message);
            }
            
        })
    },
    checked1Fun(){
        this.changeState(this.checked[0],this.$refs.ref1,this.data1);
    },
    checked2Fun(){
        this.changeState(this.checked[1],this.$refs.ref2,this.data2);
    },
    changeState(flag,doc,data){
        flag?doc.setCheckedNodes(data):doc.setCheckedKeys([]);
    }
    
  } 
};
</script>

<style scoped lang="scss">
.rowTitle{
    background: #F2F6FC;
    color: #333;
    font-size: 14px;
    height: 52px;
    line-height: 52px;
    position: relative;
    margin-bottom: 10px;
    text-align: center;
    span{
        position: absolute;
        height: 32px;
        top: 10px;
        line-height: 32px;
    }
    .btnSpan{
        right: 10px;
    }
    .checkbox{
        left: 10px;
    }
}

</style>
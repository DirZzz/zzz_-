<template>
    <div class="produceRangeBox">
        <input ref='test' type="text" :readonly="true" class="produceRange" v-model="title" @click="dialogRange" placeholder="请选择企业可见产品范围">
        <el-dialog
                :visible.sync="produceRange"
                width="620px"
                class="produceRangeDio"
                title="企业可见产品范围"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :before-close="handleClose">
            <div class="produceRange-content" v-if="produceRangeData">
                <el-tree
                        :data="produceRangeData"
                        show-checkbox
                        :default-expanded-keys="oldCategoryIds"
                        :default-checked-keys="oldCategoryIds"
                        node-key="id"
                        ref="treeCateg"
                        highlight-current>
                </el-tree>
            </div>
            <div class="btn">
                <el-button type="primary" size="medium" round @click="confirm">确定</el-button>
                <el-button size="medium" round @click="produceRange=false">取消</el-button>
            </div>
        </el-dialog>
    </div>

</template>

<script>
    import { mapState,mapActions} from 'Vuex'
    export default {
        name: "produceRange",
        props:['title'],
        data(){
            return{
                produceRange:false
            }
        },
        computed:{
            ...mapState('userManage',['produceRangeData','oldCategoryIds','flag']),

        },
        created(){
            this.init();
        },
        mounted(){
        },
        methods:{
            ...mapActions('userManage',['setProduceRange','setOldCategoryIds','setFlag']),
            addObj(item){
                item.id=item.categoryId;
                item.label=item.categoryName;
                item.children=item.list;
                return item
            },
            init(){
                this.API.mfrsCategory({companyId:sessionStorage.getItem('companyID')}).then(res=>{
                if(res.success){
                    res.datalist.map(item=>{
                        this.addObj(item);
                        item.list.map(item1=>{
                            this.addObj(item1);
                            item1.list.map(item2=>{
                                this.addObj(item2);
                            })
                        })
                    });
                    this.setProduceRange(res.datalist);
                }
            })
            },
            handleClose(){
                this.produceRange=false
            },
            dialogRange(){
                this.produceRange=true;
            },
            reset(){
                this.setOldCategoryIds([]);
            },
            echoData(arr){
               this.setOldCategoryIds(arr);
            },
            setCheckedKeysFun(arr){
                console.log(arr);
                this.$refs.treeCateg.setCheckedKeys(arr);
            },
            confirm(){
                this.setOldCategoryIds(this.$refs.treeCateg.getCheckedKeys());
                this.API.industryRange({newIndustryValues:'',oldCategoryIds: this.oldCategoryIds.join(',')}).then(res=>{
                    if(res.success){
                        this.$emit('change',{range:res.obj});
                        this.produceRange=false;
                        this.setFlag(true)
                    }
                });
            }
        },
    }
</script>

<style scoped lang="scss">
  .produceRange{
      width: 354px;
      height: 40px;
      border-radius: 4px;
      border: 1px solid #dcdfe6;
      text-indent: 16px;
      color: #999;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      cursor: pointer;
  }
  .produceRange-content{
      max-height:500px;
      overflow-y: scroll;
  }
    .btn{
        text-align: center;
    }
</style>
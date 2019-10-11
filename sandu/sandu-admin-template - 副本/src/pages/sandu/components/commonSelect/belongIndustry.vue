<template>
    <div class="industry">
        <el-select v-model="industry" placeholder="请输入所属行业" clearable filterable multiple :class="{'industry-select': isMore}"
                   ref="select"
                   allow-create
                   default-first-option @change="industryChange" @remove-tag="removeChange">
            <el-option
                    v-for="item in options"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
            </el-option>
        </el-select>
    </div>

</template>

<script>
    import { mapState,mapActions} from 'Vuex'
    export default {
        name: "belongIndustry",
        props:['linkType','pid'],//linkType关联类型  默认不关联  关联的是产品可见范围
        data(){
            return{
                industry:'',
                options:[],
                isMore:false
            }
        },
        computed:{
            ...mapState('userManage',['oldCategoryIds','flag','treeTagName'])
        },
        created(){
            if(this.pid){
                this.API.industryType2({pid:this.pid}).then(res=>{
                    if(res.success)  this.options=res.obj;
                })
            }else{
                this.API.industryType({type:'industry'}).then(res=>{
                    if(res.code==200)this.options=res.data;
                })
            }
        },
        methods:{
            ...mapActions('userManage',['setOldCategoryIds']),
            industryChange(val) {
                this.overflowEvent();
               //所属行业关联企业产品范围
                if(this.linkType==true){ // 选择了所属行业
                  this.industryEvent();
                }else {
                    this.$emit('change',{industry:this.industry});
                }
            },
            removeChange(val){
                this.API.industryRange({newIndustryValues:val,oldCategoryIds: ''}).then(res=>{
                    if(res.success){
                        let newArr = this.oldCategoryIds.filter(item=>!res.obj.categoryIds.some(ele=>ele===item));
                        this.setOldCategoryIds(newArr);
                        
                        this.industryEvent();
                    }
                })
            },
            industryEvent(){
                this.API.industryRange({newIndustryValues:this.industry.join(','),oldCategoryIds: this.flag ? this.oldCategoryIds.join(',') : ''}).then(res=>{
                    if(res.success){
                        this.$emit('change',{industry:this.industry,range:res.obj});
                        this.setOldCategoryIds(res.obj.categoryIds);
                    }
                })
            },
            reset(){
                this.industry=[]
                this.isMore = false;
            },
            echoData(arr){
                this.industry=arr.map(res=>parseInt(res));
            },
            
            overflowEvent(){
                this.$nextTick(function () {  //判断长度
                    let [$span, tags, Len] = [this.$refs.select.$refs.tags.children[0].childNodes, [], 0];
                    for (let i = 0, len = $span.length; i < len; i++) {
                        if ($span[i].className.indexOf('leave') > -1) {
                            tags.slice(i, 1);
                        } else {
                            tags.push($span[i]);
                        }
                    }
                    for (let i = 0, len = tags.length; i < len; i++) {
                        Len += tags[i].offsetWidth;
                    }
                    this.isMore = Len > 144 ? true : false;
                });
            }
        }
    }
</script>

<style scoped lang="scss">
    .industry-select{
        position: relative;
        &:after {
            display: inline-block;
            height: 40px;
            line-height: 40px;
            content: '...';
            position: absolute;
            right:20px;
            top: 0;
            z-index: 2;
        }
    }
</style>
<style lang="scss">
    .industry{
        .el-select__tags {
            overflow: hidden;
            white-space: nowrap;
        }
    }
</style>
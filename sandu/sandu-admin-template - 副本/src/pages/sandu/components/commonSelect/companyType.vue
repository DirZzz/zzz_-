<template>
    <el-select v-model="companyType" placeholder="请选择公司类型" clearable filterable @change="handChange">
        <el-option
                v-for="item in options"
                :key="item.id"
                :label="item.name"
                :value="item.id">
        </el-option>
    </el-select>
</template>

<script>
    export default {
        name: "companyType",
        props:['type'],
        data(){
            return{
                companyType:'',
                options:[]
            }
        },
        created(){
            this.API.brandBusinessType().then(res=>{
                if(res.code==200) this.options=res.data;
                this.type==1 ? this.options=this.options.filter(res=>{ return res.id!=1 && res.id!=2}) : '';//过滤掉厂商与经销商
            })
        },
        methods:{   //子组件提供的方法
            handChange(val){
                this.$emit('companyType',val)
            },
            valChange(val){
                this.companyType=val;
            },
            echoData(val){
                this.companyType=val;
            },
            reset(){
                this.companyType=''
            }
        }
    }
</script>

<style scoped>

</style>
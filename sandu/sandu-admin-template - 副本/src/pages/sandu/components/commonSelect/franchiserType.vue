<template>
    <el-select v-model="franchiserId" placeholder="请选择所属的经销商企业" clearable filterable @change="franchiserTypeChange">
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
        name: "franchiserType",
        data(){
            return{
                franchiserId:'',
                options:[]
            }
        },
        created(){
            this.API.getFranchiserCompany({companyId:this.$route.query.id||this.$route.query.companyId}).then(res=>{
                if(res.success){
                    this.options=res.obj;
                }else {
                    this.$message.error(res.message);
                }
            })
        },
        methods:{
            franchiserTypeChange(val){
                this.$emit('change',val);
            },
            reset(){
                this.franchiserId='';
            },
            echoData(franchiserId){ //回显方法
                this.franchiserId = franchiserId||'';
            },
        }
    }
</script>

<style scoped>

</style>
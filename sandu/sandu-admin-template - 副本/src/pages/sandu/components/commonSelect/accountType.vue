<template>
    <el-select v-model="accountType" placeholder="请选择账号类型" clearable filterable @change="accountChange">
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
        name: "accountType",
        data(){
            return{
                accountType:'',
                options:[{
                    id:1,
                    name:'正式'
                },{
                    id:0,
                    name:'试用'
                }]
            }
        },
        methods:{
            accountChange(val){
                this.$emit('change',val)
            },
            reset(){
                this.accountType='';
            }
        }
    }
</script>

<style scoped>

</style>
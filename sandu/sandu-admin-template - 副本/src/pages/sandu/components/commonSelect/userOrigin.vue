<template>
    <el-select v-model="userOrigin" placeholder="请选择用户来源" clearable filterable @change="originChange">
        <el-option
                v-for="item in options"
                :key="item.id"
                :label="item.name"
                :value="item.value">
        </el-option>
    </el-select>
</template>

<script>
    export default {
        name: "userOrigin",
        data(){
            return{
                userOrigin:'',
                options:[]
            }
        },
        created(){
            this.API.getUserResource().then(res=>{
                  if(res.success){
                      this.options=res.obj;
                  }else {
                      this.$message.error(res.message)
                  }
            })
        },
        methods:{
            originChange(val){
                this.$emit('change',val);
            },
            echoData(val){
                this.userOrigin=val ? val : '';
            }
        }
    }
</script>

<style scoped>

</style>
<template>
    <el-select v-model="userType" placeholder="请选择用户类型" clearable filterable @change="accountChange">
        <el-option
                v-for="item in userTypelist"
                :key="item.value"
                :label="item.name"
                :value="item.value">
        </el-option>
    </el-select>
</template>

<script>
    import minixs from '@s/minixs/index'
    export default {
        name: "userType",
        mixins:[minixs],
        props:['type'],//type='add'  为新增 或批量新增
        data(){
            return{
                options:[],
                userType:''
            }
        },
        watch:{
            userMethod(val){
                if(this.userMethod=='franchiser'){
                    this.$route.query.businessType=2;
                    this.setUserType({vm:this,companyId:this.$route.query.id?this.$route.query.id:this.$route.query.companyId,companyType:2});
                }else if(this.userMethod=='vendor'){
                    this.$route.query.businessType=1;
                    this.setUserType({vm:this,companyId:this.$route.query.id?this.$route.query.id:this.$route.query.companyId,companyType:1});
                }
            }
        },
        created(){
            let flag = this.userMethod!='franchiser'&&this.userMethod!='vendor';
            let companyId = this.$route.query.id?this.$route.query.id:this.$route.query.companyId;
            if(this.type=='add'&&flag){
                let data = {vm:this,companyId:companyId}
                this.$route.query.userType==3?data.companyType=2:'';
                this.setUserType(data);
            }else if(this.type=='reset'&&flag){
                
            }else if(this.userMethod=='franchiser'||this.userMethod=='vendor'){
                this.setUserType({vm:this,companyId:companyId,companyType:this.userMethod=='franchiser'?2:1});
            }else{
                this.API.industryType({type:'userType'}).then(res=>{
                    if(res.code==200){
                        res.data.map(item=>{
                            item.value=item.id;
                        })
                        this.setUserType(res.data);
                    };
                })
            }
            
        },
        methods:{
            accountChange(){
                this.$emit('change',this.userType);
            },
            reset(){
                this.userType='';
            },
            echoData(userType){ //回显方法
                this.userType = userType;
            },
    }
    }
</script>

<style scoped>

</style>
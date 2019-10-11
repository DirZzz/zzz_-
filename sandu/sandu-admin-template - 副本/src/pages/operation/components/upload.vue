<template>
    <el-upload
            :action="BASE_URL.uploadUrl"
            :headers="headerToken"
            :data="uploadParams"
            :show-file-list="false"
            :on-success="handleAvatarSuccess">
            <slot></slot>
    </el-upload>
</template>

<script>
    import qs from 'qs'
    export default {
        name: "upload",
        data(){
            return{
                headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
                uploadParams: {
                    fileKey:'admin.home.manager',
                    platform: 'web',
                    module: 'none',
                    type: 'image'
                },
            }
        },
        methods:{
            handleAvatarSuccess(res){
               this.$emit("success",res)
            }
        }
    }
</script>

<style scoped>

</style>
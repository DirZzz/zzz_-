<template>
    <el-upload
            :action="BASE_URL.uploadUrl"
            :headers="headerToken"
            :data="uploadParams"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
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
                    // fileKey:'admin.home.manager',
                    platform: 'web',
                    module: 'product',
                    type: 'image'
                },
            }
        },
        methods:{
            beforeAvatarUpload(file){
                if(file.type.indexOf('image/')>-1){
                   return true
                }else {
                    this.$message.error('请上传图片格式');
                    return false
                }
            },
            handleAvatarSuccess(res){
               this.$emit("success",res)
            }
        }
    }
</script>

<style scoped>

</style>
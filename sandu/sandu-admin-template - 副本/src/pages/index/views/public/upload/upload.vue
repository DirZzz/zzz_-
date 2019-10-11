<template>
  <el-form-item label="贴图图片:" prop="region" class="uploaderWraper">
    <el-upload
      class="avatar-uploader"
      action="https://jsonplaceholder.typicode.com/posts/"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload">
      <img v-if="imageUrl" :src="imageUrl" class="avatar">
      <i v-else class="el-icon-plus; avatar-uploader-icon"></i>
    </el-upload>
    <span class="text">上传图片</span>
  </el-form-item>
</template>

<script>
  export default {
    data () {
      return {
        imageUrl: ''
      };
    },
    methods: {
      handleAvatarSuccess (res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
      },
      beforeAvatarUpload (file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      }
    }
  };
</script>

<style lang="scss" scoped>
  @import "../../../assets/css/mixin.scss";

  .uploaderWraper {
    .el-form-item__content {
      max-width: 760px;
      height: 90px;
      line-height: 0;
      padding: 20px;
      border-radius: 4px;
      border: solid 1px #dddddd;
      background: #f6f6f6;
      .avatar-uploader .el-upload {
        width: 90px;
        height: 90px;
        line-height: 90px;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        background: #fff;
        cursor: pointer;
        position: relative;
        overflow: hidden;
      }
      .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
      }
      .avatar-uploader-icon {
        font-size: 28px;
        color: #fff;
        background: $font-color-orange;
        border-radius: 20px;
        text-align: center;
      }
      .avatar {
        width: 90px;
        height: 90px;
        display: block;
      }
    }
    .text{
      position: absolute;
      bottom: 40px;
      left: 36px;
      color: #666;
    }
  }
</style>

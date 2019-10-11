<template>
  <div class="uploadBox">
    <ul class="uploadPic">
      <li>
        <el-upload
          class="uploader-img"
          :action="BASE_URL.uploadUrl"
          :show-file-list="false"
          :headers="headerToken"
          :on-success="onSuccess"
          :delete-image="showDelete"
          :before-upload="beforeAvatarUpload"
          :data="uploadParams"
        >
          <!--<i v-else class="el-icon-plus image-uploader-icon"></i>
          <div class="uploader-icon" v-show="addShowone">
            <div class="avatar-uploader-icon">
              <div class="across"></div>
              <div class="vertical"></div>
            </div>
            <p class="img-text" style="padding: 0">上传图片</p>
          </div>-->
          <div class="uploader-icon">
            <div class="avatar-uploader-icon">
              <div class="across"></div>
              <div class="vertical"></div>
            </div>
            <p class="img-text" style="padding: 0">上传图片</p>
          </div>
        </el-upload>
      </li>
      <li class="uploadImgList" v-if="imageUrl">
        <span class="posDelete" @click="deleteUploadImg"></span>
        <img v-if="imageUrl" :src="imageUrl" width="90" class="image">
      </li>
    </ul>
  </div>
</template>

<script>
  import qs from 'qs';
  export default {
    name: 'SingleImageUpload',
    props: {
      modelName:String,
      imageUrl: String,
      onSuccess: Function,
      addShowone: Boolean,
      addShowtwo: Boolean
    },
    data () {
      return {
        headerToken:{Authorization:qs.parse(sessionStorage.getItem('loginUser')).token},
        showDeletes: false,
        uploadParams: {
          platform: 'web',
          module: this.modelName||'product',
          type: 'image'
        }
      }
    },
    methods: {
      deleteUploadImg () {
        this.$emit('deleteClick')
      },
      showDelete () {
        this.showDeletes = true
      },
      showDelete2 () {
        this.showDeletes = false
      },
      beforeAvatarUpload (file) {
        const isJPG = file.type === 'image/jpeg'
        const isPNG = file.type === 'image/png'
        const isLt2M = file.size / 1024 / 1024 < 10

        if (!isJPG && !isPNG) {
          this.$message.error('上传图片只能是 JPG 或 PNG 格式!')
        }
        if (!isLt2M) {
          this.$message.error('图片大小不能超过 10MB!')
        }
        return (isJPG || isPNG) && isLt2M
      }
    }
  }
</script>
<style scoped lang="scss">
  .uploadImgList {
    width: 90px;
    text-align: center;
    margin: 5px 0 5px 20px;
    position: relative;
  }
  .posDelete {
    position: absolute;
    background: url('../../../assets/images/icons/delete3.png') 0 center no-repeat;
    width: 17px;
    height: 16px;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    line-height: 40px;
    color: #fff;
    font-size: 20px;
    border-radius: 6px;
    text-align: center;
    display: none;
    cursor: pointer;
  }
  .uploadPic {
    margin-right: 25px;
    background: #eee;
    padding-top: 15px;
    width: 720px;
    box-sizing: padding-box;
    li {
      font-size: 12px;
      margin: 5px;
      height: 90px;
      width: 90px;
      overflow: hidden;
      display: inline-block;
      &:hover{
        .posDelete{
          display: inline-block!important;
        }
      }

    }
  }
  .uploader-icon{
    display: inline-block;
    width: 90px;
    height: 90px;
    background-color: #ffffff;
    border: 1px dashed #c0ccda;
    border-radius: 5px;
    box-sizing: border-box;
  }
  .image {
    vertical-align: top;
    left: 20px;
    width: 90px;
    height: 90px;
    border-radius: 6px;
    display: inline-block;
  }
</style>
<style lang="scss">

  .uploadBox{
    .image-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }

    .avatar-uploader-icon {
      position: relative;
      background-color: #FF6419;
      width: 20px;
      height: 20px;
      margin: 20px auto 0;
      border-radius: 50%;
    }
    .across{
      position: absolute;
      top: 9px;
      left: 2px;
      background-color: #fff;
      width: 16px;
      height: 2px;
    }
    .vertical{
      position: absolute;
      top: 2px;
      left: 9px;
      background-color: #fff;
      width: 2px;
      height: 16px;
    }
    .uploader-img{
      position: relative;
      width: 705px;
      height: 130px;
      background: #eee;
      box-sizing: padding-box;
    }
    .image-uploader .el-upload:hover {
      border-color: #409EFF;
    }

    .image-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;
    }
  }


</style>


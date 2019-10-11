<!--图片预览组件-->
<template>
 <div class="Img-preview">
   <el-dialog 
    :visible.sync="ImgShow"
    class="ImgView"
    :show-close="false"
    width="1080px"
    height="610px"
   >
       <img class="img" width="1080px;" height="610px" :src="ImgURL">
   </el-dialog>
 </div>
</template>

<script type="text/ecmascript-6">
export default {
 data() {
 return {
 ImgShow:false,
 ImgURL:''
 }
 },
 methods: {
    sonFun(params){
     // console.log(params.ImgURL);
      this.ImgURL = params.ImgURL;
      this.ImgShow = params.ImgShow;
    }
 }
}
</script>

<style scoped lang="scss">

.ImgView{
 .img{
      margin: 0 auto;
      display: block;
      background: #fff;
      object-fit: contain;
      position: absolute;
      top: 0;
      user-select: none;
}
}

</style>

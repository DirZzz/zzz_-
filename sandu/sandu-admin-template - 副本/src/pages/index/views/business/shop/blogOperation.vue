<template>
    <div class="blogOperation">
       <blog-detail v-if="type==0"></blog-detail>
       <add-blog v-else-if="type==1"></add-blog>
       <edit-blog v-else-if="type==2"></edit-blog>
    </div>
</template>

<script type="text/ecmascript-6">
import BlogDetail from './components/blogdetial';
import AddBlog from './components/addblog';
import EditBlog from './components/editblog';
export default {
  data() {
    return {
   }
  },
  computed: {
     type(){
         return this.$route.params.type;
     }  
  },
  components: {
      BlogDetail,
      AddBlog,
      EditBlog
  },
  methods: {
  }
};
</script>

<style scoped lang="scss">
.blogOperation {

}
</style>

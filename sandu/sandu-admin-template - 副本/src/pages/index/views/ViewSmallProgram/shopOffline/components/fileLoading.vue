<template>
    <div class="fileLoading">
        <el-dialog width="540px"
                   top="30%"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :before-close="beforeClose"
                   :show-close="false"
                   title="正在导入中...."
                   :visible.sync="gettersFileLoading">
                <div class="file-content">
                    <p>{{gettersTexttureName}}</p>
                    <el-progress :percentage="gettersPercentage" color="#ff6419"></el-progress>
                </div>

        </el-dialog>
    </div>
</template>

<script>
    import {mapGetters} from 'Vuex'
    export default {
        name: "fileLoading",
        data(){
            return{

            }
        },
        computed:{
            ...mapGetters(['gettersFileLoading','gettersPercentage','gettersTexttureName'])
        },
        methods:{
            beforeClose(){
                this.$store.dispatch('fileLoadingDialog',false);
            }
        }
    }
</script>

<style scoped>

</style>
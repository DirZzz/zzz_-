<template>
    <el-dialog width="540px"
               top="20%"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :before-close="beforeClose"
               title="产品规格信息"
               :visible.sync="gettersLookHardProductDetail">
        <div class="setProductInfo">
            <div class="box">编号： {{gettersLookProductInfo.modelCode}}</div>
            <div class="box" v-for="(item,key) in gettersLookProductInfo.textureList" :key="key">
                <div>
                    <span>{{item.parentName}}：</span>
                    <em v-for="(res,index) in item.childName" :key="index">{{res}}</em>
                </div>
            </div>
            <div class="btn">
                <input type="button" value="确定" class="confirm" @click="beforeClose">
                <input type="button" value="取消" class="cancel" @click="beforeClose">
            </div>
        </div>
    </el-dialog>
</template>
<script>
    import {mapGetters} from 'Vuex'
    export default {
        name: "lookProductInfo",
        // props:['productPorps'],
        data(){
            return{
                lookProductInfo:true
            }
        },
        computed:{
            ...mapGetters(['gettersLookHardProductDetail','gettersLookProductInfo','gettersProductPorps'])
        },
        methods:{
            beforeClose(){
                this.$store.dispatch('dialogMdDetail',false);
                this.lookProductInfo=false;
            },
        }
    }
</script>

<style lang="scss" scoped>
    .setProductInfo{
        em{
            margin-right: 20px;
            font-style: normal;
        }
        .btn{
            border-top: 1px solid #ddd;
            padding-top: 20px;
            padding-bottom: 20px;
            position: relative;
            input{
                width: 60px;
                height: 30px;
                background-color: #ff6419;
                border-radius: 15px;
                color: #fff;
                font-size: 14px;
                cursor: pointer;
            }
            .confirm{
                position: absolute;
                right: 80px;
            }
            .cancel{
                background-color: #ffdbc9;
                color: #ff6419;
                position: absolute;
                right: 0;
            }
        }
    }

</style>
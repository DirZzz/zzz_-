<template>
    <transition name="zoomIn">
        <div class="setDialog">
            <div class="setDialog-header">首页弹窗设置<span>（*必填项）</span></div>
            <div class="setDialog-content">
                <div class="setDialog-isShow">
                    <span><em>*</em>是否显示：</span>
                    <el-radio-group v-model="setDialogData.isShowHome">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </div>
                <div class="setDialog-title">
                    <span><em>*</em>标题：</span>
                    <input type="text" maxlength="8" v-model="setDialogData.title" class="setDialog-input">
                    <div class="setDialog-tip">不超过8个字</div>
                </div>
                <div class="setDialog-title">
                    <span><em>*</em>副标题：</span>
                    <input type="text" maxlength="16" v-model="setDialogData.secondTitle" class="setDialog-input">
                    <div class="setDialog-tip">不超过16个字</div>
                </div>
            </div>
            <div class="bottom">
                <div class="btn" @click="save">保存</div><span>(新增、修改、删除操作后，请及时保存。)</span>
            </div>
        </div>
    </transition>
</template>

<script>
    import { mapState,mapActions} from 'Vuex'
    import { guid } from '@/utils/validate'
    export default {
        name: "setDialog",
        data(){
            return{
                title:this.moduleTitle
            }
        },
        created(){

        },
        computed:{
            ...mapState('homeManage',['setDialogData'])
        },
        methods:{
            ...mapActions('homeManage',['setSave']),
            save(){
                if(this.setDialogData.title==null||this.setDialogData.title==''){ this.$message.error('请输入标题');return };
                if(this.setDialogData.secondTitle==null||this.setDialogData.secondTitle==''){ this.$message.error('请输入副标题');return };
                this.setSave({vm:this})
            }
        }
    }
</script>

<style scoped lang="scss">
    .setDialog{
        width: 540px;
        background-color: #fff;
        margin-top: 142px;
        margin-left: 30px;
        .setDialog-header{
            width: 100%;
            height: 40px;
            line-height: 40px;
            background-color: #fafafa;
            text-indent: 21px;
            color: #000000;
            font-size: 14px;
            span{
                color: #ff2323;
            }
        }
        .setDialog-content{
            .setDialog-title{
                height: 36px;
                line-height: 36px;
                margin-bottom: 20px;
                span{
                    display: inline-block;
                    width: 140px;
                    text-align: right;
                }
                .setDialog-tip{
                    display: inline-block;
                    font-size: 14px;
                    color: #999999;
                }
                em{
                    color: #ff2323;
                    margin-right: 5px;
                }
            }
            .setDialog-input{
                width: 240px;
                height: 36px;
                background-color: #fafafa;
                border-radius: 2px;
                border: solid 1px #dddddd;
                text-indent: 16px;
            }
            .setDialog-isShow{
                margin-top: 20px;
                //margin-left: 30px;
                span{
                    display: inline-block;
                    width: 140px;
                    text-align: right;
                    line-height: 48px;
                    em{
                        color: #ff2323;
                        margin-right: 5px;
                    }
                }
            }

        }
        .bottom{
            height: 56px;
            border-top: 1px solid #dddddd;
            display: flex;
            span{
                color: #ff6449;
                margin-top: 18px;
                margin-left: 10px;
            }
            .btn{
                width: 60px;
                height: 30px;
                background-color: #ff6419;
                border-radius: 15px;
                color: #ffffff;
                font-size: 14px;
                line-height: 30px;
                text-align: center;
                margin-left: 40px;
                margin-top: 14px;
                cursor: pointer;
                &:active{
                    opacity: .6;
                }
            }
        }
    }
</style>
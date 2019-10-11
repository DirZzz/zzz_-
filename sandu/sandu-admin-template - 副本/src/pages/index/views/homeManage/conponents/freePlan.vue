<template>
    <transition name="zoomIn">
        <div class="recommendPlan">
            <div class="recommend-header">免费获取方案<span>（*必填项）</span></div>
            <div class="recommend-content">
                <div class="recommend-title">
                    <span><em>*</em>标题：</span>
                    <input type="text" maxlength="6" v-model="freePlanData.title" class="recommend-input">
                    <div class="recommend-tip">不超过6个字，4个字为最佳展示效果</div>
                </div>
                <div class="recommend-num">
                    <span>每日名额设置：</span>
                    <input type="number" v-model="freePlanData.amount" class="recommend-input">
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
        name: "freePlan",
        data(){
            return{
                title:this.moduleTitle
            }
        },
        created(){

        },
        computed:{
            ...mapState('homeManage',['freePlanData'])
        },
        methods:{
            ...mapActions('homeManage',['setSave']),
            save(){
                if(this.freePlanData.title==null||this.freePlanData.title==''){ this.$message.error('请输入标题');return };
                this.freePlanData.uuid ? '' : this.freePlanData.uuid=guid();
                this.setSave({vm:this})
            }
        }
    }
</script>

<style scoped lang="scss">
    .recommendPlan{
        width: 540px;
        background-color: #fff;
        margin-top: 142px;
        margin-left: 30px;
        .recommend-header{
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
        .recommend-content{
            .recommend-title{
                height: 36px;
                line-height: 36px;
                margin-top: 34px;
                span{
                    display: inline-block;
                    width: 120px;
                    text-align: right;
                }
                .recommend-tip{
                    display: inline-block;
                    font-size: 14px;
                    color: #999999;
                }
                em{
                    color: #ff2323;
                    margin-right: 5px;
                }
            }
            .recommend-num{
                height: 36px;
                line-height: 36px;
                margin-top: 20px;
                padding-bottom: 20px;
                span{
                    display: inline-block;
                    width: 120px;
                    text-align: right;
                }
            }
            .recommend-input{
                width: 180px;
                height: 36px;
                background-color: #fafafa;
                border-radius: 2px;
                border: solid 1px #dddddd;
                text-indent: 16px;
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
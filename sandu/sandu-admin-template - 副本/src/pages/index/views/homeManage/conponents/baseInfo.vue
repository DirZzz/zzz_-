<template>
    <transition name="zoomIn">
        <div class="baseInfo">
            <div class="baseInfo-header">企业基本信息<span>（*必填项）</span></div>
            <div class="baseInfo-content">
                <div class="baseInfo-isShow">
                    <span><em>*</em>是否显示：</span>
                    <el-radio-group v-model="baseInfoData.isShowHome">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </div>
                <div class="baseInfo-title">
                    <span><em>*</em>标题：</span>
                    <input type="text" maxlength="8" v-model="baseInfoData.title" class="baseInfo-input">
                    <div class="baseInfo-tip">不超过8个字,4个字为最佳显示效果</div>
                </div>
                <div class="baseInfo-title">
                    <span><em>*</em>企业名称：</span>
                    <input type="text" maxlength="20" v-model="baseInfoData.secondTitle" class="baseInfo-input">
                    <div class="baseInfo-tip">最多20个字</div>
                </div>
                <div class="baseInfo-title">
                    <span><em>*</em>联系电话：</span>
                    <input type="text" maxlength="13" v-model="baseInfoData.mobile" class="baseInfo-input">
                    <div class="baseInfo-tip">支持手机号、座机、400等电话格式</div>
                </div>
                <div class="baseInfo-title" style="height: auto">
                    <span><em>*</em>所在地区：</span>
                    <el-input
                            type="textarea"
                            style="background-color: #dddddd"
                            class="changeWidth"
                            :rows="2"
                            maxlength="50"
                            placeholder="请输入内容"
                            v-model="baseInfoData.address">
                    </el-input>
                    <div class="baseInfo-address">最多为50个字</div>
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
    import { validatePhone } from '@/utils/validate'
    export default {
        name: "baseInfo",
        data(){
            return{

            }
        },
        created(){

        },
        computed:{
            ...mapState('homeManage',['baseInfoData'])
        },
        methods:{
            ...mapActions('homeManage',['setSave']),
            save(){
                if(this.baseInfoData.title==null||this.baseInfoData.title==''){ this.$message.error('请输入标题');return };
                if(this.baseInfoData.secondTitle==null||this.baseInfoData.secondTitle==''){ this.$message.error('请输入企业名称');return };
                if(this.baseInfoData.mobile==null||this.baseInfoData.mobile==''){ this.$message.error('请输入联系电话');return };
                if(!validatePhone(this.baseInfoData.mobile)){this.$message.error('请输入正确的联系电话');return}
                if(this.baseInfoData.address==null||this.baseInfoData.address==''){ this.$message.error('请输入所在地区');return };
                this.setSave({vm:this})
            }
        }
    }
</script>

<style scoped lang="scss">
    .baseInfo{
        width: 620px;
        background-color: #fff;
        margin-top: 142px;
        margin-left: 30px;
        .baseInfo-header{
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
        .baseInfo-content{
            .baseInfo-title{
                height: 36px;
                line-height: 36px;
                margin-bottom: 20px;
                span{
                    display: inline-block;
                    width: 140px;
                    text-align: right;
                }
                .baseInfo-tip{
                    display: inline-block;
                    font-size: 14px;
                    color: #999999;
                }
                .baseInfo-address{
                    @extend .baseInfo-tip;
                    text-indent: 140px;
                    line-height: 18px;
                }
                em{
                    color: #ff2323;
                    margin-right: 5px;
                }
            }
            .baseInfo-input{
                width: 240px;
                height: 36px;
                background-color: #fafafa;
                border-radius: 2px;
                border: solid 1px #dddddd;
                text-indent: 16px;
            }
            .changeWidth{
                display: inline-block;
                width: 420px;
                //height: 72px;
                vertical-align: top;
            }
            .baseInfo-isShow{
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
<style lang="scss">
    .baseInfo{
        .baseInfo-content{
            .el-textarea__inner{
                background-color: #fafafa;
            }
        }
    }
</style>
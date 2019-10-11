<template>
    <el-dialog
            :visible.sync="batchStart"
            width="480px"
            class="batchStart"
            title="批量开启"
            :append-to-body='true'
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :before-close="handleClose">
        <el-form ref="form" :model="form" label-width="120px">
            <el-form-item label="设置开启时间:">
                <el-date-picker
                        v-model="form.time"
                        type="date"
                        :picker-options="pickerOptionsStart"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button @click="batchStart=false">取消</el-button>
                <el-button style="margin-left: 75px" type="primary" @click="confirm">确定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script>
    import { mapState } from 'Vuex'
    export default {
        name: "batchStart",
        props:{
            userIds:{
                type:Array,
                default:[]
            }
        },
        computed:{
            ...mapState('userManage',['batchStopTime'])
        },
        data(){
            return{
                batchStart:false,
                form:{
                    time:''
                },
                pickerOptionsStart: {
                    disabledDate: time => { //暂停使用时间大于等于此刻  开始启用时间要小于暂停使用时间
                        let endDateVal=this.batchStopTime;
                        if(endDateVal){
                            // return time.getTime() <= Date.now()- 8.64e7 || time.getTime() <= new Date(endDateVal).getTime()
                            return time.getTime() <= Date.now()- 8.64e7
                        }else {
                            return time.getTime() <= Date.now()- 8.64e7
                        }

                    }
                }
            }
        },
        methods:{
            handleClose(){
                this.batchStart=false;
            },
            toggleDialog(type){
                if(type && this.userIds.length==0)return this.$message.error('至少选择一个用户');
                this.batchStart=type;
                this.form.time='';
            },
            confirm(){
                if(this.form.time){
                    this.API.batchFreezeManage({
                        date:this.form.time,
                        state :'enable',
                        userIds:this.userIds
                    }).then(res=>{
                        if(res.success){
                            this.batchStart=false;
                            this.$message.success('操作成功');
                            this.$emit('cancelSelect');
                        }else {
                            this.$message.error(res.message);
                        }
                    })
                }else {
                    this.$message.error('请选择日期')
                }

            },
        }
    }
</script>

<style scoped lang="scss">
    .batchStart{
        .confirm{
            margin-left: 50px;
        }
    }

</style>
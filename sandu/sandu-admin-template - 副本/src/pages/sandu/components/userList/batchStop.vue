<template>
    <el-dialog
            :visible.sync="batchStop"
            width="480px"
            class="batchStop"
            title="批量暂停"
            :append-to-body='true'
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :before-close="handleClose">
        <el-form ref="form" :model="form" label-width="120px">
            <el-form-item label="设置暂停时间:">
                <el-date-picker
                        v-model="form.time"
                        type="date"
                        :picker-options="pickerOptionsStart"
                        @change="timeChange"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button @click="batchStop=false">取消</el-button>
                <el-button style="margin-left: 75px" type="primary" @click="confirm">确定</el-button>
            </el-form-item>
        </el-form>
     </el-dialog>
</template>

<script>
    export default {
        name: "batchStop",
        props:{
            userIds:{
                type:Array,
                default:[]
            }
        },
        data(){
            return{
                batchStop:false,
                form:{
                    time:''
                },
                pickerOptionsStart: {
                    disabledDate: time => { //暂停使用时间大于此刻  开始启用时间要小于暂停使用时间
                        return time.getTime() <= Date.now() - 8.64e7
                    }
                }
            }
        },
        methods:{
            handleClose(){
               this.batchStop=false;
            },
            toggleDialog(type){
                if(type && this.userIds.length==0)return this.$message.error('至少选择一个用户');
                this.batchStop=type;
                this.form.time='';
            },
            timeChange(val){

            },
            confirm(){
                if(this.form.time){
                    this.API.batchFreezeManage({
                        date:this.form.time,
                        state :'freeze',
                        userIds:this.userIds
                    }).then(res=>{
                         if(res.success){
                             this.batchStop=false;
                             this.$message.success('操作成功');
                             //this.setBatchStopTime(this.form.time);
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

</style>
<template>
    <div class="addWheelAct">
        <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/wheel' }">幸运大转盘活动</el-breadcrumb-item>
            <el-breadcrumb-item>新增</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="addWheelAct-content">
            <el-form ref="form" :model="formData" label-width="80px" :rule="rules">
                <el-form-item label="活动名称" prop="actName" required>
                    <el-input v-model="formData.actName" clearable></el-input>
                </el-form-item>
                <el-form-item label="活动时间">
                    <el-date-picker v-model="formData.beginTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                                    placeholder="请设置起始时间" align="right" :picker-options="pickerOptionsStart"></el-date-picker>
                    -- 至 --
                    <el-date-picker v-model="formData.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                                    placeholder="请设置结束时间" align="right" :picker-options="pickerOptionsEnd"></el-date-picker>
                </el-form-item>
                <el-form-item label="抽奖条件">
                    <div class="radio-item">
                        <el-radio v-model="configItem" label="1">
                            <span>每人每天默认次数</span><el-input placeholder="请输入内容" type="number" size="small" class="input" v-model="formData.lotteryNumPerDayDefalut"></el-input>
                            <span class="smaSpan">每人每天最大次数</span><el-input placeholder="请输入内容" type="number" size="small" class="input input1" v-model="formData.lotteryNumPerDayMax"></el-input>
                        </el-radio>
                    </div>
                    <div class="radio-item">
                        <el-radio v-model="configItem" label="2">
                            <span>每人默认次数</span><el-input placeholder="请输入内容" type="number" size="small" class="input"  v-model="formData.lotteryNumDefalut"></el-input>
                            <span class="smaSpan">每人最大次数</span><el-input placeholder="请输入内容" type="number" size="small" class="input"  v-model="formData.lotteryNumMax"></el-input>
                        </el-radio>
                    </div>
                </el-form-item>
                <el-form-item label="活动规则：" prop="actRule">
                    <vue-editor
                            v-model="formData.actRule"
                            class="editor">
                    </vue-editor>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="cretedFunc">立即创建</el-button>
                    <el-button @click="closeForm">取消</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import {VueEditor} from 'vue2-editor';
    export default {
        name: "addWheelAct",
        components: {
            VueEditor
        },
        data(){
            return{
                configItem:'1',
                formData:{
                    actName:'',
                    actAmount:'',
                    beginTime:'',
                    endTime:'',
                    actRule:'',
                    lotteryNumPerDayDefalut:'',
                    lotteryNumPerDayMax:'',
                    lotteryNumDefalut:'',
                    lotteryNumMax:'',
                },
                rules:{
                    actName: [
                        { required: true, message: '请输入活动名称', trigger: 'blur' },
                        { min: 1, max: 20, message: '长度在 1 到 20个字符', trigger: 'blur' }
                    ],
                    actRule: [
                        {required: true, message: '请填写活动规则', trigger: 'blur'},
                        {min: 0, max: 500, message: '长度在 500 个字符内', trigger: 'blur'}
                    ],
                },
                pickerOptionsStart:{
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                },
                pickerOptionsEnd:{
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7
                    }
                },
            }
        },
        methods:{
            cretedFunc(){
                let data={
                    actName:this.formData.actName,
                    beginTime:this.formData.beginTime,
                    endTime:this.formData.endTime,
                    actRule:this.formData.actRule,
                    configItem:this.configItem,

                }
                if(this.configItem==1){
                    data.lotteryNumPerDayDefalut=this.formData.lotteryNumPerDayDefalut,
                    data.lotteryNumPerDayMax=this.formData.lotteryNumPerDayMax
                }else {
                    data.lotteryNumDefalut=this.formData.lotteryNumDefalut,
                    data.lotteryNumMax=this.formData.lotteryNumMax
                }
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        this.API.luckywheelCreate(data).then(res=>{
                            if(res.success){
                                this.$message.success('创建成功');
                                this.$router.push('/prizeManagement?id='+res.msgId);
                            }else{
                                this.$message.error(res.message);
                            }
                        })
                    } else {
                        return false;
                    }
                });
            },
            closeForm(){
                this.$router.push('/wheel');
            }
        }
    }
</script>

<style scoped lang="scss">
  .addWheelAct{
      background-color: #fff;
      width: 100%;
      height: 100%;
      padding: 20px 30px;
      box-sizing: border-box;
      text-align: left;
      .breadcrumb{
          height: 40px;
          line-height: 40px;
      }
      .radio-item{
          .input{
              width: 200px;
              margin-left: 20px;
          }
          .smaSpan{
              margin-left: 60px;
          }
      }
  }
</style>
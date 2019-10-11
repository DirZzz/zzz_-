<template>
    <el-dialog
            :visible.sync="packagePayDialog"
            width="540px"
            class="packagePay"
            title="套餐续费"
            :append-to-body='true'
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :before-close="handleClose">
        <div class="package-title">
            用户当前套餐为"{{packageData.servicesName}}"
        </div>
            <div class="package-time">
                <span>续费时长：</span>
                <el-input v-model="paramData.renewMount" class="packageTime"></el-input>
                <el-select v-model="paramData.renewTimeUnit" placeholder="请选择">
                    <el-option
                            v-for="item in timeUnitOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
            <div class="btn">
                <el-button type="primary" size="medium" round @click="renewServiceSubmit">保存</el-button>
                <el-button size="medium" round @click="toggleDialog(false)">取消</el-button>
            </div>
    </el-dialog>
</template>

<script>
    import minixs from '@s/minixs/index'
    export default {
        name: "packagePay",
        mixins:[minixs],
        inject:['reload'],
        data(){
            return{
                packageTime: '',
                companyId:'',
                paramData: {
                    userId: null,
                    renewMount: null,//续费时长
                    renewTimeUnit: null,//时长单元(0-年;1-月;2-日)
                },
                timeUnitOptions: [
                    {
                        value: 0,
                        label: '年'
                    },
                    {
                        value: 1,
                        label: '月'
                    },
                    {
                        value: 2,
                        label: '日'
                    },
                ]
            }
        },
        methods:{
            toggleDialog(type) {
                this.setDialog({ packagePayDialog:type });
                this.paramData = {
                    userId: null,
                    renewMount: null,//续费时长
                    renewTimeUnit: null,//时长单元(0-年;1-月;2-日)
                }
                
            },
            handleClose(){
                this.setDialog({ packagePayDialog:false });
                this.paramData = {
                    userId: null,
                    renewMount: null,//续费时长
                    renewTimeUnit: null,//时长单元(0-年;1-月;2-日)
                }
                
            },
            remove(){
                this.paramData = {
                    userId: null,
                    renewMount: null,//续费时长
                    renewTimeUnit: null,//时长单元(0-年;1-月;2-日)
                }
            },
            renewServiceSubmit() {
                if(this.paramData.renewMount==null){
                    this.$message.error('请输入时长');
                    return
                }else if(this.paramData.renewTimeUnit==null){
                    this.$message.error('请选择年，月，日');
                    return
                }else {
                    console.log('this.packageData-------',this.packageData);
                    this.paramData.userId = this.packageData.id;
                    this.API.innerRenewService(this.paramData)
                        .then(res => {
                            if (res && res.success) {
                                this.setDialog({ packagePayDialog:false });
                                this.$message.success('续费成功');
                                this.remove();
                                this.reload();
                                this.$emit('refreshList');
                            }else {
                                this.$message.error(res.message);
                            }
                        })
                }

            }
        },
    }
</script>

<style scoped lang="scss">
  .packagePay{
      .package-title{
          text-align: center;
          font-size: 16px;
          color: #333;
          line-height: 40px;
      }
      .package-time{
          text-align: center;
          margin-top: 20px;
          span{
              line-height: 40px;
          }
          .packageTime{
              display: inline-block;
              width: 200px;
          }
      }
      .btn{
          text-align: center;
          margin-top: 50px;
      }
  }
</style>
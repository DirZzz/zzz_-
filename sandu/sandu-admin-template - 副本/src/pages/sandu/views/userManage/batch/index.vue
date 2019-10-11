<template>
    <div class="batch">
        <el-dialog title="批量设置密码" :visible.sync="dialogVisible " width="30%" :before-close="pwdClose">
            <div class="">
                <span>设置密码：</span>
                <el-input placeholder="请输入密码" maxlength='6' style="width:200px" v-model="inputpwd" clearable>
                </el-input>
                <el-checkbox v-model="value">随机生成密码</el-checkbox>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="pwdClose">取 消</el-button>
                <el-button type="primary" @click="pwdSure">保存并导出Excel</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: 'batch',
        data() {
            return {
                dialogVisible: true,
                value: false,
                inputpwd: null,
            }
        },
        methods: {
           
            pwdClose() {
                this.$emit('pwdClose')
            },
            pwdSure() {
                if(!this.inputpwd && !this.value){
                    this.$message.error('请输入密码！');
                }else {
                     let obj = {
                        password: this.value ? null : this.inputpwd,
                        random: this.value
                    }
                    this.$emit('pwdSure', obj)
                }
            }
        }
    }
</script>

<style scoped lang="scss">
    .batch {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
    }
</style>